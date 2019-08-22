package com.songoda.ultimatebottles.command.commands;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSettings extends AbstractCommand {

    public CommandSettings(AbstractCommand parent) {
        super(parent, true, "Settings");
    }

    @Override
    protected ReturnType runCommand(UltimateBottles instance, CommandSender sender, String... args) {
        instance.getSettingsManager().openSettingsManager((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(UltimateBottles instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.admin";
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles settings";
    }

    @Override
    public String getDescription() {
        return "Edit the UltimateBottles Settings.";
    }
}
