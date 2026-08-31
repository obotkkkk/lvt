/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  net.minecraft.class_1799
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_7923
 */
package fi.dy.masa.litematica.render;

import fi.dy.masa.litematica.render.RenderUtils;
import fi.dy.masa.litematica.util.ItemUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.List;
import java.util.Objects;
import net.minecraft.class_1799;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_7923;

public class BlockInfo {
    private final String title;
    private final class_2680 state;
    private final class_1799 stack;
    private final String blockRegistryName;
    private final String stackName;
    private final List<String> props;
    private final int totalWidth;
    private final int totalHeight;
    private boolean useBackgroundMask = false;

    public BlockInfo(class_2680 state, String titleKey) {
        String pre = GuiBase.TXT_WHITE + GuiBase.TXT_BOLD;
        this.title = pre + StringUtils.translate((String)titleKey, (Object[])new Object[0]) + GuiBase.TXT_RST;
        this.state = state;
        this.stack = ItemUtils.getItemForState(this.state);
        class_2960 rl = class_7923.field_41175.method_10221((Object)this.state.method_26204());
        this.blockRegistryName = rl.toString();
        this.stackName = this.stack.method_7964().getString();
        int w = StringUtils.getStringWidth((String)this.stackName) + 20;
        w = Math.max(w, StringUtils.getStringWidth((String)this.blockRegistryName));
        w = Math.max(w, StringUtils.getStringWidth((String)this.title));
        this.props = BlockUtils.getFormattedBlockStateProperties((class_2680)this.state, (String)" = ");
        this.totalWidth = w + 40;
        this.totalHeight = this.props.size() * (StringUtils.getFontHeight() + 2) + 60;
    }

    public int getTotalWidth() {
        return this.totalWidth;
    }

    public int getTotalHeight() {
        return this.totalHeight;
    }

    public void toggleUseBackgroundMask(boolean toggle) {
        this.useBackgroundMask = toggle;
    }

    public void render(int x, int y, class_310 mc, class_332 drawContext) {
        if (this.state != null) {
            if (this.useBackgroundMask) {
                RenderUtils.renderBackgroundMask(x + 1, y + 1, this.totalWidth - 1, this.totalHeight - 1, drawContext);
            }
            fi.dy.masa.malilib.render.RenderUtils.drawOutlinedBox((int)x, (int)y, (int)this.totalWidth, (int)this.totalHeight, (int)-16777216, (int)-6710887);
            class_327 textRenderer = mc.field_1772;
            int x1 = x + 10;
            drawContext.method_51433(textRenderer, this.title, x1, y += 4, -1, false);
            fi.dy.masa.malilib.render.RenderUtils.enableDiffuseLightingGui3D();
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x1, (int)(y += 12), (int)16, (int)16, (int)0x20FFFFFF);
            drawContext.method_51427(this.stack, x1, y);
            drawContext.method_51431(textRenderer, this.stack, x1, y);
            fi.dy.masa.malilib.render.RenderUtils.disableDiffuseLighting();
            drawContext.method_51433(textRenderer, this.stackName, x1 + 20, y + 4, -1, false);
            drawContext.method_51433(textRenderer, this.blockRegistryName, x1, y += 20, -12558081, false);
            Objects.requireNonNull(textRenderer);
            fi.dy.masa.malilib.render.RenderUtils.renderText((int)x1, (int)(y += 9 + 4), (int)-5197648, this.props, (class_332)drawContext);
        }
    }
}

