package me.frauenfelderflorian.worldutils.config;

import me.frauenfelderflorian.worldutils.Settings;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper class for configuration file, extends config.Config
 */
public class Prefs extends Config {
    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin   the plugin to whom the Config belongs
     * @param filename the filename of the config file
     */
    public Prefs(JavaPlugin plugin, String filename) {
        super(plugin, filename);
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
     * Check if the setting contains a value
     *
     * @param setting the setting to be checked
     * @return true if something is found, false if not
     */
    public boolean contains(Settings setting) {
        return contains(setting.getKey());
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
     * Remove a setting
     *
     * @param setting the setting to be removed
     */
    public void remove(Settings setting) {
        remove(setting.getKey());
    }
}
