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

public enum ReplaceBehavior implements IConfigOptionListEntry,
class_3542
{
    NONE("none", "litematica.gui.label.replace_behavior.none"),
    ALL("all", "litematica.gui.label.replace_behavior.all"),
    WITH_NON_AIR("with_non_air", "litematica.gui.label.replace_behavior.with_non_air");

    public static final class_3542.class_7292<ReplaceBehavior> CODEC;
    public static final ImmutableList<ReplaceBehavior> VALUES;
    private final String configString;
    private final String translationKey;

    private ReplaceBehavior(String configString, String translationKey) {
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
            if (++id >= ReplaceBehavior.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = ReplaceBehavior.values().length - 1;
        }
        return ReplaceBehavior.values()[id % ReplaceBehavior.values().length];
    }

    public ReplaceBehavior fromString(String name) {
        return ReplaceBehavior.fromStringStatic(name);
    }

    public static ReplaceBehavior fromStringStatic(String name) {
        for (ReplaceBehavior val : VALUES) {
            if (!val.configString.equalsIgnoreCase(name)) continue;
            return val;
        }
        return NONE;
    }

    static {
        CODEC = class_3542.method_28140(ReplaceBehavior::values);
        VALUES = ImmutableList.copyOf((Object[])ReplaceBehavior.values());
    }
}

