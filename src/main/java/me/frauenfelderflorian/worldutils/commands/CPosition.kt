package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Messages;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Positions;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * CommandExecutor and TabCompleter for command position
 */
public record CPosition(WorldUtils plugin, Positions positions) implements TabExecutor {
    public static final String CMD = "position";

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
            case 1 -> {
                //command or position name entered
                switch (args[0]) {
                    case "list" -> {
                        //send all position info
                        for (String position : positions.getPositions())
                            Messages.sendMessage(plugin, sender,
                                    Messages.positionMessage(position, positions.getLocation(position)));
                        return true;
                    }
                    case "clear" -> {
                        //remove all positions
                        Messages.sendMessage(plugin, "§e§oCleared positions");
                        for (String pos : positions.getPositions()) positions.remove(pos);
                        return true;
                    }
                    default -> {
                        //position name entered
                        if (positions.contains(args[0]))
                            //existing position, send info
                            if (positions.containsAuthor(args[0]))
                                Messages.sendMessage(plugin, sender, Messages.positionMessage(args[0],
                                        positions.getAuthor(args[0]), positions.getLocation(args[0])));
                            else
                                Messages.sendMessage(plugin, sender,
                                        Messages.positionMessage(args[0], positions.getLocation(args[0])));
                        else if (sender instanceof Player) {
                            //new position name, save position
                            if (!Pattern.matches("\\w+", args[0])) Messages.wrongArguments(plugin, sender);
                            else {
                                positions.set(args[0], ((Player) sender).getLocation(), true);
                                if (plugin.prefs.getBoolean(Prefs.Option.POSITION_SAVE_AUTHOR))
                                    positions.setAuthor(args[0], sender.getName());
                                Messages.sendMessage(plugin, "§aAdded§r position " + Messages.positionMessage(args[0],
                                        sender.getName(), positions.getLocation(args[0])));
                            }
                        } else Messages.notConsole(plugin, sender);
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
                            ((Player) sender).teleport(positions.getLocation(args[1]));
                        else if (sender instanceof Player) Messages.notAllowed(plugin, sender);
                        else Messages.notConsole(plugin, sender);
                        return true;
                    }
                    case "del" -> {
                        //delete position
                        try {
                            Location old = positions.getLocation(args[1]);
                            positions.remove(args[1]);
                            Messages.sendMessage(plugin, "§cDeleted§r position "
                                    + Messages.positionMessage(args[1], old));
                        } catch (UnsupportedOperationException | NullPointerException e) {
                            Messages.wrongArguments(plugin, sender);
                        }
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                //command or position name being entered
                StringUtil.copyPartialMatches(args[0], List.of("list", "clear", "del"), completions);
                if (sender instanceof Player && sender.isOp())
                    StringUtil.copyPartialMatches(args[0], List.of("tp"), completions);
                StringUtil.copyPartialMatches(args[0], positions.getPositions(), completions);
            }
            case 2 -> {
                //position name being entered
                if (args[0].equals("del") || sender instanceof Player && sender.isOp() && args[0].equals("tp"))
                    StringUtil.copyPartialMatches(args[1], positions.getPositions(), completions);
            }
        }
        return completions;
    }
}
