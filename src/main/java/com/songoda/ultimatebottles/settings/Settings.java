package com.songoda.ultimatebottles.settings;

import com.songoda.core.configuration.Config;
import com.songoda.core.configuration.ConfigSetting;
import com.songoda.ultimatebottles.UltimateBottles;

import java.util.Arrays;

public class Settings {

    static final Config config = UltimateBottles.getInstance().getConfig().getCoreConfig();

    public static final ConfigSetting BOTTLE_ITEM = new ConfigSetting(config, "Main.Bottle Item",
            "EXPERIENCE_BOTTLE", "The item used to store experience in.");

    public static final ConfigSetting MIN_BOTTLE_AMOUNT = new ConfigSetting(config, "Main.Min Bottle Amount",
            25, "The minimum experience that can be stored.");

    public static final ConfigSetting COOLDOWN = new ConfigSetting(config, "Main.Cooldown Enabled",
            true, "Should there be a cooldown when a bottle is used?");

    public static final ConfigSetting COOLDOWN_TIME = new ConfigSetting(config, "Main.Cooldown Time",
            15, "How long should the cooldown be?");

    public static final ConfigSetting BLOCKED_COMMANDS = new ConfigSetting(config, "Main.Blocked Commands",
            Arrays.asList("/ub bottle"), "The commands blocked during the cooldown.");

    public static final ConfigSetting LANGUAGE_MODE = new ConfigSetting(config, "System.Language Mode",
            "en_US", "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    public static void setupConfig() {
        config.load();
    }
}
