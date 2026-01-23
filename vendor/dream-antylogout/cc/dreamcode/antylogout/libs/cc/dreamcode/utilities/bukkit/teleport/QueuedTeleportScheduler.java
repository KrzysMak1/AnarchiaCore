package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.teleport;

import java.time.Duration;
import org.bukkit.entity.HumanEntity;
import lombok.Generated;
import java.util.UUID;
import java.util.Set;
import java.util.Objects;
import java.util.HashSet;
import org.bukkit.plugin.Plugin;

public class QueuedTeleportScheduler implements Runnable
{
    private final Plugin plugin;
    private final QueuedTeleportCache queuedTeleportCache;
    
    public void run() {
        final Set<UUID> uuidToRemove = (Set<UUID>)new HashSet();
        this.queuedTeleportCache.getKeysAndValues().forEach((uuid, queuedTeleport) -> {
            final HumanEntity humanEntity = (HumanEntity)this.plugin.getServer().getPlayer(uuid);
            if (humanEntity == null) {
                uuidToRemove.add((Object)uuid);
                return;
            }
            final Duration duration = queuedTeleport.getCountdown();
            if (duration.isNegative() || duration.isZero()) {
                queuedTeleport.getTaskAfter().accept((Object)humanEntity);
                uuidToRemove.add((Object)uuid);
                return;
            }
            queuedTeleport.getCountdownNotice().accept((Object)humanEntity, (Object)duration);
        });
        final Set<UUID> set = uuidToRemove;
        final QueuedTeleportCache queuedTeleportCache = this.queuedTeleportCache;
        Objects.requireNonNull((Object)queuedTeleportCache);
        set.forEach(queuedTeleportCache::remove);
    }
    
    @Generated
    public QueuedTeleportScheduler(final Plugin plugin, final QueuedTeleportCache queuedTeleportCache) {
        this.plugin = plugin;
        this.queuedTeleportCache = queuedTeleportCache;
    }
}
