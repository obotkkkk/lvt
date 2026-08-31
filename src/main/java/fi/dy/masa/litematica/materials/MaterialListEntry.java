/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.ItemType
 *  net.minecraft.class_1799
 */
package fi.dy.masa.litematica.materials;

import fi.dy.masa.malilib.util.ItemType;
import net.minecraft.class_1799;

public class MaterialListEntry {
    private final ItemType item;
    private final int countTotal;
    private final int countMissing;
    private final int countMismatched;
    private int countAvailable;

    public MaterialListEntry(class_1799 stack, int countTotal, int countMissing, int countMismatched, int countAvailable) {
        this.item = new ItemType(stack, true, false);
        this.countTotal = countTotal;
        this.countMissing = countMissing;
        this.countMismatched = countMismatched;
        this.countAvailable = countAvailable;
    }

    public class_1799 getStack() {
        return this.item.getStack();
    }

    public int getCountTotal() {
        return this.countTotal;
    }

    public int getCountMissing() {
        return this.countMissing;
    }

    public int getCountMismatched() {
        return this.countMismatched;
    }

    public int getCountAvailable() {
        return this.countAvailable;
    }

    public void setCountAvailable(int countAvailable) {
        this.countAvailable = countAvailable;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.item == null ? 0 : this.item.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        MaterialListEntry other = (MaterialListEntry)obj;
        return !(this.item == null ? other.item != null : !this.item.equals((Object)other.item));
    }
}

