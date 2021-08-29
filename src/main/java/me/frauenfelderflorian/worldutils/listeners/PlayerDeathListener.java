package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.getEntity().sendMessage("You died at " + WorldUtils.positionMessage(event.getEntity().getLocation()));
    }
}
