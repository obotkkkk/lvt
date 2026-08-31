/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package fi.dy.masa.litematica.mixin.entity;

import fi.dy.masa.litematica.util.IEntityInvoker;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_1297.class})
public abstract class MixinEntity
implements IEntityInvoker {
    @Shadow
    protected boolean field_5957;

    @Override
    public void litematica$toggleTouchingWater(boolean toggle) {
        if (toggle != this.field_5957) {
            this.field_5957 = toggle;
        }
    }
}

