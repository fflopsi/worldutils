package me.frauenfelderflorian.worldutils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class used for controlling the timer
 */
public class Timer {
    public final BossBar timerBar;
    private int time;
    private BukkitRunnable runnable;
    private final JavaPlugin plugin;

    /**
     * Create a new Timer
     *
     * @param plugin the plugin for which the timer should be created
     */
    public Timer(WorldUtils plugin) {
        this.plugin = plugin;
        time = (int) WorldUtils.config.get(Settings.TIMER_TIME);
        timerBar = Bukkit.createBossBar("Timer: " + formatTime(time), BarColor.YELLOW, BarStyle.SEGMENTED_12);
        timerBar.setVisible(false);
        setRunnable();
    }

    /**
     * Set the timer to a new value
     *
     * @param time int of seconds for new timer value
     */
    public void set(int time) {
        this.time = time;
        update(false);
    }

    /**
     * Start or resume the timer
     */
    public void start() {
        runnable.runTaskTimer(plugin, 20, 20);
    }

    /**
     * Stop or pause the timer
     */
    public void stop() {
        runnable.cancel();
        setRunnable();
    }

    /**
     * Format the input time
     *
     * @param time int input time in seconds
     * @return String with formatted time in format <[[[~d]  ~~h] ~~'] ~~">
     */
    public static String formatTime(int time) {
        int d, h, min, s;
        d = Math.floorDiv(time, 86400);
        time %= 86400;
        h = Math.floorDiv(time, 3600);
        time %= 3600;
        min = Math.floorDiv(time, 60);
        time %= 60;
        s = time;
        return d == 0 ? h == 0 ? min == 0 ?
                s + "\"" :
                min + "'  " + s + "\"" :
                h + "h  " + min + "'  " + s + "\"" :
                d + "d  " + h + "h  " + min + "'  " + s + "\"";
    }

    /**
     * Update the timer with the new time value
     *
     * @param updateTime boolean if the time itself should be updated accordingly to reverse status
     */
    private void update(boolean updateTime) {
        if (updateTime) {
            if ((Boolean) WorldUtils.config.get(Settings.TIMER_REVERSE)) time--;
            else time++;
        }
        timerBar.setTitle("§eTimer: " + formatTime(time));
        timerBar.setProgress((time % 60) / 60.0);
        WorldUtils.config.set(Settings.TIMER_TIME, time, false);
    }

    /**
     * Set the runnable for the timer to run
     */
    private void setRunnable() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                update(true);
            }
        };
    }
}
