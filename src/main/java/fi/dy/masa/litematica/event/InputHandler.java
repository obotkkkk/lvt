/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.hotkeys.IHotkey
 *  fi.dy.masa.malilib.hotkeys.IKeybindManager
 *  fi.dy.masa.malilib.hotkeys.IKeybindProvider
 *  fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler
 *  fi.dy.masa.malilib.hotkeys.IMouseInputHandler
 *  fi.dy.masa.malilib.hotkeys.KeybindMulti
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 */
package fi.dy.masa.litematica.event;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.SchematicUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.hotkeys.IKeyboardInputHandler;
import fi.dy.masa.malilib.hotkeys.IMouseInputHandler;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_304;
import net.minecraft.class_310;

public class InputHandler
implements IKeybindProvider,
IKeyboardInputHandler,
IMouseInputHandler {
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler() {
    }

    public static InputHandler getInstance() {
        return INSTANCE;
    }

    public void addKeysToMap(IKeybindManager manager) {
        for (IHotkey iHotkey : Hotkeys.HOTKEY_LIST) {
            manager.addKeybindToMap(iHotkey.getKeybind());
        }
        for (IHotkey iHotkey : Configs.Generic.HOTKEY_LIST) {
            manager.addKeybindToMap(iHotkey.getKeybind());
        }
    }

    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory("Litematica", "litematica.hotkeys.category.generic_hotkeys", Hotkeys.HOTKEY_LIST);
        manager.addHotkeysForCategory("Litematica", "litematica.hotkeys.category.config_generic_hotkeys", Configs.Generic.HOTKEY_LIST);
    }

    public boolean onKeyInput(int keyCode, int scanCode, int modifiers, boolean eventKeyState) {
        if (eventKeyState) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1690.field_1904.method_1417(keyCode, scanCode)) {
                return this.handleUseKey(mc);
            }
            if (mc.field_1690.field_1886.method_1417(keyCode, scanCode)) {
                return this.handleAttackKey(mc);
            }
            if (mc.field_1690.field_1835.method_1417(keyCode, scanCode) && GuiSchematicManager.hasPendingPreviewTask()) {
                return GuiSchematicManager.setPreviewImage();
            }
        }
        return false;
    }

    public boolean onMouseClick(int mouseX, int mouseY, int eventButton, boolean eventButtonState) {
        class_310 mc = class_310.method_1551();
        if (GuiUtils.getCurrentScreen() == null && mc.field_1687 != null && mc.field_1724 != null && eventButtonState) {
            if (mc.field_1690.field_1904.method_1433(eventButton)) {
                return this.handleUseKey(mc);
            }
            if (mc.field_1690.field_1886.method_1433(eventButton)) {
                return this.handleAttackKey(mc);
            }
        }
        return false;
    }

    public boolean onMouseScroll(int mouseX, int mouseY, double dWheel) {
        class_310 mc = class_310.method_1551();
        if (GuiUtils.getCurrentScreen() == null && mc.field_1687 != null && mc.field_1724 != null) {
            return this.handleMouseScroll(dWheel, mc);
        }
        return false;
    }

    private boolean handleMouseScroll(double dWheel, class_310 mc) {
        boolean toolEnabled;
        boolean bl = toolEnabled = Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && Configs.Generic.TOOL_ITEM_ENABLED.getBooleanValue();
        if (!toolEnabled || !fi.dy.masa.litematica.util.EntityUtils.hasToolItem((class_1309)mc.field_1724)) {
            return false;
        }
        int amount = dWheel > 0.0 ? 1 : -1;
        ToolMode mode = DataManager.getToolMode();
        class_1297 entity = EntityUtils.getCameraEntity();
        if (Hotkeys.SELECTION_GRAB_MODIFIER.getKeybind().isKeybindHeld() && mode.getUsesAreaSelection()) {
            SelectionManager sm = DataManager.getSelectionManager();
            if (sm.hasGrabbedElement()) {
                sm.changeGrabDistance(entity, amount);
                return true;
            }
            if (sm.hasSelectedOrigin()) {
                AreaSelection area = sm.getCurrentSelection();
                class_2338 old = area.getEffectiveOrigin();
                area.moveEntireSelectionTo(old.method_10079(fi.dy.masa.litematica.util.EntityUtils.getClosestLookingDirection(entity), amount), false);
                return true;
            }
            if (mode == ToolMode.MOVE) {
                SchematicUtils.moveCurrentlySelectedWorldRegionToLookingDirection(amount, entity, mc);
                return true;
            }
        }
        if (Hotkeys.SELECTION_GROW_MODIFIER.getKeybind().isKeybindHeld()) {
            return this.growOrShrinkSelection(amount, mode);
        }
        if (Hotkeys.SELECTION_NUDGE_MODIFIER.getKeybind().isKeybindHeld()) {
            return InputHandler.nudgeSelection(amount, mode, entity);
        }
        if (Hotkeys.OPERATION_MODE_CHANGE_MODIFIER.getKeybind().isKeybindHeld()) {
            DataManager.setToolMode(DataManager.getToolMode().cycle((class_1657)mc.field_1724, amount < 0));
            return true;
        }
        if (Hotkeys.SCHEMATIC_VERSION_CYCLE_MODIFIER.getKeybind().isKeybindHeld()) {
            if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                DataManager.getSchematicProjectsManager().cycleVersion(amount * -1);
            }
            return true;
        }
        return false;
    }

    public static boolean nudgeSelection(int amount, ToolMode mode, class_1297 entity) {
        if (mode.getUsesAreaSelection()) {
            SelectionManager sm = DataManager.getSelectionManager();
            if (sm.hasSelectedElement()) {
                sm.moveSelectedElement(fi.dy.masa.litematica.util.EntityUtils.getClosestLookingDirection(entity), amount);
                return true;
            }
        } else if (mode.getUsesSchematic()) {
            class_2350 direction = fi.dy.masa.litematica.util.EntityUtils.getClosestLookingDirection(entity);
            DataManager.getSchematicPlacementManager().nudgePositionOfCurrentSelection(direction, amount);
            return true;
        }
        return false;
    }

    private boolean growOrShrinkSelection(int amount, ToolMode mode) {
        if (mode.getUsesAreaSelection()) {
            SelectionManager sm = DataManager.getSelectionManager();
            AreaSelection area = sm.getCurrentSelection();
            if (area != null) {
                Box box = area.getSelectedSubRegionBox();
                if (box != null) {
                    Box newBox = PositionUtils.growOrShrinkBox(box, amount);
                    area.setSelectedSubRegionCornerPos(newBox.getPos1(), PositionUtils.Corner.CORNER_1);
                    area.setSelectedSubRegionCornerPos(newBox.getPos2(), PositionUtils.Corner.CORNER_2);
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.area_selection.grow.no_sub_region_selected", (Object[])new Object[0]);
                }
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_area_selected", (Object[])new Object[0]);
            }
        }
        return true;
    }

    private boolean handleAttackKey(class_310 mc) {
        if (mc.field_1724 != null && DataManager.getToolMode() == ToolMode.REBUILD && KeybindMulti.getTriggeredCount() == 0) {
            if (Hotkeys.SCHEMATIC_EDIT_BREAK_DIRECTION.getKeybind().isKeybindHeld()) {
                return SchematicUtils.breakSchematicBlocks(mc);
            }
            if (Hotkeys.SCHEMATIC_EDIT_BREAK_ALL_EXCEPT.getKeybind().isKeybindHeld()) {
                return SchematicUtils.breakAllSchematicBlocksExceptTargeted(mc);
            }
            if (Hotkeys.SCHEMATIC_EDIT_BREAK_ALL.getKeybind().isKeybindHeld()) {
                return SchematicUtils.breakAllIdenticalSchematicBlocks(mc);
            }
            return SchematicUtils.breakSchematicBlock(mc);
        }
        return false;
    }

    private boolean handleUseKey(class_310 mc) {
        if (mc.field_1724 != null) {
            if (DataManager.getToolMode() == ToolMode.REBUILD) {
                if (Hotkeys.SCHEMATIC_EDIT_REPLACE_DIRECTION.getKeybind().isKeybindHeld()) {
                    return SchematicUtils.replaceSchematicBlocksInDirection(mc);
                }
                if (Hotkeys.SCHEMATIC_EDIT_REPLACE_ALL.getKeybind().isKeybindHeld()) {
                    return SchematicUtils.replaceAllIdenticalSchematicBlocks(mc);
                }
                if (Hotkeys.SCHEMATIC_EDIT_REPLACE_BLOCK.getKeybind().isKeybindHeld()) {
                    return SchematicUtils.replaceBlocksKeepingProperties(mc);
                }
                if (Hotkeys.SCHEMATIC_EDIT_BREAK_DIRECTION.getKeybind().isKeybindHeld()) {
                    return SchematicUtils.placeSchematicBlocksInDirection(mc);
                }
                if (Hotkeys.SCHEMATIC_EDIT_BREAK_ALL.getKeybind().isKeybindHeld()) {
                    return SchematicUtils.fillAirWithBlocks(mc);
                }
                return SchematicUtils.placeSchematicBlock(mc);
            }
            if (Configs.Generic.PICK_BLOCK_ENABLED.getBooleanValue() && KeybindMulti.hotkeyMatchesKeybind((IHotkey)Hotkeys.PICK_BLOCK_LAST, (class_304)mc.field_1690.field_1904)) {
                WorldUtils.doSchematicWorldPickBlock(false, mc);
            }
            if (Configs.Generic.PLACEMENT_RESTRICTION.getBooleanValue()) {
                return WorldUtils.handlePlacementRestriction(mc);
            }
        }
        return false;
    }
}

