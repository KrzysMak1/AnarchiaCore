package me.anarchiacore.combatlog;

import me.anarchiacore.config.MessageService;
import me.anarchiacore.customitems.CustomItemsManager;
import me.anarchiacore.storage.DataStore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogManager implements Listener {
    private final Plugin plugin;
    private final MessageService messageService;
    private final DataStore dataStore;
    private final CustomItemsManager customItemsManager;
    private final Map<UUID, Long> combatExpiry = new HashMap<>();
    private BukkitTask task;

    public CombatLogManager(Plugin plugin, MessageService messageService, DataStore dataStore, CustomItemsManager customItemsManager) {
        this.plugin = plugin;
        this.messageService = messageService;
        this.dataStore = dataStore;
        this.customItemsManager = customItemsManager;
    }

    public void start() {
        stopTask();
        int intervalTicks = 20;
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            combatExpiry.entrySet().removeIf(entry -> entry.getValue() <= now);
        }, intervalTicks, intervalTicks);
    }

    public void stop() {
        stopTask();
        combatExpiry.clear();
    }

    public void reload() {
        stop();
        start();
    }

    public int getActiveTaskCount() {
        return task != null && !task.isCancelled() ? 1 : 0;
    }

    public int getListenerCount() {
        return 1;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        Player attacker = null;
        if (event.getDamager() instanceof Player player) {
            attacker = player;
        } else if (event.getDamager() instanceof org.bukkit.entity.Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            attacker = shooter;
        }
        if (attacker != null) {
            tag(attacker);
            tag(victim);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!isInCombat(player.getUniqueId())) {
            return;
        }
        if (customItemsManager.consumeTotemForCombatLogout(player)) {
            dataStore.setSpawnOnJoin(player.getUniqueId(), true);
            dataStore.save();
            combatExpiry.remove(player.getUniqueId());
            return;
        }
        boolean killOnLogout = plugin.getConfig().getBoolean("combatlog.killOnLogout", true);
        if (killOnLogout && player.getGameMode() != GameMode.CREATIVE) {
            var maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealth != null) {
                player.damage(maxHealth.getValue());
            }
        }
        String message = plugin.getConfig().getString("messages.combatlog.logoutPunish");
        if (message != null) {
            messageService.send(player, message);
        }
        combatExpiry.remove(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (dataStore.getSpawnOnJoin(player.getUniqueId())) {
            Location spawn = player.getWorld().getSpawnLocation();
            player.teleport(spawn);
            dataStore.clearSpawnOnJoin(player.getUniqueId());
            dataStore.save();
            clearCombat(player.getUniqueId());
            messageService.send(player, plugin.getConfig().getString("messages.combatlog.spawned"));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        combatExpiry.remove(event.getEntity().getUniqueId());
    }

    public void tag(Player player) {
        long durationSeconds = plugin.getConfig().getLong("combatlog.tagDurationSeconds", 15);
        long expire = System.currentTimeMillis() + (durationSeconds * 1000L);
        combatExpiry.put(player.getUniqueId(), expire);
        messageService.send(player, plugin.getConfig().getString("messages.combatlog.tagged"));
    }

    public boolean isInCombat(UUID uuid) {
        Long expire = combatExpiry.get(uuid);
        return expire != null && expire > System.currentTimeMillis();
    }

    public void clearCombat(UUID uuid) {
        combatExpiry.remove(uuid);
    }

    private void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
