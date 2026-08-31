/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 */
package fi.dy.masa.litematica.schematic;

import javax.annotation.Nonnull;

public record SchematicSchema(int litematicVersion, int minecraftDataVersion) {
    @Override
    @Nonnull
    public String toString() {
        return "V" + this.litematicVersion() + " / DataVersion " + this.minecraftDataVersion();
    }
}

