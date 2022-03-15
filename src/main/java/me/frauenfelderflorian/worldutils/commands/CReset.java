package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Messages;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                             String[] args) {
        switch (args.length) {
            case 0 -> {
                //request a reset
                if (plugin.prefs.getBoolean(Prefs.Option.RESET_REQUESTED))
                    Messages.sendMessage("§eA server reset has already been requested.");
                else {
                    plugin.prefs.set(Prefs.Option.RESET_REQUESTED, true, true);
                    Messages.sendMessage(
                            "§eA server reset has been requested. Use §o/reset confirm§r§e or §o/reset cancel.");
                }
                return true;
            }
            case 1 -> {
                if (sender.isOp() || !plugin.prefs.getBoolean(Prefs.Option.RESET_NEED_OP)) switch (args[0]) {
                    case "cancel" -> {
                        //cancel a requested reset
                        if (plugin.prefs.getBoolean(Prefs.Option.RESET_REQUESTED)) {
                            plugin.prefs.set(Prefs.Option.RESET_REQUESTED, false, true);
                            Messages.sendMessage("§bThe server reset has been canceled.");
                        } else Messages.sendMessage("§bThere is no server reset requested.");
                        return true;
                    }
                    case "confirm" -> {
                        //confirm a requested reset
                        if (plugin.prefs.getBoolean(Prefs.Option.RESET_REQUESTED)) {
                            Messages.sendMessage("§c§oResetting server in 10 seconds.");
                            //kick players 1 second before restarting or shutting down
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                for (Player player : Bukkit.getOnlinePlayers())
                                    player.kickPlayer("§e§oResetting server.§r You can rejoin in a few moments.");
                            }, 200);
                            //restart or shut down
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                plugin.prefs.set(Prefs.Option.RESET_RESET, true, true);
                                plugin.prefs.set(Prefs.Option.RESET_REQUESTED, false, true);
                                if (plugin.prefs.getBoolean(Prefs.Option.RESET_RESTART_AFTER_RESET))
                                    Bukkit.spigot().restart();
                                else Bukkit.shutdown();
                            }, 220);
                        } else
                            Messages.sendMessage(
                                    "§eThere is no server reset requested. You can request one by using §o/reset");
                        return true;
                    }
                }
                else Messages.notAllowed(sender);
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args) {
        return List.of();
    }
}
