/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.mojang.serialization.DynamicOps
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.Schema
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1263
 *  net.minecraft.class_1297
 *  net.minecraft.class_1530
 *  net.minecraft.class_155
 *  net.minecraft.class_1922
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_1953
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2378
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_243
 *  net.minecraft.class_2470
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2501
 *  net.minecraft.class_2507
 *  net.minecraft.class_2509
 *  net.minecraft.class_2512
 *  net.minecraft.class_2520
 *  net.minecraft.class_2577
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3218
 *  net.minecraft.class_3481
 *  net.minecraft.class_3532
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_4076
 *  net.minecraft.class_6755
 *  net.minecraft.class_6760
 *  net.minecraft.class_6880$class_6883
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7871
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.schematic;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.DynamicOps;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.mixin.world.IMixinWorldTickScheduler;
import fi.dy.masa.litematica.network.ServuxLitematicaHandler;
import fi.dy.masa.litematica.network.ServuxLitematicaPacket;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.SchematicSchema;
import fi.dy.masa.litematica.schematic.SchematicaSchematic;
import fi.dy.masa.litematica.schematic.container.ILitematicaBlockStatePalette;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionFixers;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import fi.dy.masa.litematica.schematic.conversion.SchematicConverter;
import fi.dy.masa.litematica.schematic.conversion.SchematicDowngradeConverter;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.schematic.transmit.SchematicBufferManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.BlockUtils;
import fi.dy.masa.litematica.util.DataFixerMode;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.ReplaceBehavior;
import fi.dy.masa.litematica.util.SchematicPlacingUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.Schema;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1263;
import net.minecraft.class_1297;
import net.minecraft.class_1530;
import net.minecraft.class_155;
import net.minecraft.class_1922;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_1953;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2378;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2501;
import net.minecraft.class_2507;
import net.minecraft.class_2509;
import net.minecraft.class_2512;
import net.minecraft.class_2520;
import net.minecraft.class_2577;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_3481;
import net.minecraft.class_3532;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_4076;
import net.minecraft.class_6755;
import net.minecraft.class_6760;
import net.minecraft.class_6880;
import net.minecraft.class_7225;
import net.minecraft.class_7871;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import org.apache.commons.lang3.tuple.Pair;

public class LitematicaSchematic {
    public static final String FILE_EXTENSION = ".litematic";
    public static final int SCHEMATIC_VERSION_1_13_2 = 5;
    public static final int MINECRAFT_DATA_VERSION_1_12 = 1139;
    public static final int MINECRAFT_DATA_VERSION_1_13_2 = 1631;
    public static final int MINECRAFT_DATA_VERSION_1_20_4 = 3700;
    public static final int MINECRAFT_DATA_VERSION = class_155.method_16673().method_37912().method_38494();
    public static final int SCHEMATIC_VERSION = 7;
    public static final int SCHEMATIC_VERSION_SUB = 1;
    private final Map<String, LitematicaBlockStateContainer> blockContainers = new HashMap<String, LitematicaBlockStateContainer>();
    private final Map<String, Map<class_2338, class_2487>> tileEntities = new HashMap<String, Map<class_2338, class_2487>>();
    private final Map<String, Map<class_2338, class_6760<class_2248>>> pendingBlockTicks = new HashMap<String, Map<class_2338, class_6760<class_2248>>>();
    private final Map<String, Map<class_2338, class_6760<class_3611>>> pendingFluidTicks = new HashMap<String, Map<class_2338, class_6760<class_3611>>>();
    private final Map<String, List<EntityInfo>> entities = new HashMap<String, List<EntityInfo>>();
    private final Map<String, class_2338> subRegionPositions = new HashMap<String, class_2338>();
    private final Map<String, class_2338> subRegionSizes = new HashMap<String, class_2338>();
    private final SchematicMetadata metadata = new SchematicMetadata();
    private final SchematicConverter converter;
    private int totalBlocksReadFromWorld;
    @Nullable
    private final File schematicFile;
    private final FileType schematicType;

    public LitematicaSchematic(Path file, class_2487 nbt, FileType type) {
        this.readFromNBT(nbt);
        this.schematicFile = file.toFile();
        this.schematicType = type;
        this.converter = SchematicConverter.createForLitematica();
    }

    private LitematicaSchematic(@Nullable File file) {
        this(file, FileType.LITEMATICA_SCHEMATIC);
    }

    private LitematicaSchematic(@Nullable File file, FileType schematicType) {
        this.schematicFile = file;
        this.schematicType = schematicType;
        this.converter = SchematicConverter.createForLitematica();
    }

    private LitematicaSchematic(@Nullable Path file) {
        this(file, FileType.LITEMATICA_SCHEMATIC);
    }

    private LitematicaSchematic(@Nullable Path file, FileType schematicType) {
        this.schematicFile = file.toFile();
        this.schematicType = schematicType;
        this.converter = SchematicConverter.createForLitematica();
    }

    @Nullable
    public File getFile() {
        return this.schematicFile;
    }

    public class_2382 getTotalSize() {
        return this.metadata.getEnclosingSize();
    }

    public int getTotalBlocksReadFromWorld() {
        return this.totalBlocksReadFromWorld;
    }

    public SchematicMetadata getMetadata() {
        return this.metadata;
    }

    public int getSubRegionCount() {
        return this.blockContainers.size();
    }

    @Nullable
    public class_2338 getSubRegionPosition(String areaName) {
        return this.subRegionPositions.get(areaName);
    }

    public Map<String, class_2338> getAreaPositions() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String name : this.subRegionPositions.keySet()) {
            class_2338 pos = this.subRegionPositions.get(name);
            builder.put((Object)name, (Object)pos);
        }
        return builder.build();
    }

    public Map<String, class_2338> getAreaSizes() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String name : this.subRegionSizes.keySet()) {
            class_2338 pos = this.subRegionSizes.get(name);
            builder.put((Object)name, (Object)pos);
        }
        return builder.build();
    }

    @Nullable
    public class_2338 getAreaSize(String regionName) {
        return this.subRegionSizes.get(regionName);
    }

    @Nullable
    public class_2382 getAreaSizeAsVec3i(String regionName) {
        return (class_2382)this.subRegionSizes.get(regionName);
    }

    public Map<String, Box> getAreas() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (String name : this.subRegionPositions.keySet()) {
            class_2338 pos = this.subRegionPositions.get(name);
            class_2338 posEndRel = fi.dy.masa.litematica.util.PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)this.subRegionSizes.get(name));
            Box box = new Box(pos, pos.method_10081((class_2382)posEndRel), name);
            builder.put((Object)name, (Object)box);
        }
        return builder.build();
    }

    @Nullable
    public static LitematicaSchematic createFromWorld(class_1937 world, AreaSelection area, SchematicSaveInfo info, String author, IStringConsumer feedback) {
        List<Box> boxes = fi.dy.masa.litematica.util.PositionUtils.getValidBoxes(area);
        if (boxes.isEmpty()) {
            feedback.setString(StringUtils.translate((String)"litematica.error.schematic.create.no_selections", (Object[])new Object[0]));
            return null;
        }
        LitematicaSchematic schematic = new LitematicaSchematic((File)null);
        long time = System.currentTimeMillis();
        class_2338 origin = area.getEffectiveOrigin();
        schematic.setSubRegionPositions(boxes, origin);
        schematic.setSubRegionSizes(boxes);
        schematic.takeBlocksFromWorld(world, boxes, info);
        if (!info.ignoreEntities) {
            schematic.takeEntitiesFromWorld(world, boxes, origin);
        }
        schematic.metadata.setAuthor(author);
        schematic.metadata.setName(area.getName());
        schematic.metadata.setTimeCreated(time);
        schematic.metadata.setTimeModified(time);
        schematic.metadata.setRegionCount(boxes.size());
        schematic.metadata.setTotalVolume(fi.dy.masa.litematica.util.PositionUtils.getTotalVolume(boxes));
        schematic.metadata.setEnclosingSize(fi.dy.masa.litematica.util.PositionUtils.getEnclosingAreaSize(boxes));
        schematic.metadata.setTotalBlocks(schematic.totalBlocksReadFromWorld);
        schematic.metadata.setSchematicVersion(7);
        schematic.metadata.setMinecraftDataVersion(MINECRAFT_DATA_VERSION);
        schematic.metadata.setFileType(FileType.LITEMATICA_SCHEMATIC);
        return schematic;
    }

    public static LitematicaSchematic createEmptySchematic(AreaSelection area, String author) {
        List<Box> boxes = fi.dy.masa.litematica.util.PositionUtils.getValidBoxes(area);
        if (boxes.isEmpty()) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)StringUtils.translate((String)"litematica.error.schematic.create.no_selections", (Object[])new Object[0]), (Object[])new Object[0]);
            return null;
        }
        LitematicaSchematic schematic = new LitematicaSchematic((File)null);
        schematic.setSubRegionPositions(boxes, area.getEffectiveOrigin());
        schematic.setSubRegionSizes(boxes);
        schematic.metadata.setAuthor(author);
        schematic.metadata.setName(area.getName());
        schematic.metadata.setRegionCount(boxes.size());
        schematic.metadata.setTotalVolume(fi.dy.masa.litematica.util.PositionUtils.getTotalVolume(boxes));
        schematic.metadata.setEnclosingSize(fi.dy.masa.litematica.util.PositionUtils.getEnclosingAreaSize(boxes));
        schematic.metadata.setSchematicVersion(7);
        schematic.metadata.setMinecraftDataVersion(MINECRAFT_DATA_VERSION);
        schematic.metadata.setFileType(FileType.LITEMATICA_SCHEMATIC);
        for (Box box : boxes) {
            String regionName = box.getName();
            class_2338 size = box.getSize();
            int sizeX = Math.abs(size.method_10263());
            int sizeY = Math.abs(size.method_10264());
            int sizeZ = Math.abs(size.method_10260());
            LitematicaBlockStateContainer container = new LitematicaBlockStateContainer(sizeX, sizeY, sizeZ);
            schematic.blockContainers.put(regionName, container);
            schematic.tileEntities.put(regionName, new HashMap());
            schematic.entities.put(regionName, new ArrayList());
            schematic.pendingBlockTicks.put(regionName, new HashMap());
            schematic.pendingFluidTicks.put(regionName, new HashMap());
        }
        return schematic;
    }

    public static LitematicaSchematic createEmptySchematicFromExisting(@Nonnull LitematicaSchematic existing, String newAuthor) {
        LitematicaSchematic newSchematic = new LitematicaSchematic((File)null, existing.schematicType);
        if (!newAuthor.isEmpty()) {
            newSchematic.metadata.setAuthor(newAuthor);
        } else {
            newSchematic.metadata.setAuthor(existing.getMetadata().getAuthor());
        }
        newSchematic.metadata.setName(existing.getMetadata().getName());
        newSchematic.metadata.setDescription(existing.getMetadata().getDescription());
        newSchematic.metadata.setTimeCreated(existing.getMetadata().getTimeCreated());
        newSchematic.metadata.setTimeModifiedToNow();
        newSchematic.metadata.setRegionCount(existing.getMetadata().getRegionCount());
        newSchematic.metadata.setTotalVolume(existing.getMetadata().getTotalVolume());
        newSchematic.metadata.setTotalBlocks(existing.getMetadata().getTotalBlocks());
        newSchematic.metadata.setEnclosingSize(existing.getMetadata().getEnclosingSize());
        newSchematic.metadata.setSchematicVersion(existing.getMetadata().getSchematicVersion());
        newSchematic.metadata.setMinecraftDataVersion(existing.getMetadata().getMinecraftDataVersion());
        newSchematic.metadata.setFileType(existing.getMetadata().getFileType());
        return newSchematic;
    }

    public boolean downgradeV7toV6Schematic(LitematicaSchematic v7Schematic) {
        Map<String, Box> areas = v7Schematic.getAreas();
        for (Box box : areas.values()) {
            String regionName = box.getName();
            class_2338 size = box.getSize();
            int sizeX = Math.abs(size.method_10263());
            int sizeY = Math.abs(size.method_10264());
            int sizeZ = Math.abs(size.method_10260());
            this.blockContainers.put(regionName, v7Schematic.blockContainers.get(regionName));
            this.tileEntities.put(regionName, this.downgradeTileEntities_to_1_20_4(v7Schematic.tileEntities.get(regionName), MINECRAFT_DATA_VERSION));
            class_2499 list = this.writeEntitiesToNBT(v7Schematic.entities.get(regionName));
            list = this.downgradeEntities_to_1_20_4(list, MINECRAFT_DATA_VERSION);
            this.entities.put(regionName, this.readEntitiesFromNBT(list));
            this.pendingBlockTicks.put(regionName, v7Schematic.pendingBlockTicks.get(regionName));
            this.pendingFluidTicks.put(regionName, v7Schematic.pendingFluidTicks.get(regionName));
            this.subRegionPositions.put(regionName, v7Schematic.subRegionPositions.get(regionName));
            this.subRegionSizes.put(regionName, v7Schematic.subRegionSizes.get(regionName));
        }
        return false;
    }

    public void takeEntityDataFromSchematicaSchematic(SchematicaSchematic schematic, String subRegionName) {
        this.tileEntities.put(subRegionName, schematic.getTiles());
        this.entities.put(subRegionName, schematic.getEntities());
    }

    public boolean placeToWorld(class_1937 world, SchematicPlacement schematicPlacement, boolean notifyNeighbors) {
        return this.placeToWorld(world, schematicPlacement, notifyNeighbors, false);
    }

    public boolean placeToWorld(class_1937 world, SchematicPlacement schematicPlacement, boolean notifyNeighbors, boolean ignoreEntities) {
        WorldUtils.setShouldPreventBlockUpdates(world, true);
        ImmutableMap<String, SubRegionPlacement> relativePlacements = schematicPlacement.getEnabledRelativeSubRegionPlacements();
        class_2338 origin = schematicPlacement.getOrigin();
        for (String regionName : relativePlacements.keySet()) {
            SubRegionPlacement placement = (SubRegionPlacement)relativePlacements.get((Object)regionName);
            if (!placement.isEnabled()) continue;
            class_2338 regionPos = placement.getPos();
            class_2338 regionSize = this.subRegionSizes.get(regionName);
            LitematicaBlockStateContainer container = this.blockContainers.get(regionName);
            Map<class_2338, class_2487> tileMap = this.tileEntities.get(regionName);
            List<EntityInfo> entityList = this.entities.get(regionName);
            Map<class_2338, class_6760<class_2248>> scheduledBlockTicks = this.pendingBlockTicks.get(regionName);
            Map<class_2338, class_6760<class_3611>> scheduledFluidTicks = this.pendingFluidTicks.get(regionName);
            if (regionPos != null && regionSize != null && container != null && tileMap != null) {
                this.placeBlocksToWorld(world, origin, regionPos, regionSize, schematicPlacement, placement, container, tileMap, scheduledBlockTicks, scheduledFluidTicks, notifyNeighbors);
            } else {
                Litematica.LOGGER.warn("Invalid/missing schematic data in schematic '{}' for sub-region '{}'", (Object)this.metadata.getName(), (Object)regionName);
            }
            if (ignoreEntities || schematicPlacement.ignoreEntities() || placement.ignoreEntities() || entityList == null) continue;
            this.placeEntitiesToWorld(world, origin, regionPos, regionSize, schematicPlacement, placement, entityList);
        }
        WorldUtils.setShouldPreventBlockUpdates(world, false);
        return true;
    }

    private boolean placeBlocksToWorld(class_1937 world, class_2338 origin, class_2338 regionPos, class_2338 regionSize, SchematicPlacement schematicPlacement, SubRegionPlacement placement, LitematicaBlockStateContainer container, Map<class_2338, class_2487> tileMap, @Nullable Map<class_2338, class_6760<class_2248>> scheduledBlockTicks, @Nullable Map<class_2338, class_6760<class_3611>> scheduledFluidTicks, boolean notifyNeighbors) {
        class_2338 posEndRelSub = fi.dy.masa.litematica.util.PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)regionSize);
        class_2338 posEndRel = posEndRelSub.method_10081((class_2382)regionPos);
        class_2338 posMinRel = fi.dy.masa.litematica.util.PositionUtils.getMinCorner(regionPos, posEndRel);
        class_2338 regionPosTransformed = fi.dy.masa.litematica.util.PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        class_2338 regionPosAbs = regionPosTransformed.method_10081((class_2382)origin);
        int sizeX = Math.abs(regionSize.method_10263());
        int sizeY = Math.abs(regionSize.method_10264());
        int sizeZ = Math.abs(regionSize.method_10260());
        class_2680 barrier = class_2246.field_10499.method_9564();
        boolean ignoreInventories = Configs.Generic.PASTE_IGNORE_INVENTORY.getBooleanValue();
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        ReplaceBehavior replace = (ReplaceBehavior)Configs.Generic.PASTE_REPLACE_BEHAVIOR.getOptionListValue();
        class_2470 rotationCombined = schematicPlacement.getRotation().method_10501(placement.getRotation());
        class_2415 mirrorMain = schematicPlacement.getMirror();
        class_2415 mirrorSub = placement.getMirror();
        if (mirrorSub != class_2415.field_11302 && (schematicPlacement.getRotation() == class_2470.field_11463 || schematicPlacement.getRotation() == class_2470.field_11465)) {
            mirrorSub = mirrorSub == class_2415.field_11301 ? class_2415.field_11300 : class_2415.field_11301;
        }
        int bottomY = world.method_31607();
        int topY = world.method_31600() + 1;
        int tmp = posMinRel.method_10264() - regionPos.method_10264() + regionPosTransformed.method_10264() + origin.method_10264();
        int startY = 0;
        int endY = sizeY;
        if (tmp < bottomY) {
            startY += bottomY - tmp;
        }
        if ((tmp = posMinRel.method_10264() - regionPos.method_10264() + regionPosTransformed.method_10264() + origin.method_10264() + (endY - 1)) > topY) {
            endY -= tmp - topY;
        }
        for (int y = startY; y < endY; ++y) {
            for (int z = 0; z < sizeZ; ++z) {
                for (int x = 0; x < sizeX; ++x) {
                    class_2586 te;
                    class_2680 state = container.get(x, y, z);
                    if (state.method_26204() == class_2246.field_10369) continue;
                    posMutable.method_10103(x, y, z);
                    class_2487 teNBT = tileMap.get(posMutable);
                    posMutable.method_10103(posMinRel.method_10263() + x - regionPos.method_10263(), posMinRel.method_10264() + y - regionPos.method_10264(), posMinRel.method_10260() + z - regionPos.method_10260());
                    class_2338 pos = fi.dy.masa.litematica.util.PositionUtils.getTransformedPlacementPosition((class_2338)posMutable, schematicPlacement, placement);
                    pos = pos.method_10081((class_2382)regionPosTransformed).method_10081((class_2382)origin);
                    class_2680 stateOld = world.method_8320(pos);
                    if (replace == ReplaceBehavior.NONE && !stateOld.method_26215() || replace == ReplaceBehavior.WITH_NON_AIR && state.method_26215()) continue;
                    if (mirrorMain != class_2415.field_11302) {
                        state = state.method_26185(mirrorMain);
                    }
                    if (mirrorSub != class_2415.field_11302) {
                        state = state.method_26185(mirrorSub);
                    }
                    if (rotationCombined != class_2470.field_11467) {
                        state = state.method_26186(rotationCombined);
                    }
                    if (stateOld == state && !state.method_31709()) continue;
                    class_2586 teOld = world.method_8321(pos);
                    if (teOld != null) {
                        if (teOld instanceof class_1263) {
                            ((class_1263)teOld).method_5448();
                        }
                        world.method_8652(pos, barrier, 20);
                    }
                    if (!world.method_8652(pos, state, 18) || teNBT == null || (te = world.method_8321(pos)) == null) continue;
                    teNBT = teNBT.method_10553();
                    teNBT.method_10569("x", pos.method_10263());
                    teNBT.method_10569("y", pos.method_10264());
                    teNBT.method_10569("z", pos.method_10260());
                    if (ignoreInventories) {
                        teNBT.method_10551("Items");
                    }
                    try {
                        te.method_58690(teNBT, (class_7225.class_7874)world.method_30349());
                        if (!ignoreInventories || !(te instanceof class_1263)) continue;
                        ((class_1263)te).method_5448();
                        continue;
                    }
                    catch (Exception e) {
                        Litematica.LOGGER.warn("Failed to load TileEntity data for {} @ {}", (Object)state, (Object)pos);
                    }
                }
            }
        }
        return true;
    }

    private void placeEntitiesToWorld(class_1937 world, class_2338 origin, class_2338 regionPos, class_2338 regionSize, SchematicPlacement schematicPlacement, SubRegionPlacement placement, List<EntityInfo> entityList) {
        class_2338 regionPosRelTransformed = fi.dy.masa.litematica.util.PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        int offX = regionPosRelTransformed.method_10263() + origin.method_10263();
        int offY = regionPosRelTransformed.method_10264() + origin.method_10264();
        int offZ = regionPosRelTransformed.method_10260() + origin.method_10260();
        class_2470 rotationCombined = schematicPlacement.getRotation().method_10501(placement.getRotation());
        class_2415 mirrorMain = schematicPlacement.getMirror();
        class_2415 mirrorSub = placement.getMirror();
        if (mirrorSub != class_2415.field_11302 && (schematicPlacement.getRotation() == class_2470.field_11463 || schematicPlacement.getRotation() == class_2470.field_11465)) {
            mirrorSub = mirrorSub == class_2415.field_11301 ? class_2415.field_11300 : class_2415.field_11301;
        }
        for (EntityInfo info : entityList) {
            class_1297 entity = EntityUtils.createEntityAndPassengersFromNBT(info.nbt, world);
            if (entity == null) continue;
            class_243 pos = info.posVec;
            pos = fi.dy.masa.litematica.util.PositionUtils.getTransformedPosition(pos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
            pos = fi.dy.masa.litematica.util.PositionUtils.getTransformedPosition(pos, placement.getMirror(), placement.getRotation());
            double x = pos.field_1352 + (double)offX;
            double y = pos.field_1351 + (double)offY;
            double z = pos.field_1350 + (double)offZ;
            SchematicPlacingUtils.rotateEntity(entity, x, y, z, rotationCombined, mirrorMain, mirrorSub);
            EntityUtils.spawnEntityAndPassengersInWorld(entity, world);
        }
    }

    private void takeEntitiesFromWorld(class_1937 world, List<Box> boxes, class_2338 origin) {
        for (Box box : boxes) {
            class_238 bb = fi.dy.masa.litematica.util.PositionUtils.createEnclosingAABB(box.getPos1(), box.getPos2());
            class_2338 regionPosAbs = box.getPos1();
            ArrayList<EntityInfo> list = new ArrayList<EntityInfo>();
            List entities = world.method_8333(null, bb, EntityUtils.NOT_PLAYER);
            for (class_1297 entity : entities) {
                class_2487 tag;
                if (!entity.method_5662(tag = new class_2487())) continue;
                class_243 posVec = new class_243(entity.method_23317() - (double)regionPosAbs.method_10263(), entity.method_23318() - (double)regionPosAbs.method_10264(), entity.method_23321() - (double)regionPosAbs.method_10260());
                NbtUtils.writeEntityPositionToTag((class_243)posVec, (class_2487)tag);
                list.add(new EntityInfo(posVec, tag));
            }
            this.entities.put(box.getName(), list);
        }
    }

    public void takeEntitiesFromWorldWithinChunk(class_1937 world, int chunkX, int chunkZ, ImmutableMap<String, IntBoundingBox> volumes, ImmutableMap<String, Box> boxes, Set<UUID> existingEntities, class_2338 origin) {
        for (Map.Entry entry : volumes.entrySet()) {
            String regionName = (String)entry.getKey();
            List<EntityInfo> list = this.entities.get(regionName);
            Box box = (Box)boxes.get((Object)regionName);
            if (box == null || list == null) continue;
            class_238 bb = fi.dy.masa.litematica.util.PositionUtils.createAABBFrom((IntBoundingBox)entry.getValue());
            List entities = world.method_8333(null, bb, EntityUtils.NOT_PLAYER);
            class_2338 regionPosAbs = box.getPos1();
            for (class_1297 entity : entities) {
                UUID uuid = entity.method_5667();
                if (existingEntities.contains(uuid)) continue;
                class_2487 tag = new class_2487();
                if (EntitiesDataStorage.getInstance().hasServuxServer()) {
                    class_2487 serverTags = EntitiesDataStorage.getInstance().getFromEntityCacheNbt(entity.method_5628());
                    if (serverTags != null && !serverTags.method_33133()) {
                        tag.method_10543(serverTags);
                    }
                } else {
                    entity.method_5662(tag);
                }
                if (tag.method_33133()) continue;
                class_243 posVec = new class_243(entity.method_23317() - (double)regionPosAbs.method_10263(), entity.method_23318() - (double)regionPosAbs.method_10264(), entity.method_23321() - (double)regionPosAbs.method_10260());
                if (entity instanceof class_1530) {
                    class_1530 decorationEntity = (class_1530)entity;
                    class_2338 p = decorationEntity.method_24515();
                    tag.method_10569("TileX", p.method_10263() - regionPosAbs.method_10263());
                    tag.method_10569("TileY", p.method_10264() - regionPosAbs.method_10264());
                    tag.method_10569("TileZ", p.method_10260() - regionPosAbs.method_10260());
                }
                NbtUtils.writeEntityPositionToTag((class_243)posVec, (class_2487)tag);
                list.add(new EntityInfo(posVec, tag));
                existingEntities.add(uuid);
            }
        }
    }

    private void takeBlocksFromWorld(class_1937 world, List<Box> boxes, SchematicSaveInfo info) {
        class_2338.class_2339 posMutable = new class_2338.class_2339(0, 0, 0);
        for (Box box : boxes) {
            class_2338 size = box.getSize();
            int sizeX = Math.abs(size.method_10263());
            int sizeY = Math.abs(size.method_10264());
            int sizeZ = Math.abs(size.method_10260());
            LitematicaBlockStateContainer container = new LitematicaBlockStateContainer(sizeX, sizeY, sizeZ);
            HashMap<class_2338, class_2487> tileEntityMap = new HashMap<class_2338, class_2487>();
            HashMap blockTickMap = new HashMap();
            HashMap fluidTickMap = new HashMap();
            class_2338 minCorner = fi.dy.masa.litematica.util.PositionUtils.getMinCorner(box.getPos1(), box.getPos2());
            int startX = minCorner.method_10263();
            int startY = minCorner.method_10264();
            int startZ = minCorner.method_10260();
            boolean visibleOnly = info.visibleOnly;
            boolean includeSupport = info.includeSupportBlocks;
            for (int y = 0; y < sizeY; ++y) {
                for (int z = 0; z < sizeZ; ++z) {
                    for (int x = 0; x < sizeX; ++x) {
                        class_2586 te;
                        posMutable.method_10103(x + startX, y + startY, z + startZ);
                        if (visibleOnly && !LitematicaSchematic.isExposed(world, (class_2338)posMutable) && (!includeSupport || !LitematicaSchematic.isSupport(world, (class_2338)posMutable))) continue;
                        class_2680 state = world.method_8320((class_2338)posMutable);
                        container.set(x, y, z, state);
                        if (!state.method_26215()) {
                            ++this.totalBlocksReadFromWorld;
                        }
                        if (!state.method_31709() || (te = world.method_8321((class_2338)posMutable)) == null) continue;
                        class_2338 pos = new class_2338(x, y, z);
                        class_2487 tag = te.method_38243((class_7225.class_7874)world.method_30349());
                        NbtUtils.writeBlockPosToTag((class_2338)pos, (class_2487)tag);
                        tileEntityMap.put(pos, tag);
                    }
                }
            }
            if (world instanceof class_3218) {
                class_3218 serverWorld = (class_3218)world;
                IntBoundingBox tickBox = IntBoundingBox.createProper((int)startX, (int)startY, (int)startZ, (int)(startX + sizeX), (int)(startY + sizeY), (int)(startZ + sizeZ));
                long currentTick = world.method_8510();
                this.getTicksFromScheduler(((IMixinWorldTickScheduler)serverWorld.method_14196()).litematica_getChunkTickSchedulers(), blockTickMap, tickBox, minCorner, currentTick);
                this.getTicksFromScheduler(((IMixinWorldTickScheduler)serverWorld.method_14179()).litematica_getChunkTickSchedulers(), fluidTickMap, tickBox, minCorner, currentTick);
            }
            this.blockContainers.put(box.getName(), container);
            this.tileEntities.put(box.getName(), tileEntityMap);
            this.pendingBlockTicks.put(box.getName(), blockTickMap);
            this.pendingFluidTicks.put(box.getName(), fluidTickMap);
        }
    }

    private <T> void getTicksFromScheduler(Long2ObjectMap<class_6755<T>> chunkTickSchedulers, Map<class_2338, class_6760<T>> outputMap, IntBoundingBox box, class_2338 minCorner, long currentTick) {
        int minCX = class_4076.method_18675((int)box.minX);
        int minCZ = class_4076.method_18675((int)box.minZ);
        int maxCX = class_4076.method_18675((int)box.maxX);
        int maxCZ = class_4076.method_18675((int)box.maxZ);
        for (int cx = minCX; cx <= maxCX; ++cx) {
            for (int cz = minCZ; cz <= maxCZ; ++cz) {
                long cp = class_1923.method_8331((int)cx, (int)cz);
                class_6755 chunkTickScheduler = (class_6755)chunkTickSchedulers.get(cp);
                if (chunkTickScheduler == null) continue;
                chunkTickScheduler.method_39372().filter(t -> box.containsPos((class_2382)t.comp_253())).forEach(t -> this.addRelativeTickToMap(outputMap, (class_6760)t, minCorner, currentTick));
            }
        }
    }

    private <T> void addRelativeTickToMap(Map<class_2338, class_6760<T>> outputMap, class_6760<T> tick, class_2338 minCorner, long currentTick) {
        class_2338 pos = tick.comp_253();
        class_2338 relativePos = new class_2338(pos.method_10263() - minCorner.method_10263(), pos.method_10264() - minCorner.method_10264(), pos.method_10260() - minCorner.method_10260());
        class_6760 newTick = new class_6760(tick.comp_252(), relativePos, tick.comp_254() - currentTick, tick.comp_255(), tick.comp_256());
        outputMap.put(relativePos, newTick);
    }

    public static boolean isExposed(class_1937 world, class_2338 pos) {
        for (class_2350 dir : PositionUtils.ALL_DIRECTIONS) {
            class_2338 posAdj = pos.method_10093(dir);
            class_2680 stateAdj = world.method_8320(posAdj);
            if (stateAdj.method_26225() && stateAdj.method_26206((class_1922)world, posAdj, dir.method_10153())) continue;
            return true;
        }
        return false;
    }

    public static boolean isGravityBlock(class_2680 state) {
        return state.method_26164(class_3481.field_15466) || state.method_26164(class_3481.field_45063) || state.method_26204() == class_2246.field_10255;
    }

    public static boolean isGravityBlock(class_1937 world, class_2338 pos) {
        return LitematicaSchematic.isGravityBlock(world.method_8320(pos));
    }

    public static boolean supportsExposedBlocks(class_1937 world, class_2338 pos) {
        class_2338 posUp = pos.method_10093(class_2350.field_11036);
        class_2680 stateUp = world.method_8320(posUp);
        while (true) {
            if (LitematicaSchematic.needsSupportNonGravity(stateUp)) {
                return true;
            }
            if (!LitematicaSchematic.isGravityBlock(stateUp)) break;
            if (LitematicaSchematic.isExposed(world, posUp)) {
                return true;
            }
            if ((posUp = posUp.method_10093(class_2350.field_11036)).method_10264() >= world.method_31600() + 1) break;
            stateUp = world.method_8320(posUp);
        }
        return false;
    }

    public static boolean needsSupportNonGravity(class_2680 state) {
        class_2248 block = state.method_26204();
        return block == class_2246.field_10450 || block == class_2246.field_10377 || block == class_2246.field_10477 || block instanceof class_2577;
    }

    public static boolean isSupport(class_1937 world, class_2338 pos) {
        class_2338 posUp = pos.method_10093(class_2350.field_11036);
        class_2680 stateUp = world.method_8320(posUp);
        if (LitematicaSchematic.needsSupportNonGravity(stateUp)) {
            return true;
        }
        return LitematicaSchematic.isGravityBlock(stateUp) && (LitematicaSchematic.isExposed(world, posUp) || LitematicaSchematic.supportsExposedBlocks(world, posUp));
    }

    public void takeBlocksFromWorldWithinChunk(class_1937 world, ImmutableMap<String, IntBoundingBox> volumes, ImmutableMap<String, Box> boxes, SchematicSaveInfo info) {
        class_2338.class_2339 posMutable = new class_2338.class_2339(0, 0, 0);
        for (Map.Entry volumeEntry : volumes.entrySet()) {
            String regionName = (String)volumeEntry.getKey();
            IntBoundingBox bb = (IntBoundingBox)volumeEntry.getValue();
            Box box = (Box)boxes.get((Object)regionName);
            if (box == null) {
                Litematica.LOGGER.error("null Box for sub-region '{}' while trying to save chunk-wise schematic", (Object)regionName);
                continue;
            }
            LitematicaBlockStateContainer container = this.blockContainers.get(regionName);
            Map<class_2338, class_2487> tileEntityMap = this.tileEntities.get(regionName);
            Map blockTickMap = this.pendingBlockTicks.get(regionName);
            Map fluidTickMap = this.pendingFluidTicks.get(regionName);
            if (container == null || tileEntityMap == null || blockTickMap == null || fluidTickMap == null) {
                Litematica.LOGGER.error("null map(s) for sub-region '{}' while trying to save chunk-wise schematic", (Object)regionName);
                continue;
            }
            class_2338 minCorner = fi.dy.masa.litematica.util.PositionUtils.getMinCorner(box.getPos1(), box.getPos2());
            int offsetX = minCorner.method_10263();
            int offsetY = minCorner.method_10264();
            int offsetZ = minCorner.method_10260();
            int startX = bb.minX - minCorner.method_10263();
            int startY = bb.minY - minCorner.method_10264();
            int startZ = bb.minZ - minCorner.method_10260();
            int endX = startX + (bb.maxX - bb.minX);
            int endY = startY + (bb.maxY - bb.minY);
            int endZ = startZ + (bb.maxZ - bb.minZ);
            boolean visibleOnly = info.visibleOnly;
            boolean includeSupport = info.includeSupportBlocks;
            for (int y = startY; y <= endY; ++y) {
                for (int z = startZ; z <= endZ; ++z) {
                    for (int x = startX; x <= endX; ++x) {
                        class_2487 tag;
                        posMutable.method_10103(x + offsetX, y + offsetY, z + offsetZ);
                        if (visibleOnly && !LitematicaSchematic.isExposed(world, (class_2338)posMutable) && (!includeSupport || !LitematicaSchematic.isSupport(world, (class_2338)posMutable))) continue;
                        class_2680 state = world.method_8320((class_2338)posMutable);
                        container.set(x, y, z, state);
                        if (!state.method_26215()) {
                            ++this.totalBlocksReadFromWorld;
                        }
                        if (!state.method_31709()) continue;
                        class_2586 te = world.method_8321((class_2338)posMutable);
                        if (te != null) {
                            class_2338 pos = new class_2338(x, y, z);
                            class_2487 tag2 = te.method_38243((class_7225.class_7874)world.method_30349());
                            NbtUtils.writeBlockPosToTag((class_2338)pos, (class_2487)tag2);
                            tileEntityMap.put(pos, tag2);
                            continue;
                        }
                        if (!EntitiesDataStorage.getInstance().hasServuxServer() || (tag = EntitiesDataStorage.getInstance().getFromBlockEntityCacheNbt((class_2338)posMutable)) == null || tag.method_33133()) continue;
                        class_2338 pos = new class_2338(x, y, z);
                        NbtUtils.writeBlockPosToTag((class_2338)pos, (class_2487)tag);
                        tileEntityMap.put(pos, tag);
                    }
                }
            }
            if (!(world instanceof class_3218)) continue;
            class_3218 serverWorld = (class_3218)world;
            IntBoundingBox tickBox = IntBoundingBox.createProper((int)(offsetX + startX), (int)(offsetY + startY), (int)(offsetZ + startZ), (int)(offsetX + endX + 1), (int)(offsetY + endY + 1), (int)(offsetZ + endZ + 1));
            long currentTick = world.method_8510();
            this.getTicksFromScheduler(((IMixinWorldTickScheduler)serverWorld.method_14196()).litematica_getChunkTickSchedulers(), blockTickMap, tickBox, minCorner, currentTick);
            this.getTicksFromScheduler(((IMixinWorldTickScheduler)serverWorld.method_14179()).litematica_getChunkTickSchedulers(), fluidTickMap, tickBox, minCorner, currentTick);
        }
    }

    private void setSubRegionPositions(List<Box> boxes, class_2338 areaOrigin) {
        for (Box box : boxes) {
            this.subRegionPositions.put(box.getName(), box.getPos1().method_10059((class_2382)areaOrigin));
        }
    }

    private void setSubRegionSizes(List<Box> boxes) {
        for (Box box : boxes) {
            this.subRegionSizes.put(box.getName(), box.getSize());
        }
    }

    @Nullable
    public LitematicaBlockStateContainer getSubRegionContainer(String regionName) {
        return this.blockContainers.get(regionName);
    }

    @Nullable
    public Map<class_2338, class_2487> getBlockEntityMapForRegion(String regionName) {
        return this.tileEntities.get(regionName);
    }

    @Nullable
    public List<EntityInfo> getEntityListForRegion(String regionName) {
        return this.entities.get(regionName);
    }

    @Nullable
    public Map<class_2338, class_6760<class_2248>> getScheduledBlockTicksForRegion(String regionName) {
        return this.pendingBlockTicks.get(regionName);
    }

    @Nullable
    public Map<class_2338, class_6760<class_3611>> getScheduledFluidTicksForRegion(String regionName) {
        return this.pendingFluidTicks.get(regionName);
    }

    public class_2487 writeToNBT() {
        class_2487 nbt = new class_2487();
        nbt.method_10569("MinecraftDataVersion", MINECRAFT_DATA_VERSION);
        nbt.method_10569("Version", 7);
        nbt.method_10569("SubVersion", 1);
        nbt.method_10566("Metadata", (class_2520)this.metadata.writeToNBT());
        nbt.method_10566("Regions", (class_2520)this.writeSubRegionsToNBT());
        return nbt;
    }

    public class_2487 writeToNBT_v6() {
        class_2487 nbt = new class_2487();
        nbt.method_10569("MinecraftDataVersion", 3700);
        nbt.method_10569("Version", 6);
        nbt.method_10569("SubVersion", 1);
        nbt.method_10566("Metadata", (class_2520)this.metadata.writeToNBT());
        nbt.method_10566("Regions", (class_2520)this.writeSubRegionsToNBT());
        return nbt;
    }

    private class_2487 writeSubRegionsToNBT() {
        class_2487 wrapper = new class_2487();
        if (!this.blockContainers.isEmpty()) {
            for (String regionName : this.blockContainers.keySet()) {
                LitematicaBlockStateContainer blockContainer = this.blockContainers.get(regionName);
                Map<class_2338, class_2487> tileMap = this.tileEntities.get(regionName);
                List<EntityInfo> entityList = this.entities.get(regionName);
                Map pendingBlockTicks = this.pendingBlockTicks.get(regionName);
                Map pendingFluidTicks = this.pendingFluidTicks.get(regionName);
                class_2487 tag = new class_2487();
                tag.method_10566("BlockStatePalette", (class_2520)blockContainer.getPalette().writeToNBT());
                tag.method_10566("BlockStates", (class_2520)new class_2501(blockContainer.getBackingLongArray()));
                tag.method_10566("TileEntities", (class_2520)this.writeTileEntitiesToNBT(tileMap));
                if (pendingBlockTicks != null) {
                    tag.method_10566("PendingBlockTicks", (class_2520)this.writePendingTicksToNBT(pendingBlockTicks, (class_2378)class_7923.field_41175, "Block"));
                }
                if (pendingFluidTicks != null) {
                    tag.method_10566("PendingFluidTicks", (class_2520)this.writePendingTicksToNBT(pendingFluidTicks, (class_2378)class_7923.field_41173, "Fluid"));
                }
                if (entityList != null) {
                    tag.method_10566("Entities", (class_2520)this.writeEntitiesToNBT(entityList));
                }
                class_2338 pos = this.subRegionPositions.get(regionName);
                tag.method_10566("Position", (class_2520)NbtUtils.createBlockPosTag((class_2338)pos));
                pos = this.subRegionSizes.get(regionName);
                tag.method_10566("Size", (class_2520)NbtUtils.createBlockPosTag((class_2338)pos));
                wrapper.method_10566(regionName, (class_2520)tag);
            }
        }
        return wrapper;
    }

    private class_2499 writeEntitiesToNBT(List<EntityInfo> entityList) {
        class_2499 tagList = new class_2499();
        if (!entityList.isEmpty()) {
            for (EntityInfo info : entityList) {
                tagList.add((Object)info.nbt);
            }
        }
        return tagList;
    }

    private <T> class_2499 writePendingTicksToNBT(Map<class_2338, class_6760<T>> tickMap, class_2378<T> registry, String tagName) {
        class_2499 tagList = new class_2499();
        if (!tickMap.isEmpty()) {
            for (class_6760<T> entry : tickMap.values()) {
                Object target = entry.comp_252();
                class_2960 id = registry.method_10221(target);
                if (id == null) continue;
                class_2487 tag = new class_2487();
                tag.method_10582(tagName, id.toString());
                tag.method_10569("Priority", entry.comp_255().method_8681());
                tag.method_10544("SubTick", entry.comp_256());
                tag.method_10569("Time", (int)entry.comp_254());
                tag.method_10569("x", entry.comp_253().method_10263());
                tag.method_10569("y", entry.comp_253().method_10264());
                tag.method_10569("z", entry.comp_253().method_10260());
                tagList.add((Object)tag);
            }
        }
        return tagList;
    }

    private class_2499 writeTileEntitiesToNBT(Map<class_2338, class_2487> tileMap) {
        class_2499 tagList = new class_2499();
        if (!tileMap.isEmpty()) {
            tagList.addAll(tileMap.values());
        }
        return tagList;
    }

    public void sendTransmitFile(class_2487 nbtIn, long sessionKey, boolean printMessage) {
        if (!EntitiesDataStorage.getInstance().hasServuxServer()) {
            if (printMessage) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.schematic_transmit_not_available", (Object[])new Object[0]);
            }
            Litematica.LOGGER.error("transmitFileToServux: Cannot transmit a Litematic without having Servux present.");
            return;
        }
        if (printMessage) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.schematic_transmit_start", (Object[])new Object[0]);
        }
        Path file = this.getFile().toPath();
        class_2487 output = new class_2487();
        output.method_10582("Task", "Litematic-TransmitStart");
        output.method_10582("FileName", file.getFileName().toString());
        class_2487 finalOutput = output.method_10553();
        FileType.CODEC.encodeStart((DynamicOps)class_2509.field_11560, (Object)this.schematicType).resultOrPartial().ifPresent(result -> finalOutput.method_10566("FileType", result));
        finalOutput.method_10544("SliceKey", sessionKey);
        if (!nbtIn.method_33133()) {
            finalOutput.method_10566("PlacementData", (class_2520)nbtIn);
        }
        ServuxLitematicaHandler.getInstance().encodeClientData(ServuxLitematicaPacket.ResponseC2SStart(finalOutput));
        int bufferSize = 16384;
        byte[] buffer = new byte[16384];
        int totalBytes = 0;
        int totalSlices = 0;
        output.method_10544("SliceKey", sessionKey);
        try (InputStream is = Files.newInputStream(file, new OpenOption[0]);){
            int bytesRead = 0;
            output.method_10582("Task", "Litematic-TransmitData");
            while (bytesRead != -1) {
                output.method_10551("Slice");
                output.method_10551("Size");
                output.method_10551("Data");
                bytesRead = is.read(buffer, 0, 16384);
                output.method_10569("Slice", totalSlices);
                output.method_10569("Size", bytesRead);
                output.method_10570("Data", buffer);
                ServuxLitematicaHandler.getInstance().encodeClientData(ServuxLitematicaPacket.ResponseC2SStart(output));
                totalBytes += bytesRead;
                ++totalSlices;
            }
        }
        catch (Exception err) {
            if (printMessage) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.schematic_transmit_fail", (Object[])new Object[0]);
            }
            output = new class_2487();
            output.method_10544("SliceKey", sessionKey);
            output.method_10582("Task", "Litematic-TransmitCancel");
            ServuxLitematicaHandler.getInstance().encodeClientData(ServuxLitematicaPacket.ResponseC2SStart(output));
            Litematica.LOGGER.error("sliceForServux: Exception reading file; {}", (Object)err.getLocalizedMessage());
            return;
        }
        output.method_10551("Slice");
        output.method_10551("Size");
        output.method_10551("Data");
        output.method_10569("TotalSize", totalBytes);
        output.method_10569("TotalSlices", totalSlices);
        output.method_10582("Task", "Litematic-TransmitEnd");
        ServuxLitematicaHandler.getInstance().encodeClientData(ServuxLitematicaPacket.ResponseC2SStart(output));
        Litematica.debugLog("receiveFileTransmit: Treansmitted file '{}', [tS: {}, tB: {}]", file.toAbsolutePath().toString(), totalSlices, totalBytes);
        if (printMessage) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.schematic_transmit_complete", (Object[])new Object[]{totalBytes});
        }
    }

    @Nullable
    public static Pair<LitematicaSchematic, class_2487> receiveFileTransmit(class_2487 nbt) {
        SchematicBufferManager manager = DataManager.getSchematicBufferManager();
        String task = nbt.method_10558("Task");
        long key = nbt.method_10537("SliceKey");
        if (task.isEmpty() || key == -1L) {
            Litematica.LOGGER.error("receiveFileTransmit: Invalid sessionKey or Task received.");
            return null;
        }
        switch (task) {
            case "Litematic-TransmitStart": {
                FileType type = (FileType)((Object)FileType.CODEC.parse((DynamicOps)class_2509.field_11560, (Object)nbt.method_10580("FileType")).getPartialOrThrow());
                String name = nbt.method_10558("FileName");
                manager.createBuffer(name, type, key, nbt.method_10562("PlacementData"));
                break;
            }
            case "Litematic-TransmitData": {
                int slice = nbt.method_10550("Slice");
                int size = nbt.method_10550("Size");
                byte[] data = nbt.method_10547("Data");
                if (slice < 0 || size < 0 || data.length == 0) {
                    Litematica.LOGGER.error("receiveFileTransmit: Invalid Slice Data received for session key [{}]", (Object)key);
                    return null;
                }
                manager.receiveSlice(key, slice, data, size);
                break;
            }
            case "Litematic-TransmitCancel": {
                Litematica.LOGGER.warn("receiveFileTransmit: Cancel received for session key [{}]", (Object)key);
                manager.cancelBuffer(key);
                break;
            }
            case "Litematic-TransmitEnd": {
                int totalSize = nbt.method_10550("TotalSize");
                int totalSlices = nbt.method_10550("TotalSlices");
                Path dir = DataManager.getSchematicTransmitDirectory();
                class_2487 optional = manager.getOptionalNbt(key);
                LitematicaSchematic schematic = manager.finishBuffer(key, dir);
                if (schematic == null) {
                    Litematica.LOGGER.warn("receiveFileTransmit: Failed to create Schematic for finishing session key [{}]", (Object)key);
                    return null;
                }
                Litematica.debugLog("receiveFileTransmit: Received file '{}', [tS: {}, tB: {}]", schematic.getFile().getAbsolutePath(), totalSlices, totalSize);
                return Pair.of((Object)schematic, (Object)optional);
            }
            default: {
                Litematica.LOGGER.error("receiveFileTransmit: Invalid sessionKey or Task received.");
            }
        }
        return null;
    }

    private boolean readFromNBT(class_2487 nbt) {
        this.blockContainers.clear();
        this.tileEntities.clear();
        this.entities.clear();
        this.pendingBlockTicks.clear();
        this.subRegionPositions.clear();
        this.subRegionSizes.clear();
        if (nbt.method_10573("Version", 3)) {
            int minecraftDataVersion;
            int version = nbt.method_10550("Version");
            int n = minecraftDataVersion = nbt.method_10545("MinecraftDataVersion") ? nbt.method_10550("MinecraftDataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
            if (version >= 1 && version <= 7) {
                if (minecraftDataVersion - MINECRAFT_DATA_VERSION > 100) {
                    Object[] objectArray = new Object[2];
                    objectArray[0] = minecraftDataVersion;
                    objectArray[1] = MINECRAFT_DATA_VERSION;
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.error.schematic_load.newer_minecraft_version", (Object[])objectArray);
                }
                this.metadata.readFromNBT(nbt.method_10562("Metadata"));
                this.metadata.setSchematicVersion(version);
                this.metadata.setMinecraftDataVersion(minecraftDataVersion);
                this.metadata.setFileType(FileType.LITEMATICA_SCHEMATIC);
                this.readSubRegionsFromNBT(nbt.method_10562("Regions"), version, minecraftDataVersion);
                return true;
            }
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_load.unsupported_schematic_version", (Object[])new Object[]{version});
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_load.no_schematic_version_information", (Object[])new Object[0]);
        }
        return false;
    }

    private void readSubRegionsFromNBT(class_2487 tag, int version, int minecraftDataVersion) {
        for (String regionName : tag.method_10541()) {
            class_2520 nbtBase;
            class_2499 list;
            if (tag.method_10580(regionName).method_10711() != 10) continue;
            class_2487 regionTag = tag.method_10562(regionName);
            class_2338 regionPos = NbtUtils.readBlockPos((class_2487)regionTag.method_10562("Position"));
            class_2338 regionSize = NbtUtils.readBlockPos((class_2487)regionTag.method_10562("Size"));
            Map<class_2338, class_2487> tiles = null;
            if (regionPos == null || regionSize == null) continue;
            this.subRegionPositions.put(regionName, regionPos);
            this.subRegionSizes.put(regionName, regionSize);
            if (version >= 2) {
                tiles = this.readTileEntitiesFromNBT(regionTag.method_10554("TileEntities", 10));
                tiles = this.convertTileEntities_to_1_20_5(tiles, minecraftDataVersion);
                this.tileEntities.put(regionName, tiles);
                class_2499 entities = regionTag.method_10554("Entities", 10);
                entities = this.convertEntities_to_1_20_5(entities, minecraftDataVersion);
                this.entities.put(regionName, this.readEntitiesFromNBT(entities));
            } else if (version == 1) {
                tiles = this.readTileEntitiesFromNBT_v1(regionTag.method_10554("TileEntities", 10));
                this.tileEntities.put(regionName, tiles);
                this.entities.put(regionName, this.readEntitiesFromNBT_v1(regionTag.method_10554("Entities", 10)));
            }
            if (version >= 3) {
                list = regionTag.method_10554("PendingBlockTicks", 10);
                this.pendingBlockTicks.put(regionName, this.readPendingTicksFromNBT(list, (class_2378)class_7923.field_41175, "Block", (Object)class_2246.field_10124));
            }
            if (version >= 5) {
                list = regionTag.method_10554("PendingFluidTicks", 10);
                this.pendingFluidTicks.put(regionName, this.readPendingTicksFromNBT(list, (class_2378)class_7923.field_41173, "Fluid", (Object)class_3612.field_15906));
            }
            if ((nbtBase = regionTag.method_10580("BlockStates")) == null || nbtBase.method_10711() != 12) continue;
            class_2499 palette = regionTag.method_10554("BlockStatePalette", 10);
            long[] blockStateArr = ((class_2501)nbtBase).method_10615();
            class_2338 posEndRel = fi.dy.masa.litematica.util.PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)regionSize).method_10081((class_2382)regionPos);
            class_2338 posMin = fi.dy.masa.litematica.util.PositionUtils.getMinCorner(regionPos, posEndRel);
            class_2338 posMax = fi.dy.masa.litematica.util.PositionUtils.getMaxCorner(regionPos, posEndRel);
            class_2338 size = posMax.method_10059((class_2382)posMin).method_10069(1, 1, 1);
            palette = this.convertBlockStatePalette_1_12_to_1_13_2(palette, version, minecraftDataVersion);
            palette = this.convertBlockStatePalette_to_1_20_5(palette, minecraftDataVersion);
            LitematicaBlockStateContainer container = LitematicaBlockStateContainer.createFrom(palette, blockStateArr, size);
            if (minecraftDataVersion < MINECRAFT_DATA_VERSION) {
                this.postProcessContainerIfNeeded(palette, container, tiles);
            }
            this.blockContainers.put(regionName, container);
        }
    }

    public static boolean isSizeValid(@Nullable class_2382 size) {
        return size != null && size.method_10263() > 0 && size.method_10264() > 0 && size.method_10260() > 0;
    }

    @Nullable
    private static class_2382 readSizeFromTagImpl(class_2487 tag) {
        class_2499 tagList;
        if (tag.method_10573("size", 9) && (tagList = tag.method_10554("size", 3)).size() == 3) {
            return new class_2382(tagList.method_10600(0), tagList.method_10600(1), tagList.method_10600(2));
        }
        return null;
    }

    @Nullable
    public static class_2338 readBlockPosFromNbtList(class_2487 tag, String tagName) {
        class_2499 tagList;
        if (tag.method_10573(tagName, 9) && (tagList = tag.method_10554(tagName, 3)).size() == 3) {
            return new class_2338(tagList.method_10600(0), tagList.method_10600(1), tagList.method_10600(2));
        }
        return null;
    }

    protected boolean readPaletteFromLitematicaFormatTag(class_2499 tagList, ILitematicaBlockStatePalette palette) {
        int size = tagList.size();
        ArrayList<class_2680> list = new ArrayList<class_2680>(size);
        class_2378 lookup = SchematicWorldHandler.INSTANCE.getRegistryManager().method_30530(class_7924.field_41254);
        for (int id = 0; id < size; ++id) {
            class_2487 tag = tagList.method_10602(id);
            class_2680 state = class_2512.method_10681((class_7871)lookup, (class_2487)tag);
            list.add(state);
        }
        return palette.setMapping(list);
    }

    public static boolean isValidSpongeSchematic(class_2487 tag) {
        if (tag.method_10573("Width", 99) && tag.method_10573("Height", 99) && tag.method_10573("Length", 99) && tag.method_10573("Version", 3) && tag.method_10573("Palette", 10) && tag.method_10573("BlockData", 7)) {
            return LitematicaSchematic.isSizeValid(LitematicaSchematic.readSizeFromTagSponge(tag));
        }
        return false;
    }

    public static boolean isValidSpongeSchematicv3(class_2487 tag) {
        class_2487 nbtV3;
        if (tag.method_10573("Schematic", 10) && (nbtV3 = tag.method_10562("Schematic")).method_10573("Width", 99) && nbtV3.method_10573("Height", 99) && nbtV3.method_10573("Length", 99) && nbtV3.method_10573("Version", 3) && nbtV3.method_10550("Version") >= 3 && nbtV3.method_10545("Blocks") && nbtV3.method_10545("DataVersion")) {
            return LitematicaSchematic.isSizeValid(LitematicaSchematic.readSizeFromTagSponge(nbtV3));
        }
        return false;
    }

    public static class_2382 readSizeFromTagSponge(class_2487 tag) {
        return new class_2382(tag.method_10550("Width"), tag.method_10550("Height"), tag.method_10550("Length"));
    }

    protected boolean readSpongePaletteFromTag(class_2487 tag, ILitematicaBlockStatePalette palette, int minecraftDataVersion) {
        int size = tag.method_10541().size();
        ArrayList<class_2680> list = new ArrayList<class_2680>(size);
        class_2680 air = class_2246.field_10124.method_9564();
        for (int i = 0; i < size; ++i) {
            list.add(air);
        }
        for (String key : tag.method_10541()) {
            class_2680 state;
            int id = tag.method_10550(key);
            Optional<class_2680> stateOptional = BlockUtils.getBlockStateFromString(key, minecraftDataVersion);
            if (stateOptional.isPresent()) {
                state = stateOptional.get();
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)("Unknown block in the Sponge schematic palette: '" + key + "'"), (Object[])new Object[0]);
                state = LitematicaBlockStateContainer.AIR_BLOCK_STATE;
            }
            if (id < 0 || id >= size) {
                String msg = "Invalid ID in the Sponge schematic palette: '" + id + "'";
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)msg, (Object[])new Object[0]);
                Litematica.LOGGER.error(msg);
                return false;
            }
            list.set(id, state);
        }
        return palette.setMapping(list);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean readSpongeBlocksFromTagMetadataOnly(class_2487 tag, String schematicName, class_2382 size, int minecraftDataVersion, int spongeVersion) {
        class_2487 blocksTag = new class_2487();
        if (spongeVersion >= 3 && tag.method_10545("Blocks")) {
            blocksTag = tag.method_10562("Blocks");
            if (!blocksTag.method_10545("Palette") || !blocksTag.method_10545("Data")) return false;
            byte[] blockData = blocksTag.method_10547("Data");
            this.totalBlocksReadFromWorld = blockData.length;
            return true;
        } else {
            if (!tag.method_10545("Palette") || !tag.method_10545("BlockData")) return false;
            byte[] blockData = tag.method_10547("BlockData");
            this.totalBlocksReadFromWorld = blockData.length;
        }
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean readSpongeBlocksFromTag(class_2487 tag, String schematicName, class_2382 size, int minecraftDataVersion, int spongeVersion) {
        int paletteSize;
        byte[] blockData;
        class_2487 paletteTag;
        class_2487 blocksTag = new class_2487();
        if (spongeVersion >= 3 && tag.method_10545("Blocks")) {
            blocksTag = tag.method_10562("Blocks");
            if (!blocksTag.method_10573("Palette", 10) || !blocksTag.method_10573("Data", 7)) return false;
            paletteTag = blocksTag.method_10562("Palette");
            blockData = blocksTag.method_10547("Data");
            paletteSize = paletteTag.method_10541().size();
        } else {
            if (!tag.method_10573("Palette", 10) || !tag.method_10573("BlockData", 7)) return false;
            paletteTag = tag.method_10562("Palette");
            blockData = tag.method_10547("BlockData");
            paletteSize = paletteTag.method_10541().size();
        }
        LitematicaBlockStateContainer container = LitematicaBlockStateContainer.createContainer(paletteSize, blockData, size);
        if (container == null) {
            String msg = "Failed to read blocks from Sponge schematic";
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)msg, (Object[])new Object[0]);
            Litematica.LOGGER.error(msg);
            return false;
        }
        this.blockContainers.put(schematicName, container);
        if (!this.readSpongePaletteFromTag(paletteTag, container.getPalette(), minecraftDataVersion)) {
            return false;
        }
        if (spongeVersion < 3) return true;
        if (blocksTag.method_33133()) return false;
        Map<class_2338, class_2487> tileEntities = this.readSpongeBlockEntitiesFromTag(blocksTag, spongeVersion);
        tileEntities = this.convertTileEntities_to_1_20_5(tileEntities, minecraftDataVersion);
        this.tileEntities.put(schematicName, tileEntities);
        return true;
    }

    protected Map<class_2338, class_2487> readSpongeBlockEntitiesFromTag(class_2487 tag, int spongeVersion) {
        String tagName;
        HashMap<class_2338, class_2487> blockEntities = new HashMap<class_2338, class_2487>();
        String string = tagName = spongeVersion == 1 ? "TileEntities" : "BlockEntities";
        if (!tag.method_10545(tagName)) {
            return blockEntities;
        }
        class_2499 tagList = tag.method_10554(tagName, 10);
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 beTag = tagList.method_10602(i);
            class_2338 pos = NbtUtils.readBlockPosFromArrayTag((class_2487)beTag, (String)"Pos");
            if (pos == null || beTag.method_33133()) continue;
            beTag.method_10582("id", beTag.method_10558("Id"));
            beTag.method_10551("Id");
            beTag.method_10551("Pos");
            if (spongeVersion == 1) {
                beTag.method_10551("ContentVersion");
            }
            if (spongeVersion >= 3) {
                class_2487 beData = beTag.method_10562("Data");
                blockEntities.put(pos, beData);
                continue;
            }
            blockEntities.put(pos, beTag);
        }
        return blockEntities;
    }

    protected List<EntityInfo> readSpongeEntitiesFromTag(class_2487 tag, class_2382 offset, int spongeVersion) {
        ArrayList<EntityInfo> entities = new ArrayList<EntityInfo>();
        class_2499 tagList = tag.method_10554("Entities", 10);
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 entityEntry = tagList.method_10602(i);
            class_243 pos = NbtUtils.readVec3dFromListTag((class_2487)entityEntry);
            if (pos == null || entityEntry.method_33133()) continue;
            entityEntry.method_10582("id", entityEntry.method_10558("Id"));
            entityEntry.method_10551("Id");
            if (spongeVersion >= 3) {
                class_2487 entityData = entityEntry.method_10562("Data");
                if (!entityData.method_10573("id", 8)) {
                    entityData.method_10582("id", entityEntry.method_10558("id"));
                }
                entities.add(new EntityInfo(pos, entityData));
                continue;
            }
            pos = new class_243(pos.field_1352 - (double)offset.method_10263(), pos.field_1351 - (double)offset.method_10264(), pos.field_1350 - (double)offset.method_10260());
            entities.add(new EntityInfo(pos, entityEntry));
        }
        return entities;
    }

    public boolean readFromSpongeSchematicMetadataOnly(String name, class_2487 tag) {
        if (LitematicaSchematic.isValidSpongeSchematicv3(tag)) {
            class_2487 spongeTag = tag.method_10562("Schematic");
            tag.method_10551("Schematic");
            tag.method_10543(spongeTag);
        } else if (!LitematicaSchematic.isValidSpongeSchematic(tag)) {
            return false;
        }
        int spongeVersion = tag.method_10545("Version") ? tag.method_10550("Version") : -1;
        int minecraftDataVersion = tag.method_10545("DataVersion") ? tag.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        class_2382 size = LitematicaSchematic.readSizeFromTagSponge(tag);
        if (!this.readSpongeBlocksFromTagMetadataOnly(tag, name, size, minecraftDataVersion, spongeVersion)) {
            return false;
        }
        if (tag.method_10545("Metadata")) {
            class_2487 metadata = tag.method_10562("Metadata");
            this.metadata.setName(metadata.method_10545("Name") ? metadata.method_10558("Name") : name);
            this.metadata.setAuthor(metadata.method_10545("Author") ? metadata.method_10558("Author") : "unknown");
            this.metadata.setTimeCreated(metadata.method_10545("Date") ? metadata.method_10537("Date") : System.currentTimeMillis());
        } else {
            this.metadata.setAuthor("unknown");
            this.metadata.setName(name);
            this.metadata.setTimeCreated(System.currentTimeMillis());
        }
        if (tag.method_10545("author")) {
            this.metadata.setAuthor(tag.method_10558("author"));
        }
        this.metadata.setRegionCount(1);
        this.metadata.setTotalVolume(size.method_10263() * size.method_10264() * size.method_10260());
        this.metadata.setEnclosingSize(size);
        this.metadata.setTimeModified(this.metadata.getTimeCreated());
        this.metadata.setTotalBlocks(this.totalBlocksReadFromWorld);
        this.metadata.setSchematicVersion(spongeVersion);
        this.metadata.setMinecraftDataVersion(minecraftDataVersion);
        this.metadata.setFileType(FileType.SPONGE_SCHEMATIC);
        return true;
    }

    public boolean readFromSpongeSchematic(String name, class_2487 tag) {
        if (LitematicaSchematic.isValidSpongeSchematicv3(tag)) {
            class_2487 spongeTag = tag.method_10562("Schematic");
            tag.method_10551("Schematic");
            tag.method_10543(spongeTag);
        } else if (!LitematicaSchematic.isValidSpongeSchematic(tag)) {
            return false;
        }
        int spongeVersion = tag.method_10545("Version") ? tag.method_10550("Version") : -1;
        int minecraftDataVersion = tag.method_10545("DataVersion") ? tag.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        class_2382 size = LitematicaSchematic.readSizeFromTagSponge(tag);
        if (!this.readSpongeBlocksFromTag(tag, name, size, minecraftDataVersion, spongeVersion)) {
            return false;
        }
        class_2382 offset = NbtUtils.readVec3iFromIntArray((class_2487)tag, (String)"Offset");
        if (offset == null) {
            offset = class_2382.field_11176;
        }
        if (spongeVersion < 3) {
            Map<class_2338, class_2487> tileEntities = this.readSpongeBlockEntitiesFromTag(tag, spongeVersion);
            tileEntities = this.convertTileEntities_to_1_20_5(tileEntities, minecraftDataVersion);
            this.tileEntities.put(name, tileEntities);
        }
        List<EntityInfo> entities = this.readSpongeEntitiesFromTag(tag, offset, spongeVersion);
        entities = this.convertSpongeEntities_to_1_20_5(entities, minecraftDataVersion);
        this.entities.put(name, entities);
        if (tag.method_10573("Metadata", 10)) {
            class_2487 metadata = tag.method_10562("Metadata");
            this.metadata.setName(metadata.method_10573("Name", 8) ? metadata.method_10558("Name") : name);
            this.metadata.setAuthor(metadata.method_10573("Author", 8) ? metadata.method_10558("Author") : "unknown");
            this.metadata.setTimeCreated(metadata.method_10573("Date", 4) ? metadata.method_10537("Date") : System.currentTimeMillis());
        } else {
            this.metadata.setAuthor("unknown");
            this.metadata.setName(name);
            this.metadata.setTimeCreated(System.currentTimeMillis());
        }
        if (tag.method_10573("author", 8)) {
            this.metadata.setAuthor(tag.method_10558("author"));
        }
        this.subRegionPositions.put(name, class_2338.field_10980);
        this.subRegionSizes.put(name, new class_2338(size));
        this.metadata.setRegionCount(1);
        this.metadata.setTotalVolume(size.method_10263() * size.method_10264() * size.method_10260());
        this.metadata.setEnclosingSize(size);
        this.metadata.setTimeModified(this.metadata.getTimeCreated());
        this.metadata.setTotalBlocks(this.totalBlocksReadFromWorld);
        this.metadata.setSchematicVersion(spongeVersion);
        this.metadata.setMinecraftDataVersion(minecraftDataVersion);
        this.metadata.setFileType(FileType.SPONGE_SCHEMATIC);
        return true;
    }

    public boolean readFromVanillaStructureMetadataOnly(String name, class_2487 tag) {
        class_2382 size = LitematicaSchematic.readSizeFromTagImpl(tag);
        if (tag.method_10545("palette") && tag.method_10545("blocks") && LitematicaSchematic.isSizeValid(size)) {
            int minecraftDataVersion;
            int n = minecraftDataVersion = tag.method_10545("DataVersion") ? tag.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
            if (tag.method_10545("author")) {
                this.getMetadata().setAuthor(tag.method_10558("author"));
            }
            this.metadata.setName(name);
            this.metadata.setRegionCount(1);
            this.metadata.setTotalVolume(size.method_10263() * size.method_10264() * size.method_10260());
            this.metadata.setEnclosingSize(size);
            this.metadata.setTimeCreated(System.currentTimeMillis());
            this.metadata.setTimeModified(this.metadata.getTimeCreated());
            this.metadata.setSchematicVersion(0);
            this.metadata.setMinecraftDataVersion(minecraftDataVersion);
            this.metadata.setFileType(FileType.VANILLA_STRUCTURE);
            class_2499 blockList = tag.method_10554("blocks", 10);
            this.metadata.setTotalBlocks(blockList.size());
            return true;
        }
        return false;
    }

    public boolean readFromVanillaStructure(String name, class_2487 tag) {
        class_2382 size = LitematicaSchematic.readSizeFromTagImpl(tag);
        if (tag.method_10573("palette", 9) && tag.method_10573("blocks", 9) && LitematicaSchematic.isSizeValid(size)) {
            class_2499 paletteTag = tag.method_10554("palette", 10);
            int minecraftDataVersion = tag.method_10545("DataVersion") ? tag.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
            HashMap<class_2338, class_2487> tileMap = new HashMap<class_2338, class_2487>();
            this.tileEntities.put(name, tileMap);
            class_2680 air = class_2246.field_10124.method_9564();
            int paletteSize = paletteTag.size();
            ArrayList<class_2680> list = new ArrayList<class_2680>(paletteSize);
            class_2378 lookup = SchematicWorldHandler.INSTANCE.getRegistryManager().method_30530(class_7924.field_41254);
            Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
            if (minecraftDataVersion < MINECRAFT_DATA_VERSION && effective != null) {
                Litematica.LOGGER.info("VanillaStructure: executing Vanilla DataFixer for Block State Palette DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
            } else if (effective == null) {
                Litematica.LOGGER.warn("readFromVanillaStructure(): Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Block State Palette DataVersion {}", (Object)minecraftDataVersion);
            }
            for (int id = 0; id < paletteSize; ++id) {
                class_2487 t = paletteTag.method_10602(id);
                if (minecraftDataVersion < MINECRAFT_DATA_VERSION && effective != null) {
                    t = SchematicConversionMaps.updateBlockStates(t, minecraftDataVersion);
                }
                class_2680 state = class_2512.method_10681((class_7871)lookup, (class_2487)t);
                list.add(state);
            }
            class_2680 zeroState = (class_2680)list.get(0);
            int airId = -1;
            for (int i = 0; i < paletteSize; ++i) {
                if (list.get(i) != air) continue;
                airId = i;
                break;
            }
            if (airId != 0) {
                if (airId == -1) {
                    list.add(0, air);
                    ++paletteSize;
                } else {
                    list.set(0, air);
                    list.set(airId, zeroState);
                }
            }
            int bits = Math.max(2, 32 - Integer.numberOfLeadingZeros(paletteSize - 1));
            LitematicaBlockStateContainer container = new LitematicaBlockStateContainer(size.method_10263(), size.method_10264(), size.method_10260(), bits, null);
            ILitematicaBlockStatePalette palette = container.getPalette();
            palette.setMapping(list);
            this.blockContainers.put(name, container);
            if (tag.method_10573("author", 8)) {
                this.getMetadata().setAuthor(tag.method_10558("author"));
            }
            this.subRegionPositions.put(name, class_2338.field_10980);
            this.subRegionSizes.put(name, new class_2338(size));
            this.metadata.setName(name);
            this.metadata.setRegionCount(1);
            this.metadata.setTotalVolume(size.method_10263() * size.method_10264() * size.method_10260());
            this.metadata.setEnclosingSize(size);
            this.metadata.setTimeCreated(System.currentTimeMillis());
            this.metadata.setTimeModified(this.metadata.getTimeCreated());
            this.metadata.setSchematicVersion(0);
            this.metadata.setMinecraftDataVersion(minecraftDataVersion);
            this.metadata.setFileType(FileType.VANILLA_STRUCTURE);
            class_2499 blockList = tag.method_10554("blocks", 10);
            int count = blockList.size();
            int totalBlocks = 0;
            for (int i = 0; i < count; ++i) {
                class_2487 blockTag = blockList.method_10602(i);
                class_2338 pos = LitematicaSchematic.readBlockPosFromNbtList(blockTag, "pos");
                if (pos == null) {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Failed to read block position for vanilla structure", (Object[])new Object[0]);
                    return false;
                }
                int id = blockTag.method_10550("state");
                class_2680 state = airId == -1 ? palette.getBlockState(id + 1) : (airId != 0 ? (id == 0 ? zeroState : (id == airId ? air : palette.getBlockState(id))) : palette.getBlockState(id));
                if (state == null) {
                    state = air;
                } else if (state != air) {
                    ++totalBlocks;
                }
                container.set(pos.method_10263(), pos.method_10264(), pos.method_10260(), state);
                if (!blockTag.method_10573("nbt", 10)) continue;
                tileMap.put(pos, blockTag.method_10562("nbt"));
            }
            this.metadata.setTotalBlocks(totalBlocks);
            this.entities.put(name, this.readEntitiesFromVanillaStructure(tag, minecraftDataVersion));
            return true;
        }
        return false;
    }

    protected List<EntityInfo> readEntitiesFromVanillaStructure(class_2487 tag, int minecraftDataVersion) {
        ArrayList<EntityInfo> entities = new ArrayList<EntityInfo>();
        class_2499 tagList = tag.method_10554("entities", 10);
        int size = tagList.size();
        Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
        if (minecraftDataVersion < MINECRAFT_DATA_VERSION && effective != null) {
            Litematica.LOGGER.info("VanillaStructure: executing Vanilla DataFixer for Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
        } else if (effective == null) {
            Litematica.LOGGER.warn("readEntitiesFromVanillaStructure(): Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Entities DataVersion {}", (Object)minecraftDataVersion);
        }
        for (int i = 0; i < size; ++i) {
            class_243 pos;
            class_2487 entityData = tagList.method_10602(i);
            if (minecraftDataVersion < MINECRAFT_DATA_VERSION && effective != null) {
                entityData = SchematicConversionMaps.updateEntity(entityData, minecraftDataVersion);
            }
            if ((pos = LitematicaSchematic.readVec3dFromNbtList(entityData, "pos")) == null || !entityData.method_10573("nbt", 10)) continue;
            entities.add(new EntityInfo(pos, entityData.method_10562("nbt")));
        }
        return entities;
    }

    @Nullable
    public static class_243 readVec3dFromNbtList(@Nullable class_2487 tag, String tagName) {
        class_2499 tagList;
        if (tag != null && tag.method_10573(tagName, 9) && (tagList = tag.method_10554(tagName, 6)).method_10601() == 6 && tagList.size() == 3) {
            return new class_243(tagList.method_10611(0), tagList.method_10611(1), tagList.method_10611(2));
        }
        return null;
    }

    private void postProcessContainerIfNeeded(class_2499 palette, LitematicaBlockStateContainer container, @Nullable Map<class_2338, class_2487> tiles) {
        List<class_2680> states = LitematicaSchematic.getStatesFromPaletteTag(palette);
        if (this.converter.createPostProcessStateFilter(states)) {
            IdentityHashMap<class_2680, SchematicConversionFixers.IStateFixer> postProcessingFilter = this.converter.getPostProcessStateFilter();
            SchematicConverter.postProcessBlocks(container, tiles, postProcessingFilter);
        }
    }

    public static List<class_2680> getStatesFromPaletteTag(class_2499 palette) {
        ArrayList<class_2680> states = new ArrayList<class_2680>();
        class_2378 lookup = SchematicWorldHandler.INSTANCE.getRegistryManager().method_30530(class_7924.field_41254);
        int size = palette.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = palette.method_10602(i);
            class_2680 state = class_2512.method_10681((class_7871)lookup, (class_2487)tag);
            if (i <= 0 && state == LitematicaBlockStateContainer.AIR_BLOCK_STATE) continue;
            states.add(state);
        }
        return states;
    }

    private class_2499 convertBlockStatePalette_1_12_to_1_13_2(class_2499 oldPalette, int version, int minecraftDataVersion) {
        if (version < 5 || minecraftDataVersion < 1631 && minecraftDataVersion > 0) {
            class_2499 newPalette = new class_2499();
            int count = oldPalette.size();
            for (int i = 0; i < count; ++i) {
                newPalette.add((Object)SchematicConversionMaps.get_1_13_2_StateTagFor_1_12_Tag(oldPalette.method_10602(i)));
            }
            return newPalette;
        }
        return oldPalette;
    }

    private class_2499 convertBlockStatePalette_to_1_20_5(class_2499 oldPalette, int minecraftDataVersion) {
        if (minecraftDataVersion < Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue()) {
            minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        }
        if (minecraftDataVersion < MINECRAFT_DATA_VERSION) {
            Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
            if (effective == null) {
                Litematica.LOGGER.warn("LitematicaSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Block State Palette DataVersion {}", (Object)minecraftDataVersion);
                return oldPalette;
            }
            class_2499 newPalette = new class_2499();
            int count = oldPalette.size();
            Litematica.LOGGER.info("LitematicaSchematic: executing Vanilla DataFixer for Block State Palette DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
            for (int i = 0; i < count; ++i) {
                newPalette.add((Object)SchematicConversionMaps.updateBlockStates(oldPalette.method_10602(i), minecraftDataVersion));
            }
            return newPalette;
        }
        return oldPalette;
    }

    private Map<class_2338, class_2487> convertTileEntities_to_1_20_5(Map<class_2338, class_2487> oldTE, int minecraftDataVersion) {
        if (minecraftDataVersion < Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue()) {
            minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        }
        if (minecraftDataVersion < MINECRAFT_DATA_VERSION) {
            Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
            if (effective == null) {
                Litematica.LOGGER.warn("LitematicaSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Tile Entities DataVersion {}", (Object)minecraftDataVersion);
                return oldTE;
            }
            HashMap<class_2338, class_2487> newTE = new HashMap<class_2338, class_2487>();
            Litematica.LOGGER.info("LitematicaSchematic: executing Vanilla DataFixer for Tile Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
            for (class_2338 key : oldTE.keySet()) {
                newTE.put(key, SchematicConversionMaps.updateBlockEntity(SchematicConversionMaps.checkForIdTag(oldTE.get(key)), minecraftDataVersion));
            }
            return newTE;
        }
        return oldTE;
    }

    private class_2499 convertEntities_to_1_20_5(class_2499 oldEntitiesList, int minecraftDataVersion) {
        if (minecraftDataVersion < Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue()) {
            minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        }
        if (minecraftDataVersion < MINECRAFT_DATA_VERSION) {
            Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
            if (effective == null) {
                Litematica.LOGGER.warn("LitematicaSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Entities DataVersion {}", (Object)minecraftDataVersion);
                return oldEntitiesList;
            }
            class_2499 newEntitiesList = new class_2499();
            int size = oldEntitiesList.size();
            Litematica.LOGGER.info("LitematicaSchematic: executing Vanilla DataFixer for Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
            for (int i = 0; i < size; ++i) {
                newEntitiesList.add((Object)SchematicConversionMaps.updateEntity(oldEntitiesList.method_10602(i), minecraftDataVersion));
            }
            return newEntitiesList;
        }
        return oldEntitiesList;
    }

    private List<EntityInfo> convertSpongeEntities_to_1_20_5(List<EntityInfo> oldEntitiesList, int minecraftDataVersion) {
        if (minecraftDataVersion < Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue()) {
            minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        }
        if (minecraftDataVersion < MINECRAFT_DATA_VERSION) {
            Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
            if (effective == null) {
                Litematica.LOGGER.warn("SpongeSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Entities DataVersion {}", (Object)minecraftDataVersion);
                return oldEntitiesList;
            }
            ArrayList<EntityInfo> newEntitiesList = new ArrayList<EntityInfo>();
            Litematica.LOGGER.info("SpongeSchematic: executing Vanilla DataFixer for Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)MINECRAFT_DATA_VERSION);
            for (EntityInfo oldEntityInfo : oldEntitiesList) {
                newEntitiesList.add(new EntityInfo(oldEntityInfo.posVec, SchematicConversionMaps.updateEntity(oldEntityInfo.nbt, minecraftDataVersion)));
            }
            return newEntitiesList;
        }
        return oldEntitiesList;
    }

    private Map<class_2338, class_2487> downgradeTileEntities_to_1_20_4(Map<class_2338, class_2487> oldTE, int minecraftDataVersion) {
        HashMap<class_2338, class_2487> newTE = new HashMap<class_2338, class_2487>();
        Litematica.LOGGER.info("LitematicaSchematic: Downgrade Tile Entities from DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)3700);
        for (class_2338 key : oldTE.keySet()) {
            newTE.put(key, SchematicDowngradeConverter.downgradeBlockEntity_to_1_20_4(oldTE.get(key), minecraftDataVersion, class_310.method_1551().field_1687.method_30349()));
        }
        return newTE;
    }

    private class_2499 downgradeEntities_to_1_20_4(class_2499 oldEntitiesList, int minecraftDataVersion) {
        class_2499 newEntitiesList = new class_2499();
        int size = oldEntitiesList.size();
        Litematica.LOGGER.info("LitematicaSchematic: Downgrade Entities from DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)3700);
        for (int i = 0; i < size; ++i) {
            newEntitiesList.add((Object)SchematicDowngradeConverter.downgradeEntity_to_1_20_4(SchematicConversionMaps.fixEntityTypesFrom1_21_2(oldEntitiesList.method_10602(i)), minecraftDataVersion, class_310.method_1551().field_1687.method_30349()));
        }
        return newEntitiesList;
    }

    private List<EntityInfo> readEntitiesFromNBT(class_2499 tagList) {
        ArrayList<EntityInfo> entityList = new ArrayList<EntityInfo>();
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 entityData = tagList.method_10602(i);
            class_243 posVec = NbtUtils.readEntityPositionFromTag((class_2487)entityData);
            if (posVec == null || entityData.method_33133()) continue;
            entityList.add(new EntityInfo(posVec, entityData));
        }
        return entityList;
    }

    private Map<class_2338, class_2487> readTileEntitiesFromNBT(class_2499 tagList) {
        HashMap<class_2338, class_2487> tileMap = new HashMap<class_2338, class_2487>();
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = tagList.method_10602(i);
            class_2338 pos = NbtUtils.readBlockPos((class_2487)tag);
            if (pos == null || tag.method_33133()) continue;
            tileMap.put(pos, tag);
        }
        return tileMap;
    }

    private <T> Map<class_2338, class_6760<T>> readPendingTicksFromNBT(class_2499 tagList, class_2378<T> registry, String tagName, T emptyValue) {
        HashMap<class_2338, class_6760<T>> tickMap = new HashMap<class_2338, class_6760<T>>();
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = tagList.method_10602(i);
            if (!tag.method_10573("Time", 99)) continue;
            Object target = null;
            try {
                Optional opt = registry.method_10223(class_2960.method_12829((String)tag.method_10558(tagName)));
                if (!opt.isPresent() || !((class_6880.class_6883)opt.get()).method_40227()) continue;
                target = ((class_6880.class_6883)opt.get()).comp_349();
            }
            catch (Exception opt) {
                // empty catch block
            }
            if (target == null) continue;
            class_2338 pos = new class_2338(tag.method_10550("x"), tag.method_10550("y"), tag.method_10550("z"));
            class_1953 priority = class_1953.method_8680((int)tag.method_10550("Priority"));
            int scheduledTime = tag.method_10550("Time");
            long subTick = tag.method_10537("SubTick");
            tickMap.put(pos, new class_6760(target, pos, (long)scheduledTime, priority, subTick));
        }
        return tickMap;
    }

    private List<EntityInfo> readEntitiesFromNBT_v1(class_2499 tagList) {
        ArrayList<EntityInfo> entityList = new ArrayList<EntityInfo>();
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = tagList.method_10602(i);
            class_243 posVec = NbtUtils.readVec3d((class_2487)tag);
            class_2487 entityData = tag.method_10562("EntityData");
            if (posVec == null || entityData.method_33133()) continue;
            NbtUtils.writeEntityPositionToTag((class_243)posVec, (class_2487)entityData);
            entityList.add(new EntityInfo(posVec, entityData));
        }
        return entityList;
    }

    private Map<class_2338, class_2487> readTileEntitiesFromNBT_v1(class_2499 tagList) {
        HashMap<class_2338, class_2487> tileMap = new HashMap<class_2338, class_2487>();
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = tagList.method_10602(i);
            class_2487 tileNbt = tag.method_10562("TileNBT");
            class_2338 pos = NbtUtils.readBlockPos((class_2487)tag);
            if (pos == null || tileNbt.method_33133()) continue;
            NbtUtils.writeBlockPos((class_2338)pos, (class_2487)tileNbt);
            tileMap.put(pos, tileNbt);
        }
        return tileMap;
    }

    public boolean writeToFile(File dir, String fileNameIn, boolean override) {
        return this.writeToFile(dir, fileNameIn, override, false);
    }

    public boolean writeToFile(File dir, String fileNameIn, boolean override, boolean downgrade) {
        Object fileName = fileNameIn;
        if (!((String)fileName).endsWith(FILE_EXTENSION)) {
            fileName = (String)fileName + FILE_EXTENSION;
        }
        File fileSchematic = new File(dir, (String)fileName);
        try {
            if (!dir.exists() && !dir.mkdirs()) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_write_to_file_failed.directory_creation_failed", (Object[])new Object[]{dir.getAbsolutePath()});
                return false;
            }
            if (!override && fileSchematic.exists()) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_write_to_file_failed.exists", (Object[])new Object[]{fileSchematic.getAbsolutePath()});
                return false;
            }
            FileOutputStream os = new FileOutputStream(fileSchematic);
            if (downgrade) {
                class_2507.method_10634((class_2487)this.writeToNBT_v6(), (OutputStream)os);
            } else {
                class_2507.method_10634((class_2487)this.writeToNBT(), (OutputStream)os);
            }
            os.close();
            return true;
        }
        catch (Exception e) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_write_to_file_failed.exception", (Object[])new Object[]{fileSchematic.getAbsolutePath()});
            Litematica.LOGGER.error(StringUtils.translate((String)"litematica.error.schematic_write_to_file_failed.exception", (Object[])new Object[]{fileSchematic.getAbsolutePath()}), (Throwable)e);
            Litematica.LOGGER.error(e.getMessage());
            return false;
        }
    }

    public boolean readFromFile() {
        return this.readFromFile(this.schematicType);
    }

    private boolean readFromFile(FileType schematicType) {
        try {
            class_2487 nbt = LitematicaSchematic.readNbtFromFile(this.schematicFile);
            if (nbt != null) {
                if (schematicType == FileType.SPONGE_SCHEMATIC) {
                    String name = FileNameUtils.getFileNameWithoutExtension((String)this.schematicFile.getName()) + " (Converted Sponge)";
                    return this.readFromSpongeSchematic(name, nbt);
                }
                if (schematicType == FileType.VANILLA_STRUCTURE) {
                    String name = FileNameUtils.getFileNameWithoutExtension((String)this.schematicFile.getName()) + " (Converted Structure)";
                    return this.readFromVanillaStructure(name, nbt);
                }
                if (schematicType == FileType.LITEMATICA_SCHEMATIC) {
                    return this.readFromNBT(nbt);
                }
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.cant_read", (Object[])new Object[]{this.schematicFile.getAbsolutePath()});
            }
        }
        catch (Exception e) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.exception", (Object[])new Object[]{this.schematicFile.getAbsolutePath()});
            Litematica.LOGGER.error((Object)e);
        }
        return false;
    }

    public static class_2487 readNbtFromFile(File file) {
        if (file == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.no_file", (Object[])new Object[0]);
            return null;
        }
        if (!file.exists() || !file.canRead()) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.cant_read", (Object[])new Object[]{file.getAbsolutePath()});
            return null;
        }
        return NbtUtils.readNbtFromFileAsPath((Path)file.toPath());
    }

    public static class_2487 readNbtFromPath(Path path) {
        if (path == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.no_file", (Object[])new Object[0]);
            return null;
        }
        if (!Files.exists(path, new LinkOption[0]) || !Files.isReadable(path)) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_read_from_file_failed.cant_read", (Object[])new Object[]{path.toString()});
            return null;
        }
        return NbtUtils.readNbtFromFileAsPath((Path)path);
    }

    public static File fileFromDirAndName(File dir, String fileName, FileType schematicType) {
        if (!((String)fileName).endsWith(FILE_EXTENSION) && schematicType == FileType.LITEMATICA_SCHEMATIC) {
            fileName = (String)fileName + FILE_EXTENSION;
        }
        return new File(dir, (String)fileName);
    }

    public static Path fileFromDirAndName(Path dir, String fileName, FileType schematicType) {
        if (!((String)fileName).endsWith(FILE_EXTENSION) && schematicType == FileType.LITEMATICA_SCHEMATIC) {
            fileName = (String)fileName + FILE_EXTENSION;
        }
        return dir.resolve((String)fileName);
    }

    public static void updateMetadataWithFileTime(Path file, SchematicMetadata metadata) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class, new LinkOption[0]);
            metadata.setTimeCreated(attr.creationTime().toMillis());
            metadata.setTimeModified(attr.lastModifiedTime().toMillis());
        }
        catch (Exception err) {
            Litematica.LOGGER.error("getFileCreatedTime(): Exception reading file '{}'; {}", (Object)file.getFileName().toString(), (Object)err.getLocalizedMessage());
        }
    }

    @Nullable
    public static SchematicMetadata readMetadataFromFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        FileType type = FileType.fromFile(file);
        if (type == FileType.INVALID) {
            file = LitematicaSchematic.fileFromDirAndName(dir, fileName, FileType.LITEMATICA_SCHEMATIC);
            type = FileType.fromFile(file);
        }
        if (type == FileType.INVALID) {
            return null;
        }
        class_2487 nbt = LitematicaSchematic.readNbtFromFile(file);
        if (nbt != null) {
            switch (type) {
                case LITEMATICA_SCHEMATIC: {
                    int version;
                    SchematicMetadata metadata = new SchematicMetadata();
                    if (!nbt.method_10573("Version", 3) || (version = nbt.method_10550("Version")) < 1 || version > 7) break;
                    metadata.readFromNBT(nbt.method_10562("Metadata"));
                    metadata.setFileType(type);
                    return metadata;
                }
                case SPONGE_SCHEMATIC: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    if (!schem.readFromSpongeSchematicMetadataOnly(fileName, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return meta;
                }
                case VANILLA_STRUCTURE: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    if (!schem.readFromVanillaStructureMetadataOnly(fileName, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return meta;
                }
                case SCHEMATICA_SCHEMATIC: {
                    SchematicaSchematic schem = new SchematicaSchematic();
                    if (!schem.readBlocksFromNBTMetadataOnly(file, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return meta;
                }
            }
        }
        return null;
    }

    @Nullable
    public static Pair<SchematicSchema, SchematicMetadata> readMetadataAndVersionFromFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        FileType type = FileType.fromFile(file);
        if (type == FileType.INVALID) {
            file = LitematicaSchematic.fileFromDirAndName(dir, fileName, FileType.LITEMATICA_SCHEMATIC);
            type = FileType.fromFile(file);
        }
        if (type == FileType.INVALID) {
            return null;
        }
        class_2487 nbt = LitematicaSchematic.readNbtFromFile(file);
        if (nbt != null) {
            switch (type) {
                case LITEMATICA_SCHEMATIC: {
                    int dataVersion;
                    SchematicMetadata metadata = new SchematicMetadata();
                    if (!nbt.method_10573("Version", 3)) break;
                    int version = nbt.method_10550("Version");
                    int n = dataVersion = nbt.method_10545("MinecraftDataVersion") ? nbt.method_10550("MinecraftDataVersion") : -1;
                    if (version < 1 || version > 7) break;
                    metadata.readFromNBT(nbt.method_10562("Metadata"));
                    metadata.setFileType(type);
                    return Pair.of((Object)new SchematicSchema(version, dataVersion), (Object)metadata);
                }
                case SPONGE_SCHEMATIC: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    if (!schem.readFromSpongeSchematicMetadataOnly(fileName, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return Pair.of((Object)meta.getSchematicSchema(), (Object)meta);
                }
                case VANILLA_STRUCTURE: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    if (!schem.readFromVanillaStructureMetadataOnly(fileName, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return Pair.of((Object)meta.getSchematicSchema(), (Object)meta);
                }
                case SCHEMATICA_SCHEMATIC: {
                    SchematicaSchematic schem = new SchematicaSchematic();
                    if (!schem.readBlocksFromNBTMetadataOnly(file, nbt)) break;
                    SchematicMetadata meta = schem.getMetadata();
                    LitematicaSchematic.updateMetadataWithFileTime(file.toPath(), meta);
                    return Pair.of((Object)meta.getSchematicSchema(), (Object)meta);
                }
            }
        }
        return null;
    }

    @Nullable
    public static Pair<SchematicSchema, SchematicMetadata> readMetadataAndVersionFromFile(Path dir, String fileName) {
        Path file = dir.resolve(fileName);
        FileType type = FileType.fromFile(file);
        if (type == FileType.INVALID) {
            file = LitematicaSchematic.fileFromDirAndName(dir, fileName, FileType.LITEMATICA_SCHEMATIC);
            type = FileType.fromFile(file);
        }
        if (type == FileType.INVALID) {
            return null;
        }
        class_2487 nbt = LitematicaSchematic.readNbtFromPath(file);
        if (nbt != null) {
            switch (type) {
                case LITEMATICA_SCHEMATIC: {
                    int dataVersion;
                    SchematicMetadata metadata = new SchematicMetadata();
                    if (!nbt.method_10545("Version")) break;
                    int version = nbt.method_10550("Version");
                    int n = dataVersion = nbt.method_10545("MinecraftDataVersion") ? nbt.method_10550("MinecraftDataVersion") : -1;
                    if (version < 1 || version > 7) break;
                    metadata.readFromNBT(nbt.method_10562("Metadata"));
                    metadata.setFileType(type);
                    return Pair.of((Object)new SchematicSchema(version, dataVersion), (Object)metadata);
                }
                case SPONGE_SCHEMATIC: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    DataFixerMode dataFixer = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
                    Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.NEVER);
                    if (schem.readFromSpongeSchematic(fileName, nbt)) {
                        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)dataFixer);
                        return Pair.of((Object)schem.getMetadata().getSchematicSchema(), (Object)schem.getMetadata());
                    }
                    Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)dataFixer);
                    break;
                }
                case VANILLA_STRUCTURE: {
                    LitematicaSchematic schem = new LitematicaSchematic(file, type);
                    DataFixerMode dataFixer = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
                    Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.NEVER);
                    if (schem.readFromVanillaStructure(fileName, nbt)) {
                        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)dataFixer);
                        return Pair.of((Object)schem.getMetadata().getSchematicSchema(), (Object)schem.getMetadata());
                    }
                    Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)dataFixer);
                }
            }
        }
        return null;
    }

    @Nullable
    public static SchematicSchema readDataVersionFromFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        FileType type = FileType.fromFile(file);
        if (type == FileType.INVALID) {
            file = LitematicaSchematic.fileFromDirAndName(dir, fileName, FileType.LITEMATICA_SCHEMATIC);
            type = FileType.fromFile(file);
        }
        if (type == FileType.INVALID) {
            return null;
        }
        class_2487 nbt = LitematicaSchematic.readNbtFromFile(file);
        if (nbt != null) {
            switch (type) {
                case LITEMATICA_SCHEMATIC: {
                    int dataVersion;
                    if (!nbt.method_10573("Version", 3)) break;
                    int version = nbt.method_10550("Version");
                    int n = dataVersion = nbt.method_10545("MinecraftDataVersion") ? nbt.method_10550("MinecraftDataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
                    if (version < 1) break;
                    return new SchematicSchema(version, dataVersion);
                }
                case SPONGE_SCHEMATIC: {
                    class_2487 spongeTag = new class_2487();
                    if (LitematicaSchematic.isValidSpongeSchematicv3(nbt)) {
                        spongeTag.method_10543(nbt.method_10562("Schematic"));
                    } else if (LitematicaSchematic.isValidSpongeSchematic(nbt)) {
                        spongeTag.method_10543(nbt);
                    }
                    int spongeVersion = spongeTag.method_10545("Version") ? spongeTag.method_10550("Version") : -1;
                    int minecraftDataVersion = spongeTag.method_10545("DataVersion") ? spongeTag.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
                    return new SchematicSchema(spongeVersion, minecraftDataVersion);
                }
                case VANILLA_STRUCTURE: {
                    int minecraftDataVersion = nbt.method_10545("DataVersion") ? nbt.method_10550("DataVersion") : Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
                    return new SchematicSchema(0, minecraftDataVersion);
                }
            }
        }
        return null;
    }

    @Nullable
    public static LitematicaSchematic createFromFile(File dir, String fileName) {
        return LitematicaSchematic.createFromFile(dir, fileName, FileType.LITEMATICA_SCHEMATIC);
    }

    @Nullable
    public static LitematicaSchematic createFromFile(File dir, String fileName, FileType schematicType) {
        File file = LitematicaSchematic.fileFromDirAndName(dir, fileName, schematicType);
        LitematicaSchematic schematic = new LitematicaSchematic(file, schematicType);
        return schematic.readFromFile(schematicType) ? schematic : null;
    }

    @Nullable
    public static LitematicaSchematic createFromFile(Path dir, String fileName, FileType schematicType) {
        Path file = LitematicaSchematic.fileFromDirAndName(dir, fileName, schematicType);
        LitematicaSchematic schematic = new LitematicaSchematic(file, schematicType);
        return schematic.readFromFile(schematicType) ? schematic : null;
    }

    public static class SchematicSaveInfo {
        public final boolean visibleOnly;
        public final boolean includeSupportBlocks;
        public final boolean ignoreEntities;
        public final boolean fromSchematicWorld;

        public SchematicSaveInfo(boolean visibleOnly, boolean ignoreEntities) {
            this(visibleOnly, false, ignoreEntities, false);
        }

        public SchematicSaveInfo(boolean visibleOnly, boolean includeSupportBlocks, boolean ignoreEntities, boolean fromSchematicWorld) {
            this.visibleOnly = visibleOnly;
            this.includeSupportBlocks = includeSupportBlocks;
            this.ignoreEntities = ignoreEntities;
            this.fromSchematicWorld = fromSchematicWorld;
        }
    }

    public static class EntityInfo {
        public final class_243 posVec;
        public final class_2487 nbt;

        public EntityInfo(class_243 posVec, class_2487 nbt) {
            this.posVec = posVec;
            if (nbt.method_10573("SleepingX", 3)) {
                nbt.method_10569("SleepingX", class_3532.method_15357((double)posVec.field_1352));
            }
            if (nbt.method_10573("SleepingY", 3)) {
                nbt.method_10569("SleepingY", class_3532.method_15357((double)posVec.field_1351));
            }
            if (nbt.method_10573("SleepingZ", 3)) {
                nbt.method_10569("SleepingZ", class_3532.method_15357((double)posVec.field_1350));
            }
            this.nbt = nbt;
        }

        public class_243 toVanilla() {
            return this.posVec;
        }
    }
}

