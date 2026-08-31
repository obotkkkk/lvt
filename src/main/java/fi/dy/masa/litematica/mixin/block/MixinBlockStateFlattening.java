/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3580
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.block;

import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import net.minecraft.class_3580;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_3580.class})
public abstract class MixinBlockStateFlattening {
    @Inject(method={"method_15596"}, at={@At(value="HEAD")})
    private static void litematica_onAddEntry(int id, String fixedNBT, String[] sourceNBTs, CallbackInfo ci) {
        SchematicConversionMaps.addEntry(id, fixedNBT, sourceNBTs);
    }
}

