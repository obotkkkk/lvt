/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ImmutableMap;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkBase;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_2338;

public class TaskSaveSchematic
extends TaskProcessChunkBase {
    private final LitematicaSchematic schematic;
    private final class_2338 origin;
    private final ImmutableMap<String, Box> subRegions;
    private final Set<UUID> existingEntities = new HashSet<UUID>();
    @Nullable
    private final File dir;
    @Nullable
    private final String fileName;
    private final LitematicaSchematic.SchematicSaveInfo info;
    private final boolean overrideFile;
    protected final boolean fromSchematicWorld;

    public TaskSaveSchematic(LitematicaSchematic schematic, AreaSelection area, LitematicaSchematic.SchematicSaveInfo info) {
        this(null, null, schematic, area, info, false);
    }

    public TaskSaveSchematic(@Nullable File dir, @Nullable String fileName, LitematicaSchematic schematic, AreaSelection area, LitematicaSchematic.SchematicSaveInfo info, boolean overrideFile) {
        super("litematica.gui.label.task_name.save_schematic");
        this.dir = dir;
        this.fileName = fileName;
        this.schematic = schematic;
        this.origin = area.getEffectiveOrigin();
        this.subRegions = area.getAllSubRegions();
        this.info = info;
        this.overrideFile = overrideFile;
        this.fromSchematicWorld = info.fromSchematicWorld;
        this.addPerChunkBoxes(area.getAllSubRegionBoxes());
    }

    @Override
    protected boolean canProcessChunk(class_1923 pos) {
        if (this.fromSchematicWorld) {
            return this.schematicWorld != null && this.schematicWorld.getChunkManager().method_12123(pos.field_9181, pos.field_9180);
        }
        if ((EntitiesDataStorage.getInstance().hasServuxServer() || EntitiesDataStorage.getInstance().getIfReceivedBackupPackets()) && Objects.equals(EntitiesDataStorage.getInstance().getWorld(), this.clientWorld)) {
            if (EntitiesDataStorage.getInstance().hasCompletedChunk(pos)) {
                return this.areSurroundingChunksLoaded(pos, this.clientWorld, 0);
            }
            if (!EntitiesDataStorage.getInstance().hasPendingChunk(pos)) {
                ImmutableMap<String, IntBoundingBox> volumes = PositionUtils.getBoxesWithinChunk(pos.field_9181, pos.field_9180, this.subRegions);
                int minY = 319;
                int maxY = -60;
                for (Map.Entry volumeEntry : volumes.entrySet()) {
                    IntBoundingBox bb = (IntBoundingBox)volumeEntry.getValue();
                    minY = Math.min(bb.minY, minY);
                    maxY = Math.max(bb.maxY, maxY);
                }
                if (EntitiesDataStorage.getInstance().hasServuxServer()) {
                    EntitiesDataStorage.getInstance().requestServuxBulkEntityData(pos, minY, maxY);
                } else if (EntitiesDataStorage.getInstance().getIfReceivedBackupPackets()) {
                    EntitiesDataStorage.getInstance().requestBackupBulkEntityData(pos, minY, maxY);
                }
            }
            return false;
        }
        return this.areSurroundingChunksLoaded(pos, this.clientWorld, 0);
    }

    @Override
    protected boolean processChunk(class_1923 pos) {
        WorldSchematic world = this.fromSchematicWorld ? this.schematicWorld : this.world;
        ImmutableMap<String, IntBoundingBox> volumes = PositionUtils.getBoxesWithinChunk(pos.field_9181, pos.field_9180, this.subRegions);
        this.schematic.takeBlocksFromWorldWithinChunk(world, volumes, this.subRegions, this.info);
        if (!this.info.ignoreEntities) {
            this.schematic.takeEntitiesFromWorldWithinChunk(world, pos.field_9181, pos.field_9180, volumes, this.subRegions, this.existingEntities, this.origin);
        }
        if ((EntitiesDataStorage.getInstance().hasServuxServer() || EntitiesDataStorage.getInstance().getIfReceivedBackupPackets()) && EntitiesDataStorage.getInstance().hasCompletedChunk(pos) && Objects.equals(EntitiesDataStorage.getInstance().getWorld(), this.clientWorld)) {
            EntitiesDataStorage.getInstance().markCompletedChunkDirty(pos);
        }
        return true;
    }

    @Override
    protected void onStop() {
        if (this.finished) {
            long time = System.currentTimeMillis();
            this.schematic.getMetadata().setTimeCreated(time);
            this.schematic.getMetadata().setTimeModified(time);
            this.schematic.getMetadata().setTotalBlocks(this.schematic.getTotalBlocksReadFromWorld());
            if (this.dir != null) {
                if (this.schematic.writeToFile(this.dir, this.fileName, this.overrideFile)) {
                    if (this.printCompletionMessage) {
                        InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.schematic_saved_as", (Object[])new Object[]{this.fileName});
                    }
                } else {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.schematic_save_failed", (Object[])new Object[]{this.fileName});
                }
            } else {
                String name = this.schematic.getMetadata().getName();
                SchematicHolder.getInstance().addSchematic(this.schematic, true);
                if (this.printCompletionMessage) {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.in_memory_schematic_created", (Object[])new Object[]{name});
                }
            }
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.error.schematic_save_interrupted", (Object[])new Object[0]);
        }
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        this.notifyListener();
    }
}

