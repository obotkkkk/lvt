/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1747
 *  net.minecraft.class_1750
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.easyplace;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.util.PlacementHandler;
import net.minecraft.class_1747;
import net.minecraft.class_1750;
import net.minecraft.class_1792;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1747.class}, priority=1010)
public abstract class MixinBlockItem_easyPlace
extends class_1792 {
    private MixinBlockItem_easyPlace(class_1792.class_1793 builder) {
        super(builder);
    }

    @Shadow
    protected abstract class_2680 method_7707(class_1750 var1);

    @Shadow
    protected abstract boolean method_7709(class_1750 var1, class_2680 var2);

    @Shadow
    public abstract class_2248 method_7711();

    @Inject(method={"method_7707"}, at={@At(value="HEAD")}, cancellable=true)
    private void litematica_modifyPlacementState(class_1750 ctx, CallbackInfoReturnable<class_2680> cir) {
        class_2680 stateOrig;
        if (Configs.Generic.EASY_PLACE_MODE.getBooleanValue() && Configs.Generic.EASY_PLACE_SP_HANDLING.getBooleanValue() && (stateOrig = this.method_7711().method_9605(ctx)) != null && (!Configs.Generic.EASY_PLACE_SP_VALIDATION.getBooleanValue() || this.method_7709(ctx, stateOrig))) {
            PlacementHandler.UseContext context = PlacementHandler.UseContext.from(ctx, ctx.method_20287());
            cir.setReturnValue((Object)PlacementHandler.applyPlacementProtocolToPlacementState(stateOrig, context));
        }
    }
}

