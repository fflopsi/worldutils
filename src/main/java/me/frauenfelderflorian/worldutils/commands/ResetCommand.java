package me.frauenfelderflorian.worldutils.commands;

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

public record ResetCommand(WorldUtils plugin) implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equals("confirm")) {
            //another possibility: onLoad or onDisable deletion of worlds: settings?
            String[] worlds = new String[]{"world", "world_nether", "world_the_end"};
            for (String world : worlds) {
                Bukkit.getServer().unloadWorld(world, true);
                deleteFolder(world);
            }
            //set seed?
            //Bukkit.spigot().restart();
            return true;
        }
        return false;
    }

    private void deleteFolder(String folderName) {
        Path folder = Paths.get(folderName);
        try {
            Files.walk(folder).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) StringUtil.copyPartialMatches(args[0], List.of("confirm"), completions);
        return completions;
    }
}
