package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.teleport;

import java.time.Duration;
import org.bukkit.entity.HumanEntity;
import java.util.UUID;
import org.bukkit.event.Listener;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

public class QueuedTeleportService
{
    private final Plugin plugin;
    private final QueuedTeleportCache queuedTeleportCache;
    
    public QueuedTeleportService(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        this.plugin = plugin;
        this.queuedTeleportCache = new QueuedTeleportCache();
        final QueuedTeleportScheduler queuedTeleportScheduler = new QueuedTeleportScheduler(plugin, this.queuedTeleportCache);
        plugin.getServer().getScheduler().runTaskTimer(plugin, (Runnable)queuedTeleportScheduler, 0L, 2L);
        final QueuedTeleportController queuedTeleportController = new QueuedTeleportController(this.queuedTeleportCache);
        plugin.getServer().getPluginManager().registerEvents((Listener)queuedTeleportController, plugin);
    }
    
    public void applyTeleport(@NonNull final UUID uuid, @NonNull final QueuedTeleport queuedTeleport, final boolean forceTeleport) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (queuedTeleport == null) {
            throw new NullPointerException("queuedTeleport is marked non-null but is null");
        }
        final HumanEntity humanEntity = (HumanEntity)this.plugin.getServer().getPlayer(uuid);
        if (humanEntity == null) {
            return;
        }
        if (!forceTeleport && this.queuedTeleportCache.isQueued(uuid)) {
            final QueuedTeleport existingTeleport = this.queuedTeleportCache.get(uuid);
            final Duration duration = existingTeleport.getCountdown();
            if (!duration.isNegative()) {
                existingTeleport.getAlreadyInAction().accept((Object)humanEntity);
                return;
            }
        }
        final Duration duration2 = queuedTeleport.getCountdown();
        queuedTeleport.getCountdownNotice().accept((Object)humanEntity, (Object)duration2);
        this.queuedTeleportCache.apply(uuid, queuedTeleport);
    }
    
    public void applyTeleport(@NonNull final UUID uuid, @NonNull final QueuedTeleport queuedTeleport) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (queuedTeleport == null) {
            throw new NullPointerException("queuedTeleport is marked non-null but is null");
        }
        this.applyTeleport(uuid, queuedTeleport, false);
    }
    
    public void applyTeleport(@NonNull final HumanEntity humanEntity, @NonNull final QueuedTeleport queuedTeleport, final boolean forceTeleport) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        if (queuedTeleport == null) {
            throw new NullPointerException("queuedTeleport is marked non-null but is null");
        }
        this.applyTeleport(humanEntity.getUniqueId(), queuedTeleport, forceTeleport);
    }
    
    public void applyTeleport(@NonNull final HumanEntity humanEntity, @NonNull final QueuedTeleport queuedTeleport) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        if (queuedTeleport == null) {
            throw new NullPointerException("queuedTeleport is marked non-null but is null");
        }
        this.applyTeleport(humanEntity.getUniqueId(), queuedTeleport);
    }
    
    public void cancelTeleport(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.queuedTeleportCache.remove(uuid);
    }
    
    public void cancelTeleport(@NonNull final HumanEntity humanEntity) {
        if (humanEntity == null) {
            throw new NullPointerException("humanEntity is marked non-null but is null");
        }
        this.cancelTeleport(humanEntity.getUniqueId());
    }
    
    public static QueuedTeleportService apply(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return new QueuedTeleportService(plugin);
    }
}
