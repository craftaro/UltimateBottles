package com.songoda.ultimatebottles.command;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandGive extends AbstractCommand {

    private final UltimateBottles instance;

    public CommandGive(UltimateBottles instance) {
        super(false, "give");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        if (args.length != 2) return ReturnType.SYNTAX_ERROR;

        if (Bukkit.getPlayer(args[0]) == null) {
            instance.getLocale().getMessage("command.general.playernotfound").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        if (!Methods.isNumeric(args[1])) {
            instance.getLocale().getMessage("command.general.notanumber").sendPrefixedMessage(player);
            return ReturnType.FAILURE;
        }

        Optional<AmountObject> amountObject = AmountObject.of(args[1]);
        int toBottle = amountObject.get().getValue();

        Player target = Bukkit.getPlayer(args[0]);

        target.getInventory().addItem(instance.createBottle(player.getName(), toBottle));
        instance.getLocale().getMessage("command.give.received")
                .processPlaceholder("sender", player.getName()).sendPrefixedMessage(target);
        instance.getLocale().getMessage("command.give.given")
                .processPlaceholder("player", target.getName()).sendPrefixedMessage(player);

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
        return "ultimatebottles.command.give";
    }

    @Override
    public String getSyntax() {
        return "/ub give <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Give EXP bottles.";
    }
}
