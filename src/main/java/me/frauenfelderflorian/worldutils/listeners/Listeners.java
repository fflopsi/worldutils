package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.config.Option;
import me.frauenfelderflorian.worldutils.WorldUtils;
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
public class Listeners implements Listener {
    /**
     * Executed when a player joins: Send a welcome message and add player to timer if configured so
     *
     * @param event the triggered PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Hello " + event.getPlayer().getName() + ", nice to meet you!");
        if ((Boolean) WorldUtils.prefs.get(Option.TIMER_ADD_PLAYER_ON_JOIN))
            WorldUtils.timer.timerBar.addPlayer(event.getPlayer());
    }

    /**
     * Executed when a player quits: If the server is empty afterwards, stop the timer
     *
     * @param event the triggered PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (Bukkit.getServer().getOnlinePlayers().size() == 1)
            WorldUtils.prefs.set(Option.TIMER_RUNNING, false, true);
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
     * Executed when an entity dies: If it is the Ender Dragon, stop the timer
     *
     * @param event the triggered EntityDeathEvent
     */
    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            if ((Boolean) WorldUtils.prefs.get(Option.TIMER_PAUSE_ON_DRAGON_DEATH))
                WorldUtils.prefs.set(Option.TIMER_RUNNING, false, true);
            Objects.requireNonNull(event.getEntity().getKiller())
                    .sendMessage("§bCongratulations, you defeated the Ender Dragon!");
            Bukkit.broadcastMessage("§bCongratulations, you just won the game!");
        }
    }
}
