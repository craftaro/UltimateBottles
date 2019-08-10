package com.songoda.ultimatebottles.command.commands;

import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.AbstractCommand;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.utils.Experience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandGive extends AbstractCommand {

    public CommandGive(AbstractCommand parent) {
        super(parent, false, "give");
    }

    @Override
    protected ReturnType runCommand(UltimateBottles instance, CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length != 3) {
            return ReturnType.SYNTAX_ERROR;
        }

        if (Bukkit.getPlayer(args[1]) == null) {
            instance.getLocale().getMessage("command.general.playernotfound").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (!isInt(args[2])) {
            instance.getLocale().getMessage("command.general.notanumber").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Optional<AmountObject> amountObject = AmountObject.of(args[2]);
        int toBottle = amountObject.get().getValue();

        Player target = Bukkit.getPlayer(args[1]);

        target.getInventory().addItem(instance.createBottle(player.getName(), toBottle));
        instance.getLocale().getMessage("command.give.received")
                .processPlaceholder("sender", player.getName()).sendPrefixedMessage(target);
        instance.getLocale().getMessage("command.give.given")
                .processPlaceholder("player", target.getName()).sendPrefixedMessage(player);

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
        return "ultimatebottles.command.give";
    }

    @Override
    public String getSyntax() {
        return "/UltimateBottles give <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Give EXP bottles.";
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
