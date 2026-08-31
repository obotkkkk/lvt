/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_7923
 */
package com.luyendan;

import com.luyendan.ScannerPhapBao;
import com.lvt.guard.GuardManager;
import fi.dy.masa.litematica.config.LuyenDanConfig;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_7923;

public class AutoLuyenDan {
    private static int clickCooldown = 0;
    private static int targetSlot = -1;
    private static int preClickDelay = 0;

    public static void onTick(class_310 client) {
        if (client.field_1724 == null) {
            return;
        }
        if (!GuardManager.canUseAddons() || !LuyenDanConfig.AUTO_LUYENDAN.getBooleanValue()) {
            AutoLuyenDan.reset();
            return;
        }
        ScannerPhapBao.theoDoiBienDong(client);
        class_437 class_4372 = client.field_1755;
        if (class_4372 instanceof class_465) {
            class_465 screen = (class_465)class_4372;
            String title = class_124.method_539((String)screen.method_25440().getString());
            if (!title.contains("\u1d1b\u00e2\u1d0d \u1d0d\u1d00 \u029f\u1d1c\u028f\u1ec7\u0274 \u0111\u1d00\u0274")) {
                AutoLuyenDan.reset();
                return;
            }
            if (targetSlot != -1) {
                if (--preClickDelay <= 0) {
                    AutoLuyenDan.doActualClick(client, screen, targetSlot);
                    targetSlot = -1;
                    clickCooldown = 3;
                }
                return;
            }
            if (clickCooldown > 0) {
                --clickCooldown;
                return;
            }
            class_1703 handler = screen.method_17577();
            for (int i = 0; i < 54; ++i) {
                class_2960 id;
                String itemName;
                class_1799 stack = handler.method_7611(i).method_7677();
                if (stack.method_7960() || !(itemName = (id = class_7923.field_41178.method_10221((Object)stack.method_7909())).toString()).contains("purple_stained_glass_pane") && !itemName.contains("yellow_stained_glass_pane") && !itemName.contains("white_stained_glass_pane")) continue;
                targetSlot = i;
                preClickDelay = 1;
                break;
            }
        } else {
            AutoLuyenDan.reset();
        }
    }

    private static void doActualClick(class_310 client, class_465<?> screen, int slot) {
        if (client.field_1761 != null) {
            client.field_1761.method_2906(screen.method_17577().field_7763, slot, 0, class_1713.field_7790, (class_1657)client.field_1724);
            System.out.println("[AutoLuyenDan] \u0110\u00e3 click \u00f4 t\u1ea1i Slot: " + slot + " sau 50ms delay.");
        }
    }

    private static void reset() {
        clickCooldown = 0;
        targetSlot = -1;
        preClickDelay = 0;
    }
}

