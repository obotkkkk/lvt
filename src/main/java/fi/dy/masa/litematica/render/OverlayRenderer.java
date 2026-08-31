/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.config.HudAlignment
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.LeftRight
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.WorldUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2281
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_2745
 *  net.minecraft.class_2769
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_310
 *  net.minecraft.class_3218
 *  net.minecraft.class_332
 *  net.minecraft.class_3695
 *  net.minecraft.class_3965
 *  net.minecraft.class_7923
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 */
package fi.dy.masa.litematica.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.compat.jade.JadeCompat;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicVerificationResult;
import fi.dy.masa.litematica.render.BlockInfo;
import fi.dy.masa.litematica.render.RenderUtils;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.litematica.schematic.verifier.SchematicVerifier;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.util.BlockInfoAlignment;
import fi.dy.masa.litematica.util.InventoryUtils;
import fi.dy.masa.litematica.util.ItemUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2769;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_332;
import net.minecraft.class_3695;
import net.minecraft.class_3965;
import net.minecraft.class_7923;
import net.minecraft.class_9801;
import org.joml.Matrix4f;

public class OverlayRenderer {
    private static final OverlayRenderer INSTANCE = new OverlayRenderer();
    public static final int[] KELLY_COLORS = new int[]{16757504, 8404597, 16738304, 10927575, 12648480, 13541986, 8482918, 32052, 16152206, 21386, 16743004, 5453690, 16748032, 11741265, 16041984, 8329229, 9677312, 5845781, 15809043, 2305046};
    private final class_310 mc;
    private final Map<SchematicPlacement, ImmutableMap<String, Box>> placements = new HashMap<SchematicPlacement, ImmutableMap<String, Box>>();
    private Color4f colorPos1 = new Color4f(1.0f, 0.0625f, 0.0625f);
    private Color4f colorPos2 = new Color4f(0.0625f, 0.0625f, 1.0f);
    private Color4f colorOverlapping = new Color4f(1.0f, 0.0625f, 1.0f);
    private Color4f colorX = new Color4f(1.0f, 0.25f, 0.25f);
    private Color4f colorY = new Color4f(0.25f, 1.0f, 0.25f);
    private Color4f colorZ = new Color4f(0.25f, 0.25f, 1.0f);
    private Color4f colorArea = new Color4f(1.0f, 1.0f, 1.0f);
    private Color4f colorBoxPlacementSelected = new Color4f(0.08627451f, 1.0f, 1.0f);
    private Color4f colorSelectedCorner = new Color4f(0.0f, 1.0f, 1.0f);
    private Color4f colorAreaOrigin = new Color4f(1.0f, 0.5647059f, 0.0627451f);
    private long infoUpdateTime;
    private List<String> blockInfoLines = new ArrayList<String>();
    private int blockInfoX;
    private int blockInfoY;

    private OverlayRenderer() {
        this.mc = class_310.method_1551();
    }

    public static OverlayRenderer getInstance() {
        return INSTANCE;
    }

    public void updatePlacementCache() {
        this.placements.clear();
        List<SchematicPlacement> list = DataManager.getSchematicPlacementManager().getAllSchematicsPlacements();
        for (SchematicPlacement placement : list) {
            if (!placement.isEnabled()) continue;
            this.placements.put(placement, placement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED));
        }
    }

    public void renderBoxes(Matrix4f posMatrix, Matrix4f projMatrix, class_3695 profiler) {
        float lineWidthArea;
        profiler.method_15396("render");
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection currentSelection = sm.getCurrentSelection();
        boolean renderAreas = currentSelection != null && Configs.Visuals.ENABLE_AREA_SELECTION_RENDERING.getBooleanValue();
        boolean renderPlacements = !this.placements.isEmpty() && Configs.Visuals.ENABLE_PLACEMENT_BOXES_RENDERING.getBooleanValue();
        boolean isProjectMode = DataManager.getSchematicProjectsManager().hasProjectOpen();
        float expand = 0.001f;
        float lineWidthBlockBox = 2.0f;
        float f = lineWidthArea = isProjectMode ? 3.0f : 1.5f;
        if (renderAreas || renderPlacements || isProjectMode) {
            SchematicProject project;
            fi.dy.masa.malilib.render.RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            fi.dy.masa.malilib.render.RenderUtils.setupBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask((boolean)false);
            profiler.method_15405("render_areas");
            if (renderAreas) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-1.2f, (float)-0.2f);
                profiler.method_15396("selection_boxes");
                Box currentBox = currentSelection.getSelectedSubRegionBox();
                for (Box box : currentSelection.getAllSubRegionBoxes()) {
                    BoxType type = box == currentBox ? BoxType.AREA_SELECTED : BoxType.AREA_UNSELECTED;
                    this.renderSelectionBox(box, type, expand, lineWidthBlockBox, lineWidthArea, null, posMatrix);
                }
                class_2338 origin = currentSelection.getExplicitOrigin();
                if (origin != null) {
                    profiler.method_15405("area_sides");
                    if (currentSelection.isOriginSelected()) {
                        Color4f colorTmp = Color4f.fromColor((Color4f)this.colorAreaOrigin, (float)0.4f);
                        RenderUtils.renderAreaSides(origin, origin, colorTmp, posMatrix, this.mc);
                    }
                    profiler.method_15405("block_outlines");
                    Color4f color = currentSelection.isOriginSelected() ? this.colorSelectedCorner : this.colorAreaOrigin;
                    RenderUtils.renderBlockOutline(origin, expand, lineWidthBlockBox, color, this.mc);
                }
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
                profiler.method_15407();
            }
            profiler.method_15405("render_placements");
            if (renderPlacements) {
                SchematicPlacementManager spm = DataManager.getSchematicPlacementManager();
                SchematicPlacement currentPlacement = spm.getSelectedSchematicPlacement();
                profiler.method_15396("placement");
                for (Map.Entry<SchematicPlacement, ImmutableMap<String, Box>> entry : this.placements.entrySet()) {
                    SchematicPlacement schematicPlacement = entry.getKey();
                    ImmutableMap<String, Box> boxMap = entry.getValue();
                    boolean origin = schematicPlacement.getSelectedSubRegionPlacement() == null;
                    profiler.method_15405("selection_boxes");
                    for (Map.Entry entryBox : boxMap.entrySet()) {
                        String boxName = (String)entryBox.getKey();
                        boolean boxSelected = schematicPlacement == currentPlacement && (origin || boxName.equals(schematicPlacement.getSelectedSubRegionName()));
                        BoxType type = boxSelected ? BoxType.PLACEMENT_SELECTED : BoxType.PLACEMENT_UNSELECTED;
                        this.renderSelectionBox((Box)entryBox.getValue(), type, expand, 1.0f, 1.0f, schematicPlacement, posMatrix);
                    }
                    profiler.method_15405("block_outlines");
                    Color4f color = schematicPlacement == currentPlacement && origin ? this.colorSelectedCorner : schematicPlacement.getBoxesBBColor();
                    RenderUtils.renderBlockOutline(schematicPlacement.getOrigin(), expand, lineWidthBlockBox, color, this.mc);
                    profiler.method_15405("area_sides");
                    if (!Configs.Visuals.RENDER_PLACEMENT_ENCLOSING_BOX.getBooleanValue()) continue;
                    Box box = schematicPlacement.getEclosingBox();
                    if (!schematicPlacement.shouldRenderEnclosingBox() || box == null) continue;
                    RenderUtils.renderAreaOutline(box.getPos1(), box.getPos2(), 1.0f, color, color, color, this.mc);
                    if (!Configs.Visuals.RENDER_PLACEMENT_ENCLOSING_BOX_SIDES.getBooleanValue()) continue;
                    float alpha = (float)Configs.Visuals.PLACEMENT_BOX_SIDE_ALPHA.getDoubleValue();
                    color = new Color4f(color.r, color.g, color.b, alpha);
                    RenderUtils.renderAreaSides(box.getPos1(), box.getPos2(), color, posMatrix, this.mc);
                }
                profiler.method_15407();
            }
            profiler.method_15405("render_projects");
            if (isProjectMode && (project = DataManager.getSchematicProjectsManager().getCurrentProject()) != null) {
                RenderUtils.renderBlockOutline(project.getOrigin(), expand, 4.0f, this.colorOverlapping, this.mc);
            }
            RenderSystem.depthMask((boolean)true);
        }
        profiler.method_15407();
    }

    public void renderSelectionBox(Box box, BoxType boxType, float expand, float lineWidthBlockBox, float lineWidthArea, @Nullable SchematicPlacement placement, Matrix4f matrix4f) {
        Color4f sideColor;
        Color4f color2;
        Color4f color1;
        Color4f colorZ;
        Color4f colorY;
        Color4f colorX;
        class_2338 pos1 = box.getPos1();
        class_2338 pos2 = box.getPos2();
        if (pos1 == null && pos2 == null) {
            return;
        }
        switch (boxType.ordinal()) {
            case 0: {
                colorX = this.colorX;
                colorY = this.colorY;
                colorZ = this.colorZ;
                break;
            }
            case 1: {
                colorX = this.colorArea;
                colorY = this.colorArea;
                colorZ = this.colorArea;
                break;
            }
            case 2: {
                colorX = this.colorBoxPlacementSelected;
                colorY = this.colorBoxPlacementSelected;
                colorZ = this.colorBoxPlacementSelected;
                break;
            }
            case 3: {
                Color4f color;
                colorX = color = placement.getBoxesBBColor();
                colorY = color;
                colorZ = color;
                break;
            }
            default: {
                return;
            }
        }
        if (boxType == BoxType.PLACEMENT_SELECTED) {
            color2 = color1 = this.colorBoxPlacementSelected;
            alpha = (float)Configs.Visuals.PLACEMENT_BOX_SIDE_ALPHA.getDoubleValue();
            sideColor = new Color4f(color1.r, color1.g, color1.b, alpha);
        } else if (boxType == BoxType.PLACEMENT_UNSELECTED) {
            color2 = color1 = placement.getBoxesBBColor();
            alpha = (float)Configs.Visuals.PLACEMENT_BOX_SIDE_ALPHA.getDoubleValue();
            sideColor = new Color4f(color1.r, color1.g, color1.b, alpha);
        } else {
            color1 = box.getSelectedCorner() == PositionUtils.Corner.CORNER_1 ? this.colorSelectedCorner : this.colorPos1;
            color2 = box.getSelectedCorner() == PositionUtils.Corner.CORNER_2 ? this.colorSelectedCorner : this.colorPos2;
            sideColor = Color4f.fromColor((int)Configs.Colors.AREA_SELECTION_BOX_SIDE_COLOR.getIntegerValue());
        }
        if (pos1 != null && pos2 != null) {
            if (!pos1.equals((Object)pos2)) {
                RenderUtils.renderAreaOutlineNoCorners(pos1, pos2, lineWidthArea, colorX, colorY, colorZ, this.mc);
                if ((boxType == BoxType.AREA_SELECTED || boxType == BoxType.AREA_UNSELECTED) && Configs.Visuals.RENDER_AREA_SELECTION_BOX_SIDES.getBooleanValue() || (boxType == BoxType.PLACEMENT_SELECTED || boxType == BoxType.PLACEMENT_UNSELECTED) && Configs.Visuals.RENDER_PLACEMENT_BOX_SIDES.getBooleanValue()) {
                    RenderUtils.renderAreaSides(pos1, pos2, sideColor, matrix4f, this.mc);
                }
                if (box.getSelectedCorner() == PositionUtils.Corner.CORNER_1) {
                    Color4f color = Color4f.fromColor((Color4f)this.colorPos1, (float)0.4f);
                    RenderUtils.renderAreaSides(pos1, pos1, color, matrix4f, this.mc);
                } else if (box.getSelectedCorner() == PositionUtils.Corner.CORNER_2) {
                    Color4f color = Color4f.fromColor((Color4f)this.colorPos2, (float)0.4f);
                    RenderUtils.renderAreaSides(pos2, pos2, color, matrix4f, this.mc);
                }
                RenderUtils.renderBlockOutline(pos1, expand, lineWidthBlockBox, color1, this.mc);
                RenderUtils.renderBlockOutline(pos2, expand, lineWidthBlockBox, color2, this.mc);
            } else {
                RenderUtils.renderBlockOutlineOverlapping(pos1, expand, lineWidthBlockBox, color1, color2, this.colorOverlapping, matrix4f, this.mc);
            }
        } else {
            if (pos1 != null) {
                RenderUtils.renderBlockOutline(pos1, expand, lineWidthBlockBox, color1, this.mc);
            }
            if (pos2 != null) {
                RenderUtils.renderBlockOutline(pos2, expand, lineWidthBlockBox, color2, this.mc);
            }
        }
    }

    public void renderSchematicVerifierMismatches(Matrix4f posMatrix, Matrix4f projMatrix, class_3695 profiler) {
        SchematicVerifier verifier;
        List<SchematicVerifier.MismatchRenderPos> list;
        profiler.method_15396("render_mismatches");
        SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
        if (placement != null && placement.hasVerifier() && !(list = (verifier = placement.getSchematicVerifier()).getSelectedMismatchPositionsForRender()).isEmpty()) {
            class_1297 entity = EntityUtils.getCameraEntity();
            List<class_2338> posList = verifier.getSelectedMismatchBlockPositionsForRender();
            class_3965 trace = RayTraceUtils.traceToPositions(posList, entity, 128.0);
            class_2338 posLook = trace != null && trace.method_17783() == class_239.class_240.field_1332 ? trace.method_17777() : null;
            this.renderSchematicMismatches(list, posLook, posMatrix, profiler);
        }
        profiler.method_15407();
    }

    private void renderSchematicMismatches(List<SchematicVerifier.MismatchRenderPos> posList, @Nullable class_2338 lookPos, Matrix4f matrix4f, class_3695 profiler) {
        class_9801 meshData;
        profiler.method_15396("batched_lines");
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.lineWidth((float)2.0f);
        class_289 tessellator = class_289.method_1348();
        class_287 buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
        RenderUtils.startDrawingLines();
        SchematicVerifier.MismatchRenderPos lookedEntry = null;
        SchematicVerifier.MismatchRenderPos prevEntry = null;
        boolean connections = Configs.Visuals.RENDER_ERROR_MARKER_CONNECTIONS.getBooleanValue();
        for (SchematicVerifier.MismatchRenderPos mismatchRenderPos : posList) {
            Color4f color = mismatchRenderPos.type.getColor();
            if (!mismatchRenderPos.pos.equals((Object)lookPos)) {
                RenderUtils.drawBlockBoundingBoxOutlinesBatchedLines(mismatchRenderPos.pos, color, 0.002, buffer, this.mc);
            } else {
                lookedEntry = mismatchRenderPos;
            }
            if (connections && prevEntry != null) {
                RenderUtils.drawConnectingLineBatchedLines(prevEntry.pos, mismatchRenderPos.pos, false, color, buffer, this.mc);
            }
            prevEntry = mismatchRenderPos;
        }
        if (lookedEntry != null) {
            if (connections && prevEntry != null) {
                RenderUtils.drawConnectingLineBatchedLines(prevEntry.pos, lookedEntry.pos, false, lookedEntry.type.getColor(), buffer, this.mc);
            }
            try {
                meshData = buffer.method_60800();
                class_286.method_43433((class_9801)meshData);
                meshData.close();
            }
            catch (Exception e) {
                Litematica.LOGGER.error("renderSchematicMismatches: Failed to draw Schematic Mismatches (Step 1) (Error: {})", (Object)e.getLocalizedMessage());
            }
            profiler.method_15405("outlines");
            buffer = tessellator.method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            RenderUtils.startDrawingLines();
            RenderSystem.lineWidth((float)6.0f);
            RenderUtils.drawBlockBoundingBoxOutlinesBatchedLines(lookPos, lookedEntry.type.getColor(), 0.002, buffer, this.mc);
        }
        try {
            meshData = buffer.method_60800();
            class_286.method_43433((class_9801)meshData);
            meshData.close();
        }
        catch (Exception e) {
            Litematica.LOGGER.error("renderSchematicMismatches: Failed to draw Schematic Mismatches (Step 2) (Error: {})", (Object)e.getLocalizedMessage());
        }
        profiler.method_15405("sides");
        if (Configs.Visuals.RENDER_ERROR_MARKER_SIDES.getBooleanValue()) {
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            buffer = tessellator.method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            float alpha = (float)Configs.InfoOverlays.VERIFIER_ERROR_HILIGHT_ALPHA.getDoubleValue();
            for (SchematicVerifier.MismatchRenderPos entry : posList) {
                Color4f color = entry.type.getColor();
                color = new Color4f(color.r, color.g, color.b, alpha);
                RenderUtils.renderAreaSidesBatched(entry.pos, entry.pos, color, 0.002, buffer, this.mc);
            }
            try {
                meshData = buffer.method_60800();
                class_286.method_43433((class_9801)meshData);
                meshData.close();
            }
            catch (Exception exception) {
                Litematica.LOGGER.error("renderSchematicMismatches: Failed to draw Schematic Mismatches (Step 3) (Error: {})", (Object)exception.getLocalizedMessage());
            }
            RenderSystem.disableBlend();
        }
        RenderSystem.enableCull();
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableDepthTest();
        profiler.method_15407();
    }

    public void renderHoverInfo(class_310 mc, class_332 drawContext, class_3695 profiler) {
        profiler.method_15396("render_hover_info");
        if (mc.field_1687 != null && mc.field_1724 != null) {
            boolean infoOverlayKeyActive = Hotkeys.RENDER_INFO_OVERLAY.getKeybind().isKeybindHeld();
            boolean verifierOverlayRendered = false;
            profiler.method_15405("render_verifier_overlay");
            if (infoOverlayKeyActive && Configs.InfoOverlays.VERIFIER_OVERLAY_ENABLED.getBooleanValue()) {
                verifierOverlayRendered = this.renderVerifierOverlay(mc, drawContext);
            }
            boolean renderBlockInfoLines = Configs.InfoOverlays.BLOCK_INFO_LINES_ENABLED.getBooleanValue();
            boolean renderBlockInfoOverlay = !verifierOverlayRendered && infoOverlayKeyActive && Configs.InfoOverlays.BLOCK_INFO_OVERLAY_ENABLED.getBooleanValue();
            RayTraceUtils.RayTraceWrapper traceWrapper = null;
            profiler.method_15405("generic_trace");
            if (renderBlockInfoLines || renderBlockInfoOverlay) {
                class_1297 entity = EntityUtils.getCameraEntity();
                boolean targetFluids = Configs.InfoOverlays.INFO_OVERLAYS_TARGET_FLUIDS.getBooleanValue();
                traceWrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, entity, 10.0, true, targetFluids, false);
            }
            if (traceWrapper != null && (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.VANILLA_BLOCK || traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK)) {
                profiler.method_15405("render_block_lines");
                if (renderBlockInfoLines) {
                    this.renderBlockInfoLines(traceWrapper, mc, drawContext);
                }
                profiler.method_15405("render_block_overlay");
                if (renderBlockInfoOverlay) {
                    this.renderBlockInfoOverlay(traceWrapper, mc, drawContext);
                }
            }
        }
        profiler.method_15407();
    }

    private void renderBlockInfoLines(RayTraceUtils.RayTraceWrapper traceWrapper, class_310 mc, class_332 drawContext) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.infoUpdateTime >= 50L) {
            this.updateBlockInfoLines(traceWrapper, mc);
            this.infoUpdateTime = currentTime;
        }
        int x = Configs.InfoOverlays.BLOCK_INFO_LINES_OFFSET_X.getIntegerValue();
        int y = Configs.InfoOverlays.BLOCK_INFO_LINES_OFFSET_Y.getIntegerValue();
        double fontScale = Configs.InfoOverlays.BLOCK_INFO_LINES_FONT_SCALE.getDoubleValue();
        int textColor = -1;
        int bgColor = -1605349296;
        HudAlignment alignment = (HudAlignment)Configs.InfoOverlays.BLOCK_INFO_LINES_ALIGNMENT.getOptionListValue();
        boolean useBackground = true;
        boolean useShadow = false;
        fi.dy.masa.malilib.render.RenderUtils.renderText((int)x, (int)y, (double)fontScale, (int)textColor, (int)bgColor, (HudAlignment)alignment, (boolean)useBackground, (boolean)useShadow, this.blockInfoLines, (class_332)drawContext);
    }

    private boolean renderVerifierOverlay(class_310 mc, class_332 drawContext) {
        SchematicPlacement placement = DataManager.getSchematicPlacementManager().getSelectedSchematicPlacement();
        if (placement != null && placement.hasVerifier()) {
            class_1297 entity = EntityUtils.getCameraEntity();
            SchematicVerifier verifier = placement.getSchematicVerifier();
            List<class_2338> posList = verifier.getSelectedMismatchBlockPositionsForRender();
            class_3965 trace = RayTraceUtils.traceToPositions(posList, entity, 128.0);
            if (trace != null && trace.method_17783() == class_239.class_240.field_1332) {
                SchematicVerifier.BlockMismatch mismatch;
                WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
                class_2338 pos = trace.method_17777();
                if (!DataManager.getInstance().hasIntegratedServer()) {
                    EntitiesDataStorage.getInstance().requestBlockEntity((class_1937)mc.field_1687, pos);
                }
                if ((mismatch = verifier.getMismatchForPosition(pos)) != null && worldSchematic != null) {
                    WidgetSchematicVerificationResult.BlockMismatchInfo info = new WidgetSchematicVerificationResult.BlockMismatchInfo(mismatch.stateExpected, mismatch.stateFound);
                    BlockInfoAlignment align = (BlockInfoAlignment)Configs.InfoOverlays.BLOCK_INFO_OVERLAY_ALIGNMENT.getOptionListValue();
                    int offY = Configs.InfoOverlays.BLOCK_INFO_OVERLAY_OFFSET_Y.getIntegerValue();
                    int invHeight = RenderUtils.renderInventoryOverlays(align, offY, worldSchematic, (class_1937)mc.field_1687, pos, mc, drawContext);
                    this.getOverlayPosition(align, info.getTotalWidth(), info.getTotalHeight(), offY, invHeight, mc);
                    info.render(this.blockInfoX, this.blockInfoY, mc, drawContext);
                    return true;
                }
            }
        }
        return false;
    }

    private void renderBlockInfoOverlay(RayTraceUtils.RayTraceWrapper traceWrapper, class_310 mc, class_332 drawContext) {
        class_2680 air = class_2246.field_10124.method_9564();
        class_2680 voidAir = class_2246.field_10243.method_9564();
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        class_1937 worldClient = WorldUtils.getBestWorld((class_310)mc);
        class_2338 pos = traceWrapper.getBlockHitResult().method_17777();
        if (mc.field_1687 == null || worldClient == null || worldSchematic == null) {
            return;
        }
        class_2680 stateClient = mc.field_1687.method_8320(pos);
        class_2680 stateSchematic = worldSchematic.method_8320(pos);
        boolean hasInvClient = InventoryUtils.getTargetInventory(worldClient, pos) != null;
        boolean hasInvSchematic = InventoryUtils.getTargetInventory(worldSchematic, pos) != null;
        int invHeight = 0;
        int offY = Configs.InfoOverlays.BLOCK_INFO_OVERLAY_OFFSET_Y.getIntegerValue();
        BlockInfoAlignment align = (BlockInfoAlignment)Configs.InfoOverlays.BLOCK_INFO_OVERLAY_ALIGNMENT.getOptionListValue();
        ItemUtils.setItemForBlock(worldSchematic, pos, stateSchematic);
        ItemUtils.setItemForBlock((class_1937)mc.field_1687, pos, stateClient);
        if (hasInvClient && hasInvSchematic) {
            invHeight = RenderUtils.renderInventoryOverlays(align, offY, worldSchematic, worldClient, pos, mc, drawContext);
        } else if (hasInvClient) {
            invHeight = RenderUtils.renderInventoryOverlay(align, LeftRight.RIGHT, offY, worldClient, pos, mc, drawContext);
        } else if (hasInvSchematic) {
            invHeight = RenderUtils.renderInventoryOverlay(align, LeftRight.LEFT, offY, worldSchematic, pos, mc, drawContext);
        }
        if (stateSchematic != stateClient && stateClient != air && stateSchematic != air && stateSchematic != voidAir) {
            WidgetSchematicVerificationResult.BlockMismatchInfo info = new WidgetSchematicVerificationResult.BlockMismatchInfo(stateSchematic, stateClient);
            this.getOverlayPosition(align, info.getTotalWidth(), info.getTotalHeight(), offY, invHeight, mc);
            info.toggleUseBackgroundMask(true);
            info.render(this.blockInfoX, this.blockInfoY, mc, drawContext);
        } else if (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.VANILLA_BLOCK) {
            BlockInfo info = new BlockInfo(stateClient, "litematica.gui.label.block_info.state_client");
            this.getOverlayPosition(align, info.getTotalWidth(), info.getTotalHeight(), offY, invHeight, mc);
            info.toggleUseBackgroundMask(true);
            info.render(this.blockInfoX, this.blockInfoY, mc, drawContext);
        } else if (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            BlockInfo info = new BlockInfo(stateSchematic, "litematica.gui.label.block_info.state_schematic");
            this.getOverlayPosition(align, info.getTotalWidth(), info.getTotalHeight(), offY, invHeight, mc);
            info.toggleUseBackgroundMask(true);
            info.render(this.blockInfoX, this.blockInfoY, mc, drawContext);
        }
    }

    public void requestBlockEntityAt(class_1937 world, class_2338 pos) {
        if (!(world instanceof class_3218)) {
            class_2745 type;
            EntitiesDataStorage.getInstance().requestBlockEntity(world, pos);
            class_2680 state = world.method_8320(pos);
            if (state.method_26204() instanceof class_2281 && (type = (class_2745)state.method_11654((class_2769)class_2281.field_10770)) != class_2745.field_12569) {
                class_2338 posAdj = pos.method_10093(class_2281.method_9758((class_2680)state));
                EntitiesDataStorage.getInstance().requestBlockEntity(world, posAdj);
            }
        }
    }

    public static int calculateCompatYShift() {
        if (JadeCompat.hasJade()) {
            return JadeCompat.getJadeShift();
        }
        return 0;
    }

    protected void getOverlayPosition(BlockInfoAlignment align, int width, int height, int offY, int invHeight, class_310 mc) {
        switch (align) {
            case CENTER: {
                this.blockInfoX = GuiUtils.getScaledWindowWidth() / 2 - width / 2;
                this.blockInfoY = GuiUtils.getScaledWindowHeight() / 2 + offY;
                break;
            }
            case TOP_CENTER: {
                this.blockInfoX = GuiUtils.getScaledWindowWidth() / 2 - width / 2;
                this.blockInfoY = invHeight + offY + (invHeight > 0 ? offY : 0);
                this.blockInfoY += invHeight > 0 ? 0 : OverlayRenderer.calculateCompatYShift();
            }
        }
    }

    private void updateBlockInfoLines(RayTraceUtils.RayTraceWrapper traceWrapper, class_310 mc) {
        this.blockInfoLines.clear();
        class_2338 pos = traceWrapper.getBlockHitResult().method_17777();
        class_2680 stateClient = mc.field_1687.method_8320(pos);
        class_2680 voidAir = class_2246.field_10243.method_9564();
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        class_2680 stateSchematic = worldSchematic.method_8320(pos);
        String ul = GuiBase.TXT_UNDERLINE;
        if (stateSchematic != stateClient && !stateClient.method_26215() && !stateSchematic.method_26215() && stateSchematic != voidAir) {
            this.blockInfoLines.add(ul + "Schematic:");
            this.addBlockInfoLines(stateSchematic);
            this.blockInfoLines.add("");
            this.blockInfoLines.add(ul + "Client:");
            this.addBlockInfoLines(stateClient);
        } else if (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            this.blockInfoLines.add(ul + "Schematic:");
            this.addBlockInfoLines(stateSchematic);
        }
    }

    private void addBlockInfoLines(class_2680 state) {
        this.blockInfoLines.add(String.valueOf(class_7923.field_41175.method_10221((Object)state.method_26204())));
        this.blockInfoLines.addAll(BlockUtils.getFormattedBlockStateProperties((class_2680)state));
    }

    public void renderSchematicRebuildTargetingOverlay(Matrix4f posMatrix, Matrix4f projMatrix, class_3695 profiler) {
        profiler.method_15396("rebuild_trace");
        RayTraceUtils.RayTraceWrapper traceWrapper = null;
        Color4f color = null;
        boolean direction = false;
        class_1297 entity = EntityUtils.getCameraEntity();
        if (Hotkeys.SCHEMATIC_EDIT_BREAK_ALL.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_BREAK_OVERLAY_COLOR.getColor();
        } else if (Hotkeys.SCHEMATIC_EDIT_BREAK_ALL_EXCEPT.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_BREAK_EXCEPT_OVERLAY_COLOR.getColor();
        } else if (Hotkeys.SCHEMATIC_EDIT_BREAK_DIRECTION.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_BREAK_OVERLAY_COLOR.getColor();
            direction = true;
        } else if (Hotkeys.SCHEMATIC_EDIT_REPLACE_ALL.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_REPLACE_OVERLAY_COLOR.getColor();
        } else if (Hotkeys.SCHEMATIC_EDIT_REPLACE_BLOCK.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_REPLACE_OVERLAY_COLOR.getColor();
        } else if (Hotkeys.SCHEMATIC_EDIT_REPLACE_DIRECTION.getKeybind().isKeybindHeld()) {
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)this.mc.field_1687, entity, 20.0);
            color = Configs.Colors.REBUILD_REPLACE_OVERLAY_COLOR.getColor();
            direction = true;
        }
        profiler.method_15405("render_target_overlay");
        if (traceWrapper != null && traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = traceWrapper.getBlockHitResult();
            class_2338 pos = trace.method_17777();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.disableCull();
            fi.dy.masa.malilib.render.RenderUtils.setupBlend();
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset((float)-0.8f, (float)-1.8f);
            if (direction) {
                fi.dy.masa.malilib.render.RenderUtils.renderBlockTargetingOverlay((class_1297)entity, (class_2338)pos, (class_2350)trace.method_17780(), (class_243)trace.method_17784(), (Color4f)color, (Matrix4f)posMatrix, (class_310)this.mc);
            } else {
                fi.dy.masa.malilib.render.RenderUtils.renderBlockTargetingOverlaySimple((class_1297)entity, (class_2338)pos, (class_2350)trace.method_17780(), (Color4f)color, (Matrix4f)posMatrix, (class_310)this.mc);
            }
            RenderSystem.disablePolygonOffset();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
            RenderSystem.depthMask((boolean)true);
        }
        profiler.method_15407();
    }

    public void renderPreviewFrame(class_310 mc, class_332 drawContext, class_3695 profiler) {
        profiler.method_15396("render_preview_frame");
        int width = GuiUtils.getScaledWindowWidth();
        int height = GuiUtils.getScaledWindowHeight();
        int x = width >= height ? (width - height) / 2 : 0;
        int y = height >= width ? (height - width) / 2 : 0;
        int longerSide = Math.min(width, height);
        fi.dy.masa.malilib.render.RenderUtils.drawOutline((int)x, (int)y, (int)longerSide, (int)longerSide, (int)2, (int)-1);
        profiler.method_15407();
    }

    private static enum BoxType {
        AREA_SELECTED,
        AREA_UNSELECTED,
        PLACEMENT_SELECTED,
        PLACEMENT_UNSELECTED;

    }
}

