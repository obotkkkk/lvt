/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2343
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_2818
 *  net.minecraft.class_2818$class_2819
 *  net.minecraft.class_2826
 */
package fi.dy.masa.litematica.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2343;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;

public class ChunkSchematic
extends class_2818 {
    private static final class_2680 AIR = class_2246.field_10124.method_9564();
    private final List<class_1297> entityList = new ArrayList<class_1297>();
    private final long timeCreated;
    private final int bottomY;
    private final int topY;
    private int entityCount;
    private boolean isEmpty = true;

    public ChunkSchematic(class_1937 worldIn, class_1923 pos) {
        super(worldIn, pos);
        this.timeCreated = worldIn.method_8510();
        this.bottomY = worldIn.method_31607();
        this.topY = worldIn.method_31600();
    }

    public class_2680 method_8320(class_2338 pos) {
        class_2826 chunkSection;
        int x = pos.method_10263() & 0xF;
        int y = pos.method_10264();
        int z = pos.method_10260() & 0xF;
        int cy = this.method_31602(y);
        y &= 0xF;
        class_2826[] sections = this.method_12006();
        if (cy >= 0 && cy < sections.length && !(chunkSection = sections[cy]).method_38292()) {
            return chunkSection.method_12254(x, y, z);
        }
        return AIR;
    }

    public class_2680 method_12010(class_2338 pos, class_2680 state, boolean isMoving) {
        class_2586 te;
        class_2680 stateOld = this.method_8320(pos);
        int y = pos.method_10264();
        if (stateOld == state || y >= this.topY || y < this.bottomY) {
            return null;
        }
        int x = pos.method_10263() & 0xF;
        int z = pos.method_10260() & 0xF;
        int cy = this.method_31602(y);
        class_2248 blockNew = state.method_26204();
        class_2248 blockOld = stateOld.method_26204();
        class_2826 section = this.method_12006()[cy];
        if (section.method_38292() && state.method_26215()) {
            return null;
        }
        y &= 0xF;
        if (!state.method_26215()) {
            this.isEmpty = false;
        }
        section.method_16675(x, y, z, state);
        if (blockOld != blockNew) {
            this.method_12200().method_8544(pos);
        }
        if (section.method_12254(x, y, z).method_26204() != blockNew) {
            return null;
        }
        if (state.method_31709() && blockNew instanceof class_2343 && (te = this.method_12201(pos, class_2818.class_2819.field_12859)) == null && (te = ((class_2343)blockNew).method_10123(pos, state)) != null) {
            this.method_12200().method_8500(pos).method_12007(te);
        }
        this.method_12044();
        return stateOld;
    }

    public void method_12002(class_1297 entity) {
        this.entityList.add(entity);
        ++this.entityCount;
    }

    public List<class_1297> getEntityList() {
        return this.entityList;
    }

    public int getEntityCount() {
        return this.entityCount;
    }

    public int getTileEntityCount() {
        return this.field_34543.size();
    }

    public long getTimeCreated() {
        return this.timeCreated;
    }

    public boolean method_12223() {
        return this.isEmpty;
    }
}

