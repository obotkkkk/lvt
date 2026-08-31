/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_156
 *  net.minecraft.class_1767
 *  net.minecraft.class_1799
 *  net.minecraft.class_2338
 *  net.minecraft.class_2487
 *  net.minecraft.class_2494
 *  net.minecraft.class_2499
 *  net.minecraft.class_2512
 *  net.minecraft.class_2519
 *  net.minecraft.class_2520
 *  net.minecraft.class_2561
 *  net.minecraft.class_2586
 *  net.minecraft.class_2960
 *  net.minecraft.class_5250
 *  net.minecraft.class_5455
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_8824
 */
package fi.dy.masa.litematica.schematic.conversion;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_156;
import net.minecraft.class_1767;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2487;
import net.minecraft.class_2494;
import net.minecraft.class_2499;
import net.minecraft.class_2512;
import net.minecraft.class_2519;
import net.minecraft.class_2520;
import net.minecraft.class_2561;
import net.minecraft.class_2586;
import net.minecraft.class_2960;
import net.minecraft.class_5250;
import net.minecraft.class_5455;
import net.minecraft.class_7225;
import net.minecraft.class_8824;

public class SchematicDowngradeConverter {
    public static Optional<class_2487> asCompound(class_2520 ele) {
        if (ele.method_10711() == 10) {
            return Optional.of((class_2487)ele);
        }
        return Optional.empty();
    }

    public static Optional<class_2499> asNbtList(class_2520 ele) {
        if (ele.method_10711() == 9) {
            return Optional.of((class_2499)ele);
        }
        return Optional.empty();
    }

    public static class_2487 downgradeEntity_to_1_20_4(class_2487 oldEntity, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 newEntity = new class_2487();
        if (!oldEntity.method_10545("id")) {
            return oldEntity;
        }
        Iterator iterator = oldEntity.method_10541().iterator();
        block48: while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "x": {
                    newEntity.method_10569("x", oldEntity.method_10550("x"));
                    continue block48;
                }
                case "y": {
                    newEntity.method_10569("y", oldEntity.method_10550("y"));
                    continue block48;
                }
                case "z": {
                    newEntity.method_10569("z", oldEntity.method_10550("z"));
                    continue block48;
                }
                case "id": {
                    newEntity.method_10582("id", oldEntity.method_10558("id"));
                    continue block48;
                }
                case "attributes": {
                    newEntity.method_10566("Attributes", SchematicDowngradeConverter.processAttributes(oldEntity.method_10580(key), minecraftDataVersion, registryManager));
                    continue block48;
                }
                case "flower_pos": {
                    newEntity.method_10566("FlowerPos", SchematicDowngradeConverter.processFlowerPos(oldEntity, key));
                    continue block48;
                }
                case "hive_pos": {
                    newEntity.method_10566("HivePos", SchematicDowngradeConverter.processFlowerPos(oldEntity, key));
                    continue block48;
                }
                case "ArmorItems": {
                    newEntity.method_10566("ArmorItems", (class_2520)SchematicDowngradeConverter.processEntityItems(oldEntity.method_10554(key, 10), minecraftDataVersion, registryManager, 4));
                    continue block48;
                }
                case "HandItems": {
                    newEntity.method_10566("HandItems", (class_2520)SchematicDowngradeConverter.processEntityItems(oldEntity.method_10554(key, 10), minecraftDataVersion, registryManager, 2));
                    continue block48;
                }
                case "Item": {
                    newEntity.method_10566("Item", SchematicDowngradeConverter.processEntityItem(oldEntity.method_10580(key), minecraftDataVersion, registryManager));
                    continue block48;
                }
                case "Inventory": {
                    newEntity.method_10566("Inventory", (class_2520)SchematicDowngradeConverter.processEntityItems(oldEntity.method_10554(key, 10), minecraftDataVersion, registryManager, 1));
                    continue block48;
                }
                case "equipment": {
                    newEntity.method_10543(SchematicDowngradeConverter.processEntityEquipment(oldEntity.method_10580(key), minecraftDataVersion, registryManager));
                    continue block48;
                }
                case "drop_chances": {
                    newEntity.method_10543(SchematicDowngradeConverter.processEntityDropChances(oldEntity.method_10580(key)));
                    continue block48;
                }
                case "fall_distance": {
                    newEntity.method_10548("FallDistance", oldEntity.method_10583(key));
                    continue block48;
                }
                case "anchor_pos": {
                    SchematicDowngradeConverter.processBlockPosTag(NbtUtils.readBlockPosFromIntArray((class_2487)oldEntity, (String)key), "A", newEntity);
                    continue block48;
                }
                case "block_pos": {
                    SchematicDowngradeConverter.processBlockPosTag(NbtUtils.readBlockPosFromIntArray((class_2487)oldEntity, (String)key), "Tile", newEntity);
                    continue block48;
                }
                case "bound_pos": {
                    SchematicDowngradeConverter.processBlockPosTag(NbtUtils.readBlockPosFromIntArray((class_2487)oldEntity, (String)key), "Bound", newEntity);
                    continue block48;
                }
                case "home_pos": {
                    SchematicDowngradeConverter.processBlockPosTag(NbtUtils.readBlockPosFromIntArray((class_2487)oldEntity, (String)key), "HomePos", newEntity);
                    continue block48;
                }
                case "sleeping_pos": {
                    SchematicDowngradeConverter.processBlockPosTag(NbtUtils.readBlockPosFromIntArray((class_2487)oldEntity, (String)key), "Sleeping", newEntity);
                    continue block48;
                }
                case "has_egg": {
                    newEntity.method_10556("HasEgg", oldEntity.method_10577(key));
                    continue block48;
                }
                case "life_ticks": {
                    newEntity.method_10569("LifeTicks", oldEntity.method_10550(key));
                    continue block48;
                }
                case "size": {
                    newEntity.method_10569("Size", oldEntity.method_10550(key));
                    continue block48;
                }
            }
            newEntity.method_10566(key, oldEntity.method_10580(key));
        }
        return newEntity;
    }

    private static void processBlockPosTag(@Nullable class_2338 oldPos, String prefix, class_2487 newTags) {
        if (oldPos != null) {
            newTags.method_10569(prefix + "X", oldPos.method_10263());
            newTags.method_10569(prefix + "Y", oldPos.method_10264());
            newTags.method_10569(prefix + "Z", oldPos.method_10260());
        }
    }

    private static class_2487 processEntityDropChances(class_2520 nbtElement) {
        int i;
        class_2487 oldTags = SchematicDowngradeConverter.asCompound(nbtElement).orElse(new class_2487());
        class_2487 newTags = new class_2487();
        class_2499 handDrops = new class_2499();
        class_2499 armorDrops = new class_2499();
        for (i = 0; i < 2; ++i) {
            handDrops.add((Object)class_2494.method_23244((float)0.085f));
        }
        for (i = 0; i < 4; ++i) {
            armorDrops.add((Object)class_2494.method_23244((float)0.085f));
        }
        Iterator iterator = oldTags.method_10541().iterator();
        while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "mainhand": {
                    handDrops.method_10606(0, oldTags.method_10580(key));
                    break;
                }
                case "offhand": {
                    handDrops.method_10606(1, oldTags.method_10580(key));
                    break;
                }
                case "feet": {
                    armorDrops.method_10606(0, oldTags.method_10580(key));
                    break;
                }
                case "legs": {
                    armorDrops.method_10606(1, oldTags.method_10580(key));
                    break;
                }
                case "chest": {
                    armorDrops.method_10606(2, oldTags.method_10580(key));
                    break;
                }
                case "head": {
                    armorDrops.method_10606(3, oldTags.method_10580(key));
                    break;
                }
            }
        }
        newTags.method_10566("HandDropChances", (class_2520)handDrops);
        newTags.method_10566("ArmorDropChances", (class_2520)armorDrops);
        return newTags;
    }

    private static class_2487 processEntityEquipment(class_2520 equipmentEntries, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        int i;
        class_2487 oldTags = SchematicDowngradeConverter.asCompound(equipmentEntries).orElse(new class_2487());
        class_2487 newTags = new class_2487();
        class_2499 newHandItems = new class_2499();
        class_2499 newArmorItems = new class_2499();
        for (i = 0; i < 2; ++i) {
            newHandItems.add((Object)new class_2487());
        }
        for (i = 0; i < 4; ++i) {
            newArmorItems.add((Object)new class_2487());
        }
        Iterator iterator = oldTags.method_10541().iterator();
        while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "mainhand": {
                    newHandItems.method_10606(0, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "offhand": {
                    newHandItems.method_10606(1, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "feet": {
                    newArmorItems.method_10606(0, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "legs": {
                    newArmorItems.method_10606(1, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "chest": {
                    newArmorItems.method_10606(2, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "head": {
                    newArmorItems.method_10606(3, SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "body": {
                    class_2520 ele = SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager);
                    newArmorItems.method_10606(2, ele);
                    newTags.method_10566("ArmorItem", ele);
                    break;
                }
                case "saddle": {
                    newTags.method_10566("SaddleItem", SchematicDowngradeConverter.processEntityItem(oldTags.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
            }
        }
        newTags.method_10566("HandItems", (class_2520)newHandItems);
        newTags.method_10566("ArmorItems", (class_2520)newArmorItems);
        return newTags;
    }

    private static class_2520 processEntityItem(class_2520 itemEntry, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 oldItem = SchematicDowngradeConverter.asCompound(itemEntry).orElse(new class_2487());
        class_2487 newItem = new class_2487();
        if (!oldItem.method_10545("id")) {
            return itemEntry;
        }
        String idName = oldItem.method_10558("id");
        newItem.method_10582("id", idName);
        if (oldItem.method_10545("count")) {
            newItem.method_10567("Count", (byte)oldItem.method_10550("count"));
        }
        if (oldItem.method_10545("components")) {
            newItem.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(oldItem.method_10562("components"), idName, minecraftDataVersion, registryManager));
        } else if (SchematicDowngradeConverter.needsDamageTag(idName)) {
            class_2487 newTag = new class_2487();
            newTag.method_10569("Damage", 0);
            newItem.method_10566("tag", (class_2520)newTag);
        }
        return newItem;
    }

    private static class_2499 processEntityItems(class_2499 oldItems, int minecraftDataVersion, class_5455 registryManager, int expectedSize) {
        class_2499 newItems = new class_2499();
        for (int i = 0; i < oldItems.size(); ++i) {
            class_2487 itemEntry = oldItems.method_10602(i);
            class_2487 newEntry = new class_2487();
            if (itemEntry.method_10545("id")) {
                String idName = itemEntry.method_10558("id");
                newEntry.method_10582("id", idName);
                if (itemEntry.method_10545("count")) {
                    newEntry.method_10567("Count", (byte)itemEntry.method_10550("count"));
                } else {
                    newEntry.method_10567("Count", (byte)1);
                }
                if (itemEntry.method_10545("components")) {
                    newEntry.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(itemEntry.method_10562("components"), idName, minecraftDataVersion, registryManager));
                } else if (SchematicDowngradeConverter.needsDamageTag(idName)) {
                    class_2487 newTag = new class_2487();
                    newTag.method_10569("Damage", 0);
                    newEntry.method_10566("tag", (class_2520)newTag);
                }
            }
            newItems.add((Object)newEntry);
        }
        if (newItems.size() < expectedSize) {
            int addTotal = expectedSize - newItems.size();
            for (int i = 0; i < addTotal; ++i) {
                newItems.method_10531(i, (class_2520)new class_2487());
            }
        }
        return newItems;
    }

    private static class_2520 processAttributes(class_2520 attrib, int minecraftDataVersion, class_5455 registryManager) {
        class_2499 oldAttr = SchematicDowngradeConverter.asNbtList(attrib).orElse(new class_2499());
        if (oldAttr.isEmpty()) {
            class_2487 oldTag = SchematicDowngradeConverter.asCompound(attrib).orElse(new class_2487());
            if (oldTag.method_33133()) {
                return attrib;
            }
            for (String key : oldTag.method_10541()) {
                if (!key.equals("modifiers")) continue;
                return SchematicDowngradeConverter.processAttributeModifiers(oldTag.method_10554(key, 10), minecraftDataVersion, registryManager);
            }
        }
        return SchematicDowngradeConverter.processAttributeBase(oldAttr, minecraftDataVersion, registryManager);
    }

    private static class_2520 processAttributeBase(class_2499 oldAttr, int minecraftDataVersion, class_5455 registryManager) {
        class_2499 newAttr = new class_2499();
        for (int i = 0; i < oldAttr.size(); ++i) {
            class_2487 attrEntry = oldAttr.method_10602(i);
            class_2487 newEntry = new class_2487();
            if (attrEntry.method_10545("type")) {
                newEntry.method_10582("Name", SchematicDowngradeConverter.attributeRename(attrEntry.method_10558("type")));
                newEntry.method_10549("Base", attrEntry.method_10574("amount"));
            } else {
                newEntry.method_10582("Name", SchematicDowngradeConverter.attributeRename(attrEntry.method_10558("id")));
                newEntry.method_10549("Base", attrEntry.method_10574("base"));
            }
            class_2499 listEntry = attrEntry.method_10554("modifiers", 10);
            class_2499 newMods = SchematicDowngradeConverter.processAttributeModifiers(listEntry, minecraftDataVersion, registryManager);
            if (!newMods.isEmpty()) {
                newEntry.method_10566("Modifiers", (class_2520)newMods);
            }
            newAttr.add((Object)newEntry);
        }
        return newAttr;
    }

    private static class_2499 processAttributeModifiers(class_2499 modifiers, int minecraftDataVersion, class_5455 registryManager) {
        class_2499 newMods = new class_2499();
        if (modifiers.isEmpty()) {
            return modifiers;
        }
        for (int y = 0; y < modifiers.size(); ++y) {
            class_2487 modEntry = modifiers.method_10602(y);
            class_2487 newMod = new class_2487();
            if (modEntry.method_10545("type")) {
                newMod.method_10582("Name", SchematicDowngradeConverter.attributeRename(modEntry.method_10558("type")));
                newMod.method_10549("Base", modEntry.method_10574("amount"));
            } else {
                newMod.method_10549("Amount", modEntry.method_10574("amount"));
                newMod.method_10582("Name", SchematicDowngradeConverter.modifierIdToName(modEntry.method_10558("id")));
            }
            newMod.method_10569("Operation", SchematicDowngradeConverter.modifierOperationToInt(modEntry.method_10558("operation")));
            newMod.method_25927("UUID", modEntry.method_10545("UUID") ? modEntry.method_25926("UUID") : UUID.randomUUID());
            newMods.add((Object)newMod);
        }
        return newMods;
    }

    private static String attributeRename(String idIn) {
        switch (idIn) {
            case "minecraft:armor": {
                return "minecraft:generic.armor";
            }
            case "minecraft:armor_toughness": {
                return "minecraft:generic.armor_toughness";
            }
            case "minecraft:attack_damage": {
                return "minecraft:generic.attack_damage";
            }
            case "minecraft:attack_knockback": {
                return "minecraft:generic.attack_knockback";
            }
            case "minecraft:attack_speed": {
                return "minecraft:generic.attack_speed";
            }
            case "minecraft:flying_speed": {
                return "minecraft:generic.flying_speed";
            }
            case "minecraft:follow_range": {
                return "minecraft:generic.follow_range";
            }
            case "minecraft:jump_strength": {
                return "minecraft:horse.jump_strength";
            }
            case "minecraft:knockback_resistance": {
                return "minecraft:generic.knockback_resistance";
            }
            case "minecraft:luck": {
                return "minecraft:generic.luck";
            }
            case "minecraft:max_absorption": {
                return "minecraft:generic.max_absorption";
            }
            case "minecraft:max_health": {
                return "minecraft:generic.max_health";
            }
            case "minecraft:movement_speed": {
                return "minecraft:generic.movement_speed";
            }
            case "minecraft:spawn_reinforcements": {
                return "minecraft:zombie.spawn_reinforcements";
            }
            case "minecraft:block_break_speed": {
                return "minecraft:player.block_break_speed";
            }
            case "minecraft:block_interaction_range": {
                return "minecraft:player.block_interaction_range";
            }
            case "minecraft:burning_time": {
                return "minecraft:generic.burning_time";
            }
            case "minecraft:explosion_knockback_resistance": {
                return "minecraft:generic.explosion_knockback_resistance";
            }
            case "minecraft:entity_interaction_range": {
                return "minecraft:player.entity_interaction_range";
            }
            case "minecraft:fall_damage_multiplier": {
                return "minecraft:generic.fall_damage_multiplier";
            }
            case "minecraft:gravity": {
                return "minecraft:generic.gravity";
            }
            case "minecraft:mining_efficiency": {
                return "minecraft:player.mining_efficiency";
            }
            case "minecraft:movement_efficiency": {
                return "minecraft:generic.movement_efficiency";
            }
            case "minecraft:oxygen_bonus": {
                return "minecraft:generic.oxygen_bonus";
            }
            case "minecraft:safe_fall_distance": {
                return "minecraft:generic.safe_fall_distance";
            }
            case "minecraft:scale": {
                return "minecraft:generic.scale";
            }
            case "minecraft:sneaking_speed": {
                return "minecraft:player.sneaking_speed";
            }
            case "minecraft:step_height": {
                return "minecraft:generic.step_height";
            }
            case "minecraft:submerged_mining_speed": {
                return "minecraft:player.submerged_mining_speed";
            }
            case "minecraft:sweeping_damage_ratio": {
                return "minecraft:player.sweeping_damage_ratio";
            }
            case "minecraft:water_movement_efficiency": {
                return "minecraft:generic.water_movement_efficiency";
            }
        }
        return idIn;
    }

    private static String modifierIdToName(String idIn) {
        if (idIn.equals("minecraft:random_spawn_bonus")) {
            return "Random spawn bonus";
        }
        return "";
    }

    private static int modifierOperationToInt(String op) {
        switch (op) {
            case "add_value": {
                return 0;
            }
            case "add_multiplied_base": {
                return 1;
            }
            case "add_multiplied_total": {
                return 2;
            }
        }
        return 0;
    }

    public static class_2487 downgradeBlockEntity_to_1_20_4(class_2487 oldTE, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 newTE = new class_2487();
        if (!oldTE.method_10545("id")) {
            oldTE.method_10543(SchematicConversionMaps.checkForIdTag(oldTE));
        }
        Iterator iterator = oldTE.method_10541().iterator();
        block36: while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "x": {
                    newTE.method_10569("x", oldTE.method_10550("x"));
                    continue block36;
                }
                case "y": {
                    newTE.method_10569("y", oldTE.method_10550("y"));
                    continue block36;
                }
                case "z": {
                    newTE.method_10569("z", oldTE.method_10550("z"));
                    continue block36;
                }
                case "id": {
                    newTE.method_10582("id", oldTE.method_10558("id"));
                    continue block36;
                }
                case "Items": {
                    newTE.method_10566("Items", (class_2520)SchematicDowngradeConverter.processItemsTag(oldTE.method_10554("Items", 10), minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "patterns": {
                    newTE.method_10566("Patterns", SchematicDowngradeConverter.processBannerPatterns(oldTE.method_10580(key)));
                    continue block36;
                }
                case "profile": {
                    newTE.method_10566("SkullOwner", SchematicDowngradeConverter.processSkullProfile(oldTE.method_10580(key), newTE, minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "flower_pos": {
                    newTE.method_10566("FlowerPos", SchematicDowngradeConverter.processFlowerPos(oldTE, key));
                    continue block36;
                }
                case "bees": {
                    newTE.method_10566("Bees", SchematicDowngradeConverter.processBeesTag(oldTE.method_10580(key), minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "item": {
                    newTE.method_10566("item", SchematicDowngradeConverter.processDecoratedPot(oldTE.method_10580(key), minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "last_interacted_slot": {
                    newTE.method_10566("last_interacted_slot", oldTE.method_10580(key));
                    continue block36;
                }
                case "ticks_since_song_started": {
                    newTE.method_10544("RecordStartTick", 0L);
                    newTE.method_10544("TickCount", oldTE.method_10537(key));
                    newTE.method_10567("IsPlaying", (byte)0);
                    continue block36;
                }
                case "RecordItem": {
                    newTE.method_10566("RecordItem", SchematicDowngradeConverter.processRecordItem(oldTE.method_10580(key), minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "Book": {
                    newTE.method_10566("Book", SchematicDowngradeConverter.processBookTag(oldTE.method_10580(key), minecraftDataVersion, registryManager));
                    continue block36;
                }
                case "CustomName": {
                    newTE.method_10582("CustomName", SchematicDowngradeConverter.processCustomNameTag(oldTE, key, registryManager));
                    continue block36;
                }
                case "custom_name": {
                    newTE.method_10582("CustomName", SchematicDowngradeConverter.processCustomNameTag(oldTE, key, registryManager));
                    continue block36;
                }
            }
            newTE.method_10566(key, oldTE.method_10580(key));
        }
        return newTE;
    }

    private static class_2487 processRecipesUsedTag(class_2520 nbtIn) {
        class_2487 oldNbt = SchematicDowngradeConverter.asCompound(nbtIn).orElse(new class_2487());
        class_2487 newNbt = new class_2487();
        return oldNbt;
    }

    private static class_2499 processItemsTag(class_2499 oldItems, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2499 newItems = new class_2499();
        for (int i = 0; i < oldItems.size(); ++i) {
            class_2487 itemEntry = oldItems.method_10602(i);
            class_2487 newEntry = new class_2487();
            if (!itemEntry.method_10545("id")) continue;
            String idName = itemEntry.method_10558("id");
            newEntry.method_10582("id", idName);
            if (itemEntry.method_10545("count")) {
                newEntry.method_10567("Count", (byte)itemEntry.method_10550("count"));
            }
            if (itemEntry.method_10545("Slot")) {
                newEntry.method_10567("Slot", itemEntry.method_10571("Slot"));
            }
            if (itemEntry.method_10545("components")) {
                newEntry.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(itemEntry.method_10562("components"), idName, minecraftDataVersion, registryManager));
            } else if (SchematicDowngradeConverter.needsDamageTag(idName)) {
                class_2487 newTag = new class_2487();
                newTag.method_10569("Damage", 0);
                newEntry.method_10566("tag", (class_2520)newTag);
            }
            newItems.add((Object)newEntry);
        }
        return newItems;
    }

    private static class_2499 processItemsTag_Nested(class_2499 oldItems, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2499 newItems = new class_2499();
        for (int i = 0; i < oldItems.size(); ++i) {
            class_2487 itemEntry = oldItems.method_10602(i);
            class_2487 newEntry = new class_2487();
            int slotNum = itemEntry.method_10550("slot");
            class_2487 itemSlot = itemEntry.method_10562("item");
            if (!itemSlot.method_10545("id")) continue;
            String idName = itemSlot.method_10558("id");
            newEntry.method_10582("id", idName);
            if (itemSlot.method_10545("count")) {
                newEntry.method_10567("Count", (byte)itemSlot.method_10550("count"));
            }
            newEntry.method_10567("Slot", (byte)slotNum);
            if (itemSlot.method_10545("components")) {
                newEntry.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(itemSlot.method_10562("components"), idName, minecraftDataVersion, registryManager));
            } else if (SchematicDowngradeConverter.needsDamageTag(idName)) {
                class_2487 newTag = new class_2487();
                newTag.method_10569("Damage", 0);
                newEntry.method_10566("tag", (class_2520)newTag);
            }
            newItems.add((Object)newEntry);
        }
        return newItems;
    }

    private static class_2487 processDecoratedPot_Nested(class_2499 oldItems, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 itemEntry = oldItems.method_10602(0);
        class_2487 newEntry = new class_2487();
        int slotNum = itemEntry.method_10550("slot");
        class_2487 itemSlot = itemEntry.method_10562("item");
        if (!itemSlot.method_10545("id")) {
            return itemEntry;
        }
        String idName = itemSlot.method_10558("id");
        newEntry.method_10582("id", idName);
        newEntry.method_10567("Count", (byte)(itemSlot.method_10545("count") ? itemSlot.method_10550("count") : 1));
        if (itemSlot.method_10545("components")) {
            newEntry.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(itemSlot.method_10562("components"), idName, minecraftDataVersion, registryManager));
        } else if (SchematicDowngradeConverter.needsDamageTag(idName)) {
            class_2487 newTag = new class_2487();
            newTag.method_10569("Damage", 0);
            newEntry.method_10566("tag", (class_2520)newTag);
        }
        return newEntry;
    }

    private static boolean needsDamageTag(String id) {
        class_1799 stack = InventoryUtils.getItemStackFromString((String)id);
        return stack != null && !stack.method_7960() && stack.method_7963();
    }

    private static class_2487 processComponentsTag(class_2487 nbt, String itemId, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 outNbt = new class_2487();
        class_2487 beNbt = new class_2487();
        class_2487 dispNbt = new class_2487();
        boolean needsDamage = SchematicDowngradeConverter.needsDamageTag(itemId);
        Iterator iterator = nbt.method_10541().iterator();
        block88: while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "minecraft:attribute_modifiers": {
                    outNbt.method_10566("AttributeModifiers", SchematicDowngradeConverter.processAttributes(nbt.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "minecraft:banner_patterns": {
                    beNbt.method_10566("Patterns", SchematicDowngradeConverter.processBannerPatterns(nbt.method_10580(key)));
                    beNbt.method_10582("id", "minecraft:banner");
                    break;
                }
                case "minecraft:bees": {
                    beNbt.method_10566("Bees", SchematicDowngradeConverter.processBeesTag(nbt.method_10580(key), minecraftDataVersion, registryManager));
                    beNbt.method_10582("id", itemId);
                    break;
                }
                case "minecraft:block_state": {
                    outNbt.method_10566("BlockStateTag", SchematicDowngradeConverter.processBlockState(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:block_entity_data": {
                    SchematicDowngradeConverter.processBlockEntityData(nbt.method_10580(key), beNbt, minecraftDataVersion, registryManager);
                    break;
                }
                case "minecraft:bucket_entity_data": {
                    SchematicDowngradeConverter.processBucketEntityData(nbt.method_10580(key), beNbt, minecraftDataVersion, registryManager);
                    break;
                }
                case "minecraft:bundle_contents": {
                    outNbt.method_10566("Items", (class_2520)SchematicDowngradeConverter.processItemsTag(nbt.method_10554(key, 10), minecraftDataVersion, registryManager));
                    break;
                }
                case "minecraft:can_break": {
                    outNbt.method_10566("CanDestroy", nbt.method_10580(key));
                    break;
                }
                case "minecraft:can_place_on": {
                    outNbt.method_10566("CanPlaceOn", nbt.method_10580(key));
                    break;
                }
                case "minecraft:container": {
                    if (itemId.contains("decorated_pot")) {
                        beNbt.method_10566("item", (class_2520)SchematicDowngradeConverter.processDecoratedPot_Nested(nbt.method_10554(key, 10), minecraftDataVersion, registryManager));
                    } else {
                        beNbt.method_10566("Items", (class_2520)SchematicDowngradeConverter.processItemsTag_Nested(nbt.method_10554(key, 10), minecraftDataVersion, registryManager));
                    }
                    if (itemId.contains("shulker")) {
                        beNbt.method_10582("id", "minecraft:shulker_box");
                        break;
                    }
                    beNbt.method_10582("id", itemId);
                    break;
                }
                case "minecraft:charged_projectiles": {
                    outNbt.method_10566("ChargedProjectiles", (class_2520)SchematicDowngradeConverter.processChargedProjectile(nbt.method_10580(key), minecraftDataVersion, registryManager));
                    outNbt.method_10556("Charged", true);
                    break;
                }
                case "minecraft:container_loot": {
                    beNbt.method_10566("LootTable", SchematicDowngradeConverter.processLootTable(nbt.method_10580(key)));
                    beNbt.method_10582("id", itemId);
                    break;
                }
                case "minecraft:custom_data": {
                    SchematicDowngradeConverter.processCustomData(nbt.method_10580(key), outNbt);
                    break;
                }
                case "minecraft:custom_model_data": {
                    outNbt.method_10569("CustomModelData", nbt.method_10550(key));
                    break;
                }
                case "minecraft:custom_name": {
                    dispNbt.method_10582("Name", SchematicDowngradeConverter.processCustomNameTag(nbt, key, registryManager));
                    break;
                }
                case "minecraft:damage": {
                    outNbt.method_10569("Damage", nbt.method_10550(key));
                    break;
                }
                case "minecraft:debug_stick_state": {
                    outNbt.method_10566("DebugProperty", nbt.method_10580(key));
                    break;
                }
                case "minecraft:dyed_color": {
                    dispNbt.method_10569("color", SchematicDowngradeConverter.processDyedColor(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:enchantments": {
                    outNbt.method_10566("Enchantments", SchematicDowngradeConverter.processEnchantments(nbt.method_10580(key), true, true));
                    break;
                }
                case "minecraft:entity_data": {
                    outNbt.method_10566("EntityTag", (class_2520)SchematicDowngradeConverter.downgradeEntity_to_1_20_4((class_2487)nbt.method_10580(key), minecraftDataVersion, registryManager));
                    break;
                }
                case "minecraft:stored_enchantments": {
                    outNbt.method_10566("StoredEnchantments", SchematicDowngradeConverter.processEnchantments(nbt.method_10580(key), true, true));
                    break;
                }
                case "minecraft:fireworks": {
                    outNbt.method_10566("Fireworks", SchematicDowngradeConverter.processFireworks(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:firework_explosion": {
                    outNbt.method_10566("Explosion", SchematicDowngradeConverter.processFireworkExplosion(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:instrument": {
                    outNbt.method_10566("instrument", SchematicDowngradeConverter.processInstrument(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:item_name": {
                    dispNbt.method_10582("Name", SchematicDowngradeConverter.processItemName(nbt.method_10580(key), registryManager));
                    break;
                }
                case "minecraft:lock": {
                    beNbt.method_10566("Lock", nbt.method_10580(key));
                    beNbt.method_10582("id", itemId);
                    break;
                }
                case "minecraft:lodestone_tracker": {
                    SchematicDowngradeConverter.processLodestoneTracker(nbt.method_10580(key), outNbt);
                    break;
                }
                case "minecraft:lore": {
                    dispNbt.method_10566("Lore", nbt.method_10580(key));
                    break;
                }
                case "minecraft:map_id": {
                    outNbt.method_10566("map", SchematicDowngradeConverter.processMapId(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:map_color": {
                    dispNbt.method_10566("MapColor", nbt.method_10580(key));
                    break;
                }
                case "minecraft:map_decorations": {
                    outNbt.method_10566("Decorations", SchematicDowngradeConverter.processMapDecorations(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:note_block_sound": {
                    beNbt.method_10566("note_block_sound", nbt.method_10580(key));
                    break;
                }
                case "minecraft:pot_decorations": {
                    beNbt.method_10566("sherds", SchematicDowngradeConverter.processSherds(nbt.method_10580(key)));
                    beNbt.method_10582("id", itemId);
                    break;
                }
                case "minecraft:potion_contents": {
                    SchematicDowngradeConverter.processPotions(nbt.method_10580(key), outNbt);
                    break;
                }
                case "minecraft:profile": {
                    outNbt.method_10566("SkullOwner", SchematicDowngradeConverter.processSkullProfile(nbt.method_10580(key), dispNbt, minecraftDataVersion, registryManager));
                    break;
                }
                case "minecraft:repair_cost": {
                    outNbt.method_10569("RepairCost", nbt.method_10550(key));
                    break;
                }
                case "minecraft:recipes": {
                    outNbt.method_10566("Recipes", SchematicDowngradeConverter.processRecipes(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:suspicious_stew_effects": {
                    outNbt.method_10566("effects", SchematicDowngradeConverter.processSuspiciousStewEffects(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:trim": {
                    outNbt.method_10566("Trim", SchematicDowngradeConverter.processTrim(nbt.method_10580(key)));
                    break;
                }
                case "minecraft:writable_book_content": {
                    class_2487 bookNbt = nbt.method_10562(key);
                    bookNbt = SchematicDowngradeConverter.processWritableBookContent(bookNbt, minecraftDataVersion, registryManager);
                    for (String bookKey : bookNbt.method_10541()) {
                        outNbt.method_10566(bookKey, bookNbt.method_10580(bookKey));
                    }
                    continue block88;
                }
                case "minecraft:written_book_content": {
                    class_2487 bookNbt = nbt.method_10562(key);
                    bookNbt = SchematicDowngradeConverter.processWrittenBookContent(bookNbt, minecraftDataVersion, registryManager);
                    for (String bookKey : bookNbt.method_10541()) {
                        outNbt.method_10566(bookKey, bookNbt.method_10580(bookKey));
                    }
                    continue block88;
                }
                case "minecraft:unbreakable": {
                    outNbt.method_10556("Unbreakable", SchematicDowngradeConverter.processUnbreakable(nbt.method_10580(key)));
                }
            }
        }
        if (!beNbt.method_33133()) {
            outNbt.method_10566("BlockEntityTag", (class_2520)beNbt);
        }
        if (!dispNbt.method_33133()) {
            outNbt.method_10566("display", (class_2520)dispNbt);
        }
        if (!outNbt.method_10545("RepairCost") && (itemId.equals("minecraft:dragon_head") || needsDamage)) {
            outNbt.method_10569("RepairCost", 0);
        }
        if (!outNbt.method_10545("Damage") && needsDamage) {
            outNbt.method_10569("Damage", 0);
        }
        return outNbt;
    }

    private static void processCustomData(class_2520 oldNbt, class_2487 outNbt) {
        class_2487 origData = SchematicDowngradeConverter.asCompound(oldNbt).orElse(new class_2487());
        for (String keyData : origData.method_10541()) {
            outNbt.method_10566(keyData, origData.method_10580(keyData));
        }
    }

    private static void processLodestoneTracker(class_2520 oldEle, class_2487 outNbt) {
        class_2487 oldNbt = SchematicDowngradeConverter.asCompound(oldEle).orElse(new class_2487());
        if (oldNbt.method_10545("tracked")) {
            outNbt.method_10556("LodestoneTracked", oldNbt.method_10577("tracked"));
        }
        if (oldNbt.method_10545("target")) {
            class_2487 target = oldNbt.method_10562("target");
            outNbt.method_10566("LodestoneDimension", target.method_10580("dimension"));
            outNbt.method_10566("LodestonePos", target.method_10580("pos"));
        }
    }

    private static void processBucketEntityData(class_2520 oldTags, class_2487 beNbt, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 oldNbt = SchematicDowngradeConverter.asCompound(oldTags).orElse(new class_2487());
        for (String key : oldNbt.method_10541()) {
            beNbt.method_10566(key, oldNbt.method_10580(key));
        }
    }

    private static void processPotions(class_2520 oldPots, class_2487 outNbt) {
        class_2487 oldNbt = SchematicDowngradeConverter.asCompound(oldPots).orElse(new class_2487());
        if (oldNbt.method_10545("potion")) {
            outNbt.method_10582("Potion", oldNbt.method_10558("potion"));
        }
        if (oldNbt.method_10545("custom_color")) {
            outNbt.method_10566("CustomPotionColor", oldNbt.method_10580("custom_color"));
        }
        if (oldNbt.method_10545("custom_effects")) {
            outNbt.method_10566("custom_potion_effects", oldNbt.method_10580("custom_effects"));
        }
    }

    private static class_2520 processMapDecorations(class_2520 oldDeco) {
        class_2487 oldTag = SchematicDowngradeConverter.asCompound(oldDeco).orElse(new class_2487());
        class_2499 newTags = new class_2499();
        for (String key : oldTag.method_10541()) {
            class_2487 entryOld = oldTag.method_10562(key);
            class_2487 entryNew = new class_2487();
            entryNew.method_10582("id", key);
            entryNew.method_10549("x", entryOld.method_10545("x") ? entryOld.method_10574("x") : 0.0);
            entryNew.method_10549("z", entryOld.method_10545("z") ? entryOld.method_10574("z") : 0.0);
            entryNew.method_10549("rot", entryOld.method_10545("rotation") ? (double)entryOld.method_10583("rotation") : 0.0);
            entryNew.method_10567("type", (byte)(entryOld.method_10545("type") ? SchematicDowngradeConverter.convertMapDecoration(entryOld.method_10558("type")) : 0));
            newTags.add((Object)entryNew);
        }
        return newTags;
    }

    private static int convertMapDecoration(String type) {
        return switch (type) {
            case "minecraft:player" -> 0;
            case "minecraft:frame" -> 1;
            case "minecraft:red_marker" -> 2;
            case "minecraft:blue_marker" -> 3;
            case "minecraft:target_x" -> 4;
            case "minecraft:target_point" -> 5;
            case "minecraft:player_off_map" -> 6;
            case "minecraft:player_off_limits" -> 7;
            case "minecraft:mansion" -> 8;
            case "minecraft:monument" -> 9;
            case "minecraft:banner_white" -> 10;
            case "minecraft:banner_orange" -> 11;
            case "minecraft:banner_magenta" -> 12;
            case "minecraft:banner_light_blue" -> 13;
            case "minecraft:banner_yellow" -> 14;
            case "minecraft:banner_lime" -> 15;
            case "minecraft:banner_pink" -> 16;
            case "minecraft:banner_gray" -> 17;
            case "minecraft:banner_light_gray" -> 18;
            case "minecraft:banner_cyan" -> 19;
            case "minecraft:banner_purple" -> 20;
            case "minecraft:banner_blue" -> 21;
            case "minecraft:banner_brown" -> 22;
            case "minecraft:banner_green" -> 23;
            case "minecraft:banner_red" -> 24;
            case "minecraft:banner_black" -> 25;
            case "minecraft:red_x" -> 26;
            case "minecraft:village_desert" -> 27;
            case "minecraft:village_plains" -> 28;
            case "minecraft:village_savanna" -> 29;
            case "minecraft:village_snowy" -> 30;
            case "minecraft:village_taiga" -> 31;
            case "minecraft:jungle_temple" -> 32;
            case "minecraft:swamp_hut" -> 33;
            default -> 0;
        };
    }

    private static class_2520 processSherds(class_2520 oldSherds) {
        return oldSherds;
    }

    private static class_2520 processLootTable(class_2520 oldLoot) {
        class_2487 oldTable = SchematicDowngradeConverter.asCompound(oldLoot).orElse(new class_2487());
        class_2487 newTable = new class_2487();
        if (oldTable.method_10545("loot_table")) {
            class_2487 loot = oldTable.method_10562("loot_table");
            newTable.method_10543(loot);
        }
        if (oldTable.method_10545("seed")) {
            newTable.method_10544("LootTableSeed", oldTable.method_10537("seed"));
        }
        return newTable;
    }

    private static String processItemName(class_2520 oldName, class_5455 registryManager) {
        if (oldName != null) {
            return oldName.toString();
        }
        return "minecraft:air";
    }

    private static int processDyedColor(class_2520 oldDye) {
        class_2487 oldColor = SchematicDowngradeConverter.asCompound(oldDye).orElse(new class_2487());
        if (oldColor.method_10545("rgb")) {
            return oldColor.method_10550("rgb");
        }
        return 10511680;
    }

    private static class_2520 processRecipes(class_2520 oldRecipes) {
        return oldRecipes;
    }

    private static class_2520 processInstrument(class_2520 oldGoat) {
        return oldGoat;
    }

    private static class_2520 processSuspiciousStewEffects(class_2520 oldEffects) {
        return oldEffects;
    }

    private static class_2520 processMapId(class_2520 oldMapId) {
        return oldMapId;
    }

    private static class_2520 processTrim(class_2520 oldTrim) {
        return oldTrim;
    }

    private static class_2499 processChargedProjectile(class_2520 oldProjectiles, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2499 oldNbt = SchematicDowngradeConverter.asNbtList(oldProjectiles).orElse(new class_2499());
        class_2499 newNbt = new class_2499();
        for (int i = 0; i < oldNbt.size(); ++i) {
            class_2487 itemEntry = oldNbt.method_10602(i);
            class_2487 newEntry = new class_2487();
            if (!itemEntry.method_10545("id")) continue;
            String idName = itemEntry.method_10558("id");
            newEntry.method_10582("id", idName);
            newEntry.method_10567("Count", (byte)(itemEntry.method_10545("count") ? itemEntry.method_10550("count") : 1));
            if (itemEntry.method_10545("components")) {
                newEntry.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(itemEntry.method_10562("components"), idName, minecraftDataVersion, registryManager));
            }
            newNbt.add((Object)newEntry);
        }
        return newNbt;
    }

    private static boolean processUnbreakable(class_2520 oldNbt) {
        class_2487 oldUnbr = SchematicDowngradeConverter.asCompound(oldNbt).orElse(new class_2487());
        if (oldUnbr.method_10545("show_in_tooltip")) {
            return oldUnbr.method_10577("show_in_tooltip");
        }
        return false;
    }

    private static void processBlockEntityData(class_2520 oldBeData, class_2487 beNbt, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 newData = SchematicDowngradeConverter.downgradeBlockEntity_to_1_20_4((class_2487)oldBeData, minecraftDataVersion, registryManager);
        for (String key : newData.method_10541()) {
            beNbt.method_10566(key, newData.method_10580(key));
        }
    }

    private static class_2520 processDecoratedPot(class_2520 oldPot, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 oldNbt = SchematicDowngradeConverter.asCompound(oldPot).orElse(new class_2487());
        class_2487 newNbt = new class_2487();
        Iterator iterator = oldNbt.method_10541().iterator();
        while (iterator.hasNext()) {
            String key;
            switch (key = (String)iterator.next()) {
                case "id": {
                    newNbt.method_10582("id", oldNbt.method_10558("id"));
                    break;
                }
                case "count": {
                    newNbt.method_10567("Count", (byte)oldNbt.method_10550("count"));
                    break;
                }
                case "components": {
                    newNbt.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(oldNbt.method_10562("components"), oldNbt.method_10558("id"), minecraftDataVersion, registryManager));
                }
            }
        }
        if (!newNbt.method_10545("tag") && oldNbt.method_10545("id") && SchematicDowngradeConverter.needsDamageTag(oldNbt.method_10558("id"))) {
            class_2487 newTag = new class_2487();
            newTag.method_10569("Damage", 0);
            newNbt.method_10566("tag", (class_2520)newTag);
        }
        return newNbt;
    }

    private static class_2520 processEnchantments(class_2520 oldNbt, boolean fullId, boolean shortInt) {
        class_2487 oldEnchants = SchematicDowngradeConverter.asCompound(oldNbt).orElse(new class_2487());
        class_2487 oldLevels = oldEnchants.method_10562("levels");
        class_2499 newEnchants = new class_2499();
        boolean showTooltip = false;
        if (oldEnchants.method_10545("show_in_tooltip")) {
            showTooltip = oldEnchants.method_10577("show_in_tooltip");
        }
        for (String key : oldLevels.method_10541()) {
            class_2487 newEntry = new class_2487();
            class_2960 id = class_2960.method_60654((String)key);
            if (shortInt) {
                newEntry.method_10575("lvl", (short)oldLevels.method_10550(key));
            } else {
                newEntry.method_10569("lvl", oldLevels.method_10550(key));
            }
            newEntry.method_10582("id", fullId ? id.toString() : id.method_12832());
            newEnchants.add((Object)newEntry);
        }
        return newEnchants;
    }

    private static String processCustomNameTag(class_2487 nameTag, String key, @Nonnull class_5455 registry) {
        class_5250 oldName = class_2586.method_59894((String)nameTag.method_10558(key), (class_7225.class_7874)registry).method_27661();
        return SchematicDowngradeConverter.legacyTextDeserializer(oldName, registry);
    }

    private static String legacyTextDeserializer(class_5250 oldText, @Nonnull class_5455 registry) {
        try {
            JsonElement element = (JsonElement)class_8824.field_46597.encodeStart((DynamicOps)registry.method_57093((DynamicOps)JsonOps.INSTANCE), (Object)oldText).getOrThrow();
            return new GsonBuilder().disableHtmlEscaping().create().toJson(element);
        }
        catch (Exception err) {
            Litematica.LOGGER.error("legacyTextDeserializer: Failed to convert MutableText to JSON; (falling back to just 'getString'); {}", (Object)err.getLocalizedMessage());
            return oldText.getString();
        }
    }

    @Nullable
    private static class_5250 legacyTextSerializer(String json, @Nonnull class_5455 registry) {
        try {
            return ((class_2561)class_8824.field_46597.parse((DynamicOps)registry.method_57093((DynamicOps)JsonOps.INSTANCE), (Object)JsonParser.parseString((String)json)).getOrThrow()).method_27661();
        }
        catch (Exception err) {
            Litematica.LOGGER.error("legacyTextSerializer: Failed to convert JSON to MutableText; {}", (Object)err.getLocalizedMessage());
            return null;
        }
    }

    private static class_2520 processBlockState(class_2520 bsTag) {
        class_2487 oldBS = SchematicDowngradeConverter.asCompound(bsTag).orElse(new class_2487());
        class_2487 newBS = new class_2487();
        for (String key : oldBS.method_10541()) {
            newBS.method_10566(key, oldBS.method_10580(key));
        }
        return bsTag;
    }

    private static class_2520 processFireworks(class_2520 rocket) {
        class_2487 oldRocket = SchematicDowngradeConverter.asCompound(rocket).orElse(new class_2487());
        class_2487 newRocket = new class_2487();
        if (oldRocket.method_10545("flight_duration")) {
            newRocket.method_10567("Flight", oldRocket.method_10571("flight_duration"));
        }
        if (oldRocket.method_10545("explosions")) {
            class_2499 oldExplosions = oldRocket.method_10554("explosions", 10);
            class_2499 newExplosions = new class_2499();
            for (int i = 0; i < oldExplosions.size(); ++i) {
                newExplosions.add((Object)SchematicDowngradeConverter.processFireworkExplosion((class_2520)oldExplosions.method_10602(i)));
            }
            newRocket.method_10566("Explosions", (class_2520)newExplosions);
        }
        return newRocket;
    }

    private static class_2520 processFireworkExplosion(class_2520 explosion) {
        class_2487 oldExplosion = SchematicDowngradeConverter.asCompound(explosion).orElse(new class_2487());
        class_2487 newExplosion = new class_2487();
        if (oldExplosion.method_10545("shape")) {
            newExplosion.method_10567("Type", (byte)SchematicDowngradeConverter.convertFireworkShape(oldExplosion.method_10558("shape")));
        }
        if (oldExplosion.method_10545("colors")) {
            newExplosion.method_10539("Colors", oldExplosion.method_10561("colors"));
        }
        if (oldExplosion.method_10545("fade_colors")) {
            newExplosion.method_10539("FadeColors", oldExplosion.method_10561("fade_colors"));
        }
        if (oldExplosion.method_10545("has_trail")) {
            newExplosion.method_10556("Trail", oldExplosion.method_10577("has_trail"));
        }
        if (oldExplosion.method_10545("has_twinkle")) {
            newExplosion.method_10556("Flicker", oldExplosion.method_10577("has_twinkle"));
        }
        return newExplosion;
    }

    private static int convertFireworkShape(String shape) {
        return switch (shape) {
            case "small_ball" -> 0;
            case "large_ball" -> 1;
            case "star" -> 2;
            case "creeper" -> 3;
            case "burst" -> 4;
            default -> 0;
        };
    }

    private static class_2520 processRecordItem(class_2520 itemIn, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2487 oldRecord = SchematicDowngradeConverter.asCompound(itemIn).orElse(new class_2487());
        class_2487 recordOut = new class_2487();
        recordOut.method_10582("id", oldRecord.method_10558("id"));
        recordOut.method_10567("Count", (byte)oldRecord.method_10550("count"));
        if (oldRecord.method_10545("components")) {
            recordOut.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(oldRecord.method_10562("components"), oldRecord.method_10558("id"), minecraftDataVersion, registryManager));
        }
        return recordOut;
    }

    private static class_2520 processBookTag(class_2520 bookNbt, int minecraftDataVersion, class_5455 registryManager) {
        class_2487 oldBook = SchematicDowngradeConverter.asCompound(bookNbt).orElse(new class_2487());
        class_2487 newBook = new class_2487();
        newBook.method_10582("id", oldBook.method_10558("id"));
        newBook.method_10567("Count", (byte)oldBook.method_10550("count"));
        if (oldBook.method_10545("Page")) {
            newBook.method_10569("Page", oldBook.method_10550("Page"));
        }
        if (oldBook.method_10545("components")) {
            newBook.method_10566("tag", (class_2520)SchematicDowngradeConverter.processComponentsTag(oldBook.method_10562("components"), oldBook.method_10558("id"), minecraftDataVersion, registryManager));
        }
        return newBook;
    }

    private static class_2487 processWritableBookContent(class_2487 bookNbt, int minecraftDataVersion, class_5455 registry) {
        class_2487 newBook = new class_2487();
        class_2499 newPages = new class_2499();
        if (bookNbt.method_10545("pages")) {
            class_2499 pages = bookNbt.method_10554("pages", 10);
            for (int i = 0; i < pages.size(); ++i) {
                class_2487 page = pages.method_10602(i);
                String oldPage = page.method_10558("raw");
                try {
                    class_5250 oldText = SchematicDowngradeConverter.legacyTextSerializer(oldPage, registry);
                    String newPage = SchematicDowngradeConverter.legacyTextDeserializer(oldText, registry);
                    newPages.method_10531(i, (class_2520)class_2519.method_23256((String)newPage));
                    continue;
                }
                catch (Exception e) {
                    newPages.method_10531(i, (class_2520)class_2519.method_23256((String)oldPage));
                }
            }
        }
        if (!newPages.isEmpty()) {
            newBook.method_10566("pages", (class_2520)newPages);
        }
        return newBook;
    }

    private static class_2487 processWrittenBookContent(class_2487 bookNbt, int minecraftDataVersion, class_5455 registry) {
        class_2487 newBook = new class_2487();
        class_2487 filtered = new class_2487();
        class_2499 newPages = new class_2499();
        if (bookNbt.method_10545("author")) {
            newBook.method_10582("author", bookNbt.method_10558("author"));
        }
        if (bookNbt.method_10545("title")) {
            class_2487 title = bookNbt.method_10562("title");
            newBook.method_10582("title", title.method_10558("raw"));
        }
        if (bookNbt.method_10545("resolved")) {
            newBook.method_10556("resolved", bookNbt.method_10577("resolved"));
        }
        if (bookNbt.method_10545("generation")) {
            newBook.method_10569("generation", bookNbt.method_10550("generation"));
        }
        if (bookNbt.method_10545("pages")) {
            class_2499 pages = bookNbt.method_10554("pages", 10);
            for (int i = 0; i < pages.size(); ++i) {
                class_2487 page = pages.method_10602(i);
                String oldPage = page.method_10558("raw");
                if (page.method_10545("filtered")) {
                    String filterPage = page.method_10558("filtered");
                    try {
                        class_5250 filteredText = SchematicDowngradeConverter.legacyTextSerializer(filterPage, registry);
                        String newFilterPage = SchematicDowngradeConverter.legacyTextDeserializer(filteredText, registry);
                        filtered.method_10582(filterPage, newFilterPage);
                    }
                    catch (Exception e) {
                        filtered.method_10582(filterPage, filterPage);
                    }
                }
                try {
                    class_5250 oldText = SchematicDowngradeConverter.legacyTextSerializer(oldPage, registry);
                    String newPage = SchematicDowngradeConverter.legacyTextDeserializer(oldText, registry);
                    newPages.method_10531(i, (class_2520)class_2519.method_23256((String)newPage));
                    continue;
                }
                catch (Exception e) {
                    newPages.method_10531(i, (class_2520)class_2519.method_23256((String)oldPage));
                }
            }
        }
        if (!newPages.isEmpty()) {
            newBook.method_10566("pages", (class_2520)newPages);
        }
        if (!filtered.method_33133()) {
            newBook.method_10566("filtered_pages", (class_2520)filtered);
        }
        return newBook;
    }

    private static class_2520 processBannerPatterns(class_2520 oldPatterns) {
        class_2499 oldList = SchematicDowngradeConverter.asNbtList(oldPatterns).orElse(new class_2499());
        class_2499 newList = new class_2499();
        for (int i = 0; i < oldList.size(); ++i) {
            class_2487 oldEntry = oldList.method_10602(i);
            class_2487 newEntry = new class_2487();
            String color = oldEntry.method_10558("color");
            String pattern = oldEntry.method_10558("pattern");
            class_1767 dye = class_1767.method_7793((String)color, (class_1767)class_1767.field_7952);
            newEntry.method_10582("Pattern", SchematicDowngradeConverter.convertBannerPattern(pattern));
            newEntry.method_10569("Color", dye.method_7789());
            newList.add((Object)newEntry);
        }
        return newList;
    }

    private static String convertBannerPattern(String patternId) {
        return switch (patternId) {
            case "minecraft:base" -> "b";
            case "minecraft:square_bottom_left" -> "bl";
            case "minecraft:square_bottom_right" -> "br";
            case "minecraft:square_top_left" -> "tl";
            case "minecraft:square_top_right" -> "tr";
            case "minecraft:stripe_bottom" -> "bs";
            case "minecraft:stripe_top" -> "ts";
            case "minecraft:stripe_left" -> "ls";
            case "minecraft:stripe_right" -> "rs";
            case "minecraft:stripe_center" -> "cs";
            case "minecraft:stripe_middle" -> "ms";
            case "minecraft:stripe_downright" -> "drs";
            case "minecraft:stripe_downleft" -> "dls";
            case "minecraft:small_stripes" -> "ss";
            case "minecraft:cross" -> "cr";
            case "minecraft:straight_cross" -> "sc";
            case "minecraft:triangle_bottom" -> "bt";
            case "minecraft:triangle_top" -> "tt";
            case "minecraft:triangles_bottom" -> "bts";
            case "minecraft:triangles_top" -> "tts";
            case "minecraft:diagonal_left" -> "ld";
            case "minecraft:diagonal_up_right" -> "rd";
            case "minecraft:diagonal_up_left" -> "lud";
            case "minecraft:diagonal_right" -> "rud";
            case "minecraft:circle" -> "mc";
            case "minecraft:rhombus" -> "mr";
            case "minecraft:half_vertical" -> "vh";
            case "minecraft:half_horizontal" -> "hh";
            case "minecraft:half_vertical_right" -> "vhr";
            case "minecraft:half_horizontal_bottom" -> "hhb";
            case "minecraft:border" -> "bo";
            case "minecraft:curly_border" -> "cbo";
            case "minecraft:gradient" -> "gra";
            case "minecraft:gradient_up" -> "gru";
            case "minecraft:bricks" -> "bri";
            case "minecraft:globe" -> "glb";
            case "minecraft:creeper" -> "cre";
            case "minecraft:skull" -> "sku";
            case "minecraft:flower" -> "flo";
            case "minecraft:mojang" -> "moj";
            case "minecraft:piglin" -> "pig";
            default -> "b";
        };
    }

    private static class_2520 processSkullProfile(class_2520 oldProfile, class_2487 dispNbt, int minecraftDataVersion, @Nonnull class_5455 registry) {
        UUID uuid;
        class_2487 profile = SchematicDowngradeConverter.asCompound(oldProfile).orElse(new class_2487());
        class_2487 newProfile = new class_2487();
        String customName1 = dispNbt.method_10545("Name") ? dispNbt.method_10558("Name") : "";
        String customName2 = dispNbt.method_10545("CustomName") ? dispNbt.method_10558("CustomName") : "";
        String name = profile.method_10545("name") ? profile.method_10558("name") : "";
        UUID uUID = uuid = profile.method_10545("id") ? profile.method_25926("id") : class_156.field_25140;
        if (name.isEmpty() && !customName1.isEmpty()) {
            try {
                disp = SchematicDowngradeConverter.legacyTextSerializer(customName1, registry);
                if (disp != null) {
                    name = disp.method_54160();
                }
                if (name == null) {
                    name = customName1;
                }
            }
            catch (Exception e) {
                name = customName1;
            }
        } else if (name.isEmpty() && !customName2.isEmpty()) {
            try {
                disp = SchematicDowngradeConverter.legacyTextSerializer(customName2, registry);
                if (disp != null) {
                    name = disp.method_54160();
                }
                if (name == null) {
                    name = customName2;
                }
            }
            catch (Exception e) {
                name = customName2;
            }
        }
        newProfile.method_10582("Name", name);
        newProfile.method_25927("Id", uuid);
        class_2499 properties = profile.method_10554("properties", 10);
        class_2487 newProperties = new class_2487();
        for (int i = 0; i < properties.size(); ++i) {
            class_2487 property = properties.method_10602(i);
            String propName = property.method_10558("name");
            String propValue = property.method_10558("value");
            if (!propName.equals("textures")) continue;
            class_2499 textures = new class_2499();
            class_2487 value = new class_2487();
            value.method_10582("Value", propValue);
            textures.add((Object)value);
            newProperties.method_10566("textures", (class_2520)textures);
        }
        newProfile.method_10566("Properties", (class_2520)newProperties);
        return newProfile;
    }

    private static class_2520 processFlowerPos(class_2487 oldNbt, String key) {
        class_2487 flowerOut = new class_2487();
        class_2338 flowerPos = class_2512.method_10691((class_2487)oldNbt, (String)key).orElse(null);
        if (flowerPos != null) {
            flowerOut.method_10569("X", flowerPos.method_10263());
            flowerOut.method_10569("Y", flowerPos.method_10264());
            flowerOut.method_10569("Z", flowerPos.method_10260());
        }
        return flowerOut;
    }

    private static class_2520 processBeesTag(class_2520 beesTag, int minecraftDataVersion, @Nonnull class_5455 registryManager) {
        class_2499 oldBees = SchematicDowngradeConverter.asNbtList(beesTag).orElse(new class_2499());
        class_2499 newBees = new class_2499();
        for (int i = 0; i < oldBees.size(); ++i) {
            class_2487 oldEntry = oldBees.method_10602(i);
            class_2487 newEntry = new class_2487();
            newEntry.method_10569("TicksInHive", oldEntry.method_10550("ticks_in_hive"));
            newEntry.method_10569("MinOccupationTicks", oldEntry.method_10550("min_ticks_in_hive"));
            newEntry.method_10566("EntityData", (class_2520)SchematicDowngradeConverter.downgradeEntity_to_1_20_4(oldEntry.method_10562("entity_data"), minecraftDataVersion, registryManager));
            newBees.add((Object)newEntry);
        }
        return newBees;
    }
}

