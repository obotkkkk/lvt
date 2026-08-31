/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.interfaces.IFileBrowserIconProvider
 *  fi.dy.masa.malilib.gui.interfaces.ISelectionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase$DirectoryEntry
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.Schema
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1011
 *  net.minecraft.class_1043
 *  net.minecraft.class_1044
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.gui.widgets;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.gui.GuiSchematicBrowserBase;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.SchematicMetadata;
import fi.dy.masa.litematica.schematic.SchematicSchema;
import fi.dy.masa.malilib.gui.interfaces.IFileBrowserIconProvider;
import fi.dy.masa.malilib.gui.interfaces.ISelectionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.Schema;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_1044;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;

public class WidgetSchematicBrowser
extends WidgetFileBrowserBase {
    protected static final FileFilter SCHEMATIC_FILTER = new FileFilterSchematics();
    protected final Map<File, SchematicMetadata> cachedMetadata = new HashMap<File, SchematicMetadata>();
    protected final Map<File, SchematicSchema> cachedVersion = new HashMap<File, SchematicSchema>();
    protected final Map<File, Pair<class_2960, class_1043>> cachedPreviewImages = new HashMap<File, Pair<class_2960, class_1043>>();
    protected final GuiSchematicBrowserBase parent;
    protected final int infoWidth;
    protected final int infoHeight;

    public WidgetSchematicBrowser(int x, int y, int width, int height, GuiSchematicBrowserBase parent, @Nullable ISelectionListener<WidgetFileBrowserBase.DirectoryEntry> selectionListener) {
        super(x, y, width, height, DataManager.getDirectoryCache(), parent.getBrowserContext(), parent.getDefaultDirectory(), selectionListener, (IFileBrowserIconProvider)Icons.FILE_ICON_LITEMATIC);
        this.title = StringUtils.translate((String)"litematica.gui.title.schematic_browser", (Object[])new Object[0]);
        this.infoWidth = 170;
        this.infoHeight = 310;
        this.parent = parent;
    }

    protected int getBrowserWidthForTotalWidth(int width) {
        return super.getBrowserWidthForTotalWidth(width) - this.infoWidth;
    }

    public void method_25419() {
        super.method_25419();
        this.clearPreviewImages();
    }

    protected File getRootDirectory() {
        return DataManager.getSchematicsBaseDirectory();
    }

    protected FileFilter getFileFilter() {
        return SCHEMATIC_FILTER;
    }

    protected void drawAdditionalContents(int mouseX, int mouseY, class_332 drawContext) {
        this.drawSelectedSchematicInfo((WidgetFileBrowserBase.DirectoryEntry)this.getLastSelectedEntry(), drawContext);
    }

    protected void drawSelectedSchematicInfo(@Nullable WidgetFileBrowserBase.DirectoryEntry entry, class_332 drawContext) {
        int x = this.posX + this.totalWidth - this.infoWidth;
        int y = this.posY;
        int height = Math.min(this.infoHeight, this.parent.getMaxInfoHeight());
        RenderUtils.drawOutlinedBox((int)x, (int)y, (int)this.infoWidth, (int)height, (int)-1610612736, (int)-6710887);
        if (entry == null) {
            return;
        }
        Pair<SchematicSchema, SchematicMetadata> metaPair = this.getSchematicVersionAndMetadata(entry);
        if (metaPair == null) {
            return;
        }
        SchematicMetadata meta = (SchematicMetadata)metaPair.getRight();
        SchematicSchema version = (SchematicSchema)metaPair.getLeft();
        if (meta != null) {
            Pair<class_2960, class_1043> pair;
            RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            int textColor = -1061109568;
            int valueColor = -1;
            String str = StringUtils.translate((String)"litematica.gui.label.schematic_info.name", (Object[])new Object[0]);
            this.drawString(drawContext, str, x += 3, y += 3, textColor);
            this.drawString(drawContext, meta.getName(), x + 4, y += 12, valueColor);
            str = StringUtils.translate((String)"litematica.gui.label.schematic_info.schematic_author", (Object[])new Object[]{meta.getAuthor()});
            this.drawString(drawContext, str, x, y += 12, textColor);
            String strDate = DATE_FORMAT.format(new Date(meta.getTimeCreated()));
            str = StringUtils.translate((String)"litematica.gui.label.schematic_info.time_created", (Object[])new Object[]{strDate});
            this.drawString(drawContext, str, x, y += 12, textColor);
            y += 12;
            if (meta.hasBeenModified()) {
                strDate = DATE_FORMAT.format(new Date(meta.getTimeModified()));
                str = StringUtils.translate((String)"litematica.gui.label.schematic_info.time_modified", (Object[])new Object[]{strDate});
                this.drawString(drawContext, str, x, y, textColor);
                y += 12;
            }
            str = StringUtils.translate((String)"litematica.gui.label.schematic_info.region_count", (Object[])new Object[]{meta.getRegionCount()});
            this.drawString(drawContext, str, x, y, textColor);
            y += 12;
            if (this.parent.getScreenHeight() >= 340) {
                str = StringUtils.translate((String)"litematica.gui.label.schematic_info.total_volume", (Object[])new Object[]{meta.getTotalVolume()});
                this.drawString(drawContext, str, x, y, textColor);
                y += 12;
                if (meta.getTotalBlocks() > 0) {
                    str = StringUtils.translate((String)"litematica.gui.label.schematic_info.total_blocks", (Object[])new Object[]{meta.getTotalBlocks()});
                    this.drawString(drawContext, str, x, y, textColor);
                    y += 12;
                }
                str = StringUtils.translate((String)"litematica.gui.label.schematic_info.enclosing_size", (Object[])new Object[0]);
                this.drawString(drawContext, str, x, y, textColor);
                areaSize = meta.getEnclosingSize();
                tmp = String.format("%d x %d x %d", areaSize.method_10263(), areaSize.method_10264(), areaSize.method_10260());
                this.drawString(drawContext, tmp, x + 4, y += 12, valueColor);
                y += 12;
            } else {
                if (meta.getTotalBlocks() > 0) {
                    str = StringUtils.translate((String)"litematica.gui.label.schematic_info.total_blocks_and_volume", (Object[])new Object[]{meta.getTotalBlocks(), meta.getTotalVolume()});
                    this.drawString(drawContext, str, x, y, textColor);
                    y += 12;
                } else {
                    str = StringUtils.translate((String)"litematica.gui.label.schematic_info.total_volume", (Object[])new Object[]{meta.getTotalVolume()});
                    this.drawString(drawContext, str, x, y, textColor);
                    y += 12;
                }
                areaSize = meta.getEnclosingSize();
                tmp = String.format("%d x %d x %d", areaSize.method_10263(), areaSize.method_10264(), areaSize.method_10260());
                str = StringUtils.translate((String)"litematica.gui.label.schematic_info.enclosing_size_value", (Object[])new Object[]{tmp});
                this.drawString(drawContext, str, x, y, textColor);
                y += 12;
            }
            if (version != null) {
                switch (meta.getFileType()) {
                    case LITEMATICA_SCHEMATIC: {
                        str = StringUtils.translate((String)"litematica.gui.label.schematic_info.version", (Object[])new Object[]{version.litematicVersion()});
                        this.drawString(drawContext, str, x, y, textColor);
                        y += 12;
                        break;
                    }
                    case SPONGE_SCHEMATIC: {
                        str = StringUtils.translate((String)"litematica.gui.label.schematic_info.sponge_version", (Object[])new Object[]{version.litematicVersion()});
                        this.drawString(drawContext, str, x, y, textColor);
                        y += 12;
                        break;
                    }
                    case VANILLA_STRUCTURE: {
                        str = StringUtils.translate((String)"litematica.gui.label.schematic_info.vanilla_version", (Object[])new Object[0]);
                        this.drawString(drawContext, str, x, y, textColor);
                        y += 12;
                    }
                }
                Schema schema = Schema.getSchemaByDataVersion((int)version.minecraftDataVersion());
                if (schema != null) {
                    str = version.minecraftDataVersion() - LitematicaSchematic.MINECRAFT_DATA_VERSION > 100 ? StringUtils.translate((String)"litematica.gui.label.schematic_info.schema.newer", (Object[])new Object[]{schema.getString(), version.minecraftDataVersion()}) : StringUtils.translate((String)"litematica.gui.label.schematic_info.schema", (Object[])new Object[]{schema.getString(), version.minecraftDataVersion()});
                    this.drawString(drawContext, str, x, y, textColor);
                    y += 12;
                }
            }
            if ((pair = this.cachedPreviewImages.get(entry.getFullPath())) != null) {
                y += 12;
                int iconSize = ((class_1043)pair.getRight()).method_4525().method_4307();
                boolean needsScaling = height < this.infoHeight;
                RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                if (needsScaling) {
                    iconSize = height - y + this.posY - 6;
                }
                RenderUtils.drawOutlinedBox((int)(x + 4), (int)y, (int)iconSize, (int)iconSize, (int)-1610612736, (int)-6710887);
                drawContext.method_25290(class_1921::method_62277, (class_2960)pair.getLeft(), x + 4, y, 0.0f, 0.0f, iconSize, iconSize, iconSize, iconSize);
            }
        }
    }

    public void clearSchematicMetadataCache() {
        this.clearPreviewImages();
        this.cachedMetadata.clear();
        this.cachedPreviewImages.clear();
        this.cachedVersion.clear();
    }

    @Deprecated
    @Nullable
    protected SchematicMetadata getSchematicMetadata(WidgetFileBrowserBase.DirectoryEntry entry) {
        File file = new File(entry.getDirectory(), entry.getName());
        SchematicMetadata meta = this.cachedMetadata.get(file);
        if (meta == null && !this.cachedMetadata.containsKey(file)) {
            if (entry.getName().endsWith(".litematic") && (meta = LitematicaSchematic.readMetadataFromFile(entry.getDirectory(), entry.getName())) != null) {
                this.createPreviewImage(file, meta);
            }
            this.cachedMetadata.put(file, meta);
        }
        return meta;
    }

    @Nullable
    protected Pair<SchematicSchema, SchematicMetadata> getSchematicVersionAndMetadata(WidgetFileBrowserBase.DirectoryEntry entry) {
        Pair<SchematicSchema, SchematicMetadata> pair;
        File file = new File(entry.getDirectory(), entry.getName());
        SchematicMetadata meta = this.cachedMetadata.get(file);
        SchematicSchema version = this.cachedVersion.get(file);
        if (meta == null && !this.cachedMetadata.containsKey(file) && (pair = LitematicaSchematic.readMetadataAndVersionFromFile(entry.getDirectory(), entry.getName())) != null) {
            meta = (SchematicMetadata)pair.getRight();
            version = (SchematicSchema)pair.getLeft();
            if (entry.getName().endsWith(".litematic")) {
                this.createPreviewImage(file, meta);
            }
            this.cachedMetadata.put(file, meta);
            this.cachedVersion.put(file, version);
        }
        return Pair.of((Object)version, (Object)meta);
    }

    private void clearPreviewImages() {
        for (Pair<class_2960, class_1043> pair : this.cachedPreviewImages.values()) {
            this.mc.method_1531().method_4615((class_2960)pair.getLeft());
        }
    }

    private void createPreviewImage(File file, SchematicMetadata meta) {
        int size;
        int[] previewImageData = meta.getPreviewImagePixelData();
        if (previewImageData != null && previewImageData.length > 0 && (size = (int)Math.sqrt(previewImageData.length)) * size == previewImageData.length) {
            try {
                class_1011 image = new class_1011(size, size, false);
                class_1043 tex = new class_1043(image);
                class_2960 rl = class_2960.method_60655((String)"litematica", (String)DigestUtils.sha1Hex((String)file.getAbsolutePath()));
                this.mc.method_1531().method_4616(rl, (class_1044)tex);
                int i = 0;
                for (int y = 0; y < size; ++y) {
                    for (int x = 0; x < size; ++x) {
                        int val = previewImageData[i++];
                        image.method_61941(x, y, val);
                    }
                }
                tex.method_4524();
                this.cachedPreviewImages.put(file, (Pair<class_2960, class_1043>)Pair.of((Object)rl, (Object)tex));
            }
            catch (Exception e) {
                Litematica.LOGGER.warn("Failed to create a preview image", (Throwable)e);
            }
        }
    }

    public static class FileFilterSchematics
    implements FileFilter {
        @Override
        public boolean accept(File pathName) {
            String name = pathName.getName();
            return name.endsWith(".litematic") || name.endsWith(".schem") || name.endsWith(".schematic") || name.endsWith(".nbt");
        }
    }
}

