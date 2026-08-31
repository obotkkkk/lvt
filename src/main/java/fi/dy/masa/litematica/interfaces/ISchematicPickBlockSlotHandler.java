/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 */
package fi.dy.masa.litematica.interfaces;

import fi.dy.masa.litematica.schematic.pickblock.SchematicPickBlockEventResult;
import java.util.function.Supplier;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;

public interface ISchematicPickBlockSlotHandler {
    public Supplier<String> getName();

    public SchematicPickBlockEventResult executePickBlock(class_1937 var1, class_2338 var2, class_1799 var3);
}

