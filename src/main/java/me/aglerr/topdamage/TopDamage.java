package me.aglerr.topdamage;

import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.messages.CenterMessage;
import me.aglerr.topdamage.commands.ReloadCommand;
import me.aglerr.topdamage.configs.ConfigManager;
import me.aglerr.topdamage.configs.ConfigValue;
import me.aglerr.topdamage.manager.MobsManager;
import me.aglerr.topdamage.manager.MonitoredManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TopDamage extends JavaPlugin {

    private final MobsManager mobsManager = new MobsManager(this);
    private final MonitoredManager monitoredManager = new MonitoredManager(mobsManager);

    @Override
    public void onEnable() {
        MCLibs.init(this);
        Common.setPrefix("[TopDamage]");
        //--------------------------------------
        Dependency.init();
        ConfigManager.init();
        ConfigValue.init();
        //--------------------------------------
        this.registerListeners();
        //--------------------------------------
        this.mobsManager.loadMobs();
    }

    public void reloadEverything(){
        ConfigManager.CONFIG.reloadConfig();
        ConfigValue.init();
        //--------------------------------------
        this.mobsManager.loadMobs();
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(monitoredManager, this);
    }

    private void registerCommands(){
        this.getCommand("topdamagereload").setExecutor(new ReloadCommand(this));
    }

}
