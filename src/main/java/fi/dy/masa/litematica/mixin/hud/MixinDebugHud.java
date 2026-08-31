/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  net.minecraft.class_340
 *  org.apache.commons.lang3.tuple.Pair
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package fi.dy.masa.litematica.mixin.hud;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.LitematicaRenderer;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.GuiBase;
import java.util.List;
import net.minecraft.class_340;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_340.class})
public abstract class MixinDebugHud {
    @Inject(method={"method_1835"}, at={@At(value="RETURN")})
    private void litematica_addDebugLines(CallbackInfoReturnable<List<String>> cir) {
        WorldSchematic world = SchematicWorldHandler.getSchematicWorld();
        if (world != null) {
            List list = (List)cir.getReturnValue();
            Pair<String, String> pair = EntityUtils.getEntityDebug();
            String pre = GuiBase.TXT_GOLD;
            String rst = GuiBase.TXT_RST;
            WorldRendererSchematic renderer = LitematicaRenderer.getInstance().getWorldRenderer();
            list.add(String.format("%s[Litematica]%s %s", pre, rst, renderer.getDebugInfoRenders()));
            String str = String.format("E: %d TE: %d C: %d, CT: %d, CV: %d", world.getRegularEntityCount(), world.getChunkProvider().getTileEntityCount(), world.getChunkProvider().method_14151(), DataManager.getSchematicPlacementManager().getTouchedChunksCount(), DataManager.getSchematicPlacementManager().getLastVisibleChunksCount());
            list.add(String.format("%s[Litematica]%s %s %s", pre, rst, renderer.getDebugInfoEntities(), str));
            if (!((String)pair.getLeft()).isEmpty()) {
                list.add(String.format("%s[%s]%s %s", pre, pair.getLeft(), rst, pair.getRight()));
            }
        }
    }
}

