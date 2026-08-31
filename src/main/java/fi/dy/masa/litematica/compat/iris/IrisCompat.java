/*
 * Decompiled with CFR 0.152.
 */
package fi.dy.masa.litematica.compat.iris;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class IrisCompat {
    private static Object API;
    private static MethodHandle isShaderActive;
    private static MethodHandle isShadowPassActive;
    public static boolean isIrisActive;

    public static boolean isShaderActive() {
        if (!isIrisActive) {
            return false;
        }
        try {
            return isShaderActive.invoke(API);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isShadowPassActive() {
        if (!isIrisActive) {
            return false;
        }
        try {
            return isShadowPassActive.invoke(API);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try {
            Class<?> irisAPI = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            API = irisAPI.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            isShaderActive = MethodHandles.lookup().findVirtual(irisAPI, "isShaderPackInUse", MethodType.methodType(Boolean.TYPE));
            isShadowPassActive = MethodHandles.lookup().findVirtual(irisAPI, "isRenderingShadowPass", MethodType.methodType(Boolean.TYPE));
            isIrisActive = true;
        }
        catch (Exception e) {
            isIrisActive = false;
        }
    }
}

