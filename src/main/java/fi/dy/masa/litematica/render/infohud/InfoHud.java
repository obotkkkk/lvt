/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.HudAlignment
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 */
package fi.dy.masa.litematica.render.infohud;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.render.infohud.IInfoHudRenderer;
import fi.dy.masa.litematica.render.infohud.RenderPhase;
import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_332;

public class InfoHud {
    private static final InfoHud INSTANCE = new InfoHud();
    protected final class_310 mc;
    protected final List<String> lineList = new ArrayList<String>();
    protected final List<IInfoHudRenderer> renderers = new ArrayList<IInfoHudRenderer>();
    protected boolean enabled = true;

    public static InfoHud getInstance() {
        return INSTANCE;
    }

    protected InfoHud() {
        this.mc = class_310.method_1551();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    protected double getScaleFactor() {
        return Configs.InfoOverlays.INFO_HUD_SCALE.getDoubleValue();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean toggleEnabled() {
        this.enabled = !this.enabled;
        return this.enabled;
    }

    protected int getOffsetX() {
        return Configs.InfoOverlays.INFO_HUD_OFFSET_X.getIntegerValue();
    }

    protected int getOffsetY() {
        return Configs.InfoOverlays.INFO_HUD_OFFSET_Y.getIntegerValue();
    }

    public void renderHud(class_332 drawContext) {
        if (this.mc.field_1724 != null && this.shouldRender()) {
            this.lineList.clear();
            int maxLines = Configs.InfoOverlays.INFO_HUD_MAX_LINES.getIntegerValue();
            int xOffset = this.getOffsetX();
            int yOffset = this.getOffsetY();
            boolean isGui = GuiUtils.getCurrentScreen() != null;
            double scale = Math.max(0.05, this.getScaleFactor());
            this.getLinesForPhase(RenderPhase.PRE, maxLines, isGui);
            this.updateHudText();
            this.getLinesForPhase(RenderPhase.POST, maxLines, isGui);
            if (!this.lineList.isEmpty()) {
                int ySize = RenderUtils.renderText((int)xOffset, (int)yOffset, (double)scale, (int)-1, (int)Integer.MIN_VALUE, (HudAlignment)this.getHudAlignment(), (boolean)true, (boolean)true, this.lineList, (class_332)drawContext);
                yOffset += (int)Math.ceil((double)ySize * scale);
            }
            if (!this.renderers.isEmpty()) {
                for (IInfoHudRenderer renderer : this.renderers) {
                    if (!renderer.getShouldRenderCustom() || isGui && !renderer.shouldRenderInGuis()) continue;
                    yOffset += renderer.render(xOffset, yOffset, this.getHudAlignment(), drawContext);
                }
            }
        }
    }

    protected void getLinesForPhase(RenderPhase phase, int maxLines, boolean isGui) {
        if (!this.renderers.isEmpty()) {
            for (int rendererIndex = 0; rendererIndex < this.renderers.size(); ++rendererIndex) {
                IInfoHudRenderer renderer = this.renderers.get(rendererIndex);
                if (!renderer.getShouldRenderText(phase) || isGui && !renderer.shouldRenderInGuis()) continue;
                List<String> lines = renderer.getText(phase);
                for (int i = 0; i < lines.size() && this.lineList.size() < maxLines; ++i) {
                    this.lineList.add(lines.get(i));
                }
                if (this.lineList.size() >= maxLines) break;
            }
        }
    }

    public void addInfoHudRenderer(IInfoHudRenderer renderer, boolean enable) {
        if (!this.renderers.contains(renderer)) {
            this.renderers.add(renderer);
        }
        this.enabled |= enable;
    }

    public void removeInfoHudRenderer(IInfoHudRenderer renderer, boolean disableIfEmpty) {
        this.renderers.remove(renderer);
        if (disableIfEmpty && this.renderers.isEmpty()) {
            this.enabled = false;
        }
    }

    public void removeInfoHudRenderersOfType(Class<? extends IInfoHudRenderer> clazz, boolean disableIfEmpty) {
        for (int i = 0; i < this.renderers.size(); ++i) {
            if (!this.renderers.get(i).getClass().isAssignableFrom(clazz)) continue;
            this.renderers.remove(i);
            --i;
        }
        if (disableIfEmpty && this.renderers.isEmpty()) {
            this.enabled = false;
        }
    }

    public void reset() {
        this.renderers.clear();
        this.lineList.clear();
    }

    protected boolean shouldRender() {
        return this.enabled;
    }

    protected HudAlignment getHudAlignment() {
        return (HudAlignment)Configs.InfoOverlays.INFO_HUD_ALIGNMENT.getOptionListValue();
    }

    protected void updateHudText() {
    }
}

