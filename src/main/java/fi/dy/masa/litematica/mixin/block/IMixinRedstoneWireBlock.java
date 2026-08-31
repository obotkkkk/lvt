/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2457
 *  net.minecraft.class_2680
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package fi.dy.masa.litematica.mixin.block;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2457;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_2457.class})
public interface IMixinRedstoneWireBlock {
    @Invoker(value="method_27840")
    public class_2680 litematica_GetPlacementState(class_1922 var1, class_2680 var2, class_2338 var3);
}

