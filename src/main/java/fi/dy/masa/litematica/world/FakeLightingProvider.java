/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_1944
 *  net.minecraft.class_2338
 *  net.minecraft.class_2804
 *  net.minecraft.class_2823
 *  net.minecraft.class_3560$class_8530
 *  net.minecraft.class_3562
 *  net.minecraft.class_3568
 *  net.minecraft.class_4076
 */
package fi.dy.masa.litematica.world;

import fi.dy.masa.litematica.config.Configs;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_1944;
import net.minecraft.class_2338;
import net.minecraft.class_2804;
import net.minecraft.class_2823;
import net.minecraft.class_3560;
import net.minecraft.class_3562;
import net.minecraft.class_3568;
import net.minecraft.class_4076;

public class FakeLightingProvider
extends class_3568 {
    private final FakeLightingView lightingView = new FakeLightingView();
    private static final class_2804 chunkNibbleArray = new class_2804(15);

    public FakeLightingProvider(class_2823 chunkProvider) {
        super(chunkProvider, false, false);
    }

    public class_3562 method_15562(class_1944 type) {
        return this.lightingView;
    }

    public int method_22363(class_2338 pos, int ambientDarkness) {
        return Configs.Visuals.RENDER_FAKE_LIGHTING_LEVEL.getIntegerValue();
    }

    public static class_2804 getChunkNibbleArray() {
        return chunkNibbleArray;
    }

    public boolean method_62874(long sectionPos) {
        return true;
    }

    public class_3560.class_8530 method_51560(class_1944 lightType, class_4076 pos) {
        return class_3560.class_8530.field_44725;
    }

    public String method_22876(class_1944 lightType, class_4076 pos) {
        return Integer.toString(1);
    }

    public static class FakeLightingView
    implements class_3562 {
        @Nullable
        public class_2804 method_15544(class_4076 pos) {
            return chunkNibbleArray;
        }

        public int method_15543(class_2338 pos) {
            return Configs.Visuals.RENDER_FAKE_LIGHTING_LEVEL.getIntegerValue();
        }

        public void method_15513(class_2338 pos) {
        }

        public void method_51471(class_1923 chunkPos) {
        }

        public boolean method_15518() {
            return false;
        }

        public int method_15516() {
            return 0;
        }

        public void method_15551(class_4076 pos, boolean notReady) {
        }

        public void method_15512(class_1923 chunkPos, boolean bl) {
        }
    }
}

