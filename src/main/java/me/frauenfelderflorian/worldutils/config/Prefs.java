package me.frauenfelderflorian.worldutils.config;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Wrapper class for configuration file, extends config.Config
 */
public class Prefs extends Config {
    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin the plugin to whom the Config belongs
     */
    public Prefs(JavaPlugin plugin) {
        super(plugin, "config.yml");
    }

    /**
     * Set a setting value
     *
     * @param setting the setting to be set
     * @param value   the value Object of the setting
     * @param log     true if logging messages should be sent
     */
    public void set(Option setting, Object value, boolean log) {
        set(setting.getKey(), value, log);
    }

    /**
     * Check if the setting contains a value
     *
     * @param setting the setting to be checked
     * @return true if something is found, false if not
     */
    public boolean contains(Option setting) {
        return contains(setting.getKey());
    }

    /**
     * Get a value from a setting
     *
     * @param setting the setting
     * @return the Object at the setting's path
     */
    public Object get(Option setting) {
        return get(setting.getKey());
    }

    /**
     * Get a boolean value from a setting
     *
     * @param setting the setting
     * @return the boolean at the setting's path
     */
    public boolean getBoolean(Option setting) {
        return config.getBoolean(setting.getKey());
    }

    /**
     * Get an int from a setting
     *
     * @param setting the setting
     * @return the int at the setting's path
     */
    public int getInt(Option setting) {
        return config.getInt(setting.getKey());
    }

    /**
     * Remove a setting
     *
     * @param setting the setting to be removed
     */
    public void remove(Option setting) {
        remove(setting.getKey());
    }
}
