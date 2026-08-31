/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.PositionUtils
 *  net.minecraft.class_1767
 *  net.minecraft.class_1922
 *  net.minecraft.class_2185
 *  net.minecraft.class_2190
 *  net.minecraft.class_2215
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2283
 *  net.minecraft.class_2310
 *  net.minecraft.class_2312
 *  net.minecraft.class_2320
 *  net.minecraft.class_2323
 *  net.minecraft.class_2338
 *  net.minecraft.class_2349
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2354
 *  net.minecraft.class_2389
 *  net.minecraft.class_2428
 *  net.minecraft.class_2457
 *  net.minecraft.class_2462
 *  net.minecraft.class_2484$class_2485
 *  net.minecraft.class_2484$class_2486
 *  net.minecraft.class_2487
 *  net.minecraft.class_2493
 *  net.minecraft.class_2499
 *  net.minecraft.class_2510
 *  net.minecraft.class_2520
 *  net.minecraft.class_2538
 *  net.minecraft.class_2541
 *  net.minecraft.class_2546
 *  net.minecraft.class_2549
 *  net.minecraft.class_2680
 *  net.minecraft.class_2742
 *  net.minecraft.class_2746
 *  net.minecraft.class_2750
 *  net.minecraft.class_2756
 *  net.minecraft.class_2769
 *  net.minecraft.class_2773
 *  net.minecraft.class_3532
 *  net.minecraft.class_4770
 */
package fi.dy.masa.litematica.schematic.conversion;

import fi.dy.masa.litematica.mixin.block.IMixinFenceGateBlock;
import fi.dy.masa.litematica.mixin.block.IMixinRedstoneWireBlock;
import fi.dy.masa.litematica.mixin.block.IMixinStairsBlock;
import fi.dy.masa.litematica.mixin.block.IMixinVineBlock;
import fi.dy.masa.litematica.schematic.conversion.IBlockReaderWithData;
import fi.dy.masa.malilib.util.PositionUtils;
import net.minecraft.class_1767;
import net.minecraft.class_1922;
import net.minecraft.class_2185;
import net.minecraft.class_2190;
import net.minecraft.class_2215;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2283;
import net.minecraft.class_2310;
import net.minecraft.class_2312;
import net.minecraft.class_2320;
import net.minecraft.class_2323;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_2354;
import net.minecraft.class_2389;
import net.minecraft.class_2428;
import net.minecraft.class_2457;
import net.minecraft.class_2462;
import net.minecraft.class_2484;
import net.minecraft.class_2487;
import net.minecraft.class_2493;
import net.minecraft.class_2499;
import net.minecraft.class_2510;
import net.minecraft.class_2520;
import net.minecraft.class_2538;
import net.minecraft.class_2541;
import net.minecraft.class_2546;
import net.minecraft.class_2549;
import net.minecraft.class_2680;
import net.minecraft.class_2742;
import net.minecraft.class_2746;
import net.minecraft.class_2750;
import net.minecraft.class_2756;
import net.minecraft.class_2769;
import net.minecraft.class_2773;
import net.minecraft.class_3532;
import net.minecraft.class_4770;

public class SchematicConversionFixers {
    private static final class_2746[] HORIZONTAL_CONNECTING_BLOCK_PROPS = new class_2746[]{null, null, class_2310.field_10905, class_2310.field_10904, class_2310.field_10903, class_2310.field_10907};
    private static final class_2680 REDSTONE_WIRE_DOT_OLD = class_2246.field_10091.method_9564();
    private static final class_2680 REDSTONE_WIRE_DOT = (class_2680)((class_2680)((class_2680)((class_2680)((class_2680)class_2246.field_10091.method_9564().method_11657((class_2769)class_2457.field_11432, (Comparable)Integer.valueOf(0))).method_11657((class_2769)class_2457.field_11440, (Comparable)class_2773.field_12687)).method_11657((class_2769)class_2457.field_11436, (Comparable)class_2773.field_12687)).method_11657((class_2769)class_2457.field_11437, (Comparable)class_2773.field_12687)).method_11657((class_2769)class_2457.field_11439, (Comparable)class_2773.field_12687);
    private static final class_2680 REDSTONE_WIRE_CROSS = (class_2680)((class_2680)((class_2680)((class_2680)class_2246.field_10091.method_9564().method_11657((class_2769)class_2457.field_11440, (Comparable)class_2773.field_12689)).method_11657((class_2769)class_2457.field_11436, (Comparable)class_2773.field_12689)).method_11657((class_2769)class_2457.field_11437, (Comparable)class_2773.field_12689)).method_11657((class_2769)class_2457.field_11439, (Comparable)class_2773.field_12689);
    public static final IStateFixer FIXER_BANNER = (reader, state, pos) -> {
        class_1767 colorFromData;
        class_1767 colorOrig;
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("Base", 3) && (colorOrig = ((class_2185)state.method_26204()).method_9303()) != (colorFromData = class_1767.method_7791((int)(15 - tag.method_10550("Base"))))) {
            Integer rotation = (Integer)state.method_11654((class_2769)class_2215.field_9924);
            switch (colorFromData) {
                case field_7952: {
                    state = class_2246.field_10154.method_9564();
                    break;
                }
                case field_7946: {
                    state = class_2246.field_10045.method_9564();
                    break;
                }
                case field_7958: {
                    state = class_2246.field_10438.method_9564();
                    break;
                }
                case field_7951: {
                    state = class_2246.field_10452.method_9564();
                    break;
                }
                case field_7947: {
                    state = class_2246.field_10547.method_9564();
                    break;
                }
                case field_7961: {
                    state = class_2246.field_10229.method_9564();
                    break;
                }
                case field_7954: {
                    state = class_2246.field_10612.method_9564();
                    break;
                }
                case field_7944: {
                    state = class_2246.field_10185.method_9564();
                    break;
                }
                case field_7967: {
                    state = class_2246.field_9985.method_9564();
                    break;
                }
                case field_7955: {
                    state = class_2246.field_10165.method_9564();
                    break;
                }
                case field_7945: {
                    state = class_2246.field_10368.method_9564();
                    break;
                }
                case field_7966: {
                    state = class_2246.field_10281.method_9564();
                    break;
                }
                case field_7957: {
                    state = class_2246.field_10602.method_9564();
                    break;
                }
                case field_7942: {
                    state = class_2246.field_10198.method_9564();
                    break;
                }
                case field_7964: {
                    state = class_2246.field_10406.method_9564();
                    break;
                }
                case field_7963: {
                    state = class_2246.field_10062.method_9564();
                }
            }
            state = (class_2680)state.method_11657((class_2769)class_2215.field_9924, (Comparable)rotation);
        }
        return state;
    };
    public static final IStateFixer FIXER_BANNER_WALL = (reader, state, pos) -> {
        class_1767 colorFromData;
        class_1767 colorOrig;
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("Base", 3) && (colorOrig = ((class_2185)state.method_26204()).method_9303()) != (colorFromData = class_1767.method_7791((int)(15 - tag.method_10550("Base"))))) {
            class_2350 facing = (class_2350)state.method_11654((class_2769)class_2546.field_11722);
            switch (colorFromData) {
                case field_7952: {
                    state = class_2246.field_10202.method_9564();
                    break;
                }
                case field_7946: {
                    state = class_2246.field_10599.method_9564();
                    break;
                }
                case field_7958: {
                    state = class_2246.field_10274.method_9564();
                    break;
                }
                case field_7951: {
                    state = class_2246.field_10050.method_9564();
                    break;
                }
                case field_7947: {
                    state = class_2246.field_10139.method_9564();
                    break;
                }
                case field_7961: {
                    state = class_2246.field_10318.method_9564();
                    break;
                }
                case field_7954: {
                    state = class_2246.field_10531.method_9564();
                    break;
                }
                case field_7944: {
                    state = class_2246.field_10267.method_9564();
                    break;
                }
                case field_7967: {
                    state = class_2246.field_10604.method_9564();
                    break;
                }
                case field_7955: {
                    state = class_2246.field_10372.method_9564();
                    break;
                }
                case field_7945: {
                    state = class_2246.field_10054.method_9564();
                    break;
                }
                case field_7966: {
                    state = class_2246.field_10067.method_9564();
                    break;
                }
                case field_7957: {
                    state = class_2246.field_10370.method_9564();
                    break;
                }
                case field_7942: {
                    state = class_2246.field_10594.method_9564();
                    break;
                }
                case field_7964: {
                    state = class_2246.field_10279.method_9564();
                    break;
                }
                case field_7963: {
                    state = class_2246.field_10537.method_9564();
                }
            }
            state = (class_2680)state.method_11657((class_2769)class_2546.field_11722, (Comparable)facing);
        }
        return state;
    };
    public static final IStateFixer FIXER_BED = (reader, state, pos) -> {
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("color", 3)) {
            int colorId = tag.method_10550("color");
            class_2350 facing = (class_2350)state.method_11654((class_2769)class_2244.field_11177);
            class_2742 part = (class_2742)state.method_11654((class_2769)class_2244.field_9967);
            Boolean occupied = (Boolean)state.method_11654((class_2769)class_2244.field_9968);
            switch (colorId) {
                case 0: {
                    state = class_2246.field_10120.method_9564();
                    break;
                }
                case 1: {
                    state = class_2246.field_10410.method_9564();
                    break;
                }
                case 2: {
                    state = class_2246.field_10230.method_9564();
                    break;
                }
                case 3: {
                    state = class_2246.field_10621.method_9564();
                    break;
                }
                case 4: {
                    state = class_2246.field_10356.method_9564();
                    break;
                }
                case 5: {
                    state = class_2246.field_10180.method_9564();
                    break;
                }
                case 6: {
                    state = class_2246.field_10610.method_9564();
                    break;
                }
                case 7: {
                    state = class_2246.field_10141.method_9564();
                    break;
                }
                case 8: {
                    state = class_2246.field_10326.method_9564();
                    break;
                }
                case 9: {
                    state = class_2246.field_10109.method_9564();
                    break;
                }
                case 10: {
                    state = class_2246.field_10019.method_9564();
                    break;
                }
                case 11: {
                    state = class_2246.field_10527.method_9564();
                    break;
                }
                case 12: {
                    state = class_2246.field_10288.method_9564();
                    break;
                }
                case 13: {
                    state = class_2246.field_10561.method_9564();
                    break;
                }
                case 14: {
                    state = class_2246.field_10069.method_9564();
                    break;
                }
                case 15: {
                    state = class_2246.field_10461.method_9564();
                    break;
                }
                default: {
                    return state;
                }
            }
            state = (class_2680)((class_2680)((class_2680)state.method_11657((class_2769)class_2244.field_11177, (Comparable)facing)).method_11657((class_2769)class_2244.field_9967, (Comparable)part)).method_11657((class_2769)class_2244.field_9968, (Comparable)occupied);
        }
        return state;
    };
    public static final IStateFixer FIXER_CHRORUS_PLANT = (reader, state, pos) -> class_2283.method_9759((class_1922)reader, (class_2338)pos, (class_2680)state);
    public static final IStateFixer FIXER_DIRT_SNOWY = (reader, state, pos) -> {
        class_2248 block = reader.method_8320(pos.method_10084()).method_26204();
        return (class_2680)state.method_11657((class_2769)class_2493.field_11522, (Comparable)Boolean.valueOf(block == class_2246.field_10491 || block == class_2246.field_10477));
    };
    public static final IStateFixer FIXER_DOOR = (reader, state, pos) -> {
        if (state.method_11654((class_2769)class_2323.field_10946) == class_2756.field_12609) {
            class_2680 stateLower = reader.method_8320(pos.method_10074());
            if (stateLower.method_26204() == state.method_26204()) {
                state = (class_2680)state.method_11657((class_2769)class_2323.field_10938, (Comparable)((class_2350)stateLower.method_11654((class_2769)class_2323.field_10938)));
                state = (class_2680)state.method_11657((class_2769)class_2323.field_10945, (Comparable)((Boolean)stateLower.method_11654((class_2769)class_2323.field_10945)));
            }
        } else {
            class_2680 stateUpper = reader.method_8320(pos.method_10084());
            if (stateUpper.method_26204() == state.method_26204()) {
                state = (class_2680)state.method_11657((class_2769)class_2323.field_10941, (Comparable)((class_2750)stateUpper.method_11654((class_2769)class_2323.field_10941)));
                state = (class_2680)state.method_11657((class_2769)class_2323.field_10940, (Comparable)((Boolean)stateUpper.method_11654((class_2769)class_2323.field_10940)));
            }
        }
        return state;
    };
    public static final IStateFixer FIXER_DOUBLE_PLANT = (reader, state, pos) -> {
        class_2680 stateLower;
        if (state.method_11654((class_2769)class_2320.field_10929) == class_2756.field_12609 && (stateLower = reader.method_8320(pos.method_10074())).method_26204() instanceof class_2320) {
            state = (class_2680)stateLower.method_11657((class_2769)class_2320.field_10929, (Comparable)class_2756.field_12609);
        }
        return state;
    };
    public static final IStateFixer FIXER_FENCE = (reader, state, pos) -> {
        class_2354 fence = (class_2354)state.method_26204();
        for (class_2350 side : PositionUtils.HORIZONTAL_DIRECTIONS) {
            class_2338 posAdj = pos.method_10093(side);
            class_2680 stateAdj = reader.method_8320(posAdj);
            class_2350 sideOpposite = side.method_10153();
            boolean flag = stateAdj.method_26206((class_1922)reader, posAdj, sideOpposite);
            state = (class_2680)state.method_11657((class_2769)HORIZONTAL_CONNECTING_BLOCK_PROPS[side.method_10146()], (Comparable)Boolean.valueOf(fence.method_10184(stateAdj, flag, sideOpposite)));
        }
        return state;
    };
    public static final IStateFixer FIXER_FENCE_GATE = (reader, state, pos) -> {
        class_2349 gate = (class_2349)state.method_26204();
        class_2350 facing = (class_2350)state.method_11654((class_2769)class_2349.field_11177);
        boolean inWall = false;
        inWall = facing.method_10166() == class_2350.class_2351.field_11048 ? ((IMixinFenceGateBlock)gate).litematica_invokeIsWall(reader.method_8320(pos.method_10093(class_2350.field_11043))) || ((IMixinFenceGateBlock)gate).litematica_invokeIsWall(reader.method_8320(pos.method_10093(class_2350.field_11035))) : ((IMixinFenceGateBlock)gate).litematica_invokeIsWall(reader.method_8320(pos.method_10093(class_2350.field_11039))) || ((IMixinFenceGateBlock)gate).litematica_invokeIsWall(reader.method_8320(pos.method_10093(class_2350.field_11034)));
        return (class_2680)state.method_11657((class_2769)class_2349.field_11024, (Comparable)Boolean.valueOf(inWall));
    };
    public static final IStateFixer FIXER_FIRE = (reader, state, pos) -> class_4770.method_24416((class_1922)reader, (class_2338)pos);
    public static final IStateFixer FIXER_FLOWER_POT = (reader, state, pos) -> {
        String itemName;
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("Item", 8) && (itemName = tag.method_10558("Item")).length() > 0 && tag.method_10545("Data")) {
            int meta = tag.method_10550("Data");
            switch (itemName) {
                case "minecraft:sapling": {
                    if (meta == 0) {
                        return class_2246.field_10468.method_9564();
                    }
                    if (meta == 1) {
                        return class_2246.field_10192.method_9564();
                    }
                    if (meta == 2) {
                        return class_2246.field_10577.method_9564();
                    }
                    if (meta == 3) {
                        return class_2246.field_10304.method_9564();
                    }
                    if (meta == 4) {
                        return class_2246.field_10564.method_9564();
                    }
                    if (meta != 5) break;
                    return class_2246.field_10076.method_9564();
                }
                case "minecraft:tallgrass": {
                    if (meta == 0) {
                        return class_2246.field_10487.method_9564();
                    }
                    if (meta != 2) break;
                    return class_2246.field_10128.method_9564();
                }
                case "minecraft:red_flower": {
                    if (meta == 0) {
                        return class_2246.field_10151.method_9564();
                    }
                    if (meta == 1) {
                        return class_2246.field_9981.method_9564();
                    }
                    if (meta == 2) {
                        return class_2246.field_10162.method_9564();
                    }
                    if (meta == 3) {
                        return class_2246.field_10365.method_9564();
                    }
                    if (meta == 4) {
                        return class_2246.field_10598.method_9564();
                    }
                    if (meta == 5) {
                        return class_2246.field_10249.method_9564();
                    }
                    if (meta == 6) {
                        return class_2246.field_10400.method_9564();
                    }
                    if (meta == 7) {
                        return class_2246.field_10061.method_9564();
                    }
                    if (meta != 8) break;
                    return class_2246.field_10074.method_9564();
                }
                case "minecraft:yellow_flower": {
                    return class_2246.field_10354.method_9564();
                }
                case "minecraft:brown_mushroom": {
                    return class_2246.field_10324.method_9564();
                }
                case "minecraft:red_mushroom": {
                    return class_2246.field_10138.method_9564();
                }
                case "minecraft:deadbush": {
                    return class_2246.field_10487.method_9564();
                }
                case "minecraft:cactus": {
                    return class_2246.field_10018.method_9564();
                }
                default: {
                    return state;
                }
            }
        }
        return state;
    };
    public static final IStateFixer FIXER_NOTE_BLOCK = (reader, state, pos) -> {
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null) {
            state = (class_2680)((class_2680)((class_2680)state.method_11657((class_2769)class_2428.field_11326, (Comparable)Boolean.valueOf(tag.method_10577("powered")))).method_11657((class_2769)class_2428.field_11324, (Comparable)Integer.valueOf(class_3532.method_15340((int)tag.method_10571("note"), (int)0, (int)24)))).method_11657((class_2769)class_2428.field_11325, (Comparable)reader.method_8320(pos.method_10074()).method_51364());
        }
        return state;
    };
    public static final IStateFixer FIXER_PANE = (reader, state, pos) -> {
        class_2389 pane = (class_2389)state.method_26204();
        for (class_2350 side : PositionUtils.HORIZONTAL_DIRECTIONS) {
            class_2338 posAdj = pos.method_10093(side);
            class_2680 stateAdj = reader.method_8320(posAdj);
            class_2350 sideOpposite = side.method_10153();
            boolean flag = stateAdj.method_26206((class_1922)reader, posAdj, sideOpposite);
            state = (class_2680)state.method_11657((class_2769)HORIZONTAL_CONNECTING_BLOCK_PROPS[side.method_10146()], (Comparable)Boolean.valueOf(pane.method_10281(stateAdj, flag)));
        }
        return state;
    };
    public static final IStateFixer FIXER_REDSTONE_REPEATER = (reader, state, pos) -> (class_2680)state.method_11657((class_2769)class_2462.field_11452, (Comparable)Boolean.valueOf(SchematicConversionFixers.getIsRepeaterPoweredOnSide(reader, pos, state)));
    public static final IStateFixer FIXER_REDSTONE_WIRE = (reader, state, pos) -> {
        class_2457 wire = (class_2457)state.method_26204();
        class_2680 stateAdj = ((IMixinRedstoneWireBlock)wire).litematica_GetPlacementState(reader, state, pos);
        if (!stateAdj.equals(state)) {
            stateAdj = state;
        }
        if (!stateAdj.equals(REDSTONE_WIRE_DOT) && stateAdj.method_11657((class_2769)class_2457.field_11432, (Comparable)Integer.valueOf(0)) == REDSTONE_WIRE_DOT_OLD) {
            stateAdj = (class_2680)REDSTONE_WIRE_CROSS.method_11657((class_2769)class_2457.field_11432, (Comparable)((Integer)stateAdj.method_11654((class_2769)class_2457.field_11432)));
        }
        return stateAdj;
    };
    public static final IStateFixer FIXER_SIGN = (reader, state, pos) -> {
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("Text1", 8)) {
            class_2499 textList = new class_2499();
            textList.add((Object)tag.method_10580("Text1"));
            textList.add((Object)tag.method_10580("Text2"));
            textList.add((Object)tag.method_10580("Text3"));
            textList.add((Object)tag.method_10580("Text4"));
            class_2487 frontTextTag = new class_2487();
            frontTextTag.method_10566("messages", (class_2520)textList);
            frontTextTag.method_10582("color", tag.method_10558("Color"));
            frontTextTag.method_10567("has_glowing_text", tag.method_10571("GlowingText"));
            tag.method_10566("front_text", (class_2520)frontTextTag);
            tag.method_10551("Color");
            tag.method_10551("GlowingText");
            tag.method_10551("Text1");
            tag.method_10551("Text2");
            tag.method_10551("Text3");
            tag.method_10551("Text4");
        }
        return state;
    };
    public static final IStateFixer FIXER_SKULL = (reader, state, pos) -> {
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10545("SkullType")) {
            int id = class_3532.method_15340((int)tag.method_10571("SkullType"), (int)0, (int)5);
            if (id == 2) {
                id = 3;
            } else if (id == 3) {
                id = 2;
            }
            class_2484.class_2485 typeOrig = ((class_2190)state.method_26204()).method_9327();
            class_2484.class_2486 typeFromData = class_2484.class_2486.values()[id];
            if (typeOrig != typeFromData) {
                if (typeFromData == class_2484.class_2486.field_11512) {
                    state = class_2246.field_10481.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11513) {
                    state = class_2246.field_10177.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11510) {
                    state = class_2246.field_10432.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11508) {
                    state = class_2246.field_10241.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11507) {
                    state = class_2246.field_10042.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11511) {
                    state = class_2246.field_10337.method_9564();
                }
            }
            state = (class_2680)state.method_11657((class_2769)class_2215.field_9924, (Comparable)Integer.valueOf(class_3532.method_15340((int)tag.method_10571("Rot"), (int)0, (int)15)));
        }
        return state;
    };
    public static final IStateFixer FIXER_SKULL_WALL = (reader, state, pos) -> {
        class_2487 tag = reader.getBlockEntityData(pos);
        if (tag != null && tag.method_10573("SkullType", 1)) {
            int id = class_3532.method_15340((int)tag.method_10571("SkullType"), (int)0, (int)5);
            if (id == 2) {
                id = 3;
            } else if (id == 3) {
                id = 2;
            }
            class_2484.class_2485 typeOrig = ((class_2190)state.method_26204()).method_9327();
            class_2484.class_2486 typeFromData = class_2484.class_2486.values()[id];
            if (typeOrig != typeFromData) {
                class_2350 facing = (class_2350)state.method_11654((class_2769)class_2549.field_11724);
                if (typeFromData == class_2484.class_2486.field_11512) {
                    state = class_2246.field_10388.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11513) {
                    state = class_2246.field_10101.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11510) {
                    state = class_2246.field_10208.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11508) {
                    state = class_2246.field_10581.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11507) {
                    state = class_2246.field_10509.method_9564();
                } else if (typeFromData == class_2484.class_2486.field_11511) {
                    state = class_2246.field_10472.method_9564();
                }
                state = (class_2680)state.method_11657((class_2769)class_2549.field_11724, (Comparable)facing);
            }
        }
        return state;
    };
    public static final IStateFixer FIXER_STAIRS = (reader, state, pos) -> (class_2680)state.method_11657((class_2769)class_2510.field_11565, (Comparable)IMixinStairsBlock.litematica_invokeGetStairShape(state, reader, pos));
    public static final IStateFixer FIXER_STEM = (reader, state, pos) -> state;
    public static final IStateFixer FIXER_TRIPWIRE = (reader, state, pos) -> {
        class_2538 wire = (class_2538)state.method_26204();
        return (class_2680)((class_2680)((class_2680)((class_2680)state.method_11657((class_2769)class_2538.field_11675, (Comparable)Boolean.valueOf(wire.method_10778(reader.method_8320(pos.method_10095()), class_2350.field_11043)))).method_11657((class_2769)class_2538.field_11678, (Comparable)Boolean.valueOf(wire.method_10778(reader.method_8320(pos.method_10072()), class_2350.field_11035)))).method_11657((class_2769)class_2538.field_11674, (Comparable)Boolean.valueOf(wire.method_10778(reader.method_8320(pos.method_10067()), class_2350.field_11039)))).method_11657((class_2769)class_2538.field_11673, (Comparable)Boolean.valueOf(wire.method_10778(reader.method_8320(pos.method_10078()), class_2350.field_11034)));
    };
    public static final IStateFixer FIXER_VINE = (reader, state, pos) -> {
        class_2541 vine = (class_2541)state.method_26204();
        return (class_2680)state.method_11657((class_2769)class_2541.field_11703, (Comparable)Boolean.valueOf(((IMixinVineBlock)vine).litematica_invokeShouldConnectUp(reader, pos.method_10084(), class_2350.field_11036)));
    };

    private static boolean getIsRepeaterPoweredOnSide(class_1922 reader, class_2338 pos, class_2680 stateRepeater) {
        class_2350 facing = (class_2350)stateRepeater.method_11654((class_2769)class_2462.field_11177);
        class_2350 sideLeft = facing.method_10160();
        class_2350 sideRight = facing.method_10170();
        return SchematicConversionFixers.getRepeaterPowerOnSide(reader, pos.method_10093(sideLeft), sideLeft) > 0 || SchematicConversionFixers.getRepeaterPowerOnSide(reader, pos.method_10093(sideRight), sideRight) > 0;
    }

    private static int getRepeaterPowerOnSide(class_1922 reader, class_2338 pos, class_2350 side) {
        class_2680 state = reader.method_8320(pos);
        class_2248 block = state.method_26204();
        if (class_2312.method_9999((class_2680)state)) {
            if (block == class_2246.field_10002) {
                return 15;
            }
            return block == class_2246.field_10091 ? ((Integer)state.method_11654((class_2769)class_2457.field_11432)).intValue() : state.method_26203(reader, pos, side);
        }
        return 0;
    }

    public static interface IStateFixer {
        public class_2680 fixState(IBlockReaderWithData var1, class_2680 var2, class_2338 var3);
    }
}

