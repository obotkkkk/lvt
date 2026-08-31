/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.ItemType
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  net.minecraft.class_1263
 *  net.minecraft.class_1657
 *  net.minecraft.class_1747
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.class_2371
 *  net.minecraft.class_2382
 *  net.minecraft.class_2480
 *  net.minecraft.class_2680
 *  net.minecraft.class_2741
 *  net.minecraft.class_2769
 *  net.minecraft.class_310
 *  net.minecraft.class_5537
 */
package fi.dy.masa.litematica.materials;

import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.class_1263;
import net.minecraft.class_1657;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_2371;
import net.minecraft.class_2382;
import net.minecraft.class_2480;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2769;
import net.minecraft.class_310;
import net.minecraft.class_5537;

public class MaterialListUtils {
    public static List<MaterialListEntry> createMaterialListFor(LitematicaSchematic schematic) {
        return MaterialListUtils.createMaterialListFor(schematic, schematic.getAreas().keySet());
    }

    public static List<MaterialListEntry> createMaterialListFor(LitematicaSchematic schematic, Collection<String> subRegions) {
        Object2IntOpenHashMap countsTotal = new Object2IntOpenHashMap();
        for (String regionName : subRegions) {
            LitematicaBlockStateContainer container = schematic.getSubRegionContainer(regionName);
            if (container == null) continue;
            class_2382 size = container.getSize();
            int sizeX = size.method_10263();
            int sizeY = size.method_10264();
            int sizeZ = size.method_10260();
            for (int y = 0; y < sizeY; ++y) {
                for (int z = 0; z < sizeZ; ++z) {
                    for (int x = 0; x < sizeX; ++x) {
                        class_2680 state = container.get(x, y, z);
                        countsTotal.addTo((Object)state, 1);
                    }
                }
            }
        }
        class_310 mc = class_310.method_1551();
        return MaterialListUtils.getMaterialList((Object2IntOpenHashMap<class_2680>)countsTotal, (Object2IntOpenHashMap<class_2680>)countsTotal, (Object2IntOpenHashMap<class_2680>)new Object2IntOpenHashMap(), (class_1657)mc.field_1724);
    }

    public static List<MaterialListEntry> getMaterialList(Object2IntOpenHashMap<class_2680> countsTotal, Object2IntOpenHashMap<class_2680> countsMissing, Object2IntOpenHashMap<class_2680> countsMismatch, class_1657 player) {
        ArrayList<MaterialListEntry> list = new ArrayList<MaterialListEntry>();
        if (player != null && !countsTotal.isEmpty()) {
            MaterialCache cache = MaterialCache.getInstance();
            Object2IntOpenHashMap itemTypesTotal = new Object2IntOpenHashMap();
            Object2IntOpenHashMap itemTypesMissing = new Object2IntOpenHashMap();
            Object2IntOpenHashMap itemTypesMismatch = new Object2IntOpenHashMap();
            MaterialListUtils.convertStatesToStacks(countsTotal, (Object2IntOpenHashMap<ItemType>)itemTypesTotal, cache);
            MaterialListUtils.convertStatesToStacks(countsMissing, (Object2IntOpenHashMap<ItemType>)itemTypesMissing, cache);
            MaterialListUtils.convertStatesToStacks(countsMismatch, (Object2IntOpenHashMap<ItemType>)itemTypesMismatch, cache);
            Object2IntOpenHashMap<ItemType> playerInvItems = MaterialListUtils.getInventoryItemCounts((class_1263)player.method_31548());
            for (ItemType type : itemTypesTotal.keySet()) {
                list.add(new MaterialListEntry(type.getStack().method_7972(), itemTypesTotal.getInt((Object)type), itemTypesMissing.getInt((Object)type), itemTypesMismatch.getInt((Object)type), playerInvItems.getInt((Object)type)));
            }
        }
        return list;
    }

    private static void convertStatesToStacks(Object2IntOpenHashMap<class_2680> blockStatesIn, Object2IntOpenHashMap<ItemType> itemTypesOut, MaterialCache cache) {
        for (class_2680 state : blockStatesIn.keySet()) {
            class_2680 stateToConvert;
            int count = blockStatesIn.getInt((Object)state);
            class_2680 class_26802 = stateToConvert = MaterialListUtils.isWaterloggedBlock(state) ? MaterialListUtils.getBaseBlockState(state) : state;
            if (MaterialListUtils.isWaterloggedBlock(state)) {
                itemTypesOut.addTo((Object)new ItemType(new class_1799((class_1935)class_1802.field_8705), false, false), count);
            }
            if (cache.requiresMultipleItems(stateToConvert)) {
                for (class_1799 stack : cache.getItems(stateToConvert)) {
                    if (stack.method_7960()) continue;
                    itemTypesOut.addTo((Object)new ItemType(stack, true, false), count * stack.method_7947());
                }
                continue;
            }
            class_1799 stack = cache.getRequiredBuildItemForState(stateToConvert);
            if (stack.method_7960()) continue;
            itemTypesOut.addTo((Object)new ItemType(stack, true, false), count * stack.method_7947());
        }
    }

    public static void updateAvailableCounts(List<MaterialListEntry> list, class_1657 player) {
        if (player == null) {
            return;
        }
        Object2IntOpenHashMap<ItemType> playerInvItems = MaterialListUtils.getInventoryItemCounts((class_1263)player.method_31548());
        for (MaterialListEntry entry : list) {
            ItemType type = new ItemType(entry.getStack(), true, false);
            int countAvailable = playerInvItems.getInt((Object)type);
            entry.setCountAvailable(countAvailable);
        }
    }

    public static Object2IntOpenHashMap<ItemType> getInventoryItemCounts(class_1263 inv) {
        Object2IntOpenHashMap map = new Object2IntOpenHashMap();
        int slots = inv.method_5439();
        for (int slot = 0; slot < slots; ++slot) {
            class_1799 stack = inv.method_5438(slot);
            if (stack.method_7960()) continue;
            class_1792 item = stack.method_7909();
            if (item instanceof class_1747 && ((class_1747)stack.method_7909()).method_7711() instanceof class_2480 && InventoryUtils.shulkerBoxHasItems((class_1799)stack)) {
                Object2IntOpenHashMap<ItemType> boxCounts = MaterialListUtils.getStoredItemCounts(stack);
                for (ItemType boxType : boxCounts.keySet()) {
                    map.addTo((Object)boxType, boxCounts.getInt((Object)boxType));
                }
                boxCounts.clear();
                continue;
            }
            if (item instanceof class_5537 && InventoryUtils.bundleHasItems((class_1799)stack)) {
                Object2IntOpenHashMap<ItemType> bundleCounts = MaterialListUtils.getBundleItemCounts(stack);
                for (ItemType bundleType : bundleCounts.keySet()) {
                    map.addTo((Object)bundleType, bundleCounts.getInt((Object)bundleType));
                }
                bundleCounts.clear();
                continue;
            }
            map.addTo((Object)new ItemType(stack, true, false), stack.method_7947());
        }
        return map;
    }

    public static Object2IntOpenHashMap<ItemType> getStoredItemCounts(class_1799 stackShulkerBox) {
        Object2IntOpenHashMap map = new Object2IntOpenHashMap();
        class_2371 items = InventoryUtils.getStoredItems((class_1799)stackShulkerBox);
        for (class_1799 boxStack : items) {
            if (boxStack.method_7960()) continue;
            map.addTo((Object)new ItemType(boxStack, false, false), boxStack.method_7947());
        }
        return map;
    }

    public static Object2IntOpenHashMap<ItemType> getBundleItemCounts(class_1799 stackBundle) {
        Object2IntOpenHashMap map = new Object2IntOpenHashMap();
        class_2371 items = InventoryUtils.getBundleItems((class_1799)stackBundle);
        for (class_1799 bundleStack : items) {
            if (bundleStack.method_7960()) continue;
            map.addTo((Object)new ItemType(bundleStack, false, false), bundleStack.method_7947());
        }
        return map;
    }

    private static boolean isWaterloggedBlock(class_2680 state) {
        return state.method_28498((class_2769)class_2741.field_12508) && (Boolean)state.method_11654((class_2769)class_2741.field_12508) != false;
    }

    private static class_2680 getBaseBlockState(class_2680 state) {
        if (state.method_28498((class_2769)class_2741.field_12508)) {
            return (class_2680)state.method_11657((class_2769)class_2741.field_12508, (Comparable)Boolean.valueOf(false));
        }
        return state;
    }
}

