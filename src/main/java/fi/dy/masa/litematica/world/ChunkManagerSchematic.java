/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_2802
 *  net.minecraft.class_2806
 *  net.minecraft.class_2818
 *  net.minecraft.class_2823
 *  net.minecraft.class_3568
 */
package fi.dy.masa.litematica.world;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.litematica.world.FakeLightingProvider;
import fi.dy.masa.litematica.world.WorldSchematic;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Iterator;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_2802;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_2823;
import net.minecraft.class_3568;

public class ChunkManagerSchematic
extends class_2802 {
    private final WorldSchematic world;
    private final Long2ObjectMap<ChunkSchematic> loadedChunks = new Long2ObjectOpenHashMap(8192);
    private final ChunkSchematic blankChunk;
    private final class_3568 lightingProvider;
    private final FakeLightingProvider fakeLightingProvider;

    public ChunkManagerSchematic(WorldSchematic world) {
        this.world = world;
        this.blankChunk = new ChunkSchematic(world, new class_1923(0, 0));
        this.lightingProvider = new class_3568((class_2823)this, true, world.method_8597().comp_642());
        this.fakeLightingProvider = new FakeLightingProvider((class_2823)this);
    }

    public WorldSchematic getWorld() {
        return this.world;
    }

    public void loadChunk(int chunkX, int chunkZ) {
        ChunkSchematic chunk = new ChunkSchematic(this.world, new class_1923(chunkX, chunkZ));
        this.loadedChunks.put(class_1923.method_8331((int)chunkX, (int)chunkZ), (Object)chunk);
    }

    public boolean method_12123(int chunkX, int chunkZ) {
        return this.loadedChunks.containsKey(class_1923.method_8331((int)chunkX, (int)chunkZ));
    }

    public String method_12122() {
        return "Schematic Chunk Cache: " + this.method_14151();
    }

    public int method_14151() {
        return this.loadedChunks.size();
    }

    public Long2ObjectMap<ChunkSchematic> getLoadedChunks() {
        return this.loadedChunks;
    }

    public class_2818 getChunk(int chunkX, int chunkZ, class_2806 status, boolean fallbackToEmpty) {
        ChunkSchematic chunk = this.getChunk(chunkX, chunkZ);
        return chunk == null && fallbackToEmpty ? this.blankChunk : chunk;
    }

    public ChunkSchematic getChunk(int chunkX, int chunkZ) {
        ChunkSchematic chunk = (ChunkSchematic)((Object)this.loadedChunks.get(class_1923.method_8331((int)chunkX, (int)chunkZ)));
        return chunk == null ? this.blankChunk : chunk;
    }

    @Nullable
    public ChunkSchematic getChunkIfExists(int chunkX, int chunkZ) {
        return (ChunkSchematic)((Object)this.loadedChunks.get(class_1923.method_8331((int)chunkX, (int)chunkZ)));
    }

    public void unloadChunk(int chunkX, int chunkZ) {
        ChunkSchematic chunk = (ChunkSchematic)((Object)this.loadedChunks.remove(class_1923.method_8331((int)chunkX, (int)chunkZ)));
        if (chunk != null) {
            this.world.unloadedEntities(chunk.getEntityCount());
        }
    }

    public class_3568 method_12130() {
        if (Configs.Visuals.ENABLE_SCHEMATIC_FAKE_LIGHTING.getBooleanValue()) {
            return this.fakeLightingProvider;
        }
        return this.lightingProvider;
    }

    public void method_12127(BooleanSupplier shouldKeepTicking, boolean tickChunks) {
    }

    public int getTileEntityCount() {
        int count = 0;
        Iterator iter = this.loadedChunks.values().stream().iterator();
        while (iter.hasNext()) {
            count += ((ChunkSchematic)((Object)iter.next())).getTileEntityCount();
        }
        return count;
    }
}

