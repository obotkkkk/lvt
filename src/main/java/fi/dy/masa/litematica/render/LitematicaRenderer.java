/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.render.RenderUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1921
 *  net.minecraft.class_310
 *  net.minecraft.class_3695
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4604
 *  net.minecraft.class_5944
 *  org.joml.Matrix4f
 */
package fi.dy.masa.litematica.render;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.compat.iris.IrisCompat;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.render.RenderUtils;
import javax.annotation.Nullable;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1921;
import net.minecraft.class_310;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4604;
import net.minecraft.class_5944;
import org.joml.Matrix4f;

public class LitematicaRenderer {
    private static final LitematicaRenderer INSTANCE = new LitematicaRenderer();
    private class_310 mc;
    private WorldRendererSchematic worldRenderer;
    private class_4604 frustum;
    private int frameCount;
    private long finishTimeNano;
    private boolean renderCollidingSchematicBlocks;
    private boolean renderPiecewiseSchematic;
    private boolean renderPiecewiseBlocks;
    private boolean renderPiecewiseEntities;
    private boolean renderPiecewiseTileEntities;

    public static LitematicaRenderer getInstance() {
        return INSTANCE;
    }

    private LitematicaRenderer() {
    }

    public WorldRendererSchematic getWorldRenderer() {
        if (this.worldRenderer == null) {
            this.mc = class_310.method_1551();
            this.worldRenderer = new WorldRendererSchematic(this.mc);
        }
        return this.worldRenderer;
    }

    public WorldRendererSchematic resetWorldRenderer() {
        if (this.worldRenderer != null) {
            this.worldRenderer.setWorldAndLoadRenderers(null);
            this.worldRenderer = null;
        }
        return this.getWorldRenderer();
    }

    public void loadRenderers(@Nullable class_3695 profiler) {
        this.getWorldRenderer().loadRenderers(profiler);
    }

    public void onSchematicWorldChanged(@Nullable WorldSchematic worldClient) {
        this.getWorldRenderer().setWorldAndLoadRenderers(worldClient);
    }

    private void calculateFinishTime() {
        long fpsTarget = 60L;
        this.finishTimeNano = Configs.Generic.RENDER_THREAD_NO_TIMEOUT.getBooleanValue() ? Long.MAX_VALUE : System.nanoTime() + 1000000000L / fpsTarget / 2L;
    }

    public void renderSchematicOverlay(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        boolean invert = Hotkeys.INVERT_OVERLAY_RENDER_STATE.getKeybind().isKeybindHeld();
        if (Configs.Visuals.ENABLE_SCHEMATIC_OVERLAY.getBooleanValue() != invert) {
            boolean renderThrough = Configs.Visuals.SCHEMATIC_OVERLAY_RENDER_THROUGH.getBooleanValue() || Hotkeys.RENDER_OVERLAY_THROUGH_BLOCKS.getKeybind().isKeybindHeld();
            float lineWidth = (float)(renderThrough ? Configs.Visuals.SCHEMATIC_OVERLAY_OUTLINE_WIDTH_THROUGH.getDoubleValue() : Configs.Visuals.SCHEMATIC_OVERLAY_OUTLINE_WIDTH.getDoubleValue());
            profiler.method_15396("schematic_overlay");
            RenderSystem.disableCull();
            RenderSystem.enablePolygonOffset();
            RenderSystem.polygonOffset((float)-0.4f, (float)-0.8f);
            RenderSystem.lineWidth((float)lineWidth);
            RenderUtils.setupBlend();
            RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (!IrisCompat.isShadowPassActive()) {
                this.getWorldRenderer().renderBlockOverlays(viewMatrix, this.getCamera(), posMatrix, profiler);
            }
            RenderSystem.enableDepthTest();
            RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
            RenderSystem.disablePolygonOffset();
            RenderSystem.enableCull();
            profiler.method_15407();
        }
    }

    public void piecewisePrepare(class_4604 frustum, class_3695 profiler) {
        boolean render = Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && this.mc.method_1560() != null;
        this.renderPiecewiseSchematic = false;
        this.renderPiecewiseBlocks = false;
        this.renderPiecewiseEntities = false;
        this.renderPiecewiseTileEntities = false;
        WorldRendererSchematic worldRenderer = this.getWorldRenderer();
        if (render && frustum != null && worldRenderer.hasWorld()) {
            boolean invert = Hotkeys.INVERT_GHOST_BLOCK_RENDER_STATE.getKeybind().isKeybindHeld();
            this.renderPiecewiseSchematic = Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue() != invert;
            this.renderPiecewiseBlocks = this.renderPiecewiseSchematic && Configs.Visuals.ENABLE_SCHEMATIC_BLOCKS.getBooleanValue();
            this.renderCollidingSchematicBlocks = Configs.Visuals.RENDER_COLLIDING_SCHEMATIC_BLOCKS.getBooleanValue();
            this.renderPiecewiseEntities = this.renderPiecewiseSchematic && Configs.Visuals.RENDER_SCHEMATIC_ENTITIES.getBooleanValue();
            boolean bl = this.renderPiecewiseTileEntities = this.renderPiecewiseSchematic && Configs.Visuals.RENDER_SCHEMATIC_TILE_ENTITIES.getBooleanValue();
            if (this.renderPiecewiseSchematic) {
                profiler.method_15396("litematica_culling");
                this.calculateFinishTime();
                profiler.method_15405("litematica_terrain_setup");
                worldRenderer.setupTerrain(this.getCamera(), frustum, this.frameCount++, this.mc.field_1724.method_7325(), profiler);
                profiler.method_15407();
                this.frustum = frustum;
            }
        }
    }

    public void piecewiseUpdate(class_4184 camera, class_3695 profiler) {
        boolean render = Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && camera != null;
        WorldRendererSchematic worldRenderer = this.getWorldRenderer();
        if (render && this.frustum != null && worldRenderer.hasWorld() && this.renderPiecewiseSchematic) {
            profiler.method_15396("litematica_update_chunks");
            worldRenderer.updateChunks(this.finishTimeNano, profiler);
            profiler.method_15407();
        }
    }

    public void piecewiseRenderSolid(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseBlocks) {
            profiler.method_15396("litematica_solid");
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-0.3f, (float)-0.6f);
            }
            class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53881);
            this.getWorldRenderer().renderBlockLayer(class_1921.method_23577(), viewMatrix, this.getCamera(), posMatrix, profiler, shader);
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
            }
            profiler.method_15407();
        }
    }

    public void piecewiseRenderCutoutMipped(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseBlocks) {
            profiler.method_15396("litematica_cutout_mipped");
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-0.3f, (float)-0.6f);
            }
            class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53882);
            this.getWorldRenderer().renderBlockLayer(class_1921.method_23579(), viewMatrix, this.getCamera(), posMatrix, profiler, shader);
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
            }
            profiler.method_15407();
        }
    }

    public void piecewiseRenderCutout(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseBlocks) {
            profiler.method_15396("litematica_cutout");
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-0.3f, (float)-0.6f);
            }
            class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53883);
            this.getWorldRenderer().renderBlockLayer(class_1921.method_23581(), viewMatrix, this.getCamera(), posMatrix, profiler, shader);
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
            }
            profiler.method_15407();
        }
    }

    public void piecewiseRenderTranslucent(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseBlocks) {
            profiler.method_15396("litematica_translucent");
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-0.3f, (float)-0.6f);
            }
            class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53884);
            this.getWorldRenderer().renderBlockLayer(class_1921.method_23583(), viewMatrix, this.getCamera(), posMatrix, profiler, shader);
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
            }
            profiler.method_15407();
        }
    }

    public void piecewiseRenderTripwire(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseBlocks) {
            profiler.method_15396("litematica_tripwire");
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.enablePolygonOffset();
                RenderSystem.polygonOffset((float)-0.3f, (float)-0.6f);
            }
            class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53884);
            this.getWorldRenderer().renderBlockLayer(class_1921.method_29997(), viewMatrix, this.getCamera(), posMatrix, profiler, shader);
            if (this.renderCollidingSchematicBlocks) {
                RenderSystem.polygonOffset((float)0.0f, (float)0.0f);
                RenderSystem.disablePolygonOffset();
            }
            profiler.method_15407();
        }
    }

    public void piecewiseRenderOverlay(Matrix4f viewMatrix, Matrix4f posMatrix, class_3695 profiler) {
        if (this.renderPiecewiseSchematic) {
            profiler.method_15396("litematica_schematic_overlay");
            this.renderSchematicOverlay(viewMatrix, posMatrix, profiler);
            profiler.method_15407();
        }
        this.cleanup();
    }

    public void piecewiseRenderEntities(class_4587 matrices, class_4597.class_4598 immediate, float partialTicks, class_3695 profiler) {
        if (this.renderPiecewiseEntities) {
            profiler.method_15396("litematica_entities");
            this.getWorldRenderer().renderEntities(this.getCamera(), this.frustum, matrices, immediate, partialTicks, profiler);
            profiler.method_15407();
        }
    }

    public void piecewiseRenderBlockEntities(class_4587 matrices, class_4597.class_4598 immediate, class_4597.class_4598 immediate2, float partialTicks, class_3695 profiler) {
        if (this.renderPiecewiseTileEntities) {
            profiler.method_15396("litematica_block_entities");
            this.getWorldRenderer().renderBlockEntities(this.getCamera(), this.frustum, matrices, immediate, immediate2, partialTicks, profiler);
            profiler.method_15407();
        }
    }

    private class_4184 getCamera() {
        return this.mc.field_1773.method_19418();
    }

    private void cleanup() {
        this.renderPiecewiseSchematic = false;
        this.renderPiecewiseBlocks = false;
        this.renderPiecewiseEntities = false;
        this.renderPiecewiseTileEntities = false;
    }
}

