package me.frauenfelderflorian.worldutils;

import me.frauenfelderflorian.worldutils.completers.PersonalPositionCompleter;
import me.frauenfelderflorian.worldutils.completers.PositionCompleter;
import me.frauenfelderflorian.worldutils.executors.PersonalPositionExecutor;
import me.frauenfelderflorian.worldutils.executors.PositionExecutor;
import me.frauenfelderflorian.worldutils.executors.ResetExecutor;
import me.frauenfelderflorian.worldutils.executors.SettingsExecutor;
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
        Objects.requireNonNull(this.getCommand("position")).setExecutor(new PositionExecutor(this));
        Objects.requireNonNull(this.getCommand("position")).setTabCompleter(new PositionCompleter(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setExecutor(new PersonalPositionExecutor(this));
        Objects.requireNonNull(this.getCommand("personalposition")).setTabCompleter(new PersonalPositionCompleter(this));
        Objects.requireNonNull(this.getCommand("reset")).setExecutor(new ResetExecutor(this));
        Objects.requireNonNull(this.getCommand("settings")).setExecutor(new SettingsExecutor(this));
    }

    static public void notAllowed(CommandSender sender) {
        sender.sendMessage("§4You are not allowed to do this.");
    }

    static public void notConsoleCommand(CommandSender sender) {
        sender.sendMessage("§eThis is not a console command");
    }

    @Override
    public void onDisable() {
        positions.save();
    }
}
