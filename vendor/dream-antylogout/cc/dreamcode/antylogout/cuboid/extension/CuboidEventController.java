package cc.dreamcode.antylogout.cuboid.extension;

import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.VectorUtil;
import org.bukkit.event.Event;
import cc.dreamcode.antylogout.cuboid.extension.event.CuboidEnteredEvent;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.object.Duo;
import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Server;
import org.bukkit.event.Listener;

public class CuboidEventController implements Listener
{
    private final Server server;
    private final CuboidCache cuboidCache;
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (to == null) {
            return;
        }
        this.cuboidCache.getRegionList().forEach(duo -> {
            final boolean wasInside = this.cuboidCache.inCuboid(from, duo);
            final boolean isInside = this.cuboidCache.inCuboid(to, duo);
            if (!wasInside && isInside) {
                final CuboidEnteredEvent cuboidEnteredEvent = new CuboidEnteredEvent(player, duo.getFirst(), duo.getSecond());
                this.server.getPluginManager().callEvent((Event)cuboidEnteredEvent);
                if (cuboidEnteredEvent.isCancelled()) {
                    final Location newLoc = from.clone().add(VectorUtil.getDirection(player.getLocation(), duo.getFirst(), duo.getSecond()).getVector());
                    player.teleport(newLoc);
                }
            }
        });
    }
    
    @Inject
    @Generated
    public CuboidEventController(final Server server, final CuboidCache cuboidCache) {
        this.server = server;
        this.cuboidCache = cuboidCache;
    }
}
