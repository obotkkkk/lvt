/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigBoolean
 *  fi.dy.masa.malilib.config.options.ConfigString
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.hotkeys.IHotkey
 *  fi.dy.masa.malilib.hotkeys.IHotkeyCallback
 *  fi.dy.masa.malilib.hotkeys.IKeybind
 *  fi.dy.masa.malilib.hotkeys.KeyAction
 *  fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage
 *  fi.dy.masa.malilib.hotkeys.KeybindMulti
 *  fi.dy.masa.malilib.interfaces.IValueChangeCallback
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.LayerMode
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.event;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.event.InputHandler;
import fi.dy.masa.litematica.gui.GuiAreaSelectionManager;
import fi.dy.masa.litematica.gui.GuiConfigs;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.GuiMaterialList;
import fi.dy.masa.litematica.gui.GuiPlacementConfiguration;
import fi.dy.masa.litematica.gui.GuiRenderLayer;
import fi.dy.masa.litematica.gui.GuiSchematicLoadedList;
import fi.dy.masa.litematica.gui.GuiSchematicPlacementsList;
import fi.dy.masa.litematica.gui.GuiSchematicVerifier;
import fi.dy.masa.litematica.gui.GuiSubRegionConfiguration;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.CornerSelectionMode;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.litematica.tool.ToolModeData;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.InventoryUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.util.SchematicUtils;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import fi.dy.masa.litematica.util.ToolUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeyCallbackToggleBooleanConfigWithMessage;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.LayerMode;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_437;

public class KeyCallbacks {
    public static void init(class_310 mc) {
        KeyCallbackHotkeys callbackHotkeys = new KeyCallbackHotkeys(mc);
        KeyCallbackToggleMessage callbackMessage = new KeyCallbackToggleMessage(mc);
        ValueChangeCallback valueChangeCallback = new ValueChangeCallback();
        Configs.Generic.PICK_BLOCKABLE_SLOTS.setValueChangeCallback((IValueChangeCallback)valueChangeCallback);
        Hotkeys.CLONE_SELECTION.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.EASY_PLACE_ACTIVATION.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.EXECUTE_OPERATION.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.LAYER_MODE_NEXT.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.LAYER_MODE_PREVIOUS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.LAYER_NEXT.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.LAYER_PREVIOUS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.LAYER_SET_HERE.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.NUDGE_SELECTION_NEGATIVE.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.NUDGE_SELECTION_POSITIVE.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_AREA_SETTINGS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_LOADED_SCHEMATICS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_MAIN_MENU.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_MATERIAL_LIST.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_PLACEMENT_SETTINGS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_SCHEMATIC_PLACEMENTS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_SCHEMATIC_PROJECTS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_SCHEMATIC_VERIFIER.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_SELECTION_MANAGER.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.OPEN_GUI_SETTINGS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.PICK_BLOCK_FIRST.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.PICK_BLOCK_LAST.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.PICK_BLOCK_TOGGLE.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.PICK_BLOCK_ENABLED));
        Hotkeys.RERENDER_SCHEMATIC.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SAVE_AREA_AS_IN_MEMORY_SCHEMATIC.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SAVE_AREA_AS_SCHEMATIC_TO_FILE.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SCHEMATIC_VCS_DELETE_BY_PLACEMENT.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SCHEMATIC_VERSION_CYCLE_NEXT.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SCHEMATIC_VERSION_CYCLE_PREVIOUS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SELECTION_GROW_HOTKEY.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.SELECTION_SHRINK_HOTKEY.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.TOOL_PLACE_CORNER_1.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.TOOL_PLACE_CORNER_2.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.TOOL_SELECT_ELEMENTS.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.TOOL_SELECT_MODIFIER_BLOCK_1.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.TOOL_SELECT_MODIFIER_BLOCK_2.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.UNLOAD_CURRENT_SCHEMATIC.getKeybind().setCallback((IHotkeyCallback)callbackHotkeys);
        Hotkeys.ADD_SELECTION_BOX.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.DELETE_SELECTION_BOX.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.EASY_PLACE_FIRST.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.EASY_PLACE_FIRST));
        Hotkeys.EASY_PLACE_TOGGLE.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.EASY_PLACE_MODE));
        Hotkeys.MOVE_ENTIRE_SELECTION.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SELECTION_MODE_CYCLE.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SET_AREA_ORIGIN.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SCHEMATIC_PLACEMENT_ROTATION.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SCHEMATIC_PLACEMENT_MIRROR.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SET_SELECTION_BOX_POSITION_1.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.SET_SELECTION_BOX_POSITION_2.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
        Hotkeys.TOGGLE_ALL_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.ENABLE_RENDERING));
        Hotkeys.TOGGLE_AREA_SELECTION_RENDERING.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Visuals.ENABLE_AREA_SELECTION_RENDERING));
        Hotkeys.TOGGLE_SCHEMATIC_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.ENABLE_SCHEMATIC_RENDERING));
        Hotkeys.TOGGLE_INFO_OVERLAY_RENDERING.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.InfoOverlays.BLOCK_INFO_OVERLAY_ENABLED));
        Hotkeys.TOGGLE_OVERLAY_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.ENABLE_SCHEMATIC_OVERLAY));
        Hotkeys.TOGGLE_OVERLAY_OUTLINE_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.SCHEMATIC_OVERLAY_ENABLE_OUTLINES));
        Hotkeys.TOGGLE_OVERLAY_SIDE_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.SCHEMATIC_OVERLAY_ENABLE_SIDES));
        Hotkeys.TOGGLE_PLACEMENT_RESTRICTION.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.PLACEMENT_RESTRICTION));
        Hotkeys.TOGGLE_PLACEMENT_BOXES_RENDERING.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Visuals.ENABLE_PLACEMENT_BOXES_RENDERING));
        Hotkeys.TOGGLE_SCHEMATIC_BLOCK_RENDERING.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Visuals.ENABLE_SCHEMATIC_BLOCKS));
        Hotkeys.TOGGLE_SIGN_TEXT_PASTE.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.SIGN_TEXT_PASTE));
        Hotkeys.TOGGLE_TRANSLUCENT_RENDERING.getKeybind().setCallback((IHotkeyCallback)new RenderToggle((IConfigBoolean)Configs.Visuals.RENDER_BLOCKS_AS_TRANSLUCENT));
        Hotkeys.TOGGLE_VERIFIER_OVERLAY_RENDERING.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.InfoOverlays.VERIFIER_OVERLAY_ENABLED));
        Hotkeys.TOOL_ENABLED_TOGGLE.getKeybind().setCallback((IHotkeyCallback)new KeyCallbackToggleBooleanConfigWithMessage((IConfigBoolean)Configs.Generic.TOOL_ITEM_ENABLED));
        Hotkeys.SCHEMATIC_EDIT_REPLACE_SELECTION.getKeybind().setCallback((IHotkeyCallback)callbackMessage);
    }

    private static class KeyCallbackHotkeys
    implements IHotkeyCallback {
        private final class_310 mc;

        public KeyCallbackHotkeys(class_310 mc) {
            this.mc = mc;
        }

        public boolean onKeyAction(KeyAction action, IKeybind key) {
            boolean isToolSelect;
            if (this.mc.field_1724 == null || this.mc.field_1687 == null) {
                return false;
            }
            ToolMode mode = DataManager.getToolMode();
            boolean toolEnabled = Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && Configs.Generic.TOOL_ITEM_ENABLED.getBooleanValue();
            boolean hasTool = EntityUtils.hasToolItem((class_1309)this.mc.field_1724);
            boolean isToolPrimary = key == Hotkeys.TOOL_PLACE_CORNER_1.getKeybind();
            boolean isToolSecondary = key == Hotkeys.TOOL_PLACE_CORNER_2.getKeybind();
            boolean bl = isToolSelect = key == Hotkeys.TOOL_SELECT_ELEMENTS.getKeybind();
            if (toolEnabled && isToolSelect) {
                if (mode.getUsesBlockPrimary() && Hotkeys.TOOL_SELECT_MODIFIER_BLOCK_1.getKeybind().isKeybindHeld()) {
                    WorldUtils.setToolModeBlockState(mode, true, this.mc);
                    return true;
                }
                if (mode.getUsesBlockSecondary() && Hotkeys.TOOL_SELECT_MODIFIER_BLOCK_2.getKeybind().isKeybindHeld()) {
                    WorldUtils.setToolModeBlockState(mode, false, this.mc);
                    return true;
                }
            }
            if (toolEnabled && hasTool) {
                int maxDistance = 200;
                boolean projectMode = DataManager.getSchematicProjectsManager().hasProjectOpen();
                if (isToolPrimary || isToolSecondary) {
                    if (mode.getUsesAreaSelection() || projectMode) {
                        boolean grabModifier;
                        SelectionManager sm = DataManager.getSelectionManager();
                        boolean moveEverything = grabModifier = Hotkeys.SELECTION_GRAB_MODIFIER.getKeybind().isKeybindHeld();
                        if (grabModifier && mode == ToolMode.MOVE) {
                            class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
                            class_2338 pos = RayTraceUtils.getTargetedPosition((class_1937)this.mc.field_1687, entity, maxDistance, false);
                            if (pos != null) {
                                SchematicUtils.moveCurrentlySelectedWorldRegionTo(pos, this.mc);
                            }
                        } else if (Configs.Generic.SELECTION_CORNERS_MODE.getOptionListValue() == CornerSelectionMode.CORNERS) {
                            PositionUtils.Corner corner = isToolPrimary ? PositionUtils.Corner.CORNER_1 : PositionUtils.Corner.CORNER_2;
                            sm.setPositionOfCurrentSelectionToRayTrace(this.mc, corner, moveEverything, maxDistance);
                        } else if (Configs.Generic.SELECTION_CORNERS_MODE.getOptionListValue() == CornerSelectionMode.EXPAND) {
                            sm.handleCuboidModeMouseClick(this.mc, maxDistance, isToolSecondary, moveEverything);
                        }
                    } else if (mode.getUsesSchematic()) {
                        DataManager.getSchematicPlacementManager().setPositionOfCurrentSelectionToRayTrace(this.mc, maxDistance);
                    }
                    return true;
                }
                if (isToolSelect) {
                    if (mode.getUsesAreaSelection() || projectMode) {
                        SelectionManager sm = DataManager.getSelectionManager();
                        if (Hotkeys.SELECTION_GRAB_MODIFIER.getKeybind().isKeybindHeld()) {
                            if (sm.hasGrabbedElement()) {
                                sm.releaseGrabbedElement();
                            } else {
                                sm.grabElement(this.mc, maxDistance);
                            }
                        } else {
                            class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
                            sm.changeSelection((class_1937)this.mc.field_1687, entity, maxDistance);
                        }
                    } else if (mode.getUsesSchematic()) {
                        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
                        DataManager.getSchematicPlacementManager().changeSelection((class_1937)this.mc.field_1687, entity, maxDistance);
                    }
                    return true;
                }
            }
            if (key == Hotkeys.EASY_PLACE_ACTIVATION.getKeybind()) {
                return WorldUtils.handleEasyPlace(this.mc);
            }
            if (key == Hotkeys.OPEN_GUI_MAIN_MENU.getKeybind()) {
                GuiBase.openGui((class_437)new GuiMainMenu());
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_LOADED_SCHEMATICS.getKeybind()) {
                GuiBase.openGui((class_437)new GuiSchematicLoadedList());
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_SELECTION_MANAGER.getKeybind()) {
                if (!DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                    GuiBase.openGui((class_437)new GuiAreaSelectionManager());
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.gui.button.hover.schematic_projects.area_browser_disabled_currently_in_projects_mode", (Object[])new Object[0]);
                }
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_SCHEMATIC_PLACEMENTS.getKeybind()) {
                GuiBase.openGui((class_437)new GuiSchematicPlacementsList());
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_SCHEMATIC_PROJECTS.getKeybind()) {
                DataManager.getSchematicProjectsManager().openSchematicProjectsGui();
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_SETTINGS.getKeybind()) {
                if (DataManager.getConfigGuiTab() == GuiConfigs.ConfigGuiTab.RENDER_LAYERS) {
                    GuiBase.openGui((class_437)new GuiRenderLayer());
                } else {
                    GuiBase.openGui((class_437)new GuiConfigs());
                }
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_PLACEMENT_SETTINGS.getKeybind()) {
                SchematicPlacement schematicPlacement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
                if (schematicPlacement != null) {
                    SubRegionPlacement placement = schematicPlacement.getSelectedSubRegionPlacement();
                    if (placement != null) {
                        GuiBase.openGui((class_437)new GuiSubRegionConfiguration(schematicPlacement, placement));
                    } else {
                        GuiBase.openGui((class_437)new GuiPlacementConfiguration(schematicPlacement));
                    }
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_placement_selected", (Object[])new Object[0]);
                }
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_SCHEMATIC_VERIFIER.getKeybind()) {
                SchematicPlacement schematicPlacement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
                if (schematicPlacement != null) {
                    GuiBase.openGui((class_437)new GuiSchematicVerifier(schematicPlacement));
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_placement_selected", (Object[])new Object[0]);
                }
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_MATERIAL_LIST.getKeybind()) {
                MaterialListBase materialList = DataManager.getMaterialList();
                if (materialList == null) {
                    SchematicPlacement schematicPlacement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
                    if (schematicPlacement != null) {
                        materialList = schematicPlacement.getMaterialList();
                        materialList.reCreateMaterialList();
                    } else {
                        InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_placement_selected", (Object[])new Object[0]);
                    }
                }
                if (materialList != null) {
                    GuiBase.openGui((class_437)new GuiMaterialList(materialList));
                }
                return true;
            }
            if (key == Hotkeys.OPEN_GUI_AREA_SETTINGS.getKeybind()) {
                SelectionManager manager = DataManager.getSelectionManager();
                if (manager.getCurrentSelection() != null) {
                    manager.openEditGui(null);
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_area_selected", (Object[])new Object[0]);
                }
                return true;
            }
            if (key == Hotkeys.RERENDER_SCHEMATIC.getKeybind()) {
                SchematicWorldRefresher.INSTANCE.updateAll();
                InfoUtils.printActionbarMessage((String)"litematica.message.schematic_rendering_refreshed", (Object[])new Object[0]);
                return true;
            }
            if (key == Hotkeys.LAYER_NEXT.getKeybind()) {
                DataManager.getRenderLayerRange().moveLayer(1);
                return true;
            }
            if (key == Hotkeys.LAYER_PREVIOUS.getKeybind()) {
                DataManager.getRenderLayerRange().moveLayer(-1);
                return true;
            }
            if (key == Hotkeys.LAYER_SET_HERE.getKeybind()) {
                DataManager.getRenderLayerRange().setSingleBoundaryToPosition(fi.dy.masa.malilib.util.EntityUtils.getCameraEntity());
                return true;
            }
            if (key == Hotkeys.LAYER_MODE_NEXT.getKeybind()) {
                DataManager.getRenderLayerRange().setLayerMode((LayerMode)DataManager.getRenderLayerRange().getLayerMode().cycle(true));
                return true;
            }
            if (key == Hotkeys.LAYER_MODE_PREVIOUS.getKeybind()) {
                DataManager.getRenderLayerRange().setLayerMode((LayerMode)DataManager.getRenderLayerRange().getLayerMode().cycle(false));
                return true;
            }
            if (key == Hotkeys.PICK_BLOCK_FIRST.getKeybind()) {
                if (EntityUtils.shouldPickBlock((class_1657)this.mc.field_1724)) {
                    return WorldUtils.doSchematicWorldPickBlock(true, this.mc);
                }
                return false;
            }
            if (key == Hotkeys.PICK_BLOCK_LAST.getKeybind()) {
                if (EntityUtils.shouldPickBlock((class_1657)this.mc.field_1724) && !KeybindMulti.hotkeyMatchesKeybind((IHotkey)Hotkeys.PICK_BLOCK_LAST, (class_304)this.mc.field_1690.field_1904)) {
                    WorldUtils.doSchematicWorldPickBlock(false, this.mc);
                }
                return false;
            }
            if (key == Hotkeys.SAVE_AREA_AS_SCHEMATIC_TO_FILE.getKeybind()) {
                return SchematicUtils.saveSchematic(false);
            }
            if (key == Hotkeys.SAVE_AREA_AS_IN_MEMORY_SCHEMATIC.getKeybind()) {
                return SchematicUtils.saveSchematic(true);
            }
            if (key == Hotkeys.SCHEMATIC_VERSION_CYCLE_NEXT.getKeybind()) {
                if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                    DataManager.getSchematicProjectsManager().cycleVersion(1);
                }
                return true;
            }
            if (key == Hotkeys.SCHEMATIC_VERSION_CYCLE_PREVIOUS.getKeybind()) {
                if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                    DataManager.getSchematicProjectsManager().cycleVersion(-1);
                }
                return true;
            }
            if (key == Hotkeys.CLONE_SELECTION.getKeybind()) {
                SchematicUtils.cloneSelectionArea(this.mc);
                return true;
            }
            if (key == Hotkeys.EXECUTE_OPERATION.getKeybind() && (hasTool && toolEnabled || !Configs.Generic.EXECUTE_REQUIRE_TOOL.getBooleanValue())) {
                if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                    DataManager.getSchematicProjectsManager().pasteCurrentVersionToWorld();
                    return true;
                }
                if (mode == ToolMode.PASTE_SCHEMATIC) {
                    DataManager.getSchematicPlacementManager().pasteCurrentPlacementToWorld(this.mc);
                    return true;
                }
                if (mode == ToolMode.FILL && mode.getPrimaryBlock() != null) {
                    ToolUtils.fillSelectionVolumes(this.mc, mode.getPrimaryBlock(), null);
                    return true;
                }
                if (mode == ToolMode.REPLACE_BLOCK && mode.getPrimaryBlock() != null && mode.getSecondaryBlock() != null) {
                    ToolUtils.fillSelectionVolumes(this.mc, mode.getPrimaryBlock(), mode.getSecondaryBlock());
                    return true;
                }
                if (mode == ToolMode.DELETE) {
                    boolean removeEntities = true;
                    ToolUtils.deleteSelectionVolumes(removeEntities, this.mc);
                    return true;
                }
            } else if (key == Hotkeys.SCHEMATIC_VCS_DELETE_BY_PLACEMENT.getKeybind()) {
                if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
                    DataManager.getSchematicProjectsManager().deleteBlocksByPlacement();
                    return true;
                }
            } else {
                if (key == Hotkeys.NUDGE_SELECTION_NEGATIVE.getKeybind() || key == Hotkeys.NUDGE_SELECTION_POSITIVE.getKeybind()) {
                    int amount = key == Hotkeys.NUDGE_SELECTION_POSITIVE.getKeybind() ? 1 : -1;
                    InputHandler.nudgeSelection(amount, mode, (class_1297)this.mc.field_1724);
                    return true;
                }
                if (key == Hotkeys.SELECTION_GROW_HOTKEY.getKeybind()) {
                    if (mode.getUsesAreaSelection()) {
                        PositionUtils.growOrShrinkCurrentSelection(true);
                        return true;
                    }
                } else if (key == Hotkeys.SELECTION_SHRINK_HOTKEY.getKeybind()) {
                    if (mode.getUsesAreaSelection()) {
                        PositionUtils.growOrShrinkCurrentSelection(false);
                        return true;
                    }
                } else if (key == Hotkeys.UNLOAD_CURRENT_SCHEMATIC.getKeybind()) {
                    SchematicUtils.unloadCurrentlySelectedSchematic();
                    return true;
                }
            }
            return false;
        }
    }

    private static class KeyCallbackToggleMessage
    implements IHotkeyCallback {
        private final class_310 mc;

        public KeyCallbackToggleMessage(class_310 mc) {
            this.mc = mc;
        }

        public boolean onKeyAction(KeyAction action, IKeybind key) {
            ToolMode mode = DataManager.getToolMode();
            if (key == Hotkeys.ADD_SELECTION_BOX.getKeybind()) {
                if (mode.getUsesAreaSelection()) {
                    return DataManager.getSelectionManager().createNewSubRegion(this.mc, true);
                }
            } else if (key == Hotkeys.DELETE_SELECTION_BOX.getKeybind()) {
                SelectionManager sm;
                AreaSelection selection;
                if (mode.getUsesAreaSelection() && (selection = (sm = DataManager.getSelectionManager()).getCurrentSelection()) != null) {
                    if (selection.isOriginSelected()) {
                        selection.setExplicitOrigin(null);
                        selection.setOriginSelected(false);
                        InfoUtils.printActionbarMessage((String)"litematica.message.removed_area_origin", (Object[])new Object[0]);
                    } else {
                        String name = selection.getCurrentSubRegionBoxName();
                        if (name != null && selection.removeSelectedSubRegionBox()) {
                            InfoUtils.printActionbarMessage((String)"litematica.message.removed_selection_box", (Object[])new Object[]{name});
                            return true;
                        }
                    }
                }
            } else if (key == Hotkeys.MOVE_ENTIRE_SELECTION.getKeybind()) {
                if (mode.getUsesAreaSelection()) {
                    SelectionManager sm = DataManager.getSelectionManager();
                    AreaSelection selection = sm.getCurrentSelection();
                    if (selection != null) {
                        class_2338 pos = class_2338.method_49638((class_2374)this.mc.field_1724.method_19538());
                        if (mode == ToolMode.MOVE) {
                            SchematicUtils.moveCurrentlySelectedWorldRegionTo(pos, this.mc);
                        } else {
                            selection.moveEntireSelectionTo(pos, true);
                        }
                        return true;
                    }
                } else if (mode.getUsesSchematic()) {
                    class_2338 pos = class_2338.method_49638((class_2374)this.mc.field_1724.method_19538());
                    DataManager.getSchematicPlacementManager().setPositionOfCurrentSelectionTo(pos, this.mc);
                    return true;
                }
            } else if (key == Hotkeys.SCHEMATIC_PLACEMENT_ROTATION.getKeybind()) {
                SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
                if (placement != null) {
                    class_2470 rotation = PositionUtils.cycleRotation(placement.getRotation(), false);
                    if (placement.isLocked()) {
                        InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.placement.cant_modify_is_locked", (Object[])new Object[0]);
                    } else {
                        placement.setRotation(rotation, null);
                        InfoUtils.printActionbarMessage((String)"litematica.message.placement.rotation_set_to", (Object[])new Object[]{PositionUtils.getRotationNameShort(rotation)});
                    }
                    return true;
                }
            } else if (key == Hotkeys.SCHEMATIC_PLACEMENT_MIRROR.getKeybind()) {
                SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
                if (placement != null) {
                    class_2415 mirror = PositionUtils.cycleMirror(placement.getMirror(), false);
                    if (placement.isLocked()) {
                        InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.placement.cant_modify_is_locked", (Object[])new Object[0]);
                    } else {
                        placement.setMirror(mirror, null);
                        InfoUtils.printActionbarMessage((String)"litematica.message.placement.mirror_set_to", (Object[])new Object[]{PositionUtils.getMirrorName(mirror)});
                    }
                    return true;
                }
            } else {
                AreaSelection selection;
                if (key == Hotkeys.SELECTION_MODE_CYCLE.getKeybind()) {
                    if (mode == ToolMode.DELETE) {
                        ToolModeData.DELETE.toggleUsePlacement();
                    } else if (mode == ToolMode.PASTE_SCHEMATIC) {
                        Configs.Generic.PASTE_REPLACE_BEHAVIOR.setOptionListValue(Configs.Generic.PASTE_REPLACE_BEHAVIOR.getOptionListValue().cycle(false));
                    } else if (mode.getUsesAreaSelection()) {
                        Configs.Generic.SELECTION_CORNERS_MODE.setOptionListValue(Configs.Generic.SELECTION_CORNERS_MODE.getOptionListValue().cycle(false));
                    }
                    return true;
                }
                if (key == Hotkeys.SET_AREA_ORIGIN.getKeybind()) {
                    SelectionManager sm;
                    AreaSelection area;
                    if (mode.getUsesAreaSelection() && (area = (sm = DataManager.getSelectionManager()).getCurrentSelection()) != null) {
                        class_2338 pos = class_2338.method_49638((class_2374)this.mc.field_1724.method_19538());
                        area.setExplicitOrigin(pos);
                        String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                        InfoUtils.printActionbarMessage((String)"litematica.message.set_area_origin", (Object[])new Object[]{posStr});
                        return true;
                    }
                } else if (key == Hotkeys.SET_SELECTION_BOX_POSITION_1.getKeybind() || key == Hotkeys.SET_SELECTION_BOX_POSITION_2.getKeybind()) {
                    SelectionManager sm;
                    AreaSelection area;
                    if (mode.getUsesAreaSelection() && (area = (sm = DataManager.getSelectionManager()).getCurrentSelection()) != null && area.getSelectedSubRegionBox() != null) {
                        class_2338 pos = class_2338.method_49638((class_2374)this.mc.field_1724.method_19538());
                        PositionUtils.Corner corner = key == Hotkeys.SET_SELECTION_BOX_POSITION_1.getKeybind() ? PositionUtils.Corner.CORNER_1 : PositionUtils.Corner.CORNER_2;
                        area.setSelectedSubRegionCornerPos(pos, corner);
                        String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                        InfoUtils.printActionbarMessage((String)"litematica.message.set_selection_box_point", (Object[])new Object[]{corner.ordinal(), posStr});
                        return true;
                    }
                } else if (key == Hotkeys.SCHEMATIC_EDIT_REPLACE_SELECTION.getKeybind() && SchematicUtils.saveAreaSelectionToSchematic(selection = DataManager.getSelectionManager().getCurrentSelection(), (class_1937)this.mc.field_1687)) {
                    class_2338 pos = selection.getEffectiveOrigin();
                    String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                    InfoUtils.showInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.schematic_edit_replace_selection", (Object[])new Object[]{posStr});
                    return true;
                }
            }
            return false;
        }
    }

    private static class ValueChangeCallback
    implements IValueChangeCallback<ConfigString> {
        private ValueChangeCallback() {
        }

        public void onValueChanged(ConfigString config) {
            if (config == Configs.Generic.PICK_BLOCKABLE_SLOTS) {
                InventoryUtils.setPickBlockableSlots(Configs.Generic.PICK_BLOCKABLE_SLOTS.getStringValue());
            }
        }
    }

    private static class RenderToggle
    extends KeyCallbackToggleBooleanConfigWithMessage {
        public RenderToggle(IConfigBoolean config) {
            super(config);
        }

        public boolean onKeyAction(KeyAction action, IKeybind key) {
            super.onKeyAction(action, key);
            if (this.config.getBooleanValue()) {
                SchematicWorldRefresher.INSTANCE.updateAll();
            }
            return true;
        }
    }
}

