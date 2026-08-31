/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiTextFieldGeneric
 *  fi.dy.masa.malilib.gui.GuiTextFieldInteger
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.ButtonOnOff
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IGuiIcon
 *  fi.dy.masa.malilib.gui.interfaces.IMessageConsumer
 *  fi.dy.masa.malilib.gui.interfaces.ISelectionListener
 *  fi.dy.masa.malilib.gui.interfaces.ITextFieldListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetCheckBox
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.PositionUtils$CoordinateType
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.GuiPlacementConfiguration;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.interfaces.IMessageConsumer;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetCheckBox;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.StringUtils;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_437;

public class GuiSubRegionConfiguration
extends GuiBase {
    private final SchematicPlacement schematicPlacement;
    private final SubRegionPlacement placement;
    private ButtonGeneric buttonResetPlacement;

    public GuiSubRegionConfiguration(SchematicPlacement schematicPlacement, SubRegionPlacement placement) {
        this.schematicPlacement = schematicPlacement;
        this.placement = placement;
        this.title = StringUtils.translate((String)"litematica.gui.title.configure_schematic_sub_region", (Object[])new Object[0]);
    }

    public void initGui() {
        super.initGui();
        int width = 120;
        int x = this.getScreenWidth() - width - 10;
        int y = 26;
        String label = StringUtils.translate((String)"litematica.gui.label.placement_sub.region_name", (Object[])new Object[]{this.placement.getName()});
        this.addLabel(20, y, -1, 16, -1, new String[]{label});
        y = 10;
        this.createButtonOnOff(x, y, width - 22, this.placement.isEnabled(), ButtonListener.Type.TOGGLE_ENABLED);
        this.createButton(x + width - 20, y, 20, ButtonListener.Type.TOGGLE_RENDERING);
        this.createButtonOnOff(x, y += 21, width, this.placement.ignoreEntities(), ButtonListener.Type.TOGGLE_ENTITIES);
        label = StringUtils.translate((String)"litematica.gui.label.placement_sub.region_position", (Object[])new Object[0]);
        this.addLabel(x, y += 18, width, 20, -1, new String[]{label});
        this.createCoordinateInput(x += 2, y += 14, 70, PositionUtils.CoordinateType.X);
        this.createButton(x + 85, y + 1, -1, ButtonListener.Type.NUDGE_COORD_X);
        this.createCoordinateInput(x, y += 18, 70, PositionUtils.CoordinateType.Y);
        this.createButton(x + 85, y + 1, -1, ButtonListener.Type.NUDGE_COORD_Y);
        this.createCoordinateInput(x, y += 18, 70, PositionUtils.CoordinateType.Z);
        this.createButton(x + 85, y + 1, -1, ButtonListener.Type.NUDGE_COORD_Z);
        this.createButton(x -= 2, y += 21, width, ButtonListener.Type.MOVE_TO_PLAYER);
        this.createButton(x, y += 21, width, ButtonListener.Type.ROTATE);
        this.createButton(x, y += 21, width, ButtonListener.Type.MIRROR);
        this.createButton(x, y += 21, width, ButtonListener.Type.RESET_PLACEMENT);
        this.createButton(x, y += 21, width, ButtonListener.Type.SLICE_TYPE);
        y = this.getScreenHeight() - 36;
        label = StringUtils.translate((String)"litematica.gui.button.placement_sub.placement_configuration", (Object[])new Object[0]);
        int buttonWidth = this.getStringWidth(label) + 10;
        x = 10;
        ButtonGeneric button = new ButtonGeneric(x, y, buttonWidth, 20, label, new String[0]);
        this.addButton((ButtonBase)button, new ButtonListener(ButtonListener.Type.PLACEMENT_CONFIGURATION, this.schematicPlacement, this.placement, this));
        GuiMainMenu.ButtonListenerChangeMenu.ButtonType type = GuiMainMenu.ButtonListenerChangeMenu.ButtonType.MAIN_MENU;
        label = StringUtils.translate((String)type.getLabelKey(), (Object[])new Object[0]);
        int menuButtonWidth = this.getStringWidth(label) + 20;
        x = GuiUtils.getScaledWindowWidth() >= 270 ? this.getScreenWidth() - menuButtonWidth - 10 : x + buttonWidth + 4;
        button = new ButtonGeneric(x, y, menuButtonWidth, 20, label, new String[0]);
        this.addButton((ButtonBase)button, new GuiMainMenu.ButtonListenerChangeMenu(type, this.getParent()));
        this.updateElements();
    }

    private void createCoordinateInput(int x, int y, int width, PositionUtils.CoordinateType type) {
        String label = type.name() + ":";
        this.addLabel(x, y, width, 20, -1, new String[]{label});
        int offset = this.getStringWidth(label) + 4;
        class_2338 pos = this.placement.getPos();
        pos = PositionUtils.getTransformedBlockPos(pos, this.schematicPlacement.getMirror(), this.schematicPlacement.getRotation());
        pos = pos.method_10081((class_2382)this.schematicPlacement.getOrigin());
        String text = "";
        switch (type) {
            case X: {
                text = String.valueOf(pos.method_10263());
                break;
            }
            case Y: {
                text = String.valueOf(pos.method_10264());
                break;
            }
            case Z: {
                text = String.valueOf(pos.method_10260());
            }
        }
        GuiTextFieldInteger textField = new GuiTextFieldInteger(x + offset, y + 2, width, 14, this.textRenderer);
        textField.setTextWrapper(text);
        TextFieldListener listener = new TextFieldListener(type, this.schematicPlacement, this.placement, this);
        this.addTextField((GuiTextFieldGeneric)textField, listener);
        String hover = StringUtils.translate((String)"litematica.hud.schematic_placement.hover_info.lock_coordinate", (Object[])new Object[0]);
        x = x + offset + width + 20;
        WidgetCheckBox cb = new WidgetCheckBox(x, y + 3, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, "", hover);
        cb.setChecked(this.placement.isCoordinateLocked(type), false);
        cb.setListener((ISelectionListener)new CoordinateLockListener(type, this.placement));
        this.addWidget((WidgetBase)cb);
    }

    private int createButtonOnOff(int x, int y, int width, boolean isCurrentlyOn, ButtonListener.Type type) {
        ButtonOnOff button = new ButtonOnOff(x, y, width, false, type.getTranslationKey(), isCurrentlyOn, new String[0]);
        this.addButton((ButtonBase)button, new ButtonListener(type, this.schematicPlacement, this.placement, this));
        return button.getWidth();
    }

    private void createButton(int x, int y, int width, ButtonListener.Type type) {
        Object label;
        ButtonListener listener = new ButtonListener(type, this.schematicPlacement, this.placement, this);
        switch (type.ordinal()) {
            case 2: {
                boolean enabled = this.placement.isRenderingEnabled();
                String pre = enabled ? TXT_GREEN : TXT_RED;
                label = pre + type.getDisplayName(new Object[0]) + TXT_RST;
                String str = pre + StringUtils.translate((String)("litematica.message.value." + (enabled ? "on" : "off")), (Object[])new Object[0]) + TXT_RST;
                String hover = StringUtils.translate((String)"litematica.gui.button.schematic_placement.hover.rendering", (Object[])new Object[]{str});
                this.addButton((ButtonBase)new ButtonGeneric(x, y, width, 20, (String)label, new String[]{hover}), listener);
                break;
            }
            case 5: 
            case 6: 
            case 7: {
                String hover = StringUtils.translate((String)"litematica.gui.button.hover.plus_minus_tip", (Object[])new Object[0]);
                ButtonGeneric button = new ButtonGeneric(x, y, (IGuiIcon)Icons.BUTTON_PLUS_MINUS_16, new String[]{hover});
                this.addButton((ButtonBase)button, listener);
                return;
            }
            case 8: {
                label = type.getDisplayName(PositionUtils.getRotationNameShort(this.placement.getRotation()));
                break;
            }
            case 9: {
                label = type.getDisplayName(PositionUtils.getMirrorName(this.placement.getMirror()));
                break;
            }
            case 11: {
                label = type.getDisplayName("todo");
                break;
            }
            default: {
                label = type.getDisplayName(new Object[0]);
            }
        }
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, (String)label, new String[0]);
        this.addButton((ButtonBase)button, listener);
        if (type == ButtonListener.Type.RESET_PLACEMENT) {
            this.buttonResetPlacement = button;
        }
    }

    private void updateElements() {
        Object label = StringUtils.translate((String)"litematica.gui.button.placement_sub.reset_sub_region_placement", (Object[])new Object[0]);
        boolean enabled = this.placement.isRegionPlacementModifiedFromDefault();
        if (enabled) {
            label = TXT_GOLD + (String)label + TXT_RST;
        }
        this.buttonResetPlacement.setDisplayString((String)label);
        this.buttonResetPlacement.setEnabled(enabled);
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final GuiBase parent;
        private final SchematicPlacement schematicPlacement;
        private final SubRegionPlacement placement;
        private final Type type;
        private final String subRegionName;

        public ButtonListener(Type type, SchematicPlacement schematicPlacement, SubRegionPlacement placement, GuiBase parent) {
            this.type = type;
            this.schematicPlacement = schematicPlacement;
            this.placement = placement;
            this.parent = parent;
            this.subRegionName = placement.getName();
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            int amount;
            int n = amount = mouseButton == 1 ? -1 : 1;
            if (GuiBase.isShiftDown()) {
                amount *= 8;
            }
            if (GuiBase.isAltDown()) {
                amount *= 4;
            }
            class_2338 posOld = this.placement.getPos();
            posOld = PositionUtils.getTransformedBlockPos(posOld, this.schematicPlacement.getMirror(), this.schematicPlacement.getRotation());
            posOld = posOld.method_10081((class_2382)this.schematicPlacement.getOrigin());
            this.parent.setNextMessageType(Message.MessageType.ERROR);
            switch (this.type.ordinal()) {
                case 0: {
                    GuiBase.openGui((class_437)new GuiPlacementConfiguration(this.schematicPlacement));
                    break;
                }
                case 8: {
                    boolean reverse = mouseButton == 1;
                    class_2470 rotation = PositionUtils.cycleRotation(this.placement.getRotation(), reverse);
                    this.schematicPlacement.setSubRegionRotation(this.subRegionName, rotation, (IMessageConsumer)this.parent);
                    break;
                }
                case 9: {
                    boolean reverse = mouseButton == 1;
                    class_2415 mirror = PositionUtils.cycleMirror(this.placement.getMirror(), reverse);
                    this.schematicPlacement.setSubRegionMirror(this.subRegionName, mirror, (IMessageConsumer)this.parent);
                    break;
                }
                case 4: {
                    this.schematicPlacement.moveSubRegionTo(this.subRegionName, class_2338.method_49638((class_2374)this.parent.mc.field_1724.method_19538()), (IStringConsumer)this.parent);
                    break;
                }
                case 5: {
                    this.schematicPlacement.moveSubRegionTo(this.subRegionName, posOld.method_10069(amount, 0, 0), (IStringConsumer)this.parent);
                    break;
                }
                case 6: {
                    this.schematicPlacement.moveSubRegionTo(this.subRegionName, posOld.method_10069(0, amount, 0), (IStringConsumer)this.parent);
                    break;
                }
                case 7: {
                    this.schematicPlacement.moveSubRegionTo(this.subRegionName, posOld.method_10069(0, 0, amount), (IStringConsumer)this.parent);
                    break;
                }
                case 1: {
                    this.schematicPlacement.toggleSubRegionEnabled(this.subRegionName, (IMessageConsumer)this.parent);
                    break;
                }
                case 2: {
                    this.schematicPlacement.toggleSubRegionRenderingEnabled(this.subRegionName);
                    break;
                }
                case 3: {
                    this.schematicPlacement.toggleSubRegionIgnoreEntities(this.subRegionName, (IMessageConsumer)this.parent);
                    break;
                }
                case 10: {
                    this.schematicPlacement.resetSubRegionToSchematicValues(this.subRegionName, (IMessageConsumer)this.parent);
                    break;
                }
            }
            this.parent.initGui();
        }

        public static enum Type {
            PLACEMENT_CONFIGURATION(""),
            TOGGLE_ENABLED("litematica.gui.button.schematic_placement.region_enabled"),
            TOGGLE_RENDERING("litematica.gui.button.schematic_placement.abbr.rendering"),
            TOGGLE_ENTITIES("litematica.gui.button.schematic_placement.ignore_entities"),
            MOVE_TO_PLAYER("litematica.gui.button.move_to_player"),
            NUDGE_COORD_X(""),
            NUDGE_COORD_Y(""),
            NUDGE_COORD_Z(""),
            ROTATE("litematica.gui.button.rotation_value"),
            MIRROR("litematica.gui.button.mirror_value"),
            RESET_PLACEMENT(""),
            SLICE_TYPE("litematica.gui.button.placement_sub.slice_type");

            private final String translationKey;
            @Nullable
            private final String hoverText;

            private Type(String translationKey) {
                this(translationKey, null);
            }

            private Type(String translationKey, String hoverText) {
                this.translationKey = translationKey;
                this.hoverText = hoverText;
            }

            public String getTranslationKey() {
                return this.translationKey;
            }

            public String getDisplayName(Object ... args) {
                return StringUtils.translate((String)this.translationKey, (Object[])args);
            }
        }
    }

    private static class TextFieldListener
    implements ITextFieldListener<GuiTextFieldInteger> {
        private final GuiSubRegionConfiguration parent;
        private final SchematicPlacement schematicPlacement;
        private final SubRegionPlacement placement;
        private final PositionUtils.CoordinateType type;

        public TextFieldListener(PositionUtils.CoordinateType type, SchematicPlacement schematicPlacement, SubRegionPlacement placement, GuiSubRegionConfiguration parent) {
            this.schematicPlacement = schematicPlacement;
            this.placement = placement;
            this.type = type;
            this.parent = parent;
        }

        public boolean onTextChange(GuiTextFieldInteger textField) {
            try {
                int value = Integer.parseInt(textField.getTextWrapper());
                class_2338 posOld = this.placement.getPos();
                posOld = PositionUtils.getTransformedBlockPos(posOld, this.schematicPlacement.getMirror(), this.schematicPlacement.getRotation());
                class_2338 pos = posOld = posOld.method_10081((class_2382)this.schematicPlacement.getOrigin());
                switch (this.type) {
                    case X: {
                        pos = new class_2338(value, posOld.method_10264(), posOld.method_10260());
                        break;
                    }
                    case Y: {
                        pos = new class_2338(posOld.method_10263(), value, posOld.method_10260());
                        break;
                    }
                    case Z: {
                        pos = new class_2338(posOld.method_10263(), posOld.method_10264(), value);
                    }
                }
                this.parent.setNextMessageType(Message.MessageType.ERROR);
                this.schematicPlacement.moveSubRegionTo(this.placement.getName(), pos, (IStringConsumer)this.parent);
                this.parent.updateElements();
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            return false;
        }
    }

    private static class CoordinateLockListener
    implements ISelectionListener<WidgetCheckBox> {
        private final SubRegionPlacement placement;
        private final PositionUtils.CoordinateType type;

        private CoordinateLockListener(PositionUtils.CoordinateType type, SubRegionPlacement placement) {
            this.type = type;
            this.placement = placement;
        }

        public void onSelectionChange(WidgetCheckBox entry) {
            this.placement.setCoordinateLocked(this.type, entry.isChecked());
        }
    }
}

