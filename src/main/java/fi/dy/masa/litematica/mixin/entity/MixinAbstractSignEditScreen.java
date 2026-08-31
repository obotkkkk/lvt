/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2625
 *  net.minecraft.class_7743
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.entity;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.class_2625;
import net.minecraft.class_7743;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_7743.class}, priority=990)
public class MixinAbstractSignEditScreen {
    @Shadow
    @Final
    protected class_2625 field_40424;
    @Shadow
    @Final
    private String[] field_40425;
    @Shadow
    @Final
    private boolean field_43363;

    @Inject(method={"method_25426"}, at={@At(value="HEAD")})
    private void litematica_insertSignText(CallbackInfo ci) {
        if (Configs.Generic.SIGN_TEXT_PASTE.getBooleanValue()) {
            WorldUtils.insertSignTextFromSchematic(this.field_40424, this.field_40425, this.field_43363);
        }
    }
}

