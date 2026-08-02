package me.nam.promine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * Registers all ProMine commands.
 */
public class CommandRegistry {
    
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("promine")
                .then(literal("start").executes(CommandRegistry::startCommand))
                .then(literal("stop").executes(CommandRegistry::stopCommand))
                .then(literal("sethome").executes(CommandRegistry::setHomeCommand))
                .then(literal("setshulker").executes(CommandRegistry::setShulkerCommand))
        );
    }

    private static int startCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("[ProMine] Started"), false);
        return 1;
    }

    private static int stopCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("[ProMine] Stopped"), false);
        return 1;
    }

    private static int setHomeCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("[ProMine] Home position saved"), false);
        return 1;
    }

    private static int setShulkerCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("[ProMine] Shulker position saved"), false);
        return 1;
    }
}
