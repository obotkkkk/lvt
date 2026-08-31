/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2510
 *  net.minecraft.class_2680
 *  net.minecraft.class_2778
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package fi.dy.masa.litematica.mixin.block;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2510;
import net.minecraft.class_2680;
import net.minecraft.class_2778;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_2510.class})
public interface IMixinStairsBlock {
    @Invoker(value="method_10675")
    public static class_2778 litematica_invokeGetStairShape(class_2680 state, class_1922 worldIn, class_2338 pos) {
        return null;
    }
}

