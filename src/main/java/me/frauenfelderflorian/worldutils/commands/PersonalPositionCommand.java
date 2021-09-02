package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Config;
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

/**
 * CommandExecutor and TabCompleter for command personalposition
 */
public record PersonalPositionCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
    private static final List<String> SOLO_COMMANDS = new ArrayList<>(List.of("list", "clear"));
    private static final List<String> NAME_COMMANDS = new ArrayList<>(List.of("tp", "del"));

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
            Config positions = new Config(plugin, "positions_" + sender.getName() + ".yml");
            switch (args.length) {
                case 1 -> {
                    //command or position name entered
                    switch (args[0]) {
                        case "list" -> {
                            //send all position info
                            for (String pos : positions.getKeys(false))
                                sender.sendMessage(WorldUtils.positionMessage(pos, (Location) positions.get(pos)));
                            return true;
                        }
                        case "clear" -> {
                            //remove all positions
                            sender.sendMessage("Cleared personal positions");
                            for (String pos : positions.getKeys(false)) positions.remove(pos);
                            return true;
                        }
                        default -> {
                            //position name entered
                            if (positions.contains(args[0]))
                                //existing position, send info
                                sender.sendMessage(WorldUtils.positionMessage(args[0], (Location) positions.get(args[0])));
                            else {
                                //new position name, save position
                                positions.set(args[0], ((Player) sender).getLocation());
                                sender.sendMessage("Added personal position "
                                        + WorldUtils.positionMessage(args[0], (Location) positions.get(args[0])));
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
                            if (sender.isOp())
                                ((Player) sender).teleport((Location) positions.get(args[1]));
                            else WorldUtils.notAllowed(sender);
                            return true;
                        }
                        case "del" -> {
                            //delete position
                            sender.sendMessage("Deleted personal position "
                                    + WorldUtils.positionMessage(args[1], (Location) positions.get(args[1])));
                            positions.remove(args[1]);
                            return true;
                        }
                        default -> {
                            if ((Boolean) WorldUtils.config.get(
                                    Settings.PERSONALPOSITION.getKey("makeAccessibleGlobally"))) {
                                try {
                                    if (Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                                        //get personalposition from player
                                        try {
                                            positions = new Config(plugin, "positions_" + args[0] + ".yml");
                                            sender.sendMessage("Personal position from player " + args[0] + ": "
                                                    + WorldUtils.positionMessage(args[1], (Location) positions.get(args[1])));
                                        } catch (NullPointerException e) {
                                            WorldUtils.positionNameNotFound(sender);
                                        }
                                        return true;
                                    }
                                } catch (NullPointerException e) {
                                    WorldUtils.playerNotFound(sender);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            WorldUtils.notConsole(sender);
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
        Config positions = new Config(plugin, "positions_" + sender.getName() + ".yml");
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                //command or position name being entered
                StringUtil.copyPartialMatches(args[0], SOLO_COMMANDS, completions);
                StringUtil.copyPartialMatches(args[0], NAME_COMMANDS, completions);
                StringUtil.copyPartialMatches(args[0], positions.getKeys(false), completions);
            }
            case 2 -> {
                //position name being entered
                for (String cmd : NAME_COMMANDS)
                    if (args[0].equals(cmd))
                        StringUtil.copyPartialMatches(args[1], positions.getKeys(false), completions);
            }
        }
        return completions;
    }
}
