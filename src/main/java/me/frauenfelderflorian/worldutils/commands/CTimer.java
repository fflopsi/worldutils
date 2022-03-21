package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Messages;
import me.frauenfelderflorian.worldutils.Timer;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandExecutor and TabCompleter for command timer
 */
public record CTimer(WorldUtils plugin) implements TabExecutor {
    public static final String CMD = "timer";

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
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (sender.isOp() || plugin.timer.containsPlayer((Player) sender)) switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "visible" -> {
                        //add or remove player: changes visibility status for one single player
                        if (sender instanceof Player) {
                            if (plugin.timer.containsPlayer((Player) sender))
                                plugin.timer.removePlayer((Player) sender);
                            else plugin.timer.addPlayer((Player) sender);
                            Messages.sendMessage(plugin, sender, "§eTimer set to " +
                                    (plugin.timer.containsPlayer((Player) sender) ? "visible." : "invisible."));
                        } else Messages.notConsole(plugin, sender);
                        return true;
                    }
                    case "running" -> {
                        //change running status
                        plugin.timer.setRunning(!plugin.prefs.getBoolean(Prefs.Option.TIMER_RUNNING));
                        return true;
                    }
                    case "reverse" -> {
                        //change reverse status
                        plugin.prefs.set(Prefs.Option.TIMER_REVERSE,
                                !plugin.prefs.getBoolean(Prefs.Option.TIMER_REVERSE), true);
                        Messages.sendMessage(plugin, "§eTimer reversed, now in §b" +
                                (plugin.prefs.getBoolean(Prefs.Option.TIMER_REVERSE) ? "reverse" : "normal")
                                + "§e mode.");
                        return true;
                    }
                    case "reset" -> {
                        //set timer to 0
                        plugin.timer.setRunning(false);
                        plugin.timer.setTime(0);
                        Messages.sendMessage(plugin, "§eTimer set to 0.");
                        return true;
                    }
                }
            }
            case 2, 3, 4, 5 -> {
                switch (args[0]) {
                    case "set" -> {
                        //set time to input values
                        try {
                            plugin.timer.setTime(getTime(args));
                            Messages.sendMessage(plugin, "§eTimer set to §b" +
                                    Timer.formatTime(plugin.prefs.getInt(Prefs.Option.TIMER_TIME)));
                            return true;
                        } catch (IllegalStateException e) {
                            Messages.wrongArgumentNumber(plugin, sender);
                        } catch (NumberFormatException e) {
                            Messages.wrongArguments(plugin, sender);
                        }
                    }
                    case "add" -> {
                        //add input values to current time
                        try {
                            plugin.timer.setTime(plugin.prefs.getInt(Prefs.Option.TIMER_TIME) + getTime(args));
                            Messages.sendMessage(plugin,
                                    "§eAdded §b" + Timer.formatTime(getTime(args)) + " §eto timer");
                            return true;
                        } catch (IllegalStateException e) {
                            Messages.wrongArgumentNumber(plugin, sender);
                        } catch (NumberFormatException e) {
                            Messages.wrongArguments(plugin, sender);
                        }
                    }
                }
            }
        }
        else if (args.length == 1 && args[0].equals("visible")) {
            plugin.timer.addPlayer((Player) sender);
            return true;
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
    public List<String> onTabComplete(CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender.isOp() || plugin.timer.containsPlayer((Player) sender)) switch (args.length) {
            case 1 -> StringUtil.copyPartialMatches(args[0],
                    List.of("visible", "running", "reverse", "reset", "set", "add"), completions);
            case 2, 3, 4, 5 -> {
                if (List.of("set", "add").contains(args[0])) completions.add("<time>");
            }
        }
        else if (args.length == 1) StringUtil.copyPartialMatches(args[0], List.of("visible"), completions);
        return completions;
    }

    /**
     * Get the time from input
     *
     * @param args String array containing all input arguments (length 2 - 5)
     * @return int of time in seconds
     */
    public static int getTime(String[] args) {
        int time;
        switch (args.length) {
            case 2 -> time = Integer.parseInt(args[1]);
            case 3 -> time = 60 * Integer.parseInt(args[1]) + Integer.parseInt(args[2]);
            case 4 -> time = 3600 * Integer.parseInt(args[1]) + 60 * Integer.parseInt(args[2])
                    + Integer.parseInt(args[3]);
            case 5 -> time = 86400 * Integer.parseInt(args[1]) + 3600 * Integer.parseInt(args[2])
                    + 60 * Integer.parseInt(args[3]) + Integer.parseInt(args[4]);
            default -> throw new IllegalStateException("Array length must be >= 2 and <= 5");
        }
        return time;
    }
}
