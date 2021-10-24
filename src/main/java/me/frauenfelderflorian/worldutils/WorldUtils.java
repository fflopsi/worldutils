package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.*;
import me.frauenfelderflorian.worldutils.config.Positions;
import me.frauenfelderflorian.worldutils.config.Prefs;
import me.frauenfelderflorian.worldutils.listeners.LTimerPaused;
import me.frauenfelderflorian.worldutils.listeners.Listeners;
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
    private static WorldUtils instance;
    public Prefs prefs;
    public Timer timer;

    /**
     * Done on plugin load before world loading
     */
    @Override
    public void onLoad() {
        //set plugin instance
        instance = this;
        //load config and set defaults
        prefs = new Prefs(this);
        for (Prefs.Option setting : Prefs.Option.values())
            if (!prefs.contains(setting) && setting.isVanilla()) prefs.set(setting, setting.getDefault(), true);
        //reset if needed
        if (prefs.getBoolean(Prefs.Option.RESET_RESET)) {
            //reset worlds
            for (String w : List.of("world", "world_nether", "world_the_end")) {
                try {
                    Files.walk(Paths.get(w)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            prefs.set(Prefs.Option.RESET_RESET, false, true);
            //delete positions if needed
            if (prefs.getBoolean(Prefs.Option.RESET_DELETE_POSITIONS))
                for (File file : Objects.requireNonNull(getDataFolder().listFiles()))
                    if (file.getName().startsWith("positions") && file.getName().endsWith(".yml")) file.delete();
            //reset Settings if needed
            if (prefs.getBoolean(Prefs.Option.RESET_RESET_SETTINGS))
                for (Prefs.Option stg : Prefs.Option.values()) prefs.set(stg, stg.getDefault(), true);
            getLogger().warning("Server reset");
        }
    }

    /**
     * Done on plugin enabling
     */
    @Override
    public void onEnable() {
        //load positions
        Positions positions = new Positions(this, "positions.yml");
        //set CommandExecutors and TabCompleters
        Objects.requireNonNull(getCommand(CPosition.CMD)).setExecutor(new CPosition(this, positions));
        Objects.requireNonNull(getCommand(CPosition.CMD)).setTabCompleter(new CPosition(this, positions));
        Objects.requireNonNull(getCommand(CPersonalPosition.CMD)).setExecutor(new CPersonalPosition(this));
        Objects.requireNonNull(getCommand(CPersonalPosition.CMD)).setTabCompleter(new CPersonalPosition(this));
        Objects.requireNonNull(getCommand(CSendPosition.CMD)).setExecutor(new CSendPosition());
        Objects.requireNonNull(getCommand(CSendPosition.CMD)).setTabCompleter(new CSendPosition());
        Objects.requireNonNull(getCommand(CTimer.CMD)).setExecutor(new CTimer(this));
        Objects.requireNonNull(getCommand(CTimer.CMD)).setTabCompleter(new CTimer(this));
        Objects.requireNonNull(getCommand(CReset.CMD)).setExecutor(new CReset(this));
        Objects.requireNonNull(getCommand(CReset.CMD)).setTabCompleter(new CReset(this));
        Objects.requireNonNull(getCommand(CSettings.CMD)).setExecutor(new CSettings(this));
        Objects.requireNonNull(getCommand(CSettings.CMD)).setTabCompleter(new CSettings(this));
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new LTimerPaused(this), this);
        //set up timer
        timer = new Timer(this);
    }

    /**
     * Done on plugin disabling
     */
    @Override
    public void onDisable() {
    }

    /**
     * Get an instance of this plugin, if it is running
     *
     * @return a WorldUtils object
     */
    public static WorldUtils getInstance() {
        return instance;
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
            target.sendMessage("§e§oWrong argument(s) were entered for this command.");
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
