/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IRangeChangeListener
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  net.minecraft.class_1657
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.scheduler.tasks;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.IMaterialList;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkBase;
import fi.dy.masa.litematica.util.BlockInfoListType;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import fi.dy.masa.malilib.interfaces.IRangeChangeListener;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;

public abstract class TaskCountBlocksBase
extends TaskProcessChunkBase {
    protected final Object2IntOpenHashMap<class_2680> countsTotal = new Object2IntOpenHashMap();
    protected final Object2IntOpenHashMap<class_2680> countsMissing = new Object2IntOpenHashMap();
    protected final Object2IntOpenHashMap<class_2680> countsMismatch = new Object2IntOpenHashMap();
    protected final IMaterialList materialList;
    protected final LayerRange layerRange;

    protected TaskCountBlocksBase(IMaterialList materialList, String nameOnHud) {
        super(nameOnHud);
        this.materialList = materialList;
        this.layerRange = materialList.getMaterialListType() == BlockInfoListType.ALL ? new LayerRange((IRangeChangeListener)SchematicWorldRefresher.INSTANCE) : DataManager.getRenderLayerRange();
    }

    @Override
    protected boolean canProcessChunk(class_1923 pos) {
        return this.areSurroundingChunksLoaded(pos, this.clientWorld, 0);
    }

    @Override
    protected boolean processChunk(class_1923 pos) {
        this.countBlocksInChunk(pos);
        return true;
    }

    protected void countBlocksInChunk(class_1923 pos) {
        LayerRange range = this.layerRange;
        class_2350.class_2351 axis = range.getAxis();
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        for (IntBoundingBox bb : this.getBoxesInChunk(pos)) {
            int startX = axis == class_2350.class_2351.field_11048 ? Math.max(bb.minX, range.getLayerMin()) : bb.minX;
            int startY = axis == class_2350.class_2351.field_11052 ? Math.max(bb.minY, range.getLayerMin()) : bb.minY;
            int startZ = axis == class_2350.class_2351.field_11051 ? Math.max(bb.minZ, range.getLayerMin()) : bb.minZ;
            int endX = axis == class_2350.class_2351.field_11048 ? Math.min(bb.maxX, range.getLayerMax()) : bb.maxX;
            int endY = axis == class_2350.class_2351.field_11052 ? Math.min(bb.maxY, range.getLayerMax()) : bb.maxY;
            int endZ = axis == class_2350.class_2351.field_11051 ? Math.min(bb.maxZ, range.getLayerMax()) : bb.maxZ;
            for (int y = startY; y <= endY; ++y) {
                for (int z = startZ; z <= endZ; ++z) {
                    for (int x = startX; x <= endX; ++x) {
                        posMutable.method_10103(x, y, z);
                        this.countAtPosition((class_2338)posMutable);
                    }
                }
            }
        }
    }

    protected abstract void countAtPosition(class_2338 var1);

    @Override
    protected void onStop() {
        if (this.finished && this.isInWorld()) {
            List<MaterialListEntry> list = MaterialListUtils.getMaterialList(this.countsTotal, this.countsMissing, this.countsMismatch, (class_1657)this.mc.field_1724);
            this.materialList.setMaterialListEntries(list);
        }
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        super.onStop();
    }
}

