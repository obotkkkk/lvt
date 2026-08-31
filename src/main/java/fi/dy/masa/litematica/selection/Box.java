/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.PositionUtils$CoordinateType
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 */
package fi.dy.masa.litematica.selection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2382;

public class Box {
    @Nullable
    private class_2338 pos1;
    @Nullable
    private class_2338 pos2;
    private class_2338 size = class_2338.field_10980;
    private String name = "Unnamed";
    private PositionUtils.Corner selectedCorner = PositionUtils.Corner.NONE;

    public Box() {
        this.pos1 = class_2338.field_10980;
        this.pos2 = class_2338.field_10980;
        this.updateSize();
    }

    public Box(@Nullable class_2338 pos1, @Nullable class_2338 pos2, String name) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.name = name;
        this.updateSize();
    }

    public Box copy() {
        Box box = new Box(this.pos1, this.pos2, this.name);
        box.setSelectedCorner(this.selectedCorner);
        return box;
    }

    @Nullable
    public class_2338 getPos1() {
        return this.pos1;
    }

    @Nullable
    public class_2338 getPos2() {
        return this.pos2;
    }

    public class_2338 getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    public PositionUtils.Corner getSelectedCorner() {
        return this.selectedCorner;
    }

    public void setPos1(@Nullable class_2338 pos) {
        this.pos1 = pos;
        this.updateSize();
    }

    public void setPos2(@Nullable class_2338 pos) {
        this.pos2 = pos;
        this.updateSize();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelectedCorner(PositionUtils.Corner corner) {
        this.selectedCorner = corner;
    }

    private void updateSize() {
        this.size = this.pos1 != null && this.pos2 != null ? PositionUtils.getAreaSizeFromRelativeEndPosition(this.pos2.method_10059((class_2382)this.pos1)) : (this.pos1 == null && this.pos2 == null ? class_2338.field_10980 : new class_2338(1, 1, 1));
    }

    public class_2338 getPosition(PositionUtils.Corner corner) {
        return corner == PositionUtils.Corner.CORNER_1 ? this.getPos1() : this.getPos2();
    }

    public int getCoordinate(PositionUtils.Corner corner, PositionUtils.CoordinateType type) {
        class_2338 pos = this.getPosition(corner);
        switch (type) {
            case X: {
                return pos.method_10263();
            }
            case Y: {
                return pos.method_10264();
            }
            case Z: {
                return pos.method_10260();
            }
        }
        return 0;
    }

    protected void setPosition(class_2338 pos, PositionUtils.Corner corner) {
        if (corner == PositionUtils.Corner.CORNER_1) {
            this.setPos1(pos);
        } else if (corner == PositionUtils.Corner.CORNER_2) {
            this.setPos2(pos);
        }
    }

    public void setCoordinate(int value, PositionUtils.Corner corner, PositionUtils.CoordinateType type) {
        class_2338 pos = this.getPosition(corner);
        pos = PositionUtils.getModifiedPosition(pos, value, type);
        this.setPosition(pos, corner);
    }

    @Nullable
    public static Box fromJson(JsonObject obj) {
        if (JsonUtils.hasString((JsonObject)obj, (String)"name")) {
            class_2338 pos1 = JsonUtils.blockPosFromJson((JsonObject)obj, (String)"pos1");
            class_2338 pos2 = JsonUtils.blockPosFromJson((JsonObject)obj, (String)"pos2");
            if (pos1 != null || pos2 != null) {
                Box box = new Box();
                box.setName(obj.get("name").getAsString());
                if (pos1 != null) {
                    box.setPos1(pos1);
                }
                if (pos2 != null) {
                    box.setPos2(pos2);
                }
                return box;
            }
        }
        return null;
    }

    @Nullable
    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (this.pos1 != null) {
            obj.add("pos1", (JsonElement)JsonUtils.blockPosToJson((class_2338)this.pos1));
        }
        if (this.pos2 != null) {
            obj.add("pos2", (JsonElement)JsonUtils.blockPosToJson((class_2338)this.pos2));
        }
        obj.add("name", (JsonElement)new JsonPrimitive(this.name));
        return this.pos1 != null || this.pos2 != null ? obj : null;
    }
}

