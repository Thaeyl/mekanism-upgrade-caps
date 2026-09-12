package dev.mekupgradecaps;

import mekanism.api.Upgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;

public final class MekanismUpgradeMath {
    private static final double PER_UPGRADE_MULTIPLIER = 2.0D;

    private MekanismUpgradeMath() {
    }

    public static double fraction(IUpgradeTile tile, Upgrade upgrade) {
        if (!tile.supportsUpgrades() || !tile.supportsUpgrade(upgrade)) {
            return 0.0D;
        }
        return tile.getComponent().getUpgrades(upgrade) / 8.0D;
    }

    public static double speedTimeMultiplier(IUpgradeTile tile) {
        return 1.0D / speedMultiplier(tile);
    }

    public static double energyUsageMultiplier(IUpgradeTile tile) {
        double speed = speedMultiplier(tile);
        double energy = energyMultiplier(tile);
        return speed * speed / energy;
    }

    public static double energyCapacityMultiplier(IUpgradeTile tile) {
        return energyMultiplier(tile);
    }

    public static double speedMultiplier(IUpgradeTile tile) {
        return rawMultiplier(tile, Upgrade.SPEED);
    }

    public static double energyMultiplier(IUpgradeTile tile) {
        return rawMultiplier(tile, Upgrade.ENERGY);
    }

    public static String display(double value) {
        if (value < 1_000_000D) {
            double rounded = Math.round(value * 100.0D) / 100.0D;
            if (rounded == (long) rounded) {
                return Long.toString((long) rounded);
            }
            return Double.toString(rounded);
        }
        int exponent = (int) Math.floor(Math.log10(value));
        double mantissa = value / Math.pow(10.0D, exponent);
        return Math.round(mantissa * 100.0D) / 100.0D + "E" + exponent;
    }

    private static double rawMultiplier(IUpgradeTile tile, Upgrade upgrade) {
        if (!tile.supportsUpgrades() || !tile.supportsUpgrade(upgrade)) {
            return 1.0D;
        }
        int installed = tile.getComponent().getUpgrades(upgrade);
        if (installed <= 0) {
            return 1.0D;
        }
        return installed * PER_UPGRADE_MULTIPLIER;
    }
}
