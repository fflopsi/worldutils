package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record PositionCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
    private static final List<String> SOLO_COMMANDS = new ArrayList<>(List.of("list", "clear"));
    private static final List<String> NAME_COMMANDS = new ArrayList<>(List.of("tp", "del"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "list" -> {
                        for (String pos : plugin.positions.getKeys(false))
                            sender.sendMessage(positionMessage(pos, (Location) plugin.positions.get(pos)));
                        return true;
                    }
                    case "clear" -> {
                        plugin.getLogger().info("Cleared positions");
                        Bukkit.broadcastMessage("Cleared positions");
                        for (String pos : plugin.positions.getKeys(false)) plugin.positions.remove(pos);
                        return true;
                    }
                    default -> {
                        if (plugin.positions.contains(args[0]))
                            if (plugin.positions.contains(args[0] + ".author"))
                                sender.sendMessage(positionMessage(
                                        args[0], (String) plugin.positions.get(args[0] + ".author"),
                                        (Location) plugin.positions.get(args[0])));
                            else sender.sendMessage(positionMessage(args[0], (Location) plugin.positions.get(args[0])));
                        else {
                            if (sender instanceof Player) {
                                plugin.positions.set(args[0], ((Player) sender).getLocation());
                                if ((Boolean) plugin.config.get(Settings.POSITION.getKey(0)))
                                    plugin.positions.set(args[0] + ".author", sender.getName());
                                Bukkit.broadcastMessage("Added position "
                                        + positionMessage(args[0], (Location) plugin.positions.get(args[0])));
                            } else WorldUtils.notConsole(sender);
                        }
                        return true;
                    }
                }
            }
            case 2 -> {
                switch (args[0]) {
                    case "tp" -> {
                        if (sender instanceof Player && sender.isOp())
                            ((Player) sender).teleport((Location) plugin.positions.get(args[1]));
                        else if (sender instanceof Player) WorldUtils.notAllowed(sender);
                        else WorldUtils.notConsole(sender);
                        return true;
                    }
                    case "del" -> {
                        Bukkit.broadcastMessage("Deleted position "
                                + positionMessage(args[1], (Location) plugin.positions.get(args[1])));
                        plugin.positions.remove(args[1]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String positionMessage(String name, String author, Location location) {
        return name + " from " + author + " (" + Objects.requireNonNull(location.getWorld()).getName() + "): "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }

    public static String positionMessage(String name, Location location) {
        return name + " (" + Objects.requireNonNull(location.getWorld()).getName() + "): "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                //command or position name being entered
                StringUtil.copyPartialMatches(args[0], SOLO_COMMANDS, completions);
                StringUtil.copyPartialMatches(args[0], NAME_COMMANDS, completions);
                StringUtil.copyPartialMatches(args[0], plugin.positions.getKeys(false), completions);
            }
            case 2 -> {
                //position name being entered
                for (String cmd : NAME_COMMANDS)
                    if (args[0].equals(cmd))
                        StringUtil.copyPartialMatches(args[1], plugin.positions.getKeys(false), completions);
            }
        }
        return completions;
    }
}
