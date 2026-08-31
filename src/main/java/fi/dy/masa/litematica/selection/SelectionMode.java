/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.serialization.Codec
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nonnull
 *  net.minecraft.class_3542
 *  net.minecraft.class_3542$class_7292
 */
package fi.dy.masa.litematica.selection;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import javax.annotation.Nonnull;
import net.minecraft.class_3542;

public enum SelectionMode implements IConfigOptionListEntry,
class_3542
{
    NORMAL("normal", "litematica.gui.label.area_selection.mode.normal"),
    SIMPLE("simple", "litematica.gui.label.area_selection.mode.simple");

    public static final class_3542.class_7292<SelectionMode> CODEC;
    public static final ImmutableList<SelectionMode> VALUES;
    private final String configString;
    private final String translationKey;

    private SelectionMode(String configName, String translationKey) {
        this.configString = configName;
        this.translationKey = translationKey;
    }

    public Codec<SelectionMode> codec() {
        return CODEC;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public String getStringValue() {
        return this.configString;
    }

    public String getDisplayName() {
        return StringUtils.translate((String)this.translationKey, (Object[])new Object[0]);
    }

    public SelectionMode cycle(boolean forward) {
        int id = this.ordinal();
        if (forward) {
            if (++id >= SelectionMode.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = SelectionMode.values().length - 1;
        }
        return SelectionMode.values()[id % SelectionMode.values().length];
    }

    public SelectionMode fromString(String name) {
        return SelectionMode.fromStringStatic(name);
    }

    public static SelectionMode fromStringStatic(String name) {
        for (SelectionMode mode : SelectionMode.values()) {
            if (!mode.name().equalsIgnoreCase(name)) continue;
            return mode;
        }
        return NORMAL;
    }

    @Nonnull
    public String method_15434() {
        return this.configString;
    }

    static {
        CODEC = class_3542.method_28140(SelectionMode::values);
        VALUES = ImmutableList.copyOf((Object[])SelectionMode.values());
    }
}

