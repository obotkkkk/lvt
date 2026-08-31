/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.LeftRight
 *  fi.dy.masa.malilib.render.InventoryOverlay
 *  fi.dy.masa.malilib.render.InventoryOverlay$Context
 *  fi.dy.masa.malilib.render.InventoryOverlay$InventoryProperties
 *  fi.dy.masa.malilib.render.InventoryOverlay$InventoryRenderType
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  fi.dy.masa.malilib.util.nbt.NbtBlockUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1087
 *  net.minecraft.class_1263
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2487
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_5819
 *  net.minecraft.class_777
 *  net.minecraft.class_8887
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package fi.dy.masa.litematica.render;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.render.OverlayRenderer;
import fi.dy.masa.litematica.util.BlockInfoAlignment;
import fi.dy.masa.litematica.util.InventoryUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.render.InventoryOverlay;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import fi.dy.masa.malilib.util.nbt.NbtBlockUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1087;
import net.minecraft.class_1263;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2487;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_5819;
import net.minecraft.class_777;
import net.minecraft.class_8887;
import net.minecraft.class_9801;
import org.joml.Matrix4f;

public class RenderUtils {
    public static int getMaxStringRenderLength(List<String> list) {
        int length = 0;
        for (String str : list) {
            length = Math.max(length, StringUtils.getStringWidth((String)str));
        }
        return length;
    }

    static void startDrawingLines() {
        RenderSystem.setShader((class_10156)class_10142.field_53876);
    }

    public static void renderBlockOutline(class_2338 pos, float expand, float lineWidth, Color4f color, class_310 mc) {
        RenderSystem.lineWidth((float)lineWidth);
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        RenderUtils.startDrawingLines();
        RenderUtils.drawBlockBoundingBoxOutlinesBatchedLines(pos, color, expand, buffer, mc);
        try {
            class_9801 meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception e) {
            Litematica.LOGGER.error("renderBlockOutline: Failed to draw Area Selection box (Error: {})", (Object)e.getLocalizedMessage());
        }
    }

    public static void drawBlockBoundingBoxOutlinesBatchedLines(class_2338 pos, Color4f color, double expand, class_287 buffer, class_310 mc) {
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        float minX = (float)((double)pos.method_10263() - dx - expand);
        float minY = (float)((double)pos.method_10264() - dy - expand);
        float minZ = (float)((double)pos.method_10260() - dz - expand);
        float maxX = (float)((double)pos.method_10263() - dx + expand + 1.0);
        float maxY = (float)((double)pos.method_10264() - dy + expand + 1.0);
        float maxZ = (float)((double)pos.method_10260() - dz + expand + 1.0);
        fi.dy.masa.malilib.render.RenderUtils.drawBoxAllEdgesBatchedLines((float)minX, (float)minY, (float)minZ, (float)maxX, (float)maxY, (float)maxZ, (Color4f)color, (class_287)buffer);
    }

    public static void drawBlockBoundingBoxOutlinesBatchedLines(class_2338 pos, class_2338 relPos, Color4f color, double expand, class_287 buffer) {
        class_243 cameraPos = relPos.method_46558();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        float minX = (float)((double)pos.method_10263() - dx - expand);
        float minY = (float)((double)pos.method_10264() - dy - expand);
        float minZ = (float)((double)pos.method_10260() - dz - expand);
        float maxX = (float)((double)pos.method_10263() - dx + expand + 1.0);
        float maxY = (float)((double)pos.method_10264() - dy + expand + 1.0);
        float maxZ = (float)((double)pos.method_10260() - dz + expand + 1.0);
        fi.dy.masa.malilib.render.RenderUtils.drawBoxAllEdgesBatchedLines((float)minX, (float)minY, (float)minZ, (float)maxX, (float)maxY, (float)maxZ, (Color4f)color, (class_287)buffer);
    }

    public static void drawConnectingLineBatchedLines(class_2338 pos1, class_2338 pos2, boolean center, Color4f color, class_287 buffer, class_310 mc) {
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        float x1 = (float)((double)pos1.method_10263() - dx);
        float y1 = (float)((double)pos1.method_10264() - dy);
        float z1 = (float)((double)pos1.method_10260() - dz);
        float x2 = (float)((double)pos2.method_10263() - dx);
        float y2 = (float)((double)pos2.method_10264() - dy);
        float z2 = (float)((double)pos2.method_10260() - dz);
        if (center) {
            x1 += 0.5f;
            y1 += 0.5f;
            z1 += 0.5f;
            x2 += 0.5f;
            y2 += 0.5f;
            z2 += 0.5f;
        }
        buffer.method_22912(x1, y1, z1).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(x2, y2, z2).method_22915(color.r, color.g, color.b, color.a);
    }

    public static void renderBlockOutlineOverlapping(class_2338 pos, float expand, float lineWidth, Color4f color1, Color4f color2, Color4f color3, Matrix4f matrix4f, class_310 mc) {
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        float minX = (float)((double)pos.method_10263() - dx - (double)expand);
        float minY = (float)((double)pos.method_10264() - dy - (double)expand);
        float minZ = (float)((double)pos.method_10260() - dz - (double)expand);
        float maxX = (float)((double)pos.method_10263() - dx + (double)expand + 1.0);
        float maxY = (float)((double)pos.method_10264() - dy + (double)expand + 1.0);
        float maxZ = (float)((double)pos.method_10260() - dz + (double)expand + 1.0);
        RenderSystem.lineWidth((float)lineWidth);
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        RenderUtils.startDrawingLines();
        buffer.method_22912(minX, minY, minZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(minX, minY, minZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(minX, minY, minZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color1.r, color1.g, color1.b, color1.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color2.r, color2.g, color2.b, color2.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color3.r, color3.g, color3.b, color3.a);
        try {
            class_9801 meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception e) {
            Litematica.LOGGER.error("renderBlockOutlineOverlapping: Failed to draw Area Selection box (Error: {})", (Object)e.getLocalizedMessage());
        }
    }

    public static void renderAreaOutline(class_2338 pos1, class_2338 pos2, float lineWidth, Color4f colorX, Color4f colorY, Color4f colorZ, class_310 mc) {
        RenderSystem.lineWidth((float)lineWidth);
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        double minX = (double)Math.min(pos1.method_10263(), pos2.method_10263()) - dx;
        double minY = (double)Math.min(pos1.method_10264(), pos2.method_10264()) - dy;
        double minZ = (double)Math.min(pos1.method_10260(), pos2.method_10260()) - dz;
        double maxX = (double)Math.max(pos1.method_10263(), pos2.method_10263()) - dx + 1.0;
        double maxY = (double)Math.max(pos1.method_10264(), pos2.method_10264()) - dy + 1.0;
        double maxZ = (double)Math.max(pos1.method_10260(), pos2.method_10260()) - dz + 1.0;
        RenderUtils.drawBoundingBoxEdges((float)minX, (float)minY, (float)minZ, (float)maxX, (float)maxY, (float)maxZ, colorX, colorY, colorZ);
    }

    private static void drawBoundingBoxEdges(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color4f colorX, Color4f colorY, Color4f colorZ) {
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        RenderUtils.startDrawingLines();
        RenderUtils.drawBoundingBoxLinesX(buffer, minX, minY, minZ, maxX, maxY, maxZ, colorX);
        RenderUtils.drawBoundingBoxLinesY(buffer, minX, minY, minZ, maxX, maxY, maxZ, colorY);
        RenderUtils.drawBoundingBoxLinesZ(buffer, minX, minY, minZ, maxX, maxY, maxZ, colorZ);
        try {
            class_9801 meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void drawBoundingBoxLinesX(class_287 buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color4f color) {
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
    }

    private static void drawBoundingBoxLinesY(class_287 buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color4f color) {
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
    }

    private static void drawBoundingBoxLinesZ(class_287 buffer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color4f color) {
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
    }

    public static void renderAreaSides(class_2338 pos1, class_2338 pos2, Color4f color, Matrix4f matrix4f, class_310 mc) {
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        RenderUtils.renderAreaSidesBatched(pos1, pos2, color, 0.002, buffer, mc);
        try {
            class_9801 meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void renderAreaSidesBatched(class_2338 pos1, class_2338 pos2, Color4f color, double expand, class_287 buffer, class_310 mc) {
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        double minX = (double)Math.min(pos1.method_10263(), pos2.method_10263()) - dx - expand;
        double minY = (double)Math.min(pos1.method_10264(), pos2.method_10264()) - dy - expand;
        double minZ = (double)Math.min(pos1.method_10260(), pos2.method_10260()) - dz - expand;
        double maxX = (double)(Math.max(pos1.method_10263(), pos2.method_10263()) + 1) - dx + expand;
        double maxY = (double)(Math.max(pos1.method_10264(), pos2.method_10264()) + 1) - dy + expand;
        double maxZ = (double)(Math.max(pos1.method_10260(), pos2.method_10260()) + 1) - dz + expand;
        fi.dy.masa.malilib.render.RenderUtils.drawBoxAllSidesBatchedQuads((float)((float)minX), (float)((float)minY), (float)((float)minZ), (float)((float)maxX), (float)((float)maxY), (float)((float)maxZ), (Color4f)color, (class_287)buffer);
    }

    public static void renderAreaOutlineNoCorners(class_2338 pos1, class_2338 pos2, float lineWidth, Color4f colorX, Color4f colorY, Color4f colorZ, class_310 mc) {
        int end;
        int xMin = Math.min(pos1.method_10263(), pos2.method_10263());
        int yMin = Math.min(pos1.method_10264(), pos2.method_10264());
        int zMin = Math.min(pos1.method_10260(), pos2.method_10260());
        int xMax = Math.max(pos1.method_10263(), pos2.method_10263());
        int yMax = Math.max(pos1.method_10264(), pos2.method_10264());
        int zMax = Math.max(pos1.method_10260(), pos2.method_10260());
        double expand = 0.001;
        class_243 cameraPos = mc.field_1773.method_19418().method_19326();
        double dx = cameraPos.field_1352;
        double dy = cameraPos.field_1351;
        double dz = cameraPos.field_1350;
        float dxMin = (float)(-dx - 0.001);
        float dyMin = (float)(-dy - 0.001);
        float dzMin = (float)(-dz - 0.001);
        float dxMax = (float)(-dx + 0.001);
        float dyMax = (float)(-dy + 0.001);
        float dzMax = (float)(-dz + 0.001);
        float minX = (float)xMin + dxMin;
        float minY = (float)yMin + dyMin;
        float minZ = (float)zMin + dzMin;
        float maxX = (float)xMax + dxMax;
        float maxY = (float)yMax + dyMax;
        float maxZ = (float)zMax + dzMax;
        RenderSystem.lineWidth((float)lineWidth);
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        RenderUtils.startDrawingLines();
        int start = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? xMin + 1 : xMin;
        int n = end = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? xMax : xMax + 1;
        if (end > start) {
            buffer.method_22912((float)start + dxMin, minY, minZ).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
            buffer.method_22912((float)end + dxMax, minY, minZ).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? xMin + 1 : xMin;
        int n2 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? xMax : xMax + 1;
        if (end > start) {
            buffer.method_22912((float)start + dxMin, maxY + 1.0f, minZ).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
            buffer.method_22912((float)end + dxMax, maxY + 1.0f, minZ).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? xMin + 1 : xMin;
        int n3 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? xMax : xMax + 1;
        if (end > start) {
            buffer.method_22912((float)start + dxMin, minY, maxZ + 1.0f).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
            buffer.method_22912((float)end + dxMax, minY, maxZ + 1.0f).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? xMin + 1 : xMin;
        int n4 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? xMax : xMax + 1;
        if (end > start) {
            buffer.method_22912((float)start + dxMin, maxY + 1.0f, maxZ + 1.0f).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
            buffer.method_22912((float)end + dxMax, maxY + 1.0f, maxZ + 1.0f).method_22915(colorX.r, colorX.g, colorX.b, colorX.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? yMin + 1 : yMin;
        int n5 = end = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? yMax : yMax + 1;
        if (end > start) {
            buffer.method_22912(minX, (float)start + dyMin, minZ).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
            buffer.method_22912(minX, (float)end + dyMax, minZ).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
        }
        start = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? yMin + 1 : yMin;
        int n6 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? yMax : yMax + 1;
        if (end > start) {
            buffer.method_22912(maxX + 1.0f, (float)start + dyMin, minZ).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
            buffer.method_22912(maxX + 1.0f, (float)end + dyMax, minZ).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? yMin + 1 : yMin;
        int n7 = end = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? yMax : yMax + 1;
        if (end > start) {
            buffer.method_22912(minX, (float)start + dyMin, maxZ + 1.0f).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
            buffer.method_22912(minX, (float)end + dyMax, maxZ + 1.0f).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
        }
        start = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? yMin + 1 : yMin;
        int n8 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? yMax : yMax + 1;
        if (end > start) {
            buffer.method_22912(maxX + 1.0f, (float)start + dyMin, maxZ + 1.0f).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
            buffer.method_22912(maxX + 1.0f, (float)end + dyMax, maxZ + 1.0f).method_22915(colorY.r, colorY.g, colorY.b, colorY.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? zMin + 1 : zMin;
        int n9 = end = pos1.method_10263() == xMin && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? zMax : zMax + 1;
        if (end > start) {
            buffer.method_22912(minX, minY, (float)start + dzMin).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
            buffer.method_22912(minX, minY, (float)end + dzMax).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
        }
        start = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMin ? zMin + 1 : zMin;
        int n10 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMin && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMin && pos2.method_10260() == zMax ? zMax : zMax + 1;
        if (end > start) {
            buffer.method_22912(maxX + 1.0f, minY, (float)start + dzMin).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
            buffer.method_22912(maxX + 1.0f, minY, (float)end + dzMax).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
        }
        start = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? zMin + 1 : zMin;
        int n11 = end = pos1.method_10263() == xMin && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMin && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? zMax : zMax + 1;
        if (end > start) {
            buffer.method_22912(minX, maxY + 1.0f, (float)start + dzMin).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
            buffer.method_22912(minX, maxY + 1.0f, (float)end + dzMax).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
        }
        start = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMin || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMin ? zMin + 1 : zMin;
        int n12 = end = pos1.method_10263() == xMax && pos1.method_10264() == yMax && pos1.method_10260() == zMax || pos2.method_10263() == xMax && pos2.method_10264() == yMax && pos2.method_10260() == zMax ? zMax : zMax + 1;
        if (end > start) {
            buffer.method_22912(maxX + 1.0f, maxY + 1.0f, (float)start + dzMin).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
            buffer.method_22912(maxX + 1.0f, maxY + 1.0f, (float)end + dzMax).method_22915(colorZ.r, colorZ.g, colorZ.b, colorZ.a);
        }
        try {
            class_9801 meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void drawBlockModelOutlinesBatched(class_1087 model, class_2680 state, class_2338 pos, Color4f color, double expand, class_287 buffer, class_5819 rand) {
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            RenderUtils.renderModelQuadOutlines(model, state, pos, side, color, expand, buffer, rand);
        }
        RenderUtils.renderModelQuadOutlines(model, state, pos, null, color, expand, buffer, rand);
    }

    public static void renderModelQuadOutlines(class_1087 model, class_2680 state, class_2338 pos, class_2350 side, Color4f color, double expand, class_287 buffer, class_5819 rand) {
        try {
            RenderUtils.renderModelQuadOutlines(pos, buffer, color, model.method_4707(state, side, rand));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void renderModelQuadOutlines(class_2338 pos, class_287 buffer, Color4f color, List<class_777> quads) {
        int size = quads.size();
        for (int i = 0; i < size; ++i) {
            RenderUtils.renderQuadOutlinesBatched(pos, buffer, color, quads.get(i).method_3357());
        }
    }

    public static void renderQuadOutlinesBatched(class_2338 pos, class_287 buffer, Color4f color, int[] vertexData) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        int vertexSize = vertexData.length / 4;
        float[] fx = new float[4];
        float[] fy = new float[4];
        float[] fz = new float[4];
        for (int index = 0; index < 4; ++index) {
            fx[index] = (float)x + Float.intBitsToFloat(vertexData[index * vertexSize]);
            fy[index] = (float)y + Float.intBitsToFloat(vertexData[index * vertexSize + 1]);
            fz[index] = (float)z + Float.intBitsToFloat(vertexData[index * vertexSize + 2]);
        }
        buffer.method_22912(fx[0], fy[0], fz[0]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[1], fy[1], fz[1]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[1], fy[1], fz[1]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[2], fy[2], fz[2]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[2], fy[2], fz[2]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[3], fy[3], fz[3]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[3], fy[3], fz[3]).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(fx[0], fy[0], fz[0]).method_22915(color.r, color.g, color.b, color.a);
    }

    public static boolean stateModelHasQuads(class_2680 state, class_5819 rand) {
        return RenderUtils.modelHasQuads(Objects.requireNonNull(class_310.method_1551().method_1541().method_3349(state)), state, rand);
    }

    public static boolean modelHasQuads(@Nullable class_1087 model, class_2680 state, class_5819 rand) {
        if (model == null) {
            return false;
        }
        int totalSize = 0;
        for (class_2350 face : PositionUtils.ALL_DIRECTIONS) {
            totalSize += model.method_4707(state, face, rand).size();
        }
        return (totalSize += model.method_4707(state, null, rand).size()) > 0;
    }

    public static boolean hasQuadsForModel(class_1087 model, class_2680 state, @Nullable class_2350 side, class_5819 rand) {
        return !model.method_4707(state, side, rand).isEmpty();
    }

    public static void drawBlockModelQuadOverlayBatched(class_1087 model, class_2680 state, class_2338 pos, Color4f color, double expand, class_287 buffer, class_5819 rand) {
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            RenderUtils.drawBlockModelQuadOverlayBatched(model, state, pos, side, color, expand, buffer, rand);
        }
        RenderUtils.drawBlockModelQuadOverlayBatched(model, state, pos, null, color, expand, buffer, rand);
    }

    public static void drawBlockModelQuadOverlayBatched(class_1087 model, class_2680 state, class_2338 pos, class_2350 side, Color4f color, double expand, class_287 buffer, class_5819 rand) {
        try {
            RenderUtils.renderModelQuadOverlayBatched(pos, buffer, color, model.method_4707(state, side, rand));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void renderModelQuadOverlayBatched(class_2338 pos, class_287 buffer, Color4f color, List<class_777> quads) {
        for (class_777 quad : quads) {
            RenderUtils.renderModelQuadOverlayBatched(pos, buffer, color, quad.method_3357());
        }
    }

    private static void renderModelQuadOverlayBatched(class_2338 pos, class_287 buffer, Color4f color, int[] vertexData) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        int vertexSize = vertexData.length / 4;
        for (int index = 0; index < 4; ++index) {
            float fx = (float)x + Float.intBitsToFloat(vertexData[index * vertexSize]);
            float fy = (float)y + Float.intBitsToFloat(vertexData[index * vertexSize + 1]);
            float fz = (float)z + Float.intBitsToFloat(vertexData[index * vertexSize + 2]);
            buffer.method_22912(fx, fy, fz).method_22915(color.r, color.g, color.b, color.a);
        }
    }

    public static void drawBlockBoxSideBatchedQuads(class_2338 pos, class_2350 side, Color4f color, double expand, class_287 buffer) {
        float minX = (float)((double)pos.method_10263() - expand);
        float minY = (float)((double)pos.method_10264() - expand);
        float minZ = (float)((double)pos.method_10260() - expand);
        float maxX = (float)((double)pos.method_10263() + expand + 1.0);
        float maxY = (float)((double)pos.method_10264() + expand + 1.0);
        float maxZ = (float)((double)pos.method_10260() + expand + 1.0);
        switch (side) {
            case field_11033: {
                buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                break;
            }
            case field_11036: {
                buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                break;
            }
            case field_11043: {
                buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                break;
            }
            case field_11035: {
                buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                break;
            }
            case field_11039: {
                buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                break;
            }
            case field_11034: {
                buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
                buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
            }
        }
    }

    public static void drawBlockBoxEdgeBatchedLines(class_2338 pos, class_2350.class_2351 axis, int cornerIndex, Color4f color, class_287 buffer) {
        class_2382 offset = fi.dy.masa.litematica.util.PositionUtils.getEdgeNeighborOffsets(axis, cornerIndex)[cornerIndex];
        double minX = pos.method_10263() + offset.method_10263();
        double minY = pos.method_10264() + offset.method_10264();
        double minZ = pos.method_10260() + offset.method_10260();
        double maxX = pos.method_10263() + offset.method_10263() + (axis == class_2350.class_2351.field_11048 ? 1 : 0);
        double maxY = pos.method_10264() + offset.method_10264() + (axis == class_2350.class_2351.field_11052 ? 1 : 0);
        double maxZ = pos.method_10260() + offset.method_10260() + (axis == class_2350.class_2351.field_11051 ? 1 : 0);
        buffer.method_22912((float)minX, (float)minY, (float)minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912((float)maxX, (float)maxY, (float)maxZ).method_22915(color.r, color.g, color.b, color.a);
    }

    public static int renderInventoryOverlays(BlockInfoAlignment align, int offY, class_1937 worldSchematic, class_1937 worldClient, class_2338 pos, class_310 mc, class_332 drawContext) {
        int heightSch = RenderUtils.renderInventoryOverlay(align, LeftRight.LEFT, offY, worldSchematic, pos, mc, drawContext);
        int heightCli = RenderUtils.renderInventoryOverlay(align, LeftRight.RIGHT, offY, worldClient, pos, mc, drawContext);
        return Math.max(heightSch, heightCli);
    }

    public static int renderInventoryOverlay(BlockInfoAlignment align, LeftRight side, int offY, class_1937 world, class_2338 pos, class_310 mc, class_332 drawContext) {
        InventoryOverlay.Context ctx = InventoryUtils.getTargetInventory(world, pos);
        if (ctx != null && ctx.inv() != null) {
            InventoryOverlay.InventoryProperties props = InventoryOverlay.getInventoryPropsTemp((InventoryOverlay.InventoryRenderType)ctx.type(), (int)ctx.inv().method_5439());
            if (ctx.type() == InventoryOverlay.InventoryRenderType.CRAFTER) {
                HashSet<Integer> disabledSlots = new HashSet();
                if (ctx.nbt() != null && !ctx.nbt().method_33133()) {
                    disabledSlots = NbtBlockUtils.getDisabledSlotsFromNbt((class_2487)ctx.nbt());
                } else {
                    class_2586 class_25862 = ctx.be();
                    if (class_25862 instanceof class_8887) {
                        class_8887 cbe = (class_8887)class_25862;
                        disabledSlots = BlockUtils.getDisabledSlots((class_8887)cbe);
                    }
                }
                return RenderUtils.renderInventoryOverlay(align, side, offY, ctx.inv(), ctx.type(), props, disabledSlots, mc, drawContext);
            }
            return RenderUtils.renderInventoryOverlay(align, side, offY, ctx.inv(), ctx.type(), props, mc, drawContext);
        }
        return 0;
    }

    public static int renderInventoryOverlay(BlockInfoAlignment align, LeftRight side, int offY, class_1263 inv, InventoryOverlay.InventoryRenderType type, InventoryOverlay.InventoryProperties props, class_310 mc, class_332 drawContext) {
        return RenderUtils.renderInventoryOverlay(align, side, offY, inv, type, props, Set.of(), mc, drawContext);
    }

    public static int renderInventoryOverlay(BlockInfoAlignment align, LeftRight side, int offY, class_1263 inv, InventoryOverlay.InventoryRenderType type, InventoryOverlay.InventoryProperties props, Set<Integer> disabledSlots, class_310 mc, class_332 drawContext) {
        int xInv = 0;
        int yInv = 0;
        int compatShift = OverlayRenderer.calculateCompatYShift();
        switch (align) {
            case CENTER: {
                xInv = GuiUtils.getScaledWindowWidth() / 2 - props.width / 2;
                yInv = GuiUtils.getScaledWindowHeight() / 2 - props.height - offY;
                break;
            }
            case TOP_CENTER: {
                xInv = GuiUtils.getScaledWindowWidth() / 2 - props.width / 2;
                yInv = offY + compatShift;
            }
        }
        if (side == LeftRight.LEFT) {
            xInv -= props.width / 2 + 4;
        } else if (side == LeftRight.RIGHT) {
            xInv += props.width / 2 + 4;
        }
        fi.dy.masa.malilib.render.RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        InventoryOverlay.renderInventoryBackground((InventoryOverlay.InventoryRenderType)type, (int)xInv, (int)yInv, (int)props.slotsPerRow, (int)props.totalSlots, (class_310)mc);
        InventoryOverlay.renderInventoryStacks((InventoryOverlay.InventoryRenderType)type, (class_1263)inv, (int)(xInv + props.slotOffsetX), (int)(yInv + props.slotOffsetY), (int)props.slotsPerRow, (int)0, (int)inv.method_5439(), disabledSlots, (class_310)mc, (class_332)drawContext);
        return props.height + compatShift;
    }

    public static void renderBackgroundMask(int startX, int startY, int width, int height, class_332 drawContext) {
        fi.dy.masa.malilib.render.RenderUtils.drawTexturedRect((class_2960)GuiBase.BG_TEXTURE, (int)startX, (int)startY, (int)0, (int)0, (int)width, (int)height, (class_332)drawContext);
    }

    public static void drawBlockBoundingBoxOutlinesBatchedDebugLines(class_2338 pos, Color4f color, double expand, class_287 buffer) {
        RenderUtils.drawBoxAllEdgesBatchedDebugLines(pos, class_243.field_1353, color, expand, buffer);
    }

    public static void drawBoxAllEdgesBatchedDebugLines(class_2338 pos, class_243 cameraPos, Color4f color, double expand, class_287 buffer) {
        float minX = (float)((double)pos.method_10263() - expand - cameraPos.field_1352);
        float minY = (float)((double)pos.method_10264() - expand - cameraPos.field_1351);
        float minZ = (float)((double)pos.method_10260() - expand - cameraPos.field_1350);
        float maxX = (float)((double)pos.method_10263() + expand - cameraPos.field_1352 + 1.0);
        float maxY = (float)((double)pos.method_10264() + expand - cameraPos.field_1351 + 1.0);
        float maxZ = (float)((double)pos.method_10260() + expand - cameraPos.field_1350 + 1.0);
        RenderUtils.drawBoxAllEdgesBatchedDebugLines(minX, minY, minZ, maxX, maxY, maxZ, color, buffer);
    }

    public static void drawBoxAllEdgesBatchedDebugLines(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Color4f color, class_287 buffer) {
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, minZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, minY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(maxX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
        buffer.method_22912(minX, maxY, maxZ).method_22915(color.r, color.g, color.b, color.a);
    }
}

