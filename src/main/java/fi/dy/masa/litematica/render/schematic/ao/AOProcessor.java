/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1920
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.render.schematic.ao;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.render.schematic.ao.AOProcessorLegacy;
import fi.dy.masa.litematica.render.schematic.ao.AOProcessorModern;
import java.util.BitSet;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;

public abstract class AOProcessor {
    public final float[] brightness = new float[4];
    public final int[] light = new int[4];

    public static AOProcessor get() {
        if (Configs.Visuals.RENDER_AO_MODERN_ENABLE.getBooleanValue()) {
            return new AOProcessorModern();
        }
        return new AOProcessorLegacy();
    }

    public void apply(class_1920 world, class_2680 state, class_2338 pos, class_2350 direction, float[] box, BitSet shapeState, boolean hasShade) {
    }
}

