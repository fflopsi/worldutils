package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listener for PlayerDeathEvents
 */
public class WorldUtilsListener implements Listener {
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
        WorldUtils.timer.timerBar.addPlayer(event.getPlayer());
        event.getPlayer().sendMessage("Hello " + event.getPlayer().getName() + ", nice to meet you!");
    }
}
