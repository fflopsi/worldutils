package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandExecutor and TabCompleter for command settings
 */
public class SettingsCommand implements TabExecutor {
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
        if (sender.isOp() || !((Boolean) WorldUtils.config.get(Settings.SETTINGS_NEED_OP))) {
            if (args.length == 3) {
                //command, setting and value entered
                Settings setting = Settings.get(args[0], args[1]);
                if (setting != null)
                    if (args[2].equals("true")) {
                        WorldUtils.config.set(setting, true, true);
                        Bukkit.broadcastMessage("Setting §b" + args[1] + "§r from command §b" + args[0] + "§r set to §atrue");
                        return true;
                    } else if (args[2].equals("false")) {
                        WorldUtils.config.set(setting, false, true);
                        Bukkit.broadcastMessage("Setting §b" + args[1] + "§r from command §b" + args[0] + "§r set to §cfalse");
                        return true;
                    } else if (args[2].equals("null")) {
                        WorldUtils.config.remove(setting);
                        Bukkit.broadcastMessage("Setting §b" + args[1] + "§r from command §b" + args[0] + "§r set to §enull");
                        Bukkit.broadcastMessage("§cUse with caution: §oThe plugin might not work correctly!");
                        return true;
                    }
            }
        } else {
            WorldUtils.Messages.notAllowed(sender);
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
            case 1 -> //command being entered
                    StringUtil.copyPartialMatches(args[0], Settings.getCommands(), completions);
            case 2 -> {
                //setting being entered
                if (Settings.getCommands().contains(args[0]))
                    StringUtil.copyPartialMatches(
                            args[1], Settings.getSettings(args[0]), completions);
            }
            case 3 -> {
                //value being entered
                if (Settings.get(args[0], args[1]) != null)
                    StringUtil.copyPartialMatches(args[2], List.of("true", "false"), completions);
            }
        }
        return completions;
    }
}
