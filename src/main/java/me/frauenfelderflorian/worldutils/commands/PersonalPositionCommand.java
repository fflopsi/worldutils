package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Option;
import me.frauenfelderflorian.worldutils.config.Positions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommandExecutor and TabCompleter for command personalposition
 */
public record PersonalPositionCommand(JavaPlugin plugin) implements TabExecutor {
    public static final String command = "personalposition";
    private static String name = "";
    private static Positions positions;

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
        if (sender instanceof Player) {
            if (!name.equals(sender.getName())) {
                name = sender.getName();
                positions = new Positions(plugin, "positions_" + name + ".yml");
            }
            switch (args.length) {
                case 1 -> {
                    //command or position name entered
                    switch (args[0]) {
                        case "list" -> {
                            //send all position info
                            for (String pos : positions.getPositions())
                                sender.sendMessage(WorldUtils.Messages.positionMessage(
                                        pos, (Location) positions.get(pos)));
                            return true;
                        }
                        case "clear" -> {
                            //remove all positions
                            sender.sendMessage("§e§oCleared personal positions");
                            for (String pos : positions.getPositions()) positions.remove(pos);
                            return true;
                        }
                        default -> {
                            //position name entered
                            if (positions.contains(args[0]))
                                //existing position, send info
                                sender.sendMessage(WorldUtils.Messages.positionMessage(
                                        args[0], (Location) positions.get(args[0])));
                            else {
                                //new position name, save position
                                positions.set(args[0], ((Player) sender).getLocation(), true);
                                sender.sendMessage("§aAdded§r personal position "
                                        + WorldUtils.Messages.positionMessage(
                                        args[0], (Location) positions.get(args[0])));
                            }
                            return true;
                        }
                    }
                }
                case 2 -> {
                    //command and position entered
                    switch (args[0]) {
                        case "tp" -> {
                            //teleport player to position if OP
                            if (sender.isOp()) ((Player) sender).teleport((Location) positions.get(args[1]));
                            else WorldUtils.Messages.notAllowed(sender);
                            return true;
                        }
                        case "del" -> {
                            //delete position
                            sender.sendMessage("§cDeleted§r personal position "
                                    + WorldUtils.Messages.positionMessage(args[1], (Location) positions.get(args[1])));
                            positions.remove(args[1]);
                            return true;
                        }
                        default -> {
                            return otherPlayersPosition(sender, args);
                        }
                    }
                }
            }
        } else {
            if (args.length == 2) return otherPlayersPosition(sender, args);
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
        if (!name.equals(sender.getName())) {
            name = sender.getName();
            positions = new Positions(plugin, "positions_" + name + ".yml");
        }
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

    /**
     * Get another player's private position if possible
     *
     * @param sender sender of the commands
     * @param args   used arguments
     * @return true if correct command syntax used and no errors, false otherwise
     */
    private boolean otherPlayersPosition(CommandSender sender, String[] args) {
        if ((Boolean) WorldUtils.prefs.get(Option.PERSONALPOSITION_ACCESS_GLOBAL))
            try {
                if (Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                    //get personalposition from player
                    try {
                        Positions positions = new Positions(plugin, "positions_" + args[0] + ".yml");
                        sender.sendMessage("Personal position from player " + args[0] + ": "
                                + WorldUtils.Messages.positionMessage(args[1], (Location) positions.get(args[1])));
                    } catch (NullPointerException e) {
                        WorldUtils.Messages.positionNotFound(sender);
                    }
                    return true;
                }
            } catch (NullPointerException e) {
                WorldUtils.Messages.playerNotFound(sender);
                return true;
            }
        return false;
    }
}
