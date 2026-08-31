/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2248
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_2510
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_2778
 *  net.minecraft.class_4970$class_2251
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.block;

import fi.dy.masa.litematica.config.Configs;
import net.minecraft.class_2248;
import net.minecraft.class_2350;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_2510;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_2778;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_2510.class})
public abstract class MixinStairsBlock
extends class_2248 {
    public MixinStairsBlock(class_4970.class_2251 settings) {
        super(settings);
    }

    @Inject(method={"method_9569"}, at={@At(value="HEAD")}, cancellable=true)
    private void litematica_fixStairsMirror(class_2680 state, class_2415 mirror, CallbackInfoReturnable<class_2680> cir) {
        if (Configs.Generic.FIX_STAIRS_MIRROR.getBooleanValue()) {
            class_2350 direction = (class_2350)state.method_11654((class_2769)class_2510.field_11571);
            class_2778 stairShape = (class_2778)state.method_11654((class_2769)class_2510.field_11565);
            if (direction.method_10166() == class_2350.class_2351.field_11048 && mirror == class_2415.field_11301) {
                cir.setReturnValue((Object)(switch (stairShape) {
                    case class_2778.field_12712 -> (class_2680)state.method_26186(class_2470.field_11464).method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12713);
                    case class_2778.field_12713 -> (class_2680)state.method_26186(class_2470.field_11464).method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12712);
                    case class_2778.field_12708 -> (class_2680)state.method_26186(class_2470.field_11464).method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12709);
                    case class_2778.field_12709 -> (class_2680)state.method_26186(class_2470.field_11464).method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12708);
                    default -> state.method_26186(class_2470.field_11464);
                }));
                cir.cancel();
            } else if (direction.method_10166() == class_2350.class_2351.field_11048 && mirror == class_2415.field_11300 || direction.method_10166() == class_2350.class_2351.field_11051 && mirror == class_2415.field_11301) {
                cir.setReturnValue((Object)(switch (stairShape) {
                    case class_2778.field_12712 -> (class_2680)state.method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12713);
                    case class_2778.field_12713 -> (class_2680)state.method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12712);
                    case class_2778.field_12708 -> (class_2680)state.method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12709);
                    case class_2778.field_12709 -> (class_2680)state.method_11657((class_2769)class_2510.field_11565, (Comparable)class_2778.field_12708);
                    default -> state;
                }));
                cir.cancel();
            }
        }
    }
}

