/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.util.JsonUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 */
package fi.dy.masa.litematica.schematic.projects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.util.JsonUtils;
import javax.annotation.Nullable;
import net.minecraft.class_2338;

public class SchematicVersion {
    private final String name;
    private final String fileName;
    private final class_2338 areaOffset;
    private final int version;
    private final long timeStamp;

    SchematicVersion(String name, String fileName, class_2338 areaOffset, int version, long timeStamp) {
        this.name = name;
        this.fileName = fileName;
        this.areaOffset = areaOffset;
        this.version = version;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return this.name;
    }

    public String getFileName() {
        return this.fileName;
    }

    public class_2338 getAreaOffset() {
        return this.areaOffset;
    }

    public int getVersion() {
        return this.version;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.add("name", (JsonElement)new JsonPrimitive(this.name));
        obj.add("file_name", (JsonElement)new JsonPrimitive(this.fileName));
        obj.add("area_offset", (JsonElement)JsonUtils.blockPosToJson((class_2338)this.areaOffset));
        obj.add("version", (JsonElement)new JsonPrimitive((Number)this.version));
        obj.add("timestamp", (JsonElement)new JsonPrimitive((Number)this.timeStamp));
        return obj;
    }

    @Nullable
    public static SchematicVersion fromJson(JsonObject obj) {
        class_2338 areaOffset = JsonUtils.blockPosFromJson((JsonObject)obj, (String)"area_offset");
        if (areaOffset != null && JsonUtils.hasString((JsonObject)obj, (String)"name") && JsonUtils.hasString((JsonObject)obj, (String)"file_name")) {
            String name = JsonUtils.getString((JsonObject)obj, (String)"name");
            String fileName = JsonUtils.getString((JsonObject)obj, (String)"file_name");
            int version = JsonUtils.getInteger((JsonObject)obj, (String)"version");
            long timeStamp = JsonUtils.getLong((JsonObject)obj, (String)"timestamp");
            return new SchematicVersion(name, fileName, areaOffset, version, timeStamp);
        }
        return null;
    }
}

