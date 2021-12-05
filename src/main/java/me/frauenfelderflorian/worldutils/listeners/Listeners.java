package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

/**
 * Listener class for triggered Events
 */
public record Listeners(WorldUtils plugin) implements Listener {
    /**
     * Executed when a player joins: Send a welcome message, add player to timer and start timer if configured so
     *
     * @param event the triggered PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //greeting message
        event.getPlayer().sendMessage("Hello " + event.getPlayer().getName() + ", nice to meet you!");
        //set default personal settings
        for (Prefs.Option setting : Prefs.Option.values())
            if (!setting.isGlobal() && !plugin.prefs.contains(event.getPlayer(), setting) && setting.isVanilla())
                plugin.prefs.set(event.getPlayer(), setting, setting.getDefault(), true);
        //global timer setup
        if (plugin.prefs.getBoolean(Prefs.Option.TIMER_ADD_PLAYER_ON_JOIN))
            plugin.timer.addPlayer(event.getPlayer());
        if (plugin.prefs.getBoolean(Prefs.Option.TIMER_START_IF_WAS_RUNNING)
                && plugin.prefs.getBoolean(Prefs.Option.TIMER_WAS_RUNNING)
                && Bukkit.getServer().getOnlinePlayers().size() == 1) plugin.timer.setRunning(true);
        //personal timer setup
        plugin.addTimer(event.getPlayer());
        if (plugin.prefs.getBoolean(event.getPlayer(), Prefs.Option.PTIMER_START_IF_WAS_RUNNING)
                && plugin.prefs.getBoolean(event.getPlayer(), Prefs.Option.PTIMER_WAS_RUNNING))
            plugin.getTimer(event.getPlayer()).setRunning(true);
    }

    /**
     * Executed when a player quits: If the server is empty afterwards, stop the timer
     *
     * @param event the triggered PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        //global timer processing
        if (Bukkit.getServer().getOnlinePlayers().size() == 1) {
            plugin.prefs.set(Prefs.Option.TIMER_WAS_RUNNING, plugin.prefs.getBoolean(Prefs.Option.TIMER_RUNNING), true);
            plugin.timer.setRunning(false);
        }
        //personal timer processing
        plugin.prefs.set(event.getPlayer(), Prefs.Option.PTIMER_WAS_RUNNING,
                plugin.prefs.getBoolean(event.getPlayer(), Prefs.Option.PTIMER_RUNNING), true);
        plugin.getTimer(event.getPlayer()).setRunning(false);
        plugin.removeTimer(event.getPlayer());
    }

    /**
     * Executed when a player dies: Send a death message with the death coordinates
     *
     * @param event the triggered PlayerDeathEvent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().sendMessage("You §4died§r at "
                + WorldUtils.Messages.positionMessage(event.getEntity().getLocation()));
    }

    /**
     * Executed when an entity dies: If it is the Ender Dragon, stop the global timer and personal timer of the killer
     *
     * @param event the triggered EntityDeathEvent
     */
    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            if (plugin.prefs.getBoolean(Prefs.Option.TIMER_PAUSE_ON_DRAGON_DEATH))
                plugin.timer.setRunning(false);
            if (plugin.prefs.getBoolean(event.getEntity().getKiller(), Prefs.Option.PTIMER_PAUSE_ON_DRAGON_DEATH))
                plugin.getTimer(event.getEntity().getKiller()).setRunning(false);
            Objects.requireNonNull(event.getEntity().getKiller())
                    .sendMessage("§bCongratulations, you defeated the Ender Dragon!");
            Bukkit.broadcastMessage("§bCongratulations, you just won the game!");
        }
    }
}
