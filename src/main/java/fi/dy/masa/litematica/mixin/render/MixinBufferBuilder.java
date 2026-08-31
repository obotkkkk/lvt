/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_287
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package fi.dy.masa.litematica.mixin.render;

import fi.dy.masa.litematica.render.schematic.IBufferBuilderPatch;
import net.minecraft.class_287;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={class_287.class})
public class MixinBufferBuilder
implements IBufferBuilderPatch {
    @Unique
    private float offsetY = 0.0f;

    @ModifyArg(method={"method_22912(FFF)Lnet/minecraft/class_4588;"}, at=@At(value="INVOKE", target="Lorg/lwjgl/system/MemoryUtil;memPutFloat(JF)V", ordinal=1, remap=false), index=1)
    private float litematica_modifyOffsetY(float value) {
        return value + this.offsetY;
    }

    @Override
    public void litematica$setOffsetY(float offset) {
        this.offsetY = offset;
    }
}

