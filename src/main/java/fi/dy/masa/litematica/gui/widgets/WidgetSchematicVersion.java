/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase
 *  fi.dy.masa.malilib.render.RenderUtils
 *  fi.dy.masa.malilib.util.StringUtils
 *  net.minecraft.class_332
 */
package fi.dy.masa.litematica.gui.widgets;

import fi.dy.masa.litematica.schematic.projects.SchematicProject;
import fi.dy.masa.litematica.schematic.projects.SchematicVersion;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.ArrayList;
import net.minecraft.class_332;

public class WidgetSchematicVersion
extends WidgetListEntryBase<SchematicVersion> {
    private final SchematicProject project;
    private final boolean isOdd;

    public WidgetSchematicVersion(int x, int y, int width, int height, boolean isOdd, SchematicVersion entry, int listIndex, SchematicProject project) {
        super(x, y, width, height, (Object)entry, listIndex);
        this.project = project;
        this.isOdd = isOdd;
    }

    public void render(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        boolean versionSelected;
        RenderUtils.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        boolean bl = versionSelected = this.project.getCurrentVersion() == this.entry;
        if (selected || versionSelected || this.isMouseOver(mouseX, mouseY)) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1603243920);
        } else if (this.isOdd) {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1609560048);
        } else {
            RenderUtils.drawRect((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-1607454672);
        }
        if (versionSelected) {
            RenderUtils.drawOutline((int)this.x, (int)this.y, (int)this.width, (int)this.height, (int)-2039584);
        }
        String str = StringUtils.translate((String)"litematica.gui.label.schematic_projects.version_entry", (Object[])new Object[]{((SchematicVersion)this.entry).getVersion(), ((SchematicVersion)this.entry).getName()});
        this.drawString(this.x + 4, this.y + 4, -1, str, drawContext);
    }

    public void postRenderHovered(int mouseX, int mouseY, boolean selected, class_332 drawContext) {
        ArrayList text = new ArrayList();
        RenderUtils.drawHoverText((int)mouseX, (int)mouseY, text, (class_332)drawContext);
    }
}

