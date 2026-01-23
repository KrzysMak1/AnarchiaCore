package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.teleport;

import lombok.Generated;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.Listener;

public class QueuedTeleportController implements Listener
{
    private final QueuedTeleportCache queuedTeleportCache;
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
        if (!this.queuedTeleportCache.isQueued(player.getUniqueId())) {
            return;
        }
        final QueuedTeleport queuedTeleport = this.queuedTeleportCache.get(player.getUniqueId());
        if (!queuedTeleport.isCancelOnMove() || e.getTo() == null) {
            return;
        }
        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            queuedTeleport.getMovedNotice().accept((Object)player);
            this.queuedTeleportCache.remove(player.getUniqueId());
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.getCause().equals((Object)PlayerTeleportEvent.TeleportCause.PLUGIN) || event.getCause().equals((Object)PlayerTeleportEvent.TeleportCause.UNKNOWN)) {
            return;
        }
        final Player player = event.getPlayer();
        if (!this.queuedTeleportCache.isQueued(player.getUniqueId())) {
            return;
        }
        final QueuedTeleport queuedTeleport = this.queuedTeleportCache.get(player.getUniqueId());
        if (!queuedTeleport.isCancelOnMove()) {
            return;
        }
        queuedTeleport.getMovedNotice().accept((Object)player);
        this.queuedTeleportCache.remove(player.getUniqueId());
    }
    
    @Generated
    public QueuedTeleportController(final QueuedTeleportCache queuedTeleportCache) {
        this.queuedTeleportCache = queuedTeleportCache;
    }
}
