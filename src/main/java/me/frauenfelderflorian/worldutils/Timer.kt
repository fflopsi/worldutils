package me.frauenfelderflorian.worldutils

import me.frauenfelderflorian.worldutils.config.Option
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

/** Class used for controlling the timer */
class Timer {
    /** The [Player] to whom this [Timer] belongs */
    private val player: Player?

    /** The [BossBar] containing the time and hourly progress */
    private val timerBar: BossBar

    /** The current time in seconds */
    private var time: Int

    /** The plugin which this [Timer] belongs to */
    private val plugin: WorldUtils

    /**
     * Create a new global [Timer]
     *
     * @param plugin the plugin for which the [Timer] should be created
     */
    constructor(plugin: WorldUtils) {
        this.plugin = plugin
        player = null
        time = plugin.prefs.getInt(Option.TIMER_TIME)
        timerBar = Bukkit.createBossBar(
            "§eTimer: ${formatTime(time)}",
            BarColor.YELLOW,
            BarStyle.SEGMENTED_12,
        )
        timerBar.isVisible = true
        update(false)
        val runnable = object : BukkitRunnable() {
            override fun run() {
                //stop timer if time is over
                if (plugin.prefs.getBoolean(Option.TIMER_REVERSE) && time == 0) {
                    sendMessage(plugin, "§cTime is over.")
                    if (plugin.prefs.getBoolean(Option.TIMER_ALLOW_BELOW_ZERO)) {
                        sendMessage(plugin, "§e§oTimer is running with negative time.")
                    } else {
                        setRunning(false)
                        sendMessage(plugin, "§eTimer paused.")
                    }
                }
                //update timer
                if (plugin.prefs.getBoolean(Option.TIMER_RUNNING)) update(true)
            }
        }
        runnable.runTaskTimer(plugin, 20, 20)
    }

    /**
     * Create a new personal [Timer]
     *
     * @param plugin the plugin for which the [Timer] should be created
     * @param player the [Player] to whom this [Timer] belongs
     */
    constructor(plugin: WorldUtils, player: Player) {
        this.plugin = plugin
        this.player = player
        time = plugin.prefs.getInt(player, Option.PTIMER_TIME)
        timerBar = Bukkit.createBossBar(
            "§e${player.displayName}'s timer: ${formatTime(time)}",
            BarColor.YELLOW,
            BarStyle.SEGMENTED_12,
        )
        timerBar.isVisible = plugin.prefs.getBoolean(player, Option.PTIMER_VISIBLE_ON_JOIN)
        update(false)
        timerBar.addPlayer(player)
        val runnable = object : BukkitRunnable() {
            override fun run() {
                //stop timer if time is over
                if (plugin.prefs.getBoolean(player, Option.PTIMER_REVERSE) && time == 0) {
                    sendMessage(plugin, player, "§cPersonal time is over.")
                    if (plugin.prefs.getBoolean(player, Option.PTIMER_ALLOW_BELOW_ZERO)) {
                        sendMessage(
                            plugin, player, "§e§oPersonal timer is running with negative time."
                        )
                    } else {
                        setRunning(false)
                        sendMessage(plugin, player, "§ePersonal timer paused.")
                    }
                }
                //update timer
                if (plugin.prefs.getBoolean(player, Option.PTIMER_RUNNING)) update(true)
            }
        }
        runnable.runTaskTimer(plugin, 20, 20)
    }

    /** Set the [running] state of the [Timer] */
    fun setRunning(running: Boolean) {
        if (player == null) {
            //global timer
            plugin.prefs.set(Option.TIMER_RUNNING, running, true)
            sendMessage(
                plugin,
                "§eTimer ${
                    if (plugin.prefs.getBoolean(Option.TIMER_RUNNING)) "started" else "paused"
                }.",
            )
        } else {
            //personal timer
            plugin.prefs.set(player, Option.PTIMER_RUNNING, running, true)
            sendMessage(
                plugin, player,
                "§ePersonal timer ${
                    if (plugin.prefs.getBoolean(player, Option.PTIMER_RUNNING)) {
                        "started"
                    } else {
                        "paused"
                    }
                }.",
            )
        }
    }

    /** Set the [Timer] to [time] */
    fun setTime(time: Int) {
        this.time = time
        update(false)
    }

    /** Set a personal [Timer]'s visibility status to [visible] */
    fun setVisible(visible: Boolean) {
        checkNotNull(player) { "The global timer cannot be set to invisible." }
        timerBar.isVisible = visible
        sendMessage(
            plugin, player, "§ePersonal timer set to ${if (visible) "visible" else "invisible"}."
        )
    }

    /** Add the [player] to the [Timer], so that they can see the timer [BossBar] */
    fun addPlayer(player: Player) = timerBar.addPlayer(player)

    /** Check if the [player] can see the [Timer] [BossBar] */
    fun containsPlayer(player: Player) = timerBar.players.contains(player)

    /** Remove the [player] from the [Timer], so that they can no longer see the [BossBar] */
    fun removePlayer(player: Player) = timerBar.removePlayer(player)

    /**
     * Update the [Timer] with the new time value
     *
     * @param updateTime if the time itself should be updated accordingly to reverse status
     */
    private fun update(updateTime: Boolean) {
        if (player == null) {
            //global timer
            if (updateTime) {
                if (plugin.prefs.getBoolean(Option.TIMER_REVERSE)) time-- else time++
            }
            timerBar.setTitle("§eTimer: §l${formatTime(time)}")
            timerBar.progress = if (plugin.prefs.getBoolean(Option.TIMER_PROGRESS_MINUTE)) {
                abs(time) % 60 / 60.0
            } else {
                abs(time) % 3600 / 3600.0
            }
            plugin.prefs.set(Option.TIMER_TIME, time, false)
        } else {
            //personal timer
            if (updateTime) {
                if (plugin.prefs.getBoolean(player, Option.PTIMER_REVERSE)) time-- else time++
            }
            timerBar.setTitle("§e${player.displayName}'s timer: §l${formatTime(time)}")
            timerBar.progress =
                if (plugin.prefs.getBoolean(player, Option.PTIMER_PROGRESS_MINUTE)) {
                    abs(time) % 60 / 60.0
                } else {
                    abs(time) % 3600 / 3600.0
                }
            plugin.prefs.set(player, Option.PTIMER_TIME, time, false)
        }
    }

    companion object {
        /** Return the [time] formatted as a [String] */
        fun formatTime(time: Int): String {
            //treat negative time
            val negative = time < 0
            var seconds = abs(time)
            //calculate to days, hours, minutes and seconds
            val d = Math.floorDiv(seconds, 86400)
            seconds %= 86400
            val h = Math.floorDiv(seconds, 3600)
            seconds %= 3600
            val min = Math.floorDiv(seconds, 60)
            seconds %= 60
            val s = seconds
            //format the time
            var formatted = ""
            if (negative) formatted += "\u2212 "
            if (d != 0) {
                formatted += "${d}d"
                if (h != 0) formatted += "  ${h}h"
                if (min != 0) formatted += "  $min'"
                if (s != 0) formatted += "  $s\""
            } else if (h != 0) {
                formatted += "${h}h"
                if (min != 0) formatted += "  $min'"
                if (s != 0) formatted += "  $s\""
            } else if (min != 0) {
                formatted += "$min'"
                if (s != 0) formatted += "  $s\""
            } else {
                formatted += "$s\""
            }
            return formatted
        }
    }
}
