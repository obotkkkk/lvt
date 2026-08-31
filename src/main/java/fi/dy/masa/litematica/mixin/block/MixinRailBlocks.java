/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2241
 *  net.minecraft.class_2313
 *  net.minecraft.class_2442
 *  net.minecraft.class_2443
 *  net.minecraft.class_2470
 *  net.minecraft.class_2680
 *  net.minecraft.class_2768
 *  net.minecraft.class_2769
 *  net.minecraft.class_4970$class_2251
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.block;

import fi.dy.masa.litematica.config.Configs;
import net.minecraft.class_2241;
import net.minecraft.class_2313;
import net.minecraft.class_2442;
import net.minecraft.class_2443;
import net.minecraft.class_2470;
import net.minecraft.class_2680;
import net.minecraft.class_2768;
import net.minecraft.class_2769;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_2443.class, class_2313.class, class_2442.class})
public abstract class MixinRailBlocks
extends class_2241 {
    protected MixinRailBlocks(boolean disableCorners, class_4970.class_2251 builder) {
        super(disableCorners, builder);
    }

    @Inject(method={"method_9598"}, at={@At(value="HEAD")}, cancellable=true)
    private void litematica_fixRailRotation(class_2680 state, class_2470 rot, CallbackInfoReturnable<class_2680> cir) {
        if (Configs.Generic.FIX_RAIL_ROTATION.getBooleanValue() && rot == class_2470.field_11464) {
            class_2768 shape = null;
            if (this instanceof class_2443) {
                shape = (class_2768)state.method_11654((class_2769)class_2443.field_11369);
            } else if (this instanceof class_2313) {
                shape = (class_2768)state.method_11654((class_2769)class_2313.field_10914);
            } else if (this instanceof class_2442) {
                shape = (class_2768)state.method_11654((class_2769)class_2442.field_11365);
            }
            if (shape == class_2768.field_12674 || shape == class_2768.field_12665) {
                cir.setReturnValue((Object)state);
                cir.cancel();
            }
        }
    }
}

