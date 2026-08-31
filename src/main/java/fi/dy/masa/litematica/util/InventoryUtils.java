/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.render.InventoryOverlay
 *  fi.dy.masa.malilib.render.InventoryOverlay$Context
 *  fi.dy.masa.malilib.render.InventoryOverlay$Refresher
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1263
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1723
 *  net.minecraft.class_1735
 *  net.minecraft.class_1766
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2343
 *  net.minecraft.class_2371
 *  net.minecraft.class_2487
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_3218
 *  net.minecraft.class_5455
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_746
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.util.EasyPlaceUtils;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.render.InventoryOverlay;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_1263;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1766;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2343;
import net.minecraft.class_2371;
import net.minecraft.class_2487;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_5455;
import net.minecraft.class_7225;
import net.minecraft.class_746;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

public class InventoryUtils {
    private static final List<Integer> PICK_BLOCKABLE_SLOTS = new ArrayList<Integer>();
    private static int nextPickSlotIndex;
    private static Pair<class_2338, InventoryOverlay.Context> lastBlockEntityContext;

    public static void setPickBlockableSlots(String configStr) {
        String[] parts;
        PICK_BLOCKABLE_SLOTS.clear();
        for (String str : parts = configStr.split(",")) {
            try {
                int slotNum = Integer.parseInt(str) - 1;
                if (!class_1661.method_7380((int)slotNum) || PICK_BLOCKABLE_SLOTS.contains(slotNum)) continue;
                PICK_BLOCKABLE_SLOTS.add(slotNum);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    public static void setPickedItemToHand(class_1799 stack, class_310 mc) {
        if (mc.field_1724 == null) {
            return;
        }
        int slotNum = mc.field_1724.method_31548().method_7395(stack);
        InventoryUtils.setPickedItemToHand(slotNum, stack, mc);
    }

    public static void setPickedItemToHand(int sourceSlot, class_1799 stack, class_310 mc) {
        if (mc.field_1724 == null) {
            return;
        }
        class_746 player = mc.field_1724;
        class_1661 inventory = player.method_31548();
        if (class_1661.method_7380((int)sourceSlot)) {
            inventory.field_7545 = sourceSlot;
        } else {
            if (PICK_BLOCKABLE_SLOTS.size() == 0) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.warn.pickblock.no_valid_slots_configured", (Object[])new Object[0]);
                return;
            }
            int hotbarSlot = sourceSlot;
            if (sourceSlot == -1 || !class_1661.method_7380((int)sourceSlot)) {
                hotbarSlot = InventoryUtils.getEmptyPickBlockableHotbarSlot(inventory);
            }
            if (hotbarSlot == -1) {
                hotbarSlot = InventoryUtils.getPickBlockTargetSlot((class_1657)player);
            }
            if (hotbarSlot != -1) {
                inventory.field_7545 = hotbarSlot;
                if (EntityUtils.isCreativeMode((class_1657)player)) {
                    inventory.field_7547.set(hotbarSlot, (Object)stack.method_7972());
                } else {
                    fi.dy.masa.malilib.util.InventoryUtils.swapItemToMainHand((class_1799)stack.method_7972(), (class_310)mc);
                }
                EasyPlaceUtils.setEasyPlaceLastPickBlockTime();
            } else {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.warn.pickblock.no_suitable_slot_found", (Object[])new Object[0]);
            }
        }
    }

    public static int getPickedItemHandSlotNoSwap(class_1799 stack, class_310 mc) {
        if (mc.field_1724 == null) {
            return -1;
        }
        int slotNum = mc.field_1724.method_31548().method_7395(stack);
        return InventoryUtils.getPickedItemHandSlotNoSwap(slotNum, stack, mc);
    }

    public static int getPickedItemHandSlotNoSwap(int sourceSlot, class_1799 stack, class_310 mc) {
        if (mc.field_1724 == null) {
            return -1;
        }
        class_746 player = mc.field_1724;
        class_1661 inventory = player.method_31548();
        if (class_1661.method_7380((int)sourceSlot)) {
            inventory.method_61496(sourceSlot);
        } else {
            if (PICK_BLOCKABLE_SLOTS.size() == 0) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.warn.pickblock.no_valid_slots_configured", (Object[])new Object[0]);
                return -1;
            }
            int hotbarSlot = sourceSlot;
            if (sourceSlot == -1 || !class_1661.method_7380((int)sourceSlot)) {
                hotbarSlot = InventoryUtils.getEmptyPickBlockableHotbarSlot(inventory);
            }
            if (hotbarSlot == -1) {
                hotbarSlot = InventoryUtils.getPickBlockTargetSlot((class_1657)player);
            }
            if (hotbarSlot != -1) {
                int resultSlot = -1;
                inventory.method_61496(hotbarSlot);
                resultSlot = EntityUtils.isCreativeMode((class_1657)player) ? hotbarSlot : InventoryUtils.getMainHandSlotForItem(stack.method_7972(), mc);
                if (resultSlot != -1) {
                    EasyPlaceUtils.setEasyPlaceLastPickBlockTime();
                }
                return resultSlot;
            }
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.warn.pickblock.no_suitable_slot_found", (Object[])new Object[0]);
        }
        return -1;
    }

    private static int getMainHandSlotForItem(class_1799 stackReference, class_310 mc) {
        class_746 player = mc.field_1724;
        if (mc.field_1724 == null) {
            return -1;
        }
        boolean isCreative = player.method_56992();
        if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreNbt((class_1799)stackReference, (class_1799)player.method_6047())) {
            return -1;
        }
        if (isCreative) {
            player.method_31548().method_61496(player.method_31548().method_7386());
            return 36 + player.method_31548().field_7545;
        }
        return fi.dy.masa.malilib.util.InventoryUtils.findSlotWithItem((class_1703)player.field_7498, (class_1799)stackReference, (boolean)true);
    }

    public static void schematicWorldPickBlock(class_1799 stack, class_2338 pos, class_1937 schematicWorld, class_310 mc) {
        if (mc.field_1724 == null || mc.field_1761 == null || mc.field_1687 == null) {
            return;
        }
        if (!stack.method_7960()) {
            class_1661 inv = mc.field_1724.method_31548();
            stack = stack.method_7972();
            if (EntityUtils.isCreativeMode((class_1657)mc.field_1724)) {
                class_2586 te = schematicWorld.method_8321(pos);
                if (GuiBase.isCtrlDown() && te != null && mc.field_1687.method_22347(pos)) {
                    BlockUtils.setStackNbt((class_1799)stack, (class_2586)te, (class_5455)schematicWorld.method_30349());
                }
                InventoryUtils.setPickedItemToHand(stack, mc);
                mc.field_1761.method_2909(mc.field_1724.method_5998(class_1268.field_5808), 36 + inv.field_7545);
            } else {
                boolean shouldPick;
                int slot = inv.method_7395(stack);
                boolean bl = shouldPick = inv.field_7545 != slot;
                if (shouldPick && slot != -1) {
                    InventoryUtils.setPickedItemToHand(stack, mc);
                } else if (slot == -1 && Configs.Generic.PICK_BLOCK_SHULKERS.getBooleanValue() && (slot = InventoryUtils.findSlotWithBoxWithItem((class_1703)mc.field_1724.field_7498, stack, false)) != -1) {
                    class_1799 boxStack = ((class_1735)mc.field_1724.field_7498.field_7761.get(slot)).method_7677();
                    InventoryUtils.setPickedItemToHand(boxStack, mc);
                }
            }
        }
    }

    private static boolean canPickToSlot(class_1661 inventory, int slotNum) {
        if (!PICK_BLOCKABLE_SLOTS.contains(slotNum)) {
            return false;
        }
        class_1799 stack = inventory.method_5438(slotNum);
        if (stack.method_7960()) {
            return true;
        }
        return !(Configs.Generic.PICK_BLOCK_AVOID_DAMAGEABLE.getBooleanValue() && stack.method_7963() || Configs.Generic.PICK_BLOCK_AVOID_TOOLS.getBooleanValue() && stack.method_7909() instanceof class_1766);
    }

    private static int getPickBlockTargetSlot(class_1657 player) {
        if (PICK_BLOCKABLE_SLOTS.isEmpty() || player == null) {
            return -1;
        }
        int slotNum = player.method_31548().field_7545;
        if (InventoryUtils.canPickToSlot(player.method_31548(), slotNum)) {
            return slotNum;
        }
        if (nextPickSlotIndex >= PICK_BLOCKABLE_SLOTS.size()) {
            nextPickSlotIndex = 0;
        }
        for (int i = 0; i < PICK_BLOCKABLE_SLOTS.size(); ++i) {
            slotNum = PICK_BLOCKABLE_SLOTS.get(nextPickSlotIndex);
            if (++nextPickSlotIndex >= PICK_BLOCKABLE_SLOTS.size()) {
                nextPickSlotIndex = 0;
            }
            if (!InventoryUtils.canPickToSlot(player.method_31548(), slotNum)) continue;
            return slotNum;
        }
        return -1;
    }

    private static int getEmptyPickBlockableHotbarSlot(class_1661 inventory) {
        for (int i = 0; i < PICK_BLOCKABLE_SLOTS.size(); ++i) {
            class_1799 stack;
            int slotNum = PICK_BLOCKABLE_SLOTS.get(i);
            if (!class_1661.method_7380((int)slotNum) || !(stack = inventory.method_5438(slotNum)).method_7960()) continue;
            return slotNum;
        }
        return -1;
    }

    public static boolean doesShulkerBoxContainItem(class_1799 stack, class_1799 referenceItem) {
        class_2371 items = fi.dy.masa.malilib.util.InventoryUtils.getStoredItems((class_1799)stack);
        return InventoryUtils.doesListContainItem((class_2371<class_1799>)items, referenceItem);
    }

    public static boolean doesBundleContainItem(class_1799 stack, class_1799 referenceItem) {
        class_2371 items = fi.dy.masa.malilib.util.InventoryUtils.getBundleItems((class_1799)stack);
        return InventoryUtils.doesListContainItem((class_2371<class_1799>)items, referenceItem);
    }

    private static boolean doesListContainItem(class_2371<class_1799> items, class_1799 referenceItem) {
        if (items.size() > 0) {
            for (class_1799 item : items) {
                if (!fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreNbt((class_1799)item, (class_1799)referenceItem)) continue;
                return true;
            }
        }
        return false;
    }

    public static int findSlotWithBoxWithItem(class_1703 container, class_1799 stackReference, boolean reverse) {
        int startSlot = reverse ? container.field_7761.size() - 1 : 0;
        int endSlot = reverse ? -1 : container.field_7761.size();
        int increment = reverse ? -1 : 1;
        boolean isPlayerInv = container instanceof class_1723;
        for (int slotNum = startSlot; slotNum != endSlot; slotNum += increment) {
            class_1735 slot = (class_1735)container.field_7761.get(slotNum);
            if (isPlayerInv && !fi.dy.masa.malilib.util.InventoryUtils.isRegularInventorySlot((int)slot.field_7874, (boolean)false) || !InventoryUtils.doesShulkerBoxContainItem(slot.method_7677(), stackReference)) continue;
            return slot.field_7874;
        }
        return -1;
    }

    @Nullable
    public static InventoryOverlay.Context getTargetInventory(class_1937 world, class_2338 pos) {
        class_2680 state = world.method_8320(pos);
        class_2248 blockTmp = state.method_26204();
        class_2487 nbt = new class_2487();
        class_2586 be = null;
        if (blockTmp instanceof class_2343) {
            if (world instanceof class_3218 || world instanceof WorldSchematic) {
                be = world.method_8500(pos).method_8321(pos);
                if (be != null) {
                    nbt = be.method_38242((class_7225.class_7874)world.method_30349());
                }
            } else {
                Pair<class_2586, class_2487> pair = EntitiesDataStorage.getInstance().requestBlockEntity(world, pos);
                if (pair != null) {
                    nbt = (class_2487)pair.getRight();
                }
            }
            InventoryOverlay.Context ctx = InventoryUtils.getTargetInventoryFromBlock(world, pos, be, nbt);
            if (world instanceof WorldSchematic) {
                return ctx;
            }
            if (lastBlockEntityContext != null && !((class_2338)lastBlockEntityContext.getLeft()).equals((Object)pos)) {
                lastBlockEntityContext = null;
            }
            if (ctx != null && ctx.inv() != null) {
                lastBlockEntityContext = Pair.of((Object)pos, (Object)ctx);
                return ctx;
            }
            if (lastBlockEntityContext != null && ((class_2338)lastBlockEntityContext.getLeft()).equals((Object)pos)) {
                return (InventoryOverlay.Context)lastBlockEntityContext.getRight();
            }
        }
        return null;
    }

    @Nullable
    private static InventoryOverlay.Context getTargetInventoryFromBlock(class_1937 world, class_2338 pos, @Nullable class_2586 be, class_2487 nbt) {
        class_1263 inv;
        if (be != null) {
            if (nbt.method_33133()) {
                nbt = be.method_38242((class_7225.class_7874)world.method_30349());
            }
            inv = fi.dy.masa.malilib.util.InventoryUtils.getInventory((class_1937)world, (class_2338)pos);
        } else {
            Pair<class_2586, class_2487> pair;
            if (nbt.method_33133() && (pair = EntitiesDataStorage.getInstance().requestBlockEntity(world, pos)) != null) {
                nbt = (class_2487)pair.getRight();
            }
            inv = EntitiesDataStorage.getInstance().getBlockInventory(world, pos, false);
        }
        if (nbt != null && !nbt.method_33133()) {
            class_1263 inv2 = fi.dy.masa.malilib.util.InventoryUtils.getNbtInventory((class_2487)nbt, (int)(inv != null ? inv.method_5439() : -1), (class_7225.class_7874)world.method_30349());
            if (inv == null) {
                inv = inv2;
            }
        }
        if (inv == null || nbt == null) {
            return null;
        }
        return new InventoryOverlay.Context(InventoryOverlay.getBestInventoryType((class_1263)inv, (class_2487)nbt), inv, be != null ? be : world.method_8321(pos), null, nbt, (InventoryOverlay.Refresher)new Refresher());
    }

    @Nullable
    public static String convertItemNbtToString(class_2487 nbt) {
        int count;
        StringBuilder result = new StringBuilder();
        if (nbt.method_33133()) {
            return null;
        }
        if (!nbt.method_10545("id")) {
            return null;
        }
        result.append(nbt.method_10558("id"));
        if (nbt.method_10545("components")) {
            class_2487 components = nbt.method_10562("components");
            int count2 = 0;
            result.append("[");
            for (String key : components.method_10541()) {
                if (count2 > 0) {
                    result.append(", ");
                }
                result.append(key);
                result.append("=");
                result.append(components.method_10580(key));
                ++count2;
            }
            result.append("]");
        }
        if (nbt.method_10545("count") && (count = nbt.method_10550("count")) > 1) {
            result.append(" ");
            result.append(count);
        }
        return result.toString();
    }

    @ApiStatus.Experimental
    public static void preRestockHand(class_1657 player, class_1268 hand, int threshold, boolean allowHotbar) {
        if (player == null) {
            return;
        }
        class_1661 container = player.method_31548();
        class_1799 handStack = player.method_5998(hand);
        int count = handStack.method_7947();
        int max = handStack.method_7914();
        if (!handStack.method_7960() && InventoryUtils.getCursorStack().method_7960() && count <= threshold && count < max) {
            int endSlot = allowHotbar ? 44 : 35;
            int currentMainHandSlot = InventoryUtils.getSelectedHotbarSlot() + 36;
            int currentSlot = hand == class_1268.field_5808 ? currentMainHandSlot : 45;
            for (int slotNum = 9; slotNum <= endSlot; ++slotNum) {
                if (slotNum == currentMainHandSlot) continue;
                class_310 mc = class_310.method_1551();
                class_1723 handler = player.field_7498;
                class_1735 slot = (class_1735)handler.field_7761.get(slotNum);
                class_1799 stackSlot = container.method_5438(slotNum);
                if (!fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability((class_1799)stackSlot, (class_1799)handStack)) continue;
                int button = stackSlot.method_7947() + count <= max ? 0 : 1;
                mc.field_1761.method_2906(handler.field_7763, slot.field_7874, button, class_1713.field_7790, player);
                mc.field_1761.method_2906(handler.field_7763, currentSlot, 0, class_1713.field_7790, player);
                break;
            }
        }
    }

    @ApiStatus.Experimental
    public static class_1799 getCursorStack() {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            return class_1799.field_8037;
        }
        class_1661 inv = player.method_31548();
        return inv != null ? inv.method_7391() : class_1799.field_8037;
    }

    @ApiStatus.Experimental
    public static int getSelectedHotbarSlot() {
        class_746 player = class_310.method_1551().field_1724;
        if (player == null) {
            return 0;
        }
        class_1661 inv = player.method_31548();
        return inv != null ? inv.field_7545 : 0;
    }

    static {
        lastBlockEntityContext = null;
    }

    public static class Refresher
    implements InventoryOverlay.Refresher {
        public InventoryOverlay.Context onContextRefresh(InventoryOverlay.Context data, class_1937 world) {
            if (data.be() != null) {
                InventoryUtils.getTargetInventory(world, data.be().method_11016());
                data = InventoryUtils.getTargetInventoryFromBlock(data.be().method_10997(), data.be().method_11016(), data.be(), data.nbt());
            }
            return data;
        }
    }
}

