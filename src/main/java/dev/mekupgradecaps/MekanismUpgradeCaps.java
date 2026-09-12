package dev.mekupgradecaps;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MekanismUpgradeCaps.MOD_ID)
public final class MekanismUpgradeCaps {
    public static final String MOD_ID = "mekupgradecaps";

    public MekanismUpgradeCaps() {
        NeoForge.EVENT_BUS.addListener(UpgradeCapCommands::register);
    }
}
