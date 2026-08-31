/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 */
package fi.dy.masa.litematica.interfaces;

import fi.dy.masa.litematica.interfaces.ISchematicPickBlockEventListener;
import fi.dy.masa.litematica.interfaces.ISchematicPickBlockSlotHandler;
import fi.dy.masa.litematica.schematic.pickblock.SchematicPickBlockEventResult;
import net.minecraft.class_1799;

public interface ISchematicPickBlockEventManager {
    public void registerSchematicPickBlockEventListener(ISchematicPickBlockEventListener var1);

    public SchematicPickBlockEventResult invokeRedirectPickBlockStack(ISchematicPickBlockEventListener var1, class_1799 var2);

    public SchematicPickBlockEventResult invokeRedirectPickBlockSlotHandler(ISchematicPickBlockEventListener var1, ISchematicPickBlockSlotHandler var2);
}

