/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  net.minecraft.class_3244
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package fi.dy.masa.litematica.mixin.easyplace;

import fi.dy.masa.litematica.config.Configs;
import net.minecraft.class_243;
import net.minecraft.class_3244;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={class_3244.class}, priority=1010)
public class MixinServerPlayNetworkHandler_easyPlace {
    @Redirect(method={"method_12046"}, require=0, at=@At(value="INVOKE", target="Lnet/minecraft/class_243;method_1020(Lnet/minecraft/class_243;)Lnet/minecraft/class_243;"))
    private class_243 litematica_removeHitPosCheck(class_243 hitVec, class_243 blockCenter) {
        if (Configs.Generic.ITEM_USE_PACKET_CHECK_BYPASS.getBooleanValue()) {
            return class_243.field_1353;
        }
        return hitVec.method_1020(blockCenter);
    }
}

