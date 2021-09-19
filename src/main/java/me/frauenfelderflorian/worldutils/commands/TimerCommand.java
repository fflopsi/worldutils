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

/**
 * CommandExecutor and TabCompleter for command timer
 */
public class TimerCommand implements CommandExecutor, TabCompleter {
    /**
     * Done when command sent
     *
     * @param sender  sender of the command
     * @param command sent command
     * @param alias   used alias
     * @param args    used arguments
     * @return true if correct command syntax used and no errors, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "start" -> {
                        //start or resume timer
                        WorldUtils.timer.start();
                        Bukkit.broadcastMessage("§eTimer started.");
                        return true;
                    }
                    case "stop" -> {
                        //stop or pause timer
                        WorldUtils.timer.stop();
                        Bukkit.broadcastMessage("§eTimer stopped.");
                        return true;
                    }
                    case "reverse" -> {
                        //change reverse status
                        WorldUtils.config.set(Settings.TIMER_REVERSE,
                                !(Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE), true);
                        String reverse = (Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE) ? "reverse" : "normal";
                        Bukkit.broadcastMessage("§eTimer reversed, now in " + reverse + " mode.");
                        return true;
                    }
                    case "reset" -> {
                        //set timer to 0
                        WorldUtils.timer.set(0);
                        Bukkit.broadcastMessage("§eTimer set to 0.");
                        return true;
                    }
                }
            }
            case 2, 3, 4, 5 -> {
                switch (args[0]) {
                    case "set" -> {
                        //set time to input values
                        WorldUtils.timer.set(getTime(args));
                        Bukkit.broadcastMessage("§eTimer set to §b"
                                + Timer.formatTime((int) WorldUtils.config.get(Settings.TIMER_TIME)));
                        return true;
                    }
                    case "add" -> {
                        //add input values to current time
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

    /**
     * Done while entering command
     *
     * @param sender  sender of the command
     * @param command sent command
     * @param alias   used alias
     * @param args    used arguments
     * @return List of Strings for tab completion
     */
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

    /**
     * Get the time from input
     *
     * @param args String array containing all input arguments (length 2 - 5)
     * @return int of time in seconds
     */
    private int getTime(String[] args) {
        int time;
        switch (args.length) {
            case 2 -> time = Integer.parseInt(args[1]);
            case 3 -> time = 60 * Integer.parseInt(args[1])
                    + Integer.parseInt(args[2]);
            case 4 -> time = 3600 * Integer.parseInt(args[1])
                    + 60 * Integer.parseInt(args[2])
                    + Integer.parseInt(args[3]);
            case 5 -> time = 86400 * Integer.parseInt(args[1])
                    + 3600 * Integer.parseInt(args[2])
                    + 60 * Integer.parseInt(args[3])
                    + Integer.parseInt(args[4]);
            default -> throw new IllegalStateException("Array length must be >= 2 and <= 5");
        }
        return time;
    }
}
