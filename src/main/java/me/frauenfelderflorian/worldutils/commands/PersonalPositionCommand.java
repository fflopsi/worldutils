package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Config;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public record PersonalPositionCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
    private static final List<String> SOLO_COMMANDS = new ArrayList<>(List.of("list", "clear"));
    private static final List<String> NAME_COMMANDS = new ArrayList<>(List.of("tp", "del"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Config positions = new Config(plugin, "positions_" + sender.getName() + ".yml");
            if (args.length == 1) {
                switch (args[0]) {
                    case "list" -> {
                        for (String pos : positions.getKeys(false))
                            sender.sendMessage(PositionCommand.positionMessage(pos, (Location) positions.get(pos)));
                        return true;
                    }
                    case "clear" -> {
                        sender.sendMessage("Cleared personal positions");
                        for (String pos : positions.getKeys(false)) positions.remove(pos);
                        return true;
                    }
                    default -> {
                        if (positions.contains(args[0]))
                            sender.sendMessage(PositionCommand.positionMessage(args[0], (Location) positions.get(args[0])));
                        else {
                            positions.set(args[0], ((Player) sender).getLocation());
                            sender.sendMessage("Added personal position "
                                    + PositionCommand.positionMessage(args[0], (Location) positions.get(args[0])));
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "tp" -> {
                        if (sender.isOp())
                            ((Player) sender).teleport((Location) positions.get(args[1]));
                        else WorldUtils.notAllowed(sender);
                        return true;
                    }
                    case "del" -> {
                        sender.sendMessage("Deleted personal position "
                                + PositionCommand.positionMessage(args[1], (Location) positions.get(args[1])));
                        positions.remove(args[1]);
                        return true;
                    }
                }
            }
        } else WorldUtils.notConsole(sender);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Config positions = new Config(plugin, "positions_" + sender.getName() + ".yml");
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            //command or position name being entered
            StringUtil.copyPartialMatches(args[0], SOLO_COMMANDS, completions);
            StringUtil.copyPartialMatches(args[0], NAME_COMMANDS, completions);
            StringUtil.copyPartialMatches(args[0], positions.getKeys(false), completions);
        } else if (args.length == 2)
            //position name being entered
            for (String cmd : NAME_COMMANDS)
                if (args[0].equals(cmd))
                    StringUtil.copyPartialMatches(args[1], positions.getKeys(false), completions);
        return completions;
    }
}
