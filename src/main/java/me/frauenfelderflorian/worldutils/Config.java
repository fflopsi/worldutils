package me.frauenfelderflorian.worldutils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Wrapper class for configuration file
 */
public class Config {
    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration config;

    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin   the plugin to whom the Config belongs
     * @param filename the filename of the config file
     */
    public Config(JavaPlugin plugin, String filename) {
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
     * Set a setting value
     *
     * @param setting the setting to be set
     * @param value   the value Object of the setting
     * @param log     true if logging messages should be sent
     */
    public void set(Settings setting, Object value, boolean log) {
        set(setting.getKey(), value, log);
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
     * Check if the setting contains a value
     *
     * @param setting the setting to be checked
     * @return true if something is found, false if not
     */
    public boolean contains(Settings setting) {
        return contains(setting.getKey());
    }

    /**
     * Get a value from a path
     *
     * @param path the path of the object
     * @return the Object at the path
     */
    public Object get(String path) {
        return config.get(path);
    }

    /**
     * Get a value from a setting
     *
     * @param setting the setting
     * @return the Object at the setting's path
     */
    public Object get(Settings setting) {
        return get(setting.getKey());
    }

    /**
     * Get all keys
     *
     * @param deep set to true to get all keys and sub-keys, set to false to only get the top-level keys
     * @return Set of Strings containing all keys
     */
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
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
     * Remove a setting
     *
     * @param setting the setting to be removed
     */
    public void remove(Settings setting) {
        remove(setting.getKey());
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
