package com.songoda.ultimatebottles;

import co.aikar.commands.BukkitCommandManager;
import com.songoda.ultimatebottles.command.CommandManager;
import com.songoda.ultimatebottles.listeners.BottleListener;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.songoda.ultimatebottles.Lang.color;
import static org.bukkit.Bukkit.getConsoleSender;

public final class UltimateBottles extends JavaPlugin {
    private Map<UUID, Long> cooldownMap;
    private Lang lang;
    private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        cooldownMap = new HashMap<>();
        lang = new Lang(this);
        commandManager = new CommandManager(this);

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new BottleListener(this), this);

        getConsoleSender().sendMessage(color("&a============================="));
    }

    @Override
    public void onDisable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &cDisabling&7..."));
        getConsoleSender().sendMessage(color("&a============================="));
    }

    public Map<UUID, Long> getCooldownMap() {
        return cooldownMap;
    }

    public ItemStack createBottle(String creator, long amount) {
        ConfigurationSection section = getConfig().getConfigurationSection("item");
        Material material = Material.matchMaterial(section.getString("material"));

        if (material == null) {
            throw new IllegalStateException("Invalid material: " + section.getString("material"));
        }

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("display-name")
                .replace("{amount}", "" + amount)
                .replace("{who}", creator)));

        meta.setLore(section.getStringList("lore").stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s
                        .replace("{amount}", "" + amount)
                        .replace("{who}", creator))).collect(Collectors.toList()));

        itemStack.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger("xp-bottle", (int) amount);
        return nbtItem.getItem();
    }

    public Lang getLang() {
        return lang;
    }
}
