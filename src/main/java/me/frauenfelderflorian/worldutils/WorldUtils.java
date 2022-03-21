package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.*;
import me.frauenfelderflorian.worldutils.config.Positions;
import me.frauenfelderflorian.worldutils.config.Prefs;
import me.frauenfelderflorian.worldutils.listeners.LTimerPaused;
import me.frauenfelderflorian.worldutils.listeners.Listeners;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main plugin class
 */
public final class WorldUtils extends JavaPlugin {
    /**
     * Preferences for this plugin
     */
    public Prefs prefs;
    /**
     * Timer for this plugin
     */
    public Timer timer;
    /**
     * Contains all personal timers
     */
    private Map<Player, Timer> timers;
    /**
     * Contains the running instance of this plugin if available
     */
    private static WorldUtils instance;

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
            if (setting.isGlobal() && !prefs.contains(setting) && setting.isVanilla())
                prefs.set(setting, setting.getDefault(), true);
        //reset if needed
        if (prefs.getBoolean(Prefs.Option.RESET_RESET)) {
            //reset worlds
            for (String w : List.of("world", "world_nether", "world_the_end")) {
                try {
                    Files.walk(Paths.get(w)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                        if (file.delete()) getLogger().info("§e File§b " + file.getName() + " §edeleted.");
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            prefs.set(Prefs.Option.RESET_RESET, false, true);
            //delete positions if needed
            if (prefs.getBoolean(Prefs.Option.RESET_DELETE_POSITIONS))
                for (File file : Objects.requireNonNull(getDataFolder().listFiles()))
                    if (file.getName().startsWith("positions") && file.getName().endsWith(".yml"))
                        if (file.delete()) getLogger().info("§e File§b " + file.getName() + " §edeleted.");
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
        Positions positions = new Positions(this);
        //set CommandExecutors and TabCompleters
        Objects.requireNonNull(getCommand(CPosition.CMD)).setExecutor(new CPosition(this, positions));
        Objects.requireNonNull(getCommand(CPosition.CMD)).setTabCompleter(new CPosition(this, positions));
        Objects.requireNonNull(getCommand(CPPosition.CMD)).setExecutor(new CPPosition(this, positions));
        Objects.requireNonNull(getCommand(CPPosition.CMD)).setTabCompleter(new CPPosition(this, positions));
        Objects.requireNonNull(getCommand(CSendPosition.CMD)).setExecutor(new CSendPosition(this));
        Objects.requireNonNull(getCommand(CSendPosition.CMD)).setTabCompleter(new CSendPosition(this));
        Objects.requireNonNull(getCommand(CTimer.CMD)).setExecutor(new CTimer(this));
        Objects.requireNonNull(getCommand(CTimer.CMD)).setTabCompleter(new CTimer(this));
        Objects.requireNonNull(getCommand(CPTimer.CMD)).setExecutor(new CPTimer(this));
        Objects.requireNonNull(getCommand(CPTimer.CMD)).setTabCompleter(new CPTimer(this));
        Objects.requireNonNull(getCommand(CReset.CMD)).setExecutor(new CReset(this));
        Objects.requireNonNull(getCommand(CReset.CMD)).setTabCompleter(new CReset(this));
        Objects.requireNonNull(getCommand(CSettings.CMD)).setExecutor(new CSettings(this));
        Objects.requireNonNull(getCommand(CSettings.CMD)).setTabCompleter(new CSettings(this));
        //register Listeners
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getServer().getPluginManager().registerEvents(new LTimerPaused(this), this);
        //set up timer
        timer = new Timer(this);
        timers = new HashMap<>();
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
     * Add a personal Timer to the timers Map
     *
     * @param player the Player to whom the Timer belongs
     * @throws IllegalArgumentException if the Player already has a Timer assigned
     */
    public void addTimer(Player player) {
        if (timers.containsKey(player)) throw new IllegalArgumentException("This player already has a timer assigned.");
        timers.put(player, new Timer(this, player));
    }

    /**
     * Get a personal Timer from the timers Map
     *
     * @param player the Player to whom the Timer belongs
     * @return the Player's personal Timer
     */
    public Timer getTimer(Player player) {
        return timers.get(player);
    }

    /**
     * Remove a personal Timer from the timers Map
     *
     * @param player the Player to whom the Timer belongs
     */
    public void removeTimer(Player player) {
        timers.remove(player);
    }
}
