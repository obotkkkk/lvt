/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.Schema
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1263
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_243
 *  net.minecraft.class_2470
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2776
 *  net.minecraft.class_2818
 *  net.minecraft.class_2818$class_2819
 *  net.minecraft.class_3492
 *  net.minecraft.class_3499
 *  net.minecraft.class_7225$class_7874
 */
package fi.dy.masa.litematica.schematic;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionFixers;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import fi.dy.masa.litematica.schematic.conversion.SchematicConverter;
import fi.dy.masa.litematica.util.DataFixerMode;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.Schema;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.class_1263;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2776;
import net.minecraft.class_2818;
import net.minecraft.class_3492;
import net.minecraft.class_3499;
import net.minecraft.class_7225;

public class SchematicaSchematic {
    public static final String FILE_EXTENSION = ".schematic";
    private final SchematicMetadata metadata = new SchematicMetadata();
    private final SchematicConverter converter;
    private final class_2680[] palette = new class_2680[65536];
    private LitematicaBlockStateContainer blocks;
    private Map<class_2338, class_2487> tiles = new HashMap<class_2338, class_2487>();
    private List<class_2487> entities = new ArrayList<class_2487>();
    private class_2382 size = class_2382.field_11176;
    private String fileName;
    private IdentityHashMap<class_2680, SchematicConversionFixers.IStateFixer> postProcessingFilter;
    private boolean needsConversionPostProcessing;

    protected SchematicaSchematic() {
        this.converter = SchematicConverter.createForSchematica();
    }

    public SchematicMetadata getMetadata() {
        return this.metadata;
    }

    public class_2382 getSize() {
        return this.size;
    }

    public Map<class_2338, class_2487> getTiles() {
        return this.tiles;
    }

    public List<LitematicaSchematic.EntityInfo> getEntities() {
        ArrayList<LitematicaSchematic.EntityInfo> entityList = new ArrayList<LitematicaSchematic.EntityInfo>();
        int size = this.entities.size();
        for (int i = 0; i < size; ++i) {
            class_2487 entityData = this.entities.get(i);
            class_243 posVec = NbtUtils.readEntityPositionFromTag((class_2487)entityData);
            if (posVec == null || entityData.method_33133()) continue;
            entityList.add(new LitematicaSchematic.EntityInfo(posVec, entityData));
        }
        return entityList;
    }

    public void placeSchematicToWorld(class_1937 world, class_2338 posStart, class_3492 placement, int setBlockStateFlags) {
        int width = this.size.method_10263();
        int height = this.size.method_10264();
        int length = this.size.method_10260();
        int numBlocks = width * height * length;
        if (this.blocks != null && numBlocks > 0 && this.blocks.getSize().equals((Object)this.size)) {
            int x;
            int z;
            int y;
            class_2470 rotation = placement.method_15113();
            class_2415 mirror = placement.method_15114();
            for (y = 0; y < height; ++y) {
                for (z = 0; z < length; ++z) {
                    for (x = 0; x < width; ++x) {
                        class_2586 te;
                        class_2680 state = this.blocks.get(x, y, z);
                        class_2338 pos = new class_2338(x, y, z);
                        class_2487 teNBT = this.tiles.get(pos);
                        pos = class_3499.method_15171((class_3492)placement, (class_2338)pos).method_10081((class_2382)posStart);
                        state = state.method_26185(mirror);
                        state = state.method_26186(rotation);
                        if (teNBT != null && (te = world.method_8321(pos)) != null) {
                            if (te instanceof class_1263) {
                                ((class_1263)te).method_5448();
                            }
                            world.method_8652(pos, class_2246.field_10499.method_9564(), 20);
                        }
                        if (!world.method_8652(pos, state, setBlockStateFlags) || teNBT == null || (te = world.method_8321(pos)) == null) continue;
                        teNBT.method_10569("x", pos.method_10263());
                        teNBT.method_10569("y", pos.method_10264());
                        teNBT.method_10569("z", pos.method_10260());
                        try {
                            te.method_58690(teNBT, (class_7225.class_7874)world.method_30349());
                            continue;
                        }
                        catch (Exception e) {
                            Litematica.LOGGER.warn("Failed to load TileEntity data for {} @ {}", (Object)state, (Object)pos);
                        }
                    }
                }
            }
            if ((setBlockStateFlags & 1) != 0) {
                for (y = 0; y < height; ++y) {
                    for (z = 0; z < length; ++z) {
                        for (x = 0; x < width; ++x) {
                            class_2586 te;
                            class_2338 pos = new class_2338(x, y, z);
                            class_2487 teNBT = this.tiles.get(pos);
                            pos = class_3499.method_15171((class_3492)placement, (class_2338)pos).method_10081((class_2382)posStart);
                            world.method_8408(pos, world.method_8320(pos).method_26204());
                            if (teNBT == null || (te = world.method_8321(pos)) == null) continue;
                            te.method_5431();
                        }
                    }
                }
            }
            if (!placement.method_15135()) {
                this.addEntitiesToWorld(world, posStart, placement);
            }
        }
    }

    public void placeSchematicDirectlyToChunks(class_1937 world, class_2338 posStart, class_3492 placement) {
        int width = this.size.method_10263();
        int height = this.size.method_10264();
        int length = this.size.method_10260();
        int numBlocks = width * height * length;
        class_2338 posEnd = posStart.method_10081(this.size).method_10069(-1, -1, -1);
        if (this.blocks != null && numBlocks > 0 && this.blocks.getSize().equals((Object)this.size) && PositionUtils.arePositionsWithinWorld(world, posStart, posEnd)) {
            class_2338 posMin = PositionUtils.getMinCorner(posStart, posEnd);
            class_2338 posMax = PositionUtils.getMaxCorner(posStart, posEnd);
            int cxStart = posMin.method_10263() >> 4;
            int czStart = posMin.method_10260() >> 4;
            int cxEnd = posMax.method_10263() >> 4;
            int czEnd = posMax.method_10260() >> 4;
            class_2338.class_2339 posMutable = new class_2338.class_2339();
            for (int cz = czStart; cz <= czEnd; ++cz) {
                for (int cx = cxStart; cx <= cxEnd; ++cx) {
                    int xMinChunk = Math.max(cx << 4, posMin.method_10263());
                    int zMinChunk = Math.max(cz << 4, posMin.method_10260());
                    int xMaxChunk = Math.min((cx << 4) + 15, posMax.method_10263());
                    int zMaxChunk = Math.min((cz << 4) + 15, posMax.method_10260());
                    class_2818 chunk = world.method_8497(cx, cz);
                    if (chunk == null) continue;
                    int y = posMin.method_10264();
                    for (int ySrc = 0; ySrc < height; ++ySrc) {
                        int z = zMinChunk;
                        int zSrc = zMinChunk - posStart.method_10260();
                        while (z <= zMaxChunk) {
                            int x = xMinChunk;
                            int xSrc = xMinChunk - posStart.method_10263();
                            while (x <= xMaxChunk) {
                                class_2586 te;
                                class_2680 state = this.blocks.get(xSrc, ySrc, zSrc);
                                posMutable.method_10103(xSrc, ySrc, zSrc);
                                class_2487 teNBT = this.tiles.get(posMutable);
                                class_2338 pos = new class_2338(x, y, z);
                                if (teNBT != null && (te = chunk.method_12201(pos, class_2818.class_2819.field_12859)) != null) {
                                    if (te instanceof class_1263) {
                                        ((class_1263)te).method_5448();
                                    }
                                    world.method_8652(pos, class_2246.field_10499.method_9564(), 20);
                                }
                                chunk.method_12010(pos, state, false);
                                if (teNBT != null && (te = chunk.method_12201(pos, class_2818.class_2819.field_12859)) != null) {
                                    teNBT.method_10569("x", pos.method_10263());
                                    teNBT.method_10569("y", pos.method_10264());
                                    teNBT.method_10569("z", pos.method_10260());
                                    try {
                                        te.method_58690(teNBT, (class_7225.class_7874)world.method_30349());
                                    }
                                    catch (Exception e) {
                                        Litematica.LOGGER.warn("Failed to load TileEntity data for {} @ {}", (Object)state, (Object)pos);
                                    }
                                }
                                ++x;
                                ++xSrc;
                            }
                            ++z;
                            ++zSrc;
                        }
                        ++y;
                    }
                }
            }
            if (!placement.method_15135()) {
                this.addEntitiesToWorld(world, posStart, placement);
            }
        }
    }

    private void addEntitiesToWorld(class_1937 world, class_2338 posStart, class_3492 placement) {
        class_2415 mirror = placement.method_15114();
        class_2470 rotation = placement.method_15113();
        for (class_2487 tag : this.entities) {
            class_243 relativePos = NbtUtils.readEntityPositionFromTag((class_2487)tag);
            if (relativePos == null) continue;
            class_243 transformedRelativePos = PositionUtils.getTransformedPosition(relativePos, mirror, rotation);
            class_243 realPos = transformedRelativePos.method_1031((double)posStart.method_10263(), (double)posStart.method_10264(), (double)posStart.method_10260());
            class_1297 entity = EntityUtils.createEntityAndPassengersFromNBT(tag, world);
            if (entity == null) continue;
            float rotationYaw = entity.method_5763(mirror);
            entity.method_5808(realPos.field_1352, realPos.field_1351, realPos.field_1350, rotationYaw += entity.method_36454() - entity.method_5832(rotation), entity.method_36455());
            EntityUtils.spawnEntityAndPassengersInWorld(entity, world);
        }
    }

    public Map<class_2338, String> getDataStructureBlocks(class_2338 posStart, class_3492 placement) {
        HashMap<class_2338, String> map = new HashMap<class_2338, String>();
        for (Map.Entry<class_2338, class_2487> entry : this.tiles.entrySet()) {
            class_2487 tag = entry.getValue();
            if (!tag.method_10558("id").equals("minecraft:structure_block") || class_2776.valueOf((String)tag.method_10558("mode")) != class_2776.field_12696) continue;
            class_2338 pos = entry.getKey();
            pos = class_3499.method_15171((class_3492)placement, (class_2338)pos).method_10081((class_2382)posStart);
            map.put(pos, tag.method_10558("metadata"));
        }
        return map;
    }

    private void readBlocksFromWorld(class_1937 world, class_2338 posStart, class_2338 size) {
        int startX = posStart.method_10263();
        int startY = posStart.method_10264();
        int startZ = posStart.method_10260();
        int endX = startX + size.method_10263();
        int endY = startY + size.method_10264();
        int endZ = startZ + size.method_10260();
        class_2338.class_2339 posMutable = new class_2338.class_2339(0, 0, 0);
        this.blocks = new LitematicaBlockStateContainer(size.method_10263(), size.method_10264(), size.method_10260());
        this.tiles.clear();
        this.size = size;
        for (int y = startY; y < endY; ++y) {
            for (int z = startZ; z < endZ; ++z) {
                for (int x = startX; x < endX; ++x) {
                    int relX = x - startX;
                    int relY = y - startY;
                    int relZ = z - startZ;
                    posMutable.method_10103(x, y, z);
                    class_2680 state = world.method_8320((class_2338)posMutable);
                    this.blocks.set(relX, relY, relZ, state);
                    class_2586 te = world.method_8321((class_2338)posMutable);
                    if (te == null) continue;
                    try {
                        class_2487 nbt = te.method_38243((class_7225.class_7874)world.method_30349());
                        class_2338 pos = new class_2338(relX, relY, relZ);
                        NbtUtils.writeBlockPosToTag((class_2338)pos, (class_2487)nbt);
                        this.tiles.put(pos, nbt);
                        continue;
                    }
                    catch (Exception e) {
                        Litematica.LOGGER.warn("SchematicaSchematic: Exception while trying to store TileEntity data for block '{}' at {}", (Object)state, (Object)posMutable.toString(), (Object)e);
                    }
                }
            }
        }
    }

    private void readEntitiesFromWorld(class_1937 world, class_2338 posStart, class_2338 size) {
        this.entities.clear();
        List entities = world.method_8333(null, PositionUtils.createEnclosingAABB(posStart, posStart.method_10081((class_2382)size)), e -> !(e instanceof class_1657));
        for (class_1297 entity : entities) {
            class_2487 tag;
            if (!entity.method_5662(tag = new class_2487())) continue;
            class_243 pos = new class_243(entity.method_23317() - (double)posStart.method_10263(), entity.method_23318() - (double)posStart.method_10264(), entity.method_23321() - (double)posStart.method_10260());
            NbtUtils.writeEntityPositionToTag((class_243)pos, (class_2487)tag);
            this.entities.add(tag);
        }
    }

    public static SchematicaSchematic createFromWorld(class_1937 world, class_2338 posStart, class_2338 size, boolean ignoreEntities) {
        SchematicaSchematic schematic = new SchematicaSchematic();
        schematic.readBlocksFromWorld(world, posStart, size);
        if (!ignoreEntities) {
            schematic.readEntitiesFromWorld(world, posStart, size);
        }
        return schematic;
    }

    @Nullable
    public static SchematicaSchematic createFromFile(File file) {
        SchematicaSchematic schematic = new SchematicaSchematic();
        if (schematic.readFromFile(file)) {
            schematic.metadata.setName(file.getName());
            return schematic;
        }
        return null;
    }

    public boolean readFromNBT(class_2487 nbt) {
        if (this.readBlocksFromNBT(nbt)) {
            this.readEntitiesFromNBT(nbt);
            this.readTileEntitiesFromNBT(nbt);
            try {
                this.postProcessBlocks();
            }
            catch (Exception e) {
                Litematica.LOGGER.error("SchematicaSchematic: Exception while post-processing blocks for '{}'", (Object)this.fileName, (Object)e);
            }
            return true;
        }
        Litematica.LOGGER.error("SchematicaSchematic: Missing block data in the schematic '{}'", (Object)this.fileName);
        return false;
    }

    private boolean readPaletteFromNBT(class_2487 nbt) {
        Arrays.fill(this.palette, class_2246.field_10124.method_9564());
        if (nbt.method_10573("SchematicaMapping", 10)) {
            class_2487 tag = nbt.method_10562("SchematicaMapping");
            Set keys = tag.method_10541();
            for (String key : keys) {
                String str;
                short id = tag.method_10568(key);
                if (id < 0 || id >= 4096) {
                    str = String.format("SchematicaSchematic: Invalid ID '%d' in SchematicaMapping for block '%s', range: 0 - 4095", id, key);
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
                    Litematica.LOGGER.warn(str);
                    return false;
                }
                if (this.converter.getConvertedStatesForBlock(id, key, this.palette)) continue;
                str = String.format("SchematicaSchematic: Missing/non-existing block '%s' in SchematicaMapping", key);
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
                Litematica.LOGGER.warn(str);
            }
        } else if (nbt.method_10573("BlockIDs", 10)) {
            class_2487 tag = nbt.method_10562("BlockIDs");
            Set keys = tag.method_10541();
            for (String idStr : keys) {
                String str;
                int id;
                String key = tag.method_10558(idStr);
                try {
                    id = Integer.parseInt(idStr);
                }
                catch (NumberFormatException e) {
                    String str2 = String.format("SchematicaSchematic: Invalid ID '%d' (not a number) in MCEdit2 palette for block '%s'", idStr, key);
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str2, (Object[])new Object[0]);
                    Litematica.LOGGER.warn(str2);
                    return false;
                }
                if (id < 0 || id >= 4096) {
                    str = String.format("SchematicaSchematic: Invalid ID '%d' in MCEdit2 palette for block '%s', range: 0 - 4095", id, key);
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
                    Litematica.LOGGER.warn(str);
                    return false;
                }
                if (this.converter.getConvertedStatesForBlock(id, key, this.palette)) continue;
                str = String.format("SchematicaSchematic: Missing/non-existing block '%s' in MCEdit2 palette", key);
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
                Litematica.LOGGER.warn(str);
            }
        } else {
            this.converter.getVanillaBlockPalette(this.palette);
        }
        if (this.converter.createPostProcessStateFilter(this.palette)) {
            this.postProcessingFilter = this.converter.getPostProcessStateFilter();
            this.needsConversionPostProcessing = true;
        }
        return true;
    }

    protected boolean readBlocksFromNBTMetadataOnly(File file, class_2487 nbt) {
        if (!(nbt.method_10545("Blocks") && nbt.method_10545("Data") && nbt.method_10545("Width") && nbt.method_10545("Height") && nbt.method_10545("Length"))) {
            return false;
        }
        this.fileName = file.getName();
        short sizeX = nbt.method_10568("Width");
        short sizeY = nbt.method_10568("Height");
        short sizeZ = nbt.method_10568("Length");
        byte[] blockIdsByte = nbt.method_10547("Blocks");
        byte[] metaArr = nbt.method_10547("Data");
        int numBlocks = blockIdsByte.length;
        int layerSize = sizeX * sizeZ;
        if (numBlocks != sizeX * sizeY * sizeZ) {
            String str = String.format("SchematicaSchematic: Mismatched block array size compared to the width/height/length,\nblocks: %d, W x H x L: %d x %d x %d", numBlocks, (int)sizeX, (int)sizeY, (int)sizeZ);
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
            return false;
        }
        if (numBlocks != metaArr.length) {
            String str = String.format("SchematicaSchematic: Mismatched block ID and metadata array sizes, blocks: %d, meta: %d", numBlocks, metaArr.length);
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
            return false;
        }
        this.size = new class_2382((int)sizeX, (int)sizeY, (int)sizeZ);
        this.metadata.setEnclosingSize(this.size);
        this.metadata.setTotalBlocks(numBlocks);
        this.metadata.setTotalVolume(sizeX * sizeY * sizeZ);
        this.metadata.setRegionCount(1);
        this.metadata.setFileType(FileType.SCHEMATICA_SCHEMATIC);
        return true;
    }

    private boolean readBlocksFromNBT(class_2487 nbt) {
        int z;
        int y;
        int x;
        class_2680 state;
        byte addValue;
        int expectedAddLength;
        if (!(nbt.method_10573("Blocks", 7) && nbt.method_10573("Data", 7) && nbt.method_10573("Width", 2) && nbt.method_10573("Height", 2) && nbt.method_10573("Length", 2))) {
            return false;
        }
        short sizeX = nbt.method_10568("Width");
        short sizeY = nbt.method_10568("Height");
        short sizeZ = nbt.method_10568("Length");
        byte[] blockIdsByte = nbt.method_10547("Blocks");
        byte[] metaArr = nbt.method_10547("Data");
        int numBlocks = blockIdsByte.length;
        int layerSize = sizeX * sizeZ;
        if (numBlocks != sizeX * sizeY * sizeZ) {
            String str = String.format("SchematicaSchematic: Mismatched block array size compared to the width/height/length,\nblocks: %d, W x H x L: %d x %d x %d", numBlocks, (int)sizeX, (int)sizeY, (int)sizeZ);
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
            return false;
        }
        if (numBlocks != metaArr.length) {
            String str = String.format("SchematicaSchematic: Mismatched block ID and metadata array sizes, blocks: %d, meta: %d", numBlocks, metaArr.length);
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
            return false;
        }
        if (!this.readPaletteFromNBT(nbt)) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"SchematicaSchematic: Failed to read the block palette", (Object[])new Object[0]);
            return false;
        }
        this.size = new class_2382((int)sizeX, (int)sizeY, (int)sizeZ);
        this.blocks = new LitematicaBlockStateContainer(sizeX, (int)sizeY, sizeZ);
        this.metadata.setEnclosingSize(this.size);
        this.metadata.setTotalBlocks(numBlocks);
        this.metadata.setTotalVolume(sizeX * sizeY * sizeZ);
        this.metadata.setRegionCount(1);
        this.metadata.setFileType(FileType.SCHEMATICA_SCHEMATIC);
        if (nbt.method_10573("Add", 7)) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"SchematicaSchematic: Old Schematica format detected, not currently implemented...", (Object[])new Object[0]);
            return false;
        }
        byte[] add = null;
        if (nbt.method_10573("AddBlocks", 7) && (add = nbt.method_10547("AddBlocks")).length != (expectedAddLength = (int)Math.ceil((double)blockIdsByte.length / 2.0))) {
            String str = String.format("SchematicaSchematic: Add array size mismatch, blocks: %d, add: %d, expected add: %d", numBlocks, add.length, expectedAddLength);
            if (add.length < expectedAddLength) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)str, (Object[])new Object[0]);
                return false;
            }
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)str, (Object[])new Object[0]);
        }
        int loopMax = numBlocks % 2 == 0 ? numBlocks - 1 : numBlocks - 2;
        int bi = 0;
        int ai = 0;
        while (bi < loopMax) {
            addValue = add != null ? add[ai] : (byte)0;
            int byteId = blockIdsByte[bi] & 0xFF;
            state = this.palette[(addValue & 0xF0) << 8 | byteId << 4 | metaArr[bi]];
            x = bi % sizeX;
            y = bi / layerSize;
            z = bi % layerSize / sizeX;
            this.blocks.set(x, y, z, state);
            x = (bi + 1) % sizeX;
            y = (bi + 1) / layerSize;
            z = (bi + 1) % layerSize / sizeX;
            byteId = blockIdsByte[bi + 1] & 0xFF;
            state = this.palette[(addValue & 0xF) << 12 | byteId << 4 | metaArr[bi + 1]];
            this.blocks.set(x, y, z, state);
            bi += 2;
            ++ai;
        }
        if (numBlocks % 2 != 0) {
            addValue = add != null ? add[ai] : (byte)0;
            int byteId = blockIdsByte[bi] & 0xFF;
            state = this.palette[(addValue & 0xF0) << 8 | byteId << 4 | metaArr[bi]];
            x = bi % sizeX;
            y = bi / layerSize;
            z = bi % layerSize / sizeX;
            this.blocks.set(x, y, z, state);
        }
        return true;
    }

    private void postProcessBlocks() {
        if (this.needsConversionPostProcessing) {
            SchematicConverter.postProcessBlocks(this.blocks, this.tiles, this.postProcessingFilter);
        }
    }

    private void readEntitiesFromNBT(class_2487 nbt) {
        this.entities.clear();
        class_2499 tagList = nbt.method_10554("Entities", 10);
        int minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
        this.metadata.setSchematicVersion(-1);
        this.metadata.setMinecraftDataVersion(minecraftDataVersion);
        this.metadata.setSchema();
        if (effective != null) {
            Litematica.LOGGER.info("SchematicaSchematic: executing Vanilla DataFixer for Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)LitematicaSchematic.MINECRAFT_DATA_VERSION);
        } else {
            Litematica.LOGGER.warn("SchematicaSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Entities DataVersion {}", (Object)minecraftDataVersion);
        }
        for (int i = 0; i < tagList.size(); ++i) {
            if (effective != null) {
                this.entities.add(SchematicConversionMaps.updateEntity(tagList.method_10602(i), minecraftDataVersion));
                continue;
            }
            this.entities.add(tagList.method_10602(i));
        }
    }

    private void readTileEntitiesFromNBT(class_2487 nbt) {
        this.tiles.clear();
        class_2499 tagList = nbt.method_10554("TileEntities", 10);
        int minecraftDataVersion = Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue();
        Schema effective = DataFixerMode.getEffectiveSchema(minecraftDataVersion);
        if (effective != null) {
            Litematica.LOGGER.info("SchematicaSchematic: executing Vanilla DataFixer for Tile Entities DataVersion {} -> {}", (Object)minecraftDataVersion, (Object)LitematicaSchematic.MINECRAFT_DATA_VERSION);
        } else {
            Litematica.LOGGER.warn("SchematicaSchematic: Effective Schema has been bypassed.  Not applying Vanilla Data Fixer for Tile Entities DataVersion {}", (Object)minecraftDataVersion);
        }
        for (int i = 0; i < tagList.size(); ++i) {
            class_2487 tag = tagList.method_10602(i);
            class_2338 pos = new class_2338(tag.method_10550("x"), tag.method_10550("y"), tag.method_10550("z"));
            class_2382 size = this.blocks.getSize();
            if (pos.method_10263() < 0 || pos.method_10263() >= size.method_10263() || pos.method_10264() < 0 || pos.method_10264() >= size.method_10264() || pos.method_10260() < 0 || pos.method_10260() >= size.method_10260()) continue;
            if (effective != null) {
                this.tiles.put(pos, SchematicConversionMaps.updateBlockEntity(SchematicConversionMaps.checkForIdTag(tag), minecraftDataVersion));
                continue;
            }
            this.tiles.put(pos, SchematicConversionMaps.checkForIdTag(tag));
        }
    }

    public boolean readFromFile(File file) {
        if (file.exists() && file.isFile() && file.canRead()) {
            this.fileName = file.getName();
            try {
                class_2487 nbt = NbtUtils.readNbtFromFileAsPath((Path)file.toPath());
                return this.readFromNBT(nbt);
            }
            catch (Exception e) {
                Litematica.LOGGER.error("SchematicaSchematic: Failed to read Schematic data from file '{}'", (Object)file.getAbsolutePath());
            }
        }
        return false;
    }
}

