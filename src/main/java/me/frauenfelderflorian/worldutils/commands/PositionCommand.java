package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Options;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CommandExecutor and TabCompleter for command position
 */
public class PositionCommand implements TabExecutor {
    public static final String command = "position";

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
        switch (args.length) {
            case 1 -> {
                //command or position name entered
                switch (args[0]) {
                    case "list" -> {
                        //send all position info
                        for (String position : WorldUtils.positions.getPositions())
                            sender.sendMessage(WorldUtils.Messages.positionMessage(
                                    position, (Location) WorldUtils.positions.get(position)));
                        return true;
                    }
                    case "clear" -> {
                        //remove all positions
                        Bukkit.broadcastMessage("§e§oCleared positions");
                        for (String pos : WorldUtils.positions.getPositions()) WorldUtils.positions.remove(pos);
                        return true;
                    }
                    default -> {
                        //position name entered
                        if (WorldUtils.positions.contains(args[0]))
                            //existing position, send info
                            if (WorldUtils.positions.contains("list." + args[0]))
                                sender.sendMessage(WorldUtils.Messages.positionMessage(
                                        args[0], (String) WorldUtils.positions.get("list." + args[0]),
                                        (Location) WorldUtils.positions.get(args[0])));
                            else sender.sendMessage(WorldUtils.Messages.positionMessage(
                                    args[0], (Location) WorldUtils.positions.get(args[0])));
                        else if (sender instanceof Player) {
                            //new position name, save position
                            if (!Pattern.matches("\\w+", args[0])) WorldUtils.Messages.wrongArguments(sender);
                            else {
                                WorldUtils.positions.set(args[0], ((Player) sender).getLocation(), true);
                                if ((Boolean) WorldUtils.prefs.get(Options.POSITION_SAVE_AUTHOR))
                                    WorldUtils.positions.set("list." + args[0], sender.getName(), true);
                                Bukkit.broadcastMessage("§aAdded§r position "
                                        + WorldUtils.Messages.positionMessage(args[0], sender.getName(),
                                        (Location) WorldUtils.positions.get(args[0])));
                            }
                        } else WorldUtils.Messages.notConsole(sender);
                        return true;
                    }
                }
            }
            case 2 -> {
                //command and position entered
                switch (args[0]) {
                    case "tp" -> {
                        //teleport player to position if OP
                        if (sender instanceof Player && sender.isOp())
                            ((Player) sender).teleport((Location) WorldUtils.positions.get(args[1]));
                        else if (sender instanceof Player) WorldUtils.Messages.notAllowed(sender);
                        else WorldUtils.Messages.notConsole(sender);
                        return true;
                    }
                    case "del" -> {
                        //delete position
                        Bukkit.broadcastMessage("§cDeleted§r position "
                                + WorldUtils.Messages.positionMessage(args[1], (Location) WorldUtils.positions.get(args[1])));
                        WorldUtils.positions.remove(args[1]);
                        if (WorldUtils.positions.contains("list." + args[1]))
                            WorldUtils.positions.remove("list." + args[1]);
                        return true;
                    }
                }
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
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                //command or position name being entered
                StringUtil.copyPartialMatches(args[0], List.of("list", "clear", "tp", "del"), completions); //tp only if sender.isOp()
                StringUtil.copyPartialMatches(args[0], WorldUtils.positions.getPositions(), completions);
            }
            case 2 -> {
                //position name being entered
                if (List.of("tp", "del").contains(args[0]))
                    StringUtil.copyPartialMatches(args[1], WorldUtils.positions.getPositions(), completions);
            }
        }
        return completions;
    }
}
