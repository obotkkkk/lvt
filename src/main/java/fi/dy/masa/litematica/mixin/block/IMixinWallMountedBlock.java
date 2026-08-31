/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 *  net.minecraft.class_2341
 *  net.minecraft.class_2680
 *  net.minecraft.class_4538
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package fi.dy.masa.litematica.mixin.block;

import net.minecraft.class_2338;
import net.minecraft.class_2341;
import net.minecraft.class_2680;
import net.minecraft.class_4538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_2341.class})
public interface IMixinWallMountedBlock {
    @Invoker(value="method_9558")
    public boolean litematica_invokeCanPlaceAt(class_2680 var1, class_4538 var2, class_2338 var3);
}

