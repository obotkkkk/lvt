/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2215
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2283
 *  net.minecraft.class_2320
 *  net.minecraft.class_2323
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2349
 *  net.minecraft.class_2354
 *  net.minecraft.class_2358
 *  net.minecraft.class_2362
 *  net.minecraft.class_2372
 *  net.minecraft.class_2382
 *  net.minecraft.class_2389
 *  net.minecraft.class_2418
 *  net.minecraft.class_2428
 *  net.minecraft.class_2457
 *  net.minecraft.class_2462
 *  net.minecraft.class_2484
 *  net.minecraft.class_2487
 *  net.minecraft.class_2493
 *  net.minecraft.class_2504
 *  net.minecraft.class_2508
 *  net.minecraft.class_2510
 *  net.minecraft.class_2513
 *  net.minecraft.class_2521
 *  net.minecraft.class_2538
 *  net.minecraft.class_2541
 *  net.minecraft.class_2544
 *  net.minecraft.class_2546
 *  net.minecraft.class_2549
 *  net.minecraft.class_2551
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_3610
 */
package fi.dy.masa.litematica.schematic.conversion;

import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.litematica.schematic.conversion.IBlockReaderWithData;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionFixers;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import fi.dy.masa.litematica.schematic.conversion.WallStateFixer;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.class_2215;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2283;
import net.minecraft.class_2320;
import net.minecraft.class_2323;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2354;
import net.minecraft.class_2358;
import net.minecraft.class_2362;
import net.minecraft.class_2372;
import net.minecraft.class_2382;
import net.minecraft.class_2389;
import net.minecraft.class_2418;
import net.minecraft.class_2428;
import net.minecraft.class_2457;
import net.minecraft.class_2462;
import net.minecraft.class_2484;
import net.minecraft.class_2487;
import net.minecraft.class_2493;
import net.minecraft.class_2504;
import net.minecraft.class_2508;
import net.minecraft.class_2510;
import net.minecraft.class_2513;
import net.minecraft.class_2521;
import net.minecraft.class_2538;
import net.minecraft.class_2541;
import net.minecraft.class_2544;
import net.minecraft.class_2546;
import net.minecraft.class_2549;
import net.minecraft.class_2551;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_3610;

public class SchematicConverter {
    private final IdentityHashMap<Class<? extends class_2248>, SchematicConversionFixers.IStateFixer> fixersPerBlock = new IdentityHashMap();
    private IdentityHashMap<class_2680, SchematicConversionFixers.IStateFixer> postProcessingStateFixers = new IdentityHashMap();

    private SchematicConverter() {
    }

    public static SchematicConverter createForSchematica() {
        SchematicConverter converter = new SchematicConverter();
        converter.addPostUpdateBlocksSchematica();
        return converter;
    }

    public static SchematicConverter createForLitematica() {
        SchematicConverter converter = new SchematicConverter();
        converter.addPostUpdateBlocksLitematica();
        return converter;
    }

    public boolean getConvertedStatesForBlock(int schematicBlockId, String blockName, class_2680[] paletteOut) {
        int shiftedOldVanillaId = SchematicConversionMaps.getOldNameToShiftedBlockId(blockName);
        int successCount = 0;
        if (shiftedOldVanillaId >= 0) {
            for (int meta = 0; meta < 16; ++meta) {
                class_2680 state = SchematicConversionMaps.get_1_13_2_StateForIdMeta(shiftedOldVanillaId & 0xFFF0 | meta);
                if (state == null) continue;
                paletteOut[schematicBlockId << 4 | meta] = state;
                ++successCount;
            }
        } else {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)("Failed to convert block with old name '" + blockName + "'"), (Object[])new Object[0]);
        }
        return successCount > 0;
    }

    public boolean getVanillaBlockPalette(class_2680[] paletteOut) {
        for (int idMeta = 0; idMeta < paletteOut.length; ++idMeta) {
            class_2680 state = SchematicConversionMaps.get_1_13_2_StateForIdMeta(idMeta);
            if (state == null) continue;
            paletteOut[idMeta] = state;
        }
        return true;
    }

    public class_2680[] getBlockStatePaletteForBlockPalette(String[] blockPalette) {
        Object[] palette = new class_2680[blockPalette.length * 16];
        Arrays.fill(palette, class_2246.field_10124.method_9564());
        for (int schematicBlockId = 0; schematicBlockId < blockPalette.length; ++schematicBlockId) {
            String blockName = blockPalette[schematicBlockId];
            this.getConvertedStatesForBlock(schematicBlockId, blockName, (class_2680[])palette);
        }
        return palette;
    }

    public boolean createPostProcessStateFilter(class_2680[] palette) {
        return this.createPostProcessStateFilter(Arrays.asList(palette));
    }

    public boolean createPostProcessStateFilter(Collection<class_2680> palette) {
        boolean needsPostProcess = false;
        this.postProcessingStateFixers.clear();
        for (class_2680 state : palette) {
            if (!this.needsPostProcess(state)) continue;
            this.postProcessingStateFixers.put(state, this.getFixerFor(state));
            needsPostProcess = true;
        }
        return needsPostProcess;
    }

    public IdentityHashMap<class_2680, SchematicConversionFixers.IStateFixer> getPostProcessStateFilter() {
        return this.postProcessingStateFixers;
    }

    private boolean needsPostProcess(class_2680 state) {
        return !state.method_26215() && this.fixersPerBlock.containsKey(state.method_26204().getClass());
    }

    @Nullable
    private SchematicConversionFixers.IStateFixer getFixerFor(class_2680 state) {
        return this.fixersPerBlock.get(state.method_26204().getClass());
    }

    public class_2487 fixTileEntityNBT(class_2487 tag, class_2680 state) {
        return tag;
    }

    public static void postProcessBlocks(LitematicaBlockStateContainer container, @Nullable Map<class_2338, class_2487> tiles, IdentityHashMap<class_2680, SchematicConversionFixers.IStateFixer> postProcessingFilter) {
        int sizeX = container.getSize().method_10263();
        int sizeY = container.getSize().method_10264();
        int sizeZ = container.getSize().method_10260();
        BlockReaderLitematicaContainer reader = new BlockReaderLitematicaContainer(container, tiles);
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        for (int y = 0; y < sizeY; ++y) {
            for (int z = 0; z < sizeZ; ++z) {
                for (int x = 0; x < sizeX; ++x) {
                    class_2680 state = container.get(x, y, z);
                    SchematicConversionFixers.IStateFixer fixer = postProcessingFilter.get(state);
                    if (fixer == null) continue;
                    posMutable.method_10103(x, y, z);
                    class_2680 stateFixed = fixer.fixState(reader, state, (class_2338)posMutable);
                    if (stateFixed == state) continue;
                    container.set(x, y, z, stateFixed);
                }
            }
        }
    }

    private void addPostUpdateBlocksLitematica() {
        this.fixersPerBlock.put(class_2457.class, SchematicConversionFixers.FIXER_REDSTONE_WIRE);
        this.fixersPerBlock.put(class_2544.class, WallStateFixer.INSTANCE);
        this.fixersPerBlock.put(class_2215.class, SchematicConversionFixers.FIXER_BANNER);
        this.fixersPerBlock.put(class_2546.class, SchematicConversionFixers.FIXER_BANNER_WALL);
        this.fixersPerBlock.put(class_2244.class, SchematicConversionFixers.FIXER_BED);
        this.fixersPerBlock.put(class_2362.class, SchematicConversionFixers.FIXER_FLOWER_POT);
        this.fixersPerBlock.put(class_2428.class, SchematicConversionFixers.FIXER_NOTE_BLOCK);
        this.fixersPerBlock.put(class_2508.class, SchematicConversionFixers.FIXER_SIGN);
        this.fixersPerBlock.put(class_2484.class, SchematicConversionFixers.FIXER_SKULL);
        this.fixersPerBlock.put(class_2551.class, SchematicConversionFixers.FIXER_SIGN);
        this.fixersPerBlock.put(class_2549.class, SchematicConversionFixers.FIXER_SKULL_WALL);
    }

    private void addPostUpdateBlocksSchematica() {
        this.fixersPerBlock.put(class_2283.class, SchematicConversionFixers.FIXER_CHRORUS_PLANT);
        this.fixersPerBlock.put(class_2323.class, SchematicConversionFixers.FIXER_DOOR);
        this.fixersPerBlock.put(class_2354.class, SchematicConversionFixers.FIXER_FENCE);
        this.fixersPerBlock.put(class_2349.class, SchematicConversionFixers.FIXER_FENCE_GATE);
        this.fixersPerBlock.put(class_2358.class, SchematicConversionFixers.FIXER_FIRE);
        this.fixersPerBlock.put(class_2372.class, SchematicConversionFixers.FIXER_DIRT_SNOWY);
        this.fixersPerBlock.put(class_2418.class, SchematicConversionFixers.FIXER_DIRT_SNOWY);
        this.fixersPerBlock.put(class_2389.class, SchematicConversionFixers.FIXER_PANE);
        this.fixersPerBlock.put(class_2462.class, SchematicConversionFixers.FIXER_REDSTONE_REPEATER);
        this.fixersPerBlock.put(class_2457.class, SchematicConversionFixers.FIXER_REDSTONE_WIRE);
        this.fixersPerBlock.put(class_2493.class, SchematicConversionFixers.FIXER_DIRT_SNOWY);
        this.fixersPerBlock.put(class_2513.class, SchematicConversionFixers.FIXER_STEM);
        this.fixersPerBlock.put(class_2504.class, SchematicConversionFixers.FIXER_PANE);
        this.fixersPerBlock.put(class_2510.class, SchematicConversionFixers.FIXER_STAIRS);
        this.fixersPerBlock.put(class_2521.class, SchematicConversionFixers.FIXER_DOUBLE_PLANT);
        this.fixersPerBlock.put(class_2320.class, SchematicConversionFixers.FIXER_DOUBLE_PLANT);
        this.fixersPerBlock.put(class_2538.class, SchematicConversionFixers.FIXER_TRIPWIRE);
        this.fixersPerBlock.put(class_2541.class, SchematicConversionFixers.FIXER_VINE);
        this.fixersPerBlock.put(class_2544.class, WallStateFixer.INSTANCE);
        this.fixersPerBlock.put(class_2215.class, SchematicConversionFixers.FIXER_BANNER);
        this.fixersPerBlock.put(class_2546.class, SchematicConversionFixers.FIXER_BANNER_WALL);
        this.fixersPerBlock.put(class_2244.class, SchematicConversionFixers.FIXER_BED);
        this.fixersPerBlock.put(class_2362.class, SchematicConversionFixers.FIXER_FLOWER_POT);
        this.fixersPerBlock.put(class_2428.class, SchematicConversionFixers.FIXER_NOTE_BLOCK);
        this.fixersPerBlock.put(class_2508.class, SchematicConversionFixers.FIXER_SIGN);
        this.fixersPerBlock.put(class_2484.class, SchematicConversionFixers.FIXER_SKULL);
        this.fixersPerBlock.put(class_2551.class, SchematicConversionFixers.FIXER_SIGN);
        this.fixersPerBlock.put(class_2549.class, SchematicConversionFixers.FIXER_SKULL_WALL);
    }

    public static class BlockReaderLitematicaContainer
    implements IBlockReaderWithData {
        private final LitematicaBlockStateContainer container;
        private final Map<class_2338, class_2487> blockEntityData;
        private final class_2382 size;
        private final class_2680 air;

        public BlockReaderLitematicaContainer(LitematicaBlockStateContainer container, @Nullable Map<class_2338, class_2487> blockEntityData) {
            this.container = container;
            this.blockEntityData = blockEntityData != null ? blockEntityData : new HashMap();
            this.size = container.getSize();
            this.air = class_2246.field_10124.method_9564();
        }

        public class_2680 method_8320(class_2338 pos) {
            if (pos.method_10263() >= 0 && pos.method_10263() < this.size.method_10263() && pos.method_10264() >= 0 && pos.method_10264() < this.size.method_10264() && pos.method_10260() >= 0 && pos.method_10260() < this.size.method_10260()) {
                return this.container.get(pos.method_10263(), pos.method_10264(), pos.method_10260());
            }
            return this.air;
        }

        public class_3610 method_8316(class_2338 pos) {
            return this.method_8320(pos).method_26227();
        }

        @Nullable
        public class_2586 method_8321(class_2338 pos) {
            return null;
        }

        @Override
        @Nullable
        public class_2487 getBlockEntityData(class_2338 pos) {
            return this.blockEntityData.get(pos);
        }
    }
}

