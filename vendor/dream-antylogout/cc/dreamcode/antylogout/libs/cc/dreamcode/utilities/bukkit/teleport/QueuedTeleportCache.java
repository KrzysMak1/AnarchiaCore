package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.teleport;

import lombok.NonNull;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.UUID;
import java.util.Map;

public class QueuedTeleportCache
{
    private final Map<UUID, QueuedTeleport> queuedTeleportMap;
    
    public QueuedTeleportCache() {
        this.queuedTeleportMap = (Map<UUID, QueuedTeleport>)new WeakHashMap();
    }
    
    public Map<UUID, QueuedTeleport> getKeysAndValues() {
        return (Map<UUID, QueuedTeleport>)Collections.unmodifiableMap((Map)this.queuedTeleportMap);
    }
    
    public boolean isQueued(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        return this.queuedTeleportMap.containsKey((Object)uuid);
    }
    
    public QueuedTeleport get(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        return (QueuedTeleport)this.queuedTeleportMap.get((Object)uuid);
    }
    
    public void apply(@NonNull final UUID uuid, @NonNull final QueuedTeleport queuedTeleport) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (queuedTeleport == null) {
            throw new NullPointerException("queuedTeleport is marked non-null but is null");
        }
        this.queuedTeleportMap.put((Object)uuid, (Object)queuedTeleport);
    }
    
    public void remove(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.queuedTeleportMap.remove((Object)uuid);
    }
}
