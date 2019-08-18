package com.songoda.ultimatebottles;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Lang {
    private final UltimateBottles instance;

    public Lang(UltimateBottles instance) {
        this.instance = instance;
    }

    public static String color(String toColor) {
        return ChatColor.translateAlternateColorCodes('&', toColor);
    }

    public void sendMessage(CommandSender sender, String path, Pair... placeholder) {
        getMessage(path, placeholder).forEach(sender::sendMessage);
    }

    public String getCooldownMessage(long time) {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );
    }

    public List<String> getMessage(String path, Pair... placeholder) {
        List<String> message;

        if (instance.getConfig().isList("messages." + path)) {
            message = instance.getConfig().getStringList("messages." + path);
        } else {
            message = Collections.singletonList(instance.getConfig().getString("messages." + path));
        }

        return message.stream().map(s -> {
            for (Pair pair : placeholder) {
                s = s.replace("{" + pair.getKey().toString() + "}", pair.getRight().toString());
            }
            return s == null ? "" : color(s);
        }).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
