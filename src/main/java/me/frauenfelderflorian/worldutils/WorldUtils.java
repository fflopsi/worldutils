package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Main plugin class
 */
public final class WorldUtils extends JavaPlugin {
    public Config config;
    public Config positions;

    /**
     * Done on plugin load before world loading
     */
    @Override
    public void onLoad() {
        config = new Config(this, "config.yml");
    }

    /**
     * Done on plugin enabling
     */
    @Override
    public void onEnable() {
        //get positions
        positions = new Config(this, "positions.yml");
        //set CommandExecutors and TabCompleters
        Objects.requireNonNull(this.getCommand("position")).setExecutor(new PositionCommand(this));
        Objects.requireNonNull(this.getCommand("position")).setTabCompleter(new PositionCommand(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setExecutor(new PersonalPositionCommand(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setTabCompleter(new PersonalPositionCommand(this));
        Objects.requireNonNull(this.getCommand("sendposition")).setExecutor(new SendPositionCommand());
        Objects.requireNonNull(this.getCommand("sendposition")).setTabCompleter(new SendPositionCommand());
        Objects.requireNonNull(this.getCommand("reset")).setExecutor(new ResetCommand(this));
        Objects.requireNonNull(this.getCommand("reset")).setTabCompleter(new ResetCommand(this));
        Objects.requireNonNull(this.getCommand("settings")).setExecutor(new SettingsCommand(this));
        Objects.requireNonNull(this.getCommand("settings")).setTabCompleter(new SettingsCommand(this));
    }

    /**
     * Done on plugin disabling
     */
    @Override
    public void onDisable() {
        config.save();
    }

    /**
     * Send a message to the target: "You are not allowed to do this."
     *
     * @param target the target to whom the message should be sent
     */
    public static void notAllowed(CommandSender target) {
        target.sendMessage("§4You are not allowed to do this.");
    }

    /**
     * Send a message to the target: "This is not a console command."
     *
     * @param target the target to whom the message should be sent
     */
    public static void notConsole(CommandSender target) {
        target.sendMessage("§eThis is not a console command");
    }
}
