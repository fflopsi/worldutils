package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SettingsCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
    private static final Map<String, List<String>> settings = new HashMap<>(Map.of(
            "position", List.of(),
            "personalposition", List.of("makeAccessibleGlobally"),
            "reset", List.of("needConfirm", "restartAfterReset")));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                if (settings.containsKey(args[0])) {
                    sender.sendMessage("Enter a setting for command " + args[0]);
                    return true;
                }
            }
            case 2 -> {
                if (settings.containsKey(args[0]) && settings.get(args[0]).contains(args[1])) {
                    sender.sendMessage("Enter a value for setting " + args[1] + " from command " + args[0]);
                    return true;
                }
            }
            case 3 -> {
                if (settings.containsKey(args[0]) && settings.get(args[0]).contains(args[1])) {
                    if (args[2].equalsIgnoreCase("true")) {
                        plugin.config.set(args[0] + "." + args[1], true);
                        return true;
                    } else if (args[2].equalsIgnoreCase("false")) {
                        plugin.config.set(args[0] + "." + args[1], false);
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
            case 1 -> StringUtil.copyPartialMatches(args[0], settings.keySet(), completions);
            case 2 -> {
                if (settings.containsKey(args[0]))
                    StringUtil.copyPartialMatches(args[1], settings.get(args[0]), completions);
            }
            case 3 -> {
                if (settings.containsKey(args[0]) && settings.get(args[0]).contains(args[1]))
                    StringUtil.copyPartialMatches(args[2], List.of("true", "false"), completions);
            }
        }
        return completions;
    }
}
