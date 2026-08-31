/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 */
package com.lvt.guard;

import fi.dy.masa.litematica.config.GuardConfig;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_310;

public class GuardManager {
    private static final Set<String> WHITELIST = new HashSet<String>(Arrays.asList("jokhehe", "gamehayzl", "Rayz", "soniV_evoL", "chanhhh_", "LVT_Ngoai_Mon", "a11lalusucvat", "Cloudy", "Tysdayne", "Yuihara", "BaalethVinos", "CongMinh", "long19052010", "LVT_minertitan", "mk_long"));
    private static final LocalDateTime EXPIRY_DATE = LocalDateTime.of(2090, 6, 30, 23, 59);
    private static final String MASTER_PASSWORD = "LVT_SECRET_2026";

    public static boolean canUseAddons() {
        class_310 client = class_310.method_1551();
        if (client.field_1724 == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(EXPIRY_DATE)) {
            return false;
        }
        String username = client.method_1548().method_1676();
        if (!WHITELIST.contains(username)) {
            return false;
        }
        String inputPass = GuardConfig.ACCESS_PASSWORD.getStringValue();
        return "moon".equals(inputPass);
    }
}

