/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.ghepdo;

import com.ghepdo.AutoMinigame;
import com.ghepdo.DataScanner;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ghepdo
implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"ghepdo");

    public void onInitializeClient() {
        LOGGER.info("[v3 hehe] Mod Gh\u00e9p \u0110\u1ed3 (Auto Minigame) \u0111ang kh\u1edfi ch\u1ea1y...");
        AutoMinigame.register();
        DataScanner.register();
        LOGGER.info("[v3 hehe] \u0110\u00e3 t\u1ea3i xong! V\u00e0o game v\u00e0 b\u1eadt 'litemacEnabled' trong menu Litematica \u0111\u1ec3 hu\u1ef7 di\u1ec7t Minigame th\u00f4i!");
    }
}

