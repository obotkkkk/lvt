/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2487
 */
package fi.dy.masa.litematica.schematic.conversion;

import javax.annotation.Nullable;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2487;

public interface IBlockReaderWithData
extends class_1922 {
    @Nullable
    public class_2487 getBlockEntityData(class_2338 var1);

    default public int method_31605() {
        return 384;
    }

    default public int method_31607() {
        return -64;
    }
}

