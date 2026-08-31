/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_634
 *  net.minecraft.class_7439
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin;

import com.cauca.CauCaMod;
import com.mayquetlinhthao.LinhThaoRadarClient;
import net.minecraft.class_2561;
import net.minecraft.class_634;
import net.minecraft.class_7439;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_634.class})
public class MixinChatBait {
    @Inject(method={"method_43596"}, at={@At(value="TAIL")})
    private void onMessage(class_7439 packet, CallbackInfo ci) {
        class_2561 message = packet.comp_763();
        CauCaMod.onChatMessage(message);
        LinhThaoRadarClient.onChatMessage(message);
    }
}

