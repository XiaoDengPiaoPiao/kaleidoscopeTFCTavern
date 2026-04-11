package net.xdpp.kaleidoscopetfctavern.compat;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class GreenhouseCompat {
    private static final String HELPER_CLASS = "com.g1739.firmalifegreenhousepatch.common.temperature.GreenhouseTemperatureHelper";

    private static volatile boolean initialized;
    @Nullable
    private static Method isControlledGreenhouse;
    @Nullable
    private static Method getControlledTemperature;

    private GreenhouseCompat() {
    }

    public static boolean isControlledGreenhouse(Level level, BlockPos pos) {
        initialize();
        if (isControlledGreenhouse == null) {
            return false;
        }
        try {
            return (boolean) isControlledGreenhouse.invoke(null, level, pos);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            return false;
        }
    }

    public static float getControlledTemperature(Level level, BlockPos pos, float fallbackTemperature) {
        initialize();
        if (getControlledTemperature == null) {
            return fallbackTemperature;
        }
        try {
            return ((Float) getControlledTemperature.invoke(null, level, pos, fallbackTemperature)).floatValue();
        } catch (IllegalAccessException | InvocationTargetException ignored) {
            return fallbackTemperature;
        }
    }

    private static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            final Class<?> helperClass = Class.forName(HELPER_CLASS);
            isControlledGreenhouse = helperClass.getMethod("isControlledGreenhouse", Level.class, BlockPos.class);
            getControlledTemperature = helperClass.getMethod("getControlledTemperature", Level.class, BlockPos.class, float.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            isControlledGreenhouse = null;
            getControlledTemperature = null;
        }
    }
}
