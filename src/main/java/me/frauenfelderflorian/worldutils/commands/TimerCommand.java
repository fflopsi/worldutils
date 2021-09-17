package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.Timer;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
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
            case 2, 3, 4, 5 -> {
                switch (args[0]) {
                    case "set" -> {
                        WorldUtils.timer.set(getTime(args));
                        Bukkit.broadcastMessage("§eTimer set to §b"
                                + Timer.formatTime((int) WorldUtils.config.get(Settings.TIMER_TIME)));
                        return true;
                    }
                    case "add" -> {
                        WorldUtils.timer.set(
                                (int) WorldUtils.config.get(Settings.TIMER_TIME) + getTime(args));
                        Bukkit.broadcastMessage("§eAdded §b" + Timer.formatTime(getTime(args)) + " §eto timer");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> StringUtil.copyPartialMatches(args[0],
                    List.of("start", "stop", "reverse", "reset", "set", "add"), completions);
            case 2, 3, 4, 5 -> {
                if (List.of("set", "add").contains(args[0])) completions.add("<time>");
            }
        }
        return completions;
    }

    private int getTime(String[] args) {
        int time;
        try {
            time = 86400 * Integer.parseInt(args[1])
                    + 3600 * Integer.parseInt(args[2])
                    + 60 * Integer.parseInt(args[3])
                    + Integer.parseInt(args[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                time = 3600 * Integer.parseInt(args[1])
                        + 60 * Integer.parseInt(args[2])
                        + Integer.parseInt(args[3]);
            } catch (ArrayIndexOutOfBoundsException e1) {
                try {
                    time = 60 * Integer.parseInt(args[1])
                            + Integer.parseInt(args[2]);
                } catch (ArrayIndexOutOfBoundsException e2) {
                    time = Integer.parseInt(args[1]);
                }
            }
        }
        return time;
    }
}
