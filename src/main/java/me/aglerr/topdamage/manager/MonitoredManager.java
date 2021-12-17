package me.aglerr.topdamage.manager;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.aglerr.topdamage.Dependency;
import me.aglerr.topdamage.objects.Mob;
import me.aglerr.topdamage.objects.MonitoredMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MonitoredManager implements Listener {

    private final Map<UUID, MonitoredMob> monitoredMobMap = new HashMap<>();

    private final MobsManager mobsManager;

    public MonitoredManager(MobsManager mobsManager) {
        this.mobsManager = mobsManager;
    }

    public boolean isMonitored(Entity entity) {
        return this.monitoredMobMap.containsKey(entity.getUniqueId());
    }

    public void stopMonitoring(UUID uuid) {
        this.monitoredMobMap.remove(uuid);
    }

    public void monitor(Entity entity) {
        if (Dependency.MYTHIC_MOBS) {
            if (!MythicMobs.inst().getAPIHelper().isMythicMob(entity)) {
                return;
            }
            String internalName = MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity).getType().getInternalName();
            Mob mob = mobsManager.getMob(internalName);
            if (mob != null && mob.getIdentifier().equalsIgnoreCase("mythicmobs")) {
                this.monitoredMobMap.putIfAbsent(
                        entity.getUniqueId(),
                        new MonitoredMob(entity.getUniqueId(), mob)
                );
            }
        }
    }

    public void damage(Entity entity, Player player, double damage) {
        this.monitor(entity);
        MonitoredMob monitoredMob = this.monitoredMobMap.get(entity.getUniqueId());
        if (monitoredMob == null) {
            return;
        }
        monitoredMob.damage(player, damage);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!isMonitored(entity)) {
            return;
        }
        MonitoredMob monitoredMob = this.monitoredMobMap.remove(entity.getUniqueId());
        monitoredMob.rewardAllDamager();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        // If the damager is not player, return
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        this.damage(event.getEntity(), player, event.getFinalDamage());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByProjectile(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile) event.getDamager();
        Entity entity = event.getEntity();
        if (projectile.getShooter() == null ||
                !(projectile.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) projectile.getShooter();
        this.damage(entity, player, event.getFinalDamage());
    }

}
