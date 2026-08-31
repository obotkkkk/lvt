/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
 *  net.minecraft.class_1703
 *  net.minecraft.class_1799
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_9280
 *  net.minecraft.class_9334
 */
package com.ghepdo;

import fi.dy.masa.litematica.config.LitemacConfig;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.class_1703;
import net.minecraft.class_1799;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_9280;
import net.minecraft.class_9334;

public class DataScanner {
    private static int tickCounter = 0;
    public static long lastHitTime = 0L;

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof class_465) {
                class_465 handledScreen = (class_465)screen;
                ScreenEvents.afterRender((class_437)screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> {
                    boolean isEnabled = LitemacConfig.LITEMAC_ENABLED.getBooleanValue();
                    if (isEnabled) {
                        int x = drawContext.method_51421() - 150;
                        int y = drawContext.method_51443() - 20;
                        drawContext.method_51433(client.field_1772, "\u00a7fBot Status: \u00a7aRUNNING", x, y, -1, true);
                        if (handledScreen.method_17577().field_7761.size() >= 32) {
                            drawContext.method_51433(client.field_1772, "\u00a76[!] PH\u00c1T HI\u1ec6N MINIGAME", x, y - 12, -1, true);
                        }
                        if (System.currentTimeMillis() - lastHitTime < 2000L) {
                            drawContext.method_51433(client.field_1772, "\u00a7c>>> CLICKED SUCCESS! <<<", x, y - 24, -1, true);
                        }
                        if (++tickCounter >= 30) {
                            DataScanner.runConsoleDump(handledScreen);
                            tickCounter = 0;
                        }
                    }
                });
            }
        });
    }

    private static void runConsoleDump(class_465<?> handledScreen) {
        class_1703 handler = handledScreen.method_17577();
        System.out.println("================ [v3 hehe RADAR SCAN] ================");
        for (int i = 0; i < handler.field_7761.size(); ++i) {
            class_1799 s = handler.method_7611(i).method_7677();
            if (s.method_7960()) continue;
            System.out.println("Slot [" + i + "]: " + s.method_7964().getString() + " | CMD: " + DataScanner.getCMD(s));
        }
        System.out.println("======================================================\n");
    }

    public static float getCMD(class_1799 stack) {
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

