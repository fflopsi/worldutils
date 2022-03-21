package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Messages;
import me.frauenfelderflorian.worldutils.Timer;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandExecutor and TabCompleter for command personaltimer
 */
public record CPTimer(WorldUtils plugin) implements TabExecutor {
    public static final String CMD = "personaltimer";

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                             String[] args) {
        if (sender instanceof Player) switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "visible" -> {
                        //change visibility status
                        plugin.getTimer((Player) sender).setVisible(!plugin.prefs.getBoolean(
                                (Player) sender, Prefs.Option.PTIMER_VISIBLE));
                        return true;
                    }
                    case "running" -> {
                        //change running status
                        plugin.getTimer((Player) sender).setRunning(!plugin.prefs.getBoolean(
                                (Player) sender, Prefs.Option.PTIMER_RUNNING));
                        return true;
                    }
                    case "reverse" -> {
                        //change reverse status
                        plugin.prefs.set((Player) sender, Prefs.Option.PTIMER_REVERSE, !plugin.prefs.getBoolean(
                                (Player) sender, Prefs.Option.PTIMER_REVERSE), true);
                        Messages.sendMessage(plugin, "§eTimer reversed, now in §b" + (plugin.prefs.getBoolean(
                                (Player) sender, Prefs.Option.TIMER_REVERSE) ? "reverse" : "normal") + "§e mode.");
                        return true;
                    }
                    case "reset" -> {
                        //set timer to 0
                        plugin.getTimer((Player) sender).setRunning(false);
                        plugin.getTimer((Player) sender).setTime(0);
                        Messages.sendMessage(plugin, sender, "§ePersonal timer set to 0.");
                        return true;
                    }
                }
            }
            case 2, 3, 4, 5 -> {
                switch (args[0]) {
                    case "invite", "join" -> {
                        //add another player to the personal timer or join another player's personal timer
                        if (args.length == 2) {
                            Player other = Bukkit.getPlayer(args[1]);
                            if (other != null && other.isOnline()) {
                                if (args[0].equals("invite")
                                        && !plugin.getTimer((Player) sender).containsPlayer(other)) {
                                    plugin.getTimer((Player) sender).addPlayer(other);
                                    return true;
                                } else if (args[0].equals("join")
                                        && !plugin.getTimer(other).containsPlayer((Player) sender)
                                        && plugin.prefs.getBoolean(other, Prefs.Option.PTIMER_JOINABLE)) {
                                    plugin.getTimer(other).addPlayer((Player) sender);
                                    return true;
                                }
                            } else Messages.playerNotFound(plugin, sender);
                        }
                    }
                    case "leave", "remove" -> {
                        //leave another player's personal timer or remove a player from the personal timer
                        if (args.length == 2) {
                            Player other = Bukkit.getPlayer(args[1]);
                            if (other != null && other.isOnline()) {
                                if (args[0].equals("leave") && plugin.getTimer(other).containsPlayer((Player) sender)) {
                                    plugin.getTimer(other).removePlayer((Player) sender);
                                    return true;
                                } else if (args[0].equals("remove")
                                        && plugin.getTimer((Player) sender).containsPlayer(other)) {
                                    plugin.getTimer((Player) sender).removePlayer(other);
                                    return true;
                                }
                            } else Messages.playerNotFound(plugin, sender);
                        }
                    }
                    case "set" -> {
                        //set time to input values
                        try {
                            plugin.getTimer((Player) sender).setTime(CTimer.getTime(args));
                            Messages.sendMessage(plugin, sender, "§ePersonal timer set to §b"
                                    + Timer.formatTime(plugin.prefs.getInt((Player) sender, Prefs.Option.TIMER_TIME)));
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
                            plugin.getTimer((Player) sender).setTime(plugin.prefs.getInt(
                                    (Player) sender, Prefs.Option.TIMER_TIME) + CTimer.getTime(args));
                            Messages.sendMessage(plugin, sender, "§eAdded §b"
                                    + Timer.formatTime(CTimer.getTime(args)) + " §eto personal timer");
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
        else {
            Messages.notConsole(plugin, sender);
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        //get names of all online players
        List<String> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            players.add(player.getName());
        //get names of all players that can view the personal timer
        List<String> removablePlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            if (plugin.getTimer((Player) sender).containsPlayer(player)) removablePlayers.add(player.getName());
        //get names of all players whose personal timer is visible
        List<String> leavableTimers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            if (plugin.getTimer(player).containsPlayer((Player) sender)) leavableTimers.add(player.getName());
        switch (args.length) {
            case 1 -> StringUtil.copyPartialMatches(args[0], List.of("visible", "running", "reverse", "reset", "invite",
                    "join", "leave", "remove", "set", "add"), completions);
            case 2, 3, 4, 5 -> {
                if (args.length == 2) switch (args[0]) {
                    case "invite" -> StringUtil.copyPartialMatches(args[1], players, completions);
                    case "leave" -> StringUtil.copyPartialMatches(args[1], leavableTimers, completions);
                    case "remove" -> StringUtil.copyPartialMatches(args[1], removablePlayers, completions);
                }
                if (List.of("set", "add").contains(args[0])) completions.add("<time>");
            }
        }
        return completions;
    }
}
