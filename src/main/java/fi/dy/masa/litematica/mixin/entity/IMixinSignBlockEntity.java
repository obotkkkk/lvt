/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2625
 *  net.minecraft.class_8242
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package fi.dy.masa.litematica.mixin.entity;

import net.minecraft.class_2625;
import net.minecraft.class_8242;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2625.class})
public interface IMixinSignBlockEntity {
    @Accessor(value="field_43295")
    public class_8242 litematica_getFrontText();

    @Accessor(value="field_43296")
    public class_8242 litematica_getBackText();
}

