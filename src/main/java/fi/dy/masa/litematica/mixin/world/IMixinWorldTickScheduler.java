/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  net.minecraft.class_6755
 *  net.minecraft.class_6757
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package fi.dy.masa.litematica.mixin.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.class_6755;
import net.minecraft.class_6757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_6757.class})
public interface IMixinWorldTickScheduler<T> {
    @Accessor(value="field_35534")
    public Long2ObjectMap<class_6755<T>> litematica_getChunkTickSchedulers();
}

