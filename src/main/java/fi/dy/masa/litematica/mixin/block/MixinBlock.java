/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1542
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.block;

import fi.dy.masa.litematica.util.WorldUtils;
import java.util.function.Supplier;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_2248.class})
public class MixinBlock {
    @Inject(method={"method_36993(Lnet/minecraft/class_1937;Ljava/util/function/Supplier;Lnet/minecraft/class_1799;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private static void litematica_preventItemDrops(class_1937 world, Supplier<class_1542> itemEntitySupplier, class_1799 stack, CallbackInfo ci) {
        if (WorldUtils.shouldPreventBlockUpdates(world)) {
            ci.cancel();
        }
    }
}

