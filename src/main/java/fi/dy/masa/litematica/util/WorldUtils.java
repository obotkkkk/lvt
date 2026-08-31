/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.config.IConfigOptionListEntry
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.IStringConsumer
 *  fi.dy.masa.malilib.util.EntityUtils
 *  fi.dy.masa.malilib.util.FileNameUtils
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.MessageOutputType
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.WorldUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  fi.dy.masa.malilib.util.game.wrap.GameWrap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1269$class_9861
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1750
 *  net.minecraft.class_1799
 *  net.minecraft.class_1838
 *  net.minecraft.class_1937
 *  net.minecraft.class_2185
 *  net.minecraft.class_2190
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2286
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2341
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2382
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2458
 *  net.minecraft.class_2462
 *  net.minecraft.class_2478
 *  net.minecraft.class_2482
 *  net.minecraft.class_2487
 *  net.minecraft.class_2507
 *  net.minecraft.class_2546
 *  net.minecraft.class_2549
 *  net.minecraft.class_2551
 *  net.minecraft.class_2555
 *  net.minecraft.class_2586
 *  net.minecraft.class_2625
 *  net.minecraft.class_2680
 *  net.minecraft.class_2741
 *  net.minecraft.class_2747
 *  net.minecraft.class_2754
 *  net.minecraft.class_2760
 *  net.minecraft.class_2769
 *  net.minecraft.class_2771
 *  net.minecraft.class_2791
 *  net.minecraft.class_2806
 *  net.minecraft.class_2818
 *  net.minecraft.class_310
 *  net.minecraft.class_3492
 *  net.minecraft.class_3499
 *  net.minecraft.class_3532
 *  net.minecraft.class_3965
 *  net.minecraft.class_4538
 *  net.minecraft.class_638
 *  net.minecraft.class_8242
 *  net.minecraft.class_8810
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.config.Hotkeys;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.mixin.block.IMixinWallMountedBlock;
import fi.dy.masa.litematica.mixin.entity.IMixinSignBlockEntity;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.SchematicaSchematic;
import fi.dy.masa.litematica.schematic.pickblock.SchematicPickBlockEventHandler;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.litematica.selection.AreaSelection;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.tool.ToolMode;
import fi.dy.masa.litematica.util.DataFixerMode;
import fi.dy.masa.litematica.util.EasyPlaceProtocol;
import fi.dy.masa.litematica.util.EasyPlaceUtils;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.FileType;
import fi.dy.masa.litematica.util.IWorldUpdateSuppressor;
import fi.dy.masa.litematica.util.InventoryUtils;
import fi.dy.masa.litematica.util.PlacementHandler;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.RayTraceUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.IStringConsumer;
import fi.dy.masa.malilib.util.FileNameUtils;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.MessageOutputType;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import fi.dy.masa.malilib.util.game.wrap.GameWrap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1838;
import net.minecraft.class_1937;
import net.minecraft.class_2185;
import net.minecraft.class_2190;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2286;
import net.minecraft.class_2338;
import net.minecraft.class_2341;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2458;
import net.minecraft.class_2462;
import net.minecraft.class_2478;
import net.minecraft.class_2482;
import net.minecraft.class_2487;
import net.minecraft.class_2507;
import net.minecraft.class_2546;
import net.minecraft.class_2549;
import net.minecraft.class_2551;
import net.minecraft.class_2555;
import net.minecraft.class_2586;
import net.minecraft.class_2625;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2747;
import net.minecraft.class_2754;
import net.minecraft.class_2760;
import net.minecraft.class_2769;
import net.minecraft.class_2771;
import net.minecraft.class_2791;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_3492;
import net.minecraft.class_3499;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_4538;
import net.minecraft.class_638;
import net.minecraft.class_8242;
import net.minecraft.class_8810;

public class WorldUtils {
    @Deprecated(forRemoval=true)
    private static final List<PositionCache> EASY_PLACE_POSITIONS = new ArrayList<PositionCache>();
    @Deprecated(forRemoval=true)
    private static long easyPlaceLastPickBlockTime = System.nanoTime();

    public static double getValidBlockRange(class_310 mc) {
        return Configs.Generic.EASY_PLACE_VANILLA_REACH.getBooleanValue() ? mc.field_1724.method_55754() : mc.field_1724.method_55754() + 1.0;
    }

    public static boolean shouldPreventBlockUpdates(class_1937 world) {
        return ((IWorldUpdateSuppressor)world).litematica_getShouldPreventBlockUpdates();
    }

    public static void setShouldPreventBlockUpdates(class_1937 world, boolean preventUpdates) {
        ((IWorldUpdateSuppressor)world).litematica_setShouldPreventBlockUpdates(preventUpdates);
    }

    public static boolean convertLitematicaSchematicToLitematicaSchematic(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        LitematicaSchematic litematicaSchematic = WorldUtils.convertLitematicaSchematicToLitematicaSchematic(inputDir, inputFileName, outputFileName, feedback);
        return litematicaSchematic != null && litematicaSchematic.writeToFile(outputDir, outputFileName, override);
    }

    public static boolean convertSpongeSchematicToLitematicaSchematic(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        DataFixerMode oldMode = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.ALWAYS);
        LitematicaSchematic origSchematic = WorldUtils.convertSpongeSchematicToLitematicaSchematic(inputDir, inputFileName);
        if (origSchematic == null) {
            feedback.setString("litematica.error.schematic_conversion.sponge_to_litematica.failed_to_read_sponge");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return false;
        }
        WorldSchematic world = SchematicWorldHandler.createSchematicWorld(null);
        class_2338 size = new class_2338(origSchematic.getTotalSize());
        WorldUtils.loadChunksSchematicWorld(world, class_2338.field_10980, (class_2382)size);
        SchematicPlacement schematicPlacement = SchematicPlacement.createForSchematicConversion(origSchematic, class_2338.field_10980);
        origSchematic.placeToWorld(world, schematicPlacement, false);
        String subRegionName = FileNameUtils.getFileNameWithoutExtension((String)inputFileName);
        AreaSelection area = new AreaSelection();
        area.setName(subRegionName);
        subRegionName = area.createNewSubRegionBox(class_2338.field_10980, subRegionName);
        area.setSelectedSubRegionBox(subRegionName);
        Box box = area.getSelectedSubRegionBox();
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_1, class_2338.field_10980);
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_2, size.method_10069(-1, -1, -1));
        LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
        LitematicaSchematic newSchem = LitematicaSchematic.createFromWorld(world, area, info, "?", feedback);
        if (newSchem == null) {
            feedback.setString("litematica.error.schematic_conversion.sponge_to_litematica.failed_to_create_litematic");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return false;
        }
        SchematicMetadata origMetadata = origSchematic.getMetadata();
        if (origMetadata.getAuthor().isEmpty() || origMetadata.getAuthor() == "?") {
            newSchem.getMetadata().setAuthor(GameWrap.getPlayerName());
        } else {
            newSchem.getMetadata().setAuthor(origMetadata.getAuthor());
        }
        if (origMetadata.getName().isEmpty() || origMetadata.getName() == "?") {
            newSchem.getMetadata().setName(subRegionName);
        } else {
            newSchem.getMetadata().setName(origMetadata.getName());
        }
        newSchem.getMetadata().setDescription("Converted Sponge V" + origMetadata.getSchematicVersion() + ", Schema " + origMetadata.getSchemaString());
        newSchem.getMetadata().setTimeCreated(origMetadata.getTimeCreated());
        newSchem.getMetadata().setTimeModifiedToNow();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
        return newSchem.writeToFile(outputDir, outputFileName, override);
    }

    public static boolean convertSchematicaSchematicToLitematicaSchematic(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        LitematicaSchematic litematicaSchematic = WorldUtils.convertSchematicaSchematicToLitematicaSchematic(inputDir, inputFileName, ignoreEntities, feedback);
        return litematicaSchematic != null && litematicaSchematic.writeToFile(outputDir, outputFileName, override);
    }

    @Nullable
    public static LitematicaSchematic convertLitematicaSchematicToLitematicaSchematic(File inputDir, String inputFileName, String outputFilename, IStringConsumer feedback) {
        DataFixerMode oldMode = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.ALWAYS);
        LitematicaSchematic newSchematic = LitematicaSchematic.createFromFile(inputDir, inputFileName, FileType.LITEMATICA_SCHEMATIC);
        if (newSchematic == null) {
            feedback.setString("litematica.error.schematic_conversion.litematic_to_litematica.failed_to_read_litematic");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return null;
        }
        SchematicMetadata origMetadata = newSchematic.getMetadata();
        if (origMetadata.getAuthor().isEmpty() || origMetadata.getAuthor() == "?") {
            newSchematic.getMetadata().setAuthor(GameWrap.getPlayerName());
        } else {
            newSchematic.getMetadata().setAuthor(origMetadata.getAuthor());
        }
        if (origMetadata.getName().isEmpty() || origMetadata.getName() == "?") {
            newSchematic.getMetadata().setName(outputFilename);
        } else {
            newSchematic.getMetadata().setName(origMetadata.getName());
        }
        newSchematic.getMetadata().setDescription("Converted Litematic V" + origMetadata.getSchematicVersion() + ", Schema " + origMetadata.getSchemaString());
        newSchematic.getMetadata().setTimeCreated(origMetadata.getTimeCreated());
        newSchematic.getMetadata().setTimeModifiedToNow();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
        return newSchematic;
    }

    @Nullable
    public static LitematicaSchematic convertSchematicaSchematicToLitematicaSchematic(File inputDir, String inputFileName, boolean ignoreEntities, IStringConsumer feedback) {
        DataFixerMode oldMode = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.ALWAYS);
        SchematicaSchematic schematic = SchematicaSchematic.createFromFile(new File(inputDir, inputFileName));
        if (schematic == null) {
            feedback.setString("litematica.error.schematic_conversion.schematic_to_litematica.failed_to_read_schematic");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return null;
        }
        WorldSchematic world = SchematicWorldHandler.createSchematicWorld(null);
        WorldUtils.loadChunksSchematicWorld(world, class_2338.field_10980, schematic.getSize());
        class_3492 placementSettings = new class_3492();
        placementSettings.method_15133(ignoreEntities);
        schematic.placeSchematicDirectlyToChunks(world, class_2338.field_10980, placementSettings);
        Object subRegionName = FileNameUtils.getFileNameWithoutExtension((String)inputFileName) + " (Converted Schematic)";
        AreaSelection area = new AreaSelection();
        area.setName((String)subRegionName);
        subRegionName = area.createNewSubRegionBox(class_2338.field_10980, (String)subRegionName);
        area.setSelectedSubRegionBox((String)subRegionName);
        Box box = area.getSelectedSubRegionBox();
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_1, class_2338.field_10980);
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_2, new class_2338(schematic.getSize()).method_10069(-1, -1, -1));
        LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
        LitematicaSchematic newSchematic = LitematicaSchematic.createFromWorld(world, area, info, "?", feedback);
        if (newSchematic != null && !ignoreEntities) {
            newSchematic.takeEntityDataFromSchematicaSchematic(schematic, (String)subRegionName);
        } else {
            feedback.setString("litematica.error.schematic_conversion.schematic_to_litematica.failed_to_create_schematic");
        }
        newSchematic.getMetadata().setName((String)subRegionName);
        newSchematic.getMetadata().setAuthor(GameWrap.getPlayerName());
        newSchematic.getMetadata().setDescription("Converted Schematica Schematic, Schema " + String.valueOf(schematic.getMetadata().getSchema()));
        newSchematic.getMetadata().setTimeCreated(System.currentTimeMillis());
        newSchematic.getMetadata().setTimeModifiedToNow();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
        return newSchematic;
    }

    public static boolean convertStructureToLitematicaSchematic(File structureDir, String structureFileName, File outputDir, String outputFileName, boolean override) {
        LitematicaSchematic litematicaSchematic = WorldUtils.convertStructureToLitematicaSchematic(structureDir, structureFileName);
        return litematicaSchematic != null && litematicaSchematic.writeToFile(outputDir, outputFileName, override);
    }

    public static boolean convertStructureToLitematicaSchematic(File structureDir, String structureFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        DataFixerMode oldMode = (DataFixerMode)Configs.Generic.DATAFIXER_MODE.getOptionListValue();
        Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)DataFixerMode.ALWAYS);
        LitematicaSchematic origStructure = WorldUtils.convertStructureToLitematicaSchematic(structureDir, structureFileName);
        if (origStructure == null) {
            feedback.setString("litematica.error.schematic_conversion.structure_to_litematica.failed_to_read_structure");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return false;
        }
        WorldSchematic world = SchematicWorldHandler.createSchematicWorld(null);
        class_2338 size = new class_2338(origStructure.getTotalSize());
        WorldUtils.loadChunksSchematicWorld(world, class_2338.field_10980, (class_2382)size);
        SchematicPlacement schematicPlacement = SchematicPlacement.createForSchematicConversion(origStructure, class_2338.field_10980);
        origStructure.placeToWorld(world, schematicPlacement, false);
        String subRegionName = FileNameUtils.getFileNameWithoutExtension((String)structureFileName);
        AreaSelection area = new AreaSelection();
        area.setName(subRegionName);
        subRegionName = area.createNewSubRegionBox(class_2338.field_10980, subRegionName);
        area.setSelectedSubRegionBox(subRegionName);
        Box box = area.getSelectedSubRegionBox();
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_1, class_2338.field_10980);
        area.setSubRegionCornerPos(box, PositionUtils.Corner.CORNER_2, size.method_10069(-1, -1, -1));
        LitematicaSchematic.SchematicSaveInfo info = new LitematicaSchematic.SchematicSaveInfo(false, false);
        LitematicaSchematic newSchem = LitematicaSchematic.createFromWorld(world, area, info, "?", feedback);
        if (newSchem == null) {
            feedback.setString("litematica.error.schematic_conversion.structure_to_litematica.failed_to_create_litematic");
            Configs.Generic.DATAFIXER_MODE.setOptionListValue((IConfigOptionListEntry)oldMode);
            return false;
        }
        SchematicMetadata origMetadata = origStructure.getMetadata();
        if (origMetadata.getAuthor().isEmpty() || origMetadata.getAuthor() == "?") {
            newSchem.getMetadata().setAuthor(GameWrap.getPlayerName());
        } else {
            newSchem.getMetadata().setAuthor(origMetadata.getAuthor());
        }
        if (origMetadata.getName().isEmpty() || origMetadata.getName() == "?") {
            newSchem.getMetadata().setName(subRegionName);
        } else {
            newSchem.getMetadata().setName(origMetadata.getName());
        }
        newSchem.getMetadata().setDescription("Converted Vanilla Strucutre, Schema " + origMetadata.getSchemaString());
        newSchem.getMetadata().setTimeCreated(origMetadata.getTimeCreated());
        newSchem.getMetadata().setTimeModifiedToNow();
        boolean result = newSchem.writeToFile(outputDir, outputFileName, override);
        return result;
    }

    @Nullable
    public static LitematicaSchematic convertSpongeSchematicToLitematicaSchematic(File dir, String fileName) {
        try {
            LitematicaSchematic schematic = LitematicaSchematic.createFromFile(dir, fileName, FileType.SPONGE_SCHEMATIC);
            if (schematic == null) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)("Failed to read the Sponge schematic from '" + fileName + "\""), (Object[])new Object[0]);
            }
            return schematic;
        }
        catch (Exception e) {
            String msg = "Exception while trying to load the Sponge schematic: " + e.getMessage();
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)msg, (Object[])new Object[0]);
            Litematica.LOGGER.error(msg);
            return null;
        }
    }

    @Nullable
    public static LitematicaSchematic convertStructureToLitematicaSchematic(File structureDir, String structureFileName) {
        try {
            LitematicaSchematic litematicaSchematic = LitematicaSchematic.createFromFile(structureDir, structureFileName, FileType.VANILLA_STRUCTURE);
            if (litematicaSchematic == null) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)("Failed to read the vanilla structure template from '" + structureFileName + "\""), (Object[])new Object[0]);
            }
            return litematicaSchematic;
        }
        catch (Exception e) {
            InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)("Exception while trying to load the vanilla structure: " + e.getMessage()), (Object[])new Object[0]);
            Litematica.LOGGER.error("Exception while trying to load the vanilla structure: " + e.getMessage());
            return null;
        }
    }

    public static boolean convertLitematicaSchematicToSchematicaSchematic(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        return false;
    }

    public static boolean convertLitematicaSchematicToV6LitematicaSchematic(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        LitematicaSchematic v7LitematicaSchematic = LitematicaSchematic.createFromFile(inputDir, inputFileName, FileType.LITEMATICA_SCHEMATIC);
        if (v7LitematicaSchematic == null) {
            feedback.setString("litematica.error.schematic_conversion.litematic_to_litematica.failed_to_read_schematic");
            return false;
        }
        LitematicaSchematic v6LitematicaSchematic = LitematicaSchematic.createEmptySchematicFromExisting(v7LitematicaSchematic, class_310.method_1551().field_1724.method_5477().getString());
        v6LitematicaSchematic.downgradeV7toV6Schematic(v7LitematicaSchematic);
        if (v6LitematicaSchematic.writeToFile(outputDir, outputFileName, override, true)) {
            return true;
        }
        feedback.setString("litematica.error.schematic_conversion.litematic_to_litematica.failed_to_downgrade_litematic");
        return false;
    }

    public static boolean convertLitematicaSchematicToVanillaStructure(File inputDir, String inputFileName, File outputDir, String outputFileName, boolean ignoreEntities, boolean override, IStringConsumer feedback) {
        class_3499 template = WorldUtils.convertLitematicaSchematicToVanillaStructure(inputDir, inputFileName, ignoreEntities, feedback);
        return WorldUtils.writeVanillaStructureToFile(template, outputDir, outputFileName, override, feedback);
    }

    @Nullable
    public static class_3499 convertLitematicaSchematicToVanillaStructure(File inputDir, String inputFileName, boolean ignoreEntities, IStringConsumer feedback) {
        LitematicaSchematic litematicaSchematic = LitematicaSchematic.createFromFile(inputDir, inputFileName);
        if (litematicaSchematic == null) {
            feedback.setString("litematica.error.schematic_conversion.litematic_to_structure.failed_to_read_litematic");
            return null;
        }
        WorldSchematic world = SchematicWorldHandler.createSchematicWorld(null);
        class_2338 size = new class_2338(litematicaSchematic.getTotalSize());
        WorldUtils.loadChunksSchematicWorld(world, class_2338.field_10980, (class_2382)size);
        SchematicPlacement schematicPlacement = SchematicPlacement.createForSchematicConversion(litematicaSchematic, class_2338.field_10980);
        litematicaSchematic.placeToWorld(world, schematicPlacement, false);
        class_3499 template = new class_3499();
        template.method_15174((class_1937)world, class_2338.field_10980, (class_2382)size, !ignoreEntities, class_2246.field_10369);
        return template;
    }

    private static boolean writeVanillaStructureToFile(class_3499 template, File dir, String fileNameIn, boolean override, IStringConsumer feedback) {
        Object fileName = fileNameIn;
        String extension = ".nbt";
        if (!((String)fileName).endsWith(extension)) {
            fileName = (String)fileName + extension;
        }
        File file = new File(dir, (String)fileName);
        FileOutputStream os = null;
        try {
            if (!dir.exists() && !dir.mkdirs()) {
                feedback.setString(StringUtils.translate((String)"litematica.error.schematic_write_to_file_failed.directory_creation_failed", (Object[])new Object[]{dir.getAbsolutePath()}));
                return false;
            }
            if (!override && file.exists()) {
                feedback.setString(StringUtils.translate((String)"litematica.error.structure_write_to_file_failed.exists", (Object[])new Object[]{file.getAbsolutePath()}));
                return false;
            }
            class_2487 tag = template.method_15175(new class_2487());
            os = new FileOutputStream(file);
            class_2507.method_10634((class_2487)tag, (OutputStream)os);
            os.close();
            return true;
        }
        catch (Exception e) {
            feedback.setString(StringUtils.translate((String)"litematica.error.structure_write_to_file_failed.exception", (Object[])new Object[]{file.getAbsolutePath()}));
            return false;
        }
    }

    public static boolean isClientChunkLoaded(class_638 world, int chunkX, int chunkZ) {
        boolean test = world.method_2935().method_2857(chunkX, chunkZ, class_2806.field_12803, false) != null;
        return test;
    }

    public static void loadChunksSchematicWorld(WorldSchematic world, class_2338 origin, class_2382 areaSize) {
        class_2338 posEnd = origin.method_10081((class_2382)PositionUtils.getRelativeEndPositionFromAreaSize(areaSize));
        class_2338 posMin = PositionUtils.getMinCorner(origin, posEnd);
        class_2338 posMax = PositionUtils.getMaxCorner(origin, posEnd);
        int cxMin = posMin.method_10263() >> 4;
        int czMin = posMin.method_10260() >> 4;
        int cxMax = posMax.method_10263() >> 4;
        int czMax = posMax.method_10260() >> 4;
        for (int cz = czMin; cz <= czMax; ++cz) {
            for (int cx = cxMin; cx <= cxMax; ++cx) {
                world.getChunkProvider().loadChunk(cx, cz);
            }
        }
    }

    public static void setToolModeBlockState(ToolMode mode, boolean primary, class_310 mc) {
        class_3965 trace;
        class_2680 state = class_2246.field_10124.method_9564();
        class_1297 entity = fi.dy.masa.malilib.util.EntityUtils.getCameraEntity();
        RayTraceUtils.RayTraceWrapper wrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, entity, WorldUtils.getValidBlockRange(mc));
        if (wrapper != null && (trace = wrapper.getBlockHitResult()) != null && trace.method_17783() == class_239.class_240.field_1332) {
            class_2338 pos = trace.method_17777();
            if (wrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
                state = SchematicWorldHandler.getSchematicWorld().method_8320(pos);
            } else if (wrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.VANILLA_BLOCK) {
                state = mc.field_1687.method_8320(pos);
            }
        }
        if (primary) {
            mode.setPrimaryBlock(state);
        } else {
            mode.setSecondaryBlock(state);
        }
    }

    public static boolean doSchematicWorldPickBlock(boolean closest, class_310 mc) {
        WorldSchematic world;
        if (SchematicPickBlockEventHandler.getInstance().onSchematicPickBlockStart(closest)) {
            return true;
        }
        class_2338 pos = closest ? RayTraceUtils.getSchematicWorldTraceIfClosest((class_1937)mc.field_1687, (class_1297)mc.field_1724, WorldUtils.getValidBlockRange(mc)) : RayTraceUtils.getFurthestSchematicWorldBlockBeforeVanilla((class_1937)mc.field_1687, (class_1297)mc.field_1724, WorldUtils.getValidBlockRange(mc), true);
        if (pos != null && (world = SchematicWorldHandler.getSchematicWorld()) != null) {
            class_2680 state = world.method_8320(pos);
            if (SchematicPickBlockEventHandler.getInstance().onSchematicPickBlockPreGather(world, pos, state)) {
                return true;
            }
            class_1799 stack = SchematicPickBlockEventHandler.getInstance().hasPickStack() ? SchematicPickBlockEventHandler.getInstance().getPickStack() : MaterialCache.getInstance().getRequiredBuildItemForState(state, world, pos);
            if (SchematicPickBlockEventHandler.getInstance().onSchematicPickBlockPrePick(world, pos, state, stack)) {
                return true;
            }
            if (SchematicPickBlockEventHandler.getInstance().hasSlotHandler() && SchematicPickBlockEventHandler.getInstance().executePickBlockHandler(world, pos, stack)) {
                SchematicPickBlockEventHandler.getInstance().onSchematicPickBlockSuccess();
                return true;
            }
            InventoryUtils.schematicWorldPickBlock(stack, pos, world, mc);
            SchematicPickBlockEventHandler.getInstance().onSchematicPickBlockSuccess();
            return true;
        }
        return false;
    }

    public static void insertSignTextFromSchematic(class_2625 beClient, String[] screenTextArr, boolean front) {
        class_2586 beSchem;
        WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
        if (worldSchematic != null && (beSchem = worldSchematic.method_8321(beClient.method_11016())) instanceof class_2625) {
            class_8242 textSchematic;
            IMixinSignBlockEntity beMixinSchem = (IMixinSignBlockEntity)beSchem;
            class_8242 class_82422 = textSchematic = front ? beMixinSchem.litematica_getFrontText() : beMixinSchem.litematica_getBackText();
            if (textSchematic != null) {
                for (int i = 0; i < screenTextArr.length; ++i) {
                    screenTextArr[i] = textSchematic.method_49859(i, false).getString();
                }
                beClient.method_49840(textSchematic, front);
            }
        }
    }

    public static void easyPlaceOnUseTick(class_310 mc) {
        if (mc.field_1724 != null && DataManager.getToolMode() != ToolMode.REBUILD && Configs.Generic.EASY_PLACE_MODE.getBooleanValue() && Configs.Generic.EASY_PLACE_HOLD_ENABLED.getBooleanValue() && Hotkeys.EASY_PLACE_ACTIVATION.getKeybind().isKeybindHeld()) {
            WorldUtils.doEasyPlaceAction(mc);
        }
    }

    public static boolean handleEasyPlace(class_310 mc) {
        if (Configs.Generic.EASY_PLACE_MODE.getBooleanValue() && DataManager.getToolMode() != ToolMode.REBUILD) {
            class_1269 result = WorldUtils.doEasyPlaceAction(mc);
            if (result == class_1269.field_5814) {
                MessageOutputType type = (MessageOutputType)Configs.Generic.PLACEMENT_RESTRICTION_WARN.getOptionListValue();
                if (type == MessageOutputType.MESSAGE) {
                    InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.easy_place_fail", (Object[])new Object[0]);
                } else if (type == MessageOutputType.ACTIONBAR) {
                    InfoUtils.printActionbarMessage((String)"litematica.message.easy_place_fail", (Object[])new Object[0]);
                }
                return true;
            }
            return result != class_1269.field_5811;
        }
        return false;
    }

    private static class_1269 doEasyPlaceAction(class_310 mc) {
        RayTraceUtils.RayTraceWrapper traceWrapper;
        double traceMaxRange = WorldUtils.getValidBlockRange(mc);
        if (Configs.Generic.EASY_PLACE_FIRST.getBooleanValue()) {
            boolean targetFluids = Configs.InfoOverlays.INFO_OVERLAYS_TARGET_FLUIDS.getBooleanValue();
            traceWrapper = RayTraceUtils.getGenericTrace((class_1937)mc.field_1687, (class_1297)mc.field_1724, traceMaxRange, true, targetFluids, false);
        } else {
            traceWrapper = RayTraceUtils.getFurthestSchematicWorldTraceBeforeVanilla((class_1937)mc.field_1687, (class_1297)mc.field_1724, traceMaxRange);
            if (traceWrapper == null && WorldUtils.placementRestrictionInEffect(mc)) {
                return class_1269.field_5814;
            }
        }
        if (traceWrapper == null) {
            return class_1269.field_5811;
        }
        if (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.SCHEMATIC_BLOCK) {
            class_3965 trace = traceWrapper.getBlockHitResult();
            class_239 traceVanilla = RayTraceUtils.getRayTraceFromEntity((class_1937)mc.field_1687, (class_1297)mc.field_1724, false, traceMaxRange);
            class_2338 pos = trace.method_17777();
            WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
            class_2680 stateSchematic = world.method_8320(pos);
            class_1799 stack = MaterialCache.getInstance().getRequiredBuildItemForState(stateSchematic);
            if (EasyPlaceUtils.easyPlaceIsPositionCached(pos)) {
                return class_1269.field_5814;
            }
            if (EasyPlaceUtils.easyPlaceIsTooFast()) {
                return class_1269.field_5814;
            }
            if (!stack.method_7960()) {
                class_2680 stateClient = mc.field_1687.method_8320(pos);
                if (stateSchematic == stateClient) {
                    return class_1269.field_5814;
                }
                if (WorldUtils.easyPlaceBlockChecksCancel(stateSchematic, stateClient, (class_1657)mc.field_1724, traceVanilla, stack)) {
                    return class_1269.field_5814;
                }
                InventoryUtils.schematicWorldPickBlock(stack, pos, world, mc);
                class_1268 hand = EntityUtils.getUsedHandForItem((class_1657)mc.field_1724, stack);
                if (hand == null) {
                    return class_1269.field_5814;
                }
                class_243 hitPos = trace.method_17784();
                class_2350 sideOrig = trace.method_17780();
                EasyPlaceProtocol protocol = PlacementHandler.getEffectiveProtocolVersion();
                if ((protocol == EasyPlaceProtocol.NONE || protocol == EasyPlaceProtocol.SLAB_ONLY) && traceVanilla != null && traceVanilla.method_17783() == class_239.class_240.field_1332) {
                    class_3965 hitResult = (class_3965)traceVanilla;
                    class_2338 posVanilla = hitResult.method_17777();
                    class_2350 sideVanilla = hitResult.method_17780();
                    class_2680 stateVanilla = mc.field_1687.method_8320(posVanilla);
                    class_243 hit = traceVanilla.method_17784();
                    class_1750 ctx = new class_1750(new class_1838((class_1657)mc.field_1724, hand, hitResult));
                    if (!stateVanilla.method_26166(ctx) && pos.equals((Object)(posVanilla = posVanilla.method_10093(sideVanilla)))) {
                        hitPos = hit;
                        sideOrig = sideVanilla;
                    }
                }
                class_2350 side = WorldUtils.applyPlacementFacing(stateSchematic, sideOrig, stateClient);
                PlacementProtocolData placementData = WorldUtils.applyPlacementProtocolAll(pos, stateSchematic, hitPos);
                if (placementData.mustFail) {
                    return class_1269.field_5814;
                }
                if (placementData.handled) {
                    pos = placementData.pos;
                    side = placementData.side;
                    hitPos = placementData.hitVec;
                }
                if (protocol == EasyPlaceProtocol.V3) {
                    hitPos = WorldUtils.applyPlacementProtocolV3(pos, stateSchematic, hitPos);
                } else if (protocol == EasyPlaceProtocol.V2) {
                    hitPos = WorldUtils.applyCarpetProtocolHitVec(pos, stateSchematic, hitPos);
                } else if (protocol == EasyPlaceProtocol.SLAB_ONLY) {
                    hitPos = WorldUtils.applyBlockSlabProtocol(pos, stateSchematic, hitPos);
                }
                EasyPlaceUtils.cacheEasyPlacePosition(pos);
                class_3965 hitResult = new class_3965(hitPos, side, pos, false);
                class_1269 result = mc.field_1761.method_2896(mc.field_1724, hand, hitResult);
                if (class_1269.field_5812.comp_2909().equals((Object)class_1269.class_9861.field_52427) && Configs.Generic.EASY_PLACE_SWING_HAND.getBooleanValue()) {
                    mc.field_1724.method_6104(hand);
                }
                if (stateSchematic.method_26204() instanceof class_2482 && stateSchematic.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12682 && (stateClient = mc.field_1687.method_8320(pos)).method_26204() instanceof class_2482 && stateClient.method_11654((class_2769)class_2482.field_11501) != class_2771.field_12682) {
                    side = WorldUtils.applyPlacementFacing(stateSchematic, sideOrig, stateClient);
                    hitResult = new class_3965(hitPos, side, pos, false);
                    mc.field_1761.method_2896(mc.field_1724, hand, hitResult);
                }
            }
            return class_1269.field_5812;
        }
        if (traceWrapper.getHitType() == RayTraceUtils.RayTraceWrapper.HitType.VANILLA_BLOCK) {
            return WorldUtils.placementRestrictionInEffect(mc) ? class_1269.field_5814 : class_1269.field_5811;
        }
        return class_1269.field_5811;
    }

    private static boolean easyPlaceBlockChecksCancel(class_2680 stateSchematic, class_2680 stateClient, class_1657 player, class_239 trace, class_1799 stack) {
        class_2248 blockClient;
        class_2248 blockSchematic = stateSchematic.method_26204();
        if (blockSchematic instanceof class_2482 && stateSchematic.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12682 && (blockClient = stateClient.method_26204()) instanceof class_2482 && stateClient.method_11654((class_2769)class_2482.field_11501) != class_2771.field_12682) {
            return blockSchematic != blockClient;
        }
        if (trace.method_17783() != class_239.class_240.field_1332) {
            return false;
        }
        class_3965 hitResult = (class_3965)trace;
        class_1750 ctx = new class_1750(new class_1838(player, class_1268.field_5808, hitResult));
        return !stateClient.method_26166(ctx);
    }

    public static PlacementProtocolData applyPlacementProtocolAll(class_2338 pos, class_2680 stateSchematic, class_243 hitVecIn) {
        PlacementProtocolData placementData = new PlacementProtocolData();
        class_2248 stateBlock = stateSchematic.method_26204();
        class_638 world = class_310.method_1551().field_1687;
        if (stateBlock instanceof class_8810 || stateBlock instanceof class_2185 || stateBlock instanceof class_2478 || stateBlock instanceof class_2190) {
            placementData.handled = true;
            placementData.hitVec = hitVecIn;
            if (stateBlock instanceof class_2555 || stateBlock instanceof class_2458 || stateBlock instanceof class_2546 || stateBlock instanceof class_2551 || stateBlock instanceof class_2549) {
                placementData.side = (class_2350)stateSchematic.method_11654((class_2769)class_2741.field_12481);
                placementData.pos = pos.method_10093(placementData.side.method_10153());
            } else {
                placementData.side = class_2350.field_11036;
                placementData.pos = pos.method_10074();
            }
            class_2680 stateFacing = world.method_8320(placementData.pos);
            if (stateFacing == null || stateFacing.method_26215()) {
                placementData.mustFail = true;
            }
        } else if (stateBlock instanceof class_2341 && !((IMixinWallMountedBlock)stateBlock).litematica_invokeCanPlaceAt(stateSchematic, (class_4538)world, pos)) {
            placementData.mustFail = true;
        }
        return placementData;
    }

    public static class_243 applyCarpetProtocolHitVec(class_2338 pos, class_2680 state, class_243 hitVecIn) {
        double x = hitVecIn.field_1352;
        double y = hitVecIn.field_1351;
        double z = hitVecIn.field_1350;
        class_2248 block = state.method_26204();
        Optional facing = BlockUtils.getFirstPropertyFacingValue((class_2680)state);
        int propertyIncrement = 16;
        boolean hasData = false;
        int protocolValue = 0;
        if (facing.isPresent()) {
            protocolValue = ((class_2350)facing.get()).method_10146();
            hasData = true;
        } else if (state.method_28498((class_2769)class_2741.field_12496)) {
            class_2350.class_2351 axis = (class_2350.class_2351)state.method_11654((class_2769)class_2741.field_12496);
            protocolValue = axis.ordinal();
            hasData = true;
        }
        if (block instanceof class_2462) {
            protocolValue += (Integer)state.method_11654((class_2769)class_2462.field_11451) * 16;
        } else if (block instanceof class_2286 && state.method_11654((class_2769)class_2286.field_10789) == class_2747.field_12578) {
            protocolValue += 16;
        } else if (state.method_28498((class_2769)class_2741.field_12518) && state.method_11654((class_2769)class_2741.field_12518) == class_2760.field_12619) {
            protocolValue += 16;
        } else if (state.method_28498((class_2769)class_2741.field_12485) && state.method_11654((class_2769)class_2741.field_12485) == class_2771.field_12679) {
            protocolValue += 16;
        }
        y = WorldUtils.applySlabOrStairHitVecY(y, pos, state);
        if (protocolValue != 0 || hasData) {
            x += (double)(protocolValue * 2 + 2);
        }
        return new class_243(x, y, z);
    }

    private static double applySlabOrStairHitVecY(double origY, class_2338 pos, class_2680 state) {
        double y = origY;
        if (state.method_28498((class_2769)class_2741.field_12485)) {
            y = pos.method_10264();
            if (state.method_11654((class_2769)class_2741.field_12485) == class_2771.field_12679) {
                y += 0.99;
            }
        } else if (state.method_28498((class_2769)class_2741.field_12518)) {
            y = pos.method_10264();
            if (state.method_11654((class_2769)class_2741.field_12518) == class_2760.field_12619) {
                y += 0.99;
            }
        }
        return y;
    }

    protected static class_243 applyBlockSlabProtocol(class_2338 pos, class_2680 state, class_243 hitVecIn) {
        double newY = WorldUtils.applySlabOrStairHitVecY(hitVecIn.field_1351, pos, state);
        return newY != hitVecIn.field_1351 ? new class_243(hitVecIn.field_1352, newY, hitVecIn.field_1350) : hitVecIn;
    }

    public static <T extends Comparable<T>> class_243 applyPlacementProtocolV3(class_2338 pos, class_2680 state, class_243 hitVecIn) {
        Collection props = state.method_26204().method_9595().method_11659();
        if (props.isEmpty()) {
            return hitVecIn;
        }
        double relX = hitVecIn.field_1352 - (double)pos.method_10263();
        int protocolValue = 0;
        int shiftAmount = 1;
        int propCount = 0;
        Optional property = BlockUtils.getFirstDirectionProperty((class_2680)state);
        if (property.isPresent() && property.get() != class_2741.field_28062) {
            class_2350 direction = (class_2350)state.method_11654((class_2769)property.get());
            protocolValue |= direction.method_10146() << shiftAmount;
            shiftAmount += 3;
            ++propCount;
        }
        ArrayList<class_2769> propList = new ArrayList<class_2769>(props);
        propList.sort(Comparator.comparing(class_2769::method_11899));
        try {
            for (class_2769 p : propList) {
                if (property.isPresent() && ((class_2754)property.get()).equals((Object)p) || !PlacementHandler.WHITELISTED_PROPERTIES.contains((Object)p) || PlacementHandler.BLACKLISTED_PROPERTIES.contains((Object)p)) continue;
                class_2769 prop = p;
                ArrayList list = new ArrayList(prop.method_11898());
                list.sort(Comparable::compareTo);
                int requiredBits = class_3532.method_15351((int)class_3532.method_15339((int)list.size()));
                int valueIndex = list.indexOf(state.method_11654(prop));
                if (valueIndex == -1) continue;
                protocolValue |= valueIndex << shiftAmount;
                shiftAmount += requiredBits;
                ++propCount;
            }
        }
        catch (Exception e) {
            Litematica.LOGGER.warn("Exception trying to request placement protocol value", (Throwable)e);
        }
        if (propCount > 0) {
            double x = (double)pos.method_10263() + relX + 2.0 + (double)protocolValue;
            return new class_243(x, hitVecIn.field_1351, hitVecIn.field_1350);
        }
        return hitVecIn;
    }

    protected static class_2350 applyPlacementFacing(class_2680 stateSchematic, class_2350 side, class_2680 stateClient) {
        class_2248 blockSchematic = stateSchematic.method_26204();
        class_2248 blockClient = stateClient.method_26204();
        if (blockSchematic instanceof class_2482) {
            if (stateSchematic.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12682 && blockClient instanceof class_2482 && stateClient.method_11654((class_2769)class_2482.field_11501) != class_2771.field_12682) {
                if (stateClient.method_11654((class_2769)class_2482.field_11501) == class_2771.field_12679) {
                    return class_2350.field_11033;
                }
                return class_2350.field_11036;
            }
            return class_2350.field_11043;
        }
        if (stateSchematic.method_28498((class_2769)class_2741.field_12518)) {
            side = stateSchematic.method_11654((class_2769)class_2741.field_12518) == class_2760.field_12619 ? class_2350.field_11033 : class_2350.field_11036;
        }
        return side;
    }

    public static boolean handlePlacementRestriction(class_310 mc) {
        boolean cancel = WorldUtils.placementRestrictionInEffect(mc);
        if (cancel) {
            MessageOutputType type = (MessageOutputType)Configs.Generic.PLACEMENT_RESTRICTION_WARN.getOptionListValue();
            if (type == MessageOutputType.MESSAGE) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.WARNING, (String)"litematica.message.placement_restriction_fail", (Object[])new Object[0]);
            } else if (type == MessageOutputType.ACTIONBAR) {
                InfoUtils.printActionbarMessage((String)"litematica.message.placement_restriction_fail", (Object[])new Object[0]);
            }
        }
        return cancel;
    }

    private static boolean placementRestrictionInEffect(class_310 mc) {
        class_239 trace = mc.field_1765;
        class_1799 stack = mc.field_1724.method_6047();
        if (stack.method_7960()) {
            stack = mc.field_1724.method_6079();
        }
        if (stack.method_7960()) {
            return false;
        }
        if (trace != null && trace.method_17783() == class_239.class_240.field_1332) {
            class_3965 blockHitResult = (class_3965)trace;
            class_1750 ctx = new class_1750(new class_1838((class_1657)mc.field_1724, class_1268.field_5808, blockHitResult));
            class_2338 pos = ctx.method_8037();
            class_2680 stateClient = mc.field_1687.method_8320(pos);
            WorldSchematic worldSchematic = SchematicWorldHandler.getSchematicWorld();
            LayerRange range = DataManager.getRenderLayerRange();
            boolean schematicHasAir = worldSchematic.method_22347(pos);
            if (!schematicHasAir && !range.isPositionWithinRange(pos)) {
                return true;
            }
            if (schematicHasAir && WorldUtils.isPositionWithinRangeOfSchematicRegions(pos, 2)) {
                return true;
            }
            ctx = new class_1750(new class_1838((class_1657)mc.field_1724, class_1268.field_5808, blockHitResult = new class_3965(blockHitResult.method_17784(), blockHitResult.method_17780(), pos, false)));
            if (!stateClient.method_26166(ctx)) {
                return true;
            }
            class_2680 stateSchematic = worldSchematic.method_8320(pos);
            stack = MaterialCache.getInstance().getRequiredBuildItemForState(stateSchematic);
            if (!stack.method_7960() && EntityUtils.getUsedHandForItem((class_1657)mc.field_1724, stack) == null) {
                return true;
            }
            class_2248 schematicBlock = stateSchematic.method_26204();
            if ((schematicBlock instanceof class_2555 || schematicBlock instanceof class_2458 || schematicBlock instanceof class_2546 || schematicBlock instanceof class_2551 || schematicBlock instanceof class_2549) && blockHitResult.method_17780() != stateSchematic.method_11654((class_2769)class_2741.field_12481)) {
                return true;
            }
            class_2680 attemptState = schematicBlock.method_9605(ctx);
            return !WorldUtils.isMatchingStatePlacementRestriction(attemptState, stateSchematic);
        }
        return false;
    }

    private static boolean isMatchingStatePlacementRestriction(class_2680 state1, class_2680 state2) {
        class_2769[] orientationProperties;
        if (state1 == null || state2 == null) {
            return false;
        }
        if (state1 == state2) {
            return true;
        }
        for (class_2769 property : orientationProperties = new class_2769[]{class_2741.field_12525, class_2741.field_12518, class_2741.field_12545, class_2741.field_12520, class_2741.field_12481, class_2741.field_12496, class_2741.field_12485, class_2741.field_28062, class_2741.field_12532, class_2741.field_16561, class_2741.field_12555, class_2741.field_17104}) {
            boolean hasProperty2;
            boolean hasProperty1 = state1.method_28498(property);
            if (hasProperty1 != (hasProperty2 = state2.method_28498(property))) {
                return false;
            }
            if (!hasProperty1 || state1.method_11654(property) == state2.method_11654(property)) continue;
            return false;
        }
        return true;
    }

    public static boolean isPositionWithinRangeOfSchematicRegions(class_2338 pos, int range) {
        SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
        int x = pos.method_10263();
        int y = pos.method_10264();
        int z = pos.method_10260();
        int minCX = x - range >> 4;
        int minCZ = z - range >> 4;
        int maxCX = x + range >> 4;
        int maxCZ = z + range >> 4;
        for (int cz = minCZ; cz <= maxCZ; ++cz) {
            for (int cx = minCX; cx <= maxCX; ++cx) {
                List<SchematicPlacementManager.PlacementPart> parts = manager.getPlacementPartsInChunk(cx, cz);
                for (SchematicPlacementManager.PlacementPart part : parts) {
                    IntBoundingBox box = part.bb;
                    if (x < box.minX - range || x > box.maxX + range || y < box.minY - range || y > box.maxY + range || z < box.minZ - range || z > box.maxZ + range) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSliceEmpty(class_1937 world, class_2350.class_2351 axis, class_2338 pos1, class_2338 pos2) {
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        switch (axis) {
            case field_11051: {
                int x1 = Math.min(pos1.method_10263(), pos2.method_10263());
                int x2 = Math.max(pos1.method_10263(), pos2.method_10263());
                int y1 = Math.min(pos1.method_10264(), pos2.method_10264());
                int y2 = Math.max(pos1.method_10264(), pos2.method_10264());
                int z = pos1.method_10260();
                int cxMin = x1 >> 4;
                int cxMax = x2 >> 4;
                for (int cx = cxMin; cx <= cxMax; ++cx) {
                    class_2818 chunk = world.method_8497(cx, z >> 4);
                    int xMin = Math.max(x1, cx << 4);
                    int xMax = Math.min(x2, (cx << 4) + 15);
                    int yMax = Math.min(y2, fi.dy.masa.malilib.util.WorldUtils.getHighestSectionYOffset((class_2791)chunk) + 15);
                    for (int x = xMin; x <= xMax; ++x) {
                        for (int y = y1; y <= yMax; ++y) {
                            if (chunk.method_8320((class_2338)posMutable.method_10103(x, y, z)).method_26215()) continue;
                            return false;
                        }
                    }
                }
                break;
            }
            case field_11052: {
                int x1 = Math.min(pos1.method_10263(), pos2.method_10263());
                int x2 = Math.max(pos1.method_10263(), pos2.method_10263());
                int y = pos1.method_10264();
                int z1 = Math.min(pos1.method_10260(), pos2.method_10260());
                int z2 = Math.max(pos1.method_10260(), pos2.method_10260());
                int cxMin = x1 >> 4;
                int cxMax = x2 >> 4;
                int czMin = z1 >> 4;
                int czMax = z2 >> 4;
                for (int cz = czMin; cz <= czMax; ++cz) {
                    for (int cx = cxMin; cx <= cxMax; ++cx) {
                        class_2818 chunk = world.method_8497(cx, cz);
                        if (y > fi.dy.masa.malilib.util.WorldUtils.getHighestSectionYOffset((class_2791)chunk) + 15) continue;
                        int xMin = Math.max(x1, cx << 4);
                        int xMax = Math.min(x2, (cx << 4) + 15);
                        int zMin = Math.max(z1, cz << 4);
                        int zMax = Math.min(z2, (cz << 4) + 15);
                        for (int z = zMin; z <= zMax; ++z) {
                            for (int x = xMin; x <= xMax; ++x) {
                                if (chunk.method_8320((class_2338)posMutable.method_10103(x, y, z)).method_26215()) continue;
                                return false;
                            }
                        }
                    }
                }
                break;
            }
            case field_11048: {
                int x = pos1.method_10263();
                int z1 = Math.min(pos1.method_10260(), pos2.method_10260());
                int z2 = Math.max(pos1.method_10260(), pos2.method_10260());
                int y1 = Math.min(pos1.method_10264(), pos2.method_10264());
                int y2 = Math.max(pos1.method_10264(), pos2.method_10264());
                int czMin = z1 >> 4;
                int czMax = z2 >> 4;
                for (int cz = czMin; cz <= czMax; ++cz) {
                    class_2818 chunk = world.method_8497(x >> 4, cz);
                    int zMin = Math.max(z1, cz << 4);
                    int zMax = Math.min(z2, (cz << 4) + 15);
                    int yMax = Math.min(y2, fi.dy.masa.malilib.util.WorldUtils.getHighestSectionYOffset((class_2791)chunk) + 15);
                    for (int z = zMin; z <= zMax; ++z) {
                        for (int y = y1; y <= yMax; ++y) {
                            if (chunk.method_8320((class_2338)posMutable.method_10103(x, y, z)).method_26215()) continue;
                            return false;
                        }
                    }
                }
                break;
            }
        }
        return true;
    }

    @Deprecated(forRemoval=true)
    private static boolean easyPlaceIsPositionCached(class_2338 pos) {
        long currentTime = System.nanoTime();
        boolean cached = false;
        for (int i = 0; i < EASY_PLACE_POSITIONS.size(); ++i) {
            PositionCache val = EASY_PLACE_POSITIONS.get(i);
            boolean expired = val.hasExpired(currentTime);
            if (expired) {
                EASY_PLACE_POSITIONS.remove(i);
                --i;
                continue;
            }
            if (!val.getPos().equals((Object)pos)) continue;
            cached = true;
            if (EASY_PLACE_POSITIONS.size() < 16) break;
        }
        return cached;
    }

    @Deprecated(forRemoval=true)
    private static void cacheEasyPlacePosition(class_2338 pos) {
        EASY_PLACE_POSITIONS.add(new PositionCache(pos, System.nanoTime(), 2000000000L));
    }

    @Deprecated(forRemoval=true)
    private static boolean easyPlaceIsTooFast() {
        return System.nanoTime() - easyPlaceLastPickBlockTime < 1000000L * (long)Configs.Generic.EASY_PLACE_SWAP_INTERVAL.getIntegerValue();
    }

    @Deprecated(forRemoval=true)
    private static void setEasyPlaceLastPickBlockTime() {
        easyPlaceLastPickBlockTime = System.nanoTime();
    }

    public static class PlacementProtocolData {
        boolean handled;
        boolean mustFail;
        class_2338 pos;
        class_2350 side;
        class_243 hitVec;
    }

    @Deprecated(forRemoval=true)
    private static class PositionCache {
        private final class_2338 pos;
        private final long time;
        private final long timeout;

        private PositionCache(class_2338 pos, long time, long timeout) {
            this.pos = pos;
            this.time = time;
            this.timeout = timeout;
        }

        public class_2338 getPos() {
            return this.pos;
        }

        public boolean hasExpired(long currentTime) {
            return currentTime - this.time > this.timeout;
        }
    }
}

