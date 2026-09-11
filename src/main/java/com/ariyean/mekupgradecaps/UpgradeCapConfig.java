package com.ariyean.mekupgradecaps;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class UpgradeCapConfig {
    private static final Path CONFIG_PATH = Path.of("config", "mekanism-upgrade-caps.properties");
    private static final int DEFAULT_SPEED = 16;
    private static final int DEFAULT_ENERGY = 16;

    private static int speedMax = DEFAULT_SPEED;
    private static int energyMax = DEFAULT_ENERGY;
    private static long lastLoaded = Long.MIN_VALUE;

    private UpgradeCapConfig() {
    }

    public static int speedMax() {
        loadIfNeeded();
        return speedMax;
    }

    public static int energyMax() {
        loadIfNeeded();
        return energyMax;
    }

    public static synchronized void setSpeedMax(int value) throws IOException {
        loadIfNeeded();
        speedMax = clamp(value);
        writeCurrent();
    }

    public static synchronized void setEnergyMax(int value) throws IOException {
        loadIfNeeded();
        energyMax = clamp(value);
        writeCurrent();
    }

    private static synchronized void loadIfNeeded() {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                writeDefaults();
            }
            long modified = Files.getLastModifiedTime(CONFIG_PATH).toMillis();
            if (modified == lastLoaded) {
                return;
            }
            Properties properties = new Properties();
            try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                properties.load(input);
            }
            speedMax = readInt(properties, "speedMax", DEFAULT_SPEED);
            energyMax = readInt(properties, "energyMax", DEFAULT_ENERGY);
            lastLoaded = modified;
        } catch (IOException ignored) {
            speedMax = DEFAULT_SPEED;
            energyMax = DEFAULT_ENERGY;
        }
    }

    private static int readInt(Properties properties, String key, int fallback) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return clamp(Integer.parseInt(value.trim()));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static int clamp(int value) {
        return Math.clamp(value, 0, 1024);
    }

    private static void writeDefaults() throws IOException {
        Files.createDirectories(CONFIG_PATH.getParent());
        speedMax = DEFAULT_SPEED;
        energyMax = DEFAULT_ENERGY;
        writeCurrent();
    }

    private static void writeCurrent() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("speedMax", Integer.toString(speedMax));
        properties.setProperty("energyMax", Integer.toString(energyMax));
        try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
            properties.store(output, "Mekanism Upgrade Caps. Change values, then restart the pack.");
        }
        lastLoaded = Files.getLastModifiedTime(CONFIG_PATH).toMillis();
    }
}
