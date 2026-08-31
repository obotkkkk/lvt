package com.pb;

import com.lvt.guard.GuardManager;
import com.pb.ConfirmHandler;
import fi.dy.masa.litematica.config.PbConfig;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import net.minecraft.class_465;
import net.minecraft.class_9290;
import net.minecraft.class_9334;

public class AutoPhoban {
    private static boolean hasClickedInThisSession = false;
    private static int guiRetryTick = 0;
    private static int loopTick = 0;
    private static String lastGuiTitle = "";
    private static boolean isInitialized = false;
    private static int startupDelayTick = 0;
    private static String selectedPbType = "ngucthan";
    private static boolean wasKeyPressed = false;
    private static final Pattern COUNT_PATTERN = Pattern.compile("(\\d+)\\s*/\\s*(\\d+)");

    public static void onTick(class_310 client) {
        if (client.field_1724 == null) {
            return;
        }
        long windowHandle = client.method_22683().method_4490();
        boolean isPressed = class_3675.method_15987((long)windowHandle, (int)293);
        if (isPressed) {
            if (!wasKeyPressed) {
                wasKeyPressed = true;
                selectedPbType = switch (selectedPbType) {
                    case "ngucthan" -> "tantic";
                    case "tantic" -> "dautruong";
                    case "dautruong" -> "dilang";
                    default -> "ngucthan";
                };
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e\u2726 [AutoPB] M\u1ee5c ti\u00eau qu\u00e9t m\u1edbi: \u00a7b" + selectedPbType.toUpperCase())), true);
            }
        } else {
            wasKeyPressed = false;
        }
        if (!GuardManager.canUseAddons()) {
            isInitialized = false;
            startupDelayTick = 0;
            return;
        }
        if (!PbConfig.AUTO_PB_ENABLED.getBooleanValue()) {
            isInitialized = false;
            startupDelayTick = 0;
            loopTick = 0;
            return;
        }
        if (!isInitialized) {
            if (++startupDelayTick < 200) {
                return;
            }
            isInitialized = true;
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7a\u2714 AutoPB: H\u1ec7 th\u1ed1ng \u0111\u00e3 s\u1eb5n s\u00e0ng!"), false);
        }
        if (++loopTick >= 60) {
            loopTick = 0;
            if (client.field_1755 == null) {
                client.field_1724.field_3944.method_45730("phoban");
            }
        }
        if (client.field_1755 instanceof class_465<?> handledScreen) {
            String title = class_124.method_539((String)client.field_1755.method_25440().getString());
            if (!title.equals(lastGuiTitle)) {
                hasClickedInThisSession = false;
                lastGuiTitle = title;
                guiRetryTick = 0;
            }
            if (!hasClickedInThisSession && ++guiRetryTick >= 10) {
                guiRetryTick = 0;
                String simplifiedTitle = AutoPhoban.simplify(title);
                if (simplifiedTitle.contains("pho ban")) {
                    AutoPhoban.runGlobalSearchLogic(client, handledScreen);
                } else {
                    boolean handled = ConfirmHandler.handle(client, handledScreen);
                    if (handled) {
                        hasClickedInThisSession = true;
                    }
                }
            }
        } else {
            hasClickedInThisSession = false;
            lastGuiTitle = "";
            guiRetryTick = 0;
        }
    }

    private static void runGlobalSearchLogic(class_310 client, class_465<?> screen) {
        String name;
        class_1799 stack;
        int i;
        class_1703 handler = screen.method_17577();
        int bestSlot = -1;
        String bestName = "";
        String targetPbKey = AutoPhoban.convertPbTypeToKey(selectedPbType);
        for (i = 0; i < handler.field_7761.size(); ++i) {
            stack = handler.method_7611(i).method_7677();
            if (stack.method_7960() || !stack.method_31574(class_1802.field_8892)) continue;
            name = AutoPhoban.simplify(stack.method_7964().getString());
            boolean isJoinable = false;
            boolean isPlaying = false;
            class_9290 lore = (class_9290)stack.method_57824(class_9334.field_49632);
            if (lore != null) {
                for (Object lineObj : lore.comp_2400()) {
                    class_2561 line = (class_2561)lineObj;
                    Matcher m;
                    String simLine = AutoPhoban.simplify(line.getString());
                    if (simLine.contains("dang danh") || simLine.contains("chien dau") || simLine.contains("da day") || simLine.contains("dang hoat dong")) {
                        isPlaying = true;
                    }
                    if (simLine.contains("dang cho") || simLine.contains("san sang")) {
                        isJoinable = true;
                    }
                    if (!(m = COUNT_PATTERN.matcher(simLine)).find()) continue;
                    try {
                        int current = Integer.parseInt(m.group(1));
                        int max = Integer.parseInt(m.group(2));
                        if (current < max) {
                            isJoinable = true;
                            continue;
                        }
                        isPlaying = true;
                    }
                    catch (Exception exception) {}
                }
            }
            if (!isJoinable || isPlaying || !name.contains(targetPbKey) || bestSlot != -1 && name.compareTo(AutoPhoban.simplify(bestName)) <= 0) continue;
            bestSlot = i;
            bestName = stack.method_7964().getString();
        }
        if (bestSlot != -1) {
            AutoPhoban.doClick(client, screen, bestSlot, class_124.method_539((String)bestName));
        } else {
            hasClickedInThisSession = false;
            guiRetryTick = 0;
        }
    }

    public static boolean handleChatCommand(class_310 client, String message) {
        if (message == null || !message.startsWith("/pb")) {
            return false;
        }
        String[] args = message.split("\\s+");
        if (args.length == 2 && args[1].equalsIgnoreCase("status")) {
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7e[AutoPB] Lo\u1ea1i ph\u00f3 b\u1ea3n \u0111ang qu\u00e9t: \u00a7b" + selectedPbType.toUpperCase())), false);
            return true;
        }
        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            String type = args[2].toLowerCase();
            if (type.equals("ngucthan") || type.equals("tantic") || type.equals("dautruong") || type.equals("dilang")) {
                selectedPbType = type;
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7a\u2714 [AutoPB] \u0110\u00e3 chuy\u1ec3n m\u1ee5c ti\u00eau qu\u00e9t th\u00e0nh: \u00a7b" + type.toUpperCase())), false);
            } else {
                client.field_1724.method_7353((class_2561)class_2561.method_43470((String)"\u00a7c\u274c [AutoPB] Sai l\u1ec7nh! H\u00e3y ch\u1ecdn: ngucthan, tantic, dautruong, dilang"), false);
            }
            return true;
        }
        return false;
    }

    private static String convertPbTypeToKey(String type) {
        return switch (type) {
            case "tantic" -> "tan tich";
            case "dautruong" -> "dau truong";
            case "dilang" -> "di lang";
            default -> "nguc than";
        };
    }

    public static String simplify(String input) {
        if (input == null) {
            return "";
        }
        String out = class_124.method_539((String)input).toLowerCase();
        out = out.replace("\u1d00", "a").replace("\u6a6b", "b").replace("\u1d04", "c").replace("\u1d05", "d").replace("\u1d07", "e").replace("\ua730", "f").replace("\u0262", "g").replace("\u029c", "h").replace("\u026a", "i").replace("\u1d0a", "j").replace("\u1d0b", "k").replace("\u029f", "l").replace("\u1d0d", "m").replace("\u0274", "n").replace("\u1d0f", "o").replace("\u1d18", "p").replace("\u01eb", "q").replace("\u0280", "r").replace("s", "s").replace("\u1d1b", "t").replace("\u1d1c", "u").replace("\u1d20", "v").replace("\u1d21", "w").replace("x", "x").replace("\u028f", "y").replace("\u1d22", "z");
        out = Normalizer.normalize(out, Normalizer.Form.NFD);
        out = out.replaceAll("\\p{M}", "");
        out = out.replace("\u0111", "d");
        return out.replaceAll("\\s+", " ").trim();
    }

    private static void doClick(class_310 client, class_465<?> screen, int slotId, String msg) {
        if (client.field_1761 != null) {
            client.field_1761.method_2906(screen.method_17577().field_7763, slotId, 0, class_1713.field_7790, (class_1657)client.field_1724);
            hasClickedInThisSession = true;
            client.field_1724.method_7353((class_2561)class_2561.method_43470((String)("\u00a7a\u2714 AutoPB: " + msg)), false);
        }
    }
}
