package com.songoda.ultimatebottles.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatebottles.UltimateBottles;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandUltimateBottles extends AbstractCommand {

    private final UltimateBottles instance;

    public CommandUltimateBottles(UltimateBottles instance) {
        super(false, "UltimateBottles");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        sender.sendMessage("");
        instance.getLocale().newMessage("&7Version " + instance.getDescription().getVersion() +
                " Created with <3 by &5&l&oSongoda").sendPrefixedMessage(sender);

        for (AbstractCommand command : instance.getCommandManager().getAllCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }

        sender.sendMessage("");
        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return new ArrayList<>();
    }

}
