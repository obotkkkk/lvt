/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2349
 *  net.minecraft.class_2350
 *  net.minecraft.class_2389
 *  net.minecraft.class_247
 *  net.minecraft.class_2544
 *  net.minecraft.class_259
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 *  net.minecraft.class_3481
 *  net.minecraft.class_3610
 *  net.minecraft.class_3612
 *  net.minecraft.class_4778
 */
package fi.dy.masa.litematica.schematic.conversion;

import fi.dy.masa.litematica.schematic.conversion.IBlockReaderWithData;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionFixers;
import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_2389;
import net.minecraft.class_247;
import net.minecraft.class_2544;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2769;
import net.minecraft.class_3481;
import net.minecraft.class_3610;
import net.minecraft.class_3612;
import net.minecraft.class_4778;

public class WallStateFixer
implements SchematicConversionFixers.IStateFixer {
    public static final WallStateFixer INSTANCE = new WallStateFixer();
    private static final class_265 SHAPE_PILLAR = class_2248.method_9541((double)7.0, (double)0.0, (double)7.0, (double)9.0, (double)16.0, (double)9.0);
    private static final class_265 SHAPE_NORTH = class_2248.method_9541((double)7.0, (double)0.0, (double)0.0, (double)9.0, (double)16.0, (double)9.0);
    private static final class_265 SHAPE_SOUTH = class_2248.method_9541((double)7.0, (double)0.0, (double)7.0, (double)9.0, (double)16.0, (double)16.0);
    private static final class_265 SHAPE_WEST = class_2248.method_9541((double)0.0, (double)0.0, (double)7.0, (double)9.0, (double)16.0, (double)9.0);
    private static final class_265 SHAPE_EAST = class_2248.method_9541((double)7.0, (double)0.0, (double)7.0, (double)16.0, (double)16.0, (double)9.0);

    @Override
    public class_2680 fixState(IBlockReaderWithData reader, class_2680 state, class_2338 pos) {
        IBlockReaderWithData world = reader;
        class_3610 fluidState = state.method_26227();
        class_2338 posNorth = pos.method_10095();
        class_2338 posEast = pos.method_10078();
        class_2338 posSouth = pos.method_10072();
        class_2338 posWest = pos.method_10067();
        class_2338 posUp = pos.method_10084();
        class_2680 stateNorth = world.method_8320(posNorth);
        class_2680 stateEast = world.method_8320(posEast);
        class_2680 stateSouth = world.method_8320(posSouth);
        class_2680 stateWest = world.method_8320(posWest);
        class_2680 stateUp = world.method_8320(posUp);
        boolean connectNorth = this.shouldConnectTo(stateNorth, stateNorth.method_26206((class_1922)world, posNorth, class_2350.field_11035), class_2350.field_11035);
        boolean connectEast = this.shouldConnectTo(stateEast, stateEast.method_26206((class_1922)world, posEast, class_2350.field_11039), class_2350.field_11039);
        boolean connectSouth = this.shouldConnectTo(stateSouth, stateSouth.method_26206((class_1922)world, posSouth, class_2350.field_11043), class_2350.field_11043);
        boolean connectWest = this.shouldConnectTo(stateWest, stateWest.method_26206((class_1922)world, posWest, class_2350.field_11034), class_2350.field_11034);
        class_2680 baseState = (class_2680)state.method_26204().method_9564().method_11657((class_2769)class_2544.field_22160, (Comparable)Boolean.valueOf(fluidState.method_15772() == class_3612.field_15910));
        return this.getWallStateWithConnections(world, baseState, posUp, stateUp, connectNorth, connectEast, connectSouth, connectWest);
    }

    private class_2680 getWallStateWithConnections(class_1922 worldView, class_2680 baseState, class_2338 pos, class_2680 stateUp, boolean canConnectNorth, boolean canConnectEast, boolean canConnectSouth, boolean canConnectWest) {
        class_265 shapeAbove = stateUp.method_26220(worldView, pos).method_20538(class_2350.field_11033);
        class_2680 stateWithSides = this.getWallSideConnections(baseState, canConnectNorth, canConnectEast, canConnectSouth, canConnectWest, shapeAbove);
        return (class_2680)stateWithSides.method_11657((class_2769)class_2544.field_11717, (Comparable)Boolean.valueOf(this.shouldConnectUp(stateWithSides, stateUp, shapeAbove)));
    }

    private class_2680 getWallSideConnections(class_2680 blockState, boolean canConnectNorth, boolean canConnectEast, boolean canConnectSouth, boolean canConnectWest, class_265 shapeAbove) {
        return (class_2680)((class_2680)((class_2680)((class_2680)blockState.method_11657((class_2769)class_2544.field_22157, (Comparable)this.getConnectionShape(canConnectNorth, shapeAbove, SHAPE_NORTH))).method_11657((class_2769)class_2544.field_22156, (Comparable)this.getConnectionShape(canConnectEast, shapeAbove, SHAPE_EAST))).method_11657((class_2769)class_2544.field_22158, (Comparable)this.getConnectionShape(canConnectSouth, shapeAbove, SHAPE_SOUTH))).method_11657((class_2769)class_2544.field_22159, (Comparable)this.getConnectionShape(canConnectWest, shapeAbove, SHAPE_WEST));
    }

    private boolean shouldConnectTo(class_2680 state, boolean faceFullSquare, class_2350 side) {
        class_2248 block = state.method_26204();
        return state.method_26164(class_3481.field_15504) || !class_2248.method_9581((class_2680)state) && faceFullSquare || block instanceof class_2389 || block instanceof class_2349 && class_2349.method_16703((class_2680)state, (class_2350)side);
    }

    private boolean shouldConnectUp(class_2680 blockState, class_2680 stateUp, class_265 shapeAbove) {
        boolean inTallLine;
        boolean isPillarOrWallEnd;
        boolean isUpConnectedWallAbove;
        boolean bl = isUpConnectedWallAbove = stateUp.method_26204() instanceof class_2544 && (Boolean)stateUp.method_11654((class_2769)class_2544.field_11717) != false;
        if (isUpConnectedWallAbove) {
            return true;
        }
        class_4778 shapeNorth = (class_4778)blockState.method_11654((class_2769)class_2544.field_22157);
        class_4778 shapeSouth = (class_4778)blockState.method_11654((class_2769)class_2544.field_22158);
        class_4778 shapeEast = (class_4778)blockState.method_11654((class_2769)class_2544.field_22156);
        class_4778 shapeWest = (class_4778)blockState.method_11654((class_2769)class_2544.field_22159);
        boolean unconnectedNorth = shapeNorth == class_4778.field_22178;
        boolean unconnectedSouth = shapeSouth == class_4778.field_22178;
        boolean unconnectedEast = shapeEast == class_4778.field_22178;
        boolean unconnectedWest = shapeWest == class_4778.field_22178;
        boolean bl2 = isPillarOrWallEnd = unconnectedNorth && unconnectedSouth && unconnectedWest && unconnectedEast || unconnectedNorth != unconnectedSouth || unconnectedWest != unconnectedEast;
        if (isPillarOrWallEnd) {
            return true;
        }
        boolean bl3 = inTallLine = shapeNorth == class_4778.field_22180 && shapeSouth == class_4778.field_22180 || shapeEast == class_4778.field_22180 && shapeWest == class_4778.field_22180;
        if (inTallLine) {
            return false;
        }
        return stateUp.method_26164(class_3481.field_22276) || this.shapesDoNotIntersect(shapeAbove, SHAPE_PILLAR);
    }

    private class_4778 getConnectionShape(boolean canConnect, class_265 shapeAbove, class_265 shapeSideClearance) {
        if (canConnect) {
            return this.shapesDoNotIntersect(shapeAbove, shapeSideClearance) ? class_4778.field_22180 : class_4778.field_22179;
        }
        return class_4778.field_22178;
    }

    private boolean shapesDoNotIntersect(class_265 voxelShape, class_265 voxelShape2) {
        return !class_259.method_1074((class_265)voxelShape2, (class_265)voxelShape, (class_247)class_247.field_16886);
    }
}

