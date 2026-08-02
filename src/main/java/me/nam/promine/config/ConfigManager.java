package me.nam.promine.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages configuration loading and saving.
 */
public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");
    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = "promine-config.json";
    
    private Config config;
    private Path configPath;
    private Gson gson;

    public ConfigManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configPath = Paths.get(CONFIG_DIR, CONFIG_FILE);
        this.config = new Config();
    }

    /**
     * Load configuration from file or create default if not exists.
     */
    public void loadConfig() {
        try {
            File configDir = new File(CONFIG_DIR);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            File configFile = configPath.toFile();
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    config = gson.fromJson(reader, Config.class);
                    if (config == null) {
                        config = new Config();
                    }
                    LOGGER.info("[ProMine] Configuration loaded successfully");
                }
            } else {
                LOGGER.info("[ProMine] Config file not found, creating default configuration");
                saveConfig();
            }
        } catch (IOException e) {
            LOGGER.error("[ProMine] Failed to load configuration", e);
            config = new Config();
        }
    }

    /**
     * Save configuration to file.
     */
    public void saveConfig() {
        try {
            File configDir = new File(CONFIG_DIR);
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            File configFile = configPath.toFile();
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
                LOGGER.info("[ProMine] Configuration saved successfully");
            }
        } catch (IOException e) {
            LOGGER.error("[ProMine] Failed to save configuration", e);
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
