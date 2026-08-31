/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.Schema
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_2487
 *  net.minecraft.class_2520
 */
package fi.dy.masa.litematica.schematic;

import fi.dy.masa.litematica.schematic.SchematicSchema;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.malilib.util.Schema;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2487;
import net.minecraft.class_2520;

public class SchematicMetadata {
    private String name = "?";
    private String author = "?";
    private String description = "";
    private class_2382 enclosingSize = class_2382.field_11176;
    private long timeCreated;
    private long timeModified;
    protected int minecraftDataVersion;
    protected int schematicVersion;
    protected Schema schema;
    protected FileType type;
    private int regionCount;
    protected int entityCount;
    protected int blockEntityCount;
    private int totalVolume = -1;
    private int totalBlocks = -1;
    private boolean modifiedSinceSaved;
    @Nullable
    protected int[] thumbnailPixelData;

    public String getName() {
        return this.name;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDescription() {
        return this.description;
    }

    @Nullable
    public int[] getPreviewImagePixelData() {
        return this.thumbnailPixelData;
    }

    public int getRegionCount() {
        return this.regionCount;
    }

    public int getTotalVolume() {
        return this.totalVolume;
    }

    public int getTotalBlocks() {
        return this.totalBlocks;
    }

    public int getEntityCount() {
        return this.entityCount;
    }

    public int getBlockEntityCount() {
        return this.blockEntityCount;
    }

    public class_2382 getEnclosingSize() {
        return this.enclosingSize;
    }

    public class_2382 getEnclosingSizeAsVanilla() {
        return this.enclosingSize;
    }

    public class_2338 getEnclosingSizeAsBlockPos() {
        return new class_2338(this.enclosingSize);
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

    public long getTimeModified() {
        return this.timeModified;
    }

    public int getSchematicVersion() {
        return this.schematicVersion;
    }

    public int getMinecraftDataVersion() {
        return this.minecraftDataVersion;
    }

    public SchematicSchema getSchematicSchema() {
        return new SchematicSchema(this.schematicVersion, this.minecraftDataVersion);
    }

    public Schema getSchema() {
        return this.schema;
    }

    public String getMinecraftVersion() {
        return this.schema.getString();
    }

    public String getSchemaString() {
        return this.schema.toString();
    }

    public FileType getFileType() {
        return Objects.requireNonNullElse(this.type, FileType.UNKNOWN);
    }

    public boolean hasBeenModified() {
        return this.timeCreated != this.timeModified;
    }

    public boolean wasModifiedSinceSaved() {
        return this.modifiedSinceSaved;
    }

    public void setModifiedSinceSaved() {
        this.modifiedSinceSaved = true;
    }

    public void clearModifiedSinceSaved() {
        this.modifiedSinceSaved = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPreviewImagePixelData(@Nullable int[] pixelData) {
        this.thumbnailPixelData = pixelData;
    }

    public void setRegionCount(int regionCount) {
        this.regionCount = regionCount;
    }

    public void setTotalVolume(int totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setTotalBlocks(int totalBlocks) {
        this.totalBlocks = totalBlocks;
    }

    public void setEnclosingSize(class_2382 enclosingSize) {
        this.enclosingSize = enclosingSize;
    }

    public void setEnclosingSize(class_2338 enclosingSize) {
        this.enclosingSize = enclosingSize;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setTimeModified(long timeModified) {
        this.timeModified = timeModified;
    }

    public void setTimeModifiedToNow() {
        this.timeModified = System.currentTimeMillis();
    }

    public void setTimeModifiedToNowIfNotRecentlyCreated() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.timeCreated > 600000L) {
            this.timeModified = currentTime;
        }
    }

    public void setSchematicVersion(int version) {
        this.schematicVersion = version;
    }

    public void setMinecraftDataVersion(int minecraftDataVersion) {
        this.minecraftDataVersion = minecraftDataVersion;
        this.schema = Schema.getSchemaByDataVersion((int)this.minecraftDataVersion);
    }

    public void setSchema() {
        this.schema = Schema.getSchemaByDataVersion((int)this.minecraftDataVersion);
    }

    public void setFileType(FileType type) {
        this.type = type;
    }

    public void copyFrom(SchematicMetadata other) {
        this.name = other.name;
        this.author = other.author;
        this.description = other.description;
        this.enclosingSize = other.enclosingSize;
        this.timeCreated = other.timeCreated;
        this.timeModified = other.timeModified;
        this.regionCount = other.regionCount;
        this.totalVolume = other.totalVolume;
        this.totalBlocks = other.totalBlocks;
        this.modifiedSinceSaved = false;
        this.schematicVersion = other.schematicVersion;
        this.minecraftDataVersion = other.minecraftDataVersion;
        this.schema = Schema.getSchemaByDataVersion((int)other.minecraftDataVersion);
        this.type = other.getFileType();
        if (other.thumbnailPixelData != null) {
            this.thumbnailPixelData = new int[other.thumbnailPixelData.length];
            System.arraycopy(other.thumbnailPixelData, 0, this.thumbnailPixelData, 0, this.thumbnailPixelData.length);
        } else {
            this.thumbnailPixelData = null;
        }
    }

    public class_2487 writeToNBT() {
        class_2487 nbt = new class_2487();
        nbt.method_10582("Name", this.name);
        nbt.method_10582("Author", this.author);
        nbt.method_10582("Description", this.description);
        if (this.regionCount > 0) {
            nbt.method_10569("RegionCount", this.regionCount);
        }
        if (this.totalVolume > 0) {
            nbt.method_10569("TotalVolume", this.totalVolume);
        }
        if (this.totalBlocks >= 0) {
            nbt.method_10569("TotalBlocks", this.totalBlocks);
        }
        if (this.timeCreated > 0L) {
            nbt.method_10544("TimeCreated", this.timeCreated);
        }
        if (this.timeModified > 0L) {
            nbt.method_10544("TimeModified", this.timeModified);
        }
        nbt.method_10566("EnclosingSize", (class_2520)NbtUtils.createBlockPosTag((class_2382)this.enclosingSize));
        if (this.thumbnailPixelData != null) {
            nbt.method_10539("PreviewImageData", this.thumbnailPixelData);
        }
        return nbt;
    }

    public void readFromNBT(class_2487 nbt) {
        class_2382 size;
        this.name = nbt.method_10558("Name");
        this.author = nbt.method_10558("Author");
        this.description = nbt.method_10558("Description");
        this.regionCount = nbt.method_10550("RegionCount");
        this.timeCreated = nbt.method_10537("TimeCreated");
        this.timeModified = nbt.method_10537("TimeModified");
        if (nbt.method_10573("TotalVolume", 99)) {
            this.totalVolume = nbt.method_10550("TotalVolume");
        }
        if (nbt.method_10573("TotalBlocks", 99)) {
            this.totalBlocks = nbt.method_10550("TotalBlocks");
        }
        if (nbt.method_10573("EnclosingSize", 10) && (size = NbtUtils.readVec3iFromTag((class_2487)nbt.method_10562("EnclosingSize"))) != null) {
            this.enclosingSize = size;
        }
        this.thumbnailPixelData = (int[])(nbt.method_10573("PreviewImageData", 11) ? nbt.method_10561("PreviewImageData") : null);
    }

    public class_2487 writeToNbtExtra() {
        class_2487 nbt = this.writeToNBT();
        nbt.method_10582("FileType", this.type.name());
        if (this.minecraftDataVersion > 0) {
            nbt.method_10569("MinecraftDataVersion", this.minecraftDataVersion);
        }
        if (this.schematicVersion > 0) {
            nbt.method_10569("SchematicVersion", this.schematicVersion);
        }
        if (this.schema != null) {
            nbt.method_10582("Schema", this.schema.toString());
        }
        if (this.entityCount > 0) {
            nbt.method_10569("EntityCount", this.entityCount);
        }
        if (this.blockEntityCount > 0) {
            nbt.method_10569("BlockEntityCount", this.blockEntityCount);
        }
        nbt.method_10556("IsModified", this.modifiedSinceSaved);
        return nbt;
    }

    public String toString() {
        class_2487 nbt = this.writeToNbtExtra();
        if (nbt.method_10545("PreviewImageData")) {
            nbt.method_10551("PreviewImageData");
            nbt.method_10556("PreviewImageData", true);
        }
        return "SchematicMetadata[" + nbt.toString() + "]";
    }

    public void dumpMetadata() {
        System.out.print("SchematicMetadata() DUMP -->\n");
        System.out.printf("   %s\n", this.toString());
        System.out.print("<END>\n");
    }
}

