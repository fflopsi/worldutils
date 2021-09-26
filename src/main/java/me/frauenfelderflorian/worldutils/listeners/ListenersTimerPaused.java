package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

public class ListenersTimerPaused implements Listener {
    @EventHandler
    public void onAreaEffectCloudApplyEvent(AreaEffectCloudApplyEvent event) {
        cancel(event);
    }

    private void cancel(Event event) {
        if (!((Boolean) WorldUtils.config.get(Settings.TIMER_RUNNING)))
            if (event instanceof Cancellable) ((Cancellable) event).setCancelled(true);
    }

    /*
     * Events to cancel when occurring during paused timer (how to do this?):
     * Every event where the player interacts with the world (blocks, entities, damage etc.)
AreaEffectCloudApplyEvent
BlockBreakEvent
BlockDropItemEvent
BlockFertilizeEvent
BlockIgniteEvent
BlockPlaceEvent
EnchantItemEvent
EntityAirChangeEvent getEntity
EntityChangeBlockEvent getEntity
EntityDamageEvent getEntity
EntityDismountEvent
EntityEnterLoveModeEvent
EntityExhaustionEvent
EntityInteractEvent
EntityMountEvent
EntityPickupItemEvent getEntity
EntityPlaceEvent
EntityPotionEffectEvent getEntity
EntityRegainHealthEvent getEntity
EntityShootBowEvent getEntity
EntityTameEvent getOwner
EntityToggleGlideEvent
EntityToggleSwimEvent
FoodLevelChangeEvent
HangingBreakByEntityEvent getRemover
HangingPlaceEvent
InventoryClickEvent not creative
InventoryDragEvent
InventoryOpenEvent
ItemDespawnEvent
PiglinBarterEvent
PlayerBedEnterEvent
PlayerBedLeaveEvent
PlayerBucketEmptyEvent
PlayerBucketEntityEvent
PlayerBucketFillEvent
PlayerDropItemEvent
PlayerEditBookEvent
PlayerFishEvent
PlayerHarvestBlockEvent
PlayerInteractEntityEvent
PlayerInteractEvent
PlayerItemConsumeEvent
PlayerItemDamageEvent
PlayerItemMendEvent
PlayerLeashEntityEvent
PlayerMoveEvent for fun
PlayerPortalEvent
PlayerShearEntityEvent
PlayerSwapHandItemsEvent
PlayerTakeLecternBookEvent
PlayerToggleFlightEvent elytra???
(PlayerToggleSneakEvent)
(PlayerToggleSprintEvent)
PlayerUnleashEntityEvent
PortalCreateEvent
PrepareItemEnchantEvent
ProjectileHitEvent getHitEntity
ProjectileLaunchEvent
RaidTriggerEvent
SheepDyeWoolEvent
SignChangeEvent
StructureGrowEvent isFromBonemeal
TradeSelectEvent
VehicleCreateEvent
VehicleDamageEvent
VehicleDestroyEvent
VehicleEnterEvent getEntered
VehicleExitEvent getExited
     */
}
