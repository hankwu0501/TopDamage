package me.aglerr.topdamage.configs;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigValue {

    public static String LEADERBOARD_STYLE;
    public static boolean LEADERBOARD_CENTER_MESSAGE;

    // Show All style
    public static int SHOW_ALL_MAX_POSITION;
    public static List<String> SHOW_ALL_HEADER;
    public static String SHOW_ALL_FORMAT;
    public static List<String> SHOW_ALL_FOOTER;

    // Show Specific style
    public static String SHOW_SPECIFIC_PLAYER_NOT_FOUND;
    public static String SHOW_SPECIFIC_DAMAGE_NOT_FOUND;
    public static List<String> SHOW_SPECIFIC_MESSAGES;

    public static String NO_PERMISSION;
    public static String RELOAD;

    public static void init() {
        FileConfiguration config = ConfigManager.CONFIG.getConfig();

        LEADERBOARD_STYLE = config.getString("leaderboardStyle.type", "SHOW_ALL");
        LEADERBOARD_CENTER_MESSAGE = config.getBoolean("leaderboardStyle.centerMessage");

        SHOW_ALL_MAX_POSITION = config.getInt("leaderboardStyle.SHOW_ALL.maxPosition");
        SHOW_ALL_HEADER = config.getStringList("leaderboardStyle.SHOW_ALL.header");
        SHOW_ALL_FORMAT = config.getString("leaderboardStyle.SHOW_ALL.format", "&6{position}. &e{player} &fdeals &a{damage} damage");
        SHOW_ALL_FOOTER = config.getStringList("leaderboardStyle.SHOW_ALL.footer");

        SHOW_SPECIFIC_PLAYER_NOT_FOUND = config.getString("leaderboardStyle.SHOW_SPECIFIC.playerNotFound", "&7None");
        SHOW_SPECIFIC_DAMAGE_NOT_FOUND = config.getString("leaderboardStyle.SHOW_SPECIFIC.damageNotFound", "&a0");
        SHOW_SPECIFIC_MESSAGES = config.getStringList("leaderboardStyle.SHOW_SPECIFIC.messages");

        NO_PERMISSION = config.getString("messages.noPermission", "&cYou don't have permission to use this command!");
        RELOAD = config.getString("messages.reload", "&aYou have reloaded the configuration!");
    }

}
