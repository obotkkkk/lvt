/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_4093
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.client;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.class_310;
import net.minecraft.class_4093;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_310.class})
public abstract class MixinMinecraftClient
extends class_4093<Runnable> {
    public MixinMinecraftClient(String string_1) {
        super(string_1);
    }

    @Inject(method={"method_1583()V"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_1799;method_7947()I", ordinal=0)}, cancellable=true)
    private void handlePlacementRestriction(CallbackInfo ci) {
        if (Configs.Generic.PLACEMENT_RESTRICTION.getBooleanValue() && WorldUtils.handlePlacementRestriction((class_310)this)) {
            ci.cancel();
        }
    }

    @Inject(method={"method_1574()V"}, at={@At(value="HEAD")})
    private void onRunTickStart(CallbackInfo ci) {
        DataManager.onClientTickStart();
    }
}

