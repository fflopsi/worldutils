package me.frauenfelderflorian.worldutils.commands;

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
 * CommandExecutor and TabCompleter for command settings
 */
public record CSettings(WorldUtils plugin) implements TabExecutor {
    public static final String CMD = "settings";

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
        if (args.length == 3) {
            //correct number of arguments
            Prefs.Option setting = Prefs.Option.get(args[0], args[1]);
            if (setting != null && List.of("true", "false", "null").contains(args[2])) {
                //correct setting and value
                if (!setting.isGlobal() && sender instanceof Player) {
                    //personal setting
                    switch (args[2]) {
                        case "true" -> {
                            //set to true
                            plugin.prefs.set((Player) sender, setting, true, true);
                            sender.sendMessage(WorldUtils.Messages.settingSet(setting, "true"));
                            return true;
                        }
                        case "false" -> {
                            //set to false
                            plugin.prefs.set((Player) sender, setting, false, true);
                            sender.sendMessage(WorldUtils.Messages.settingSet(setting, "false"));
                            return true;
                        }
                        case "null" -> {
                            //remove setting
                            plugin.prefs.remove((Player) sender, setting);
                            sender.sendMessage(WorldUtils.Messages.settingSet(setting, "null"));
                            sender.sendMessage("§cUse with caution: §oThe plugin might not work correctly!");
                            return true;
                        }
                    }
                } else if (setting.isGlobal()
                        && (sender.isOp() || !plugin.prefs.getBoolean(Prefs.Option.SETTINGS_NEED_OP))) {
                    //global setting
                    switch (args[2]) {
                        case "true" -> {
                            //set to true
                            plugin.prefs.set(setting, true, true);
                            Bukkit.broadcastMessage(WorldUtils.Messages.settingSet(setting, "true"));
                            return true;
                        }
                        case "false" -> {
                            //set to false
                            plugin.prefs.set(setting, false, true);
                            sender.sendMessage(WorldUtils.Messages.settingSet(setting, "false"));
                            return true;
                        }
                        case "null" -> {
                            //remove setting
                            plugin.prefs.remove(setting);
                            Bukkit.broadcastMessage(WorldUtils.Messages.settingSet(setting, "null"));
                            Bukkit.broadcastMessage("§cUse with caution: §oThe plugin might not work correctly!");
                            return true;
                        }
                    }
                }
            } else WorldUtils.Messages.wrongArguments(sender);
        } else WorldUtils.Messages.wrongArgumentNumber(sender);
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
            case 1 -> {
                //command being entered
                if (sender.isOp() || !plugin.prefs.getBoolean(Prefs.Option.SETTINGS_NEED_OP)) {
                    StringUtil.copyPartialMatches(args[0], Prefs.Option.getGlobalCommands(), completions);
                }
                if (sender instanceof Player) {
                    List<String> commands = new ArrayList<>(Prefs.Option.getCommands());
                    commands.removeAll(Prefs.Option.getGlobalCommands());
                    StringUtil.copyPartialMatches(args[0], commands, completions);
                }
            }
            case 2 -> {
                //setting being entered
                if (Prefs.Option.getCommands().contains(args[0])) {
                    if (sender.isOp() || !plugin.prefs.getBoolean(Prefs.Option.SETTINGS_NEED_OP)) {
                        StringUtil.copyPartialMatches(args[1], Prefs.Option.getGlobalSettings(args[0]), completions);
                    }
                    if (sender instanceof Player) {
                        List<String> settings = new ArrayList<>(Prefs.Option.getSettings(args[0]));
                        settings.removeAll(Prefs.Option.getGlobalSettings(args[0]));
                        StringUtil.copyPartialMatches(args[1], settings, completions);
                    }
                }
            }
            case 3 -> {
                //value being entered
                Prefs.Option setting = Prefs.Option.get(args[0], args[1]);
                if (setting != null && (sender instanceof Player && !setting.isGlobal()
                        || (sender.isOp() || !plugin.prefs.getBoolean(Prefs.Option.SETTINGS_NEED_OP))
                        && setting.isGlobal()))
                    StringUtil.copyPartialMatches(args[2], List.of("true", "false"), completions);
            }
        }
        return completions;
    }
}
