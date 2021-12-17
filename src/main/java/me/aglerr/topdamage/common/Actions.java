package me.aglerr.topdamage.common;

import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.xseries.XSound;
import me.aglerr.mclibs.xseries.messages.ActionBar;
import me.aglerr.mclibs.xseries.messages.Titles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class Actions {

    public static void run(Player player, List<String> actions) {
        actions.forEach(action -> run(player, action));
    }

    public static void run(Player player, String action) {
        if (!action.contains(";")) {
            return;
        }
        String[] split = action.split(";");
        switch (split[0].toLowerCase()) {
            case "message": {
                message(player, split[1]);
                break;
            }
            case "console": {
                console(player, split[1]);
                break;
            }
            case "player": {
                player(player, split[1]);
                break;
            }
            case "title": {
                int fadeIn = Common.isInt(split[3]) ? Integer.parseInt(split[3]) : 20;
                int stay = Common.isInt(split[4]) ? Integer.parseInt(split[4]) : 40;
                int fadeOut = Common.isInt(split[5]) ? Integer.parseInt(split[5]) : 20;
                title(player, split[1], split[2], fadeIn, stay, fadeOut);
                break;
            }
            case "actionbar": {
                int duration = Common.isInt(split[2]) ? Integer.parseInt(split[2]) : 60;
                actionbar(player, split[1], duration);
                break;
            }
            case "sound": {
                float volume = Common.isDouble(split[3]) ? (float) Double.parseDouble(split[3]) : 1.0f;
                float pitch = Common.isDouble(split[3]) ? (float) Double.parseDouble(split[3]) : 1.0f;
                Optional<XSound> xSound = XSound.matchXSound(split[1]);
                if (!xSound.isPresent()) {
                    return;
                }
                sound(player, xSound.get().parseSound(), volume, pitch);
                break;
            }
            default:
                Common.log("&eThere is no action '" + split[0] + "', please fix it!");
                break;
        }
    }

    private static void message(Player player, String message) {
        Common.sendMessage(player, message);
    }

    private static void console(Player player, String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                .replace("{player}", player.getName()));
    }

    private static void player(Player player, String command) {
        player.performCommand(command
                .replace("{player}", player.getName()));
    }

    private static void title(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Titles.sendTitle(player, fadeIn, stay, fadeOut, Common.tryParsePAPI(player, title), Common.tryParsePAPI(player, subTitle));
    }

    private static void actionbar(Player player, String message, int duration) {
        ActionBar.sendActionBar(MCLibs.INSTANCE, player, Common.tryParsePAPI(player, message), duration);
    }

    private static void sound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
