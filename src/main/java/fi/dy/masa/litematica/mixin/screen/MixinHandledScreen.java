/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.screen;

import fi.dy.masa.litematica.materials.MaterialListHudRenderer;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_465.class})
public abstract class MixinHandledScreen
extends class_437 {
    private MixinHandledScreen(class_2561 title) {
        super(title);
    }

    @Inject(method={"method_25394"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_437;method_25394(Lnet/minecraft/class_332;IIF)V")})
    private void litematica_renderSlotHighlightsPre(class_332 drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MaterialListHudRenderer.renderLookedAtBlockInInventory((class_465)this, this.field_22787);
    }

    @Inject(method={"method_25394"}, at={@At(value="TAIL")})
    private void litematica_renderSlotHighlightsPost(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MaterialListHudRenderer.renderLookedAtBlockInInventory((class_465)this, this.field_22787);
    }
}

