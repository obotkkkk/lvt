/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_2499
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.schematic.container;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_2499;
import net.minecraft.class_2680;

public interface ILitematicaBlockStatePalette {
    public int idFor(class_2680 var1);

    @Nullable
    public class_2680 getBlockState(int var1);

    public int getPaletteSize();

    public void readFromNBT(class_2499 var1);

    public class_2499 writeToNBT();

    public boolean setMapping(List<class_2680> var1);
}

