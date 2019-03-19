package com.songoda.ultimatebottles.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.songoda.ultimatebottles.UltimateBottles;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.songoda.ultimatebottles.utils.Experience.getTotalExperience;
import static org.apache.commons.lang3.tuple.Pair.of;

@CommandAlias("%xp")
public class XPCommand extends BaseCommand {

    @Dependency
    private UltimateBottles instance;

    @Default
    @CommandPermission("ultimatebottles.command.xp")
    @Description("Check EXP of yourself or others")
    @CommandCompletion("@players")
    public void onXP(Player player) {
        instance.getLang().sendMessage(player, "xp.yourself", of("amount", getTotalExperience(player)));
    }

    @Subcommand("check")
    @CommandPermission("ultimatebottles.command.xp.other")
    @Description("Check EXP of others")
    @CommandCompletion("@players")
    public void onXPCheck(CommandSender sender, @Flags("other") Player target) {
        instance.getLang().sendMessage(sender, "xp.other", of("amount", getTotalExperience(target)), of("target", target.getName()));
    }

    @HelpCommand
    @CommandPermission("ultimatebottles.command.help")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }
}
