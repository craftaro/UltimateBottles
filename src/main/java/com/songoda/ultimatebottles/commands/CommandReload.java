package com.songoda.ultimatebottles.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatebottles.UltimateBottles;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandReload extends AbstractCommand {

    private final UltimateBottles instance;

    public CommandReload(UltimateBottles instance) {
        super(false, "reload");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... strings) {
        instance.onConfigReload();
        instance.getLocale().getMessage("command.reload.reloaded").sendPrefixedMessage(sender);
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
        return "/ub reload";
    }

    @Override
    public String getDescription() {
        return "Reload the Configuration and Language files.";
    }
}
