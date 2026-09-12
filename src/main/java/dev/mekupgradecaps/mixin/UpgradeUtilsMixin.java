package dev.mekupgradecaps.mixin;

import dev.mekupgradecaps.MekanismUpgradeMath;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = UpgradeUtils.class, remap = false)
public abstract class UpgradeUtilsMixin {
    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Display expanded linear speed/energy multipliers instead of Mekanism's standard cap-based value.
     */
    @Overwrite
    public static List<Component> getMultScaledInfo(IUpgradeTile tile, Upgrade upgrade) {
        List<Component> info = new ArrayList<>();
        if (tile.supportsUpgrades() && upgrade.getMax() > 1) {
            double multiplier;
            if (upgrade == Upgrade.SPEED) {
                multiplier = MekanismUpgradeMath.speedMultiplier(tile);
            } else if (upgrade == Upgrade.ENERGY) {
                multiplier = MekanismUpgradeMath.energyMultiplier(tile);
            } else {
                multiplier = Math.pow(MekanismUpgradeMath.energyMultiplier(tile), MekanismUpgradeMath.fraction(tile, upgrade));
            }
            info.add(MekanismLang.UPGRADES_EFFECT.translate(MekanismUpgradeMath.display(multiplier)));
        }
        return info;
    }

    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Display the same expanded speed multiplier for exponential-style info callers.
     */
    @Overwrite
    public static List<Component> getExpScaledInfo(IUpgradeTile tile, Upgrade upgrade) {
        List<Component> info = new ArrayList<>();
        if (tile.supportsUpgrades() && upgrade.getMax() > 1) {
            info.add(MekanismLang.UPGRADES_EFFECT.translate(MekanismUpgradeMath.display(MekanismUpgradeMath.speedMultiplier(tile))));
        }
        return info;
    }
}
