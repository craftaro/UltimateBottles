package com.songoda.ultimatebottles.command.commands;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.AbstractCommand;
import com.songoda.ultimatebottles.utils.Experience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCheck extends AbstractCommand {

    public CommandCheck(AbstractCommand parent) {
        super(parent, true, "check");
    }

    @Override
    protected ReturnType runCommand(UltimateBottles instance, CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            instance.getLocale().getMessage("command.check.self")
                    .processPlaceholder("amount", Experience.getTotalExperience(player)).sendPrefixedMessage(player);
            return ReturnType.SUCCESS;
        }

        if (!player.hasPermission("ultimatebottles.command.xp.other")) {
            instance.getLocale().getMessage("command.general.noperms").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (Bukkit.getPlayer(args[1]) == null) {
            instance.getLocale().getMessage("command.general.playernotfound").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Player target = Bukkit.getPlayer(args[1]);
        instance.getLocale().getMessage("command.check.other")
                .processPlaceholder("target", target.getName())
                .processPlaceholder("amount", Experience.getTotalExperience(target)).sendPrefixedMessage(player);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(UltimateBottles instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return null;
        }

        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.command.xp";
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles check <player>";
    }

    @Override
    public String getDescription() {
        return "Check EXP of players.";
    }
}
