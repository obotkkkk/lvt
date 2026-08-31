/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1703
 *  net.minecraft.class_1799
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_7923
 */
package com.luyendan;

import net.minecraft.class_124;
import net.minecraft.class_1703;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_7923;

public class ScannerPhapBao {
    private static final String[] lastFrame = new String[54];

    public static void theoDoiBienDong(class_310 client) {
        class_437 class_4372 = client.field_1755;
        if (class_4372 instanceof class_465) {
            class_465 screen = (class_465)class_4372;
            String title = class_124.method_539((String)screen.method_25440().getString());
            if (!title.contains("\u1d1b\u00e2\u1d0d \u1d0d\u1d00 \u029f\u1d1c\u028f\u1ec7\u0274 \u0111\u1d00\u0274")) {
                return;
            }
            class_1703 handler = screen.method_17577();
            for (int i = 0; i < 54; ++i) {
                String currentId;
                class_1799 stack = handler.method_7611(i).method_7677();
                String string = currentId = stack.method_7960() ? "EMPTY" : class_7923.field_41178.method_10221((Object)stack.method_7909()).toString();
                if (currentId.equals(lastFrame[i])) continue;
                ScannerPhapBao.lastFrame[i] = currentId;
                if (currentId.equals("EMPTY")) continue;
                System.out.printf("[Scanner] Slot [%d] xu\u1ea5t hi\u1ec7n: %s%n", i, currentId);
            }
        } else {
            for (int i = 0; i < 54; ++i) {
                ScannerPhapBao.lastFrame[i] = null;
            }
        }
    }
}

