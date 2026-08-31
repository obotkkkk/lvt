/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiTextFieldGeneric
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IGuiIcon
 *  fi.dy.masa.malilib.gui.interfaces.ISelectionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetCheckBox
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntry
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntryType
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_332
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.gui.GuiSchematicBrowserBase;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicBrowser;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetCheckBox;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.StringUtils;
import javax.annotation.Nullable;
import net.minecraft.class_332;

public abstract class GuiSchematicSaveBase
extends GuiSchematicBrowserBase
implements ISelectionListener<WidgetFileBrowserBase.DirectoryEntry> {
    protected GuiTextFieldGeneric textField;
    protected WidgetCheckBox checkboxIgnoreEntities;
    protected WidgetCheckBox checkboxVisibleOnly;
    protected WidgetCheckBox checkboxIncludeSupportBlocks;
    protected final WidgetCheckBox checkboxSaveFromSchematicWorld;
    protected String lastText = "";
    protected String defaultText = "";
    @Nullable
    protected final LitematicaSchematic schematic;

    public GuiSchematicSaveBase(@Nullable LitematicaSchematic schematic) {
        super(10, 80);
        this.schematic = schematic;
        this.textField = new GuiTextFieldGeneric(10, 32, 160, 20, this.textRenderer);
        this.textField.setMaxLengthWrapper(256);
        this.textField.setFocusedWrapper(true);
        this.checkboxSaveFromSchematicWorld = new WidgetCheckBox(0, 0, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, StringUtils.translate((String)"litematica.gui.label.schematic_save.checkbox.save_from_schematic_world", (Object[])new Object[0]), StringUtils.translate((String)"litematica.gui.label.schematic_save.hover_info.save_from_schematic_world", (Object[])new Object[0]));
    }

    @Override
    public int getBrowserHeight() {
        return this.getScreenHeight() - 80;
    }

    public void initGui() {
        super.initGui();
        boolean focused = this.textField.isFocusedWrapper();
        String text = this.textField.getTextWrapper();
        this.textField = new GuiTextFieldGeneric(10, 32, this.getScreenWidth() - 260, 18, this.textRenderer);
        this.textField.setTextWrapper(text);
        this.textField.setFocusedWrapper(focused);
        WidgetFileBrowserBase.DirectoryEntry entry = (WidgetFileBrowserBase.DirectoryEntry)((WidgetSchematicBrowser)this.getListWidget()).getLastSelectedEntry();
        if (this.lastText.isEmpty()) {
            if (entry != null && entry.getType() != WidgetFileBrowserBase.DirectoryEntryType.DIRECTORY && entry.getType() != WidgetFileBrowserBase.DirectoryEntryType.INVALID) {
                this.setTextFieldText(FileNameUtils.getFileNameWithoutExtension((String)entry.getName()));
            } else if (this.schematic != null) {
                this.setTextFieldText(this.schematic.getMetadata().getName());
            } else {
                this.setTextFieldText(this.defaultText);
            }
        }
        int x = this.textField.getXWrapper() + this.textField.getWidthWrapper() + 4;
        int y = 28;
        String str = StringUtils.translate((String)"litematica.gui.label.schematic_save.checkbox.ignore_entities", (Object[])new Object[0]);
        this.checkboxIgnoreEntities = new WidgetCheckBox(x, y, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, str);
        this.addWidget((WidgetBase)this.checkboxIgnoreEntities);
        this.checkboxSaveFromSchematicWorld.setPosition(x, y + 12);
        this.addWidget((WidgetBase)this.checkboxSaveFromSchematicWorld);
        this.checkboxVisibleOnly = new WidgetCheckBox(x, y + 24, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, StringUtils.translate((String)"litematica.gui.label.schematic_save.checkbox.visible_blocks_only", (Object[])new Object[0]));
        this.addWidget((WidgetBase)this.checkboxVisibleOnly);
        this.checkboxIncludeSupportBlocks = new WidgetCheckBox(x, y + 36, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, StringUtils.translate((String)"litematica.gui.label.schematic_save.checkbox.support_blocks", (Object[])new Object[0]), StringUtils.translate((String)"litematica.gui.label.schematic_save.hover_info.support_blocks", (Object[])new Object[0]));
        this.addWidget((WidgetBase)this.checkboxIncludeSupportBlocks);
        this.createButton(10, 54, ButtonType.SAVE);
    }

    protected void setTextFieldText(String text) {
        this.lastText = text;
        this.textField.setTextWrapper(text);
    }

    protected String getTextFieldText() {
        return this.textField.getTextWrapper();
    }

    protected abstract IButtonActionListener createButtonListener(ButtonType var1);

    private int createButton(int x, int y, ButtonType type) {
        String label = StringUtils.translate((String)type.getLabelKey(), (Object[])new Object[0]);
        int width = this.getStringWidth(label) + 10;
        ButtonGeneric button = type == ButtonType.SAVE ? new ButtonGeneric(x, y, width, 20, label, new String[]{"litematica.gui.label.schematic_save.hover_info.hold_shift_to_overwrite"}) : new ButtonGeneric(x, y, width, 20, label, new String[0]);
        this.addButton((ButtonBase)button, this.createButtonListener(type));
        return x + width + 4;
    }

    public void setString(String string) {
        this.setNextMessageType(Message.MessageType.ERROR);
        super.setString(string);
    }

    public void drawContents(class_332 drawContext, int mouseX, int mouseY, float partialTicks) {
        super.drawContents(drawContext, mouseX, mouseY, partialTicks);
        this.textField.renderWrapper(drawContext, mouseX, mouseY, partialTicks);
    }

    public void onSelectionChange(@Nullable WidgetFileBrowserBase.DirectoryEntry entry) {
        if (entry != null && entry.getType() != WidgetFileBrowserBase.DirectoryEntryType.DIRECTORY && entry.getType() != WidgetFileBrowserBase.DirectoryEntryType.INVALID) {
            this.setTextFieldText(FileNameUtils.getFileNameWithoutExtension((String)entry.getName()));
        }
    }

    @Override
    protected ISelectionListener<WidgetFileBrowserBase.DirectoryEntry> getSelectionListener() {
        return this;
    }

    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.textField.mouseClickedWrapper((double)mouseX, (double)mouseY, mouseButton)) {
            return true;
        }
        return super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (this.textField.keyPressedWrapper(keyCode, scanCode, modifiers)) {
            ((WidgetSchematicBrowser)this.getListWidget()).clearSelection();
            return true;
        }
        if (keyCode == 258) {
            this.textField.setFocusedWrapper(!this.textField.isFocusedWrapper());
            return true;
        }
        return super.onKeyTyped(keyCode, scanCode, modifiers);
    }

    public boolean onCharTyped(char charIn, int modifiers) {
        if (this.textField.charTypedWrapper(charIn, modifiers)) {
            ((WidgetSchematicBrowser)this.getListWidget()).clearSelection();
            return true;
        }
        return super.onCharTyped(charIn, modifiers);
    }

    public static enum ButtonType {
        SAVE("litematica.gui.button.save_schematic");

        private final String labelKey;

        private ButtonType(String labelKey) {
            this.labelKey = labelKey;
        }

        public String getLabelKey() {
            return this.labelKey;
        }
    }
}

