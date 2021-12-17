package me.aglerr.topdamage.objects.rewards;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MobReward {

    private final Map<Integer, DropTable> dropTableMap = new HashMap<>();
    private final String name;

    public MobReward(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addDropTable(int position, DropTable dropTable) {
        this.dropTableMap.put(position, dropTable);
    }

    @Nullable
    public DropTable getDropTable(int position) {
        return this.dropTableMap.get(position);
    }

    public boolean hasDropTable(int position) {
        return this.dropTableMap.containsKey(position);
    }

    public boolean giveReward(Player player, double damage, int position) {
        DropTable dropTable = getDropTable(position);
        if (dropTable == null) {
            return false;
        }
        dropTable.reward(player, damage, position);
        return true;
    }

}
