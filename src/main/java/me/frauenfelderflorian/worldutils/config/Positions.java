package me.frauenfelderflorian.worldutils.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * Wrapper class for configuration file for positions, extends config.Config
 */
public class Positions extends Config {

    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin   the plugin to whom the Config belongs
     * @param filename the filename of the config file
     */
    public Positions(JavaPlugin plugin, String filename) {
        super(plugin, filename);
    }

    /**
     * Get all positions
     *
     * @return Set of Strings containing all positions
     */
    public Set<String> getPositions() {
        Set<String> keys = config.getKeys(false);
        if (keys.contains("list")) keys.remove("keys");
        return keys;
    }
}