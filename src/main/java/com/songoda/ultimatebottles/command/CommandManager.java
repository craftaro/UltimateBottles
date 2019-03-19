package com.songoda.ultimatebottles.command;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.InvalidCommandArgument;
import com.songoda.ultimatebottles.UltimateBottles;
import com.songoda.ultimatebottles.command.commands.BottleCommand;
import com.songoda.ultimatebottles.command.commands.XPCommand;
import com.songoda.ultimatebottles.objects.AmountObject;
import com.songoda.ultimatebottles.objects.RangeObject;

import java.util.Optional;

import static org.apache.commons.lang3.tuple.Pair.of;

public class CommandManager extends BukkitCommandManager {
    private final UltimateBottles instance;

    public CommandManager(UltimateBottles instance) {
        super(instance);
        this.instance = instance;

        registerDependency(UltimateBottles.class, "instance", instance);

        getCommandReplacements().addReplacements(
                "xpbottle", instance.getConfig().getString("commands.xpbottle"),
                "xp", instance.getConfig().getString("commands.xp")
        );

        getCommandContexts().registerContext(AmountObject.class, c -> {
            Optional<AmountObject> amountObject = AmountObject.of(c.popFirstArg());

            if (!amountObject.isPresent()) {
                throw new InvalidCommandArgument("Invalid amount: [amount = range/amount]", false);
            }

            AmountObject amount = amountObject.get();

            if(amount.getGetter() instanceof RangeObject) {
                RangeObject rangeObject = (RangeObject) amountObject.get().getGetter();

                if(rangeObject.getLower().get() >= rangeObject.getUpper().get()) {
                    throw new InvalidCommandArgument(getThrowableMessage("bound.upper-greater-than-lower"), false);
                }

                if(instance.getConfig().getInt("minimum-bottle-amount") > rangeObject.getLower().get()) {
                    throw new InvalidCommandArgument(getThrowableMessage("bound.lower-too-low"), false);
                }
            }

            int toTest = amount.getValue();

            if (instance.getConfig().getInt("minimum-bottle-amount") > toTest) {
                throw new InvalidCommandArgument(getThrowableMessage("minimum-bottle-amount"), false);
            }

            return amount;
        });

        registerCommand(new BottleCommand());
        registerCommand(new XPCommand());

        enableUnstableAPI("help");
    }

    private String getThrowableMessage(String path) {
        return instance.getLang().getMessage(path, of("minimum", "" + instance.getConfig().getInt("minimum-bottle-amount")))
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find throwable message."));
    }

}
