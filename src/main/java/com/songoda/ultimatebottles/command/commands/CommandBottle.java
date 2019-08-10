package com.songoda.ultimatebottles.command.commands;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.AbstractCommand;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.utils.Experience;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandBottle extends AbstractCommand {

    public CommandBottle(AbstractCommand parent) {
        super(parent, false, "bottle");
    }

    @Override
    protected ReturnType runCommand(UltimateBottles instance, CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            return ReturnType.SYNTAX_ERROR;
        }

        if (!isInt(args[1]) && !args[1].equalsIgnoreCase("all")) {
            instance.getLocale().getMessage("command.general.notanumber").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (args[1].equalsIgnoreCase("all") && !player.hasPermission("ultimatebottles.bottleall")) {
            instance.getLocale().getMessage("command.general.noperms").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Optional<AmountObject> amountObject = args[1].equalsIgnoreCase("all") ? null : AmountObject.of(args[1]);
        int toBottle = amountObject != null ? amountObject.get().getValue() : Experience.getTotalExperience(player);


        if (toBottle < 0 || toBottle > Experience.getTotalExperience(player)) {
            instance.getLocale().getMessage("command.bottle.notenough")
                    .processPlaceholder("amount", Experience.getTotalExperience(player)).sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (instance.getConfig().getInt("minimum-bottle-amount") > toBottle) {
            instance.getLocale().getMessage("command.general.minimum")
                    .processPlaceholder("minimum", instance.getConfig().getInt("minimum-bottle-amount")).sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        player.getInventory().addItem(instance.createBottle(player.getName(), toBottle));
        Experience.setTotalExperience(player, Experience.getTotalExperience(player) - toBottle);
        instance.getLocale().getMessage("command.bottle.bottled")
                .processPlaceholder("amount", toBottle).sendMessage(player);

        if (!player.hasPermission("ultimatebottles.cooldown.override") && instance.getConfig().getBoolean("cool-down.enabled")) {
            long time = getCoolDownTime(player);
            instance.getLocale().getMessage("event.cooldown.started")
                    .processPlaceholder("time", instance.getLang().getCooldownMessage(time - System.currentTimeMillis()));
            instance.getCooldownMap().put(player.getUniqueId(), time);
        }

        return ReturnType.SUCCESS;
    }

    private long getCoolDownTime(Player player) {
        return player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(perm -> perm.startsWith("ultimatebottles.cooldown."))
                .map(s -> s.replace("ultimatebottles.cooldown.", ""))
                .filter(StringUtils::isNumeric)
                .mapToInt(Integer::parseInt)
                .max().orElse(UltimateBottles.getInstance().getConfig().getInt("cool-down.time-in-minutes")) * 60 * 1000 + System.currentTimeMillis();
    }

    @Override
    protected List<String> onTab(UltimateBottles instance, CommandSender sender, String... args) {
        if (args.length == 2) {
            return Arrays.asList("all", "amount");
        }

        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.bottle";
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles bottle <amount/all>";
    }

    @Override
    public String getDescription() {
        return "Bottle your EXP.";
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
