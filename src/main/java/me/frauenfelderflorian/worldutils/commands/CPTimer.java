package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Timer;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

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
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player)
            switch (args.length) {
                case 1 -> {
                    switch (args[0]) {
                        case "visible" -> {
                            //change visibility status
                            plugin.getTimer((Player) sender).setVisible(
                                    !plugin.prefs.getBoolean((Player) sender, Prefs.Option.PTIMER_VISIBLE));
                            return true;
                        }
                        case "running" -> {
                            //change running status
                            plugin.getTimer((Player) sender).setRunning(
                                    !plugin.prefs.getBoolean((Player) sender, Prefs.Option.PTIMER_RUNNING));
                            return true;
                        }
                        case "reverse" -> {
                            //change reverse status
                            plugin.prefs.set((Player) sender, Prefs.Option.PTIMER_REVERSE,
                                    !plugin.prefs.getBoolean((Player) sender, Prefs.Option.PTIMER_REVERSE), true);
                            Bukkit.broadcastMessage("§eTimer reversed, now in §b"
                                    + (plugin.prefs.getBoolean((Player) sender, Prefs.Option.TIMER_REVERSE)
                                    ? "reverse" : "normal") + "§e mode.");
                            return true;
                        }
                        case "reset" -> {
                            //set timer to 0
                            plugin.getTimer((Player) sender).setRunning(false);
                            plugin.getTimer((Player) sender).setTime(0);
                            sender.sendMessage("§ePersonal timer set to 0.");
                            return true;
                        }
                    }
                }
                case 2, 3, 4, 5 -> {
                    switch (args[0]) {
                        case "set" -> {
                            //set time to input values
                            try {
                                plugin.getTimer((Player) sender).setTime(CTimer.getTime(args));
                                sender.sendMessage("§ePersonal timer set to §b"
                                        + Timer.formatTime(plugin.prefs.getInt((Player) sender, Prefs.Option.TIMER_TIME)));
                                return true;
                            } catch (IllegalStateException e) {
                                WorldUtils.Messages.wrongArgumentNumber(sender);
                            } catch (NumberFormatException e) {
                                WorldUtils.Messages.wrongArguments(sender);
                            }
                        }
                        case "add" -> {
                            //add input values to current time
                            try {
                                plugin.getTimer((Player) sender).setTime(
                                        plugin.prefs.getInt((Player) sender, Prefs.Option.TIMER_TIME) + CTimer.getTime(args));
                                sender.sendMessage("§eAdded §b" + Timer.formatTime(CTimer.getTime(args)) + " §eto personal timer");
                                return true;
                            } catch (IllegalStateException e) {
                                WorldUtils.Messages.wrongArgumentNumber(sender);
                            } catch (NumberFormatException e) {
                                WorldUtils.Messages.wrongArguments(sender);
                            }
                        }
                    }
                }
            }
        else {
            WorldUtils.Messages.notConsole(sender);
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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> StringUtil.copyPartialMatches(args[0],
                    List.of("visible", "running", "reverse", "reset", "set", "add"), completions);
            case 2, 3, 4, 5 -> {
                if (List.of("set", "add").contains(args[0])) completions.add("<time>");
            }
        }
        return completions;
    }
}
