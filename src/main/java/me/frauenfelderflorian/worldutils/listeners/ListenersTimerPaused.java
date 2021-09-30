package me.frauenfelderflorian.worldutils.listeners;

import me.frauenfelderflorian.worldutils.Settings;
import me.frauenfelderflorian.worldutils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class ListenersTimerPaused implements Listener {
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        //cancelEvent(event); //just for fun
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        //cancelEvent(event); //just for fun
    }

    @EventHandler
    public void onPlayerToggleSprintEvent(PlayerToggleSprintEvent event) {
        //cancelEvent(event); //just for fun
    }

    @EventHandler
    public void onAreaEffectCloudApplyEvent(AreaEffectCloudApplyEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        cancelEvent(event); //PlayerInteractEvent
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        cancelEvent(event); //PlayerInteractEvent
    }

    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event) {
        cancelEvent(event); //PlayerInteractEvent
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        cancelEvent(event); //PlayerInteractEvent
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onEntityAirChangeEvent(EntityAirChangeEvent event) {
        cancelEntityEvent(event); //rapidly firing
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        cancelEntityEvent(event); //not reproducible
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        cancelEntityEvent(event); //player does damage not included
    }

    @EventHandler
    public void onEntityDismountEvent(EntityDismountEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityEnterLoveModeEvent(EntityEnterLoveModeEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityExhaustionEvent(EntityExhaustionEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void EntityInteractEvent(EntityInteractEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void EntityMountEvent(EntityMountEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void EntityPickupItemEvent(EntityPickupItemEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityPlaceEvent(EntityPlaceEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityTameEvent(EntityTameEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onEntityToggleSwimEvent(EntityToggleSwimEvent event) {
        cancelEntityEvent(event);
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) cancelEvent(event);
    }

    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event instanceof InventoryCreativeEvent)) cancelEvent(event);
    }

    @EventHandler
    public void onInventoryDragEvent(InventoryDragEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onItemDespawnEvent(ItemDespawnEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPiglinBarterEvent(PiglinBarterEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerBucketEntityEvent(PlayerBucketEntityEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerEditBookEvent(PlayerEditBookEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerHarvestBlockEvent(PlayerHarvestBlockEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerItemMendEvent(PlayerItemMendEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerShearEntityEvent(PlayerShearEntityEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerTakeLecternBookEvent(PlayerTakeLecternBookEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPlayerUnleashEntityEvent(PlayerUnleashEntityEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPortalCreateEvent(PortalCreateEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        if (event.getHitEntity() instanceof Player) cancelEvent(event);
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onRaidTriggerEvent(RaidTriggerEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onSheepDyeWoolEvent(SheepDyeWoolEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event) {
        if (event.isFromBonemeal()) cancelEvent(event);
    }

    @EventHandler
    public void onTradeSelectEvent(TradeSelectEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onVehicleCreateEvent(VehicleCreateEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onVehicleDamageEvent(VehicleDamageEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onVehicleDestroyEvent(VehicleDestroyEvent event) {
        cancelEvent(event);
    }

    @EventHandler
    public void onVehicleEnterEvent(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) cancelEvent(event);
    }

    @EventHandler
    public void onVehicleExitEvent(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) cancelEvent(event);
    }

    private void cancelEvent(Event event) {
        if (!((Boolean) WorldUtils.prefs.get(Settings.TIMER_RUNNING))
                && event instanceof Cancellable
                && !((Cancellable) event).isCancelled()) {
            ((Cancellable) event).setCancelled(true);
            Bukkit.broadcastMessage("§e" + event.getClass().getName() + "§r was canceled.");
        }
    }

    private void cancelEntityEvent(EntityEvent event) {
        if (event.getEntity() instanceof Player) cancelEvent(event);
    }
}
