/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.Validate
 */
package fi.dy.masa.litematica.schematic.container;

import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class LitematicaBitArray {
    private final long[] longArray;
    private final int bitsPerEntry;
    private final long maxEntryValue;
    private final long arraySize;

    public LitematicaBitArray(int bitsPerEntryIn, long arraySizeIn) {
        this(bitsPerEntryIn, arraySizeIn, null);
    }

    public LitematicaBitArray(int bitsPerEntryIn, long arraySizeIn, @Nullable long[] longArrayIn) {
        Validate.inclusiveBetween((long)1L, (long)32L, (long)bitsPerEntryIn);
        this.arraySize = arraySizeIn;
        this.bitsPerEntry = bitsPerEntryIn;
        this.maxEntryValue = (1L << bitsPerEntryIn) - 1L;
        this.longArray = longArrayIn != null ? longArrayIn : new long[(int)(LitematicaBitArray.roundUp(arraySizeIn * (long)bitsPerEntryIn, 64L) / 64L)];
    }

    public void setAt(long index, int value) {
        long startOffset = index * (long)this.bitsPerEntry;
        int startArrIndex = (int)(startOffset >> 6);
        int endArrIndex = (int)((index + 1L) * (long)this.bitsPerEntry - 1L >> 6);
        int startBitOffset = (int)(startOffset & 0x3FL);
        this.longArray[startArrIndex] = this.longArray[startArrIndex] & (this.maxEntryValue << startBitOffset ^ 0xFFFFFFFFFFFFFFFFL) | ((long)value & this.maxEntryValue) << startBitOffset;
        if (startArrIndex != endArrIndex) {
            int endOffset = 64 - startBitOffset;
            int j1 = this.bitsPerEntry - endOffset;
            this.longArray[endArrIndex] = this.longArray[endArrIndex] >>> j1 << j1 | ((long)value & this.maxEntryValue) >> endOffset;
        }
    }

    public int getAt(long index) {
        long startOffset = index * (long)this.bitsPerEntry;
        int startArrIndex = (int)(startOffset >> 6);
        int endArrIndex = (int)((index + 1L) * (long)this.bitsPerEntry - 1L >> 6);
        int startBitOffset = (int)(startOffset & 0x3FL);
        if (startArrIndex == endArrIndex) {
            return (int)(this.longArray[startArrIndex] >>> startBitOffset & this.maxEntryValue);
        }
        int endOffset = 64 - startBitOffset;
        return (int)((this.longArray[startArrIndex] >>> startBitOffset | this.longArray[endArrIndex] << endOffset) & this.maxEntryValue);
    }

    public long[] getBackingLongArray() {
        return this.longArray;
    }

    public long size() {
        return this.arraySize;
    }

    public static long roundUp(long value, long interval) {
        long remainder;
        if (interval == 0L) {
            return 0L;
        }
        if (value == 0L) {
            return interval;
        }
        if (value < 0L) {
            interval *= -1L;
        }
        return (remainder = value % interval) == 0L ? value : value + interval - remainder;
    }
}

