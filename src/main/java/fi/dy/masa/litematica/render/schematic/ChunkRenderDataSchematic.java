/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArraySet
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1921
 *  net.minecraft.class_2586
 *  net.minecraft.class_9801$class_9802
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.BuiltBufferCache;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1921;
import net.minecraft.class_2586;
import net.minecraft.class_9801;

public class ChunkRenderDataSchematic
implements AutoCloseable {
    public static final ChunkRenderDataSchematic EMPTY = new ChunkRenderDataSchematic(){

        @Override
        protected void setBlockLayerUsed(class_1921 layer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void setBlockLayerStarted(class_1921 layer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void setOverlayTypeUsed(OverlayRenderType layer) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void setOverlayTypeStarted(OverlayRenderType layer) {
            throw new UnsupportedOperationException();
        }
    };
    private final List<class_2586> blockEntities = new ArrayList<class_2586>();
    private final Set<class_1921> blockLayersUsed = new ObjectArraySet();
    private final Set<class_1921> blockLayersStarted = new ObjectArraySet();
    private final Set<OverlayRenderType> overlayLayersUsed = new ObjectArraySet();
    private final Set<OverlayRenderType> overlayLayersStarted = new ObjectArraySet();
    private final BuiltBufferCache builtBufferCache = new BuiltBufferCache();
    private final Map<OverlayRenderType, class_9801.class_9802> overlaySortingData = new HashMap<OverlayRenderType, class_9801.class_9802>();
    private class_9801.class_9802 transparentSortingData = null;
    private boolean overlayEmpty = true;
    private boolean empty = true;
    private long timeBuilt;

    public boolean isEmpty() {
        return this.empty;
    }

    public boolean isBlockLayerEmpty(class_1921 layer) {
        return !this.blockLayersUsed.contains(layer);
    }

    public boolean isBlockLayerStarted(class_1921 layer) {
        return this.blockLayersStarted.contains(layer);
    }

    protected void setBlockLayerStarted(class_1921 layer) {
        this.blockLayersStarted.add(layer);
    }

    protected void setBlockLayerUsed(class_1921 layer) {
        this.empty = false;
        this.blockLayersUsed.add(layer);
    }

    protected void setBlockLayerUnused(class_1921 layer) {
        this.blockLayersStarted.remove(layer);
        this.blockLayersUsed.remove(layer);
    }

    public boolean isOverlayEmpty() {
        return this.overlayEmpty;
    }

    public boolean isOverlayTypeEmpty(OverlayRenderType type) {
        return !this.overlayLayersUsed.contains((Object)type);
    }

    protected void setOverlayTypeStarted(OverlayRenderType type) {
        this.overlayLayersStarted.add(type);
    }

    public boolean isOverlayTypeStarted(OverlayRenderType type) {
        return this.overlayLayersStarted.contains((Object)type);
    }

    protected void setOverlayTypeUsed(OverlayRenderType type) {
        this.overlayEmpty = false;
        this.overlayLayersUsed.add(type);
    }

    protected void setOverlayTypeUnused(OverlayRenderType type) {
        this.overlayLayersStarted.remove((Object)type);
        this.overlayLayersUsed.remove((Object)type);
    }

    public List<class_2586> getBlockEntities() {
        return this.blockEntities;
    }

    protected void addBlockEntity(class_2586 be) {
        this.blockEntities.add(be);
    }

    protected BuiltBufferCache getBuiltBufferCache() {
        return this.builtBufferCache;
    }

    protected void closeBuiltBufferCache() {
        this.builtBufferCache.closeAll();
    }

    public boolean hasTransparentSortingData() {
        return this.transparentSortingData != null;
    }

    public boolean hasTransparentSortingDataForOverlay(OverlayRenderType type) {
        return this.overlaySortingData.get((Object)type) != null;
    }

    protected void setTransparentSortingData(@Nonnull class_9801.class_9802 transparentSortingData) {
        this.transparentSortingData = transparentSortingData;
    }

    protected void setTransparentSortingDataForOverlay(OverlayRenderType type, @Nonnull class_9801.class_9802 transparentSortingData) {
        this.overlaySortingData.put(type, transparentSortingData);
    }

    protected class_9801.class_9802 getTransparentSortingData() {
        return this.transparentSortingData;
    }

    @Nullable
    protected class_9801.class_9802 getTransparentSortingDataForOverlay(OverlayRenderType type) {
        return this.overlaySortingData.get((Object)type);
    }

    public long getTimeBuilt() {
        return this.timeBuilt;
    }

    protected void setTimeBuilt(long time) {
        this.timeBuilt = time;
    }

    protected void clearAll() {
        this.closeBuiltBufferCache();
        this.timeBuilt = 0L;
        this.overlaySortingData.clear();
        this.transparentSortingData = null;
        this.blockLayersUsed.clear();
        this.overlayLayersUsed.clear();
        this.blockLayersStarted.clear();
        this.overlayLayersStarted.clear();
        this.blockEntities.clear();
        this.overlayEmpty = true;
        this.empty = true;
    }

    @Override
    public void close() throws Exception {
        this.clearAll();
    }
}

