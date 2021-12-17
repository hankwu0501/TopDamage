package me.aglerr.topdamage;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Dependency {

    public static boolean MYTHIC_MOBS;

    public static void init() {
        PluginManager pm = Bukkit.getPluginManager();

        MYTHIC_MOBS = pm.getPlugin("MythicMobs") != null;
    }

}
