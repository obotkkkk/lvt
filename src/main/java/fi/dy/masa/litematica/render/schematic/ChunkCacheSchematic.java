/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1920
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_1959
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2818
 *  net.minecraft.class_2818$class_2819
 *  net.minecraft.class_2823
 *  net.minecraft.class_3568
 *  net.minecraft.class_3610
 *  net.minecraft.class_638
 *  net.minecraft.class_6539
 *  net.minecraft.class_8527
 */
package fi.dy.masa.litematica.render.schematic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1920;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_1959;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2823;
import net.minecraft.class_3568;
import net.minecraft.class_3610;
import net.minecraft.class_638;
import net.minecraft.class_6539;
import net.minecraft.class_8527;

public class ChunkCacheSchematic
implements class_1920,
class_2823 {
    private static final class_2680 AIR = class_2246.field_10124.method_9564();
    protected final class_1937 world;
    protected final class_638 worldClient;
    protected int chunkStartX;
    protected int chunkStartZ;
    protected class_2818[][] chunkArray;
    protected boolean empty;

    public ChunkCacheSchematic(@Nonnull class_1937 worldIn, @Nonnull class_638 clientWorld, @Nonnull class_2338 pos, int expand) {
        this.world = worldIn;
        this.worldClient = clientWorld;
        int chunkX = pos.method_10263() >> 4;
        int chunkZ = pos.method_10260() >> 4;
        this.chunkStartX = pos.method_10263() - expand >> 4;
        this.chunkStartZ = pos.method_10260() - expand >> 4;
        int chunkEndX = pos.method_10263() + expand + 15 >> 4;
        int chunkEndZ = pos.method_10260() + expand + 15 >> 4;
        this.chunkArray = new class_2818[chunkEndX - this.chunkStartX + 1][chunkEndZ - this.chunkStartZ + 1];
        this.empty = true;
        for (int cx = this.chunkStartX; cx <= chunkEndX; ++cx) {
            for (int cz = this.chunkStartZ; cz <= chunkEndZ; ++cz) {
                class_2818 chunk;
                this.chunkArray[cx - this.chunkStartX][cz - this.chunkStartZ] = chunk = worldIn.method_8497(cx, cz);
                if (cx != chunkX || cz != chunkZ || chunk.method_12228(worldIn.method_31607(), worldIn.method_31600() - 1)) continue;
                this.empty = false;
            }
        }
    }

    public class_1922 method_16399() {
        return this.world;
    }

    public class_8527 method_12246(int chunkX, int chunkZ) {
        return this.worldClient.method_8497(chunkX, chunkZ);
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public class_2680 method_8320(class_2338 pos) {
        class_2818 chunk;
        int cx = (pos.method_10263() >> 4) - this.chunkStartX;
        int cz = (pos.method_10260() >> 4) - this.chunkStartZ;
        if (cx >= 0 && cx < this.chunkArray.length && cz >= 0 && cz < this.chunkArray[cx].length && (chunk = this.chunkArray[cx][cz]) != null) {
            return chunk.method_8320(pos);
        }
        return AIR;
    }

    @Nullable
    public class_2586 method_8321(class_2338 pos) {
        return this.getBlockEntity(pos, class_2818.class_2819.field_12859);
    }

    @Nullable
    public class_2586 getBlockEntity(class_2338 pos, class_2818.class_2819 type) {
        int i = (pos.method_10263() >> 4) - this.chunkStartX;
        int j = (pos.method_10260() >> 4) - this.chunkStartZ;
        return this.chunkArray[i][j].method_12201(pos, type);
    }

    public class_3610 method_8316(class_2338 pos) {
        return this.method_8320(pos).method_26227();
    }

    public class_3568 method_22336() {
        return this.world.method_22336();
    }

    public int method_23752(class_2338 pos, class_6539 colorResolver) {
        return colorResolver.getColor((class_1959)this.worldClient.method_23753(pos).comp_349(), (double)pos.method_10263(), (double)pos.method_10260());
    }

    public float method_24852(class_2350 direction, boolean bl) {
        return this.worldClient.method_24852(direction, bl);
    }

    public int method_31605() {
        return this.world.method_31605();
    }

    public int method_31607() {
        return this.world.method_31607();
    }
}

