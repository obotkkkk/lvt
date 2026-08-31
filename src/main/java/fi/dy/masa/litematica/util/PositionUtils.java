/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.PositionUtils$CoordinateType
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_2415
 *  net.minecraft.class_243
 *  net.minecraft.class_2470
 *  net.minecraft.class_2784
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_638
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.selection.SelectionManager;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.PositionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_2415;
import net.minecraft.class_243;
import net.minecraft.class_2470;
import net.minecraft.class_2784;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import org.apache.commons.lang3.tuple.Pair;

public class PositionUtils {
    public static final BlockPosComparator BLOCK_POS_COMPARATOR = new BlockPosComparator();
    public static final ChunkPosComparator CHUNK_POS_COMPARATOR = new ChunkPosComparator();
    public static final class_2350.class_2351[] AXES_ALL = new class_2350.class_2351[]{class_2350.class_2351.field_11048, class_2350.class_2351.field_11052, class_2350.class_2351.field_11051};
    public static final class_2350[] ADJACENT_SIDES_ZY = new class_2350[]{class_2350.field_11033, class_2350.field_11036, class_2350.field_11043, class_2350.field_11035};
    public static final class_2350[] ADJACENT_SIDES_XY = new class_2350[]{class_2350.field_11033, class_2350.field_11036, class_2350.field_11034, class_2350.field_11039};
    public static final class_2350[] ADJACENT_SIDES_XZ = new class_2350[]{class_2350.field_11043, class_2350.field_11035, class_2350.field_11034, class_2350.field_11039};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XN_ZN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(-1, 0, 0), new class_2382(0, 0, -1), new class_2382(-1, 0, -1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XP_ZN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(1, 0, 0), new class_2382(0, 0, -1), new class_2382(1, 0, -1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XN_ZP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(-1, 0, 0), new class_2382(0, 0, 1), new class_2382(-1, 0, 1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XP_ZP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(1, 0, 0), new class_2382(0, 0, 1), new class_2382(1, 0, 1)};
    private static final class_2382[][] EDGE_NEIGHBOR_OFFSETS_Y = new class_2382[][]{EDGE_NEIGHBOR_OFFSETS_XN_ZN, EDGE_NEIGHBOR_OFFSETS_XP_ZN, EDGE_NEIGHBOR_OFFSETS_XN_ZP, EDGE_NEIGHBOR_OFFSETS_XP_ZP};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XN_YN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(-1, 0, 0), new class_2382(0, -1, 0), new class_2382(-1, -1, 0)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XP_YN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(1, 0, 0), new class_2382(0, -1, 0), new class_2382(1, -1, 0)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XN_YP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(-1, 0, 0), new class_2382(0, 1, 0), new class_2382(-1, 1, 0)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_XP_YP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(1, 0, 0), new class_2382(0, 1, 0), new class_2382(1, 1, 0)};
    private static final class_2382[][] EDGE_NEIGHBOR_OFFSETS_Z = new class_2382[][]{EDGE_NEIGHBOR_OFFSETS_XN_YN, EDGE_NEIGHBOR_OFFSETS_XP_YN, EDGE_NEIGHBOR_OFFSETS_XN_YP, EDGE_NEIGHBOR_OFFSETS_XP_YP};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_YN_ZN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(0, -1, 0), new class_2382(0, 0, -1), new class_2382(0, -1, -1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_YP_ZN = new class_2382[]{new class_2382(0, 0, 0), new class_2382(0, 1, 0), new class_2382(0, 0, -1), new class_2382(0, 1, -1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_YN_ZP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(0, -1, 0), new class_2382(0, 0, 1), new class_2382(0, -1, 1)};
    private static final class_2382[] EDGE_NEIGHBOR_OFFSETS_YP_ZP = new class_2382[]{new class_2382(0, 0, 0), new class_2382(0, 1, 0), new class_2382(0, 0, 1), new class_2382(0, 1, 1)};
    private static final class_2382[][] EDGE_NEIGHBOR_OFFSETS_X = new class_2382[][]{EDGE_NEIGHBOR_OFFSETS_YN_ZN, EDGE_NEIGHBOR_OFFSETS_YP_ZN, EDGE_NEIGHBOR_OFFSETS_YN_ZP, EDGE_NEIGHBOR_OFFSETS_YP_ZP};

    public static class_2382[] getEdgeNeighborOffsets(class_2350.class_2351 axis, int cornerIndex) {
        switch (axis) {
            case field_11048: {
                return EDGE_NEIGHBOR_OFFSETS_X[cornerIndex];
            }
            case field_11052: {
                return EDGE_NEIGHBOR_OFFSETS_Y[cornerIndex];
            }
            case field_11051: {
                return EDGE_NEIGHBOR_OFFSETS_Z[cornerIndex];
            }
        }
        return null;
    }

    public static long getChunkPosLong(class_2338 blockPos) {
        return class_1923.method_8331((int)(blockPos.method_10263() >> 4), (int)(blockPos.method_10260() >> 4));
    }

    public static class_2338 getMinCorner(class_2338 pos1, class_2338 pos2) {
        return new class_2338(Math.min(pos1.method_10263(), pos2.method_10263()), Math.min(pos1.method_10264(), pos2.method_10264()), Math.min(pos1.method_10260(), pos2.method_10260()));
    }

    public static class_2338 getMaxCorner(class_2338 pos1, class_2338 pos2) {
        return new class_2338(Math.max(pos1.method_10263(), pos2.method_10263()), Math.max(pos1.method_10264(), pos2.method_10264()), Math.max(pos1.method_10260(), pos2.method_10260()));
    }

    public static boolean isPositionInsideArea(class_2338 pos, class_2338 posMin, class_2338 posMax) {
        return pos.method_10263() >= posMin.method_10263() && pos.method_10263() <= posMax.method_10263() && pos.method_10264() >= posMin.method_10264() && pos.method_10264() <= posMax.method_10264() && pos.method_10260() >= posMin.method_10260() && pos.method_10260() <= posMax.method_10260();
    }

    public static class_2338 getTransformedPlacementPosition(class_2338 posWithinSub, SchematicPlacement schematicPlacement, SubRegionPlacement placement) {
        class_2338 pos = posWithinSub;
        pos = PositionUtils.getTransformedBlockPos(pos, schematicPlacement.getMirror(), schematicPlacement.getRotation());
        pos = PositionUtils.getTransformedBlockPos(pos, placement.getMirror(), placement.getRotation());
        return pos;
    }

    public static boolean arePositionsWithinWorld(class_1937 world, class_2338 pos1, class_2338 pos2) {
        if (pos1.method_10264() >= world.method_31607() && pos1.method_10264() < world.method_31600() && pos2.method_10264() >= world.method_31607() && pos2.method_10264() < world.method_31600()) {
            class_2784 border = world.method_8621();
            return border.method_11952(pos1) && border.method_11952(pos2);
        }
        return false;
    }

    public static boolean isBoxWithinWorld(class_1937 world, Box box) {
        if (box.getPos1() != null && box.getPos2() != null) {
            return PositionUtils.arePositionsWithinWorld(world, box.getPos1(), box.getPos2());
        }
        return false;
    }

    public static boolean isPlacementWithinWorld(class_1937 world, SchematicPlacement schematicPlacement, boolean respectRenderRange) {
        LayerRange range = DataManager.getRenderLayerRange();
        class_2338.class_2339 posMutable1 = new class_2338.class_2339();
        class_2338.class_2339 posMutable2 = new class_2338.class_2339();
        for (Box box : schematicPlacement.getSubRegionBoxes(SubRegionPlacement.RequiredEnabled.PLACEMENT_ENABLED).values()) {
            if (respectRenderRange) {
                IntBoundingBox bb;
                if (!range.intersectsBox(box.getPos1(), box.getPos2()) || (bb = range.getClampedArea(box.getPos1(), box.getPos2())) == null) continue;
                posMutable1.method_10103(bb.minX, bb.minY, bb.minZ);
                posMutable2.method_10103(bb.maxX, bb.maxY, bb.maxZ);
                if (PositionUtils.arePositionsWithinWorld(world, (class_2338)posMutable1, (class_2338)posMutable2)) continue;
                return false;
            }
            if (PositionUtils.isBoxWithinWorld(world, box)) continue;
            return false;
        }
        return true;
    }

    public static class_2338 getAreaSizeFromRelativeEndPosition(class_2338 posEndRelative) {
        int x = posEndRelative.method_10263();
        int y = posEndRelative.method_10264();
        int z = posEndRelative.method_10260();
        x = x >= 0 ? x + 1 : x - 1;
        y = y >= 0 ? y + 1 : y - 1;
        z = z >= 0 ? z + 1 : z - 1;
        return new class_2338(x, y, z);
    }

    public static class_2338 getAreaSizeFromRelativeEndPositionAbs(class_2338 posEndRelative) {
        int x = posEndRelative.method_10263();
        int y = posEndRelative.method_10264();
        int z = posEndRelative.method_10260();
        x = x >= 0 ? x + 1 : x - 1;
        y = y >= 0 ? y + 1 : y - 1;
        z = z >= 0 ? z + 1 : z - 1;
        return new class_2338(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public static class_2338 getRelativeEndPositionFromAreaSize(class_2382 size) {
        int x = size.method_10263();
        int y = size.method_10264();
        int z = size.method_10260();
        x = x >= 0 ? x - 1 : x + 1;
        y = y >= 0 ? y - 1 : y + 1;
        z = z >= 0 ? z - 1 : z + 1;
        return new class_2338(x, y, z);
    }

    public static List<Box> getValidBoxes(AreaSelection area) {
        ArrayList<Box> boxes = new ArrayList<Box>();
        List<Box> originalBoxes = area.getAllSubRegionBoxes();
        for (Box box : originalBoxes) {
            if (!PositionUtils.isBoxValid(box)) continue;
            boxes.add(box);
        }
        return boxes;
    }

    public static boolean isBoxValid(Box box) {
        return box.getPos1() != null && box.getPos2() != null;
    }

    public static class_2338 getEnclosingAreaSize(AreaSelection area) {
        return PositionUtils.getEnclosingAreaSize(area.getAllSubRegionBoxes());
    }

    public static class_2338 getEnclosingAreaSize(Collection<Box> boxes) {
        Pair<class_2338, class_2338> pair = PositionUtils.getEnclosingAreaCorners(boxes);
        return ((class_2338)pair.getRight()).method_10059((class_2382)pair.getLeft()).method_10069(1, 1, 1);
    }

    @Nullable
    public static Pair<class_2338, class_2338> getEnclosingAreaCorners(Collection<Box> boxes) {
        if (boxes.isEmpty()) {
            return null;
        }
        class_2338.class_2339 posMin = new class_2338.class_2339(60000000, 60000000, 60000000);
        class_2338.class_2339 posMax = new class_2338.class_2339(-60000000, -60000000, -60000000);
        for (Box box : boxes) {
            PositionUtils.getMinMaxCoords(posMin, posMax, box.getPos1());
            PositionUtils.getMinMaxCoords(posMin, posMax, box.getPos2());
        }
        return Pair.of((Object)posMin.method_10062(), (Object)posMax.method_10062());
    }

    private static void getMinMaxCoords(class_2338.class_2339 posMin, class_2338.class_2339 posMax, @Nullable class_2338 posToCheck) {
        if (posToCheck != null) {
            posMin.method_10103(Math.min(posMin.method_10263(), posToCheck.method_10263()), Math.min(posMin.method_10264(), posToCheck.method_10264()), Math.min(posMin.method_10260(), posToCheck.method_10260()));
            posMax.method_10103(Math.max(posMax.method_10263(), posToCheck.method_10263()), Math.max(posMax.method_10264(), posToCheck.method_10264()), Math.max(posMax.method_10260(), posToCheck.method_10260()));
        }
    }

    @Nullable
    public static IntBoundingBox clampBoxToWorldHeightRange(IntBoundingBox box, class_1937 world) {
        int minY = world.method_31607();
        int maxY = world.method_31600();
        if (box.minY > maxY || box.maxY < minY) {
            return null;
        }
        if (box.minY < minY || box.maxY > maxY) {
            box = new IntBoundingBox(box.minX, Math.max(box.minY, minY), box.minZ, box.maxX, Math.min(box.maxY, maxY), box.maxZ);
        }
        return box;
    }

    public static int getTotalVolume(Collection<Box> boxes) {
        if (boxes.isEmpty()) {
            return 0;
        }
        int volume = 0;
        for (Box box : boxes) {
            if (!PositionUtils.isBoxValid(box)) continue;
            class_2338 min = PositionUtils.getMinCorner(box.getPos1(), box.getPos2());
            class_2338 max = PositionUtils.getMaxCorner(box.getPos1(), box.getPos2());
            volume += (max.method_10263() - min.method_10263() + 1) * (max.method_10264() - min.method_10264() + 1) * (max.method_10260() - min.method_10260() + 1);
        }
        return volume;
    }

    public static ImmutableMap<String, IntBoundingBox> getBoxesWithinChunk(int chunkX, int chunkZ, ImmutableMap<String, Box> subRegions) {
        ImmutableMap.Builder builder = new ImmutableMap.Builder();
        for (Map.Entry entry : subRegions.entrySet()) {
            Box box = (Box)entry.getValue();
            IntBoundingBox bb = box != null ? PositionUtils.getBoundsWithinChunkForBox(box, chunkX, chunkZ) : null;
            if (bb == null) continue;
            builder.put((Object)((String)entry.getKey()), (Object)bb);
        }
        return builder.build();
    }

    public static ImmutableList<IntBoundingBox> getBoxesWithinChunk(int chunkX, int chunkZ, Collection<Box> boxes) {
        ImmutableList.Builder builder = new ImmutableList.Builder();
        for (Box box : boxes) {
            IntBoundingBox bb = PositionUtils.getBoundsWithinChunkForBox(box, chunkX, chunkZ);
            if (bb == null) continue;
            builder.add((Object)bb);
        }
        return builder.build();
    }

    public static Set<class_1923> getTouchedChunks(ImmutableMap<String, Box> boxes) {
        return PositionUtils.getTouchedChunksForBoxes((Collection<Box>)boxes.values());
    }

    public static Set<class_1923> getTouchedChunksForBoxes(Collection<Box> boxes) {
        HashSet<class_1923> set = new HashSet<class_1923>();
        for (Box box : boxes) {
            int boxXMin = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263()) >> 4;
            int boxZMin = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260()) >> 4;
            int boxXMax = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263()) >> 4;
            int boxZMax = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260()) >> 4;
            for (int cz = boxZMin; cz <= boxZMax; ++cz) {
                for (int cx = boxXMin; cx <= boxXMax; ++cx) {
                    set.add(new class_1923(cx, cz));
                }
            }
        }
        return set;
    }

    @Nullable
    public static IntBoundingBox getBoundsWithinChunkForBox(Box box, int chunkX, int chunkZ) {
        boolean notOverlapping;
        int chunkXMin = chunkX << 4;
        int chunkZMin = chunkZ << 4;
        int chunkXMax = chunkXMin + 15;
        int chunkZMax = chunkZMin + 15;
        int boxXMin = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
        int boxZMin = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
        int boxXMax = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
        int boxZMax = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
        boolean bl = notOverlapping = boxXMin > chunkXMax || boxZMin > chunkZMax || boxXMax < chunkXMin || boxZMax < chunkZMin;
        if (!notOverlapping) {
            int xMin = Math.max(chunkXMin, boxXMin);
            int yMin = Math.min(box.getPos1().method_10264(), box.getPos2().method_10264());
            int zMin = Math.max(chunkZMin, boxZMin);
            int xMax = Math.min(chunkXMax, boxXMax);
            int yMax = Math.max(box.getPos1().method_10264(), box.getPos2().method_10264());
            int zMax = Math.min(chunkZMax, boxZMax);
            return new IntBoundingBox(xMin, yMin, zMin, xMax, yMax, zMax);
        }
        return null;
    }

    public static void getPerChunkBoxes(Collection<Box> boxes, BiConsumer<class_1923, IntBoundingBox> consumer) {
        for (Box box : boxes) {
            int boxMinX = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMinY = Math.min(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMinZ = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxMaxX = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMaxY = Math.max(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMaxZ = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxMinChunkX = boxMinX >> 4;
            int boxMinChunkZ = boxMinZ >> 4;
            int boxMaxChunkX = boxMaxX >> 4;
            int boxMaxChunkZ = boxMaxZ >> 4;
            for (int cz = boxMinChunkZ; cz <= boxMaxChunkZ; ++cz) {
                for (int cx = boxMinChunkX; cx <= boxMaxChunkX; ++cx) {
                    int chunkMinX = cx << 4;
                    int chunkMinZ = cz << 4;
                    int chunkMaxX = chunkMinX + 15;
                    int chunkMaxZ = chunkMinZ + 15;
                    int minX = Math.max(chunkMinX, boxMinX);
                    int minZ = Math.max(chunkMinZ, boxMinZ);
                    int maxX = Math.min(chunkMaxX, boxMaxX);
                    int maxZ = Math.min(chunkMaxZ, boxMaxZ);
                    consumer.accept(new class_1923(cx, cz), new IntBoundingBox(minX, boxMinY, minZ, maxX, boxMaxY, maxZ));
                }
            }
        }
    }

    public static void getLayerRangeClampedPerChunkBoxes(Collection<Box> boxes, LayerRange range, BiConsumer<class_1923, IntBoundingBox> consumer) {
        block5: for (Box box : boxes) {
            int rangeMin = range.getLayerMin();
            int rangeMax = range.getLayerMax();
            int boxMinX = Math.min(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMinY = Math.min(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMinZ = Math.min(box.getPos1().method_10260(), box.getPos2().method_10260());
            int boxMaxX = Math.max(box.getPos1().method_10263(), box.getPos2().method_10263());
            int boxMaxY = Math.max(box.getPos1().method_10264(), box.getPos2().method_10264());
            int boxMaxZ = Math.max(box.getPos1().method_10260(), box.getPos2().method_10260());
            switch (range.getAxis()) {
                case field_11048: {
                    if (rangeMax < boxMinX || rangeMin > boxMaxX) continue block5;
                    boxMinX = Math.max(boxMinX, rangeMin);
                    boxMaxX = Math.min(boxMaxX, rangeMax);
                    break;
                }
                case field_11052: {
                    if (rangeMax < boxMinY || rangeMin > boxMaxY) continue block5;
                    boxMinY = Math.max(boxMinY, rangeMin);
                    boxMaxY = Math.min(boxMaxY, rangeMax);
                    break;
                }
                case field_11051: {
                    if (rangeMax < boxMinZ || rangeMin > boxMaxZ) continue block5;
                    boxMinZ = Math.max(boxMinZ, rangeMin);
                    boxMaxZ = Math.min(boxMaxZ, rangeMax);
                }
            }
            int boxMinChunkX = boxMinX >> 4;
            int boxMinChunkZ = boxMinZ >> 4;
            int boxMaxChunkX = boxMaxX >> 4;
            int boxMaxChunkZ = boxMaxZ >> 4;
            for (int cz = boxMinChunkZ; cz <= boxMaxChunkZ; ++cz) {
                for (int cx = boxMinChunkX; cx <= boxMaxChunkX; ++cx) {
                    int chunkMinX = cx << 4;
                    int chunkMinZ = cz << 4;
                    int chunkMaxX = chunkMinX + 15;
                    int chunkMaxZ = chunkMinZ + 15;
                    int minX = Math.max(chunkMinX, boxMinX);
                    int minZ = Math.max(chunkMinZ, boxMinZ);
                    int maxX = Math.min(chunkMaxX, boxMaxX);
                    int maxZ = Math.min(chunkMaxZ, boxMaxZ);
                    consumer.accept(new class_1923(cx, cz), new IntBoundingBox(minX, boxMinY, minZ, maxX, boxMaxY, maxZ));
                }
            }
        }
    }

    public static class_238 createEnclosingAABB(class_2338 pos1, class_2338 pos2) {
        int minX = Math.min(pos1.method_10263(), pos2.method_10263());
        int minY = Math.min(pos1.method_10264(), pos2.method_10264());
        int minZ = Math.min(pos1.method_10260(), pos2.method_10260());
        int maxX = Math.max(pos1.method_10263(), pos2.method_10263()) + 1;
        int maxY = Math.max(pos1.method_10264(), pos2.method_10264()) + 1;
        int maxZ = Math.max(pos1.method_10260(), pos2.method_10260()) + 1;
        return PositionUtils.createAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static class_238 createAABBFrom(IntBoundingBox bb) {
        return PositionUtils.createAABB(bb.minX, bb.minY, bb.minZ, bb.maxX + 1, bb.maxY + 1, bb.maxZ + 1);
    }

    public static class_238 createAABBForPosition(class_2338 pos) {
        return PositionUtils.createAABBForPosition(pos.method_10263(), pos.method_10264(), pos.method_10260());
    }

    public static class_238 createAABBForPosition(int x, int y, int z) {
        return PositionUtils.createAABB(x, y, z, x + 1, y + 1, z + 1);
    }

    public static class_238 createAABB(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new class_238((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);
    }

    public static class_2338 getModifiedPosition(class_2338 pos, int value, PositionUtils.CoordinateType type) {
        switch (type) {
            case X: {
                pos = new class_2338(value, pos.method_10264(), pos.method_10260());
                break;
            }
            case Y: {
                pos = new class_2338(pos.method_10263(), value, pos.method_10260());
                break;
            }
            case Z: {
                pos = new class_2338(pos.method_10263(), pos.method_10264(), value);
            }
        }
        return pos;
    }

    public static int getCoordinate(class_2338 pos, PositionUtils.CoordinateType type) {
        switch (type) {
            case X: {
                return pos.method_10263();
            }
            case Y: {
                return pos.method_10264();
            }
            case Z: {
                return pos.method_10260();
            }
        }
        return 0;
    }

    public static Box growOrShrinkBox(Box box, int amount) {
        class_2338 pos1 = box.getPos1();
        class_2338 pos2 = box.getPos2();
        if (pos1 == null || pos2 == null) {
            if (pos1 == null && pos2 == null) {
                return box;
            }
            if (pos2 == null) {
                pos2 = pos1;
            } else {
                pos1 = pos2;
            }
        }
        Pair<Integer, Integer> x = PositionUtils.growCoordinatePair(pos1.method_10263(), pos2.method_10263(), amount);
        Pair<Integer, Integer> y = PositionUtils.growCoordinatePair(pos1.method_10264(), pos2.method_10264(), amount);
        Pair<Integer, Integer> z = PositionUtils.growCoordinatePair(pos1.method_10260(), pos2.method_10260(), amount);
        Box boxNew = box.copy();
        boxNew.setPos1(new class_2338(((Integer)x.getLeft()).intValue(), ((Integer)y.getLeft()).intValue(), ((Integer)z.getLeft()).intValue()));
        boxNew.setPos2(new class_2338(((Integer)x.getRight()).intValue(), ((Integer)y.getRight()).intValue(), ((Integer)z.getRight()).intValue()));
        return boxNew;
    }

    private static Pair<Integer, Integer> growCoordinatePair(int v1, int v2, int amount) {
        if (v2 >= v1) {
            if (v2 + amount >= v1) {
                v2 += amount;
            }
            if (v1 - amount <= v2) {
                v1 -= amount;
            }
        } else if (v1 > v2) {
            if (v1 + amount >= v2) {
                v1 += amount;
            }
            if (v2 - amount <= v1) {
                v2 -= amount;
            }
        }
        return Pair.of((Object)v1, (Object)v2);
    }

    public static void growOrShrinkCurrentSelection(boolean grow) {
        SelectionManager sm = DataManager.getSelectionManager();
        AreaSelection area = sm.getCurrentSelection();
        class_638 world = class_310.method_1551().field_1687;
        if (area == null || world == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.no_area_selected", (Object[])new Object[0]);
            return;
        }
        Box box = area.getSelectedSubRegionBox();
        if (box == null || box.getPos1() == null && box.getPos2() == null) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.error.area_selection.grow.no_sub_region_selected", (Object[])new Object[0]);
            return;
        }
        if (box != null && (box.getPos1() != null || box.getPos2() != null)) {
            int amount = 1;
            Box boxNew = box.copy();
            for (int i = 0; i < 256; ++i) {
                if (grow) {
                    boxNew = PositionUtils.growOrShrinkBox(boxNew, amount);
                }
                class_2338 pos1 = boxNew.getPos1();
                class_2338 pos2 = boxNew.getPos2();
                int xMin = Math.min(pos1.method_10263(), pos2.method_10263());
                int yMin = Math.min(pos1.method_10264(), pos2.method_10264());
                int zMin = Math.min(pos1.method_10260(), pos2.method_10260());
                int xMax = Math.max(pos1.method_10263(), pos2.method_10263());
                int yMax = Math.max(pos1.method_10264(), pos2.method_10264());
                int zMax = Math.max(pos1.method_10260(), pos2.method_10260());
                int emptySides = 0;
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11048, new class_2338(xMin, yMin, zMin), new class_2338(xMin, yMax, zMax))) {
                    xMin += amount;
                    ++emptySides;
                }
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11048, new class_2338(xMax, yMin, zMin), new class_2338(xMax, yMax, zMax))) {
                    xMax -= amount;
                    ++emptySides;
                }
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11052, new class_2338(xMin, yMin, zMin), new class_2338(xMax, yMin, zMax))) {
                    yMin += amount;
                    ++emptySides;
                }
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11052, new class_2338(xMin, yMax, zMin), new class_2338(xMax, yMax, zMax))) {
                    yMax -= amount;
                    ++emptySides;
                }
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11051, new class_2338(xMin, yMin, zMin), new class_2338(xMax, yMax, zMin))) {
                    zMin += amount;
                    ++emptySides;
                }
                if (WorldUtils.isSliceEmpty((class_1937)world, class_2350.class_2351.field_11051, new class_2338(xMin, yMin, zMax), new class_2338(xMax, yMax, zMax))) {
                    zMax -= amount;
                    ++emptySides;
                }
                boxNew.setPos1(new class_2338(xMin, yMin, zMin));
                boxNew.setPos2(new class_2338(xMax, yMax, zMax));
                if (grow && emptySides >= 6 || !grow && emptySides == 0) break;
            }
            area.setSelectedSubRegionCornerPos(boxNew.getPos1(), Corner.CORNER_1);
            area.setSelectedSubRegionCornerPos(boxNew.getPos2(), Corner.CORNER_2);
        }
    }

    public static class_2338 getTransformedBlockPos(class_2338 pos, class_2415 mirror, class_2470 rotation) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        boolean isMirrored = true;
        switch (mirror) {
            case field_11300: {
                z = -z;
                break;
            }
            case field_11301: {
                x = -x;
                break;
            }
            default: {
                isMirrored = false;
            }
        }
        switch (rotation) {
            case field_11463: {
                return new class_2338(-z, y, x);
            }
            case field_11465: {
                return new class_2338(z, y, -x);
            }
            case field_11464: {
                return new class_2338(-x, y, -z);
            }
        }
        return isMirrored ? new class_2338(x, y, z) : pos;
    }

    public static class_2338 getReverseTransformedBlockPos(class_2338 pos, class_2415 mirror, class_2470 rotation) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        boolean isRotated = true;
        int tmp = x;
        switch (rotation) {
            case field_11463: {
                x = z;
                z = -tmp;
                break;
            }
            case field_11465: {
                x = -z;
                z = tmp;
                break;
            }
            case field_11464: {
                x = -x;
                z = -z;
                break;
            }
            default: {
                isRotated = false;
            }
        }
        switch (mirror) {
            case field_11300: {
                z = -z;
                break;
            }
            case field_11301: {
                x = -x;
                break;
            }
            default: {
                if (isRotated) break;
                return pos;
            }
        }
        return new class_2338(x, y, z);
    }

    public static class_2338 getOriginalPositionFromTransformed(class_2338 pos, class_2415 mirror, class_2470 rotation) {
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        boolean noRotation = false;
        switch (rotation) {
            case field_11463: {
                int tmp = x;
                x = -z;
                z = tmp;
            }
            case field_11465: {
                int tmp = x;
                x = z;
                z = -tmp;
            }
            case field_11464: {
                x = -x;
                z = -z;
            }
        }
        noRotation = true;
        switch (mirror) {
            case field_11300: {
                z = -z;
                break;
            }
            case field_11301: {
                x = -x;
                break;
            }
            default: {
                if (!noRotation) break;
                return pos;
            }
        }
        return new class_2338(x, y, z);
    }

    public static class_243 getTransformedPosition(class_243 originalPos, class_2415 mirror, class_2470 rotation) {
        double x = originalPos.field_1352;
        double y = originalPos.field_1351;
        double z = originalPos.field_1350;
        boolean transformed = true;
        switch (mirror) {
            case field_11300: {
                z = 1.0 - z;
                break;
            }
            case field_11301: {
                x = 1.0 - x;
                break;
            }
            default: {
                transformed = false;
            }
        }
        switch (rotation) {
            case field_11465: {
                return new class_243(z, y, 1.0 - x);
            }
            case field_11463: {
                return new class_243(1.0 - z, y, x);
            }
            case field_11464: {
                return new class_243(1.0 - x, y, 1.0 - z);
            }
        }
        return transformed ? new class_243(x, y, z) : originalPos;
    }

    public static class_2470 getReverseRotation(class_2470 rotationIn) {
        switch (rotationIn) {
            case field_11465: {
                return class_2470.field_11463;
            }
            case field_11463: {
                return class_2470.field_11465;
            }
            case field_11464: {
                return class_2470.field_11464;
            }
        }
        return rotationIn;
    }

    public static class_2338 getModifiedPartiallyLockedPosition(class_2338 posOriginal, class_2338 posNew, int lockMask) {
        if (lockMask != 0) {
            int x = posNew.method_10263();
            int y = posNew.method_10264();
            int z = posNew.method_10260();
            if ((lockMask & 1 << PositionUtils.CoordinateType.X.ordinal()) != 0) {
                x = posOriginal.method_10263();
            }
            if ((lockMask & 1 << PositionUtils.CoordinateType.Y.ordinal()) != 0) {
                y = posOriginal.method_10264();
            }
            if ((lockMask & 1 << PositionUtils.CoordinateType.Z.ordinal()) != 0) {
                z = posOriginal.method_10260();
            }
            posNew = new class_2338(x, y, z);
        }
        return posNew;
    }

    public static class_2350 getFacingFromPositions(class_2338 pos1, class_2338 pos2) {
        if (pos1 == null || pos2 == null) {
            return null;
        }
        return PositionUtils.getFacingFromPositions(pos1.method_10263(), pos1.method_10260(), pos2.method_10263(), pos2.method_10260());
    }

    private static class_2350 getFacingFromPositions(int x1, int z1, int x2, int z2) {
        if (x2 == x1) {
            return z2 > z1 ? class_2350.field_11035 : class_2350.field_11043;
        }
        if (z2 == z1) {
            return x2 > x1 ? class_2350.field_11034 : class_2350.field_11039;
        }
        if (x2 > x1) {
            return z2 > z1 ? class_2350.field_11034 : class_2350.field_11043;
        }
        return z2 > z1 ? class_2350.field_11035 : class_2350.field_11039;
    }

    public static class_2470 cycleRotation(class_2470 rotation, boolean reverse) {
        int ordinal = rotation.ordinal();
        ordinal = reverse ? (ordinal == 0 ? class_2470.values().length - 1 : ordinal - 1) : (ordinal >= class_2470.values().length - 1 ? 0 : ordinal + 1);
        return class_2470.values()[ordinal];
    }

    public static class_2415 cycleMirror(class_2415 mirror, boolean reverse) {
        int ordinal = mirror.ordinal();
        ordinal = reverse ? (ordinal == 0 ? class_2415.values().length - 1 : ordinal - 1) : (ordinal >= class_2415.values().length - 1 ? 0 : ordinal + 1);
        return class_2415.values()[ordinal];
    }

    public static String getRotationNameShort(class_2470 rotation) {
        switch (rotation) {
            case field_11463: {
                return "CW_90";
            }
            case field_11464: {
                return "CW_180";
            }
            case field_11465: {
                return "CCW_90";
            }
        }
        return "NONE";
    }

    public static String getMirrorName(class_2415 mirror) {
        switch (mirror) {
            case field_11301: {
                return "FRONT_BACK";
            }
            case field_11300: {
                return "LEFT_RIGHT";
            }
        }
        return "NONE";
    }

    public static float getRotatedYaw(float yaw, class_2470 rotation) {
        yaw = class_3532.method_15393((float)yaw);
        switch (rotation) {
            case field_11464: {
                yaw += 180.0f;
                break;
            }
            case field_11465: {
                yaw += 270.0f;
                break;
            }
            case field_11463: {
                yaw += 90.0f;
                break;
            }
        }
        return yaw;
    }

    public static float getMirroredYaw(float yaw, class_2415 mirror) {
        yaw = class_3532.method_15393((float)yaw);
        switch (mirror) {
            case field_11300: {
                yaw = 180.0f - yaw;
                break;
            }
            case field_11301: {
                yaw = -yaw;
                break;
            }
        }
        return yaw;
    }

    @Nullable
    public static IntBoundingBox getClampedBox(IntBoundingBox box, LayerRange range) {
        return PositionUtils.getClampedArea(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, range);
    }

    @Nullable
    public static IntBoundingBox getClampedArea(class_2338 posMin, class_2338 posMax, LayerRange range) {
        int minX = Math.min(posMin.method_10263(), posMax.method_10263());
        int minY = Math.min(posMin.method_10264(), posMax.method_10264());
        int minZ = Math.min(posMin.method_10260(), posMax.method_10260());
        int maxX = Math.max(posMin.method_10263(), posMax.method_10263());
        int maxY = Math.max(posMin.method_10264(), posMax.method_10264());
        int maxZ = Math.max(posMin.method_10260(), posMax.method_10260());
        return PositionUtils.getClampedArea(minX, minY, minZ, maxX, maxY, maxZ, range);
    }

    @Nullable
    public static IntBoundingBox getClampedArea(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, LayerRange range) {
        if (!range.intersectsBox(minX, minY, minZ, maxX, maxY, maxZ)) {
            return null;
        }
        switch (range.getAxis()) {
            case field_11048: {
                int clampedMinX = Math.max(minX, range.getLayerMin());
                int clampedMaxX = Math.min(maxX, range.getLayerMax());
                return IntBoundingBox.createProper((int)clampedMinX, (int)minY, (int)minZ, (int)clampedMaxX, (int)maxY, (int)maxZ);
            }
            case field_11052: {
                int clampedMinY = Math.max(minY, range.getLayerMin());
                int clampedMaxY = Math.min(maxY, range.getLayerMax());
                return IntBoundingBox.createProper((int)minX, (int)clampedMinY, (int)minZ, (int)maxX, (int)clampedMaxY, (int)maxZ);
            }
            case field_11051: {
                int clampedMinZ = Math.max(minZ, range.getLayerMin());
                int clampedMaxZ = Math.min(maxZ, range.getLayerMax());
                return IntBoundingBox.createProper((int)minX, (int)minY, (int)clampedMinZ, (int)maxX, (int)maxY, (int)clampedMaxZ);
            }
        }
        return null;
    }

    public static enum Corner {
        NONE,
        CORNER_1,
        CORNER_2;

    }

    public static class BlockPosComparator
    implements Comparator<class_2338> {
        private class_2338 posReference = class_2338.field_10980;
        private boolean closestFirst;

        public void setClosestFirst(boolean closestFirst) {
            this.closestFirst = closestFirst;
        }

        public void setReferencePosition(class_2338 pos) {
            this.posReference = pos;
        }

        @Override
        public int compare(class_2338 pos1, class_2338 pos2) {
            double dist2;
            double dist1 = pos1.method_10262((class_2382)this.posReference);
            if (dist1 == (dist2 = pos2.method_10262((class_2382)this.posReference))) {
                return 0;
            }
            return dist1 < dist2 == this.closestFirst ? -1 : 1;
        }
    }

    public static class ChunkPosComparator
    implements Comparator<class_1923> {
        private class_2338 posReference = class_2338.field_10980;
        private boolean closestFirst;

        public ChunkPosComparator setClosestFirst(boolean closestFirst) {
            this.closestFirst = closestFirst;
            return this;
        }

        public ChunkPosComparator setReferencePosition(class_2338 pos) {
            this.posReference = pos;
            return this;
        }

        @Override
        public int compare(class_1923 pos1, class_1923 pos2) {
            double dist2;
            double dist1 = this.distanceSq(pos1);
            if (dist1 == (dist2 = this.distanceSq(pos2))) {
                return 0;
            }
            return dist1 < dist2 == this.closestFirst ? -1 : 1;
        }

        private double distanceSq(class_1923 pos) {
            double dx = (double)(pos.field_9181 << 4) - (double)this.posReference.method_10263();
            double dz = (double)(pos.field_9180 << 4) - (double)this.posReference.method_10260();
            return dx * dx + dz * dz;
        }
    }

    public static class ChunkPosDistanceComparator
    implements Comparator<class_1923> {
        private final class_1923 referencePosition;

        public ChunkPosDistanceComparator(class_1923 referencePosition) {
            this.referencePosition = referencePosition;
        }

        @Override
        public int compare(class_1923 pos1, class_1923 pos2) {
            int refX = this.referencePosition.field_9181;
            int refZ = this.referencePosition.field_9180;
            double dist1 = (refX - pos1.field_9181) * (refX - pos1.field_9181) + (refZ - pos1.field_9180) * (refZ - pos1.field_9180);
            double dist2 = (refX - pos2.field_9181) * (refX - pos2.field_9181) + (refZ - pos2.field_9180) * (refZ - pos2.field_9180);
            return Double.compare(dist1, dist2);
        }
    }
}

