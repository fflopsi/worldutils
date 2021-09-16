package me.frauenfelderflorian.worldutils.commands;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class TimerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                switch (args[0]) {
                    case "start" -> WorldUtils.timer.run();
                    case "stop" -> WorldUtils.timer.cancel();
                    case "reverse" -> WorldUtils.config.set(Settings.TIMER_REVERSE, true);
                    case "reset" -> WorldUtils.timer.time = 0;
                }
            }
            case 2 -> {
                if (args[0].equals("set")) WorldUtils.timer.time = Integer.parseInt(args[1]);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
