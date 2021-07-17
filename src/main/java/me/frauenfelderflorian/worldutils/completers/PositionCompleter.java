package me.frauenfelderflorian.worldutils.completers;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <code>org.bukkit.command.TabCompleter</code> for command "position"
 */
public record PositionCompleter(WorldUtils plugin) implements TabCompleter {
    private static final List<String> SOLO_COMMANDS = new ArrayList<>(List.of("list", "clear"));
    private static final List<String> NAME_COMMANDS = new ArrayList<>(List.of("tp", "del"));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            //command or position name being entered
            StringUtil.copyPartialMatches(args[0], SOLO_COMMANDS, completions);
            StringUtil.copyPartialMatches(args[0], NAME_COMMANDS, completions);
            StringUtil.copyPartialMatches(args[0], plugin.positions.getKeys(false), completions);
        } else if (args.length == 2)
            //position name being entered
            for (String cmd : NAME_COMMANDS)
                if (args[0].equals(cmd))
                    StringUtil.copyPartialMatches(args[1], plugin.positions.getKeys(false), completions);
        return completions;
    }
}
