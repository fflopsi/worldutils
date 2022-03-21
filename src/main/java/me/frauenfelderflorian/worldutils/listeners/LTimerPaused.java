package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.Messages;
import me.frauenfelderflorian.worldutils.WorldUtils;
import me.frauenfelderflorian.worldutils.config.Prefs;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 * Listener class for triggered events / interactions of the player and the world
 * <p>
 * If the timer is paused and the respective settings are set to true, all these events are canceled
 */
public record LTimerPaused(WorldUtils plugin) implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (plugin.prefs.getBoolean(Prefs.Option.TIMER_DISABLE_MOVEMENT_ON_PAUSE)) cancelEvent(event, false);
    }

    @EventHandler
    public void onAreaEffectCloudApplyEvent(AreaEffectCloudApplyEvent event) {
        cancelEvent(event, false);
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onEntityAirChangeEvent(EntityAirChangeEvent event) {
        cancelEntityEvent(event, false);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) cancelEvent(event, true);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        cancelEntityEvent(event, true);
    }

    @EventHandler
    public void onEntityExhaustionEvent(EntityExhaustionEvent event) {
        cancelEntityEvent(event, false);
    }

    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        cancelEntityEvent(event, false);
    }

    @EventHandler
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        cancelEntityEvent(event, false);
    }

    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {
        cancelEntityEvent(event, false);
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        cancelEntityEvent(event, true);
    }

    @EventHandler
    public void onEntityTameEvent(EntityTameEvent event) {
        if (event.getOwner() instanceof Player) cancelEvent(event, true);
    }

    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        cancelEntityEvent(event, true);
    }

    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) cancelEvent(event, true);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event instanceof InventoryCreativeEvent)) cancelEvent(event, true);
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent event) {
        cancelEvent(event, false);
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        cancelEvent(event, true); //when block not framed, but within 5 blocks?
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerItemMendEvent(PlayerItemMendEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        cancelEvent(event, false);
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onPlayerTakeLecternBookEvent(PlayerTakeLecternBookEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof Player) cancelEvent(event, true);
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) cancelEvent(event, true);
    }

    @EventHandler
    public void onRaidTriggerEvent(RaidTriggerEvent event) {
        cancelEvent(event, false);
    }

    @EventHandler
    public void onVehicleDamageEvent(VehicleDamageEvent event) {
        cancelEvent(event, true);
    }

    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) cancelEvent(event, false);
    }

    /**
     * Cancel given event if timer is paused, respective setting is set to true and event can be and is not yet canceled
     *
     * @param event   the Event to be canceled
     * @param message true if this message should be broadcast: "<span style="color:red">Timer is paused.</span>
     *                <span style="color:yellow">Interaction not possible.</span>"
     */
    private void cancelEvent(Event event, boolean message) {
        if (plugin.prefs.getBoolean(Prefs.Option.TIMER_DISABLE_ACTIONS_ON_PAUSE)
                && !plugin.prefs.getBoolean(Prefs.Option.TIMER_RUNNING) && event instanceof Cancellable
                && !((Cancellable) event).isCancelled()) {
            ((Cancellable) event).setCancelled(true);
            if (message) Messages.sendMessage(plugin, "§cTimer is paused. §eInteraction not possible.");
        }
    }

    /**
     * Cancel given entity event if involved entity is a player, timer is paused,
     * respective setting is set to true and event is not yet canceled
     *
     * @param event   the EntityEvent to be canceled
     * @param message true if this message should be broadcast: "<span style="color:red">Timer is paused.</span>
     *                <span style="color:yellow">Interaction not possible.</span>"
     */
    private void cancelEntityEvent(EntityEvent event, boolean message) {
        if (event.getEntity() instanceof Player) cancelEvent(event, message);
    }
}
