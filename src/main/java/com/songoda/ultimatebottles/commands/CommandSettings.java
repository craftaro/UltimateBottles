package com.songoda.ultimatebottles.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.configuration.editor.PluginConfigGui;
import com.songoda.core.gui.GuiManager;
import com.songoda.ultimatebottles.UltimateBottles;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandSettings extends AbstractCommand {

    private final GuiManager guiManager;

    public CommandSettings(GuiManager guiManager) {
        super(true, "settings");
        this.guiManager = guiManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... strings) {
        guiManager.showGUI((Player) sender, new PluginConfigGui(UltimateBottles.getInstance()));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... strings) {
        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.admin";
    }

    @Override
    public String getSyntax() {
        return "/ub settings";
    }

    @Override
    public String getDescription() {
        return "Edit the UltimateBottles Settings.";
    }
}
