/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Queues
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  fi.dy.masa.malilib.util.LayerRange
 *  fi.dy.masa.malilib.util.PositionUtils
 *  fi.dy.masa.malilib.util.game.BlockUtils
 *  it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap
 *  it.unimi.dsi.fastutil.longs.LongArrayList
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1533
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2259
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_2487
 *  net.minecraft.class_2586
 *  net.minecraft.class_2625
 *  net.minecraft.class_2680
 *  net.minecraft.class_2791
 *  net.minecraft.class_2818
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_3481
 *  net.minecraft.class_3532
 *  net.minecraft.class_3695
 *  net.minecraft.class_3965
 *  net.minecraft.class_4538
 *  net.minecraft.class_5455
 *  net.minecraft.class_638
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7923
 *  net.minecraft.class_9279
 *  net.minecraft.class_9334
 */
package fi.dy.masa.litematica.scheduler.tasks;

import com.google.common.collect.Queues;
import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.mixin.block.IMixinAbstractBlock;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskPasteSchematicPerChunkBase;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkMultiPhase;
import fi.dy.masa.litematica.schematic.placement.SchematicPlacement;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.PasteNbtBehavior;
import fi.dy.masa.litematica.util.ReplaceBehavior;
import fi.dy.masa.litematica.world.ChunkSchematic;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import fi.dy.masa.malilib.util.LayerRange;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.malilib.util.game.BlockUtils;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Queue;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2259;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_2487;
import net.minecraft.class_2586;
import net.minecraft.class_2625;
import net.minecraft.class_2680;
import net.minecraft.class_2791;
import net.minecraft.class_2818;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3481;
import net.minecraft.class_3532;
import net.minecraft.class_3695;
import net.minecraft.class_3965;
import net.minecraft.class_4538;
import net.minecraft.class_5455;
import net.minecraft.class_638;
import net.minecraft.class_7225;
import net.minecraft.class_7923;
import net.minecraft.class_9279;
import net.minecraft.class_9334;

public class TaskPasteSchematicPerChunkCommand
extends TaskPasteSchematicPerChunkBase {
    protected final Queue<String> queuedCommands = Queues.newArrayDeque();
    protected final Long2LongOpenHashMap placedPositionTimestamps = new Long2LongOpenHashMap();
    protected final LongArrayList fillVolumes = new LongArrayList();
    protected final class_2338.class_2339 mutablePos = new class_2338.class_2339();
    protected final PasteNbtBehavior nbtBehavior;
    protected final String cloneCommand;
    protected final String fillCommand;
    protected final String setBlockCommand;
    protected final String summonCommand;
    protected final int maxBoxVolume;
    protected final boolean useFillCommand;
    protected final boolean useWorldEdit;
    protected int[][][] workArr;
    protected int maxCommandLength = 255;
    protected int sentFillCommands;
    protected int sentSetblockCommands;
    protected String useStrict;

    public TaskPasteSchematicPerChunkCommand(Collection<SchematicPlacement> placements, LayerRange range, boolean changedBlocksOnly) {
        super(placements, range, changedBlocksOnly);
        this.maxCommandsPerTick = Configs.Generic.COMMAND_LIMIT.getIntegerValue();
        this.maxBoxVolume = Configs.Generic.COMMAND_FILL_MAX_VOLUME.getIntegerValue();
        this.cloneCommand = Configs.Generic.COMMAND_NAME_CLONE.getStringValue();
        this.fillCommand = Configs.Generic.COMMAND_NAME_FILL.getStringValue();
        this.setBlockCommand = Configs.Generic.COMMAND_NAME_SETBLOCK.getStringValue();
        this.summonCommand = Configs.Generic.COMMAND_NAME_SUMMON.getStringValue();
        this.useFillCommand = Configs.Generic.PASTE_USE_FILL_COMMAND.getBooleanValue();
        this.useWorldEdit = Configs.Generic.COMMAND_USE_WORLDEDIT.getBooleanValue();
        this.useStrict = "";
        this.nbtBehavior = (PasteNbtBehavior)Configs.Generic.PASTE_NBT_BEHAVIOR.getOptionListValue();
        this.processBoxBlocksTask = this.useFillCommand ? this::processBlocksInCurrentBoxUsingFill : this::processBlocksInCurrentBoxUsingSetBlockOnly;
        this.processBoxEntitiesTask = this::processEntitiesInCurrentBox;
    }

    @Override
    public boolean execute(class_3695 profiler) {
        if (this.ignoreBlocks && this.ignoreEntities) {
            return true;
        }
        return this.executeMultiPhase(profiler);
    }

    @Override
    public void init() {
        super.init();
        if (this.useWorldEdit && this.isInWorld()) {
            this.sendCommand("/perf neighbors off");
        }
    }

    @Override
    protected void onNextChunkFetched(class_1923 pos) {
        this.startNextBox(pos);
    }

    @Override
    protected void onStartNextBox(IntBoundingBox box) {
        if (!this.ignoreBlocks) {
            this.prepareSettingBlocks(box);
        } else {
            this.prepareSummoningEntities(box);
        }
    }

    protected void prepareSettingBlocks(IntBoundingBox box) {
        if (this.useFillCommand) {
            this.generateFillVolumes(box);
        } else {
            this.positionIterator = class_2338.method_10094((int)box.minX, (int)box.minY, (int)box.minZ, (int)box.maxX, (int)box.maxY, (int)box.maxZ).iterator();
        }
        this.phase = TaskProcessChunkMultiPhase.TaskPhase.PROCESS_BOX_BLOCKS;
    }

    protected void prepareSummoningEntities(IntBoundingBox box) {
        class_238 bb = new class_238((double)box.minX, (double)box.minY, (double)box.minZ, (double)(box.maxX + 1), (double)(box.maxY + 1), (double)(box.maxZ + 1));
        this.entityIterator = this.schematicWorld.method_8333(null, bb, e -> true).iterator();
        this.phase = TaskProcessChunkMultiPhase.TaskPhase.PROCESS_BOX_ENTITIES;
    }

    @Override
    protected void sendQueuedCommands() {
        while (this.sentCommandsThisTick < this.maxCommandsPerTick && !this.queuedCommands.isEmpty()) {
            this.sendCommand(this.queuedCommands.poll());
        }
    }

    protected void processBlocksInCurrentBoxUsingSetBlockOnly() {
        class_1923 chunkPos = this.currentChunkPos;
        ChunkSchematic schematicChunk = this.schematicWorld.getChunkProvider().getChunk(chunkPos.field_9181, chunkPos.field_9180);
        class_2818 clientChunk = this.mc.field_1687.method_8497(chunkPos.field_9181, chunkPos.field_9180);
        boolean ignoreLimit = Configs.Generic.PASTE_IGNORE_CMD_LIMIT.getBooleanValue();
        while (this.positionIterator.hasNext() && this.queuedCommands.size() < this.maxCommandsPerTick && (!ignoreLimit || this.sentCommandsThisTick < this.maxCommandsPerTick)) {
            class_2338 pos = (class_2338)this.positionIterator.next();
            this.pasteBlock(pos, schematicChunk, (class_2791)clientChunk, ignoreLimit);
        }
        this.sendQueuedCommands();
        if (!this.positionIterator.hasNext() && this.queuedCommands.isEmpty()) {
            if (this.ignoreEntities) {
                this.onFinishedProcessingBox(this.currentChunkPos, this.currentBox);
            } else {
                this.prepareSummoningEntities(this.currentBox);
            }
        }
    }

    protected void processBlocksInCurrentBoxUsingFill() {
        class_1923 chunkPos = this.currentChunkPos;
        int baseX = chunkPos.field_9181 << 4;
        int baseZ = chunkPos.field_9180 << 4;
        ChunkSchematic schematicChunk = this.schematicWorld.getChunkProvider().getChunk(chunkPos.field_9181, chunkPos.field_9180);
        class_2818 clientChunk = this.mc.field_1687.method_8497(chunkPos.field_9181, chunkPos.field_9180);
        while (!this.fillVolumes.isEmpty() && this.queuedCommands.size() < this.maxCommandsPerTick) {
            int index = this.fillVolumes.size() - 1;
            long encodedValue = this.fillVolumes.removeLong(index);
            this.fillVolume(encodedValue, baseX, baseZ, schematicChunk, (class_2791)clientChunk);
        }
        this.sendQueuedCommands();
        if (this.fillVolumes.isEmpty() && this.queuedCommands.isEmpty()) {
            if (this.ignoreEntities) {
                this.onFinishedProcessingBox(this.currentChunkPos, this.currentBox);
            } else {
                this.prepareSummoningEntities(this.currentBox);
            }
        }
    }

    protected void processEntitiesInCurrentBox() {
        while (this.entityIterator.hasNext() && this.queuedCommands.size() < this.maxCommandsPerTick) {
            this.summonEntity((class_1297)this.entityIterator.next());
        }
        this.sendQueuedCommands();
        if (!this.entityIterator.hasNext() && this.queuedCommands.isEmpty()) {
            this.onFinishedProcessingBox(this.currentChunkPos, this.currentBox);
        }
    }

    protected void pasteBlock(class_2338 pos, class_2818 schematicChunk, class_2791 clientChunk, boolean ignoreLimit) {
        class_2680 stateClient;
        class_2680 stateSchematic = schematicChunk.method_8320(pos);
        if (this.shouldSetBlock(stateSchematic, stateClient = clientChunk.method_8320(pos))) {
            if (this.useSpecialPasting(stateSchematic)) {
                this.specialPasteBlock(pos, stateSchematic, this.schematicWorld, this.queuedCommands::offer);
                return;
            }
            PasteNbtBehavior nbtBehavior = this.nbtBehavior;
            class_2586 be = schematicChunk.method_8321(pos);
            if (be != null && nbtBehavior != PasteNbtBehavior.NONE && !this.useWorldEdit) {
                Consumer<String> commandHandler = ignoreLimit ? this::sendCommand : this.queuedCommands::offer;
                class_1937 schematicWorld = schematicChunk.method_12200();
                if (nbtBehavior == PasteNbtBehavior.PLACE_MODIFY) {
                    this.setDataViaDataModify(pos, stateSchematic, be, schematicWorld, this.mc.field_1687, commandHandler);
                } else if (nbtBehavior == PasteNbtBehavior.PLACE_CLONE) {
                    this.placeBlockViaClone(pos, stateSchematic, be, schematicWorld, this.mc.field_1687, commandHandler);
                }
            } else {
                this.queueSetBlockCommand(pos.method_10263(), pos.method_10264(), pos.method_10260(), stateSchematic);
            }
        }
    }

    protected boolean useSpecialPasting(class_2680 state) {
        return !this.useWorldEdit && state.method_26164(class_3481.field_41282);
    }

    protected boolean shouldSetBlock(class_2680 stateSchematic, class_2680 stateClient) {
        if (stateSchematic.method_31709() && Configs.Generic.PASTE_IGNORE_BE_ENTIRELY.getBooleanValue()) {
            return false;
        }
        if (stateSchematic.method_26215() && stateClient.method_26215() || this.changedBlockOnly && stateClient == stateSchematic) {
            return false;
        }
        return !(this.replace == ReplaceBehavior.NONE && !stateClient.method_26215() || this.replace == ReplaceBehavior.WITH_NON_AIR && stateSchematic.method_26215());
    }

    protected void summonEntity(class_1297 entity) {
        String id = EntityUtils.getEntityId(entity);
        if (id != null) {
            String command = String.format(Locale.ROOT, "%s %s %f %f %f", this.summonCommand, id, entity.method_23317(), entity.method_23318(), entity.method_23321());
            if (entity instanceof class_1533) {
                class_1533 itemFrame = (class_1533)entity;
                command = this.getSummonCommandForItemFrame(itemFrame, command);
            }
            this.queuedCommands.offer(command);
        }
    }

    protected String getSummonCommandForItemFrame(class_1533 itemFrame, String originalCommand) {
        class_1799 stack = itemFrame.method_6940();
        if (!stack.method_7960()) {
            class_2960 itemId = class_7923.field_41178.method_10221((Object)stack.method_7909());
            int facingId = itemFrame.method_5735().method_10146();
            String nbtStr = String.format(" {Facing:%db,Item:{id:\"%s\",Count:1b}}", facingId, itemId);
            class_9279 entityComp = (class_9279)stack.method_57824(class_9334.field_49609);
            if (entityComp != null && !entityComp.method_57458()) {
                String itemNbt = entityComp.toString();
                String tmp = String.format(" {Facing:%db,Item:{id:\"%s\",Count:1b,tag:%s}}", facingId, itemId, itemNbt);
                if (originalCommand.length() + tmp.length() < 255) {
                    nbtStr = tmp;
                }
            }
            return originalCommand + nbtStr;
        }
        return originalCommand;
    }

    protected void queueSetBlockCommand(int x, int y, int z, class_2680 state) {
        this.queueSetBlockCommand(x, y, z, state, this.queuedCommands::offer);
    }

    protected void queueSetBlockCommand(int x, int y, int z, class_2680 state, Consumer<String> commandHandler) {
        String blockString = class_2259.method_9685((class_2680)state);
        if (this.useWorldEdit) {
            commandHandler.accept(String.format("/pos1 %d,%d,%d", x, y, z));
            commandHandler.accept(String.format("/pos2 %d,%d,%d", x, y, z));
            commandHandler.accept("/set " + blockString);
        } else {
            String cmdName = this.setBlockCommand;
            String cmd = String.format("%s %d %d %d %s%s", cmdName, x, y, z, blockString, this.useStrict);
            commandHandler.accept(cmd);
        }
        ++this.sentSetblockCommands;
    }

    protected void pasteVolume(int x1, int y1, int z1, int x2, int y2, int z2, class_2680 state) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);
        int singleLayerVolume = (maxX - minX + 1) * (maxZ - minZ + 1);
        int totalVolume = singleLayerVolume * (maxY - minY + 1);
        if (totalVolume <= this.maxBoxVolume || this.useWorldEdit) {
            this.queueFillCommandForBox(minX, minY, minZ, maxX, maxY, maxZ, state);
        } else {
            int singleBoxHeight = this.maxBoxVolume / singleLayerVolume;
            if (singleBoxHeight < 1) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Error: Calculated single box height was less than 1 block", (Object[])new Object[0]);
                return;
            }
            for (int y = minY; y <= maxY; y += singleBoxHeight) {
                int boxMaxY = Math.min(y + singleBoxHeight - 1, maxY);
                this.queueFillCommandForBox(minX, y, minZ, maxX, boxMaxY, maxZ, state);
            }
        }
    }

    protected void queueFillCommandForBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, class_2680 state) {
        String blockString = class_2259.method_9685((class_2680)state);
        if (this.useWorldEdit) {
            this.queuedCommands.offer(String.format("/pos1 %d,%d,%d", minX, minY, minZ));
            this.queuedCommands.offer(String.format("/pos2 %d,%d,%d", maxX, maxY, maxZ));
            this.queuedCommands.offer("/set " + blockString);
        } else {
            String cmdName = this.fillCommand;
            Object fillCommand = String.format("%s %d %d %d %d %d %d %s", cmdName, minX, minY, minZ, maxX, maxY, maxZ, blockString);
            if (this.replace == ReplaceBehavior.NONE || this.replace == ReplaceBehavior.WITH_NON_AIR && state.method_26215()) {
                fillCommand = (String)fillCommand + " replace air";
            }
            fillCommand = (String)fillCommand + this.useStrict;
            this.queuedCommands.offer((String)fillCommand);
        }
        ++this.sentFillCommands;
    }

    protected void setDataViaDataModify(class_2338 pos, class_2680 state, class_2586 be, class_1937 schematicWorld, class_638 clientWorld, Consumer<String> commandHandler) {
        class_2338 placementPos = this.placeNbtPickedBlock(pos, state, be, schematicWorld, clientWorld);
        if (placementPos != null) {
            this.queueSetBlockCommand(pos.method_10263(), pos.method_10264(), pos.method_10260(), state, commandHandler);
            try {
                HashSet keys = new HashSet(be.method_38244((class_7225.class_7874)clientWorld.method_30349()).method_10541());
                keys.remove("id");
                keys.remove("x");
                keys.remove("y");
                keys.remove("z");
                for (String key : keys) {
                    String command = String.format("data modify block %d %d %d %s set from block %d %d %d %s", pos.method_10263(), pos.method_10264(), pos.method_10260(), key, placementPos.method_10263(), placementPos.method_10264(), placementPos.method_10260(), key);
                    commandHandler.accept(command);
                }
            }
            catch (Exception keys) {
                // empty catch block
            }
            String cmdName = this.setBlockCommand;
            String command = String.format("%s %d %d %d air%s", cmdName, placementPos.method_10263(), placementPos.method_10264(), placementPos.method_10260(), this.useStrict);
            commandHandler.accept(command);
        }
    }

    protected void specialPasteBlock(class_2338 pos, class_2680 state, class_1937 schematicWorld, Consumer<String> commandHandler) {
        if (state.method_26164(class_3481.field_41282)) {
            this.specialPasteSignBlock(pos, state, schematicWorld, commandHandler);
        }
    }

    protected void specialPasteSignBlock(class_2338 pos, class_2680 state, class_1937 schematicWorld, Consumer<String> commandHandler) {
        class_2586 be = schematicWorld.method_8321(pos);
        String cmdName = this.setBlockCommand;
        String blockString = class_2259.method_9685((class_2680)state);
        if (be instanceof class_2625) {
            class_2625 signBe = (class_2625)be;
            class_2487 tag = be.method_38244((class_7225.class_7874)schematicWorld.method_30349());
            if (tag != null) {
                if (!signBe.method_49854().method_49861((class_1657)this.mc.field_1724)) {
                    tag.method_10551("back_text");
                }
                if (!signBe.method_49853().method_49861((class_1657)this.mc.field_1724)) {
                    tag.method_10551("front_text");
                }
                if (!signBe.method_49855()) {
                    tag.method_10551("is_waxed");
                }
                tag.method_10551("components");
                String cmd = String.format("%s %d %d %d %s%s%s", cmdName, pos.method_10263(), pos.method_10264(), pos.method_10260(), blockString, tag.toString(), this.useStrict);
                if (cmd.length() <= this.maxCommandLength) {
                    commandHandler.accept(cmd);
                    ++this.sentSetblockCommands;
                    return;
                }
            }
        }
        String cmd = String.format("%s %d %d %d %s%s", cmdName, pos.method_10263(), pos.method_10264(), pos.method_10260(), blockString, this.useStrict);
        commandHandler.accept(cmd);
        ++this.sentSetblockCommands;
    }

    protected void placeBlockViaClone(class_2338 pos, class_2680 state, class_2586 be, class_1937 schematicWorld, class_638 clientWorld, Consumer<String> commandHandler) {
        class_2338 placementPos = this.placeNbtPickedBlock(pos, state, be, schematicWorld, clientWorld);
        if (placementPos != null) {
            String command = String.format("%s %d %d %d %d %d %d %d %d %d%s", this.cloneCommand, placementPos.method_10263(), placementPos.method_10264(), placementPos.method_10260(), placementPos.method_10263(), placementPos.method_10264(), placementPos.method_10260(), pos.method_10263(), pos.method_10264(), pos.method_10260(), this.useStrict);
            commandHandler.accept(command);
            String cmdName = this.setBlockCommand;
            command = String.format("%s %d %d %d air%s", cmdName, placementPos.method_10263(), placementPos.method_10264(), placementPos.method_10260(), this.useStrict);
            commandHandler.accept(command);
        }
    }

    @Nullable
    protected class_2338 placeNbtPickedBlock(class_2338 pos, class_2680 state, class_2586 be, @Nonnull class_1937 schematicWorld, @Nonnull class_638 clientWorld) {
        double reach = this.mc.field_1724.method_55754();
        class_2338 placementPos = this.findEmptyNearbyPosition((class_1937)clientWorld, this.mc.field_1724.method_19538(), 4, reach);
        if (placementPos != null && TaskPasteSchematicPerChunkCommand.preparePickedStack(pos, state, be, schematicWorld, this.mc, clientWorld.method_30349())) {
            class_243 posVec = new class_243((double)placementPos.method_10263() + 0.5, (double)placementPos.method_10264() + 0.5, (double)placementPos.method_10260() + 0.5);
            class_3965 hitResult = new class_3965(posVec, class_2350.field_11036, placementPos, true);
            this.mc.field_1761.method_2896(this.mc.field_1724, class_1268.field_5810, hitResult);
            this.placedPositionTimestamps.put(placementPos.method_10063(), System.nanoTime());
            this.mc.field_1724.method_31548().method_5447(40, class_1799.field_8037);
            return placementPos;
        }
        return null;
    }

    protected void fillVolume(long encodedValue, int baseX, int baseZ, ChunkSchematic schematicChunk, class_2791 clientChunk) {
        int startPos = (int)encodedValue;
        int packedOffset = TaskPasteSchematicPerChunkCommand.getPackedSize(encodedValue);
        int startX = TaskPasteSchematicPerChunkCommand.unpackX(startPos) + baseX;
        int startY = TaskPasteSchematicPerChunkCommand.unpackY(startPos);
        int startZ = TaskPasteSchematicPerChunkCommand.unpackZ(startPos) + baseZ;
        int endOffsetX = TaskPasteSchematicPerChunkCommand.unpackX(packedOffset);
        int endOffsetY = TaskPasteSchematicPerChunkCommand.unpackY(packedOffset);
        int endOffsetZ = TaskPasteSchematicPerChunkCommand.unpackZ(packedOffset);
        this.mutablePos.method_10103(startX, startY, startZ);
        if (endOffsetX > 0 || endOffsetY > 0 || endOffsetZ > 0 || Configs.Generic.PASTE_ALWAYS_USE_FILL.getBooleanValue()) {
            int endX = startX + endOffsetX;
            int endY = startY + endOffsetY;
            int endZ = startZ + endOffsetZ;
            class_2680 state = schematicChunk.method_8320((class_2338)this.mutablePos);
            this.pasteVolume(startX, startY, startZ, endX, endY, endZ, state);
        } else {
            this.pasteBlock((class_2338)this.mutablePos, schematicChunk, clientChunk, false);
        }
    }

    protected void generateFillVolumes(IntBoundingBox box) {
        ChunkSchematic chunk = this.schematicWorld.getChunkProvider().getChunk(box.minX >> 4, box.minZ >> 4);
        boolean ignoreBeFromFill = Configs.Generic.PASTE_IGNORE_BE_IN_FILL.getBooleanValue() && Configs.Generic.PASTE_NBT_BEHAVIOR.getOptionListValue() != PasteNbtBehavior.NONE;
        this.fillVolumes.clear();
        if (this.workArr == null) {
            int height = this.world.method_31605();
            this.workArr = new int[16][height][16];
        }
        this.generateStrips(this.workArr, class_2350.field_11034, box, chunk, ignoreBeFromFill);
        this.combineStripsToLayers(this.workArr, class_2350.field_11034, class_2350.field_11035, class_2350.field_11036, box, chunk, this.fillVolumes, ignoreBeFromFill);
        Collections.reverse(this.fillVolumes);
    }

    protected int getBlockStripLength(class_2338.class_2339 pos, class_2350 direction, int maxLength, class_2680 firstState, class_2791 chunk) {
        int length;
        for (length = 1; length < maxLength; ++length) {
            pos.method_10098(direction);
            class_2680 state = chunk.method_8320((class_2338)pos);
            if (state != firstState) break;
        }
        return length;
    }

    protected void generateStrips(int[][][] workArr, class_2350 stripDirection, IntBoundingBox box, ChunkSchematic chunk, boolean ignoreBeFromFill) {
        boolean ignoreBeEntirely = Configs.Generic.PASTE_IGNORE_BE_ENTIRELY.getBooleanValue();
        class_2338.class_2339 mutablePos = this.mutablePos;
        ReplaceBehavior replace = this.replace;
        int startX = box.minX & 0xF;
        int startZ = box.minZ & 0xF;
        int endX = box.maxX & 0xF;
        int endZ = box.maxZ & 0xF;
        int worldMinY = chunk.method_31607();
        for (int y = box.minY; y <= box.maxY; ++y) {
            for (int z = startZ; z <= endZ; ++z) {
                for (int x = startX; x <= endX; ++x) {
                    int length;
                    mutablePos.method_10103(x, y, z);
                    class_2680 state = chunk.method_8320((class_2338)mutablePos);
                    if (state.method_26215() && replace != ReplaceBehavior.ALL) continue;
                    if (state.method_31709()) {
                        if (ignoreBeFromFill) {
                            workArr[x][y - worldMinY][z] = 1;
                            continue;
                        }
                        if (ignoreBeEntirely) continue;
                    }
                    workArr[x][y - worldMinY][z] = length = this.getBlockStripLength(mutablePos, stripDirection, endX - x + 1, state, (class_2791)chunk);
                    x += length - 1;
                }
            }
        }
    }

    protected void combineStripsToLayers(int[][][] workArr, class_2350 stripDirection, class_2350 stripCombineDirection, class_2350 layerCombineDirection, IntBoundingBox box, ChunkSchematic chunk, LongArrayList volumesOut, boolean ignoreBe) {
        int nextZ;
        class_2680 state;
        int nextY;
        int nextX;
        class_2338.class_2339 mutablePos = this.mutablePos;
        int sdOffX = stripDirection.method_10148();
        int sdOffY = stripDirection.method_10164();
        int sdOffZ = stripDirection.method_10165();
        int scOffX = stripCombineDirection.method_10148();
        int scOffY = stripCombineDirection.method_10164();
        int scOffZ = stripCombineDirection.method_10165();
        int lcOffX = layerCombineDirection.method_10148();
        int lcOffY = layerCombineDirection.method_10164();
        int lcOffZ = layerCombineDirection.method_10165();
        int startX = box.minX & 0xF;
        int startZ = box.minZ & 0xF;
        int endX = box.maxX & 0xF;
        int endZ = box.maxZ & 0xF;
        int worldMinY = chunk.method_31607();
        for (int y = box.minY; y <= box.maxY; ++y) {
            for (int x = startX; x <= endX; ++x) {
                for (int z = startZ; z <= endZ; ++z) {
                    int packedSize;
                    int length = workArr[x][y - worldMinY][z];
                    if (length <= 0) continue;
                    nextX = x + scOffX;
                    nextY = y + scOffY;
                    int stripCount = 1;
                    mutablePos.method_10103(x, y, z);
                    state = chunk.method_8320((class_2338)mutablePos);
                    if (!ignoreBe || !state.method_31709()) {
                        for (nextZ = z + scOffZ; nextX <= 15 && nextY <= box.maxY && nextZ <= 15 && workArr[nextX][nextY - worldMinY][nextZ] == length && chunk.method_8320((class_2338)mutablePos.method_10103(nextX, nextY, nextZ)) == state; nextX += scOffX, nextY += scOffY, nextZ += scOffZ) {
                            ++stripCount;
                            workArr[nextX][nextY - worldMinY][nextZ] = 0;
                        }
                    }
                    int packedX = sdOffX * length + scOffX * stripCount;
                    int packedY = sdOffY * length + scOffY * stripCount;
                    int packedZ = sdOffZ * length + scOffZ * stripCount;
                    workArr[x][y - worldMinY][z] = packedSize = TaskPasteSchematicPerChunkCommand.packCoordinate5bit(packedX, packedY, packedZ);
                    if (stripCount <= 1) continue;
                    int extraStrips = stripCount - 1;
                    x += scOffX * extraStrips;
                    y += scOffY * extraStrips;
                    z += scOffZ * extraStrips;
                }
            }
        }
        for (int x = startX; x <= endX; ++x) {
            for (int z = startZ; z <= endZ; ++z) {
                for (int y = box.minY; y <= box.maxY; ++y) {
                    int packedSize = workArr[x][y - worldMinY][z];
                    if (packedSize == 0) continue;
                    nextX = x + lcOffX;
                    nextY = y + lcOffY;
                    int layerCount = 1;
                    mutablePos.method_10103(x, y, z);
                    state = chunk.method_8320((class_2338)mutablePos);
                    if (!ignoreBe || !state.method_31709()) {
                        for (nextZ = z + lcOffZ; nextX <= 15 && nextY <= box.maxY && nextZ <= 15 && workArr[nextX][nextY - worldMinY][nextZ] == packedSize && chunk.method_8320((class_2338)mutablePos.method_10103(nextX, nextY, nextZ)) == state; nextX += lcOffX, nextY += lcOffY, nextZ += lcOffZ) {
                            ++layerCount;
                            workArr[nextX][nextY - worldMinY][nextZ] = 0;
                        }
                    }
                    int volumeEndOffsetX = lcOffX * layerCount + TaskPasteSchematicPerChunkCommand.unpackX5bit(packedSize) - 1;
                    int volumeEndOffsetY = lcOffY * layerCount + TaskPasteSchematicPerChunkCommand.unpackY5bit(packedSize) - 1;
                    int volumeEndOffsetZ = lcOffZ * layerCount + TaskPasteSchematicPerChunkCommand.unpackZ5bit(packedSize) - 1;
                    int packedVolumeEndOffset = TaskPasteSchematicPerChunkCommand.packCoordinate(volumeEndOffsetX, volumeEndOffsetY, volumeEndOffsetZ);
                    long encodedValue = (long)packedVolumeEndOffset << 32 | (long)TaskPasteSchematicPerChunkCommand.packCoordinate(x, y, z) & 0xFFFFFFFFL;
                    volumesOut.add(encodedValue);
                    workArr[x][y - worldMinY][z] = 0;
                    if (layerCount <= 1) continue;
                    int extraLayers = layerCount - 1;
                    x += lcOffX * extraLayers;
                    y += lcOffY * extraLayers;
                    z += lcOffZ * extraLayers;
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if (this.finished) {
            if (this.printCompletionMessage) {
                if (this.useWorldEdit) {
                    InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.schematic_pasted_using_world_edit", (Object[])new Object[]{this.sentSetblockCommands + this.sentFillCommands});
                } else if (this.useFillCommand) {
                    InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.schematic_pasted_using_fill_and_setblock", (Object[])new Object[]{this.sentFillCommands, this.sentSetblockCommands});
                } else {
                    InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.INFO, (String)"litematica.message.schematic_pasted_using_setblock", (Object[])new Object[]{this.sentSetblockCommands});
                }
            }
        } else {
            InfoUtils.showGuiOrActionBarMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.error.schematic_paste_failed", (Object[])new Object[0]);
        }
        this.sendTaskEndCommands();
        DataManager.removeChatListener(this.gameRuleListener);
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        super.onStop();
    }

    protected static int unpackX(int value) {
        return value & 0xF;
    }

    protected static int unpackY(int value) {
        return value >> 8;
    }

    protected static int unpackZ(int value) {
        return value >> 4 & 0xF;
    }

    protected static int packCoordinate(int x, int y, int z) {
        return y << 8 | (z & 0xF) << 4 | x & 0xF;
    }

    protected static int unpackX5bit(int value) {
        return value & 0x1F;
    }

    protected static int unpackY5bit(int value) {
        return value >> 10;
    }

    protected static int unpackZ5bit(int value) {
        return value >> 5 & 0x1F;
    }

    protected static int packCoordinate5bit(int x, int y, int z) {
        return y << 10 | (z & 0x1F) << 5 | x & 0x1F;
    }

    protected static int getPackedSize(long fullPackedValue) {
        return (int)(fullPackedValue >> 32);
    }

    @Nullable
    public class_2338 findEmptyNearbyPosition(class_1937 world, class_243 centerPos, int radius, double reachDistance) {
        class_2338.class_2339 pos = new class_2338.class_2339();
        class_2338.class_2339 sidePos = new class_2338.class_2339();
        long currentTime = System.nanoTime();
        long timeout = 2000000000L;
        double squaredReach = reachDistance * reachDistance;
        int radiusY = Math.min(radius, 2);
        for (double y = centerPos.method_10214() - (double)radiusY; y <= centerPos.method_10214() + (double)radiusY; y += 1.0) {
            for (double z = centerPos.method_10215() - (double)radius; z <= centerPos.method_10215() + (double)radius; z += 1.0) {
                for (double x = centerPos.method_10216() - (double)radius; x <= centerPos.method_10216() + (double)radius; x += 1.0) {
                    if (centerPos.method_1028(x, y, z) > squaredReach || class_3532.method_15375((float)class_3532.method_15379((float)((float)(centerPos.method_10216() - x)))) < 2 && class_3532.method_15375((float)class_3532.method_15379((float)((float)(centerPos.method_10215() - z)))) < 2 && y >= centerPos.method_10214() - 2.0 && y <= centerPos.method_10214() + 2.0) continue;
                    pos.method_10102(x, y, z);
                    long posLong = pos.method_10063();
                    if (this.placedPositionTimestamps.containsKey(posLong) && currentTime - this.placedPositionTimestamps.get(posLong) < timeout || !TaskPasteSchematicPerChunkCommand.isPositionAndSidesEmpty(world, (class_2338)pos, sidePos)) continue;
                    return pos.method_10062();
                }
            }
        }
        return null;
    }

    public static boolean isPositionAndSidesEmpty(class_1937 world, class_2338 centerPos, class_2338.class_2339 pos) {
        if (!world.method_22347(centerPos)) {
            return false;
        }
        for (class_2350 side : PositionUtils.ALL_DIRECTIONS) {
            if (world.method_22347((class_2338)pos.method_25505((class_2382)centerPos, side))) continue;
            return false;
        }
        return true;
    }

    protected static boolean preparePickedStack(class_2338 pos, class_2680 state, class_2586 be, class_1937 world, class_310 mc, @Nonnull class_5455 registryManager) {
        if (mc.field_1724 == null || mc.field_1761 == null) {
            return false;
        }
        class_1799 stack = ((IMixinAbstractBlock)state.method_26204()).litematica_getPickStack((class_4538)world, pos, state, false);
        if (!stack.method_7960()) {
            BlockUtils.setStackNbt((class_1799)stack, (class_2586)be, (class_5455)registryManager);
            mc.field_1724.method_31548().field_7544.set(0, (Object)stack);
            mc.field_1761.method_2909(stack, 45);
            return true;
        }
        return false;
    }
}

