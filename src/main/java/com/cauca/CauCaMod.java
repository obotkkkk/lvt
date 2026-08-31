/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 */
package com.cauca;

import com.cauca.FishingData;
import com.cauca.PIDController;
import com.lvt.guard.GuardManager;
import fi.dy.masa.litematica.config.CauCaConfig;
import fi.dy.masa.litematica.mixin.InGameHudAccessor;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_310;

public class CauCaMod {
    private static final PIDController pid = new PIDController(5.0, 0.05, 2.0);
    private static String state = "IDLE";
    private static long stateTimer = 0L;
    private static long lastBarTime = 0L;
    private static float lastProgress = -1.0f;
    private static long lastProgressTime = 0L;
    private static int startDelayTicks = -1;
    private static long lastActiveFishingTime = 0L;
    private static final long MAX_IDLE_TIMEOUT_MS = 60000L;
    private static long lastHardResetTime = 0L;
    private static final long HARD_RESET_INTERVAL_MS = 1800000L;

    public static void onTick(class_310 client) {
        long now;
        if (client.field_1724 == null) {
            return;
        }
        if (!GuardManager.canUseAddons()) {
            if (!state.equals("IDLE")) {
                CauCaMod.stopFishing(client);
            }
            startDelayTicks = -1;
            return;
        }
        if (!CauCaConfig.AUTO_FISH_ENABLED.getBooleanValue()) {
            if (!state.equals("IDLE")) {
                CauCaMod.stopFishing(client);
            }
            startDelayTicks = -1;
            return;
        }
        if (startDelayTicks == -1) {
            startDelayTicks = 200;
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7e[LVT] \u0110\u00e3 x\u00e1c th\u1ef1c. Ch\u1edd 10s \u0111\u1ec3 kh\u1edfi \u0111\u1ed9ng addon..."), true);
            lastActiveFishingTime = now = System.currentTimeMillis();
            lastHardResetTime = now;
        }
        if (startDelayTicks > 0) {
            if (--startDelayTicks % 20 == 0) {
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e[LVT] B\u1eaft \u0111\u1ea7u c\u00e2u c\u00e1 sau: " + startDelayTicks / 20 + "s")), true);
            }
            return;
        }
        now = System.currentTimeMillis();
        if (!state.equals("HARD_RESET_WAIT")) {
            if (now - lastHardResetTime > 1800000L) {
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7d[LVT] \u0110\u00e3 ho\u1ea1t \u0111\u1ed9ng 30 ph\u00fat, ti\u1ebfn h\u00e0nh thu c\u1ea7n v\u00e0 ch\u1edd 10s \u0111\u1ec3 ki\u1ec3m tra an to\u00e0n..."), true);
                if (client.field_1724.field_7513 != null) {
                    CauCaMod.selectFishingRod(client);
                    client.field_1761.method_2919((class_1657)client.field_1724, class_1268.field_5808);
                }
                state = "HARD_RESET_WAIT";
                stateTimer = now;
                client.field_1690.field_1832.method_23481(false);
                return;
            }
        } else {
            if (now - stateTimer > 10000L) {
                if (client.field_1724.field_7513 != null) {
                    CauCaMod.selectFishingRod(client);
                    client.field_1761.method_2919((class_1657)client.field_1724, class_1268.field_5808);
                    client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[LVT] Server lag, ph\u00e1t hi\u1ec7n phao \u1ea3o ch\u01b0a thu h\u1ebft, \u00e9p thu l\u1ea7n cu\u1ed1i!"), true);
                }
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7a[LVT] Reset 30 ph\u00fat ho\u00e0n t\u1ea5t. Kh\u1edfi \u0111\u1ed9ng l\u1ea1i Bot t\u1eeb \u0111\u1ea7u!"), true);
                CauCaMod.stopFishing(client);
                startDelayTicks = -1;
                return;
            }
            return;
        }
        CauCaMod.parseActionHud(client);
        if (state.equals("FISHING") || state.equals("WAITING_BITE")) {
            lastActiveFishingTime = now;
        } else if (now - lastActiveFishingTime > 60000L) {
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c[LVT] Ph\u00e1t hi\u1ec7n k\u1eb9t kh\u00f4ng qu\u0103ng c\u1ea7n qu\u00e1 60s! \u0110ang qu\u0103ng l\u1ea1i ngay..."), true);
            if (client.field_1724.field_7513 != null) {
                CauCaMod.selectFishingRod(client);
                client.field_1761.method_2919((class_1657)client.field_1724, class_1268.field_5808);
            }
            state = "PREPARE";
            stateTimer = now;
            lastActiveFishingTime = now;
        }
        if (state.equals("FISHING")) {
            CauCaMod.handleFishingMovement(client);
            if (FishingData.progress >= 100.0f) {
                CauCaMod.stopFishing(client);
            }
        } else {
            client.field_1690.field_1832.method_23481(false);
        }
        CauCaMod.handleSafetyChecks(client, now);
        CauCaMod.handleStateCycle(client, now);
    }

    private static void handleFishingMovement(class_310 client) {
        boolean isRecentlyActive;
        long now = System.currentTimeMillis();
        boolean bl = isRecentlyActive = FishingData.active || now - lastBarTime < 500L;
        if (isRecentlyActive) {
            double error = FishingData.barX - FishingData.dotX;
            if (error > 2.0) {
                client.field_1690.field_1832.method_23481(true);
            } else if (error < -2.0) {
                client.field_1690.field_1832.method_23481(false);
            } else {
                double pwr = pid.calculate(FishingData.barX, FishingData.dotX);
                client.field_1690.field_1832.method_23481(pwr > 0.0);
            }
        } else {
            client.field_1690.field_1832.method_23481(false);
        }
    }

    private static void handleSafetyChecks(class_310 client, long now) {
        if (state.equals("FISHING")) {
            if (FishingData.progress >= 95.0f) {
                if (FishingData.progress == lastProgress) {
                    if (now - lastProgressTime > 5000L) {
                        client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7e[LVT] Lag ti\u1ebfn \u0111\u1ed9, \u0111ang \u0111\u1ee3i th\u00eam..."), true);
                        lastProgressTime = now;
                    }
                } else {
                    lastProgress = FishingData.progress;
                    lastProgressTime = now;
                }
            }
            if (client.field_1724.field_7513 == null && now - stateTimer > 2000L) {
                CauCaMod.stopFishing(client);
            }
        }
    }

    private static void handleStateCycle(class_310 client, long now) {
        if (state.equals("IDLE") && now - stateTimer > 2000L) {
            state = "PREPARE";
            stateTimer = now;
        }
        if (state.equals("PREPARE")) {
            CauCaMod.selectFishingRod(client);
            if (client.field_1724.field_7513 != null) {
                client.field_1761.method_2919((class_1657)client.field_1724, class_1268.field_5808);
            }
            state = "BAITING";
            stateTimer = now;
        }
        if (state.equals("BAITING") && now - stateTimer > 500L) {
            CauCaMod.autoBait(client);
            state = "CASTING";
            stateTimer = now;
        }
        if (state.equals("CASTING") && now - stateTimer > 1000L) {
            CauCaMod.selectFishingRod(client);
            if (client.field_1724.field_7513 == null) {
                client.field_1761.method_2919((class_1657)client.field_1724, class_1268.field_5808);
            }
            state = "WAITING_BITE";
            stateTimer = now;
        }
    }

    public static void onChatMessage(class_2561 message) {
        if (!GuardManager.canUseAddons() || !CauCaConfig.AUTO_FISH_ENABLED.getBooleanValue()) {
            return;
        }
        if (message.getString().contains("\u1d04\u1ea7\u0274 \u1d18\u029c\u1ea3\u026a \u0262\u1eaf\u0274 \u1d0d\u1ed3i")) {
            state = "PREPARE";
            stateTimer = System.currentTimeMillis();
        }
    }

    private static void parseActionHud(class_310 client) {
        InGameHudAccessor hud = (InGameHudAccessor)client.field_1705;
        class_2561 actionText = hud.getOverlayMessage();
        if (actionText == null) {
            FishingData.active = false;
            return;
        }
        String raw = actionText.getString();
        if (raw.contains("[") && raw.contains("]")) {
            int start = raw.indexOf("[");
            int end = raw.indexOf("]");
            String barContent = raw.substring(start + 1, end);
            int fishIdx = barContent.indexOf("\ud83c\udf1f");
            int barStart = barContent.indexOf("\u2588");
            int barEnd = barContent.lastIndexOf("\u2588");
            if (barStart != -1) {
                FishingData.active = true;
                FishingData.dotX = fishIdx != -1 ? (float)fishIdx : 0.0f;
                FishingData.barX = (float)(barStart + barEnd) / 2.0f;
                lastBarTime = System.currentTimeMillis();
                try {
                    String tail = raw.substring(end + 1).trim();
                    if (tail.contains("%")) {
                        FishingData.progress = Float.parseFloat(tail.split("%")[0].trim());
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (!state.equals("FISHING") && FishingData.progress < 20.0f) {
                    state = "FISHING";
                    stateTimer = System.currentTimeMillis();
                    pid.reset();
                }
            } else {
                FishingData.active = false;
            }
        } else {
            FishingData.active = false;
        }
    }

    private static void stopFishing(class_310 client) {
        client.field_1690.field_1832.method_23481(false);
        state = "IDLE";
        stateTimer = System.currentTimeMillis();
        FishingData.reset();
    }

    private static void selectFishingRod(class_310 client) {
        for (int i = 0; i < 9; ++i) {
            if (!client.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_8378)) continue;
            client.field_1724.method_31548().field_7545 = i;
            return;
        }
    }

    private static void autoBait(class_310 client) {
        int p = -1;
        int r = -1;
        for (int i = 0; i < 36; ++i) {
            class_1799 s = client.field_1724.method_31548().method_5438(i);
            if (s.method_31574(class_1802.field_8323)) {
                p = i;
            }
            if (!s.method_31574(class_1802.field_8378)) continue;
            r = i;
        }
        if (p != -1 && r != -1) {
            int ps = p < 9 ? p + 36 : p;
            int rs = r < 9 ? r + 36 : r;
            client.field_1761.method_2906(0, ps, 0, class_1713.field_7790, (class_1657)client.field_1724);
            client.field_1761.method_2906(0, rs, 1, class_1713.field_7790, (class_1657)client.field_1724);
            client.field_1761.method_2906(0, ps, 0, class_1713.field_7790, (class_1657)client.field_1724);
        }
    }
}

