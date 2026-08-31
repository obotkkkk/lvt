/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.PrimitiveCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.PositionUtils$CoordinateType
 *  io.netty.buffer.ByteBuf
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_9135
 *  net.minecraft.class_9139
 */
package fi.dy.masa.litematica.schematic.placement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementEventHandler;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public class SubRegionPlacement {
    public static final Codec<SubRegionPlacement> CODEC = RecordCodecBuilder.create(inst -> inst.group((App)PrimitiveCodec.STRING.fieldOf("Name").forGetter(get -> get.name), (App)class_2338.field_25064.fieldOf("DefaultPos").forGetter(get -> get.defaultPos), (App)class_2338.field_25064.fieldOf("Pos").forGetter(get -> get.pos), (App)class_2470.field_39313.fieldOf("Rotation").forGetter(get -> get.rotation), (App)class_2415.field_39311.fieldOf("Mirror").forGetter(get -> get.mirror), (App)PrimitiveCodec.BOOL.fieldOf("Enabled").forGetter(get -> get.enabled), (App)PrimitiveCodec.BOOL.fieldOf("RenderingEnabled").forGetter(get -> get.renderingEnabled), (App)PrimitiveCodec.BOOL.fieldOf("IgnoreEntities").forGetter(get -> get.ignoreEntities), (App)PrimitiveCodec.INT.fieldOf("CoordinateLockMask").forGetter(get -> get.coordinateLockMask)).apply((Applicative)inst, SubRegionPlacement::new));
    public static final class_9139<ByteBuf, class_2415> BLOCK_MIRROR_PACKET_CODEC = class_9135.field_48554.method_56432(class_2415::valueOf, class_2415::method_15434);
    public static final class_9139<ByteBuf, class_2470> BLOCK_ROTATION_PACKET_CODEC = class_9135.field_48554.method_56432(class_2470::valueOf, class_2470::method_15434);
    public static final class_9139<ByteBuf, SubRegionPlacement> PACKET_CODEC = new class_9139<ByteBuf, SubRegionPlacement>(){

        public void encode(ByteBuf buf, SubRegionPlacement value) {
            class_9135.field_48554.encode((Object)buf, (Object)value.name);
            class_2338.field_48404.encode((Object)buf, (Object)value.defaultPos);
            class_2338.field_48404.encode((Object)buf, (Object)value.pos);
            BLOCK_ROTATION_PACKET_CODEC.encode((Object)buf, (Object)value.rotation);
            BLOCK_MIRROR_PACKET_CODEC.encode((Object)buf, (Object)value.mirror);
            class_9135.field_48547.encode((Object)buf, (Object)value.enabled);
            class_9135.field_48547.encode((Object)buf, (Object)value.renderingEnabled);
            class_9135.field_48547.encode((Object)buf, (Object)value.ignoreEntities);
            class_9135.field_49675.encode((Object)buf, (Object)value.coordinateLockMask);
        }

        public SubRegionPlacement decode(ByteBuf buf) {
            return new SubRegionPlacement((String)class_9135.field_48554.decode((Object)buf), (class_2338)class_2338.field_48404.decode((Object)buf), (class_2338)class_2338.field_48404.decode((Object)buf), (class_2470)BLOCK_ROTATION_PACKET_CODEC.decode((Object)buf), (class_2415)BLOCK_MIRROR_PACKET_CODEC.decode((Object)buf), (Boolean)class_9135.field_48547.decode((Object)buf), (Boolean)class_9135.field_48547.decode((Object)buf), (Boolean)class_9135.field_48547.decode((Object)buf), (Integer)class_9135.field_49675.decode((Object)buf));
        }
    };
    private final String name;
    private final class_2338 defaultPos;
    private class_2338 pos;
    private class_2470 rotation = class_2470.field_11467;
    private class_2415 mirror = class_2415.field_11302;
    private boolean enabled = true;
    private boolean renderingEnabled = true;
    private boolean ignoreEntities;
    private int coordinateLockMask;

    public SubRegionPlacement(class_2338 pos, String name) {
        this.pos = pos;
        this.defaultPos = pos;
        this.name = name;
        SchematicPlacementEventHandler.getInstance().onSubRegionInit(this);
    }

    private SubRegionPlacement(String name, class_2338 defPos, class_2338 pos, class_2470 rot, class_2415 mirror, Boolean enabled, Boolean renderingEnabled, Boolean ignoreEntities, Integer coordinateLockMask) {
        this(defPos, name);
        this.pos = pos;
        this.rotation = rot;
        this.mirror = mirror;
        this.enabled = enabled;
        this.renderingEnabled = renderingEnabled;
        this.ignoreEntities = ignoreEntities;
        this.coordinateLockMask = coordinateLockMask;
        SchematicPlacementEventHandler.getInstance().onSubRegionInit(this);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isRenderingEnabled() {
        return this.renderingEnabled;
    }

    public boolean ignoreEntities() {
        return this.ignoreEntities;
    }

    public void setCoordinateLocked(PositionUtils.CoordinateType coord, boolean locked) {
        int mask = 1 << coord.ordinal();
        this.coordinateLockMask = locked ? (this.coordinateLockMask |= mask) : (this.coordinateLockMask &= ~mask);
    }

    public boolean isCoordinateLocked(PositionUtils.CoordinateType coord) {
        int mask = 1 << coord.ordinal();
        return (this.coordinateLockMask & mask) != 0;
    }

    public boolean matchesRequirement(RequiredEnabled required) {
        if (required == RequiredEnabled.ANY) {
            return true;
        }
        if (required == RequiredEnabled.PLACEMENT_ENABLED) {
            return this.isEnabled();
        }
        return this.isEnabled() && this.isRenderingEnabled();
    }

    public String getName() {
        return this.name;
    }

    public class_2338 getDefaultPos() {
        return this.defaultPos;
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public class_2470 getRotation() {
        return this.rotation;
    }

    public class_2415 getMirror() {
        return this.mirror;
    }

    public void setRenderingEnabled(boolean renderingEnabled) {
        this.renderingEnabled = renderingEnabled;
        SchematicPlacementEventHandler.getInstance().onSetSubRegionRender(this, renderingEnabled);
    }

    public void toggleRenderingEnabled() {
        this.setRenderingEnabled(!this.isRenderingEnabled());
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
        SchematicPlacementEventHandler.getInstance().onSetSubRegionEnabled(this, enabled);
    }

    void toggleEnabled() {
        this.setEnabled(!this.isEnabled());
    }

    void toggleIgnoreEntities() {
        this.ignoreEntities = !this.ignoreEntities;
    }

    void setPos(class_2338 pos) {
        this.pos = PositionUtils.getModifiedPartiallyLockedPosition(this.pos, pos, this.coordinateLockMask);
        SchematicPlacementEventHandler.getInstance().onSetSubRegionOrigin(this, this.pos);
    }

    void setRotation(class_2470 rotation) {
        this.rotation = rotation;
        SchematicPlacementEventHandler.getInstance().onSetSubRegionRotation(this, rotation);
    }

    void setMirror(class_2415 mirror) {
        this.mirror = mirror;
        SchematicPlacementEventHandler.getInstance().onSetSubRegionMirror(this, mirror);
    }

    void resetToOriginalValues() {
        SchematicPlacementEventHandler.getInstance().onSubRegionReset(this);
        this.pos = this.defaultPos;
        this.rotation = class_2470.field_11467;
        this.mirror = class_2415.field_11302;
        this.enabled = true;
        this.ignoreEntities = false;
    }

    public boolean isRegionPlacementModifiedFromDefault() {
        return this.isRegionPlacementModified(this.defaultPos);
    }

    public boolean isRegionPlacementModified(class_2338 originalPosition) {
        return !this.isEnabled() || this.ignoreEntities() || this.getMirror() != class_2415.field_11302 || this.getRotation() != class_2470.field_11467 || !this.getPos().equals((Object)originalPosition);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        arr.add((Number)this.pos.method_10263());
        arr.add((Number)this.pos.method_10264());
        arr.add((Number)this.pos.method_10260());
        obj.add("pos", (JsonElement)arr);
        obj.add("name", (JsonElement)new JsonPrimitive(this.getName()));
        obj.add("rotation", (JsonElement)new JsonPrimitive(this.rotation.name()));
        obj.add("mirror", (JsonElement)new JsonPrimitive(this.mirror.name()));
        obj.add("locked_coords", (JsonElement)new JsonPrimitive((Number)this.coordinateLockMask));
        obj.add("enabled", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.enabled)));
        obj.add("rendering_enabled", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.renderingEnabled)));
        obj.add("ignore_entities", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.ignoreEntities)));
        SchematicPlacementEventHandler.getInstance().onSaveSubRegionToJson(this, obj);
        return obj;
    }

    @Nullable
    public static SubRegionPlacement fromJson(JsonObject obj) {
        if (JsonUtils.hasArray((JsonObject)obj, (String)"pos") && JsonUtils.hasString((JsonObject)obj, (String)"name") && JsonUtils.hasString((JsonObject)obj, (String)"rotation") && JsonUtils.hasString((JsonObject)obj, (String)"mirror")) {
            JsonArray posArr = obj.get("pos").getAsJsonArray();
            if (posArr.size() != 3) {
                Litematica.LOGGER.warn("Placement.fromJson(): Failed to load a placement from JSON, invalid position data");
                return null;
            }
            class_2338 pos = new class_2338(posArr.get(0).getAsInt(), posArr.get(1).getAsInt(), posArr.get(2).getAsInt());
            SubRegionPlacement placement = new SubRegionPlacement(pos, obj.get("name").getAsString());
            placement.setEnabled(JsonUtils.getBoolean((JsonObject)obj, (String)"enabled"));
            placement.setRenderingEnabled(JsonUtils.getBoolean((JsonObject)obj, (String)"rendering_enabled"));
            placement.ignoreEntities = JsonUtils.getBoolean((JsonObject)obj, (String)"ignore_entities");
            placement.coordinateLockMask = JsonUtils.getInteger((JsonObject)obj, (String)"locked_coords");
            try {
                class_2470 rotation = class_2470.valueOf((String)obj.get("rotation").getAsString());
                class_2415 mirror = class_2415.valueOf((String)obj.get("mirror").getAsString());
                placement.setRotation(rotation);
                placement.setMirror(mirror);
            }
            catch (Exception e) {
                Litematica.LOGGER.warn("Placement.fromJson(): Invalid rotation or mirror value for a placement");
            }
            SchematicPlacementEventHandler.getInstance().onSubRegionCreateFromJson(placement, pos, placement.getName(), placement.getRotation(), placement.getMirror(), placement.isEnabled(), placement.renderingEnabled, obj);
            return placement;
        }
        return null;
    }

    public static enum RequiredEnabled {
        ANY,
        PLACEMENT_ENABLED,
        RENDERING_ENABLED;

    }
}

