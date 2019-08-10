package com.songoda.ultimatebottles.command.commands;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandReload extends AbstractCommand {

    public CommandReload(AbstractCommand parent) {
        super(parent, false, "reload");
    }

    @Override
    protected ReturnType runCommand(UltimateBottles instance, CommandSender sender, String... args) {
        instance.reloadConfig();
        instance.getLocale().getMessage("command.reload.reloaded").sendPrefixedMessage(sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(UltimateBottles instance, CommandSender sender, String... args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.admin";
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles reload";
    }

    @Override
    public String getDescription() {
        return "Reload the Configuration and Language files.";
    }
}
