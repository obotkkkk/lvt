/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.interfaces.IMessageConsumer
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.PositionUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_638
 *  org.apache.commons.io.FileUtils
 */
package fi.dy.masa.litematica.selection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiAreaSelectionEditorNormal;
import fi.dy.masa.litematica.gui.GuiAreaSelectionEditorSimple;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.AreaSelectionSimple;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionMode;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.interfaces.IMessageConsumer;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_638;
import org.apache.commons.io.FileUtils;

public class SelectionManager {
    private final class_310 mc = class_310.method_1551();
    private final Map<String, AreaSelection> selections = new HashMap<String, AreaSelection>();
    private final Map<String, AreaSelection> readOnlySelections = new HashMap<String, AreaSelection>();
    @Nullable
    private String currentSelectionId;
    @Nullable
    private GrabbedElement grabbedElement;
    private SelectionMode mode = (SelectionMode)Configs.InfoOverlays.DEFAULT_SELECTION_MODE.getOptionListValue();
    private boolean modeDirty = true;

    public SelectionMode getSelectionMode() {
        if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
            SchematicProject project = DataManager.getSchematicProjectsManager().getCurrentProject();
            return project != null ? project.getSelectionMode() : SelectionMode.SIMPLE;
        }
        return this.mode;
    }

    public void checkSelectionModeConfig() {
        if (this.modeDirty) {
            this.mode = (SelectionMode)Configs.InfoOverlays.DEFAULT_SELECTION_MODE.getOptionListValue();
            this.modeDirty = false;
        }
    }

    public void switchSelectionMode() {
        if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
            SchematicProject project = DataManager.getSchematicProjectsManager().getCurrentProject();
            if (project != null) {
                project.switchSelectionMode();
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.error.schematic_projects.in_projects_mode_but_no_project_open", (Object[])new Object[0]);
            }
        } else {
            this.mode = this.mode.cycle(true);
        }
    }

    @Nullable
    public String getCurrentSelectionId() {
        return this.mode == SelectionMode.NORMAL ? this.currentSelectionId : null;
    }

    @Nullable
    public String getCurrentNormalSelectionId() {
        return this.currentSelectionId;
    }

    public boolean hasNormalSelection() {
        if (DataManager.getSchematicProjectsManager().hasProjectOpen()) {
            return true;
        }
        return this.getNormalSelection(this.currentSelectionId) != null;
    }

    @Nullable
    public AreaSelection getCurrentSelection() {
        SchematicProject project;
        if (DataManager.getSchematicProjectsManager().hasProjectOpen() && (project = DataManager.getSchematicProjectsManager().getCurrentProject()) != null) {
            return project.getSelection();
        }
        return this.getSelection(this.currentSelectionId);
    }

    @Nullable
    public AreaSelection getSelection(@Nullable String selectionId) {
        if (this.mode == SelectionMode.SIMPLE) {
            return this.getSimpleSelection();
        }
        return this.getNormalSelection(selectionId);
    }

    protected AreaSelectionSimple getSimpleSelection() {
        return DataManager.getSimpleArea();
    }

    @Nullable
    protected AreaSelection getNormalSelection(@Nullable String selectionId) {
        return selectionId != null ? this.selections.get(selectionId) : null;
    }

    @Nullable
    public AreaSelection getOrLoadSelection(String selectionId) {
        AreaSelection selection = this.getNormalSelection(selectionId);
        if (selection == null && (selection = this.tryLoadSelectionFromFile(selectionId)) != null) {
            this.selections.put(selectionId, selection);
        }
        return selection;
    }

    @Nullable
    public AreaSelection getOrLoadSelectionReadOnly(String selectionId) {
        AreaSelection selection = this.getNormalSelection(selectionId);
        if (selection == null && (selection = this.readOnlySelections.get(selectionId)) == null && (selection = this.tryLoadSelectionFromFile(selectionId)) != null) {
            this.readOnlySelections.put(selectionId, selection);
        }
        return selection;
    }

    @Nullable
    private AreaSelection tryLoadSelectionFromFile(String selectionId) {
        return SelectionManager.tryLoadSelectionFromFile(new File(selectionId));
    }

    @Nullable
    public static AreaSelection tryLoadSelectionFromFile(File file) {
        JsonElement el = JsonUtils.parseJsonFile((File)file);
        if (el != null && el.isJsonObject()) {
            return AreaSelection.fromJson(el.getAsJsonObject());
        }
        return null;
    }

    public boolean removeSelection(String selectionId) {
        if (selectionId != null && this.selections.remove(selectionId) != null) {
            File file = new File(selectionId);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    public boolean renameSelection(String selectionId, String newName, IMessageConsumer feedback) {
        File dir = new File(selectionId);
        dir = dir.getParentFile();
        return this.renameSelection(dir, selectionId, newName, feedback);
    }

    public boolean renameSelection(File dir, String selectionId, String newName, IMessageConsumer feedback) {
        return this.renameSelection(dir, selectionId, newName, false, feedback);
    }

    public boolean renameSelection(File dir, String selectionId, String newName, boolean copy, IMessageConsumer feedback) {
        File file = new File(selectionId);
        if (file.exists() && file.isFile()) {
            String newFileName = FileNameUtils.generateSafeFileName((String)newName);
            if (newFileName.isEmpty()) {
                feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_selection.rename.invalid_safe_file_name", new Object[]{newFileName});
                return false;
            }
            File newFile = new File(dir, newFileName + ".json");
            if (!newFile.exists() && (copy || file.renameTo(newFile))) {
                AreaSelection selection;
                String newId = newFile.getAbsolutePath();
                if (copy) {
                    try {
                        FileUtils.copyFile((File)file, (File)newFile);
                    }
                    catch (Exception e) {
                        feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_selection.copy_failed", new Object[0]);
                        Litematica.LOGGER.warn("Copy failed", (Throwable)e);
                        return false;
                    }
                    selection = this.getOrLoadSelection(newId);
                } else {
                    selection = this.selections.remove(selectionId);
                }
                if (selection != null) {
                    SelectionManager.renameSubRegionBoxIfSingle(selection, newName);
                    selection.setName(newName);
                    this.selections.put(newId, selection);
                    if (selectionId.equals(this.currentSelectionId)) {
                        this.currentSelectionId = newId;
                    }
                    return true;
                }
            } else {
                feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_selection.rename.already_exists", new Object[]{newFile.getName()});
            }
        }
        return false;
    }

    public void setCurrentSelection(@Nullable String selectionId) {
        this.currentSelectionId = selectionId;
        if (this.currentSelectionId != null) {
            this.getOrLoadSelection(this.currentSelectionId);
        }
    }

    public String createNewSelection(File dir, String nameIn) {
        Object name = nameIn;
        String safeName = FileNameUtils.generateSafeFileName((String)name);
        File file = new File(dir, safeName + ".json");
        String selectionId = file.getAbsolutePath();
        for (int i = 1; i < 1000 && (safeName.isEmpty() || this.selections.containsKey(selectionId) || file.exists()); ++i) {
            name = nameIn + " " + i;
            safeName = FileNameUtils.generateSafeFileName((String)name);
            file = new File(dir, safeName + ".json");
            selectionId = file.getAbsolutePath();
        }
        AreaSelection selection = new AreaSelection();
        selection.setName((String)name);
        class_2338 pos = class_2338.field_10980;
        if (this.mc.field_1724 != null) {
            pos = PositionUtils.getEntityBlockPos((class_1297)this.mc.field_1724);
        }
        selection.createNewSubRegionBox(pos, (String)name);
        this.selections.put(selectionId, selection);
        this.currentSelectionId = selectionId;
        JsonUtils.writeJsonToFile((JsonObject)selection.toJson(), (File)file);
        return this.currentSelectionId;
    }

    public boolean createNewSubRegion(class_310 mc, boolean printMessage) {
        class_2338 pos;
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection selection = sm.getCurrentSelection();
        if (selection != null && mc.field_1724 != null && selection.createNewSubRegionBox(pos = class_2338.method_49638((class_2374)mc.field_1724.method_19538()), selection.getName()) != null) {
            if (printMessage) {
                String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.added_selection_box", (Object[])new Object[]{posStr});
            }
            return true;
        }
        return false;
    }

    public boolean createNewSubRegionIfDoesntExist(String name, class_310 mc, IMessageConsumer feedback) {
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection selection = sm.getCurrentSelection();
        if (selection != null && mc.field_1724 != null) {
            if (selection.getSubRegionBox(name) != null) {
                feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_editor.create_sub_region.exists", new Object[]{name});
                return false;
            }
            class_2338 pos = class_2338.method_49638((class_2374)mc.field_1724.method_19538());
            if (selection.createNewSubRegionBox(pos, name) != null) {
                String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                feedback.addMessage(Message.MessageType.SUCCESS, "litematica.message.added_selection_box", new Object[]{posStr});
                return true;
            }
        }
        return false;
    }

    public boolean createSelectionFromPlacement(File dir, SchematicPlacement placement, String name, IMessageConsumer feedback) {
        String safeName = FileNameUtils.generateSafeFileName((String)name);
        if (safeName.isEmpty()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_selection.rename.invalid_safe_file_name", new Object[]{safeName});
            return false;
        }
        File file = new File(dir, safeName + ".json");
        String selectionId = file.getAbsolutePath();
        AreaSelection selection = this.getOrLoadSelectionReadOnly(selectionId);
        if (selection == null) {
            selection = AreaSelection.fromPlacement(placement);
            SelectionManager.renameSubRegionBoxIfSingle(selection, name);
            selection.setName(name);
            this.selections.put(selectionId, selection);
            this.currentSelectionId = selectionId;
            JsonUtils.writeJsonToFile((JsonObject)selection.toJson(), (File)file);
            return true;
        }
        feedback.addMessage(Message.MessageType.ERROR, "litematica.error.area_selection.create_failed", new Object[]{safeName});
        return false;
    }

    public boolean changeSelection(class_1937 world, class_1297 entity, int maxDistance) {
        AreaSelection area = this.getCurrentSelection();
        if (area != null) {
            RayTraceUtils.RayTraceWrapper trace = RayTraceUtils.getWrappedRayTraceFromEntity(world, entity, maxDistance);
            if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_CORNER || trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_BODY || trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_ORIGIN) {
                this.changeSelection(area, trace);
                return true;
            }
            if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.MISS) {
                area.clearCurrentSelectedCorner();
                area.setSelectedSubRegionBox(null);
                area.setOriginSelected(false);
                return true;
            }
        }
        return false;
    }

    private void changeSelection(AreaSelection area, RayTraceUtils.RayTraceWrapper trace) {
        area.clearCurrentSelectedCorner();
        if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_CORNER || trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_BODY) {
            Box box = trace.getHitSelectionBox();
            area.setSelectedSubRegionBox(box.getName());
            area.setOriginSelected(false);
            box.setSelectedCorner(trace.getHitCorner());
        } else if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_ORIGIN) {
            area.setSelectedSubRegionBox(null);
            area.setOriginSelected(true);
        }
    }

    public boolean hasSelectedElement() {
        AreaSelection area = this.getCurrentSelection();
        return area != null && (area.getSelectedSubRegionBox() != null || area.isOriginSelected());
    }

    public boolean hasSelectedOrigin() {
        AreaSelection area = this.getCurrentSelection();
        return area != null && area.isOriginSelected();
    }

    public void moveSelectedElement(class_2350 direction, int amount) {
        AreaSelection area = this.getCurrentSelection();
        if (area != null) {
            area.moveSelectedElement(direction, amount);
        }
    }

    public boolean hasGrabbedElement() {
        return this.grabbedElement != null;
    }

    public boolean grabElement(class_310 mc, int maxDistance) {
        RayTraceUtils.RayTraceWrapper trace;
        class_638 world = mc.field_1687;
        class_1297 entity = EntityUtils.getCameraEntity();
        AreaSelection area = this.getCurrentSelection();
        if (area != null && area.getAllSubRegionBoxes().size() > 0 && ((trace = RayTraceUtils.getWrappedRayTraceFromEntity((class_1937)world, entity, maxDistance)).getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_CORNER || trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SELECTION_BOX_BODY)) {
            this.changeSelection(area, trace);
            this.grabbedElement = new GrabbedElement(area, trace.getHitSelectionBox(), trace.getHitCorner(), trace.getHitVec(), entity.method_5836(1.0f).method_1022(trace.getHitVec()));
            InfoUtils.printActionbarMessage((String)"litematica.message.grabbed_element_for_moving", (Object[])new Object[0]);
            return true;
        }
        return false;
    }

    public void setPositionOfCurrentSelectionToRayTrace(class_310 mc, PositionUtils.Corner corner, boolean moveEntireSelection, double maxDistance) {
        AreaSelection area = this.getCurrentSelection();
        if (area != null) {
            boolean movingCorner = area.getSelectedSubRegionBox() != null && corner != PositionUtils.Corner.NONE;
            boolean movingOrigin = area.isOriginSelected();
            if (movingCorner || movingOrigin) {
                class_1297 entity = EntityUtils.getCameraEntity();
                class_2338 pos = RayTraceUtils.getTargetedPosition((class_1937)mc.field_1687, entity, maxDistance, true);
                if (pos == null) {
                    return;
                }
                if (movingOrigin) {
                    this.moveSelectionOrigin(area, pos, moveEntireSelection);
                } else {
                    int cornerIndex = corner.ordinal();
                    if (corner == PositionUtils.Corner.CORNER_1 || corner == PositionUtils.Corner.CORNER_2) {
                        area.setSelectedSubRegionCornerPos(pos, corner);
                    }
                    if (Configs.Generic.CHANGE_SELECTED_CORNER.getBooleanValue()) {
                        area.getSelectedSubRegionBox().setSelectedCorner(corner);
                    }
                    String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                    InfoUtils.printActionbarMessage((String)"litematica.message.set_selection_box_point", (Object[])new Object[]{cornerIndex, posStr});
                }
            }
        }
    }

    public void moveSelectionOrigin(AreaSelection area, class_2338 newOrigin, boolean moveEntireSelection) {
        if (moveEntireSelection) {
            area.moveEntireSelectionTo(newOrigin, true);
        } else {
            class_2338 old = area.getEffectiveOrigin();
            area.setExplicitOrigin(newOrigin);
            String posStrOld = String.format("x: %d, y: %d, z: %d", old.method_10263(), old.method_10264(), old.method_10260());
            String posStrNew = String.format("x: %d, y: %d, z: %d", newOrigin.method_10263(), newOrigin.method_10264(), newOrigin.method_10260());
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.moved_area_origin", (Object[])new Object[]{posStrOld, posStrNew});
        }
    }

    public void handleCuboidModeMouseClick(class_310 mc, double maxDistance, boolean isRightClick, boolean moveEntireSelection) {
        AreaSelection selection = this.getCurrentSelection();
        if (selection != null) {
            if (selection.isOriginSelected()) {
                class_1297 entity = EntityUtils.getCameraEntity();
                class_2338 newOrigin = RayTraceUtils.getTargetedPosition((class_1937)mc.field_1687, entity, maxDistance, true);
                if (newOrigin != null) {
                    this.moveSelectionOrigin(selection, newOrigin, moveEntireSelection);
                }
            } else if (isRightClick) {
                this.resetSelectionToClickedPosition(mc, maxDistance);
            } else {
                this.growSelectionToContainClickedPosition(mc, maxDistance);
            }
        }
    }

    private void resetSelectionToClickedPosition(class_310 mc, double maxDistance) {
        class_1297 entity;
        class_2338 pos;
        AreaSelection area = this.getCurrentSelection();
        if (area != null && area.getSelectedSubRegionBox() != null && (pos = RayTraceUtils.getTargetedPosition((class_1937)mc.field_1687, entity = EntityUtils.getCameraEntity(), maxDistance, true)) != null) {
            area.setSelectedSubRegionCornerPos(pos, PositionUtils.Corner.CORNER_1);
            area.setSelectedSubRegionCornerPos(pos, PositionUtils.Corner.CORNER_2);
        }
    }

    private void growSelectionToContainClickedPosition(class_310 mc, double maxDistance) {
        class_1297 entity;
        class_2338 pos;
        AreaSelection sel = this.getCurrentSelection();
        if (sel != null && sel.getSelectedSubRegionBox() != null && (pos = RayTraceUtils.getTargetedPosition((class_1937)mc.field_1687, entity = EntityUtils.getCameraEntity(), maxDistance, true)) != null) {
            Box box = sel.getSelectedSubRegionBox();
            class_2338 pos1 = box.getPos1();
            class_2338 pos2 = box.getPos2();
            if (pos1 == null) {
                pos1 = pos;
            }
            if (pos2 == null) {
                pos2 = pos;
            }
            class_2338 posMin = fi.dy.masa.litematica.util.PositionUtils.getMinCorner(fi.dy.masa.litematica.util.PositionUtils.getMinCorner(pos1, pos2), pos);
            class_2338 posMax = fi.dy.masa.litematica.util.PositionUtils.getMaxCorner(fi.dy.masa.litematica.util.PositionUtils.getMaxCorner(pos1, pos2), pos);
            sel.setSelectedSubRegionCornerPos(posMin, PositionUtils.Corner.CORNER_1);
            sel.setSelectedSubRegionCornerPos(posMax, PositionUtils.Corner.CORNER_2);
        }
    }

    public static void renameSubRegionBoxIfSingle(AreaSelection selection, String newName) {
        List<Box> boxes = selection.getAllSubRegionBoxes();
        if (boxes.size() == 1 && boxes.getFirst().getName().equals(selection.getName())) {
            selection.renameSubRegionBox(selection.getName(), newName);
        }
    }

    public void releaseGrabbedElement() {
        this.grabbedElement = null;
    }

    public void changeGrabDistance(class_1297 entity, double amount) {
        if (this.grabbedElement != null) {
            this.grabbedElement.changeGrabDistance(amount);
            this.grabbedElement.moveElement(entity);
        }
    }

    public void moveGrabbedElement(class_1297 entity) {
        if (this.grabbedElement != null) {
            this.grabbedElement.moveElement(entity);
        }
    }

    public void clear() {
        this.grabbedElement = null;
        this.currentSelectionId = null;
        this.selections.clear();
        this.readOnlySelections.clear();
    }

    @Nullable
    public GuiBase getEditGui() {
        AreaSelection selection = this.getCurrentSelection();
        if (this.getSelectionMode() == SelectionMode.NORMAL) {
            if (selection != null) {
                return new GuiAreaSelectionEditorNormal(selection);
            }
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.error.area_editor.open_gui.no_selection", (Object[])new Object[0]);
            return null;
        }
        return new GuiAreaSelectionEditorSimple(selection);
    }

    public void openEditGui(@Nullable class_437 parent) {
        GuiBase gui = this.getEditGui();
        if (gui != null) {
            gui.setParent(parent);
            GuiBase.openGui((class_437)gui);
        }
    }

    public void loadFromJson(JsonObject obj) {
        String currentId;
        AreaSelection selection;
        this.clear();
        if (JsonUtils.hasString((JsonObject)obj, (String)"current") && (selection = this.tryLoadSelectionFromFile(currentId = obj.get("current").getAsString())) != null) {
            this.selections.put(currentId, selection);
            this.setCurrentSelection(currentId);
        }
        if (JsonUtils.hasString((JsonObject)obj, (String)"mode")) {
            this.mode = SelectionMode.fromStringStatic(obj.get("mode").getAsString());
            this.modeDirty = false;
        } else {
            this.mode = (SelectionMode)Configs.InfoOverlays.DEFAULT_SELECTION_MODE.getOptionListValue();
        }
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("mode", (JsonElement)new JsonPrimitive(this.mode.name()));
        try {
            for (Map.Entry<String, AreaSelection> entry : this.selections.entrySet()) {
                JsonUtils.writeJsonToFile((JsonObject)entry.getValue().toJson(), (File)new File(entry.getKey()));
            }
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("Exception while writing area selections to disk", (Throwable)e);
        }
        AreaSelection current = this.currentSelectionId != null ? this.selections.get(this.currentSelectionId) : null;
        this.selections.clear();
        this.readOnlySelections.clear();
        if (current != null) {
            obj.add("current", (JsonElement)new JsonPrimitive(this.currentSelectionId));
            this.selections.put(this.currentSelectionId, current);
        }
        return obj;
    }

    private static class GrabbedElement {
        private final AreaSelection area;
        public final Box grabbedBox;
        public final Box originalBox;
        public final class_243 grabPosition;
        public final PositionUtils.Corner grabbedCorner;
        public double grabDistance;

        private GrabbedElement(AreaSelection area, Box box, PositionUtils.Corner corner, class_243 grabPosition, double grabDistance) {
            this.area = area;
            this.grabbedBox = box;
            this.grabbedCorner = corner;
            this.grabPosition = grabPosition;
            this.grabDistance = grabDistance;
            this.originalBox = new Box(box.getPos1(), box.getPos2(), "");
        }

        public void changeGrabDistance(double amount) {
            this.grabDistance += amount;
        }

        public void moveElement(class_1297 entity) {
            class_2338 pos;
            class_243 newLookPos = entity.method_5836(1.0f).method_1019(entity.method_5828(1.0f).method_1021(this.grabDistance));
            class_243 change = newLookPos.method_1020(this.grabPosition);
            if ((this.grabbedCorner == PositionUtils.Corner.NONE || this.grabbedCorner == PositionUtils.Corner.CORNER_1) && this.grabbedBox.getPos1() != null) {
                pos = this.originalBox.getPos1().method_10069((int)Math.floor(change.field_1352), (int)Math.floor(change.field_1351), (int)Math.floor(change.field_1350));
                this.area.setSubRegionCornerPos(this.grabbedBox, PositionUtils.Corner.CORNER_1, pos);
            }
            if ((this.grabbedCorner == PositionUtils.Corner.NONE || this.grabbedCorner == PositionUtils.Corner.CORNER_2) && this.grabbedBox.getPos2() != null) {
                pos = this.originalBox.getPos2().method_10069((int)Math.floor(change.field_1352), (int)Math.floor(change.field_1351), (int)Math.floor(change.field_1350));
                this.area.setSubRegionCornerPos(this.grabbedBox, PositionUtils.Corner.CORNER_2, pos);
            }
        }
    }
}

