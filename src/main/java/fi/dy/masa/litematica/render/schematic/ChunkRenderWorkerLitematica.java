/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.FutureCallback
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.MoreExecutors
 *  javax.annotation.Nullable
 *  net.minecraft.class_10209
 *  net.minecraft.class_128
 *  net.minecraft.class_1297
 *  net.minecraft.class_1921
 *  net.minecraft.class_310
 *  net.minecraft.class_3695
 *  org.apache.logging.log4j.Logger
 */
package fi.dy.masa.litematica.render.schematic;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDispatcherLitematica;
import fi.dy.masa.litematica.render.schematic.ChunkRenderLayers;
import fi.dy.masa.litematica.render.schematic.ChunkRenderTaskSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.class_10209;
import net.minecraft.class_128;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_310;
import net.minecraft.class_3695;
import org.apache.logging.log4j.Logger;

public class ChunkRenderWorkerLitematica
implements Runnable {
    private static final Logger LOGGER = Litematica.LOGGER;
    private final ChunkRenderDispatcherLitematica chunkRenderDispatcher;
    private final BufferAllocatorCache allocatorCache;
    private boolean shouldRun = true;
    private class_3695 profiler;

    public ChunkRenderWorkerLitematica(ChunkRenderDispatcherLitematica chunkRenderDispatcherIn, class_3695 profiler) {
        this(chunkRenderDispatcherIn, null, profiler);
    }

    public ChunkRenderWorkerLitematica(ChunkRenderDispatcherLitematica chunkRenderDispatcherIn, @Nullable BufferAllocatorCache allocatorCache, class_3695 profiler) {
        this.chunkRenderDispatcher = chunkRenderDispatcherIn;
        this.allocatorCache = allocatorCache;
        this.profiler = profiler;
    }

    @Override
    public void run() {
        if (this.profiler == null) {
            this.profiler = class_10209.method_64146();
        }
        while (this.shouldRun) {
            try {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate(), this.profiler);
            }
            catch (InterruptedException e) {
                LOGGER.debug("Stopping chunk worker due to interrupt");
                return;
            }
            catch (Throwable throwable) {
                class_128 crashreport = class_128.method_560((Throwable)throwable, (String)"Batching chunks");
                class_310.method_1551().method_43587(class_310.method_1551().method_1587(crashreport));
                return;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void processTask(final ChunkRenderTaskSchematic task, class_3695 profiler) throws InterruptedException {
        profiler.method_15396("process_task");
        task.getLock().lock();
        try {
            if (task.getStatus() != ChunkRenderTaskSchematic.Status.PENDING) {
                if (!task.isFinished()) {
                    LOGGER.warn("Chunk render task was {} when I expected it to be pending; ignoring task", (Object)task.getStatus());
                }
                profiler.method_15407();
                return;
            }
            task.setStatus(ChunkRenderTaskSchematic.Status.COMPILING);
        }
        finally {
            task.getLock().unlock();
        }
        class_1297 entity = class_310.method_1551().method_1560();
        if (entity == null) {
            task.finish();
        } else {
            if (!task.setRegionRenderCacheBuilder(this.getRegionRenderAllocatorCache())) {
                profiler.method_15407();
                throw new InterruptedException("No free Allocator Cache found");
            }
            ChunkRenderTaskSchematic.Type taskType = task.getType();
            profiler.method_15405("run_task_now_" + taskType.name());
            if (taskType == ChunkRenderTaskSchematic.Type.REBUILD_CHUNK) {
                task.getRenderChunk().rebuildChunk(task, profiler);
            } else if (taskType == ChunkRenderTaskSchematic.Type.RESORT_TRANSPARENCY) {
                task.getRenderChunk().resortTransparency(task, profiler);
            }
            task.getLock().lock();
            try {
                if (task.getStatus() != ChunkRenderTaskSchematic.Status.COMPILING) {
                    if (!task.isFinished()) {
                        LOGGER.warn("Chunk render task was {} when I expected it to be compiling; aborting task", (Object)task.getStatus());
                    }
                    this.resetRenderAllocators(task);
                    profiler.method_15407();
                    return;
                }
                task.setStatus(ChunkRenderTaskSchematic.Status.UPLOADING);
            }
            finally {
                task.getLock().unlock();
            }
            profiler.method_15405("run_task_schedule_" + taskType.name());
            final ChunkRenderDataSchematic chunkRenderData = task.getChunkRenderData();
            ArrayList futuresList = Lists.newArrayList();
            ChunkRendererSchematicVbo renderChunk = task.getRenderChunk();
            BufferAllocatorCache allocators = task.getAllocatorCache();
            if (taskType == ChunkRenderTaskSchematic.Type.REBUILD_CHUNK) {
                for (class_1921 layer : ChunkRenderLayers.LAYERS) {
                    if (chunkRenderData.isBlockLayerEmpty(layer)) continue;
                    futuresList.add(this.chunkRenderDispatcher.uploadChunkBlocks(layer, allocators, renderChunk, chunkRenderData, task.getDistanceSq(), false, profiler));
                }
                for (OverlayRenderType type : ChunkRenderLayers.TYPES) {
                    if (chunkRenderData.isOverlayTypeEmpty(type)) continue;
                    futuresList.add(this.chunkRenderDispatcher.uploadChunkOverlay(type, allocators, renderChunk, chunkRenderData, task.getDistanceSq(), false, profiler));
                }
            } else if (taskType == ChunkRenderTaskSchematic.Type.RESORT_TRANSPARENCY) {
                class_1921 layer = class_1921.method_23583();
                if (!chunkRenderData.isBlockLayerEmpty(layer)) {
                    futuresList.add(this.chunkRenderDispatcher.uploadChunkBlocks(class_1921.method_23583(), allocators, renderChunk, chunkRenderData, task.getDistanceSq(), true, profiler));
                }
                if (!chunkRenderData.isOverlayTypeEmpty(OverlayRenderType.QUAD)) {
                    futuresList.add(this.chunkRenderDispatcher.uploadChunkOverlay(OverlayRenderType.QUAD, allocators, renderChunk, chunkRenderData, task.getDistanceSq(), true, profiler));
                }
            }
            profiler.method_15405("run_task_later_" + taskType.name());
            final ListenableFuture listenablefuture = Futures.allAsList((Iterable)futuresList);
            task.addFinishRunnable(new Runnable(){

                @Override
                public void run() {
                    listenablefuture.cancel(false);
                }
            });
            Futures.addCallback((ListenableFuture)listenablefuture, (FutureCallback)new FutureCallback<List<Object>>(){

                public void onSuccess(@Nullable List<Object> list) {
                    block6: {
                        ChunkRenderWorkerLitematica.this.clearRenderAllocators(task);
                        task.getLock().lock();
                        try {
                            if (task.getStatus() == ChunkRenderTaskSchematic.Status.UPLOADING) {
                                task.setStatus(ChunkRenderTaskSchematic.Status.DONE);
                                break block6;
                            }
                            if (!task.isFinished()) {
                                LOGGER.warn("Chunk render task was {} when I expected it to be uploading; aborting task", (Object)task.getStatus());
                            }
                        }
                        finally {
                            task.getLock().unlock();
                        }
                        return;
                    }
                    task.getRenderChunk().setChunkRenderData(chunkRenderData);
                }

                public void onFailure(Throwable throwable) {
                    ChunkRenderWorkerLitematica.this.resetRenderAllocators(task);
                    if (!(throwable instanceof CancellationException) && !(throwable instanceof InterruptedException)) {
                        class_310.method_1551().method_43587(class_128.method_560((Throwable)throwable, (String)"Rendering Litematica chunk"));
                    }
                }
            }, (Executor)MoreExecutors.directExecutor());
        }
        profiler.method_15407();
    }

    @Nullable
    private BufferAllocatorCache getRegionRenderAllocatorCache() throws InterruptedException {
        return this.allocatorCache != null ? this.allocatorCache : this.chunkRenderDispatcher.allocateRenderAllocators();
    }

    private void clearRenderAllocators(ChunkRenderTaskSchematic generator) {
        BufferAllocatorCache bufferAllocatorCache = generator.getAllocatorCache();
        bufferAllocatorCache.clearAll();
        if (this.allocatorCache == null) {
            this.chunkRenderDispatcher.freeRenderAllocators(bufferAllocatorCache);
        }
    }

    private void resetRenderAllocators(ChunkRenderTaskSchematic generator) {
        BufferAllocatorCache bufferAllocatorCache = generator.getAllocatorCache();
        bufferAllocatorCache.resetAll();
        if (this.allocatorCache == null) {
            this.chunkRenderDispatcher.freeRenderAllocators(bufferAllocatorCache);
        }
    }

    public void notifyToStop() {
        this.shouldRun = false;
    }
}

