/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2281
 *  net.minecraft.class_2415
 *  net.minecraft.class_2680
 *  net.minecraft.class_2745
 *  net.minecraft.class_2769
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.block;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.util.BlockUtils;
import net.minecraft.class_2281;
import net.minecraft.class_2415;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2769;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_2281.class})
public class MixinChestBlock {
    @Inject(method={"method_9569"}, at={@At(value="HEAD")}, cancellable=true)
    private void litematica_fixChestMirror(class_2680 state, class_2415 mirror, CallbackInfoReturnable<class_2680> cir) {
        class_2745 type = (class_2745)state.method_11654((class_2769)class_2281.field_10770);
        if (Configs.Generic.FIX_CHEST_MIRROR.getBooleanValue() && type != class_2745.field_12569) {
            state = BlockUtils.fixMirrorDoubleChest(state, mirror, type);
            cir.setReturnValue((Object)state);
        }
    }
}

