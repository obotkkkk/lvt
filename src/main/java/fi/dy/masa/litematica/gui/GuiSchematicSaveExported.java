/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntryType
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.StringUtils
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicManager;
import fi.dy.masa.litematica.gui.GuiSchematicSaveBase;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicBrowser;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;

public class GuiSchematicSaveExported
extends GuiSchematicSaveBase {
    private final GuiSchematicManager.ExportType exportType;
    private final WidgetFileBrowserBase.DirectoryEntryType type;
    private final File dirSource;
    private final String inputFileName;

    public GuiSchematicSaveExported(WidgetFileBrowserBase.DirectoryEntryType type, File dirSource, String inputFileName, GuiSchematicManager.ExportType exportType) {
        super(null);
        this.exportType = exportType;
        this.type = type;
        this.dirSource = dirSource;
        this.inputFileName = inputFileName;
        this.defaultText = FileNameUtils.getFileNameWithoutExtension((String)inputFileName);
        this.title = StringUtils.translate((String)"litematica.gui.title.save_exported_schematic", (Object[])new Object[]{exportType.getDisplayName(), inputFileName});
        this.useTitleHierarchy = false;
    }

    @Override
    public String getBrowserContext() {
        return "schematic_save_exported";
    }

    @Override
    public File getDefaultDirectory() {
        return DataManager.getSchematicsBaseDirectory();
    }

    @Override
    protected IButtonActionListener createButtonListener(GuiSchematicSaveBase.ButtonType type) {
        return new ButtonListener(type, this);
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final GuiSchematicSaveExported gui;
        private final GuiSchematicSaveBase.ButtonType type;

        public ButtonListener(GuiSchematicSaveBase.ButtonType type, GuiSchematicSaveExported gui) {
            this.type = type;
            this.gui = gui;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == GuiSchematicSaveBase.ButtonType.SAVE) {
                File dir = ((WidgetSchematicBrowser)this.gui.getListWidget()).getCurrentDirectory();
                String fileName = this.gui.getTextFieldText();
                if (!dir.isDirectory()) {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_save.invalid_directory", new Object[]{dir.getAbsolutePath()});
                    return;
                }
                if (fileName.isEmpty()) {
                    this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_save.invalid_schematic_name", new Object[]{fileName});
                    return;
                }
                if (this.gui.type == WidgetFileBrowserBase.DirectoryEntryType.FILE) {
                    File inDir = this.gui.dirSource;
                    String inFile = this.gui.inputFileName;
                    boolean override = GuiBase.isShiftDown();
                    boolean ignoreEntities = this.gui.checkboxIgnoreEntities.isChecked();
                    FileType fileType = FileType.fromFile(new File(inDir, inFile));
                    if (fileType == FileType.LITEMATICA_SCHEMATIC) {
                        if (this.gui.exportType == GuiSchematicManager.ExportType.V6_LITEMATIC) {
                            if (WorldUtils.convertLitematicaSchematicToV6LitematicaSchematic(inDir, inFile, dir, fileName, ignoreEntities, override, (IStringConsumer)this.gui)) {
                                this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.message.litematic_downgrade_exported_as", new Object[]{fileName});
                                ((WidgetSchematicBrowser)this.gui.getListWidget()).refreshEntries();
                            }
                        } else if (this.gui.exportType == GuiSchematicManager.ExportType.SCHEMATIC) {
                            if (WorldUtils.convertLitematicaSchematicToSchematicaSchematic(inDir, inFile, dir, fileName, ignoreEntities, override, (IStringConsumer)this.gui)) {
                                this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.message.schematic_exported_as", new Object[]{fileName});
                                ((WidgetSchematicBrowser)this.gui.getListWidget()).refreshEntries();
                            }
                        } else if (this.gui.exportType == GuiSchematicManager.ExportType.VANILLA && WorldUtils.convertLitematicaSchematicToVanillaStructure(inDir, inFile, dir, fileName, ignoreEntities, override, (IStringConsumer)this.gui)) {
                            this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.message.schematic_exported_as", new Object[]{fileName});
                            ((WidgetSchematicBrowser)this.gui.getListWidget()).refreshEntries();
                        }
                        return;
                    }
                }
                this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_export.unsupported_type", new Object[]{this.gui.inputFileName});
            }
        }
    }
}

