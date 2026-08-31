/*
 * Decompiled with CFR 0.152.
 */
package com.cauca;

public class FishingData {
    public static boolean active = false;
    public static float barX = 0.0f;
    public static float dotX = 0.0f;
    public static float progress = 0.0f;

    public static boolean isActive() {
        return active;
    }

    public static void reset() {
        active = false;
        barX = 0.0f;
        dotX = 0.0f;
        progress = 0.0f;
    }
}

