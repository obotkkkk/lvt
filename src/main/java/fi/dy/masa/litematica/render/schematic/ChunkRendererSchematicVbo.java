/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_10142
 *  net.minecraft.class_10156
 *  net.minecraft.class_1087
 *  net.minecraft.class_1297
 *  net.minecraft.class_1921
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2464
 *  net.minecraft.class_2586
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2818$class_2819
 *  net.minecraft.class_287
 *  net.minecraft.class_291
 *  net.minecraft.class_310
 *  net.minecraft.class_3481
 *  net.minecraft.class_3610
 *  net.minecraft.class_3695
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4696
 *  net.minecraft.class_5819
 *  net.minecraft.class_638
 *  net.minecraft.class_8251
 *  net.minecraft.class_827
 *  net.minecraft.class_8555
 *  net.minecraft.class_9799
 *  net.minecraft.class_9799$class_9800
 *  net.minecraft.class_9801
 *  net.minecraft.class_9801$class_9802
 */
package fi.dy.masa.litematica.render.schematic;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.RenderUtils;
import fi.dy.masa.litematica.render.schematic.BufferAllocatorCache;
import fi.dy.masa.litematica.render.schematic.BufferBuilderCache;
import fi.dy.masa.litematica.render.schematic.ChunkCacheSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderDataSchematic;
import fi.dy.masa.litematica.render.schematic.ChunkRenderLayers;
import fi.dy.masa.litematica.render.schematic.ChunkRenderTaskSchematic;
import fi.dy.masa.litematica.render.schematic.IBufferBuilderPatch;
import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.util.IgnoreBlockRegistry;
import fi.dy.masa.litematica.util.OverlayType;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.EntityUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_10142;
import net.minecraft.class_10156;
import net.minecraft.class_1087;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2464;
import net.minecraft.class_2586;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_287;
import net.minecraft.class_291;
import net.minecraft.class_310;
import net.minecraft.class_3481;
import net.minecraft.class_3610;
import net.minecraft.class_3695;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4696;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_8251;
import net.minecraft.class_827;
import net.minecraft.class_8555;
import net.minecraft.class_9799;
import net.minecraft.class_9801;

public class ChunkRendererSchematicVbo
implements AutoCloseable {
    protected static int schematicRenderChunksUpdated;
    protected volatile WorldSchematic world;
    protected final WorldRendererSchematic worldRenderer;
    private final class_5819 rand;
    protected final ReentrantLock chunkRenderLock;
    protected final ReentrantLock chunkRenderDataLock;
    protected final Set<class_2586> setBlockEntities = new HashSet<class_2586>();
    protected class_3695 profiler;
    protected final class_2338.class_2339 position;
    protected final class_2338.class_2339 chunkRelativePos;
    protected final Map<class_1921, class_291> vertexBufferBlocks;
    protected final Map<OverlayRenderType, class_291> vertexBufferOverlay;
    protected final List<IntBoundingBox> boxes = new ArrayList<IntBoundingBox>();
    protected final EnumSet<OverlayRenderType> existingOverlays = EnumSet.noneOf(OverlayRenderType.class);
    private class_238 boundingBox;
    protected Color4f overlayColor;
    protected boolean hasOverlay;
    private boolean ignoreClientWorldFluids;
    private IgnoreBlockRegistry ignoreBlockRegistry;
    protected ChunkCacheSchematic schematicWorldView;
    protected ChunkCacheSchematic clientWorldView;
    private final BufferBuilderCache builderCache;
    protected ChunkRenderTaskSchematic compileTask;
    protected ChunkRenderDataSchematic chunkRenderData;
    private boolean needsUpdate;
    private boolean needsImmediateUpdate;

    protected ChunkRendererSchematicVbo(WorldSchematic world, WorldRendererSchematic worldRenderer) {
        this.world = world;
        this.worldRenderer = worldRenderer;
        this.rand = class_5819.method_43047();
        this.chunkRenderData = ChunkRenderDataSchematic.EMPTY;
        this.chunkRenderLock = new ReentrantLock();
        this.chunkRenderDataLock = new ReentrantLock();
        this.vertexBufferBlocks = new IdentityHashMap<class_1921, class_291>();
        this.vertexBufferOverlay = new IdentityHashMap<OverlayRenderType, class_291>();
        this.position = new class_2338.class_2339();
        this.chunkRelativePos = new class_2338.class_2339();
        this.builderCache = new BufferBuilderCache();
        this.hasOverlay = false;
    }

    public boolean hasOverlay() {
        return this.hasOverlay;
    }

    protected class_3695 getProfiler() {
        if (this.profiler == null) {
            this.profiler = this.worldRenderer.getProfiler();
        }
        return this.profiler;
    }

    public EnumSet<OverlayRenderType> getOverlayTypes() {
        return this.existingOverlays;
    }

    protected class_291 getBlocksVertexBufferByLayer(class_1921 layer) {
        return this.vertexBufferBlocks.computeIfAbsent(layer, l -> new class_291(class_8555.field_54340));
    }

    protected class_291 getOverlayVertexBuffer(OverlayRenderType type) {
        return this.vertexBufferOverlay.computeIfAbsent(type, l -> new class_291(class_8555.field_54340));
    }

    protected ChunkRenderDataSchematic getChunkRenderData() {
        return this.chunkRenderData;
    }

    protected BufferBuilderCache getBuilderCache() {
        return this.builderCache;
    }

    protected void setChunkRenderData(ChunkRenderDataSchematic data) {
        this.chunkRenderDataLock.lock();
        try {
            this.chunkRenderData = data;
        }
        finally {
            this.chunkRenderDataLock.unlock();
        }
    }

    public class_2338 getOrigin() {
        return this.position;
    }

    public class_238 getBoundingBox() {
        if (this.boundingBox == null) {
            int x = this.position.method_10263();
            int y = this.position.method_10264();
            int z = this.position.method_10260();
            this.boundingBox = new class_238((double)x, (double)y, (double)z, (double)(x + 16), (double)(y + this.world.method_31605()), (double)(z + 16));
        }
        return this.boundingBox;
    }

    protected void setPosition(int x, int y, int z) {
        if (x != this.position.method_10263() || y != this.position.method_10264() || z != this.position.method_10260()) {
            this.clear();
            this.boundingBox = null;
            this.position.method_10103(x, y, z);
        }
    }

    protected double getDistanceSq() {
        class_1297 entity = EntityUtils.getCameraEntity();
        double x = (double)this.position.method_10263() + 8.0 - entity.method_23317();
        double z = (double)this.position.method_10260() + 8.0 - entity.method_23321();
        return x * x + z * z;
    }

    protected void deleteGlResources() {
        this.clear();
        this.closeAllVertexBuffers();
    }

    private void closeAllVertexBuffers() {
        this.vertexBufferBlocks.values().forEach(class_291::close);
        this.vertexBufferOverlay.values().forEach(class_291::close);
        this.vertexBufferBlocks.clear();
        this.vertexBufferOverlay.clear();
    }

    protected void resortTransparency(ChunkRenderTaskSchematic task, class_3695 profiler) {
        this.profiler = profiler;
        this.getProfiler().method_15396("resort_task");
        ChunkRenderDataSchematic data = task.getChunkRenderData();
        class_243 cameraPos = task.getCameraPosSupplier().get();
        class_1921 layerTranslucent = class_1921.method_23583();
        BufferAllocatorCache allocators = task.getAllocatorCache();
        float x = (float)cameraPos.field_1352 - (float)this.position.method_10263();
        float y = (float)cameraPos.field_1351 - (float)this.position.method_10264();
        float z = (float)cameraPos.field_1350 - (float)this.position.method_10260();
        if (!data.isBlockLayerEmpty(layerTranslucent) && Configs.Visuals.RENDER_ENABLE_TRANSLUCENT_RESORTING.getBooleanValue()) {
            this.getProfiler().method_15405("resort_blocks");
            RenderSystem.setShader((class_10156)class_10142.field_53884);
            if (data.getBuiltBufferCache().hasBuiltBufferByLayer(layerTranslucent)) {
                try {
                    this.resortRenderBlocks(layerTranslucent, x, y, z, data, allocators);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("resortTransparency() [VBO] caught exception for layer [{}] // {}", (Object)ChunkRenderLayers.getFriendlyName(layerTranslucent), (Object)e.toString());
                }
            }
        }
        this.getProfiler().method_15407();
        this.profiler = null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void rebuildChunk(ChunkRenderTaskSchematic task, class_3695 profiler) {
        this.profiler = profiler;
        this.getProfiler().method_15396("rebuild_chunk");
        ChunkRenderDataSchematic data = new ChunkRenderDataSchematic();
        task.getLock().lock();
        try {
            if (task.getStatus() != ChunkRenderTaskSchematic.Status.COMPILING) {
                return;
            }
            task.setChunkRenderData(data);
        }
        finally {
            task.getLock().unlock();
        }
        this.builderCache.clearAll();
        HashSet<class_2586> tileEntities = new HashSet<class_2586>();
        class_2338.class_2339 posChunk = this.position;
        LayerRange range = DataManager.getRenderLayerRange();
        BufferAllocatorCache allocators = task.getAllocatorCache();
        this.existingOverlays.clear();
        this.hasOverlay = false;
        this.getProfiler().method_15405("rebuild_chunk_start");
        List<IntBoundingBox> list = this.boxes;
        synchronized (list) {
            int minX = posChunk.method_10263();
            int minY = posChunk.method_10264();
            int minZ = posChunk.method_10260();
            int maxX = minX + 15;
            int maxY = minY + this.world.method_31605();
            int maxZ = minZ + 15;
            if (!(this.boxes.isEmpty() || this.schematicWorldView.isEmpty() && this.clientWorldView.isEmpty() || !range.intersectsBox(minX, minY, minZ, maxX, maxY, maxZ))) {
                ++schematicRenderChunksUpdated;
                class_243 cameraPos = task.getCameraPosSupplier().get();
                float x = (float)cameraPos.field_1352 - (float)this.position.method_10263();
                float y = (float)cameraPos.field_1351 - (float)this.position.method_10264();
                float z = (float)cameraPos.field_1350 - (float)this.position.method_10260();
                HashSet<class_1921> usedLayers = new HashSet<class_1921>();
                class_4587 matrixStack = new class_4587();
                int bottomY = this.position.method_10264();
                this.getProfiler().method_15405("rebuild_chunk_boxes");
                for (IntBoundingBox box : this.boxes) {
                    if ((box = range.getClampedRenderBoundingBox(box)) == null) continue;
                    class_2338 posFrom = new class_2338(box.minX, box.minY, box.minZ);
                    class_2338 posTo = new class_2338(box.maxX, box.maxY, box.maxZ);
                    for (class_2338 posMutable : class_2338.class_2339.method_10097((class_2338)posFrom, (class_2338)posTo)) {
                        matrixStack.method_22903();
                        matrixStack.method_46416((float)(posMutable.method_10263() & 0xF), (float)(posMutable.method_10264() - bottomY), (float)(posMutable.method_10260() & 0xF));
                        this.renderBlocksAndOverlay(posMutable, data, allocators, tileEntities, usedLayers, matrixStack);
                        matrixStack.method_22909();
                    }
                }
                this.getProfiler().method_15405("rebuild_chunk_layers");
                for (class_1921 layerTmp : ChunkRenderLayers.LAYERS) {
                    if (usedLayers.contains(layerTmp)) {
                        data.setBlockLayerUsed(layerTmp);
                    }
                    if (!data.isBlockLayerStarted(layerTmp)) continue;
                    try {
                        data.setBlockLayerUsed(layerTmp);
                        this.postRenderBlocks(layerTmp, x, y, z, data, allocators);
                    }
                    catch (Exception e) {
                        Litematica.LOGGER.error("rebuildChunk() [VBO] failed to postRenderBlocks() for layer [{}] --> {}", (Object)ChunkRenderLayers.getFriendlyName(layerTmp), (Object)e.toString());
                    }
                }
                if (this.hasOverlay) {
                    this.getProfiler().method_15405("rebuild_chunk_overlays");
                    for (OverlayRenderType type : this.existingOverlays) {
                        if (!data.isOverlayTypeStarted(type)) continue;
                        try {
                            data.setOverlayTypeUsed(type);
                            this.postRenderOverlay(type, x, y, z, data, allocators);
                        }
                        catch (Exception e) {
                            Litematica.LOGGER.error("rebuildChunk() [VBO] failed to postRenderOverlay() for overlay type [{}] --> {}", (Object)type.getDrawMode().name(), (Object)e.toString());
                        }
                    }
                }
            }
        }
        this.getProfiler().method_15405("rebuild_chunk_lock");
        this.chunkRenderLock.lock();
        try {
            HashSet set = Sets.newHashSet(tileEntities);
            HashSet set1 = Sets.newHashSet(this.setBlockEntities);
            set.removeAll(this.setBlockEntities);
            set1.removeAll(tileEntities);
            this.setBlockEntities.clear();
            this.setBlockEntities.addAll(tileEntities);
            this.worldRenderer.updateBlockEntities(set1, set);
            this.builderCache.clearAll();
        }
        finally {
            this.chunkRenderLock.unlock();
        }
        this.getProfiler().method_15407();
        this.profiler = null;
        data.setTimeBuilt(this.world.method_8510());
    }

    protected void renderBlocksAndOverlay(class_2338 pos, @Nonnull ChunkRenderDataSchematic data, @Nonnull BufferAllocatorCache allocators, Set<class_2586> tileEntities, Set<class_1921> usedLayers, class_4587 matrixStack) {
        class_2680 stateSchematic = this.schematicWorldView.method_8320(pos);
        class_2680 stateClient = this.clientWorldView.method_8320(pos);
        boolean clientHasAir = stateClient.method_26215();
        boolean schematicHasAir = stateSchematic.method_26215();
        boolean missing = false;
        if (clientHasAir && schematicHasAir) {
            return;
        }
        this.getProfiler().method_15396("render_build");
        this.overlayColor = null;
        if (clientHasAir || stateSchematic != stateClient && Configs.Visuals.RENDER_COLLIDING_SCHEMATIC_BLOCKS.getBooleanValue()) {
            class_1921 layer;
            if (stateSchematic.method_31709()) {
                this.addBlockEntity(pos, data, tileEntities);
            }
            boolean translucent = Configs.Visuals.RENDER_BLOCKS_AS_TRANSLUCENT.getBooleanValue();
            class_3610 fluidState = stateSchematic.method_26227();
            if (!fluidState.method_15769() && Configs.Visuals.ENABLE_SCHEMATIC_FLUIDS.getBooleanValue()) {
                this.getProfiler().method_15405("render_build_fluids");
                layer = class_4696.method_23680((class_3610)fluidState);
                int offsetY = (pos.method_10264() >> 4 << 4) - this.position.method_10264();
                class_287 bufferSchematic = this.builderCache.getBufferByLayer(layer, allocators);
                if (!data.isBlockLayerStarted(layer) || bufferSchematic == null) {
                    data.setBlockLayerStarted(layer);
                    bufferSchematic = this.preRenderBlocks(layer, allocators);
                }
                ((IBufferBuilderPatch)bufferSchematic).litematica$setOffsetY(offsetY);
                this.worldRenderer.renderFluid(this.schematicWorldView, stateSchematic, fluidState, pos, bufferSchematic);
                usedLayers.add(layer);
                ((IBufferBuilderPatch)bufferSchematic).litematica$setOffsetY(0.0f);
            }
            if (stateSchematic.method_26217() != class_2464.field_11455) {
                this.getProfiler().method_15405("render_build_blocks");
                layer = translucent ? class_1921.method_23583() : class_4696.method_23679((class_2680)stateSchematic);
                class_287 bufferSchematic = this.builderCache.getBufferByLayer(layer, allocators);
                if (!data.isBlockLayerStarted(layer) || bufferSchematic == null) {
                    data.setBlockLayerStarted(layer);
                    bufferSchematic = this.preRenderBlocks(layer, allocators);
                }
                if (this.worldRenderer.renderBlock(this.schematicWorldView, stateSchematic, pos, matrixStack, bufferSchematic)) {
                    usedLayers.add(layer);
                }
                if (clientHasAir) {
                    missing = true;
                }
            }
        }
        if (Configs.Visuals.ENABLE_SCHEMATIC_OVERLAY.getBooleanValue()) {
            this.getProfiler().method_15405("render_build_overlays");
            OverlayType type = this.getOverlayType(stateSchematic, stateClient);
            this.overlayColor = ChunkRendererSchematicVbo.getOverlayColor(type);
            if (this.overlayColor != null) {
                if (this.shouldCullOverlayPos(pos, stateSchematic, stateClient)) {
                    this.getProfiler().method_15407();
                    return;
                }
                this.renderOverlay(type, pos, stateSchematic, missing, data, allocators);
            }
        }
        this.getProfiler().method_15407();
    }

    private boolean shouldCullOverlayPos(class_2338 posIn, class_2680 stateSchematic, class_2680 stateClient) {
        if (!stateSchematic.method_26227().method_15769() && !Configs.Visuals.ENABLE_SCHEMATIC_FLUIDS.getBooleanValue()) {
            return true;
        }
        if (Configs.Visuals.RENDER_BLOCKS_AS_TRANSLUCENT.getBooleanValue() && Configs.Visuals.RENDER_TRANSLUCENT_INNER_SIDES.getBooleanValue()) {
            return false;
        }
        if (Configs.Visuals.ENABLE_SCHEMATIC_OVERLAY_CULLING.getBooleanValue() && stateClient.method_26164(class_3481.field_51989)) {
            int count = 0;
            for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
                if (!DataManager.getRenderLayerRange().isPositionAtRenderEdgeOnSide(posIn, side) && !class_2248.method_9607((class_2680)stateSchematic, (class_2680)this.schematicWorldView.method_8320(posIn.method_10093(side)), (class_2350)side)) continue;
                ++count;
            }
            if (count == 0) {
                return true;
            }
        }
        return false;
    }

    protected void renderOverlay(OverlayType type, class_2338 pos, class_2680 stateSchematic, boolean missing, @Nonnull ChunkRenderDataSchematic data, @Nonnull BufferAllocatorCache allocators) {
        OverlayRenderType overlayType;
        this.getProfiler().method_15396("render_overlay");
        boolean useDefault = false;
        class_2338.class_2339 relPos = this.getChunkRelativePosition(pos);
        if (Configs.Visuals.SCHEMATIC_OVERLAY_ENABLE_SIDES.getBooleanValue()) {
            this.getProfiler().method_15396("overlay_sides");
            overlayType = OverlayRenderType.QUAD;
            class_287 bufferOverlayQuads = this.builderCache.getBufferByOverlay(overlayType, allocators);
            if (!data.isOverlayTypeStarted(overlayType) || bufferOverlayQuads == null) {
                data.setOverlayTypeStarted(overlayType);
                bufferOverlayQuads = this.preRenderOverlay(overlayType, allocators);
            }
            if (Configs.Visuals.OVERLAY_REDUCED_INNER_SIDES.getBooleanValue()) {
                this.getProfiler().method_15405("cull_inner_sides");
                class_2338.class_2339 posMutable = new class_2338.class_2339();
                for (int i = 0; i < 6; ++i) {
                    class_2350 side = PositionUtils.ALL_DIRECTIONS[i];
                    posMutable.method_10103(pos.method_10263() + side.method_10148(), pos.method_10264() + side.method_10164(), pos.method_10260() + side.method_10165());
                    class_2680 adjStateSchematic = this.schematicWorldView.method_8320((class_2338)posMutable);
                    class_2680 adjStateClient = this.clientWorldView.method_8320((class_2338)posMutable);
                    OverlayType typeAdj = this.getOverlayType(adjStateSchematic, adjStateClient);
                    if (missing && Configs.Visuals.SCHEMATIC_OVERLAY_MODEL_SIDES.getBooleanValue()) {
                        this.getProfiler().method_15405("cull_render_model_sides");
                        class_1087 bakedModel = this.worldRenderer.getModelForState(stateSchematic);
                        if (type.getRenderPriority() <= typeAdj.getRenderPriority() && class_2248.method_9501((class_265)stateSchematic.method_26220((class_1922)this.schematicWorldView, pos), (class_2350)side)) continue;
                        this.getProfiler().method_15405("cull_render_model");
                        RenderUtils.drawBlockModelQuadOverlayBatched(bakedModel, stateSchematic, (class_2338)relPos, side, this.overlayColor, 0.0, bufferOverlayQuads, this.rand);
                        continue;
                    }
                    if (type.getRenderPriority() > typeAdj.getRenderPriority()) {
                        this.getProfiler().method_15405("cull_render_default");
                        RenderUtils.drawBlockBoxSideBatchedQuads((class_2338)relPos, side, this.overlayColor, 0.0, bufferOverlayQuads);
                        continue;
                    }
                    useDefault = true;
                }
            } else if (missing && Configs.Visuals.SCHEMATIC_OVERLAY_MODEL_SIDES.getBooleanValue()) {
                this.getProfiler().method_15405("render_model_sides");
                class_1087 bakedModel = this.worldRenderer.getModelForState(stateSchematic);
                if (RenderUtils.modelHasQuads(bakedModel, stateSchematic, this.rand)) {
                    this.getProfiler().method_15405("render_batched");
                    RenderUtils.drawBlockModelQuadOverlayBatched(bakedModel, stateSchematic, (class_2338)relPos, this.overlayColor, 0.0, bufferOverlayQuads, this.rand);
                } else {
                    useDefault = true;
                }
            } else {
                useDefault = true;
            }
            if (useDefault) {
                this.getProfiler().method_15405("render_batched_default");
                fi.dy.masa.malilib.render.RenderUtils.drawBlockBoundingBoxSidesBatchedQuads((class_2338)relPos, (Color4f)this.overlayColor, (double)0.0, (class_287)bufferOverlayQuads);
            }
            this.getProfiler().method_15407();
        }
        if (Configs.Visuals.SCHEMATIC_OVERLAY_ENABLE_OUTLINES.getBooleanValue()) {
            this.getProfiler().method_15396("overlay_outlines");
            useDefault = false;
            overlayType = OverlayRenderType.OUTLINE;
            class_287 bufferOverlayOutlines = this.builderCache.getBufferByOverlay(overlayType, allocators);
            if (!data.isOverlayTypeStarted(overlayType) || bufferOverlayOutlines == null) {
                data.setOverlayTypeStarted(overlayType);
                bufferOverlayOutlines = this.preRenderOverlay(overlayType, allocators);
            }
            Color4f overlayColor = new Color4f(this.overlayColor.r, this.overlayColor.g, this.overlayColor.b, 1.0f);
            this.getProfiler().method_15405("cull_inner_sides");
            if (Configs.Visuals.OVERLAY_REDUCED_INNER_SIDES.getBooleanValue()) {
                OverlayType[][][] adjTypes = new OverlayType[3][3][3];
                class_2338.class_2339 posMutable = new class_2338.class_2339();
                for (int y = 0; y <= 2; ++y) {
                    for (int z = 0; z <= 2; ++z) {
                        for (int x = 0; x <= 2; ++x) {
                            if (x != 1 || y != 1 || z != 1) {
                                posMutable.method_10103(pos.method_10263() + x - 1, pos.method_10264() + y - 1, pos.method_10260() + z - 1);
                                class_2680 adjStateSchematic = this.schematicWorldView.method_8320((class_2338)posMutable);
                                class_2680 adjStateClient = this.clientWorldView.method_8320((class_2338)posMutable);
                                adjTypes[x][y][z] = this.getOverlayType(adjStateSchematic, adjStateClient);
                                continue;
                            }
                            adjTypes[x][y][z] = type;
                        }
                    }
                }
                if (missing && Configs.Visuals.SCHEMATIC_OVERLAY_MODEL_OUTLINE.getBooleanValue()) {
                    if (stateSchematic.method_26225()) {
                        useDefault = true;
                    } else {
                        class_1087 bakedModel = this.worldRenderer.getModelForState(stateSchematic);
                        if (RenderUtils.modelHasQuads(bakedModel, stateSchematic, this.rand)) {
                            this.getProfiler().method_15405("render_model_batched");
                            RenderUtils.drawBlockModelOutlinesBatched(bakedModel, stateSchematic, (class_2338)relPos, overlayColor, 0.0, bufferOverlayOutlines, this.rand);
                        } else {
                            useDefault = true;
                        }
                    }
                } else {
                    this.getProfiler().method_15405("render_reduced_edges");
                    this.renderOverlayReducedEdges(pos, adjTypes, type, bufferOverlayOutlines);
                }
            } else {
                this.getProfiler().method_15405("render_fallback");
                if (missing && Configs.Visuals.SCHEMATIC_OVERLAY_MODEL_OUTLINE.getBooleanValue()) {
                    this.getProfiler().method_15405("render_model_batched");
                    class_1087 bakedModel = this.worldRenderer.getModelForState(stateSchematic);
                    if (RenderUtils.modelHasQuads(bakedModel, stateSchematic, this.rand)) {
                        RenderUtils.drawBlockModelOutlinesBatched(bakedModel, stateSchematic, (class_2338)relPos, overlayColor, 0.0, bufferOverlayOutlines, this.rand);
                    } else {
                        useDefault = true;
                    }
                } else {
                    useDefault = true;
                }
            }
            if (useDefault) {
                try {
                    this.getProfiler().method_15405("render_batched_box");
                    RenderUtils.drawBlockBoundingBoxOutlinesBatchedDebugLines((class_2338)relPos, overlayColor, 0.0, bufferOverlayOutlines);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            this.getProfiler().method_15407();
        }
        this.getProfiler().method_15407();
    }

    protected class_2338.class_2339 getChunkRelativePosition(class_2338 pos) {
        return this.chunkRelativePos.method_10103(pos.method_10263() & 0xF, pos.method_10264() - this.position.method_10264(), pos.method_10260() & 0xF);
    }

    protected void renderOverlayReducedEdges(class_2338 pos, OverlayType[][][] adjTypes, OverlayType typeSelf, class_287 bufferOverlayOutlines) {
        OverlayType[] neighborTypes = new OverlayType[4];
        class_2382[] neighborPositions = new class_2382[4];
        int lines = 0;
        this.getProfiler().method_15396("overlay_reduced_edges");
        for (class_2350.class_2351 axis : fi.dy.masa.litematica.util.PositionUtils.AXES_ALL) {
            for (int corner = 0; corner < 4; ++corner) {
                class_2382[] offsets = fi.dy.masa.litematica.util.PositionUtils.getEdgeNeighborOffsets(axis, corner);
                int index = -1;
                boolean hasCurrent = false;
                for (int i = 0; i < 4; ++i) {
                    class_2382 offset = offsets[i];
                    OverlayType type = adjTypes[offset.method_10263() + 1][offset.method_10264() + 1][offset.method_10260() + 1];
                    if (type == OverlayType.NONE || index != -1 && type.getRenderPriority() < neighborTypes[index - 1].getRenderPriority()) continue;
                    if (index < 0 || type.getRenderPriority() > neighborTypes[index - 1].getRenderPriority()) {
                        index = 0;
                    }
                    neighborPositions[index] = new class_2382(pos.method_10263() + offset.method_10263(), pos.method_10264() + offset.method_10264(), pos.method_10260() + offset.method_10260());
                    neighborTypes[index] = type;
                    hasCurrent |= i == 0;
                    ++index;
                }
                this.getProfiler().method_15405("edges_plop");
                if (index <= 0 || !hasCurrent) continue;
                class_2382 posTmp = new class_2382(pos.method_10263(), pos.method_10264(), pos.method_10260());
                int ind = -1;
                for (int i = 0; i < index; ++i) {
                    class_2382 tmp = neighborPositions[i];
                    if (tmp.method_10263() > posTmp.method_10263() || tmp.method_10264() > posTmp.method_10264() || tmp.method_10260() > posTmp.method_10260()) continue;
                    posTmp = tmp;
                    ind = i;
                }
                if (posTmp.method_10263() != pos.method_10263() || posTmp.method_10264() != pos.method_10264() || posTmp.method_10260() != pos.method_10260()) continue;
                try {
                    this.getProfiler().method_15405("render_batched");
                    RenderUtils.drawBlockBoxEdgeBatchedLines((class_2338)this.getChunkRelativePosition(pos), axis, corner, this.overlayColor, bufferOverlayOutlines);
                }
                catch (IllegalStateException ignored) {
                    this.getProfiler().method_15407();
                    return;
                }
                ++lines;
            }
        }
        this.getProfiler().method_15407();
    }

    protected OverlayType getOverlayType(class_2680 stateSchematic, class_2680 stateClient) {
        if (stateSchematic == stateClient) {
            return OverlayType.NONE;
        }
        boolean clientHasAir = stateClient.method_26215();
        boolean schematicHasAir = stateSchematic.method_26215();
        if (schematicHasAir) {
            if (clientHasAir) {
                return OverlayType.NONE;
            }
            if (this.ignoreClientWorldFluids && stateClient.method_51176()) {
                return OverlayType.NONE;
            }
            if (this.ignoreBlockRegistry.hasBlock(stateClient.method_26204())) {
                return OverlayType.NONE;
            }
            return OverlayType.EXTRA;
        }
        if (clientHasAir || this.ignoreClientWorldFluids && stateClient.method_51176()) {
            return OverlayType.MISSING;
        }
        if (stateSchematic.method_26204() != stateClient.method_26204()) {
            if (Configs.Generic.ENABLE_DIFFERENT_BLOCKS.getBooleanValue() && BlockUtils.isInSameGroup((class_2680)stateSchematic, (class_2680)stateClient)) {
                if (BlockUtils.matchPropertiesOnly((class_2680)stateSchematic, (class_2680)stateClient)) {
                    return OverlayType.DIFF_BLOCK;
                }
                return OverlayType.WRONG_STATE;
            }
            return OverlayType.WRONG_BLOCK;
        }
        return OverlayType.WRONG_STATE;
    }

    @Nullable
    protected static Color4f getOverlayColor(OverlayType overlayType) {
        Color4f overlayColor = null;
        switch (overlayType) {
            case MISSING: {
                if (!Configs.Visuals.SCHEMATIC_OVERLAY_TYPE_MISSING.getBooleanValue()) break;
                overlayColor = Configs.Colors.SCHEMATIC_OVERLAY_COLOR_MISSING.getColor();
                break;
            }
            case EXTRA: {
                if (!Configs.Visuals.SCHEMATIC_OVERLAY_TYPE_EXTRA.getBooleanValue()) break;
                overlayColor = Configs.Colors.SCHEMATIC_OVERLAY_COLOR_EXTRA.getColor();
                break;
            }
            case WRONG_BLOCK: {
                if (!Configs.Visuals.SCHEMATIC_OVERLAY_TYPE_WRONG_BLOCK.getBooleanValue()) break;
                overlayColor = Configs.Colors.SCHEMATIC_OVERLAY_COLOR_WRONG_BLOCK.getColor();
                break;
            }
            case WRONG_STATE: {
                if (!Configs.Visuals.SCHEMATIC_OVERLAY_TYPE_WRONG_STATE.getBooleanValue()) break;
                overlayColor = Configs.Colors.SCHEMATIC_OVERLAY_COLOR_WRONG_STATE.getColor();
                break;
            }
            case DIFF_BLOCK: {
                if (!Configs.Visuals.SCHEMATIC_OVERLAY_TYPE_DIFF_BLOCK.getBooleanValue()) break;
                overlayColor = Configs.Colors.SCHEMATIC_OVERLAY_COLOR_DIFF_BLOCK.getColor();
                break;
            }
        }
        return overlayColor;
    }

    private void addBlockEntity(class_2338 pos, ChunkRenderDataSchematic chunkRenderData, Set<class_2586> blockEntities) {
        class_827 tesr;
        class_2586 te = this.schematicWorldView.getBlockEntity(pos, class_2818.class_2819.field_12859);
        if (te != null && (tesr = class_310.method_1551().method_31975().method_3550(te)) != null) {
            chunkRenderData.addBlockEntity(te);
            if (tesr.method_3563(te)) {
                blockEntities.add(te);
            }
        }
    }

    private class_287 preRenderBlocks(class_1921 layer, @Nonnull BufferAllocatorCache allocators) {
        return this.builderCache.getBufferByLayer(layer, allocators);
    }

    private class_287 preRenderOverlay(OverlayRenderType type, @Nonnull BufferAllocatorCache allocators) {
        this.existingOverlays.add(type);
        this.hasOverlay = true;
        RenderSystem.setShader((class_10156)class_10142.field_53876);
        return this.builderCache.getBufferByOverlay(type, allocators);
    }

    protected void uploadBuiltBuffer(@Nonnull class_9801 builtBuffer, @Nonnull class_291 vertexBuffer) {
        if (vertexBuffer.method_43444()) {
            builtBuffer.close();
            return;
        }
        vertexBuffer.method_1353();
        vertexBuffer.method_1352(builtBuffer);
        class_291.method_1354();
    }

    private void postRenderBlocks(class_1921 layer, float x, float y, float z, @Nonnull ChunkRenderDataSchematic chunkRenderData, @Nonnull BufferAllocatorCache allocators) throws RuntimeException {
        if (!chunkRenderData.isBlockLayerEmpty(layer)) {
            class_9801 built;
            if (chunkRenderData.getBuiltBufferCache().hasBuiltBufferByLayer(layer)) {
                chunkRenderData.getBuiltBufferCache().getBuiltBufferByLayer(layer).close();
            }
            if (this.builderCache.hasBufferByLayer(layer)) {
                class_287 builder = this.builderCache.getBufferByLayer(layer, allocators);
                built = builder.method_60794();
                if (built == null) {
                    chunkRenderData.setBlockLayerUnused(layer);
                    return;
                }
            } else {
                chunkRenderData.setBlockLayerUnused(layer);
                return;
            }
            chunkRenderData.getBuiltBufferCache().storeBuiltBufferByLayer(layer, built);
            if (layer == class_1921.method_23583() && Configs.Visuals.RENDER_ENABLE_TRANSLUCENT_RESORTING.getBooleanValue()) {
                try {
                    this.resortRenderBlocks(layer, x, y, z, chunkRenderData, allocators);
                }
                catch (Exception e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
    }

    private void postRenderOverlay(OverlayRenderType type, float x, float y, float z, @Nonnull ChunkRenderDataSchematic chunkRenderData, @Nonnull BufferAllocatorCache allocators) throws RuntimeException {
        if (!chunkRenderData.isOverlayTypeEmpty(type)) {
            if (chunkRenderData.getBuiltBufferCache().hasBuiltBufferByType(type)) {
                chunkRenderData.getBuiltBufferCache().getBuiltBufferByType(type).close();
            }
            if (this.builderCache.hasBufferByOverlay(type)) {
                class_287 builder = this.builderCache.getBufferByOverlay(type, allocators);
                class_9801 built = builder.method_60794();
                if (built == null) {
                    chunkRenderData.setOverlayTypeUnused(type);
                    return;
                }
                chunkRenderData.getBuiltBufferCache().storeBuiltBufferByType(type, built);
            } else {
                chunkRenderData.setOverlayTypeUnused(type);
                return;
            }
        }
    }

    protected class_8251 createVertexSorter(float x, float y, float z) {
        return class_8251.method_49906((float)x, (float)y, (float)z);
    }

    protected class_8251 createVertexSorter(class_243 pos) {
        return class_8251.method_49906((float)((float)pos.method_10216()), (float)((float)pos.method_10214()), (float)((float)pos.method_10215()));
    }

    protected class_8251 createVertexSorter(class_243 pos, class_2338 origin) {
        return class_8251.method_49906((float)((float)(pos.field_1352 - (double)origin.method_10263())), (float)((float)(pos.field_1351 - (double)origin.method_10264())), (float)((float)(pos.field_1350 - (double)origin.method_10260())));
    }

    protected class_8251 createVertexSorter(class_4184 camera) {
        class_243 vec3d = camera.method_19326();
        return this.createVertexSorter(vec3d, this.getOrigin());
    }

    protected void uploadSortingState(@Nonnull class_9799.class_9800 result, @Nonnull class_291 vertexBuffer) {
        if (vertexBuffer.method_43444()) {
            result.close();
            return;
        }
        vertexBuffer.method_1353();
        vertexBuffer.method_60829(result);
        class_291.method_1354();
    }

    private void resortRenderBlocks(class_1921 layer, float x, float y, float z, @Nonnull ChunkRenderDataSchematic chunkRenderData, @Nonnull BufferAllocatorCache allocators) throws InterruptedException {
        if (!chunkRenderData.isBlockLayerEmpty(layer)) {
            class_9799 allocator = allocators.getBufferByLayer(layer);
            if (allocator == null) {
                chunkRenderData.setBlockLayerUnused(layer);
                return;
            }
            if (!chunkRenderData.getBuiltBufferCache().hasBuiltBufferByLayer(layer)) {
                chunkRenderData.setBlockLayerUnused(layer);
                return;
            }
            class_9801 built = chunkRenderData.getBuiltBufferCache().getBuiltBufferByLayer(layer);
            if (built == null) {
                chunkRenderData.setBlockLayerUnused(layer);
                return;
            }
            if (layer == class_1921.method_23583() && Configs.Visuals.RENDER_ENABLE_TRANSLUCENT_RESORTING.getBooleanValue()) {
                class_9801.class_9802 sortingData;
                class_8251 sorter = class_8251.method_49906((float)x, (float)y, (float)z);
                if (!chunkRenderData.hasTransparentSortingData()) {
                    sortingData = built.method_60819(allocator, sorter);
                    if (sortingData == null) {
                        throw new InterruptedException("Sort State failure");
                    }
                    chunkRenderData.setTransparentSortingData(sortingData);
                } else {
                    sortingData = chunkRenderData.getTransparentSortingData();
                }
                if (sortingData == null) {
                    throw new InterruptedException("Sorting Data failure");
                }
            }
        }
    }

    private void resortRenderOverlay(OverlayRenderType type, float x, float y, float z, @Nonnull ChunkRenderDataSchematic chunkRenderData, @Nonnull BufferAllocatorCache allocators) throws InterruptedException {
        if (!chunkRenderData.isOverlayTypeEmpty(type)) {
            class_9799 allocator = allocators.getBufferByOverlay(type);
            if (allocator == null) {
                chunkRenderData.setOverlayTypeUnused(type);
                return;
            }
            if (!chunkRenderData.getBuiltBufferCache().hasBuiltBufferByType(type)) {
                chunkRenderData.setOverlayTypeUnused(type);
                return;
            }
            class_9801 built = chunkRenderData.getBuiltBufferCache().getBuiltBufferByType(type);
            if (built == null) {
                chunkRenderData.setOverlayTypeUnused(type);
                return;
            }
        }
    }

    protected ChunkRenderTaskSchematic makeCompileTaskChunkSchematic(Supplier<class_243> cameraPosSupplier) {
        ChunkRenderTaskSchematic generator;
        this.chunkRenderLock.lock();
        try {
            this.finishCompileTask();
            this.rebuildWorldView();
            generator = this.compileTask = new ChunkRenderTaskSchematic(this, ChunkRenderTaskSchematic.Type.REBUILD_CHUNK, cameraPosSupplier, this.getDistanceSq());
        }
        finally {
            this.chunkRenderLock.unlock();
        }
        return generator;
    }

    @Nullable
    protected ChunkRenderTaskSchematic makeCompileTaskTransparencySchematic(Supplier<class_243> cameraPosSupplier) {
        this.chunkRenderLock.lock();
        try {
            if (this.compileTask == null || this.compileTask.getStatus() != ChunkRenderTaskSchematic.Status.PENDING) {
                if (this.compileTask != null && this.compileTask.getStatus() != ChunkRenderTaskSchematic.Status.DONE) {
                    this.compileTask.finish();
                }
                this.compileTask = new ChunkRenderTaskSchematic(this, ChunkRenderTaskSchematic.Type.RESORT_TRANSPARENCY, cameraPosSupplier, this.getDistanceSq());
                this.compileTask.setChunkRenderData(this.chunkRenderData);
                ChunkRenderTaskSchematic chunkRenderTaskSchematic = this.compileTask;
                return chunkRenderTaskSchematic;
            }
        }
        finally {
            this.chunkRenderLock.unlock();
        }
        return null;
    }

    protected void finishCompileTask() {
        this.chunkRenderLock.lock();
        try {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkRenderTaskSchematic.Status.DONE) {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally {
            this.chunkRenderLock.unlock();
        }
    }

    protected ReentrantLock getLockCompileTask() {
        return this.chunkRenderLock;
    }

    protected void clear() {
        try {
            this.finishCompileTask();
        }
        finally {
            if (this.chunkRenderData != null && !this.chunkRenderData.equals(ChunkRenderDataSchematic.EMPTY)) {
                this.chunkRenderData.clearAll();
            }
            this.builderCache.clearAll();
            this.chunkRenderData = ChunkRenderDataSchematic.EMPTY;
            this.existingOverlays.clear();
            this.hasOverlay = false;
        }
    }

    protected void setNeedsUpdate(boolean immediate) {
        if (this.needsUpdate) {
            immediate |= this.needsImmediateUpdate;
        }
        this.needsUpdate = true;
        this.needsImmediateUpdate = immediate;
    }

    protected void clearNeedsUpdate() {
        this.needsUpdate = false;
        this.needsImmediateUpdate = false;
    }

    protected boolean needsUpdate() {
        return this.needsUpdate;
    }

    protected boolean needsImmediateUpdate() {
        return this.needsUpdate && this.needsImmediateUpdate;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void rebuildWorldView() {
        List<IntBoundingBox> list = this.boxes;
        synchronized (list) {
            this.ignoreClientWorldFluids = Configs.Visuals.IGNORE_EXISTING_FLUIDS.getBooleanValue();
            this.ignoreBlockRegistry = new IgnoreBlockRegistry();
            class_638 worldClient = class_310.method_1551().field_1687;
            assert (worldClient != null);
            this.schematicWorldView = new ChunkCacheSchematic(this.world, worldClient, (class_2338)this.position, 2);
            this.clientWorldView = new ChunkCacheSchematic((class_1937)worldClient, worldClient, (class_2338)this.position, 2);
            this.boxes.clear();
            int chunkX = this.position.method_10263() / 16;
            int chunkZ = this.position.method_10260() / 16;
            for (SchematicPlacementManager.PlacementPart part : DataManager.getSchematicPlacementManager().getPlacementPartsInChunk(chunkX, chunkZ)) {
                this.boxes.add(part.bb);
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.deleteGlResources();
    }
}

