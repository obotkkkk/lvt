/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IRangeChangeListener
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 *  net.minecraft.class_310
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.interfaces.IRangeChangeListener;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_310;

public class SchematicWorldRefresher
implements IRangeChangeListener {
    public static final SchematicWorldRefresher INSTANCE = new SchematicWorldRefresher();
    private final class_310 mc = class_310.method_1551();

    public void updateAll() {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null) {
            DataManager.getSchematicPlacementManager().setVisibleSubChunksNeedsUpdate();
            int minY = world.method_31607();
            int maxY = world.method_31600() - 1;
            this.updateBetweenY(minY, maxY);
        }
    }

    public void updateBetweenX(int minX, int maxX) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null) {
            DataManager.getSchematicPlacementManager().setVisibleSubChunksNeedsUpdate();
            Long2ObjectMap<ChunkSchematic> schematicChunks = world.getChunkProvider().getLoadedChunks();
            int cxMin = Math.min(minX, maxX) >> 4;
            int cxMax = Math.max(minX, maxX) >> 4;
            for (ChunkSchematic chunk : schematicChunks.values()) {
                class_1923 pos = chunk.method_12004();
                if (pos.field_9181 < cxMin || pos.field_9181 > cxMax || chunk.method_12223() || !WorldUtils.isClientChunkLoaded(this.mc.field_1687, pos.field_9181, pos.field_9180)) continue;
                world.scheduleChunkRenders(pos.field_9181, pos.field_9180);
            }
        }
    }

    public void updateBetweenY(int minY, int maxY) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null) {
            DataManager.getSchematicPlacementManager().setVisibleSubChunksNeedsUpdate();
            Long2ObjectMap<ChunkSchematic> schematicChunks = world.getChunkProvider().getLoadedChunks();
            for (ChunkSchematic chunk : schematicChunks.values()) {
                class_1923 pos = chunk.method_12004();
                if (chunk.method_12223() || !WorldUtils.isClientChunkLoaded(this.mc.field_1687, pos.field_9181, pos.field_9180)) continue;
                world.scheduleChunkRenders(pos.field_9181, pos.field_9180);
            }
        }
    }

    public void updateBetweenZ(int minZ, int maxZ) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null) {
            DataManager.getSchematicPlacementManager().setVisibleSubChunksNeedsUpdate();
            Long2ObjectMap<ChunkSchematic> schematicChunks = world.getChunkProvider().getLoadedChunks();
            int czMin = Math.min(minZ, maxZ) >> 4;
            int czMax = Math.max(minZ, maxZ) >> 4;
            for (ChunkSchematic chunk : schematicChunks.values()) {
                class_1923 pos = chunk.method_12004();
                if (pos.field_9180 < czMin || pos.field_9180 > czMax || chunk.method_12223() || !WorldUtils.isClientChunkLoaded(this.mc.field_1687, pos.field_9181, pos.field_9180)) continue;
                world.scheduleChunkRenders(pos.field_9181, pos.field_9180);
            }
        }
    }

    public void markSchematicChunksForRenderUpdate(int chunkX, int chunkZ) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null && world.getChunkProvider().method_12123(chunkX, chunkZ) && WorldUtils.isClientChunkLoaded(this.mc.field_1687, chunkX, chunkZ)) {
            world.scheduleChunkRenders(chunkX, chunkZ);
        }
    }

    public void markSchematicChunkForRenderUpdate(class_2338 pos) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null && this.mc.field_1687 != null) {
            int chunkX = pos.method_10263() >> 4;
            int chunkZ = pos.method_10260() >> 4;
            if (world.getChunkProvider().method_12123(chunkX, chunkZ) && WorldUtils.isClientChunkLoaded(this.mc.field_1687, chunkX, chunkZ)) {
                world.scheduleChunkRenders(chunkX, chunkZ);
            }
        }
    }
}

