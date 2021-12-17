package me.aglerr.topdamage.objects;

import me.aglerr.topdamage.objects.rewards.MobReward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Mob {

    private final String name;
    private final String identifier;
    private final List<String> globalCommands;
    private final MobReward mobReward;

    public Mob(String name, String identifier, List<String> globalCommands, MobReward mobReward) {
        this.name = name;
        this.identifier = identifier;
        this.globalCommands = globalCommands;
        this.mobReward = mobReward;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void executeGlobalCommands() {
        globalCommands.forEach(command ->
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    public boolean reward(Player player, double damage, int position) {
        return this.mobReward.giveReward(player, damage, position);
    }

}
