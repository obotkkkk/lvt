/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap
 *  it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap
 *  net.minecraft.class_156
 *  net.minecraft.class_1920
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_2680
 *  net.minecraft.class_761
 */
package fi.dy.masa.litematica.render.schematic.ao;

import fi.dy.masa.litematica.render.schematic.BlockModelRendererSchematic;
import fi.dy.masa.litematica.render.schematic.ao.AOProcessor;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntLinkedOpenHashMap;
import java.util.BitSet;
import net.minecraft.class_156;
import net.minecraft.class_1920;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_2680;
import net.minecraft.class_761;

public class AOProcessorModern
extends AOProcessor {
    private static final class_2350[] DIRECTIONS = class_2350.values();

    @Override
    public void apply(class_1920 world, class_2680 state, class_2338 pos, class_2350 direction, float[] box, BitSet shapeState, boolean hasShade) {
        float x;
        int u;
        float t;
        int s;
        float r;
        int q;
        float p;
        class_2680 blockState9;
        int o;
        float n;
        boolean bl5;
        class_2338 blockPos = shapeState.get(0) ? pos.method_10093(direction) : pos;
        ND neighborData = ND.getData(direction);
        class_2338.class_2339 mutable = new class_2338.class_2339();
        BC brightnessCache = BlockModelRendererSchematic.CACHE.get();
        mutable.method_25505((class_2382)blockPos, neighborData.faces[0]);
        class_2680 blockState = world.method_8320((class_2338)mutable);
        int i = brightnessCache.getInt(blockState, world, (class_2338)mutable);
        float f = brightnessCache.getFloat(blockState, world, (class_2338)mutable);
        mutable.method_25505((class_2382)blockPos, neighborData.faces[1]);
        class_2680 blockState2 = world.method_8320((class_2338)mutable);
        int j = brightnessCache.getInt(blockState2, world, (class_2338)mutable);
        float g = brightnessCache.getFloat(blockState2, world, (class_2338)mutable);
        mutable.method_25505((class_2382)blockPos, neighborData.faces[2]);
        class_2680 blockState3 = world.method_8320((class_2338)mutable);
        int k = brightnessCache.getInt(blockState3, world, (class_2338)mutable);
        float h = brightnessCache.getFloat(blockState3, world, (class_2338)mutable);
        mutable.method_25505((class_2382)blockPos, neighborData.faces[3]);
        class_2680 blockState4 = world.method_8320((class_2338)mutable);
        int l = brightnessCache.getInt(blockState4, world, (class_2338)mutable);
        float m = brightnessCache.getFloat(blockState4, world, (class_2338)mutable);
        class_2680 blockState5 = world.method_8320((class_2338)mutable.method_25505((class_2382)blockPos, neighborData.faces[0]).method_10098(direction));
        boolean bl2 = !blockState5.method_26230((class_1922)world, (class_2338)mutable) || blockState5.method_26193() == 0;
        class_2680 blockState6 = world.method_8320((class_2338)mutable.method_25505((class_2382)blockPos, neighborData.faces[1]).method_10098(direction));
        boolean bl3 = !blockState6.method_26230((class_1922)world, (class_2338)mutable) || blockState6.method_26193() == 0;
        class_2680 blockState7 = world.method_8320((class_2338)mutable.method_25505((class_2382)blockPos, neighborData.faces[2]).method_10098(direction));
        boolean bl4 = !blockState7.method_26230((class_1922)world, (class_2338)mutable) || blockState7.method_26193() == 0;
        class_2680 blockState8 = world.method_8320((class_2338)mutable.method_25505((class_2382)blockPos, neighborData.faces[3]).method_10098(direction));
        boolean bl = bl5 = !blockState8.method_26230((class_1922)world, (class_2338)mutable) || blockState8.method_26193() == 0;
        if (!bl4 && !bl2) {
            n = f;
            o = i;
        } else {
            mutable.method_25505((class_2382)blockPos, neighborData.faces[0]).method_10098(neighborData.faces[2]);
            blockState9 = world.method_8320((class_2338)mutable);
            n = brightnessCache.getFloat(blockState9, world, (class_2338)mutable);
            o = brightnessCache.getInt(blockState9, world, (class_2338)mutable);
        }
        if (!bl5 && !bl2) {
            p = f;
            q = i;
        } else {
            mutable.method_25505((class_2382)blockPos, neighborData.faces[0]).method_10098(neighborData.faces[3]);
            blockState9 = world.method_8320((class_2338)mutable);
            p = brightnessCache.getFloat(blockState9, world, (class_2338)mutable);
            q = brightnessCache.getInt(blockState9, world, (class_2338)mutable);
        }
        if (!bl4 && !bl3) {
            r = f;
            s = i;
        } else {
            mutable.method_25505((class_2382)blockPos, neighborData.faces[1]).method_10098(neighborData.faces[2]);
            blockState9 = world.method_8320((class_2338)mutable);
            r = brightnessCache.getFloat(blockState9, world, (class_2338)mutable);
            s = brightnessCache.getInt(blockState9, world, (class_2338)mutable);
        }
        if (!bl5 && !bl3) {
            t = f;
            u = i;
        } else {
            mutable.method_25505((class_2382)blockPos, neighborData.faces[1]).method_10098(neighborData.faces[3]);
            blockState9 = world.method_8320((class_2338)mutable);
            t = brightnessCache.getFloat(blockState9, world, (class_2338)mutable);
            u = brightnessCache.getInt(blockState9, world, (class_2338)mutable);
        }
        int v = brightnessCache.getInt(state, world, pos);
        mutable.method_25505((class_2382)pos, direction);
        class_2680 blockState10 = world.method_8320((class_2338)mutable);
        if (shapeState.get(0) || !blockState10.method_26216()) {
            v = brightnessCache.getInt(blockState10, world, (class_2338)mutable);
        }
        float w = shapeState.get(0) ? brightnessCache.getFloat(world.method_8320(blockPos), world, blockPos) : brightnessCache.getFloat(world.method_8320(pos), world, pos);
        Tl translation = Tl.getTranslations(direction);
        if (shapeState.get(1) && neighborData.nonCubicWeight) {
            x = (m + f + p + w) * 0.25f;
            y = (h + f + n + w) * 0.25f;
            z = (h + g + r + w) * 0.25f;
            aa = (m + g + t + w) * 0.25f;
            float ab = box[neighborData.neighbor1[0].shape] * box[neighborData.neighbor1[1].shape];
            float ac = box[neighborData.neighbor1[2].shape] * box[neighborData.neighbor1[3].shape];
            float ad = box[neighborData.neighbor1[4].shape] * box[neighborData.neighbor1[5].shape];
            float ae = box[neighborData.neighbor1[6].shape] * box[neighborData.neighbor1[7].shape];
            float af = box[neighborData.neighbor2[0].shape] * box[neighborData.neighbor2[1].shape];
            float ag = box[neighborData.neighbor2[2].shape] * box[neighborData.neighbor2[3].shape];
            float ah = box[neighborData.neighbor2[4].shape] * box[neighborData.neighbor2[5].shape];
            float ai = box[neighborData.neighbor2[6].shape] * box[neighborData.neighbor2[7].shape];
            float aj = box[neighborData.neighbor3[0].shape] * box[neighborData.neighbor3[1].shape];
            float ak = box[neighborData.neighbor3[2].shape] * box[neighborData.neighbor3[3].shape];
            float al = box[neighborData.neighbor3[4].shape] * box[neighborData.neighbor3[5].shape];
            float am = box[neighborData.neighbor3[6].shape] * box[neighborData.neighbor3[7].shape];
            float an = box[neighborData.neighbor4[0].shape] * box[neighborData.neighbor4[1].shape];
            float ao = box[neighborData.neighbor4[2].shape] * box[neighborData.neighbor4[3].shape];
            float ap = box[neighborData.neighbor4[4].shape] * box[neighborData.neighbor4[5].shape];
            float aq = box[neighborData.neighbor4[6].shape] * box[neighborData.neighbor4[7].shape];
            this.brightness[translation.firstCorner] = Math.clamp(x * ab + y * ac + z * ad + aa * ae, 0.0f, 1.0f);
            this.brightness[translation.secondCorner] = Math.clamp(x * af + y * ag + z * ah + aa * ai, 0.0f, 1.0f);
            this.brightness[translation.thirdCorner] = Math.clamp(x * aj + y * ak + z * al + aa * am, 0.0f, 1.0f);
            this.brightness[translation.fourthCorner] = Math.clamp(x * an + y * ao + z * ap + aa * aq, 0.0f, 1.0f);
            int ar = this.getAmbientOcclusionBrightness(l, i, q, v);
            int as = this.getAmbientOcclusionBrightness(k, i, o, v);
            int at = this.getAmbientOcclusionBrightness(k, j, s, v);
            int au = this.getAmbientOcclusionBrightness(l, j, u, v);
            this.light[translation.firstCorner] = this.getBrightness(ar, as, at, au, ab, ac, ad, ae);
            this.light[translation.secondCorner] = this.getBrightness(ar, as, at, au, af, ag, ah, ai);
            this.light[translation.thirdCorner] = this.getBrightness(ar, as, at, au, aj, ak, al, am);
            this.light[translation.fourthCorner] = this.getBrightness(ar, as, at, au, an, ao, ap, aq);
        } else {
            x = (m + f + p + w) * 0.25f;
            y = (h + f + n + w) * 0.25f;
            z = (h + g + r + w) * 0.25f;
            aa = (m + g + t + w) * 0.25f;
            this.light[translation.firstCorner] = this.getAmbientOcclusionBrightness(l, i, q, v);
            this.light[translation.secondCorner] = this.getAmbientOcclusionBrightness(k, i, o, v);
            this.light[translation.thirdCorner] = this.getAmbientOcclusionBrightness(k, j, s, v);
            this.light[translation.fourthCorner] = this.getAmbientOcclusionBrightness(l, j, u, v);
            this.brightness[translation.firstCorner] = x;
            this.brightness[translation.secondCorner] = y;
            this.brightness[translation.thirdCorner] = z;
            this.brightness[translation.fourthCorner] = aa;
        }
        x = world.method_24852(direction, hasShade);
        int av = 0;
        while (av < this.brightness.length) {
            int n2 = av++;
            this.brightness[n2] = this.brightness[n2] * x;
        }
    }

    private int getAmbientOcclusionBrightness(int i, int j, int k, int l) {
        if (i == 0) {
            i = l;
        }
        if (j == 0) {
            j = l;
        }
        if (k == 0) {
            k = l;
        }
        return i + j + k + l >> 2 & 0xFF00FF;
    }

    private int getBrightness(int i, int j, int k, int l, float f, float g, float h, float m) {
        int n = (int)((float)(i >> 16 & 0xFF) * f + (float)(j >> 16 & 0xFF) * g + (float)(k >> 16 & 0xFF) * h + (float)(l >> 16 & 0xFF) * m) & 0xFF;
        int o = (int)((float)(i & 0xFF) * f + (float)(j & 0xFF) * g + (float)(k & 0xFF) * h + (float)(l & 0xFF) * m) & 0xFF;
        return n << 16 | o;
    }

    protected static enum ND {
        DOWN(new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11043, class_2350.field_11035}, 0.5f, true, new NO[]{NO.FLIP_WEST, NO.SOUTH, NO.FLIP_WEST, NO.FLIP_SOUTH, NO.WEST, NO.FLIP_SOUTH, NO.WEST, NO.SOUTH}, new NO[]{NO.FLIP_WEST, NO.NORTH, NO.FLIP_WEST, NO.FLIP_NORTH, NO.WEST, NO.FLIP_NORTH, NO.WEST, NO.NORTH}, new NO[]{NO.FLIP_EAST, NO.NORTH, NO.FLIP_EAST, NO.FLIP_NORTH, NO.EAST, NO.FLIP_NORTH, NO.EAST, NO.NORTH}, new NO[]{NO.FLIP_EAST, NO.SOUTH, NO.FLIP_EAST, NO.FLIP_SOUTH, NO.EAST, NO.FLIP_SOUTH, NO.EAST, NO.SOUTH}),
        UP(new class_2350[]{class_2350.field_11034, class_2350.field_11039, class_2350.field_11043, class_2350.field_11035}, 1.0f, true, new NO[]{NO.EAST, NO.SOUTH, NO.EAST, NO.FLIP_SOUTH, NO.FLIP_EAST, NO.FLIP_SOUTH, NO.FLIP_EAST, NO.SOUTH}, new NO[]{NO.EAST, NO.NORTH, NO.EAST, NO.FLIP_NORTH, NO.FLIP_EAST, NO.FLIP_NORTH, NO.FLIP_EAST, NO.NORTH}, new NO[]{NO.WEST, NO.NORTH, NO.WEST, NO.FLIP_NORTH, NO.FLIP_WEST, NO.FLIP_NORTH, NO.FLIP_WEST, NO.NORTH}, new NO[]{NO.WEST, NO.SOUTH, NO.WEST, NO.FLIP_SOUTH, NO.FLIP_WEST, NO.FLIP_SOUTH, NO.FLIP_WEST, NO.SOUTH}),
        NORTH(new class_2350[]{class_2350.field_11036, class_2350.field_11033, class_2350.field_11034, class_2350.field_11039}, 0.8f, true, new NO[]{NO.UP, NO.FLIP_WEST, NO.UP, NO.WEST, NO.FLIP_UP, NO.WEST, NO.FLIP_UP, NO.FLIP_WEST}, new NO[]{NO.UP, NO.FLIP_EAST, NO.UP, NO.EAST, NO.FLIP_UP, NO.EAST, NO.FLIP_UP, NO.FLIP_EAST}, new NO[]{NO.DOWN, NO.FLIP_EAST, NO.DOWN, NO.EAST, NO.FLIP_DOWN, NO.EAST, NO.FLIP_DOWN, NO.FLIP_EAST}, new NO[]{NO.DOWN, NO.FLIP_WEST, NO.DOWN, NO.WEST, NO.FLIP_DOWN, NO.WEST, NO.FLIP_DOWN, NO.FLIP_WEST}),
        SOUTH(new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11033, class_2350.field_11036}, 0.8f, true, new NO[]{NO.UP, NO.FLIP_WEST, NO.FLIP_UP, NO.FLIP_WEST, NO.FLIP_UP, NO.WEST, NO.UP, NO.WEST}, new NO[]{NO.DOWN, NO.FLIP_WEST, NO.FLIP_DOWN, NO.FLIP_WEST, NO.FLIP_DOWN, NO.WEST, NO.DOWN, NO.WEST}, new NO[]{NO.DOWN, NO.FLIP_EAST, NO.FLIP_DOWN, NO.FLIP_EAST, NO.FLIP_DOWN, NO.EAST, NO.DOWN, NO.EAST}, new NO[]{NO.UP, NO.FLIP_EAST, NO.FLIP_UP, NO.FLIP_EAST, NO.FLIP_UP, NO.EAST, NO.UP, NO.EAST}),
        WEST(new class_2350[]{class_2350.field_11036, class_2350.field_11033, class_2350.field_11043, class_2350.field_11035}, 0.6f, true, new NO[]{NO.UP, NO.SOUTH, NO.UP, NO.FLIP_SOUTH, NO.FLIP_UP, NO.FLIP_SOUTH, NO.FLIP_UP, NO.SOUTH}, new NO[]{NO.UP, NO.NORTH, NO.UP, NO.FLIP_NORTH, NO.FLIP_UP, NO.FLIP_NORTH, NO.FLIP_UP, NO.NORTH}, new NO[]{NO.DOWN, NO.NORTH, NO.DOWN, NO.FLIP_NORTH, NO.FLIP_DOWN, NO.FLIP_NORTH, NO.FLIP_DOWN, NO.NORTH}, new NO[]{NO.DOWN, NO.SOUTH, NO.DOWN, NO.FLIP_SOUTH, NO.FLIP_DOWN, NO.FLIP_SOUTH, NO.FLIP_DOWN, NO.SOUTH}),
        EAST(new class_2350[]{class_2350.field_11033, class_2350.field_11036, class_2350.field_11043, class_2350.field_11035}, 0.6f, true, new NO[]{NO.FLIP_DOWN, NO.SOUTH, NO.FLIP_DOWN, NO.FLIP_SOUTH, NO.DOWN, NO.FLIP_SOUTH, NO.DOWN, NO.SOUTH}, new NO[]{NO.FLIP_DOWN, NO.NORTH, NO.FLIP_DOWN, NO.FLIP_NORTH, NO.DOWN, NO.FLIP_NORTH, NO.DOWN, NO.NORTH}, new NO[]{NO.FLIP_UP, NO.NORTH, NO.FLIP_UP, NO.FLIP_NORTH, NO.UP, NO.FLIP_NORTH, NO.UP, NO.NORTH}, new NO[]{NO.FLIP_UP, NO.SOUTH, NO.FLIP_UP, NO.FLIP_SOUTH, NO.UP, NO.FLIP_SOUTH, NO.UP, NO.SOUTH});

        final class_2350[] faces;
        final boolean nonCubicWeight;
        final NO[] neighbor1;
        final NO[] neighbor2;
        final NO[] neighbor3;
        final NO[] neighbor4;
        private static final ND[] VALUES;

        private ND(class_2350[] faces, float f, boolean nonCubicWeight, NO[] neighbor1, NO[] neighbor2, NO[] neighbor3, NO[] neighbor4) {
            this.faces = faces;
            this.nonCubicWeight = nonCubicWeight;
            this.neighbor1 = neighbor1;
            this.neighbor2 = neighbor2;
            this.neighbor3 = neighbor3;
            this.neighbor4 = neighbor4;
        }

        public static ND getData(class_2350 direction) {
            return VALUES[direction.method_10146()];
        }

        static {
            VALUES = (ND[])class_156.method_654((Object)new ND[6], values -> {
                values[class_2350.field_11033.method_10146()] = DOWN;
                values[class_2350.field_11036.method_10146()] = UP;
                values[class_2350.field_11043.method_10146()] = NORTH;
                values[class_2350.field_11035.method_10146()] = SOUTH;
                values[class_2350.field_11039.method_10146()] = WEST;
                values[class_2350.field_11034.method_10146()] = EAST;
            });
        }
    }

    public static class BC {
        private boolean enabled;
        private final Long2IntLinkedOpenHashMap intCache = (Long2IntLinkedOpenHashMap)class_156.method_656(() -> {
            Long2IntLinkedOpenHashMap long2IntLinkedOpenHashMap = new Long2IntLinkedOpenHashMap(100, 0.25f){

                protected void rehash(int newN) {
                }
            };
            long2IntLinkedOpenHashMap.defaultReturnValue(Integer.MAX_VALUE);
            return long2IntLinkedOpenHashMap;
        });
        private final Long2FloatLinkedOpenHashMap floatCache = (Long2FloatLinkedOpenHashMap)class_156.method_656(() -> {
            Long2FloatLinkedOpenHashMap long2FloatLinkedOpenHashMap = new Long2FloatLinkedOpenHashMap(100, 0.25f){

                protected void rehash(int newN) {
                }
            };
            long2FloatLinkedOpenHashMap.defaultReturnValue(Float.NaN);
            return long2FloatLinkedOpenHashMap;
        });

        public void enable() {
            this.enabled = true;
        }

        public void disable() {
            this.enabled = false;
            this.intCache.clear();
            this.floatCache.clear();
        }

        public int getInt(class_2680 state, class_1920 world, class_2338 pos) {
            int i;
            long l = pos.method_10063();
            if (this.enabled && (i = this.intCache.get(l)) != Integer.MAX_VALUE) {
                return i;
            }
            i = class_761.method_23793((class_1920)world, (class_2680)state, (class_2338)pos);
            if (this.enabled) {
                if (this.intCache.size() == 100) {
                    this.intCache.removeFirstInt();
                }
                this.intCache.put(l, i);
            }
            return i;
        }

        public float getFloat(class_2680 state, class_1920 blockView, class_2338 pos) {
            float f;
            long l = pos.method_10063();
            if (this.enabled && !Float.isNaN(f = this.floatCache.get(l))) {
                return f;
            }
            f = state.method_26210((class_1922)blockView, pos);
            if (this.enabled) {
                if (this.floatCache.size() == 100) {
                    this.floatCache.removeFirstFloat();
                }
                this.floatCache.put(l, f);
            }
            return f;
        }
    }

    private static enum Tl {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);

        final int firstCorner;
        final int secondCorner;
        final int thirdCorner;
        final int fourthCorner;
        private static final Tl[] VALUES;

        private Tl(int firstCorner, int secondCorner, int thirdCorner, int fourthCorner) {
            this.firstCorner = firstCorner;
            this.secondCorner = secondCorner;
            this.thirdCorner = thirdCorner;
            this.fourthCorner = fourthCorner;
        }

        public static Tl getTranslations(class_2350 direction) {
            return VALUES[direction.method_10146()];
        }

        static {
            VALUES = (Tl[])class_156.method_654((Object)new Tl[6], values -> {
                values[class_2350.field_11033.method_10146()] = DOWN;
                values[class_2350.field_11036.method_10146()] = UP;
                values[class_2350.field_11043.method_10146()] = NORTH;
                values[class_2350.field_11035.method_10146()] = SOUTH;
                values[class_2350.field_11039.method_10146()] = WEST;
                values[class_2350.field_11034.method_10146()] = EAST;
            });
        }
    }

    protected static enum NO {
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

        final int shape;

        private NO(class_2350 direction, boolean flip) {
            this.shape = direction.method_10146() + (flip ? DIRECTIONS.length : 0);
        }
    }
}

