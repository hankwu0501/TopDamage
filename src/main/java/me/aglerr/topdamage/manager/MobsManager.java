package me.aglerr.topdamage.manager;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.topdamage.Dependency;
import me.aglerr.topdamage.TopDamage;
import me.aglerr.topdamage.objects.Mob;
import me.aglerr.topdamage.objects.rewards.DropTable;
import me.aglerr.topdamage.objects.rewards.MobReward;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobsManager {

    private final Map<String, Mob> mobMap = new HashMap<>();

    private final TopDamage plugin;

    public MobsManager(TopDamage plugin) {
        this.plugin = plugin;
    }

    public Mob getMob(String mobName) {
        return this.mobMap.get(mobName);
    }

    public void loadMobs() {
        Common.log("&rStarting monitored mobs load task...");
        long start = System.currentTimeMillis();
        this.mobMap.clear();

        String DIRECTORY = plugin.getDataFolder() + File.separator + "mobs";
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (new File(DIRECTORY).listFiles().length <= 0) {
            plugin.saveResource("mobs/SkeletalKnight.yml", false);
        }
        for (File file : new File(DIRECTORY).listFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            String mobName = file.getName().replace(".", ";").split(";")[0];
            String identifier = config.getString("mob_identifier", "mythicmobs");
            List<String> globalCommands = config.getStringList("globalCommands");
            MobReward mobReward = new MobReward(mobName);

            if (identifier.equalsIgnoreCase("mythicmobs") && !Dependency.MYTHIC_MOBS) {
                Common.log(
                        "&c-----------------------------",
                        "&cFound loaded mobs '" + mobName + "' with mythicmobs as the identifier",
                        "&cBut MythicMobs is not installed!",
                        "&cSkipping the load process for this mob!",
                        "&c-----------------------------"
                );
                continue;
            }

            for (String position : config.getConfigurationSection("rewards").getKeys(false)) {
                double minimalDamage = config.getDouble("rewards." + position + ".min_damage");
                String mythicDropTable = config.getString("rewards." + position + ".mythicDropTable");
                List<String> actions = config.getStringList("rewards." + position + ".actions");
                if (Common.isInt(position)) {
                    int positionInt = Integer.parseInt(position);
                    mobReward.addDropTable(positionInt, new DropTable(minimalDamage, mythicDropTable, actions));
                }
                if (position.contains("-")) {
                    String[] split = position.split("-");
                    int min = Integer.parseInt(split[0]);
                    if (Common.isInt(split[1])) {
                        int max = Integer.parseInt(split[1]);
                        for (int i = min; i <= max; i++) {
                            mobReward.addDropTable(i, new DropTable(minimalDamage, mythicDropTable, actions));
                        }
                    } else if (split[1].equalsIgnoreCase("all")) {
                        for (int i = min; i <= 150; i++) {
                            mobReward.addDropTable(i, new DropTable(minimalDamage, mythicDropTable, actions));
                        }
                    }
                }
                this.mobMap.put(mobName, new Mob(mobName, identifier, globalCommands, mobReward));
            }
        }
        long total = System.currentTimeMillis() - start;
        Common.log("&rFinished monitored mobs loading task (took " + total + "ms)");
    }

}
