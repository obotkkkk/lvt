/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IWorldLoadListener
 *  javax.annotation.Nullable
 *  net.minecraft.class_310
 *  net.minecraft.class_3580
 *  net.minecraft.class_5455$class_6890
 *  net.minecraft.class_638
 */
package fi.dy.masa.litematica.event;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.compat.jade.JadeCompat;
import fi.dy.masa.litematica.data.CachedTagManager;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.schematic.conversion.SchematicConversionMaps;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.interfaces.IWorldLoadListener;
import javax.annotation.Nullable;
import net.minecraft.class_310;
import net.minecraft.class_3580;
import net.minecraft.class_5455;
import net.minecraft.class_638;

public class WorldLoadListener
implements IWorldLoadListener {
    public void onWorldLoadImmutable(class_5455.class_6890 immutable) {
        SchematicWorldHandler.INSTANCE.setDynamicRegistryManager(immutable);
    }

    public void onWorldLoadPre(@Nullable class_638 worldBefore, @Nullable class_638 worldAfter, class_310 mc) {
        if (worldBefore != null) {
            DataManager.save();
        }
        if (worldAfter != null) {
            JadeCompat.checkForJade();
            EntitiesDataStorage.getInstance().onWorldPre();
            DataManager.getInstance().onWorldPre(worldAfter.method_30349());
        }
    }

    public void onWorldLoadPost(@Nullable class_638 worldBefore, @Nullable class_638 worldAfter, class_310 mc) {
        SchematicWorldHandler.INSTANCE.recreateSchematicWorld(worldAfter == null);
        DataManager.getInstance().reset(worldAfter == null);
        EntitiesDataStorage.getInstance().reset(worldAfter == null);
        if (worldAfter != null) {
            DataManager.load();
            Litematica.debugLog("onWorldLoadPost(): Init BlockStateFlattening DataFixer [Test: {}]", class_3580.method_15600((String)"minecraft:air"));
            SchematicConversionMaps.computeMaps();
            EntitiesDataStorage.getInstance().onWorldJoin();
            CachedTagManager.startCache();
        } else {
            DataManager.clear();
        }
    }
}

