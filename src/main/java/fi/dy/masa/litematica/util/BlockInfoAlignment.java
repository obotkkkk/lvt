/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.util.StringUtils
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum BlockInfoAlignment implements IConfigOptionListEntry
{
    CENTER("center", "litematica.label.alignment.center"),
    TOP_CENTER("top_center", "litematica.label.alignment.top_center");

    private final String configString;
    private final String unlocName;

    private BlockInfoAlignment(String configString, String unlocName) {
        this.configString = configString;
        this.unlocName = unlocName;
    }

    public String getStringValue() {
        return this.configString;
    }

    public String getDisplayName() {
        return StringUtils.translate((String)this.unlocName, (Object[])new Object[0]);
    }

    public IConfigOptionListEntry cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            if (++id >= BlockInfoAlignment.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = BlockInfoAlignment.values().length - 1;
        }
        return BlockInfoAlignment.values()[id % BlockInfoAlignment.values().length];
    }

    public BlockInfoAlignment fromString(String name) {
        return BlockInfoAlignment.fromStringStatic(name);
    }

    public static BlockInfoAlignment fromStringStatic(String name) {
        for (BlockInfoAlignment aligment : BlockInfoAlignment.values()) {
            if (!aligment.configString.equalsIgnoreCase(name)) continue;
            return aligment;
        }
        return CENTER;
    }
}

