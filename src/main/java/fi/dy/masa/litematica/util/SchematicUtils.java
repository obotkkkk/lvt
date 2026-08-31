/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiTextInput
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.IRangeChangeListener
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.interfaces.IStringConsumerFeedback
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.SubChunkPos
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1747
 *  net.minecraft.class_1750
 *  net.minecraft.class_1799
 *  net.minecraft.class_1838
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_243
 *  net.minecraft.class_2470
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3965
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiSchematicSave;
import fi.dy.masa.litematica.mixin.entity.IMixinEntity;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskDeleteArea;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkBase;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkCommand;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkDirect;
import fi.dy.masa.litematica.scheduler.tasks.TaskSaveSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.litematica.util.BlockUtils;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextInput;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.IRangeChangeListener;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.interfaces.IStringConsumerFeedback;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.SubChunkPos;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1838;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_437;

public class SchematicUtils {
    private static long areaMovedTime;

    public static boolean saveSchematic(boolean inMemoryOnly) {
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();
        if (area != null) {
            if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                String title = "litematica.gui.title.schematic_projects.save_new_version";
                SchematicProject project = DataManager.getSchematicProjectsManager().getCurrentProject();
                GuiTextInput gui = new GuiTextInput(512, title, project.getCurrentVersionName(), GuiUtils.getCurrentScreen(), (IStringConsumerFeedback)new SchematicVersionCreator());
                GuiBase.openGui((class_437)gui);
            } else if (inMemoryOnly) {
                String title = "litematica.gui.title.create_in_memory_schematic";
                GuiTextInput gui = new GuiTextInput(512, title, area.getName(), GuiUtils.getCurrentScreen(), (IStringConsumer)new GuiSchematicSave.InMemorySchematicCreator(area));
                GuiBase.openGui((class_437)gui);
            } else {
                GuiSchematicSave gui = new GuiSchematicSave();
                gui.setParent(GuiUtils.getCurrentScreen());
                GuiBase.openGui((class_437)gui);
            }
            return true;
        }
        return false;
    }

    public static void unloadCurrentlySelectedSchematic() {
        SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
        if (placement != null) {
            SchematicHolder.getInstance().removeSchematic(placement.getSchematic());
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_placement_selected", (Object[])new Object[0]);
        }
    }

    public static boolean breakSchematicBlock(class_310 mc) {
        return SchematicUtils.setTargetedSchematicBlockState(mc, class_2246.field_10124.method_9564());
    }

    public static boolean placeSchematicBlock(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null) {
            class_2338 pos = info.pos.method_10093(info.side);
            if (DataManager.getRenderLayerRange().isPositionWithinRange(pos)) {
                return SchematicUtils.setTargetedSchematicBlockState(pos, info.stateNew);
            }
        }
        return false;
    }

    public static boolean replaceSchematicBlocksInDirection(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null) {
            class_2350 playerFacingH = mc.field_1724.method_5735();
            class_2350 direction = fi.dy.masa.malilib.util.PositionUtils.getTargetedDirection((class_2350)info.side, (class_2350)playerFacingH, (class_2338)info.pos, (class_243)info.hitVec);
            if (direction == info.side) {
                direction = direction.method_10153();
            }
            class_2338 posEnd = SchematicUtils.getReplacementBoxEndPos(info.pos, direction);
            return SchematicUtils.setSchematicBlockStates(info.pos, posEnd, info.stateNew);
        }
        return false;
    }

    public static boolean replaceAllIdenticalSchematicBlocks(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null) {
            return SchematicUtils.setAllIdenticalSchematicBlockStates(info.pos, info.stateOriginal, info.stateNew, (class_1937)mc.field_1687);
        }
        return false;
    }

    public static boolean replaceBlocksKeepingProperties(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null && info.stateNew != info.stateOriginal && BlockUtils.blocksHaveSameProperties(info.stateOriginal, info.stateNew)) {
            Object2ObjectOpenHashMap map = new Object2ObjectOpenHashMap();
            BiPredicate<class_2680, class_2680> blockStateTest = (testedState, originalState) -> testedState.method_26204() == originalState.method_26204();
            BiFunction<class_2680, class_2680, class_2680> blockModifier = (newState, originalState) -> (class_2680)map.computeIfAbsent(originalState, k -> {
                class_2680 finalState = newState;
                for (class_2769 prop : newState.method_28501()) {
                    finalState = BlockUtils.getBlockStateWithProperty(finalState, prop, originalState.method_11654(prop));
                }
                return finalState;
            });
            return SchematicUtils.setAllIdenticalSchematicBlockStates(info.pos, info.stateOriginal, info.stateNew, blockStateTest, blockModifier, (class_1937)mc.field_1687);
        }
        return false;
    }

    public static boolean breakSchematicBlocks(class_310 mc) {
        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
        RayTraceUtils.RayTraceWrapper wrapper = RayTraceUtils.getSchematicWorldTraceWrapperIfClosest((class_1937)mc.field_1687, entity, 10.0);
        if (wrapper != null && wrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = wrapper.getBlockHitResult();
            class_2338 pos = trace.method_17777();
            class_2350 playerFacingH = mc.field_1724.method_5735();
            class_2350 direction = fi.dy.masa.malilib.util.PositionUtils.getTargetedDirection((class_2350)trace.method_17780(), (class_2350)playerFacingH, (class_2338)pos, (class_243)trace.method_17784());
            if (direction == trace.method_17780()) {
                direction = direction.method_10153();
            }
            class_2338 posEnd = SchematicUtils.getReplacementBoxEndPos(pos, direction);
            return SchematicUtils.setSchematicBlockStates(pos, posEnd, class_2246.field_10124.method_9564());
        }
        return false;
    }

    public static boolean breakAllIdenticalSchematicBlocks(class_310 mc) {
        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
        RayTraceUtils.RayTraceWrapper wrapper = RayTraceUtils.getSchematicWorldTraceWrapperIfClosest((class_1937)mc.field_1687, entity, 10.0);
        if (wrapper != null && wrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = wrapper.getBlockHitResult();
            class_2338 pos = trace.method_17777();
            class_2680 stateOriginal = SchematicWorldHandler.getSchematicWorld().method_8320(pos);
            return SchematicUtils.setAllIdenticalSchematicBlockStates(pos, stateOriginal, class_2246.field_10124.method_9564(), (class_1937)mc.field_1687);
        }
        return false;
    }

    public static boolean placeSchematicBlocksInDirection(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null && mc.field_1724 != null) {
            class_2350 playerFacingH = mc.field_1724.method_5735();
            class_2350 direction = fi.dy.masa.malilib.util.PositionUtils.getTargetedDirection((class_2350)info.side, (class_2350)playerFacingH, (class_2338)info.pos, (class_243)info.hitVec);
            class_2338 posStart = info.pos.method_10093(info.side);
            if (SchematicWorldHandler.getSchematicWorld().method_8320(posStart).method_26215()) {
                class_2338 posEnd = SchematicUtils.getReplacementBoxEndPos(posStart, direction);
                return SchematicUtils.setSchematicBlockStates(posStart, posEnd, info.stateNew);
            }
        }
        return false;
    }

    public static boolean breakAllSchematicBlocksExceptTargeted(class_310 mc) {
        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
        RayTraceUtils.RayTraceWrapper wrapper = RayTraceUtils.getSchematicWorldTraceWrapperIfClosest((class_1937)mc.field_1687, entity, 10.0);
        if (wrapper != null && wrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = wrapper.getBlockHitResult();
            class_2338 pos = trace.method_17777();
            class_2680 stateOriginal = SchematicWorldHandler.getSchematicWorld().method_8320(pos);
            return SchematicUtils.setAllStatesToAirExcept(pos, stateOriginal, (class_1937)mc.field_1687);
        }
        return false;
    }

    public static boolean fillAirWithBlocks(class_310 mc) {
        ReplacementInfo info = SchematicUtils.getTargetInfo(mc);
        if (info != null && info.stateNew != null) {
            class_2338 posStart = info.pos.method_10093(info.side);
            if (SchematicWorldHandler.getSchematicWorld().method_8320(posStart).method_26215()) {
                return SchematicUtils.setAllIdenticalSchematicBlockStates(posStart, class_2246.field_10124.method_9564(), info.stateNew, (class_1937)mc.field_1687);
            }
        }
        return false;
    }

    @Nullable
    private static ReplacementInfo getTargetInfo(class_310 mc) {
        class_1799 stack = mc.field_1724.method_6047();
        if (!stack.method_7960() && stack.method_7909() instanceof class_1747 || stack.method_7960() && ToolMode.REBUILD.getPrimaryBlock() != null) {
            WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
            class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
            RayTraceUtils.RayTraceWrapper traceWrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, entity, 10.0);
            if (worldSchematic != null && traceWrapper != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
                class_3965 trace = traceWrapper.getBlockHitResult();
                class_2350 side = trace.method_17780();
                class_243 hitVec = trace.method_17784();
                class_2338 pos = trace.method_17777();
                class_2680 stateOriginal = worldSchematic.method_8320(pos);
                class_2680 stateNew = class_2246.field_10124.method_9564();
                if (stack.method_7909() instanceof class_1747) {
                    class_1937 worldClient = mc.field_1724.method_37908();
                    ((IMixinEntity)mc.field_1724).litematica_setWorld(worldSchematic);
                    class_3965 hit = new class_3965(trace.method_17784(), side, pos.method_10093(side), false);
                    class_1750 ctx = new class_1750(new class_1838((class_1657)mc.field_1724, class_1268.field_5808, hit));
                    ((IMixinEntity)mc.field_1724).litematica_setWorld(worldClient);
                    stateNew = ((class_1747)stack.method_7909()).method_7711().method_9605(ctx);
                } else if (ToolMode.REBUILD.getPrimaryBlock() != null) {
                    stateNew = ToolMode.REBUILD.getPrimaryBlock();
                }
                return new ReplacementInfo(pos, side, hitVec, stateOriginal, stateNew);
            }
        }
        return null;
    }

    private static class_2338 getReplacementBoxEndPos(class_2338 startPos, class_2350 direction) {
        return SchematicUtils.getReplacementBoxEndPos(startPos, direction, 10000);
    }

    private static class_2338 getReplacementBoxEndPos(class_2338 startPos, class_2350 direction, int maxBlocks) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        LayerRange range = DataManager.getRenderLayerRange();
        class_2680 stateStart = world.method_8320(startPos);
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        posMutable.method_10101((class_2382)startPos);
        while (maxBlocks-- > 0) {
            posMutable.method_10098(direction);
            if (range.isPositionWithinRange((class_2338)posMutable) && world.getChunkProvider().method_12123(posMutable.method_10263() >> 4, posMutable.method_10260() >> 4) && world.method_8320((class_2338)posMutable) == stateStart) continue;
            posMutable.method_10098(direction.method_10153());
            break;
        }
        return posMutable.method_10062();
    }

    public static boolean setTargetedSchematicBlockState(class_310 mc, class_2680 state) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
        RayTraceUtils.RayTraceWrapper traceWrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, entity, WorldUtils.getValidBlockRange(mc));
        if (world != null && traceWrapper != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = traceWrapper.getBlockHitResult();
            class_2338 pos = trace.method_17777();
            return SchematicUtils.setTargetedSchematicBlockState(pos, state);
        }
        return false;
    }

    private static boolean setTargetedSchematicBlockState(class_2338 pos, class_2680 state) {
        if (pos != null) {
            SubChunkPos cpos = new SubChunkPos(pos);
            List<SchematicPlacementManager.PlacementPart> list = DataManager.getSchematicPlacementManager().getAllPlacementsTouchingChunk(pos);
            if (!list.isEmpty()) {
                for (SchematicPlacementManager.PlacementPart part : list) {
                    if (!part.getBox().containsPos((class_2382)pos)) continue;
                    SchematicPlacement placement = part.getPlacement();
                    String regionName = part.getSubRegionName();
                    LitematicaBlockStateContainer container = placement.getSchematic().getSubRegionContainer(regionName);
                    class_2338 posSchematic = SchematicUtils.getSchematicContainerPositionFromWorldPosition(pos, placement.getSchematic(), regionName, placement, placement.getRelativeSubRegionPlacement(regionName), container);
                    if (posSchematic != null) {
                        state = SchematicUtils.getUntransformedBlockState(state, placement, regionName);
                        class_2680 stateOriginal = container.get(posSchematic.method_10263(), posSchematic.method_10264(), posSchematic.method_10260());
                        int totalBlocks = part.getPlacement().getSchematic().getMetadata().getTotalBlocks();
                        int increment = 0;
                        increment = !stateOriginal.method_26215() ? (!state.method_26215() ? 0 : -1) : (!state.method_26215() ? 1 : 0);
                        container.set(posSchematic.method_10263(), posSchematic.method_10264(), posSchematic.method_10260(), state);
                        SchematicMetadata metadata = part.getPlacement().getSchematic().getMetadata();
                        metadata.setTotalBlocks(totalBlocks += increment);
                        metadata.setTimeModifiedToNow();
                        metadata.setModifiedSinceSaved();
                        DataManager.getSchematicPlacementManager().markChunkForRebuild(new class_1923(cpos.method_10263(), cpos.method_10260()));
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    private static boolean setSchematicBlockStates(class_2338 posStart, class_2338 posEnd, class_2680 state) {
        List<SchematicPlacementManager.PlacementPart> list;
        if (posStart != null && posEnd != null && !(list = DataManager.getSchematicPlacementManager().getAllPlacementsTouchingChunk(posStart)).isEmpty()) {
            for (SchematicPlacementManager.PlacementPart part : list) {
                if (!part.getBox().containsPos((class_2382)posStart)) continue;
                SchematicPlacement placement = part.getPlacement();
                String regionName = part.getSubRegionName();
                LitematicaBlockStateContainer container = placement.getSchematic().getSubRegionContainer(regionName);
                class_2338 posStartSchematic = SchematicUtils.getSchematicContainerPositionFromWorldPosition(posStart, placement.getSchematic(), regionName, placement, placement.getRelativeSubRegionPlacement(regionName), container);
                class_2338 posEndSchematic = SchematicUtils.getSchematicContainerPositionFromWorldPosition(posEnd, placement.getSchematic(), regionName, placement, placement.getRelativeSubRegionPlacement(regionName), container);
                if (posStartSchematic != null && posEndSchematic != null) {
                    class_2338 posMin = PositionUtils.getMinCorner(posStartSchematic, posEndSchematic);
                    class_2338 posMax = PositionUtils.getMaxCorner(posStartSchematic, posEndSchematic);
                    int minX = Math.max(posMin.method_10263(), 0);
                    int minY = Math.max(posMin.method_10264(), 0);
                    int minZ = Math.max(posMin.method_10260(), 0);
                    int maxX = Math.min(posMax.method_10263(), container.getSize().method_10263() - 1);
                    int maxY = Math.min(posMax.method_10264(), container.getSize().method_10264() - 1);
                    int maxZ = Math.min(posMax.method_10260(), container.getSize().method_10260() - 1);
                    int totalBlocks = part.getPlacement().getSchematic().getMetadata().getTotalBlocks();
                    int increment = 0;
                    state = SchematicUtils.getUntransformedBlockState(state, placement, regionName);
                    for (int y = minY; y <= maxY; ++y) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            for (int x = minX; x <= maxX; ++x) {
                                class_2680 stateOriginal = container.get(x, y, z);
                                increment = !stateOriginal.method_26215() ? (!state.method_26215() ? 0 : -1) : (!state.method_26215() ? 1 : 0);
                                totalBlocks += increment;
                                container.set(x, y, z, state);
                            }
                        }
                    }
                    SchematicMetadata metadata = part.getPlacement().getSchematic().getMetadata();
                    metadata.setTotalBlocks(totalBlocks);
                    metadata.setTimeModifiedToNow();
                    metadata.setModifiedSinceSaved();
                    DataManager.getSchematicPlacementManager().markAllPlacementsOfSchematicForRebuild(placement.getSchematic());
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean setAllIdenticalSchematicBlockStates(class_2338 posStart, class_2680 stateOriginal, class_2680 stateNew, class_1937 world) {
        BiPredicate<class_2680, class_2680> blockStateTest = (testedState, originalState) -> testedState == originalState;
        BiFunction<class_2680, class_2680, class_2680> blockModifier = (newState, originalState) -> newState;
        return SchematicUtils.setAllIdenticalSchematicBlockStates(posStart, stateOriginal, stateNew, blockStateTest, blockModifier, world);
    }

    private static boolean setAllIdenticalSchematicBlockStates(class_2338 posStart, class_2680 stateOriginal, class_2680 stateNew, BiPredicate<class_2680, class_2680> blockStateTest, BiFunction<class_2680, class_2680, class_2680> blockModifier, class_1937 world) {
        SchematicPlacementManager manager;
        List<SchematicPlacementManager.PlacementPart> list;
        if (posStart != null && !(list = (manager = DataManager.getSchematicPlacementManager()).getAllPlacementsTouchingChunk(posStart)).isEmpty()) {
            for (SchematicPlacementManager.PlacementPart part : list) {
                if (!part.getBox().containsPos((class_2382)posStart)) continue;
                if (SchematicUtils.replaceAllIdenticalBlocks(manager, part, stateOriginal, stateNew, blockStateTest, blockModifier, world)) {
                    manager.markAllPlacementsOfSchematicForRebuild(part.getPlacement().getSchematic());
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean setAllStatesToAirExcept(class_2338 pos, class_2680 state, class_1937 world) {
        SchematicPlacementManager manager;
        List<SchematicPlacementManager.PlacementPart> list;
        if (pos != null && !(list = (manager = DataManager.getSchematicPlacementManager()).getAllPlacementsTouchingChunk(pos)).isEmpty()) {
            for (SchematicPlacementManager.PlacementPart part : list) {
                if (!part.getBox().containsPos((class_2382)pos)) continue;
                if (SchematicUtils.setAllStatesToAirExcept(manager, part, state, world)) {
                    manager.markAllPlacementsOfSchematicForRebuild(part.getPlacement().getSchematic());
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean setAllStatesToAirExcept(SchematicPlacementManager manager, SchematicPlacementManager.PlacementPart part, class_2680 state, class_1937 world) {
        SchematicPlacement schematicPlacement = part.getPlacement();
        String selected = schematicPlacement.getSelectedSubRegionName();
        ArrayList<String> regions = new ArrayList<String>();
        class_2680 air = class_2246.field_10124.method_9564();
        if (selected != null) {
            regions.add(selected);
        } else if (manager.getSelectedSchematicPlacement() == schematicPlacement) {
            regions.addAll((Collection<String>)schematicPlacement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED).keySet());
        } else {
            InfoUtils.showInGameMessage((Message.MessageType)Message.MessageType.WARNING, (long)20000L, (String)"litematica.message.warn.schematic_rebuild_placement_not_selected", (Object[])new Object[0]);
            return false;
        }
        LayerRange range = DataManager.getRenderLayerRange();
        int totalBlocks = schematicPlacement.getSchematic().getMetadata().getTotalBlocks();
        for (String regionName : regions) {
            LitematicaBlockStateContainer container = schematicPlacement.getSchematic().getSubRegionContainer(regionName);
            SubRegionPlacement placement = schematicPlacement.getRelativeSubRegionPlacement(regionName);
            if (container == null || placement == null) continue;
            int minX = range.getClampedValue(-30000000, class_2350.class_2351.field_11048);
            int minZ = range.getClampedValue(-30000000, class_2350.class_2351.field_11051);
            int maxX = range.getClampedValue(30000000, class_2350.class_2351.field_11048);
            int maxZ = range.getClampedValue(30000000, class_2350.class_2351.field_11051);
            int minY = range.getClampedValue(world.method_31607(), class_2350.class_2351.field_11052);
            int maxY = range.getClampedValue(world.method_31600(), class_2350.class_2351.field_11052);
            class_2338 posStart = new class_2338(minX, minY, minZ);
            class_2338 posEnd = new class_2338(maxX, maxY, maxZ);
            class_2338 pos1 = SchematicUtils.getReverserTransformedWorldPosition(posStart, schematicPlacement.getSchematic(), regionName, schematicPlacement, schematicPlacement.getRelativeSubRegionPlacement(regionName));
            class_2338 pos2 = SchematicUtils.getReverserTransformedWorldPosition(posEnd, schematicPlacement.getSchematic(), regionName, schematicPlacement, schematicPlacement.getRelativeSubRegionPlacement(regionName));
            if (pos1 == null || pos2 == null) {
                return false;
            }
            class_2338 posStartWorld = PositionUtils.getMinCorner(pos1, pos2);
            class_2338 posEndWorld = PositionUtils.getMaxCorner(pos1, pos2);
            class_2382 size = container.getSize();
            int startX = Math.max(posStartWorld.method_10263(), 0);
            int startY = Math.max(posStartWorld.method_10264(), 0);
            int startZ = Math.max(posStartWorld.method_10260(), 0);
            int endX = Math.min(posEndWorld.method_10263(), size.method_10263() - 1);
            int endY = Math.min(posEndWorld.method_10264(), size.method_10264() - 1);
            int endZ = Math.min(posEndWorld.method_10260(), size.method_10260() - 1);
            if (endX >= size.method_10263() || endY >= size.method_10264() || endZ >= size.method_10260()) {
                System.out.printf("OUT OF BOUNDS == region: %s, sx: %d, sy: %s, sz: %d, ex: %d, ey: %d, ez: %d - size x: %d y: %d z: %d =============\n", regionName, startX, startY, startZ, endX, endY, endZ, size.method_10263(), size.method_10264(), size.method_10260());
                return false;
            }
            class_2680 stateOriginal = SchematicUtils.getUntransformedBlockState(state, schematicPlacement, regionName);
            for (int y = startY; y <= endY; ++y) {
                for (int z = startZ; z <= endZ; ++z) {
                    for (int x = startX; x <= endX; ++x) {
                        class_2680 oldState = container.get(x, y, z);
                        if (oldState == stateOriginal || oldState.method_26215()) continue;
                        container.set(x, y, z, air);
                        --totalBlocks;
                    }
                }
            }
        }
        schematicPlacement.getSchematic().getMetadata().setTotalBlocks(totalBlocks);
        return true;
    }

    private static boolean replaceAllIdenticalBlocks(SchematicPlacementManager manager, SchematicPlacementManager.PlacementPart part, class_2680 stateOriginalIn, class_2680 stateNewIn, BiPredicate<class_2680, class_2680> blockStateTest, BiFunction<class_2680, class_2680, class_2680> blockModifier, class_1937 world) {
        SchematicPlacement schematicPlacement = part.getPlacement();
        String selected = schematicPlacement.getSelectedSubRegionName();
        ArrayList<String> regions = new ArrayList<String>();
        if (selected != null) {
            regions.add(selected);
        } else if (manager.getSelectedSchematicPlacement() == schematicPlacement) {
            regions.addAll((Collection<String>)schematicPlacement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED).keySet());
        } else {
            InfoUtils.showInGameMessage((Message.MessageType)Message.MessageType.WARNING, (long)20000L, (String)"litematica.message.warn.schematic_rebuild_placement_not_selected", (Object[])new Object[0]);
            return false;
        }
        LayerRange range = DataManager.getRenderLayerRange();
        int totalBlocks = schematicPlacement.getSchematic().getMetadata().getTotalBlocks();
        int increment = 0;
        increment = !stateOriginalIn.method_26215() ? (!stateNewIn.method_26215() ? 0 : -1) : (!stateNewIn.method_26215() ? 1 : 0);
        for (String regionName : regions) {
            LitematicaBlockStateContainer container = schematicPlacement.getSchematic().getSubRegionContainer(regionName);
            SubRegionPlacement placement = schematicPlacement.getRelativeSubRegionPlacement(regionName);
            if (container == null || placement == null) continue;
            int minX = range.getClampedValue(-30000000, class_2350.class_2351.field_11048);
            int minZ = range.getClampedValue(-30000000, class_2350.class_2351.field_11051);
            int maxX = range.getClampedValue(30000000, class_2350.class_2351.field_11048);
            int maxZ = range.getClampedValue(30000000, class_2350.class_2351.field_11051);
            int minY = range.getClampedValue(world.method_31607(), class_2350.class_2351.field_11052);
            int maxY = range.getClampedValue(world.method_31600(), class_2350.class_2351.field_11052);
            class_2338 posStart = new class_2338(minX, minY, minZ);
            class_2338 posEnd = new class_2338(maxX, maxY, maxZ);
            class_2338 pos1 = SchematicUtils.getReverserTransformedWorldPosition(posStart, schematicPlacement.getSchematic(), regionName, schematicPlacement, schematicPlacement.getRelativeSubRegionPlacement(regionName));
            class_2338 pos2 = SchematicUtils.getReverserTransformedWorldPosition(posEnd, schematicPlacement.getSchematic(), regionName, schematicPlacement, schematicPlacement.getRelativeSubRegionPlacement(regionName));
            if (pos1 == null || pos2 == null) {
                return false;
            }
            class_2338 posStartWorld = PositionUtils.getMinCorner(pos1, pos2);
            class_2338 posEndWorld = PositionUtils.getMaxCorner(pos1, pos2);
            class_2382 size = container.getSize();
            int startX = Math.max(posStartWorld.method_10263(), 0);
            int startY = Math.max(posStartWorld.method_10264(), 0);
            int startZ = Math.max(posStartWorld.method_10260(), 0);
            int endX = Math.min(posEndWorld.method_10263(), size.method_10263() - 1);
            int endY = Math.min(posEndWorld.method_10264(), size.method_10264() - 1);
            int endZ = Math.min(posEndWorld.method_10260(), size.method_10260() - 1);
            if (startX < 0 || startY < 0 || startZ < 0 || endX >= size.method_10263() || endY >= size.method_10264() || endZ >= size.method_10260()) {
                System.out.printf("OUT OF BOUNDS == region: %s, sx: %d, sy: %s, sz: %d, ex: %d, ey: %d, ez: %d - size x: %d y: %d z: %d =============\n", regionName, startX, startY, startZ, endX, endY, endZ, size.method_10263(), size.method_10264(), size.method_10260());
                return false;
            }
            class_2680 stateOriginal = SchematicUtils.getUntransformedBlockState(stateOriginalIn, schematicPlacement, regionName);
            class_2680 stateNew = SchematicUtils.getUntransformedBlockState(stateNewIn, schematicPlacement, regionName);
            for (int y = startY; y <= endY; ++y) {
                for (int z = startZ; z <= endZ; ++z) {
                    for (int x = startX; x <= endX; ++x) {
                        class_2680 oldState = container.get(x, y, z);
                        if (!blockStateTest.test(oldState, stateOriginal)) continue;
                        class_2680 finalState = blockModifier.apply(stateNew, oldState);
                        container.set(x, y, z, finalState);
                        totalBlocks += increment;
                    }
                }
            }
        }
        SchematicMetadata metadata = part.getPlacement().getSchematic().getMetadata();
        metadata.setTotalBlocks(totalBlocks);
        metadata.setTimeModifiedToNow();
        metadata.setModifiedSinceSaved();
        return true;
    }

    public static void moveCurrentlySelectedWorldRegionToLookingDirection(int amount, class_1297 entity, class_310 mc) {
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();
        if (area != null && area.getAllSubRegionBoxes().size() > 0) {
            class_2338 pos = area.getEffectiveOrigin().method_10079(EntityUtils.getClosestLookingDirection(entity), amount);
            SchematicUtils.moveCurrentlySelectedWorldRegionTo(pos, mc);
        }
    }

    public static void moveCurrentlySelectedWorldRegionTo(class_2338 pos, class_310 mc) {
        if (mc.field_1724 == null || !EntityUtils.isCreativeMode((class_1657)mc.field_1724)) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.generic.creative_mode_only", (Object[])new Object[0]);
            return;
        }
        TaskScheduler scheduler = TaskScheduler.getServerInstanceIfExistsOrClient();
        long currentTime = System.currentTimeMillis();
        if (currentTime - areaMovedTime < 400L || scheduler.hasTask(TaskSaveSchematic.class) || scheduler.hasTask(TaskDeleteArea.class) || scheduler.hasTask(TaskPasteSchematicPerChunkCommand.class) || scheduler.hasTask(TaskPasteSchematicPerChunkDirect.class)) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.move.pending_tasks", (Object[])new Object[0]);
            return;
        }
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();
        if (area != null && area.getAllSubRegionBoxes().size() > 0) {
            LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(area, "");
            LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
            TaskSaveSchematic taskSave = new TaskSaveSchematic(schematic, area, info);
            taskSave.disableCompletionMessage();
            areaMovedTime = System.currentTimeMillis();
            taskSave.setCompletionListener(() -> {
                SchematicPlacement placement = SchematicPlacement.createFor(schematic, pos, "-", true, true);
                DataManager.getSchematicPlacementManager().addSchematicPlacement(placement, false);
                TaskDeleteArea taskDelete = new TaskDeleteArea(area.getAllSubRegionBoxes(), true);
                taskDelete.disableCompletionMessage();
                areaMovedTime = System.currentTimeMillis();
                taskDelete.setCompletionListener(() -> {
                    LayerRange range = new LayerRange((IRangeChangeListener)SchematicWorldRefresher.INSTANCE);
                    TaskPasteSchematicPerChunkBase taskPaste = mc.method_1496() ? new TaskPasteSchematicPerChunkDirect(Collections.singletonList(placement), range, false) : new TaskPasteSchematicPerChunkCommand(Collections.singletonList(placement), range, false);
                    taskPaste.disableCompletionMessage();
                    areaMovedTime = System.currentTimeMillis();
                    taskPaste.setCompletionListener(() -> {
                        SchematicHolder.getInstance().removeSchematic(schematic);
                        area.moveEntireSelectionTo(pos, false);
                        areaMovedTime = System.currentTimeMillis();
                    });
                    scheduler.scheduleTask(taskPaste, 1);
                });
                scheduler.scheduleTask(taskDelete, 1);
            });
            scheduler.scheduleTask(taskSave, 1);
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_area_selected", (Object[])new Object[0]);
        }
    }

    public static void cloneSelectionArea(class_310 mc) {
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();
        if (area != null && area.getAllSubRegionBoxes().size() > 0) {
            class_2338 originTmp;
            LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(area, mc.field_1724.method_5477().getString());
            LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
            TaskSaveSchematic taskSave = new TaskSaveSchematic(schematic, area, info);
            taskSave.disableCompletionMessage();
            class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
            if (Configs.Generic.CLONE_AT_ORIGINAL_POS.getBooleanValue()) {
                originTmp = area.getEffectiveOrigin();
            } else {
                originTmp = RayTraceUtils.getTargetedPosition((class_1937)mc.field_1687, entity, WorldUtils.getValidBlockRange(mc), false);
                if (originTmp == null) {
                    originTmp = fi.dy.masa.malilib.util.PositionUtils.getEntityBlockPos((class_1297)entity);
                }
            }
            class_2338 origin = originTmp;
            String name = schematic.getMetadata().getName();
            taskSave.setCompletionListener(() -> {
                SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
                SchematicPlacement placement = SchematicPlacement.createFor(schematic, origin, name, true, true);
                manager.addSchematicPlacement(placement, false);
                manager.setSelectedSchematicPlacement(placement);
                if (EntityUtils.isCreativeMode((class_1657)mc.field_1724)) {
                    DataManager.setToolMode(ToolMode.PASTE_SCHEMATIC);
                }
            });
            TaskScheduler.getServerInstanceIfExistsOrClient().scheduleTask(taskSave, 10);
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_area_selected", (Object[])new Object[0]);
        }
    }

    @Nullable
    public static class_2338 getSchematicContainerPositionFromWorldPosition(class_2338 worldPos, LitematicaSchematic schematic, String regionName, SchematicPlacement schematicPlacement, SubRegionPlacement regionPlacement, LitematicaBlockStateContainer container) {
        class_2338 boxMinRel = SchematicUtils.getReverserTransformedWorldPosition(worldPos, schematic, regionName, schematicPlacement, regionPlacement);
        if (boxMinRel == null) {
            return null;
        }
        int startX = boxMinRel.method_10263();
        int startY = boxMinRel.method_10264();
        int startZ = boxMinRel.method_10260();
        class_2382 size = container.getSize();
        return new class_2338(class_3532.method_15340((int)startX, (int)0, (int)(size.method_10263() - 1)), class_3532.method_15340((int)startY, (int)0, (int)(size.method_10264() - 1)), class_3532.method_15340((int)startZ, (int)0, (int)(size.method_10260() - 1)));
    }

    @Nullable
    private static class_2338 getReverserTransformedWorldPosition(class_2338 worldPos, LitematicaSchematic schematic, String regionName, SchematicPlacement schematicPlacement, SubRegionPlacement regionPlacement) {
        class_2338 origin = schematicPlacement.getOrigin();
        class_2338 regionPos = regionPlacement.getPos();
        class_2338 regionSize = schematic.getAreaSize(regionName);
        if (regionSize == null) {
            return null;
        }
        class_2338 posEndRel = PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)regionSize).method_10081((class_2382)regionPos);
        class_2338 posMinRel = PositionUtils.getMinCorner(regionPos, posEndRel);
        class_2338 regionPosTransformed = PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        class_2338 relPos = new class_2338(worldPos.method_10263() - origin.method_10263() - regionPosTransformed.method_10263(), worldPos.method_10264() - origin.method_10264() - regionPosTransformed.method_10264(), worldPos.method_10260() - origin.method_10260() - regionPosTransformed.method_10260());
        relPos = PositionUtils.getReverseTransformedBlockPos(relPos, regionPlacement.getMirror(), regionPlacement.getRotation());
        relPos = PositionUtils.getReverseTransformedBlockPos(relPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        relPos = relPos.method_10059((class_2382)posMinRel.method_10059((class_2382)regionPos));
        return relPos;
    }

    public static class_2680 getUntransformedBlockState(class_2680 state, SchematicPlacement schematicPlacement, String subRegionName) {
        SubRegionPlacement placement = schematicPlacement.getRelativeSubRegionPlacement(subRegionName);
        if (placement != null) {
            class_2470 rotationCombined = PositionUtils.getReverseRotation(schematicPlacement.getRotation().method_10501(placement.getRotation()));
            class_2415 mirrorMain = schematicPlacement.getMirror();
            class_2415 mirrorSub = placement.getMirror();
            if (mirrorSub != class_2415.field_11302 && (schematicPlacement.getRotation() == class_2470.field_11463 || schematicPlacement.getRotation() == class_2470.field_11465)) {
                class_2415 class_24152 = mirrorSub = mirrorSub == class_2415.field_11301 ? class_2415.field_11300 : class_2415.field_11301;
            }
            if (rotationCombined != class_2470.field_11467) {
                state = state.method_26186(rotationCombined);
            }
            if (mirrorSub != class_2415.field_11302) {
                state = state.method_26185(mirrorSub);
            }
            if (mirrorMain != class_2415.field_11302) {
                state = state.method_26185(mirrorMain);
            }
        }
        return state;
    }

    public static boolean saveAreaSelectionToSchematic(AreaSelection currentSelection, class_1937 mcWorld) {
        if (currentSelection == null) {
            return false;
        }
        WorldSchematic schematicWorld = SchematicWorldHandler.getSchematicWorld();
        if (schematicWorld == null) {
            return false;
        }
        for (Box subregion : currentSelection.getAllSubRegionBoxes()) {
            class_2338 pos1 = subregion.getPos1();
            class_2338 pos2 = subregion.getPos2();
            if (pos1 == null || pos2 == null) continue;
            class_2338.class_2339 pos = new class_2338.class_2339();
            int yEnd = Math.max(pos1.method_10264(), pos2.method_10264());
            for (int y = Math.min(pos1.method_10264(), pos2.method_10264()); y <= yEnd; ++y) {
                int xEnd = Math.max(pos1.method_10263(), pos2.method_10263());
                for (int x = Math.min(pos1.method_10263(), pos2.method_10263()); x <= xEnd; ++x) {
                    int zEnd = Math.max(pos1.method_10260(), pos2.method_10260());
                    for (int z = Math.min(pos1.method_10260(), pos2.method_10260()); z <= zEnd; ++z) {
                        pos.method_10103(x, y, z);
                        class_2680 worldState = mcWorld.method_8320((class_2338)pos);
                        class_2680 schematicState = schematicWorld.method_8320((class_2338)pos);
                        if (worldState == schematicState) continue;
                        SchematicUtils.setTargetedSchematicBlockState((class_2338)pos, worldState);
                    }
                }
            }
        }
        return true;
    }

    public static class SchematicVersionCreator
    implements IStringConsumerFeedback {
        public boolean setString(String string) {
            return DataManager.getSchematicProjectsManager().commitNewVersion(string);
        }
    }

    private static class ReplacementInfo {
        public final class_2338 pos;
        public final class_2350 side;
        public final class_243 hitVec;
        public final class_2680 stateOriginal;
        public final class_2680 stateNew;

        public ReplacementInfo(class_2338 pos, class_2350 side, class_243 hitVec, class_2680 stateOriginal, class_2680 stateNew) {
            this.pos = pos;
            this.side = side;
            this.hitVec = hitVec;
            this.stateOriginal = stateOriginal;
            this.stateNew = stateNew;
        }
    }
}

