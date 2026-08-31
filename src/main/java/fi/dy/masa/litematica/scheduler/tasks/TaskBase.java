/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.gui.GuiBase
 *  fi.dy.masa.malilib.interfaces.ICompletionListener
 *  fi.dy.masa.malilib.util.StringUtils
 *  javax.annotation.Nullable
 *  net.minecraft.class_1923
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_310
 *  net.minecraft.class_638
 */
package fi.dy.masa.litematica.scheduler.tasks;

import fi.dy.masa.litematica.config.Configs;
import fi.dy.masa.litematica.render.infohud.IInfoHudRenderer;
import fi.dy.masa.litematica.render.infohud.RenderPhase;
import fi.dy.masa.litematica.scheduler.ITask;
import fi.dy.masa.litematica.scheduler.TaskTimer;
import fi.dy.masa.litematica.util.PositionUtils;
import fi.dy.masa.litematica.util.WorldUtils;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.interfaces.ICompletionListener;
import fi.dy.masa.malilib.util.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_310;
import net.minecraft.class_638;

public abstract class TaskBase
implements ITask,
IInfoHudRenderer {
    protected final List<String> infoHudLines = new ArrayList<String>();
    protected final class_310 mc;
    protected String name = "";
    private TaskTimer timer = new TaskTimer(1);
    @Nullable
    private ICompletionListener completionListener;
    protected boolean finished;
    protected boolean printCompletionMessage = true;

    protected TaskBase() {
        this.mc = class_310.method_1551();
    }

    @Override
    public TaskTimer getTimer() {
        return this.timer;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public void createTimer(int interval) {
        this.timer = new TaskTimer(interval);
    }

    public void disableCompletionMessage() {
        this.printCompletionMessage = false;
    }

    public void setCompletionListener(@Nullable ICompletionListener listener) {
        this.completionListener = listener;
    }

    @Override
    public boolean canExecute() {
        return this.isInWorld();
    }

    @Override
    public boolean shouldRemove() {
        return !this.canExecute();
    }

    @Override
    public void init() {
    }

    @Override
    public void stop() {
        this.notifyListener();
    }

    protected boolean isInWorld() {
        return this.mc.field_1687 != null && this.mc.field_1724 != null;
    }

    protected void notifyListener() {
        if (this.completionListener != null) {
            this.mc.execute(() -> {
                if (this.finished) {
                    this.completionListener.onTaskCompleted();
                } else {
                    this.completionListener.onTaskAborted();
                }
            });
        }
    }

    protected boolean areSurroundingChunksLoaded(class_1923 pos, class_638 world, int radius) {
        if (radius <= 0) {
            return WorldUtils.isClientChunkLoaded(world, pos.field_9181, pos.field_9180);
        }
        int chunkX = pos.field_9181;
        int chunkZ = pos.field_9180;
        for (int cx = chunkX - radius; cx <= chunkX + radius; ++cx) {
            for (int cz = chunkZ - radius; cz <= chunkZ + radius; ++cz) {
                if (WorldUtils.isClientChunkLoaded(world, cx, cz)) continue;
                return false;
            }
        }
        return true;
    }

    protected void updateInfoHudLinesPendingChunks(Collection<class_1923> pendingChunks) {
        this.infoHudLines.clear();
        if (!pendingChunks.isEmpty()) {
            ArrayList<class_1923> list = new ArrayList<class_1923>(pendingChunks);
            PositionUtils.CHUNK_POS_COMPARATOR.setReferencePosition(class_2338.method_49638((class_2374)this.mc.field_1724.method_19538()));
            PositionUtils.CHUNK_POS_COMPARATOR.setClosestFirst(true);
            list.sort(PositionUtils.CHUNK_POS_COMPARATOR);
            String pre = GuiBase.TXT_WHITE + GuiBase.TXT_BOLD;
            String title = StringUtils.translate((String)"litematica.gui.label.task.title.remaining_chunks", (Object[])new Object[]{this.getDisplayName(), pendingChunks.size()});
            this.infoHudLines.add(String.format("%s%s%s", pre, title, GuiBase.TXT_RST));
            int maxLines = Math.min(list.size(), Configs.InfoOverlays.INFO_HUD_MAX_LINES.getIntegerValue());
            for (int i = 0; i < maxLines; ++i) {
                class_1923 pos = (class_1923)list.get(i);
                this.infoHudLines.add(String.format("cx: %5d, cz: %5d (x: %d, z: %d)", pos.field_9181, pos.field_9180, pos.field_9181 << 4, pos.field_9180 << 4));
            }
        }
    }

    @Override
    public boolean getShouldRenderText(RenderPhase phase) {
        return phase == RenderPhase.POST;
    }

    @Override
    public List<String> getText(RenderPhase phase) {
        return this.infoHudLines;
    }
}

