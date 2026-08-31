/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.registry.Registry
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  fi.dy.masa.malilib.util.game.PlacementUtils
 *  fi.dy.masa.malilib.util.game.RayTraceUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1269$class_9861
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1750
 *  net.minecraft.class_1799
 *  net.minecraft.class_1838
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2286
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2462
 *  net.minecraft.class_2482
 *  net.minecraft.class_2510
 *  net.minecraft.class_2533
 *  net.minecraft.class_2680
 *  net.minecraft.class_2747
 *  net.minecraft.class_2760
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_310
 *  net.minecraft.class_3481
 *  net.minecraft.class_3675$class_306
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3965
 *  net.minecraft.class_4538
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.mixin.input.IMixinKeyBinding;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.util.EasyPlaceProtocol;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PickBlockUtils;
import fi.dy.masa.litematica.util.PlacementHandler;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.game.BlockUtils;
import fi.dy.masa.malilib.util.game.PlacementUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1838;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2286;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2462;
import net.minecraft.class_2482;
import net.minecraft.class_2510;
import net.minecraft.class_2533;
import net.minecraft.class_2680;
import net.minecraft.class_2747;
import net.minecraft.class_2760;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_310;
import net.minecraft.class_3481;
import net.minecraft.class_3675;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4538;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class EasyPlaceUtils {
    private static final List<PositionCache> EASY_PLACE_POSITIONS = new ArrayList<PositionCache>();
    private static final HashMap<class_2248, Boolean> HAS_USE_ACTION_CACHE = new HashMap();
    private static boolean isHandling;
    private static boolean isFirstClickEasyPlace;
    private static boolean isFirstClickPlacementRestriction;
    private static long easyPlaceLastPickBlockTime;

    public static boolean isHandling() {
        return isHandling;
    }

    public static void setHandling(boolean handling) {
        isHandling = handling;
    }

    public static void setIsFirstClick() {
        if (EasyPlaceUtils.shouldDoEasyPlaceActions()) {
            isFirstClickEasyPlace = true;
        }
        if (Configs.Generic.PLACEMENT_RESTRICTION.getBooleanValue()) {
            isFirstClickPlacementRestriction = true;
        }
    }

    private static boolean hasUseAction(class_2248 block) {
        Boolean val = HAS_USE_ACTION_CACHE.get(block);
        if (val == null) {
            try {
                String name = class_2248.class.getSimpleName().equals("Block") ? "onUse" : "a";
                Method method = block.getClass().getMethod(name, class_2680.class, class_1937.class, class_2338.class, class_1657.class, class_3965.class);
                Method baseMethod = class_2248.class.getMethod(name, class_2680.class, class_1937.class, class_2338.class, class_1657.class, class_3965.class);
                val = !method.equals(baseMethod);
            }
            catch (Exception e) {
                Litematica.LOGGER.warn("EasyPlaceUtils: Failed to reflect method Block::onUse", (Throwable)e);
                val = false;
            }
            HAS_USE_ACTION_CACHE.put(block, val);
        }
        return val;
    }

    public static boolean shouldDoEasyPlaceActions() {
        return false;
    }

    public static void easyPlaceOnUseTick() {
        class_3675.class_306 useKey = ((IMixinKeyBinding)class_310.method_1551().field_1690.field_1904).litematica_getBoundKey();
        if (!isHandling && Configs.Generic.EASY_PLACE_HOLD_ENABLED.getBooleanValue() && EasyPlaceUtils.shouldDoEasyPlaceActions() && Hotkeys.EASY_PLACE_ACTIVATION.getKeybind().isKeybindHeld()) {
            isHandling = true;
            EasyPlaceUtils.handleEasyPlace();
            isHandling = false;
        }
    }

    public static boolean handleEasyPlaceWithMessage() {
        if (EasyPlaceUtils.isHandling()) {
            return false;
        }
        isHandling = true;
        class_1269 result = EasyPlaceUtils.handleEasyPlace();
        isHandling = false;
        if (isFirstClickEasyPlace && result == class_1269.field_5814) {
            InfoUtils.printActionbarMessage((String)"litematica.message.easy_place_fail", (Object[])new Object[0]);
        }
        isFirstClickEasyPlace = false;
        return result != class_1269.field_5811;
    }

    public static void onRightClickTail() {
        if (isFirstClickEasyPlace) {
            EasyPlaceUtils.handleEasyPlaceWithMessage();
        }
    }

    @Nullable
    private static class_3965 getTargetPosition(@Nullable RayTraceUtils.RayTraceWrapper traceWrapper) {
        class_310 mc = class_310.method_1551();
        class_2338 overriddenPos = Registry.BLOCK_PLACEMENT_POSITION_HANDLER.getCurrentPlacementPosition();
        if (overriddenPos != null) {
            class_2350 side;
            class_243 hitPos;
            if (mc.field_1724 == null) {
                return null;
            }
            double reach = mc.field_1724.method_55754();
            class_1297 entity = mc.method_1560();
            class_3965 trace = RayTraceUtils.traceToPositions(Collections.singletonList(overriddenPos), entity, reach);
            class_2338 pos = overriddenPos;
            if (trace != null && trace.method_17783() == class_239.class_240.field_1332) {
                hitPos = trace.method_17784();
                side = trace.method_17780();
            } else {
                hitPos = new class_243((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 1.0, (double)pos.method_10260() + 0.5);
                side = class_2350.field_11036;
            }
            return new class_3965(hitPos, side, pos, false);
        }
        if (traceWrapper != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            return traceWrapper.getBlockHitResult();
        }
        return null;
    }

    @Nullable
    private static class_3965 getAdjacentClickPosition(class_2338 targetPos) {
        class_3965 blockHitResult;
        class_2338 posVanilla;
        class_310 mc = class_310.method_1551();
        class_638 world = mc.field_1687;
        if (mc.field_1724 == null) {
            return null;
        }
        double reach = mc.field_1724.method_55754();
        class_1297 entity = mc.method_1560();
        if (entity == null || world == null) {
            return null;
        }
        class_239 traceVanilla = fi.dy.masa.malilib.util.game.RayTraceUtils.getRayTraceFromEntity((class_1937)world, (class_1297)entity, (class_3959.class_242)class_3959.class_242.field_1348, (boolean)false, (double)reach);
        if (traceVanilla.method_17783() == class_239.class_240.field_1332 && !PlacementUtils.isReplaceable((class_1937)world, (class_2338)(posVanilla = (blockHitResult = (class_3965)traceVanilla).method_17777()), (boolean)false) && targetPos.equals((Object)posVanilla.method_10093(blockHitResult.method_17780()))) {
            return new class_3965(blockHitResult.method_17784(), ((class_3965)traceVanilla).method_17780(), posVanilla, false);
        }
        for (class_2350 side : class_2350.values()) {
            class_2338 posSide = targetPos.method_10093(side);
            if (PlacementUtils.isReplaceable((class_1937)world, (class_2338)posSide, (boolean)false)) continue;
            class_243 hitPos = EasyPlaceUtils.getHitPositionForSidePosition(posSide, side);
            return new class_3965(hitPos, side.method_10153(), posSide, false);
        }
        return null;
    }

    private static class_243 getHitPositionForSidePosition(class_2338 posSide, class_2350 sideFromTarget) {
        class_2350.class_2351 axis = sideFromTarget.method_10166();
        double x = (double)posSide.method_10263() + 0.5 - (double)sideFromTarget.method_10148() * 0.5;
        double y = (double)posSide.method_10264() + (axis == class_2350.class_2351.field_11052 ? (sideFromTarget == class_2350.field_11033 ? 1.0 : 0.0) : 0.0);
        double z = (double)posSide.method_10260() + 0.5 - (double)sideFromTarget.method_10165() * 0.5;
        return new class_243(x, y, z);
    }

    @Nullable
    private static class_3965 getClickPosition(class_3965 targetPosition, class_2680 stateSchematic, class_2680 stateClient) {
        boolean isSlab = stateSchematic.method_26204() instanceof class_2482;
        if (isSlab) {
            return EasyPlaceUtils.getClickPositionForSlab(targetPosition, stateSchematic, stateClient);
        }
        class_2338 targetBlockPos = targetPosition.method_17777();
        boolean requireAdjacent = false;
        return requireAdjacent ? EasyPlaceUtils.getAdjacentClickPosition(targetBlockPos) : targetPosition;
    }

    @Nullable
    private static class_3965 getClickPositionForSlab(class_3965 targetPosition, class_2680 stateSchematic, class_2680 stateClient) {
        class_310 mc = class_310.method_1551();
        class_2482 slab = (class_2482)stateSchematic.method_26204();
        class_2338 targetBlockPos = targetPosition.method_17777();
        class_638 worldClient = mc.field_1687;
        boolean isDouble = ((class_2771)stateSchematic.method_11654((class_2769)class_2482.field_11501)).equals((Object)class_2771.field_12682);
        if (isDouble) {
            if (EasyPlaceUtils.clientBlockIsSameMaterialSingleSlab(stateSchematic, stateClient)) {
                boolean isTop = stateClient.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12679;
                class_2350 side = isTop ? class_2350.field_11033 : class_2350.field_11036;
                class_243 hitPos = targetPosition.method_17784();
                return new class_3965(new class_243(hitPos.field_1352, (double)targetBlockPos.method_10264() + 0.5, hitPos.field_1350), side, targetBlockPos, false);
            }
            if (PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)targetBlockPos, (boolean)true)) {
                class_3965 pos = EasyPlaceUtils.getClickPositionForSlabHalf(targetPosition, stateSchematic, false, (class_1937)worldClient);
                return pos != null ? pos : EasyPlaceUtils.getClickPositionForSlabHalf(targetPosition, stateSchematic, true, (class_1937)worldClient);
            }
        } else if (!isDouble && PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)targetBlockPos, (boolean)true)) {
            boolean isTop = stateSchematic.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12679;
            return EasyPlaceUtils.getClickPositionForSlabHalf(targetPosition, stateSchematic, isTop, (class_1937)worldClient);
        }
        return null;
    }

    @Nullable
    private static class_3965 getClickPositionForSlabHalf(class_3965 targetPosition, class_2680 stateSchematic, boolean isTop, class_1937 worldClient) {
        class_2338 targetBlockPos = targetPosition.method_17777();
        boolean requireAdjacent = false;
        if (!requireAdjacent) {
            class_2350 clickSide = isTop ? class_2350.field_11033 : class_2350.field_11036;
            boolean isReplaceable = PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)targetBlockPos, (boolean)false);
            if (isReplaceable) {
                class_2338 posOffset = targetBlockPos.method_10093(clickSide);
                class_2680 stateSide = worldClient.method_8320(posOffset);
                if (!EasyPlaceUtils.clientBlockIsSameMaterialSingleSlab(stateSchematic, stateSide)) {
                    class_243 hitPos = targetPosition.method_17784();
                    return new class_3965(new class_243(hitPos.field_1352, (double)targetBlockPos.method_10264() + 0.5, hitPos.field_1350), clickSide, targetBlockPos, false);
                }
            } else if (worldClient.method_8320(targetBlockPos).method_51176() && EasyPlaceUtils.canClickOnAdjacentBlockToPlaceSingleSlabAt(targetBlockPos, stateSchematic, clickSide.method_10153(), worldClient)) {
                class_2338 pos = targetBlockPos.method_10093(clickSide.method_10153());
                class_243 hitPos = new class_243((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 0.5, (double)pos.method_10260() + 0.5);
                return new class_3965(hitPos, clickSide, pos, false);
            }
        }
        return EasyPlaceUtils.getAdjacentClickPositionForSlab(targetBlockPos, stateSchematic, isTop, worldClient);
    }

    @Nullable
    private static class_3965 getAdjacentClickPositionForSlab(class_2338 targetBlockPos, class_2680 stateSchematic, boolean isTop, class_1937 worldClient) {
        class_2350 clickSide = isTop ? class_2350.field_11033 : class_2350.field_11036;
        class_2350 clickSideOpposite = clickSide.method_10153();
        class_2338 posSide = targetBlockPos.method_10093(clickSideOpposite);
        if (EasyPlaceUtils.canClickOnAdjacentBlockToPlaceSingleSlabAt(targetBlockPos, stateSchematic, clickSideOpposite, worldClient)) {
            return new class_3965(EasyPlaceUtils.getHitPositionForSidePosition(posSide, clickSideOpposite), clickSide, posSide, false);
        }
        for (class_2350 side : class_2350.values()) {
            if (!EasyPlaceUtils.canClickOnAdjacentBlockToPlaceSingleSlabAt(targetBlockPos, stateSchematic, side, worldClient)) continue;
            posSide = targetBlockPos.method_10093(side);
            class_243 hitPos = EasyPlaceUtils.getHitPositionForSidePosition(posSide, side);
            double y = isTop ? 0.9 : 0.1;
            return new class_3965(new class_243(hitPos.field_1352, (double)posSide.method_10264() + y, hitPos.field_1350), side.method_10153(), posSide, false);
        }
        return null;
    }

    private static boolean canClickOnAdjacentBlockToPlaceSingleSlabAt(class_2338 targetBlockPos, class_2680 targetState, class_2350 side, class_1937 worldClient) {
        class_2338 posSide = targetBlockPos.method_10093(side);
        class_2680 stateSide = worldClient.method_8320(posSide);
        return !PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)posSide, (boolean)false) && (side.method_10166() != class_2350.class_2351.field_11052 || !EasyPlaceUtils.clientBlockIsSameMaterialSingleSlab(targetState, stateSide) || stateSide.method_11654((class_2769)class_2482.field_11501) != targetState.method_11654((class_2769)class_2482.field_11501));
    }

    private static class_1269 handleEasyPlace() {
        double reach;
        class_310 mc;
        class_638 world = mc.field_1687;
        mc = class_310.method_1551();
        class_1297 entity = mc.method_1560();
        RayTraceUtils.RayTraceWrapper traceWrapper = RayTraceUtils.getGenericTrace((class_1937)world, entity, reach = mc.field_1724.method_55754(), true, true, true);
        class_3965 targetPosition = EasyPlaceUtils.getTargetPosition(traceWrapper);
        if (targetPosition == null) {
            if (traceWrapper != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.VANILLA_BLOCK) {
                return EasyPlaceUtils.placementRestrictionInEffect() ? class_1269.field_5814 : class_1269.field_5811;
            }
            return class_1269.field_5811;
        }
        class_2338 targetBlockPos = targetPosition.method_17777();
        WorldSchematic schematicWorld = SchematicWorldHandler.getSchematicWorld();
        class_2680 stateSchematic = schematicWorld.method_8320(targetBlockPos);
        class_2680 stateClient = world.method_8320(targetBlockPos);
        class_1799 requiredStack = MaterialCache.getInstance().getRequiredBuildItemForState(stateSchematic);
        if (stateSchematic.method_26164(class_3481.field_51989)) {
            return class_1269.field_5814;
        }
        if (stateSchematic == stateClient || requiredStack.method_7960() || EasyPlaceUtils.easyPlaceIsPositionCached(targetBlockPos) || EasyPlaceUtils.easyPlaceIsTooFast() || !EasyPlaceUtils.canPlaceBlock(targetBlockPos, (class_1937)world, stateSchematic, stateClient)) {
            return class_1269.field_5814;
        }
        class_3965 clickPosition = EasyPlaceUtils.getClickPosition(targetPosition, stateSchematic, stateClient);
        class_1268 hand = PickBlockUtils.doPickBlockForStack(requiredStack);
        if (clickPosition == null || hand == null) {
            return class_1269.field_5814;
        }
        boolean isSlab = stateSchematic.method_26204() instanceof class_2482;
        boolean usingAdjacentClickPosition = !clickPosition.method_17777().equals((Object)targetBlockPos);
        class_2338 clickPos = clickPosition.method_17777();
        class_243 hitPos = clickPosition.method_17784();
        class_2350 side = clickPosition.method_17780();
        class_2350 sideOrig = targetPosition.method_17780();
        EasyPlaceProtocol protocol = PlacementHandler.getEffectiveProtocolVersion();
        double traceMaxRange = Configs.Generic.EASY_PLACE_VANILLA_REACH.getBooleanValue() ? 4.5 : 6.0;
        class_239 traceVanilla = RayTraceUtils.getRayTraceFromEntity((class_1937)mc.field_1687, (class_1297)mc.field_1724, false, traceMaxRange);
        if ((protocol == EasyPlaceProtocol.NONE || protocol == EasyPlaceProtocol.SLAB_ONLY) && traceVanilla != null && traceVanilla.method_17783() == class_239.class_240.field_1332) {
            class_3965 hitResult = (class_3965)traceVanilla;
            class_2338 posVanilla = hitResult.method_17777();
            class_2350 sideVanilla = hitResult.method_17780();
            class_2680 stateVanilla = mc.field_1687.method_8320(posVanilla);
            class_243 hit = traceVanilla.method_17784();
            class_1750 ctx = new class_1750(new class_1838((class_1657)mc.field_1724, hand, hitResult));
            if (!stateVanilla.method_26166(ctx) && targetBlockPos.equals((Object)(posVanilla = posVanilla.method_10093(sideVanilla)))) {
                hitPos = hit;
                sideOrig = sideVanilla;
            }
        }
        if (!usingAdjacentClickPosition && !isSlab) {
            side = WorldUtils.applyPlacementFacing(stateSchematic, side, stateClient);
            if (!stateClient.method_26184((class_4538)world, targetBlockPos) && stateClient.method_51176()) {
                clickPos = clickPos.method_10079(side, -1);
            }
        }
        WorldUtils.PlacementProtocolData placementData = WorldUtils.applyPlacementProtocolAll(clickPos, stateSchematic, hitPos);
        if (placementData.mustFail) {
            return class_1269.field_5814;
        }
        if (placementData.handled) {
            clickPos = placementData.pos;
            side = placementData.side;
            hitPos = placementData.hitVec;
        }
        if (protocol == EasyPlaceProtocol.V3) {
            hitPos = WorldUtils.applyPlacementProtocolV3(clickPos, stateSchematic, hitPos);
        } else if (protocol == EasyPlaceProtocol.V2 && !isSlab) {
            hitPos = WorldUtils.applyCarpetProtocolHitVec(clickPos, stateSchematic, hitPos);
        } else if (protocol == EasyPlaceProtocol.SLAB_ONLY) {
            hitPos = WorldUtils.applyBlockSlabProtocol(clickPos, stateSchematic, hitPos);
        }
        stateClient = world.method_8320(clickPos);
        boolean needsSneak = EasyPlaceUtils.hasUseAction(stateClient.method_26204());
        boolean didFakeSneak = needsSneak && EntityUtils.setFakedSneakingState(true);
        class_746 player = mc.field_1724;
        EasyPlaceUtils.cacheEasyPlacePosition(clickPos);
        class_3965 hitResult = new class_3965(hitPos, side, clickPos, false);
        class_1269 result = mc.field_1761.method_2896(mc.field_1724, hand, hitResult);
        if (result == class_1269.field_5811) {
            if (class_1269.field_5812.comp_2909().equals((Object)class_1269.class_9861.field_52427) && Configs.Generic.EASY_PLACE_SWING_HAND.getBooleanValue()) {
                player.method_6104(hand);
            }
            mc.method_1561().method_43336().method_3215(hand);
            if (isSlab && ((class_2771)stateSchematic.method_11654((class_2769)class_2482.field_11501)).equals((Object)class_2771.field_12682) && (stateClient = world.method_8320(clickPos)).method_26204() instanceof class_2482 && !((class_2771)stateClient.method_11654((class_2769)class_2482.field_11501)).equals((Object)class_2771.field_12682) && stateClient.method_26204() instanceof class_2482 && stateClient.method_11654((class_2769)class_2482.field_11501) != class_2771.field_12682) {
                side = EasyPlaceUtils.applyPlacementFacing(stateSchematic, sideOrig, stateClient);
                hitResult = new class_3965(hitPos, side, clickPos, false);
                mc.field_1761.method_2896(mc.field_1724, hand, hitResult);
            }
            if (didFakeSneak) {
                EntityUtils.setFakedSneakingState(false);
            }
            return class_1269.field_5812;
        }
        return class_1269.field_5811;
    }

    private static boolean clientBlockIsSameMaterialSingleSlab(class_2680 stateSchematic, class_2680 stateClient) {
        class_2248 blockSchematic = stateSchematic.method_26204();
        class_2248 blockClient = stateClient.method_26204();
        if (blockSchematic instanceof class_2482 && blockClient instanceof class_2482 && !((class_2771)stateClient.method_11654((class_2769)class_2482.field_11501)).equals((Object)class_2771.field_12682)) {
            class_2771 propClient;
            class_2771 propSchematic = (class_2771)stateSchematic.method_11654((class_2769)class_2482.field_11501);
            return propSchematic == (propClient = (class_2771)stateClient.method_11654((class_2769)class_2482.field_11501)) && stateSchematic.method_11654((class_2769)class_2482.field_11501) == stateClient.method_11654((class_2769)class_2482.field_11501);
        }
        return false;
    }

    private static boolean canPlaceBlock(class_2338 targetPos, class_1937 worldClient, class_2680 stateSchematic, class_2680 stateClient) {
        boolean isSlab = stateSchematic.method_26204() instanceof class_2482;
        if (isSlab) {
            return PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)targetPos, (boolean)true) || ((class_2771)stateSchematic.method_11654((class_2769)class_2482.field_11501)).equals((Object)class_2771.field_12682) && EasyPlaceUtils.clientBlockIsSameMaterialSingleSlab(stateSchematic, stateClient);
        }
        return PlacementUtils.isReplaceable((class_1937)worldClient, (class_2338)targetPos, (boolean)true);
    }

    private static class_243 applyCarpetProtocolHitVec(class_2338 pos, class_2680 state, class_243 hitVecIn) {
        double x = hitVecIn.field_1352;
        double y = hitVecIn.field_1351;
        double z = hitVecIn.field_1350;
        class_2248 block = state.method_26204();
        Optional facingOptional = BlockUtils.getFirstPropertyFacingValue((class_2680)state);
        if (facingOptional.isPresent()) {
            x = ((class_2350)facingOptional.get()).ordinal() + 2 + pos.method_10263();
        }
        if (block instanceof class_2462) {
            x += (double)(((Integer)state.method_11654((class_2769)class_2462.field_11451) - 1) * 10);
        } else if (block instanceof class_2533 && state.method_11654((class_2769)class_2533.field_11625) == class_2760.field_12619) {
            x += 10.0;
        } else if (block instanceof class_2286 && state.method_11654((class_2769)class_2286.field_10789) == class_2747.field_12578) {
            x += 10.0;
        } else if (block instanceof class_2510 && state.method_11654((class_2769)class_2510.field_11572) == class_2760.field_12619) {
            x += 10.0;
        }
        return new class_243(x, y, z);
    }

    private static class_2350 applyPlacementFacing(class_2680 stateSchematic, class_2350 side, class_2680 stateClient) {
        Optional propOptional = BlockUtils.getFirstDirectionProperty((class_2680)stateSchematic);
        if (propOptional.isPresent()) {
            side = ((class_2350)stateSchematic.method_11654((class_2769)propOptional.get())).method_10153();
        }
        return side;
    }

    public static boolean handlePlacementRestriction() {
        boolean cancel = EasyPlaceUtils.placementRestrictionInEffect();
        if (cancel && isFirstClickPlacementRestriction) {
            InfoUtils.printActionbarMessage((String)"litematica.message.placement_restriction_fail", (Object[])new Object[0]);
        }
        isFirstClickPlacementRestriction = false;
        return cancel;
    }

    private static boolean placementRestrictionInEffect() {
        class_310 mc = class_310.method_1551();
        class_1297 entity = mc.method_1560();
        class_638 world = mc.field_1687;
        if (world == null || mc.field_1724 == null || entity == null) {
            return false;
        }
        double reach = mc.field_1724.method_55754();
        class_239 trace = fi.dy.masa.malilib.util.game.RayTraceUtils.getRayTraceFromEntity((class_1937)world, (class_1297)entity, (class_3959.class_242)class_3959.class_242.field_1348, (boolean)false, (double)reach);
        if (trace.method_17783() == class_239.class_240.field_1332) {
            class_3965 blockHitResult = (class_3965)trace;
            class_2338 pos = blockHitResult.method_17777();
            class_2680 stateClient = world.method_8320(pos);
            if (!stateClient.method_26184((class_4538)world, pos)) {
                pos = pos.method_10093(blockHitResult.method_17780());
                stateClient = world.method_8320(pos);
            }
            if (!EasyPlaceUtils.isPositionWithinRangeOfSchematicRegions(pos, 2)) {
                return false;
            }
            if (!stateClient.method_26184((class_4538)world, pos) && !stateClient.method_51176()) {
                return true;
            }
            WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
            LayerRange range = DataManager.getRenderLayerRange();
            if (worldSchematic.method_22347(pos) || !range.isPositionWithinRange(pos)) {
                return true;
            }
            class_2680 stateSchematic = worldSchematic.method_8320(pos);
            class_1799 stack = MaterialCache.getInstance().getRequiredBuildItemForState(stateSchematic);
            return stack.method_7960() || EntityUtils.getUsedHandForItem((class_1309)mc.field_1724, stack, true) == null;
        }
        return false;
    }

    private static boolean isPositionWithinRangeOfSchematicRegions(class_2338 pos, int range) {
        SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
        int minCX = pos.method_10263() - range >> 4;
        int minCY = pos.method_10264() - range >> 4;
        int minCZ = pos.method_10260() - range >> 4;
        int maxCX = pos.method_10263() + range >> 4;
        int maxCY = pos.method_10264() + range >> 4;
        int maxCZ = pos.method_10260() + range >> 4;
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        for (int cy = minCY; cy <= maxCY; ++cy) {
            for (int cz = minCZ; cz <= maxCZ; ++cz) {
                for (int cx = minCX; cx <= maxCX; ++cx) {
                    List<SchematicPlacementManager.PlacementPart> parts = manager.getPlacementPartsInChunk(cx, cz);
                    for (SchematicPlacementManager.PlacementPart part : parts) {
                        IntBoundingBox box = part.bb;
                        if (x < box.minX - range || x > box.maxX + range || y < box.minY - range || y > box.maxY + range || z < box.minZ - range || z > box.maxZ + range) continue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected static boolean easyPlaceIsPositionCached(class_2338 pos) {
        long currentTime = System.nanoTime();
        boolean cached = false;
        for (int i = 0; i < EASY_PLACE_POSITIONS.size(); ++i) {
            PositionCache val = EASY_PLACE_POSITIONS.get(i);
            boolean expired = val.hasExpired(currentTime);
            if (expired) {
                EASY_PLACE_POSITIONS.remove(i);
                --i;
                continue;
            }
            if (!val.getPos().equals((Object)pos)) continue;
            cached = true;
            if (EASY_PLACE_POSITIONS.size() < 16) break;
        }
        return cached;
    }

    protected static void cacheEasyPlacePosition(class_2338 pos) {
        EASY_PLACE_POSITIONS.add(new PositionCache(pos, System.nanoTime(), 2000000000L));
    }

    protected static boolean easyPlaceIsTooFast() {
        return System.nanoTime() - easyPlaceLastPickBlockTime < 1000000L * (long)Configs.Generic.EASY_PLACE_SWAP_INTERVAL.getIntegerValue();
    }

    protected static void setEasyPlaceLastPickBlockTime() {
        easyPlaceLastPickBlockTime = System.nanoTime();
    }

    static {
        easyPlaceLastPickBlockTime = System.nanoTime();
    }

    protected static class PositionCache {
        private final class_2338 pos;
        private final long time;
        private final long timeout;

        private PositionCache(class_2338 pos, long time, long timeout) {
            this.pos = pos;
            this.time = time;
            this.timeout = timeout;
        }

        public class_2338 getPos() {
            return this.pos;
        }

        public boolean hasExpired(long currentTime) {
            return currentTime - this.time > this.timeout;
        }
    }
}

