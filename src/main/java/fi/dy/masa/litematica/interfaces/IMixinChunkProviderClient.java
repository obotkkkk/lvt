/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  net.minecraft.class_2791
 */
package fi.dy.masa.litematica.interfaces;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.class_2791;

public interface IMixinChunkProviderClient {
    public Long2ObjectMap<class_2791> getLoadedChunks();
}

