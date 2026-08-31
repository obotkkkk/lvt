/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin;

import com.atj.Atj;
import com.autoclick.AutoClickMod;
import com.cauca.CauCaMod;
import com.luyendan.AutoLuyenDan;
import com.mayquetlinhthao.LinhThaoRadarClient;
import com.pb.AutoPhoban;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net/minecraft/class_310"})
public class MixinAtjTick {
    @Inject(method={"method_1574"}, at={@At(value="TAIL")})
    private void onTick(CallbackInfo ci) {
        class_310 client = class_310.method_1551();
        Atj.onTick(client);
        AutoPhoban.onTick(client);
        AutoLuyenDan.onTick(client);
        CauCaMod.onTick(client);
        LinhThaoRadarClient.onTick(client);
        AutoClickMod.onTick(client);
    }
}

