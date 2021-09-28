package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.*;
import me.frauenfelderflorian.worldutils.listeners.Listeners;
import me.frauenfelderflorian.worldutils.listeners.ListenersTimerPaused;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Main plugin class
 */
public final class WorldUtils extends JavaPlugin {
    public static Config config;
    public static Config positions;
    public static Timer timer;

    /**
     * Done on plugin load before world loading
     */
    @Override
    public void onLoad() {
        //load config and set defaults
        config = new Config(this, "config.yml");
        for (Settings setting : Settings.values())
            if (!config.contains(setting)) config.set(setting, setting.getDefault(), true);
        //reset if needed
        if ((Boolean) config.get(Settings.RESET_RESET)) {
            //reset worlds
            for (String w : List.of("world", "world_nether", "world_the_end")) {
                try {
                    Files.walk(Paths.get(w)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            config.set(Settings.RESET_RESET, false, true);
            //delete positions if needed
            if ((Boolean) config.get(Settings.RESET_DELETE_POSITIONS))
                for (File file : Objects.requireNonNull(getDataFolder().listFiles()))
                    if (file.getName().startsWith("positions") && file.getName().endsWith(".yml")) file.delete();
            //reset Settings if needed
            if ((Boolean) config.get(Settings.RESET_RESET_SETTINGS))
                for (Settings stg : Settings.values()) config.set(stg, stg.getDefault(), true);
            getLogger().warning("Server reset");
        }
    }

    /**
     * Done on plugin enabling
     */
    @Override
    public void onEnable() {
        //load positions
        positions = new Config(this, "positions.yml");
        //set CommandExecutors and TabCompleters
        Objects.requireNonNull(getCommand(PositionCommand.command)).setExecutor(new PositionCommand());
        Objects.requireNonNull(getCommand(PositionCommand.command)).setTabCompleter(new PositionCommand());
        Objects.requireNonNull(getCommand(PersonalPositionCommand.command))
                .setExecutor(new PersonalPositionCommand(this));
        Objects.requireNonNull(getCommand(PersonalPositionCommand.command))
                .setTabCompleter(new PersonalPositionCommand(this));
        Objects.requireNonNull(getCommand(SendPositionCommand.command)).setExecutor(new SendPositionCommand());
        Objects.requireNonNull(getCommand(SendPositionCommand.command)).setTabCompleter(new SendPositionCommand());
        Objects.requireNonNull(getCommand(TimerCommand.command)).setExecutor(new TimerCommand());
        Objects.requireNonNull(getCommand(TimerCommand.command)).setTabCompleter(new TimerCommand());
        Objects.requireNonNull(getCommand(ResetCommand.command)).setExecutor(new ResetCommand(this));
        Objects.requireNonNull(getCommand(ResetCommand.command)).setTabCompleter(new ResetCommand(this));
        Objects.requireNonNull(getCommand(SettingsCommand.command)).setExecutor(new SettingsCommand());
        Objects.requireNonNull(getCommand(SettingsCommand.command)).setTabCompleter(new SettingsCommand());
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getServer().getPluginManager().registerEvents(new ListenersTimerPaused(), this);
        //set up timer
        timer = new Timer(this);
    }

    /**
     * Done on plugin disabling
     */
    @Override
    public void onDisable() {
        config.set(Settings.TIMER_RUNNING, false, true);
        config.save(true);
    }

    public enum Messages {
        ;

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
         * Send a message to the target: "You are not allowed to do this."
         *
         * @param target the target to whom the message should be sent
         */
        public static void notAllowed(CommandSender target) {
            target.sendMessage("§4§lYou are not allowed to do this.");
        }

        /**
         * Send a message to the target: "This is not a console command."
         *
         * @param target the target to whom the message should be sent
         */
        public static void notConsole(CommandSender target) {
            target.sendMessage("§e§oThis is not a console command.");
        }

        /**
         * Send a message to the target: "Wrong argument(s) entered for this command."
         *
         * @param target the target to whom the message should be sent
         */
        public static void wrongArguments(CommandSender target) {
            target.sendMessage("§e§oWrong argument(s) entered for this command.");
        }

        /**
         * Send a message to the target: "A wrong number of arguments was entered for this command."
         *
         * @param target the target to whom the message should be sent
         */
        public static void wrongArgumentNumber(CommandSender target) {
            target.sendMessage("§e§oA wrong number of arguments was entered for this command.");
        }

        /**
         * Send a message to the target: "The entered name does not belong to an online player."
         *
         * @param target the target to whom the message should be sent
         */
        public static void playerNotFound(CommandSender target) {
            target.sendMessage("§e§oThe entered name does not belong to an online player.");
        }

        /**
         * Send a message to the target: "The entered position name cannot be found."
         *
         * @param target the target to whom the message should be sent
         */
        public static void positionNotFound(CommandSender target) {
            target.sendMessage("§e§oThe entered position name cannot be found.");
        }

        /**
         * Get the better world name formatted with an appropriate color:
         * <p>
         * world -> overworld
         * <p>
         * world_nether -> nether
         * <p>
         * world_the_end -> end
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
}
