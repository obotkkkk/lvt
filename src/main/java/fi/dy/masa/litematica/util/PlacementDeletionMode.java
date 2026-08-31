/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_3542
 *  net.minecraft.class_3542$class_7292
 */
package fi.dy.masa.litematica.util;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.class_3542;

public enum PlacementDeletionMode implements IConfigOptionListEntry,
class_3542
{
    MATCHING_BLOCK("matching_block", "litematica.gui.label.placement_deletion_mode.matching_block"),
    NON_MATCHING_BLOCK("non_matching_block", "litematica.gui.label.placement_deletion_mode.non_matching_block"),
    ANY_SCHEMATIC_BLOCK("any_schematic_block", "litematica.gui.label.placement_deletion_mode.any_schematic_block"),
    NO_SCHEMATIC_BLOCK("no_schematic_block", "litematica.gui.label.placement_deletion_mode.no_schematic_block"),
    ENTIRE_VOLUME("entire_volume", "litematica.gui.label.placement_deletion_mode.entire_volume");

    public static final class_3542.class_7292<PlacementDeletionMode> CODEC;
    public static final ImmutableList<PlacementDeletionMode> VALUES;
    private final String configString;
    private final String translationKey;

    private PlacementDeletionMode(String configString, String translationKey) {
        this.configString = configString;
        this.translationKey = translationKey;
    }

    public String method_15434() {
        return this.configString;
    }

    public String getStringValue() {
        return this.configString;
    }

    public String getDisplayName() {
        return StringUtils.translate((String)this.translationKey, (Object[])new Object[0]);
    }

    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            if (++id >= PlacementDeletionMode.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = PlacementDeletionMode.values().length - 1;
        }
        return PlacementDeletionMode.values()[id % PlacementDeletionMode.values().length];
    }

    public PlacementDeletionMode fromString(String name) {
        return PlacementDeletionMode.fromStringStatic(name);
    }

    public static PlacementDeletionMode fromStringStatic(String name) {
        for (PlacementDeletionMode val : PlacementDeletionMode.values()) {
            if (!val.configString.equalsIgnoreCase(name)) continue;
            return val;
        }
        return ENTIRE_VOLUME;
    }

    static {
        CODEC = class_3542.method_28140(PlacementDeletionMode::values);
        VALUES = ImmutableList.copyOf((Object[])PlacementDeletionMode.values());
    }
}

