/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_290
 *  net.minecraft.class_293
 *  net.minecraft.class_293$class_5596
 */
package fi.dy.masa.litematica.render.schematic;

import net.minecraft.class_290;
import net.minecraft.class_293;

public enum OverlayRenderType {
    OUTLINE(class_293.class_5596.field_29344, 786432, class_290.field_1576, false, false),
    QUAD(class_293.class_5596.field_27382, 786432, class_290.field_1576, false, true);

    private final class_293.class_5596 drawMode;
    private final class_293 vertexFormat;
    private final int bufferSize;
    private final boolean hasCrumbling;
    private final boolean translucent;

    private OverlayRenderType(class_293.class_5596 drawMode, int bufferSize, class_293 format, boolean crumbling, boolean translucent) {
        this.drawMode = drawMode;
        this.bufferSize = Math.max(bufferSize, format.method_1362());
        this.vertexFormat = format;
        this.hasCrumbling = crumbling;
        this.translucent = translucent;
    }

    public class_293.class_5596 getDrawMode() {
        return this.drawMode;
    }

    public int getExpectedBufferSize() {
        return this.bufferSize;
    }

    public class_293 getVertexFormat() {
        return this.vertexFormat;
    }

    public boolean hasCrumbling() {
        return this.hasCrumbling;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }
}

