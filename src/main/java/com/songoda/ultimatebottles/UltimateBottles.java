package com.songoda.ultimatebottles;

import com.songoda.ultimatebottles.command.CommandManager;
import com.songoda.ultimatebottles.listeners.BottleListener;
import com.songoda.ultimatebottles.utils.Metrics;
import com.songoda.ultimatebottles.utils.itemnbtapi.NBTItem;
import com.songoda.ultimatebottles.utils.locale.Locale;
import com.songoda.ultimatebottles.utils.updateModules.LocaleModule;
import com.songoda.update.Plugin;
import com.songoda.update.SongodaUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.songoda.ultimatebottles.Lang.color;
import static org.bukkit.Bukkit.getConsoleSender;

public final class UltimateBottles extends JavaPlugin {

    private static UltimateBottles instance;
    private Map<UUID, Long> cooldownMap;
    private Lang lang;
    private CommandManager commandManager;
    private Locale locale;

    public static UltimateBottles getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getConsoleSender().sendMessage(color("&a============================="));
        getConsoleSender().sendMessage(color("&7" + getDescription().getName() + " " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        getConsoleSender().sendMessage(color("&7Action: &aEnabling&7..."));

        instance = this;

        cooldownMap = new HashMap<>();
        lang = new Lang(this);
        this.commandManager = new CommandManager(this);

        saveDefaultConfig();

        new Locale(this, "en_US");
        this.locale = Locale.getLocale(getConfig().getString("language-mode"));

        //Running Songoda Updater
        Plugin plugin = new Plugin(this, 68);
        plugin.addModule(new LocaleModule());
        SongodaUpdate.load(plugin);

        Bukkit.getPluginManager().registerEvents(new BottleListener(this), this);

        // Starting Metrics
        new Metrics(this);

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

        meta.setDisplayName(locale.getMessage("general.bottle.name").getMessage());

        List<String> lore = new ArrayList<>();
        String[] loreSplit = locale.getMessage("general.bottle.lore")
                .processPlaceholder("amount", amount)
                .processPlaceholder("who", creator).getMessage().split("\\|");
        for (String line : loreSplit) lore.add(line);
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger("xp-bottle", (int) amount);
        return nbtItem.getItem();
    }

    public Lang getLang() {
        return lang;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public Locale getLocale() {
        return locale;
    }
}
