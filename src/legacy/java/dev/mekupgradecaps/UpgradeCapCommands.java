package dev.mekupgradecaps;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.IOException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class UpgradeCapCommands {
    private UpgradeCapCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
              Commands.m_82127_("mekupgradecaps")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.m_82127_("get")
                          .executes(context -> show(context.getSource())))
                    .then(Commands.m_82127_("set")
                          .then(setCommand("speed"))
                          .then(setCommand("energy")))
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> setCommand(String upgrade) {
        return Commands.m_82127_(upgrade)
              .then(Commands.m_82129_("value", IntegerArgumentType.integer(0, 1024))
                    .executes(context -> set(
                          context.getSource(),
                          upgrade,
                          IntegerArgumentType.getInteger(context, "value")
                    )));
    }

    private static int show(CommandSourceStack source) {
        source.m_81354_(Component.m_237113_("Mekanism upgrade caps: speed=" + UpgradeCapConfig.speedMax()
              + ", energy=" + UpgradeCapConfig.energyMax()), false);
        return 1;
    }

    private static int set(CommandSourceStack source, String upgrade, int value) {
        try {
            if ("speed".equals(upgrade)) {
                UpgradeCapConfig.setSpeedMax(value);
            } else {
                UpgradeCapConfig.setEnergyMax(value);
            }
            source.m_81354_(Component.m_237113_(
                  "Set Mekanism " + upgrade + " upgrade cap to " + value + "."
            ), true);
            return show(source);
        } catch (IOException exception) {
            source.m_81352_(Component.m_237113_("Could not save Mekanism upgrade cap: " + exception.getMessage()));
            return 0;
        }
    }
}
