/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1657
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.tool;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.tool.ToolModeData;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.malilib.util.StringUtils;
import javax.annotation.Nullable;
import net.minecraft.class_1657;
import net.minecraft.class_2680;

public enum ToolMode {
    AREA_SELECTION("litematica.tool_mode.name.area_selection", false, false),
    SCHEMATIC_PLACEMENT("litematica.tool_mode.name.schematic_placement", false, true),
    FILL("litematica.tool_mode.name.fill", true, false, true, false),
    REPLACE_BLOCK("litematica.tool_mode.name.replace_block", true, false, true, true),
    PASTE_SCHEMATIC("litematica.tool_mode.name.paste_schematic", true, true),
    GRID_PASTE("litematica.tool_mode.name.grid_paste", true, true),
    MOVE("litematica.tool_mode.name.move", true, false),
    DELETE("litematica.tool_mode.name.delete", true, false),
    REBUILD("litematica.tool_mode.name.rebuild", false, true, true, false);

    private final String unlocName;
    private final boolean creativeOnly;
    private final boolean usesSchematic;
    private final boolean usesBlockPrimary;
    private final boolean usesBlockSecondary;
    @Nullable
    private class_2680 blockPrimary;
    @Nullable
    private class_2680 blockSecondary;

    private ToolMode(String unlocName, boolean creativeOnly, boolean usesSchematic) {
        this(unlocName, creativeOnly, usesSchematic, false, false);
    }

    private ToolMode(String unlocName, boolean creativeOnly, boolean usesSchematic, boolean usesBlockPrimary, boolean usesBlockSecondary) {
        this.unlocName = unlocName;
        this.creativeOnly = creativeOnly;
        this.usesSchematic = usesSchematic;
        this.usesBlockPrimary = usesBlockPrimary;
        this.usesBlockSecondary = usesBlockSecondary;
    }

    public boolean getUsesSchematic() {
        if (this == DELETE && ToolModeData.DELETE.getUsePlacement()) {
            return true;
        }
        return this.usesSchematic;
    }

    public boolean getUsesAreaSelection() {
        return !this.getUsesSchematic() || DataManager.getSchematicProjectsManager().hasProjectOpen();
    }

    public boolean getUsesBlockPrimary() {
        return this.usesBlockPrimary;
    }

    public boolean getUsesBlockSecondary() {
        return this.usesBlockSecondary;
    }

    @Nullable
    public class_2680 getPrimaryBlock() {
        return this.blockPrimary;
    }

    @Nullable
    public class_2680 getSecondaryBlock() {
        return this.blockSecondary;
    }

    public void setPrimaryBlock(@Nullable class_2680 state) {
        this.blockPrimary = state;
    }

    public void setSecondaryBlock(@Nullable class_2680 state) {
        this.blockSecondary = state;
    }

    public String getName() {
        return StringUtils.translate((String)this.unlocName, (Object[])new Object[0]);
    }

    public ToolMode cycle(class_1657 player, boolean forward) {
        ToolMode[] values = ToolMode.values();
        boolean isCreative = EntityUtils.isCreativeMode(player);
        int numModes = values.length;
        int inc = forward ? 1 : -1;
        int nextId = this.ordinal() + inc;
        for (int i = 0; i < numModes; ++i) {
            if (nextId < 0) {
                nextId = numModes - 1;
            } else if (nextId >= numModes) {
                nextId = 0;
            }
            ToolMode mode = values[nextId];
            if (isCreative || !mode.creativeOnly) {
                return mode;
            }
            nextId += inc;
        }
        return this;
    }
}

