/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_329
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package fi.dy.masa.litematica.mixin;

import net.minecraft.class_2561;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_329.class})
public interface InGameHudAccessor {
    @Accessor(value="field_2018")
    public class_2561 getOverlayMessage();

    @Accessor(value="field_2016")
    public class_2561 getTitle();

    @Accessor(value="field_2039")
    public class_2561 getSubtitle();
}

