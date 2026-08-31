/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1722
 *  net.minecraft.class_1799
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_488
 */
package com.atj;

import com.atj.Atj;
import java.util.Random;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1722;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_488;

public class GUIScanner {
    private static final Random random = new Random();

    public static void scanAndPrepare(class_310 client) {
        class_488 screen;
        String title;
        class_437 class_4372 = client.field_1755;
        if (class_4372 instanceof class_488 && (title = (screen = (class_488)class_4372).method_25440().getString()).contains("Are you sure")) {
            Atj.clickDelayTimer = 10 + random.nextInt(15);
        }
    }

    public static void executeClick(class_310 client) {
        int acceptSlot;
        class_488 screen;
        class_1722 handler;
        class_1799 item;
        class_437 class_4372 = client.field_1755;
        if (class_4372 instanceof class_488 && !(item = (handler = (class_1722)(screen = (class_488)class_4372).method_17577()).method_7611(acceptSlot = 4).method_7677()).method_7960() && item.method_7964().getString().contains("Accept")) {
            client.field_1761.method_2906(handler.field_7763, acceptSlot, 0, class_1713.field_7790, (class_1657)client.field_1724);
            client.field_1724.method_7346();
            Atj.scanTimer = 100;
        }
    }
}

