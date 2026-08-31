/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  net.minecraft.class_1263
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2259
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_3695
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskFillArea;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkMultiPhase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.util.PlacementDeletionMode;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_1263;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2259;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_3695;

public class TaskDeleteBlocksByPlacement
extends TaskProcessChunkMultiPhase {
    protected static final class_2680 AIR = class_2246.field_10124.method_9564();
    protected final ImmutableList<SchematicPlacement> placements;
    protected final LayerRange layerRange;
    protected final PlacementDeletionMode mode;
    protected final String setBlockCommand;
    protected final String blockString;
    protected long blockCount;
    protected String useStrict;

    public TaskDeleteBlocksByPlacement(Collection<SchematicPlacement> placements, PlacementDeletionMode mode, LayerRange layerRange) {
        super("Delete Blocks");
        this.placements = ImmutableList.copyOf(placements);
        this.mode = mode;
        this.layerRange = layerRange;
        this.setBlockCommand = Configs.Generic.COMMAND_NAME_SETBLOCK.getStringValue();
        this.useStrict = "";
        this.blockString = class_2259.method_9685((class_2680)class_2246.field_10124.method_9564());
        this.processBoxBlocksTask = this::sendQueuedCommands;
    }

    @Override
    public boolean canExecute() {
        return super.canExecute() && this.schematicWorld != null;
    }

    protected void onChunkAddedForHandling(class_1923 pos, SchematicPlacement placement) {
    }

    protected void addPlacement(SchematicPlacement placement, LayerRange range) {
        Set<class_1923> touchedChunks = placement.getTouchedChunks();
        for (class_1923 pos : touchedChunks) {
            int count = 0;
            for (IntBoundingBox box : placement.getBoxesWithinChunk(pos.field_9181, pos.field_9180).values()) {
                if ((box = PositionUtils.getClampedBox(box, range)) == null || (box = PositionUtils.clampBoxToWorldHeightRange(box, (class_1937)this.clientWorld)) == null) continue;
                this.boxesInChunks.put((Object)pos, (Object)box);
                ++count;
            }
            if (count <= 0) continue;
            this.onChunkAddedForHandling(pos, placement);
        }
    }

    @Override
    public void init() {
        if (this.useWorldEdit && this.isInWorld()) {
            this.sendCommand("/perf neighbors off");
        }
        for (SchematicPlacement placement : this.placements) {
            this.addPlacement(placement, this.layerRange);
        }
        this.pendingChunks.clear();
        this.pendingChunks.addAll(this.boxesInChunks.keySet());
        this.sortChunkList();
    }

    @Override
    public boolean execute(class_3695 profiler) {
        return this.executeMultiPhase(profiler);
    }

    @Override
    protected void onNextChunkFetched(class_1923 pos) {
        if (this.isClientWorld) {
            this.queueCommandsForBoxesInChunk(pos);
        } else {
            this.directRemoveBoxesInChunk(pos);
        }
    }

    protected void queueCommandsForBoxesInChunk(class_1923 pos) {
        for (IntBoundingBox box : this.getBoxesInChunk(pos)) {
            this.removeEntitiesByCommand(box);
            this.removeBlocksInBox(box, this.mode, this::removeBlockByCommand);
        }
        this.phase = TaskProcessChunkMultiPhase.TaskPhase.PROCESS_BOX_BLOCKS;
    }

    protected void directRemoveBoxesInChunk(class_1923 pos) {
        for (IntBoundingBox box : this.getBoxesInChunk(pos)) {
            TaskFillArea.directRemoveEntities(box, this.world);
            this.removeBlocksInBox(box, this.mode, this::removeBlockDirect);
        }
        this.finishProcessingChunk(pos);
    }

    protected void removeBlocksInBox(IntBoundingBox box, PlacementDeletionMode mode, Consumer<class_2338> removeFunc) {
        BlockCheck check = this.getCheckFor(mode);
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        for (int y = box.maxY; y >= box.minY; --y) {
            for (int x = box.minX; x <= box.maxX; ++x) {
                for (int z = box.minZ; z <= box.maxZ; ++z) {
                    posMutable.method_10103(x, y, z);
                    if (this.world.method_8320((class_2338)posMutable) == AIR || !check.shouldDelete((class_2338)posMutable, this.schematicWorld, this.world)) continue;
                    removeFunc.accept((class_2338)posMutable);
                    this.removeBlockDirect((class_2338)posMutable);
                    ++this.blockCount;
                }
            }
        }
    }

    protected void removeBlockDirect(class_2338 pos) {
        class_2586 te = this.world.method_8321(pos);
        if (te instanceof class_1263) {
            ((class_1263)te).method_5448();
            this.world.method_8652(pos, class_2246.field_10499.method_9564(), 50);
        }
        this.world.method_8652(pos, class_2246.field_10124.method_9564(), 50);
    }

    protected void removeEntitiesByCommand(IntBoundingBox box) {
        String killCmd = String.format("kill @e[type=!player,x=%d,y=%d,z=%d,dx=%d,dy=%d,dz=%d]", box.minX, box.minY, box.minZ, box.maxX - box.minX + 1, box.maxY - box.minY + 1, box.maxZ - box.minZ + 1);
        this.queuedCommands.offer(killCmd);
    }

    protected void removeBlockByCommand(class_2338 pos) {
        if (this.useWorldEdit) {
            this.queuedCommands.offer(String.format("/pos1 %d,%d,%d", pos.method_10263(), pos.method_10264(), pos.method_10260()));
            this.queuedCommands.offer(String.format("/pos2 %d,%d,%d", pos.method_10263(), pos.method_10264(), pos.method_10260()));
            this.queuedCommands.offer("/set " + this.blockString);
        } else {
            String cmdName = this.setBlockCommand;
            String fillCommand = String.format("%s %d %d %d %s%s", cmdName, pos.method_10263(), pos.method_10264(), pos.method_10260(), this.blockString, this.useStrict);
            this.queuedCommands.offer(fillCommand);
        }
    }

    protected BlockCheck getCheckFor(PlacementDeletionMode mode) {
        switch (mode) {
            case MATCHING_BLOCK: {
                return (pos, sw, w) -> {
                    class_2680 stateSchematic = sw.method_8320(pos);
                    return stateSchematic != AIR && stateSchematic == w.method_8320(pos);
                };
            }
            case NON_MATCHING_BLOCK: {
                return (pos, sw, w) -> {
                    class_2680 stateSchematic = sw.method_8320(pos);
                    return stateSchematic != AIR && stateSchematic != w.method_8320(pos);
                };
            }
            case ANY_SCHEMATIC_BLOCK: {
                return (pos, sw, w) -> sw.method_8320(pos) != class_2246.field_10124.method_9564();
            }
            case NO_SCHEMATIC_BLOCK: {
                return (pos, sw, w) -> sw.method_8320(pos) == class_2246.field_10124.method_9564();
            }
        }
        return (pos, sw, w) -> true;
    }

    @Override
    protected boolean canProcessChunk(class_1923 pos) {
        if (!this.schematicWorld.getChunkProvider().method_12123(pos.field_9181, pos.field_9180)) {
            return false;
        }
        return this.areSurroundingChunksLoaded(pos, this.clientWorld, 1);
    }

    @Override
    protected void onStop() {
        if (this.finished) {
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)String.format("Deleted %d blocks", this.blockCount), (Object[])new Object[0]);
        } else {
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Deletion task failed", (Object[])new Object[0]);
        }
        this.sendTaskEndCommands();
        DataManager.removeChatListener(this.gameRuleListener);
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        super.onStop();
    }

    protected static interface BlockCheck {
        public boolean shouldDelete(class_2338 var1, class_1937 var2, class_1937 var3);
    }
}

