/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2248
 *  net.minecraft.class_6862
 */
package fi.dy.masa.litematica.util;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.util.BlockUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_2248;
import net.minecraft.class_6862;

public class IgnoreBlockRegistry {
    private final List<class_2248> blocks = new ArrayList<class_2248>();
    private final List<class_6862<class_2248>> blockTags = new ArrayList<class_6862<class_2248>>();

    public boolean hasBlock(class_2248 block) {
        if (this.blocks.contains(block)) {
            return true;
        }
        for (class_6862<class_2248> tag : this.blockTags) {
            if (!block.method_9564().method_26164(tag)) continue;
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.blocks.isEmpty() && this.blockTags.isEmpty();
    }

    public IgnoreBlockRegistry() {
        if (Configs.Visuals.IGNORE_EXISTING_BLOCKS.getBooleanValue()) {
            for (String value : Configs.Visuals.IGNORABLE_EXISTING_BLOCKS.getStrings()) {
                String trimmed = value.trim();
                if (trimmed.startsWith("#")) {
                    Optional<class_6862<class_2248>> tag = BlockUtils.getBlockTagFromString(trimmed);
                    tag.ifPresent(this.blockTags::add);
                    continue;
                }
                Optional<class_2248> block = BlockUtils.getBlockFromString(trimmed);
                block.ifPresent(this.blocks::add);
            }
        }
    }
}

