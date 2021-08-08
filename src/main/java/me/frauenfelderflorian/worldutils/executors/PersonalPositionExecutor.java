package me.frauenfelderflorian.worldutils.executors;

import me.frauenfelderflorian.worldutils.Config;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record PersonalPositionExecutor(WorldUtils plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            Config positions = new Config(plugin, "positions_" + sender.getName() + ".yml");
            if (args.length == 1) {
                switch (args[0]) {
                    case "list" -> {
                        for (String pos : positions.getKeys(false))
                            sender.sendMessage(PositionExecutor.positionMessage(pos, (Location) positions.get(pos)));
                        return true;
                    }
                    case "clear" -> {
                        sender.sendMessage("Cleared personal positions");
                        for (String pos : positions.getKeys(false)) positions.remove(pos);
                        return true;
                    }
                    default -> {
                        if (positions.contains(args[0]))
                            sender.sendMessage(PositionExecutor.positionMessage(args[0], (Location) positions.get(args[0])));
                        else {
                            positions.set(args[0], ((Player) sender).getLocation());
                            sender.sendMessage("Added personal position "
                                    + PositionExecutor.positionMessage(args[0], (Location) positions.get(args[0])));
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
                                + PositionExecutor.positionMessage(args[1], (Location) positions.get(args[1])));
                        positions.remove(args[1]);
                        return true;
                    }
                }
            }
        } else WorldUtils.notConsoleCommand(sender);
        return false;
    }
}
