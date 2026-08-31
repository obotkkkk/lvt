/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.button.ButtonBase
 *  fi.dy.masa.malilib.gui.button.ButtonGeneric
 *  fi.dy.masa.malilib.gui.button.ButtonOnOff
 *  fi.dy.masa.malilib.gui.button.IButtonActionListener
 *  fi.dy.masa.malilib.gui.interfaces.IMessageConsumer
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package fi.dy.masa.litematica.gui.widgets;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.gui.GuiSubRegionConfiguration;
import fi.dy.masa.litematica.gui.Icons;
import fi.dy.masa.litematica.gui.widgets.WidgetListPlacementSubRegions;
import fi.dy.masa.litematica.gui.widgets.WidgetSchematicPlacement;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.schematic.placement.SubRegionPlacement;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ButtonOnOff;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.interfaces.IMessageConsumer;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class WidgetPlacementSubRegion
extends WidgetListEntryBase<SubRegionPlacement> {
    private final SchematicPlacement schematicPlacement;
    private final WidgetListPlacementSubRegions parent;
    private final SubRegionPlacement placement;
    private final boolean isOdd;
    private int buttonsStartX;

    public WidgetPlacementSubRegion(int x, int y, int width, int height, boolean isOdd, SchematicPlacement schematicPlacement, SubRegionPlacement placement, int listIndex, WidgetListPlacementSubRegions parent) {
        super(x, y, width, height, (Object)placement, listIndex);
        this.parent = parent;
        this.schematicPlacement = schematicPlacement;
        this.placement = placement;
        this.isOdd = isOdd;
        int posX = x + width - 2;
        int posY = y + 1;
        posX = this.createButtonOnOff(posX, posY, this.placement.isEnabled(), WidgetSchematicPlacement.ButtonListener.ButtonType.TOGGLE_ENABLED);
        this.buttonsStartX = posX = this.createButtonGeneric(posX, posY, WidgetSchematicPlacement.ButtonListener.ButtonType.CONFIGURE);
    }

    private int createButtonGeneric(int xRight, int y, WidgetSchematicPlacement.ButtonListener.ButtonType type) {
        String label = StringUtils.translate((String)type.getTranslationKey(), (Object[])new Object[0]);
        int len = this.getStringWidth(label) + 10;
        this.addButton((ButtonBase)new ButtonGeneric(xRight -= len, y, len, 20, label, new String[0]), new ButtonListener(type, this));
        return xRight - 2;
    }

    private int createButtonOnOff(int xRight, int y, boolean isCurrentlyOn, WidgetSchematicPlacement.ButtonListener.ButtonType type) {
        ButtonOnOff button = new ButtonOnOff(xRight, y, -1, true, type.getTranslationKey(), isCurrentlyOn, new String[0]);
        this.addButton((ButtonBase)button, new ButtonListener(type, this));
        return xRight - button.getWidth() - 2;
    }

    public boolean canSelectAt(int mouseX, int mouseY, int mouseButton) {
        return mouseX < this.buttonsStartX && super.canSelectAt(mouseX, mouseY, mouseButton);
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        boolean placementSelected;
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        boolean bl = placementSelected = this.schematicPlacement.getSelectedSubRegionPlacement() == this.placement;
        if (selected || placementSelected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1603243920);
        } else if (this.isOdd) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1609560048);
        } else {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1607454672);
        }
        if (placementSelected) {
            RenderUtils.drawOutline((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-2039584);
        }
        String name = this.placement.getName();
        String pre = this.placement.isEnabled() ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;
        this.drawString(this.x + 20, this.y + 7, -1, pre + name, drawContext);
        Icons icon = this.schematicPlacement.getSchematic().getFile() != null ? Icons.SCHEMATIC_TYPE_FILE : Icons.SCHEMATIC_TYPE_MEMORY;
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.parent.bindTexture(Icons.TEXTURE);
        icon.renderAt(this.x + 2, this.y + 5, this.zLevel, false, false, drawContext);
        if (this.placement.isRegionPlacementModifiedFromDefault()) {
            icon = Icons.NOTICE_EXCLAMATION_11;
            icon.renderAt(this.buttonsStartX - icon.getWidth() - 2, this.y + 6, this.zLevel, false, false, drawContext);
        }
        super.render(mouseX, mouseY, placementSelected, drawContext);
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        String fileName;
        LitematicaSchematic schematic = this.schematicPlacement.getSchematic();
        File schematicFile = schematic.getFile();
        String string = fileName = schematicFile != null ? schematicFile.getName() : StringUtils.translate((String)"litematica.gui.label.schematic_placement.in_memory", (Object[])new Object[0]);
        if (this.placement.isRegionPlacementModifiedFromDefault() && GuiBase.isMouseOver((int)mouseX, (int)mouseY, (int)(this.x + this.buttonsStartX - 25), (int)(this.y + 6), (int)11, (int)11)) {
            String str = StringUtils.translate((String)"litematica.hud.schematic_placement.hover_info.placement_sub_region_modified", (Object[])new Object[0]);
            RenderUtils.drawHoverText((int)mouseX, (int)mouseY, (List)ImmutableList.of((Object)str), (class_332)drawContext);
        } else if (GuiBase.isMouseOver((int)mouseX, (int)mouseY, (int)this.x, (int)this.y, (int)(this.buttonsStartX - 14), (int)this.height)) {
            ArrayList<String> text = new ArrayList<String>();
            text.add(StringUtils.translate((String)"litematica.gui.label.schematic_placement.schematic_name", (Object[])new Object[]{schematic.getMetadata().getName()}));
            text.add(StringUtils.translate((String)"litematica.gui.label.schematic_placement.schematic_file", (Object[])new Object[]{fileName}));
            class_2338 o = this.placement.getPos();
            o = PositionUtils.getTransformedBlockPos(o, this.schematicPlacement.getMirror(), this.schematicPlacement.getRotation());
            o = o.method_10081((class_2382)this.schematicPlacement.getOrigin());
            String strOrigin = String.format("x: %d, y: %d, z: %d", o.method_10263(), o.method_10264(), o.method_10260());
            text.add(StringUtils.translate((String)"litematica.gui.label.schematic_placement.origin", (Object[])new Object[]{strOrigin}));
            class_2338 size = schematic.getAreaSize(this.placement.getName());
            if (size != null) {
                String strSize = String.format("%d x %d x %d", size.method_10263(), size.method_10264(), size.method_10260());
                text.add(StringUtils.translate((String)"litematica.gui.label.placement_sub.region_size", (Object[])new Object[]{strSize}));
            }
            RenderUtils.drawHoverText((int)mouseX, (int)mouseY, text, (class_332)drawContext);
        }
    }

    private static class ButtonListener
    implements IButtonActionListener {
        private final WidgetSchematicPlacement.ButtonListener.ButtonType type;
        private final WidgetPlacementSubRegion widget;

        public ButtonListener(WidgetSchematicPlacement.ButtonListener.ButtonType type, WidgetPlacementSubRegion widget) {
            this.type = type;
            this.widget = widget;
        }

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == WidgetSchematicPlacement.ButtonListener.ButtonType.CONFIGURE) {
                GuiSubRegionConfiguration gui = new GuiSubRegionConfiguration(this.widget.schematicPlacement, this.widget.placement);
                gui.setParent((class_437)this.widget.parent.getParentGui());
                GuiBase.openGui((class_437)gui);
            } else if (this.type == WidgetSchematicPlacement.ButtonListener.ButtonType.TOGGLE_ENABLED) {
                this.widget.schematicPlacement.toggleSubRegionEnabled(this.widget.placement.getName(), (IMessageConsumer)this.widget.parent.getParentGui());
                this.widget.parent.refreshEntries();
            }
        }
    }
}

