package dev.mekupgradecaps.mixin;

import dev.mekupgradecaps.MekanismUpgradeMath;
import mekanism.api.math.FloatingLong;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = MekanismUtils.class, remap = false)
public abstract class MekanismUtilsLegacyMixin {
    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Scale processing speed from installed upgrade count divided by Mekanism's original cap of 8.
     */
    @Overwrite
    public static int getTicks(IUpgradeTile tile, int defTicks) {
        return clampToInt(Math.max(1.0D, defTicks * MekanismUpgradeMath.speedTimeMultiplier(tile)));
    }

    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Match energy use to the expanded speed and energy upgrade counts.
     */
    @Overwrite
    public static FloatingLong getEnergyPerTick(IUpgradeTile tile, FloatingLong def) {
        if (!tile.supportsUpgrades()) {
            return def;
        }
        return def.multiply(MekanismUpgradeMath.energyUsageMultiplier(tile)).ceil();
    }

    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Match energy capacity scaling to the expanded energy upgrade count.
     */
    @Overwrite
    public static FloatingLong getMaxEnergy(IUpgradeTile tile, FloatingLong def) {
        if (!tile.supportsUpgrades()) {
            return def;
        }
        return def.multiply(MekanismUpgradeMath.energyCapacityMultiplier(tile));
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
}
