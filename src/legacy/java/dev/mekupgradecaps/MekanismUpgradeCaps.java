package dev.mekupgradecaps;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(MekanismUpgradeCaps.MOD_ID)
public final class MekanismUpgradeCaps {
    public static final String MOD_ID = "mekupgradecaps";

    public MekanismUpgradeCaps() {
        MinecraftForge.EVENT_BUS.addListener(UpgradeCapCommands::register);
    }
}
