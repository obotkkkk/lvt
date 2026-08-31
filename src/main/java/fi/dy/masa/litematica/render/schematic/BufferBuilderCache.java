/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.class_1921
 *  net.minecraft.class_287
 *  net.minecraft.class_9801
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import net.minecraft.class_1921;
import net.minecraft.class_287;
import net.minecraft.class_9801;

public class BufferBuilderCache
implements AutoCloseable {
    private final ConcurrentHashMap<class_1921, class_287> blockBufferBuilders = new ConcurrentHashMap();
    private final ConcurrentHashMap<OverlayRenderType, class_287> overlayBufferBuilders = new ConcurrentHashMap();

    protected BufferBuilderCache() {
    }

    protected boolean hasBufferByLayer(class_1921 layer) {
        return this.blockBufferBuilders.containsKey(layer);
    }

    protected boolean hasBufferByOverlay(OverlayRenderType type) {
        return this.overlayBufferBuilders.containsKey((Object)type);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected class_287 getBufferByLayer(class_1921 layer, @Nonnull BufferAllocatorCache allocators) {
        ConcurrentHashMap<class_1921, class_287> concurrentHashMap = this.blockBufferBuilders;
        synchronized (concurrentHashMap) {
            return this.blockBufferBuilders.computeIfAbsent(layer, key -> new class_287(allocators.getBufferByLayer((class_1921)key), key.method_23033(), key.method_23031()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected class_287 getBufferByOverlay(OverlayRenderType type, @Nonnull BufferAllocatorCache allocators) {
        ConcurrentHashMap<OverlayRenderType, class_287> concurrentHashMap = this.overlayBufferBuilders;
        synchronized (concurrentHashMap) {
            return this.overlayBufferBuilders.computeIfAbsent(type, key -> new class_287(allocators.getBufferByOverlay((OverlayRenderType)((Object)key)), key.getDrawMode(), key.getVertexFormat()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void clearAll() {
        ArrayList<class_287> buffers;
        Object object = this.blockBufferBuilders;
        synchronized (object) {
            buffers = new ArrayList<class_287>(this.blockBufferBuilders.values());
            this.blockBufferBuilders.clear();
        }
        object = this.overlayBufferBuilders;
        synchronized (object) {
            buffers.addAll(this.overlayBufferBuilders.values());
            this.overlayBufferBuilders.clear();
        }
        for (class_287 buffer : buffers) {
            class_9801 built;
            if (!buffer.field_1556 || (built = buffer.method_60794()) == null) continue;
            built.close();
        }
    }

    @Override
    public void close() throws Exception {
        this.clearAll();
    }
}

