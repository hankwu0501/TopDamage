package me.aglerr.topdamage.commands;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.topdamage.TopDamage;
import me.aglerr.topdamage.configs.ConfigValue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final TopDamage plugin;

    public ReloadCommand(TopDamage plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("topdamage.reload")) {
            Common.sendMessage(sender, ConfigValue.NO_PERMISSION);
            return true;
        }
        plugin.reloadEverything();
        Common.sendMessage(sender, ConfigValue.RELOAD);
        return true;
    }

}
