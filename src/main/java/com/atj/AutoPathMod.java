/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_746
 */
package com.atj;

import net.minecraft.class_310;
import net.minecraft.class_746;

public class AutoPathMod {
    private static double anchorZ = Double.NaN;
    private static double anchorX = Double.NaN;
    private static final double TOLERANCE = 0.15;

    public static void reset() {
        anchorZ = Double.NaN;
        anchorX = Double.NaN;
    }

    public static void onTick(class_310 client, boolean isRunningEast) {
        class_746 player = client.field_1724;
        if (player == null) {
            return;
        }
        if (Double.isNaN(anchorZ)) {
            anchorZ = player.method_23321();
            anchorX = player.method_23317();
            return;
        }
        if (isRunningEast) {
            double currentZ = player.method_23321();
            double diffZ = currentZ - anchorZ;
            if (diffZ > 0.15) {
                client.field_1690.field_1913.method_23481(true);
                client.field_1690.field_1849.method_23481(false);
            } else if (diffZ < -0.15) {
                client.field_1690.field_1849.method_23481(true);
                client.field_1690.field_1913.method_23481(false);
            } else {
                client.field_1690.field_1913.method_23481(false);
                client.field_1690.field_1849.method_23481(false);
            }
        }
    }
}

