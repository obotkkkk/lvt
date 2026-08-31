/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_465
 */
package com.pb;

import com.pb.AutoPhoban;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_465;

public class ConfirmHandler {
    public static boolean handle(class_310 client, class_465<?> screen) {
        if (screen == null || screen.method_25440() == null) {
            return false;
        }
        String title = class_124.method_539((String)screen.method_25440().getString()).toLowerCase();
        if (title.contains("sure") || title.contains("x\u00e1c nh\u1eadn") || title.contains("confirm") || title.contains("x\u00e1c \u0274\u029c\u1ead\u0274")) {
            class_1703 handler = screen.method_17577();
            for (int i = 0; i < handler.field_7761.size(); ++i) {
                String itemName;
                class_1799 stack = handler.method_7611(i).method_7677();
                if (stack.method_7960() || !(itemName = AutoPhoban.simplify(stack.method_7964().getString())).equals("accept") && !itemName.contains("dong y") && !itemName.contains("chap nhan") && !itemName.contains("xac nhan")) continue;
                ConfirmHandler.clickSlot(client, screen, i);
                if (client.field_1724 != null) {
                    client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7a\u2714 AutoPB: \u0110\u00e3 t\u1ef1 \u0111\u1ed9ng x\u00e1c nh\u1eadn"), true);
                }
                return true;
            }
        }
        return false;
    }

    private static void clickSlot(class_310 client, class_465<?> screen, int slotId) {
        if (client.field_1761 != null && client.field_1724 != null) {
            client.field_1761.method_2906(screen.method_17577().field_7763, slotId, 0, class_1713.field_7790, (class_1657)client.field_1724);
        }
    }
}

