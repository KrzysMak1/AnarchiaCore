package cc.dreamcode.antylogout.hook.worldguard;

import java.util.Iterator;
import cc.dreamcode.antylogout.hook.worldguard.extension.event.RegionLeftEvent;
import cc.dreamcode.antylogout.hook.worldguard.extension.event.RegionEnteredEvent;
import cc.dreamcode.antylogout.hook.worldguard.extension.event.RegionsLeftEvent;
import cc.dreamcode.antylogout.hook.worldguard.extension.event.RegionsEnteredEvent;
import org.bukkit.event.Event;
import cc.dreamcode.antylogout.hook.worldguard.extension.event.RegionsChangedEvent;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Set;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import org.bukkit.Bukkit;
import com.sk89q.worldguard.session.Session;
import org.bukkit.plugin.PluginManager;
import com.sk89q.worldguard.session.handler.Handler;

public class WGHandler extends Handler
{
    public final PluginManager pm;
    public static final Factory factory;
    
    public WGHandler(final Session session) {
        super(session);
        this.pm = Bukkit.getPluginManager();
    }
    
    public boolean onCrossBoundary(final LocalPlayer player, final Location from, final Location to, final ApplicableRegionSet toSet, final Set<ProtectedRegion> entered, final Set<ProtectedRegion> left, final MoveType moveType) {
        final RegionsChangedEvent rce = new RegionsChangedEvent(player.getUniqueId(), left, entered);
        this.pm.callEvent((Event)rce);
        if (rce.isCancelled()) {
            return false;
        }
        final RegionsEnteredEvent ree = new RegionsEnteredEvent(player.getUniqueId(), entered);
        this.pm.callEvent((Event)ree);
        if (ree.isCancelled()) {
            return false;
        }
        final RegionsLeftEvent rle = new RegionsLeftEvent(player.getUniqueId(), left);
        this.pm.callEvent((Event)rle);
        if (rle.isCancelled()) {
            return false;
        }
        for (final ProtectedRegion r : entered) {
            final RegionEnteredEvent regentered = new RegionEnteredEvent(player.getUniqueId(), r);
            this.pm.callEvent((Event)regentered);
            if (regentered.isCancelled()) {
                return false;
            }
        }
        for (final ProtectedRegion r : left) {
            final RegionLeftEvent regleft = new RegionLeftEvent(player.getUniqueId(), r);
            this.pm.callEvent((Event)regleft);
            if (regleft.isCancelled()) {
                return false;
            }
        }
        return true;
    }
    
    static {
        factory = new Factory();
    }
    
    public static class Factory extends Handler.Factory<WGHandler>
    {
        public WGHandler create(final Session session) {
            return new WGHandler(session);
        }
    }
}
