/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  fi.dy.masa.malilib.interfaces.IServerListener
 *  net.minecraft.class_1132
 */
package fi.dy.masa.litematica.event;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.malilib.interfaces.IServerListener;
import net.minecraft.class_1132;

public class ServerListener
implements IServerListener {
    public void onServerIntegratedSetup(class_1132 server) {
        DataManager.getInstance().setHasIntegratedServer(true);
    }
}

