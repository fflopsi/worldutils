package me.frauenfelderflorian.worldutils.executors;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public record ResetExecutor(WorldUtils plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equals("confirm")) {
            //another possibility: onLoad deletion of worlds: settings
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
}
