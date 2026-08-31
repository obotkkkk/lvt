/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiStringListSelection
 *  fi.dy.masa.malilib.gui.GuiTextInputFeedback
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator
 *  fi.dy.masa.malilib.gui.interfaces.IGuiIcon
 *  fi.dy.masa.malilib.gui.interfaces.ISelectionListener
 *  fi.dy.masa.malilib.gui.interfaces.IStringListConsumer
 *  fi.dy.masa.malilib.gui.widgets.WidgetBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetCheckBox
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntry
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.interfaces.IStringConsumerFeedback
 *  fi.dy.masa.malilib.util.FileRenamer
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.GuiMaterialList;
import fi.dy.masa.litematica.gui.GuiSchematicBrowserBase;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicBrowser;
import fi.dy.masa.litematica.materials.MaterialListSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiStringListSelection;
import fi.dy.masa.malilib.gui.GuiTextInputFeedback;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.interfaces.IStringListConsumer;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetCheckBox;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.interfaces.IStringConsumerFeedback;
import fi.dy.masa.malilib.util.FileRenamer;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_437;

public class GuiSchematicLoad
extends GuiSchematicBrowserBase {
    public GuiSchematicLoad() {
        super(12, 24);
        this.title = StringUtils.translate((String)"litematica.gui.title.load_schematic", (Object[])new Object[0]);
    }

    @Override
    public String getBrowserContext() {
        return "schematic_load";
    }

    @Override
    public File getDefaultDirectory() {
        return DataManager.getSchematicsBaseDirectory();
    }

    @Override
    public int getMaxInfoHeight() {
        return this.getBrowserHeight() + 10;
    }

    public void initGui() {
        super.initGui();
        int x = 12;
        int y = this.getScreenHeight() - 40;
        String label = StringUtils.translate((String)"litematica.gui.label.schematic_load.checkbox.create_placement", (Object[])new Object[0]);
        String hover = StringUtils.translate((String)"litematica.gui.label.schematic_load.hoverinfo.create_placement", (Object[])new Object[0]);
        WidgetCheckBox checkbox = new WidgetCheckBox(x, y, (IGuiIcon)Icons.CHECKBOX_UNSELECTED, (IGuiIcon)Icons.CHECKBOX_SELECTED, label, hover);
        checkbox.setListener((ISelectionListener)new CheckboxListener());
        checkbox.setChecked(DataManager.getCreatePlacementOnLoad(), false);
        this.addWidget((WidgetBase)checkbox);
        y = this.getScreenHeight() - 26;
        x += this.createButton(x, y, -1, ButtonListener.Type.LOAD_SCHEMATIC) + 4;
        x += this.createButton(x, y, -1, ButtonListener.Type.MATERIAL_LIST) + 4;
        x += this.createButton(x, y, -1, ButtonListener.Type.RENAME_SCHEMATIC) + 4;
        x += this.createButton(x, y, -1, ButtonListener.Type.RENAME_FILE) + 4;
        GuiMainMenu.ButtonListenerChangeMenu.ButtonType type = GuiMainMenu.ButtonListenerChangeMenu.ButtonType.LOADED_SCHEMATICS;
        label = StringUtils.translate((String)type.getLabelKey(), (Object[])new Object[0]);
        int buttonWidth = this.getStringWidth(label) + 30;
        ButtonGeneric button = new ButtonGeneric(x, y, buttonWidth, 20, label, (IGuiIcon)type.getIcon(), new String[0]);
        this.addButton((ButtonBase)button, new GuiMainMenu.ButtonListenerChangeMenu(type, this.getParent()));
        type = GuiMainMenu.ButtonListenerChangeMenu.ButtonType.MAIN_MENU;
        label = StringUtils.translate((String)type.getLabelKey(), (Object[])new Object[0]);
        buttonWidth = this.getStringWidth(label) + 20;
        x = this.getScreenWidth() - buttonWidth - 10;
        button = new ButtonGeneric(x, y, buttonWidth, 20, label, new String[0]);
        this.addButton((ButtonBase)button, new GuiMainMenu.ButtonListenerChangeMenu(type, this.getParent()));
    }

    private int createButton(int x, int y, int width, ButtonListener.Type type) {
        ButtonListener listener = new ButtonListener(type, this);
        String label = StringUtils.translate((String)type.getTranslationKey(), (Object[])new Object[0]);
        if (width == -1) {
            width = this.getStringWidth(label) + 10;
        }
        ButtonGeneric button = new ButtonGeneric(x, y, width, 20, label, new String[0]);
        if (type == ButtonListener.Type.MATERIAL_LIST) {
            button.setHoverStrings(new String[]{StringUtils.translate((String)"litematica.gui.button.hover.material_list_shift_to_select_sub_regions", (Object[])new Object[0])});
        }
        this.addButton((ButtonBase)button, listener);
        return width;
    }

    private static class CheckboxListener
    implements ISelectionListener<WidgetCheckBox> {
        private CheckboxListener() {
        }

        public void onSelectionChange(WidgetCheckBox entry) {
            DataManager.setCreatePlacementOnLoad(entry.isChecked());
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final Type type;
        private final GuiSchematicLoad gui;

        public ButtonListener(Type type, GuiSchematicLoad gui) {
            this.type = type;
            this.gui = gui;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.gui.getListWidget() == null) {
                return;
            }
            WidgetFileBrowserBase.DirectoryEntry entry = (WidgetFileBrowserBase.DirectoryEntry)((WidgetSchematicBrowser)this.gui.getListWidget()).getLastSelectedEntry();
            if (entry == null) {
                this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_load.no_schematic_selected", new Object[0]);
            } else {
                Path file = entry.getFullPath().toPath();
                if (!Files.exists(file, new LinkOption[0]) || !Files.isReadable(file)) {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_load.cant_read_file", new Object[]{file.getFileName()});
                    return;
                }
                this.gui.setNextMessageType(Message.MessageType.ERROR);
                LitematicaSchematic schematic = null;
                FileType fileType = FileType.fromFile(entry.getFullPath());
                boolean warnType = false;
                if (fileType == FileType.LITEMATICA_SCHEMATIC) {
                    schematic = LitematicaSchematic.createFromFile(entry.getDirectory(), entry.getName());
                } else if (fileType == FileType.SCHEMATICA_SCHEMATIC) {
                    schematic = WorldUtils.convertSchematicaSchematicToLitematicaSchematic(entry.getDirectory(), entry.getName(), false, (IStringConsumer)this.gui);
                    warnType = true;
                } else if (fileType == FileType.VANILLA_STRUCTURE) {
                    schematic = WorldUtils.convertStructureToLitematicaSchematic(entry.getDirectory(), entry.getName());
                    warnType = true;
                } else if (fileType == FileType.SPONGE_SCHEMATIC) {
                    schematic = WorldUtils.convertSpongeSchematicToLitematicaSchematic(entry.getDirectory(), entry.getName());
                    warnType = true;
                } else {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_load.unsupported_type", new Object[]{file.getFileName()});
                }
                if (schematic != null) {
                    if (this.type == Type.LOAD_SCHEMATIC) {
                        SchematicHolder.getInstance().addSchematic(schematic, true);
                        this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.info.schematic_load.schematic_loaded", new Object[]{file.getFileName()});
                        if (DataManager.getCreatePlacementOnLoad() && this.gui.mc.field_1724 != null) {
                            class_2338 pos = class_2338.method_49638((class_2374)this.gui.mc.field_1724.method_19538());
                            String name = schematic.getMetadata().getName();
                            boolean enabled = !GuiBase.isShiftDown();
                            SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
                            SchematicPlacement placement = SchematicPlacement.createFor(schematic, pos, name, enabled, enabled);
                            manager.addSchematicPlacement(placement, true);
                            manager.setSelectedSchematicPlacement(placement);
                        }
                    } else if (this.type == Type.MATERIAL_LIST) {
                        if (GuiBase.isShiftDown()) {
                            MaterialListCreator creator = new MaterialListCreator(schematic);
                            GuiStringListSelection gui = new GuiStringListSelection(schematic.getAreas().keySet(), (IStringListConsumer)creator);
                            gui.setTitle(StringUtils.translate((String)"litematica.gui.title.material_list.select_schematic_regions", (Object[])new Object[]{schematic.getMetadata().getName()}));
                            gui.setParent(GuiUtils.getCurrentScreen());
                            GuiBase.openGui((class_437)gui);
                        } else {
                            MaterialListSchematic materialList = new MaterialListSchematic(schematic, true);
                            DataManager.setMaterialList(materialList);
                            GuiBase.openGui((class_437)new GuiMaterialList(materialList));
                        }
                    } else if (this.type == Type.RENAME_SCHEMATIC) {
                        String oldName = schematic.getMetadata().getName();
                        GuiBase.openGui((class_437)new GuiTextInputFeedback(256, "litematica.gui.title.rename_schematic", oldName, (class_437)this.gui, (IStringConsumerFeedback)new SchematicRenamer(entry.getDirectory().toPath(), entry.getName(), this.gui)));
                    } else if (this.type == Type.RENAME_FILE) {
                        FileRenamer renamer = new FileRenamer(file, (IDirectoryNavigator)this.gui.getListWidget(), Configs.Generic.DISPLAY_FILE_OPS_FEEDBACK.getBooleanValue());
                        GuiBase.openGui((class_437)new GuiTextInputFeedback(256, "litematica.gui.title.rename_file", entry.getName(), (class_437)this.gui, (IStringConsumerFeedback)renamer));
                    }
                    if (warnType) {
                        InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (int)15000, (String)"litematica.message.warn.schematic_load_non_litematica", (Object[])new Object[0]);
                    }
                }
            }
        }

        public static enum Type {
            LOAD_SCHEMATIC("litematica.gui.button.load_schematic_to_memory"),
            MATERIAL_LIST("litematica.gui.button.material_list"),
            RENAME_SCHEMATIC("litematica.gui.button.rename_schematic"),
            RENAME_FILE("litematica.gui.button.rename_file");

            private final String translationKey;

            private Type(String translationKey) {
                this.translationKey = translationKey;
            }

            public String getTranslationKey() {
                return this.translationKey;
            }
        }

        private record SchematicRenamer(Path dir, String fileName, GuiSchematicLoad gui) implements IStringConsumerFeedback
        {
            public boolean setString(String string) {
                LitematicaSchematic schematic = LitematicaSchematic.createFromFile(this.dir.toFile(), this.fileName);
                if (schematic != null) {
                    schematic.getMetadata().setName(string);
                    schematic.getMetadata().setTimeModifiedToNow();
                    if (schematic.writeToFile(this.dir.toFile(), this.fileName, true)) {
                        if (this.gui.getListWidget() != null) {
                            ((WidgetSchematicBrowser)this.gui.getListWidget()).clearSchematicMetadataCache();
                        }
                        return true;
                    }
                } else {
                    this.gui.setString(StringUtils.translate((String)"litematica.error.schematic_rename.read_failed", (Object[])new Object[0]));
                }
                return false;
            }
        }
    }

    private record MaterialListCreator(LitematicaSchematic schematic) implements IStringListConsumer
    {
        public boolean consume(Collection<String> strings) {
            MaterialListSchematic materialList = new MaterialListSchematic(this.schematic, strings, true);
            DataManager.setMaterialList(materialList);
            GuiBase.openGui((class_437)new GuiMaterialList(materialList));
            return true;
        }
    }
}

