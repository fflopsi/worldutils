package me.frauenfelderflorian.worldutils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener for PlayerDeathEvents
 */
public class Listeners implements Listener {
    /**
     * Executed when a player dies
     *
     * @param event the PlayerDeathEvent that is sent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().sendMessage("You §4died§r at " + WorldUtils.Messages.positionMessage(event.getEntity().getLocation()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("Hello " + event.getPlayer().getName() + ", nice to meet you!");
        if ((Boolean) WorldUtils.config.get(Settings.TIMER_ADD_PLAYER_ON_JOIN))
            WorldUtils.timer.timerBar.addPlayer(event.getPlayer());
    }
}
