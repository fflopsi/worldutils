package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * CommandExecutor and TabCompleter for command reset
 */
public record ResetCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
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
        if (!(Boolean) plugin.config.get(Settings.RESET.getKey(0))
                || args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            //another possibility: onLoad or onDisable deletion of worlds: settings?
            String[] worlds = new String[]{"world", "world_nether", "world_the_end"};
            for (String world : worlds) {
                Bukkit.getServer().unloadWorld(world, true);
                Path folder = Paths.get(world);
                try {
                    Files.walk(folder).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //set seed?
            if ((Boolean) plugin.config.get(Settings.RESET.getKey(1))) {
                Bukkit.spigot().restart();
            }
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
        List<String> completions = new ArrayList<>();
        if ((Boolean) plugin.config.get(Settings.RESET.getKey(0)) && args.length == 1)
            StringUtil.copyPartialMatches(args[0], List.of("confirm"), completions);
        return completions;
    }
}
