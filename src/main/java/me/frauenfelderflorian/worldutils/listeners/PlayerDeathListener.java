package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Listener for PlayerDeathEvents
 */
public class PlayerDeathListener implements Listener {
    /**
     * Executed when a player dies
     *
     * @param event the PlayerDeathEvent that is sent
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().sendMessage("You §4died§r at " + WorldUtils.positionMessage(event.getEntity().getLocation()));
    }
}
