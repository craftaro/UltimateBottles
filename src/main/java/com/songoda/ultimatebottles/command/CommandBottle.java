package com.songoda.ultimatebottles.command;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.settings.Settings;
import com.songoda.ultimatebottles.utils.Experience;
import com.songoda.ultimatebottles.utils.Methods;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandBottle extends AbstractCommand {

    private final UltimateBottles instance;

    public CommandBottle(UltimateBottles instance) {
        super(false, "bottle");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length != 1) {
            return ReturnType.SYNTAX_ERROR;
        }

        if (!Methods.isNumeric(args[0]) && !args[0].equalsIgnoreCase("all")) {
            instance.getLocale().getMessage("command.general.notanumber").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (args[0].equalsIgnoreCase("all") && !player.hasPermission("ultimatebottles.bottleall")) {
            instance.getLocale().getMessage("command.general.noperms").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Optional<AmountObject> amountObject = args[0].equalsIgnoreCase("all") ? null : AmountObject.of(args[0]);
        int toBottle = amountObject != null ? amountObject.get().getValue() : Experience.getTotalExperience(player);


        if (toBottle < 0 || toBottle > Experience.getTotalExperience(player)) {
            instance.getLocale().getMessage("command.bottle.notenough")
                    .processPlaceholder("amount", Experience.getTotalExperience(player)).sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (Settings.MIN_BOTTLE_AMOUNT.getInt() > toBottle) {
            instance.getLocale().getMessage("command.general.minimum")
                    .processPlaceholder("minimum", Settings.MIN_BOTTLE_AMOUNT.getInt()).sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        player.getInventory().addItem(instance.createBottle(player.getName(), toBottle));
        Experience.setTotalExperience(player, Experience.getTotalExperience(player) - toBottle);
        instance.getLocale().getMessage("command.bottle.bottled")
                .processPlaceholder("amount", toBottle).sendMessage(player);

        if (!player.hasPermission("ultimatebottles.cooldown.override") && Settings.COOLDOWN.getBoolean()) {
            long time = getCoolDownTime(player);
            instance.getLocale().getMessage("event.cooldown.started")
                    .processPlaceholder("time", instance.getLang().getCooldownMessage(time - System.currentTimeMillis()));
            instance.getCooldownMap().put(player.getUniqueId(), time);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        if (args.length == 1) return Arrays.asList("all", "amount");

        return new ArrayList<>();
    }

    @Override
    public String getPermissionNode() {
        return "ultimatebottles.bottle";
    }

    @Override
    public String getSyntax() {
        return "/ub bottle <amount/all>";
    }

    @Override
    public String getDescription() {
        return "Bottle your EXP.";
    }

    private long getCoolDownTime(Player player) {
        return player.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .filter(perm -> perm.startsWith("ultimatebottles.cooldown."))
                .map(s -> s.replace("ultimatebottles.cooldown.", ""))
                .filter(StringUtils::isNumeric)
                .mapToInt(Integer::parseInt)
                .max().orElse(Settings.COOLDOWN_TIME.getInt()) * 60 * 1000 + System.currentTimeMillis();
    }
}
