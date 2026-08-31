/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1263
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1533
 *  net.minecraft.class_1534
 *  net.minecraft.class_1535
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2281
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2350$class_2352
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_243
 *  net.minecraft.class_2470
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2745
 *  net.minecraft.class_2769
 *  net.minecraft.class_3218
 *  net.minecraft.class_3611
 *  net.minecraft.class_6757
 *  net.minecraft.class_6760
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_8113
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PasteLayerBehavior;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.ReplaceBehavior;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.class_1263;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1533;
import net.minecraft.class_1534;
import net.minecraft.class_1535;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2769;
import net.minecraft.class_3218;
import net.minecraft.class_3611;
import net.minecraft.class_6757;
import net.minecraft.class_6760;
import net.minecraft.class_7225;
import net.minecraft.class_8113;

public class SchematicPlacingUtils {
    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean placeToWorldWithinChunk(class_1937 world, class_1923 chunkPos, SchematicPlacement schematicPlacement, ReplaceBehavior replace, PasteLayerBehavior layerBehavior, boolean notifyNeighbors) {
        LitematicaSchematic schematic = schematicPlacement.getSchematic();
        Set<String> regionsTouchingChunk = schematicPlacement.getRegionsTouchingChunk(chunkPos.field_9181, chunkPos.field_9180);
        class_2338 origin = schematicPlacement.getOrigin();
        boolean allSuccess = true;
        if (world instanceof WorldSchematic && layerBehavior != PasteLayerBehavior.ALL) {
            layerBehavior = PasteLayerBehavior.ALL;
        }
        try {
            if (!notifyNeighbors) {
                WorldUtils.setShouldPreventBlockUpdates(world, true);
            }
            for (String regionName : regionsTouchingChunk) {
                Map<class_2338, class_6760<class_3611>> scheduledFluidTicks;
                Map<class_2338, class_6760<class_2248>> scheduledBlockTicks;
                LitematicaBlockStateContainer container = schematic.getSubRegionContainer(regionName);
                if (container == null) {
                    allSuccess = false;
                    continue;
                }
                SubRegionPlacement placement = schematicPlacement.getRelativeSubRegionPlacement(regionName);
                if (!placement.isEnabled()) continue;
                Map<class_2338, class_2487> blockEntityMap = schematic.getBlockEntityMapForRegion(regionName);
                if (!SchematicPlacingUtils.placeBlocksWithinChunk(world, chunkPos, regionName, container, blockEntityMap, origin, schematicPlacement, placement, scheduledBlockTicks = schematic.getScheduledBlockTicksForRegion(regionName), scheduledFluidTicks = schematic.getScheduledFluidTicksForRegion(regionName), replace, layerBehavior, notifyNeighbors)) {
                    allSuccess = false;
                    Litematica.LOGGER.warn("Invalid/missing schematic data in schematic '{}' for sub-region '{}'", (Object)schematic.getMetadata().getName(), (Object)regionName);
                }
                List<LitematicaSchematic.EntityInfo> entityList = schematic.getEntityListForRegion(regionName);
                if (schematicPlacement.ignoreEntities() || placement.ignoreEntities() || entityList == null) continue;
                SchematicPlacingUtils.placeEntitiesToWorldWithinChunk(world, chunkPos, entityList, origin, schematicPlacement, placement, layerBehavior);
            }
        }
        finally {
            WorldUtils.setShouldPreventBlockUpdates(world, false);
        }
        return allSuccess;
    }

    public static boolean placeBlocksWithinChunk(class_1937 world, class_1923 chunkPos, String regionName, LitematicaBlockStateContainer container, Map<class_2338, class_2487> blockEntityMap, class_2338 origin, SchematicPlacement schematicPlacement, SubRegionPlacement placement, @Nullable Map<class_2338, class_6760<class_2248>> scheduledBlockTicks, @Nullable Map<class_2338, class_6760<class_3611>> scheduledFluidTicks, ReplaceBehavior replace, PasteLayerBehavior layerBehavior, boolean notifyNeighbors) {
        int y;
        IntBoundingBox bounds = schematicPlacement.getBoxWithinChunkForRegion(regionName, chunkPos.field_9181, chunkPos.field_9180);
        class_2382 regionSize = schematicPlacement.getSchematic().getAreaSizeAsVec3i(regionName);
        if (bounds == null || container == null || blockEntityMap == null || regionSize == null) {
            return false;
        }
        class_2338 regionPos = placement.getPos();
        class_2338 posEndRel = new class_2338((class_2382)PositionUtils.getRelativeEndPositionFromAreaSize(regionSize)).method_10081((class_2382)regionPos);
        class_2338 posMinRel = PositionUtils.getMinCorner(regionPos, posEndRel);
        class_2338 regionPosTransformed = PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        class_2338 boxMinRel = new class_2338(bounds.minX - origin.method_10263() - regionPosTransformed.method_10263(), 0, bounds.minZ - origin.method_10260() - regionPosTransformed.method_10260());
        class_2338 boxMaxRel = new class_2338(bounds.maxX - origin.method_10263() - regionPosTransformed.method_10263(), 0, bounds.maxZ - origin.method_10260() - regionPosTransformed.method_10260());
        boxMinRel = PositionUtils.getReverseTransformedBlockPos(boxMinRel, placement.getMirror(), placement.getRotation());
        boxMaxRel = PositionUtils.getReverseTransformedBlockPos(boxMaxRel, placement.getMirror(), placement.getRotation());
        boxMinRel = PositionUtils.getReverseTransformedBlockPos(boxMinRel, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        boxMaxRel = PositionUtils.getReverseTransformedBlockPos(boxMaxRel, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        boxMinRel = boxMinRel.method_10059((class_2382)posMinRel.method_10059((class_2382)regionPos));
        boxMaxRel = boxMaxRel.method_10059((class_2382)posMinRel.method_10059((class_2382)regionPos));
        class_2338 posMin = PositionUtils.getMinCorner(boxMinRel, boxMaxRel);
        class_2338 posMax = PositionUtils.getMaxCorner(boxMinRel, boxMaxRel);
        class_2338 totalRegionPosTransformed = regionPosTransformed.method_10081((class_2382)origin);
        int startX = posMin.method_10263();
        int startZ = posMin.method_10260();
        int endX = posMax.method_10263();
        int endZ = posMax.method_10260();
        boolean startY = false;
        int endY = Math.abs(regionSize.method_10264()) - 1;
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        if (startX < 0 || startZ < 0 || endX >= container.getSize().method_10263() || endZ >= container.getSize().method_10260()) {
            System.out.printf("DEBUG ============= OUT OF BOUNDS - region: %s, sx: %d, sz: %d, ex: %d, ez: %d - size x: %d z: %d =============\n", regionName, startX, startZ, endX, endZ, container.getSize().method_10263(), container.getSize().method_10260());
            return false;
        }
        class_2470 rotationCombined = schematicPlacement.getRotation().method_10501(placement.getRotation());
        class_2415 mirrorMain = schematicPlacement.getMirror();
        class_2680 barrier = class_2246.field_10499.method_9564();
        class_2415 mirrorSub = placement.getMirror();
        boolean ignoreInventories = Configs.Generic.PASTE_IGNORE_INVENTORY.getBooleanValue();
        if (mirrorSub != class_2415.field_11302 && (schematicPlacement.getRotation() == class_2470.field_11463 || schematicPlacement.getRotation() == class_2470.field_11465)) {
            mirrorSub = mirrorSub == class_2415.field_11301 ? class_2415.field_11300 : class_2415.field_11301;
        }
        int posMinRelMinusRegX = posMinRel.method_10263() - regionPos.method_10263();
        int posMinRelMinusRegY = posMinRel.method_10264() - regionPos.method_10264();
        int posMinRelMinusRegZ = posMinRel.method_10260() - regionPos.method_10260();
        for (y = 0; y <= endY; ++y) {
            for (int z = startZ; z <= endZ; ++z) {
                for (int x = startX; x <= endX; ++x) {
                    class_2487 var43_54;
                    class_2586 te;
                    Object state = container.get(x, y, z);
                    if (state.method_26204() == class_2246.field_10369) continue;
                    posMutable.method_10103(x, y, z);
                    class_2487 class_24872 = blockEntityMap.get(posMutable);
                    var43_54 = class_24872;
                    class_2338 origPos = posMutable.method_10062();
                    posMutable.method_10103(posMinRelMinusRegX + x, posMinRelMinusRegY + y, posMinRelMinusRegZ + z);
                    class_2338 pos = PositionUtils.getTransformedPlacementPosition((class_2338)posMutable, schematicPlacement, placement);
                    pos = pos.method_10081((class_2382)totalRegionPosTransformed);
                    if (!SchematicPlacingUtils.shouldPasteBlock(pos, layerBehavior)) continue;
                    class_2680 stateOld = world.method_8320(pos);
                    if (replace == ReplaceBehavior.NONE && !stateOld.method_26215() || replace == ReplaceBehavior.WITH_NON_AIR && state.method_26215()) continue;
                    if (state.method_31709() && state.method_27852(class_2246.field_10034) && !ignoreInventories && mirrorMain != class_2415.field_11302 && state.method_11654((class_2769)class_2281.field_10770) != class_2745.field_12569 && Configs.Generic.FIX_CHEST_MIRROR.getBooleanValue()) {
                        class_2350 facing = (class_2350)state.method_11654((class_2769)class_2281.field_10768);
                        class_2350.class_2351 axis = facing.method_10166();
                        class_2745 type = ((class_2745)state.method_11654((class_2769)class_2281.field_10770)).method_11824();
                        if (mirrorMain != class_2415.field_11302 && axis != class_2350.class_2351.field_11052) {
                            class_2350 facingAdj = type == class_2745.field_12574 ? facing.method_35834(class_2350.class_2351.field_11052) : facing.method_35833(class_2350.class_2351.field_11052);
                            class_2338 posAdj = origPos.method_10093(facingAdj);
                            class_2487 class_24873 = blockEntityMap.getOrDefault(posAdj, class_24872).method_10553();
                        }
                    }
                    if (mirrorMain != class_2415.field_11302) {
                        state = state.method_26185(mirrorMain);
                    }
                    if (mirrorSub != class_2415.field_11302) {
                        state = state.method_26185(mirrorSub);
                    }
                    if (rotationCombined != class_2470.field_11467) {
                        state = state.method_26186(rotationCombined);
                    }
                    if ((te = world.method_8321(pos)) != null) {
                        if (te instanceof class_1263) {
                            ((class_1263)te).method_5448();
                        }
                        world.method_8652(pos, barrier, 20);
                    }
                    if (!world.method_8652(pos, (class_2680)state, 18) || var43_54 == null || (te = world.method_8321(pos)) == null) continue;
                    class_2487 class_24874 = var43_54.method_10553();
                    class_24874.method_10569("x", pos.method_10263());
                    class_24874.method_10569("y", pos.method_10264());
                    class_24874.method_10569("z", pos.method_10260());
                    if (ignoreInventories) {
                        class_24874.method_10551("Items");
                    }
                    try {
                        te.method_58690(class_24874, (class_7225.class_7874)world.method_30349());
                        if (!ignoreInventories || !(te instanceof class_1263)) continue;
                        ((class_1263)te).method_5448();
                        continue;
                    }
                    catch (Exception e) {
                        Litematica.LOGGER.warn("Failed to load BlockEntity data for {} @ {}", state, (Object)pos);
                    }
                }
            }
        }
        if (world instanceof class_3218) {
            class_6760 tick;
            class_2338 pos;
            class_3218 serverWorld = (class_3218)world;
            IntBoundingBox box = new IntBoundingBox(startX, 0, startZ, endX, endY, endZ);
            if (scheduledBlockTicks != null && !scheduledBlockTicks.isEmpty()) {
                class_6757 scheduler = serverWorld.method_14196();
                for (Map.Entry entry : scheduledBlockTicks.entrySet()) {
                    pos = (class_2338)entry.getKey();
                    if (!box.containsPos((class_2382)pos)) continue;
                    posMutable.method_10103(posMinRelMinusRegX + pos.method_10263(), posMinRelMinusRegY + pos.method_10264(), posMinRelMinusRegZ + pos.method_10260());
                    pos = PositionUtils.getTransformedPlacementPosition((class_2338)posMutable, schematicPlacement, placement);
                    pos = pos.method_10081((class_2382)totalRegionPosTransformed);
                    tick = (class_6760)entry.getValue();
                    if (world.method_8320(pos).method_26204() != tick.comp_252()) continue;
                    scheduler.method_39363(new class_6760((Object)((class_2248)tick.comp_252()), pos, tick.comp_254(), tick.comp_255(), tick.comp_256()));
                }
            }
            if (scheduledFluidTicks != null && !scheduledFluidTicks.isEmpty()) {
                class_6757 scheduler = serverWorld.method_14179();
                for (Map.Entry entry : scheduledFluidTicks.entrySet()) {
                    pos = (class_2338)entry.getKey();
                    if (!box.containsPos((class_2382)pos)) continue;
                    posMutable.method_10103(posMinRelMinusRegX + pos.method_10263(), posMinRelMinusRegY + pos.method_10264(), posMinRelMinusRegZ + pos.method_10260());
                    pos = PositionUtils.getTransformedPlacementPosition((class_2338)posMutable, schematicPlacement, placement);
                    pos = pos.method_10081((class_2382)totalRegionPosTransformed);
                    tick = (class_6760)entry.getValue();
                    if (world.method_8320(pos).method_26227().method_15772() != tick.comp_252()) continue;
                    scheduler.method_39363(new class_6760((Object)((class_3611)tick.comp_252()), pos, tick.comp_254(), tick.comp_255(), tick.comp_256()));
                }
            }
        }
        if (notifyNeighbors) {
            for (y = 0; y <= endY; ++y) {
                for (int z = startZ; z <= endZ; ++z) {
                    for (int x = startX; x <= endX; ++x) {
                        posMutable.method_10103(posMinRelMinusRegX + x, posMinRelMinusRegY + y, posMinRelMinusRegZ + z);
                        class_2338 pos = PositionUtils.getTransformedPlacementPosition((class_2338)posMutable, schematicPlacement, placement);
                        pos = pos.method_10081((class_2382)totalRegionPosTransformed);
                        world.method_8408(pos, world.method_8320(pos).method_26204());
                    }
                }
            }
        }
        return true;
    }

    private static void dumpBlockEntityMap(Map<class_2338, class_2487> teMap) {
        System.out.print("DUMP TE-MAP:\n");
        for (class_2338 pos : teMap.keySet()) {
            class_2487 nbt = teMap.get(pos);
            System.out.printf("  pos[%s]: %s\n", pos.method_23854(), nbt.toString());
        }
        System.out.print("DUMP TE-MAP -- END\n");
    }

    public static void placeEntitiesToWorldWithinChunk(class_1937 world, class_1923 chunkPos, List<LitematicaSchematic.EntityInfo> entityList, class_2338 origin, SchematicPlacement schematicPlacement, SubRegionPlacement placement, PasteLayerBehavior layerBehavior) {
        class_2338 regionPos = placement.getPos();
        if (entityList == null) {
            return;
        }
        class_2338 regionPosRelTransformed = PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        int offX = regionPosRelTransformed.method_10263() + origin.method_10263();
        int offY = regionPosRelTransformed.method_10264() + origin.method_10264();
        int offZ = regionPosRelTransformed.method_10260() + origin.method_10260();
        double minX = chunkPos.field_9181 << 4;
        double minZ = chunkPos.field_9180 << 4;
        double maxX = (chunkPos.field_9181 << 4) + 16;
        double maxZ = (chunkPos.field_9180 << 4) + 16;
        class_2470 rotationCombined = schematicPlacement.getRotation().method_10501(placement.getRotation());
        class_2415 mirrorMain = schematicPlacement.getMirror();
        class_2415 mirrorSub = placement.getMirror();
        if (mirrorSub != class_2415.field_11302 && (schematicPlacement.getRotation() == class_2470.field_11463 || schematicPlacement.getRotation() == class_2470.field_11465)) {
            mirrorSub = mirrorSub == class_2415.field_11301 ? class_2415.field_11300 : class_2415.field_11301;
        }
        for (LitematicaSchematic.EntityInfo info : entityList) {
            class_1533 frameEntity;
            class_1309 living;
            class_243 pos = info.posVec;
            pos = PositionUtils.getTransformedPosition(pos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
            pos = PositionUtils.getTransformedPosition(pos, placement.getMirror(), placement.getRotation());
            double x = pos.field_1352 + (double)offX;
            double y = pos.field_1351 + (double)offY;
            double z = pos.field_1350 + (double)offZ;
            float[] origRot = new float[2];
            if (!SchematicPlacingUtils.shouldPasteEntity(new class_243(x, y, z), layerBehavior) || !(x >= minX) || !(x < maxX) || !(z >= minZ) || !(z < maxZ)) continue;
            class_2487 tag = info.nbt.method_10553();
            String id = tag.method_10558("id");
            if (id.equals("minecraft:glow_item_frame") || id.equals("minecraft:item_frame") || id.equals("minecraft:leash_knot") || id.equals("minecraft:painting")) {
                class_243 p = NbtUtils.readEntityPositionFromTag((class_2487)tag);
                if (p == null) {
                    p = new class_243(x, y, z);
                    NbtUtils.writeEntityPositionToTag((class_243)p, (class_2487)tag);
                }
                tag.method_10569("TileX", (int)p.field_1352);
                tag.method_10569("TileY", (int)p.field_1351);
                tag.method_10569("TileZ", (int)p.field_1350);
            }
            class_2499 rotation = tag.method_10554("Rotation", 5);
            origRot[0] = rotation.method_10604(0);
            origRot[1] = rotation.method_10604(1);
            class_1297 entity = EntityUtils.createEntityAndPassengersFromNBT(tag, world);
            if (entity == null) continue;
            SchematicPlacingUtils.rotateEntity(entity, x, y, z, rotationCombined, mirrorMain, mirrorSub);
            if (entity instanceof class_1309 && (living = (class_1309)entity).method_6113()) {
                living.method_18402(class_2338.method_49637((double)x, (double)y, (double)z));
            }
            if (entity instanceof class_1534) {
                class_1534 paintingEntity = (class_1534)entity;
                class_2350 right = paintingEntity.method_5735().method_10160();
                if (((class_1535)paintingEntity.method_43404().comp_349()).comp_2670() % 2 == 0 && right.method_10171() == class_2350.class_2352.field_11056) {
                    x -= 1.0 * (double)right.method_10148();
                    z -= 1.0 * (double)right.method_10165();
                }
                if (((class_1535)paintingEntity.method_43404().comp_349()).comp_2671() % 2 == 0) {
                    y -= 1.0;
                }
                entity.method_5814(x, y, z);
            }
            if (entity instanceof class_1533 && (frameEntity = (class_1533)entity).method_36454() != origRot[0] && (frameEntity.method_36455() == 90.0f || frameEntity.method_36455() == -90.0f)) {
                frameEntity.method_36456(origRot[0]);
            }
            EntityUtils.spawnEntityAndPassengersInWorld(entity, world);
            if (!(entity instanceof class_8113)) continue;
            entity.method_5773();
        }
    }

    public static void rotateEntity(class_1297 entity, double x, double y, double z, class_2470 rotationCombined, class_2415 mirrorMain, class_2415 mirrorSub) {
        float rotationYaw = entity.method_36454();
        if (mirrorMain != class_2415.field_11302) {
            rotationYaw = entity.method_5763(mirrorMain);
        }
        if (mirrorSub != class_2415.field_11302) {
            rotationYaw = entity.method_5763(mirrorSub);
        }
        if (rotationCombined != class_2470.field_11467) {
            rotationYaw += entity.method_36454() - entity.method_5832(rotationCombined);
        }
        entity.method_5808(x, y, z, rotationYaw, entity.method_36455());
        EntityUtils.setEntityRotations(entity, rotationYaw, entity.method_36455());
    }

    public static boolean shouldPasteBlock(class_2338 pos, PasteLayerBehavior layerBehavior) {
        if (layerBehavior == PasteLayerBehavior.ALL) {
            return true;
        }
        return DataManager.getRenderLayerRange().isPositionWithinRange(pos);
    }

    public static boolean shouldPasteEntity(class_243 pos, PasteLayerBehavior layerBehavior) {
        if (layerBehavior == PasteLayerBehavior.ALL) {
            return true;
        }
        return DataManager.getRenderLayerRange().isPositionWithinRange((int)pos.method_10216(), (int)pos.method_10214(), (int)pos.method_10215());
    }
}
