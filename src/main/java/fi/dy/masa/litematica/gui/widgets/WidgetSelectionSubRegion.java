/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiTextInputFeedback
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IMessageConsumer
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase
 *  fi.dy.masa.malilib.interfaces.IStringConsumerFeedback
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui.widgets;

import fi.dy.masa.litematica.gui.GuiAreaSelectionEditorSubRegion;
import fi.dy.masa.litematica.gui.widgets.WidgetListSelectionSubRegions;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextInputFeedback;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IMessageConsumer;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.interfaces.IStringConsumerFeedback;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.ArrayList;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class WidgetSelectionSubRegion
extends WidgetListEntryBase<String> {
    private final WidgetListSelectionSubRegions parent;
    private final AreaSelection selection;
    private final Box box;
    private final boolean isOdd;
    private final int buttonsStartX;

    public WidgetSelectionSubRegion(int x, int y, int width, int height, boolean isOdd, String entry, int listIndex, AreaSelection selection, WidgetListSelectionSubRegions parent) {
        super(x, y, width, height, (Object)entry, listIndex);
        this.selection = selection;
        this.box = selection.getSubRegionBox(entry);
        this.isOdd = isOdd;
        this.parent = parent;
        int posX = x + width - 2;
        int posY = y + 1;
        posX = this.createButton(posX, posY, ButtonListener.ButtonType.REMOVE);
        posX = this.createButton(posX, posY, ButtonListener.ButtonType.RENAME);
        this.buttonsStartX = posX = this.createButton(posX, posY, ButtonListener.ButtonType.CONFIGURE);
    }

    private int createButton(int x, int y, ButtonListener.ButtonType type) {
        return ((ButtonGeneric)this.addButton((ButtonBase)new ButtonGeneric(x, y, -1, true, type.getDisplayName(), new Object[0]), new ButtonListener(type, this))).getX() - 1;
    }

    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
        return mouseX < this.buttonsStartX && super.canSelectAt(mouseX, mouseY, mouseButton);
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        selected = ((String)this.entry).equals(this.selection.getCurrentSubRegionBoxName());
        if (selected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1603243920);
        } else if (this.isOdd) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1609560048);
        } else {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1607454672);
        }
        if (selected) {
            RenderUtils.drawOutline((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-2039584, (float)0.001f);
        }
        this.drawString(this.x + 2, this.y + 7, -1, (String)this.entry, drawContext);
        super.render(mouseX, mouseY, selected, drawContext);
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        int offset;
        ArrayList<String> text = new ArrayList<String>();
        if (this.box != null) {
            String str;
            class_2338 pos1 = this.box.getPos1();
            class_2338 pos2 = this.box.getPos2();
            if (pos1 != null) {
                str = StringUtils.translate((String)"litematica.gui.label.area_editor.pos1", (Object[])new Object[0]);
                text.add(String.format("%s: x: %d, y: %d, z: %d", str, pos1.method_10263(), pos1.method_10264(), pos1.method_10260()));
            }
            if (pos2 != null) {
                str = StringUtils.translate((String)"litematica.gui.label.area_editor.pos2", (Object[])new Object[0]);
                text.add(String.format("%s: x: %d, y: %d, z: %d", str, pos2.method_10263(), pos2.method_10264(), pos2.method_10260()));
            }
            if (pos1 != null && pos2 != null) {
                str = StringUtils.translate((String)"litematica.gui.label.area_editor.dimensions", (Object[])new Object[0]);
                class_2338 size = PositionUtils.getAreaSizeFromRelativeEndPosition(pos2.method_10059((class_2382)pos1));
                text.add(String.format("%s: %d x %d x %d", str, Math.abs(size.method_10263()), Math.abs(size.method_10264()), Math.abs(size.method_10260())));
            }
        }
        if (GuiBase.isMouseOver((int)mouseX, (int)mouseY, (int)this.x, (int)this.y, (int)(this.buttonsStartX - (offset = 12)), (int)this.height)) {
            RenderUtils.drawHoverText((int)mouseX, (int)mouseY, text, (class_332)drawContext);
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final WidgetSelectionSubRegion widget;
        private final ButtonType type;

        public ButtonListener(ButtonType type, WidgetSelectionSubRegion widget) {
            this.type = type;
            this.widget = widget;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == ButtonType.RENAME) {
                String title = "litematica.gui.title.rename_area_sub_region";
                String name = this.widget.box != null ? this.widget.box.getName() : "<error>";
                BoxRenamer renamer = new BoxRenamer(this.widget.selection, this.widget);
                GuiBase.openGui((class_437)new GuiTextInputFeedback(160, title, name, (class_437)this.widget.parent.getEditorGui(), (IStringConsumerFeedback)renamer));
            } else if (this.type == ButtonType.REMOVE) {
                this.widget.selection.removeSubRegionBox((String)this.widget.entry);
                this.widget.parent.refreshEntries();
            } else if (this.type == ButtonType.CONFIGURE) {
                GuiAreaSelectionEditorSubRegion gui = new GuiAreaSelectionEditorSubRegion(this.widget.selection, this.widget.box);
                gui.setParent(GuiUtils.getCurrentScreen());
                GuiBase.openGui((class_437)gui);
            }
        }

        public static enum ButtonType {
            RENAME("litematica.gui.button.rename"),
            CONFIGURE("litematica.gui.button.configure"),
            REMOVE(GuiBase.TXT_RED + "-");

            private final String labelKey;

            private ButtonType(String labelKey) {
                this.labelKey = labelKey;
            }

            public String getDisplayName() {
                return StringUtils.translate((String)this.labelKey, (Object[])new Object[0]);
            }
        }
    }

    private static class BoxRenamer
    implements IStringConsumerFeedback {
        private final WidgetSelectionSubRegion widget;
        private final AreaSelection selection;

        public BoxRenamer(AreaSelection selection, WidgetSelectionSubRegion widget) {
            this.widget = widget;
            this.selection = selection;
        }

        public boolean setString(String string) {
            String oldName = (String)this.widget.entry;
            return this.selection.renameSubRegionBox(oldName, string, (IMessageConsumer)this.widget.parent.getEditorGui());
        }
    }
}

