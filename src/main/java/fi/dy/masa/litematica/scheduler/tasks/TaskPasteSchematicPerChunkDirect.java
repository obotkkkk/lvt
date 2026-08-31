/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.LayerRange
 *  net.minecraft.class_1132
 *  net.minecraft.class_156
 *  net.minecraft.class_1923
 *  net.minecraft.class_3695
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ArrayListMultimap;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkBase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.util.SchematicPlacingUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.LayerRange;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.class_1132;
import net.minecraft.class_156;
import net.minecraft.class_1923;
import net.minecraft.class_3695;

public class TaskPasteSchematicPerChunkDirect
extends TaskPasteSchematicPerChunkBase {
    private final ArrayListMultimap<class_1923, SchematicPlacement> placementsPerChunk = ArrayListMultimap.create();

    public TaskPasteSchematicPerChunkDirect(Collection<SchematicPlacement> placements, LayerRange range, boolean changedBlocksOnly) {
        super(placements, range, changedBlocksOnly);
    }

    @Override
    public boolean canExecute() {
        return super.canExecute() && this.mc.method_1496() && this.world != null && !this.world.field_9236;
    }

    @Override
    protected void onChunkAddedForHandling(class_1923 pos, SchematicPlacement placement) {
        super.onChunkAddedForHandling(pos, placement);
        this.placementsPerChunk.put((Object)pos, (Object)placement);
    }

    @Override
    public boolean execute(class_3695 profiler) {
        long currentTime;
        long elapsedTickTime;
        if (this.ignoreBlocks && this.ignoreEntities) {
            return true;
        }
        profiler.method_15396("per_chunk_paste");
        class_1132 server = this.mc.method_1576();
        long vanillaTickTime = server.method_54835()[server.method_3780() % 100];
        long timeStart = class_156.method_648();
        this.sortChunkList();
        for (int chunkIndex = 0; chunkIndex < this.pendingChunks.size() && (elapsedTickTime = vanillaTickTime + ((currentTime = class_156.method_648()) - timeStart)) < 60000000L; ++chunkIndex) {
            profiler.method_15396("process_chunk");
            class_1923 pos = (class_1923)this.pendingChunks.get(chunkIndex);
            if (this.canProcessChunk(pos) && this.processChunk(pos)) {
                this.pendingChunks.remove(chunkIndex);
                --chunkIndex;
            }
            profiler.method_15407();
        }
        if (this.pendingChunks.isEmpty()) {
            this.finished = true;
            profiler.method_15407();
            return true;
        }
        this.updateInfoHudLines();
        profiler.method_15407();
        return false;
    }

    @Override
    protected boolean processChunk(class_1923 pos) {
        ArrayList placements = new ArrayList(this.placementsPerChunk.get((Object)pos));
        for (SchematicPlacement placement : placements) {
            if (!SchematicPlacingUtils.placeToWorldWithinChunk(this.world, pos, placement, this.replace, this.layerBehavior, false)) continue;
            this.placementsPerChunk.remove((Object)pos, (Object)placement);
        }
        return !this.placementsPerChunk.containsKey((Object)pos);
    }

    @Override
    protected void onStop() {
        if (this.finished) {
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.schematic_pasted", (Object[])new Object[0]);
        } else {
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.schematic_paste_failed", (Object[])new Object[0]);
        }
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        super.onStop();
    }
}

