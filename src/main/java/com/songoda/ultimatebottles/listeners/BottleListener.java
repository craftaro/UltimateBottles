package com.songoda.ultimatebottles.listeners;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.utils.Experience;
import com.songoda.ultimatebottles.utils.itemnbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BottleListener implements Listener {
    private final UltimateBottles instance;

    public BottleListener(UltimateBottles instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
            return;
        }

        ItemStack itemStack = event.getItem();
        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey("xp-bottle")) {
            return;
        }

        event.setCancelled(true);

        long amount = nbtItem.getLong("xp-bottle");

        if (Experience.getTotalExperience(event.getPlayer()) + amount > Integer.MAX_VALUE) {
            instance.getLocale().getMessage("event.throw.max").sendPrefixedMessage(event.getPlayer());
            return;
        }

        Experience.setTotalExperience(event.getPlayer(), (int) (Experience.getTotalExperience(event.getPlayer()) + amount));
        instance.getLocale().getMessage("event.throw.used").processPlaceholder("amount", amount).sendMessage(event.getPlayer());

        int slot = event.getPlayer().getInventory().getHeldItemSlot();

        try {
            if (event.getHand() == EquipmentSlot.OFF_HAND) {
                slot = 40;
            }
        } catch (NoSuchMethodError ignore) {
        }

        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
            event.getPlayer().getInventory().setItem(slot, itemStack);
            return;
        }

        event.getPlayer().getInventory().clear(slot);
    }


//    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
//    public void onBlockDispense(BlockDispenseEvent event) {
//        if (event.getItem() == null || event.getItem().getType() == Material.AIR) {
//            return;
//        }
//
//        NBTItem nbtItem = new NBTItem(event.getItem());
//
//        if (!nbtItem.hasKey("xp-bottle")) {
//            return;
//        }
//
//        int expAmount = nbtItem.getInteger("xp-bottle");
//
//        event.setCancelled(true);
//
//        ThrownExpBottle bottle = (ThrownExpBottle) event.getBlock().getWorld()
//                .spawnEntity(event.getBlock().getRelative(((org.bukkit.material.Dispenser) event.getBlock().getState().getData()).getFacing())
//                        .getLocation().add(0.5, 0.5, 0.5), EntityType.THROWN_EXP_BOTTLE);
//
//        bottle.setVelocity(event.getVelocity());
//        bottle.setMetadata("bottle-amount", new FixedMetadataValue(instance, expAmount));
//
//        Dispenser dispenser = (Dispenser) event.getBlock().getState();
//
//        event.setItem(null);
//
//        for (int i = 0; i < dispenser.getInventory().getSize(); i++) {
//            ItemStack itemStack = dispenser.getInventory().getItem(i);
//            if(itemStack == null) {
//                continue;
//            }
//
//            NBTItem nbt  = new NBTItem(itemStack);
//
//            if(!nbt.hasKey("xp-bottle") || nbt.getInteger("xp-bottle") != expAmount) {
//                continue;
//            }
//
//            dispenser.getInventory().clear(i);
//            System.out.println("Clearing slot");
//            return;
//        }
//    }
//
//    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
//    public void onExpBottle(ExpBottleEvent event) {
//        if (!event.getEntity().hasMetadata("bottle-amount")) {
//            return;
//        }
//
//        event.setExperience(event.getEntity().getMetadata("bottle-amount").get(0).asInt());
//    }
}
