package com.songoda.ultimatebottles.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.utils.Experience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandCheck extends AbstractCommand {

    private final UltimateBottles instance;

    public CommandCheck(UltimateBottles instance) {
        super(true, "check");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            instance.getLocale().getMessage("command.check.self")
                    .processPlaceholder("amount", Experience.getTotalExperience(player)).sendPrefixedMessage(player);
            return ReturnType.SUCCESS;
        }

        if (!player.hasPermission("ultimatebottles.command.xp.other")) {
            instance.getLocale().getMessage("command.general.noperms").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            instance.getLocale().getMessage("command.general.playernotfound").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Player target = Bukkit.getPlayer(args[0]);
        instance.getLocale().getMessage("command.check.other")
                .processPlaceholder("target", target.getName())
                .processPlaceholder("amount", Experience.getTotalExperience(target)).sendPrefixedMessage(player);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) {
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
        return "/ub check <player>";
    }

    @Override
    public String getDescription() {
        return "Check EXP of players.";
    }
}
