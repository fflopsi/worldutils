package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommandExecutor and TabCompleter for command sendposition
 */
public class SendPositionCommand implements TabExecutor {
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
                case 0 -> {
                    Bukkit.broadcastMessage(WorldUtils.Messages.positionMessage(
                            sender.getName(), ((Player) sender).getLocation()));
                    return true;
                }
                case 1 -> {
                    try {
                        Objects.requireNonNull(Bukkit.getPlayer(args[0])).sendMessage(WorldUtils.Messages.positionMessage(
                                sender.getName(), ((Player) sender).getLocation()));
                    } catch (NullPointerException e) {
                        WorldUtils.Messages.playerNotFound(sender);
                    }
                    return true;
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
        List<String> players = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            players.add(player.getName());
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], players, completions);
        }
        return completions;
    }
}
