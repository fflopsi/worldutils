package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TimerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "start" -> {
                        WorldUtils.timer.start();
                        Bukkit.broadcastMessage("§eTimer started.");
                        return true;
                    }
                    case "stop" -> {
                        WorldUtils.timer.stop();
                        Bukkit.broadcastMessage("§eTimer stopped.");
                        return true;
                    }
                    case "reverse" -> {
                        WorldUtils.config.set(Settings.TIMER_REVERSE,
                                !(Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE));
                        String reverse = (Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE) ? "reverse" : "normal";
                        Bukkit.broadcastMessage("§eTimer reversed, now in " + reverse + " mode.");
                        return true;
                    }
                    case "reset" -> {
                        WorldUtils.timer.set(0);
                        Bukkit.broadcastMessage("§eTimer set to 0.");
                        return true;
                    }
                }
            }
            case 2 -> {
                switch (args[0]) {
                    case "set" -> {
                        WorldUtils.timer.set(Integer.parseInt(args[1]));
                        Bukkit.broadcastMessage("§eTimer set to " + args[1] + ".");
                        return true;
                    }
                    case "add" -> {
                        WorldUtils.timer.set(
                                (int) WorldUtils.config.get(Settings.TIMER_TIME) + Integer.parseInt(args[1]));
                        Bukkit.broadcastMessage("§eAdded " + args[1] + " to timer.");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
