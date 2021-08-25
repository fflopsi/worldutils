package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommandExecutor and TabCompleter for command settings
 */
public class SettingsCommand implements CommandExecutor, TabCompleter {
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
                //only command entered
                if (Settings.contains(args[0])) {
                    sender.sendMessage("Enter a setting for command " + args[0]);
                    return true;
                }
            }
            case 2 -> {
                //command and setting entered
                if (Settings.contains(args[0])
                        && Objects.requireNonNull(Settings.get(args[0])).containsSetting(args[1])) {
                    sender.sendMessage("Enter a value for setting " + args[1] + " from command " + args[0]);
                    return true;
                }
            }
            case 3 -> {
                //command, setting and value entered
                if (Settings.contains(args[0])
                        && Objects.requireNonNull(Settings.get(args[0])).containsSetting(args[1])) {
                    if (args[2].equalsIgnoreCase("true")) {
                        WorldUtils.config.set(Objects.requireNonNull(Settings.get(args[0])).getKey(args[1]), true);
                        return true;
                    } else if (args[2].equalsIgnoreCase("false")) {
                        WorldUtils.config.set(Objects.requireNonNull(Settings.get(args[0])).getKey(args[1]), false);
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
            case 1 -> //command being entered
                    StringUtil.copyPartialMatches(args[0], Settings.getCommands(), completions);
            case 2 -> {
                //setting being entered
                if (Settings.contains(args[0]))
                    StringUtil.copyPartialMatches(
                            args[1], Objects.requireNonNull(Settings.get(args[0])).getSettings().keySet(), completions);
            }
            case 3 -> {
                //value being entered
                if (Settings.contains(args[0])
                        && Objects.requireNonNull(Settings.get(args[0])).containsSetting(args[1]))
                    StringUtil.copyPartialMatches(args[2], List.of("true", "false"), completions);
            }
        }
        return completions;
    }
}
