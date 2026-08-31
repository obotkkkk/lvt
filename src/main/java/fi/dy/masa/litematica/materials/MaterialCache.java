/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  javax.annotation.Nullable
 *  net.minecraft.class_10376
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.class_1937
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2320
 *  net.minecraft.class_2323
 *  net.minecraft.class_2338
 *  net.minecraft.class_2362
 *  net.minecraft.class_2382
 *  net.minecraft.class_2404
 *  net.minecraft.class_2472
 *  net.minecraft.class_2482
 *  net.minecraft.class_2488
 *  net.minecraft.class_2542
 *  net.minecraft.class_2680
 *  net.minecraft.class_2742
 *  net.minecraft.class_2756
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_4538
 *  net.minecraft.class_5544
 */
package fi.dy.masa.litematica.materials;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.mixin.block.IMixinAbstractBlock;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import java.util.IdentityHashMap;
import javax.annotation.Nullable;
import net.minecraft.class_10376;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_1937;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2320;
import net.minecraft.class_2323;
import net.minecraft.class_2338;
import net.minecraft.class_2362;
import net.minecraft.class_2382;
import net.minecraft.class_2404;
import net.minecraft.class_2472;
import net.minecraft.class_2482;
import net.minecraft.class_2488;
import net.minecraft.class_2542;
import net.minecraft.class_2680;
import net.minecraft.class_2742;
import net.minecraft.class_2756;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_4538;
import net.minecraft.class_5544;

public class MaterialCache {
    private static final MaterialCache INSTANCE = new MaterialCache();
    protected final IdentityHashMap<class_2680, class_1799> buildItemsForStates = new IdentityHashMap();
    protected final IdentityHashMap<class_2680, class_1799> displayItemsForStates = new IdentityHashMap();
    protected final WorldSchematic tempWorld = SchematicWorldHandler.createSchematicWorld(null);
    protected final class_2338 checkPos = new class_2338(8, 0, 8);

    private MaterialCache() {
        WorldUtils.loadChunksSchematicWorld(this.tempWorld, this.checkPos, new class_2382(1, 1, 1));
    }

    public static MaterialCache getInstance() {
        return INSTANCE;
    }

    public void clearCache() {
        this.buildItemsForStates.clear();
        this.displayItemsForStates.clear();
    }

    public class_1799 getRequiredBuildItemForState(class_2680 state) {
        return this.getRequiredBuildItemForState(state, this.tempWorld, this.checkPos);
    }

    public class_1799 getRequiredBuildItemForState(class_2680 state, class_1937 world, class_2338 pos) {
        class_1799 stack = this.buildItemsForStates.get(state);
        if (stack == null) {
            stack = this.getItemForStateFromWorld(state, world, pos, true);
        }
        return stack;
    }

    public class_1799 getItemForDisplayNameForState(class_2680 state) {
        class_1799 stack = this.displayItemsForStates.get(state);
        if (stack == null) {
            stack = this.getItemForStateFromWorld(state, this.tempWorld, this.checkPos, false);
        }
        return stack;
    }

    protected class_1799 getItemForStateFromWorld(class_2680 state, class_1937 world, class_2338 pos, boolean isBuildItem) {
        class_1799 stack;
        class_1799 class_17992 = stack = isBuildItem ? this.getStateToItemOverride(state) : null;
        if (stack == null) {
            world.method_8652(pos, state, 20);
            stack = ((IMixinAbstractBlock)state.method_26204()).litematica_getPickStack((class_4538)world, pos, state, false);
        }
        if (stack == null || stack.method_7960()) {
            stack = class_1799.field_8037;
        } else {
            this.overrideStackSize(state, stack);
        }
        if (isBuildItem) {
            this.buildItemsForStates.put(state, stack);
        } else {
            this.displayItemsForStates.put(state, stack);
        }
        return stack;
    }

    public boolean requiresMultipleItems(class_2680 state) {
        class_2248 block = state.method_26204();
        return block instanceof class_2362 && block != class_2246.field_10495;
    }

    public ImmutableList<class_1799> getItems(class_2680 state) {
        return this.getItems(state, this.tempWorld, this.checkPos);
    }

    public ImmutableList<class_1799> getItems(class_2680 state, class_1937 world, class_2338 pos) {
        class_2248 block = state.method_26204();
        if (block instanceof class_2362 && block != class_2246.field_10495) {
            return ImmutableList.of((Object)new class_1799((class_1935)class_2246.field_10495), (Object)((IMixinAbstractBlock)block).litematica_getPickStack((class_4538)world, pos, state, false));
        }
        return ImmutableList.of((Object)this.getRequiredBuildItemForState(state, world, pos));
    }

    @Nullable
    protected class_1799 getStateToItemOverride(class_2680 state) {
        class_2248 block = state.method_26204();
        if (block == class_2246.field_10379 || block == class_2246.field_10008 || block == class_2246.field_10316 || block == class_2246.field_10027 || block == class_2246.field_10613) {
            return class_1799.field_8037;
        }
        if (block == class_2246.field_10362) {
            return new class_1799((class_1935)class_2246.field_10566);
        }
        if (block == class_2246.field_10580) {
            return new class_1799((class_1935)class_2246.field_10580);
        }
        if (block == class_2246.field_10240) {
            return new class_1799((class_1935)class_2246.field_10240);
        }
        if (block == class_2246.field_10164) {
            if ((Integer)state.method_11654((class_2769)class_2404.field_11278) == 0) {
                return new class_1799((class_1935)class_1802.field_8187);
            }
            return class_1799.field_8037;
        }
        if (block == class_2246.field_10382) {
            if ((Integer)state.method_11654((class_2769)class_2404.field_11278) == 0) {
                return new class_1799((class_1935)class_1802.field_8705);
            }
            return class_1799.field_8037;
        }
        if (block instanceof class_2323 && state.method_11654((class_2769)class_2323.field_10946) == class_2756.field_12609) {
            return class_1799.field_8037;
        }
        if (block instanceof class_2244 && state.method_11654((class_2769)class_2244.field_9967) == class_2742.field_12560) {
            return class_1799.field_8037;
        }
        if (block instanceof class_2320 && state.method_11654((class_2769)class_2320.field_10929) == class_2756.field_12609) {
            return class_1799.field_8037;
        }
        return null;
    }

    protected void overrideStackSize(class_2680 state, class_1799 stack) {
        class_2248 block = state.method_26204();
        if (block instanceof class_2482 && state.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12682) {
            stack.method_7939(2);
        } else if (block == class_2246.field_10477) {
            stack.method_7939(((Integer)state.method_11654((class_2769)class_2488.field_11518)).intValue());
        } else if (block instanceof class_2542) {
            stack.method_7939(((Integer)state.method_11654((class_2769)class_2542.field_11710)).intValue());
        } else if (block instanceof class_2472) {
            stack.method_7939(((Integer)state.method_11654((class_2769)class_2472.field_11472)).intValue());
        } else if (block instanceof class_5544) {
            stack.method_7939(((Integer)state.method_11654((class_2769)class_5544.field_27174)).intValue());
        } else if (block instanceof class_10376) {
            stack.method_7939(class_10376.method_41440((class_2680)state).size());
        }
    }
}

