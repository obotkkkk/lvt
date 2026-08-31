/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  fi.dy.masa.malilib.util.JsonUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 */
package fi.dy.masa.litematica.selection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.malilib.util.JsonUtils;
import javax.annotation.Nullable;
import net.minecraft.class_2338;

public class AreaSelectionSimple
extends AreaSelection {
    public AreaSelectionSimple(boolean createDefaultBox) {
        if (createDefaultBox) {
            this.createDefaultBoxIfNeeded();
        }
    }

    @Override
    public boolean setSelectedSubRegionBox(String name) {
        return false;
    }

    @Override
    @Nullable
    public String createNewSubRegionBox(class_2338 pos1, String nameIn) {
        return null;
    }

    @Override
    public boolean addSubRegionBox(Box box, boolean replace) {
        return false;
    }

    @Override
    public void removeAllSubRegionBoxes() {
    }

    @Override
    public boolean removeSubRegionBox(String name) {
        return false;
    }

    @Override
    public boolean removeSelectedSubRegionBox() {
        return false;
    }

    private void createDefaultBoxIfNeeded() {
        if (this.subRegionBoxes.size() != 1) {
            this.subRegionBoxes.clear();
            Box box = new Box(class_2338.field_10980, class_2338.field_10980, this.getName());
            this.subRegionBoxes.put(box.getName(), box);
            this.currentBox = box.getName();
        } else if (this.currentBox == null || this.subRegionBoxes.get(this.currentBox) == null) {
            this.currentBox = (String)this.subRegionBoxes.keySet().iterator().next();
        }
    }

    @Override
    public AreaSelectionSimple copy() {
        return AreaSelectionSimple.fromJson(this.toJson());
    }

    public static AreaSelectionSimple fromJson(JsonObject obj) {
        class_2338 pos;
        Box box;
        JsonElement el;
        JsonArray arr;
        AreaSelectionSimple area = new AreaSelectionSimple(false);
        if (JsonUtils.hasArray((JsonObject)obj, (String)"boxes") && (arr = obj.get("boxes").getAsJsonArray()).size() > 0 && (el = arr.get(0)).isJsonObject() && (box = Box.fromJson(el.getAsJsonObject())) != null) {
            area.subRegionBoxes.put(box.getName(), box);
            area.currentBox = box.getName();
        }
        if (JsonUtils.hasString((JsonObject)obj, (String)"name")) {
            area.setName(obj.get("name").getAsString());
        }
        if ((pos = JsonUtils.blockPosFromJson((JsonObject)obj, (String)"origin")) != null) {
            area.setExplicitOrigin(pos);
        } else {
            area.updateCalculatedOrigin();
        }
        area.createDefaultBoxIfNeeded();
        return area;
    }
}

