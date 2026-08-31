/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1309
 *  net.minecraft.class_1532
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2561
 *  net.minecraft.class_2561$class_2562
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3730
 *  net.minecraft.class_5819
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_746
 *  net.minecraft.class_9817
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.ApiStatus$Experimental
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.mixin.entity.IMixinEntity;
import fi.dy.masa.litematica.mixin.world.IMixinWorld;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1532;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3730;
import net.minecraft.class_5819;
import net.minecraft.class_7225;
import net.minecraft.class_746;
import net.minecraft.class_9817;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

public class EntityUtils {
    public static final Predicate<class_1297> NOT_PLAYER = entity -> !(entity instanceof class_1657);
    private static boolean entityDebugRandom;
    private static boolean entityDebugRandom2;

    public static boolean isCreativeMode(class_1657 player) {
        return player.method_31549().field_7477;
    }

    public static boolean hasToolItem(class_1309 entity) {
        return EntityUtils.hasToolItemInHand(entity, class_1268.field_5808) || EntityUtils.hasToolItemInHand(entity, class_1268.field_5810);
    }

    public static boolean hasToolItemInHand(class_1309 entity, class_1268 hand) {
        if (DataManager.getInstance().hasToolItemComponents()) {
            class_1799 toolItem = DataManager.getInstance().getToolItemComponents();
            class_1799 stackHand = entity.method_5998(hand);
            if (toolItem != null) {
                return InventoryUtils.areStacksAndNbtEqual((class_1799)toolItem, (class_1799)stackHand);
            }
            return false;
        }
        class_1799 toolItem = DataManager.getToolItem();
        if (toolItem.method_7960()) {
            return entity.method_6047().method_7960();
        }
        class_1799 stackHand = entity.method_5998(hand);
        return InventoryUtils.areStacksEqualIgnoreNbt((class_1799)toolItem, (class_1799)stackHand);
    }

    @Nullable
    public static class_1268 getUsedHandForItem(class_1657 player, class_1799 stack) {
        class_1268 hand = null;
        if (InventoryUtils.areStacksEqualIgnoreNbt((class_1799)player.method_6047(), (class_1799)stack)) {
            hand = class_1268.field_5808;
        } else if (player.method_6047().method_7960() && InventoryUtils.areStacksEqualIgnoreNbt((class_1799)player.method_6079(), (class_1799)stack)) {
            hand = class_1268.field_5810;
        }
        return hand;
    }

    public static boolean areStacksEqualIgnoreDurability(class_1799 stack1, class_1799 stack2) {
        return InventoryUtils.areStacksEqualIgnoreDurability((class_1799)stack1, (class_1799)stack2);
    }

    public static class_2350 getHorizontalLookingDirection(class_1297 entity) {
        return class_2350.method_10150((double)entity.method_36454());
    }

    public static class_2350 getVerticalLookingDirection(class_1297 entity) {
        return entity.method_36455() > 0.0f ? class_2350.field_11033 : class_2350.field_11036;
    }

    public static class_2350 getClosestLookingDirection(class_1297 entity) {
        if (entity.method_36455() > 60.0f) {
            return class_2350.field_11033;
        }
        if (-entity.method_36455() > 60.0f) {
            return class_2350.field_11036;
        }
        return EntityUtils.getHorizontalLookingDirection(entity);
    }

    @Nullable
    public static <T extends class_1297> T findEntityByUUID(List<T> list, UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (class_1297 entity : list) {
            if (!entity.method_5667().equals(uuid)) continue;
            return (T)entity;
        }
        return null;
    }

    public static void initEntityUtils() {
        class_5819 rand = class_5819.method_43047();
        entityDebugRandom = rand.method_43056();
        entityDebugRandom2 = rand.method_43056();
    }

    public static Pair<String, String> getEntityDebug() {
        String name;
        class_310 mc = class_310.method_1551();
        if (mc.field_1724 == null || !entityDebugRandom) {
            return Pair.of((Object)"", (Object)"");
        }
        switch (name = mc.field_1724.method_7334().getName().toLowerCase()) {
            case "sakuraryoko": {
                return Pair.of((Object)"Sakuramatica", (Object)"The Sakura Goddess Herself.");
            }
            case "docm77": {
                return Pair.of((Object)"Goatmatica", (Object)"Grind. Optimize. Automate. Thrive.");
            }
            case "xisuma": 
            case "xisumavoid": {
                return entityDebugRandom2 ? Pair.of((Object)"Xisumatica", (Object)"Chief architect & humble leader.") : Pair.of((Object)"Xisumatica", (Object)"Check out Soulside Eclipse on Spotify.");
            }
            case "renthedog": 
            case "rendog": {
                return entityDebugRandom2 ? Pair.of((Object)"Dogmatica", (Object)"Gigacorps' most famous employee.") : Pair.of((Object)"Renmatica", (Object)"Docm77's single ladies' favorite.");
            }
            case "geminitay": {
                return entityDebugRandom2 ? Pair.of((Object)"Slaymatica", (Object)"God's favorite Princess.") : Pair.of((Object)"Slaymatica", (Object)"Hermitcraft's chief remover of heads.");
            }
            case "pearlescentmoon": {
                return entityDebugRandom2 ? Pair.of((Object)"Pearlmatica", (Object)"The queen of aussie ping.") : Pair.of((Object)"", (Object)"");
            }
            case "falsesymmetry": {
                return entityDebugRandom2 ? Pair.of((Object)"Queenmatica", (Object)"The Queen of Hearts, Heads, and Body Parts.") : Pair.of((Object)"", (Object)"");
            }
            case "tango": {
                return entityDebugRandom2 ? Pair.of((Object)"Tangomatica", (Object)"The Dungeon Master.") : Pair.of((Object)"Tangomatica", (Object)"Master of the thingificator.");
            }
            case "etho": 
            case "ethoslab": {
                return entityDebugRandom2 ? Pair.of((Object)"Slabmatica", (Object)"The Canadian legend.") : Pair.of((Object)"", (Object)"");
            }
            case "ijevin": {
                return entityDebugRandom2 ? Pair.of((Object)"iJevinatica", (Object)"iJevin's favorite mod suite (thank you!)") : Pair.of((Object)"", (Object)"");
            }
            case "cubfan135": {
                return entityDebugRandom2 ? Pair.of((Object)"Cubmatica", (Object)"Ladies and gentlemen; Beautiful, absolutely beautiful.") : Pair.of((Object)"", (Object)"");
            }
            case "smajor1995": {
                return entityDebugRandom2 ? Pair.of((Object)"Scottmatica", (Object)"The most friendly and soothing voice in the game.") : Pair.of((Object)"", (Object)"");
            }
            case "shubbleyt": {
                return entityDebugRandom2 ? Pair.of((Object)"Starmatica", (Object)"Red Mushroom blocks are soo underrated.") : Pair.of((Object)"", (Object)"");
            }
            case "goodtimewithscar": {
                return entityDebugRandom2 ? Pair.of((Object)"Scarmatica", (Object)"The Ore Snatcher.") : Pair.of((Object)"Scarmatica", (Object)"Architect of the whimsy.");
            }
            case "joehillssays": 
            case "joehillstsd": {
                return entityDebugRandom2 ? Pair.of((Object)"Joematica", (Object)"One of the True Hermits.") : Pair.of((Object)"Hillsmatica", (Object)"Howdy y'all from Nashville, TN!");
            }
        }
        return Pair.of((Object)"", (Object)"");
    }

    @Nullable
    public static String getEntityId(class_1297 entity) {
        class_1299 entitytype = entity.method_5864();
        class_2960 resourcelocation = class_1299.method_5890((class_1299)entitytype);
        return entitytype.method_5893() && resourcelocation != null ? resourcelocation.toString() : null;
    }

    @Nullable
    private static class_1297 createEntityFromNBTSingle(class_2487 nbt, class_1937 world) {
        try {
            Optional optional = class_1299.method_5892((class_2487)nbt, (class_1937)world, (class_3730)class_3730.field_52444);
            if (optional.isPresent()) {
                class_1297 entity = (class_1297)optional.get();
                entity.method_5826(UUID.randomUUID());
                return entity;
            }
        }
        catch (Exception err) {
            Litematica.LOGGER.error("createEntityFromNBTSingle: Exception; {}", (Object)err.getLocalizedMessage());
        }
        return null;
    }

    @Nullable
    public static class_1297 createEntityAndPassengersFromNBT(class_2487 nbt, class_1937 world) {
        class_1297 entity = EntityUtils.createEntityFromNBTSingle(nbt, world);
        if (entity == null) {
            return null;
        }
        if (nbt.method_10573("Passengers", 9)) {
            class_2499 taglist = nbt.method_10554("Passengers", 10);
            for (int i = 0; i < taglist.size(); ++i) {
                class_1297 passenger = EntityUtils.createEntityAndPassengersFromNBT(taglist.method_10602(i), world);
                if (passenger == null) continue;
                passenger.method_5873(entity, true);
            }
        }
        return entity;
    }

    public static void spawnEntityAndPassengersInWorld(class_1297 entity, class_1937 world) {
        if (world.method_8649(entity) && entity.method_5782()) {
            for (class_1297 passenger : entity.method_5685()) {
                class_243 adjPos = entity.method_52538(passenger);
                passenger.method_5808(adjPos.method_10216(), adjPos.method_10214(), adjPos.method_10215(), passenger.method_36454(), passenger.method_36455());
                EntityUtils.setEntityRotations(passenger, passenger.method_36454(), passenger.method_36455());
                EntityUtils.spawnEntityAndPassengersInWorld(passenger, world);
                entity.method_24201(passenger);
            }
        }
    }

    public static void setEntityRotations(class_1297 entity, float yaw, float pitch) {
        entity.method_36456(yaw);
        entity.field_5982 = yaw;
        entity.method_36457(pitch);
        entity.field_6004 = pitch;
        if (entity instanceof class_1309) {
            class_1309 livingBase = (class_1309)entity;
            livingBase.field_6241 = yaw;
            livingBase.field_6283 = yaw;
            livingBase.field_6259 = yaw;
            livingBase.field_6220 = yaw;
        }
    }

    public static List<class_1297> getEntitiesWithinSubRegion(class_1937 world, class_2338 origin, class_2338 regionPos, class_2338 regionSize, SchematicPlacement schematicPlacement, SubRegionPlacement placement) {
        class_2338 regionPosRelTransformed = PositionUtils.getTransformedBlockPos(regionPos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        class_2338 posEndAbs = PositionUtils.getTransformedPlacementPosition(regionSize.method_10069(-1, -1, -1), schematicPlacement, placement).method_10081((class_2382)regionPosRelTransformed).method_10081((class_2382)origin);
        class_2338 regionPosAbs = regionPosRelTransformed.method_10081((class_2382)origin);
        class_238 bb = PositionUtils.createEnclosingAABB(regionPosAbs, posEndAbs);
        return world.method_8333(null, bb, NOT_PLAYER);
    }

    public static boolean shouldPickBlock(class_1657 player) {
        return Configs.Generic.PICK_BLOCK_ENABLED.getBooleanValue() && (!Configs.Generic.TOOL_ITEM_ENABLED.getBooleanValue() || !EntityUtils.hasToolItem((class_1309)player)) && Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue();
    }

    @Deprecated
    public static void loadNbtIntoEntity(class_1297 entity, class_2487 nbt) {
        entity.field_6017 = nbt.method_10583("FallDistance");
        entity.method_20803((int)nbt.method_10568("Fire"));
        if (nbt.method_10545("Air")) {
            entity.method_5855((int)nbt.method_10568("Air"));
        }
        entity.method_24830(nbt.method_10577("OnGround"));
        entity.method_5684(nbt.method_10577("Invulnerable"));
        entity.method_51850(nbt.method_10550("PortalCooldown"));
        if (nbt.method_25928("UUID")) {
            entity.method_5826(nbt.method_25926("UUID"));
        }
        if (nbt.method_10573("CustomName", 8)) {
            String string = nbt.method_10558("CustomName");
            entity.method_5665((class_2561)class_2561.class_2562.method_10877((String)string, (class_7225.class_7874)entity.method_56673()));
        }
        entity.method_5880(nbt.method_10577("CustomNameVisible"));
        entity.method_5803(nbt.method_10577("Silent"));
        entity.method_5875(nbt.method_10577("NoGravity"));
        entity.method_5834(nbt.method_10577("Glowing"));
        entity.method_32317(nbt.method_10550("TicksFrozen"));
        if (nbt.method_10573("Tags", 9)) {
            entity.method_5752().clear();
            class_2499 nbtList4 = nbt.method_10554("Tags", 8);
            int max = Math.min(nbtList4.size(), 1024);
            for (int i = 0; i < max; ++i) {
                entity.method_5752().add(nbtList4.method_10608(i));
            }
        }
        if (entity instanceof class_9817) {
            EntityUtils.readLeashableEntityCustomData(entity, nbt);
        } else {
            ((IMixinEntity)entity).litematica_readCustomDataFromNbt(nbt);
        }
    }

    @Deprecated
    private static void readLeashableEntityCustomData(class_1297 entity, class_2487 nbt) {
        class_310 mc = class_310.method_1551();
        if (mc.field_1687 == null) {
            return;
        }
        assert (entity instanceof class_9817);
        class_9817 leashable = (class_9817)entity;
        ((IMixinEntity)entity).litematica_readCustomDataFromNbt(nbt);
        if (leashable.method_60955() != null && leashable.method_60955().field_52218 != null) {
            leashable.method_60955().field_52218.ifLeft(uuid -> leashable.method_60964((class_1297)((IMixinWorld)mc.field_1687).litematica_getEntityLookup().method_31808(uuid), false)).ifRight(pos -> leashable.method_60964((class_1297)class_1532.method_6932((class_1937)mc.field_1687, (class_2338)pos), false));
        }
    }

    @ApiStatus.Experimental
    public static boolean setFakedSneakingState(boolean sneaking) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player != null && player.method_5715() != sneaking) {
            player.method_5660(sneaking);
            return true;
        }
        return false;
    }

    @Nullable
    @ApiStatus.Experimental
    public static class_1268 getUsedHandForItem(class_1309 entity, class_1799 stack, boolean lenient) {
        class_1268 hand = null;
        class_1268 tmpHand = entity.method_6047().method_7960() ? class_1268.field_5810 : class_1268.field_5808;
        class_1799 handStack = entity.method_5998(tmpHand);
        if (lenient && InventoryUtils.areStacksEqualIgnoreDurability((class_1799)handStack, (class_1799)stack) || !lenient && InventoryUtils.areStacksEqual((class_1799)handStack, (class_1799)stack)) {
            hand = tmpHand;
        }
        return hand;
    }

    public static class_2499 updatePassengersToRelativeRegionPos(class_2499 passengers, class_2338 relPos) {
        class_2499 newList = new class_2499();
        for (int i = 0; i < passengers.size(); ++i) {
            class_2487 entry = passengers.method_10602(i);
            if (entry.method_33133()) continue;
            if (entry.method_10545("Pos")) {
                class_243 pos = NbtUtils.readVec3dFromListTag((class_2487)entry);
                class_243 adjPos = new class_243(pos.method_10216() - (double)relPos.method_10263(), pos.method_10214() - (double)relPos.method_10264(), pos.method_10215() - (double)relPos.method_10260());
                NbtUtils.writeVec3dToListTag((class_243)adjPos, (class_2487)entry);
                newList.add((Object)entry);
                continue;
            }
            newList.add((Object)entry);
        }
        return newList;
    }
}

