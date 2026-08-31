/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.MathUtils
 *  fi.dy.masa.malilib.util.game.RayTraceUtils
 *  fi.dy.masa.malilib.util.game.RayTraceUtils$RayTraceCalculationData
 *  fi.dy.masa.malilib.util.game.RayTraceUtils$RayTraceFluidHandling
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1297
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3610
 *  net.minecraft.class_3726
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  net.minecraft.class_4538
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.schematic.verifier.SchematicVerifier;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.MathUtils;
import fi.dy.masa.malilib.util.game.RayTraceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_3726;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_4538;
import org.jetbrains.annotations.ApiStatus;

public class RayTraceUtils {
    private static final class_238 FULL_BLOCK_BOUNDS = new class_238(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    private static RayTraceWrapper closestBox;
    private static RayTraceWrapper closestCorner;
    private static RayTraceWrapper closestOrigin;
    private static double closestBoxDistance;
    private static double closestCornerDistance;
    private static double closestOriginDistance;
    private static RayTraceWrapper.HitType originType;

    @Nullable
    public static class_2338 getTargetedPosition(class_1937 world, class_1297 player, double maxDistance, boolean sneakToOffset) {
        class_239 trace = RayTraceUtils.getRayTraceFromEntity(world, player, false, maxDistance);
        if (trace.method_17783() != class_239.class_240.field_1332) {
            return null;
        }
        class_3965 traceBlock = (class_3965)trace;
        class_2338 pos = traceBlock.method_17777();
        if (sneakToOffset == player.method_5715()) {
            pos = pos.method_10093(traceBlock.method_17780());
        }
        return pos;
    }

    @Nonnull
    public static RayTraceWrapper getWrappedRayTraceFromEntity(class_1937 world, class_1297 entity, double range) {
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(range);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        class_239 result = RayTraceUtils.getRayTraceFromEntity(world, entity, false, range);
        double closestVanilla = result.method_17783() != class_239.class_240.field_1333 ? result.method_17784().method_1022(eyesPos) : -1.0;
        AreaSelection area = DataManager.getSelectionManager().getCurrentSelection();
        RayTraceWrapper wrapper = null;
        RayTraceUtils.clearTraceVars();
        if (!DataManager.getToolMode().getUsesSchematic() && area != null) {
            for (Box box : area.getAllSubRegionBoxes()) {
                boolean hitCorner = false;
                hitCorner |= RayTraceUtils.traceToSelectionBoxCorner(box, PositionUtils.Corner.CORNER_1, eyesPos, lookEndPos);
                if (hitCorner |= RayTraceUtils.traceToSelectionBoxCorner(box, PositionUtils.Corner.CORNER_2, eyesPos, lookEndPos)) continue;
                RayTraceUtils.traceToSelectionBoxBody(box, eyesPos, lookEndPos);
            }
            class_2338 origin = area.getExplicitOrigin();
            if (origin != null) {
                RayTraceUtils.traceToOrigin(origin, eyesPos, lookEndPos, RayTraceWrapper.HitType.SELECTION_ORIGIN, null);
            }
        }
        if (DataManager.getToolMode().getUsesSchematic()) {
            for (SchematicPlacement placement : DataManager.getSchematicPlacementManager().getAllSchematicsPlacements()) {
                if (!placement.isEnabled()) continue;
                RayTraceUtils.traceToPlacementBox(placement, eyesPos, lookEndPos);
                RayTraceUtils.traceToOrigin(placement.getOrigin(), eyesPos, lookEndPos, RayTraceWrapper.HitType.PLACEMENT_ORIGIN, placement);
            }
        }
        double closestDistance = closestVanilla;
        if (closestBoxDistance >= 0.0 && (closestVanilla < 0.0 || closestBoxDistance <= closestVanilla)) {
            closestDistance = closestBoxDistance;
            wrapper = closestBox;
        }
        if (closestCornerDistance >= 0.0 && (closestVanilla < 0.0 || closestCornerDistance <= closestVanilla)) {
            closestDistance = closestCornerDistance;
            wrapper = closestCorner;
        }
        if (closestOriginDistance >= 0.0 && (closestVanilla < 0.0 || closestOriginDistance <= closestVanilla)) {
            closestDistance = closestOriginDistance;
            wrapper = originType == RayTraceWrapper.HitType.PLACEMENT_ORIGIN ? closestOrigin : new RayTraceWrapper(RayTraceWrapper.HitType.SELECTION_ORIGIN);
        }
        RayTraceUtils.clearTraceVars();
        if (wrapper == null || closestDistance < 0.0) {
            wrapper = new RayTraceWrapper();
        }
        return wrapper;
    }

    private static void clearTraceVars() {
        closestBox = null;
        closestCorner = null;
        closestOrigin = null;
        closestBoxDistance = -1.0;
        closestCornerDistance = -1.0;
        closestOriginDistance = -1.0;
    }

    private static boolean traceToSelectionBoxCorner(Box box, PositionUtils.Corner corner, class_243 start, class_243 end) {
        class_238 bb;
        Optional optional;
        class_2338 pos;
        Object object = corner == PositionUtils.Corner.CORNER_1 ? box.getPos1() : (pos = corner == PositionUtils.Corner.CORNER_2 ? box.getPos2() : null);
        if (pos != null && (optional = (bb = PositionUtils.createAABBForPosition(pos)).method_992(start, end)).isPresent()) {
            double dist = ((class_243)optional.get()).method_1022(start);
            if (closestCornerDistance < 0.0 || dist < closestCornerDistance) {
                closestCornerDistance = dist;
                closestCorner = new RayTraceWrapper(box, corner, (class_243)optional.get());
            }
            return true;
        }
        return false;
    }

    private static boolean traceToSelectionBoxBody(Box box, class_243 start, class_243 end) {
        class_238 bb;
        Optional optional;
        if (box.getPos1() != null && box.getPos2() != null && (optional = (bb = PositionUtils.createEnclosingAABB(box.getPos1(), box.getPos2())).method_992(start, end)).isPresent()) {
            double dist = ((class_243)optional.get()).method_1022(start);
            if (closestBoxDistance < 0.0 || dist < closestBoxDistance) {
                closestBoxDistance = dist;
                closestBox = new RayTraceWrapper(box, PositionUtils.Corner.NONE, (class_243)optional.get());
            }
            return true;
        }
        return false;
    }

    private static boolean traceToPlacementBox(SchematicPlacement placement, class_243 start, class_243 end) {
        ImmutableMap<String, Box> boxes = placement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED);
        boolean hitSomething = false;
        for (Map.Entry entry : boxes.entrySet()) {
            class_238 bb;
            Optional optional;
            String boxName = (String)entry.getKey();
            Box box = (Box)entry.getValue();
            if (box.getPos1() == null || box.getPos2() == null || !(optional = (bb = PositionUtils.createEnclosingAABB(box.getPos1(), box.getPos2())).method_992(start, end)).isPresent()) continue;
            double dist = ((class_243)optional.get()).method_1022(start);
            if (!(closestBoxDistance < 0.0) && !(dist < closestBoxDistance)) continue;
            closestBoxDistance = dist;
            closestBox = new RayTraceWrapper(placement, (class_243)optional.get(), boxName);
            hitSomething = true;
        }
        return hitSomething;
    }

    private static boolean traceToOrigin(class_2338 pos, class_243 start, class_243 end, RayTraceWrapper.HitType type, @Nullable SchematicPlacement placement) {
        class_238 bb;
        Optional optional;
        if (pos != null && (optional = (bb = PositionUtils.createAABBForPosition(pos)).method_992(start, end)).isPresent()) {
            double dist = ((class_243)optional.get()).method_1022(start);
            if (closestOriginDistance < 0.0 || dist < closestOriginDistance) {
                closestOriginDistance = dist;
                originType = type;
                if (type == RayTraceWrapper.HitType.PLACEMENT_ORIGIN) {
                    closestOrigin = new RayTraceWrapper(placement, (class_243)optional.get(), null);
                }
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static class_3965 traceToPositions(List<class_2338> posList, class_1297 entity, double range) {
        if (posList.isEmpty()) {
            return null;
        }
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(range);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        double closest = -1.0;
        class_3965 trace = null;
        for (class_2338 pos : posList) {
            class_3965 hit;
            if (pos == null || (hit = class_238.method_1010((Iterable)ImmutableList.of((Object)FULL_BLOCK_BOUNDS), (class_243)eyesPos, (class_243)lookEndPos, (class_2338)pos)) == null) continue;
            double dist = hit.method_17784().method_1022(eyesPos);
            if (!(closest < 0.0) && !(dist < closest)) continue;
            trace = new class_3965(hit.method_17784(), hit.method_17780(), pos, false);
            closest = dist;
        }
        return trace;
    }

    @Nullable
    public static class_3965 traceToSchematicWorld(class_1297 entity, double range, boolean respectRenderRange, boolean targetFluids) {
        boolean invert = Hotkeys.INVERT_GHOST_BLOCK_RENDER_STATE.getKeybind().isKeybindHeld();
        if (respectRenderRange && (!Configs.Visuals.ENABLE_RENDERING.getBooleanValue() || Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue() == invert)) {
            return null;
        }
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world == null) {
            return null;
        }
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(range);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        class_3959.class_242 fluidMode = targetFluids ? class_3959.class_242.field_1347 : class_3959.class_242.field_1348;
        return RayTraceUtils.rayTraceBlocks(world, eyesPos, lookEndPos, fluidMode, false, true, respectRenderRange, 200);
    }

    @Nullable
    public static RayTraceWrapper getGenericTrace(class_1937 worldClient, class_1297 entity, double range) {
        return RayTraceUtils.getGenericTrace(worldClient, entity, range, true, true, false);
    }

    @Nullable
    public static RayTraceWrapper getGenericTrace(class_1937 worldClient, class_1297 entity, double range, boolean respectRenderRange, boolean targetFluids, boolean includeVerifier) {
        SchematicVerifier verifier;
        List<class_2338> posList;
        class_3965 traceMismatch;
        double dist;
        class_239 traceClient = RayTraceUtils.getRayTraceFromEntity(worldClient, entity, targetFluids, range);
        class_3965 traceSchematic = RayTraceUtils.traceToSchematicWorld(entity, range, respectRenderRange, targetFluids);
        double distClosest = -1.0;
        RayTraceWrapper.HitType type = RayTraceWrapper.HitType.MISS;
        class_243 eyesPos = entity.method_5836(1.0f);
        class_3965 trace = null;
        if (traceSchematic != null && traceSchematic.method_17783() == class_239.class_240.field_1332) {
            dist = eyesPos.method_1025(traceSchematic.method_17784());
            if (distClosest < 0.0 || dist < distClosest) {
                trace = traceSchematic;
                distClosest = eyesPos.method_1025(traceSchematic.method_17784());
                type = RayTraceWrapper.HitType.SCHEMATIC_BLOCK;
            }
        }
        if (traceClient != null && traceClient.method_17783() == class_239.class_240.field_1332) {
            dist = eyesPos.method_1025(traceClient.method_17784());
            if (distClosest < 0.0 || dist < distClosest) {
                trace = traceClient;
                type = RayTraceWrapper.HitType.VANILLA_BLOCK;
            }
        }
        SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
        if (includeVerifier && placement != null && placement.hasVerifier() && (traceMismatch = RayTraceUtils.traceToPositions(posList = (verifier = placement.getSchematicVerifier()).getSelectedMismatchBlockPositionsForRender(), entity, range)) != null) {
            trace = traceMismatch;
            type = RayTraceWrapper.HitType.MISMATCH_OVERLAY;
        }
        if (type != RayTraceWrapper.HitType.MISS) {
            return new RayTraceWrapper(type, trace);
        }
        return null;
    }

    @Nullable
    public static RayTraceWrapper getSchematicWorldTraceWrapperIfClosest(class_1937 worldClient, class_1297 entity, double range) {
        RayTraceWrapper trace = RayTraceUtils.getGenericTrace(worldClient, entity, range);
        if (trace != null && trace.getHitType() == RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            return trace;
        }
        return null;
    }

    @Nullable
    public static class_2338 getSchematicWorldTraceIfClosest(class_1937 worldClient, class_1297 entity, double range) {
        RayTraceWrapper trace = RayTraceUtils.getSchematicWorldTraceWrapperIfClosest(worldClient, entity, range);
        return trace != null && trace.getHitType() == RayTraceWrapper.HitType.SCHEMATIC_BLOCK ? trace.getBlockHitResult().method_17777() : null;
    }

    @Nullable
    public static class_2338 getFurthestSchematicWorldBlockBeforeVanilla(class_1937 worldClient, class_1297 entity, double maxRange, boolean requireVanillaBlockBehind) {
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(maxRange);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        class_2338 closestVanillaPos = null;
        class_2350 side = null;
        double closestVanilla = -1.0;
        class_239 traceVanilla = RayTraceUtils.getRayTraceFromEntity(worldClient, entity, false, maxRange);
        if (traceVanilla.method_17783() == class_239.class_240.field_1332) {
            closestVanilla = traceVanilla.method_17784().method_1025(eyesPos);
            class_3965 vanillaHitResult = (class_3965)traceVanilla;
            side = vanillaHitResult.method_17780();
            closestVanillaPos = vanillaHitResult.method_17777();
        } else if (requireVanillaBlockBehind) {
            return null;
        }
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        List<class_3965> list = RayTraceUtils.rayTraceBlocksToList(worldSchematic, eyesPos, lookEndPos, class_3959.class_242.field_1348, false, false, true, 200);
        class_3965 furthestTrace = null;
        double furthestDist = -1.0;
        if (!list.isEmpty()) {
            for (class_3965 trace : list) {
                double dist = trace.method_17784().method_1025(eyesPos);
                if ((furthestDist < 0.0 || dist > furthestDist) && (dist < closestVanilla || closestVanilla < 0.0) && !trace.method_17777().equals((Object)closestVanillaPos)) {
                    furthestDist = dist;
                    furthestTrace = trace;
                }
                if (!(closestVanilla >= 0.0) || !(dist > closestVanilla)) continue;
                break;
            }
        }
        if (furthestTrace == null && side != null && closestVanillaPos != null) {
            class_2338 pos = closestVanillaPos.method_10093(side);
            LayerRange layerRange = DataManager.getRenderLayerRange();
            if (layerRange.isPositionWithinRange(pos) && !worldSchematic.method_8320(pos).method_26215() && worldClient.method_8320(pos).method_26215()) {
                return pos;
            }
        }
        return furthestTrace != null ? furthestTrace.method_17777() : null;
    }

    @Nullable
    public static RayTraceWrapper getFurthestSchematicWorldTraceBeforeVanilla(class_1937 worldClient, class_1297 entity, double maxRange) {
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(maxRange);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        class_2338 closestVanillaPos = null;
        double closestVanilla = -1.0;
        class_239 traceVanilla = RayTraceUtils.getRayTraceFromEntity(worldClient, entity, false, maxRange);
        if (traceVanilla.method_17783() == class_239.class_240.field_1332) {
            closestVanilla = traceVanilla.method_17784().method_1025(eyesPos);
            class_3965 vanillaHitResult = (class_3965)traceVanilla;
            closestVanillaPos = vanillaHitResult.method_17777();
        }
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        List<class_3965> list = RayTraceUtils.rayTraceBlocksToList(worldSchematic, eyesPos, lookEndPos, class_3959.class_242.field_1348, false, false, true, 200);
        class_3965 furthestTrace = null;
        double furthestDist = -1.0;
        if (!list.isEmpty()) {
            for (class_3965 trace : list) {
                double dist = trace.method_17784().method_1025(eyesPos);
                if ((furthestDist < 0.0 || dist > furthestDist) && (dist < closestVanilla || closestVanilla < 0.0) && !trace.method_17777().equals((Object)closestVanillaPos)) {
                    furthestDist = dist;
                    furthestTrace = trace;
                }
                if (!(closestVanilla >= 0.0) || !(dist > closestVanilla)) continue;
                break;
            }
        }
        return furthestTrace != null ? new RayTraceWrapper(RayTraceWrapper.HitType.SCHEMATIC_BLOCK, furthestTrace) : null;
    }

    @Nonnull
    public static class_239 getRayTraceFromEntity(class_1937 worldIn, class_1297 entityIn, boolean useLiquids) {
        double reach = class_310.method_1551().field_1724 != null ? class_310.method_1551().field_1724.method_55755() + 0.5 : 5.0;
        return RayTraceUtils.getRayTraceFromEntity(worldIn, entityIn, useLiquids, reach);
    }

    @Nonnull
    public static class_239 getRayTraceFromEntity(class_1937 world, class_1297 entity, boolean useLiquids, double range) {
        class_243 eyesPos = entity.method_5836(1.0f);
        class_243 rangedLookRot = entity.method_5828(1.0f).method_1021(range);
        class_243 lookEndPos = eyesPos.method_1019(rangedLookRot);
        class_3959.class_242 fluidMode = useLiquids ? class_3959.class_242.field_1347 : class_3959.class_242.field_1348;
        class_3965 result = RayTraceUtils.rayTraceBlocks(world, eyesPos, lookEndPos, fluidMode, false, false, false, 1000);
        class_238 bb = entity.method_5829().method_1009(rangedLookRot.field_1352, rangedLookRot.field_1351, rangedLookRot.field_1350).method_1009(1.0, 1.0, 1.0);
        List list = world.method_8335(entity, bb);
        double closest = result != null && result.method_17783() == class_239.class_240.field_1332 ? eyesPos.method_1022(result.method_17784()) : Double.MAX_VALUE;
        class_1297 targetEntity = null;
        Optional optional = Optional.empty();
        for (int i = 0; i < list.size(); ++i) {
            double distance;
            class_1297 entityTmp = (class_1297)list.get(i);
            Optional optionalTmp = entityTmp.method_5829().method_992(eyesPos, lookEndPos);
            if (!optionalTmp.isPresent() || !((distance = eyesPos.method_1022((class_243)optionalTmp.get())) <= closest)) continue;
            targetEntity = entityTmp;
            optional = optionalTmp;
            closest = distance;
        }
        if (targetEntity != null) {
            result = new class_3966(targetEntity, (class_243)optional.get());
        }
        if (result == null || eyesPos.method_1022(result.method_17784()) > range) {
            result = class_3965.method_17778((class_243)class_243.field_1353, (class_2350)class_2350.field_11036, (class_2338)class_2338.field_10980);
        }
        return result;
    }

    @Nullable
    public static class_3965 rayTraceBlocks(class_1937 world, class_243 start, class_243 end, class_3959.class_242 fluidMode, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, boolean respectLayerRange, int maxSteps) {
        class_3610 fluidState;
        class_2680 blockState;
        if (Double.isNaN(start.field_1352) || Double.isNaN(start.field_1351) || Double.isNaN(start.field_1350) || Double.isNaN(end.field_1352) || Double.isNaN(end.field_1351) || Double.isNaN(end.field_1350)) {
            return null;
        }
        LayerRange range = DataManager.getRenderLayerRange();
        RayTraceCalcsData data = new RayTraceCalcsData(start, end, range, fluidMode);
        class_3965 trace = RayTraceUtils.traceFirstStep(data, world, blockState = world.method_8320(data.blockPos), fluidState = world.method_8316(data.blockPos), ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, respectLayerRange);
        if (trace != null) {
            return trace;
        }
        while (--maxSteps >= 0) {
            if (RayTraceUtils.rayTraceCalcs(data, returnLastUncollidableBlock, respectLayerRange)) {
                return data.trace;
            }
            blockState = world.method_8320(data.blockPos);
            if (!RayTraceUtils.traceLoopSteps(data, world, blockState, fluidState = world.method_8316(data.blockPos), ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, respectLayerRange)) continue;
            return data.trace;
        }
        return returnLastUncollidableBlock ? data.trace : null;
    }

    @Nullable
    private static class_3965 traceFirstStep(RayTraceCalcsData data, class_1937 world, class_2680 blockState, class_3610 fluidState, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, boolean respectLayerRange) {
        if (!(respectLayerRange && !data.range.isPositionWithinRange(data.x, data.y, data.z) || ignoreBlockWithoutBoundingBox && blockState.method_26220((class_1922)world, data.blockPos).method_1110())) {
            class_265 blockShape = blockState.method_26172((class_1922)world, data.blockPos, class_3726.method_16195((class_1297)class_310.method_1551().field_1724));
            boolean blockCollidable = !blockShape.method_1110();
            boolean fluidCollidable = data.fluidMode.method_17751(fluidState);
            if (blockCollidable || fluidCollidable) {
                class_3965 trace = null;
                if (blockCollidable) {
                    trace = blockShape.method_1092(data.start, data.end, data.blockPos);
                }
                if (trace == null && fluidCollidable) {
                    trace = fluidState.method_17776((class_1922)world, data.blockPos).method_1092(data.start, data.end, data.blockPos);
                }
                if (trace != null) {
                    return trace;
                }
            }
        }
        return null;
    }

    private static boolean traceLoopSteps(RayTraceCalcsData data, class_1937 world, class_2680 blockState, class_3610 fluidState, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, boolean respectLayerRange) {
        if (!(respectLayerRange && !data.range.isPositionWithinRange(data.x, data.y, data.z) || ignoreBlockWithoutBoundingBox && blockState.method_26204() != class_2246.field_10316 && blockState.method_26204() != class_2246.field_10027 && blockState.method_26204() != class_2246.field_10613 && blockState.method_26220((class_1922)world, data.blockPos).method_1110())) {
            class_265 blockShape = blockState.method_26172((class_1922)world, data.blockPos, class_3726.method_16195((class_1297)class_310.method_1551().field_1724));
            boolean blockCollidable = !blockShape.method_1110();
            boolean fluidCollidable = data.fluidMode.method_17751(fluidState);
            if (!blockCollidable && !fluidCollidable) {
                class_243 pos = new class_243(data.currentX, data.currentY, data.currentZ);
                data.trace = class_3965.method_17778((class_243)pos, (class_2350)data.facing, (class_2338)data.blockPos);
            } else {
                class_3965 traceTmp = null;
                if (blockCollidable) {
                    traceTmp = blockShape.method_1092(data.start, data.end, data.blockPos);
                }
                if (traceTmp == null && fluidCollidable) {
                    traceTmp = fluidState.method_17776((class_1922)world, data.blockPos).method_1092(data.start, data.end, data.blockPos);
                }
                if (traceTmp != null) {
                    data.trace = traceTmp;
                    return true;
                }
            }
        }
        return false;
    }

    public static List<class_3965> rayTraceBlocksToList(class_1937 world, class_243 start, class_243 end, class_3959.class_242 fluidMode, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock, boolean respectLayerRange, int maxSteps) {
        if (Double.isNaN(start.field_1352) || Double.isNaN(start.field_1351) || Double.isNaN(start.field_1350) || Double.isNaN(end.field_1352) || Double.isNaN(end.field_1351) || Double.isNaN(end.field_1350)) {
            return ImmutableList.of();
        }
        LayerRange range = DataManager.getRenderLayerRange();
        RayTraceCalcsData data = new RayTraceCalcsData(start, end, range, fluidMode);
        class_2680 blockState = world.method_8320(data.blockPos);
        class_3610 fluidState = world.method_8316(data.blockPos);
        class_3965 trace = RayTraceUtils.traceFirstStep(data, world, blockState, fluidState, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, respectLayerRange);
        ArrayList<class_3965> hits = new ArrayList<class_3965>();
        if (trace != null) {
            hits.add(trace);
        }
        while (--maxSteps >= 0) {
            if (RayTraceUtils.rayTraceCalcs(data, returnLastUncollidableBlock, respectLayerRange)) {
                if (data.trace != null) {
                    hits.add(data.trace);
                }
                return hits;
            }
            blockState = world.method_8320(data.blockPos);
            if (!RayTraceUtils.traceLoopSteps(data, world, blockState, fluidState = world.method_8316(data.blockPos), ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock, respectLayerRange)) continue;
            hits.add(data.trace);
        }
        return hits;
    }

    private static boolean rayTraceCalcs(RayTraceCalcsData data, boolean returnLastNonCollidableBlock, boolean respectLayerRange) {
        boolean xDiffers = true;
        boolean yDiffers = true;
        boolean zDiffers = true;
        double nextX = 999.0;
        double nextY = 999.0;
        double nextZ = 999.0;
        if (Double.isNaN(data.currentX) || Double.isNaN(data.currentY) || Double.isNaN(data.currentZ)) {
            data.trace = null;
            return true;
        }
        if (data.x == data.xEnd && data.y == data.yEnd && data.z == data.zEnd) {
            if (!returnLastNonCollidableBlock) {
                data.trace = null;
            }
            return true;
        }
        if (data.xEnd > data.x) {
            nextX = (double)data.x + 1.0;
        } else if (data.xEnd < data.x) {
            nextX = (double)data.x + 0.0;
        } else {
            xDiffers = false;
        }
        if (data.yEnd > data.y) {
            nextY = (double)data.y + 1.0;
        } else if (data.yEnd < data.y) {
            nextY = (double)data.y + 0.0;
        } else {
            yDiffers = false;
        }
        if (data.zEnd > data.z) {
            nextZ = (double)data.z + 1.0;
        } else if (data.zEnd < data.z) {
            nextZ = (double)data.z + 0.0;
        } else {
            zDiffers = false;
        }
        double relStepX = 999.0;
        double relStepY = 999.0;
        double relStepZ = 999.0;
        double distToEndX = data.end.field_1352 - data.currentX;
        double distToEndY = data.end.field_1351 - data.currentY;
        double distToEndZ = data.end.field_1350 - data.currentZ;
        if (xDiffers) {
            relStepX = (nextX - data.currentX) / distToEndX;
        }
        if (yDiffers) {
            relStepY = (nextY - data.currentY) / distToEndY;
        }
        if (zDiffers) {
            relStepZ = (nextZ - data.currentZ) / distToEndZ;
        }
        if (relStepX == -0.0) {
            relStepX = -1.0E-4;
        }
        if (relStepY == -0.0) {
            relStepY = -1.0E-4;
        }
        if (relStepZ == -0.0) {
            relStepZ = -1.0E-4;
        }
        if (relStepX < relStepY && relStepX < relStepZ) {
            data.facing = data.xEnd > data.x ? class_2350.field_11039 : class_2350.field_11034;
            data.currentX = nextX;
            data.currentY += distToEndY * relStepX;
            data.currentZ += distToEndZ * relStepX;
        } else if (relStepY < relStepZ) {
            data.facing = data.yEnd > data.y ? class_2350.field_11033 : class_2350.field_11036;
            data.currentX += distToEndX * relStepY;
            data.currentY = nextY;
            data.currentZ += distToEndZ * relStepY;
        } else {
            data.facing = data.zEnd > data.z ? class_2350.field_11043 : class_2350.field_11035;
            data.currentX += distToEndX * relStepZ;
            data.currentY += distToEndY * relStepZ;
            data.currentZ = nextZ;
        }
        data.x = class_3532.method_15357((double)data.currentX) - (data.facing == class_2350.field_11034 ? 1 : 0);
        data.y = class_3532.method_15357((double)data.currentY) - (data.facing == class_2350.field_11036 ? 1 : 0);
        data.z = class_3532.method_15357((double)data.currentZ) - (data.facing == class_2350.field_11035 ? 1 : 0);
        data.blockPos = new class_2338(data.x, data.y, data.z);
        return false;
    }

    @Nullable
    @ApiStatus.Experimental
    public static class_2338 getPickBlockLastTrace(class_1937 worldClient, class_1297 entity, double maxRange, boolean adjacentOnly) {
        class_2338 pos;
        class_243 eyesPos = entity.method_33571();
        class_243 look = MathUtils.scale((class_243)MathUtils.getRotationVector((float)entity.method_36454(), (float)entity.method_36455()), (double)maxRange);
        class_243 lookEndPos = eyesPos.method_1019(look);
        class_239 traceVanilla = RayTraceUtils.getRayTraceFromEntity(worldClient, entity, false, maxRange);
        if (traceVanilla.method_17783() != class_239.class_240.field_1332) {
            return null;
        }
        class_3966 entityTrace = (class_3966)traceVanilla;
        double closestVanilla = RayTraceUtils.squareDistanceTo(entityTrace.method_17784(), eyesPos);
        class_2338 closestVanillaPos = entityTrace.method_17782().method_24515();
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        List<class_3965> list = RayTraceUtils.rayTraceSchematicWorldBlocksToList(worldSchematic, eyesPos, lookEndPos, 24);
        class_3965 furthestTrace = null;
        double furthestDist = -1.0;
        boolean vanillaPosReplaceable = worldClient.method_8320(closestVanillaPos).method_26184((class_4538)worldClient, closestVanillaPos);
        if (!list.isEmpty()) {
            for (class_3965 trace : list) {
                double dist = RayTraceUtils.squareDistanceTo(trace.method_17784(), eyesPos);
                class_2338 pos2 = trace.method_17777();
                if ((furthestDist < 0.0 || dist >= furthestDist) && (closestVanilla < 0.0 || dist < closestVanilla || pos2.equals((Object)closestVanillaPos) && vanillaPosReplaceable) && (vanillaPosReplaceable || !pos2.equals((Object)closestVanillaPos))) {
                    furthestDist = dist;
                    furthestTrace = trace;
                }
                if (!(closestVanilla >= 0.0) || !(dist > closestVanilla)) continue;
                break;
            }
        }
        if (furthestTrace == null) {
            pos = closestVanillaPos.method_10093(entityTrace.method_17782().method_58149());
            LayerRange layerRange = DataManager.getRenderLayerRange();
            if (layerRange.isPositionWithinRange(pos) && worldSchematic.method_8320(pos) != class_2246.field_10124.method_9564() && worldClient.method_8320(pos) == class_2246.field_10124.method_9564()) {
                return pos;
            }
        }
        if (furthestTrace != null) {
            pos = furthestTrace.method_17777();
            if (adjacentOnly) {
                class_2338 placementPos;
                class_2338 class_23382 = placementPos = vanillaPosReplaceable ? closestVanillaPos : closestVanillaPos.method_10093(entityTrace.method_17782().method_58149());
                if (!pos.equals((Object)placementPos)) {
                    return null;
                }
            }
            return pos;
        }
        return null;
    }

    @ApiStatus.Experimental
    public static List<class_3965> rayTraceSchematicWorldBlocksToList(class_1937 world, class_243 start, class_243 end, int maxSteps) {
        if (Double.isNaN(start.field_1352) || Double.isNaN(start.field_1351) || Double.isNaN(start.field_1350) || Double.isNaN(end.field_1352) || Double.isNaN(end.field_1351) || Double.isNaN(end.field_1350)) {
            return ImmutableList.of();
        }
        RayTraceUtils.RayTraceCalculationData data = new RayTraceUtils.RayTraceCalculationData(start, end, RayTraceUtils.RayTraceFluidHandling.SOURCE_ONLY, fi.dy.masa.malilib.util.game.RayTraceUtils.BLOCK_FILTER_NON_AIR, DataManager.getRenderLayerRange());
        ArrayList<class_3965> hits = new ArrayList<class_3965>();
        while (--maxSteps >= 0) {
            if (fi.dy.masa.malilib.util.game.RayTraceUtils.checkRayCollision((RayTraceUtils.RayTraceCalculationData)data, (class_1937)world, (boolean)false)) {
                hits.add((class_3965)data.trace);
            }
            if (!fi.dy.masa.malilib.util.game.RayTraceUtils.rayTraceAdvance((RayTraceUtils.RayTraceCalculationData)data)) continue;
            break;
        }
        return hits;
    }

    public static double squareDistanceTo(class_243 i, class_243 v) {
        return RayTraceUtils.squareDistanceTo(i, v.field_1352, v.field_1351, v.field_1350);
    }

    public static double squareDistanceTo(class_243 v, double x, double y, double z) {
        return v.field_1352 * x + v.field_1351 * y + v.field_1350 * z;
    }

    public static class RayTraceWrapper {
        private final HitType type;
        private PositionUtils.Corner corner = PositionUtils.Corner.NONE;
        private class_243 hitVec = class_243.field_1353;
        @Nullable
        private class_3965 traceBlock = null;
        @Nullable
        private class_3966 traceEntity = null;
        @Nullable
        private Box box = null;
        @Nullable
        private SchematicPlacement schematicPlacement = null;
        @Nullable
        private String placementRegionName = null;

        public RayTraceWrapper() {
            this.type = HitType.MISS;
        }

        public RayTraceWrapper(HitType type) {
            this.type = type;
        }

        public RayTraceWrapper(HitType type, class_3965 trace) {
            this.type = type;
            this.hitVec = trace.method_17784();
            this.traceBlock = trace;
        }

        public RayTraceWrapper(HitType type, class_3966 trace) {
            this.type = type;
            this.hitVec = trace.method_17784();
            this.traceEntity = trace;
        }

        public RayTraceWrapper(Box box, PositionUtils.Corner corner, class_243 hitVec) {
            this.type = corner == PositionUtils.Corner.NONE ? HitType.SELECTION_BOX_BODY : HitType.SELECTION_BOX_CORNER;
            this.corner = corner;
            this.hitVec = hitVec;
            this.box = box;
        }

        public RayTraceWrapper(SchematicPlacement placement, class_243 hitVec, @Nullable String regionName) {
            this.type = regionName != null ? HitType.PLACEMENT_SUBREGION : HitType.PLACEMENT_ORIGIN;
            this.hitVec = hitVec;
            this.schematicPlacement = placement;
            this.placementRegionName = regionName;
        }

        public HitType getHitType() {
            return this.type;
        }

        @Nullable
        public class_3965 getBlockHitResult() {
            return this.traceBlock;
        }

        @Nullable
        public class_3966 getEntityHitResult() {
            return this.traceEntity;
        }

        @Nullable
        public Box getHitSelectionBox() {
            return this.box;
        }

        @Nullable
        public SchematicPlacement getHitSchematicPlacement() {
            return this.schematicPlacement;
        }

        @Nullable
        public String getHitSchematicPlacementRegionName() {
            return this.placementRegionName;
        }

        public class_243 getHitVec() {
            return this.hitVec;
        }

        public PositionUtils.Corner getHitCorner() {
            return this.corner;
        }

        public static enum HitType {
            MISS,
            VANILLA_BLOCK,
            VANILLA_ENTITY,
            SELECTION_BOX_BODY,
            SELECTION_BOX_CORNER,
            SELECTION_ORIGIN,
            PLACEMENT_SUBREGION,
            PLACEMENT_ORIGIN,
            SCHEMATIC_BLOCK,
            SCHEMATIC_ENTITY,
            MISMATCH_OVERLAY;

        }
    }

    public static class RayTraceCalcsData {
        public final LayerRange range;
        public final class_3959.class_242 fluidMode;
        public final class_243 start;
        public final class_243 end;
        public final int xEnd;
        public final int yEnd;
        public final int zEnd;
        public int x;
        public int y;
        public int z;
        public double currentX;
        public double currentY;
        public double currentZ;
        public class_2338 blockPos;
        public class_2350 facing;
        public class_3965 trace;

        public RayTraceCalcsData(class_243 start, class_243 end, LayerRange range, class_3959.class_242 fluidMode) {
            this.start = start;
            this.end = end;
            this.range = range;
            this.fluidMode = fluidMode;
            this.currentX = start.field_1352;
            this.currentY = start.field_1351;
            this.currentZ = start.field_1350;
            this.xEnd = class_3532.method_15357((double)end.field_1352);
            this.yEnd = class_3532.method_15357((double)end.field_1351);
            this.zEnd = class_3532.method_15357((double)end.field_1350);
            this.x = class_3532.method_15357((double)start.field_1352);
            this.y = class_3532.method_15357((double)start.field_1351);
            this.z = class_3532.method_15357((double)start.field_1350);
            this.blockPos = new class_2338(this.x, this.y, this.z);
            this.trace = null;
        }
    }
}

