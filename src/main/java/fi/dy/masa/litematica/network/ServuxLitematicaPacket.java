/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.network.IClientPayloadData
 *  io.netty.buffer.Unpooled
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 *  net.minecraft.class_2487
 *  net.minecraft.class_2505
 *  net.minecraft.class_2520
 *  net.minecraft.class_2540
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9139
 */
package fi.dy.masa.litematica.network;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.network.ServuxLitematicaHandler;
import fi.dy.masa.malilib.network.IClientPayloadData;
import io.netty.buffer.Unpooled;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2487;
import net.minecraft.class_2505;
import net.minecraft.class_2520;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_9139;

public class ServuxLitematicaPacket
implements IClientPayloadData {
    private Type packetType;
    private int transactionId;
    private int entityId;
    private class_2338 pos;
    private class_2487 nbt;
    private class_1923 chunkPos;
    private class_2540 buffer;
    public static final int PROTOCOL_VERSION = 1;

    private ServuxLitematicaPacket(Type type) {
        this.packetType = type;
        this.transactionId = -1;
        this.entityId = -1;
        this.pos = class_2338.field_10980;
        this.chunkPos = class_1923.field_35107;
        this.nbt = new class_2487();
        this.clearPacket();
    }

    public static ServuxLitematicaPacket MetadataRequest(@Nullable class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_METADATA_REQUEST);
        if (nbt != null) {
            packet.nbt.method_10543(nbt);
        }
        return packet;
    }

    public static ServuxLitematicaPacket MetadataResponse(@Nullable class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_S2C_METADATA);
        if (nbt != null) {
            packet.nbt.method_10543(nbt);
        }
        return packet;
    }

    public static ServuxLitematicaPacket SimpleEntityResponse(int entityId, @Nullable class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_S2C_ENTITY_NBT_RESPONSE_SIMPLE);
        if (nbt != null) {
            packet.nbt.method_10543(nbt);
        }
        packet.entityId = entityId;
        return packet;
    }

    public static ServuxLitematicaPacket SimpleBlockResponse(class_2338 pos, @Nullable class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_S2C_BLOCK_NBT_RESPONSE_SIMPLE);
        if (nbt != null) {
            packet.nbt.method_10543(nbt);
        }
        packet.pos = pos.method_10062();
        return packet;
    }

    public static ServuxLitematicaPacket BlockEntityRequest(class_2338 pos) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_BLOCK_ENTITY_REQUEST);
        packet.pos = pos.method_10062();
        return packet;
    }

    public static ServuxLitematicaPacket EntityRequest(int entityId) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_ENTITY_REQUEST);
        packet.entityId = entityId;
        return packet;
    }

    public static ServuxLitematicaPacket BulkNbtRequest(class_1923 chunkPos, @Nullable class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_BULK_ENTITY_NBT_REQUEST);
        packet.chunkPos = chunkPos;
        if (nbt != null) {
            packet.nbt.method_10543(nbt);
        }
        return packet;
    }

    public static ServuxLitematicaPacket ResponseS2CStart(@Nonnull class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_S2C_NBT_RESPONSE_START);
        packet.nbt.method_10543(nbt);
        return packet;
    }

    public static ServuxLitematicaPacket ResponseS2CData(@Nonnull class_2540 buffer) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_S2C_NBT_RESPONSE_DATA);
        packet.buffer = new class_2540(buffer.copy());
        packet.nbt = new class_2487();
        return packet;
    }

    public static ServuxLitematicaPacket ResponseC2SStart(@Nonnull class_2487 nbt) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_NBT_RESPONSE_START);
        packet.nbt.method_10543(nbt);
        return packet;
    }

    public static ServuxLitematicaPacket ResponseC2SData(@Nonnull class_2540 buffer) {
        ServuxLitematicaPacket packet = new ServuxLitematicaPacket(Type.PACKET_C2S_NBT_RESPONSE_DATA);
        packet.buffer = new class_2540(buffer.copy());
        packet.nbt = new class_2487();
        return packet;
    }

    private void clearPacket() {
        if (this.buffer != null) {
            this.buffer.method_52931();
            this.buffer = new class_2540(Unpooled.buffer());
        }
    }

    public int getVersion() {
        return 1;
    }

    public int getPacketType() {
        return this.packetType.get();
    }

    public int getTotalSize() {
        int total = 2;
        if (this.nbt != null && !this.nbt.method_33133()) {
            total += this.nbt.method_47988();
        }
        if (this.buffer != null) {
            total += this.buffer.readableBytes();
        }
        return total;
    }

    public Type getType() {
        return this.packetType;
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public class_2487 getCompound() {
        return this.nbt;
    }

    public class_1923 getChunkPos() {
        return this.chunkPos;
    }

    public class_2540 getBuffer() {
        return this.buffer;
    }

    public boolean hasBuffer() {
        return this.buffer != null && this.buffer.isReadable();
    }

    public boolean hasNbt() {
        return this.nbt != null && !this.nbt.method_33133();
    }

    public boolean isEmpty() {
        return !this.hasBuffer() && !this.hasNbt();
    }

    public void toPacket(class_2540 output) {
        output.method_10804(this.packetType.get());
        switch (this.packetType.ordinal()) {
            case 2: {
                try {
                    output.method_10804(this.transactionId);
                    output.method_10807(this.pos);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing Block Entity Request to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 3: {
                try {
                    output.method_10804(this.transactionId);
                    output.method_10804(this.entityId);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing Entity Request to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 4: {
                try {
                    output.method_10807(this.pos);
                    output.method_10794((class_2520)this.nbt);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing Block Entity Response to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 5: {
                try {
                    output.method_10804(this.entityId);
                    output.method_10794((class_2520)this.nbt);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing Entity Response to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 6: {
                try {
                    output.method_36130(this.chunkPos);
                    output.method_10794((class_2520)this.nbt);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing Bulk Entity Request to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 8: 
            case 10: {
                try {
                    class_2540 serverReplay = new class_2540(this.buffer.copy());
                    output.method_52975(serverReplay.readBytes(serverReplay.readableBytes()));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing buffer data to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            case 0: 
            case 1: {
                try {
                    output.method_10794((class_2520)this.nbt);
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: error writing NBT to packet: [{}]", (Object)e.getLocalizedMessage());
                }
                break;
            }
            default: {
                Litematica.LOGGER.error("ServuxEntitiesPacket#toPacket: Unknown packet type!");
            }
        }
    }

    @Nullable
    public static ServuxLitematicaPacket fromPacket(class_2540 input) {
        int i = input.method_10816();
        Type type = ServuxLitematicaPacket.getType(i);
        if (type == null) {
            Litematica.LOGGER.warn("ServuxEntitiesPacket#fromPacket: invalid packet type received");
            return null;
        }
        switch (type.ordinal()) {
            case 2: {
                try {
                    input.method_10816();
                    return ServuxLitematicaPacket.BlockEntityRequest(input.method_10811());
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Block Entity Request from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 3: {
                try {
                    input.method_10816();
                    return ServuxLitematicaPacket.EntityRequest(input.method_10816());
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Entity Request from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 4: {
                try {
                    return ServuxLitematicaPacket.SimpleBlockResponse(input.method_10811(), (class_2487)input.method_30616(class_2505.method_53898()));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Block Entity Response from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 5: {
                try {
                    return ServuxLitematicaPacket.SimpleEntityResponse(input.method_10816(), (class_2487)input.method_30616(class_2505.method_53898()));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Entity Response from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 6: {
                try {
                    return ServuxLitematicaPacket.BulkNbtRequest(input.method_36133(), (class_2487)input.method_30616(class_2505.method_53898()));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Bulk Entity Request from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 8: {
                try {
                    return ServuxLitematicaPacket.ResponseS2CData(new class_2540(input.readBytes(input.readableBytes())));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading S2C Bulk Response Buffer from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 10: {
                try {
                    return ServuxLitematicaPacket.ResponseC2SData(new class_2540(input.readBytes(input.readableBytes())));
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading C2S Bulk Response Buffer from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 1: {
                try {
                    return ServuxLitematicaPacket.MetadataRequest(input.method_10798());
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Metadata Request from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            case 0: {
                try {
                    return ServuxLitematicaPacket.MetadataResponse(input.method_10798());
                }
                catch (Exception e) {
                    Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: error reading Metadata Response from packet: [{}]", (Object)e.getLocalizedMessage());
                    break;
                }
            }
            default: {
                Litematica.LOGGER.error("ServuxEntitiesPacket#fromPacket: Unknown packet type!");
            }
        }
        return null;
    }

    public void clear() {
        if (this.nbt != null && !this.nbt.method_33133()) {
            this.nbt = new class_2487();
        }
        this.clearPacket();
        this.transactionId = -1;
        this.entityId = -1;
        this.pos = class_2338.field_10980;
        this.packetType = null;
    }

    @Nullable
    public static Type getType(int input) {
        for (Type type : Type.values()) {
            if (type.get() != input) continue;
            return type;
        }
        return null;
    }

    public static enum Type {
        PACKET_S2C_METADATA(1),
        PACKET_C2S_METADATA_REQUEST(2),
        PACKET_C2S_BLOCK_ENTITY_REQUEST(3),
        PACKET_C2S_ENTITY_REQUEST(4),
        PACKET_S2C_BLOCK_NBT_RESPONSE_SIMPLE(5),
        PACKET_S2C_ENTITY_NBT_RESPONSE_SIMPLE(6),
        PACKET_C2S_BULK_ENTITY_NBT_REQUEST(7),
        PACKET_S2C_NBT_RESPONSE_START(10),
        PACKET_S2C_NBT_RESPONSE_DATA(11),
        PACKET_C2S_NBT_RESPONSE_START(12),
        PACKET_C2S_NBT_RESPONSE_DATA(13);

        private final int type;

        private Type(int type) {
            this.type = type;
        }

        int get() {
            return this.type;
        }
    }

    public record Payload(ServuxLitematicaPacket data) implements class_8710
    {
        public static final class_8710.class_9154<Payload> ID = new class_8710.class_9154(ServuxLitematicaHandler.CHANNEL_ID);
        public static final class_9139<class_2540, Payload> CODEC = class_8710.method_56484(Payload::write, Payload::new);

        public Payload(class_2540 input) {
            this(ServuxLitematicaPacket.fromPacket(input));
        }

        private void write(class_2540 output) {
            this.data.toPacket(output);
        }

        public class_8710.class_9154<? extends class_8710> method_56479() {
            return ID;
        }
    }
}

