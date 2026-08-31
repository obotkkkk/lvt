/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_2378
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2512
 *  net.minecraft.class_2680
 *  net.minecraft.class_7871
 *  net.minecraft.class_7924
 */
package fi.dy.masa.litematica.schematic.container;

import fi.dy.masa.litematica.schematic.container.ILitematicaBlockStatePalette;
import fi.dy.masa.litematica.schematic.container.ILitematicaBlockStatePaletteResizer;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_2378;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2512;
import net.minecraft.class_2680;
import net.minecraft.class_7871;
import net.minecraft.class_7924;

public class LitematicaBlockStatePaletteLinear
implements ILitematicaBlockStatePalette {
    private final class_2680[] states;
    private final ILitematicaBlockStatePaletteResizer resizeHandler;
    private final int bits;
    private int currentSize;

    public LitematicaBlockStatePaletteLinear(int bitsIn, ILitematicaBlockStatePaletteResizer resizeHandler) {
        this.states = new class_2680[1 << bitsIn];
        this.bits = bitsIn;
        this.resizeHandler = resizeHandler;
    }

    @Override
    public int idFor(class_2680 state) {
        int size;
        for (int i = 0; i < this.currentSize; ++i) {
            if (this.states[i] != state) continue;
            return i;
        }
        if ((size = this.currentSize++) < this.states.length) {
            this.states[size] = state;
            return size;
        }
        return this.resizeHandler.onResize(this.bits + 1, state);
    }

    @Override
    @Nullable
    public class_2680 getBlockState(int indexKey) {
        return indexKey >= 0 && indexKey < this.currentSize ? this.states[indexKey] : null;
    }

    @Override
    public int getPaletteSize() {
        return this.currentSize;
    }

    private void requestNewId(class_2680 state) {
        int size;
        if ((size = this.currentSize++) < this.states.length) {
            this.states[size] = state;
        } else {
            int newId = this.resizeHandler.onResize(this.bits + 1, LitematicaBlockStateContainer.AIR_BLOCK_STATE);
            if (newId <= size) {
                this.states[size] = state;
                ++this.currentSize;
            }
        }
    }

    @Override
    public void readFromNBT(class_2499 tagList) {
        class_2378 lookup = SchematicWorldHandler.INSTANCE.getRegistryManager().method_30530(class_7924.field_41254);
        int size = tagList.size();
        for (int i = 0; i < size; ++i) {
            class_2487 tag = tagList.method_10602(i);
            class_2680 state = class_2512.method_10681((class_7871)lookup, (class_2487)tag);
            if (i <= 0 && state == LitematicaBlockStateContainer.AIR_BLOCK_STATE) continue;
            this.requestNewId(state);
        }
    }

    @Override
    public class_2499 writeToNBT() {
        class_2499 tagList = new class_2499();
        for (int id = 0; id < this.currentSize; ++id) {
            class_2680 state = this.states[id];
            if (state == null) {
                state = LitematicaBlockStateContainer.AIR_BLOCK_STATE;
            }
            class_2487 tag = class_2512.method_10686((class_2680)state);
            tagList.add((Object)tag);
        }
        return tagList;
    }

    @Override
    public boolean setMapping(List<class_2680> list) {
        int size = list.size();
        if (size <= this.states.length) {
            for (int id = 0; id < size; ++id) {
                this.states[id] = list.get(id);
            }
            this.currentSize = size;
            return true;
        }
        return false;
    }
}

