package com.ariyean.mekupgradecaps.mixin;

import com.ariyean.mekupgradecaps.MekanismUpgradeMath;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MekanismUtils.class, remap = false)
public abstract class MekanismUtilsMixin {
    /**
     * @author Ariyean
     * @reason Scale processing speed from installed upgrade count divided by Mekanism's original cap of 8.
     */
    @Overwrite
    public static double getTicksD(IUpgradeTile tile, int defTicks) {
        return defTicks * MekanismUpgradeMath.speedTimeMultiplier(tile);
    }

    /**
     * @author Ariyean
     * @reason Allow upgrades beyond 8 to increase operations per tick after processing reaches 1 tick.
     */
    @Overwrite
    public static int getOperationsPerTick(IUpgradeTile tile, int defTicks, int def) {
        double ticks = getTicksD(tile, defTicks);
        if (ticks >= 1.0D) {
            return def;
        }
        return clampToInt(Math.max(1.0D, 1.0D / ticks) * def);
    }

    /**
     * @author Ariyean
     * @reason Match energy use to the expanded speed and energy upgrade counts.
     */
    @Overwrite
    public static long getEnergyPerTick(IUpgradeTile tile, long def) {
        if (!tile.supportsUpgrades()) {
            return def;
        }
        return ceilToLong(def * MekanismUpgradeMath.energyUsageMultiplier(tile));
    }

    /**
     * @author Ariyean
     * @reason Match energy capacity scaling to the expanded energy upgrade count.
     */
    @Overwrite
    public static long getMaxEnergy(IUpgradeTile tile, long def) {
        if (!tile.supportsUpgrades()) {
            return def;
        }
        return clampToLong(def * MekanismUpgradeMath.energyCapacityMultiplier(tile));
    }

    private static int clampToInt(double value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    private static long ceilToLong(double value) {
        return clampToLong(Math.ceil(value));
    }

    private static long clampToLong(double value) {
        if (value > Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        if (value < Long.MIN_VALUE) {
            return Long.MIN_VALUE;
        }
        return (long) value;
    }
}
