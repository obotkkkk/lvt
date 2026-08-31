/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.network.IClientPayloadData
 *  fi.dy.masa.malilib.network.IPluginClientPlayHandler
 *  fi.dy.masa.malilib.network.PacketSplitter
 *  io.netty.buffer.Unpooled
 *  javax.annotation.Nullable
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking$Context
 *  net.minecraft.class_156
 *  net.minecraft.class_2487
 *  net.minecraft.class_2505
 *  net.minecraft.class_2520
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_5819
 *  net.minecraft.class_634
 *  net.minecraft.class_8710
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.network;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.data.EntitiesDataStorage;
import fi.dy.masa.litematica.network.ServuxLitematicaPacket;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.malilib.network.IClientPayloadData;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malilib.network.PacketSplitter;
import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.class_156;
import net.minecraft.class_2487;
import net.minecraft.class_2505;
import net.minecraft.class_2520;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5819;
import net.minecraft.class_634;
import net.minecraft.class_8710;
import org.apache.commons.lang3.tuple.Pair;

@Environment(value=EnvType.CLIENT)
public abstract class ServuxLitematicaHandler<T extends class_8710>
implements IPluginClientPlayHandler<T> {
    private static final ServuxLitematicaHandler<ServuxLitematicaPacket.Payload> INSTANCE = new ServuxLitematicaHandler<ServuxLitematicaPacket.Payload>(){

        public void receive(ServuxLitematicaPacket.Payload payload, ClientPlayNetworking.Context context) {
            INSTANCE.receivePlayPayload(payload, context);
        }
    };
    public static final class_2960 CHANNEL_ID = class_2960.method_60655((String)"servux", (String)"litematics");
    private boolean servuxRegistered;
    private boolean payloadRegistered = false;
    private int failures = 0;
    private static final int MAX_FAILURES = 4;
    private long readingSessionKey = -1L;

    public static ServuxLitematicaHandler<ServuxLitematicaPacket.Payload> getInstance() {
        return INSTANCE;
    }

    public class_2960 getPayloadChannel() {
        return CHANNEL_ID;
    }

    public boolean isPlayRegistered(class_2960 channel) {
        if (channel.equals((Object)CHANNEL_ID)) {
            return this.payloadRegistered;
        }
        return false;
    }

    public void setPlayRegistered(class_2960 channel) {
        if (channel.equals((Object)CHANNEL_ID)) {
            this.payloadRegistered = true;
        }
    }

    public <P extends IClientPayloadData> void decodeClientData(class_2960 channel, P data) {
        ServuxLitematicaPacket packet = (ServuxLitematicaPacket)data;
        if (!channel.equals((Object)CHANNEL_ID)) {
            return;
        }
        switch (packet.getType()) {
            case PACKET_S2C_METADATA: {
                if (!EntitiesDataStorage.getInstance().receiveServuxMetadata(packet.getCompound())) break;
                this.servuxRegistered = true;
                break;
            }
            case PACKET_S2C_BLOCK_NBT_RESPONSE_SIMPLE: {
                EntitiesDataStorage.getInstance().handleBlockEntityData(packet.getPos(), packet.getCompound(), null);
                break;
            }
            case PACKET_S2C_ENTITY_NBT_RESPONSE_SIMPLE: {
                EntitiesDataStorage.getInstance().handleEntityData(packet.getEntityId(), packet.getCompound());
                break;
            }
            case PACKET_S2C_NBT_RESPONSE_DATA: {
                class_2540 fullPacket;
                if (this.readingSessionKey == -1L) {
                    this.readingSessionKey = class_5819.method_43049((long)class_156.method_658()).method_43055();
                }
                if ((fullPacket = PacketSplitter.receive((IPluginClientPlayHandler)this, (long)this.readingSessionKey, (class_2540)packet.getBuffer())) == null) break;
                try {
                    this.readingSessionKey = -1L;
                    this.handleBulkData(fullPacket.method_10816(), (class_2487)fullPacket.method_30616(class_2505.method_53898()));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxLitematicaHandler#decodeClientData(): Entity Data: error reading fullBuffer [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            default: {
                Litematica.LOGGER.warn("ServuxLitematicaHandler#decodeClientData(): received unhandled packetType {} of size {} bytes.", (Object)packet.getPacketType(), (Object)packet.getTotalSize());
            }
        }
    }

    private void handleBulkData(int type, @Nullable class_2487 nbt) {
        String task;
        if (nbt == null || nbt.method_33133()) {
            return;
        }
        switch (task = nbt.method_10558("Task")) {
            case "Litematic-TransmitStart": 
            case "Litematic-TransmitCancel": 
            case "Litematic-TransmitData": 
            case "Litematic-TransmitEnd": {
                Pair<LitematicaSchematic, class_2487> schemPair = LitematicaSchematic.receiveFileTransmit(nbt);
                if (schemPair == null || ((LitematicaSchematic)schemPair.getLeft()).getFile() == null) break;
                Litematica.LOGGER.info("handleBulkData(): Received litematic '{}' from the server", (Object)((LitematicaSchematic)schemPair.getLeft()).getFile().getAbsolutePath());
                SchematicPlacement placement = SchematicPlacement.createFromNbt((LitematicaSchematic)schemPair.getLeft(), (class_2487)schemPair.getRight());
                if (placement == null) break;
                DataManager.getSchematicPlacementManager().addSchematicPlacement(placement, true);
                break;
            }
            default: {
                EntitiesDataStorage.getInstance().handleBulkEntityData(type, nbt);
            }
        }
    }

    public void reset(class_2960 channel) {
        if (channel.equals((Object)CHANNEL_ID) && this.servuxRegistered) {
            this.servuxRegistered = false;
            this.failures = 0;
            this.readingSessionKey = -1L;
        }
    }

    public void resetFailures(class_2960 channel) {
        if (channel.equals((Object)CHANNEL_ID) && this.failures > 0) {
            this.failures = 0;
        }
    }

    public void receivePlayPayload(T payload, ClientPlayNetworking.Context ctx) {
        if (payload.method_56479().comp_2242().equals((Object)CHANNEL_ID)) {
            INSTANCE.decodeClientData(CHANNEL_ID, ((ServuxLitematicaPacket.Payload)payload).data());
        }
    }

    public void encodeWithSplitter(class_2540 buffer, class_634 handler) {
        INSTANCE.sendPlayPayload(new ServuxLitematicaPacket.Payload(ServuxLitematicaPacket.ResponseC2SData(buffer)));
    }

    public <P extends IClientPayloadData> void encodeClientData(P data) {
        ServuxLitematicaPacket packet = (ServuxLitematicaPacket)data;
        if (packet.getType().equals((Object)ServuxLitematicaPacket.Type.PACKET_C2S_NBT_RESPONSE_START)) {
            class_2540 buffer = new class_2540(Unpooled.buffer());
            buffer.method_10804(packet.getTransactionId());
            buffer.method_10794((class_2520)packet.getCompound());
            PacketSplitter.send((IPluginClientPlayHandler)this, (class_2540)buffer, (class_634)class_310.method_1551().method_1562());
        } else if (!INSTANCE.sendPlayPayload(new ServuxLitematicaPacket.Payload(packet))) {
            if (this.failures > 4) {
                Litematica.LOGGER.warn("encodeClientData(): encountered [{}] sendPayload failures, cancelling any Servux join attempt(s)", (Object)4);
                this.servuxRegistered = false;
                INSTANCE.unregisterPlayReceiver();
                EntitiesDataStorage.getInstance().onPacketFailure();
            } else {
                ++this.failures;
            }
        }
    }
}

