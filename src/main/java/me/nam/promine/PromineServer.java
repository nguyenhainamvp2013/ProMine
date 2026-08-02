package me.nam.promine;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import me.nam.promine.command.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server-side initialization for ProMine mod.
 */
public class PromineServer {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");

    public static void initialize() {
        LOGGER.info("[ProMine] Initializing server components");
        
        // Register command callback
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegistry.registerCommands(dispatcher);
            LOGGER.info("[ProMine] Commands registered successfully");
        });
    }
}
