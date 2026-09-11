package com.ariyean.mekupgradecaps;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.IOException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class UpgradeCapCommands {
    private UpgradeCapCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
              Commands.literal("mekupgradecaps")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.literal("get")
                          .executes(context -> show(context.getSource())))
                    .then(Commands.literal("set")
                          .then(setCommand("speed"))
                          .then(setCommand("energy")))
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> setCommand(String upgrade) {
        return Commands.literal(upgrade)
              .then(Commands.argument("value", IntegerArgumentType.integer(0, 1024))
                    .executes(context -> set(
                          context.getSource(),
                          upgrade,
                          IntegerArgumentType.getInteger(context, "value")
                    )));
    }

    private static int show(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal(
              "Mekanism upgrade caps: speed=" + UpgradeCapConfig.speedMax()
                    + ", energy=" + UpgradeCapConfig.energyMax()
        ), false);
        return 1;
    }

    private static int set(CommandSourceStack source, String upgrade, int value) {
        try {
            if ("speed".equals(upgrade)) {
                UpgradeCapConfig.setSpeedMax(value);
            } else {
                UpgradeCapConfig.setEnergyMax(value);
            }
            source.sendSuccess(() -> Component.literal(
                  "Set Mekanism " + upgrade + " upgrade cap to " + value + "."
            ), true);
            return show(source);
        } catch (IOException exception) {
            source.sendFailure(Component.literal("Could not save Mekanism upgrade cap: " + exception.getMessage()));
            return 0;
        }
    }
}
