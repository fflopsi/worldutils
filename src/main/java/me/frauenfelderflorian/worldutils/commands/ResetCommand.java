package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Options;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandExecutor and TabCompleter for command reset
 */
public record ResetCommand(JavaPlugin plugin) implements TabExecutor {
    public static final String command = "reset";

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
        if (!(Boolean) WorldUtils.prefs.get(Options.RESET_NEED_CONFIRM)
                || args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            Bukkit.broadcastMessage("§e§oResetting server in 10 seconds.");
            //kick players 2 seconds before restarting
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.kickPlayer("§e§oResetting server.§r You can rejoin in a few moments.");
            }, 200);
            //restart
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                WorldUtils.prefs.set(Options.RESET_RESET, true, true);
                Bukkit.spigot().restart();
            }, 220);
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
        if ((Boolean) WorldUtils.prefs.get(Options.RESET_NEED_CONFIRM) && args.length == 1)
            StringUtil.copyPartialMatches(args[0], List.of("confirm"), completions);
        return completions;
    }
}
