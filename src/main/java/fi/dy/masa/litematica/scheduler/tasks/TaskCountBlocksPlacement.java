/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableCollection
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.ImmutableCollection;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.IMaterialList;
import fi.dy.masa.litematica.scheduler.tasks.TaskCountBlocksBase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.BlockInfoListType;
import java.util.Collection;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public class TaskCountBlocksPlacement
extends TaskCountBlocksBase {
    protected final SchematicPlacement schematicPlacement;
    protected final boolean ignoreState;

    public TaskCountBlocksPlacement(SchematicPlacement schematicPlacement, IMaterialList materialList) {
        this(schematicPlacement, materialList, false);
    }

    public TaskCountBlocksPlacement(SchematicPlacement schematicPlacement, IMaterialList materialList, boolean ignoreState) {
        super(materialList, "litematica.gui.label.task_name.material_list");
        this.schematicPlacement = schematicPlacement;
        this.ignoreState = ignoreState;
        ImmutableCollection boxes = schematicPlacement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED).values();
        if (materialList.getMaterialListType() == BlockInfoListType.RENDER_LAYERS) {
            this.addPerChunkBoxes((Collection<Box>)boxes, DataManager.getRenderLayerRange());
        } else {
            this.addPerChunkBoxes((Collection<Box>)boxes);
        }
    }

    @Override
    public boolean canExecute() {
        return super.canExecute() && this.schematicWorld != null;
    }

    @Override
    protected void countAtPosition(class_2338 pos) {
        class_2680 stateSchematic = this.schematicWorld.method_8320(pos);
        if (!stateSchematic.method_26215()) {
            class_2680 stateClient = this.clientWorld.method_8320(pos);
            this.countsTotal.addTo((Object)stateSchematic, 1);
            if (stateClient.method_26215()) {
                this.countsMissing.addTo((Object)stateSchematic, 1);
            } else if (!(stateClient == stateSchematic || this.ignoreState && stateClient.method_26204() == stateSchematic.method_26204())) {
                this.countsMissing.addTo((Object)stateSchematic, 1);
                this.countsMismatch.addTo((Object)stateSchematic, 1);
            }
        }
    }
}

