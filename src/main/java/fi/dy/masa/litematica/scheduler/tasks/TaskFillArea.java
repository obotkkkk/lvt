/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.Message$MessageType
 *  fi.dy.masa.malilib.util.InfoUtils
 *  fi.dy.masa.malilib.util.IntBoundingBox
 *  javax.annotation.Nullable
 *  net.minecraft.class_1263
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2259
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_238
 *  net.minecraft.class_2586
 *  net.minecraft.class_2680
 *  net.minecraft.class_3695
 */
package fi.dy.masa.litematica.scheduler.tasks;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.render.infohud.InfoHud;
import fi.dy.masa.litematica.scheduler.tasks.TaskProcessChunkMultiPhase;
import fi.dy.masa.litematica.selection.Box;
import fi.dy.masa.litematica.util.EntityUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.Message;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.IntBoundingBox;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_1263;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2259;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_3695;

public class TaskFillArea
extends TaskProcessChunkMultiPhase {
    protected final String fillCommand;
    protected final class_2680 fillState;
    protected final String blockString;
    @Nullable
    protected final class_2680 replaceState;
    @Nullable
    protected final String replaceBlockString;
    protected final int maxBoxVolume;
    protected final boolean removeEntities;
    protected final String useStrict;

    public TaskFillArea(List<Box> boxes, class_2680 fillState, @Nullable class_2680 replaceState, boolean removeEntities) {
        this(boxes, fillState, replaceState, removeEntities, "litematica.gui.label.task_name.fill");
    }

    protected TaskFillArea(List<Box> boxes, class_2680 fillState, @Nullable class_2680 replaceState, boolean removeEntities, String nameOnHud) {
        super(nameOnHud);
        this.fillState = fillState;
        this.replaceState = replaceState;
        this.removeEntities = removeEntities;
        this.maxBoxVolume = Configs.Generic.COMMAND_FILL_MAX_VOLUME.getIntegerValue();
        this.maxCommandsPerTick = Configs.Generic.COMMAND_LIMIT.getIntegerValue();
        this.fillCommand = Configs.Generic.COMMAND_NAME_FILL.getStringValue();
        this.useStrict = "";
        this.blockString = class_2259.method_9685((class_2680)fillState);
        this.replaceBlockString = replaceState != null ? class_2259.method_9685((class_2680)replaceState) : null;
        this.processBoxBlocksTask = this::sendQueuedCommands;
        if (Configs.Generic.COMMAND_FILL_NO_CHUNK_CLAMP.getBooleanValue()) {
            this.addNonChunkClampedBoxes(boxes);
        } else {
            this.addPerChunkBoxes(boxes);
        }
    }

    @Override
    public boolean canExecute() {
        return super.canExecute() && this.blockString != null;
    }

    @Override
    protected boolean canProcessChunk(class_1923 pos) {
        return this.areSurroundingChunksLoaded(pos, this.clientWorld, 0);
    }

    @Override
    public void init() {
        super.init();
        if (this.useWorldEdit && this.isInWorld()) {
            this.sendCommand("/perf neighbors off");
        }
    }

    @Override
    public boolean execute(class_3695 profiler) {
        return this.executeMultiPhase(profiler);
    }

    @Override
    protected void onNextChunkFetched(class_1923 pos) {
        if (this.isClientWorld) {
            this.queueCommandsForBoxesInChunk(pos);
        } else {
            this.directFillBoxesInChunk(pos);
        }
    }

    protected void queueCommandsForBoxesInChunk(class_1923 pos) {
        for (IntBoundingBox box : this.getBoxesInChunk(pos)) {
            this.queueFillCommandsForBox(box, this.removeEntities);
        }
        this.phase = TaskProcessChunkMultiPhase.TaskPhase.PROCESS_BOX_BLOCKS;
    }

    protected void directFillBoxesInChunk(class_1923 pos) {
        for (IntBoundingBox box : this.getBoxesInChunk(pos)) {
            this.directFillBox(box, this.removeEntities);
        }
        this.finishProcessingChunk(pos);
    }

    protected void directFillBox(IntBoundingBox box, boolean removeEntities) {
        if (removeEntities) {
            TaskFillArea.directRemoveEntities(box, this.world);
        }
        WorldUtils.setShouldPreventBlockUpdates(this.world, true);
        class_2680 barrier = class_2246.field_10499.method_9564();
        class_2338.class_2339 posMutable = new class_2338.class_2339();
        for (int z = box.minZ; z <= box.maxZ; ++z) {
            for (int x = box.minX; x <= box.maxX; ++x) {
                for (int y = box.maxY; y >= box.minY; --y) {
                    posMutable.method_10103(x, y, z);
                    class_2680 oldState = this.world.method_8320((class_2338)posMutable);
                    if ((this.replaceState != null || oldState == this.fillState) && oldState != this.replaceState) continue;
                    class_2586 te = this.world.method_8321((class_2338)posMutable);
                    if (te instanceof class_1263) {
                        ((class_1263)te).method_5448();
                        this.world.method_8652((class_2338)posMutable, barrier, 50);
                    }
                    this.world.method_8652((class_2338)posMutable, this.fillState, 50);
                }
            }
        }
        WorldUtils.setShouldPreventBlockUpdates(this.world, false);
    }

    public static void directRemoveEntities(IntBoundingBox box, class_1937 world) {
        class_238 aabb = new class_238((double)box.minX, (double)box.minY, (double)box.minZ, (double)(box.maxX + 1), (double)(box.maxY + 1), (double)(box.maxZ + 1));
        List entities = world.method_8333(null, aabb, EntityUtils.NOT_PLAYER);
        for (class_1297 entity : entities) {
            if (entity instanceof class_1657) continue;
            entity.method_31472();
        }
    }

    protected void queueFillCommandsForBox(IntBoundingBox box, boolean removeEntities) {
        int totalVolume;
        class_238 aabb;
        if (removeEntities && this.world.method_8333((class_1297)this.mc.field_1724, aabb = new class_238((double)box.minX, (double)box.minY, (double)box.minZ, (double)(box.maxX + 1), (double)(box.maxY + 1), (double)(box.maxZ + 1)), EntityUtils.NOT_PLAYER).size() > 0) {
            String killCmd = String.format("kill @e[type=!player,x=%d,y=%d,z=%d,dx=%d,dy=%d,dz=%d]", box.minX, box.minY, box.minZ, box.maxX - box.minX + 1, box.maxY - box.minY + 1, box.maxZ - box.minZ + 1);
            this.queuedCommands.offer(killCmd);
        }
        if ((totalVolume = (box.maxX - box.minX + 1) * (box.maxY - box.minY + 1) * (box.maxZ - box.minZ + 1)) <= this.maxBoxVolume || this.useWorldEdit) {
            this.queueFillCommandForBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        } else {
            int singleLayerVolume = (box.maxX - box.minX + 1) * (box.maxZ - box.minZ + 1);
            int singleBoxHeight = this.maxBoxVolume / singleLayerVolume;
            if (singleBoxHeight < 1) {
                InfoUtils.showGuiOrInGameMessage((Message.MessageType)Message.MessageType.ERROR, (String)"Error: Calculated single box height was less than 1 block", (Object[])new Object[0]);
                return;
            }
            for (int y = box.minY; y <= box.maxY; y += singleBoxHeight) {
                int maxY = Math.min(y + singleBoxHeight - 1, box.maxY);
                this.queueFillCommandForBox(box.minX, y, box.minZ, box.maxX, maxY, box.maxZ);
            }
        }
    }

    protected void queueFillCommandForBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (this.useWorldEdit) {
            this.queuedCommands.offer(String.format("/pos1 %d,%d,%d", minX, minY, minZ));
            this.queuedCommands.offer(String.format("/pos2 %d,%d,%d", maxX, maxY, maxZ));
            if (this.replaceState != null) {
                this.queuedCommands.offer(String.format("/replace %s %s", this.replaceBlockString, this.blockString));
            } else {
                this.queuedCommands.offer("/set " + this.blockString);
            }
        } else {
            String fillCmd = this.replaceState != null ? String.format("%s %d %d %d %d %d %d %s replace %s%s", this.fillCommand, minX, minY, minZ, maxX, maxY, maxZ, this.blockString, this.replaceBlockString, this.useStrict) : String.format("%s %d %d %d %d %d %d %s%s", this.fillCommand, minX, minY, minZ, maxX, maxY, maxZ, this.blockString, this.useStrict);
            this.queuedCommands.offer(fillCmd);
        }
    }

    @Override
    protected void onStop() {
        this.printCompletionMessage();
        this.sendTaskEndCommands();
        DataManager.removeChatListener(this.gameRuleListener);
        InfoHud.getInstance().removeInfoHudRenderer(this, false);
        super.onStop();
    }

    protected void printCompletionMessage() {
        if (this.finished) {
            if (this.printCompletionMessage) {
                InfoUtils.showGuiMessage((Message.MessageType)Message.MessageType.SUCCESS, (String)"litematica.message.area_filled", (Object[])new Object[0]);
            }
        } else {
            InfoUtils.showGuiMessage((Message.MessageType)Message.MessageType.ERROR, (String)"litematica.message.area_fill_fail", (Object[])new Object[0]);
        }
    }
}

