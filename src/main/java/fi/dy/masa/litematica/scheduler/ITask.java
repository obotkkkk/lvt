/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3695
 */
package fi.dy.masa.litematica.scheduler;

import fi.dy.masa.litematica.scheduler.TaskTimer;
import net.minecraft.class_3695;

public interface ITask {
    public String getDisplayName();

    public void init();

    public boolean canExecute();

    public boolean execute(class_3695 var1);

    public boolean shouldRemove();

    public void stop();

    public TaskTimer getTimer();

    public void createTimer(int var1);
}

