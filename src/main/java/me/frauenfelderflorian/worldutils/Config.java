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

    public Config(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
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

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public void remove(String path) {
        config.set(path, null);
        save();
    }

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
