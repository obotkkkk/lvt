/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2658
 *  net.minecraft.class_8673
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.network;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.data.DataManager;
import net.minecraft.class_2658;
import net.minecraft.class_8673;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_8673.class})
public class MixinClientCommonNetworkHandler {
    @Inject(method={"method_52780(Lnet/minecraft/class_2658;)V"}, at={@At(value="HEAD")})
    private void litematica_onCustomPayload(class_2658 packet, CallbackInfo ci) {
        if (packet.comp_1646().method_56479().comp_2242().equals((Object)DataManager.CARPET_HELLO)) {
            Litematica.debugLog("ClientCommonNetworkHandler#litematica_onCustomPayload(): received carpet hello packet", new Object[0]);
            DataManager.setIsCarpetServer(true);
        } else if (packet.comp_1646().method_56479().comp_2242().method_12836().equals("servux")) {
            DataManager.setHasServuxServer(true);
        }
    }
}

