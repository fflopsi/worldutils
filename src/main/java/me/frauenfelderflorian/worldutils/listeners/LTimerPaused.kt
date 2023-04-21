package me.frauenfelderflorian.worldutils.listeners

import me.frauenfelderflorian.worldutils.WorldUtils
import me.frauenfelderflorian.worldutils.config.Option
import me.frauenfelderflorian.worldutils.sendMessage
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.*
import org.bukkit.event.raid.RaidTriggerEvent
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleExitEvent

/**
 * [Listener] class for triggered events / interactions of the player and the world
 *
 * If the timer is paused and the respective settings are set to true, all these events are canceled
 */
data class LTimerPaused(val plugin: WorldUtils) : Listener {
    @EventHandler
    fun onPlayerMoveEvent(event: PlayerMoveEvent) {
        if (plugin.prefs.getBoolean(Option.TIMER_DISABLE_MOVEMENT_ON_PAUSE)) {
            cancelEvent(event, false)
        }
    }

    @EventHandler
    fun onAreaEffectCloudApplyEvent(event: AreaEffectCloudApplyEvent) = cancelEvent(event, false)

    @EventHandler
    fun onEnchantItemEvent(event: EnchantItemEvent) = cancelEvent(event, true)

    @EventHandler
    fun onEntityAirChangeEvent(event: EntityAirChangeEvent) = cancelEntityEvent(event, false)

    @EventHandler
    fun onEntityDamageByEntityEvent(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) cancelEvent(event, true)
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageEvent) = cancelEntityEvent(event, true)

    @EventHandler
    fun onEntityExhaustionEvent(event: EntityExhaustionEvent) = cancelEntityEvent(event, false)

    @EventHandler
    fun onEntityPickupItemEvent(event: EntityPickupItemEvent) = cancelEntityEvent(event, false)

    @EventHandler
    fun onEntityPotionEffectEvent(event: EntityPotionEffectEvent) = cancelEntityEvent(event, false)

    @EventHandler
    fun onEntityRegainHealthEvent(event: EntityRegainHealthEvent) = cancelEntityEvent(event, false)

    @EventHandler
    fun onEntityShootBowEvent(event: EntityShootBowEvent) = cancelEntityEvent(event, true)

    @EventHandler
    fun onEntityTameEvent(event: EntityTameEvent) {
        if (event.owner is Player) cancelEvent(event, true)
    }

    @EventHandler
    fun onEntityToggleGlideEvent(event: EntityToggleGlideEvent) = cancelEntityEvent(event, true)

    @EventHandler
    fun onHangingBreakByEntityEvent(event: HangingBreakByEntityEvent) {
        if (event.remover is Player) cancelEvent(event, true)
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (event !is InventoryCreativeEvent) cancelEvent(event, true)
    }

    @EventHandler
    fun onInventoryDragEvent(event: InventoryDragEvent) = cancelEvent(event, true)

    @EventHandler
    fun onItemDespawnEvent(event: ItemDespawnEvent) = cancelEvent(event, false)

    @EventHandler
    fun onPlayerBucketEmptyEvent(event: PlayerBucketEmptyEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerBucketFillEvent(event: PlayerBucketFillEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerEditBookEvent(event: PlayerEditBookEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerFishEvent(event: PlayerFishEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerItemConsumeEvent(event: PlayerItemConsumeEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerItemDamageEvent(event: PlayerItemDamageEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerItemMendEvent(event: PlayerItemMendEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerPortalEvent(event: PlayerPortalEvent) = cancelEvent(event, false)

    @EventHandler
    fun onPlayerSwapHandItemsEvent(event: PlayerSwapHandItemsEvent) = cancelEvent(event, true)

    @EventHandler
    fun onPlayerTakeLecternBookEvent(event: PlayerTakeLecternBookEvent) = cancelEvent(event, true)

    @EventHandler
    fun onProjectileHitEvent(event: ProjectileHitEvent) {
        if (event.hitEntity is Player) cancelEvent(event, true)
    }

    @EventHandler
    fun onProjectileLaunchEvent(event: ProjectileLaunchEvent) {
        if (event.entity.shooter is Player) cancelEvent(event, true)
    }

    @EventHandler
    fun onRaidTriggerEvent(event: RaidTriggerEvent) = cancelEvent(event, false)

    @EventHandler
    fun onVehicleDamageEvent(event: VehicleDamageEvent) = cancelEvent(event, true)

    @EventHandler
    fun onVehicleExitEvent(event: VehicleExitEvent) {
        if (event.exited is Player) cancelEvent(event, false)
    }

    /**
     * Cancel the [event] if timer is paused, respective setting is set to true and event can be
     * and is not yet canceled
     *
     * @param message true if this message should be broadcast: Timer is paused. Interaction not possible.
     */
    private fun cancelEvent(event: Event, message: Boolean) {
        if (plugin.prefs.getBoolean(Option.TIMER_DISABLE_ACTIONS_ON_PAUSE) && !plugin.prefs.getBoolean(
                Option.TIMER_RUNNING
            ) && event is Cancellable && !(event as Cancellable).isCancelled
        ) {
            (event as Cancellable).isCancelled = true
            if (message) sendMessage(plugin, "§cTimer is paused. §eInteraction not possible.")
        }
    }

    /**
     * Cancel given entity [event] if involved entity is a player, timer is paused, respective
     * setting is set to true and event is not yet canceled
     *
     * @param message true if this message should be broadcast: Timer is paused. Interaction not possible.
     */
    private fun cancelEntityEvent(event: EntityEvent, message: Boolean) {
        if (event.entity is Player) cancelEvent(event, message)
    }
}
