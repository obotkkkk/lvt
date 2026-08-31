/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_9779
 */
package com.atj;

import com.atj.Atj;
import fi.dy.masa.litematica.config.AtjConfig;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_9779;

public class HUDRenderer {
    public static void render(class_332 drawContext, class_9779 tickCounter) {
        class_310 client = class_310.method_1551();
        if (client.field_1724 == null || client.field_1690.field_1842) {
            return;
        }
        int x = 10;
        int y = 10;
        boolean isRunning = AtjConfig.AUTO_JOIN.getBooleanValue();
        drawContext.method_51433(client.field_1772, "\u00a76\u00a7l[ATJ - LITEMATICA ADDON]", x, y, 0xFFFFFF, true);
        String statusText = "\u00a7fTr\u1ea1ng th\u00e1i: " + (isRunning ? "\u00a7a\u00a7lB\u1eacT" : "\u00a7c\u00a7lT\u1eaeT");
        drawContext.method_51433(client.field_1772, statusText, x, y += 12, 0xFFFFFF, true);
        if (isRunning) {
            y += 12;
            if (Atj.clickDelayTimer > 0) {
                drawContext.method_51433(client.field_1772, "\u00a7e\u00a7o\u26a1 \u0110ang ch\u1edd click (" + Atj.clickDelayTimer + " ticks)...", x, y, 0xFFFFFF, true);
            } else {
                drawContext.method_51433(client.field_1772, "\u00a77\ud83d\udd0e \u0110ang qu\u00e9t giao di\u1ec7n...", x, y, 0xFFFFFF, true);
            }
        }
    }
}

