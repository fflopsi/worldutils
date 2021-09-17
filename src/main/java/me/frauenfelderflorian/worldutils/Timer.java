package me.frauenfelderflorian.worldutils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    public int time;
    public final BossBar timerBar;
    private BukkitRunnable runnable;
    private final JavaPlugin plugin;

    public Timer(WorldUtils plugin) {
        this.plugin = plugin;
        time = (int) WorldUtils.config.get(Settings.TIMER_TIME);
        timerBar = Bukkit.createBossBar("Timer: " + formatTime(time), BarColor.YELLOW, BarStyle.SEGMENTED_12);
        timerBar.setVisible(true);
        setRunnable();
    }

    public void set(int time) {
        this.time = time;
        update(false);
    }

    public void start() {
        runnable.runTaskTimer(plugin, 20, 20);
    }

    public void stop() {
        runnable.cancel();
        setRunnable();
    }

    private void update(boolean updateTime) {
        if (updateTime) {
            if ((Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE)) time--;
            else time++;
        }
        timerBar.setTitle("§eTimer: " + formatTime(time));
        timerBar.setProgress((time % 60) / 60.0);
        WorldUtils.config.set(Settings.TIMER_TIME, time);
    }

    private void setRunnable() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                update(true);
            }
        };
    }

    public static String formatTime(int time) {
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
