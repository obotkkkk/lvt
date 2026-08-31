/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_2487
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package fi.dy.masa.litematica.mixin.entity;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1297.class})
public interface IMixinEntity {
    @Accessor(value="field_6002")
    public void litematica_setWorld(class_1937 var1);

    @Invoker(value="method_5749")
    public void litematica_readCustomDataFromNbt(class_2487 var1);

    @Accessor(value="field_5957")
    public boolean litematica_isTouchingWater();
}

