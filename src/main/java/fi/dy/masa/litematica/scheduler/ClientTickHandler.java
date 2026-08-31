/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IClientTickHandler
 *  fi.dy.masa.malilib.util.EntityUtils
 *  net.minecraft.class_1297
 *  net.minecraft.class_310
 */
package fi.dy.masa.litematica.scheduler;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import fi.dy.masa.malilib.util.EntityUtils;
import net.minecraft.class_1297;
import net.minecraft.class_310;

public class ClientTickHandler
implements IClientTickHandler {
    public void onClientTick(class_310 mc) {
        if (mc.field_1687 != null && mc.field_1724 != null) {
            SelectionManager sm = DataManager.getSelectionManager();
            if (sm.hasGrabbedElement()) {
                sm.moveGrabbedElement((class_1297)mc.field_1724);
            }
            if (mc.field_1755 == null) {
                WorldUtils.easyPlaceOnUseTick(mc);
            }
            if (Configs.Generic.LAYER_MODE_DYNAMIC.getBooleanValue()) {
                DataManager.getRenderLayerRange().setSingleBoundaryToPosition(EntityUtils.getCameraEntity());
            }
            DataManager.getSchematicPlacementManager().processQueuedChunks();
            TaskScheduler.getInstanceClient().runTasks();
        }
    }
}

