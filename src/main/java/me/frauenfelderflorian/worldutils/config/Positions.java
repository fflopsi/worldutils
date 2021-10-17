package me.frauenfelderflorian.worldutils.config;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Location;

import java.util.Set;

/**
 * Wrapper class for configuration file for positions, extends config.Config
 */
public class Positions extends Config {
    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin   the plugin to whom the Positions belongs
     * @param filename the filename of the config file
     */
    public Positions(WorldUtils plugin, String filename) {
        super(plugin, filename);
    }

    /**
     * Set the author of a position
     *
     * @param position the position name
     * @param author   the author's name
     */
    public void setAuthor(String position, String author) {
        set("list." + position, author, true);
    }

    /**
     * Check if the position has a saved author
     *
     * @param position the position to be checked
     * @return true if an author was found, false otherwise
     */
    public boolean containsAuthor(String position) {
        return config.contains("list." + position);
    }

    /**
     * Get a String from a path
     *
     * @param position the position
     * @return String of the author of the position
     */
    public String getAuthor(String position) {
        return config.getString("list." + position);
    }

    /**
     * Get a Location from a path
     *
     * @param path the path of the Location
     * @return the Location at the path
     */
    public Location getLocation(String path) {
        return config.getLocation(path);
    }

    /**
     * Remove a position
     *
     * @param position the position to be removed
     */
    @Override
    public void remove(String position) {
        super.remove(position);
        if (containsAuthor(position)) super.remove("list." + position);
    }

    /**
     * Get all positions
     *
     * @return Set of Strings containing all positions
     */
    public Set<String> getPositions() {
        Set<String> keys = config.getKeys(false);
        keys.remove("list");
        return keys;
    }
}
