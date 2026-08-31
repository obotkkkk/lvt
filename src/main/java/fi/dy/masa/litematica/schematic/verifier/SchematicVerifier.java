/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ArrayListMultimap
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Lists
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.interfaces.ICompletionListener
 *  fi.dy.masa.malilib.util.Color4f
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.StringUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2374
 *  net.minecraft.class_2382
 *  net.minecraft.class_2680
 *  net.minecraft.class_2791
 *  net.minecraft.class_2818
 *  net.minecraft.class_310
 *  net.minecraft.class_3695
 *  net.minecraft.class_638
 *  net.minecraft.class_7923
 *  org.apache.commons.lang3.tuple.MutablePair
 *  org.apache.commons.lang3.tuple.Pair
 */
package fi.dy.masa.litematica.schematic.verifier;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.infohud.IInfoHudRenderer;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.render.infohud.RenderPhase;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskBase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.util.BlockInfoListType;
import fi.dy.masa.litematica.util.IgnoreBlockRegistry;
import fi.dy.masa.litematica.util.ItemUtils;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.litematica.world.WorldSchematic;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.util.Color4f;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_2382;
import net.minecraft.class_2680;
import net.minecraft.class_2791;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_3695;
import net.minecraft.class_638;
import net.minecraft.class_7923;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class SchematicVerifier
extends TaskBase
implements IInfoHudRenderer {
    private static final MutablePair<class_2680, class_2680> MUTABLE_PAIR = new MutablePair();
    private static final class_2338.class_2339 MUTABLE_POS = new class_2338.class_2339();
    private static final List<SchematicVerifier> ACTIVE_VERIFIERS = new ArrayList<SchematicVerifier>();
    private final ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> missingBlocksPositions = ArrayListMultimap.create();
    private final ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> extraBlocksPositions = ArrayListMultimap.create();
    private final ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> wrongBlocksPositions = ArrayListMultimap.create();
    private final ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> wrongStatesPositions = ArrayListMultimap.create();
    private final ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> diffBlocksPositions = ArrayListMultimap.create();
    private final Object2IntOpenHashMap<class_2680> correctStateCounts = new Object2IntOpenHashMap();
    private final Object2ObjectOpenHashMap<class_2338, BlockMismatch> blockMismatches = new Object2ObjectOpenHashMap();
    private final HashSet<Pair<class_2680, class_2680>> ignoredMismatches = new HashSet();
    private final List<class_2338> missingBlocksPositionsClosest = new ArrayList<class_2338>();
    private final List<class_2338> extraBlocksPositionsClosest = new ArrayList<class_2338>();
    private final List<class_2338> mismatchedBlocksPositionsClosest = new ArrayList<class_2338>();
    private final List<class_2338> mismatchedStatesPositionsClosest = new ArrayList<class_2338>();
    private final List<class_2338> diffBlocksPositionsClosest = new ArrayList<class_2338>();
    private final Set<MismatchType> selectedCategories = new HashSet<MismatchType>();
    private final HashMultimap<MismatchType, BlockMismatch> selectedEntries = HashMultimap.create();
    private final Set<class_1923> requiredChunks = new HashSet<class_1923>();
    private final Set<class_2338> recheckQueue = new HashSet<class_2338>();
    private final class_310 mc = class_310.method_1551();
    private class_638 worldClient;
    private WorldSchematic worldSchematic;
    private SchematicPlacement schematicPlacement;
    private final List<MismatchRenderPos> mismatchPositionsForRender = new ArrayList<MismatchRenderPos>();
    private final List<class_2338> mismatchBlockPositionsForRender = new ArrayList<class_2338>();
    private SortCriteria sortCriteria = SortCriteria.NAME_EXPECTED;
    private boolean sortReverse;
    private boolean verificationStarted;
    private boolean verificationActive;
    private boolean shouldRenderInfoHud = true;
    private int totalRequiredChunks;
    private int schematicBlocks;
    private int clientBlocks;
    private int correctStatesCount;
    private IgnoreBlockRegistry ignoreBlockRegistry;

    public SchematicVerifier() {
        this.name = StringUtils.translate((String)"litematica.gui.label.schematic_verifier.verifier", (Object[])new Object[0]);
    }

    public static void clearActiveVerifiers() {
        ACTIVE_VERIFIERS.clear();
    }

    public static void markVerifierBlockChanges(class_2338 pos) {
        for (int i = 0; i < ACTIVE_VERIFIERS.size(); ++i) {
            ACTIVE_VERIFIERS.get(i).markBlockChanged(pos);
        }
    }

    @Override
    public boolean getShouldRenderText(RenderPhase phase) {
        return this.shouldRenderInfoHud && phase == RenderPhase.POST && Configs.InfoOverlays.VERIFIER_OVERLAY_ENABLED.getBooleanValue();
    }

    public void toggleShouldRenderInfoHUD() {
        this.shouldRenderInfoHud = !this.shouldRenderInfoHud;
    }

    public boolean isActive() {
        return this.verificationActive;
    }

    public boolean isPaused() {
        return this.verificationStarted && !this.verificationActive && !this.finished;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public int getTotalChunks() {
        return this.totalRequiredChunks;
    }

    public int getUnseenChunks() {
        return this.requiredChunks.size();
    }

    public int getSchematicTotalBlocks() {
        return this.schematicBlocks;
    }

    public int getRealWorldTotalBlocks() {
        return this.clientBlocks;
    }

    public int getMissingBlocks() {
        return this.missingBlocksPositions.size();
    }

    public int getExtraBlocks() {
        return this.extraBlocksPositions.size();
    }

    public int getMismatchedBlocks() {
        return this.wrongBlocksPositions.size();
    }

    public int getMismatchedStates() {
        return this.wrongStatesPositions.size();
    }

    public int getDiffBlocks() {
        return this.diffBlocksPositions.size();
    }

    public int getCorrectStatesCount() {
        return this.correctStatesCount;
    }

    public int getTotalErrors() {
        return this.getMismatchedBlocks() + this.getMismatchedStates() + this.getExtraBlocks() + this.getMissingBlocks() + this.getDiffBlocks();
    }

    public SortCriteria getSortCriteria() {
        return this.sortCriteria;
    }

    public boolean getSortInReverse() {
        return this.sortReverse;
    }

    public void setSortCriteria(SortCriteria criteria) {
        if (this.sortCriteria == criteria) {
            this.sortReverse = !this.sortReverse;
        } else {
            this.sortCriteria = criteria;
            this.sortReverse = criteria != SortCriteria.COUNT;
        }
    }

    public void toggleMismatchCategorySelected(MismatchType type) {
        if (type == MismatchType.CORRECT_STATE) {
            return;
        }
        if (this.selectedCategories.contains((Object)type)) {
            this.selectedCategories.remove((Object)type);
        } else {
            this.selectedCategories.add(type);
            this.removeSelectedEntriesOfType(type);
        }
        this.updateMismatchOverlays();
    }

    public void toggleMismatchEntrySelected(BlockMismatch mismatch) {
        MismatchType type = mismatch.mismatchType;
        if (this.selectedEntries.containsValue((Object)mismatch)) {
            this.selectedEntries.remove((Object)type, (Object)mismatch);
        } else {
            this.selectedCategories.remove((Object)type);
            this.selectedEntries.put((Object)type, (Object)mismatch);
        }
        this.updateMismatchOverlays();
    }

    private void removeSelectedEntriesOfType(MismatchType type) {
        this.selectedEntries.removeAll((Object)type);
    }

    public boolean isMismatchCategorySelected(MismatchType type) {
        return this.selectedCategories.contains((Object)type);
    }

    public boolean isMismatchEntrySelected(BlockMismatch mismatch) {
        return this.selectedEntries.containsValue((Object)mismatch);
    }

    private void clearActiveMismatchRenderPositions() {
        this.mismatchPositionsForRender.clear();
        this.mismatchBlockPositionsForRender.clear();
        this.infoHudLines.clear();
    }

    public List<MismatchRenderPos> getSelectedMismatchPositionsForRender() {
        return this.mismatchPositionsForRender;
    }

    public List<class_2338> getSelectedMismatchBlockPositionsForRender() {
        return this.mismatchBlockPositionsForRender;
    }

    @Override
    public boolean shouldRemove() {
        return !this.canExecute();
    }

    @Override
    public boolean execute(class_3695 profiler) {
        this.verifyChunks(profiler);
        this.checkChangedPositions(profiler);
        return false;
    }

    @Override
    public void stop() {
    }

    public void startVerification(class_638 worldClient, WorldSchematic worldSchematic, SchematicPlacement schematicPlacement, ICompletionListener completionListener) {
        this.reset();
        this.worldClient = worldClient;
        this.worldSchematic = worldSchematic;
        this.schematicPlacement = schematicPlacement;
        this.ignoreBlockRegistry = new IgnoreBlockRegistry();
        this.setCompletionListener(completionListener);
        this.requiredChunks.addAll(schematicPlacement.getTouchedChunks());
        this.totalRequiredChunks = this.requiredChunks.size();
        this.verificationStarted = true;
        TaskScheduler.getInstanceClient().scheduleTask(this, 10);
        InfoHud.getInstance().addInfoHudRenderer(this, true);
        ACTIVE_VERIFIERS.add(this);
        this.verificationActive = true;
        this.updateRequiredChunksStringList();
    }

    public void resume() {
        if (this.verificationStarted) {
            this.verificationActive = true;
            this.updateRequiredChunksStringList();
        }
    }

    public void stopVerification() {
        this.verificationActive = false;
    }

    public void reset() {
        this.stopVerification();
        this.clearReferences();
        this.clearData();
    }

    private void clearReferences() {
        this.worldClient = null;
        this.worldSchematic = null;
        this.schematicPlacement = null;
    }

    private void clearData() {
        this.verificationActive = false;
        this.verificationStarted = false;
        this.finished = false;
        this.totalRequiredChunks = 0;
        this.correctStatesCount = 0;
        this.schematicBlocks = 0;
        this.clientBlocks = 0;
        this.requiredChunks.clear();
        this.recheckQueue.clear();
        this.missingBlocksPositions.clear();
        this.diffBlocksPositions.clear();
        this.extraBlocksPositions.clear();
        this.wrongBlocksPositions.clear();
        this.wrongStatesPositions.clear();
        this.blockMismatches.clear();
        this.correctStateCounts.clear();
        this.selectedCategories.clear();
        this.selectedEntries.clear();
        this.mismatchBlockPositionsForRender.clear();
        this.mismatchPositionsForRender.clear();
        ACTIVE_VERIFIERS.remove(this);
        TaskScheduler.getInstanceClient().removeTask(this);
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        this.clearActiveMismatchRenderPositions();
    }

    public void markBlockChanged(class_2338 pos) {
        BlockMismatch mismatch;
        if (this.finished && (mismatch = (BlockMismatch)this.blockMismatches.get((Object)pos)) != null) {
            this.recheckQueue.add(pos.method_10062());
        }
    }

    private void checkChangedPositions(class_3695 profiler) {
        profiler.method_15396("verify_check_pos");
        if (this.finished && !this.recheckQueue.isEmpty()) {
            Iterator<class_2338> iter = this.recheckQueue.iterator();
            while (iter.hasNext()) {
                class_2338 pos = iter.next();
                boolean isLoadedClient = this.worldClient.method_22340(pos);
                boolean isLoadedSchematic = this.worldSchematic.method_22340(pos);
                if (!isLoadedClient || !isLoadedSchematic) continue;
                BlockMismatch mismatch = (BlockMismatch)this.blockMismatches.get((Object)pos);
                if (mismatch != null) {
                    this.blockMismatches.remove((Object)pos);
                    class_2680 stateFound = this.worldClient.method_8320(pos);
                    MUTABLE_PAIR.setLeft((Object)mismatch.stateExpected);
                    MUTABLE_PAIR.setRight((Object)mismatch.stateFound);
                    this.getMapForMismatchType(mismatch.mismatchType).remove(MUTABLE_PAIR, (Object)pos);
                    this.checkBlockStates(pos.method_10263(), pos.method_10264(), pos.method_10260(), mismatch.stateExpected, stateFound);
                    if (!stateFound.method_26215() && mismatch.stateFound.method_26215()) {
                        ++this.clientBlocks;
                    }
                } else {
                    class_2680 stateExpected = this.worldSchematic.method_8320(pos);
                    class_2680 stateFound = this.worldClient.method_8320(pos);
                    this.checkBlockStates(pos.method_10263(), pos.method_10264(), pos.method_10260(), stateExpected, stateFound);
                }
                iter.remove();
            }
            if (this.recheckQueue.isEmpty()) {
                this.updateMismatchOverlays();
            }
        }
        profiler.method_15407();
    }

    private ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> getMapForMismatchType(MismatchType mismatchType) {
        return switch (mismatchType.ordinal()) {
            case 1 -> this.missingBlocksPositions;
            case 2 -> this.extraBlocksPositions;
            case 3 -> this.wrongBlocksPositions;
            case 4 -> this.wrongStatesPositions;
            case 6 -> this.diffBlocksPositions;
            default -> null;
        };
    }

    private boolean verifyChunks(class_3695 profiler) {
        profiler.method_15396("verify_chunks");
        if (this.verificationActive) {
            Iterator<class_1923> iter = this.requiredChunks.iterator();
            boolean checkedSome = false;
            while (iter.hasNext() && System.nanoTime() - DataManager.getClientTickStartTime() < 50000000L) {
                class_1923 pos = iter.next();
                int count = 0;
                for (int cx = pos.field_9181 - 1; cx <= pos.field_9181 + 1; ++cx) {
                    for (int cz = pos.field_9180 - 1; cz <= pos.field_9180 + 1; ++cz) {
                        if (!WorldUtils.isClientChunkLoaded(this.worldClient, cx, cz)) continue;
                        ++count;
                    }
                }
                if (count != 9 || !this.worldSchematic.getChunkProvider().method_12123(pos.field_9181, pos.field_9180)) continue;
                class_2818 chunkClient = this.worldClient.method_8497(pos.field_9181, pos.field_9180);
                ChunkSchematic chunkSchematic = this.worldSchematic.getChunk(pos.field_9181, pos.field_9180);
                ImmutableMap<String, IntBoundingBox> boxes = this.schematicPlacement.getBoxesWithinChunk(pos.field_9181, pos.field_9180);
                for (IntBoundingBox box : boxes.values()) {
                    this.verifyChunk((class_2791)chunkClient, (class_2791)chunkSchematic, box);
                }
                iter.remove();
                checkedSome = true;
            }
            if (checkedSome) {
                this.updateRequiredChunksStringList();
            }
            if (this.requiredChunks.isEmpty()) {
                this.verificationActive = false;
                this.verificationStarted = false;
                this.finished = true;
                this.notifyListener();
            }
        }
        profiler.method_15407();
        return !this.verificationActive;
    }

    public void ignoreStateMismatch(BlockMismatch mismatch) {
        this.ignoreStateMismatch(mismatch, true);
    }

    private void ignoreStateMismatch(BlockMismatch mismatch, boolean updateOverlay) {
        Pair ignore = Pair.of((Object)mismatch.stateExpected, (Object)mismatch.stateFound);
        if (!this.ignoredMismatches.contains(ignore)) {
            this.ignoredMismatches.add((Pair<class_2680, class_2680>)ignore);
            this.getMapForMismatchType(mismatch.mismatchType).removeAll((Object)ignore);
            this.blockMismatches.entrySet().removeIf(entry -> ((BlockMismatch)entry.getValue()).equals(mismatch));
        }
        if (updateOverlay) {
            this.updateMismatchOverlays();
        }
    }

    public void addIgnoredStateMismatches(Collection<BlockMismatch> ignore) {
        for (BlockMismatch mismatch : ignore) {
            this.ignoreStateMismatch(mismatch, false);
        }
        this.updateMismatchOverlays();
    }

    public void resetIgnoredStateMismatches() {
        this.ignoredMismatches.clear();
    }

    public Set<Pair<class_2680, class_2680>> getIgnoredMismatches() {
        return this.ignoredMismatches;
    }

    public Object2IntOpenHashMap<class_2680> getCorrectStates() {
        return this.correctStateCounts;
    }

    @Nullable
    public BlockMismatch getMismatchForPosition(class_2338 pos) {
        return (BlockMismatch)this.blockMismatches.get((Object)pos);
    }

    public List<BlockMismatch> getMismatchOverviewFor(MismatchType type) {
        ArrayList<BlockMismatch> list = new ArrayList<BlockMismatch>();
        if (type == MismatchType.ALL) {
            return this.getMismatchOverviewCombined();
        }
        this.addCountFor(type, this.getMapForMismatchType(type), list);
        return list;
    }

    public List<BlockMismatch> getMismatchOverviewCombined() {
        ArrayList<BlockMismatch> list = new ArrayList<BlockMismatch>();
        this.addCountFor(MismatchType.MISSING, this.missingBlocksPositions, list);
        this.addCountFor(MismatchType.EXTRA, this.extraBlocksPositions, list);
        this.addCountFor(MismatchType.WRONG_BLOCK, this.wrongBlocksPositions, list);
        this.addCountFor(MismatchType.WRONG_STATE, this.wrongStatesPositions, list);
        this.addCountFor(MismatchType.DIFF_BLOCK, this.diffBlocksPositions, list);
        Collections.sort(list);
        return list;
    }

    private void addCountFor(MismatchType mismatchType, ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> map, List<BlockMismatch> list) {
        for (Pair pair : map.keySet()) {
            list.add(new BlockMismatch(mismatchType, (class_2680)pair.getLeft(), (class_2680)pair.getRight(), map.get((Object)pair).size()));
        }
    }

    public List<Pair<class_2680, class_2680>> getIgnoredStateMismatchPairs(GuiBase gui) {
        ArrayList list = Lists.newArrayList(this.ignoredMismatches);
        try {
            list.sort((o1, o2) -> {
                String name2;
                String name1 = class_7923.field_41175.method_10221((Object)((class_2680)o1.getLeft()).method_26204()).toString();
                int val = name1.compareTo(name2 = class_7923.field_41175.method_10221((Object)((class_2680)o2.getLeft()).method_26204()).toString());
                if (val < 0) {
                    return -1;
                }
                if (val > 0) {
                    return 1;
                }
                name1 = class_7923.field_41175.method_10221((Object)((class_2680)o1.getRight()).method_26204()).toString();
                name2 = class_7923.field_41175.method_10221((Object)((class_2680)o2.getRight()).method_26204()).toString();
                return name1.compareTo(name2);
            });
        }
        catch (Exception e) {
            gui.addMessage(Message.MessageType.ERROR, "litematica.error.generic.failed_to_sort_list_of_ignored_states", new Object[0]);
        }
        return list;
    }

    private boolean verifyChunk(class_2791 chunkClient, class_2791 chunkSchematic, IntBoundingBox box) {
        LayerRange range = DataManager.getRenderLayerRange();
        class_2350.class_2351 axis = range.getAxis();
        boolean ranged = this.schematicPlacement.getSchematicVerifierType() == BlockInfoListType.RENDER_LAYERS;
        int startX = ranged && axis == class_2350.class_2351.field_11048 ? Math.max(box.minX, range.getLayerMin()) : box.minX;
        int startY = ranged && axis == class_2350.class_2351.field_11052 ? Math.max(box.minY, range.getLayerMin()) : box.minY;
        int startZ = ranged && axis == class_2350.class_2351.field_11051 ? Math.max(box.minZ, range.getLayerMin()) : box.minZ;
        int endX = ranged && axis == class_2350.class_2351.field_11048 ? Math.min(box.maxX, range.getLayerMax()) : box.maxX;
        int endY = ranged && axis == class_2350.class_2351.field_11052 ? Math.min(box.maxY, range.getLayerMax()) : box.maxY;
        int endZ = ranged && axis == class_2350.class_2351.field_11051 ? Math.min(box.maxZ, range.getLayerMax()) : box.maxZ;
        for (int y = startY; y <= endY; ++y) {
            for (int z = startZ; z <= endZ; ++z) {
                for (int x = startX; x <= endX; ++x) {
                    MUTABLE_POS.method_10103(x, y, z);
                    class_2680 stateClient = chunkClient.method_8320((class_2338)MUTABLE_POS);
                    class_2680 stateSchematic = chunkSchematic.method_8320((class_2338)MUTABLE_POS);
                    this.checkBlockStates(x, y, z, stateSchematic, stateClient);
                    if (!stateSchematic.method_26215()) {
                        ++this.schematicBlocks;
                    }
                    if (stateClient.method_26215()) continue;
                    ++this.clientBlocks;
                }
            }
        }
        return true;
    }

    private void checkBlockStates(int x, int y, int z, class_2680 stateSchematic, class_2680 stateClient) {
        class_2338 pos = new class_2338(x, y, z);
        if (!(stateClient == stateSchematic || stateClient.method_26215() && stateSchematic.method_26215())) {
            MUTABLE_PAIR.setLeft((Object)stateSchematic);
            MUTABLE_PAIR.setRight((Object)stateClient);
            if (!this.ignoredMismatches.contains(MUTABLE_PAIR)) {
                BlockMismatch mismatch = null;
                if (!stateSchematic.method_26215()) {
                    if (stateClient.method_26215()) {
                        mismatch = new BlockMismatch(MismatchType.MISSING, stateSchematic, stateClient, 1);
                        this.missingBlocksPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                    } else if (stateSchematic.method_26204() != stateClient.method_26204()) {
                        if (Configs.Generic.ENABLE_DIFFERENT_BLOCKS.getBooleanValue() && BlockUtils.isInSameGroup((class_2680)stateSchematic, (class_2680)stateClient)) {
                            if (BlockUtils.matchPropertiesOnly((class_2680)stateSchematic, (class_2680)stateClient)) {
                                mismatch = new BlockMismatch(MismatchType.DIFF_BLOCK, stateSchematic, stateClient, 1);
                                this.diffBlocksPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                            } else {
                                mismatch = new BlockMismatch(MismatchType.WRONG_STATE, stateSchematic, stateClient, 1);
                                this.wrongStatesPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                            }
                        } else {
                            mismatch = new BlockMismatch(MismatchType.WRONG_BLOCK, stateSchematic, stateClient, 1);
                            this.wrongBlocksPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                        }
                    } else {
                        mismatch = new BlockMismatch(MismatchType.WRONG_STATE, stateSchematic, stateClient, 1);
                        this.wrongStatesPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                    }
                } else if (!(Configs.Visuals.IGNORE_EXISTING_FLUIDS.getBooleanValue() && stateClient.method_51176() || this.ignoreBlockRegistry.hasBlock(stateClient.method_26204()))) {
                    mismatch = new BlockMismatch(MismatchType.EXTRA, stateSchematic, stateClient, 1);
                    this.extraBlocksPositions.put((Object)Pair.of((Object)stateSchematic, (Object)stateClient), (Object)pos);
                }
                if (mismatch != null) {
                    this.blockMismatches.put((Object)pos, (Object)mismatch);
                    ItemUtils.setItemForBlock((class_1937)this.worldClient, pos, stateClient);
                    ItemUtils.setItemForBlock(this.worldSchematic, pos, stateSchematic);
                }
            }
        } else {
            ItemUtils.setItemForBlock((class_1937)this.worldClient, pos, stateClient);
            this.correctStateCounts.addTo((Object)stateClient, 1);
            if (!stateSchematic.method_26215()) {
                ++this.correctStatesCount;
            }
        }
    }

    private void updateMismatchOverlays() {
        if (this.mc.field_1724 != null) {
            int maxEntries = Configs.InfoOverlays.VERIFIER_ERROR_HILIGHT_MAX_POSITIONS.getIntegerValue();
            class_2338 centerPos = class_2338.method_49638((class_2374)this.mc.field_1724.method_19538());
            this.updateClosestPositions(centerPos, maxEntries);
            this.combineClosestPositions(centerPos, maxEntries);
            if (this.selectedCategories.size() == 1 && this.selectedEntries.size() == 0) {
                MismatchType type = this.mismatchPositionsForRender.size() > 0 ? this.mismatchPositionsForRender.get((int)0).type : null;
                this.updateMismatchPositionStringList(type, this.mismatchPositionsForRender);
            } else {
                this.updateMismatchPositionStringList(null, this.mismatchPositionsForRender);
            }
        }
    }

    private void updateClosestPositions(class_2338 centerPos, int maxEntries) {
        PositionUtils.BLOCK_POS_COMPARATOR.setReferencePosition(centerPos);
        PositionUtils.BLOCK_POS_COMPARATOR.setClosestFirst(true);
        this.addAndSortPositions(MismatchType.DIFF_BLOCK, this.diffBlocksPositions, this.diffBlocksPositionsClosest, maxEntries);
        this.addAndSortPositions(MismatchType.WRONG_BLOCK, this.wrongBlocksPositions, this.mismatchedBlocksPositionsClosest, maxEntries);
        this.addAndSortPositions(MismatchType.WRONG_STATE, this.wrongStatesPositions, this.mismatchedStatesPositionsClosest, maxEntries);
        this.addAndSortPositions(MismatchType.EXTRA, this.extraBlocksPositions, this.extraBlocksPositionsClosest, maxEntries);
        this.addAndSortPositions(MismatchType.MISSING, this.missingBlocksPositions, this.missingBlocksPositionsClosest, maxEntries);
    }

    private void addAndSortPositions(MismatchType type, ArrayListMultimap<Pair<class_2680, class_2680>, class_2338> sourceMap, List<class_2338> listOut, int maxEntries) {
        listOut.clear();
        if (this.selectedCategories.contains((Object)type)) {
            listOut.addAll(sourceMap.values());
        } else {
            Set mismatches = this.selectedEntries.get((Object)type);
            for (BlockMismatch mismatch : mismatches) {
                MUTABLE_PAIR.setLeft((Object)mismatch.stateExpected);
                MUTABLE_PAIR.setRight((Object)mismatch.stateFound);
                listOut.addAll(sourceMap.get(MUTABLE_PAIR));
            }
        }
        listOut.sort(PositionUtils.BLOCK_POS_COMPARATOR);
    }

    private void combineClosestPositions(class_2338 centerPos, int maxEntries) {
        this.mismatchPositionsForRender.clear();
        this.mismatchBlockPositionsForRender.clear();
        ArrayList<MismatchRenderPos> tempList = new ArrayList<MismatchRenderPos>();
        this.getMismatchRenderPositionFor(MismatchType.WRONG_BLOCK, tempList);
        this.getMismatchRenderPositionFor(MismatchType.DIFF_BLOCK, tempList);
        this.getMismatchRenderPositionFor(MismatchType.WRONG_STATE, tempList);
        this.getMismatchRenderPositionFor(MismatchType.EXTRA, tempList);
        this.getMismatchRenderPositionFor(MismatchType.MISSING, tempList);
        tempList.sort(new RenderPosComparator(centerPos, true));
        int max = Math.min(maxEntries, tempList.size());
        for (int i = 0; i < max; ++i) {
            MismatchRenderPos entry = (MismatchRenderPos)tempList.get(i);
            this.mismatchPositionsForRender.add(entry);
            this.mismatchBlockPositionsForRender.add(entry.pos);
        }
    }

    private void getMismatchRenderPositionFor(MismatchType type, List<MismatchRenderPos> listOut) {
        List<class_2338> list = this.getClosestMismatchedPositionsFor(type);
        for (class_2338 pos : list) {
            listOut.add(new MismatchRenderPos(type, pos));
        }
    }

    private List<class_2338> getClosestMismatchedPositionsFor(MismatchType type) {
        return switch (type.ordinal()) {
            case 1 -> this.missingBlocksPositionsClosest;
            case 2 -> this.extraBlocksPositionsClosest;
            case 3 -> this.mismatchedBlocksPositionsClosest;
            case 4 -> this.mismatchedStatesPositionsClosest;
            case 6 -> this.diffBlocksPositionsClosest;
            default -> Collections.emptyList();
        };
    }

    private void updateMismatchPositionStringList(@Nullable MismatchType mismatchType, List<MismatchRenderPos> positionList) {
        this.infoHudLines.clear();
        if (!positionList.isEmpty()) {
            String rst = GuiBase.TXT_RST;
            if (mismatchType != null) {
                this.infoHudLines.add(String.format("%s%s%s", mismatchType.getFormattingCode(), mismatchType.getDisplayname(), rst));
            } else {
                String title = StringUtils.translate((String)"litematica.gui.title.schematic_verifier_errors", (Object[])new Object[0]);
                this.infoHudLines.add(String.format("%s%s%s", GuiBase.TXT_BOLD, title, rst));
            }
            int count = Math.min(positionList.size(), Configs.InfoOverlays.INFO_HUD_MAX_LINES.getIntegerValue());
            for (int i = 0; i < count; ++i) {
                MismatchRenderPos entry = positionList.get(i);
                class_2338 pos = entry.pos;
                String pre = entry.type.getColorCode();
                this.infoHudLines.add(String.format("%sx: %5d, y: %3d, z: %5d%s", pre, pos.method_10263(), pos.method_10264(), pos.method_10260(), rst));
            }
        }
    }

    public void updateRequiredChunksStringList() {
        this.updateInfoHudLinesPendingChunks(this.requiredChunks);
    }

    public static enum SortCriteria {
        NAME_EXPECTED,
        NAME_FOUND,
        COUNT;

    }

    public static enum MismatchType {
        ALL(0xFF0000, "litematica.gui.label.schematic_verifier_display_type.all", GuiBase.TXT_WHITE),
        MISSING(65535, "litematica.gui.label.schematic_verifier_display_type.missing", GuiBase.TXT_AQUA),
        EXTRA(0xFF00CF, "litematica.gui.label.schematic_verifier_display_type.extra", GuiBase.TXT_LIGHT_PURPLE),
        WRONG_BLOCK(0xFF0000, "litematica.gui.label.schematic_verifier_display_type.wrong_blocks", GuiBase.TXT_RED),
        WRONG_STATE(0xFFAF00, "litematica.gui.label.schematic_verifier_display_type.wrong_state", GuiBase.TXT_GOLD),
        CORRECT_STATE(0x11FF11, "litematica.gui.label.schematic_verifier_display_type.correct_state", GuiBase.TXT_GREEN),
        DIFF_BLOCK(0xFAF000, "litematica.gui.label.schematic_verifier_display_type.diff_blocks", GuiBase.TXT_YELLOW);

        private final String unlocName;
        private final String colorCode;
        private final Color4f color;

        private MismatchType(int color, String unlocName, String colorCode) {
            this.color = Color4f.fromColor((int)color, (float)1.0f);
            this.unlocName = unlocName;
            this.colorCode = colorCode;
        }

        public Color4f getColor() {
            return this.color;
        }

        public String getDisplayname() {
            return StringUtils.translate((String)this.unlocName, (Object[])new Object[0]);
        }

        public String getColorCode() {
            return this.colorCode;
        }

        public String getFormattingCode() {
            return this.colorCode + GuiBase.TXT_BOLD;
        }
    }

    public static class BlockMismatch
    implements Comparable<BlockMismatch> {
        public final MismatchType mismatchType;
        public final class_2680 stateExpected;
        public final class_2680 stateFound;
        public final int count;

        public BlockMismatch(MismatchType mismatchType, class_2680 stateExpected, class_2680 stateFound, int count) {
            this.mismatchType = mismatchType;
            this.stateExpected = stateExpected;
            this.stateFound = stateFound;
            this.count = count;
        }

        @Override
        public int compareTo(BlockMismatch other) {
            return this.count > other.count ? -1 : (this.count < other.count ? 1 : 0);
        }

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + (this.mismatchType == null ? 0 : this.mismatchType.hashCode());
            result = 31 * result + (this.stateExpected == null ? 0 : this.stateExpected.hashCode());
            result = 31 * result + (this.stateFound == null ? 0 : this.stateFound.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            BlockMismatch other = (BlockMismatch)obj;
            if (this.mismatchType != other.mismatchType) {
                return false;
            }
            if (this.stateExpected == null ? other.stateExpected != null : this.stateExpected != other.stateExpected) {
                return false;
            }
            return !(this.stateFound == null ? other.stateFound != null : this.stateFound != other.stateFound);
        }
    }

    public static class MismatchRenderPos {
        public final MismatchType type;
        public final class_2338 pos;

        public MismatchRenderPos(MismatchType type, class_2338 pos) {
            this.type = type;
            this.pos = pos;
        }
    }

    private static class RenderPosComparator
    implements Comparator<MismatchRenderPos> {
        private final class_2338 posReference;
        private final boolean closestFirst;

        public RenderPosComparator(class_2338 posReference, boolean closestFirst) {
            this.posReference = posReference;
            this.closestFirst = closestFirst;
        }

        @Override
        public int compare(MismatchRenderPos pos1, MismatchRenderPos pos2) {
            double dist2;
            double dist1 = pos1.pos.method_10262((class_2382)this.posReference);
            if (dist1 == (dist2 = pos2.pos.method_10262((class_2382)this.posReference))) {
                return 0;
            }
            return dist1 < dist2 == this.closestFirst ? -1 : 1;
        }
    }
}

