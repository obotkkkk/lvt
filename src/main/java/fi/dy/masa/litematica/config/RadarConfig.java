/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.config.options.ConfigBoolean
 *  fi.dy.masa.malilib.config.options.ConfigOptionList
 */
package fi.dy.masa.litematica.config;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

public class RadarConfig {
    public static final ConfigBoolean RADAR_ENABLED = new ConfigBoolean("radarEnabled", false, "B\u1eadt radar qu\u00e9t linh th\u1ea3o (L\u1ea1c V\u00e2n T\u00f4ng)");
    public static final ConfigOptionList RADAR_FOCUS = new ConfigOptionList("radarFocus", (IConfigOptionListEntry)RadarFocusType.DIA, "Ch\u1ecdn b\u1eadc linh th\u1ea3o mu\u1ed1n radar nh\u1eafm t\u1edbi");

    public static enum RadarFocusType implements IConfigOptionListEntry
    {
        HA("\u029c\u1ea1", "B\u1eadc H\u1ea1"),
        LINH("\u029f\u026a\u0274\u029c", "B\u1eadc Linh"),
        DIA("\u0111\u1ecb\u1d00", "B\u1eadc \u0110\u1ecba"),
        THIEN("\u1d1b\u029c\u026a\u00ea\u0274", "B\u1eadc Thi\u00ean"),
        THAN("\u1d1b\u029c\u1ea7\u0274", "B\u1eadc Th\u1ea7n");

        private final String label;
        private final String comment;

        private RadarFocusType(String label, String comment) {
            this.label = label;
            this.comment = comment;
        }

        public String getStringValue() {
            return this.label;
        }

        public String getDisplayName() {
            return this.label;
        }

        public IConfigOptionListEntry cycle(boolean forward) {
            int id = this.ordinal();
            if (forward) {
                return RadarFocusType.values()[(id + 1) % RadarFocusType.values().length];
            }
            return RadarFocusType.values()[(id - 1 + RadarFocusType.values().length) % RadarFocusType.values().length];
        }

        public IConfigOptionListEntry fromString(String value) {
            for (RadarFocusType type : RadarFocusType.values()) {
                if (!type.label.equalsIgnoreCase(value)) continue;
                return type;
            }
            return DIA;
        }
    }
}

