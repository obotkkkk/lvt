/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigOptionList
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiConfirmAction
 *  fi.dy.masa.malilib.gui.GuiTextInputFeedback
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.ConfigButtonOptionList
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator
 *  fi.dy.masa.malilib.gui.interfaces.ISelectionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntry
 *  fi.dy.masa.malilib.interfaces.IConfirmationListener
 *  fi.dy.masa.malilib.interfaces.IStringConsumerFeedback
 *  fi.dy.masa.malilib.util.FileCopier
 *  fi.dy.masa.malilib.util.FileDeleter
 *  fi.dy.masa.malilib.util.FileRenamer
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1011
 *  net.minecraft.class_276
 *  net.minecraft.class_310
 *  net.minecraft.class_318
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiMainMenu;
import fi.dy.masa.litematica.gui.GuiSchematicBrowserBase;
import fi.dy.masa.litematica.gui.GuiSchematicSaveExported;
import fi.dy.masa.litematica.gui.GuiSchematicSaveImported;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicBrowser;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.malilib.config.IConfigOptionList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfirmAction;
import fi.dy.masa.malilib.gui.GuiTextInputFeedback;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonOptionList;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IDirectoryNavigator;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.interfaces.IConfirmationListener;
import fi.dy.masa.malilib.interfaces.IStringConsumerFeedback;
import fi.dy.masa.malilib.util.FileCopier;
import fi.dy.masa.malilib.util.FileDeleter;
import fi.dy.masa.malilib.util.FileRenamer;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.class_1011;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_318;
import net.minecraft.class_437;

public class GuiSchematicManager
extends GuiSchematicBrowserBase
implements ISelectionListener<WidgetFileBrowserBase.DirectoryEntry> {
    private static PreviewGenerator previewGenerator;
    private ExportType exportType = ExportType.V6_LITEMATIC;
    private EditType editType = EditType.RENAME_SCHEMATIC;
    private FileOpType fileOpType = FileOpType.RENAME_FILE;

    public GuiSchematicManager() {
        super(10, 24);
        this.title = StringUtils.translate((String)"litematica.gui.title.schematic_manager", (Object[])new Object[0]);
    }

    @Override
    public String getBrowserContext() {
        return "schematic_manager";
    }

    @Override
    public File getDefaultDirectory() {
        return DataManager.getSchematicsBaseDirectory();
    }

    @Override
    protected int getBrowserHeight() {
        return this.getScreenHeight() - 60;
    }

    public void initGui() {
        super.initGui();
        this.createButtons();
    }

    private void createButtons() {
        Enum type;
        int x = 10;
        int y = this.getScreenHeight() - 26;
        if (this.getListWidget() == null) {
            return;
        }
        WidgetFileBrowserBase.DirectoryEntry selected = (WidgetFileBrowserBase.DirectoryEntry)((WidgetSchematicBrowser)this.getListWidget()).getLastSelectedEntry();
        if (selected != null) {
            type = FileType.fromFile(selected.getFullPath());
            if (type == FileType.LITEMATICA_SCHEMATIC) {
                x = this.createButton(x, y, ButtonListener.Type.EDIT_SCHEMATIC);
                x = this.createButton(x, y, ButtonListener.Type.EDIT_TYPE);
                x = this.createButton(x, y, ButtonListener.Type.IMPORT_SCHEMATIC);
                x = this.createButton(x, y, ButtonListener.Type.EXPORT_SCHEMATIC);
                x = this.createButton(x, y, ButtonListener.Type.EXPORT_TYPE);
                x = this.createButton(x, y, ButtonListener.Type.FILE_OPS);
                x = this.createButton(x, y, ButtonListener.Type.FILE_OPS_TYPE);
            } else if (type == FileType.SPONGE_SCHEMATIC || type == FileType.SCHEMATICA_SCHEMATIC || type == FileType.VANILLA_STRUCTURE) {
                x = this.createButton(x, y, ButtonListener.Type.IMPORT_SCHEMATIC);
                x = this.createButton(x, y, ButtonListener.Type.FILE_OPS);
                x = this.createButton(x, y, ButtonListener.Type.FILE_OPS_TYPE);
            }
        }
        type = GuiMainMenu.ButtonListenerChangeMenu.ButtonType.MAIN_MENU;
        String label = StringUtils.translate((String)((GuiMainMenu.ButtonListenerChangeMenu.ButtonType)type).getLabelKey(), (Object[])new Object[0]);
        int buttonWidth = this.getStringWidth(label) + 20;
        this.addButton((ButtonBase)new ButtonGeneric(this.getScreenWidth() - buttonWidth - 10, y, buttonWidth, 20, label, new String[0]), new GuiMainMenu.ButtonListenerChangeMenu((GuiMainMenu.ButtonListenerChangeMenu.ButtonType)type, null));
    }

    public void onSelectionChange(@Nullable WidgetFileBrowserBase.DirectoryEntry entry) {
        this.clearButtons();
        this.createButtons();
    }

    private int createButton(int x, int y, ButtonListener.Type type) {
        ButtonGeneric button;
        String label = type.getLabel();
        String hover = type.getHoverText();
        int buttonWidth = this.getStringWidth(label) + 10;
        if (type == ButtonListener.Type.EDIT_TYPE) {
            buttonWidth = this.getStringWidth(this.editType.getDisplayName()) + 10;
            button = new ConfigButtonOptionList(x, y, buttonWidth, 20, (IConfigOptionList)new EditSchematicWrapper());
            if (this.editType.getHoverText() != null) {
                button.setHoverStrings(new String[]{this.editType.getHoverText()});
            }
        } else if (type == ButtonListener.Type.EXPORT_TYPE) {
            buttonWidth = this.getStringWidth(this.exportType.getDisplayName()) + 10;
            button = new ConfigButtonOptionList(x, y, buttonWidth, 20, (IConfigOptionList)new ExportTypeWrapper());
            if (this.exportType.getHoverText() != null) {
                button.setHoverStrings(new String[]{this.exportType.getHoverText()});
            }
        } else if (type == ButtonListener.Type.FILE_OPS_TYPE) {
            buttonWidth = this.getStringWidth(this.fileOpType.getDisplayName()) + 10;
            button = new ConfigButtonOptionList(x, y, buttonWidth, 20, (IConfigOptionList)new FileOpsWrapper());
            if (this.fileOpType.getHoverText() != null) {
                button.setHoverStrings(new String[]{this.fileOpType.getHoverText()});
            }
        } else {
            button = hover != null ? new ButtonGeneric(x, y, buttonWidth, 20, label, new String[]{hover}) : new ButtonGeneric(x, y, buttonWidth, 20, label, new String[0]);
        }
        this.addButton((ButtonBase)button, new ButtonListener(type, this));
        return x + buttonWidth + 4;
    }

    @Override
    protected ISelectionListener<WidgetFileBrowserBase.DirectoryEntry> getSelectionListener() {
        return this;
    }

    public static boolean setPreviewImage() {
        if (previewGenerator != null) {
            previewGenerator.createAndSetPreviewImage();
            previewGenerator = null;
            return true;
        }
        return false;
    }

    public static boolean hasPendingPreviewTask() {
        return previewGenerator != null;
    }

    public static enum ExportType implements IConfigOptionListEntry
    {
        SCHEMATIC("litematica.gui.label.schematic_manager.export_type.schematic", "litematica.gui.label.schematic_manager.export_type.schematic.hover"),
        V6_LITEMATIC("litematica.gui.label.schematic_manager.export_type.v6_litematic", "litematica.gui.label.schematic_manager.export_type.v6_litematic.hover"),
        VANILLA("litematica.gui.label.schematic_manager.export_type.vanilla", "litematica.gui.label.schematic_manager.export_type.vanilla.hover");

        private final String label;
        private final String hoverText;

        private ExportType(String label) {
            this(label, null);
        }

        private ExportType(String label, String hoverText) {
            this.label = label;
            this.hoverText = hoverText;
        }

        public String getStringValue() {
            return this.name().toLowerCase();
        }

        public String getDisplayName() {
            return StringUtils.translate((String)this.label, (Object[])new Object[0]);
        }

        @Nullable
        public String getHoverText() {
            return this.hoverText != null ? StringUtils.translate((String)this.hoverText, (Object[])new Object[0]) : null;
        }

        public IConfigOptionListEntry cycle(boolean forward) {
            int id = this.ordinal();
            if (forward) {
                if (++id >= ExportType.values().length) {
                    id = 0;
                }
            } else if (--id < 0) {
                id = ExportType.values().length - 1;
            }
            return ExportType.values()[id % ExportType.values().length];
        }

        public ExportType fromString(String name) {
            return ExportType.fromStringStatic(name);
        }

        public static ExportType fromStringStatic(String name) {
            for (ExportType al : ExportType.values()) {
                if (!al.name().equalsIgnoreCase(name)) continue;
                return al;
            }
            return V6_LITEMATIC;
        }
    }

    public static enum EditType implements IConfigOptionListEntry
    {
        RENAME_SCHEMATIC("litematica.gui.label.schematic_manager.edit_type.rename_schematic", "litematica.gui.label.schematic_manager.edit_type.rename_schematic.hover"),
        CHANGE_AUTHOR("litematica.gui.label.schematic_manager.edit_type.change_author", "litematica.gui.label.schematic_manager.edit_type.change_author.hover"),
        SET_PREVIEW("litematica.gui.label.schematic_manager.edit_type.set_preview", "litematica.gui.label.schematic_manager.edit_type.set_preview.hover");

        private final String label;
        private final String hoverText;

        private EditType(String label) {
            this(label, null);
        }

        private EditType(String label, String hoverText) {
            this.label = label;
            this.hoverText = hoverText;
        }

        public String getStringValue() {
            return this.name().toLowerCase();
        }

        public String getDisplayName() {
            return StringUtils.translate((String)this.label, (Object[])new Object[0]);
        }

        @Nullable
        public String getHoverText() {
            return this.hoverText != null ? StringUtils.translate((String)this.hoverText, (Object[])new Object[0]) : null;
        }

        public IConfigOptionListEntry cycle(boolean forward) {
            int id = this.ordinal();
            if (forward) {
                if (++id >= EditType.values().length) {
                    id = 0;
                }
            } else if (--id < 0) {
                id = EditType.values().length - 1;
            }
            return EditType.values()[id % EditType.values().length];
        }

        public EditType fromString(String name) {
            return EditType.fromStringStatic(name);
        }

        public static EditType fromStringStatic(String name) {
            for (EditType al : EditType.values()) {
                if (!al.name().equalsIgnoreCase(name)) continue;
                return al;
            }
            return RENAME_SCHEMATIC;
        }
    }

    public static enum FileOpType implements IConfigOptionListEntry
    {
        RENAME_FILE("litematica.gui.label.schematic_manager.file_op_type.rename", "litematica.gui.label.schematic_manager.file_op_type.rename.hover"),
        COPY_FILE("litematica.gui.label.schematic_manager.file_op_type.copy", "litematica.gui.label.schematic_manager.file_op_type.copy.hover"),
        DELETE_FILE("litematica.gui.label.schematic_manager.file_op_type.delete", "litematica.gui.label.schematic_manager.file_op_type.delete.hover");

        private final String label;
        private final String hoverText;

        private FileOpType(String label) {
            this(label, null);
        }

        private FileOpType(String label, String hoverText) {
            this.label = label;
            this.hoverText = hoverText;
        }

        public String getStringValue() {
            return this.name().toLowerCase();
        }

        public String getDisplayName() {
            return StringUtils.translate((String)this.label, (Object[])new Object[0]);
        }

        @Nullable
        public String getHoverText() {
            return this.hoverText != null ? StringUtils.translate((String)this.hoverText, (Object[])new Object[0]) : null;
        }

        public IConfigOptionListEntry cycle(boolean forward) {
            int id = this.ordinal();
            if (forward) {
                if (++id >= FileOpType.values().length) {
                    id = 0;
                }
            } else if (--id < 0) {
                id = FileOpType.values().length - 1;
            }
            return FileOpType.values()[id % FileOpType.values().length];
        }

        public FileOpType fromString(String name) {
            return FileOpType.fromStringStatic(name);
        }

        public static FileOpType fromStringStatic(String name) {
            for (FileOpType al : FileOpType.values()) {
                if (!al.name().equalsIgnoreCase(name)) continue;
                return al;
            }
            return RENAME_FILE;
        }
    }

    private record ButtonListener(Type type, GuiSchematicManager gui) implements IButtonActionListener
    {
        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == Type.EDIT_SCHEMATIC && this.gui.editType == EditType.SET_PREVIEW && mouseButton == 1) {
                if (previewGenerator != null) {
                    previewGenerator = null;
                    this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.message.schematic_preview_cancelled", new Object[0]);
                }
                return;
            }
            WidgetFileBrowserBase.DirectoryEntry entry = (WidgetFileBrowserBase.DirectoryEntry)((WidgetSchematicBrowser)this.gui.getListWidget()).getLastSelectedEntry();
            if (entry == null) {
                this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_load.no_schematic_selected", new Object[0]);
                return;
            }
            Path file = entry.getFullPath().toPath();
            if (!(Files.exists(file, new LinkOption[0]) && Files.isRegularFile(file, new LinkOption[0]) && Files.isReadable(file))) {
                this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_load.cant_read_file", new Object[]{file.getFileName()});
                return;
            }
            FileType fileType = FileType.fromFile(entry.getFullPath());
            if (this.type == Type.EDIT_SCHEMATIC) {
                if (fileType == FileType.LITEMATICA_SCHEMATIC) {
                    if (this.gui.editType == EditType.RENAME_SCHEMATIC) {
                        schematic = LitematicaSchematic.createFromFile(entry.getDirectory(), entry.getName());
                        String oldName = schematic != null ? schematic.getMetadata().getName() : "";
                        GuiBase.openGui((class_437)new GuiTextInputFeedback(256, "litematica.gui.title.rename_schematic", oldName, (class_437)this.gui, (IStringConsumerFeedback)new SchematicRenamer(entry.getDirectory().toPath(), entry.getName(), this.gui)));
                    } else if (this.gui.editType == EditType.CHANGE_AUTHOR) {
                        schematic = LitematicaSchematic.createFromFile(entry.getDirectory(), entry.getName());
                        String oldName = schematic != null ? schematic.getMetadata().getAuthor() : "";
                        GuiBase.openGui((class_437)new GuiTextInputFeedback(128, "litematica.gui.title.schematic_change_author", oldName, (class_437)this.gui, (IStringConsumerFeedback)new SchematicChangeAuthor(entry.getDirectory().toPath(), entry.getName(), this.gui)));
                    } else if (this.gui.editType == EditType.SET_PREVIEW) {
                        if (GuiBase.isShiftDown() && GuiBase.isCtrlDown() && GuiBase.isAltDown()) {
                            Path imageFile = entry.getDirectory().toPath().resolve("thumb.png");
                            if (Files.exists(imageFile, new LinkOption[0]) && Files.isReadable(imageFile)) {
                                LitematicaSchematic schematic = LitematicaSchematic.createFromFile(entry.getDirectory(), entry.getName());
                                if (schematic != null) {
                                    try {
                                        InputStream inputStream = Files.newInputStream(imageFile, new OpenOption[0]);
                                        class_1011 image = class_1011.method_4309((InputStream)inputStream);
                                        int x = image.method_4307() >= image.method_4323() ? (image.method_4307() - image.method_4323()) / 2 : 0;
                                        int y = image.method_4323() >= image.method_4307() ? (image.method_4323() - image.method_4307()) / 2 : 0;
                                        int longerSide = Math.min(image.method_4307(), image.method_4323());
                                        int previewDimensions = 120;
                                        class_1011 scaled = new class_1011(previewDimensions, previewDimensions, false);
                                        image.method_4300(x, y, longerSide, longerSide, scaled);
                                        int[] pixels = scaled.method_4322();
                                        schematic.getMetadata().setPreviewImagePixelData(pixels);
                                        schematic.getMetadata().setTimeModifiedToNow();
                                        if (schematic.writeToFile(entry.getDirectory(), entry.getName(), true)) {
                                            InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"Custom preview image set", (Object[])new Object[0]);
                                        }
                                        return;
                                    }
                                    catch (Exception exception) {}
                                }
                            } else {
                                InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Image 'thumb.png' not found", (Object[])new Object[0]);
                                return;
                            }
                            InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Failed to set custom preview image", (Object[])new Object[0]);
                            return;
                        }
                        previewGenerator = new PreviewGenerator(entry.getDirectory().toPath(), entry.getName());
                        GuiBase.openGui(null);
                        InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.info.schematic_manager.preview.set_preview_by_taking_a_screenshot", (Object[])new Object[0]);
                    }
                } else {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_manager.schematic_edit.unsupported_type", new Object[]{file.getFileName()});
                }
            }
            if (this.type == Type.EXPORT_SCHEMATIC) {
                if (fileType == FileType.LITEMATICA_SCHEMATIC) {
                    gui = new GuiSchematicSaveExported(entry.getType(), entry.getDirectory(), entry.getName(), this.gui.exportType);
                    gui.setParent((class_437)this.gui);
                    GuiBase.openGui((class_437)gui);
                } else {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_manager.schematic_export.unsupported_type", new Object[]{file.getFileName()});
                }
            } else if (this.type == Type.IMPORT_SCHEMATIC) {
                if (fileType == FileType.LITEMATICA_SCHEMATIC || fileType == FileType.SPONGE_SCHEMATIC || fileType == FileType.SCHEMATICA_SCHEMATIC || fileType == FileType.VANILLA_STRUCTURE) {
                    gui = new GuiSchematicSaveImported(entry.getType(), entry.getDirectory(), entry.getName());
                    gui.setParent((class_437)this.gui);
                    GuiBase.openGui((class_437)gui);
                } else {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_manager.schematic_import.unsupported_type", new Object[]{file.getFileName()});
                }
            } else if (this.type == Type.FILE_OPS) {
                if (this.gui.fileOpType == FileOpType.RENAME_FILE) {
                    FileRenamer renamer = new FileRenamer(file, (IDirectoryNavigator)this.gui.getListWidget(), Configs.Generic.DISPLAY_FILE_OPS_FEEDBACK.getBooleanValue());
                    GuiBase.openGui((class_437)new GuiTextInputFeedback(256, "litematica.gui.title.rename_file", entry.getName(), (class_437)this.gui, (IStringConsumerFeedback)renamer));
                } else if (this.gui.fileOpType == FileOpType.COPY_FILE) {
                    FileCopier copier = new FileCopier(file, (IDirectoryNavigator)this.gui.getListWidget(), Configs.Generic.DISPLAY_FILE_OPS_FEEDBACK.getBooleanValue());
                    GuiBase.openGui((class_437)new GuiTextInputFeedback(256, "litematica.gui.title.copy_file", entry.getName(), (class_437)this.gui, (IStringConsumerFeedback)copier));
                } else if (this.gui.fileOpType == FileOpType.DELETE_FILE) {
                    FileDeleter deleter = new FileDeleter(entry.getFullPath().toPath(), (IDirectoryNavigator)this.gui.getListWidget(), Configs.Generic.DISPLAY_FILE_OPS_FEEDBACK.getBooleanValue());
                    GuiBase.openGui((class_437)new GuiConfirmAction(400, "litematica.gui.title.confirm_file_deletion", (IConfirmationListener)deleter, (class_437)this.gui, "litematica.gui.message.confirm_file_deletion", new Object[]{entry.getName()}));
                }
            }
        }

        public static enum Type {
            EDIT_SCHEMATIC("litematica.gui.button.schematic_manager.edit_schematic", "litematica.gui.button.schematic_manager.edit_schematic.hover"),
            EDIT_TYPE(""),
            IMPORT_SCHEMATIC("litematica.gui.button.import", "litematica.gui.button.import.hover"),
            EXPORT_SCHEMATIC("litematica.gui.button.schematic_manager.export_as", "litematica.gui.button.schematic_manager.export_as.hover"),
            EXPORT_TYPE(""),
            FILE_OPS("litematica.gui.button.schematic_manager.file_ops", "litematica.gui.button.schematic_manager.file_ops.hover"),
            FILE_OPS_TYPE("");

            private final String label;
            @Nullable
            private final String hoverText;

            private Type(String label) {
                this(label, null);
            }

            private Type(String label, String hoverText) {
                this.label = label;
                this.hoverText = hoverText;
            }

            public String getLabel() {
                return StringUtils.translate((String)this.label, (Object[])new Object[0]);
            }

            @Nullable
            public String getHoverText() {
                return this.hoverText != null ? StringUtils.translate((String)this.hoverText, (Object[])new Object[0]) : null;
            }
        }
    }

    private class EditSchematicWrapper
    implements IConfigOptionList {
        private EditSchematicWrapper() {
        }

        public IConfigOptionListEntry getOptionListValue() {
            return GuiSchematicManager.this.editType;
        }

        public IConfigOptionListEntry getDefaultOptionListValue() {
            return EditType.RENAME_SCHEMATIC;
        }

        public void setOptionListValue(IConfigOptionListEntry value) {
            GuiSchematicManager.this.editType = (EditType)value;
            GuiSchematicManager.this.clearButtons();
            GuiSchematicManager.this.createButtons();
        }
    }

    private class ExportTypeWrapper
    implements IConfigOptionList {
        private ExportTypeWrapper() {
        }

        public IConfigOptionListEntry getOptionListValue() {
            return GuiSchematicManager.this.exportType;
        }

        public IConfigOptionListEntry getDefaultOptionListValue() {
            return ExportType.V6_LITEMATIC;
        }

        public void setOptionListValue(IConfigOptionListEntry value) {
            GuiSchematicManager.this.exportType = (ExportType)value;
            GuiSchematicManager.this.clearButtons();
            GuiSchematicManager.this.createButtons();
        }
    }

    private class FileOpsWrapper
    implements IConfigOptionList {
        private FileOpsWrapper() {
        }

        public IConfigOptionListEntry getOptionListValue() {
            return GuiSchematicManager.this.fileOpType;
        }

        public IConfigOptionListEntry getDefaultOptionListValue() {
            return FileOpType.RENAME_FILE;
        }

        public void setOptionListValue(IConfigOptionListEntry value) {
            GuiSchematicManager.this.fileOpType = (FileOpType)value;
            GuiSchematicManager.this.clearButtons();
            GuiSchematicManager.this.createButtons();
        }
    }

    public record PreviewGenerator(Path dir, String fileName) {
        public void createAndSetPreviewImage() {
            LitematicaSchematic schematic = LitematicaSchematic.createFromFile(this.dir.toFile(), this.fileName);
            if (schematic != null) {
                try {
                    class_310 mc = class_310.method_1551();
                    class_1011 screenshot = class_318.method_1663((class_276)mc.method_1522());
                    int x = screenshot.method_4307() >= screenshot.method_4323() ? (screenshot.method_4307() - screenshot.method_4323()) / 2 : 0;
                    int y = screenshot.method_4323() >= screenshot.method_4307() ? (screenshot.method_4323() - screenshot.method_4307()) / 2 : 0;
                    int longerSide = Math.min(screenshot.method_4307(), screenshot.method_4323());
                    int previewDimensions = 120;
                    class_1011 scaled = new class_1011(previewDimensions, previewDimensions, false);
                    screenshot.method_4300(x, y, longerSide, longerSide, scaled);
                    int[] pixels = scaled.method_4322();
                    schematic.getMetadata().setPreviewImagePixelData(pixels);
                    schematic.getMetadata().setTimeModifiedToNow();
                    schematic.writeToFile(this.dir.toFile(), this.fileName, true);
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.info.schematic_manager.preview.success", (Object[])new Object[0]);
                }
                catch (Exception e) {
                    Litematica.LOGGER.warn("Exception while creating preview image", (Throwable)e);
                }
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_rename.read_failed", (Object[])new Object[0]);
            }
        }
    }

    private record SchematicChangeAuthor(Path dir, String fileName, GuiSchematicManager gui) implements IStringConsumerFeedback
    {
        public boolean setString(String string) {
            LitematicaSchematic schematic = LitematicaSchematic.createFromFile(this.dir.toFile(), this.fileName);
            if (schematic != null) {
                schematic.getMetadata().setAuthor(string);
                schematic.getMetadata().setTimeModifiedToNow();
                if (schematic.writeToFile(this.dir.toFile(), this.fileName, true)) {
                    ((WidgetSchematicBrowser)this.gui.getListWidget()).clearSchematicMetadataCache();
                    return true;
                }
            } else {
                this.gui.setString(StringUtils.translate((String)"litematica.error.schematic_load.cant_read_file", (Object[])new Object[0]));
            }
            return false;
        }
    }

    private record SchematicRenamer(Path dir, String fileName, GuiSchematicManager gui) implements IStringConsumerFeedback
    {
        public boolean setString(String string) {
            LitematicaSchematic schematic = LitematicaSchematic.createFromFile(this.dir.toFile(), this.fileName);
            if (schematic != null) {
                schematic.getMetadata().setName(string);
                schematic.getMetadata().setTimeModifiedToNow();
                if (schematic.writeToFile(this.dir.toFile(), this.fileName, true)) {
                    ((WidgetSchematicBrowser)this.gui.getListWidget()).clearSchematicMetadataCache();
                    return true;
                }
            } else {
                this.gui.setString(StringUtils.translate((String)"litematica.error.schematic_load.cant_read_file", (Object[])new Object[0]));
            }
            return false;
        }
    }
}

