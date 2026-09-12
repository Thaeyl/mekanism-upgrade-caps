package dev.mekupgradecaps;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Supplier;
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
                    .requires(source -> source.m_6761_(2))
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
        sendSuccess(source, "Mekanism upgrade caps: speed=" + UpgradeCapConfig.speedMax()
              + ", energy=" + UpgradeCapConfig.energyMax(), false);
        return 1;
    }

    private static int set(CommandSourceStack source, String upgrade, int value) {
        try {
            if ("speed".equals(upgrade)) {
                UpgradeCapConfig.setSpeedMax(value);
            } else {
                UpgradeCapConfig.setEnergyMax(value);
            }
            sendSuccess(source, "Set Mekanism " + upgrade + " upgrade cap to " + value + ".", true);
            return show(source);
        } catch (IOException exception) {
            source.m_81352_(literal("Could not save Mekanism upgrade cap: " + exception.getMessage()));
            return 0;
        }
    }

    private static void sendSuccess(CommandSourceStack source, String message, boolean broadcast) {
        Component component = literal(message);
        try {
            Method modern = source.getClass().getMethod("m_288197_", Supplier.class, boolean.class);
            modern.invoke(source, (Supplier<Component>) () -> component, broadcast);
            return;
        } catch (ReflectiveOperationException ignored) {
        }
        try {
            Method legacy = source.getClass().getMethod("m_81354_", Component.class, boolean.class);
            legacy.invoke(source, component, broadcast);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not send command feedback", exception);
        }
    }

    private static Component literal(String message) {
        try {
            Method factory = Component.class.getMethod("m_237113_", String.class);
            return (Component) factory.invoke(null, message);
        } catch (ReflectiveOperationException ignored) {
        }
        try {
            Class<?> textComponent = Class.forName("net.minecraft.network.chat.TextComponent");
            return (Component) textComponent.getConstructor(String.class).newInstance(message);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not create command feedback text", exception);
        }
    }
}
