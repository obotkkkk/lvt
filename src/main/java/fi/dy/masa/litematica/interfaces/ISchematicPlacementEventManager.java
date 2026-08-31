/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.class_2338
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 */
package fi.dy.masa.litematica.interfaces;

import fi.dy.masa.litematica.interfaces.ISchematicPlacementEventListener;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementEventFlag;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.class_2338;
import net.minecraft.class_2415;
import net.minecraft.class_2470;

public interface ISchematicPlacementEventManager {
    public void registerSchematicPlacementEventListener(@Nonnull ISchematicPlacementEventListener var1, @Nonnull List<SchematicPlacementEventFlag> var2);

    public void invokePrePlacementChange(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SchematicPlacement var2);

    public void invokePostPlacementChange(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SchematicPlacement var2);

    public void invokePlacementModified(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SchematicPlacement var2);

    public void invokeSetSubRegionEnabled(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SubRegionPlacement var2, boolean var3);

    public void invokeSetSubRegionOrigin(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SubRegionPlacement var2, class_2338 var3);

    public void invokeSetSubRegionMirror(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SubRegionPlacement var2, class_2415 var3);

    public void invokeSetSubRegionRotation(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SubRegionPlacement var2, class_2470 var3);

    public void invokeSubRegionModified(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SchematicPlacement var2, @Nonnull String var3);

    public void invokeResetSubRegion(@Nonnull ISchematicPlacementEventListener var1, @Nonnull SubRegionPlacement var2);
}

