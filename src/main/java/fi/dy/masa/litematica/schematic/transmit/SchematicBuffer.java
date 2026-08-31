/*
 * Decompiled with CFR 0.152.
 */
package fi.dy.masa.litematica.schematic.transmit;

import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.util.FileType;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;

public class SchematicBuffer
implements AutoCloseable {
    public static final int BUFFER_SIZE = 16384;
    private final String name;
    private final FileType type;
    private final HashMap<Integer, Slice> buffer;

    public SchematicBuffer(String name) {
        this(name, FileType.LITEMATICA_SCHEMATIC);
    }

    public SchematicBuffer(String name, FileType type) {
        this.name = name;
        this.type = type;
        this.buffer = new HashMap();
    }

    public String getName() {
        return this.name;
    }

    public FileType getType() {
        return this.type;
    }

    public Path getFileName() {
        String ext = FileType.getFileExt(this.type);
        if (this.name.contains(ext)) {
            return Path.of(this.name, new String[0]);
        }
        return Path.of(this.name + ext, new String[0]);
    }

    public void receiveSlice(int number, Slice slice) {
        this.buffer.put(number, slice);
    }

    public Path writeFile(Path dir) {
        Path file;
        if (!Files.isDirectory(dir, new LinkOption[0])) {
            try {
                Files.createDirectory(dir, new FileAttribute[0]);
            }
            catch (IOException err) {
                Litematica.LOGGER.error("LitematicBuffer#writeFile(): Exception creating directory '{}'; {}", (Object)dir.toAbsolutePath().toString(), (Object)err.getLocalizedMessage());
                return null;
            }
        }
        if (Files.exists(file = dir.resolve(this.getFileName()), new LinkOption[0])) {
            try {
                Files.delete(file);
            }
            catch (IOException err) {
                Litematica.LOGGER.error("LitematicBuffer#writeFile(): Exception deleting file '{}'; {}", (Object)file.toAbsolutePath().toString(), (Object)err.getLocalizedMessage());
                return null;
            }
        }
        try (OutputStream os = Files.newOutputStream(file, new OpenOption[0]);){
            for (int i = 0; i < this.buffer.size(); ++i) {
                Slice entry = this.buffer.get(i);
                if (entry == null) continue;
                os.write(entry.data(), 0, entry.size());
            }
        }
        catch (Exception err) {
            Litematica.LOGGER.error("LitematicBuffer#writeFile(): Exception saving file '{}'; {}", (Object)file.toAbsolutePath().toString(), (Object)err.getLocalizedMessage());
            return null;
        }
        Litematica.debugLog("LitematicBuffer#writeFile(): Saved file '{}' successfully", file.toAbsolutePath().toString());
        this.buffer.clear();
        return file;
    }

    @Override
    public void close() throws Exception {
        this.buffer.clear();
    }

    public record Slice(byte[] data, int size) {
    }
}

