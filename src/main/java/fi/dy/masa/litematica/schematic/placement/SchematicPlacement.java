/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.mojang.serialization.DynamicOps
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.gui.interfaces.IMessageConsumer
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.PositionUtils$CoordinateType
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_2487
 *  net.minecraft.class_2509
 *  net.minecraft.class_2512
 *  net.minecraft.class_2520
 *  net.minecraft.class_3492
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.schematic.placement;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.DynamicOps;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.materials.MaterialListBase;
import fi.dy.masa.litematica.materials.MaterialListPlacement;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementEventHandler;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.schematic.verifier.SchematicVerifier;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.BlockInfoListType;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.gui.interfaces.IMessageConsumer;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_2487;
import net.minecraft.class_2509;
import net.minecraft.class_2512;
import net.minecraft.class_2520;
import net.minecraft.class_3492;
import org.apache.commons.lang3.tuple.Pair;

public class SchematicPlacement {
    private static final Set<Integer> USED_COLORS = new HashSet<Integer>();
    private static int nextColorIndex;
    private final UUID hashId;
    protected final SchematicPlacementManager placementManager;
    private final Map<String, SubRegionPlacement> relativeSubRegionPlacements = new HashMap<String, SubRegionPlacement>();
    private final int subRegionCount;
    private SchematicVerifier verifier;
    private LitematicaSchematic schematic;
    private class_2338 origin;
    private String name;
    private class_2470 rotation = class_2470.field_11467;
    private class_2415 mirror = class_2415.field_11302;
    private BlockInfoListType verifierType = BlockInfoListType.ALL;
    private boolean ignoreEntities;
    private boolean enabled;
    private boolean enableRender;
    private boolean renderEnclosingBox;
    private boolean regionPlacementsModified;
    private boolean locked;
    private boolean shouldBeSaved = true;
    private int coordinateLockMask;
    private int boxesBBColor;
    private Color4f boxesBBColorVec = new Color4f(255.0f, 255.0f, 255.0f);
    @Nullable
    private Box enclosingBox;
    @Nullable
    private File schematicFile;
    @Nullable
    private String selectedSubRegionName;
    @Nullable
    private MaterialListBase materialList;

    public SchematicPlacement(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender) {
        this(schematic, origin, name, enabled, enableRender, DataManager.getSchematicPlacementManager(), null);
    }

    public SchematicPlacement(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender, UUID hash) {
        this(schematic, origin, name, enabled, enableRender, DataManager.getSchematicPlacementManager(), hash);
    }

    public SchematicPlacement(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender, SchematicPlacementManager placementManager) {
        this(schematic, origin, name, enabled, enableRender, placementManager, null);
    }

    public SchematicPlacement(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender, SchematicPlacementManager placementManager, @Nullable UUID hash) {
        this.hashId = hash != null ? hash : UUID.randomUUID();
        this.schematic = schematic;
        this.schematicFile = schematic.getFile();
        this.origin = origin;
        this.name = name;
        this.subRegionCount = schematic.getSubRegionCount();
        this.enabled = enabled;
        this.enableRender = enableRender;
        this.placementManager = placementManager;
        SchematicPlacementEventHandler.getInstance().onPlacementInit(this);
    }

    public static SchematicPlacement createFor(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender) {
        return SchematicPlacement.createFor(schematic, origin, name, enabled, enableRender, null);
    }

    public static SchematicPlacement createFor(LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender, UUID hash) {
        SchematicPlacement placement = new SchematicPlacement(schematic, origin, name, enabled, enableRender, hash);
        placement.setBoxesBBColorNext();
        placement.resetAllSubRegionsToSchematicValues(InfoUtils.INFO_MESSAGE_CONSUMER);
        SchematicPlacementEventHandler.getInstance().onPlacementCreateFor(placement, schematic, origin, name, enabled, enableRender);
        return placement;
    }

    public static SchematicPlacement createForSchematicConversion(LitematicaSchematic schematic, class_2338 origin) {
        Pair<class_2338, class_2338> pair = PositionUtils.getEnclosingAreaCorners(schematic.getAreas().values());
        class_2338 originAdjusted = pair != null ? origin.method_10059((class_2382)pair.getLeft()) : origin;
        return SchematicPlacement.createTemporary(schematic, originAdjusted);
    }

    public static SchematicPlacement createTemporary(LitematicaSchematic schematic, class_2338 origin) {
        return SchematicPlacement.createTemporary(schematic, origin, null);
    }

    public static SchematicPlacement createTemporary(LitematicaSchematic schematic, class_2338 origin, UUID hash) {
        SchematicPlacement placement = new SchematicPlacement(schematic, origin, "?", true, true, hash);
        placement.resetAllSubRegionsToSchematicValues(InfoUtils.INFO_MESSAGE_CONSUMER, false);
        SchematicPlacementEventHandler.getInstance().onPlacementCreateForConversion(placement, schematic, origin);
        return placement;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isRenderingEnabled() {
        return this.isEnabled() && this.enableRender;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean shouldRenderEnclosingBox() {
        return this.renderEnclosingBox;
    }

    public boolean shouldBeSaved() {
        return this.shouldBeSaved;
    }

    public void setShouldBeSaved(boolean shouldbeSaved) {
        this.shouldBeSaved = shouldbeSaved;
    }

    public boolean matchesRequirement(SubRegionPlacement.RequiredEnabled required) {
        switch (required) {
            case ANY: {
                return true;
            }
            case PLACEMENT_ENABLED: {
                return this.isEnabled();
            }
        }
        return this.isEnabled() && this.enableRender;
    }

    public boolean isRegionPlacementModified() {
        return this.regionPlacementsModified;
    }

    public boolean ignoreEntities() {
        return this.ignoreEntities;
    }

    public void toggleIgnoreEntities(IMessageConsumer feedback) {
        this.placementManager.onPrePlacementChange(this);
        this.ignoreEntities = !this.ignoreEntities;
        this.onModified(this.placementManager);
    }

    public void toggleRenderEnclosingBox() {
        boolean bl = this.renderEnclosingBox = !this.renderEnclosingBox;
        if (this.shouldRenderEnclosingBox()) {
            this.updateEnclosingBox();
        }
    }

    public void toggleLocked() {
        this.locked = !this.locked;
        SchematicPlacementEventHandler.getInstance().onToggleLocked(this, this.locked);
    }

    public void setCoordinateLocked(PositionUtils.CoordinateType coord, boolean locked) {
        int mask = 1 << coord.ordinal();
        this.coordinateLockMask = locked ? (this.coordinateLockMask |= mask) : (this.coordinateLockMask &= ~mask);
    }

    public boolean isCoordinateLocked(PositionUtils.CoordinateType coord) {
        int mask = 1 << coord.ordinal();
        return (this.coordinateLockMask & mask) != 0;
    }

    public String getName() {
        return this.name;
    }

    public LitematicaSchematic getSchematic() {
        return this.schematic;
    }

    @Nullable
    public File getSchematicFile() {
        return this.schematicFile;
    }

    @Nullable
    public Box getEclosingBox() {
        return this.enclosingBox;
    }

    public void setName(String name) {
        this.name = name;
        SchematicPlacementEventHandler.getInstance().onSetName(this, name);
    }

    public SchematicPlacement setBoxesBBColor(int color) {
        this.boxesBBColor = color;
        this.boxesBBColorVec = Color4f.fromColor((int)color, (float)1.0f);
        USED_COLORS.add(color);
        return this;
    }

    public UUID getHashId() {
        return this.hashId;
    }

    public class_2338 getOrigin() {
        return this.origin;
    }

    public class_2470 getRotation() {
        return this.rotation;
    }

    public class_2415 getMirror() {
        return this.mirror;
    }

    public Color4f getBoxesBBColor() {
        return this.boxesBBColorVec;
    }

    public int getSubRegionCount() {
        return this.subRegionCount;
    }

    public BlockInfoListType getSchematicVerifierType() {
        return this.verifierType;
    }

    public void setSchematicVerifierType(BlockInfoListType type) {
        this.verifierType = type;
    }

    public MaterialListBase getMaterialList() {
        if (this.materialList == null) {
            this.materialList = new MaterialListPlacement(this, true);
        }
        return this.materialList;
    }

    public boolean hasVerifier() {
        return this.verifier != null;
    }

    public SchematicVerifier getSchematicVerifier() {
        if (this.verifier == null) {
            this.verifier = new SchematicVerifier();
        }
        return this.verifier;
    }

    public class_3492 getPlacementSettings() {
        class_3492 placement = new class_3492();
        placement.method_15125(this.mirror);
        placement.method_15123(this.rotation);
        placement.method_15133(this.ignoreEntities);
        return placement;
    }

    @Nullable
    public String getSelectedSubRegionName() {
        return this.selectedSubRegionName;
    }

    public void setSelectedSubRegionName(@Nullable String name) {
        this.selectedSubRegionName = name;
    }

    @Nullable
    public SubRegionPlacement getSelectedSubRegionPlacement() {
        return this.selectedSubRegionName != null ? this.relativeSubRegionPlacements.get(this.selectedSubRegionName) : null;
    }

    @Nullable
    public SubRegionPlacement getRelativeSubRegionPlacement(String areaName) {
        return this.relativeSubRegionPlacements.get(areaName);
    }

    public Collection<SubRegionPlacement> getAllSubRegionsPlacements() {
        return this.relativeSubRegionPlacements.values();
    }

    public ImmutableMap<String, SubRegionPlacement> getEnabledRelativeSubRegionPlacements() {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        for (Map.Entry<String, SubRegionPlacement> entry : this.relativeSubRegionPlacements.entrySet()) {
            SubRegionPlacement placement = entry.getValue();
            if (!placement.matchesRequirement(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED)) continue;
            builder.put((Object)entry.getKey(), (Object)entry.getValue());
        }
        return builder.build();
    }

    private void updateEnclosingBox() {
        if (this.shouldRenderEnclosingBox()) {
            ImmutableMap<String, Box> boxes = this.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.ANY);
            class_2338 pos1 = null;
            class_2338 pos2 = null;
            for (Box box : boxes.values()) {
                class_2338 tmp = PositionUtils.getMinCorner(box.getPos1(), box.getPos2());
                if (pos1 == null) {
                    pos1 = tmp;
                } else if (tmp.method_10263() < pos1.method_10263() || tmp.method_10264() < pos1.method_10264() || tmp.method_10260() < pos1.method_10260()) {
                    pos1 = PositionUtils.getMinCorner(tmp, pos1);
                }
                tmp = PositionUtils.getMaxCorner(box.getPos1(), box.getPos2());
                if (pos2 == null) {
                    pos2 = tmp;
                    continue;
                }
                if (tmp.method_10263() <= pos2.method_10263() && tmp.method_10264() <= pos2.method_10264() && tmp.method_10260() <= pos2.method_10260()) continue;
                pos2 = PositionUtils.getMaxCorner(tmp, pos2);
            }
            if (pos1 != null && pos2 != null) {
                this.enclosingBox = new Box(pos1, pos2, "Enclosing Box");
            }
        }
    }

    public ImmutableMap<String, Box> getSubRegionBoxes(SubRegionPlacement.RequiredEnabled required) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        Map<String, class_2338> areaSizes = this.schematic.getAreaSizes();
        for (Map.Entry<String, SubRegionPlacement> entry : this.relativeSubRegionPlacements.entrySet()) {
            String name = entry.getKey();
            class_2338 areaSize = areaSizes.get(name);
            if (areaSize == null) {
                Litematica.LOGGER.warn("SchematicPlacement.getSubRegionBoxes(): Size for sub-region '{}' not found in the schematic '{}'", (Object)name, (Object)this.schematic.getMetadata().getName());
                continue;
            }
            SubRegionPlacement placement = entry.getValue();
            if (!placement.matchesRequirement(required)) continue;
            class_2338 boxOriginRelative = placement.getPos();
            class_2338 boxOriginAbsolute = PositionUtils.getTransformedBlockPos(boxOriginRelative, this.mirror, this.rotation).method_10081((class_2382)this.origin);
            class_2338 pos2 = PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)areaSize);
            pos2 = PositionUtils.getTransformedBlockPos(pos2, this.mirror, this.rotation);
            pos2 = PositionUtils.getTransformedBlockPos(pos2, placement.getMirror(), placement.getRotation()).method_10081((class_2382)boxOriginAbsolute);
            builder.put((Object)name, (Object)new Box(boxOriginAbsolute, pos2, name));
        }
        return builder.build();
    }

    public ImmutableMap<String, Box> getSubRegionBoxFor(String regionName, SubRegionPlacement.RequiredEnabled required) {
        ImmutableMap.Builder builder = ImmutableMap.builder();
        Map<String, class_2338> areaSizes = this.schematic.getAreaSizes();
        SubRegionPlacement placement = this.relativeSubRegionPlacements.get(regionName);
        if (placement != null && placement.matchesRequirement(required)) {
            class_2338 areaSize = areaSizes.get(regionName);
            if (areaSize != null) {
                class_2338 boxOriginRelative = placement.getPos();
                class_2338 boxOriginAbsolute = PositionUtils.getTransformedBlockPos(boxOriginRelative, this.mirror, this.rotation).method_10081((class_2382)this.origin);
                class_2338 pos2 = PositionUtils.getRelativeEndPositionFromAreaSize((class_2382)areaSize);
                pos2 = PositionUtils.getTransformedBlockPos(pos2, this.mirror, this.rotation);
                pos2 = PositionUtils.getTransformedBlockPos(pos2, placement.getMirror(), placement.getRotation()).method_10081((class_2382)boxOriginAbsolute);
                builder.put((Object)regionName, (Object)new Box(boxOriginAbsolute, pos2, regionName));
            } else {
                Litematica.LOGGER.warn("SchematicPlacement.getSubRegionBoxFor(): Size for sub-region '{}' not found in the schematic '{}'", (Object)regionName, (Object)this.schematic.getMetadata().getName());
            }
        }
        return builder.build();
    }

    public Set<String> getRegionsTouchingChunk(int chunkX, int chunkZ) {
        ImmutableMap<String, Box> map = this.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED);
        int chunkXMin = chunkX << 4;
        int chunkZMin = chunkZ << 4;
        int chunkXMax = chunkXMin + 15;
        int chunkZMax = chunkZMin + 15;
        HashSet<String> set = new HashSet<String>();
        for (Map.Entry entry : map.entrySet()) {
            Box box = (Box)entry.getValue();
            int boxXMin = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxZMin = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxXMax = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxZMax = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
            boolean notOverlapping = boxXMin > chunkXMax || boxZMin > chunkZMax || boxXMax < chunkXMin || boxZMax < chunkZMin;
            if (notOverlapping) continue;
            set.add((String)entry.getKey());
        }
        return set;
    }

    public ImmutableMap<String, IntBoundingBox> getBoxesWithinChunk(int chunkX, int chunkZ) {
        ImmutableMap<String, Box> subRegions = this.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED);
        return PositionUtils.getBoxesWithinChunk(chunkX, chunkZ, subRegions);
    }

    @Nullable
    public IntBoundingBox getBoxWithinChunkForRegion(String regionName, int chunkX, int chunkZ) {
        Box box = (Box)this.getSubRegionBoxFor(regionName, SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED).get((Object)regionName);
        return box != null ? PositionUtils.getBoundsWithinChunkForBox(box, chunkX, chunkZ) : null;
    }

    public Set<class_1923> getTouchedChunks() {
        return PositionUtils.getTouchedChunks(this.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED));
    }

    public Set<class_1923> getTouchedChunksForRegion(String regionName) {
        return PositionUtils.getTouchedChunks(this.getSubRegionBoxFor(regionName, SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED));
    }

    private void checkAreSubRegionsModified() {
        Map<String, class_2338> areaPositions = this.schematic.getAreaPositions();
        if (areaPositions.size() != this.relativeSubRegionPlacements.size()) {
            this.regionPlacementsModified = true;
            return;
        }
        for (Map.Entry<String, class_2338> entry : areaPositions.entrySet()) {
            SubRegionPlacement placement = this.relativeSubRegionPlacements.get(entry.getKey());
            if (placement != null && !placement.isRegionPlacementModified(entry.getValue())) continue;
            this.regionPlacementsModified = true;
            return;
        }
        this.regionPlacementsModified = false;
    }

    public void moveSubRegionTo(String regionName, class_2338 newPos, IStringConsumer feedback) {
        if (this.isLocked()) {
            feedback.setString("litematica.message.placement.cant_modify_is_locked");
            return;
        }
        if (this.relativeSubRegionPlacements.containsKey(regionName)) {
            this.placementManager.onPrePlacementChange(this);
            newPos = newPos.method_10059((class_2382)this.origin);
            newPos = PositionUtils.getReverseTransformedBlockPos(newPos, this.mirror, this.rotation);
            this.relativeSubRegionPlacements.get(regionName).setPos(newPos);
            this.onModified(regionName, this.placementManager);
        }
    }

    public void setSubRegionRotation(String regionName, class_2470 rotation, IMessageConsumer feedback) {
        if (this.isLocked()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.message.placement.cant_modify_is_locked", new Object[0]);
            return;
        }
        if (this.relativeSubRegionPlacements.containsKey(regionName)) {
            this.placementManager.onPrePlacementChange(this);
            SubRegionPlacement placement = this.relativeSubRegionPlacements.get(regionName);
            placement.setRotation(rotation);
            this.onModified(regionName, this.placementManager);
        }
    }

    public void setSubRegionMirror(String regionName, class_2415 mirror, IMessageConsumer feedback) {
        if (this.isLocked()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.message.placement.cant_modify_is_locked", new Object[0]);
            return;
        }
        if (this.relativeSubRegionPlacements.containsKey(regionName)) {
            this.placementManager.onPrePlacementChange(this);
            this.relativeSubRegionPlacements.get(regionName).setMirror(mirror);
            this.onModified(regionName, this.placementManager);
        }
    }

    public void setSubRegionsEnabledState(boolean state, Collection<SubRegionPlacement> subRegions, IMessageConsumer feedback) {
        this.placementManager.onPrePlacementChange(this);
        for (SubRegionPlacement placement : subRegions) {
            if ((placement = this.relativeSubRegionPlacements.get(placement.getName())) == null || placement.isEnabled() == state) continue;
            placement.setEnabled(state);
        }
        this.checkAreSubRegionsModified();
        this.onModified(this.placementManager);
    }

    public void toggleSubRegionEnabled(String regionName, IMessageConsumer feedback) {
        if (this.relativeSubRegionPlacements.containsKey(regionName)) {
            this.placementManager.onPrePlacementChange(this);
            this.relativeSubRegionPlacements.get(regionName).toggleEnabled();
            this.onModified(regionName, this.placementManager);
        }
    }

    public void toggleSubRegionIgnoreEntities(String regionName, IMessageConsumer feedback) {
        if (this.relativeSubRegionPlacements.containsKey(regionName)) {
            this.placementManager.onPrePlacementChange(this);
            this.relativeSubRegionPlacements.get(regionName).toggleIgnoreEntities();
            this.onModified(regionName, this.placementManager);
        }
    }

    public void resetAllSubRegionsToSchematicValues(IStringConsumer feedback) {
        this.resetAllSubRegionsToSchematicValues(feedback, true);
    }

    public void resetAllSubRegionsToSchematicValues(IStringConsumer feedback, boolean updatePlacementManager) {
        if (this.isLocked()) {
            feedback.setString("litematica.message.placement.cant_modify_is_locked");
            return;
        }
        if (updatePlacementManager) {
            this.placementManager.onPrePlacementChange(this);
        }
        SchematicPlacementEventHandler.getInstance().onPlacementReset(this);
        Map<String, class_2338> areaPositions = this.schematic.getAreaPositions();
        this.relativeSubRegionPlacements.clear();
        this.regionPlacementsModified = false;
        for (Map.Entry<String, class_2338> entry : areaPositions.entrySet()) {
            String name = entry.getKey();
            this.relativeSubRegionPlacements.put(name, new SubRegionPlacement(entry.getValue(), name));
        }
        if (updatePlacementManager) {
            this.onModified(this.placementManager);
        }
    }

    public void resetSubRegionToSchematicValues(String regionName, IMessageConsumer feedback) {
        if (this.isLocked()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.message.placement.cant_modify_is_locked", new Object[0]);
            return;
        }
        class_2338 pos = this.schematic.getSubRegionPosition(regionName);
        SubRegionPlacement placement = this.relativeSubRegionPlacements.get(regionName);
        if (pos != null && placement != null) {
            this.placementManager.onPrePlacementChange(this);
            placement.resetToOriginalValues();
            this.onModified(regionName, this.placementManager);
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.placementManager.onPrePlacementChange(this);
            this.enabled = enabled;
            SchematicPlacementEventHandler.getInstance().onSetEnabled(this, enabled);
            this.onModified(this.placementManager);
        }
    }

    public void toggleEnabled() {
        this.setEnabled(!this.enabled);
    }

    public void setRenderSchematic(boolean render) {
        if (render != this.enableRender) {
            this.placementManager.onPrePlacementChange(this);
            this.enableRender = render;
            SchematicPlacementEventHandler.getInstance().onSetRender(this, render);
            this.onModified(this.placementManager);
        }
    }

    public void toggleSubRegionRenderingEnabled(String regionName) {
        SubRegionPlacement placement = this.relativeSubRegionPlacements.get(regionName);
        if (placement != null) {
            placement.toggleRenderingEnabled();
        }
    }

    public SchematicPlacement setOrigin(class_2338 origin, IStringConsumer feedback) {
        if (this.isLocked()) {
            feedback.setString("litematica.message.placement.cant_modify_is_locked");
            return this;
        }
        if (!this.origin.equals((Object)(origin = PositionUtils.getModifiedPartiallyLockedPosition(this.origin, origin, this.coordinateLockMask)))) {
            this.placementManager.onPrePlacementChange(this);
            this.origin = origin;
            SchematicPlacementEventHandler.getInstance().onSetOrigin(this, origin);
            this.onModified(this.placementManager);
        }
        return this;
    }

    public SchematicPlacement setRotation(class_2470 rotation, IMessageConsumer feedback) {
        if (this.isLocked()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.message.placement.cant_modify_is_locked", new Object[0]);
            return this;
        }
        if (this.rotation != rotation) {
            this.placementManager.onPrePlacementChange(this);
            this.rotation = rotation;
            SchematicPlacementEventHandler.getInstance().onSetRotation(this, rotation);
            this.onModified(this.placementManager);
        }
        return this;
    }

    public SchematicPlacement setMirror(class_2415 mirror, IMessageConsumer feedback) {
        if (this.isLocked()) {
            feedback.addMessage(Message.MessageType.ERROR, "litematica.message.placement.cant_modify_is_locked", new Object[0]);
            return this;
        }
        if (this.mirror != mirror) {
            this.placementManager.onPrePlacementChange(this);
            this.mirror = mirror;
            SchematicPlacementEventHandler.getInstance().onSetMirror(this, mirror);
            this.onModified(this.placementManager);
        }
        return this;
    }

    private SchematicPlacement setBoxesBBColorNext() {
        return this.setBoxesBBColor(SchematicPlacement.getNextBoxColor());
    }

    protected void onModified(SchematicPlacementManager manager) {
        this.updateEnclosingBox();
        manager.onPostPlacementChange(this);
        OverlayRenderer.getInstance().updatePlacementCache();
    }

    protected void onModified(String regionName, SchematicPlacementManager manager) {
        this.checkAreSubRegionsModified();
        this.updateEnclosingBox();
        manager.onPostPlacementChange(this);
        OverlayRenderer.getInstance().updatePlacementCache();
    }

    public void onRemoved() {
        SchematicPlacementEventHandler.getInstance().onPlacementRemoved(this);
        USED_COLORS.remove(this.boxesBBColor);
        if (USED_COLORS.isEmpty()) {
            nextColorIndex = 0;
        }
    }

    @Nullable
    public JsonObject toJson() {
        if (this.schematic.getFile() != null) {
            JsonObject obj = new JsonObject();
            JsonArray arr = new JsonArray();
            arr.add((Number)this.origin.method_10263());
            arr.add((Number)this.origin.method_10264());
            arr.add((Number)this.origin.method_10260());
            obj.add("schematic", (JsonElement)new JsonPrimitive(this.schematic.getFile().getAbsolutePath()));
            obj.add("name", (JsonElement)new JsonPrimitive(this.name));
            obj.add("origin", (JsonElement)arr);
            obj.add("rotation", (JsonElement)new JsonPrimitive(this.rotation.name()));
            obj.add("mirror", (JsonElement)new JsonPrimitive(this.mirror.name()));
            obj.add("ignore_entities", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.ignoreEntities())));
            obj.add("enabled", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.isEnabled())));
            obj.add("enable_render", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.enableRender)));
            obj.add("render_enclosing_box", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.shouldRenderEnclosingBox())));
            obj.add("locked", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.isLocked())));
            obj.add("locked_coords", (JsonElement)new JsonPrimitive((Number)this.coordinateLockMask));
            obj.add("bb_color", (JsonElement)new JsonPrimitive((Number)this.boxesBBColor));
            obj.add("verifier_type", (JsonElement)new JsonPrimitive(this.verifierType.getStringValue()));
            obj.add("hash_code", (JsonElement)new JsonPrimitive(this.hashId.toString()));
            if (this.selectedSubRegionName != null) {
                obj.add("selected_region", (JsonElement)new JsonPrimitive(this.selectedSubRegionName));
            }
            if (this.materialList != null) {
                obj.add("material_list", (JsonElement)this.materialList.toJson());
            }
            if (!this.relativeSubRegionPlacements.isEmpty()) {
                arr = new JsonArray();
                for (Map.Entry<String, SubRegionPlacement> entry : this.relativeSubRegionPlacements.entrySet()) {
                    JsonObject placementObj = new JsonObject();
                    placementObj.add("name", (JsonElement)new JsonPrimitive(entry.getKey()));
                    placementObj.add("placement", (JsonElement)entry.getValue().toJson());
                    arr.add((JsonElement)placementObj);
                }
                obj.add("placements", (JsonElement)arr);
            }
            SchematicPlacementEventHandler.getInstance().onSavePlacementToJson(this, obj);
            return obj;
        }
        return null;
    }

    @Nullable
    public static SchematicPlacement fromJson(JsonObject obj) {
        if (JsonUtils.hasString((JsonObject)obj, (String)"schematic") && JsonUtils.hasString((JsonObject)obj, (String)"name") && JsonUtils.hasArray((JsonObject)obj, (String)"origin") && JsonUtils.hasString((JsonObject)obj, (String)"rotation") && JsonUtils.hasString((JsonObject)obj, (String)"mirror") && JsonUtils.hasArray((JsonObject)obj, (String)"placements")) {
            File file = new File(obj.get("schematic").getAsString());
            LitematicaSchematic schematic = SchematicHolder.getInstance().getOrLoad(file);
            if (schematic == null) {
                Litematica.LOGGER.warn("Failed to load schematic '{}'", (Object)file.getAbsolutePath());
                return null;
            }
            JsonArray posArr = obj.get("origin").getAsJsonArray();
            if (posArr.size() != 3) {
                Litematica.LOGGER.warn("Failed to load schematic placement for '{}', invalid origin position", (Object)file.getAbsolutePath());
                return null;
            }
            UUID hashCode = JsonUtils.hasString((JsonObject)obj, (String)"hash_code") ? UUID.fromString(JsonUtils.getString((JsonObject)obj, (String)"hash_code")) : null;
            String name = obj.get("name").getAsString();
            class_2338 pos = new class_2338(posArr.get(0).getAsInt(), posArr.get(1).getAsInt(), posArr.get(2).getAsInt());
            class_2470 rotation = class_2470.valueOf((String)obj.get("rotation").getAsString());
            class_2415 mirror = class_2415.valueOf((String)obj.get("mirror").getAsString());
            boolean enabled = JsonUtils.getBoolean((JsonObject)obj, (String)"enabled");
            boolean enableRender = JsonUtils.getBoolean((JsonObject)obj, (String)"enable_render");
            SchematicPlacement schematicPlacement = new SchematicPlacement(schematic, pos, name, enabled, enableRender, hashCode);
            schematicPlacement.rotation = rotation;
            schematicPlacement.mirror = mirror;
            schematicPlacement.ignoreEntities = JsonUtils.getBoolean((JsonObject)obj, (String)"ignore_entities");
            schematicPlacement.renderEnclosingBox = JsonUtils.getBoolean((JsonObject)obj, (String)"render_enclosing_box");
            schematicPlacement.locked = JsonUtils.getBoolean((JsonObject)obj, (String)"locked");
            schematicPlacement.coordinateLockMask = JsonUtils.getInteger((JsonObject)obj, (String)"locked_coords");
            if (JsonUtils.hasInteger((JsonObject)obj, (String)"bb_color")) {
                schematicPlacement.setBoxesBBColor(JsonUtils.getInteger((JsonObject)obj, (String)"bb_color"));
            } else {
                schematicPlacement.setBoxesBBColorNext();
            }
            if (JsonUtils.hasObject((JsonObject)obj, (String)"material_list")) {
                schematicPlacement.materialList = new MaterialListPlacement(schematicPlacement);
                schematicPlacement.materialList.fromJson(JsonUtils.getNestedObject((JsonObject)obj, (String)"material_list", (boolean)false));
            }
            if (JsonUtils.hasString((JsonObject)obj, (String)"verifier_type")) {
                schematicPlacement.verifierType = BlockInfoListType.fromStringStatic(JsonUtils.getString((JsonObject)obj, (String)"verifier_type"));
            }
            if (JsonUtils.hasString((JsonObject)obj, (String)"selected_region")) {
                schematicPlacement.selectedSubRegionName = JsonUtils.getString((JsonObject)obj, (String)"selected_region");
            }
            JsonArray placementArr = obj.get("placements").getAsJsonArray();
            for (int i = 0; i < placementArr.size(); ++i) {
                SubRegionPlacement placement;
                JsonObject placementObj;
                JsonElement el = placementArr.get(i);
                if (!el.isJsonObject() || !JsonUtils.hasString((JsonObject)(placementObj = el.getAsJsonObject()), (String)"name") || !JsonUtils.hasObject((JsonObject)placementObj, (String)"placement") || (placement = SubRegionPlacement.fromJson(placementObj.get("placement").getAsJsonObject())) == null) continue;
                String placementName = placementObj.get("name").getAsString();
                schematicPlacement.relativeSubRegionPlacements.put(placementName, placement);
            }
            SchematicPlacementEventHandler.getInstance().onPlacementCreateFromJson(schematicPlacement, schematic, pos, name, rotation, mirror, enabled, enableRender, obj);
            schematicPlacement.checkAreSubRegionsModified();
            schematicPlacement.updateEnclosingBox();
            return schematicPlacement;
        }
        return null;
    }

    private static int getNextBoxColor() {
        int length = OverlayRenderer.KELLY_COLORS.length;
        int color = OverlayRenderer.KELLY_COLORS[nextColorIndex];
        nextColorIndex = (nextColorIndex + 1) % length;
        for (int i = 0; i < length; ++i) {
            if (!USED_COLORS.contains(color)) {
                return color;
            }
            color = OverlayRenderer.KELLY_COLORS[nextColorIndex];
            nextColorIndex = (nextColorIndex + 1) % length;
        }
        return color;
    }

    public class_2487 toNbt(boolean withSchematic) {
        class_2487 compound = new class_2487();
        compound.method_10582("Name", this.name);
        compound.method_10582("HashCode", this.hashId.toString());
        if (withSchematic) {
            compound.method_10566("Schematics", (class_2520)this.schematic.writeToNBT());
        }
        compound.method_10566("Origin", class_2512.method_10692((class_2338)this.origin));
        compound.method_10569("Rotation", this.rotation.ordinal());
        compound.method_10569("Mirror", this.mirror.ordinal());
        class_2487 subs = new class_2487();
        for (String name : this.relativeSubRegionPlacements.keySet()) {
            class_2487 sub = new class_2487();
            SubRegionPlacement subRegionPlacement = this.relativeSubRegionPlacements.get(name);
            subs.method_10566(name, (class_2520)sub);
            sub.method_10566("Pos", class_2512.method_10692((class_2338)subRegionPlacement.getPos()));
            sub.method_10569("Rotation", subRegionPlacement.getRotation().ordinal());
            sub.method_10569("Mirror", subRegionPlacement.getMirror().ordinal());
            sub.method_10582("Name", subRegionPlacement.getName());
            sub.method_10556("Enabled", subRegionPlacement.isEnabled());
            sub.method_10556("IgnoreEntities", subRegionPlacement.ignoreEntities());
        }
        compound.method_10566("SubRegions", (class_2520)subs);
        compound.method_10582("ReplaceMode", Configs.Generic.PASTE_REPLACE_BEHAVIOR.getStringValue());
        compound.method_10582("PasteLayerBehavior", Configs.Generic.PASTE_LAYER_BEHAVIOR.getStringValue());
        LayerRange.CODEC.encodeStart((DynamicOps)class_2509.field_11560, (Object)DataManager.getRenderLayerRange()).resultOrPartial().ifPresent(range -> compound.method_10566("RenderLayerRange", range));
        SchematicPlacementEventHandler.getInstance().onSavePlacementToNbt(this, compound);
        return compound;
    }

    @Nullable
    public static SchematicPlacement createFromNbt(class_2487 nbt) {
        String name = nbt.method_10558("Name");
        UUID hashCode = nbt.method_10545("HashCode") ? UUID.fromString(nbt.method_10558("HashCode")) : null;
        LitematicaSchematic schematic = new LitematicaSchematic(Path.of(name, new String[0]), nbt.method_10562("Schematics"), FileType.LITEMATICA_SCHEMATIC);
        class_2338 origin = NbtUtils.readBlockPosFromArrayTag((class_2487)nbt, (String)"Origin");
        class_2470 rot = class_2470.values()[nbt.method_10550("Rotation")];
        class_2415 mirror = class_2415.values()[nbt.method_10550("Mirror")];
        SchematicPlacement placement = SchematicPlacement.createFor(schematic, origin, name, true, true, hashCode);
        placement.rotation = rot;
        placement.mirror = mirror;
        class_2487 subs = nbt.method_10562("SubRegions");
        for (String key : subs.method_10541()) {
            class_2487 entry = subs.method_10562(key);
            if (entry.method_33133()) continue;
            name = entry.method_10558("Name");
            origin = NbtUtils.readBlockPosFromArrayTag((class_2487)entry, (String)"Pos");
            rot = class_2470.values()[entry.method_10550("Rotation")];
            mirror = class_2415.values()[entry.method_10550("Mirror")];
            boolean enabled = entry.method_10577("Enabled");
            boolean ignore = entry.method_10577("IgnoreEntities");
            SubRegionPlacement subRegion = new SubRegionPlacement(origin, name);
            subRegion.setMirror(mirror);
            subRegion.setRotation(rot);
            subRegion.setEnabled(enabled);
            if (ignore) {
                subRegion.toggleIgnoreEntities();
            }
            placement.relativeSubRegionPlacements.put(key, subRegion);
        }
        SchematicPlacementEventHandler.getInstance().onPlacementCreateFromNbt(placement, schematic, origin, name, rot, mirror, placement.enabled, placement.enableRender, nbt);
        placement.checkAreSubRegionsModified();
        placement.updateEnclosingBox();
        return placement;
    }

    @Nullable
    public static SchematicPlacement createFromNbt(@Nonnull LitematicaSchematic schematic, class_2487 nbt) {
        String name = nbt.method_10558("Name");
        UUID hashCode = nbt.method_10545("HashCode") ? UUID.fromString(nbt.method_10558("HashCode")) : null;
        class_2338 origin = NbtUtils.readBlockPosFromArrayTag((class_2487)nbt, (String)"Origin");
        class_2470 rot = class_2470.values()[nbt.method_10550("Rotation")];
        class_2415 mirror = class_2415.values()[nbt.method_10550("Mirror")];
        SchematicPlacement placement = SchematicPlacement.createFor(schematic, origin, name, true, true, hashCode);
        placement.rotation = rot;
        placement.mirror = mirror;
        class_2487 subs = nbt.method_10562("SubRegions");
        for (String key : subs.method_10541()) {
            class_2487 entry = subs.method_10562(key);
            if (entry.method_33133()) continue;
            name = entry.method_10558("Name");
            origin = NbtUtils.readBlockPosFromArrayTag((class_2487)entry, (String)"Pos");
            rot = class_2470.values()[entry.method_10550("Rotation")];
            mirror = class_2415.values()[entry.method_10550("Mirror")];
            boolean enabled = entry.method_10577("Enabled");
            boolean ignore = entry.method_10577("IgnoreEntities");
            SubRegionPlacement subRegion = new SubRegionPlacement(origin, name);
            subRegion.setMirror(mirror);
            subRegion.setRotation(rot);
            subRegion.setEnabled(enabled);
            if (ignore) {
                subRegion.toggleIgnoreEntities();
            }
            placement.relativeSubRegionPlacements.put(key, subRegion);
        }
        SchematicPlacementEventHandler.getInstance().onPlacementCreateFromNbt(placement, schematic, origin, name, rot, mirror, placement.enabled, placement.enableRender, nbt);
        placement.checkAreSubRegionsModified();
        placement.updateEnclosingBox();
        return placement;
    }
}

