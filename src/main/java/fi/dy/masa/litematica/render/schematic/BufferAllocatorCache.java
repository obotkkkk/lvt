/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1921
 *  net.minecraft.class_9799
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.ChunkRenderLayers;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1921;
import net.minecraft.class_9799;

@Environment(value=EnvType.CLIENT)
public class BufferAllocatorCache
implements AutoCloseable {
    protected static final List<class_1921> LAYERS = ChunkRenderLayers.LAYERS;
    protected static final List<OverlayRenderType> TYPES = ChunkRenderLayers.TYPES;
    protected static final int EXPECTED_TOTAL_SIZE = LAYERS.stream().mapToInt(class_1921::method_22722).sum() + TYPES.stream().mapToInt(OverlayRenderType::getExpectedBufferSize).sum();
    private final ConcurrentHashMap<class_1921, class_9799> layerCache = new ConcurrentHashMap();
    private final ConcurrentHashMap<OverlayRenderType, class_9799> overlayCache = new ConcurrentHashMap();
    private boolean clear = true;

    protected BufferAllocatorCache() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void allocateCache() {
        ConcurrentHashMap<Object, class_9799> concurrentHashMap;
        for (class_1921 layer : LAYERS) {
            if (this.layerCache.containsKey(layer)) {
                this.layerCache.get(layer).close();
            }
            concurrentHashMap = this.layerCache;
            synchronized (concurrentHashMap) {
                this.layerCache.put(layer, new class_9799(layer.method_22722()));
            }
        }
        for (OverlayRenderType type : TYPES) {
            if (this.overlayCache.containsKey((Object)type)) {
                this.overlayCache.get((Object)type).close();
            }
            concurrentHashMap = this.overlayCache;
            synchronized (concurrentHashMap) {
                this.overlayCache.put(type, new class_9799(type.getExpectedBufferSize()));
            }
        }
        this.clear = true;
    }

    protected boolean hasBufferByLayer(class_1921 layer) {
        return this.layerCache.containsKey(layer);
    }

    protected boolean hasBufferByOverlay(OverlayRenderType type) {
        return this.overlayCache.containsKey((Object)type);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected class_9799 getBufferByLayer(class_1921 layer) {
        this.clear = false;
        ConcurrentHashMap<class_1921, class_9799> concurrentHashMap = this.layerCache;
        synchronized (concurrentHashMap) {
            return this.layerCache.computeIfAbsent(layer, l -> new class_9799(l.method_22722()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected class_9799 getBufferByOverlay(OverlayRenderType type) {
        this.clear = false;
        ConcurrentHashMap<OverlayRenderType, class_9799> concurrentHashMap = this.overlayCache;
        synchronized (concurrentHashMap) {
            return this.overlayCache.computeIfAbsent(type, t -> new class_9799(t.getExpectedBufferSize()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void closeByLayer(class_1921 layer) {
        try {
            ConcurrentHashMap<class_1921, class_9799> concurrentHashMap = this.layerCache;
            synchronized (concurrentHashMap) {
                this.layerCache.remove(layer).close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void closeByType(OverlayRenderType type) {
        try {
            ConcurrentHashMap<OverlayRenderType, class_9799> concurrentHashMap = this.overlayCache;
            synchronized (concurrentHashMap) {
                this.overlayCache.remove((Object)type).close();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    protected boolean isClear() {
        return this.clear;
    }

    protected void resetAll() {
        try {
            this.layerCache.values().forEach(class_9799::method_60811);
            this.overlayCache.values().forEach(class_9799::method_60811);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.clear = true;
    }

    protected void clearAll() {
        try {
            this.layerCache.values().forEach(class_9799::method_60809);
            this.overlayCache.values().forEach(class_9799::method_60809);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.clear = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void closeAll() {
        ArrayList<class_9799> allocators;
        ConcurrentHashMap<Object, class_9799> concurrentHashMap = this.layerCache;
        synchronized (concurrentHashMap) {
            allocators = new ArrayList<class_9799>(this.layerCache.values());
            this.layerCache.clear();
        }
        concurrentHashMap = this.overlayCache;
        synchronized (concurrentHashMap) {
            allocators.addAll(this.overlayCache.values());
            this.overlayCache.clear();
        }
        allocators.forEach(class_9799::close);
        this.clear = true;
    }

    @Override
    public void close() {
        this.closeAll();
    }
}

