package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * CommandExecutor and TabCompleter for command reset
 */
public record CReset(WorldUtils plugin) implements TabExecutor {
    public static final String CMD = "reset";

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
        if (!plugin.prefs.getBoolean(Prefs.Option.RESET_NEED_CONFIRM)
                || args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            Bukkit.broadcastMessage("§e§oResetting server in 10 seconds.");
            //kick players 2 seconds before restarting or shutting down
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.kickPlayer("§e§oResetting server.§r You can rejoin in a few moments.");
            }, 200);
            //restart or shut down
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.prefs.set(Prefs.Option.RESET_RESET, true, true);
                if (plugin.prefs.getBoolean(Prefs.Option.RESET_RESTART_AFTER_RESET)) Bukkit.spigot().restart();
                else Bukkit.shutdown();
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
        return List.of();
    }
}
