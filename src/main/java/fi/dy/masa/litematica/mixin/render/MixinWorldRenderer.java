/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.minecraft.class_10209
 *  net.minecraft.class_1297
 *  net.minecraft.class_1921
 *  net.minecraft.class_310
 *  net.minecraft.class_3533
 *  net.minecraft.class_3695
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4604
 *  net.minecraft.class_638
 *  net.minecraft.class_757
 *  net.minecraft.class_761
 *  net.minecraft.class_9779
 *  net.minecraft.class_9922
 *  org.joml.Matrix4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.mixin.client.IMixinProfilerSystem;
import fi.dy.masa.litematica.render.LitematicaRenderer;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import java.util.List;
import net.minecraft.class_10209;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_310;
import net.minecraft.class_3533;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4604;
import net.minecraft.class_638;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_9779;
import net.minecraft.class_9922;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_761.class})
public abstract class MixinWorldRenderer {
    @Shadow
    private class_638 field_4085;
    @Shadow
    @Final
    private class_310 field_4088;
    @Unique
    private class_3695 profiler;

    @Unique
    private void litematica$prepareProfiler() {
        class_3533 ps;
        class_3695 class_36952;
        if (this.profiler == null) {
            this.profiler = class_10209.method_64146();
        }
        if ((class_36952 = this.profiler) instanceof class_3533 && !((IMixinProfilerSystem)(ps = (class_3533)class_36952)).litematica_isStarted()) {
            this.profiler.method_16065();
        }
    }

    @Inject(method={"method_3279()V"}, at={@At(value="RETURN")})
    private void litematica_onLoadRenderers(CallbackInfo ci) {
        if (this.field_4085 != null && this.field_4085 == this.field_4088.field_1687) {
            this.litematica$prepareProfiler();
            LitematicaRenderer.getInstance().loadRenderers(this.profiler);
            SchematicWorldRefresher.INSTANCE.updateAll();
        }
    }

    @Inject(method={"method_3273"}, at={@At(value="TAIL")})
    private void litematica_onPostSetupTerrain(class_4184 camera, class_4604 frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {
        this.litematica$prepareProfiler();
        LitematicaRenderer.getInstance().piecewisePrepare(frustum, this.profiler);
    }

    @Inject(method={"method_3269"}, at={@At(value="TAIL")})
    private void litematica_onPostSetupChunks(class_4184 camera, CallbackInfo ci) {
        this.litematica$prepareProfiler();
        LitematicaRenderer.getInstance().piecewiseUpdate(camera, this.profiler);
    }

    @Inject(method={"method_22710"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_761;method_62202(Lnet/minecraft/class_9909;Lnet/minecraft/class_4604;Lnet/minecraft/class_4184;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/class_9958;ZZLnet/minecraft/class_9779;Lnet/minecraft/class_3695;)V", shift=At.Shift.BEFORE)})
    private void litematica_onPreRenderMain(class_9922 allocator, class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, class_757 gameRenderer, Matrix4f positionMatrix, Matrix4f matrix4f2, CallbackInfo ci, @Local class_3695 profiler) {
        this.profiler = profiler;
    }

    @Inject(method={"method_3251"}, at={@At(value="TAIL")})
    private void litematica_onRenderLayer(class_1921 renderLayer, double x, double y, double z, Matrix4f viewMatrix, Matrix4f posMatrix, CallbackInfo ci) {
        this.litematica$prepareProfiler();
        if (renderLayer == class_1921.method_23577()) {
            LitematicaRenderer.getInstance().piecewiseRenderSolid(viewMatrix, posMatrix, this.profiler);
        } else if (renderLayer == class_1921.method_23579()) {
            LitematicaRenderer.getInstance().piecewiseRenderCutoutMipped(viewMatrix, posMatrix, this.profiler);
        } else if (renderLayer == class_1921.method_23581()) {
            LitematicaRenderer.getInstance().piecewiseRenderCutout(viewMatrix, posMatrix, this.profiler);
        } else if (renderLayer == class_1921.method_23583()) {
            LitematicaRenderer.getInstance().piecewiseRenderTranslucent(viewMatrix, posMatrix, this.profiler);
        } else if (renderLayer == class_1921.method_29997()) {
            LitematicaRenderer.getInstance().piecewiseRenderTripwire(viewMatrix, posMatrix, this.profiler);
            LitematicaRenderer.getInstance().piecewiseRenderOverlay(viewMatrix, posMatrix, this.profiler);
        }
    }

    @Inject(method={"method_62207"}, at={@At(value="RETURN")})
    private void litematica_onPostRenderEntities(class_4587 matrices, class_4597.class_4598 immediate, class_4184 camera, class_9779 tickCounter, List<class_1297> entities, CallbackInfo ci) {
        this.litematica$prepareProfiler();
        LitematicaRenderer.getInstance().piecewiseRenderEntities(matrices, immediate, tickCounter.method_60637(false), this.profiler);
    }

    @Inject(method={"method_62208"}, at={@At(value="RETURN")})
    private void litematica_onPostRenderBlockEntities(class_4587 matrices, class_4597.class_4598 entityVertexConsumers, class_4597.class_4598 effectVertexConsumers, class_4184 camera, float tickDelta, CallbackInfo ci) {
        this.litematica$prepareProfiler();
        LitematicaRenderer.getInstance().piecewiseRenderBlockEntities(matrices, entityVertexConsumers, effectVertexConsumers, tickDelta, this.profiler);
    }
}

