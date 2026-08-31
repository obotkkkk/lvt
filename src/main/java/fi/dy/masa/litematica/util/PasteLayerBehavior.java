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

public enum PasteLayerBehavior implements IConfigOptionListEntry,
class_3542
{
    ALL("all", "litematica.gui.label.paste_layer_behavior.all"),
    RENDERED_ONLY("rendered_only", "litematica.gui.label.paste_layer_behavior.rendered_only");

    public static final class_3542.class_7292<PasteLayerBehavior> CODEC;
    public static final ImmutableList<PasteLayerBehavior> VALUES;
    private final String configString;
    private final String translationKey;

    private PasteLayerBehavior(String configString, String translationKey) {
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
            if (++id >= PasteLayerBehavior.values().length) {
                id = 0;
            }
        } else if (--id < 0) {
            id = PasteLayerBehavior.values().length - 1;
        }
        return PasteLayerBehavior.values()[id % PasteLayerBehavior.values().length];
    }

    public PasteLayerBehavior fromString(String name) {
        return PasteLayerBehavior.fromStringStatic(name);
    }

    public static PasteLayerBehavior fromStringStatic(String name) {
        for (PasteLayerBehavior val : PasteLayerBehavior.values()) {
            if (!val.configString.equalsIgnoreCase(name)) continue;
            return val;
        }
        return ALL;
    }

    static {
        CODEC = class_3542.method_28140(PasteLayerBehavior::values);
        VALUES = ImmutableList.copyOf((Object[])PasteLayerBehavior.values());
    }
}

