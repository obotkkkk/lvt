/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 */
package fi.dy.masa.litematica.interfaces;

import fi.dy.masa.litematica.schematic.pickblock.SchematicPickBlockEventResult;
import java.util.function.Supplier;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2680;

public interface ISchematicPickBlockEventListener {
    public Supplier<String> getName();

    public void onSchematicPickBlockCancelled(Supplier<String> var1);

    public SchematicPickBlockEventResult onSchematicPickBlockStart(boolean var1);

    public SchematicPickBlockEventResult onSchematicPickBlockPreGather(class_1937 var1, class_2338 var2, class_2680 var3);

    public SchematicPickBlockEventResult onSchematicPickBlockPrePick(class_1937 var1, class_2338 var2, class_2680 var3, class_1799 var4);

    public void onSchematicPickBlockSuccess();
}

