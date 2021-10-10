package me.frauenfelderflorian.worldutils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Class used for controlling the timer
 */
public class Timer {
    public final BossBar timerBar;
    private int time;

    /**
     * Create a new Timer
     *
     * @param plugin the plugin for which the timer should be created
     */
    public Timer(WorldUtils plugin) {
        time = (int) WorldUtils.prefs.get(Options.TIMER_TIME);
        timerBar = Bukkit.createBossBar("§eTimer: " + formatTime(time), BarColor.YELLOW, BarStyle.SEGMENTED_12);
        timerBar.setVisible(true);
        update(false);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                //stop timer if time is over
                if ((Boolean) WorldUtils.prefs.get(Options.TIMER_REVERSE) && time == 0) {
                    WorldUtils.prefs.set(Options.TIMER_RUNNING, false, true);
                    Bukkit.broadcastMessage("§cTime is over. §eTimer paused.");
                }
                //update timer
                if ((Boolean) WorldUtils.prefs.get(Options.TIMER_RUNNING)) update(true);
            }
        };
        runnable.runTaskTimer(plugin, 20, 20);
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
            if ((Boolean) WorldUtils.prefs.get(Options.TIMER_REVERSE)) time--;
            else time++;
        }
        timerBar.setTitle("§eTimer: " + formatTime(time));
        if ((Boolean) WorldUtils.prefs.get(Options.TIMER_PROGRESS_MINUTE)) timerBar.setProgress((time % 60) / 60.0);
        else timerBar.setProgress((time % 3600) / 3600.0);
        WorldUtils.prefs.set(Options.TIMER_TIME, time, false);
    }
}
