/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_5577
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package fi.dy.masa.litematica.mixin.world;

import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_5577;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1937.class})
public interface IMixinWorld {
    @Invoker(value="method_31592")
    public class_5577<class_1297> litematica_getEntityLookup();
}

