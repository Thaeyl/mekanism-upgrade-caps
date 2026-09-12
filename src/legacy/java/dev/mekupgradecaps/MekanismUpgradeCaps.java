package dev.mekupgradecaps;

import java.lang.reflect.Method;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(MekanismUpgradeCaps.MOD_ID)
public final class MekanismUpgradeCaps {
    public static final String MOD_ID = "mekupgradecaps";

    public MekanismUpgradeCaps() {
        registerEventHandler();
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        UpgradeCapCommands.register(event);
    }

    private static void registerEventHandler() {
        try {
            Object eventBus = Class.forName("net.minecraftforge.common.MinecraftForge")
                  .getField("EVENT_BUS")
                  .get(null);
            Method register = eventBus.getClass().getMethod("register", Object.class);
            register.invoke(eventBus, MekanismUpgradeCaps.class);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not register Mekanism Upgrade Caps commands", exception);
        }
    }
}
