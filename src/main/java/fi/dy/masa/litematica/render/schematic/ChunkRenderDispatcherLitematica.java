/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  com.google.common.primitives.Doubles
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  javax.annotation.Nonnull
 *  net.minecraft.class_1921
 *  net.minecraft.class_243
 *  net.minecraft.class_291
 *  net.minecraft.class_310
 *  net.minecraft.class_3695
 *  net.minecraft.class_8251
 *  net.minecraft.class_9799
 *  net.minecraft.class_9799$class_9800
 *  net.minecraft.class_9801
 *  net.minecraft.class_9801$class_9802
 *  org.apache.logging.log4j.Logger
 */
package fi.dy.masa.litematica.render.schematic;

import com.google.common.collect.Queues;
import com.google.common.primitives.Doubles;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderLayers;
import fi.dy.masa.litematica.render.schematic.ChunkRenderTaskSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderWorkerLitematica;
import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import javax.annotation.Nonnull;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_291;
import net.minecraft.class_310;
import net.minecraft.class_3695;
import net.minecraft.class_8251;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import org.apache.logging.log4j.Logger;

public class ChunkRenderDispatcherLitematica {
    private static final Logger LOGGER = Litematica.LOGGER;
    private final List<Thread> listWorkerThreads = new ArrayList<Thread>();
    private final List<ChunkRenderWorkerLitematica> listThreadedWorkers = new ArrayList<ChunkRenderWorkerLitematica>();
    private final PriorityBlockingQueue<ChunkRenderTaskSchematic> queueChunkUpdates = Queues.newPriorityBlockingQueue();
    private final BlockingQueue<BufferAllocatorCache> queueFreeRenderAllocators;
    private final Queue<PendingUpload> queueChunkUploads = Queues.newPriorityQueue();
    private final ChunkRenderWorkerLitematica renderWorker;
    private final int countRenderAllocators;
    private class_243 cameraPos = class_243.field_1353;

    public ChunkRenderDispatcherLitematica(class_3695 profiler) {
        this.countRenderAllocators = 2;
        LOGGER.info("Using {} total BufferAllocator caches", (Object)(this.countRenderAllocators + 1));
        this.queueFreeRenderAllocators = Queues.newArrayBlockingQueue((int)this.countRenderAllocators);
        for (int i = 0; i < this.countRenderAllocators; ++i) {
            this.queueFreeRenderAllocators.add(new BufferAllocatorCache());
        }
        this.renderWorker = new ChunkRenderWorkerLitematica(this, new BufferAllocatorCache(), profiler);
    }

    protected void setCameraPosition(class_243 cameraPos) {
        this.cameraPos = cameraPos;
    }

    public class_243 getCameraPos() {
        return this.cameraPos;
    }

    protected String getDebugInfo() {
        return this.listWorkerThreads.isEmpty() ? String.format("pC: %03d, single-threaded", this.queueChunkUpdates.size()) : String.format("pC: %03d, pU: %1d, aB: %1d", this.queueChunkUpdates.size(), this.queueChunkUploads.size(), this.queueFreeRenderAllocators.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected boolean runChunkUploads(long finishTimeNano, class_3695 profiler) {
        boolean processedTask;
        boolean ranTasks = false;
        profiler.method_15396("run_chunk_uploads");
        do {
            ChunkRenderTaskSchematic generator;
            processedTask = false;
            if (this.listWorkerThreads.isEmpty() && (generator = this.queueChunkUpdates.poll()) != null) {
                try {
                    this.renderWorker.processTask(generator, profiler);
                    processedTask = true;
                }
                catch (InterruptedException e) {
                    LOGGER.warn("runChunkUploads(): Process Interrupted; error message: [{}]", (Object)e.getLocalizedMessage());
                }
            }
            Queue<PendingUpload> queue = this.queueChunkUploads;
            synchronized (queue) {
                if (!this.queueChunkUploads.isEmpty()) {
                    this.queueChunkUploads.poll().uploadTask.run();
                    processedTask = true;
                    ranTasks = true;
                }
            }
        } while (finishTimeNano != 0L && processedTask && finishTimeNano >= System.nanoTime());
        profiler.method_15407();
        return ranTasks;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected boolean updateChunkLater(ChunkRendererSchematicVbo renderChunk, class_3695 profiler) {
        boolean flag1;
        profiler.method_15396("update_chunk_later");
        renderChunk.getLockCompileTask().lock();
        try {
            final ChunkRenderTaskSchematic generator = renderChunk.makeCompileTaskChunkSchematic(this::getCameraPos);
            generator.addFinishRunnable(new Runnable(){

                @Override
                public void run() {
                    ChunkRenderDispatcherLitematica.this.queueChunkUpdates.remove(generator);
                }
            });
            boolean flag = this.queueChunkUpdates.offer(generator);
            if (!flag) {
                generator.finish();
            }
            flag1 = flag;
        }
        finally {
            renderChunk.getLockCompileTask().unlock();
        }
        profiler.method_15407();
        return flag1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected boolean updateChunkNow(ChunkRendererSchematicVbo chunkRenderer, class_3695 profiler) {
        boolean flag;
        profiler.method_15396("update_chunk_now");
        chunkRenderer.getLockCompileTask().lock();
        try {
            ChunkRenderTaskSchematic generator = chunkRenderer.makeCompileTaskChunkSchematic(this::getCameraPos);
            try {
                this.renderWorker.processTask(generator, profiler);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            flag = true;
        }
        finally {
            chunkRenderer.getLockCompileTask().unlock();
        }
        profiler.method_15407();
        return flag;
    }

    protected void stopChunkUpdates(class_3695 profiler) {
        profiler.method_15396("stop_chunk_updates");
        this.clearChunkUpdates();
        ArrayList<BufferAllocatorCache> list = new ArrayList<BufferAllocatorCache>();
        while (list.size() != this.countRenderAllocators) {
            this.runChunkUploads(Long.MAX_VALUE, profiler);
            try {
                list.add(this.allocateRenderAllocators());
            }
            catch (InterruptedException e) {
                LOGGER.warn("stopChunkUpdates(): Process Interrupted; error message: [{}]", (Object)e.getLocalizedMessage());
            }
        }
        this.queueFreeRenderAllocators.addAll(list);
        profiler.method_15407();
    }

    public void freeRenderAllocators(BufferAllocatorCache allocatorCache) {
        if (allocatorCache != null) {
            try {
                allocatorCache.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        allocatorCache = new BufferAllocatorCache();
        this.queueFreeRenderAllocators.add(allocatorCache);
    }

    public BufferAllocatorCache allocateRenderAllocators() throws InterruptedException {
        return this.queueFreeRenderAllocators.take();
    }

    protected ChunkRenderTaskSchematic getNextChunkUpdate() throws InterruptedException {
        return this.queueChunkUpdates.take();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected boolean updateTransparencyLater(ChunkRendererSchematicVbo renderChunk, class_3695 profiler) {
        boolean flag;
        profiler.method_15396("update_transparency_later");
        renderChunk.getLockCompileTask().lock();
        try {
            final ChunkRenderTaskSchematic generator = renderChunk.makeCompileTaskTransparencySchematic(this::getCameraPos);
            if (generator == null) {
                boolean flag2 = true;
                profiler.method_15407();
                boolean bl = flag2;
                return bl;
            }
            generator.addFinishRunnable(new Runnable(){

                @Override
                public void run() {
                    ChunkRenderDispatcherLitematica.this.queueChunkUpdates.remove(generator);
                }
            });
            flag = this.queueChunkUpdates.offer(generator);
        }
        finally {
            renderChunk.getLockCompileTask().unlock();
        }
        profiler.method_15407();
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected ListenableFuture<Object> uploadChunkBlocks(final class_1921 layer, final BufferAllocatorCache allocators, final ChunkRendererSchematicVbo renderChunk, final ChunkRenderDataSchematic chunkRenderData, final double distanceSq, final boolean resortOnly, final class_3695 profiler) {
        profiler.method_15396("upload_chunk_blocks");
        if (class_310.method_1551().method_18854()) {
            try {
                this.uploadVertexBufferByLayer(layer, allocators, renderChunk, chunkRenderData, renderChunk.createVertexSorter(this.getCameraPos(), renderChunk.getOrigin()), resortOnly, profiler);
            }
            catch (Exception e) {
                LOGGER.warn("uploadChunkBlocks(): [Dispatch] Error uploading Vertex Buffer for layer [{}], Caught error: [{}]", (Object)ChunkRenderLayers.getFriendlyName(layer), (Object)e.toString());
            }
            profiler.method_15407();
            return Futures.immediateFuture(null);
        }
        profiler.method_15405("upload_chunk_blocks_later");
        ListenableFutureTask futureTask = ListenableFutureTask.create((Runnable)new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcherLitematica.this.uploadChunkBlocks(layer, allocators, renderChunk, chunkRenderData, distanceSq, resortOnly, profiler);
            }
        }, null);
        Queue<PendingUpload> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add(new PendingUpload((ListenableFutureTask<Object>)futureTask, distanceSq));
            profiler.method_15407();
            return futureTask;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected ListenableFuture<Object> uploadChunkOverlay(final OverlayRenderType type, final BufferAllocatorCache allocators, final ChunkRendererSchematicVbo renderChunk, final ChunkRenderDataSchematic compiledChunk, final double distanceSq, final boolean resortOnly, final class_3695 profiler) {
        profiler.method_15396("upload_chunk_overlay");
        if (class_310.method_1551().method_18854()) {
            try {
                this.uploadVertexBufferByType(type, allocators, renderChunk, compiledChunk, renderChunk.createVertexSorter(this.getCameraPos(), renderChunk.getOrigin()), resortOnly, profiler);
            }
            catch (Exception e) {
                LOGGER.warn("uploadChunkOverlay(): [Dispatch] Error uploading Vertex Buffer for overlay type [{}], Caught error: [{}]", (Object)type.getDrawMode().name(), (Object)e.toString());
            }
            profiler.method_15407();
            return Futures.immediateFuture(null);
        }
        profiler.method_15405("upload_chunk_overlay_later");
        ListenableFutureTask futureTask = ListenableFutureTask.create((Runnable)new Runnable(){

            @Override
            public void run() {
                ChunkRenderDispatcherLitematica.this.uploadChunkOverlay(type, allocators, renderChunk, compiledChunk, distanceSq, resortOnly, profiler);
            }
        }, null);
        Queue<PendingUpload> queue = this.queueChunkUploads;
        synchronized (queue) {
            this.queueChunkUploads.add(new PendingUpload((ListenableFutureTask<Object>)futureTask, distanceSq));
            profiler.method_15407();
            return futureTask;
        }
    }

    private void uploadVertexBufferByLayer(class_1921 layer, @Nonnull BufferAllocatorCache allocators, @Nonnull ChunkRendererSchematicVbo renderChunk, @Nonnull ChunkRenderDataSchematic compiledChunk, @Nonnull class_8251 sorter, boolean resortOnly, class_3695 profiler) throws InterruptedException {
        profiler.method_15396("upload_vbo_layer_" + layer.toString());
        class_9799 allocator = allocators.getBufferByLayer(layer);
        class_9801 renderBuffer = compiledChunk.getBuiltBufferCache().getBuiltBufferByLayer(layer);
        if (allocator == null) {
            allocators.closeByLayer(layer);
            compiledChunk.setBlockLayerUnused(layer);
            profiler.method_15407();
            throw new InterruptedException("BufferAllocators are invalid");
        }
        if (renderBuffer == null) {
            compiledChunk.setBlockLayerUnused(layer);
            profiler.method_15407();
            return;
        }
        class_291 vertexBuffer = renderChunk.getBlocksVertexBufferByLayer(layer);
        if (layer == class_1921.method_23583() && Configs.Visuals.RENDER_ENABLE_TRANSLUCENT_RESORTING.getBooleanValue()) {
            class_9799.class_9800 result;
            class_9801.class_9802 sorting = compiledChunk.getTransparentSortingData();
            if (sorting == null) {
                sorting = renderBuffer.method_60819(allocator, sorter);
                if (sorting == null) {
                    profiler.method_15407();
                    throw new InterruptedException("Sort State failed to sortQuads()");
                }
                compiledChunk.setTransparentSortingData(sorting);
            }
            if ((result = sorting.method_60824(allocator, sorter)) != null) {
                renderChunk.uploadSortingState(result, vertexBuffer);
                result.close();
            }
        }
        if (!resortOnly) {
            renderChunk.uploadBuiltBuffer(renderBuffer, vertexBuffer);
        }
        profiler.method_15407();
    }

    private void uploadVertexBufferByType(OverlayRenderType type, @Nonnull BufferAllocatorCache allocators, @Nonnull ChunkRendererSchematicVbo renderChunk, @Nonnull ChunkRenderDataSchematic compiledChunk, @Nonnull class_8251 sorter, boolean resortOnly, class_3695 profiler) throws InterruptedException {
        profiler.method_15396("upload_vbo_overlay_" + type.name());
        class_9799 allocator = allocators.getBufferByOverlay(type);
        class_9801 renderBuffer = compiledChunk.getBuiltBufferCache().getBuiltBufferByType(type);
        if (allocator == null) {
            allocators.closeByType(type);
            compiledChunk.setOverlayTypeUnused(type);
            profiler.method_15407();
            throw new InterruptedException("BufferAllocators are invalid");
        }
        if (renderBuffer == null) {
            compiledChunk.setOverlayTypeUnused(type);
            profiler.method_15407();
            return;
        }
        class_291 vertexBuffer = renderChunk.getOverlayVertexBuffer(type);
        if (!resortOnly) {
            renderChunk.uploadBuiltBuffer(renderBuffer, vertexBuffer);
        }
        profiler.method_15407();
    }

    protected void clearChunkUpdates() {
        while (!this.queueChunkUpdates.isEmpty()) {
            ChunkRenderTaskSchematic generator = this.queueChunkUpdates.poll();
            if (generator == null) continue;
            generator.finish();
        }
    }

    public boolean hasChunkUpdates() {
        return this.queueChunkUpdates.isEmpty() && this.queueChunkUploads.isEmpty();
    }

    protected void stopWorkerThreads() {
        this.clearChunkUpdates();
        for (ChunkRenderWorkerLitematica worker : this.listThreadedWorkers) {
            worker.notifyToStop();
        }
        for (Thread thread : this.listWorkerThreads) {
            try {
                thread.interrupt();
                thread.join();
            }
            catch (InterruptedException interruptedexception) {
                LOGGER.warn("Interrupted whilst waiting for worker to die", (Throwable)interruptedexception);
            }
        }
        this.queueFreeRenderAllocators.forEach(BufferAllocatorCache::close);
        this.queueFreeRenderAllocators.clear();
    }

    public boolean hasNoFreeRenderAllocators() {
        return this.queueFreeRenderAllocators.isEmpty();
    }

    protected static class PendingUpload
    implements Comparable<PendingUpload> {
        private final ListenableFutureTask<Object> uploadTask;
        private final double distanceSq;

        public PendingUpload(ListenableFutureTask<Object> uploadTaskIn, double distanceSqIn) {
            this.uploadTask = uploadTaskIn;
            this.distanceSq = distanceSqIn;
        }

        @Override
        public int compareTo(PendingUpload other) {
            return Doubles.compare((double)this.distanceSq, (double)other.distanceSq);
        }
    }
}

