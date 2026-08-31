/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1921
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_7833
 */
package com.mayquetlinhthao;

import com.lvt.guard.GuardManager;
import fi.dy.masa.litematica.config.RadarConfig;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_124;
import net.minecraft.class_1921;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_7833;

public class LinhThaoRadarClient {
    private static final class_2960 DIAMOND_SWORD = class_2960.method_60655((String)"minecraft", (String)"textures/item/diamond_sword.png");
    private static class_243 nearestHerb = null;
    private static String currentTargetName = "";
    private static double minDistance = Double.MAX_VALUE;
    private static long bcNotifyEndTime = 0L;
    private static final Set<String> ignoredCoords = new HashSet<String>();

    public static void onTick(class_310 client) {
        if (client.field_1724 == null) {
            return;
        }
        if (!GuardManager.canUseAddons()) {
            nearestHerb = null;
            return;
        }
        if (nearestHerb != null && client.field_1724.method_19538().method_1022(nearestHerb) < 2.0) {
            ignoredCoords.add(LinhThaoRadarClient.getCoordKey(nearestHerb));
            nearestHerb = null;
            minDistance = Double.MAX_VALUE;
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7a\u2714 \u0110\u00e3 ghi nh\u1edb t\u1ecda \u0111\u1ed9: \u0110\u00e3 h\u00e1i!"), true);
        }
    }

    public static void onChatMessage(class_2561 message) {
        boolean isAllMode;
        if (!GuardManager.canUseAddons() || !RadarConfig.RADAR_ENABLED.getBooleanValue()) {
            return;
        }
        String rawText = message.getString();
        String cleanText = LinhThaoRadarClient.simplify(rawText);
        String currentFocus = RadarConfig.RADAR_FOCUS.getOptionListValue().getStringValue();
        String cleanFocus = LinhThaoRadarClient.simplify(currentFocus);
        if (cleanText.contains("bi canh xuat hien")) {
            bcNotifyEndTime = System.currentTimeMillis() + 3000L;
        }
        if (cleanText.contains("danh sach vi tri")) {
            minDistance = Double.MAX_VALUE;
            nearestHerb = null;
            ignoredCoords.clear();
        }
        Object regexPattern = (isAllMode = cleanFocus.equals("all")) ? "bac:\\s*([^,]+).*?x:\\s*([-]?\\d+).*?y:\\s*([-]?\\d+).*?z:\\s*([-]?\\d+)" : "bac:\\s*" + cleanFocus + ".*?x:\\s*([-]?\\d+).*?y:\\s*([-]?\\d+).*?z:\\s*([-]?\\d+)";
        Pattern pattern = Pattern.compile((String)regexPattern);
        Matcher matcher = pattern.matcher(cleanText);
        while (matcher.find()) {
            try {
                double dist;
                double z;
                double y;
                String herbName;
                int offset = isAllMode ? 1 : 0;
                String string = herbName = isAllMode ? matcher.group(1).trim() : currentFocus;
                double x = Double.parseDouble(matcher.group(1 + offset));
                class_243 herbPos = new class_243(x, y = Double.parseDouble(matcher.group(2 + offset)), z = Double.parseDouble(matcher.group(3 + offset)));
                if (ignoredCoords.contains(LinhThaoRadarClient.getCoordKey(herbPos)) || !((dist = class_310.method_1551().field_1724.method_19538().method_1022(herbPos)) < minDistance)) continue;
                minDistance = dist;
                nearestHerb = herbPos;
                currentTargetName = herbName;
            }
            catch (Exception exception) {}
        }
    }

    private static String getCoordKey(class_243 pos) {
        return (int)pos.field_1352 + "," + (int)pos.field_1351 + "," + (int)pos.field_1350;
    }

    public static void onHudRender(class_332 drawContext, float tickDelta) {
        class_310 client = class_310.method_1551();
        if (!GuardManager.canUseAddons() || !RadarConfig.RADAR_ENABLED.getBooleanValue() || client.field_1724 == null) {
            return;
        }
        if (System.currentTimeMillis() < bcNotifyEndTime) {
            LinhThaoRadarClient.renderBigAlert(drawContext, client);
        }
        LinhThaoRadarClient.renderCompass(drawContext, client);
    }

    private static void renderBigAlert(class_332 drawContext, class_310 client) {
        int w = client.method_22683().method_4486();
        int h = client.method_22683().method_4502();
        if (System.currentTimeMillis() / 250L % 2L == 0L) {
            drawContext.method_51448().method_22903();
            drawContext.method_51448().method_22905(2.0f, 2.0f, 1.0f);
            drawContext.method_25300(client.field_1772, "\u00a7l\u26a0 B\u00cd C\u1ea2NH XU\u1ea4T HI\u1ec6N \u26a0", w / 4, h / 6, 0xFF0000);
            drawContext.method_51448().method_22909();
        }
    }

    private static void renderCompass(class_332 drawContext, class_310 client) {
        class_243 targetPos = nearestHerb != null ? nearestHerb : new class_243(0.0, 0.0, 0.0);
        String targetLabel = nearestHerb != null ? currentTargetName : "Trung t\u00e2m (0, 0, 0)";
        double dx = targetPos.field_1352 - client.field_1724.method_23317();
        double dz = targetPos.field_1350 - client.field_1724.method_23321();
        double distance = Math.sqrt(dx * dx + dz * dz);
        int w = client.method_22683().method_4486();
        int h = client.method_22683().method_4502();
        int color = nearestHerb == null ? -5592406 : (distance < 10.0 ? -65536 : (distance < 50.0 ? -256 : (distance < 100.0 ? -16711936 : -872349697)));
        drawContext.method_51433(client.field_1772, "\u00a7e\u2726 M\u1ee5c ti\u00eau: \u00a7b" + targetLabel, 10, 34, 0xFFFFFF, true);
        drawContext.method_51433(client.field_1772, String.format("\ud83d\udccd [X:%.0f | Y:%.0f | Z:%.0f]", targetPos.field_1352, targetPos.field_1351, targetPos.field_1350), 10, 10, 65535, true);
        drawContext.method_51433(client.field_1772, String.format("\ud83d\udccf C\u00e1ch: %.1f m", distance), 10, 22, color, true);
        drawContext.method_51433(client.field_1772, "\u00a77(\u0110\u00e3 b\u1ecf qua: " + ignoredCoords.size() + " v\u1ecb tr\u00ed)", 10, 46, 0xAAAAAA, true);
        if (distance <= 1.0) {
            return;
        }
        float rotation = class_3532.method_15393((float)((float)Math.toDegrees(class_3532.method_15349((double)(-dx), (double)dz)) - client.field_1724.method_36454()));
        drawContext.method_51448().method_22903();
        drawContext.method_51448().method_46416((float)w / 2.0f, (float)h / 2.0f + 50.0f, 0.0f);
        drawContext.method_51448().method_22903();
        drawContext.method_51448().method_22907(class_7833.field_40718.rotationDegrees(rotation));
        for (int i = 6; i < 46; i += 6) {
            drawContext.method_25294(-1, -i, 1, -i - 3, color);
        }
        drawContext.method_51448().method_22909();
        drawContext.method_51448().method_22907(class_7833.field_40714.rotationDegrees(70.0f));
        drawContext.method_51448().method_22907(class_7833.field_40718.rotationDegrees(rotation + 135.0f));
        drawContext.method_25290(class_1921::method_62277, DIAMOND_SWORD, -8, -8, 0.0f, 0.0f, 16, 16, 16, 16);
        drawContext.method_51448().method_22909();
    }

    private static String simplify(String input) {
        if (input == null) {
            return "";
        }
        String out = class_124.method_539((String)input).toLowerCase();
        out = out.replace("\u1d00", "a").replace("\u0299", "b").replace("\u1d04", "c").replace("\u1d05", "d").replace("\u1d07", "e").replace("\ua730", "f").replace("\u0262", "g").replace("\u029c", "h").replace("\u026a", "i").replace("\u1d0a", "j").replace("\u1d0b", "k").replace("\u029f", "l").replace("\u1d0d", "m").replace("\u0274", "n").replace("\u1d0f", "o").replace("\u1d18", "p").replace("\u01eb", "q").replace("\u0280", "r").replace("s", "s").replace("\u1d1b", "t").replace("\u1d1c", "u").replace("\u1d20", "v").replace("\u1d21", "w").replace("x", "x").replace("\u028f", "y").replace("\u1d22", "z");
        out = out.replaceAll("[\u00e0\u00e1\u1ea1\u1ea3\u00e3\u00e2\u1ea7\u1ea5\u1ead\u1ea9\u1eab\u0103\u1eb1\u1eaf\u1eb7\u1eb3\u1eb5]", "a").replaceAll("[\u00e8\u00e9\u1eb9\u1ebb\u1ebd\u00ea\u1ec1\u1ebf\u1ec7\u1ec3\u1ec5]", "e").replaceAll("[\u00ec\u00ed\u1ecb\u1ec9\u0129]", "i").replaceAll("[\u00f2\u00f3\u1ecd\u1ecf\u00f5\u00f4\u1ed3\u1ed1\u1ed9\u1ed5\u1ed7\u01a1\u1edd\u1edb\u1ee3\u1edf\u1ee1]", "o").replaceAll("[\u00f9\u00fa\u1ee5\u1ee7\u0169\u01b0\u1eeb\u1ee9\u1ef1\u1eed\u1eef]", "u").replaceAll("[\u1ef3\u00fd\u1ef5\u1ef7\u1ef9]", "y").replaceAll("\u0111", "d");
        return out;
    }
}

