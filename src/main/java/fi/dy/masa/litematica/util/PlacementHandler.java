/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1309
 *  net.minecraft.class_1750
 *  net.minecraft.class_1937
 *  net.minecraft.class_2244
 *  net.minecraft.class_2248
 *  net.minecraft.class_2286
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_243
 *  net.minecraft.class_2462
 *  net.minecraft.class_2680
 *  net.minecraft.class_2741
 *  net.minecraft.class_2747
 *  net.minecraft.class_2754
 *  net.minecraft.class_2760
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3611
 *  net.minecraft.class_3612
 *  net.minecraft.class_4538
 */
package fi.dy.masa.litematica.util;

import com.google.common.collect.ImmutableSet;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.util.EasyPlaceProtocol;
import fi.dy.masa.malilib.util.game.BlockUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_1750;
import net.minecraft.class_1937;
import net.minecraft.class_2244;
import net.minecraft.class_2248;
import net.minecraft.class_2286;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2462;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2747;
import net.minecraft.class_2754;
import net.minecraft.class_2760;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3611;
import net.minecraft.class_3612;
import net.minecraft.class_4538;

public class PlacementHandler {
    public static final ImmutableSet<class_2769<?>> WHITELISTED_PROPERTIES = ImmutableSet.of((Object)class_2741.field_12501, (Object)class_2741.field_12537, (Object)class_2741.field_17104, (Object)class_2741.field_12496, (Object)class_2741.field_12518, (Object)class_2741.field_12555, (Object[])new class_2769[]{class_2741.field_12506, class_2741.field_12534, class_2741.field_12520, class_2741.field_12525, class_2741.field_12481, class_2741.field_12545, class_2741.field_23333, class_2741.field_12507, class_2741.field_12542, class_2741.field_12485, class_2741.field_12503, class_2741.field_12505, class_2741.field_12494, class_2741.field_12524, class_2741.field_12532});
    public static final ImmutableSet<class_2769<?>> BLACKLISTED_PROPERTIES = ImmutableSet.of((Object)class_2741.field_12508, (Object)class_2741.field_12484);

    public static EasyPlaceProtocol getEffectiveProtocolVersion() {
        EasyPlaceProtocol protocol = (EasyPlaceProtocol)Configs.Generic.EASY_PLACE_PROTOCOL.getOptionListValue();
        if (protocol == EasyPlaceProtocol.AUTO) {
            if (class_310.method_1551().method_1542() || EntitiesDataStorage.getInstance().hasServuxServer() || DataManager.hasServuxServer()) {
                return EasyPlaceProtocol.V3;
            }
            if (DataManager.isCarpetServer()) {
                return EasyPlaceProtocol.V2;
            }
            return EasyPlaceProtocol.SLAB_ONLY;
        }
        return protocol;
    }

    @Nullable
    public static class_2680 applyPlacementProtocolToPlacementState(class_2680 state, UseContext context) {
        EasyPlaceProtocol protocol = PlacementHandler.getEffectiveProtocolVersion();
        if (protocol == EasyPlaceProtocol.V3) {
            return PlacementHandler.applyPlacementProtocolV3(state, context);
        }
        if (protocol == EasyPlaceProtocol.V2) {
            return PlacementHandler.applyPlacementProtocolV2(state, context);
        }
        return state;
    }

    public static class_2680 applyPlacementProtocolV2(class_2680 state, UseContext context) {
        int protocolValue = (int)(context.getHitVec().field_1352 - (double)context.getPos().method_10263()) - 2;
        if (protocolValue < 0) {
            return state;
        }
        Optional property = BlockUtils.getFirstDirectionProperty((class_2680)state);
        if (property.isPresent()) {
            if ((state = PlacementHandler.applyDirectionProperty(state, context, (class_2754<class_2350>)((class_2754)property.get()), protocolValue)) == null) {
                return null;
            }
        } else if (state.method_28498((class_2769)class_2741.field_12496)) {
            class_2350.class_2351 axis = class_2350.class_2351.field_23780[(protocolValue >> 1 & 3) % 3];
            if (class_2741.field_12496.method_11898().contains(axis)) {
                state = (class_2680)state.method_11657((class_2769)class_2741.field_12496, (Comparable)axis);
            }
        }
        if ((protocolValue >>>= 5) > 0) {
            class_2248 block = state.method_26204();
            if (block instanceof class_2462) {
                Integer delay = protocolValue;
                if (class_2462.field_11451.method_11898().contains(delay)) {
                    state = (class_2680)state.method_11657((class_2769)class_2462.field_11451, (Comparable)delay);
                }
            } else if (block instanceof class_2286) {
                state = (class_2680)state.method_11657((class_2769)class_2286.field_10789, (Comparable)class_2747.field_12578);
            }
        }
        if (state.method_28498((class_2769)class_2741.field_12518)) {
            state = (class_2680)state.method_11657((class_2769)class_2741.field_12518, (Comparable)(protocolValue > 0 ? class_2760.field_12619 : class_2760.field_12617));
        }
        return state;
    }

    public static <T extends Comparable<T>> class_2680 applyPlacementProtocolV3(class_2680 state, UseContext context) {
        class_2769 prop;
        int protocolValue = (int)(context.getHitVec().field_1352 - (double)context.getPos().method_10263()) - 2;
        class_2680 oldState = state;
        if (protocolValue < 0) {
            return oldState;
        }
        Optional property = BlockUtils.getFirstDirectionProperty((class_2680)state);
        if (property.isPresent() && property.get() != class_2741.field_28062) {
            if ((state = PlacementHandler.applyDirectionProperty(state, context, (class_2754<class_2350>)((class_2754)property.get()), protocolValue)) == null) {
                return null;
            }
            if (Configs.Generic.EASY_PLACE_SP_VALIDATION.getBooleanValue()) {
                if (state.method_26184((class_4538)context.getWorld(), context.getPos())) {
                    oldState = state;
                } else {
                    state = oldState;
                }
            } else {
                oldState = state;
            }
            protocolValue >>>= 3;
        }
        protocolValue >>>= 1;
        ArrayList<class_2769> propList = new ArrayList<class_2769>(state.method_26204().method_9595().method_11659());
        propList.sort(Comparator.comparing(class_2769::method_11899));
        try {
            for (class_2769 p : propList) {
                if (property.isPresent() && ((class_2754)property.get()).equals((Object)p) || !WHITELISTED_PROPERTIES.contains((Object)p) || BLACKLISTED_PROPERTIES.contains((Object)p)) continue;
                prop = p;
                ArrayList list = new ArrayList(prop.method_11898());
                list.sort(Comparable::compareTo);
                int requiredBits = class_3532.method_15351((int)class_3532.method_15339((int)list.size()));
                int bitMask = ~(-1 << requiredBits);
                int valueIndex = protocolValue & bitMask;
                if (valueIndex < 0 || valueIndex >= list.size()) continue;
                Comparable value = (Comparable)list.get(valueIndex);
                if (!state.method_11654(prop).equals(value) && value != class_2771.field_12682) {
                    state = (class_2680)state.method_11657(prop, value);
                    if (Configs.Generic.EASY_PLACE_SP_VALIDATION.getBooleanValue()) {
                        if (state.method_26184((class_4538)context.getWorld(), context.getPos())) {
                            oldState = state;
                        } else {
                            state = oldState;
                        }
                    } else {
                        oldState = state;
                    }
                }
                protocolValue >>>= requiredBits;
            }
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("Exception trying to apply placement protocol value", (Throwable)e);
        }
        for (class_2769 p : BLACKLISTED_PROPERTIES) {
            if (!state.method_28498(p)) continue;
            prop = p;
            class_2680 def = state.method_26204().method_9564();
            state = (class_2680)state.method_11657(prop, def.method_11654(prop));
        }
        if (state.method_28498((class_2769)class_2741.field_12508) && (oldState.method_28498((class_2769)class_2741.field_12508) && ((Boolean)oldState.method_11654((class_2769)class_2741.field_12508)).booleanValue() || oldState.method_26227() != null && oldState.method_26227().method_15772().method_15780((class_3611)class_3612.field_15910))) {
            state = (class_2680)state.method_11657((class_2769)class_2741.field_12508, (Comparable)Boolean.valueOf(true));
        }
        if (Configs.Generic.EASY_PLACE_SP_VALIDATION.getBooleanValue()) {
            if (state.method_26184((class_4538)context.getWorld(), context.getPos())) {
                return state;
            }
            return null;
        }
        return state;
    }

    private static class_2680 applyDirectionProperty(class_2680 state, UseContext context, class_2754<class_2350> property, int protocolValue) {
        class_2350 facingOrig;
        class_2350 facing = facingOrig = (class_2350)state.method_11654(property);
        int decodedFacingIndex = (protocolValue & 0xF) >> 1;
        if (decodedFacingIndex == 6) {
            facing = facing.method_10153();
        } else if (decodedFacingIndex >= 0 && decodedFacingIndex <= 5) {
            facing = class_2350.method_10143((int)decodedFacingIndex);
            if (!property.method_11898().contains(facing)) {
                facing = context.getEntity().method_5735().method_10153();
            }
        }
        if (facing != facingOrig && property.method_11898().contains(facing)) {
            if (state.method_26204() instanceof class_2244) {
                class_2338 headPos = context.pos.method_10093(facing);
                class_1750 ctx = context.getItemPlacementContext();
                if (!context.getWorld().method_8320(headPos).method_26166(ctx)) {
                    return null;
                }
            }
            state = (class_2680)state.method_11657(property, (Comparable)facing);
        }
        return state;
    }

    public static class UseContext {
        private final class_1937 world;
        private final class_2338 pos;
        private final class_2350 side;
        private final class_243 hitVec;
        private final class_1309 entity;
        private final class_1268 hand;
        @Nullable
        private final class_1750 itemPlacementContext;

        public UseContext(class_1937 world, class_2338 pos, class_2350 side, class_243 hitVec, class_1309 entity, class_1268 hand, @Nullable class_1750 itemPlacementContext) {
            this.world = world;
            this.pos = pos;
            this.side = side;
            this.hitVec = hitVec;
            this.entity = entity;
            this.hand = hand;
            this.itemPlacementContext = itemPlacementContext;
        }

        public static UseContext from(class_1750 ctx, class_1268 hand) {
            class_243 pos = ctx.method_17698();
            return new UseContext(ctx.method_8045(), ctx.method_8037(), ctx.method_8038(), new class_243(pos.field_1352, pos.field_1351, pos.field_1350), (class_1309)ctx.method_8036(), hand, ctx);
        }

        public class_1937 getWorld() {
            return this.world;
        }

        public class_2338 getPos() {
            return this.pos;
        }

        public class_2350 getSide() {
            return this.side;
        }

        public class_243 getHitVec() {
            return this.hitVec;
        }

        public class_1309 getEntity() {
            return this.entity;
        }

        public class_1268 getHand() {
            return this.hand;
        }

        @Nullable
        public class_1750 getItemPlacementContext() {
            return this.itemPlacementContext;
        }
    }
}

