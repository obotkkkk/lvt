/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkMultiPhase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.util.PasteLayerBehavior;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.ReplaceBehavior;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import java.util.Collection;
import java.util.Set;
import net.minecraft.class_1923;
import net.minecraft.class_1937;

public abstract class TaskPasteSchematicPerChunkBase
extends TaskProcessChunkMultiPhase {
    protected final ImmutableList<SchematicPlacement> placements;
    protected final LayerRange layerRange;
    protected final ReplaceBehavior replace;
    protected final PasteLayerBehavior layerBehavior;
    protected final boolean changedBlockOnly;
    protected boolean ignoreBlocks;
    protected boolean ignoreEntities;

    public TaskPasteSchematicPerChunkBase(Collection<SchematicPlacement> placements, LayerRange range, boolean changedBlocksOnly) {
        super("litematica.gui.label.task_name.paste");
        this.placements = ImmutableList.copyOf(placements);
        this.layerRange = range;
        this.changedBlockOnly = changedBlocksOnly;
        this.ignoreEntities = Configs.Generic.PASTE_IGNORE_ENTITIES.getBooleanValue();
        this.replace = (ReplaceBehavior)Configs.Generic.PASTE_REPLACE_BEHAVIOR.getOptionListValue();
        this.layerBehavior = (PasteLayerBehavior)Configs.Generic.PASTE_LAYER_BEHAVIOR.getOptionListValue();
    }

    @Override
    public void init() {
        for (SchematicPlacement placement : this.placements) {
            this.addPlacement(placement, this.layerRange);
        }
        this.pendingChunks.clear();
        this.pendingChunks.addAll(this.boxesInChunks.keySet());
        this.sortChunkList();
    }

    @Override
    public boolean canExecute() {
        return super.canExecute() && this.schematicWorld != null;
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

    protected void onChunkAddedForHandling(class_1923 pos, SchematicPlacement placement) {
    }

    @Override
    protected boolean canProcessChunk(class_1923 pos) {
        if (!this.schematicWorld.getChunkProvider().method_12123(pos.field_9181, pos.field_9180) || DataManager.getSchematicPlacementManager().hasPendingRebuildFor(pos)) {
            return false;
        }
        return this.areSurroundingChunksLoaded(pos, this.clientWorld, 1);
    }
}

