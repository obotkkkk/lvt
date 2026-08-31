/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1921
 *  net.minecraft.class_9801
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1921;
import net.minecraft.class_9801;

public class BuiltBufferCache
implements AutoCloseable {
    private final ConcurrentHashMap<class_1921, class_9801> layerBuffers = new ConcurrentHashMap();
    private final ConcurrentHashMap<OverlayRenderType, class_9801> overlayBuffers = new ConcurrentHashMap();

    protected BuiltBufferCache() {
    }

    protected boolean hasBuiltBufferByLayer(class_1921 layer) {
        return this.layerBuffers.containsKey(layer);
    }

    protected boolean hasBuiltBufferByType(OverlayRenderType type) {
        return this.overlayBuffers.containsKey((Object)type);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void storeBuiltBufferByLayer(class_1921 layer, @Nonnull class_9801 newBuffer) {
        if (this.hasBuiltBufferByLayer(layer)) {
            this.layerBuffers.get(layer).close();
        }
        ConcurrentHashMap<class_1921, class_9801> concurrentHashMap = this.layerBuffers;
        synchronized (concurrentHashMap) {
            this.layerBuffers.put(layer, newBuffer);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void storeBuiltBufferByType(OverlayRenderType type, @Nonnull class_9801 newBuffer) {
        if (this.hasBuiltBufferByType(type)) {
            this.overlayBuffers.get((Object)type).close();
        }
        ConcurrentHashMap<OverlayRenderType, class_9801> concurrentHashMap = this.overlayBuffers;
        synchronized (concurrentHashMap) {
            this.overlayBuffers.put(type, newBuffer);
        }
    }

    @Nullable
    protected class_9801 getBuiltBufferByLayer(class_1921 layer) {
        return this.layerBuffers.get(layer);
    }

    @Nullable
    protected class_9801 getBuiltBufferByType(OverlayRenderType type) {
        return this.overlayBuffers.get((Object)type);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void closeAll() {
        ArrayList<class_9801> builtBuffers;
        ConcurrentHashMap<Object, class_9801> concurrentHashMap = this.layerBuffers;
        synchronized (concurrentHashMap) {
            builtBuffers = new ArrayList<class_9801>(this.layerBuffers.values());
            this.layerBuffers.clear();
        }
        concurrentHashMap = this.overlayBuffers;
        synchronized (concurrentHashMap) {
            builtBuffers.addAll(this.overlayBuffers.values());
            this.overlayBuffers.clear();
        }
        try {
            builtBuffers.forEach(class_9801::close);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void close() throws Exception {
        this.closeAll();
    }
}

