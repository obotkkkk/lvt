/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.options.ConfigBoolean
 *  fi.dy.masa.malilib.config.options.ConfigInteger
 *  fi.dy.masa.malilib.config.options.ConfigString
 */
package fi.dy.masa.litematica.config;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigString;

public class AutoClickConfig {
    public static final ConfigBoolean ENABLE_AUTOCLICK = new ConfigBoolean("autoClickEnable", false, "B\u1eadt Auto Click (LVT)");
    public static final ConfigBoolean CLICK_LEFT = new ConfigBoolean("clickLeft", true, "Auto Click Chu\u1ed9t Tr\u00e1i");
    public static final ConfigBoolean CLICK_RIGHT = new ConfigBoolean("clickRight", false, "Auto Click Chu\u1ed9t Ph\u1ea3i");
    public static final ConfigInteger CLICK_DELAY = new ConfigInteger("clickDelay", 4, 2, 200, "\u0110\u1ed9 tr\u1ec5 click (Ticks)");
    public static final ConfigBoolean PRESS_EXTRA_KEY = new ConfigBoolean("pressExtraKey", false, "B\u1ea5m k\u00e8m ph\u00edm ph\u1ee5");
    public static final ConfigString EXTRA_KEY_NAME = new ConfigString("extraKeyName", "F", "T\u00ean ph\u00edm ph\u1ee5 (VD: F, R, SPACE, SHIFT)");
}

