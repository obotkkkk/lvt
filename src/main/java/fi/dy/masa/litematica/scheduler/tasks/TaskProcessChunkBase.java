/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerMode
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.WorldUtils
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_310
 *  net.minecraft.class_3695
 *  net.minecraft.class_638
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ArrayListMultimap;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskBase;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerMode;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_3695;
import net.minecraft.class_638;

public abstract class TaskProcessChunkBase
extends TaskBase {
    protected final ArrayListMultimap<class_1923, IntBoundingBox> boxesInChunks = ArrayListMultimap.create();
    protected final ArrayList<class_1923> pendingChunks = new ArrayList();
    protected final class_638 clientWorld;
    protected final WorldSchematic schematicWorld;
    protected final class_1937 world;
    protected final boolean isClientWorld;
    protected PositionUtils.ChunkPosComparator comparator = new PositionUtils.ChunkPosComparator();

    protected TaskProcessChunkBase(String nameOnHud) {
        this.clientWorld = this.mc.field_1687;
        this.world = WorldUtils.getBestWorld((class_310)this.mc);
        this.schematicWorld = SchematicWorldHandler.getSchematicWorld();
        this.isClientWorld = this.world == this.mc.field_1687;
        this.name = StringUtils.translate((String)nameOnHud, (Object[])new Object[0]);
        this.comparator.setClosestFirst(true);
        InfoHud.getInstance().addInfoHudRenderer(this, true);
    }

    @Override
    public boolean execute(class_3695 profiler) {
        return this.executeForAllPendingChunks(profiler);
    }

    @Override
    public void stop() {
        if (this.isClientWorld) {
            this.onStop();
        } else {
            this.mc.execute(this::onStop);
        }
    }

    protected void onStop() {
        this.notifyListener();
    }

    protected abstract boolean canProcessChunk(class_1923 var1);

    protected boolean processChunk(class_1923 pos) {
        return true;
    }

    protected boolean executeForAllPendingChunks(class_3695 profiler) {
        profiler.method_15396("process_chunks");
        Iterator<class_1923> iterator = this.pendingChunks.iterator();
        int processed = 0;
        while (iterator.hasNext()) {
            class_1923 pos = iterator.next();
            profiler.method_15396("process_chunk");
            if (this.canProcessChunk(pos) && this.processChunk(pos)) {
                iterator.remove();
                ++processed;
            }
            profiler.method_15407();
        }
        if (processed > 0) {
            this.updateInfoHudLinesPendingChunks(this.pendingChunks);
        }
        this.finished = this.pendingChunks.isEmpty();
        profiler.method_15407();
        return this.finished;
    }

    protected void addPerChunkBoxes(Collection<Box> allBoxes) {
        this.addPerChunkBoxes(allBoxes, new LayerRange(null));
    }

    protected void addPerChunkBoxes(Collection<Box> allBoxes, LayerRange range) {
        this.boxesInChunks.clear();
        this.pendingChunks.clear();
        if (range.getLayerMode() == LayerMode.ALL) {
            PositionUtils.getPerChunkBoxes(allBoxes, this::clampToWorldHeightAndAddBox);
        } else {
            PositionUtils.getLayerRangeClampedPerChunkBoxes(allBoxes, range, this::clampToWorldHeightAndAddBox);
        }
        this.pendingChunks.addAll(this.boxesInChunks.keySet());
        this.sortChunkList();
    }

    protected void clampToWorldHeightAndAddBox(class_1923 pos, IntBoundingBox box) {
        if ((box = PositionUtils.clampBoxToWorldHeightRange(box, (class_1937)this.clientWorld)) != null) {
            this.boxesInChunks.put((Object)pos, (Object)box);
        }
    }

    protected void addNonChunkClampedBoxes(Collection<Box> allBoxes) {
        this.addNonChunkClampedBoxes(allBoxes, new LayerRange(null));
    }

    protected void addNonChunkClampedBoxes(Collection<Box> allBoxes, LayerRange range) {
        this.boxesInChunks.clear();
        this.pendingChunks.clear();
        if (range.getLayerMode() == LayerMode.ALL) {
            TaskProcessChunkBase.addBoxes(allBoxes, this::clampToWorldHeightAndAddBox);
        } else {
            TaskProcessChunkBase.getLayerRangeClampedBoxes(allBoxes, range, this::clampToWorldHeightAndAddBox);
        }
        this.pendingChunks.addAll(this.boxesInChunks.keySet());
        this.sortChunkList();
    }

    protected static void addBoxes(Collection<Box> boxes, BiConsumer<class_1923, IntBoundingBox> consumer) {
        for (Box box : boxes) {
            int boxMinX = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMinY = Math.min(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMinZ = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxMaxX = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMaxY = Math.max(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMaxZ = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
            consumer.accept(new class_1923(boxMinX >> 4, boxMinZ >> 4), new IntBoundingBox(boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ));
        }
    }

    protected static void getLayerRangeClampedBoxes(Collection<Box> boxes, LayerRange range, BiConsumer<class_1923, IntBoundingBox> consumer) {
        block5: for (Box box : boxes) {
            int rangeMin = range.getLayerMin();
            int rangeMax = range.getLayerMax();
            int boxMinX = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMinY = Math.min(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMinZ = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxMaxX = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMaxY = Math.max(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMaxZ = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
            switch (range.getAxis()) {
                case field_11048: {
                    if (rangeMax < boxMinX || rangeMin > boxMaxX) continue block5;
                    boxMinX = Math.max(boxMinX, rangeMin);
                    boxMaxX = Math.min(boxMaxX, rangeMax);
                    break;
                }
                case field_11052: {
                    if (rangeMax < boxMinY || rangeMin > boxMaxY) continue block5;
                    boxMinY = Math.max(boxMinY, rangeMin);
                    boxMaxY = Math.min(boxMaxY, rangeMax);
                    break;
                }
                case field_11051: {
                    if (rangeMax < boxMinZ || rangeMin > boxMaxZ) continue block5;
                    boxMinZ = Math.max(boxMinZ, rangeMin);
                    boxMaxZ = Math.min(boxMaxZ, rangeMax);
                }
            }
            consumer.accept(new class_1923(boxMinX >> 4, boxMinZ >> 4), new IntBoundingBox(boxMinX, boxMinY, boxMinZ, boxMaxX, boxMaxY, boxMaxZ));
        }
    }

    protected List<IntBoundingBox> getBoxesInChunk(class_1923 pos) {
        return this.boxesInChunks.get((Object)pos);
    }

    protected void sortChunkList() {
        if (this.pendingChunks.size() > 0) {
            if (this.mc.field_1724 != null) {
                this.comparator.setReferencePosition(this.mc.field_1724.method_24515());
                this.pendingChunks.sort(this.comparator);
            }
            this.updateInfoHudLines();
            this.onChunkListSorted();
        }
    }

    protected void onChunkListSorted() {
    }

    protected void updateInfoHudLines() {
        this.updateInfoHudLinesPendingChunks(this.pendingChunks);
    }
}

