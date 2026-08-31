/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.minecraft.class_2248
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2960
 *  net.minecraft.class_9824
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.block;

import com.google.common.collect.ImmutableMap;
import fi.dy.masa.litematica.render.schematic.blocks.FallbackBlocks;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.class_2248;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2960;
import net.minecraft.class_9824;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_9824.class})
public class MixinBlockStatesLoader {
    @Mutable
    @Final
    @Shadow
    private static Map<class_2960, class_2689<class_2248, class_2680>> field_52266;

    @Inject(method={"method_62626"}, at={@At(value="HEAD")})
    private static void litematica$fillFallbackBlocks(CallbackInfoReturnable<Function<class_2960, class_2689<class_2248, class_2680>>> cir) {
        FallbackBlocks.register();
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        builder.putAll(field_52266);
        for (class_2960 id : FallbackBlocks.ID_TO_STATE_MANAGER.keySet()) {
            if (field_52266.containsKey(id)) continue;
            builder.put((Object)id, FallbackBlocks.ID_TO_STATE_MANAGER.get(id));
        }
        field_52266 = builder.build();
    }
}

