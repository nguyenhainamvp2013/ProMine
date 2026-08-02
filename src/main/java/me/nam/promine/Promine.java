package me.nam.promine;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nam.promine.config.ConfigManager;

public class Promine implements ModInitializer {
	public static final String MOD_ID = "promine";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static ConfigManager configManager;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[ProMine] Initializing mod");
		
		// Initialize configuration manager
		configManager = new ConfigManager();
		configManager.loadConfig();
		
		// Initialize server components
		PromineServer.initialize();
		
		LOGGER.info("[ProMine] Mod initialized successfully");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static ConfigManager getConfigManager() {
		return configManager;
	}
}
