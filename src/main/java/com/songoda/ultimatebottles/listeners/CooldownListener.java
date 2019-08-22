package com.songoda.ultimatebottles.listeners;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.utils.settings.Setting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CooldownListener implements Listener {
    private final UltimateBottles instance;

    public CooldownListener(UltimateBottles instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!Setting.COOLDOWN.getBoolean()) {
            return;
        }

        if (instance.getCooldownMap().getOrDefault(event.getPlayer().getUniqueId(), 0L) <= System.currentTimeMillis()) {
            instance.getCooldownMap().remove(event.getPlayer().getUniqueId());
            return;
        }

        String toCheck = event.getMessage().split(" ")[0].replace("/", "");

        if (!Setting.BLOCKED_COMMANDS.getStringList().contains(toCheck)) {
            return;
        }

        long timeLeft = instance.getCooldownMap().get(event.getPlayer().getUniqueId()) - System.currentTimeMillis();
        instance.getLocale().getMessage("event.cooldown.oncooldown").processPlaceholder("time", instance.getLang().getCooldownMessage(timeLeft));
        event.setCancelled(true);
    }
}
