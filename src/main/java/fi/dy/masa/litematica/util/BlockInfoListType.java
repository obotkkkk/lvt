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

public enum BlockInfoListType implements IConfigOptionListEntry
{
    ALL("all", "litematica.gui.label.block_info_list_type.all"),
    RENDER_LAYERS("render_layers", "litematica.gui.label.block_info_list_type.render_layers");

    private final String configString;
    private final String translationKey;

    private BlockInfoListType(String configString, String translationKey) {
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
            if (++id >= BlockInfoListType.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = BlockInfoListType.values().length - 1;
        }
        return BlockInfoListType.values()[id % BlockInfoListType.values().length];
    }

    public BlockInfoListType fromString(String name) {
        return BlockInfoListType.fromStringStatic(name);
    }

    public static BlockInfoListType fromStringStatic(String name) {
        for (BlockInfoListType mode : BlockInfoListType.values()) {
            if (!mode.configString.equalsIgnoreCase(name)) continue;
            return mode;
        }
        return ALL;
    }
}

