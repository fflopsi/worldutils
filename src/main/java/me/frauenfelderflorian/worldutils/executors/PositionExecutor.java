package me.frauenfelderflorian.worldutils.executors;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public record PositionExecutor(WorldUtils plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
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
                        sender.sendMessage(positionMessage(args[0], (Location) plugin.positions.get(args[0])));
                    else {
                        if (sender instanceof Player) {
                            plugin.positions.set(args[0], ((Player) sender).getLocation());
                            Bukkit.broadcastMessage("Added position "
                                    + positionMessage(args[0], (Location) plugin.positions.get(args[0])));
                        } else WorldUtils.notConsoleCommand(sender);
                    }
                    return true;
                }
            }
        } else if (args.length == 2) {
            switch (args[0]) {
                case "tp" -> {
                    if (sender instanceof Player && sender.isOp())
                        ((Player) sender).teleport((Location) plugin.positions.get(args[1]));
                    else if (sender instanceof Player) WorldUtils.notAllowed(sender);
                    else WorldUtils.notConsoleCommand(sender);
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
        return false;
    }

    public static String positionMessage(String name, Location location) {
        return name + " (" + Objects.requireNonNull(location.getWorld()).getName() + "): "
                + location.getBlockX() + "  " + location.getBlockY() + "  " + location.getBlockZ();
    }
}
