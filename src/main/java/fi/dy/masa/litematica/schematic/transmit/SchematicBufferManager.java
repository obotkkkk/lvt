/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.class_2487
 */
package fi.dy.masa.litematica.schematic.transmit;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.transmit.SchematicBuffer;
import fi.dy.masa.litematica.util.FileType;
import java.nio.file.Path;
import java.util.HashMap;
import javax.annotation.Nullable;
import net.minecraft.class_2487;

public class SchematicBufferManager {
    private final HashMap<Long, SchematicBuffer> fileBuffers = new HashMap();
    private final HashMap<Long, class_2487> optionalNbt = new HashMap();

    public void createBuffer(String name, long sessionKey) {
        this.createBuffer(name, FileType.LITEMATICA_SCHEMATIC, sessionKey, null);
    }

    public void createBuffer(String name, long sessionKey, @Nullable class_2487 optional) {
        this.createBuffer(name, FileType.LITEMATICA_SCHEMATIC, sessionKey, optional);
    }

    public void createBuffer(String name, FileType type, long sessionKey, @Nullable class_2487 optional) {
        if (this.fileBuffers.containsKey(sessionKey) || this.optionalNbt.containsKey(sessionKey)) {
            Litematica.LOGGER.warn("createBuffer: Cannot create a new buffer for an existing session key!");
            return;
        }
        SchematicBuffer newBuf = new SchematicBuffer(name, type);
        this.fileBuffers.put(sessionKey, newBuf);
        if (optional != null && !optional.method_33133()) {
            this.optionalNbt.put(sessionKey, optional.method_10553());
        }
    }

    @Nullable
    private SchematicBuffer getBuffer(long sessionKey) {
        if (this.fileBuffers.containsKey(sessionKey)) {
            return this.fileBuffers.get(sessionKey);
        }
        return null;
    }

    public class_2487 getOptionalNbt(long sessionKey) {
        if (this.optionalNbt.containsKey(sessionKey)) {
            return this.optionalNbt.get(sessionKey);
        }
        return new class_2487();
    }

    public void receiveSlice(long sessionKey, int slice, byte[] dataIn, int size) {
        if (this.fileBuffers.containsKey(sessionKey)) {
            this.fileBuffers.get(sessionKey).receiveSlice(slice, new SchematicBuffer.Slice(dataIn, size));
        } else {
            Litematica.LOGGER.error("receiveSlice: Error; cannot receive a slice for a non-existing session");
        }
    }

    public void cancelBuffer(long sessionKey) {
        if (this.fileBuffers.containsKey(sessionKey)) {
            try (SchematicBuffer buffer = this.fileBuffers.remove(sessionKey);){
                buffer.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this.optionalNbt.remove(sessionKey);
    }

    @Nullable
    public LitematicaSchematic finishBuffer(long sessionKey, @Nullable Path dir) {
        if (this.fileBuffers.containsKey(sessionKey)) {
            Path file;
            SchematicBuffer buffer = this.fileBuffers.get(sessionKey);
            if (dir == null) {
                dir = DataManager.getSchematicTransmitDirectory();
            }
            if ((file = buffer.writeFile(dir)) == null) {
                Litematica.LOGGER.error("finishBuffer: Failed writing Schematic Buffer to file: '{}'", (Object)buffer.getFileName());
                return null;
            }
            LitematicaSchematic schematic = LitematicaSchematic.createFromFile(dir, buffer.getName(), buffer.getType());
            this.cancelBuffer(sessionKey);
            return schematic;
        }
        return null;
    }
}

