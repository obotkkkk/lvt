/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.mojang.datafixers.util.Either
 *  fi.dy.masa.malilib.interfaces.IClientTickHandler
 *  fi.dy.masa.malilib.interfaces.IDataSyncer
 *  fi.dy.masa.malilib.mixin.entity.IMixinAbstractHorseEntity
 *  fi.dy.masa.malilib.mixin.entity.IMixinPiglinEntity
 *  fi.dy.masa.malilib.mixin.network.IMixinDataQueryHandler
 *  fi.dy.masa.malilib.network.ClientPlayHandler
 *  fi.dy.masa.malilib.network.IPluginClientPlayHandler
 *  fi.dy.masa.malilib.util.InventoryUtils
 *  fi.dy.masa.malilib.util.WorldUtils
 *  fi.dy.masa.malilib.util.nbt.NbtUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1258
 *  net.minecraft.class_1263
 *  net.minecraft.class_1277
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1496
 *  net.minecraft.class_156
 *  net.minecraft.class_1646
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2281
 *  net.minecraft.class_2338
 *  net.minecraft.class_2343
 *  net.minecraft.class_238
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_2586
 *  net.minecraft.class_2591
 *  net.minecraft.class_2595
 *  net.minecraft.class_2680
 *  net.minecraft.class_2745
 *  net.minecraft.class_2769
 *  net.minecraft.class_2791
 *  net.minecraft.class_2806
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3218
 *  net.minecraft.class_3481
 *  net.minecraft.class_3532
 *  net.minecraft.class_4836
 *  net.minecraft.class_634
 *  net.minecraft.class_638
 *  net.minecraft.class_6880$class_6883
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7923
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.data;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import fi.dy.masa.litematica.Litematica;
import fi.dy.masa.litematica.Reference;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.network.ServuxLitematicaHandler;
import fi.dy.masa.litematica.network.ServuxLitematicaPacket;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.interfaces.IClientTickHandler;
import fi.dy.masa.malilib.interfaces.IDataSyncer;
import fi.dy.masa.malilib.mixin.entity.IMixinAbstractHorseEntity;
import fi.dy.masa.malilib.mixin.entity.IMixinPiglinEntity;
import fi.dy.masa.malilib.mixin.network.IMixinDataQueryHandler;
import fi.dy.masa.malilib.network.ClientPlayHandler;
import fi.dy.masa.malilib.network.IPluginClientPlayHandler;
import fi.dy.masa.malilib.util.InventoryUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.malilib.util.nbt.NbtUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nullable;
import net.minecraft.class_1258;
import net.minecraft.class_1263;
import net.minecraft.class_1277;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1496;
import net.minecraft.class_156;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2281;
import net.minecraft.class_2338;
import net.minecraft.class_2343;
import net.minecraft.class_238;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2586;
import net.minecraft.class_2591;
import net.minecraft.class_2595;
import net.minecraft.class_2680;
import net.minecraft.class_2745;
import net.minecraft.class_2769;
import net.minecraft.class_2791;
import net.minecraft.class_2806;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3218;
import net.minecraft.class_3481;
import net.minecraft.class_3532;
import net.minecraft.class_4836;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_7225;
import net.minecraft.class_7923;
import org.apache.commons.lang3.tuple.Pair;

public class EntitiesDataStorage
implements IClientTickHandler,
IDataSyncer {
    private static final EntitiesDataStorage INSTANCE = new EntitiesDataStorage();
    private static final ServuxLitematicaHandler<ServuxLitematicaPacket.Payload> HANDLER = ServuxLitematicaHandler.getInstance();
    private final class_310 mc;
    private boolean servuxServer = false;
    private boolean hasInValidServux = false;
    private String servuxVersion;
    private final long chunkTimeoutMs = 5000L;
    private boolean checkOpStatus = true;
    private boolean hasOpStatus = false;
    private long lastOpCheck = 0L;
    private final ConcurrentHashMap<class_2338, Pair<Long, Pair<class_2586, class_2487>>> blockEntityCache = new ConcurrentHashMap();
    private final ConcurrentHashMap<Integer, Pair<Long, Pair<class_1297, class_2487>>> entityCache = new ConcurrentHashMap();
    private final long cacheTimeout = 4L;
    private final long longCacheTimeout = 30L;
    private boolean shouldUseLongTimeout = false;
    private long serverTickTime = 0L;
    private final Set<class_2338> pendingBlockEntitiesQueue = new LinkedHashSet<class_2338>();
    private final Set<Integer> pendingEntitiesQueue = new LinkedHashSet<Integer>();
    private final Set<class_1923> pendingChunks = new LinkedHashSet<class_1923>();
    private final Set<class_1923> completedChunks = new LinkedHashSet<class_1923>();
    private final Map<class_1923, Long> pendingChunkTimeout = new HashMap<class_1923, Long>();
    private final Map<Integer, Either<class_2338, Integer>> transactionToBlockPosOrEntityId = new HashMap<Integer, Either<class_2338, Integer>>();
    private class_638 clientWorld;
    private boolean sentBackupPackets = false;
    private boolean receivedBackupPackets = false;
    private final HashMap<class_1923, Set<class_2338>> pendingBackupChunk_BlockEntities = new HashMap();
    private final HashMap<class_1923, Set<Integer>> pendingBackupChunk_Entities = new HashMap();

    public static EntitiesDataStorage getInstance() {
        return INSTANCE;
    }

    @Nullable
    public class_1937 getWorld() {
        return WorldUtils.getBestWorld((class_310)this.mc);
    }

    public class_638 getClientWorld() {
        if (this.clientWorld == null) {
            this.clientWorld = this.mc.field_1687;
        }
        return this.clientWorld;
    }

    private EntitiesDataStorage() {
        this.mc = class_310.method_1551();
    }

    public void onClientTick(class_310 mc) {
        long now = System.currentTimeMillis();
        if (now - this.serverTickTime > 50L) {
            if (!Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue()) {
                this.serverTickTime = now;
                if (!DataManager.getInstance().hasIntegratedServer() && this.hasServuxServer()) {
                    this.servuxServer = false;
                    HANDLER.unregisterPlayReceiver();
                }
                if (!Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
                    if (!this.pendingBlockEntitiesQueue.isEmpty()) {
                        this.pendingBlockEntitiesQueue.clear();
                    }
                    if (!this.pendingEntitiesQueue.isEmpty()) {
                        this.pendingEntitiesQueue.clear();
                    }
                    return;
                }
            } else if (!(DataManager.getInstance().hasIntegratedServer() || this.hasServuxServer() || this.hasInValidServux || this.getWorld() == null)) {
                HANDLER.registerPlayReceiver(ServuxLitematicaPacket.Payload.ID, HANDLER::receivePlayPayload);
                this.requestMetadata();
            }
            this.tickCache(now);
            for (int i = 0; i < Configs.Generic.SERVER_NBT_REQUEST_RATE.getIntegerValue(); ++i) {
                Iterator<Object> iter;
                if (!this.pendingBlockEntitiesQueue.isEmpty()) {
                    iter = this.pendingBlockEntitiesQueue.iterator();
                    class_2338 pos = iter.next();
                    iter.remove();
                    if (this.hasServuxServer()) {
                        this.requestServuxBlockEntityData(pos);
                    } else if (this.shouldUseQuery()) {
                        this.requestQueryBlockEntity(pos);
                    }
                }
                if (this.pendingEntitiesQueue.isEmpty()) continue;
                iter = this.pendingEntitiesQueue.iterator();
                int entityId = (Integer)iter.next();
                iter.remove();
                if (this.hasServuxServer()) {
                    this.requestServuxEntityData(entityId);
                    continue;
                }
                if (!this.shouldUseQuery()) continue;
                this.requestQueryEntityData(entityId);
            }
            this.serverTickTime = System.currentTimeMillis();
        }
    }

    public class_2960 getNetworkChannel() {
        return ServuxLitematicaHandler.CHANNEL_ID;
    }

    private class_634 getVanillaHandler() {
        if (this.mc.field_1724 != null) {
            return this.mc.field_1724.field_3944;
        }
        return null;
    }

    public IPluginClientPlayHandler<ServuxLitematicaPacket.Payload> getNetworkHandler() {
        return HANDLER;
    }

    public void reset(boolean isLogout) {
        if (isLogout) {
            Litematica.debugLog("EntitiesDataStorage#reset() - log-out", new Object[0]);
            HANDLER.reset(this.getNetworkChannel());
            HANDLER.resetFailures(this.getNetworkChannel());
            this.servuxServer = false;
            this.hasInValidServux = false;
            this.sentBackupPackets = false;
            this.receivedBackupPackets = false;
            this.checkOpStatus = false;
            this.hasOpStatus = false;
            this.lastOpCheck = 0L;
        } else {
            Litematica.debugLog("EntitiesDataStorage#reset() - dimension change or log-in", new Object[0]);
            long now = System.currentTimeMillis();
            this.serverTickTime = now - (this.getCacheTimeout() + 5000L);
            this.tickCache(now);
            this.serverTickTime = now;
            this.clientWorld = this.mc.field_1687;
            this.checkOpStatus = true;
            this.lastOpCheck = now;
        }
        this.blockEntityCache.clear();
        this.entityCache.clear();
        this.pendingBlockEntitiesQueue.clear();
        this.pendingEntitiesQueue.clear();
        this.completedChunks.clear();
        this.pendingChunks.clear();
        this.pendingChunkTimeout.clear();
        this.pendingBackupChunk_BlockEntities.clear();
        this.pendingBackupChunk_Entities.clear();
    }

    private boolean shouldUseQuery() {
        if (this.hasOpStatus) {
            return true;
        }
        if (this.checkOpStatus) {
            if (System.currentTimeMillis() - this.lastOpCheck < 900000L) {
                return true;
            }
            this.checkOpStatus = false;
        }
        return false;
    }

    public void resetOpCheck() {
        this.hasOpStatus = false;
        this.checkOpStatus = true;
        this.lastOpCheck = System.currentTimeMillis();
    }

    private long getCacheTimeout() {
        int modifier = Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue() ? 5 : 1;
        return (long)(class_3532.method_15363((float)(Configs.Generic.ENTITY_DATA_SYNC_CACHE_TIMEOUT.getFloatValue() * (float)modifier), (float)1.0f, (float)500.0f) * 1000.0f);
    }

    private long getCacheTimeoutLong() {
        int modifier = Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue() ? 5 : 1;
        float f = Configs.Generic.ENTITY_DATA_SYNC_CACHE_TIMEOUT.getFloatValue() * (float)modifier;
        Objects.requireNonNull(this);
        return (long)(class_3532.method_15363((float)(f * 30.0f), (float)120.0f, (float)(300.0f * (float)modifier)) * 1000.0f);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void tickCache(long nowTime) {
        Pair<Long, Pair<class_2586, class_2487>> pair;
        int count;
        long blockTimeout = this.getCacheTimeout();
        long entityTimeout = this.getCacheTimeout();
        boolean beEmpty = false;
        boolean entEmpty = false;
        if (this.shouldUseLongTimeout) {
            blockTimeout = this.getCacheTimeoutLong();
            entityTimeout = this.getCacheTimeoutLong();
            if (!this.hasServuxServer() && this.getIfReceivedBackupPackets()) {
                blockTimeout += 3000L;
                entityTimeout += 3000L;
            }
        }
        ConcurrentHashMap<Object, Object> concurrentHashMap = this.blockEntityCache;
        synchronized (concurrentHashMap) {
            count = 0;
            for (class_2338 pos : this.blockEntityCache.keySet()) {
                pair = this.blockEntityCache.get(pos);
                if (nowTime - (Long)pair.getLeft() > blockTimeout || (Long)pair.getLeft() > nowTime) {
                    this.blockEntityCache.remove(pos);
                    continue;
                }
                ++count;
            }
            if (count == 0) {
                beEmpty = true;
            }
        }
        concurrentHashMap = this.entityCache;
        synchronized (concurrentHashMap) {
            count = 0;
            for (Integer entityId : this.entityCache.keySet()) {
                pair = this.entityCache.get(entityId);
                if (nowTime - (Long)pair.getLeft() > entityTimeout || (Long)pair.getLeft() > nowTime) {
                    this.entityCache.remove(entityId);
                    continue;
                }
                ++count;
            }
            if (count == 0) {
                entEmpty = true;
            }
        }
        if (beEmpty && entEmpty && this.shouldUseLongTimeout) {
            this.shouldUseLongTimeout = false;
        }
    }

    @Nullable
    public class_2487 getFromBlockEntityCacheNbt(class_2338 pos) {
        if (this.blockEntityCache.containsKey(pos)) {
            return (class_2487)((Pair)this.blockEntityCache.get(pos).getRight()).getRight();
        }
        return null;
    }

    @Nullable
    public class_2586 getFromBlockEntityCache(class_2338 pos) {
        if (this.blockEntityCache.containsKey(pos)) {
            return (class_2586)((Pair)this.blockEntityCache.get(pos).getRight()).getLeft();
        }
        return null;
    }

    @Nullable
    public class_2487 getFromEntityCacheNbt(int entityId) {
        if (this.entityCache.containsKey(entityId)) {
            return (class_2487)((Pair)this.entityCache.get(entityId).getRight()).getRight();
        }
        return null;
    }

    @Nullable
    public class_1297 getFromEntityCache(int entityId) {
        if (this.entityCache.containsKey(entityId)) {
            return (class_1297)((Pair)this.entityCache.get(entityId).getRight()).getLeft();
        }
        return null;
    }

    public void setIsServuxServer() {
        this.servuxServer = true;
        this.hasInValidServux = false;
    }

    public boolean hasServuxServer() {
        return this.servuxServer;
    }

    public boolean hasBackupStatus() {
        return Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue() && this.hasOpStatus;
    }

    public void setServuxVersion(String ver) {
        if (ver != null && !ver.isEmpty()) {
            this.servuxVersion = ver;
            Litematica.debugLog("LitematicDataChannel: joining Servux version {}", ver);
        } else {
            this.servuxVersion = "unknown";
        }
    }

    public String getServuxVersion() {
        return this.servuxVersion;
    }

    public int getPendingBlockEntitiesCount() {
        return this.pendingBlockEntitiesQueue.size();
    }

    public int getPendingEntitiesCount() {
        return this.pendingEntitiesQueue.size();
    }

    public int getBlockEntityCacheCount() {
        return this.blockEntityCache.size();
    }

    public int getEntityCacheCount() {
        return this.entityCache.size();
    }

    public boolean getIfReceivedBackupPackets() {
        if (Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            return this.sentBackupPackets & this.receivedBackupPackets;
        }
        return false;
    }

    public void onGameInit() {
        ClientPlayHandler.getInstance().registerClientPlayHandler(HANDLER);
        HANDLER.registerPlayPayload(ServuxLitematicaPacket.Payload.ID, ServuxLitematicaPacket.Payload.CODEC, 6);
    }

    public void onWorldPre() {
        if (!DataManager.getInstance().hasIntegratedServer()) {
            HANDLER.registerPlayReceiver(ServuxLitematicaPacket.Payload.ID, HANDLER::receivePlayPayload);
        }
    }

    public void onWorldJoin() {
    }

    public void requestMetadata() {
        if (!DataManager.getInstance().hasIntegratedServer() && Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue()) {
            class_2487 nbt = new class_2487();
            nbt.method_10582("version", Reference.MOD_STRING);
            HANDLER.encodeClientData(ServuxLitematicaPacket.MetadataRequest(nbt));
        }
    }

    public boolean receiveServuxMetadata(class_2487 data) {
        if (!DataManager.getInstance().hasIntegratedServer()) {
            Litematica.debugLog("LitematicDataChannel: received METADATA from Servux", new Object[0]);
            if (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue()) {
                if (data.method_10550("version") != 1) {
                    Litematica.LOGGER.warn("LitematicDataChannel: Mis-matched protocol version!");
                }
                this.setServuxVersion(data.method_10558("servux"));
                this.setIsServuxServer();
                return true;
            }
        }
        return false;
    }

    public void onPacketFailure() {
        this.servuxServer = false;
        this.hasInValidServux = true;
    }

    @Nullable
    public Pair<class_2586, class_2487> requestBlockEntity(class_1937 world, class_2338 pos) {
        if (world instanceof WorldSchematic) {
            return this.refreshBlockEntityFromWorld(world, pos);
        }
        if (this.blockEntityCache.containsKey(pos)) {
            if (!DataManager.getInstance().hasIntegratedServer() && (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) && System.currentTimeMillis() - (Long)this.blockEntityCache.get(pos).getLeft() > this.getCacheTimeout() / 4L) {
                this.pendingBlockEntitiesQueue.add(pos);
            }
            if (world instanceof class_3218) {
                return this.refreshBlockEntityFromWorld(world, pos);
            }
            return (Pair)this.blockEntityCache.get(pos).getRight();
        }
        if (world.method_8320(pos).method_26204() instanceof class_2343) {
            if (!DataManager.getInstance().hasIntegratedServer() && (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue())) {
                this.pendingBlockEntitiesQueue.add(pos);
            }
            return this.refreshBlockEntityFromWorld((class_1937)this.getClientWorld(), pos);
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    private Pair<class_2586, class_2487> refreshBlockEntityFromWorld(class_1937 world, class_2338 pos) {
        class_2586 be;
        if (world != null && world.method_8320(pos).method_31709() && (be = world.method_8500(pos).method_8321(pos)) != null) {
            class_2487 nbt = be.method_38242((class_7225.class_7874)world.method_30349());
            Pair pair = Pair.of((Object)be, (Object)nbt);
            if (!(world instanceof WorldSchematic)) {
                ConcurrentHashMap<class_2338, Pair<Long, Pair<class_2586, class_2487>>> concurrentHashMap = this.blockEntityCache;
                synchronized (concurrentHashMap) {
                    this.blockEntityCache.put(pos, (Pair<Long, Pair<class_2586, class_2487>>)Pair.of((Object)System.currentTimeMillis(), (Object)pair));
                }
            }
            return pair;
        }
        return null;
    }

    @Nullable
    public Pair<class_1297, class_2487> requestEntity(class_1937 world, int entityId) {
        if (world instanceof WorldSchematic) {
            return this.refreshEntityFromWorld(world, entityId);
        }
        if (this.entityCache.containsKey(entityId)) {
            if (!DataManager.getInstance().hasIntegratedServer() && (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) && System.currentTimeMillis() - (Long)this.entityCache.get(entityId).getLeft() > this.getCacheTimeout() / 4L) {
                this.pendingEntitiesQueue.add(entityId);
            }
            if (world instanceof class_3218) {
                return this.refreshEntityFromWorld(world, entityId);
            }
            return (Pair)this.entityCache.get(entityId).getRight();
        }
        if (!DataManager.getInstance().hasIntegratedServer() && (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue())) {
            this.pendingEntitiesQueue.add(entityId);
        }
        return this.refreshEntityFromWorld((class_1937)this.getClientWorld(), entityId);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    private Pair<class_1297, class_2487> refreshEntityFromWorld(class_1937 world, int entityId) {
        if (world != null) {
            class_1297 entity = world.method_8469(entityId);
            class_2487 nbt = new class_2487();
            if (entity != null && entity.method_5786(nbt)) {
                Pair pair = Pair.of((Object)entity, (Object)nbt.method_10553());
                if (!(world instanceof WorldSchematic)) {
                    ConcurrentHashMap<Integer, Pair<Long, Pair<class_1297, class_2487>>> concurrentHashMap = this.entityCache;
                    synchronized (concurrentHashMap) {
                        this.entityCache.put(entityId, (Pair<Long, Pair<class_1297, class_2487>>)Pair.of((Object)System.currentTimeMillis(), (Object)pair));
                    }
                }
                return pair;
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    public class_1263 getBlockInventory(class_1937 world, class_2338 pos, boolean useNbt) {
        if (world instanceof WorldSchematic) {
            return InventoryUtils.getInventory((class_1937)world, (class_2338)pos);
        }
        if (this.blockEntityCache.containsKey(pos)) {
            class_1263 inv = null;
            if (useNbt) {
                inv = InventoryUtils.getNbtInventory((class_2487)((class_2487)((Pair)this.blockEntityCache.get(pos).getRight()).getRight()), (int)-1, (class_7225.class_7874)world.method_30349());
            } else {
                class_2586 be = (class_2586)((Pair)this.blockEntityCache.get(pos).getRight()).getLeft();
                class_2680 state = world.method_8320(pos);
                if (state.method_26164(class_3481.field_51989) || !state.method_31709()) {
                    ConcurrentHashMap<class_2338, Pair<Long, Pair<class_2586, class_2487>>> concurrentHashMap = this.blockEntityCache;
                    synchronized (concurrentHashMap) {
                        this.blockEntityCache.remove(pos);
                    }
                    return null;
                }
                if (be instanceof class_1263) {
                    class_1263 inv1 = (class_1263)be;
                    if (be instanceof class_2595 && state.method_28498((class_2769)class_2281.field_10770)) {
                        class_2745 type = (class_2745)state.method_11654((class_2769)class_2281.field_10770);
                        if (type != class_2745.field_12569) {
                            class_2338 posAdj = pos.method_10093(class_2281.method_9758((class_2680)state));
                            if (!world.method_22340(posAdj)) {
                                return null;
                            }
                            class_2680 stateAdj = world.method_8320(posAdj);
                            class_2586 dataAdj = this.getFromBlockEntityCache(posAdj);
                            if (dataAdj == null) {
                                this.requestBlockEntity(world, posAdj);
                            }
                            if (stateAdj.method_26204() == state.method_26204() && dataAdj instanceof class_2595) {
                                class_2595 inv2 = (class_2595)dataAdj;
                                if (stateAdj.method_11654((class_2769)class_2281.field_10770) != class_2745.field_12569 && stateAdj.method_11654((class_2769)class_2281.field_10768) == state.method_11654((class_2769)class_2281.field_10768)) {
                                    class_1263 invRight = type == class_2745.field_12571 ? inv1 : inv2;
                                    class_2595 invLeft = type == class_2745.field_12571 ? inv2 : inv1;
                                    inv = new class_1258(invRight, (class_1263)invLeft);
                                }
                            }
                        } else {
                            inv = inv1;
                        }
                    } else {
                        inv = inv1;
                    }
                }
            }
            if (inv != null) {
                return inv;
            }
        }
        if (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            this.requestBlockEntity(world, pos);
        }
        return null;
    }

    @Nullable
    public class_1263 getEntityInventory(class_1937 world, int entityId, boolean useNbt) {
        if (world instanceof WorldSchematic) {
            return null;
        }
        if (this.entityCache.containsKey(entityId) && this.getWorld() != null) {
            class_1263 inv = null;
            if (useNbt) {
                inv = InventoryUtils.getNbtInventory((class_2487)((class_2487)((Pair)this.entityCache.get(entityId).getRight()).getRight()), (int)-1, (class_7225.class_7874)this.getWorld().method_30349());
            } else {
                class_1657 player;
                class_1297 entity = (class_1297)((Pair)this.entityCache.get(entityId).getRight()).getLeft();
                if (entity instanceof class_1263) {
                    inv = (class_1263)entity;
                } else if (entity instanceof class_1657 && (player = (class_1657)entity) != null) {
                    inv = new class_1277((class_1799[])player.method_31548().field_7547.toArray((Object[])new class_1799[36]));
                } else if (entity instanceof class_1646) {
                    inv = ((class_1646)entity).method_35199();
                } else if (entity instanceof class_1496) {
                    inv = ((IMixinAbstractHorseEntity)entity).malilib_getHorseInventory();
                } else if (entity instanceof class_4836) {
                    inv = ((IMixinPiglinEntity)entity).malilib_getInventory();
                }
            }
            if (inv != null) {
                return inv;
            }
        }
        if (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue() || Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            this.requestEntity(world, entityId);
        }
        return null;
    }

    private void requestQueryBlockEntity(class_2338 pos) {
        if (!Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            return;
        }
        class_634 handler = this.getVanillaHandler();
        if (handler != null) {
            this.sentBackupPackets = true;
            handler.method_2876().method_1403(pos, nbtCompound -> this.handleBlockEntityData(pos, (class_2487)nbtCompound, null));
            this.transactionToBlockPosOrEntityId.put(((IMixinDataQueryHandler)handler.method_2876()).malilib_currentTransactionId(), (Either<class_2338, Integer>)Either.left((Object)pos));
        }
    }

    private void requestQueryEntityData(int entityId) {
        if (!Configs.Generic.ENTITY_DATA_SYNC_BACKUP.getBooleanValue()) {
            return;
        }
        class_634 handler = this.getVanillaHandler();
        if (handler != null) {
            this.sentBackupPackets = true;
            handler.method_2876().method_1405(entityId, nbtCompound -> this.handleEntityData(entityId, (class_2487)nbtCompound));
            this.transactionToBlockPosOrEntityId.put(((IMixinDataQueryHandler)handler.method_2876()).malilib_currentTransactionId(), (Either<class_2338, Integer>)Either.right((Object)entityId));
        }
    }

    private void requestServuxBlockEntityData(class_2338 pos) {
        if (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue()) {
            HANDLER.encodeClientData(ServuxLitematicaPacket.BlockEntityRequest(pos));
        }
    }

    private void requestServuxEntityData(int entityId) {
        if (Configs.Generic.ENTITY_DATA_SYNC.getBooleanValue()) {
            HANDLER.encodeClientData(ServuxLitematicaPacket.EntityRequest(entityId));
        }
    }

    public void requestServuxBulkEntityData(class_1923 chunkPos, int minY, int maxY) {
        if (!this.hasServuxServer()) {
            return;
        }
        class_2487 req = new class_2487();
        this.completedChunks.remove(chunkPos);
        this.pendingChunks.add(chunkPos);
        this.pendingChunkTimeout.put(chunkPos, class_156.method_658());
        minY = class_3532.method_15340((int)minY, (int)-60, (int)319);
        maxY = class_3532.method_15340((int)maxY, (int)-60, (int)319);
        req.method_10582("Task", "BulkEntityRequest");
        req.method_10569("minY", minY);
        req.method_10569("maxY", maxY);
        Litematica.debugLog("EntitiesDataStorage#requestServuxBulkEntityData(): for chunkPos [{}] to Servux (minY [{}], maxY [{}])", chunkPos.toString(), minY, maxY);
        HANDLER.encodeClientData(ServuxLitematicaPacket.BulkNbtRequest(chunkPos, req));
    }

    public void requestBackupBulkEntityData(class_1923 chunkPos, int minY, int maxY) {
        class_2791 chunk;
        if (!this.getIfReceivedBackupPackets() || this.hasServuxServer()) {
            return;
        }
        this.completedChunks.remove(chunkPos);
        minY = class_3532.method_15340((int)minY, (int)-60, (int)319);
        maxY = class_3532.method_15340((int)maxY, (int)-60, (int)319);
        class_638 world = this.getClientWorld();
        class_2791 class_27912 = chunk = world != null ? world.method_8402(chunkPos.field_9181, chunkPos.field_9180, class_2806.field_12803, false) : null;
        if (chunk == null) {
            return;
        }
        class_2338 pos1 = new class_2338(chunkPos.method_8326(), minY, chunkPos.method_8328());
        class_2338 pos2 = new class_2338(chunkPos.method_8327(), maxY, chunkPos.method_8329());
        class_238 bb = PositionUtils.createEnclosingAABB(pos1, pos2);
        Set teSet = chunk.method_12021();
        List entList = world.method_8333(null, bb, EntityUtils.NOT_PLAYER);
        Litematica.debugLog("EntitiesDataStorage#requestBackupBulkEntityData(): for chunkPos {} (minY [{}], maxY [{}]) // Request --> TE: [{}], E: [{}]", chunkPos.toString(), minY, maxY, teSet.size(), entList.size());
        for (class_2338 tePos : teSet) {
            if (tePos.method_10263() < chunkPos.method_8326() || tePos.method_10263() > chunkPos.method_8327() || tePos.method_10260() < chunkPos.method_8328() || tePos.method_10260() > chunkPos.method_8329() || tePos.method_10264() < minY || tePos.method_10264() > maxY) continue;
            this.requestBlockEntity((class_1937)world, tePos);
        }
        if (teSet.size() > 0) {
            this.pendingBackupChunk_BlockEntities.put(chunkPos, teSet);
        }
        LinkedHashSet<Integer> entSet = new LinkedHashSet<Integer>();
        for (class_1297 entity : entList) {
            this.requestEntity((class_1937)world, entity.method_5628());
            entSet.add(entity.method_5628());
        }
        if (entSet.size() > 0) {
            this.pendingBackupChunk_Entities.put(chunkPos, entSet);
        }
        if (teSet.size() > 0 || entSet.size() > 0) {
            this.pendingChunks.add(chunkPos);
            this.pendingChunkTimeout.put(chunkPos, class_156.method_658());
        } else {
            this.completedChunks.add(chunkPos);
        }
    }

    private boolean markBackupBlockEntityComplete(class_1923 chunkPos, class_2338 pos) {
        Set<class_2338> teSet;
        if (!this.getIfReceivedBackupPackets() || this.hasServuxServer()) {
            return true;
        }
        if (this.pendingChunks.contains(chunkPos) && this.pendingBackupChunk_BlockEntities.containsKey(chunkPos) && (teSet = this.pendingBackupChunk_BlockEntities.get(chunkPos)).contains(pos)) {
            teSet.remove(pos);
            if (teSet.isEmpty()) {
                Litematica.debugLog("EntitiesDataStorage#markBackupBlockEntityComplete(): ChunkPos {} - Block Entity List Complete!", chunkPos.toString());
                this.pendingBackupChunk_BlockEntities.remove(chunkPos);
                this.pendingChunks.remove(chunkPos);
                this.pendingChunkTimeout.remove(chunkPos);
                this.completedChunks.add(chunkPos);
                return true;
            }
            this.pendingBackupChunk_BlockEntities.replace(chunkPos, teSet);
        }
        return false;
    }

    private boolean markBackupEntityComplete(class_1923 chunkPos, int entityId) {
        Set<Integer> entSet;
        if (!this.getIfReceivedBackupPackets() || this.hasServuxServer()) {
            return true;
        }
        if (this.pendingChunks.contains(chunkPos) && this.pendingBackupChunk_Entities.containsKey(chunkPos) && (entSet = this.pendingBackupChunk_Entities.get(chunkPos)).contains(entityId)) {
            entSet.remove(entityId);
            if (entSet.isEmpty()) {
                Litematica.debugLog("EntitiesDataStorage#markBackupEntityComplete(): ChunkPos {} - EntitiyList Complete!", chunkPos.toString());
                this.pendingBackupChunk_Entities.remove(chunkPos);
                this.pendingChunks.remove(chunkPos);
                this.pendingChunkTimeout.remove(chunkPos);
                this.completedChunks.add(chunkPos);
                return true;
            }
            this.pendingBackupChunk_Entities.replace(chunkPos, entSet);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    public class_2586 handleBlockEntityData(class_2338 pos, class_2487 nbt, @Nullable class_2960 type) {
        class_2586 blockEntity2;
        class_2591 beType;
        this.pendingBlockEntitiesQueue.remove(pos);
        if (nbt == null || this.getClientWorld() == null) {
            return null;
        }
        class_2586 blockEntity = this.getClientWorld().method_8321(pos);
        if (blockEntity != null && (type == null || type.equals((Object)class_2591.method_11033((class_2591)blockEntity.method_11017())))) {
            Object id;
            if (!nbt.method_10573("id", 8) && (id = class_2591.method_11033((class_2591)blockEntity.method_11017())) != null) {
                nbt.method_10582("id", id.toString());
            }
            id = this.blockEntityCache;
            synchronized (id) {
                this.blockEntityCache.put(pos, (Pair<Long, Pair<class_2586, class_2487>>)Pair.of((Object)System.currentTimeMillis(), (Object)Pair.of((Object)blockEntity, (Object)nbt)));
            }
            blockEntity.method_58690(nbt, (class_7225.class_7874)this.getClientWorld().method_30349());
            class_1923 chunkPos = new class_1923(pos);
            if (this.hasPendingChunk(chunkPos) && !this.hasServuxServer()) {
                this.markBackupBlockEntityComplete(chunkPos, pos);
            }
            return blockEntity;
        }
        Optional opt = class_7923.field_41181.method_10223(type);
        if (opt.isPresent() && (beType = (class_2591)((class_6880.class_6883)opt.get()).comp_349()).method_20526(this.getClientWorld().method_8320(pos)) && (blockEntity2 = beType.method_11032(pos, this.getClientWorld().method_8320(pos))) != null) {
            class_1923 chunkPos;
            Object id;
            if (!nbt.method_10573("id", 8) && (id = class_2591.method_11033((class_2591)beType)) != null) {
                nbt.method_10582("id", id.toString());
            }
            id = this.blockEntityCache;
            synchronized (id) {
                this.blockEntityCache.put(pos, (Pair<Long, Pair<class_2586, class_2487>>)Pair.of((Object)System.currentTimeMillis(), (Object)Pair.of((Object)blockEntity2, (Object)nbt)));
            }
            if (Configs.Generic.ENTITY_DATA_LOAD_NBT.getBooleanValue()) {
                blockEntity2.method_58690(nbt, (class_7225.class_7874)this.getClientWorld().method_30349());
                this.getClientWorld().method_8438(blockEntity2);
            }
            if (this.hasPendingChunk(chunkPos = new class_1923(pos)) && !this.hasServuxServer()) {
                this.markBackupBlockEntityComplete(chunkPos, pos);
            }
            return blockEntity2;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    public class_1297 handleEntityData(int entityId, class_2487 nbt) {
        this.pendingEntitiesQueue.remove(entityId);
        if (nbt == null || this.getClientWorld() == null) {
            return null;
        }
        class_1297 entity = this.getClientWorld().method_8469(entityId);
        if (entity != null) {
            class_2960 id;
            if (!nbt.method_10573("id", 8) && (id = class_1299.method_5890((class_1299)entity.method_5864())) != null) {
                nbt.method_10582("id", id.toString());
            }
            ConcurrentHashMap<Integer, Pair<Long, Pair<class_1297, class_2487>>> concurrentHashMap = this.entityCache;
            synchronized (concurrentHashMap) {
                this.entityCache.put(entityId, (Pair<Long, Pair<class_1297, class_2487>>)Pair.of((Object)System.currentTimeMillis(), (Object)Pair.of((Object)entity, (Object)nbt)));
            }
            if (Configs.Generic.ENTITY_DATA_LOAD_NBT.getBooleanValue()) {
                EntityUtils.loadNbtIntoEntity(entity, nbt);
            }
            if (this.hasPendingChunk(entity.method_31476()) && !this.hasServuxServer()) {
                this.markBackupEntityComplete(entity.method_31476(), entityId);
            }
        }
        return entity;
    }

    public void handleBulkEntityData(int transactionId, @Nullable class_2487 nbt) {
        if (nbt == null) {
            return;
        }
        if (nbt.method_10545("Task") && nbt.method_10558("Task").equals("BulkEntityReply") || !nbt.method_10545("Task")) {
            class_2338 pos;
            int i;
            class_2499 tileList = nbt.method_10545("TileEntities") ? nbt.method_10554("TileEntities", 10) : new class_2499();
            class_2499 entityList = nbt.method_10545("Entities") ? nbt.method_10554("Entities", 10) : new class_2499();
            class_1923 chunkPos = new class_1923(nbt.method_10550("chunkX"), nbt.method_10550("chunkZ"));
            this.shouldUseLongTimeout = true;
            for (i = 0; i < tileList.size(); ++i) {
                class_2487 te = tileList.method_10602(i);
                pos = NbtUtils.readBlockPos((class_2487)te);
                class_2960 type = class_2960.method_60654((String)te.method_10558("id"));
                this.handleBlockEntityData(pos, te, type);
            }
            for (i = 0; i < entityList.size(); ++i) {
                class_2487 ent = entityList.method_10602(i);
                pos = NbtUtils.readEntityPositionFromTag((class_2487)ent);
                int entityId = ent.method_10550("entityId");
                this.handleEntityData(entityId, ent);
            }
            this.pendingChunks.remove(chunkPos);
            this.pendingChunkTimeout.remove(chunkPos);
            this.completedChunks.add(chunkPos);
            Litematica.debugLog("EntitiesDataStorage#handleBulkEntityData(): [ChunkPos {}] received TE: [{}], and E: [{}] entiries from Servux", chunkPos.toString(), tileList.size(), entityList.size());
        }
    }

    public void handleVanillaQueryNbt(int transactionId, class_2487 nbt) {
        Either<class_2338, Integer> either;
        if (this.checkOpStatus) {
            this.hasOpStatus = true;
            this.checkOpStatus = false;
            this.lastOpCheck = System.currentTimeMillis();
        }
        if ((either = this.transactionToBlockPosOrEntityId.remove(transactionId)) != null) {
            this.receivedBackupPackets = true;
            either.ifLeft(pos -> this.handleBlockEntityData((class_2338)pos, nbt, null)).ifRight(entityId -> this.handleEntityData((int)entityId, nbt));
        }
    }

    public boolean hasPendingChunk(class_1923 pos) {
        if (this.hasServuxServer() || this.getIfReceivedBackupPackets()) {
            return this.pendingChunks.contains(pos);
        }
        return false;
    }

    private void checkForPendingChunkTimeout(class_1923 pos) {
        if (this.hasServuxServer() && this.hasPendingChunk(pos) || this.getIfReceivedBackupPackets() && this.hasPendingChunk(pos)) {
            long now = class_156.method_658();
            if (!fi.dy.masa.litematica.util.WorldUtils.isClientChunkLoaded(this.mc.field_1687, pos.field_9181, pos.field_9180)) {
                this.pendingChunkTimeout.replace(pos, now);
                return;
            }
            long duration = now - this.pendingChunkTimeout.get(pos);
            if (duration > this.getChunkTimeoutMs()) {
                Litematica.debugLog("EntitiesDataStorage#checkForPendingChunkTimeout(): [ChunkPos {}] has timed out waiting for data, marking complete without Receiving Entity Data.", pos.toString());
                this.pendingChunkTimeout.remove(pos);
                this.pendingChunks.remove(pos);
                this.completedChunks.add(pos);
            }
        }
    }

    private long getChunkTimeoutMs() {
        if (this.hasServuxServer()) {
            return this.chunkTimeoutMs;
        }
        if (this.getIfReceivedBackupPackets()) {
            return this.chunkTimeoutMs + 3000L;
        }
        return 1000L;
    }

    public boolean hasCompletedChunk(class_1923 pos) {
        if (this.hasServuxServer() || this.getIfReceivedBackupPackets()) {
            this.checkForPendingChunkTimeout(pos);
            return this.completedChunks.contains(pos);
        }
        return true;
    }

    public void markCompletedChunkDirty(class_1923 pos) {
        if (this.hasServuxServer() || this.getIfReceivedBackupPackets()) {
            this.completedChunks.remove(pos);
        }
    }

    public JsonObject toJson() {
        return new JsonObject();
    }

    public void fromJson(JsonObject obj) {
    }
}

