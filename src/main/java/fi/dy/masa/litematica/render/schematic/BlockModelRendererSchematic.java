/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.PositionUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_10418
 *  net.minecraft.class_1087
 *  net.minecraft.class_1092
 *  net.minecraft.class_128
 *  net.minecraft.class_129
 *  net.minecraft.class_148
 *  net.minecraft.class_1920
 *  net.minecraft.class_1922
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2464
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_324
 *  net.minecraft.class_3532
 *  net.minecraft.class_3610
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4608
 *  net.minecraft.class_4696
 *  net.minecraft.class_5539
 *  net.minecraft.class_5819
 *  net.minecraft.class_6566
 *  net.minecraft.class_6575
 *  net.minecraft.class_761
 *  net.minecraft.class_775
 *  net.minecraft.class_777
 *  net.minecraft.class_811
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.schematic.ao.AOProcessor;
import fi.dy.masa.litematica.render.schematic.ao.AOProcessorModern;
import fi.dy.masa.malilib.util.PositionUtils;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_10418;
import net.minecraft.class_1087;
import net.minecraft.class_1092;
import net.minecraft.class_128;
import net.minecraft.class_129;
import net.minecraft.class_148;
import net.minecraft.class_1920;
import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2464;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_324;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_4696;
import net.minecraft.class_5539;
import net.minecraft.class_5819;
import net.minecraft.class_6566;
import net.minecraft.class_6575;
import net.minecraft.class_761;
import net.minecraft.class_775;
import net.minecraft.class_777;
import net.minecraft.class_811;
import org.jetbrains.annotations.ApiStatus;

public class BlockModelRendererSchematic {
    public static final ThreadLocal<AOProcessorModern.BC> CACHE = ThreadLocal.withInitial(AOProcessorModern.BC::new);
    private final class_6575 random = new class_6575(0L);
    private final class_324 colorMap;
    private final class_775 liquidRenderer;
    private class_1092 bakedManager;

    public BlockModelRendererSchematic(class_324 blockColorsIn) {
        this.colorMap = blockColorsIn;
        this.liquidRenderer = new class_775();
    }

    public void setBakedManager(class_1092 manager) {
        this.bakedManager = manager;
    }

    public static void enableCache() {
        if (Configs.Visuals.RENDER_AO_MODERN_ENABLE.getBooleanValue()) {
            CACHE.get().enable();
        }
    }

    public static void disableCache() {
        if (Configs.Visuals.RENDER_AO_MODERN_ENABLE.getBooleanValue()) {
            CACHE.get().disable();
        }
    }

    public boolean renderModel(class_1920 worldIn, class_1087 modelIn, class_2680 stateIn, class_2338 posIn, class_4587 matrixStack, class_4588 vertexConsumer, long rand) {
        boolean ao = class_310.method_1588() && stateIn.method_26213() == 0 && modelIn.method_4708();
        class_243 offset = stateIn.method_26226(posIn);
        matrixStack.method_46416((float)offset.field_1352, (float)offset.field_1351, (float)offset.field_1350);
        int overlay = class_4608.field_21444;
        try {
            if (ao) {
                return this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, matrixStack, vertexConsumer, (class_6566)this.random, rand, overlay);
            }
            return this.renderModelFlat(worldIn, modelIn, stateIn, posIn, matrixStack, vertexConsumer, (class_6566)this.random, rand, overlay);
        }
        catch (Throwable throwable) {
            class_128 crashreport = class_128.method_560((Throwable)throwable, (String)"Tesselating block model");
            class_129 crashreportcategory = crashreport.method_562("Block model being tesselated");
            class_129.method_586((class_129)crashreportcategory, (class_5539)worldIn, (class_2338)posIn, (class_2680)stateIn);
            crashreportcategory.method_578("Using AO", (Object)ao);
            throw new class_148(crashreport);
        }
    }

    private boolean renderModelSmooth(class_1920 worldIn, class_1087 modelIn, class_2680 stateIn, class_2338 posIn, class_4587 matrixStack, class_4588 vertexConsumer, class_6566 random, long seedIn, int overlay) {
        boolean renderedSomething = false;
        float[] quadBounds = new float[PositionUtils.ALL_DIRECTIONS.length * 2];
        BitSet bitset = new BitSet(3);
        AOProcessor aoFace = AOProcessor.get();
        class_2338.class_2339 mutablePos = posIn.method_25503();
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            random.method_43052(seedIn);
            List quads = modelIn.method_4707(stateIn, side, (class_5819)random);
            if (quads.isEmpty()) continue;
            mutablePos.method_25505((class_2382)posIn, side);
            if (!this.shouldRenderModelSide(worldIn, stateIn, posIn, side, (class_2338)mutablePos)) continue;
            this.renderQuadsSmooth(worldIn, stateIn, posIn, matrixStack, vertexConsumer, quads, quadBounds, bitset, aoFace, overlay);
            renderedSomething = true;
        }
        random.method_43052(seedIn);
        List quads = modelIn.method_4707(stateIn, null, (class_5819)random);
        if (!quads.isEmpty()) {
            this.renderQuadsSmooth(worldIn, stateIn, posIn, matrixStack, vertexConsumer, quads, quadBounds, bitset, aoFace, overlay);
            renderedSomething = true;
        }
        return renderedSomething;
    }

    private boolean renderModelFlat(class_1920 worldIn, class_1087 modelIn, class_2680 stateIn, class_2338 posIn, class_4587 matrixStack, class_4588 vertexConsumer, class_6566 random, long seedIn, int overlay) {
        boolean renderedSomething = false;
        BitSet bitset = new BitSet(3);
        class_2338.class_2339 mutablePos = posIn.method_25503();
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            random.method_43052(seedIn);
            List quads = modelIn.method_4707(stateIn, side, (class_5819)random);
            if (quads.isEmpty()) continue;
            mutablePos.method_25505((class_2382)posIn, side);
            if (!this.shouldRenderModelSide(worldIn, stateIn, posIn, side, (class_2338)mutablePos)) continue;
            int light = class_761.method_23793((class_1920)worldIn, (class_2680)stateIn, (class_2338)mutablePos);
            this.renderQuadsFlat(worldIn, stateIn, posIn, light, overlay, false, matrixStack, vertexConsumer, quads, bitset);
            renderedSomething = true;
        }
        random.method_43052(seedIn);
        List quads = modelIn.method_4707(stateIn, null, (class_5819)random);
        if (!quads.isEmpty()) {
            this.renderQuadsFlat(worldIn, stateIn, posIn, -1, overlay, true, matrixStack, vertexConsumer, quads, bitset);
            renderedSomething = true;
        }
        return renderedSomething;
    }

    private boolean shouldRenderModelSide(class_1920 worldIn, class_2680 stateIn, class_2338 posIn, class_2350 side, class_2338 mutable) {
        return DataManager.getRenderLayerRange().isPositionAtRenderEdgeOnSide(posIn, side) || Configs.Visuals.RENDER_BLOCKS_AS_TRANSLUCENT.getBooleanValue() && Configs.Visuals.RENDER_TRANSLUCENT_INNER_SIDES.getBooleanValue() || class_2248.method_9607((class_2680)stateIn, (class_2680)worldIn.method_8320(mutable), (class_2350)side);
    }

    private void renderQuadsSmooth(class_1920 world, class_2680 state, class_2338 pos, class_4587 matrixStack, class_4588 vertexConsumer, List<class_777> list, float[] box, BitSet flags, AOProcessor aoCalc, int overlay) {
        int size = list.size();
        for (class_777 bakedQuad : list) {
            this.getQuadDimensions(world, state, pos, bakedQuad.method_3357(), bakedQuad.method_3358(), box, flags);
            aoCalc.apply(world, state, pos, bakedQuad.method_3358(), box, flags, bakedQuad.method_24874());
            this.renderQuad(world, state, pos, vertexConsumer, matrixStack, bakedQuad, aoCalc.brightness, aoCalc.light, overlay);
        }
    }

    private void renderQuadsFlat(class_1920 world, class_2680 state, class_2338 pos, int light, int overlay, boolean useWorldLight, class_4587 matrixStack, class_4588 vertexConsumer, List<class_777> list, BitSet flags) {
        for (class_777 bakedQuad : list) {
            if (useWorldLight) {
                this.getQuadDimensions(world, state, pos, bakedQuad.method_3357(), bakedQuad.method_3358(), null, flags);
                class_2338 blockPos = flags.get(0) ? pos.method_10093(bakedQuad.method_3358()) : pos;
                light = class_761.method_23793((class_1920)world, (class_2680)state, (class_2338)blockPos);
            }
            float b = world.method_24852(bakedQuad.method_3358(), bakedQuad.method_24874());
            int[] lo = new int[]{light, light, light, light};
            float[] bo = new float[]{b, b, b, b};
            this.renderQuad(world, state, pos, vertexConsumer, matrixStack, bakedQuad, bo, lo, overlay);
        }
    }

    private void renderQuad(class_1920 world, class_2680 state, class_2338 pos, class_4588 vertexConsumer, class_4587 matrixStack, class_777 quad, float[] brightness, int[] light, int overlay) {
        float b;
        float g;
        float r;
        if (quad.method_3360()) {
            int color = this.colorMap.method_1697(state, world, pos, quad.method_3359());
            r = (float)(color >> 16 & 0xFF) / 255.0f;
            g = (float)(color >> 8 & 0xFF) / 255.0f;
            b = (float)(color & 0xFF) / 255.0f;
        } else {
            r = 1.0f;
            g = 1.0f;
            b = 1.0f;
        }
        float a = 1.0f;
        vertexConsumer.method_22920(matrixStack.method_23760(), quad, brightness, r, g, b, a, light, overlay, true);
    }

    private void getQuadDimensions(class_1920 world, class_2680 state, class_2338 pos, int[] vertexData, class_2350 face, @Nullable float[] box, BitSet flags) {
        float minX = 32.0f;
        float minY = 32.0f;
        float minZ = 32.0f;
        float maxX = -32.0f;
        float maxY = -32.0f;
        float maxZ = -32.0f;
        int vertexSize = vertexData.length / 4;
        for (int index = 0; index < 4; ++index) {
            float x = Float.intBitsToFloat(vertexData[index * vertexSize]);
            float y = Float.intBitsToFloat(vertexData[index * vertexSize + 1]);
            float z = Float.intBitsToFloat(vertexData[index * vertexSize + 2]);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }
        if (box != null) {
            box[class_2350.field_11039.method_10146()] = minX;
            box[class_2350.field_11034.method_10146()] = maxX;
            box[class_2350.field_11033.method_10146()] = minY;
            box[class_2350.field_11036.method_10146()] = maxY;
            box[class_2350.field_11043.method_10146()] = minZ;
            box[class_2350.field_11035.method_10146()] = maxZ;
            box[class_2350.field_11039.method_10146() + 6] = 1.0f - minX;
            box[class_2350.field_11034.method_10146() + 6] = 1.0f - maxX;
            box[class_2350.field_11033.method_10146() + 6] = 1.0f - minY;
            box[class_2350.field_11036.method_10146() + 6] = 1.0f - maxY;
            box[class_2350.field_11043.method_10146() + 6] = 1.0f - minZ;
            box[class_2350.field_11035.method_10146() + 6] = 1.0f - maxZ;
        }
        float min = 1.0E-4f;
        float max = 0.9999f;
        switch (face) {
            case field_11033: {
                flags.set(1, minX >= min || minZ >= min || maxX <= max || maxZ <= max);
                flags.set(0, minY == maxY && (minY < min || state.method_26234((class_1922)world, pos)));
                break;
            }
            case field_11036: {
                flags.set(1, minX >= min || minZ >= min || maxX <= max || maxZ <= max);
                flags.set(0, minY == maxY && (maxY > max || state.method_26234((class_1922)world, pos)));
                break;
            }
            case field_11043: {
                flags.set(1, minX >= min || minY >= min || maxX <= max || maxY <= max);
                flags.set(0, minZ == maxZ && (minZ < min || state.method_26234((class_1922)world, pos)));
                break;
            }
            case field_11035: {
                flags.set(1, minX >= min || minY >= min || maxX <= max || maxY <= max);
                flags.set(0, minZ == maxZ && (maxZ > max || state.method_26234((class_1922)world, pos)));
                break;
            }
            case field_11039: {
                flags.set(1, minY >= min || minZ >= min || maxY <= max || maxZ <= max);
                flags.set(0, minX == maxX && (minX < min || state.method_26234((class_1922)world, pos)));
                break;
            }
            case field_11034: {
                flags.set(1, minY >= min || minZ >= min || maxY <= max || maxZ <= max);
                flags.set(0, minX == maxX && (maxX > max || state.method_26234((class_1922)world, pos)));
            }
        }
    }

    @ApiStatus.Experimental
    public void renderBlockEntity(class_4588 vertexConsumer, class_4587 matrixStack, @Nullable class_2680 stateIn, class_1087 modelIn, float red, float green, float blue, int light, int overlay) {
        class_5819 rand = class_5819.method_43047();
        long life = 42L;
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            rand.method_43052(life);
            this.renderBlockEntityQuads(vertexConsumer, matrixStack, red, green, blue, modelIn.method_4707(stateIn, side, rand), light, overlay);
        }
        rand.method_43052(life);
        this.renderBlockEntityQuads(vertexConsumer, matrixStack, red, green, blue, modelIn.method_4707(stateIn, null, rand), light, overlay);
    }

    @ApiStatus.Experimental
    private void renderBlockEntityQuads(class_4588 vertexConsumer, class_4587 matrixStack, float red, float green, float blue, List<class_777> quads, int light, int overlay) {
        for (class_777 quad : quads) {
            float h;
            float g;
            float f;
            if (quad.method_3360()) {
                f = class_3532.method_15363((float)red, (float)0.0f, (float)1.0f);
                g = class_3532.method_15363((float)green, (float)0.0f, (float)1.0f);
                h = class_3532.method_15363((float)blue, (float)0.0f, (float)1.0f);
            } else {
                h = 1.0f;
                g = 1.0f;
                f = 1.0f;
            }
            vertexConsumer.method_22919(matrixStack.method_23760(), quad, f, g, h, 1.0f, light, overlay);
        }
    }

    @ApiStatus.Experimental
    public void renderLiquid(class_4588 consumer, class_1920 world, class_2338 pos, class_2680 stateIn, class_3610 fluid) {
        try {
            this.liquidRenderer.method_3347(world, pos, consumer, stateIn, fluid);
        }
        catch (Throwable var9) {
            class_128 crashReport = class_128.method_560((Throwable)var9, (String)"Tesselating liquid in world");
            class_129 crashReportSection = crashReport.method_562("Block being tesselated");
            class_129.method_586((class_129)crashReportSection, (class_5539)world, (class_2338)pos, null);
            throw new class_148(crashReport);
        }
    }

    public class_1087 getBakedModel(class_2680 stateIn) {
        return this.bakedManager.method_4743().method_3335(stateIn);
    }

    @ApiStatus.Experimental
    public boolean renderBlockEntity(class_4597 consumer, class_4587 matrixStack, class_2680 stateIn, int light, int overlay) {
        class_2464 blockRenderType = stateIn.method_26217();
        if (blockRenderType == class_2464.field_11455) {
            return false;
        }
        class_1087 bakedModel = this.getBakedModel(stateIn);
        int i = this.colorMap.method_1697(stateIn, null, null, 0);
        float red = (float)(i >> 16 & 0xFF) / 255.0f;
        float green = (float)(i >> 8 & 0xFF) / 255.0f;
        float blue = (float)(i & 0xFF) / 255.0f;
        this.renderBlockEntity(consumer.getBuffer(class_4696.method_23683((class_2680)stateIn)), matrixStack, stateIn, bakedModel, red, green, blue, light, overlay);
        ((class_10418)this.bakedManager.method_65756().get()).method_65535(stateIn.method_26204(), class_811.field_4315, matrixStack, consumer, light, overlay);
        return true;
    }
}

