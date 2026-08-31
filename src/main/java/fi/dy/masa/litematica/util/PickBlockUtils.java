/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.registry.Registry
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  fi.dy.masa.malilib.util.game.PlacementUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_5455
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.InventoryUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.game.BlockUtils;
import fi.dy.masa.malilib.util.game.PlacementUtils;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_5455;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class PickBlockUtils {
    @Nullable
    public static class_1268 doPickBlockForStack(class_1799 stack) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        boolean ignoreNbt = false;
        class_1268 hand = EntityUtils.getUsedHandForItem((class_1309)player, stack, ignoreNbt);
        if (!stack.method_7960() && hand == null) {
            fi.dy.masa.malilib.util.InventoryUtils.swapItemToMainHand((class_1799)stack, (class_310)mc);
            hand = class_1268.field_5808;
        }
        if (hand != null) {
            InventoryUtils.preRestockHand((class_1657)player, hand, 6, true);
        }
        return hand;
    }

    @Nullable
    public static class_1268 pickBlockLast() {
        class_310 mc = class_310.method_1551();
        class_638 world = mc.field_1687;
        class_2338 pos = Registry.BLOCK_PLACEMENT_POSITION_HANDLER.getCurrentPlacementPosition();
        if (mc.field_1724 == null) {
            return null;
        }
        if (pos == null) {
            double reach = mc.field_1724.method_55754();
            class_1297 entity = mc.method_1560();
            pos = RayTraceUtils.getPickBlockLastTrace((class_1937)world, entity, reach, true);
        }
        if (pos != null && PlacementUtils.isReplaceable((class_1937)world, (class_2338)pos, (boolean)true)) {
            return PickBlockUtils.doPickBlockForPosition(pos);
        }
        return null;
    }

    @Nullable
    private static class_1268 doPickBlockForPosition(class_2338 pos) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        class_638 clientWorld = mc.field_1687;
        if (world == null || clientWorld == null) {
            return null;
        }
        class_2680 state = world.method_8320(pos);
        class_1799 stack = MaterialCache.getInstance().getRequiredBuildItemForState(state, world, pos);
        boolean ignoreNbt = false;
        if (!stack.method_7960()) {
            class_1268 hand = EntityUtils.getUsedHandForItem((class_1309)player, stack, ignoreNbt);
            if (hand == null) {
                class_2586 te;
                if (player.method_7337() && GuiBase.isCtrlDown() && (te = world.method_8321(pos)) != null && mc.field_1687.method_22347(pos)) {
                    stack = stack.method_7972();
                    BlockUtils.setStackNbt((class_1799)stack, (class_2586)te, (class_5455)clientWorld.method_30349());
                }
                return PickBlockUtils.doPickBlockForStack(stack);
            }
            return hand;
        }
        return null;
    }
}

