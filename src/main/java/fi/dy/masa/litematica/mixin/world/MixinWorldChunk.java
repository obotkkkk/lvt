/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1937
 *  net.minecraft.class_2818
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.Slice
 */
package fi.dy.masa.litematica.mixin.world;

import fi.dy.masa.litematica.util.WorldUtils;
import net.minecraft.class_1937;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value={class_2818.class})
public abstract class MixinWorldChunk {
    @Redirect(method={"method_12010"}, slice=@Slice(from=@At(value="INVOKE", target="Lnet/minecraft/class_2826;method_12254(III)Lnet/minecraft/class_2680;")), at=@At(value="FIELD", target="Lnet/minecraft/class_1937;field_9236:Z", ordinal=0, opcode=180))
    private boolean litematica_redirectIsRemote(class_1937 world) {
        return WorldUtils.shouldPreventBlockUpdates(world) || world.field_9236;
    }
}

