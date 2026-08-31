/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IRenderer
 *  fi.dy.masa.malilib.util.GuiUtils
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_3695
 *  net.minecraft.class_4184
 *  net.minecraft.class_4604
 *  net.minecraft.class_9958
 *  org.joml.Matrix4f
 */
package fi.dy.masa.litematica.event;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicManager;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.render.infohud.ToolHud;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.malilib.interfaces.IRenderer;
import fi.dy.masa.malilib.util.GuiUtils;
import java.util.function.Supplier;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_9958;
import org.joml.Matrix4f;

public class RenderHandler
implements IRenderer {
    public void onRenderWorldLastAdvanced(Matrix4f posMatrix, Matrix4f projMatrix, class_4604 frustum, class_4184 camera, class_9958 fog, class_3695 profiler) {
    }

    public void onRenderWorldPreWeather(Matrix4f posMatrix, Matrix4f projMatrix, class_4604 frustum, class_4184 camera, class_9958 fog, class_3695 profiler) {
        class_310 mc = class_310.method_1551();
        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && mc.field_1724 != null) {
            profiler.method_15396("overlay_boxes");
            OverlayRenderer.getInstance().renderBoxes(posMatrix, projMatrix, profiler);
            if (Configs.InfoOverlays.VERIFIER_OVERLAY_ENABLED.getBooleanValue()) {
                profiler.method_15405("overlay_mismatches");
                OverlayRenderer.getInstance().renderSchematicVerifierMismatches(posMatrix, projMatrix, profiler);
            }
            if (DataManager.getToolMode() == ToolMode.REBUILD) {
                profiler.method_15405("overlay_targeting");
                OverlayRenderer.getInstance().renderSchematicRebuildTargetingOverlay(posMatrix, projMatrix, profiler);
            }
            profiler.method_15407();
        }
    }

    public Supplier<String> getProfilerSectionSupplier() {
        return () -> "litematica_render_handler";
    }

    public void onRenderGameOverlayPostAdvanced(class_332 drawContext, float partialTicks, class_3695 profiler, class_310 mc) {
        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && mc.field_1724 != null) {
            profiler.method_15396("overlay_hud");
            InfoHud.getInstance().renderHud(drawContext);
            if (GuiUtils.getCurrentScreen() == null) {
                if (!mc.field_1690.field_1842) {
                    ToolHud.getInstance().renderHud(drawContext);
                    profiler.method_15405("overlay_hover_info");
                    OverlayRenderer.getInstance().renderHoverInfo(mc, drawContext, profiler);
                }
                if (GuiSchematicManager.hasPendingPreviewTask()) {
                    profiler.method_15405("overlay_preview_frame");
                    OverlayRenderer.getInstance().renderPreviewFrame(mc, drawContext, profiler);
                }
            }
            profiler.method_15407();
        }
    }
}

