/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.HudAlignment
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1735
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2371
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_465
 */
package fi.dy.masa.litematica.materials;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListSorter;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.mixin.screen.IMixinHandledScreen;
import fi.dy.masa.litematica.render.infohud.IInfoHudRenderer;
import fi.dy.masa.litematica.render.infohud.RenderPhase;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2371;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_465;

public class MaterialListHudRenderer
implements IInfoHudRenderer {
    protected static class_2680 lastLookedAtBlock = class_2246.field_10124.method_9564();
    protected static class_1799 lastLookedAtBlocksItem = class_1799.field_8037;
    protected final MaterialListBase materialList;
    protected final MaterialListSorter sorter;
    protected boolean shouldRender;
    protected long lastUpdateTime;

    public MaterialListHudRenderer(MaterialListBase materialList) {
        this.materialList = materialList;
        this.sorter = new MaterialListSorter(materialList);
    }

    @Override
    public boolean getShouldRenderText(RenderPhase phase) {
        return false;
    }

    @Override
    public boolean getShouldRenderCustom() {
        return this.shouldRender;
    }

    @Override
    public boolean shouldRenderInGuis() {
        return Configs.Generic.RENDER_MATERIALS_IN_GUI.getBooleanValue();
    }

    public void toggleShouldRender() {
        this.shouldRender = !this.shouldRender;
    }

    @Override
    public List<String> getText(RenderPhase phase) {
        return Collections.emptyList();
    }

    @Override
    public int render(int xOffset, int yOffset, HudAlignment alignment, class_332 drawContext) {
        List<MaterialListEntry> list;
        class_310 mc = class_310.method_1551();
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastUpdateTime > 2000L) {
            MaterialListUtils.updateAvailableCounts(this.materialList.getMaterialsAll(), (class_1657)mc.field_1724);
            list = this.materialList.getMaterialsMissingOnly(true);
            Collections.sort(list, this.sorter);
            this.lastUpdateTime = currentTime;
        } else {
            list = this.materialList.getMaterialsMissingOnly(false);
        }
        if (list.size() == 0) {
            return 0;
        }
        class_327 font = mc.field_1772;
        double scale = Configs.InfoOverlays.MATERIAL_LIST_HUD_SCALE.getDoubleValue();
        int maxLines = Configs.InfoOverlays.MATERIAL_LIST_HUD_MAX_LINES.getIntegerValue();
        int bgMargin = 2;
        int lineHeight = 16;
        int contentHeight = Math.min(list.size(), maxLines) * lineHeight + bgMargin + 10;
        int maxTextLength = 0;
        int maxCountLength = 0;
        int posX = xOffset + bgMargin;
        int posY = yOffset + bgMargin;
        int bgColor = -1610612736;
        int textColor = -1;
        boolean useBackground = true;
        boolean useShadow = false;
        int size = Math.min(list.size(), maxLines);
        if (scale == 0.0) {
            return 0;
        }
        for (int i = 0; i < size; ++i) {
            MaterialListEntry entry = list.get(i);
            maxTextLength = Math.max(maxTextLength, font.method_1727(entry.getStack().method_7964().getString()));
            int multiplier = this.materialList.getMultiplier();
            int count = multiplier == 1 ? entry.getCountMissing() - entry.getCountAvailable() : entry.getCountTotal();
            String strCount = GuiBase.TXT_RED + this.getFormattedCountString(count *= multiplier, entry.getStack().method_7914()) + GuiBase.TXT_RST;
            maxCountLength = Math.max(maxCountLength, font.method_1727(strCount));
        }
        int maxLineLength = maxTextLength + maxCountLength + 30;
        switch (alignment) {
            case TOP_RIGHT: 
            case BOTTOM_RIGHT: {
                posX = (int)((double)GuiUtils.getScaledWindowWidth() / scale - (double)maxLineLength - (double)xOffset - (double)bgMargin);
                break;
            }
            case CENTER: {
                posX = (int)((double)GuiUtils.getScaledWindowWidth() / scale / 2.0 - (double)(maxLineLength / 2) - (double)xOffset);
                break;
            }
        }
        if (scale != 1.0 && scale != 0.0) {
            yOffset = (int)((double)yOffset / scale);
        }
        posY = RenderUtils.getHudPosY((int)posY, (int)yOffset, (int)contentHeight, (double)scale, (HudAlignment)alignment);
        posY += RenderUtils.getHudOffsetForPotions((HudAlignment)alignment, (double)scale, (class_1657)mc.field_1724);
        class_4587 matrixStack = drawContext.method_51448();
        if (scale != 1.0) {
            matrixStack.method_22903();
            matrixStack.method_22905((float)scale, (float)scale, (float)scale);
        }
        if (useBackground) {
            int x1 = posX - bgMargin;
            int y1 = posY - bgMargin;
            int x2 = x1 + maxLineLength + bgMargin * 2;
            int y2 = y1 + contentHeight + bgMargin;
            drawContext.method_25294(x1, y1, x2, y2, bgColor);
        }
        int x = posX;
        int y = posY + 12;
        RenderUtils.setupBlend();
        for (int i = 0; i < size; ++i) {
            drawContext.method_51427(list.get(i).getStack(), x, y);
            y += lineHeight;
        }
        String title = GuiBase.TXT_BOLD + StringUtils.translate((String)"litematica.gui.button.material_list", (Object[])new Object[0]) + GuiBase.TXT_RST;
        drawContext.method_51433(font, title, posX + 2, posY + 2, textColor, useShadow);
        int itemCountTextColor = Configs.Colors.MATERIAL_LIST_HUD_ITEM_COUNTS.getIntegerValue();
        x = posX + 18;
        y = posY + 16;
        for (int i = 0; i < size; ++i) {
            MaterialListEntry entry = list.get(i);
            String text = entry.getStack().method_7964().getString();
            int multiplier = this.materialList.getMultiplier();
            int count = multiplier == 1 ? entry.getCountMissing() - entry.getCountAvailable() : entry.getCountTotal();
            String strCount = this.getFormattedCountString(count *= multiplier, entry.getStack().method_7914());
            int cntLen = font.method_1727(strCount);
            int cntPosX = posX + maxLineLength - cntLen - 2;
            drawContext.method_51433(font, text, x, y, textColor, useShadow);
            drawContext.method_51433(font, strCount, cntPosX, y, itemCountTextColor, useShadow);
            y += lineHeight;
        }
        if (scale != 1.0) {
            matrixStack.method_22909();
        }
        return contentHeight + 4;
    }

    protected String getFormattedCountString(int count, int maxStackSize) {
        int stacks = count / maxStackSize;
        int remainder = count % maxStackSize;
        double boxCount = (double)count / (27.0 * (double)maxStackSize);
        if (count > maxStackSize) {
            if (boxCount >= 1.0) {
                return String.format("%d (%.2f %s)", count, boxCount, StringUtils.translate((String)"litematica.gui.label.material_list.abbr.shulker_box", (Object[])new Object[0]));
            }
            if (remainder > 0) {
                return String.format("%d (%d x %d + %d)", count, stacks, maxStackSize, remainder);
            }
            return String.format("%d (%d x %d)", count, stacks, maxStackSize);
        }
        return String.format("%d", count);
    }

    public static void renderLookedAtBlockInInventory(class_465<?> gui, class_310 mc) {
        RayTraceUtils.RayTraceWrapper traceWrapper;
        if (Configs.Generic.HIGHLIGHT_BLOCK_IN_INV.getBooleanValue() && (traceWrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, (class_1297)mc.field_1724, 10.0)) != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_2338 pos = traceWrapper.getBlockHitResult().method_17777();
            class_2680 state = SchematicWorldHandler.getSchematicWorld().method_8320(pos);
            if (state != lastLookedAtBlock) {
                lastLookedAtBlock = state;
                lastLookedAtBlocksItem = MaterialCache.getInstance().getRequiredBuildItemForState(state);
            }
            Color4f color = Configs.Colors.HIGHTLIGHT_BLOCK_IN_INV_COLOR.getColor();
            MaterialListHudRenderer.highlightSlotsWithItem(lastLookedAtBlocksItem, gui, color, mc);
        }
    }

    public static void highlightSlotsWithItem(class_1799 referenceItem, class_465<?> gui, Color4f color, class_310 mc) {
        class_2371 slots = gui.method_17577().field_7761;
        RenderUtils.setupBlend();
        int guiX = ((IMixinHandledScreen)gui).litematica_getX();
        int guiY = ((IMixinHandledScreen)gui).litematica_getY();
        for (class_1735 slot : slots) {
            if (!slot.method_7681() || !InventoryUtils.areStacksEqualIgnoreNbt((class_1799)slot.method_7677(), (class_1799)referenceItem) && !fi.dy.masa.litematica.util.InventoryUtils.doesShulkerBoxContainItem(slot.method_7677(), referenceItem) && !fi.dy.masa.litematica.util.InventoryUtils.doesBundleContainItem(slot.method_7677(), referenceItem)) continue;
            MaterialListHudRenderer.renderOutlinedBox(guiX + slot.field_7873, guiY + slot.field_7872, 16, 16, color.intValue, color.intValue | 0xFF000000, 1.0f);
        }
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void renderOutlinedBox(int x, int y, int width, int height, int colorBg, int colorBorder, float zLevel) {
        RenderUtils.drawRect((int)(x + 1), (int)(y + 1), (int)(width - 2), (int)(height - 2), (int)colorBg, (float)zLevel);
        RenderUtils.drawOutline((int)x, (int)y, (int)width, (int)height, (int)1, (int)colorBorder, (float)zLevel);
    }
}

