/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.ICompletionListener
 *  fi.dy.masa.malilib.util.FileUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.JsonUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_310
 *  org.apache.commons.io.FileUtils
 */
package fi.dy.masa.litematica.schematic.projects;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskSaveSchematic;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.projects.SchematicVersion;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.AreaSelectionSimple;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.selection.SelectionMode;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.PlacementDeletionMode;
import fi.dy.masa.litematica.util.ToolUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_310;
import org.apache.commons.io.FileUtils;

public class SchematicProject {
    private final List<SchematicVersion> versions = new ArrayList<SchematicVersion>();
    private final File directory;
    private File projectFile;
    private class_2338 origin = class_2338.field_10980;
    private String projectName = "unnamed";
    private AreaSelection selection = new AreaSelection();
    private AreaSelection lastSeenArea = new AreaSelection();
    private AreaSelectionSimple selectionSimple = new AreaSelectionSimple(true);
    private SelectionMode selectionMode = SelectionMode.SIMPLE;
    private int currentVersionId = -1;
    private int lastCheckedOutVersion = -1;
    private int lastPastedVersion = -1;
    private boolean saveInProgress;
    private boolean dirty;
    @Nullable
    private SchematicPlacement currentPlacement;

    public SchematicProject(File directory, File projectFile) {
        this.directory = directory;
        this.projectFile = projectFile;
    }

    public File getDirectory() {
        return this.directory;
    }

    public String getName() {
        return this.projectName;
    }

    public int getVersionCount() {
        return this.versions.size();
    }

    public int getCurrentVersionId() {
        return this.currentVersionId;
    }

    public String getCurrentVersionName() {
        SchematicVersion currentVersion = this.getCurrentVersion();
        return currentVersion != null ? currentVersion.getName() : this.getSelection().getName();
    }

    public void setName(String name) {
        File newFile = new File(this.directory, name + ".json");
        if (!newFile.exists()) {
            try {
                if (this.projectFile.exists()) {
                    FileUtils.moveFile((File)this.projectFile, (File)newFile);
                }
                this.projectName = name;
                this.projectFile = newFile;
                this.selection.setName(name);
                this.selectionSimple.setName(name);
                SelectionManager.renameSubRegionBoxIfSingle(this.selection, name);
                SelectionManager.renameSubRegionBoxIfSingle(this.selectionSimple, name);
                this.dirty = true;
            }
            catch (Exception e) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.failed_to_rename_project_file_exception", (Object[])new Object[]{newFile.getAbsolutePath()});
            }
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.failed_to_rename_project_file_exists", (Object[])new Object[]{name});
        }
    }

    public void setOrigin(class_2338 origin) {
        class_2338 offset = this.selection.getEffectiveOrigin().method_10059((class_2382)this.origin);
        this.selection.moveEntireSelectionTo(origin.method_10081((class_2382)offset), false);
        offset = this.selectionSimple.getEffectiveOrigin().method_10059((class_2382)this.origin);
        this.selectionSimple.moveEntireSelectionTo(origin.method_10081((class_2382)offset), false);
        this.lastSeenArea = new AreaSelection();
        this.origin = origin;
        this.lastPastedVersion = -1;
        SchematicVersion currentVersion = this.getCurrentVersion();
        if (currentVersion != null) {
            class_2338 areaPosition = this.origin.method_10081((class_2382)currentVersion.getAreaOffset());
            if (this.currentPlacement != null) {
                this.currentPlacement.setOrigin(areaPosition, InfoUtils.INFO_MESSAGE_CONSUMER);
            }
        }
        this.dirty = true;
    }

    public File getProjectFile() {
        return this.projectFile;
    }

    public class_2338 getOrigin() {
        return this.origin;
    }

    public AreaSelection getSelection() {
        if (this.selectionMode == SelectionMode.SIMPLE) {
            return this.selectionSimple;
        }
        return this.selection;
    }

    public SelectionMode getSelectionMode() {
        return this.selectionMode;
    }

    public void switchSelectionMode() {
        this.selectionMode = this.selectionMode.cycle(true);
        this.dirty = true;
    }

    public ImmutableList<SchematicVersion> getAllVersions() {
        return ImmutableList.copyOf(this.versions);
    }

    @Nullable
    public SchematicVersion getCurrentVersion() {
        if (this.currentVersionId >= 0 && this.currentVersionId < this.versions.size()) {
            return this.versions.get(this.currentVersionId);
        }
        return null;
    }

    @Nullable
    public SchematicPlacement getCurrentPlacement() {
        return this.currentPlacement;
    }

    private void createAndAddPlacement() {
        SchematicVersion version = this.getCurrentVersion();
        if (version != null && this.currentVersionId != this.lastCheckedOutVersion) {
            this.removeCurrentPlacement();
            Object fileName = version.getFileName();
            FileType fileType = FileType.fromName((String)fileName);
            if (fileType == FileType.UNKNOWN) {
                fileName = (String)fileName + ".litematic";
                fileType = FileType.LITEMATICA_SCHEMATIC;
            }
            LitematicaSchematic schematic = null;
            if (fileType == FileType.LITEMATICA_SCHEMATIC) {
                schematic = LitematicaSchematic.createFromFile(this.directory, (String)fileName);
            } else if (fileType == FileType.SCHEMATICA_SCHEMATIC) {
                schematic = WorldUtils.convertSchematicaSchematicToLitematicaSchematic(this.directory, (String)fileName, false, f -> {});
            } else if (fileType == FileType.VANILLA_STRUCTURE) {
                schematic = WorldUtils.convertStructureToLitematicaSchematic(this.directory, (String)fileName);
            } else if (fileType == FileType.SPONGE_SCHEMATIC) {
                schematic = WorldUtils.convertSpongeSchematicToLitematicaSchematic(this.directory, (String)fileName);
            }
            if (schematic != null) {
                class_2338 areaPosition = this.origin.method_10081((class_2382)version.getAreaOffset());
                this.currentPlacement = SchematicPlacement.createFor(schematic, areaPosition, version.getName(), true, true);
                this.currentPlacement.setShouldBeSaved(false);
                DataManager.getSchematicPlacementManager().addSchematicPlacement(this.currentPlacement, false);
                long time = schematic.getMetadata().getTimeCreated();
                if (time != version.getTimeStamp()) {
                    version = new SchematicVersion(version.getName(), version.getFileName(), version.getAreaOffset(), version.getVersion(), time);
                    this.versions.set(this.currentVersionId, version);
                    this.dirty = true;
                }
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.failed_to_load_schematic", (Object[])new Object[0]);
            }
            this.lastCheckedOutVersion = this.currentVersionId;
        }
    }

    public void pasteToWorld() {
        if (this.currentPlacement != null) {
            class_310 mc = class_310.method_1551();
            if (mc.field_1724 == null || !EntityUtils.isCreativeMode((class_1657)mc.field_1724)) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.generic.creative_mode_only", (Object[])new Object[0]);
                return;
            }
            this.cacheCurrentAreaFromPlacement();
            PlacementDeletionMode mode = (PlacementDeletionMode)Configs.Generic.SCHEMATIC_VCS_DELETE_MODE.getOptionListValue();
            if (mode == PlacementDeletionMode.ENTIRE_VOLUME) {
                ToolUtils.deleteSelectionVolumes(this.lastSeenArea, true, this::pasteCurrentPlacement, mc);
            } else {
                if (this.lastPastedVersion < 0 || this.lastPastedVersion >= this.versions.size()) {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"No previous pasted version known, skipping delete", (Object[])new Object[0]);
                    this.pasteCurrentPlacement();
                    return;
                }
                this.pasteCurrentPlacement();
            }
        }
    }

    protected void pasteCurrentPlacement() {
        this.lastPastedVersion = this.currentVersionId;
        this.dirty = true;
        class_310 mc = class_310.method_1551();
        DataManager.getSchematicPlacementManager().pastePlacementToWorld(this.currentPlacement, false, mc);
    }

    public void deleteLastSeenArea(class_310 mc) {
        ToolUtils.deleteSelectionVolumes(this.lastSeenArea, true, mc);
    }

    public void deleteBlocksByPlacement() {
        if (this.currentPlacement != null) {
            PlacementDeletionMode mode = (PlacementDeletionMode)Configs.Generic.SCHEMATIC_VCS_DELETE_MODE.getOptionListValue();
            ToolUtils.deleteBlocksByPlacement(this.currentPlacement, mode, null);
        }
    }

    public void removeCurrentPlacement() {
        if (this.currentPlacement != null) {
            DataManager.getSchematicPlacementManager().removeSchematicPlacement(this.currentPlacement);
        }
    }

    public boolean cycleVersion(int amount) {
        if (this.currentVersionId >= 0) {
            return this.switchVersion(this.currentVersionId + amount, true);
        }
        return false;
    }

    public boolean switchVersion(int version, boolean createPlacement) {
        if (version != this.currentVersionId && version >= 0 && version < this.versions.size()) {
            this.currentVersionId = version;
            this.dirty = true;
            if (createPlacement) {
                this.createAndAddPlacement();
            }
            return true;
        }
        return false;
    }

    public boolean switchVersion(SchematicVersion version, boolean createPlacement) {
        int index = this.versions.indexOf(version);
        if (index >= 0 && version != this.getCurrentVersion()) {
            return this.switchVersion(index, createPlacement);
        }
        return false;
    }

    private void cacheCurrentAreaFromPlacement() {
        if (this.currentPlacement != null) {
            this.lastSeenArea = AreaSelection.fromPlacement(this.currentPlacement);
            this.dirty = true;
        }
    }

    public boolean commitNewVersion(String name) {
        if (this.checkCanSaveOrPrintError()) {
            class_310 mc = class_310.method_1551();
            String author = mc.field_1724.method_5477().getString();
            String fileName = this.getNextFileName();
            AreaSelection selection = this.getSelection();
            LitematicaSchematic schematic = LitematicaSchematic.createEmptySchematic(selection, author);
            schematic.getMetadata().setName(name);
            class_2338 areaOffset = selection.getEffectiveOrigin().method_10059((class_2382)this.origin);
            SaveCompletionListener listener = new SaveCompletionListener(name, fileName, areaOffset);
            LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
            TaskSaveSchematic task = new TaskSaveSchematic(this.directory, fileName, schematic, selection.copy(), info, false);
            task.setCompletionListener(listener);
            TaskScheduler.getServerInstanceIfExistsOrClient().scheduleTask(task, 2);
            this.saveInProgress = true;
            this.dirty = true;
            this.saveToFile();
            return true;
        }
        return false;
    }

    private String getNextFileName() {
        String nameBase = this.projectName + "_";
        int version = 1;
        int failsafe = 10000000;
        while (failsafe-- > 0) {
            String name = nameBase + String.format("%05d", version) + ".litematic";
            File file = new File(this.directory, name);
            if (!file.exists()) {
                return name;
            }
            ++version;
        }
        return nameBase + "error";
    }

    public void clear() {
        this.origin = class_2338.field_10980;
        this.versions.clear();
        this.selection = new AreaSelection();
        this.selectionSimple = new AreaSelectionSimple(true);
        this.lastSeenArea = new AreaSelection();
        this.currentVersionId = -1;
        this.lastCheckedOutVersion = -1;
        this.lastPastedVersion = -1;
        this.saveInProgress = false;
    }

    public boolean saveToFile() {
        if (!this.dirty || JsonUtils.writeJsonToFile((JsonObject)this.toJson(), (File)this.projectFile)) {
            this.dirty = false;
            return true;
        }
        return false;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("name", (JsonElement)new JsonPrimitive(this.projectName));
        obj.add("origin", (JsonElement)JsonUtils.blockPosToJson((class_2338)this.origin));
        obj.add("current_version_id", (JsonElement)new JsonPrimitive((Number)this.currentVersionId));
        obj.add("last_pasted_version", (JsonElement)new JsonPrimitive((Number)this.lastPastedVersion));
        obj.add("selection_normal", (JsonElement)this.selection.toJson());
        obj.add("selection_simple", (JsonElement)this.selectionSimple.toJson());
        obj.add("last_seen_area", (JsonElement)this.lastSeenArea.toJson());
        obj.add("selection_mode", (JsonElement)new JsonPrimitive(this.selectionMode.name()));
        JsonArray arr = new JsonArray();
        for (int i = 0; i < this.versions.size(); ++i) {
            arr.add((JsonElement)this.versions.get(i).toJson());
        }
        if (arr.size() > 0) {
            obj.add("versions", (JsonElement)arr);
        }
        return obj;
    }

    @Nullable
    public static SchematicProject fromJson(JsonObject obj, File projectFile, boolean createPlacement) {
        class_2338 origin = JsonUtils.blockPosFromJson((JsonObject)obj, (String)"origin");
        if (JsonUtils.hasString((JsonObject)obj, (String)"name") && JsonUtils.hasInteger((JsonObject)obj, (String)"current_version_id") && origin != null) {
            int tmp;
            projectFile = fi.dy.masa.malilib.util.FileUtils.getCanonicalFileIfPossible((File)projectFile);
            SchematicProject project = new SchematicProject(projectFile.getParentFile(), projectFile);
            project.projectName = JsonUtils.getString((JsonObject)obj, (String)"name");
            project.origin = origin;
            if (JsonUtils.hasObject((JsonObject)obj, (String)"selection_normal")) {
                project.selection = AreaSelection.fromJson(JsonUtils.getNestedObject((JsonObject)obj, (String)"selection_normal", (boolean)false));
            }
            if (JsonUtils.hasObject((JsonObject)obj, (String)"selection_simple")) {
                project.selectionSimple = AreaSelectionSimple.fromJson(JsonUtils.getNestedObject((JsonObject)obj, (String)"selection_simple", (boolean)false));
            }
            if (JsonUtils.hasObject((JsonObject)obj, (String)"last_seen_area")) {
                project.lastSeenArea = AreaSelection.fromJson(JsonUtils.getNestedObject((JsonObject)obj, (String)"last_seen_area", (boolean)false));
            }
            if (JsonUtils.hasString((JsonObject)obj, (String)"selection_mode")) {
                project.selectionMode = SelectionMode.fromStringStatic(JsonUtils.getString((JsonObject)obj, (String)"selection_mode"));
            }
            if (JsonUtils.hasArray((JsonObject)obj, (String)"versions")) {
                JsonArray arr = obj.get("versions").getAsJsonArray();
                for (int i = 0; i < arr.size(); ++i) {
                    SchematicVersion version;
                    JsonElement el = arr.get(i);
                    if (!el.isJsonObject() || (version = SchematicVersion.fromJson(el.getAsJsonObject())) == null) continue;
                    project.versions.add(version);
                }
            }
            if (JsonUtils.hasInteger((JsonObject)obj, (String)"last_pasted_version")) {
                project.lastPastedVersion = JsonUtils.getInteger((JsonObject)obj, (String)"last_pasted_version");
            }
            int id = project.versions.size() - 1;
            if (JsonUtils.hasInteger((JsonObject)obj, (String)"current_version_id") && (tmp = JsonUtils.getInteger((JsonObject)obj, (String)"current_version_id")) >= 0 && tmp < project.versions.size()) {
                id = tmp;
            }
            project.switchVersion(id, createPlacement);
            project.dirty = false;
            return project;
        }
        return null;
    }

    boolean checkCanSaveOrPrintError() {
        if (this.saveInProgress) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.save_already_in_progress", (Object[])new Object[0]);
            return false;
        }
        if (this.directory == null || !this.directory.exists() || !this.directory.isDirectory()) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.invalid_project_directory", (Object[])new Object[0]);
            return false;
        }
        if (this.getSelection().getAllSubRegionBoxes().size() == 0) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.empty_selection", (Object[])new Object[0]);
            return false;
        }
        if (class_310.method_1551().field_1724 == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.null_player", (Object[])new Object[0]);
            return false;
        }
        return true;
    }

    private class SaveCompletionListener
    implements ICompletionListener {
        private final String name;
        private final String fileName;
        private final class_2338 areaOffset;
        private final int version;

        private SaveCompletionListener(String name, String fileName, class_2338 areaOffset) {
            this.name = name;
            this.fileName = fileName;
            this.areaOffset = areaOffset;
            this.version = SchematicProject.this.versions.size() + 1;
        }

        public void onTaskCompleted() {
            class_310 mc = class_310.method_1551();
            if (mc.method_18854()) {
                this.saveVersion();
            } else {
                mc.execute(this::saveVersion);
            }
        }

        private void saveVersion() {
            SchematicVersion version = new SchematicVersion(this.name, this.fileName, this.areaOffset, this.version, System.currentTimeMillis());
            SchematicProject.this.versions.add(version);
            SchematicProject.this.switchVersion(SchematicProject.this.versions.size() - 1, true);
            SchematicProject.this.cacheCurrentAreaFromPlacement();
            SchematicProject.this.saveInProgress = false;
            if (GuiUtils.getCurrentScreen() instanceof ICompletionListener) {
                ((ICompletionListener)GuiUtils.getCurrentScreen()).onTaskCompleted();
            }
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.schematic_projects.version_saved", (Object[])new Object[]{this.version, this.name});
        }
    }
}

