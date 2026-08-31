/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2350
 */
package fi.dy.masa.litematica.selection;

import fi.dy.masa.litematica.selection.Box;
import net.minecraft.class_2350;

public class BoxSliced
extends Box {
    private class_2350 sliceDirection = class_2350.field_11034;
    private int sliceStart = 0;
    private int sliceEnd = 1;
    private int sliceCount;

    public class_2350 getSliceDirection() {
        return this.sliceDirection;
    }

    public int getSliceStart() {
        return this.sliceStart;
    }

    public int getSliceEnd() {
        return this.sliceEnd;
    }

    public int getSliceCount() {
        return this.sliceCount;
    }

    public int getMaxSliceLength() {
        switch (this.sliceDirection.method_10166()) {
            case field_11048: {
                return this.getSize().method_10263();
            }
            case field_11052: {
                return this.getSize().method_10264();
            }
            case field_11051: {
                return this.getSize().method_10260();
            }
        }
        return 1;
    }

    public void setSliceDirection(class_2350 sliceDirection) {
        this.sliceDirection = sliceDirection;
    }

    public void setSliceStart(int sliceStart) {
        this.sliceStart = Math.min(sliceStart, this.getMaxSliceLength() - 1);
    }

    public void setSliceEnd(int sliceEnd) {
        this.sliceEnd = Math.min(sliceEnd, this.getMaxSliceLength());
    }

    public void setSliceCount(int sliceCount) {
        this.sliceCount = sliceCount;
    }
}

