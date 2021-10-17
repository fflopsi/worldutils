package me.frauenfelderflorian.worldutils.config;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Abstract wrapper class for configuration file
 */
public abstract class Config {
    protected final WorldUtils plugin;
    protected final File file;
    protected final YamlConfiguration config;

    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin   the plugin to whom the Config belongs
     * @param filename the filename of the config file
     */
    public Config(WorldUtils plugin, String filename) {
        this.plugin = plugin;
        //set config file, create if not exists
        if (!plugin.getDataFolder().exists() && plugin.getDataFolder().mkdirs())
            plugin.getLogger().info("Created " + plugin.getName() + " folder");
        file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) plugin.getLogger().info("Created " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        plugin.getLogger().info("Loaded config from " + file.getName());
    }

    /**
     * Set a config value
     *
     * @param path  the path of the new value
     * @param value the value Object to be added
     * @param log   true if logging messages should be sent
     */
    public void set(String path, Object value, boolean log) {
        config.set(path, value);
        if (log) plugin.getLogger().info("Set config " + path + " to " + value);
        save(log);
    }

    /**
     * Check if the path contains a value
     *
     * @param path the path to be checked
     * @return true if something is found, false if nothing found
     */
    public boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * Remove a value
     *
     * @param path the path to the value to be removed
     */
    public void remove(String path) {
        set(path, null, true);
    }

    /**
     * Save the config file
     *
     * @param log true if logging messages should be sent
     */
    public void save(boolean log) {
        try {
            config.save(file);
            if (log) plugin.getLogger().info("Saved config to " + file.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + file.getName());
            plugin.getLogger().info(config.saveToString());
        }
    }
}
