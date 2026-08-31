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

public enum PasteNbtBehavior implements IConfigOptionListEntry,
class_3542
{
    NONE("none", "litematica.gui.label.paste_nbt_behavior.none"),
    PLACE_MODIFY("place_data_modify", "litematica.gui.label.paste_nbt_behavior.place_data_modify"),
    PLACE_CLONE("place_clone", "litematica.gui.label.paste_nbt_behavior.place_clone");

    public static final class_3542.class_7292<PasteNbtBehavior> CODEC;
    public static final ImmutableList<PasteNbtBehavior> VALUES;
    private final String configString;
    private final String translationKey;

    private PasteNbtBehavior(String configString, String translationKey) {
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
            if (++id >= PasteNbtBehavior.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = PasteNbtBehavior.values().length - 1;
        }
        return PasteNbtBehavior.values()[id % PasteNbtBehavior.values().length];
    }

    public PasteNbtBehavior fromString(String name) {
        return PasteNbtBehavior.fromStringStatic(name);
    }

    public static PasteNbtBehavior fromStringStatic(String name) {
        for (PasteNbtBehavior val : VALUES) {
            if (!val.configString.equalsIgnoreCase(name)) continue;
            return val;
        }
        return NONE;
    }

    static {
        CODEC = class_3542.method_28140(PasteNbtBehavior::values);
        VALUES = ImmutableList.copyOf((Object[])PasteNbtBehavior.values());
    }
}

