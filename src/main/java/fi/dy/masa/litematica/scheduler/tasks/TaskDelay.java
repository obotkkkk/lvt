/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3695
 */
package fi.dy.masa.litematica.scheduler.tasks;

import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.litematica.scheduler.tasks.TaskBase;
import java.util.function.BooleanSupplier;
import net.minecraft.class_3695;

public class TaskDelay
extends TaskBase {
    protected final TaskScheduler scheduler;
    protected final TaskBase task;
    protected final BooleanSupplier startConditionChecker;
    protected final int interval;

    public TaskDelay(TaskBase task, int interval, TaskScheduler scheduler, BooleanSupplier startConditionChecker) {
        this.task = task;
        this.scheduler = scheduler;
        this.interval = interval;
        this.startConditionChecker = startConditionChecker;
    }

    @Override
    public boolean execute(class_3695 profiler) {
        if (this.startConditionChecker.getAsBoolean()) {
            this.scheduler.scheduleTask(this.task, this.interval);
            this.finished = true;
            return true;
        }
        return false;
    }
}

