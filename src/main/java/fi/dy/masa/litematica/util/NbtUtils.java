/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2505
 *  net.minecraft.class_2507
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.Litematica;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2505;
import net.minecraft.class_2507;

@Deprecated(forRemoval=true)
public class NbtUtils {
    @Nullable
    public static class_2338 readBlockPosFromArrayTag(class_2487 tag, String tagName) {
        int[] pos;
        if (tag.method_10573(tagName, 11) && (pos = tag.method_10561("Pos")).length == 3) {
            return new class_2338(pos[0], pos[1], pos[2]);
        }
        return null;
    }

    @Nullable
    public static class_243 readVec3dFromListTag(@Nullable class_2487 tag) {
        return NbtUtils.readVec3dFromListTag(tag, "Pos");
    }

    @Nullable
    public static class_243 readVec3dFromListTag(@Nullable class_2487 tag, String tagName) {
        class_2499 tagList;
        if (tag != null && tag.method_10573(tagName, 9) && (tagList = tag.method_10554(tagName, 6)).method_10601() == 6 && tagList.size() == 3) {
            return new class_243(tagList.method_10611(0), tagList.method_10611(1), tagList.method_10611(2));
        }
        return null;
    }

    @Nullable
    public static class_2382 readVec3iFromIntArray(@Nullable class_2487 tag, String tagName) {
        int[] arr;
        if (tag != null && tag.method_10573(tagName, 11) && (arr = tag.method_10561(tagName)) != null && arr.length == 3) {
            return new class_2382(arr[0], arr[1], arr[2]);
        }
        return null;
    }

    @Nullable
    public static class_2487 readNbtFromFile(File file) {
        FileInputStream is;
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        try {
            is = new FileInputStream(file);
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("Failed to read NBT data from file '{}' (failed to create the input stream)", (Object)file.getAbsolutePath());
            return null;
        }
        class_2487 nbt = null;
        if (is != null) {
            try {
                nbt = class_2507.method_10629((InputStream)is, (class_2505)class_2505.method_53898());
            }
            catch (Exception e) {
                try {
                    is.close();
                    is = new FileInputStream(file);
                    nbt = class_2507.method_10633((Path)file.toPath());
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            try {
                is.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (nbt == null) {
            Litematica.LOGGER.warn("Failed to read NBT data from file '{}'", (Object)file.getAbsolutePath());
        }
        return nbt;
    }
}

