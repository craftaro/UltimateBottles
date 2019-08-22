package com.songoda.ultimatebottles.utils.settings;


import com.songoda.ultimatebottles.UltimateBottles;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Setting {

    BOTTLE_ITEM("Main.Bottle Item", "EXPERIENCE_BOTTLE",
            "The item used to store experience in."),

    MIN_BOTTLE_AMOUNT("Main.Min Bottle Amount", 25,
            "The minimum experience that can be stored."),

    COOLDOWN("Main.Cooldown Enabled", true,
            "Should there be a cooldown when a bottle is used?"),

    COOLDOWN_TIME("Main.Cooldown Time", 15,
            "How long should the cooldown be?"),

    BLOCKED_COMMANDS("Main.Blocked Commands", Arrays.asList("bottle"),
            "The commands blocked during the cooldown."),

    GLASS_TYPE_1("Interfaces.Glass Type 1", 7),
    GLASS_TYPE_2("Interfaces.Glass Type 2", 11),
    GLASS_TYPE_3("Interfaces.Glass Type 3", 3),

    LANGUGE_MODE("System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    private String setting;
    private Object option;
    private String[] comments;

    Setting(String setting, Object option, String... comments) {
        this.setting = setting;
        this.option = option;
        this.comments = comments;
    }

    Setting(String setting, Object option) {
        this.setting = setting;
        this.option = option;
        this.comments = null;
    }

    public static Setting getSetting(String setting) {
        List<Setting> settings = Arrays.stream(values()).filter(setting1 -> setting1.setting.equals(setting)).collect(Collectors.toList());
        if (settings.isEmpty()) return null;
        return settings.get(0);
    }

    public String getSetting() {
        return setting;
    }

    public Object getOption() {
        return option;
    }

    public String[] getComments() {
        return comments;
    }

    public List<Integer> getIntegerList() {
        return UltimateBottles.getInstance().getConfig().getIntegerList(setting);
    }

    public List<String> getStringList() {
        return UltimateBottles.getInstance().getConfig().getStringList(setting);
    }

    public boolean getBoolean() {
        return UltimateBottles.getInstance().getConfig().getBoolean(setting);
    }

    public int getInt() {
        return UltimateBottles.getInstance().getConfig().getInt(setting);
    }

    public long getLong() {
        return UltimateBottles.getInstance().getConfig().getLong(setting);
    }

    public String getString() {
        return UltimateBottles.getInstance().getConfig().getString(setting);
    }

    public char getChar() {
        return UltimateBottles.getInstance().getConfig().getString(setting).charAt(0);
    }

    public double getDouble() {
        return UltimateBottles.getInstance().getConfig().getDouble(setting);
    }

    public Material getMaterial() {
        String materialStr = UltimateBottles.getInstance().getConfig().getString(setting);
        Material material = Material.getMaterial(materialStr);

        if (material == null) {
            System.out.println(String.format("Config value \"%s\" has an invalid material name: \"%s\"", setting, materialStr));
        }

        return material;
    }
}