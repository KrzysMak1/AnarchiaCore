package cc.dreamcode.antylogout.barrier.scheduler;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import java.util.HashMap;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import cc.dreamcode.antylogout.profile.Profile;
import org.bukkit.entity.Player;
import cc.dreamcode.antylogout.barrier.BarrierType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.UUID;
import java.util.Map;
import cc.dreamcode.antylogout.barrier.BarrierService;
import cc.dreamcode.antylogout.config.PluginConfig;
import cc.dreamcode.antylogout.profile.ProfileCache;
import org.bukkit.event.Listener;

public class WallBarrierController implements Listener
{
    private final ProfileCache profileCache;
    private final PluginConfig pluginConfig;
    private final BarrierService barrierService;
    private final Map<UUID, Long> lastUpdateMap;
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = this.profileCache.get((HumanEntity)player);
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (profile == null) {
            return;
        }
        if (player.hasPermission("dream.antylogout.bypass")) {
            return;
        }
        if (this.pluginConfig.barrier.type != BarrierType.WALL) {
            return;
        }
        if (!profile.isInCombat()) {
            return;
        }
        if (to == null || (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())) {
            return;
        }
        this.throttledEvent(player);
    }
    
    private void throttledEvent(final Player player) {
        final UUID id = player.getUniqueId();
        final long now = System.currentTimeMillis();
        final Long lastUpdate = (Long)this.lastUpdateMap.get((Object)id);
        if (lastUpdate == null || now - lastUpdate > 250L) {
            this.barrierService.updatePlayer(player);
            this.lastUpdateMap.put((Object)id, (Object)now);
        }
    }
    
    @Inject
    @Generated
    public WallBarrierController(final ProfileCache profileCache, final PluginConfig pluginConfig, final BarrierService barrierService) {
        this.lastUpdateMap = (Map<UUID, Long>)new HashMap();
        this.profileCache = profileCache;
        this.pluginConfig = pluginConfig;
        this.barrierService = barrierService;
    }
}
