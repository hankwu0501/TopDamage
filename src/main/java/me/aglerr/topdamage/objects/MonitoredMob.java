package me.aglerr.topdamage.objects;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.messages.CenterMessage;
import me.aglerr.topdamage.configs.ConfigValue;
import org.bukkit.entity.Player;

import java.util.*;

public class MonitoredMob {

    private final Map<Player, Double> damageDoneMap = new HashMap<>();

    private final UUID uuid;
    private final Mob mob;

    public MonitoredMob(UUID uuid, Mob mob) {
        this.uuid = uuid;
        this.mob = mob;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void damage(Player player, double damage) {
        this.damageDoneMap.put(
                player,
                (this.damageDoneMap.getOrDefault(player, 0.0) + damage)
        );
    }

    public void rewardAllDamager() {
        List<Player> sorted = this.getSortedDamageDone();
        for (int i = 0; i < sorted.size(); i++) {
            Player player = sorted.get(i);
            if (player == null || !player.isOnline() || !this.damageDoneMap.containsKey(player)) {
                continue;
            }
            this.broadcastLeaderboard(player, sorted);
            mob.reward(player, this.damageDoneMap.get(player), i + 1);
        }
    }

    protected void broadcastLeaderboard(Player player, List<Player> sorted) {
        if(ConfigValue.LEADERBOARD_STYLE.equalsIgnoreCase("SHOW_ALL")){
            Common.sendMessage(player, showAll(sorted));
        } else if(ConfigValue.LEADERBOARD_STYLE.equalsIgnoreCase("SHOW_SPECIFIC")){
            Common.sendMessage(player, showSpecific(sorted));
        }
    }

    protected List<String> showAll(List<Player> sorted) {
        List<String> messages = new ArrayList<>();
        for (String message : ConfigValue.SHOW_ALL_HEADER) {
            messages.add(ConfigValue.LEADERBOARD_CENTER_MESSAGE ? CenterMessage.centerMessage(message) : message);
        }
        int position = 1;
        for (Player player : sorted) {
            String message = ConfigValue.SHOW_ALL_FORMAT;
            if (position > ConfigValue.SHOW_ALL_MAX_POSITION) {
                return messages;
            }
            double damage = damageDoneMap.get(player);
            message = message.replace("{position}", position + "");
            message = message.replace("{player}", player.getName());
            message = message.replace("{damage}", Common.digits(damage));
            messages.add(ConfigValue.LEADERBOARD_CENTER_MESSAGE ? CenterMessage.centerMessage(message) : message);
            position++;
        }
        for (String message : ConfigValue.SHOW_ALL_FOOTER) {
            messages.add(ConfigValue.LEADERBOARD_CENTER_MESSAGE ? CenterMessage.centerMessage(message) : message);
        }
        return messages;
    }

    protected List<String> showSpecific(List<Player> sorted) {
        List<String> messages = new ArrayList<>();
        for (String message : ConfigValue.SHOW_SPECIFIC_MESSAGES) {
            for (int i = 0; i <= 50; i++) {
                int position = i + 1;
                String playerName;
                String damage;
                if (this.isValidIndex(i, sorted)) {
                    Player player = sorted.get(i);
                    playerName = player.getName();
                    damage = Common.digits(this.damageDoneMap.get(player));
                } else {
                    playerName = ConfigValue.SHOW_SPECIFIC_PLAYER_NOT_FOUND;
                    damage = ConfigValue.SHOW_SPECIFIC_DAMAGE_NOT_FOUND;
                }
                message = message.replace("{player_" + position + "}", playerName);
                message = message.replace("{damage_" + position + "}", damage);
            }
            messages.add(ConfigValue.LEADERBOARD_CENTER_MESSAGE ? CenterMessage.centerMessage(message) : message);
        }
        return messages;
    }

    protected List<Player> getSortedDamageDone() {
        List<Player> damager = new ArrayList<>(damageDoneMap.keySet());
        damager.sort((player1, player2) -> {
            Float d1 = damageDoneMap.get(player1).floatValue();
            Float d2 = damageDoneMap.get(player2).floatValue();

            return d2.compareTo(d1);
        });
        return damager;
    }

    protected boolean isValidIndex(int index, List<?> list) {
        try {
            list.get(index);
            return true;
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

}
