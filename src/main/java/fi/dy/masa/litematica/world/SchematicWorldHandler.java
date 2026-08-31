/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_1267
 *  net.minecraft.class_2874
 *  net.minecraft.class_310
 *  net.minecraft.class_5269
 *  net.minecraft.class_5455
 *  net.minecraft.class_5455$class_6890
 *  net.minecraft.class_638
 *  net.minecraft.class_638$class_5271
 *  net.minecraft.class_6880
 */
package fi.dy.masa.litematica.world;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.render.LitematicaRenderer;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.world.WorldSchematic;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.class_1267;
import net.minecraft.class_2874;
import net.minecraft.class_310;
import net.minecraft.class_5269;
import net.minecraft.class_5455;
import net.minecraft.class_638;
import net.minecraft.class_6880;

public class SchematicWorldHandler {
    public static final SchematicWorldHandler INSTANCE = new SchematicWorldHandler(LitematicaRenderer.getInstance()::getWorldRenderer);
    protected final Supplier<WorldRendererSchematic> rendererSupplier;
    @Nullable
    protected WorldSchematic world;
    @Nullable
    protected class_5455.class_6890 dynamicRegistryManager = class_5455.field_40585;

    public SchematicWorldHandler(Supplier<WorldRendererSchematic> rendererSupplier) {
        this.rendererSupplier = rendererSupplier;
    }

    @Nullable
    public static WorldSchematic getSchematicWorld() {
        return INSTANCE.getWorld();
    }

    @Nullable
    public WorldSchematic getWorld() {
        if (this.world == null) {
            this.world = SchematicWorldHandler.createSchematicWorld(this.rendererSupplier.get());
        }
        return this.world;
    }

    public void setDynamicRegistryManager(@Nullable class_5455.class_6890 immutable) {
        if (immutable == null) {
            return;
        }
        this.dynamicRegistryManager = immutable;
    }

    public class_5455 getRegistryManager() {
        return this.dynamicRegistryManager;
    }

    public static WorldSchematic createSchematicWorld(@Nullable WorldRendererSchematic worldRenderer) {
        class_638 world = class_310.method_1551().field_1687;
        if (world == null) {
            return null;
        }
        class_638.class_5271 levelInfo = new class_638.class_5271(class_1267.field_5801, false, true);
        return new WorldSchematic((class_5269)levelInfo, world.method_30349(), (class_6880<class_2874>)world.method_40134(), worldRenderer);
    }

    public void recreateSchematicWorld(boolean remove) {
        if (remove) {
            Litematica.debugLog("Removing the schematic world...", new Object[0]);
            this.world = null;
            LitematicaRenderer.getInstance().onSchematicWorldChanged(null);
        } else {
            Litematica.debugLog("(Re-)creating the schematic world...", new Object[0]);
            WorldRendererSchematic worldRenderer = this.world != null ? this.world.worldRenderer : LitematicaRenderer.getInstance().resetWorldRenderer();
            this.world = SchematicWorldHandler.createSchematicWorld(worldRenderer);
            Litematica.debugLog("Schematic world (re-)created: {}", new Object[]{this.world});
        }
        LitematicaRenderer.getInstance().onSchematicWorldChanged(this.world);
    }
}

