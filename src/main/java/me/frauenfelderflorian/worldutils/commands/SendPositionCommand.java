package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendPositionCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player)
            switch (args.length) {
                case 0 -> {
                    Bukkit.broadcastMessage(PositionCommand.positionMessage(
                            sender.getName(), ((Player) sender).getLocation()));
                    return true;
                }
                case 1 -> {
                    Objects.requireNonNull(Bukkit.getPlayer(args[0])).sendMessage(PositionCommand.positionMessage(
                            sender.getName(), ((Player) sender).getLocation()));
                    return true;
                }
            }
        else {
            WorldUtils.notConsole(sender);
            return true;
        }
        return false;
    }

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
