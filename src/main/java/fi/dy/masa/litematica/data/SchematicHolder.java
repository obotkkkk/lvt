/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package fi.dy.masa.litematica.data;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.util.FileType;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public class SchematicHolder {
    private static final SchematicHolder INSTANCE = new SchematicHolder();
    private final List<LitematicaSchematic> schematics = new ArrayList<LitematicaSchematic>();

    public static SchematicHolder getInstance() {
        return INSTANCE;
    }

    public void clearLoadedSchematics() {
        this.schematics.clear();
    }

    @Nullable
    public LitematicaSchematic getOrLoad(File file) {
        LitematicaSchematic schematic2;
        for (LitematicaSchematic schematic2 : this.schematics) {
            if (!file.equals(schematic2.getFile())) continue;
            return schematic2;
        }
        FileType type = FileType.fromFile(file);
        schematic2 = LitematicaSchematic.createFromFile(file.getParentFile(), file.getName(), type);
        if (schematic2 != null) {
            this.schematics.add(schematic2);
        }
        return schematic2;
    }

    public void addSchematic(LitematicaSchematic schematic, boolean allowDuplicates) {
        if (allowDuplicates || !this.schematics.contains(schematic)) {
            if (!allowDuplicates && schematic.getFile() != null) {
                for (LitematicaSchematic tmp : this.schematics) {
                    if (!schematic.getFile().equals(tmp.getFile())) continue;
                    return;
                }
            }
            this.schematics.add(schematic);
        }
    }

    public boolean removeSchematic(LitematicaSchematic schematic) {
        if (this.schematics.remove(schematic)) {
            DataManager.getSchematicPlacementManager().removeAllPlacementsOfSchematic(schematic);
            return true;
        }
        return false;
    }

    public Collection<LitematicaSchematic> getAllSchematics() {
        return this.schematics;
    }
}

