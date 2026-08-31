/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.mojang.blaze3d.systems.RenderSystem
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.GuiUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui.widgets;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.SchematicHolder;
import fi.dy.masa.litematica.gui.GuiSchematicSave;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetListLoadedSchematics;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacementManager;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetFileBrowserBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class WidgetSchematicEntry
extends WidgetListEntryBase<LitematicaSchematic> {
    private final WidgetListLoadedSchematics parent;
    private final LitematicaSchematic schematic;
    private final int typeIconX;
    private final int typeIconY;
    private final boolean isOdd;
    private final int buttonsStartX;

    public WidgetSchematicEntry(int x, int y, int width, int height, boolean isOdd, LitematicaSchematic schematic, int listIndex, WidgetListLoadedSchematics parent) {
        super(x, y, width, height, (Object)schematic, listIndex);
        this.parent = parent;
        this.schematic = schematic;
        this.isOdd = isOdd;
        int posX = x + width;
        posX -= this.addButton(posX, ++y, ButtonListener.Type.UNLOAD);
        posX -= this.addButton(posX, y, ButtonListener.Type.RELOAD);
        posX -= this.addButton(posX, y, ButtonListener.Type.SAVE_TO_FILE);
        posX -= this.addButton(posX, y, ButtonListener.Type.CREATE_PLACEMENT);
        this.buttonsStartX = posX;
        this.typeIconX = this.x + 2;
        this.typeIconY = y + 4;
    }

    private int addButton(int x, int y, ButtonListener.Type type) {
        ButtonListener listener = new ButtonListener(type, this);
        ButtonGeneric button = new ButtonGeneric(x, y, -1, true, type.getDisplayName(), new Object[0]);
        if (type.getHoverKey() != null) {
            button.setHoverStrings(new String[]{type.getHoverKey()});
        }
        this.addButton((ButtonBase)button, listener);
        return button.getWidth() + 2;
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (selected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)0x70FFFFFF);
        } else if (this.isOdd) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)0x20FFFFFF);
        } else {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)0x50FFFFFF);
        }
        boolean modified = this.schematic.getMetadata().wasModifiedSinceSaved();
        String schematicName = this.schematic.getMetadata().getName();
        int color = modified ? -28656 : -1;
        this.drawString(this.x + 20, this.y + 7, color, schematicName, drawContext);
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        RenderSystem.disableBlend();
        File schematicFile = this.schematic.getFile();
        String fileName = schematicFile != null ? schematicFile.getName() : null;
        this.parent.bindTexture(Icons.TEXTURE);
        Icons icon = fileName != null ? Icons.SCHEMATIC_TYPE_FILE : Icons.SCHEMATIC_TYPE_MEMORY;
        icon.renderAt(this.typeIconX, this.typeIconY, this.zLevel, false, false, drawContext);
        if (modified) {
            Icons.NOTICE_EXCLAMATION_11.renderAt(this.buttonsStartX - 13, this.y + 6, this.zLevel, false, false, drawContext);
        }
        this.drawSubWidgets(mouseX, mouseY, drawContext);
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.schematic.getMetadata().wasModifiedSinceSaved() && GuiBase.isMouseOver((int)mouseX, (int)mouseY, (int)(this.buttonsStartX - 13), (int)(this.y + 6), (int)11, (int)11)) {
            String str = WidgetFileBrowserBase.DATE_FORMAT.format(new Date(this.schematic.getMetadata().getTimeModified()));
            ImmutableList strs = ImmutableList.of((Object)StringUtils.translate((String)"litematica.gui.label.loaded_schematic.modified_on", (Object[])new Object[]{str}));
            RenderUtils.drawHoverText((int)mouseX, (int)mouseY, (List)strs, (class_332)drawContext);
        } else if (GuiBase.isMouseOver((int)mouseX, (int)mouseY, (int)this.x, (int)this.y, (int)(this.buttonsStartX - 12), (int)this.height)) {
            String fileName;
            ArrayList<String> lines = new ArrayList<String>();
            File schematicFile = this.schematic.getFile();
            String string = fileName = schematicFile != null ? schematicFile.getName() : null;
            if (fileName != null) {
                lines.add(fileName);
            } else {
                lines.add(StringUtils.translate((String)"litematica.gui.label.schematic_placement.in_memory", (Object[])new Object[0]));
            }
            RenderUtils.drawHoverText((int)mouseX, (int)mouseY, lines, (class_332)drawContext);
        }
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        super.postRenderHovered(mouseX, mouseY, selected, drawContext);
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final Type type;
        private final WidgetSchematicEntry widget;

        public ButtonListener(Type type, WidgetSchematicEntry widget) {
            this.type = type;
            this.widget = widget;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == Type.CREATE_PLACEMENT) {
                class_2338 pos = class_2338.method_49638((class_2374)((WidgetSchematicEntry)this.widget).mc.field_1724.method_19538());
                LitematicaSchematic entry = this.widget.schematic;
                String name = entry.getMetadata().getName();
                boolean enabled = !GuiBase.isShiftDown();
                SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
                SchematicPlacement placement = SchematicPlacement.createFor(entry, pos, name, enabled, enabled);
                manager.addSchematicPlacement(placement, true);
                manager.setSelectedSchematicPlacement(placement);
            } else if (this.type == Type.SAVE_TO_FILE) {
                LitematicaSchematic entry = this.widget.schematic;
                GuiSchematicSave gui = new GuiSchematicSave(entry);
                gui.setParent(GuiUtils.getCurrentScreen());
                GuiBase.openGui((class_437)gui);
            } else if (this.type == Type.RELOAD) {
                this.widget.schematic.readFromFile();
                SchematicPlacementManager manager = DataManager.getSchematicPlacementManager();
                manager.getAllPlacementsOfSchematic(this.widget.schematic).forEach(manager::markChunksForRebuild);
            } else if (this.type == Type.UNLOAD) {
                SchematicHolder.getInstance().removeSchematic(this.widget.schematic);
                this.widget.parent.refreshEntries();
            }
        }

        public static enum Type {
            CREATE_PLACEMENT("litematica.gui.button.create_placement"),
            RELOAD("litematica.gui.button.reload", "litematica.gui.button.hover.schematic_list.reload_schematic"),
            SAVE_TO_FILE("litematica.gui.button.save_to_file"),
            UNLOAD("litematica.gui.button.unload");

            private final String translationKey;
            @Nullable
            private final String hoverKey;

            private Type(String translationKey) {
                this(translationKey, null);
            }

            private Type(String translationKey, String hoverKey) {
                this.translationKey = translationKey;
                this.hoverKey = hoverKey;
            }

            @Nullable
            public String getHoverKey() {
                return this.hoverKey;
            }

            public String getDisplayName() {
                return StringUtils.translate((String)this.translationKey, (Object[])new Object[0]);
            }
        }
    }
}

