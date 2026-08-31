/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.ImmutableMap
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  fi.dy.masa.malilib.config.options.ConfigHotkey
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.GuiConfirmAction
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.IConfirmationListener
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.JsonUtils
 *  fi.dy.masa.malilib.util.LayerMode
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.StringUtils
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.longs.LongIterator
 *  it.unimi.dsi.fastutil.longs.LongOpenHashSet
 *  javax.annotation.Nullable
 *  net.minecraft.class_1297
 *  net.minecraft.class_156
 *  net.minecraft.class_1657
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_2487
 *  net.minecraft.class_310
 *  net.minecraft.class_3965
 *  net.minecraft.class_437
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 */
package fi.dy.masa.litematica.schematic.placement;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.network.ServuxLitematicaHandler;
import fi.dy.masa.litematica.network.ServuxLitematicaPacket;
import fi.dy.masa.litematica.render.LitematicaRenderer;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.render.infohud.StatusInfoRenderer;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkCommand;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkDirect;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicSetblockToMcfunction;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementEventHandler;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PasteLayerBehavior;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.util.ReplaceBehavior;
import fi.dy.masa.litematica.util.SchematicPlacingUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfirmAction;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.IConfirmationListener;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.LayerMode;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.StringUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_156;
import net.minecraft.class_1657;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_2487;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_5819;
import net.minecraft.class_638;

public class SchematicPlacementManager {
    protected final List<SchematicPlacement> schematicPlacements = new ArrayList<SchematicPlacement>();
    protected final ArrayListMultimap<class_1923, SchematicPlacement> schematicsTouchingChunk = ArrayListMultimap.create();
    protected final Long2ObjectOpenHashMap<List<PlacementPart>> touchedVolumesInChunk = new Long2ObjectOpenHashMap();
    protected final Set<class_1923> chunksToRebuild = new HashSet<class_1923>();
    protected final Set<class_1923> chunkRebuildQueue = new HashSet<class_1923>();
    protected final LongOpenHashSet chunksToUnload = new LongOpenHashSet();
    protected final Set<class_1923> chunksPreChange = new HashSet<class_1923>();
    protected final List<class_1923> visibleChunks = new ArrayList<class_1923>();
    protected final Supplier<WorldSchematic> worldSupplier;
    protected class_1923 lastVisibleChunksSortPos = new class_1923(0, 0);
    protected boolean visibleChunksNeedsUpdate;
    @Nullable
    private SchematicPlacement selectedPlacement;

    public SchematicPlacementManager() {
        this(SchematicWorldHandler::getSchematicWorld);
    }

    protected SchematicPlacementManager(Supplier<WorldSchematic> worldSupplier) {
        this.worldSupplier = worldSupplier;
    }

    public boolean hasPendingRebuilds() {
        return !this.chunksToRebuild.isEmpty();
    }

    public boolean hasPendingRebuildFor(class_1923 pos) {
        return this.chunksToRebuild.contains(pos);
    }

    public void setVisibleSubChunksNeedsUpdate() {
        this.visibleChunksNeedsUpdate = true;
    }

    protected boolean hasTimeToExecuteMoreTasks() {
        return System.nanoTime() - DataManager.getClientTickStartTime() <= 50000000L;
    }

    protected boolean canHandleChunk(class_638 clientWorld, int chunkX, int chunkZ) {
        return Configs.Generic.LOAD_ENTIRE_SCHEMATICS.getBooleanValue() || WorldUtils.isClientChunkLoaded(clientWorld, chunkX, chunkZ);
    }

    public boolean hasQueuedChunks() {
        return !this.chunkRebuildQueue.isEmpty();
    }

    public void processQueuedChunks() {
        if (!this.chunksToUnload.isEmpty()) {
            WorldSchematic worldSchematic = this.worldSupplier.get();
            if (worldSchematic != null) {
                LongIterator longIterator = this.chunksToUnload.iterator();
                while (longIterator.hasNext()) {
                    long posLong = (Long)longIterator.next();
                    this.unloadSchematicChunk(worldSchematic, class_1923.method_8325((long)posLong), class_1923.method_8332((long)posLong));
                }
            }
            this.chunksToUnload.clear();
        }
        if (this.hasQueuedChunks()) {
            class_638 worldClient = class_310.method_1551().field_1687;
            if (worldClient == null) {
                this.chunksToRebuild.clear();
                this.chunkRebuildQueue.clear();
                return;
            }
            WorldSchematic worldSchematic = this.worldSupplier.get();
            Iterator<class_1923> queueIterator = this.chunkRebuildQueue.iterator();
            while (queueIterator.hasNext() && this.hasTimeToExecuteMoreTasks()) {
                class_1923 pos = queueIterator.next();
                if (!this.schematicsTouchingChunk.containsKey((Object)pos)) {
                    queueIterator.remove();
                    this.chunksToRebuild.remove(pos);
                    continue;
                }
                if (this.canHandleChunk(worldClient, pos.field_9181, pos.field_9180)) {
                    this.unloadSchematicChunk(worldSchematic, pos.field_9181, pos.field_9180);
                    worldSchematic.getChunkProvider().loadChunk(pos.field_9181, pos.field_9180);
                    this.visibleChunksNeedsUpdate = true;
                }
                if (worldSchematic.getChunkProvider().method_12123(pos.field_9181, pos.field_9180)) {
                    List placements = this.schematicsTouchingChunk.get((Object)pos);
                    if (!placements.isEmpty()) {
                        ReplaceBehavior behavior = (ReplaceBehavior)Configs.Generic.PLACEMENT_REPLACE_BEHAVIOR.getOptionListValue();
                        PasteLayerBehavior layers = (PasteLayerBehavior)Configs.Generic.PASTE_LAYER_BEHAVIOR.getOptionListValue();
                        for (SchematicPlacement placement : placements) {
                            if (!placement.isEnabled()) continue;
                            SchematicPlacingUtils.placeToWorldWithinChunk(worldSchematic, pos, placement, behavior, layers, false);
                        }
                        worldSchematic.scheduleChunkRenders(pos.field_9181, pos.field_9180);
                    }
                    this.chunksToRebuild.remove(pos);
                }
                queueIterator.remove();
            }
            LitematicaRenderer.getInstance().getWorldRenderer().markNeedsUpdate();
        }
    }

    public void onClientChunkLoad(int chunkX, int chunkZ) {
        class_1923 pos = new class_1923(chunkX, chunkZ);
        this.chunkRebuildQueue.add(pos);
    }

    public void onClientChunkUnload(int chunkX, int chunkZ) {
        if (!Configs.Generic.LOAD_ENTIRE_SCHEMATICS.getBooleanValue()) {
            this.chunksToUnload.add(class_1923.method_8331((int)chunkX, (int)chunkZ));
        }
    }

    protected void unloadSchematicChunk(WorldSchematic worldSchematic, int chunkX, int chunkZ) {
        if (worldSchematic.getChunkProvider().method_12123(chunkX, chunkZ)) {
            worldSchematic.getChunkProvider().unloadChunk(chunkX, chunkZ);
            worldSchematic.scheduleChunkRenders(chunkX, chunkZ);
            this.visibleChunksNeedsUpdate = true;
        }
    }

    public int getLastVisibleChunksCount() {
        return this.visibleChunks.size();
    }

    public List<class_1923> getAndUpdateVisibleChunks(class_1923 viewChunk) {
        if (this.visibleChunksNeedsUpdate) {
            this.visibleChunks.clear();
            WorldSchematic worldSchematic = this.worldSupplier.get();
            LayerRange range = DataManager.getRenderLayerRange();
            if (worldSchematic != null) {
                int minY = worldSchematic.method_31607();
                int maxY = worldSchematic.method_31600() - 1;
                LongIterator longIterator = worldSchematic.getChunkManager().getLoadedChunks().keySet().iterator();
                while (longIterator.hasNext()) {
                    int maxZ;
                    int maxX;
                    int minZ;
                    long posLong = (Long)longIterator.next();
                    int minX = class_1923.method_8325((long)posLong) << 4;
                    if (!range.intersectsBox(minX, minY, minZ = class_1923.method_8332((long)posLong) << 4, maxX = minX + 15, maxY, maxZ = minZ + 15)) continue;
                    this.visibleChunks.add(new class_1923(posLong));
                }
                this.visibleChunks.sort(new PositionUtils.ChunkPosDistanceComparator(viewChunk));
                this.lastVisibleChunksSortPos = viewChunk;
            }
            this.visibleChunksNeedsUpdate = false;
        } else if (!viewChunk.equals((Object)this.lastVisibleChunksSortPos)) {
            this.visibleChunks.sort(new PositionUtils.ChunkPosDistanceComparator(viewChunk));
            this.lastVisibleChunksSortPos = viewChunk;
        }
        return this.visibleChunks;
    }

    public List<SchematicPlacement> getAllSchematicsPlacements() {
        return this.schematicPlacements;
    }

    public List<PlacementPart> getPlacementPartsInChunk(int chunkX, int chunkZ) {
        return (List)this.touchedVolumesInChunk.getOrDefault(class_1923.method_8331((int)chunkX, (int)chunkZ), Collections.emptyList());
    }

    public List<PlacementPart> getAllPlacementsTouchingChunk(class_2338 pos) {
        return (List)this.touchedVolumesInChunk.getOrDefault(class_1923.method_8331((int)(pos.method_10263() >> 4), (int)(pos.method_10260() >> 4)), Collections.emptyList());
    }

    public int getTouchedChunksCount() {
        return this.touchedVolumesInChunk.size();
    }

    protected void onPlacementAdded() {
        StatusInfoRenderer.getInstance().startOverrideDelay();
    }

    public void addSchematicPlacement(SchematicPlacement placement, boolean printMessages) {
        if (!this.schematicPlacements.contains(placement)) {
            this.schematicPlacements.add(placement);
            this.addTouchedChunksFor(placement);
            SchematicPlacementEventHandler.getInstance().onPlacementAdded(placement);
            this.onPlacementAdded();
            if (this.selectedPlacement == null) {
                this.setSelectedSchematicPlacement(placement);
            }
            if (printMessages) {
                InfoUtils.showGuiMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)StringUtils.translate((String)"litematica.message.schematic_placement_created", (Object[])new Object[]{placement.getName()}), (Object[])new Object[0]);
                if (Configs.InfoOverlays.WARN_DISABLED_RENDERING.getBooleanValue()) {
                    String hotkeyVal;
                    String hotkeyName;
                    String configName;
                    ConfigHotkey hotkey;
                    LayerMode mode = DataManager.getRenderLayerRange().getLayerMode();
                    if (mode != LayerMode.ALL) {
                        InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.warn.layer_mode_currently_at", (Object[])new Object[]{mode.getDisplayName()});
                    }
                    if (!Configs.Visuals.ENABLE_RENDERING.getBooleanValue()) {
                        hotkey = Hotkeys.TOGGLE_ALL_RENDERING;
                        configName = Configs.Visuals.ENABLE_RENDERING.getName();
                        hotkeyName = hotkey.getName();
                        hotkeyVal = hotkey.getKeybind().getKeysDisplayString();
                        InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.WARNING, (int)8000, (String)"litematica.message.warn.main_rendering_disabled", (Object[])new Object[]{configName, hotkeyName, hotkeyVal});
                    }
                    if (!Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue()) {
                        hotkey = Hotkeys.TOGGLE_SCHEMATIC_RENDERING;
                        configName = Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getName();
                        hotkeyName = hotkey.getName();
                        hotkeyVal = hotkey.getKeybind().getKeysDisplayString();
                        InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.WARNING, (int)8000, (String)"litematica.message.warn.schematic_rendering_disabled", (Object[])new Object[]{configName, hotkeyName, hotkeyVal});
                    }
                    if (!Configs.Visuals.ENABLE_SCHEMATIC_BLOCKS.getBooleanValue()) {
                        hotkey = Hotkeys.TOGGLE_SCHEMATIC_BLOCK_RENDERING;
                        configName = Configs.Visuals.ENABLE_SCHEMATIC_BLOCKS.getName();
                        hotkeyName = hotkey.getName();
                        hotkeyVal = hotkey.getKeybind().getKeysDisplayString();
                        InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.WARNING, (int)8000, (String)"litematica.message.warn.schematic_blocks_rendering_disabled", (Object[])new Object[]{configName, hotkeyName, hotkeyVal});
                    }
                }
            }
        } else if (printMessages) {
            InfoUtils.showGuiAndInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.duplicate_schematic_placement", (Object[])new Object[0]);
        }
    }

    public boolean removeSchematicPlacement(SchematicPlacement placement) {
        return this.removeSchematicPlacement(placement, true);
    }

    public boolean removeSchematicPlacement(SchematicPlacement placement, boolean update) {
        if (this.selectedPlacement == placement) {
            this.selectedPlacement = null;
        }
        if (placement.hasVerifier()) {
            placement.getSchematicVerifier().reset();
        }
        boolean ret = this.schematicPlacements.remove(placement);
        this.removeTouchedChunksFor(placement);
        if (ret) {
            placement.onRemoved();
            if (update) {
                this.onPlacementModified(placement);
            }
        }
        return ret;
    }

    public List<SchematicPlacement> getAllPlacementsOfSchematic(LitematicaSchematic schematic) {
        ArrayList<SchematicPlacement> list = new ArrayList<SchematicPlacement>();
        for (SchematicPlacement placement : this.schematicPlacements) {
            if (placement.getSchematic() != schematic) continue;
            list.add(placement);
        }
        return list;
    }

    public void removeAllPlacementsOfSchematic(LitematicaSchematic schematic) {
        boolean removed = false;
        for (int i = 0; i < this.schematicPlacements.size(); ++i) {
            SchematicPlacement placement = this.schematicPlacements.get(i);
            if (placement.getSchematic() != schematic) continue;
            if (placement.hasVerifier()) {
                placement.getSchematicVerifier().reset();
            }
            removed |= this.removeSchematicPlacement(placement, false);
            --i;
        }
        if (removed) {
            OverlayRenderer.getInstance().updatePlacementCache();
        }
    }

    @Nullable
    public SchematicPlacement getSelectedSchematicPlacement() {
        return this.selectedPlacement;
    }

    public void setSelectedSchematicPlacement(@Nullable SchematicPlacement placement) {
        if (placement == null || this.schematicPlacements.contains(placement)) {
            SchematicPlacementEventHandler.getInstance().onPlacementSelected(this.selectedPlacement, placement);
            this.selectedPlacement = placement;
            OverlayRenderer.getInstance().updatePlacementCache();
            DataManager.setMaterialList(null);
        }
    }

    protected void addTouchedChunksFor(SchematicPlacement placement) {
        if (placement.matchesRequirement(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED)) {
            Set<class_1923> chunks = placement.getTouchedChunks();
            for (class_1923 pos : chunks) {
                if (!this.schematicsTouchingChunk.containsEntry((Object)pos, (Object)placement)) {
                    this.schematicsTouchingChunk.put((Object)pos, (Object)placement);
                    this.updateTouchedBoxesInChunk(pos);
                }
                this.chunksToUnload.remove(pos.method_8324());
            }
            this.markChunksForRebuild(placement);
            this.onPlacementModified(placement);
        }
    }

    protected void removeTouchedChunksFor(SchematicPlacement placement) {
        if (placement.matchesRequirement(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED)) {
            Set<class_1923> chunks = placement.getTouchedChunks();
            Iterator<class_1923> it = chunks.iterator();
            while (it.hasNext()) {
                class_1923 pos = it.next();
                this.schematicsTouchingChunk.remove((Object)pos, (Object)placement);
                this.updateTouchedBoxesInChunk(pos);
                if (this.schematicsTouchingChunk.containsKey((Object)pos)) continue;
                this.chunksToUnload.add(pos.method_8324());
                it.remove();
            }
            this.markChunksForRebuild(chunks);
        }
    }

    void onPrePlacementChange(SchematicPlacement placement) {
        this.chunksPreChange.clear();
        this.chunksPreChange.addAll(placement.getTouchedChunks());
    }

    void onPostPlacementChange(SchematicPlacement placement) {
        Set<class_1923> chunksPost = placement.getTouchedChunks();
        HashSet<class_1923> toRebuild = new HashSet<class_1923>(chunksPost);
        this.chunksPreChange.removeAll(chunksPost);
        for (class_1923 pos : this.chunksPreChange) {
            this.schematicsTouchingChunk.remove((Object)pos, (Object)placement);
            this.updateTouchedBoxesInChunk(pos);
            if (!this.schematicsTouchingChunk.containsKey((Object)pos)) {
                this.chunksToUnload.add(pos.method_8324());
                continue;
            }
            toRebuild.add(pos);
        }
        for (class_1923 pos : chunksPost) {
            if (!this.schematicsTouchingChunk.containsEntry((Object)pos, (Object)placement)) {
                this.schematicsTouchingChunk.put((Object)pos, (Object)placement);
            }
            this.updateTouchedBoxesInChunk(pos);
        }
        this.markChunksForRebuild(toRebuild);
        this.onPlacementModified(placement);
    }

    protected void updateTouchedBoxesInChunk(class_1923 pos) {
        long chunkPosLong = pos.method_8324();
        this.touchedVolumesInChunk.remove(chunkPosLong);
        List placements = this.schematicsTouchingChunk.get((Object)pos);
        if (!placements.isEmpty()) {
            for (SchematicPlacement placement : placements) {
                ImmutableMap<String, IntBoundingBox> boxMap;
                if (!placement.matchesRequirement(SubRegionPlacement.RequiredEnabled.RENDERING_ENABLED) || (boxMap = placement.getBoxesWithinChunk(pos.field_9181, pos.field_9180)).isEmpty()) continue;
                List list = (List)this.touchedVolumesInChunk.computeIfAbsent(chunkPosLong, p -> new ArrayList());
                for (Map.Entry entry : boxMap.entrySet()) {
                    list.add(new PlacementPart(placement, (String)entry.getKey(), (IntBoundingBox)entry.getValue()));
                }
            }
        }
    }

    public void markAllPlacementsOfSchematicForRebuild(LitematicaSchematic schematic) {
        for (SchematicPlacement placement : this.schematicPlacements) {
            if (placement.getSchematic() != schematic) continue;
            this.markChunksForRebuild(placement);
        }
    }

    public void markChunksForRebuild(SchematicPlacement placement) {
        if (placement.matchesRequirement(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED)) {
            this.markChunksForRebuild(placement.getTouchedChunks());
        }
    }

    void markChunksForRebuild(Collection<class_1923> chunks) {
        this.chunksToRebuild.addAll(chunks);
        this.chunkRebuildQueue.addAll(chunks);
    }

    public void markChunkForRebuild(class_1923 pos) {
        this.chunksToRebuild.add(pos);
        this.chunkRebuildQueue.add(pos);
    }

    protected void onPlacementModified(SchematicPlacement placement) {
        SchematicPlacementEventHandler.getInstance().onPlacementUpdated(placement);
        if (placement.isEnabled()) {
            OverlayRenderer.getInstance().updatePlacementCache();
        }
    }

    public boolean changeSelection(class_1937 world, class_1297 entity, int maxDistance) {
        if (this.schematicPlacements.size() > 0) {
            RayTraceUtils.RayTraceWrapper trace = RayTraceUtils.getWrappedRayTraceFromEntity(world, entity, maxDistance);
            SchematicPlacement placement = this.getSelectedSchematicPlacement();
            if (placement != null) {
                placement.setSelectedSubRegionName(null);
            }
            if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.PLACEMENT_SUBREGION || trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.PLACEMENT_ORIGIN) {
                this.setSelectedSchematicPlacement(trace.getHitSchematicPlacement());
                boolean selectSubRegion = Hotkeys.SELECTION_GRAB_MODIFIER.getKeybind().isKeybindHeld();
                String subRegionName = selectSubRegion ? trace.getHitSchematicPlacementRegionName() : null;
                this.getSelectedSchematicPlacement().setSelectedSubRegionName(subRegionName);
                return true;
            }
            if (trace.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.MISS) {
                this.setSelectedSchematicPlacement(null);
                return true;
            }
        }
        return false;
    }

    public void setPositionOfCurrentSelectionToRayTrace(class_310 mc, double maxDistance) {
        SchematicPlacement schematicPlacement = this.getSelectedSchematicPlacement();
        if (schematicPlacement != null) {
            class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
            class_239 trace = RayTraceUtils.getRayTraceFromEntity((class_1937)mc.field_1687, entity, false, maxDistance);
            if (trace.method_17783() != class_239.class_240.field_1332) {
                return;
            }
            class_2338 pos = ((class_3965)trace).method_17777();
            if (!mc.field_1724.method_5715()) {
                pos = pos.method_10093(((class_3965)trace).method_17780());
            }
            this.setPositionOfCurrentSelectionTo(pos, mc);
        }
    }

    public void setPositionOfCurrentSelectionTo(class_2338 pos, class_310 mc) {
        SchematicPlacement schematicPlacement = this.getSelectedSchematicPlacement();
        if (schematicPlacement != null) {
            boolean movingBox;
            if (schematicPlacement.isLocked()) {
                InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.placement.cant_modify_is_locked", (Object[])new Object[0]);
                return;
            }
            boolean bl = movingBox = schematicPlacement.getSelectedSubRegionPlacement() != null;
            if (movingBox) {
                schematicPlacement.moveSubRegionTo(schematicPlacement.getSelectedSubRegionName(), pos, InfoUtils.INFO_MESSAGE_CONSUMER);
                String posStr = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.placement.moved_subregion_to", (Object[])new Object[]{posStr});
            } else {
                class_2338 old = schematicPlacement.getOrigin();
                schematicPlacement.setOrigin(pos, InfoUtils.INFO_MESSAGE_CONSUMER);
                if (!old.equals((Object)schematicPlacement.getOrigin())) {
                    String posStrOld = String.format("x: %d, y: %d, z: %d", old.method_10263(), old.method_10264(), old.method_10260());
                    String posStrNew = String.format("x: %d, y: %d, z: %d", pos.method_10263(), pos.method_10264(), pos.method_10260());
                    InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.placement.moved_placement_origin", (Object[])new Object[]{posStrOld, posStrNew});
                }
            }
        }
    }

    public void nudgePositionOfCurrentSelection(class_2350 direction, int amount) {
        SchematicPlacement schematicPlacement = this.getSelectedSchematicPlacement();
        if (schematicPlacement != null) {
            if (schematicPlacement.isLocked()) {
                InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.placement.cant_modify_is_locked", (Object[])new Object[0]);
                return;
            }
            SubRegionPlacement placement = schematicPlacement.getSelectedSubRegionPlacement();
            if (placement != null) {
                class_2338 old = PositionUtils.getTransformedBlockPos(placement.getPos(), schematicPlacement.getMirror(), schematicPlacement.getRotation());
                old = old.method_10081((class_2382)schematicPlacement.getOrigin());
                schematicPlacement.moveSubRegionTo(placement.getName(), old.method_10079(direction, amount), InfoUtils.INFO_MESSAGE_CONSUMER);
            } else {
                class_2338 old = schematicPlacement.getOrigin();
                schematicPlacement.setOrigin(old.method_10079(direction, amount), InfoUtils.INFO_MESSAGE_CONSUMER);
            }
        }
    }

    public void pasteCurrentPlacementToWorld(class_310 mc) {
        this.pastePlacementToWorld(this.getSelectedSchematicPlacement(), mc);
    }

    public void pastePlacementToWorld(SchematicPlacement schematicPlacement, class_310 mc) {
        this.pastePlacementToWorld(schematicPlacement, true, mc);
    }

    public void pastePlacementToWorld(SchematicPlacement schematicPlacement, boolean changedBlocksOnly, class_310 mc) {
        this.pastePlacementToWorld(schematicPlacement, changedBlocksOnly, true, mc);
    }

    public void pastePlacementToWorld(SchematicPlacement schematicPlacement, boolean changedBlocksOnly, boolean printMessage, class_310 mc) {
        if (mc.field_1724 != null && EntityUtils.isCreativeMode((class_1657)mc.field_1724)) {
            if (schematicPlacement != null) {
                LayerRange range = DataManager.getRenderLayerRange();
                if (Configs.Generic.PASTE_TO_MCFUNCTION.getBooleanValue()) {
                    PasteToCommandsListener cl = new PasteToCommandsListener(schematicPlacement, changedBlocksOnly);
                    GuiConfirmAction screen = new GuiConfirmAction(320, "Confirm paste to command files", (IConfirmationListener)cl, null, "Are you sure you want to paste the current placement as setblock commands into command/mcfunction files?", new Object[0]);
                    GuiBase.openGui((class_437)screen);
                } else if (!mc.method_1496() || Configs.Generic.PASTE_USING_COMMANDS_IN_SP.getBooleanValue()) {
                    if (EntitiesDataStorage.getInstance().hasServuxServer() && Configs.Generic.PASTE_USING_SERVUX.getBooleanValue()) {
                        Litematica.debugLog("Found a Servux server, I am sending the Schematic Placement to it.", new Object[0]);
                        InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.paste_with_servux", (Object[])new Object[0]);
                        class_2487 nbt = schematicPlacement.toNbt(true);
                        int maxSize = 0x3FFF000;
                        if (nbt.method_47988() > 0x3FFF000) {
                            Litematica.LOGGER.warn("[Servux Paste]: Slicing Oversided Schematic for Servux Paste ...");
                            this.sliceForServux(schematicPlacement.getSchematic(), nbt, 0x3FFF000, printMessage);
                        } else {
                            nbt.method_10582("Task", "LitematicaPaste");
                            ServuxLitematicaHandler.getInstance().encodeClientData(ServuxLitematicaPacket.ResponseC2SStart(nbt));
                        }
                    } else {
                        TaskPasteSchematicPerChunkCommand task = new TaskPasteSchematicPerChunkCommand(Collections.singletonList(schematicPlacement), range, changedBlocksOnly);
                        TaskScheduler.getInstanceClient().scheduleTask(task, Configs.Generic.COMMAND_TASK_INTERVAL.getIntegerValue());
                        if (printMessage) {
                            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.scheduled_task_added", (Object[])new Object[0]);
                        }
                    }
                } else if (mc.method_1496()) {
                    TaskPasteSchematicPerChunkDirect task = new TaskPasteSchematicPerChunkDirect(Collections.singletonList(schematicPlacement), range, changedBlocksOnly);
                    TaskScheduler.getInstanceServer().scheduleTask(task, Configs.Generic.COMMAND_TASK_INTERVAL.getIntegerValue());
                    if (printMessage) {
                        InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.scheduled_task_added", (Object[])new Object[0]);
                    }
                }
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_placement_selected", (Object[])new Object[0]);
            }
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.generic.creative_mode_only", (Object[])new Object[0]);
        }
    }

    private void sliceForServux(LitematicaSchematic litematic, class_2487 nbt, int maxSize, boolean printMessage) {
        long sessionKey = class_5819.method_43049((long)class_156.method_658()).method_43055();
        nbt.method_10551("Schematics");
        litematic.sendTransmitFile(nbt, sessionKey, printMessage);
    }

    public void clear() {
        this.schematicPlacements.clear();
        this.selectedPlacement = null;
        this.schematicsTouchingChunk.clear();
        this.touchedVolumesInChunk.clear();
        this.chunksPreChange.clear();
        this.chunksToRebuild.clear();
        this.chunkRebuildQueue.clear();
        this.chunksToUnload.clear();
        this.visibleChunks.clear();
        SchematicHolder.getInstance().clearLoadedSchematics();
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        if (this.schematicPlacements.size() > 0) {
            JsonArray arr = new JsonArray();
            int selectedIndex = 0;
            boolean indexValid = false;
            for (SchematicPlacement placement : this.schematicPlacements) {
                JsonObject objPlacement;
                if (!placement.shouldBeSaved() || (objPlacement = placement.toJson()) == null) continue;
                arr.add((JsonElement)objPlacement);
                if (this.selectedPlacement == placement) {
                    indexValid = true;
                    continue;
                }
                if (indexValid) continue;
                ++selectedIndex;
            }
            obj.add("placements", (JsonElement)arr);
            if (indexValid) {
                obj.add("selected", (JsonElement)new JsonPrimitive((Number)selectedIndex));
                obj.add("origin_selected", (JsonElement)new JsonPrimitive(Boolean.valueOf(true)));
            }
        }
        return obj;
    }

    public void loadFromJson(JsonObject obj) {
        this.clear();
        if (JsonUtils.hasArray((JsonObject)obj, (String)"placements")) {
            JsonArray arr = obj.get("placements").getAsJsonArray();
            int index = JsonUtils.hasInteger((JsonObject)obj, (String)"selected") ? obj.get("selected").getAsInt() : -1;
            int size = arr.size();
            for (int i = 0; i < size; ++i) {
                JsonElement el = arr.get(i);
                if (el.isJsonObject()) {
                    SchematicPlacement placement = SchematicPlacement.fromJson(el.getAsJsonObject());
                    if (placement == null) continue;
                    this.addSchematicPlacement(placement, false);
                    continue;
                }
                index = -1;
            }
            if (index >= 0 && index < this.schematicPlacements.size()) {
                this.selectedPlacement = this.schematicPlacements.get(index);
            }
        }
        OverlayRenderer.getInstance().updatePlacementCache();
    }

    public static class PlacementPart {
        public final SchematicPlacement placement;
        public final String subRegionName;
        public final IntBoundingBox bb;

        public PlacementPart(SchematicPlacement placement, String subRegionName, IntBoundingBox bb) {
            this.placement = placement;
            this.subRegionName = subRegionName;
            this.bb = bb;
        }

        public SchematicPlacement getPlacement() {
            return this.placement;
        }

        public String getSubRegionName() {
            return this.subRegionName;
        }

        public IntBoundingBox getBox() {
            return this.bb;
        }
    }

    private static class PasteToCommandsListener
    implements IConfirmationListener {
        private final SchematicPlacement schematicPlacement;
        private final boolean changedBlocksOnly;

        public PasteToCommandsListener(SchematicPlacement schematicPlacement, boolean changedBlocksOnly) {
            this.schematicPlacement = schematicPlacement;
            this.changedBlocksOnly = changedBlocksOnly;
        }

        public boolean onActionConfirmed() {
            LayerRange range = DataManager.getRenderLayerRange();
            TaskPasteSchematicSetblockToMcfunction task = new TaskPasteSchematicSetblockToMcfunction(Collections.singletonList(this.schematicPlacement), range, this.changedBlocksOnly);
            TaskScheduler.getInstanceClient().scheduleTask(task, 1);
            return true;
        }

        public boolean onActionCancelled() {
            return true;
        }
    }
}

