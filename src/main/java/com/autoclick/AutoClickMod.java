/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 *  net.minecraft.class_3675$class_306
 *  net.minecraft.class_3675$class_307
 */
package com.autoclick;

import com.lvt.guard.GuardManager;
import fi.dy.masa.litematica.config.AutoClickConfig;
import fi.dy.masa.litematica.mixin.IMinecraftClientAccessor;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3675;

public class AutoClickMod {
    private static int tickTimer = 0;
    private static int startDelayTicks = -1;

    public static void onTick(class_310 client) {
        if (client.field_1724 == null || client.field_1761 == null) {
            return;
        }
        if (!GuardManager.canUseAddons() || !AutoClickConfig.ENABLE_AUTOCLICK.getBooleanValue()) {
            tickTimer = 0;
            startDelayTicks = -1;
            return;
        }
        if (startDelayTicks == -1) {
            startDelayTicks = 100;
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7e[LVT] \u0110\u00e3 b\u1eadt Auto Click. B\u1eaft \u0111\u1ea7u sau 5s..."), true);
        }
        if (startDelayTicks > 0) {
            if (--startDelayTicks % 20 == 0 && startDelayTicks > 0) {
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e[LVT] Auto Click \u0111\u1ebfm ng\u01b0\u1ee3c: " + startDelayTicks / 20 + "s")), true);
            } else if (startDelayTicks == 0) {
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7a\u2714 [LVT] Auto Click \u0111ang ho\u1ea1t \u0111\u1ed9ng!"), true);
            }
            return;
        }
        int delay = AutoClickConfig.CLICK_DELAY.getIntegerValue();
        if (delay < 2) {
            delay = 2;
        }
        if (++tickTimer == 1) {
            AutoClickMod.executeClickActions(client);
        }
        if (tickTimer >= delay) {
            tickTimer = 0;
        }
    }

    private static void executeClickActions(class_310 client) {
        IMinecraftClientAccessor accessor = (IMinecraftClientAccessor)client;
        if (AutoClickConfig.CLICK_LEFT.getBooleanValue()) {
            client.execute(accessor::invokeDoAttack);
        }
        if (AutoClickConfig.CLICK_RIGHT.getBooleanValue()) {
            client.execute(accessor::invokeDoItemUse);
        }
        if (AutoClickConfig.PRESS_EXTRA_KEY.getBooleanValue()) {
            AutoClickMod.handleExtraKeyPress(client);
        }
    }

    private static void handleExtraKeyPress(class_310 client) {
        String[] keyStrings;
        String rawKeys = AutoClickConfig.EXTRA_KEY_NAME.getStringValue().toUpperCase().trim();
        for (String keyStr : keyStrings = rawKeys.split("[,\\s]+")) {
            int keyCode;
            if (keyStr.isEmpty()) continue;
            switch (keyStr) {
                case "SPACE": {
                    int n = 32;
                    break;
                }
                case "SHIFT": {
                    int n = 340;
                    break;
                }
                case "CTRL": {
                    int n = 341;
                    break;
                }
                case "ALT": {
                    int n = 342;
                    break;
                }
                case "TAB": {
                    int n = 258;
                    break;
                }
                default: {
                    int n = keyCode = keyStr.length() == 1 ? (int)keyStr.charAt(0) : -1;
                }
            }
            if (keyCode == -1) continue;
            class_3675.class_306 key = class_3675.class_307.field_1668.method_1447(keyCode);
            class_304.method_1416((class_3675.class_306)key, (boolean)true);
            class_304.method_1420((class_3675.class_306)key);
            client.execute(() -> class_304.method_1416((class_3675.class_306)key, (boolean)false));
        }
    }
}

