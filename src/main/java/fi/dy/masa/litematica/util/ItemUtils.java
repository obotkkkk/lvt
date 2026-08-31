/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2482
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_2960
 *  net.minecraft.class_4538
 *  net.minecraft.class_7923
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.mixin.block.IMixinAbstractBlock;
import java.util.IdentityHashMap;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2482;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_2960;
import net.minecraft.class_4538;
import net.minecraft.class_7923;

public class ItemUtils {
    private static final IdentityHashMap<class_2680, class_1799> ITEMS_FOR_STATES = new IdentityHashMap();

    public static boolean areTagsEqualIgnoreDamage(class_1799 stackReference, class_1799 stackToCheck) {
        class_1799 ref = stackReference.method_7972();
        class_1799 check = stackToCheck.method_7972();
        if (ref.method_7963() && ref.method_7986()) {
            ref.method_7974(0);
        }
        if (check.method_7963() && check.method_7986()) {
            check.method_7974(0);
        }
        return class_1799.method_31577((class_1799)ref, (class_1799)check);
    }

    public static class_1799 getItemForState(class_2680 state) {
        class_1799 stack = ITEMS_FOR_STATES.get(state);
        return stack != null ? stack : class_1799.field_8037;
    }

    public static void setItemForBlock(class_1937 world, class_2338 pos, class_2680 state) {
        if (!ITEMS_FOR_STATES.containsKey(state)) {
            ITEMS_FOR_STATES.put(state, ItemUtils.getItemForBlock(world, pos, state, false));
        }
    }

    public static class_1799 getItemForBlock(class_1937 world, class_2338 pos, class_2680 state, boolean checkCache) {
        class_1799 stack;
        if (checkCache && (stack = ITEMS_FOR_STATES.get(state)) != null) {
            return stack;
        }
        if (state.method_26215()) {
            return class_1799.field_8037;
        }
        stack = ItemUtils.getStateToItemOverride(state);
        if (stack.method_7960()) {
            stack = ((IMixinAbstractBlock)state.method_26204()).litematica_getPickStack((class_4538)world, pos, state, false);
        }
        if (stack.method_7960()) {
            stack = class_1799.field_8037;
        } else {
            ItemUtils.overrideStackSize(state, stack);
        }
        ITEMS_FOR_STATES.put(state, stack);
        return stack;
    }

    public static class_1799 getStateToItemOverride(class_2680 state) {
        if (state.method_26204() == class_2246.field_10164) {
            return new class_1799((class_1935)class_1802.field_8187);
        }
        if (state.method_26204() == class_2246.field_10382) {
            return new class_1799((class_1935)class_1802.field_8705);
        }
        return class_1799.field_8037;
    }

    private static void overrideStackSize(class_2680 state, class_1799 stack) {
        if (state.method_26204() instanceof class_2482 && state.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12682) {
            stack.method_7939(2);
        }
    }

    public static String getStackString(class_1799 stack) {
        if (!stack.method_7960()) {
            class_2960 rl = class_7923.field_41178.method_10221((Object)stack.method_7909());
            return String.format("[%s - display: %s - NBT: %s] (%s)", rl != null ? rl.toString() : "null", stack.method_7964().getString(), stack.method_57353() != null ? stack.method_57353().toString() : "<no NBT>", stack);
        }
        return "<empty>";
    }
}

