/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2310
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2689$class_2690
 *  net.minecraft.class_2769
 *  net.minecraft.class_2960
 */
package fi.dy.masa.litematica.render.schematic.blocks;

import fi.dy.masa.litematica.Litematica;
import java.util.HashMap;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2310;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2769;
import net.minecraft.class_2960;

public class FallbackBlocks {
    public static HashMap<class_2248, class_2960> BLOCK_TO_ID = new HashMap();
    public static HashMap<class_2960, class_2248> ID_TO_BLOCK = new HashMap();
    public static HashMap<class_2960, class_2689<class_2248, class_2680>> ID_TO_STATE_MANAGER = new HashMap();
    public static class_2960 BLACK_GLASS = FallbackBlocks.registerBasic("black_glass_fallback", class_2246.field_9997);
    public static class_2960 BLUE_GLASS = FallbackBlocks.registerBasic("blue_glass_fallback", class_2246.field_10060);
    public static class_2960 BROWN_GLASS = FallbackBlocks.registerBasic("brown_glass_fallback", class_2246.field_10073);
    public static class_2960 CYAN_GLASS = FallbackBlocks.registerBasic("cyan_glass_fallback", class_2246.field_10248);
    public static class_2960 GLASS = FallbackBlocks.registerBasic("glass_fallback", class_2246.field_10033);
    public static class_2960 GRAY_GLASS = FallbackBlocks.registerBasic("gray_glass_fallback", class_2246.field_10555);
    public static class_2960 GREEN_GLASS = FallbackBlocks.registerBasic("green_glass_fallback", class_2246.field_10357);
    public static class_2960 LIME_GLASS = FallbackBlocks.registerBasic("lime_glass_fallback", class_2246.field_10157);
    public static class_2960 LT_BLUE_GLASS = FallbackBlocks.registerBasic("lt_blue_glass_fallback", class_2246.field_10271);
    public static class_2960 LT_GRAY_GLASS = FallbackBlocks.registerBasic("lt_gray_glass_fallback", class_2246.field_9996);
    public static class_2960 MAGENTA_GLASS = FallbackBlocks.registerBasic("magenta_glass_fallback", class_2246.field_10574);
    public static class_2960 ORANGE_GLASS = FallbackBlocks.registerBasic("orange_glass_fallback", class_2246.field_10227);
    public static class_2960 PINK_GLASS = FallbackBlocks.registerBasic("pink_glass_fallback", class_2246.field_10317);
    public static class_2960 PURPLE_GLASS = FallbackBlocks.registerBasic("purple_glass_fallback", class_2246.field_10399);
    public static class_2960 RED_GLASS = FallbackBlocks.registerBasic("red_glass_fallback", class_2246.field_10272);
    public static class_2960 TINTED_GLASS = FallbackBlocks.registerBasic("tinted_glass_fallback", class_2246.field_27115);
    public static class_2960 WHITE_GLASS = FallbackBlocks.registerBasic("white_glass_fallback", class_2246.field_10087);
    public static class_2960 YELLOW_GLASS = FallbackBlocks.registerBasic("yellow_glass_fallback", class_2246.field_10049);
    public static class_2960 BLACK_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("black_glass_pane_fallback", class_2246.field_10070);
    public static class_2960 BLUE_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("blue_glass_pane_fallback", class_2246.field_9982);
    public static class_2960 BROWN_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("brown_glass_pane_fallback", class_2246.field_10163);
    public static class_2960 CYAN_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("cyan_glass_pane_fallback", class_2246.field_10355);
    public static class_2960 GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("glass_pane_fallback", class_2246.field_10285);
    public static class_2960 GRAY_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("gray_glass_pane_fallback", class_2246.field_10077);
    public static class_2960 GREEN_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("green_glass_pane_fallback", class_2246.field_10419);
    public static class_2960 LIME_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("lime_glass_pane_fallback", class_2246.field_10305);
    public static class_2960 LT_BLUE_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("lt_blue_glass_pane_fallback", class_2246.field_10193);
    public static class_2960 LT_GRAY_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("lt_gray_glass_pane_fallback", class_2246.field_10129);
    public static class_2960 MAGENTA_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("magenta_glass_pane_fallback", class_2246.field_10469);
    public static class_2960 ORANGE_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("orange_glass_pane_fallback", class_2246.field_10496);
    public static class_2960 PINK_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("pink_glass_pane_fallback", class_2246.field_10565);
    public static class_2960 PURPLE_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("purple_glass_pane_fallback", class_2246.field_10152);
    public static class_2960 RED_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("red_glass_pane_fallback", class_2246.field_10118);
    public static class_2960 WHITE_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("white_glass_pane_fallback", class_2246.field_9991);
    public static class_2960 YELLOW_GLASS_PANE = FallbackBlocks.registerHorizontalConnecting("yellow_glass_pane_fallback", class_2246.field_10578);

    private static class_2960 registerBasic(String name, class_2248 block) {
        class_2960 id = class_2960.method_60655((String)"litematica", (String)name);
        BLOCK_TO_ID.put(block, id);
        ID_TO_BLOCK.put(id, block);
        ID_TO_STATE_MANAGER.put(id, (class_2689<class_2248, class_2680>)new class_2689.class_2690((Object)block).method_11668(class_2248::method_9564, class_2680::new));
        return id;
    }

    private static class_2960 registerHorizontalConnecting(String name, class_2248 block) {
        class_2689.class_2690 builder = new class_2689.class_2690((Object)block);
        class_2960 id = class_2960.method_60655((String)"litematica", (String)name);
        BLOCK_TO_ID.put(block, id);
        ID_TO_BLOCK.put(id, block);
        builder.method_11667(new class_2769[]{class_2310.field_10905});
        builder.method_11667(new class_2769[]{class_2310.field_10907});
        builder.method_11667(new class_2769[]{class_2310.field_10904});
        builder.method_11667(new class_2769[]{class_2310.field_10903});
        builder.method_11667(new class_2769[]{class_2310.field_10900});
        ID_TO_STATE_MANAGER.put(id, (class_2689<class_2248, class_2680>)builder.method_11668(FallbackBlocks::defaultHorizontalConnectingBlockState, class_2680::new));
        return id;
    }

    public static class_2680 defaultHorizontalConnectingBlockState(class_2248 block) {
        return (class_2680)((class_2680)((class_2680)((class_2680)((class_2680)block.method_9564().method_11657((class_2769)class_2310.field_10905, (Comparable)Boolean.valueOf(false))).method_11657((class_2769)class_2310.field_10907, (Comparable)Boolean.valueOf(false))).method_11657((class_2769)class_2310.field_10904, (Comparable)Boolean.valueOf(false))).method_11657((class_2769)class_2310.field_10903, (Comparable)Boolean.valueOf(false))).method_11657((class_2769)class_2310.field_10900, (Comparable)Boolean.valueOf(false));
    }

    public static void register() {
        Litematica.debugLog("FallbackBlockModels: initialized.", new Object[0]);
    }
}

