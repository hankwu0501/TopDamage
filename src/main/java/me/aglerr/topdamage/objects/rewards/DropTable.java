package me.aglerr.topdamage.objects.rewards;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.topdamage.Dependency;
import me.aglerr.topdamage.common.Actions;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DropTable {

    private final double minimalDamage;
    private final String mythicDropTable;
    private final List<String> actions;

    public DropTable(double minimalDamage, String mythicDropTable, List<String> actions) {
        this.minimalDamage = minimalDamage;
        this.mythicDropTable = mythicDropTable;
        this.actions = actions;
    }

    public void reward(Player player, double damage, int position) {
        if (damage < minimalDamage) {
            return;
        }
        Actions.run(player, parse(actions, player, damage, position));
        if (Dependency.MYTHIC_MOBS) {
            Optional<io.lumine.xikage.mythicmobs.drops.DropTable> dropTable =
                    MythicMobs.inst().getDropManager().getDropTable(mythicDropTable);
            if (!dropTable.isPresent()) {
                return;
            }
            AbstractPlayer abstractPlayer = MythicMobs.inst().server().getPlayer(player.getUniqueId());
            dropTable.get().generate(new DropMetadata(null, null))
                    .give(abstractPlayer);
        }
    }

    private List<String> parse(List<String> messages, Player player, double damage, int position) {
        List<String> translated = new ArrayList<>();
        for (String message : messages) {
            message = message.replace("{player}", player.getName());
            message = message.replace("{damage}", Common.digits(damage));
            message = message.replace("{position}", position + "");
            translated.add(message);
        }
        return translated;
    }

}
