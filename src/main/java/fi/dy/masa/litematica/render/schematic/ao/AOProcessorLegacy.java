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

import fi.dy.masa.litematica.render.schematic.ao.AOProcessor;
import java.util.BitSet;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;

public class AOProcessorLegacy
extends AOProcessor {
    @Override
    public void apply(class_1920 world, class_2680 state, class_2338 pos, class_2350 direction, float[] box, BitSet shapeState, boolean hasShade) {
        EnumNeighborInfo neighborInfo = EnumNeighborInfo.getNeighbourInfo(direction);
        VertexTranslations vertexTranslations = VertexTranslations.getVertexTranslations(direction);
        int l1 = 0xF000F0;
        int k1 = 0xF000F0;
        int j1 = 0xF000F0;
        int i3 = 0xF000F0;
        int i1 = 0xF000F0;
        int l = 0xF000F0;
        int k = 0xF000F0;
        int j = 0xF000F0;
        int i = 0xF000F0;
        float b1 = 1.0f;
        float b2 = 1.0f;
        float b3 = 1.0f;
        float b4 = 1.0f;
        if (shapeState.get(1) && neighborInfo.doNonCubicWeight) {
            float f13 = box[neighborInfo.vert0Weights[0].shape] * box[neighborInfo.vert0Weights[1].shape];
            float f14 = box[neighborInfo.vert0Weights[2].shape] * box[neighborInfo.vert0Weights[3].shape];
            float f15 = box[neighborInfo.vert0Weights[4].shape] * box[neighborInfo.vert0Weights[5].shape];
            float f16 = box[neighborInfo.vert0Weights[6].shape] * box[neighborInfo.vert0Weights[7].shape];
            float f17 = box[neighborInfo.vert1Weights[0].shape] * box[neighborInfo.vert1Weights[1].shape];
            float f18 = box[neighborInfo.vert1Weights[2].shape] * box[neighborInfo.vert1Weights[3].shape];
            float f19 = box[neighborInfo.vert1Weights[4].shape] * box[neighborInfo.vert1Weights[5].shape];
            float f20 = box[neighborInfo.vert1Weights[6].shape] * box[neighborInfo.vert1Weights[7].shape];
            float f21 = box[neighborInfo.vert2Weights[0].shape] * box[neighborInfo.vert2Weights[1].shape];
            float f22 = box[neighborInfo.vert2Weights[2].shape] * box[neighborInfo.vert2Weights[3].shape];
            float f23 = box[neighborInfo.vert2Weights[4].shape] * box[neighborInfo.vert2Weights[5].shape];
            float f24 = box[neighborInfo.vert2Weights[6].shape] * box[neighborInfo.vert2Weights[7].shape];
            float f25 = box[neighborInfo.vert3Weights[0].shape] * box[neighborInfo.vert3Weights[1].shape];
            float f26 = box[neighborInfo.vert3Weights[2].shape] * box[neighborInfo.vert3Weights[3].shape];
            float f27 = box[neighborInfo.vert3Weights[4].shape] * box[neighborInfo.vert3Weights[5].shape];
            float f28 = box[neighborInfo.vert3Weights[6].shape] * box[neighborInfo.vert3Weights[7].shape];
            this.brightness[vertexTranslations.vert0] = b1 * f13 + b2 * f14 + b3 * f15 + b4 * f16;
            this.brightness[vertexTranslations.vert1] = b1 * f17 + b2 * f18 + b3 * f19 + b4 * f20;
            this.brightness[vertexTranslations.vert2] = b1 * f21 + b2 * f22 + b3 * f23 + b4 * f24;
            this.brightness[vertexTranslations.vert3] = b1 * f25 + b2 * f26 + b3 * f27 + b4 * f28;
            int i2 = this.getAoBrightness(l, i, j1, i3);
            int j2 = this.getAoBrightness(k, i, i1, i3);
            int k2 = this.getAoBrightness(k, j, k1, i3);
            int l2 = this.getAoBrightness(l, j, l1, i3);
            this.light[vertexTranslations.vert0] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
            this.light[vertexTranslations.vert1] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
            this.light[vertexTranslations.vert2] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
            this.light[vertexTranslations.vert3] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
        } else {
            this.light[vertexTranslations.vert0] = this.getAoBrightness(l, i, j1, i3);
            this.light[vertexTranslations.vert1] = this.getAoBrightness(k, i, i1, i3);
            this.light[vertexTranslations.vert2] = this.getAoBrightness(k, j, k1, i3);
            this.light[vertexTranslations.vert3] = this.getAoBrightness(l, j, l1, i3);
            this.brightness[vertexTranslations.vert0] = b1;
            this.brightness[vertexTranslations.vert1] = b2;
            this.brightness[vertexTranslations.vert2] = b3;
            this.brightness[vertexTranslations.vert3] = b4;
        }
        float b = world.method_24852(direction, hasShade);
        int index = 0;
        while (index < this.brightness.length) {
            int n = index++;
            this.brightness[n] = this.brightness[n] * b;
        }
    }

    private int getAoBrightness(int br1, int br2, int br3, int br4) {
        if (br1 == 0) {
            br1 = br4;
        }
        if (br2 == 0) {
            br2 = br4;
        }
        if (br3 == 0) {
            br3 = br4;
        }
        return br1 + br2 + br3 + br4 >> 2 & 0xFF00FF;
    }

    private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_) {
        int i = (int)((float)(p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (float)(p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (float)(p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (float)(p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
        int j = (int)((float)(p_178203_1_ & 0xFF) * p_178203_5_ + (float)(p_178203_2_ & 0xFF) * p_178203_6_ + (float)(p_178203_3_ & 0xFF) * p_178203_7_ + (float)(p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
        return i << 16 | j;
    }

    public static enum EnumNeighborInfo {
        DOWN(new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11043, class_2350.field_11035}, 0.5f, true, new Orientation[]{Orientation.FLIP_WEST, Orientation.SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.SOUTH}, new Orientation[]{Orientation.FLIP_WEST, Orientation.NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_EAST, Orientation.NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_EAST, Orientation.SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.SOUTH}),
        UP(new class_2350[]{class_2350.field_11034, class_2350.field_11039, class_2350.field_11043, class_2350.field_11035}, 1.0f, true, new Orientation[]{Orientation.EAST, Orientation.SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.SOUTH}, new Orientation[]{Orientation.EAST, Orientation.NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.NORTH}, new Orientation[]{Orientation.WEST, Orientation.NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.NORTH}, new Orientation[]{Orientation.WEST, Orientation.SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.SOUTH}),
        NORTH(new class_2350[]{class_2350.field_11036, class_2350.field_11033, class_2350.field_11034, class_2350.field_11039}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST}),
        SOUTH(new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11033, class_2350.field_11036}, 0.8f, true, new Orientation[]{Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST}, new Orientation[]{Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST}, new Orientation[]{Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST}),
        WEST(new class_2350[]{class_2350.field_11036, class_2350.field_11033, class_2350.field_11043, class_2350.field_11035}, 0.6f, true, new Orientation[]{Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH}, new Orientation[]{Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH}, new Orientation[]{Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH}),
        EAST(new class_2350[]{class_2350.field_11033, class_2350.field_11036, class_2350.field_11043, class_2350.field_11035}, 0.6f, true, new Orientation[]{Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH}, new Orientation[]{Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH}, new Orientation[]{Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH});

        private final boolean doNonCubicWeight;
        private final Orientation[] vert0Weights;
        private final Orientation[] vert1Weights;
        private final Orientation[] vert2Weights;
        private final Orientation[] vert3Weights;
        private static final EnumNeighborInfo[] VALUES;

        private EnumNeighborInfo(class_2350[] p_i46236_3_, float p_i46236_4_, boolean p_i46236_5_, Orientation[] p_i46236_6_, Orientation[] p_i46236_7_, Orientation[] p_i46236_8_, Orientation[] p_i46236_9_) {
            this.doNonCubicWeight = p_i46236_5_;
            this.vert0Weights = p_i46236_6_;
            this.vert1Weights = p_i46236_7_;
            this.vert2Weights = p_i46236_8_;
            this.vert3Weights = p_i46236_9_;
        }

        public static EnumNeighborInfo getNeighbourInfo(class_2350 p_178273_0_) {
            return VALUES[p_178273_0_.method_10146()];
        }

        static {
            VALUES = new EnumNeighborInfo[6];
            EnumNeighborInfo.VALUES[class_2350.field_11033.method_10146()] = DOWN;
            EnumNeighborInfo.VALUES[class_2350.field_11036.method_10146()] = UP;
            EnumNeighborInfo.VALUES[class_2350.field_11043.method_10146()] = NORTH;
            EnumNeighborInfo.VALUES[class_2350.field_11035.method_10146()] = SOUTH;
            EnumNeighborInfo.VALUES[class_2350.field_11039.method_10146()] = WEST;
            EnumNeighborInfo.VALUES[class_2350.field_11034.method_10146()] = EAST;
        }
    }

    static enum VertexTranslations {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        private final int vert0;
        private final int vert1;
        private final int vert2;
        private final int vert3;
        private static final VertexTranslations[] VALUES;

        private VertexTranslations(int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_) {
            this.vert0 = p_i46234_3_;
            this.vert1 = p_i46234_4_;
            this.vert2 = p_i46234_5_;
            this.vert3 = p_i46234_6_;
        }

        public static VertexTranslations getVertexTranslations(class_2350 p_178184_0_) {
            return VALUES[p_178184_0_.method_10146()];
        }

        static {
            VALUES = new VertexTranslations[6];
            VertexTranslations.VALUES[class_2350.field_11033.method_10146()] = DOWN;
            VertexTranslations.VALUES[class_2350.field_11036.method_10146()] = UP;
            VertexTranslations.VALUES[class_2350.field_11043.method_10146()] = NORTH;
            VertexTranslations.VALUES[class_2350.field_11035.method_10146()] = SOUTH;
            VertexTranslations.VALUES[class_2350.field_11039.method_10146()] = WEST;
            VertexTranslations.VALUES[class_2350.field_11034.method_10146()] = EAST;
        }
    }

    public static enum Orientation {
        DOWN(class_2350.field_11033, false),
        UP(class_2350.field_11036, false),
        NORTH(class_2350.field_11043, false),
        SOUTH(class_2350.field_11035, false),
        WEST(class_2350.field_11039, false),
        EAST(class_2350.field_11034, false),
        FLIP_DOWN(class_2350.field_11033, true),
        FLIP_UP(class_2350.field_11036, true),
        FLIP_NORTH(class_2350.field_11043, true),
        FLIP_SOUTH(class_2350.field_11035, true),
        FLIP_WEST(class_2350.field_11039, true),
        FLIP_EAST(class_2350.field_11034, true);

        private final int shape;

        private Orientation(class_2350 p_i46233_3_, boolean p_i46233_4_) {
            this.shape = p_i46233_3_.method_10146() + (p_i46233_4_ ? class_2350.values().length : 0);
        }
    }
}

