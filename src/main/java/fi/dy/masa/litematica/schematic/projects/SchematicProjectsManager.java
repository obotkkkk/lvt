/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.PositionUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1297
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.schematic.projects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.gui.GuiSchematicProjectManager;
import fi.dy.masa.litematica.gui.GuiSchematicProjectsBrowser;
import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import net.minecraft.class_437;

public class SchematicProjectsManager {
    private final class_310 mc = class_310.method_1551();
    @Nullable
    private SchematicProject currentProject;

    public void openSchematicProjectsGui() {
        if (!Configs.Generic.UNHIDE_SCHEMATIC_PROJECTS.getBooleanValue()) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (int)10000, (String)"litematica.message.warning.schematic_projects_hidden", (Object[])new Object[0]);
            return;
        }
        if (this.currentProject != null) {
            GuiSchematicProjectManager gui = new GuiSchematicProjectManager(this.currentProject);
            gui.setParent(GuiUtils.getCurrentScreen());
            GuiBase.openGui((class_437)gui);
        } else {
            GuiSchematicProjectsBrowser gui = new GuiSchematicProjectsBrowser();
            gui.setParent(GuiUtils.getCurrentScreen());
            GuiBase.openGui((class_437)gui);
        }
    }

    @Nullable
    public SchematicProject getCurrentProject() {
        return this.hasProjectOpen() ? this.currentProject : null;
    }

    public boolean hasProjectOpen() {
        return this.currentProject != null && Configs.Generic.UNHIDE_SCHEMATIC_PROJECTS.getBooleanValue();
    }

    public void createNewProject(File dir, String projectName) {
        this.closeCurrentProject();
        this.currentProject = new SchematicProject(dir, new File(dir, projectName + ".json"));
        this.currentProject.setName(projectName);
        this.currentProject.setOrigin(PositionUtils.getEntityBlockPos((class_1297)this.mc.field_1724));
        this.currentProject.saveToFile();
    }

    public boolean openProject(File projectFile) {
        this.closeCurrentProject();
        this.currentProject = this.loadProjectFromFile(projectFile, true);
        if (this.currentProject == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.failed_to_load_project", (Object[])new Object[0]);
            return false;
        }
        return true;
    }

    @Nullable
    public SchematicProject loadProjectFromFile(File projectFile, boolean createPlacement) {
        JsonElement el;
        if (projectFile.getName().endsWith(".json") && projectFile.exists() && projectFile.isFile() && projectFile.canRead() && (el = JsonUtils.parseJsonFile((File)projectFile)) != null && el.isJsonObject()) {
            return SchematicProject.fromJson(el.getAsJsonObject(), projectFile, createPlacement);
        }
        return null;
    }

    public void closeCurrentProject() {
        if (this.currentProject != null) {
            this.currentProject.saveToFile();
            this.removeCurrentPlacement();
            this.clear();
        }
    }

    public void saveCurrentProject() {
        if (this.currentProject != null) {
            this.currentProject.saveToFile();
        }
    }

    private void removeCurrentPlacement() {
        if (this.currentProject != null) {
            this.currentProject.removeCurrentPlacement();
        }
    }

    public void clear() {
        this.currentProject = null;
    }

    public boolean cycleVersion(int amount) {
        if (this.currentProject != null) {
            return this.currentProject.cycleVersion(amount);
        }
        InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.no_project_open", (Object[])new Object[0]);
        return false;
    }

    public boolean commitNewVersion(String string) {
        if (this.currentProject != null) {
            return this.currentProject.commitNewVersion(string);
        }
        InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.schematic_projects.no_project_open", (Object[])new Object[0]);
        return false;
    }

    public boolean pasteCurrentVersionToWorld() {
        SchematicProject project = this.getCurrentProject();
        if (project != null) {
            project.pasteToWorld();
            return true;
        }
        return false;
    }

    public boolean deleteLastSeenArea(class_310 mc) {
        SchematicProject project = this.getCurrentProject();
        if (project != null) {
            project.deleteLastSeenArea(mc);
            return true;
        }
        return false;
    }

    public boolean deleteBlocksByPlacement() {
        SchematicProject project = this.getCurrentProject();
        if (project != null) {
            project.deleteBlocksByPlacement();
            return true;
        }
        return false;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (this.currentProject != null) {
            obj.add("current_project", (JsonElement)new JsonPrimitive(this.currentProject.getProjectFile().getAbsolutePath()));
        }
        return obj;
    }

    public void loadFromJson(JsonObject obj) {
        if (JsonUtils.hasString((JsonObject)obj, (String)"current_project")) {
            File file = new File(JsonUtils.getString((JsonObject)obj, (String)"current_project"));
            this.currentProject = this.loadProjectFromFile(file, true);
        }
    }
}

