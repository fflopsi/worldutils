package me.frauenfelderflorian.worldutils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    public boolean running;
    public int time;
    private final BossBar timerBar;
    private final JavaPlugin plugin;
    private final BukkitRunnable runnable;

    public Timer(WorldUtils plugin) {
        this.plugin = plugin;
        running = false;
        time = (int) WorldUtils.config.get(Settings.TIMER_TIME);
        timerBar = Bukkit.createBossBar("Timer: " + formatTime(time), BarColor.YELLOW, BarStyle.SEGMENTED_20);
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        };

    }

    public void run() {
        runnable.runTaskTimer(plugin, 20, 20);
    }

    public void cancel() {
        runnable.cancel();
    }

    public void update() {
        if ((Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE)) time--;
        else time++;
        timerBar.setTitle("Timer: " + formatTime(time));
        timerBar.setProgress(time % 60);
    }

    private String formatTime(int time) {
        int d, h, min, s;
        d = Math.floorDiv(time, 86400);
        time %= 86400;
        h = Math.floorDiv(time, 3600);
        time %= 3600;
        min = Math.floorDiv(time, 60);
        time %= 60;
        s = time;
        return d == 0 ?
                h == 0 ?
                        min == 0 ?
                                s + "\"" :
                                min + "'  " + s + "\"" :
                        h + "h  " + min + "'  " + s + "\"" :
                d + "d  " + h + "h  " + min + "'  " + s + "\"";
    }
}
