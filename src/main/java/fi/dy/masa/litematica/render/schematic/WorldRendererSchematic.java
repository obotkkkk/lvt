/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.LayerRange
 *  javax.annotation.Nullable
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_10209
 *  net.minecraft.class_1087
 *  net.minecraft.class_128
 *  net.minecraft.class_129
 *  net.minecraft.class_1297
 *  net.minecraft.class_1431
 *  net.minecraft.class_1462
 *  net.minecraft.class_1474
 *  net.minecraft.class_148
 *  net.minecraft.class_1496
 *  net.minecraft.class_1920
 *  net.minecraft.class_1921
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2464
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_291
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3610
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_3695
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4604
 *  net.minecraft.class_5539
 *  net.minecraft.class_5944
 *  net.minecraft.class_7110
 *  net.minecraft.class_776
 *  net.minecraft.class_824
 *  net.minecraft.class_898
 *  net.minecraft.class_9866
 *  net.minecraft.class_9958
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fStack
 */
package fi.dy.masa.litematica.render.schematic;

import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.mixin.entity.IMixinEntity;
import fi.dy.masa.litematica.render.schematic.BlockModelRendererSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDispatcherLitematica;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDispatcherSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderLayers;
import fi.dy.masa.litematica.render.schematic.ChunkRendererSchematicVbo;
import fi.dy.masa.litematica.render.schematic.IChunkRendererFactory;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import fi.dy.masa.litematica.render.schematic.blocks.FallbackBlocks;
import fi.dy.masa.litematica.util.IEntityInvoker;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.LayerRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_10209;
import net.minecraft.class_1087;
import net.minecraft.class_128;
import net.minecraft.class_129;
import net.minecraft.class_1297;
import net.minecraft.class_1431;
import net.minecraft.class_1462;
import net.minecraft.class_1474;
import net.minecraft.class_148;
import net.minecraft.class_1496;
import net.minecraft.class_1920;
import net.minecraft.class_1921;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2464;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_291;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_4604;
import net.minecraft.class_5539;
import net.minecraft.class_5944;
import net.minecraft.class_7110;
import net.minecraft.class_776;
import net.minecraft.class_824;
import net.minecraft.class_898;
import net.minecraft.class_9866;
import net.minecraft.class_9958;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public class WorldRendererSchematic {
    private final class_310 mc;
    private final class_898 entityRenderDispatcher;
    private final class_824 blockEntityRenderDispatcher;
    private final class_776 blockRenderManager;
    private final BlockModelRendererSchematic blockModelRenderer;
    private final Set<class_2586> blockEntities = new HashSet<class_2586>();
    private final List<ChunkRendererSchematicVbo> renderInfos = new ArrayList<ChunkRendererSchematicVbo>(1024);
    private Set<ChunkRendererSchematicVbo> chunksToUpdate = new LinkedHashSet<ChunkRendererSchematicVbo>();
    private WorldSchematic world;
    private ChunkRenderDispatcherSchematic chunkRendererDispatcher;
    private class_3695 profiler;
    private double lastCameraChunkUpdateX = Double.MIN_VALUE;
    private double lastCameraChunkUpdateY = Double.MIN_VALUE;
    private double lastCameraChunkUpdateZ = Double.MIN_VALUE;
    private double lastCameraX = Double.MIN_VALUE;
    private double lastCameraY = Double.MIN_VALUE;
    private double lastCameraZ = Double.MIN_VALUE;
    private float lastCameraPitch = Float.MIN_VALUE;
    private float lastCameraYaw = Float.MIN_VALUE;
    private ChunkRenderDispatcherLitematica renderDispatcher;
    private final IChunkRendererFactory renderChunkFactory;
    private int renderDistanceChunks = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    private double lastTranslucentSortX;
    private double lastTranslucentSortY;
    private double lastTranslucentSortZ;
    private boolean displayListEntitiesDirty = true;

    public WorldRendererSchematic(class_310 mc) {
        this.mc = mc;
        this.renderChunkFactory = ChunkRendererSchematicVbo::new;
        this.blockRenderManager = class_310.method_1551().method_1541();
        this.entityRenderDispatcher = mc.method_1561();
        this.blockEntityRenderDispatcher = mc.method_31975();
        this.blockModelRenderer = new BlockModelRendererSchematic(mc.method_1505());
        this.blockModelRenderer.setBakedManager(mc.method_1554());
        this.profiler = null;
    }

    public void markNeedsUpdate() {
        this.displayListEntitiesDirty = true;
    }

    public boolean hasWorld() {
        return this.world != null;
    }

    public String getDebugInfoRenders() {
        int rcTotal = this.chunkRendererDispatcher != null ? this.chunkRendererDispatcher.getRendererCount() : 0;
        int rcRendered = this.chunkRendererDispatcher != null ? this.getRenderedChunks() : 0;
        return String.format("C: %d/%d %sD: %d, L: %d, %s", rcRendered, rcTotal, this.mc.field_1730 ? "(s) " : "", this.renderDistanceChunks, 0, this.renderDispatcher == null ? "null" : this.renderDispatcher.getDebugInfo());
    }

    public String getDebugInfoEntities() {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden;
    }

    protected int getRenderedChunks() {
        int count = 0;
        for (ChunkRendererSchematicVbo chunkRenderer : this.renderInfos) {
            ChunkRenderDataSchematic data = chunkRenderer.chunkRenderData;
            if (data == ChunkRenderDataSchematic.EMPTY || data.isEmpty()) continue;
            ++count;
        }
        return count;
    }

    protected class_3695 getProfiler() {
        if (this.profiler == null) {
            this.profiler = class_10209.method_64146();
            this.profiler.method_16065();
        }
        return this.profiler;
    }

    protected class_898 getEntityRenderer() {
        return this.entityRenderDispatcher;
    }

    protected class_824 getBlockEntityRenderer() {
        return this.blockEntityRenderDispatcher;
    }

    private <T extends Comparable<T>> class_2680 getFallbackState(class_2680 origState) {
        Collection props = origState.method_28501();
        class_2248 block = origState.method_26204();
        if (FallbackBlocks.BLOCK_TO_ID.containsKey(block)) {
            class_2960 id = FallbackBlocks.BLOCK_TO_ID.get(block);
            Litematica.LOGGER.warn("getFallbackState: Invalid Block State/Block Model for block [{}]; but we found a matching Litematica fallback block state that you can use.  Perhaps you have the Fusion mod installed?", (Object)origState.method_26204().method_9518().getString());
            class_2680 newState = (class_2680)FallbackBlocks.ID_TO_STATE_MANAGER.get(id).method_11664();
            for (class_2769 entry : props) {
                class_2769 p = entry;
                if (!newState.method_28498(p)) continue;
                Comparable value = origState.method_11654(p);
                if (newState.method_11654(p).equals(value)) continue;
                newState = (class_2680)newState.method_11657(p, value);
            }
            Litematica.debugLog("Fallback Block State -- OLD: [{}] --> NEW: [{}]", origState.toString(), newState.toString());
            return newState;
        }
        return origState;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setWorldAndLoadRenderers(@Nullable WorldSchematic worldSchematic) {
        this.lastCameraChunkUpdateX = Double.MIN_VALUE;
        this.lastCameraChunkUpdateY = Double.MIN_VALUE;
        this.lastCameraChunkUpdateZ = Double.MIN_VALUE;
        this.world = worldSchematic;
        if (worldSchematic != null) {
            this.loadRenderers(this.profiler);
        } else {
            this.chunksToUpdate.forEach(ChunkRendererSchematicVbo::deleteGlResources);
            this.chunksToUpdate.clear();
            this.renderInfos.forEach(ChunkRendererSchematicVbo::deleteGlResources);
            this.renderInfos.clear();
            if (this.chunkRendererDispatcher != null) {
                this.chunkRendererDispatcher.delete();
                this.chunkRendererDispatcher = null;
            }
            if (this.renderDispatcher != null) {
                this.renderDispatcher.stopWorkerThreads();
            }
            this.renderDispatcher = null;
            this.profiler = null;
            Set<class_2586> set = this.blockEntities;
            synchronized (set) {
                this.blockEntities.clear();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadRenderers(@Nullable class_3695 profiler) {
        if (this.hasWorld()) {
            if (profiler == null) {
                profiler = class_10209.method_64146();
            }
            this.profiler = profiler;
            profiler.method_15396("load_renderers");
            if (this.renderDispatcher == null) {
                this.renderDispatcher = new ChunkRenderDispatcherLitematica(profiler);
            }
            this.displayListEntitiesDirty = true;
            this.renderDistanceChunks = (Integer)this.mc.field_1690.method_42503().method_41753() + 2;
            if (this.chunkRendererDispatcher != null) {
                this.chunkRendererDispatcher.delete();
            }
            this.stopChunkUpdates(profiler);
            Set<class_2586> set = this.blockEntities;
            synchronized (set) {
                this.blockEntities.clear();
            }
            this.chunkRendererDispatcher = new ChunkRenderDispatcherSchematic(this.world, this.renderDistanceChunks, this, this.renderChunkFactory);
            this.renderEntitiesStartupCounter = 2;
            profiler.method_15407();
        }
    }

    protected void stopChunkUpdates(class_3695 profiler) {
        if (!this.chunksToUpdate.isEmpty()) {
            this.chunksToUpdate.forEach(ChunkRendererSchematicVbo::deleteGlResources);
        }
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates(profiler);
        this.profiler = null;
    }

    public void setupTerrain(class_4184 camera, class_4604 frustum, int frameCount, boolean playerSpectator, class_3695 profiler) {
        class_1297 entity;
        this.profiler = profiler;
        profiler.method_15396("setup_terrain");
        if (this.chunkRendererDispatcher == null || (Integer)this.mc.field_1690.method_42503().method_41753() + 2 != this.renderDistanceChunks) {
            this.loadRenderers(profiler);
        }
        if ((entity = EntityUtils.getCameraEntity()) == null) {
            entity = this.mc.field_1724;
        }
        profiler.method_15405("camera");
        double entityX = entity.method_23317();
        double entityY = entity.method_23318();
        double entityZ = entity.method_23321();
        double diffX = entityX - this.lastCameraChunkUpdateX;
        double diffY = entityY - this.lastCameraChunkUpdateY;
        double diffZ = entityZ - this.lastCameraChunkUpdateZ;
        if (diffX * diffX + diffY * diffY + diffZ * diffZ > 256.0) {
            this.lastCameraChunkUpdateX = entityX;
            this.lastCameraChunkUpdateY = entityY;
            this.lastCameraChunkUpdateZ = entityZ;
            this.chunkRendererDispatcher.removeOutOfRangeRenderers();
        }
        profiler.method_15405("renderlist_camera");
        class_243 cameraPos = camera.method_19326();
        double cameraX = cameraPos.field_1352;
        double cameraY = cameraPos.field_1351;
        double cameraZ = cameraPos.field_1350;
        this.renderDispatcher.setCameraPosition(cameraPos);
        profiler.method_15405("culling");
        class_2338 viewPos = class_2338.method_49637((double)cameraX, (double)(cameraY + (double)entity.method_5751()), (double)cameraZ);
        int centerChunkX = viewPos.method_10263() >> 4;
        int centerChunkZ = viewPos.method_10260() >> 4;
        int renderDistance = (Integer)this.mc.field_1690.method_42503().method_41753() + 2;
        class_1923 viewChunk = new class_1923(viewPos);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || entityX != this.lastCameraX || entityY != this.lastCameraY || entityZ != this.lastCameraZ || entity.method_36455() != this.lastCameraPitch || entity.method_36454() != this.lastCameraYaw;
        this.lastCameraX = cameraX;
        this.lastCameraY = cameraY;
        this.lastCameraZ = cameraZ;
        this.lastCameraPitch = camera.method_19329();
        this.lastCameraYaw = camera.method_19330();
        profiler.method_15405("update");
        if (this.displayListEntitiesDirty) {
            this.displayListEntitiesDirty = false;
            this.renderInfos.clear();
            profiler.method_15396("update_sort");
            List<class_1923> positions = DataManager.getSchematicPlacementManager().getAndUpdateVisibleChunks(viewChunk);
            profiler.method_15405("update_iteration");
            for (class_1923 chunkPos : positions) {
                ChunkRendererSchematicVbo chunkRenderer;
                int cx = chunkPos.field_9181;
                int cz = chunkPos.field_9180;
                if (Math.abs(cx - centerChunkX) > renderDistance || Math.abs(cz - centerChunkZ) > renderDistance || !this.world.getChunkProvider().method_12123(cx, cz) || (chunkRenderer = this.chunkRendererDispatcher.getChunkRenderer(cx, cz)) == null || !frustum.method_23093(chunkRenderer.getBoundingBox())) continue;
                if (chunkRenderer.needsUpdate() && chunkPos.equals((Object)viewChunk)) {
                    chunkRenderer.setNeedsUpdate(true);
                }
                this.renderInfos.add(chunkRenderer);
            }
            profiler.method_15407();
        }
        profiler.method_15405("rebuild_near");
        Set<ChunkRendererSchematicVbo> set = this.chunksToUpdate;
        this.chunksToUpdate = new LinkedHashSet<ChunkRendererSchematicVbo>();
        for (ChunkRendererSchematicVbo chunkRendererTmp : this.renderInfos) {
            boolean isNear;
            if (!chunkRendererTmp.needsUpdate() && !set.contains(chunkRendererTmp)) continue;
            this.displayListEntitiesDirty = true;
            class_2338 pos = chunkRendererTmp.getOrigin().method_10069(8, 8, 8);
            boolean bl = isNear = pos.method_10262((class_2382)viewPos) < 1024.0;
            if (!chunkRendererTmp.needsImmediateUpdate() && !isNear) {
                this.chunksToUpdate.add(chunkRendererTmp);
                continue;
            }
            profiler.method_15396("update_now");
            this.profiler = profiler;
            this.renderDispatcher.updateChunkNow(chunkRendererTmp, profiler);
            chunkRendererTmp.clearNeedsUpdate();
            profiler.method_15407();
        }
        this.chunksToUpdate.addAll(set);
        profiler.method_15407();
    }

    public void updateChunks(long finishTimeNano, class_3695 profiler) {
        this.profiler = profiler;
        profiler.method_15396("run_chunk_uploads");
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano, profiler);
        if (this.profiler == null) {
            this.profiler = profiler;
        }
        profiler.method_15405("check_update");
        if (!this.chunksToUpdate.isEmpty()) {
            ChunkRendererSchematicVbo renderChunk;
            boolean flag;
            Iterator<ChunkRendererSchematicVbo> iterator = this.chunksToUpdate.iterator();
            int index = 0;
            while (iterator.hasNext() && (flag = (renderChunk = iterator.next()).needsImmediateUpdate() ? this.renderDispatcher.updateChunkNow(renderChunk, profiler) : this.renderDispatcher.updateChunkLater(renderChunk, profiler))) {
                renderChunk.clearNeedsUpdate();
                iterator.remove();
                long i = finishTimeNano - System.nanoTime();
                if (i < 0L) break;
                ++index;
            }
        }
        profiler.method_15407();
    }

    public int renderBlockLayer(class_1921 renderLayer, Matrix4f matrices, class_4184 camera, Matrix4f projMatrix, class_3695 profiler, class_5944 shader) {
        this.profiler = profiler;
        RenderSystem.assertOnRenderThread();
        if (renderLayer.toString().contains("RenderType")) {
            profiler.method_15396("layer_multi_phase");
        } else {
            profiler.method_15396("layer_" + ChunkRenderLayers.getFriendlyName(renderLayer));
        }
        boolean isTranslucent = renderLayer == class_1921.method_23583();
        renderLayer.method_23516();
        class_243 cameraPos = camera.method_19326();
        double x = cameraPos.field_1352;
        double y = cameraPos.field_1351;
        double z = cameraPos.field_1350;
        if (isTranslucent) {
            profiler.method_15396("translucent_sort");
            this.profiler = profiler;
            double diffX = x - this.lastTranslucentSortX;
            double diffY = y - this.lastTranslucentSortY;
            double diffZ = z - this.lastTranslucentSortZ;
            if (diffX * diffX + diffY * diffY + diffZ * diffZ > 1.0) {
                this.lastTranslucentSortX = x;
                this.lastTranslucentSortY = y;
                this.lastTranslucentSortZ = z;
                int h = 0;
                for (ChunkRendererSchematicVbo chunkRenderer : this.renderInfos) {
                    if (!chunkRenderer.getChunkRenderData().isBlockLayerStarted(renderLayer) && (chunkRenderer.getChunkRenderData() == ChunkRenderDataSchematic.EMPTY || !chunkRenderer.hasOverlay()) || h++ >= 15) continue;
                    this.renderDispatcher.updateTransparencyLater(chunkRenderer, profiler);
                }
            }
            profiler.method_15407();
        }
        profiler.method_15405("layer_setup");
        boolean reverse = isTranslucent;
        int startIndex = reverse ? this.renderInfos.size() - 1 : 0;
        int stopIndex = reverse ? -1 : this.renderInfos.size();
        int increment = reverse ? -1 : 1;
        int count = 0;
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        class_9958 orgFog = RenderSystem.getShaderFog();
        class_286.method_34420();
        boolean renderAsTranslucent = Configs.Visuals.RENDER_BLOCKS_AS_TRANSLUCENT.getBooleanValue();
        if (renderAsTranslucent) {
            float alpha = (float)Configs.Visuals.GHOST_BLOCK_ALPHA.getDoubleValue();
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        }
        profiler.method_15405("layer_uniforms");
        shader.method_60897(renderLayer.method_23033(), matrices, projMatrix, class_310.method_1551().method_22683());
        RenderSystem.setupShaderLights((class_5944)shader);
        RenderSystem.setShaderFog((class_9958)class_9958.field_53065);
        shader.method_34586();
        boolean startedDrawing = false;
        profiler.method_15405("layer_iteration");
        this.profiler = profiler;
        for (int i = startIndex; i != stopIndex; i += increment) {
            ChunkRendererSchematicVbo renderer = this.renderInfos.get(i);
            if (renderer.getChunkRenderData().isBlockLayerEmpty(renderLayer)) continue;
            class_2338 chunkOrigin = renderer.getOrigin();
            class_291 buffer = renderer.getBlocksVertexBufferByLayer(renderLayer);
            if (buffer == null || buffer.method_43444() || !renderer.getChunkRenderData().getBuiltBufferCache().hasBuiltBufferByLayer(renderLayer)) continue;
            matrix4fStack.pushMatrix();
            matrix4fStack.translate((float)((double)chunkOrigin.method_10263() - x), (float)((double)chunkOrigin.method_10264() - y), (float)((double)chunkOrigin.method_10260() - z));
            buffer.method_1353();
            buffer.method_34427((Matrix4f)matrix4fStack, projMatrix, shader);
            class_291.method_1354();
            matrix4fStack.popMatrix();
            startedDrawing = true;
            ++count;
        }
        profiler.method_15405("layer_cleanup");
        if (renderAsTranslucent) {
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        shader.method_34585();
        if (startedDrawing) {
            renderLayer.method_23031().method_22651();
        }
        class_291.method_1354();
        renderLayer.method_23518();
        RenderSystem.setShaderFog((class_9958)orgFog);
        profiler.method_15407();
        return count;
    }

    public void renderBlockOverlays(Matrix4f viewMatrix, class_4184 camera, Matrix4f projMatrix, class_3695 profiler) {
        this.profiler = profiler;
        this.renderBlockOverlay(OverlayRenderType.OUTLINE, viewMatrix, camera, projMatrix, profiler);
        this.renderBlockOverlay(OverlayRenderType.QUAD, viewMatrix, camera, projMatrix, profiler);
    }

    protected void renderBlockOverlay(OverlayRenderType type, Matrix4f viewMatrix, class_4184 camera, Matrix4f projMatrix, class_3695 profiler) {
        boolean renderThrough;
        profiler.method_15396("overlay_" + type.name());
        this.profiler = profiler;
        class_1921 renderLayer = class_1921.method_23583();
        renderLayer.method_23516();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        class_243 cameraPos = camera.method_19326();
        double x = cameraPos.field_1352;
        double y = cameraPos.field_1351;
        double z = cameraPos.field_1350;
        boolean bl = renderThrough = Configs.Visuals.SCHEMATIC_OVERLAY_RENDER_THROUGH.getBooleanValue() || Hotkeys.RENDER_OVERLAY_THROUGH_BLOCKS.getKeybind().isKeybindHeld();
        if (renderThrough) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }
        class_5944 originalShader = RenderSystem.getShader();
        class_5944 shader = RenderSystem.setShader((class_10156)class_10142.field_53876);
        class_286.method_34420();
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        profiler.method_15405("overlay_iterate");
        this.profiler = profiler;
        for (int i = this.renderInfos.size() - 1; i >= 0; --i) {
            ChunkRenderDataSchematic compiledChunk;
            ChunkRendererSchematicVbo renderer = this.renderInfos.get(i);
            if (renderer.getChunkRenderData() == ChunkRenderDataSchematic.EMPTY || !renderer.hasOverlay() || (compiledChunk = renderer.getChunkRenderData()).isOverlayTypeEmpty(type)) continue;
            class_291 buffer = renderer.getOverlayVertexBuffer(type);
            class_2338 chunkOrigin = renderer.getOrigin();
            if (buffer == null || buffer.method_43444() || !renderer.getChunkRenderData().getBuiltBufferCache().hasBuiltBufferByType(type)) continue;
            matrix4fStack.pushMatrix();
            matrix4fStack.translate((float)((double)chunkOrigin.method_10263() - x), (float)((double)chunkOrigin.method_10264() - y), (float)((double)chunkOrigin.method_10260() - z));
            buffer.method_1353();
            buffer.method_34427((Matrix4f)matrix4fStack, projMatrix, shader);
            class_291.method_1354();
            matrix4fStack.popMatrix();
        }
        renderLayer.method_23518();
        RenderSystem.setShader((class_5944)originalShader);
        RenderSystem.disableBlend();
        profiler.method_15407();
    }

    public boolean renderBlock(class_1920 world, class_2680 state, class_2338 pos, class_4587 matrices, class_287 bufferBuilderIn) {
        try {
            class_2464 renderType = state.method_26217();
            if (renderType == class_2464.field_11455) {
                return false;
            }
            this.getProfiler().method_15396("render_block");
            BlockModelRendererSchematic.enableCache();
            boolean result = renderType == class_2464.field_11458 && this.blockModelRenderer.renderModel(world, this.getModelForState(state), state, pos, matrices, (class_4588)bufferBuilderIn, state.method_26190(pos));
            BlockModelRendererSchematic.disableCache();
            this.getProfiler().method_15407();
            return result;
        }
        catch (Throwable throwable) {
            class_128 crashreport = class_128.method_560((Throwable)throwable, (String)"Tesselating block in world");
            class_129 crashreportcategory = crashreport.method_562("Block being tesselated");
            class_129.method_586((class_129)crashreportcategory, (class_5539)world, (class_2338)pos, (class_2680)state);
            throw new class_148(crashreport);
        }
    }

    public void renderFluid(class_1920 world, class_2680 blockState, class_3610 fluidState, class_2338 pos, class_287 bufferBuilderIn) {
        this.getProfiler().method_15396("render_fluid");
        try {
            this.blockRenderManager.method_3352(pos, world, (class_4588)bufferBuilderIn, blockState, fluidState);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.getProfiler().method_15407();
    }

    public class_1087 getModelForState(class_2680 state) {
        class_1087 model = this.blockRenderManager.method_3351().method_3335(state);
        return model;
    }

    public void renderEntities(class_4184 camera, class_4604 frustum, class_4587 matrices, class_4597.class_4598 immediate, float partialTicks, class_3695 profiler) {
        this.profiler = profiler;
        if (this.renderEntitiesStartupCounter > 0) {
            --this.renderEntitiesStartupCounter;
        } else {
            profiler.method_15396("entities_prepare");
            double cameraX = camera.method_19326().field_1352;
            double cameraY = camera.method_19326().field_1351;
            double cameraZ = camera.method_19326().field_1350;
            this.entityRenderDispatcher.method_3941((class_1937)this.world, camera, this.mc.field_1692);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            this.countEntitiesTotal = this.world.getRegularEntityCount();
            profiler.method_15405("regular");
            LayerRange layerRange = DataManager.getRenderLayerRange();
            profiler.method_15405("regular_iterate");
            this.profiler = profiler;
            for (ChunkRendererSchematicVbo chunkRenderer : this.renderInfos) {
                class_2338 pos = chunkRenderer.getOrigin();
                ChunkSchematic chunk = this.world.getChunk(pos.method_10263() >> 4, pos.method_10260() >> 4);
                List<class_1297> list = chunk.getEntityList();
                if (list.isEmpty()) continue;
                for (class_1297 entityTmp : list) {
                    boolean shouldRender;
                    if (!layerRange.isPositionWithinRange((int)entityTmp.method_23317(), (int)entityTmp.method_23318(), (int)entityTmp.method_23321()) || !(shouldRender = this.entityRenderDispatcher.method_3950(entityTmp, frustum, cameraX, cameraY, cameraZ))) continue;
                    double lerpX = class_3532.method_16436((double)partialTicks, (double)entityTmp.field_6038, (double)entityTmp.method_23317());
                    double lerpY = class_3532.method_16436((double)partialTicks, (double)entityTmp.field_5971, (double)entityTmp.method_23318());
                    double lerpZ = class_3532.method_16436((double)partialTicks, (double)entityTmp.field_5989, (double)entityTmp.method_23321());
                    double x = lerpX - cameraX;
                    double y = lerpY - cameraY;
                    double z = lerpZ - cameraZ;
                    matrices.method_22903();
                    if (entityTmp instanceof class_1462 || entityTmp instanceof class_1431 || entityTmp instanceof class_7110 || entityTmp instanceof class_1496 || entityTmp instanceof class_1474 || entityTmp instanceof class_9866) {
                        class_3611 fluid;
                        class_2680 state = this.world.method_8320(entityTmp.method_24515());
                        class_3611 class_36112 = fluid = state.method_26227() != null ? state.method_26227().method_15772() : class_3612.field_15906;
                        if (!(fluid != class_3612.field_15910 && fluid != class_3612.field_15909 || ((IMixinEntity)entityTmp).litematica_isTouchingWater())) {
                            ((IEntityInvoker)entityTmp).litematica$toggleTouchingWater(true);
                        }
                    }
                    this.entityRenderDispatcher.method_62424(entityTmp, x, y, z, partialTicks, matrices, (class_4597)immediate, this.entityRenderDispatcher.method_23839(entityTmp, partialTicks));
                    ++this.countEntitiesRendered;
                    matrices.method_22909();
                }
            }
            profiler.method_15407();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void renderBlockEntities(class_4184 camera, class_4604 frustum, class_4587 matrices, class_4597.class_4598 immediate, class_4597.class_4598 immediate2, float partialTicks, class_3695 profiler) {
        this.profiler = profiler;
        profiler.method_15396("block_entities_prepare");
        double cameraX = camera.method_19326().field_1352;
        double cameraY = camera.method_19326().field_1351;
        double cameraZ = camera.method_19326().field_1350;
        this.blockEntityRenderDispatcher.method_3549((class_1937)this.world, camera, this.mc.field_1765);
        LayerRange layerRange = DataManager.getRenderLayerRange();
        profiler.method_15405("block_entities");
        this.profiler = profiler;
        profiler.method_15405("render_be");
        for (ChunkRendererSchematicVbo chunkRenderer : this.renderInfos) {
            ChunkRenderDataSchematic data = chunkRenderer.getChunkRenderData();
            List<class_2586> tiles = data.getBlockEntities();
            if (tiles.isEmpty()) continue;
            class_2338 chunkOrigin = chunkRenderer.getOrigin();
            ChunkSchematic chunk = this.world.getChunkProvider().getChunk(chunkOrigin.method_10263() >> 4, chunkOrigin.method_10260() >> 4);
            if (chunk == null || data.getTimeBuilt() < chunk.getTimeCreated()) continue;
            for (class_2586 te : tiles) {
                class_2338 pos = te.method_11016();
                if (!layerRange.isPositionWithinRange(pos.method_10263(), pos.method_10264(), pos.method_10260())) continue;
                try {
                    matrices.method_22903();
                    matrices.method_22904((double)pos.method_10263() - cameraX, (double)pos.method_10264() - cameraY, (double)pos.method_10260() - cameraZ);
                    this.blockEntityRenderDispatcher.method_3555(te, partialTicks, matrices, (class_4597)immediate2);
                    matrices.method_22909();
                }
                catch (Exception err) {
                    Litematica.LOGGER.error("[Pass 1] Error rendering blockEntities; Exception: {}", (Object)err.getLocalizedMessage());
                }
            }
        }
        immediate2.method_37104();
        profiler.method_15405("render_be_no_cull");
        Set<class_2586> set = this.blockEntities;
        synchronized (set) {
            for (class_2586 te : this.blockEntities) {
                class_2338 pos = te.method_11016();
                if (!layerRange.isPositionWithinRange(pos.method_10263(), pos.method_10264(), pos.method_10260())) continue;
                try {
                    matrices.method_22903();
                    matrices.method_22904((double)pos.method_10263() - cameraX, (double)pos.method_10264() - cameraY, (double)pos.method_10260() - cameraZ);
                    this.blockEntityRenderDispatcher.method_3555(te, partialTicks, matrices, (class_4597)immediate);
                    matrices.method_22909();
                }
                catch (Exception err) {
                    Litematica.LOGGER.error("[Pass 2] Error rendering blockEntities; Exception: {}", (Object)err.getLocalizedMessage());
                }
            }
        }
        immediate.method_37104();
        profiler.method_15407();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void updateBlockEntities(Collection<class_2586> toRemove, Collection<class_2586> toAdd) {
        Set<class_2586> set = this.blockEntities;
        synchronized (set) {
            this.blockEntities.removeAll(toRemove);
            this.blockEntities.addAll(toAdd);
        }
    }

    public void scheduleChunkRenders(int chunkX, int chunkZ) {
        this.getProfiler().method_15396("schedule_render");
        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue()) {
            this.chunkRendererDispatcher.scheduleChunkRender(chunkX, chunkZ);
        }
        this.getProfiler().method_15407();
    }
}

