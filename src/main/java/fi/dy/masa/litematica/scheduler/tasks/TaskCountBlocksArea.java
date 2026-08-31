/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.scheduler.tasks;

import fi.dy.masa.litematica.materials.IMaterialList;
import fi.dy.masa.litematica.scheduler.tasks.TaskCountBlocksBase;
import fi.dy.masa.litematica.selection.AreaSelection;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public class TaskCountBlocksArea
extends TaskCountBlocksBase {
    public TaskCountBlocksArea(AreaSelection selection, IMaterialList materialList) {
        super(materialList, "litematica.gui.label.task_name.area_analyzer");
        this.addPerChunkBoxes(selection.getAllSubRegionBoxes());
    }

    @Override
    protected void countAtPosition(class_2338 pos) {
        class_2680 stateClient = this.clientWorld.method_8320(pos);
        this.countsTotal.addTo((Object)stateClient, 1);
    }
}

