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
     * Load the config file ro creates a new one if none is found
     *
     * @param plugin   the plugin to whom the Config belongs
     * @param filename the filename of the config file
     */
    public Config(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        //set config file, create if not exists
        if (!plugin.getDataFolder().exists()) {
            boolean dirsCreated = plugin.getDataFolder().mkdirs();
            if (dirsCreated) plugin.getLogger().info("Created " + plugin.getName() + " folder");
        }
        file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            try {
                boolean fileCreated = file.createNewFile();
                if (fileCreated) plugin.getLogger().info("Created " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        plugin.getLogger().info("Loaded configs from " + file.getName());

    }

    /**
     * Set a new config value
     *
     * @param path  the path of the new value
     * @param value the value Object to be added
     */
    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    /**
     * Check if the path contains a value
     *
     * @param path the path to be checked
     * @return true if something is there, false if nothing found
     */
    public boolean contains(String path) {
        return config.contains(path);
    }

    /**
     * @param path the path of the object
     * @return the Object at the path
     */
    public Object get(String path) {
        return config.get(path);
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
        //contains(path)?
        set(path, null);
    }

    /**
     * Save the config file
     */
    public void save() {
        try {
            config.save(file);
            plugin.getLogger().info("Saved " + file.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + file.getName());
            plugin.getLogger().info(config.saveToString());
        }
    }
}
