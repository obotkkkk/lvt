/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_640
 *  net.minecraft.class_9280
 *  net.minecraft.class_9334
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.ghepdo;

import com.ghepdo.DataScanner;
import fi.dy.masa.litematica.config.LitemacConfig;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_640;
import net.minecraft.class_9280;
import net.minecraft.class_9334;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoMinigame {
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"v4-Speedrun-Bot");
    private static boolean canClick = true;
    private static boolean wasGuiOpen = false;
    private static boolean isWarmupDone = false;
    private static long firstDetectTime = 0L;
    private static String lastRawStateStr = "";
    private static int savedTargetSlot = -1;

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof class_465) {
                class_465 handledScreen = (class_465)screen;
                wasGuiOpen = false;
                isWarmupDone = false;
                ScreenEvents.afterRender((class_437)screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> {
                    if (LitemacConfig.LITEMAC_ENABLED.getBooleanValue()) {
                        AutoMinigame.runSpeedrunLogic(client, handledScreen);
                    }
                });
            }
        });
    }

    private static void runSpeedrunLogic(class_310 client, class_465<?> screen) {
        class_1703 handler = screen.method_17577();
        if (handler.field_7761.size() < 32) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        boolean hasTarget = false;
        for (int i = 9; i <= 17; ++i) {
            if (AutoMinigame.getCMD(handler.method_7611(i).method_7677()) != 10021.0f) continue;
            hasTarget = true;
            break;
        }
        if (hasTarget || AutoMinigame.getCMD(handler.method_7611(31).method_7677()) == 10022.0f) {
            if (!wasGuiOpen) {
                wasGuiOpen = true;
                isWarmupDone = false;
                firstDetectTime = currentTime;
                savedTargetSlot = -1;
                LOGGER.info("================ GAME START ================");
            }
        } else {
            return;
        }
        if (!isWarmupDone) {
            if (currentTime - firstDetectTime > 500L) {
                isWarmupDone = true;
                LOGGER.info(">>> RADAR READY!");
            }
            return;
        }
        int currentBallSlot = -1;
        StringBuilder rawLine = new StringBuilder();
        for (int i = 9; i <= 17; ++i) {
            float cmd = AutoMinigame.getCMD(handler.method_7611(i).method_7677());
            rawLine.append(String.format("%8.1f", Float.valueOf(cmd))).append(" |");
            if (cmd == 10021.0f) {
                savedTargetSlot = i;
                continue;
            }
            if (cmd != 10023.0f && cmd != 10024.0f) continue;
            currentBallSlot = i;
        }
        String currentRaw = rawLine.toString();
        if (!currentRaw.equals(lastRawStateStr)) {
            LOGGER.info("[RADAR] Slots 9-17: [ {}] | Mem: {} | Ping: {}ms", new Object[]{currentRaw, savedTargetSlot, AutoMinigame.getCurrentPing(client)});
            lastRawStateStr = currentRaw;
        }
        if (currentBallSlot != -1 && savedTargetSlot != -1) {
            if (currentBallSlot == savedTargetSlot) {
                if (canClick) {
                    client.field_1761.method_2906(handler.field_7763, 31, 0, class_1713.field_7790, (class_1657)client.field_1724);
                    canClick = false;
                    DataScanner.lastHitTime = System.currentTimeMillis();
                    LOGGER.warn(">>> HIT! Click Slot 31 t\u1ea1i \u00f4 {} | Ping: {}", (Object)currentBallSlot, (Object)AutoMinigame.getCurrentPing(client));
                }
            } else {
                canClick = true;
            }
        }
    }

    private static int getCurrentPing(class_310 client) {
        if (client.method_1562() == null || client.field_1724 == null) {
            return 0;
        }
        class_640 entry = client.method_1562().method_2871(client.field_1724.method_5667());
        return entry != null ? entry.method_2959() : 0;
    }

    private static float getCMD(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return -1.0f;
        }
        class_9280 cmd = (class_9280)stack.method_57824(class_9334.field_49637);
        if (cmd != null && !cmd.comp_3354().isEmpty()) {
            return ((Float)cmd.comp_3354().get(0)).floatValue();
        }
        return 0.0f;
    }
}

