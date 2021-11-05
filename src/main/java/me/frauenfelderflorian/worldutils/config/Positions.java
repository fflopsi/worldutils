package me.frauenfelderflorian.worldutils.config;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Wrapper class for configuration file for positions, extends config.Config
 */
public class Positions extends Config {
    /**
     * Load the config file or create a new one if none is found
     *
     * @param plugin the plugin to whom the Positions belongs
     */
    public Positions(WorldUtils plugin) {
        super(plugin, "positions.yml");
    }

    /**
     * Set a personal position
     *
     * @param player the owner of the position
     * @param name   the name of the position
     */
    public void setPersonal(Player player, String name) {
        set(player.getUniqueId() + "." + name, player.getLocation(), true);
    }

    /**
     * Set the author of a position
     *
     * @param name   the position name
     * @param author the author's name
     */
    public void setAuthor(String name, String author) {
        set("list." + name, author, true);
    }

    /**
     * Check if the personal position exists
     *
     * @param player the Player whose personal positions to check
     * @param name   the name of the position to check
     * @return true if something is found, false otherwise
     */
    public boolean containsPersonal(Player player, String name) {
        return config.contains(player.getUniqueId() + "." + name);
    }

    /**
     * Check if the position has a saved author
     *
     * @param name the position to be checked
     * @return true if an author was found, false otherwise
     */
    public boolean containsAuthor(String name) {
        return config.contains("list." + name);
    }

    /**
     * Get a String from a path
     *
     * @param name the position name
     * @return String of the author of the position
     */
    public String getAuthor(String name) {
        return config.getString("list." + name);
    }

    /**
     * Get a Location from a path
     *
     * @param name the name of the Location
     * @return the Location at the path
     */
    public Location getLocation(String name) {
        return config.getLocation(name);
    }

    /**
     * Get a personal Location from a Player and the position name
     *
     * @param player the Player
     * @param name   the name of the position
     * @return the Location at the path
     */
    public Location getPersonalLocation(Player player, String name) {
        return config.getLocation(player.getUniqueId() + "." + name);
    }

    /**
     * Remove a position
     *
     * @param name the name of the position
     */
    @Override
    public void remove(String name) {
        super.remove(name);
        if (containsAuthor(name)) super.remove("list." + name);
    }

    /**
     * Remove a (personal) position
     *
     * @param player the owner of the position
     * @param name   the name of the position
     */
    public void remove(Player player, String name) {
        super.remove(player.getUniqueId() + "." + name);
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

    /**
     * Get the personal positions of a Player
     *
     * @param player the Player
     * @return a Set of Strings containing all position names
     */
    public Set<String> getPositions(Player player) {
        ConfigurationSection positions = config.getConfigurationSection(player.getUniqueId().toString());
        if (positions != null) {
            return positions.getKeys(false);
        }
        return null;
    }
}
