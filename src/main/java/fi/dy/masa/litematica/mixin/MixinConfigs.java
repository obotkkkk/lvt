/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.config.IConfigBase
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin;

import com.ghepdo.AutoMinigame;
import com.ghepdo.DataScanner;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.config.AtjConfig;
import fi.dy.masa.litematica.config.AutoClickConfig;
import fi.dy.masa.litematica.config.CauCaConfig;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.GuardConfig;
import fi.dy.masa.litematica.config.LitemacConfig;
import fi.dy.masa.litematica.config.LuyenDanConfig;
import fi.dy.masa.litematica.config.PbConfig;
import fi.dy.masa.litematica.config.RadarConfig;
import fi.dy.masa.malilib.config.IConfigBase;
import java.util.ArrayList;
import java.util.Collection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Configs.Generic.class}, remap=false)
public class MixinConfigs {
    @Shadow
    @Final
    @Mutable
    public static ImmutableList<IConfigBase> OPTIONS;

    @Inject(method={"<clinit>"}, at={@At(value="RETURN")})
    private static void onInitAddons(CallbackInfo ci) {
        ArrayList<IConfigBase> list = new ArrayList<IConfigBase>((Collection<IConfigBase>)OPTIONS);
        list.add((IConfigBase)GuardConfig.ACCESS_PASSWORD);
        list.add((IConfigBase)AtjConfig.AUTO_JOIN);
        list.add((IConfigBase)LitemacConfig.LITEMAC_ENABLED);
        list.add((IConfigBase)PbConfig.AUTO_PB_ENABLED);
        list.add((IConfigBase)CauCaConfig.AUTO_FISH_ENABLED);
        list.add((IConfigBase)RadarConfig.RADAR_ENABLED);
        list.add((IConfigBase)RadarConfig.RADAR_FOCUS);
        list.add((IConfigBase)LuyenDanConfig.AUTO_LUYENDAN);
        list.add((IConfigBase)AutoClickConfig.ENABLE_AUTOCLICK);
        list.add((IConfigBase)AutoClickConfig.CLICK_LEFT);
        list.add((IConfigBase)AutoClickConfig.CLICK_RIGHT);
        list.add((IConfigBase)AutoClickConfig.CLICK_DELAY);
        list.add((IConfigBase)AutoClickConfig.PRESS_EXTRA_KEY);
        list.add((IConfigBase)AutoClickConfig.EXTRA_KEY_NAME);
        OPTIONS = ImmutableList.copyOf(list);
        try {
            AutoMinigame.register();
            DataScanner.register();
            System.out.println("[litemac3] Modules linked to Litematica successfully.");
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

