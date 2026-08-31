/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  javax.annotation.Nullable
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_2499
 *  net.minecraft.class_2540
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.schematic.container;

import fi.dy.masa.litematica.schematic.container.ILitematicaBlockStatePalette;
import fi.dy.masa.litematica.schematic.container.ILitematicaBlockStatePaletteResizer;
import fi.dy.masa.litematica.schematic.container.LitematicaBitArray;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStatePaletteHashMap;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStatePaletteLinear;
import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2499;
import net.minecraft.class_2540;
import net.minecraft.class_2680;

public class LitematicaBlockStateContainer
implements ILitematicaBlockStatePaletteResizer {
    public static final class_2680 AIR_BLOCK_STATE = class_2246.field_10124.method_9564();
    protected LitematicaBitArray storage;
    protected ILitematicaBlockStatePalette palette;
    protected final class_2382 size;
    protected final int sizeX;
    protected final int sizeY;
    protected final int sizeZ;
    protected final int sizeLayer;
    protected final long totalVolume;
    protected int bits;
    protected long[] blockCounts = new long[0];

    public LitematicaBlockStateContainer(int sizeX, int sizeY, int sizeZ) {
        this(sizeX, sizeY, sizeZ, 2, null);
    }

    public LitematicaBlockStateContainer(class_2382 size, int bits, @Nullable long[] backingLongArray) {
        this(size.method_10263(), size.method_10264(), size.method_10260(), bits, backingLongArray);
    }

    public LitematicaBlockStateContainer(int sizeX, int sizeY, int sizeZ, int bits, @Nullable long[] backingLongArray) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.sizeLayer = sizeX * sizeZ;
        this.totalVolume = (long)this.sizeX * (long)this.sizeY * (long)this.sizeZ;
        this.size = new class_2382(this.sizeX, this.sizeY, this.sizeZ);
        this.setBits(bits, backingLongArray);
    }

    public class_2382 getSize() {
        return this.size;
    }

    public LitematicaBitArray getArray() {
        return this.storage;
    }

    public long[] getBlockCounts() {
        return this.blockCounts;
    }

    public class_2680 get(int x, int y, int z) {
        class_2680 state = this.palette.getBlockState(this.storage.getAt(this.getIndex(x, y, z)));
        return state == null ? AIR_BLOCK_STATE : state;
    }

    public void set(int x, int y, int z, class_2680 state) {
        int id = this.palette.idFor(state);
        this.storage.setAt(this.getIndex(x, y, z), id);
    }

    protected void set(int index, class_2680 state) {
        int id = this.palette.idFor(state);
        this.storage.setAt(index, id);
    }

    protected int getIndex(int x, int y, int z) {
        return y * this.sizeLayer + z * this.sizeX + x;
    }

    protected void setBits(int bitsIn, @Nullable long[] backingLongArray) {
        if (bitsIn != this.bits) {
            this.bits = bitsIn;
            if (this.bits <= 4) {
                this.bits = Math.max(2, this.bits);
                this.palette = new LitematicaBlockStatePaletteLinear(this.bits, this);
            } else {
                this.palette = new LitematicaBlockStatePaletteHashMap(this.bits, this);
            }
            this.palette.idFor(AIR_BLOCK_STATE);
            this.storage = backingLongArray != null ? new LitematicaBitArray(this.bits, this.totalVolume, backingLongArray) : new LitematicaBitArray(this.bits, this.totalVolume);
        }
    }

    @Override
    public int onResize(int bits, class_2680 state) {
        LitematicaBitArray oldStorage = this.storage;
        ILitematicaBlockStatePalette oldPalette = this.palette;
        long storageLength = oldStorage.size();
        this.setBits(bits, null);
        LitematicaBitArray newStorage = this.storage;
        for (long index = 0L; index < storageLength; ++index) {
            newStorage.setAt(index, oldStorage.getAt(index));
        }
        this.palette.readFromNBT(oldPalette.writeToNBT());
        return this.palette.idFor(state);
    }

    public long[] getBackingLongArray() {
        return this.storage.getBackingLongArray();
    }

    public ILitematicaBlockStatePalette getPalette() {
        return this.palette;
    }

    public static LitematicaBlockStateContainer createFrom(class_2499 palette, long[] blockStates, class_2338 size) {
        int bits = Math.max(2, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
        LitematicaBlockStateContainer container = new LitematicaBlockStateContainer(size.method_10263(), size.method_10264(), size.method_10260(), bits, blockStates);
        container.palette.readFromNBT(palette);
        return container;
    }

    @Nullable
    public static LitematicaBlockStateContainer createContainer(int paletteSize, byte[] blockData, class_2382 size) {
        int bits = Math.max(2, 32 - Integer.numberOfLeadingZeros(paletteSize - 1));
        SpongeBlockstateConverterResults results = LitematicaBlockStateContainer.convertVarIntByteArrayToPackedLongArray(size, bits, blockData);
        LitematicaBlockStateContainer container = new LitematicaBlockStateContainer(size, bits, results.backingArray);
        container.blockCounts = results.blockCounts;
        return container;
    }

    public static SpongeBlockstateConverterResults convertVarIntByteArrayToPackedLongArray(class_2382 size, int bits, byte[] blockStates) {
        int volume = size.method_10263() * size.method_10264() * size.method_10260();
        LitematicaBitArray bitArray = new LitematicaBitArray(bits, volume);
        class_2540 buf = new class_2540(Unpooled.wrappedBuffer((byte[])blockStates));
        long[] blockCounts = new long[1 << bits];
        for (int i = 0; i < volume; ++i) {
            int id = buf.method_10816();
            bitArray.setAt(i, id);
            int n = id;
            blockCounts[n] = blockCounts[n] + 1L;
        }
        return new SpongeBlockstateConverterResults(bitArray.getBackingLongArray(), blockCounts);
    }

    public static class SpongeBlockstateConverterResults {
        public final long[] backingArray;
        public final long[] blockCounts;

        protected SpongeBlockstateConverterResults(long[] backingArray, long[] blockCounts) {
            this.backingArray = backingArray;
            this.blockCounts = blockCounts;
        }
    }
}

