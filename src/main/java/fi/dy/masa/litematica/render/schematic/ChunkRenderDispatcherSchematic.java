/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import fi.dy.masa.litematica.render.schematic.IChunkRendererFactory;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.world.WorldSchematic;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.class_1923;

public class ChunkRenderDispatcherSchematic {
    protected final Long2ObjectOpenHashMap<ChunkRendererSchematicVbo> chunkRenderers = new Long2ObjectOpenHashMap();
    protected final WorldRendererSchematic renderer;
    protected final IChunkRendererFactory chunkRendererFactory;
    protected final WorldSchematic world;
    protected int viewDistanceChunks;
    protected int viewDistanceBlocksSq;

    protected ChunkRenderDispatcherSchematic(WorldSchematic world, int viewDistanceChunks, WorldRendererSchematic worldRenderer, IChunkRendererFactory factory) {
        this.chunkRendererFactory = factory;
        this.renderer = worldRenderer;
        this.world = world;
        this.setViewDistanceChunks(viewDistanceChunks);
    }

    protected void setViewDistanceChunks(int viewDistanceChunks) {
        this.viewDistanceChunks = viewDistanceChunks;
        this.viewDistanceBlocksSq = viewDistanceChunks + 2 << 4;
        this.viewDistanceBlocksSq *= this.viewDistanceBlocksSq;
    }

    protected void delete() {
        for (ChunkRendererSchematicVbo chunkRenderer : this.chunkRenderers.values()) {
            chunkRenderer.deleteGlResources();
        }
        this.chunkRenderers.clear();
    }

    private boolean rendererOutOfRange(ChunkRendererSchematicVbo cr) {
        if (cr.getDistanceSq() > (double)this.viewDistanceBlocksSq) {
            cr.deleteGlResources();
            return true;
        }
        return false;
    }

    protected void removeOutOfRangeRenderers() {
        this.chunkRenderers.values().removeIf(this::rendererOutOfRange);
    }

    protected void scheduleChunkRender(int chunkX, int chunkZ) {
        this.getOrCreateChunkRenderer(chunkX, chunkZ).setNeedsUpdate(false);
    }

    protected int getRendererCount() {
        return this.chunkRenderers.size();
    }

    protected ChunkRendererSchematicVbo getOrCreateChunkRenderer(int chunkX, int chunkZ) {
        long index = class_1923.method_8331((int)chunkX, (int)chunkZ);
        ChunkRendererSchematicVbo renderer = (ChunkRendererSchematicVbo)this.chunkRenderers.get(index);
        if (renderer == null) {
            renderer = this.chunkRendererFactory.create(this.world, this.renderer);
            renderer.setPosition(chunkX << 4, this.world.method_31607(), chunkZ << 4);
            this.chunkRenderers.put(index, (Object)renderer);
        }
        return renderer;
    }

    @Nullable
    protected ChunkRendererSchematicVbo getChunkRenderer(int chunkX, int chunkZ) {
        return this.getOrCreateChunkRenderer(chunkX, chunkZ);
    }
}

