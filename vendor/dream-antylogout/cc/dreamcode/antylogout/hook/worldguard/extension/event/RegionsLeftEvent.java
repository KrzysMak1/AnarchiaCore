package cc.dreamcode.antylogout.hook.worldguard.extension.event;

import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class RegionsLeftEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancelled;
    private final UUID uuid;
    private final Set<ProtectedRegion> regions;
    private final Set<String> regionsNames;
    
    public RegionsLeftEvent(final UUID playerUUID, final Set<ProtectedRegion> regions) {
        this.cancelled = false;
        this.uuid = playerUUID;
        this.regionsNames = (Set<String>)new HashSet();
        this.regions = (Set<ProtectedRegion>)new HashSet();
        if (regions != null) {
            this.regions.addAll((Collection)regions);
            for (final ProtectedRegion region : regions) {
                this.regionsNames.add((Object)region.getId());
            }
        }
    }
    
    public static HandlerList getHandlerList() {
        return RegionsLeftEvent.handlers;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return RegionsLeftEvent.handlers;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegionsLeftEvent)) {
            return false;
        }
        final RegionsLeftEvent other = (RegionsLeftEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isCancelled() != other.isCancelled()) {
            return false;
        }
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        Label_0078: {
            if (this$uuid == null) {
                if (other$uuid == null) {
                    break Label_0078;
                }
            }
            else if (this$uuid.equals(other$uuid)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$regions = this.getRegions();
        final Object other$regions = other.getRegions();
        Label_0115: {
            if (this$regions == null) {
                if (other$regions == null) {
                    break Label_0115;
                }
            }
            else if (this$regions.equals(other$regions)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$regionsNames = this.getRegionsNames();
        final Object other$regionsNames = other.getRegionsNames();
        if (this$regionsNames == null) {
            if (other$regionsNames == null) {
                return true;
            }
        }
        else if (this$regionsNames.equals(other$regionsNames)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof RegionsLeftEvent;
    }
    
    @Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        final Object $uuid = this.getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        final Object $regions = this.getRegions();
        result = result * 59 + (($regions == null) ? 43 : $regions.hashCode());
        final Object $regionsNames = this.getRegionsNames();
        result = result * 59 + (($regionsNames == null) ? 43 : $regionsNames.hashCode());
        return result;
    }
    
    @Generated
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Generated
    public UUID getUuid() {
        return this.uuid;
    }
    
    @Generated
    public Set<ProtectedRegion> getRegions() {
        return this.regions;
    }
    
    @Generated
    public Set<String> getRegionsNames() {
        return this.regionsNames;
    }
    
    @Generated
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Generated
    public String toString() {
        return "RegionsLeftEvent(cancelled=" + this.isCancelled() + ", uuid=" + String.valueOf((Object)this.getUuid()) + ", regions=" + String.valueOf((Object)this.getRegions()) + ", regionsNames=" + String.valueOf((Object)this.getRegionsNames());
    }
    
    static {
        handlers = new HandlerList();
    }
}
