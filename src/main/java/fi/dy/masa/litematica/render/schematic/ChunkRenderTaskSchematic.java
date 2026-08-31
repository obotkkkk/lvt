/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.primitives.Doubles
 *  net.minecraft.class_243
 */
package fi.dy.masa.litematica.render.schematic;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import net.minecraft.class_243;

public class ChunkRenderTaskSchematic
implements Comparable<ChunkRenderTaskSchematic> {
    private final ChunkRendererSchematicVbo chunkRenderer;
    private final Type type;
    private final List<Runnable> listFinishRunnables;
    private final ReentrantLock lock;
    private final Supplier<class_243> cameraPosSupplier;
    private final double distanceSq;
    private BufferAllocatorCache allocatorCache;
    private ChunkRenderDataSchematic chunkRenderData;
    private Status status;
    private boolean finished;

    public ChunkRenderTaskSchematic(ChunkRendererSchematicVbo renderChunkIn, Type typeIn, Supplier<class_243> cameraPosSupplier, double distanceSqIn) {
        this.chunkRenderer = renderChunkIn;
        this.type = typeIn;
        this.listFinishRunnables = Lists.newArrayList();
        this.lock = new ReentrantLock();
        this.cameraPosSupplier = cameraPosSupplier;
        this.distanceSq = distanceSqIn;
        this.status = Status.PENDING;
    }

    public Supplier<class_243> getCameraPosSupplier() {
        return this.cameraPosSupplier;
    }

    public Status getStatus() {
        return this.status;
    }

    protected ChunkRendererSchematicVbo getRenderChunk() {
        return this.chunkRenderer;
    }

    protected ChunkRenderDataSchematic getChunkRenderData() {
        return this.chunkRenderData;
    }

    protected void setChunkRenderData(ChunkRenderDataSchematic chunkRenderData) {
        if (this.chunkRenderData != null) {
            this.chunkRenderData.clearAll();
        }
        this.chunkRenderData = chunkRenderData;
    }

    public BufferAllocatorCache getAllocatorCache() {
        return this.allocatorCache;
    }

    public boolean setRegionRenderCacheBuilder(BufferAllocatorCache allocatorCache) {
        if (allocatorCache == null) {
            Litematica.LOGGER.error("setRegionRenderCacheBuilder() [Task] allocatorCache is null");
            return false;
        }
        if (this.allocatorCache != null) {
            this.allocatorCache.closeAll();
        }
        this.allocatorCache = allocatorCache;
        return true;
    }

    protected void setStatus(Status statusIn) {
        this.lock.lock();
        try {
            this.status = statusIn;
        }
        finally {
            this.lock.unlock();
        }
    }

    protected void finish() {
        this.lock.lock();
        try {
            if (this.type == Type.REBUILD_CHUNK && this.status != Status.DONE) {
                this.chunkRenderer.setNeedsUpdate(false);
            }
            this.finished = true;
            this.status = Status.DONE;
            for (Runnable runnable : this.listFinishRunnables) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    protected void addFinishRunnable(Runnable runnable) {
        this.lock.lock();
        try {
            this.listFinishRunnables.add(runnable);
            if (this.finished) {
                runnable.run();
            }
        }
        finally {
            this.lock.unlock();
        }
    }

    public ReentrantLock getLock() {
        return this.lock;
    }

    protected Type getType() {
        return this.type;
    }

    protected boolean isFinished() {
        return this.finished;
    }

    @Override
    public int compareTo(ChunkRenderTaskSchematic other) {
        return Doubles.compare((double)this.distanceSq, (double)other.distanceSq);
    }

    public double getDistanceSq() {
        return this.distanceSq;
    }

    public static enum Type {
        REBUILD_CHUNK,
        RESORT_TRANSPARENCY;

    }

    public static enum Status {
        PENDING,
        COMPILING,
        UPLOADING,
        DONE;

    }
}

