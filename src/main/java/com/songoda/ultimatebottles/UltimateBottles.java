package com.songoda.ultimatebottles;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.songoda.core.commands.CommandManager;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.configuration.Config;
import com.songoda.core.gui.GuiManager;
import com.songoda.ultimatebottles.commands.CommandBottle;
import com.songoda.ultimatebottles.commands.CommandCheck;
import com.songoda.ultimatebottles.commands.CommandGive;
import com.songoda.ultimatebottles.commands.CommandReload;
import com.songoda.ultimatebottles.commands.CommandSettings;
import com.songoda.ultimatebottles.commands.CommandUltimateBottles;
import com.songoda.ultimatebottles.listeners.BottleListener;
import com.songoda.ultimatebottles.settings.Settings;
import com.songoda.ultimatebottles.utils.itemnbtapi.NBTItem;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UltimateBottles extends SongodaPlugin {

    private static UltimateBottles instance;
    private Map<UUID, Long> cooldownMap;
    private ServerVersion serverVersion = ServerVersion.getServerVersion();
    private CommandManager commandManager;
    private GuiManager guiManager = new GuiManager(this);

    public static UltimateBottles getInstance() {
        return instance;
    }

    @Override
    public void onPluginLoad() {
        instance = this;
    }

    @Override
    public void onPluginEnable() {
        // Running Songoda Updater
        SongodaCore.registerPlugin(this, 68, CompatibleMaterial.EXPERIENCE_BOTTLE);

        Settings.setupConfig();
        this.setLocale(Settings.LANGUAGE_MODE.getString(), false);

        this.commandManager = new CommandManager(this);
        this.commandManager.addCommand(new CommandUltimateBottles(this))
                .addSubCommands(
                        new CommandCheck(this),
                        new CommandGive(this),
                        new CommandReload(this),
                        new CommandSettings(guiManager),
                        new CommandBottle(this)
                );

        guiManager.init();

        cooldownMap = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new BottleListener(this), this);
    }

    @Override
    public void onPluginDisable() {
    }

    @Override
    public void onConfigReload() {
        this.setLocale(Settings.LANGUAGE_MODE.getString(), true);
    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    public Map<UUID, Long> getCooldownMap() {
        return cooldownMap;
    }

    public ItemStack createBottle(String creator, long amount) {
        Material material = Settings.BOTTLE_ITEM.getMaterial().getMaterial();

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

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }

    public boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
