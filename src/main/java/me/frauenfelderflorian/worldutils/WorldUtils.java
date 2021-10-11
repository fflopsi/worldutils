package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.*;
import me.frauenfelderflorian.worldutils.config.Option;
import me.frauenfelderflorian.worldutils.config.Positions;
import me.frauenfelderflorian.worldutils.config.Prefs;
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
    public static Prefs prefs;
    public static Positions positions;
    public static Timer timer;

    /**
     * Done on plugin load before world loading
     */
    @Override
    public void onLoad() {
        //load config and set defaults
        prefs = new Prefs(this);
        for (Option setting : Option.values())
            if (!prefs.contains(setting)) prefs.set(setting, setting.getDefault(), true);
        //reset if needed
        if ((Boolean) prefs.get(Option.RESET_RESET)) {
            //reset worlds
            for (String w : List.of("world", "world_nether", "world_the_end")) {
                try {
                    Files.walk(Paths.get(w)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            prefs.set(Option.RESET_RESET, false, true);
            //delete positions if needed
            if ((Boolean) prefs.get(Option.RESET_DELETE_POSITIONS))
                for (File file : Objects.requireNonNull(getDataFolder().listFiles()))
                    if (file.getName().startsWith("positions") && file.getName().endsWith(".yml")) file.delete();
            //reset Settings if needed
            if ((Boolean) prefs.get(Option.RESET_RESET_SETTINGS))
                for (Option stg : Option.values()) prefs.set(stg, stg.getDefault(), true);
            getLogger().warning("Server reset");
        }
    }

    /**
     * Done on plugin enabling
     */
    @Override
    public void onEnable() {
        //load positions
        positions = new Positions(this, "positions.yml");
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
        prefs.set(Option.TIMER_RUNNING, false, true);
        prefs.save(true);
    }

    /**
     * Class containing messaging methods
     */
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
         * Send a message to the target:
         * "<span style="color:red; font-weight:bold">You are not allowed to do this.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void notAllowed(CommandSender target) {
            target.sendMessage("§c§lYou are not allowed to do this.");
        }

        /**
         * Send a message to the target:
         * "<span style="color:yellow; font-style:italic">This is not a console command.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void notConsole(CommandSender target) {
            target.sendMessage("§e§oThis is not a console command.");
        }

        /**
         * Send a message to the target:
         * "<span style="color:yellow; font-style:italic">Wrong argument(s) entered for this command.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void wrongArguments(CommandSender target) {
            target.sendMessage("§e§oWrong argument(s) entered for this command.");
        }

        /**
         * Send a message to the target:
         * "<span style="color:yellow; font-style:italic">A wrong number of arguments was entered for this command.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void wrongArgumentNumber(CommandSender target) {
            target.sendMessage("§e§oA wrong number of arguments was entered for this command.");
        }

        /**
         * Send a message to the target:
         * "<span style="color:yellow; font-style:italic">The entered name does not belong to an online player.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void playerNotFound(CommandSender target) {
            target.sendMessage("§e§oThe entered name does not belong to an online player.");
        }

        /**
         * Send a message to the target:
         * "<span style="color:yellow; font-style:italic">The entered position name cannot be found.</span>"
         *
         * @param target the target to whom the message should be sent
         */
        public static void positionNotFound(CommandSender target) {
            target.sendMessage("§e§oThe entered position name cannot be found.");
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
}
