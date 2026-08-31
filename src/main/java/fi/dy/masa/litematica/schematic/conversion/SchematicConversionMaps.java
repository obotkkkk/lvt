/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixUtils
 *  com.mojang.serialization.Dynamic
 *  com.mojang.serialization.DynamicOps
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1208
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2378
 *  net.minecraft.class_2465
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2509
 *  net.minecraft.class_2512
 *  net.minecraft.class_2519
 *  net.minecraft.class_2520
 *  net.minecraft.class_2522
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_2960
 *  net.minecraft.class_3551
 *  net.minecraft.class_7871
 *  net.minecraft.class_7924
 */
package fi.dy.masa.litematica.schematic.conversion;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.class_1208;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2378;
import net.minecraft.class_2465;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2509;
import net.minecraft.class_2512;
import net.minecraft.class_2519;
import net.minecraft.class_2520;
import net.minecraft.class_2522;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_2960;
import net.minecraft.class_3551;
import net.minecraft.class_7871;
import net.minecraft.class_7924;

public class SchematicConversionMaps {
    private static final Object2IntOpenHashMap<String> OLD_BLOCK_NAME_TO_SHIFTED_BLOCK_ID = (Object2IntOpenHashMap)DataFixUtils.make((Object)new Object2IntOpenHashMap(), map -> map.defaultReturnValue(-1));
    private static final Int2ObjectOpenHashMap<String> ID_META_TO_UPDATED_NAME = new Int2ObjectOpenHashMap();
    private static final Object2IntOpenHashMap<class_2680> BLOCKSTATE_TO_ID_META = (Object2IntOpenHashMap)DataFixUtils.make((Object)new Object2IntOpenHashMap(), map -> map.defaultReturnValue(-1));
    private static final Int2ObjectOpenHashMap<class_2680> ID_META_TO_BLOCKSTATE = new Int2ObjectOpenHashMap();
    private static final HashMap<String, String> OLD_NAME_TO_NEW_NAME = new HashMap();
    private static final HashMap<String, String> NEW_NAME_TO_OLD_NAME = new HashMap();
    private static final HashMap<class_2487, class_2487> OLD_STATE_TO_NEW_STATE = new HashMap();
    private static final HashMap<class_2487, class_2487> NEW_STATE_TO_OLD_STATE = new HashMap();
    private static final ArrayList<ConversionData> CACHED_DATA = new ArrayList();
    private static boolean initialized;

    public static void addEntry(int idMeta, String newStateString, String ... oldStateStrings) {
        CACHED_DATA.add(new ConversionData(idMeta, newStateString, oldStateStrings));
    }

    public static void computeMaps() {
        if (initialized) {
            return;
        }
        SchematicConversionMaps.clearMaps();
        SchematicConversionMaps.addOverrides();
        for (ConversionData data : CACHED_DATA) {
            try {
                class_2487 newStateTag;
                class_2487 oldStateTag;
                if (data.oldStateStrings.length > 0 && (oldStateTag = SchematicConversionMaps.getStateTagFromString(data.oldStateStrings[0])) != null) {
                    String name = oldStateTag.method_10558("Name");
                    OLD_BLOCK_NAME_TO_SHIFTED_BLOCK_ID.putIfAbsent((Object)name, data.idMeta & 0xFFF0);
                }
                if ((newStateTag = SchematicConversionMaps.getStateTagFromString(data.newStateString)) == null) continue;
                SchematicConversionMaps.addIdMetaToBlockState(data.idMeta, newStateTag, data.oldStateStrings);
            }
            catch (Exception e) {
                Litematica.LOGGER.warn("addEntry(): Exception while adding blockstate conversion map entry for ID '{}' (fixed state: '{}')", (Object)data.idMeta, (Object)data.newStateString, (Object)e);
            }
        }
        initialized = true;
    }

    @Nullable
    public static class_2680 get_1_13_2_StateForIdMeta(int idMeta) {
        return (class_2680)ID_META_TO_BLOCKSTATE.get(idMeta);
    }

    public static class_2487 get_1_13_2_StateTagFor_1_12_Tag(class_2487 oldStateTag) {
        class_2487 tag = OLD_STATE_TO_NEW_STATE.get(oldStateTag);
        return tag != null ? tag : oldStateTag;
    }

    public static class_2487 get_1_12_StateTagFor_1_13_2_Tag(class_2487 newStateTag) {
        class_2487 tag = NEW_STATE_TO_OLD_STATE.get(newStateTag);
        return tag != null ? tag : newStateTag;
    }

    public static int getOldNameToShiftedBlockId(String oldBlockname) {
        return OLD_BLOCK_NAME_TO_SHIFTED_BLOCK_ID.getInt((Object)oldBlockname);
    }

    private static void addOverrides() {
        class_2680 air = class_2246.field_10124.method_9564();
        BLOCKSTATE_TO_ID_META.put((Object)air, 0);
        ID_META_TO_BLOCKSTATE.put(0, (Object)air);
        int idOldLog = 284;
        int idNewLog = 2604;
        ID_META_TO_BLOCKSTATE.put(idOldLog | 0, (Object)((class_2680)class_2246.field_10126.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_BLOCKSTATE.put(idOldLog | 1, (Object)((class_2680)class_2246.field_10155.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_BLOCKSTATE.put(idOldLog | 2, (Object)((class_2680)class_2246.field_10307.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_BLOCKSTATE.put(idOldLog | 3, (Object)((class_2680)class_2246.field_10303.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_BLOCKSTATE.put(idNewLog | 0, (Object)((class_2680)class_2246.field_9999.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_BLOCKSTATE.put(idNewLog | 1, (Object)((class_2680)class_2246.field_10178.method_9564().method_11657((class_2769)class_2465.field_11459, (Comparable)class_2350.class_2351.field_11052)));
        ID_META_TO_UPDATED_NAME.put(1648, (Object)"minecraft:melon");
        ID_META_TO_UPDATED_NAME.put(2304, (Object)"minecraft:skeleton_skull");
        ID_META_TO_UPDATED_NAME.put(2305, (Object)"minecraft:skeleton_skull");
        ID_META_TO_UPDATED_NAME.put(2306, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2307, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2308, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2309, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2312, (Object)"minecraft:skeleton_skull");
        ID_META_TO_UPDATED_NAME.put(2313, (Object)"minecraft:skeleton_skull");
        ID_META_TO_UPDATED_NAME.put(2314, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2315, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2316, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(2317, (Object)"minecraft:skeleton_wall_skull");
        ID_META_TO_UPDATED_NAME.put(3664, (Object)"minecraft:shulker_box");
        ID_META_TO_UPDATED_NAME.put(3665, (Object)"minecraft:shulker_box");
        ID_META_TO_UPDATED_NAME.put(3666, (Object)"minecraft:shulker_box");
        ID_META_TO_UPDATED_NAME.put(3667, (Object)"minecraft:shulker_box");
        ID_META_TO_UPDATED_NAME.put(3668, (Object)"minecraft:shulker_box");
        ID_META_TO_UPDATED_NAME.put(3669, (Object)"minecraft:shulker_box");
    }

    private static void clearMaps() {
        OLD_BLOCK_NAME_TO_SHIFTED_BLOCK_ID.clear();
        ID_META_TO_UPDATED_NAME.clear();
        BLOCKSTATE_TO_ID_META.clear();
        ID_META_TO_BLOCKSTATE.clear();
        OLD_NAME_TO_NEW_NAME.clear();
        NEW_NAME_TO_OLD_NAME.clear();
        OLD_STATE_TO_NEW_STATE.clear();
        NEW_STATE_TO_OLD_STATE.clear();
    }

    private static void addIdMetaToBlockState(int idMeta, class_2487 newStateTag, String ... oldStateStrings) {
        try {
            String newName = newStateTag.method_10558("Name");
            String overriddenName = (String)ID_META_TO_UPDATED_NAME.get(idMeta);
            if (overriddenName != null) {
                newName = overriddenName;
                newStateTag.method_10582("Name", newName);
            }
            class_2378 lookup = SchematicWorldHandler.INSTANCE.getRegistryManager().method_30530(class_7924.field_41254);
            class_2680 state = class_2512.method_10681((class_7871)lookup, (class_2487)newStateTag);
            ID_META_TO_BLOCKSTATE.putIfAbsent(idMeta, (Object)state);
            BLOCKSTATE_TO_ID_META.putIfAbsent((Object)state, idMeta);
            if (oldStateStrings.length > 0) {
                class_2487 oldStateTag = SchematicConversionMaps.getStateTagFromString(oldStateStrings[0]);
                String oldName = oldStateTag.method_10558("Name");
                if (overriddenName == null) {
                    newName = SchematicConversionMaps.updateBlockName(newName, Configs.Generic.DATAFIXER_DEFAULT_SCHEMA.getIntegerValue());
                    newStateTag.method_10582("Name", newName);
                }
                if (!oldName.equals(newName)) {
                    OLD_NAME_TO_NEW_NAME.putIfAbsent(oldName, newName);
                    NEW_NAME_TO_OLD_NAME.putIfAbsent(newName, oldName);
                }
                SchematicConversionMaps.addOldStateToNewState(newStateTag, oldStateStrings);
            }
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("addIdMetaToBlockState(): Exception while adding blockstate conversion map entry for ID '{}'", (Object)idMeta, (Object)e);
        }
    }

    private static void addOldStateToNewState(class_2487 newStateTagIn, String ... oldStateStrings) {
        try {
            String oldBlockName;
            String newBlockName;
            class_2487 oldStateTag;
            if (oldStateStrings.length == 1) {
                class_2487 oldStateTag2 = SchematicConversionMaps.getStateTagFromString(oldStateStrings[0]);
                if (oldStateTag2 != null) {
                    OLD_STATE_TO_NEW_STATE.putIfAbsent(oldStateTag2, newStateTagIn);
                    NEW_STATE_TO_OLD_STATE.putIfAbsent(newStateTagIn, oldStateTag2);
                }
            } else if (oldStateStrings.length > 1 && (oldStateTag = SchematicConversionMaps.getStateTagFromString(oldStateStrings[0])) != null && newStateTagIn.method_10541().equals(oldStateTag.method_10541()) && (newBlockName = OLD_NAME_TO_NEW_NAME.get(oldBlockName = oldStateTag.method_10558("Name"))) != null && !newBlockName.equals(oldBlockName)) {
                for (String oldStateString : oldStateStrings) {
                    oldStateTag = SchematicConversionMaps.getStateTagFromString(oldStateString);
                    if (oldStateTag == null) continue;
                    class_2487 newTag = oldStateTag.method_10553();
                    newTag.method_10582("Name", newBlockName);
                    OLD_STATE_TO_NEW_STATE.putIfAbsent(oldStateTag, newTag);
                    NEW_STATE_TO_OLD_STATE.putIfAbsent(newTag, oldStateTag);
                }
            }
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("addOldStateToNewState(): Exception while adding new blockstate to old blockstate conversion map entry for '{}'", (Object)newStateTagIn, (Object)e);
        }
    }

    public static class_2487 getStateTagFromString(String str) {
        try {
            return class_2522.method_10718((String)str.replace('\'', '\"'));
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String updateBlockName(String oldName, int oldVersion) {
        class_2519 tagStr = class_2519.method_23256((String)oldName);
        try {
            return ((class_2520)class_3551.method_15450().update(class_1208.field_5731, new Dynamic((DynamicOps)class_2509.field_11560, (Object)tagStr), oldVersion, LitematicaSchematic.MINECRAFT_DATA_VERSION).getValue()).method_10714();
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("updateBlockName: failed to update Block Name [{}], preserving original state (data may become lost)", (Object)oldName);
            return oldName;
        }
    }

    public static class_2487 updateBlockStates(class_2487 oldBlockState, int oldVersion) {
        String blockName;
        String oldName = oldBlockState.method_10558("Name");
        if (!oldName.equalsIgnoreCase(blockName = SchematicConversionMaps.updateBlockName(oldName, oldVersion))) {
            oldBlockState.method_10582("Name", blockName);
        }
        try {
            return (class_2487)class_3551.method_15450().update(class_1208.field_5720, new Dynamic((DynamicOps)class_2509.field_11560, (Object)oldBlockState), oldVersion, LitematicaSchematic.MINECRAFT_DATA_VERSION).getValue();
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("updateBlockStates: failed to update Block State [{}], preserving original state (data may become lost)", (Object)(oldBlockState.method_10545("Name") ? oldBlockState.method_10558("Name") : "?"));
            return oldBlockState;
        }
    }

    public static class_2487 updateBlockEntity(class_2487 oldBlockEntity, int oldVersion) {
        try {
            return (class_2487)class_3551.method_15450().update(class_1208.field_5727, new Dynamic((DynamicOps)class_2509.field_11560, (Object)oldBlockEntity), oldVersion, LitematicaSchematic.MINECRAFT_DATA_VERSION).getValue();
        }
        catch (Exception e) {
            class_2338 pos = NbtUtils.readBlockPos((class_2487)oldBlockEntity);
            Litematica.LOGGER.warn("updateBlockEntity: failed to update Block Entity [{}] at [{}], preserving original state (data may become lost)", (Object)(oldBlockEntity.method_10545("id") ? oldBlockEntity.method_10558("id") : "?"), (Object)(pos != null ? pos.method_23854() : "?"));
            return oldBlockEntity;
        }
    }

    public static class_2487 updateEntity(class_2487 oldEntity, int oldVersion) {
        try {
            return (class_2487)class_3551.method_15450().update(class_1208.field_5729, new Dynamic((DynamicOps)class_2509.field_11560, (Object)oldEntity), oldVersion, LitematicaSchematic.MINECRAFT_DATA_VERSION).getValue();
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("updateEntity: failed to update Entity [{}], preserving original state (data may become lost)", (Object)(oldEntity.method_10545("id") ? oldEntity.method_10558("id") : "?"));
            return oldEntity;
        }
    }

    public static class_2487 checkForIdTag(class_2487 tags) {
        if (tags.method_10545("id")) {
            return tags;
        }
        if (tags.method_10545("Id")) {
            tags.method_10582("id", tags.method_10558("Id"));
            return tags;
        }
        if (tags.method_10545("Bees") || tags.method_10545("bees")) {
            tags.method_10582("id", "minecraft:beehive");
        } else if (tags.method_10545("TransferCooldown") && tags.method_10545("Items")) {
            tags.method_10582("id", "minecraft:hopper");
        } else if (tags.method_10545("SkullOwner")) {
            tags.method_10582("id", "minecraft:skull");
        } else if (tags.method_10545("Patterns") || tags.method_10545("patterns")) {
            tags.method_10582("id", "minecraft:banner");
        } else if (tags.method_10545("Sherds") || tags.method_10545("sherds")) {
            tags.method_10582("id", "minecraft:decorated_pot");
        } else if (tags.method_10545("last_interacted_slot") && tags.method_10545("Items")) {
            tags.method_10582("id", "minecraft:chiseled_bookshelf");
        } else if (tags.method_10545("CookTime") && tags.method_10545("Items")) {
            tags.method_10582("id", "minecraft:furnace");
        } else if (tags.method_10545("RecordItem")) {
            tags.method_10582("id", "minecraft:jukebox");
        } else if (tags.method_10545("Book") || tags.method_10545("book")) {
            tags.method_10582("id", "minecraft:lectern");
        } else if (tags.method_10545("front_text")) {
            tags.method_10582("id", "minecraft:sign");
        } else if (tags.method_10545("BrewTime") || tags.method_10545("Fuel")) {
            tags.method_10582("id", "minecraft:brewing_stand");
        } else if (tags.method_10545("LootTable") && tags.method_10545("LootTableSeed") || tags.method_10545("hit_direction") || tags.method_10545("item")) {
            tags.method_10582("id", "minecraft:suspicious_sand");
        } else if (tags.method_10545("SpawnData") || tags.method_10545("SpawnPotentials")) {
            tags.method_10582("id", "minecraft:spawner");
        } else if (tags.method_10545("normal_config")) {
            tags.method_10582("id", "minecraft:trial_spawner");
        } else if (tags.method_10545("shared_data")) {
            tags.method_10582("id", "minecraft:vault");
        } else if (tags.method_10545("pool") && tags.method_10545("final_state") && tags.method_10545("placement_priority")) {
            tags.method_10582("id", "minecraft:jigsaw");
        } else if (tags.method_10545("author") && tags.method_10545("metadata") && tags.method_10545("showboundingbox")) {
            tags.method_10582("id", "minecraft:structure_block");
        } else if (tags.method_10545("ExactTeleport") && tags.method_10545("Age")) {
            tags.method_10582("id", "minecraft:end_gateway");
        } else if (tags.method_10545("Items")) {
            tags.method_10582("id", "minecraft:chest");
        } else if (tags.method_10545("last_vibration_frequency") || tags.method_10545("listener")) {
            tags.method_10582("id", "minecraft:sculk_sensor");
        } else if (tags.method_10545("warning_level") || tags.method_10545("listener")) {
            tags.method_10582("id", "minecraft:sculk_shrieker");
        } else if (tags.method_10545("OutputSignal")) {
            tags.method_10582("id", "minecraft:comparator");
        } else if (tags.method_10545("facing") || tags.method_10545("extending")) {
            tags.method_10582("id", "minecraft:piston");
        } else if (tags.method_10545("x") && tags.method_10545("y") && tags.method_10545("z")) {
            tags.method_10582("id", "minecraft:piston");
        }
        if (tags.method_10545("Items")) {
            class_2499 items = SchematicConversionMaps.fixItemsTag(tags.method_10554("Items", 10));
            tags.method_10566("Items", (class_2520)items);
        }
        return tags;
    }

    private static class_2499 fixItemsTag(class_2499 items) {
        class_2499 newList = new class_2499();
        for (int i = 0; i < items.size(); ++i) {
            class_2487 itemEntry = SchematicConversionMaps.fixItemTypesFrom1_21_2(items.method_10602(i));
            if (itemEntry.method_10545("tag")) {
                class_2487 tag = null;
                try {
                    tag = itemEntry.method_10562("tag");
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (tag == null) {
                    itemEntry.method_10551("tag");
                } else {
                    if (tag.method_10573("BlockEntityTag", 10)) {
                        class_2487 entityEntry = tag.method_10562("BlockEntityTag");
                        if (entityEntry.method_10573("Items", 9)) {
                            class_2499 nestedItems = SchematicConversionMaps.fixItemsTag(entityEntry.method_10554("Items", 10));
                            entityEntry.method_10566("Items", (class_2520)nestedItems);
                        }
                        tag.method_10566("BlockEntityTag", (class_2520)entityEntry);
                    }
                    itemEntry.method_10566("tag", (class_2520)tag);
                }
            }
            newList.add((Object)itemEntry);
        }
        return newList;
    }

    private static class_2487 fixItemTypesFrom1_21_2(class_2487 nbt) {
        if (!nbt.method_10545("id")) {
            return nbt;
        }
        String id = nbt.method_10558("id");
        class_2960 newId = null;
        switch (id) {
            case "minecraft:pale_oak_boat": {
                newId = class_2960.method_60656((String)"oak_boat");
                break;
            }
            case "minecraft:pale_oak_chest_boat": {
                newId = class_2960.method_60656((String)"oak_chest_boat");
            }
        }
        if (newId != null) {
            nbt.method_10582("id", newId.toString());
        }
        return nbt;
    }

    public static class_2487 fixEntityTypesFrom1_21_2(class_2487 nbt) {
        if (!nbt.method_10545("id")) {
            return nbt;
        }
        if (nbt.method_10545("Items")) {
            class_2499 items = SchematicConversionMaps.fixItemsTag(nbt.method_10554("Items", 10));
            nbt.method_10566("Items", (class_2520)items);
        }
        String id = nbt.method_10558("id");
        class_2960 newId = null;
        String type = "";
        boolean boatFix = false;
        switch (id) {
            case "minecraft:oak_boat": 
            case "minecraft:pale_oak_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "oak";
                boatFix = true;
                break;
            }
            case "minecraft:spruce_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "spruce";
                boatFix = true;
                break;
            }
            case "minecraft:birch_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "birch";
                boatFix = true;
                break;
            }
            case "minecraft:jungle_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "jungle";
                boatFix = true;
                break;
            }
            case "minecraft:acacia_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "acacia";
                boatFix = true;
                break;
            }
            case "minecraft:cherry_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "cherry";
                boatFix = true;
                break;
            }
            case "minecraft:dark_oak_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "dark_oak";
                boatFix = true;
                break;
            }
            case "minecraft:mangrove_boat": {
                newId = class_2960.method_60656((String)"boat");
                type = "mangrove";
                boatFix = true;
                break;
            }
            case "minecraft:bamboo_raft": {
                newId = class_2960.method_60656((String)"boat");
                type = "bamboo";
                boatFix = true;
                break;
            }
            case "minecraft:oak_chest_boat": 
            case "minecraft:pale_oak_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "oak";
                boatFix = true;
                break;
            }
            case "minecraft:spruce_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "spruce";
                boatFix = true;
                break;
            }
            case "minecraft:birch_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "birch";
                boatFix = true;
                break;
            }
            case "minecraft:jungle_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "jungle";
                boatFix = true;
                break;
            }
            case "minecraft:acacia_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "acacia";
                boatFix = true;
                break;
            }
            case "minecraft:cherry_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "cherry";
                boatFix = true;
                break;
            }
            case "minecraft:dark_oak_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "dark_oak";
                boatFix = true;
                break;
            }
            case "minecraft:mangrove_chest_boat": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "mangrove";
                boatFix = true;
                break;
            }
            case "minecraft:bamboo_chest_raft": {
                newId = class_2960.method_60656((String)"chest_boat");
                type = "bamboo";
                boatFix = true;
                break;
            }
            default: {
                if (id.contains("_chest_boat")) {
                    newId = class_2960.method_60656((String)"chest_boat");
                    type = "oak";
                    boatFix = true;
                    break;
                }
                if (!id.contains("_boat")) break;
                newId = class_2960.method_60656((String)"boat");
                type = "oak";
                boatFix = true;
            }
        }
        if (newId != null) {
            nbt.method_10582("id", newId.toString());
        }
        if (boatFix) {
            nbt.method_10582("Type", type);
        }
        return nbt;
    }

    private static class ConversionData {
        private final int idMeta;
        private final String newStateString;
        private final String[] oldStateStrings;

        private ConversionData(int idMeta, String newStateString, String[] oldStateStrings) {
            this.idMeta = idMeta;
            this.newStateString = newStateString;
            this.oldStateStrings = oldStateStrings;
        }
    }
}

