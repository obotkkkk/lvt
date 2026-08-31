/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntrySortable
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package fi.dy.masa.litematica.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetListMaterialList;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.render.RenderUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntrySortable;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class WidgetMaterialListEntry
extends WidgetListEntrySortable<MaterialListEntry> {
    private static final String[] HEADERS = new String[]{"litematica.gui.label.material_list.title.item", "litematica.gui.label.material_list.title.total", "litematica.gui.label.material_list.title.missing", "litematica.gui.label.material_list.title.available"};
    private static int maxNameLength;
    private static int maxCountLength1;
    private static int maxCountLength2;
    private static int maxCountLength3;
    private final MaterialListBase materialList;
    private final WidgetListMaterialList listWidget;
    @Nullable
    private final MaterialListEntry entry;
    @Nullable
    private final String header1;
    @Nullable
    private final String header2;
    @Nullable
    private final String header3;
    @Nullable
    private final String header4;
    private final String shulkerBoxAbbr;
    private final boolean isOdd;

    public WidgetMaterialListEntry(int x, int y, int width, int height, boolean isOdd, MaterialListBase materialList, @Nullable MaterialListEntry entry, int listIndex, WidgetListMaterialList listWidget) {
        super(x, y, width, height, (Object)entry, listIndex);
        this.columnCount = 4;
        this.entry = entry;
        this.isOdd = isOdd;
        this.listWidget = listWidget;
        this.materialList = materialList;
        this.shulkerBoxAbbr = StringUtils.translate((String)"litematica.gui.label.material_list.abbr.shulker_box", (Object[])new Object[0]);
        if (this.entry != null) {
            this.header1 = null;
            this.header2 = null;
            this.header3 = null;
            this.header4 = null;
        } else {
            this.header1 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[0], (Object[])new Object[0]) + GuiBase.TXT_RST;
            this.header2 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[1], (Object[])new Object[0]) + GuiBase.TXT_RST;
            this.header3 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[2], (Object[])new Object[0]) + GuiBase.TXT_RST;
            this.header4 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[3], (Object[])new Object[0]) + GuiBase.TXT_RST;
        }
        int posX = x + width;
        int posY = y + 1;
        posX = this.createButtonGeneric(posX, posY, ButtonListener.ButtonType.IGNORE);
    }

    private int createButtonGeneric(int xRight, int y, ButtonListener.ButtonType type) {
        String label = type.getDisplayName();
        ButtonListener listener = new ButtonListener(type, this.materialList, this.entry, this.listWidget);
        return ((ButtonGeneric)this.addButton((ButtonBase)new ButtonGeneric(xRight, y, -1, true, label, new Object[0]), listener)).getX();
    }

    public static void setMaxNameLength(List<MaterialListEntry> materials, int multiplier) {
        maxNameLength = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[0], (Object[])new Object[0]) + GuiBase.TXT_RST));
        maxCountLength1 = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[1], (Object[])new Object[0]) + GuiBase.TXT_RST));
        maxCountLength2 = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[2], (Object[])new Object[0]) + GuiBase.TXT_RST));
        maxCountLength3 = StringUtils.getStringWidth((String)(GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[3], (Object[])new Object[0]) + GuiBase.TXT_RST));
        for (MaterialListEntry entry : materials) {
            int countTotal = entry.getCountTotal() * multiplier;
            int countMissing = multiplier == 1 ? entry.getCountMissing() : countTotal;
            maxNameLength = Math.max(maxNameLength, StringUtils.getStringWidth((String)entry.getStack().method_7964().getString()));
            maxCountLength1 = Math.max(maxCountLength1, StringUtils.getStringWidth((String)String.valueOf(countTotal)));
            maxCountLength2 = Math.max(maxCountLength2, StringUtils.getStringWidth((String)String.valueOf(countMissing)));
            maxCountLength3 = Math.max(maxCountLength3, StringUtils.getStringWidth((String)String.valueOf(entry.getCountAvailable())));
        }
    }

    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    protected int getCurrentSortColumn() {
        return this.materialList.getSortCriteria().ordinal();
    }

    protected boolean getSortInReverse() {
        return this.materialList.getSortInReverse();
    }

    protected int getColumnPosX(int column) {
        int x1 = this.x + 4;
        int x2 = x1 + maxNameLength + 40;
        int x3 = x2 + maxCountLength1 + 20;
        int x4 = x3 + maxCountLength2 + 20;
        switch (column) {
            case 0: {
                return x1;
            }
            case 1: {
                return x2;
            }
            case 2: {
                return x3;
            }
            case 3: {
                return x4;
            }
            case 4: {
                return x4 + maxCountLength3 + 20;
            }
        }
        return x1;
    }

    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (this.entry != null) {
            return false;
        }
        int column = this.getMouseOverColumn(mouseX, mouseY);
        switch (column) {
            case 0: {
                this.materialList.setSortCriteria(MaterialListBase.SortCriteria.NAME);
                break;
            }
            case 1: {
                this.materialList.setSortCriteria(MaterialListBase.SortCriteria.COUNT_TOTAL);
                break;
            }
            case 2: {
                this.materialList.setSortCriteria(MaterialListBase.SortCriteria.COUNT_MISSING);
                break;
            }
            case 3: {
                this.materialList.setSortCriteria(MaterialListBase.SortCriteria.COUNT_AVAILABLE);
                break;
            }
            default: {
                return false;
            }
        }
        this.listWidget.refreshEntries();
        return true;
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        if (this.header1 == null && (selected || this.isMouseOver(mouseX, mouseY))) {
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1603243920);
        } else if (this.isOdd) {
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1609560048);
        } else {
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1607454672);
        }
        int x1 = this.getColumnPosX(0);
        int x2 = this.getColumnPosX(1);
        int x3 = this.getColumnPosX(2);
        int x4 = this.getColumnPosX(3);
        int y = this.y + 7;
        int color = -1;
        if (this.header1 != null) {
            if (!this.listWidget.getSearchBarWidget().isSearchOpen()) {
                this.drawString(x1, y, color, this.header1, drawContext);
                this.drawString(x2, y, color, this.header2, drawContext);
                this.drawString(x3, y, color, this.header3, drawContext);
                this.drawString(x4, y, color, this.header4, drawContext);
                this.renderColumnHeader(mouseX, mouseY, Icons.ARROW_DOWN, Icons.ARROW_UP, drawContext);
            }
        } else if (this.entry != null) {
            int multiplier = this.materialList.getMultiplier();
            int countTotal = this.entry.getCountTotal() * multiplier;
            int countMissing = multiplier == 1 ? this.entry.getCountMissing() : countTotal;
            int countAvailable = this.entry.getCountAvailable();
            String green = GuiBase.TXT_GREEN;
            String gold = GuiBase.TXT_GOLD;
            String red = GuiBase.TXT_RED;
            this.drawString(x1 + 20, y, color, this.entry.getStack().method_7964().getString(), drawContext);
            this.drawString(x2, y, color, String.valueOf(countTotal), drawContext);
            String pre = countMissing == 0 ? green : (countAvailable >= countMissing ? gold : red);
            this.drawString(x3, y, color, pre + String.valueOf(countMissing), drawContext);
            pre = countAvailable >= countMissing ? green : red;
            this.drawString(x4, y, color, pre + String.valueOf(countAvailable), drawContext);
            drawContext.method_51448().method_22903();
            fi.dy.masa.malilib.render.RenderUtils.enableDiffuseLightingGui3D();
            y = this.y + 3;
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x1, (int)y, (int)16, (int)16, (int)0x20FFFFFF);
            drawContext.method_51427(this.entry.getStack(), x1, y);
            RenderSystem.disableBlend();
            fi.dy.masa.malilib.render.RenderUtils.disableDiffuseLighting();
            drawContext.method_51448().method_22909();
            super.render(mouseX, mouseY, selected, drawContext);
        }
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        if (this.entry != null) {
            class_4587 matrixStack = drawContext.method_51448();
            matrixStack.method_22903();
            matrixStack.method_46416(0.0f, 0.0f, 200.0f);
            String header1 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[0], (Object[])new Object[0]);
            String header2 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[1], (Object[])new Object[0]);
            String header3 = GuiBase.TXT_BOLD + StringUtils.translate((String)HEADERS[2], (Object[])new Object[0]);
            class_1799 stack = this.entry.getStack();
            String stackName = stack.method_7964().getString();
            int multiplier = this.materialList.getMultiplier();
            int total = this.entry.getCountTotal() * multiplier;
            int missing = multiplier == 1 ? this.entry.getCountMissing() : total;
            String strCountTotal = this.getFormattedCountString(total, stack.method_7914());
            String strCountMissing = this.getFormattedCountString(missing, stack.method_7914());
            int w1 = Math.max(this.getStringWidth(header1), Math.max(this.getStringWidth(header2), this.getStringWidth(header3)));
            int w2 = Math.max(this.getStringWidth(stackName) + 20, Math.max(this.getStringWidth(strCountTotal), this.getStringWidth(strCountMissing)));
            int totalWidth = w1 + w2 + 60;
            int x = mouseX + 10;
            int y = mouseY - 10;
            if (x + totalWidth - 20 >= this.width) {
                x -= totalWidth + 20;
            }
            int x1 = x + 10;
            int x2 = x1 + w1 + 20;
            RenderUtils.renderBackgroundMask(x + 1, y + 1, totalWidth - 2, 58, drawContext);
            fi.dy.masa.malilib.render.RenderUtils.drawOutlinedBox((int)x, (int)y, (int)totalWidth, (int)60, (int)-16777216, (int)-6710887);
            int y1 = y += 6;
            this.drawString(x1, y += 4, -1, header1, drawContext);
            this.drawString(x2 + 20, y, -1, stackName, drawContext);
            this.drawString(x1, y += 16, -1, header2, drawContext);
            this.drawString(x2, y, -1, strCountTotal, drawContext);
            this.drawString(x1, y += 16, -1, header3, drawContext);
            this.drawString(x2, y, -1, strCountMissing, drawContext);
            fi.dy.masa.malilib.render.RenderUtils.drawRect((int)x2, (int)y1, (int)16, (int)16, (int)0x20FFFFFF);
            fi.dy.masa.malilib.render.RenderUtils.enableDiffuseLightingGui3D();
            drawContext.method_51427(stack, x2, y1);
            fi.dy.masa.malilib.render.RenderUtils.disableDiffuseLighting();
            matrixStack.method_22909();
        }
    }

    private String getFormattedCountString(int total, int maxStackSize) {
        int stacks = total / maxStackSize;
        int remainder = total % maxStackSize;
        double boxCount = (double)total / (27.0 * (double)maxStackSize);
        String strCount = total > maxStackSize ? (maxStackSize > 1 ? (remainder > 0 ? String.format("%d = %d x %d + %d = %.2f %s", total, stacks, maxStackSize, remainder, boxCount, this.shulkerBoxAbbr) : String.format("%d = %d x %d = %.2f %s", total, stacks, maxStackSize, boxCount, this.shulkerBoxAbbr)) : String.format("%d = %.2f %s", total, boxCount, this.shulkerBoxAbbr)) : String.format("%d", total);
        return strCount;
    }

    static class ButtonListener
    implements IButtonActionListener {
        private final ButtonType type;
        private final MaterialListBase materialList;
        private final WidgetListMaterialList listWidget;
        private final MaterialListEntry entry;

        public ButtonListener(ButtonType type, MaterialListBase materialList, MaterialListEntry entry, WidgetListMaterialList listWidget) {
            this.type = type;
            this.materialList = materialList;
            this.listWidget = listWidget;
            this.entry = entry;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == ButtonType.IGNORE) {
                this.materialList.ignoreEntry(this.entry);
                this.listWidget.refreshEntries();
            }
        }

        public static enum ButtonType {
            IGNORE("litematica.gui.button.material_list.ignore");

            private final String translationKey;

            private ButtonType(String translationKey) {
                this.translationKey = translationKey;
            }

            public String getDisplayName() {
                return StringUtils.translate((String)this.translationKey, (Object[])new Object[0]);
            }
        }
    }
}

