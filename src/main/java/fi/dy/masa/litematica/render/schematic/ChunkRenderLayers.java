/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1921
 */
package fi.dy.masa.litematica.render.schematic;

import fi.dy.masa.litematica.render.schematic.OverlayRenderType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_1921;

public record ChunkRenderLayers() {
    public static final List<class_1921> LAYERS = ChunkRenderLayers.getLayers();
    public static final List<OverlayRenderType> TYPES = ChunkRenderLayers.getTypes();

    private static List<class_1921> getLayers() {
        ArrayList<class_1921> list = new ArrayList<class_1921>(class_1921.method_22720());
        list.add(class_1921.method_23589());
        return list;
    }

    private static List<OverlayRenderType> getTypes() {
        return Arrays.stream(OverlayRenderType.values()).toList();
    }

    public static String getFriendlyName(class_1921 layer) {
        String base = layer.toString();
        if (base.contains(":")) {
            String[] results1 = base.split(":", 2);
            if (results1[0].contains("[")) {
                String[] results2 = results1[0].split("\\[");
                return layer.method_23033().name() + "/" + results2[1];
            }
            return layer.method_23033().name() + "/" + results1[0];
        }
        return layer.method_23033().name() + "/" + base;
    }
}

