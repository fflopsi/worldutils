package me.frauenfelderflorian.worldutils.listeners

import me.frauenfelderflorian.worldutils.WorldUtils
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.positionMessage
import me.frauenfelderflorian.worldutils.sendMessage
import org.bukkit.Bukkit
import org.bukkit.entity.EnderDragon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/** [Listener] class for triggered Events */
data class Listeners(val plugin: WorldUtils) : Listener {
    /** Executed when a player joins: Send a welcome message, add player to timer and start timer if configured so */
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        //greeting message
        sendMessage(plugin, event.player, "Hello ${event.player.name}, nice to meet you!")
        //set default personal settings
        Option.values()
            .filter { !it.isGlobal && !plugin.prefs.contains(event.player, it) && it.isVanilla }
            .forEach { plugin.prefs.set(event.player, it, it.default, true) }
        //global timer setup
        if (plugin.prefs.getBoolean(Option.TIMER_ADD_PLAYER_ON_JOIN)) {
            plugin.timer.addPlayer(event.player)
        }
        if (plugin.prefs.getBoolean(Option.TIMER_START_IF_WAS_RUNNING) && plugin.prefs.getBoolean(
                Option.TIMER_WAS_RUNNING
            ) && Bukkit.getServer().onlinePlayers.size == 1
        ) {
            plugin.timer.setRunning(true)
        }
        //personal timer setup
        plugin.addTimer(event.player)
        if (plugin.prefs.getBoolean(
                event.player, Option.PTIMER_START_IF_WAS_RUNNING
            ) && plugin.prefs.getBoolean(event.player, Option.PTIMER_WAS_RUNNING)
        ) {
            plugin.getTimer(event.player)!!.setRunning(true)
        }
    }

    /** Executed when a player quits: If the server is empty afterward, stop the timer */
    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        //global timer processing
        if (Bukkit.getServer().onlinePlayers.size == 1) {
            plugin.prefs.set(
                Option.TIMER_WAS_RUNNING,
                plugin.prefs.getBoolean(Option.TIMER_RUNNING),
                true,
            )
            plugin.timer.setRunning(false)
        }
        //personal timer processing
        plugin.prefs.set(
            event.player, Option.PTIMER_WAS_RUNNING,
            plugin.prefs.getBoolean(event.player, Option.PTIMER_RUNNING),
            true,
        )
        plugin.getTimer(event.player)!!.setRunning(false)
        plugin.removeTimer(event.player)
    }

    /** Executed when a player dies: Send a death message with the death coordinates */
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) = sendMessage(
        plugin, event.entity, "You §4died§r at ${positionMessage(event.entity.location)}"
    )

    /** Executed when an entity dies: If it is the Ender Dragon, stop the global timer and personal timer of the killer */
    @EventHandler
    fun onDragonDeath(event: EntityDeathEvent) {
        if (event.entity is EnderDragon) {
            if (plugin.prefs.getBoolean(Option.TIMER_PAUSE_ON_DRAGON_DEATH)) {
                plugin.timer.setRunning(false)
            }
            if (plugin.prefs.getBoolean(
                    event.entity.killer!!, Option.PTIMER_PAUSE_ON_DRAGON_DEATH
                )
            ) {
                plugin.getTimer(event.entity.killer!!)!!.setRunning(false)
            }
            sendMessage(
                plugin, event.entity.killer!!, "§bCongratulations, you defeated the Ender Dragon!"
            )
            sendMessage(plugin, "§bCongratulations, you just won the game!")
        }
    }
}
