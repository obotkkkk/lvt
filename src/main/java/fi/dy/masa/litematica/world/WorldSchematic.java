/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  fi.dy.masa.malilib.util.WorldUtils
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_10286
 *  net.minecraft.class_1282
 *  net.minecraft.class_1297
 *  net.minecraft.class_1508
 *  net.minecraft.class_1657
 *  net.minecraft.class_1845
 *  net.minecraft.class_1937
 *  net.minecraft.class_1937$class_7867
 *  net.minecraft.class_1959
 *  net.minecraft.class_22
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2394
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_269
 *  net.minecraft.class_2791
 *  net.minecraft.class_2806
 *  net.minecraft.class_2818
 *  net.minecraft.class_2874
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_3532
 *  net.minecraft.class_3568
 *  net.minecraft.class_3611
 *  net.minecraft.class_5269
 *  net.minecraft.class_5294
 *  net.minecraft.class_5294$class_5297
 *  net.minecraft.class_5321
 *  net.minecraft.class_5362
 *  net.minecraft.class_5455
 *  net.minecraft.class_5575
 *  net.minecraft.class_5577
 *  net.minecraft.class_5712
 *  net.minecraft.class_5712$class_7397
 *  net.minecraft.class_6754
 *  net.minecraft.class_6756
 *  net.minecraft.class_6880
 *  net.minecraft.class_7134
 *  net.minecraft.class_7699
 *  net.minecraft.class_7924
 *  net.minecraft.class_8921
 *  net.minecraft.class_9209
 *  net.minecraft.class_9895
 *  org.jetbrains.annotations.Nullable
 */
package fi.dy.masa.litematica.world;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.litematica.render.schematic.WorldRendererSchematic;
import fi.dy.masa.litematica.world.ChunkManagerSchematic;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.litematica.world.SchematicWorldHandler;
import fi.dy.masa.malilib.util.WorldUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.class_10286;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1508;
import net.minecraft.class_1657;
import net.minecraft.class_1845;
import net.minecraft.class_1937;
import net.minecraft.class_1959;
import net.minecraft.class_22;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2394;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_269;
import net.minecraft.class_2791;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_2874;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_3532;
import net.minecraft.class_3568;
import net.minecraft.class_3611;
import net.minecraft.class_5269;
import net.minecraft.class_5294;
import net.minecraft.class_5321;
import net.minecraft.class_5362;
import net.minecraft.class_5455;
import net.minecraft.class_5575;
import net.minecraft.class_5577;
import net.minecraft.class_5712;
import net.minecraft.class_6754;
import net.minecraft.class_6756;
import net.minecraft.class_6880;
import net.minecraft.class_7134;
import net.minecraft.class_7699;
import net.minecraft.class_7924;
import net.minecraft.class_8921;
import net.minecraft.class_9209;
import net.minecraft.class_9895;
import org.jetbrains.annotations.Nullable;

public class WorldSchematic
extends class_1937 {
    protected static final class_5321<class_1937> REGISTRY_KEY = class_5321.method_29179((class_5321)class_7924.field_41223, (class_2960)class_2960.method_60655((String)"litematica", (String)"schematic_world"));
    protected final class_310 mc;
    protected final ChunkManagerSchematic chunkManagerSchematic;
    protected class_6880<class_1959> biome;
    @Nullable
    protected final WorldRendererSchematic worldRenderer;
    protected int nextEntityId;
    protected int entityCount;
    private final class_8921 tickManager;
    private final class_6880<class_2874> dimensionType;
    private class_5294 dimensionEffects = new class_5294.class_5297();

    public WorldSchematic(class_5269 properties, @Nonnull class_5455 registryManager, class_6880<class_2874> dimension, @Nullable WorldRendererSchematic worldRenderer) {
        super(properties, REGISTRY_KEY, !registryManager.equals((Object)class_5455.field_40585) ? registryManager : SchematicWorldHandler.INSTANCE.getRegistryManager(), dimension, true, false, 0L, 0);
        this.mc = class_310.method_1551();
        if (this.mc == null || this.mc.field_1687 == null) {
            throw new RuntimeException("WorldSchematic invoked when MinecraftClient.getInstance() or mc.world is null");
        }
        this.worldRenderer = worldRenderer;
        this.chunkManagerSchematic = new ChunkManagerSchematic(this);
        this.dimensionType = dimension;
        if (!registryManager.equals((Object)class_5455.field_40585)) {
            this.setDimension(registryManager);
        } else {
            this.setDimension(this.mc.field_1687.method_30349());
        }
        this.tickManager = new class_8921();
    }

    public String toString() {
        return "SchematicWorld[" + REGISTRY_KEY.method_29177().toString() + "]";
    }

    private void setDimension(class_5455 registryManager) {
        registryManager.method_46759(class_7924.field_41241).ifPresent(entryLookup -> {
            class_6880 nether = entryLookup.method_46746(class_7134.field_37667).orElse(null);
            class_6880 end = entryLookup.method_46746(class_7134.field_37668).orElse(null);
            this.biome = nether != null && this.dimensionType.equals((Object)nether) ? WorldUtils.getWastes((class_5455)registryManager) : (end != null && this.dimensionType.equals((Object)end) ? WorldUtils.getTheEnd((class_5455)registryManager) : WorldUtils.getPlains((class_5455)registryManager));
        });
        this.dimensionEffects = class_5294.method_28111((class_2874)((class_2874)this.dimensionType.comp_349()));
    }

    public ChunkManagerSchematic getChunkProvider() {
        return this.getChunkManager();
    }

    public ChunkManagerSchematic getChunkManager() {
        return this.chunkManagerSchematic;
    }

    public class_8921 method_54719() {
        return this.tickManager;
    }

    @Nullable
    public class_22 method_17891(class_9209 id) {
        return null;
    }

    public void method_17890(class_9209 id, class_22 state) {
    }

    public class_9209 method_17889() {
        return null;
    }

    public class_6756<class_2248> method_8397() {
        return class_6754.method_39362();
    }

    public class_6756<class_3611> method_8405() {
        return class_6754.method_39362();
    }

    public int getRegularEntityCount() {
        return this.entityCount;
    }

    public class_2818 method_8500(class_2338 pos) {
        return this.getChunk(pos.method_10263() >> 4, pos.method_10260() >> 4);
    }

    public ChunkSchematic getChunk(int chunkX, int chunkZ) {
        return this.chunkManagerSchematic.getChunk(chunkX, chunkZ);
    }

    public class_2791 method_8402(int chunkX, int chunkZ, class_2806 status, boolean required) {
        return this.getChunk(chunkX, chunkZ);
    }

    public class_6880<class_1959> method_22387(int biomeX, int biomeY, int biomeZ) {
        return this.biome;
    }

    public int method_8615() {
        return 0;
    }

    public boolean method_8652(class_2338 pos, class_2680 newState, int flags) {
        if (pos.method_10264() < this.method_31607() || pos.method_10264() >= this.method_31600()) {
            return false;
        }
        return this.getChunk(pos.method_10263() >> 4, pos.method_10260() >> 4).method_12010(pos, newState, false) != null;
    }

    public boolean method_8649(class_1297 entity) {
        int chunkZ;
        int chunkX = class_3532.method_15357((double)(entity.method_23317() / 16.0));
        if (!this.chunkManagerSchematic.method_12123(chunkX, chunkZ = class_3532.method_15357((double)(entity.method_23321() / 16.0)))) {
            return false;
        }
        entity.method_5838(this.nextEntityId++);
        ChunkSchematic chunk = this.chunkManagerSchematic.getChunk(chunkX, chunkZ);
        if (chunk == null) {
            return false;
        }
        chunk.method_12002(entity);
        ++this.entityCount;
        return true;
    }

    public void unloadedEntities(int count) {
        this.entityCount -= count;
    }

    @Nullable
    public class_1297 method_8469(int id) {
        return null;
    }

    public Collection<class_1508> method_65097() {
        return List.of();
    }

    public List<? extends class_1657> method_18456() {
        return ImmutableList.of();
    }

    public long method_8510() {
        return this.mc.field_1687 != null ? this.mc.field_1687.method_8510() : 0L;
    }

    public class_269 method_8428() {
        return this.mc.field_1687 != null ? this.mc.field_1687.method_8428() : null;
    }

    public class_10286 method_8433() {
        return this.mc.field_1687 != null ? this.mc.field_1687.method_8433() : null;
    }

    protected class_5577<class_1297> method_31592() {
        return null;
    }

    public List<class_1297> method_8333(@Nullable class_1297 except, class_238 box, Predicate<? super class_1297> predicate) {
        ArrayList<class_1297> list = new ArrayList<class_1297>();
        List<ChunkSchematic> chunks = this.getChunksWithinBox(box);
        for (ChunkSchematic chunk : chunks) {
            chunk.getEntityList().forEach(e -> {
                if (e != except && box.method_994(e.method_5829()) && predicate.test((class_1297)e)) {
                    list.add((class_1297)e);
                }
            });
        }
        return list;
    }

    public <T extends class_1297> List<T> method_18023(class_5575<class_1297, T> arg, class_238 box, Predicate<? super T> predicate) {
        ArrayList<class_1297> list = new ArrayList<class_1297>();
        for (class_1297 e2 : this.method_8333(null, box, e -> true)) {
            class_1297 t = (class_1297)arg.method_31796((Object)e2);
            if (t == null || !predicate.test(t)) continue;
            list.add(t);
        }
        return list;
    }

    public List<ChunkSchematic> getChunksWithinBox(class_238 box) {
        int minX = class_3532.method_15357((double)(box.field_1323 / 16.0));
        int minZ = class_3532.method_15357((double)(box.field_1321 / 16.0));
        int maxX = class_3532.method_15357((double)(box.field_1320 / 16.0));
        int maxZ = class_3532.method_15357((double)(box.field_1324 / 16.0));
        ArrayList<ChunkSchematic> chunks = new ArrayList<ChunkSchematic>();
        for (int cx = minX; cx <= maxX; ++cx) {
            for (int cz = minZ; cz <= maxZ; ++cz) {
                ChunkSchematic chunk = this.chunkManagerSchematic.getChunkIfExists(cx, cz);
                if (chunk == null) continue;
                chunks.add(chunk);
            }
        }
        return chunks;
    }

    public void method_16109(class_2338 pos, class_2680 stateOld, class_2680 stateNew) {
        if (stateNew != stateOld) {
            this.scheduleChunkRenders(pos.method_10263() >> 4, pos.method_10260() >> 4);
        }
    }

    public void scheduleChunkRenders(int chunkX, int chunkZ) {
        if (this.worldRenderer != null) {
            this.worldRenderer.scheduleChunkRenders(chunkX, chunkZ);
        }
    }

    public int method_31607() {
        return this.mc.field_1687 != null ? this.mc.field_1687.method_31607() : -64;
    }

    public int method_31605() {
        return this.mc.field_1687 != null ? this.mc.field_1687.method_31605() : 384;
    }

    public int method_31600() {
        return this.method_31607() + this.method_31605();
    }

    public int method_32891() {
        return this.method_31607() >> 4;
    }

    public int method_31597() {
        return this.method_31600() >> 4;
    }

    public int method_32890() {
        return this.method_31597() - this.method_32891();
    }

    public boolean method_31606(class_2338 pos) {
        return this.method_31601(pos.method_10264());
    }

    public boolean method_31601(int y) {
        return y < this.method_31607() || y >= this.method_31600();
    }

    public int method_31602(int y) {
        return (y >> 4) - (this.method_31607() >> 4);
    }

    public int method_31603(int coord) {
        return coord - (this.method_31607() >> 4);
    }

    public int method_31604(int index) {
        return index + (this.method_31607() >> 4);
    }

    public class_6880<class_2874> getDimensionType() {
        return this.dimensionType;
    }

    public class_5294 getDimensionEffects() {
        return this.dimensionEffects;
    }

    public float method_24852(class_2350 direction, boolean shaded) {
        boolean darkened = this.getDimensionEffects().method_29993();
        if (!shaded) {
            return darkened ? 0.9f : 1.0f;
        }
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case class_2350.field_11033 -> {
                if (darkened) {
                    yield 0.9f;
                }
                yield 0.5f;
            }
            case class_2350.field_11036 -> {
                if (darkened) {
                    yield 0.9f;
                }
                yield 1.0f;
            }
            case class_2350.field_11043, class_2350.field_11035 -> 0.8f;
            case class_2350.field_11039, class_2350.field_11034 -> 0.6f;
        };
    }

    public class_3568 method_22336() {
        return this.getChunkManager().method_12130();
    }

    public void method_8413(class_2338 blockPos_1, class_2680 blockState_1, class_2680 blockState_2, int flags) {
    }

    public void method_8517(int entityId, class_2338 pos, int progress) {
    }

    public void method_8474(int eventId, class_2338 pos, int data) {
    }

    public void method_8444(@Nullable class_1657 entity, int id, class_2338 pos, int data) {
    }

    public void method_32888(class_6880<class_5712> event, class_243 emitterPos, class_5712.class_7397 emitter) {
    }

    public void method_47967(@Nullable class_1657 except, double x, double y, double z, class_3414 sound, class_3419 category, float volume, float pitch, long seed) {
    }

    public void method_8449(@javax.annotation.Nullable class_1657 except, class_1297 entity, class_6880<class_3414> sound, class_3419 category, float volume, float pitch, long seed) {
    }

    public void method_8406(class_2394 particleParameters_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
    }

    public void method_8494(class_2394 particleParameters_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
    }

    public void method_17452(class_2394 particleParameters_1, boolean boolean_1, double double_1, double double_2, double double_3, double double_4, double double_5, double double_6) {
    }

    public void method_8454(@Nullable class_1297 entity, @Nullable class_1282 damageSource, @Nullable class_5362 behavior, double x, double y, double z, float power, boolean createFire, class_1937.class_7867 explosionSourceType, class_2394 smallParticle, class_2394 largeParticle, class_6880<class_3414> soundEvent) {
    }

    public void method_8486(double x, double y, double z, class_3414 soundIn, class_3419 category, float volume, float pitch, boolean distanceDelay) {
    }

    public void method_8396(class_1657 player, class_2338 pos, class_3414 soundIn, class_3419 category, float volume, float pitch) {
    }

    public void method_8465(@javax.annotation.Nullable class_1657 except, double x, double y, double z, class_6880<class_3414> sound, class_3419 category, float volume, float pitch, long seed) {
    }

    public void method_43128(class_1657 player, double x, double y, double z, class_3414 soundIn, class_3419 category, float volume, float pitch) {
    }

    public void method_43129(@Nullable class_1657 player, class_1297 entity, class_3414 sound, class_3419 category, float volume, float pitch) {
    }

    public class_5455 method_30349() {
        if (this.mc != null && this.mc.field_1687 != null) {
            return this.mc.field_1687.method_30349();
        }
        if (!SchematicWorldHandler.INSTANCE.getRegistryManager().equals((Object)class_5455.field_40585)) {
            return SchematicWorldHandler.INSTANCE.getRegistryManager();
        }
        return class_5455.field_40585;
    }

    public class_1845 method_59547() {
        if (this.mc != null && this.mc.field_1687 != null) {
            return this.mc.field_1687.method_59547();
        }
        return class_1845.field_51402;
    }

    public class_9895 method_61269() {
        return null;
    }

    public class_7699 method_45162() {
        if (this.mc != null && this.mc.field_1687 != null) {
            return this.mc.field_1687.method_45162();
        }
        return class_7699.method_45397();
    }

    public String method_31419() {
        return "Chunks[SCH] W: " + this.getChunkManager().method_12122() + " E: " + this.getRegularEntityCount();
    }

    public void method_43275(@Nullable class_1297 entity, class_6880<class_5712> event, class_243 pos) {
    }

    public void method_33596(@Nullable class_1297 entity, class_6880<class_5712> event, class_2338 pos) {
    }

    public void method_55764(class_5321<class_5712> event, class_2338 pos, @Nullable class_5712.class_7397 emitter) {
    }
}

