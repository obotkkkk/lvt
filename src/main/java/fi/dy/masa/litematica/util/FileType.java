/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  net.minecraft.class_3542
 *  net.minecraft.class_3542$class_7292
 */
package fi.dy.masa.litematica.util;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraft.class_3542;

public enum FileType implements class_3542
{
    INVALID,
    UNKNOWN,
    JSON,
    LITEMATICA_SCHEMATIC,
    SCHEMATICA_SCHEMATIC,
    SPONGE_SCHEMATIC,
    VANILLA_STRUCTURE;

    public static final class_3542.class_7292<FileType> CODEC;
    public static final ImmutableList<FileType> VALUES;

    public static FileType fromName(String fileName) {
        if (fileName.endsWith(".litematic")) {
            return LITEMATICA_SCHEMATIC;
        }
        if (fileName.endsWith(".schematic")) {
            return SCHEMATICA_SCHEMATIC;
        }
        if (fileName.endsWith(".nbt")) {
            return VANILLA_STRUCTURE;
        }
        if (fileName.endsWith(".schem")) {
            return SPONGE_SCHEMATIC;
        }
        if (fileName.endsWith(".json")) {
            return JSON;
        }
        return UNKNOWN;
    }

    @Deprecated
    public static FileType fromFile(File file) {
        if (file.isFile() && file.canRead()) {
            return FileType.fromName(file.getName());
        }
        return INVALID;
    }

    public static FileType fromFile(Path file) {
        if (Files.exists(file, new LinkOption[0]) && Files.isReadable(file)) {
            return FileType.fromName(file.getFileName().toString());
        }
        return INVALID;
    }

    public static String getFileExt(FileType type) {
        return switch (type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 3 -> ".litematic";
            case 4 -> ".schematic";
            case 5 -> ".schem";
            case 6 -> ".nbt";
            case 2 -> ".json";
            case 0 -> ".invalid";
            case 1 -> ".unknown";
        };
    }

    public static String getString(FileType type) {
        return switch (type.ordinal()) {
            default -> throw new MatchException(null, null);
            case 3 -> "litematic";
            case 4 -> "schematic";
            case 5 -> "sponge";
            case 6 -> "vanilla_nbt";
            case 2 -> "JSON";
            case 0 -> "invalid";
            case 1 -> "unknown";
        };
    }

    public String method_15434() {
        return FileType.getString(this);
    }

    static {
        CODEC = class_3542.method_28140(FileType::values);
        VALUES = ImmutableList.copyOf((Object[])FileType.values());
    }
}

