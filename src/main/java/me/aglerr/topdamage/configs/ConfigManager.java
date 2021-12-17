package me.aglerr.topdamage.configs;

import me.aglerr.mclibs.libs.CustomConfig;

public class ConfigManager {

    public static CustomConfig CONFIG;

    public static void init() {
        CONFIG = new CustomConfig("config.yml", null);
    }

}
