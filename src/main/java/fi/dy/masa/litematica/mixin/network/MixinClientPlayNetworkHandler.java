/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2666
 *  net.minecraft.class_2672
 *  net.minecraft.class_2774
 *  net.minecraft.class_634
 *  net.minecraft.class_7439
 *  net.minecraft.class_8710
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package fi.dy.masa.litematica.mixin.network;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.util.SchematicWorldRefresher;
import net.minecraft.class_2666;
import net.minecraft.class_2672;
import net.minecraft.class_2774;
import net.minecraft.class_634;
import net.minecraft.class_7439;
import net.minecraft.class_8710;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_634.class})
public abstract class MixinClientPlayNetworkHandler {
    @Inject(method={"method_11128"}, at={@At(value="RETURN")})
    private void litematica_onUpdateChunk(class_2672 packet, CallbackInfo ci) {
        int chunkX = packet.method_11523();
        int chunkZ = packet.method_11524();
        if (Configs.Visuals.ENABLE_RENDERING.getBooleanValue() && Configs.Visuals.ENABLE_SCHEMATIC_RENDERING.getBooleanValue()) {
            SchematicWorldRefresher.INSTANCE.markSchematicChunksForRenderUpdate(chunkX, chunkZ);
        }
        DataManager.getSchematicPlacementManager().onClientChunkLoad(chunkX, chunkZ);
    }

    @Inject(method={"method_11107"}, at={@At(value="RETURN")})
    private void litematica_onChunkUnload(class_2666 packet, CallbackInfo ci) {
        if (!Configs.Generic.LOAD_ENTIRE_SCHEMATICS.getBooleanValue()) {
            DataManager.getSchematicPlacementManager().onClientChunkUnload(packet.comp_1726().field_9181, packet.comp_1726().field_9180);
        }
    }

    @Inject(method={"method_43596"}, cancellable=true, at={@At(value="INVOKE", target="Lnet/minecraft/class_7594;method_44736(Lnet/minecraft/class_2561;Z)V")})
    private void litematica_onGameMessage(class_7439 packet, CallbackInfo ci) {
        if (DataManager.onChatMessage(packet.comp_763())) {
            ci.cancel();
        }
    }

    @Inject(method={"method_11152"}, at={@At(value="HEAD")})
    private void litematica_onCustomPayload(class_8710 payload, CallbackInfo ci) {
        if (payload.method_56479().comp_2242().equals((Object)DataManager.CARPET_HELLO)) {
            Litematica.debugLog("MixinClientPlayNetworkHandler#litematica_onCustomPayload(): received carpet hello packet", new Object[0]);
            DataManager.setIsCarpetServer(true);
        } else if (payload.method_56479().comp_2242().method_12836().equals("servux")) {
            DataManager.setHasServuxServer(true);
        }
    }

    @Inject(method={"method_11127"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_300;method_1404(ILnet/minecraft/class_2487;)Z")})
    private void litematica_onQueryResponse(class_2774 packet, CallbackInfo ci) {
        if (Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            EntitiesDataStorage.getInstance().handleVanillaQueryNbt(packet.method_11910(), packet.method_11911());
        }
    }

    @Inject(method={"method_11145"}, at={@At(value="RETURN")})
    private void minihud_onCommandTree(CallbackInfo ci) {
        if (Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            EntitiesDataStorage.getInstance().resetOpCheck();
        }
    }
}

