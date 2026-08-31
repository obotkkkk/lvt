/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2415
 *  net.minecraft.class_2470
 *  net.minecraft.class_2487
 */
package fi.dy.masa.litematica.interfaces;

import com.google.gson.JsonObject;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2415;
import net.minecraft.class_2470;
import net.minecraft.class_2487;

public interface ISchematicPlacementEventListener {
    default public void onPlacementInit(SchematicPlacement placement) {
    }

    default public void onSubRegionInit(SubRegionPlacement subRegion) {
    }

    default public void onPlacementCreateFor(SchematicPlacement placement, LitematicaSchematic schematic, class_2338 origin, String name, boolean enabled, boolean enableRender) {
    }

    default public void onPlacementCreateForConversion(SchematicPlacement placement, LitematicaSchematic schematic, class_2338 origin) {
    }

    default public void onPlacementCreateFromJson(SchematicPlacement placement, LitematicaSchematic schematic, class_2338 origin, String name, class_2470 rotation, class_2415 mirror, boolean enabled, boolean enableRender, JsonObject obj) {
    }

    default public void onPlacementCreateFromNbt(SchematicPlacement placement, LitematicaSchematic schematic, class_2338 origin, String name, class_2470 rotation, class_2415 mirror, boolean enabled, boolean enableRender, class_2487 nbt) {
    }

    default public void onSavePlacementToJson(SchematicPlacement placement, JsonObject json) {
    }

    default public void onSavePlacementToNbt(SchematicPlacement placement, class_2487 nbt) {
    }

    default public void onSubRegionCreateFromJson(SubRegionPlacement subRegion, class_2338 origin, String name, class_2470 rotation, class_2415 mirror, boolean enabled, boolean enableRender, JsonObject obj) {
    }

    default public void onSaveSubRegionToJson(SubRegionPlacement subRegion, JsonObject json) {
    }

    default public void onToggleLocked(SchematicPlacement placement, boolean toggle) {
    }

    default public void onSetEnabled(SchematicPlacement placement, boolean toggle) {
    }

    default public void onSetRender(SchematicPlacement placement, boolean toggle) {
    }

    default public void onSetName(SchematicPlacement placement, String name) {
    }

    default public void onSetOrigin(SchematicPlacement placement, class_2338 origin) {
    }

    default public void onSetMirror(SchematicPlacement placement, class_2415 mirror) {
    }

    default public void onSetRotation(SchematicPlacement placement, class_2470 rotation) {
    }

    default public void onPlacementReset(SchematicPlacement placement) {
    }

    default public void onSetSubRegionEnabled(SubRegionPlacement subRegion, boolean toggle) {
    }

    default public void onSetSubRegionRender(SubRegionPlacement subRegion, boolean toggle) {
    }

    default public void onSetSubRegionOrigin(SubRegionPlacement subRegion, class_2338 origin) {
    }

    default public void onSetSubRegionMirror(SubRegionPlacement subRegion, class_2415 mirror) {
    }

    default public void onSetSubRegionRotation(SubRegionPlacement subRegion, class_2470 rotation) {
    }

    default public void onSubRegionReset(SubRegionPlacement subRegion) {
    }

    default public void onPlacementSelected(@Nullable SchematicPlacement prevPlacement, @Nullable SchematicPlacement selected) {
    }

    public void onPlacementAdded(SchematicPlacement var1);

    public void onPlacementRemoved(SchematicPlacement var1);

    default public void onPlacementUpdated(SchematicPlacement placement) {
    }
}

