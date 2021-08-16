package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.commands.PersonalPositionCommand;
import me.frauenfelderflorian.worldutils.commands.PositionCommand;
import me.frauenfelderflorian.worldutils.commands.ResetCommand;
import me.frauenfelderflorian.worldutils.commands.SettingsCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class WorldUtils extends JavaPlugin {
    public Config config;
    public Config positions;

    @Override
    public void onLoad() {
        config = new Config(this, "config.yml");
    }

    @Override
    public void onEnable() {
        positions = new Config(this, "positions.yml");
        Objects.requireNonNull(this.getCommand("position")).setExecutor(new PositionCommand(this));
        Objects.requireNonNull(this.getCommand("position")).setTabCompleter(new PositionCommand(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setExecutor(new PersonalPositionCommand(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setTabCompleter(new PersonalPositionCommand(this));
        Objects.requireNonNull(this.getCommand("reset")).setExecutor(new ResetCommand(this));
        Objects.requireNonNull(this.getCommand("reset")).setTabCompleter(new ResetCommand(this));
        Objects.requireNonNull(this.getCommand("settings")).setExecutor(new SettingsCommand(this));
        Objects.requireNonNull(this.getCommand("settings")).setTabCompleter(new SettingsCommand(this));
    }

    @Override
    public void onDisable() {
        config.save();
    }

    public static void notAllowed(CommandSender sender) {
        sender.sendMessage("§4You are not allowed to do this.");
    }

    public static void notConsole(CommandSender sender) {
        sender.sendMessage("§eThis is not a console command");
    }
}
