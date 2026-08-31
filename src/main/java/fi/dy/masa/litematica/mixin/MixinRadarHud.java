/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin;

import com.mayquetlinhthao.LinhThaoRadarClient;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_329.class})
public class MixinRadarHud {
    @Inject(method={"method_1753"}, at={@At(value="TAIL")})
    private void onRender(class_332 drawContext, class_9779 tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.method_60637(false);
        LinhThaoRadarClient.onHudRender(drawContext, tickDelta);
    }
}

