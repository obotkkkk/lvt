/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  javax.annotation.Nullable
 *  net.minecraft.class_2248
 *  net.minecraft.class_2281
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2415
 *  net.minecraft.class_2680
 *  net.minecraft.class_2689
 *  net.minecraft.class_2745
 *  net.minecraft.class_2769
 *  net.minecraft.class_2960
 *  net.minecraft.class_5321
 *  net.minecraft.class_6862
 *  net.minecraft.class_6880$class_6883
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 */
package fi.dy.masa.litematica.util;

import com.google.common.base.Splitter;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import java.util.Iterator;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2350;
import net.minecraft.class_2415;
import net.minecraft.class_2680;
import net.minecraft.class_2689;
import net.minecraft.class_2745;
import net.minecraft.class_2769;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_6862;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import net.minecraft.class_7924;

public class BlockUtils {
    private static final Splitter COMMA_SPLITTER = Splitter.on((char)',');
    private static final Splitter EQUAL_SPLITTER = Splitter.on((char)'=').limit(2);

    public static class_2680 fixMirrorDoubleChest(class_2680 state, class_2415 mirror, class_2745 type) {
        class_2350 facing = (class_2350)state.method_11654((class_2769)class_2281.field_10768);
        class_2350.class_2351 axis = facing.method_10166();
        if (mirror == class_2415.field_11301) {
            state = (class_2680)state.method_11657((class_2769)class_2281.field_10770, (Comparable)type.method_11824());
            if (axis == class_2350.class_2351.field_11048) {
                state = (class_2680)state.method_11657((class_2769)class_2281.field_10768, (Comparable)facing.method_10153());
            }
        } else if (mirror == class_2415.field_11300) {
            state = (class_2680)state.method_11657((class_2769)class_2281.field_10770, (Comparable)type.method_11824());
            if (axis == class_2350.class_2351.field_11051) {
                state = (class_2680)state.method_11657((class_2769)class_2281.field_10768, (Comparable)facing.method_10153());
            }
        }
        return state;
    }

    public static Optional<class_2680> getBlockStateFromString(String str, int minecraftDataVersion) {
        int index = str.indexOf("[");
        String blockName = index != -1 ? str.substring(0, index) : str;
        try {
            Optional opt;
            blockName = SchematicConversionMaps.updateBlockName(blockName, minecraftDataVersion);
            class_2960 id = class_2960.method_12829((String)blockName);
            if (class_7923.field_41175.method_10250(id) && (opt = class_7923.field_41175.method_10223(id)).isPresent()) {
                class_2248 block = (class_2248)((class_6880.class_6883)opt.get()).comp_349();
                class_2680 state = block.method_9564();
                if (index != -1 && str.length() > index + 4 && str.charAt(str.length() - 1) == ']') {
                    class_2689 stateManager = block.method_9595();
                    String propStr = str.substring(index + 1, str.length() - 1);
                    for (String propAndVal : COMMA_SPLITTER.split((CharSequence)propStr)) {
                        Object val;
                        class_2769 prop;
                        Iterator valIter = EQUAL_SPLITTER.split((CharSequence)propAndVal).iterator();
                        if (!valIter.hasNext() || (prop = stateManager.method_11663((String)valIter.next())) == null || !valIter.hasNext() || (val = BlockUtils.getPropertyValueByName(prop, (String)valIter.next())) == null) continue;
                        state = BlockUtils.getBlockStateWithProperty(state, prop, val);
                    }
                }
                return Optional.of(state);
            }
        }
        catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public static <T extends Comparable<T>> class_2680 getBlockStateWithProperty(class_2680 state, class_2769<T> prop, Comparable<?> value) {
        return (class_2680)state.method_11657(prop, value);
    }

    @Nullable
    public static <T extends Comparable<T>> T getPropertyValueByName(class_2769<T> prop, String valStr) {
        return (T)((Comparable)prop.method_11900(valStr).orElse(null));
    }

    public static boolean blocksHaveSameProperties(class_2680 state1, class_2680 state2) {
        class_2689 stateManager1 = state1.method_26204().method_9595();
        class_2689 stateManager2 = state2.method_26204().method_9595();
        return stateManager1.method_11659().equals(stateManager2.method_11659());
    }

    public static Optional<class_2248> getBlockFromString(String str) {
        int index = str.indexOf("[");
        String blockName = index != -1 ? str.substring(0, index) : str;
        try {
            class_2960 id = class_2960.method_12829((String)blockName);
            if (class_7923.field_41175.method_10250(id)) {
                class_2248 block = (class_2248)class_7923.field_41175.method_63535(id);
                return Optional.of(block);
            }
        }
        catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public static Optional<class_6862<class_2248>> getBlockTagFromString(String str) {
        if (str.startsWith("#")) {
            try {
                String tagName = str.substring(1);
                class_2960 id = class_2960.method_12829((String)tagName);
                class_6862 blockTag = class_6862.method_40092((class_5321)class_7924.field_41254, (class_2960)id);
                return Optional.of(blockTag);
            }
            catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}

