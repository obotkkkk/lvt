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
 *  net.minecraft.class_3513
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
import net.minecraft.class_3513;
import net.minecraft.class_7871;
import net.minecraft.class_7924;

public class LitematicaBlockStatePaletteHashMap
implements ILitematicaBlockStatePalette {
    private final class_3513<class_2680> statePaletteMap;
    private final ILitematicaBlockStatePaletteResizer paletteResizer;
    private final int bits;

    public LitematicaBlockStatePaletteHashMap(int bitsIn, ILitematicaBlockStatePaletteResizer paletteResizer) {
        this.bits = bitsIn;
        this.paletteResizer = paletteResizer;
        this.statePaletteMap = class_3513.method_37913((int)(1 << bitsIn));
    }

    @Override
    public int idFor(class_2680 state) {
        int i = this.statePaletteMap.method_10206((Object)state);
        if (i == -1 && (i = this.statePaletteMap.method_15225((Object)state)) >= 1 << this.bits) {
            i = this.paletteResizer.onResize(this.bits + 1, state);
        }
        return i;
    }

    @Override
    @Nullable
    public class_2680 getBlockState(int indexKey) {
        return (class_2680)this.statePaletteMap.method_10200(indexKey);
    }

    @Override
    public int getPaletteSize() {
        return this.statePaletteMap.method_10204();
    }

    private void requestNewId(class_2680 state) {
        int newId;
        int origId = this.statePaletteMap.method_15225((Object)state);
        if (origId >= 1 << this.bits && (newId = this.paletteResizer.onResize(this.bits + 1, LitematicaBlockStateContainer.AIR_BLOCK_STATE)) <= origId) {
            this.statePaletteMap.method_15225((Object)state);
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
        for (int id = 0; id < this.statePaletteMap.method_10204(); ++id) {
            class_2680 state = (class_2680)this.statePaletteMap.method_10200(id);
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
        this.statePaletteMap.method_15229();
        for (class_2680 blockState : list) {
            this.statePaletteMap.method_15225((Object)blockState);
        }
        return true;
    }
}

