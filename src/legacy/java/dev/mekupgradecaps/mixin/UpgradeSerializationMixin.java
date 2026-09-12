package dev.mekupgradecaps.mixin;

import java.util.EnumMap;
import java.util.Map;
import mekanism.api.Upgrade;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeSerializationMixin {
    /**
     * @author Mekanism Upgrade Caps contributors
     * @reason Preserve saved upgrade counts above Mekanism's built-in maxStack when this mod raises the dynamic cap.
     */
    @Overwrite
    public static Map<Upgrade, Integer> buildMap(CompoundTag tag) {
        Map<Upgrade, Integer> upgrades = new EnumMap<>(Upgrade.class);
        if (tag != null && tag.m_128425_("upgrades", 9)) {
            ListTag list = tag.m_128437_("upgrades", 10);
            for (int index = 0; index < list.size(); index++) {
                CompoundTag upgradeTag = list.m_128728_(index);
                Upgrade upgrade = Upgrade.byIndexStatic(upgradeTag.m_128451_("type"));
                int amount = Mth.m_14045_(upgradeTag.m_128451_("amount"), 0, upgrade.getMax());
                if (amount > 0) {
                    upgrades.put(upgrade, amount);
                }
            }
        }
        return upgrades;
    }
}
