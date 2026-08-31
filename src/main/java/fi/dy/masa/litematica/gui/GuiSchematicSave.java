/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.interfaces.ICompletionListener
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.FileUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_310
 */
package fi.dy.masa.litematica.gui;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicSaveBase;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicBrowser;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskSaveSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.class_310;

public class GuiSchematicSave
extends GuiSchematicSaveBase
implements ICompletionListener {
    protected final SelectionManager selectionManager;

    public GuiSchematicSave() {
        this(null);
    }

    public GuiSchematicSave(@Nullable LitematicaSchematic schematic) {
        super(schematic);
        this.title = schematic != null ? StringUtils.translate((String)"litematica.gui.title.save_schematic_from_memory", (Object[])new Object[0]) : StringUtils.translate((String)"litematica.gui.title.create_schematic_from_selection", (Object[])new Object[0]);
        this.selectionManager = DataManager.getSelectionManager();
        AreaSelection area = this.selectionManager.getCurrentSelection();
        if (area != null) {
            this.defaultText = FileNameUtils.generateSafeFileName((String)area.getName());
            if (Configs.Generic.GENERATE_LOWERCASE_NAMES.getBooleanValue()) {
                this.defaultText = FileNameUtils.generateSimpleSafeFileName((String)this.defaultText);
            }
        }
    }

    @Override
    public String getBrowserContext() {
        return "schematic_save";
    }

    @Override
    public File getDefaultDirectory() {
        return DataManager.getSchematicsBaseDirectory();
    }

    @Override
    protected IButtonActionListener createButtonListener(GuiSchematicSaveBase.ButtonType type) {
        return new ButtonListener(type, this.selectionManager, this);
    }

    public void onTaskCompleted() {
        if (this.mc.method_18854()) {
            this.refreshList();
        } else {
            this.mc.execute(this::refreshList);
        }
    }

    private void refreshList() {
        if (GuiUtils.getCurrentScreen() == this) {
            ((WidgetSchematicBrowser)this.getListWidget()).refreshEntries();
            ((WidgetSchematicBrowser)this.getListWidget()).clearSchematicMetadataCache();
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final GuiSchematicSave gui;
        private final SelectionManager selectionManager;
        private final GuiSchematicSaveBase.ButtonType type;

        public ButtonListener(GuiSchematicSaveBase.ButtonType type, SelectionManager selectionManager, GuiSchematicSave gui) {
            this.type = type;
            this.selectionManager = selectionManager;
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
                if (this.gui.schematic != null) {
                    LitematicaSchematic schematic = this.gui.schematic;
                    if (schematic.writeToFile(dir, fileName, GuiBase.isShiftDown())) {
                        schematic.getMetadata().clearModifiedSinceSaved();
                        ((WidgetSchematicBrowser)this.gui.getListWidget()).refreshEntries();
                        this.gui.addMessage(Message.MessageType.SUCCESS, "litematica.message.schematic_saved_as", new Object[]{fileName});
                    }
                } else {
                    AreaSelection area = this.selectionManager.getCurrentSelection();
                    if (area != null) {
                        boolean overwrite = GuiBase.isShiftDown();
                        Object fileNameTmp = fileName;
                        if (!((String)fileNameTmp).endsWith(".litematic")) {
                            fileNameTmp = (String)fileNameTmp + ".litematic";
                        }
                        if (!FileUtils.canWriteToFile((File)dir, (String)fileNameTmp, (boolean)overwrite)) {
                            this.gui.addMessage(Message.MessageType.ERROR, "litematica.error.schematic_write_to_file_failed.exists", new Object[]{fileNameTmp});
                            return;
                        }
                        String author = this.gui.mc.field_1724.method_5477().getString();
                        boolean ignoreEntities = this.gui.checkboxIgnoreEntities.isChecked();
                        boolean visibleOnly = this.gui.checkboxVisibleOnly.isChecked();
                        boolean includeSupportBlocks = this.gui.checkboxIncludeSupportBlocks.isChecked();
                        boolean fromSchematicWorld = this.gui.checkboxSaveFromSchematicWorld.isChecked();
                        LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(visibleOnly, includeSupportBlocks, ignoreEntities, fromSchematicWorld);
                        LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(area, author);
                        TaskSaveSchematic task = new TaskSaveSchematic(dir, fileName, schematic, area, info, overwrite);
                        task.setCompletionListener(this.gui);
                        TaskScheduler.getServerInstanceIfExistsOrClient().scheduleTask(task, 10);
                        this.gui.addMessage(Message.MessageType.INFO, "litematica.message.schematic_save_task_created", new Object[0]);
                    } else {
                        this.gui.addMessage(Message.MessageType.ERROR, "litematica.message.error.schematic_save_no_area_selected", new Object[0]);
                    }
                }
            }
        }
    }

    public static class InMemorySchematicCreator
    implements IStringConsumer {
        private final AreaSelection area;
        private final class_310 mc;

        public InMemorySchematicCreator(AreaSelection area) {
            this.area = area;
            this.mc = class_310.method_1551();
        }

        public void setString(String string) {
            LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
            String author = this.mc.field_1724.method_5477().getString();
            LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(this.area, author);
            if (schematic != null) {
                schematic.getMetadata().setName(string);
                TaskSaveSchematic task = new TaskSaveSchematic(schematic, this.area, info);
                TaskScheduler.getServerInstanceIfExistsOrClient().scheduleTask(task, 10);
            }
        }
    }
}

