/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.util.StringUtils
 */
package fi.dy.masa.litematica.selection;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum CornerSelectionMode implements IConfigOptionListEntry
{
    CORNERS("corners", "litematica.hud.area_selection.mode.corners"),
    EXPAND("expand", "litematica.hud.area_selection.mode.expand");

    private final String configString;
    private final String translationKey;

    private CornerSelectionMode(String configString, String translationKey) {
        this.configString = configString;
        this.translationKey = translationKey;
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
            if (++id >= CornerSelectionMode.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = CornerSelectionMode.values().length - 1;
        }
        return CornerSelectionMode.values()[id % CornerSelectionMode.values().length];
    }

    public CornerSelectionMode fromString(String name) {
        return CornerSelectionMode.fromStringStatic(name);
    }

    public static CornerSelectionMode fromStringStatic(String name) {
        for (CornerSelectionMode mode : CornerSelectionMode.values()) {
            if (!mode.configString.equalsIgnoreCase(name)) continue;
            return mode;
        }
        return CORNERS;
    }
}

