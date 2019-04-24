package com.songoda.ultimatebottles.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.utils.Experience;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import static com.songoda.ultimatebottles.utils.Experience.*;
import static org.apache.commons.lang3.tuple.Pair.of;

@CommandAlias("%xpbottle")
public class BottleCommand extends BaseCommand {

    @Dependency
    private UltimateBottles instance;

    @Subcommand("reload")
    @Description("Reloads the config")
    @CommandPermission("ultimatebottles.command.reload")
    public void onReload(CommandSender sender) {
        instance.reloadConfig();
        instance.getLang().sendMessage(sender, "reloaded");
    }

    @Subcommand("all")
    @Description("Bottle all your EXP")
    @CommandPermission("ultimatebottles.command.reload")
    public void onAll(Player player) {
        int total = getTotalExperience(player);
        if (total <= 0) {
            instance.getLang().sendMessage(player, "not-enough-exp", of("amount", "" + total));
            return;
        }

        if (instance.getConfig().getInt("minimum-bottle-amount") > total) {
            instance.getLang().sendMessage(player, "minimum-bottle-amount", of("minimum", "" + instance.getConfig().getInt("minimum-bottle-amount")));
            return;
        }

        player.getInventory().addItem(instance.createBottle(player.getName(), total));
        setTotalExperience(player, getTotalExperience(player) - total);
        instance.getLang().sendMessage(player, "bottled", of("amount", "" + total));
    }


    @Subcommand("give")
    @Description("Give EXP bottles")
    @CommandCompletion("@players @nothing")
    @CommandPermission("ultimatebottles.command.give")
    public void onGive(CommandSender sender, @Flags("other") Player target, AmountObject amount) {
        target.getInventory().addItem(instance.createBottle(sender.getName(), amount.getValue()));
        instance.getLang().sendMessage(target, "give.received", of("sender", sender.getName()));
        instance.getLang().sendMessage(sender, "give.given", of("player", target.getName()));
    }

    @Subcommand("giveall")
    @Description("Give EXP bottles to all online players")
    @CommandCompletion("@nothing")
    @CommandPermission("ultimatebottles.command.giveall")
    public void onGive(CommandSender sender, AmountObject amount) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().addItem(instance.createBottle(sender.getName(), amount.getValue()));
            instance.getLang().sendMessage(player, "give.received", of("sender", sender.getName()));
        });

        instance.getLang().sendMessage(sender, "give.givenall");
    }

    @Default
    @Description("Bottle your EXP")
    @CommandPermission("ultimatebottles.command.bottle")
    public void onBottle(Player player, @Optional AmountObject amount) {
        if (amount == null) {
            instance.getLang().sendMessage(player, "syntax");
            return;
        }

        int toBottle = amount.getValue();

        if (toBottle < 0 || toBottle > getTotalExperience(player)) {
            instance.getLang().sendMessage(player, "not-enough-exp", of("amount", "" + Experience.getTotalExperience(player)));
            return;
        }

        if (instance.getConfig().getInt("minimum-bottle-amount") > toBottle) {
            instance.getLang().sendMessage(player, "minimum-bottle-amount", of("minimum", "" + instance.getConfig().getInt("minimum-bottle-amount")));
            return;
        }

        Experience.setTotalExperience(player, (getTotalExperience(player) - toBottle));
        player.getInventory().addItem(instance.createBottle(player.getName(), toBottle));
        instance.getLang().sendMessage(player, "bottled", of("amount", "" + toBottle));

        if (!player.hasPermission("ultimatebottles.cooldown.override") && instance.getConfig().getBoolean("cool-down.enabled")) {
            long time = getCoolDownTime(player);
            instance.getLang().sendMessage(player, "cool-down.started", of("time", instance.getLang().getCooldownMessage(time - System.currentTimeMillis())));
            instance.getCooldownMap().put(player.getUniqueId(), time);
        }
    }

    @HelpCommand
    @CommandPermission("ultimatebottles.command.help")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    private long getCoolDownTime(Player player) {
        return player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(perm -> perm.startsWith("ultimatebottles.cooldown."))
                .map(s -> s.replace("ultimatebottles.cooldown.", ""))
                .filter(StringUtils::isNumeric)
                .mapToInt(Integer::parseInt)
                .max().orElse(instance.getConfig().getInt("cool-down.time-in-minutes")) * 60 * 1000 + System.currentTimeMillis();
    }


}
