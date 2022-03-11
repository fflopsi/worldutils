package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * Class containing messaging methods
 */
public enum Messages {
    ;

    private final static String pluginPrefix = "[§3§lWorldUtils§r] ";

    /**
     * Broadcast a message with the plugin name as a prefix
     *
     * @param message message to be sent
     */
    public static void sendMessage(String message) {
        Bukkit.broadcastMessage(pluginPrefix + message);
    }

    /**
     * Send a message with the plugin name as a prefix to a CommandSender
     *
     * @param sender  CommandSender to whom the message should be sent
     * @param message message to be sent
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(pluginPrefix + message);
    }

    /**
     * Get a formatted message with position information
     *
     * @param location location of the position
     * @return String with formatted position
     */
    public static String positionMessage(Location location) {
        return betterWorld(Objects.requireNonNull(location.getWorld()).getName()) + ": "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }

    /**
     * Get a formatted message with position information
     *
     * @param name     name of the position
     * @param location location of the position
     * @return String with formatted position
     */
    public static String positionMessage(String name, Location location) {
        return "§b" + name + "§r (" + betterWorld(Objects.requireNonNull(location.getWorld()).getName()) + "): "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }

    /**
     * Get a formatted message with position information
     *
     * @param name     name  of the position
     * @param author   who saved the position
     * @param location location of the position
     * @return String with formatted position
     */
    public static String positionMessage(String name, String author, Location location) {
        return "§b" + name + "§r from §b§o" + author
                + "§r (" + betterWorld(Objects.requireNonNull(location.getWorld()).getName()) + "): "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }

    /**
     * Get a message:
     * <p>
     * Setting <span style="color:blue">[command.setting]</span> set to <span style="color:blue">[value]</span>.
     *
     * @param setting the setting
     * @param value   the new value of the setting
     */
    public static String settingSet(Prefs.Option setting, String value) {
        return "Setting §b" + setting.getKey() + "§r set to §b" + value + "§r.";
    }

    /**
     * Send a message to the target:
     * "<span style="color:red; font-weight:bold">You are not allowed to do this.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void notAllowed(CommandSender target) {
        Messages.sendMessage(target, "§c§lYou are not allowed to do this.");
    }

    /**
     * Send a message to the target:
     * "<span style="color:yellow; font-style:italic">This is not a console command.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void notConsole(CommandSender target) {
        Messages.sendMessage(target, "§e§oThis is not a console command.");
    }

    /**
     * Send a message to the target:
     * "<span style="color:yellow; font-style:italic">Wrong argument(s) entered for this command.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void wrongArguments(CommandSender target) {
        Messages.sendMessage(target, "§e§oWrong argument(s) were entered for this command.");
    }

    /**
     * Send a message to the target:
     * "<span style="color:yellow; font-style:italic">A wrong number of arguments was entered for this command.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void wrongArgumentNumber(CommandSender target) {
        Messages.sendMessage(target, "§e§oA wrong number of arguments was entered for this command.");
    }

    /**
     * Send a message to the target:
     * "<span style="color:yellow; font-style:italic">The entered name does not belong to an online player.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void playerNotFound(CommandSender target) {
        Messages.sendMessage(target, "§e§oThe entered name does not belong to an online player.");
    }

    /**
     * Send a message to the target:
     * "<span style="color:yellow; font-style:italic">The entered position name cannot be found.</span>"
     *
     * @param target the target to whom the message should be sent
     */
    public static void positionNotFound(CommandSender target) {
        Messages.sendMessage(target, "§e§oThe entered position name cannot be found.");
    }

    /**
     * Get the better world name formatted with an appropriate color:
     * <p>
     * world -> <span style="color:green">overworld</span>
     * <p>
     * world_nether -> <span style="color:red">nether</span>
     * <p>
     * world_the_end -> <span style="color:yellow">end</span>
     *
     * @param world the unformatted world name
     * @return the better world name if one of the above, else the given String
     */
    private static String betterWorld(String world) {
        String betterName;
        switch (world) {
            case "world" -> betterName = "§aoverworld§r";
            case "world_nether" -> betterName = "§cnether§r";
            case "world_the_end" -> betterName = "§eend§r";
            default -> betterName = world;
        }
        return betterName;
    }
}
