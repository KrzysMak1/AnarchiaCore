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

public class RegionsChangedEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancelled;
    private final UUID uuid;
    private final Set<ProtectedRegion> previousRegions;
    private final Set<ProtectedRegion> currentRegions;
    private final Set<String> previousRegionsNames;
    private final Set<String> currentRegionsNames;
    
    public RegionsChangedEvent(final UUID playerUUID, final Set<ProtectedRegion> previous, final Set<ProtectedRegion> current) {
        this.cancelled = false;
        this.previousRegions = (Set<ProtectedRegion>)new HashSet();
        this.currentRegions = (Set<ProtectedRegion>)new HashSet();
        this.previousRegionsNames = (Set<String>)new HashSet();
        this.currentRegionsNames = (Set<String>)new HashSet();
        this.uuid = playerUUID;
        this.previousRegions.addAll((Collection)previous);
        this.currentRegions.addAll((Collection)current);
        for (final ProtectedRegion r : current) {
            this.currentRegionsNames.add((Object)r.getId());
        }
        for (final ProtectedRegion r : previous) {
            this.previousRegionsNames.add((Object)r.getId());
        }
    }
    
    public static HandlerList getHandlerList() {
        return RegionsChangedEvent.handlers;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return RegionsChangedEvent.handlers;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegionsChangedEvent)) {
            return false;
        }
        final RegionsChangedEvent other = (RegionsChangedEvent)o;
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
        final Object this$previousRegions = this.getPreviousRegions();
        final Object other$previousRegions = other.getPreviousRegions();
        Label_0115: {
            if (this$previousRegions == null) {
                if (other$previousRegions == null) {
                    break Label_0115;
                }
            }
            else if (this$previousRegions.equals(other$previousRegions)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$currentRegions = this.getCurrentRegions();
        final Object other$currentRegions = other.getCurrentRegions();
        Label_0152: {
            if (this$currentRegions == null) {
                if (other$currentRegions == null) {
                    break Label_0152;
                }
            }
            else if (this$currentRegions.equals(other$currentRegions)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$previousRegionsNames = this.getPreviousRegionsNames();
        final Object other$previousRegionsNames = other.getPreviousRegionsNames();
        Label_0189: {
            if (this$previousRegionsNames == null) {
                if (other$previousRegionsNames == null) {
                    break Label_0189;
                }
            }
            else if (this$previousRegionsNames.equals(other$previousRegionsNames)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$currentRegionsNames = this.getCurrentRegionsNames();
        final Object other$currentRegionsNames = other.getCurrentRegionsNames();
        if (this$currentRegionsNames == null) {
            if (other$currentRegionsNames == null) {
                return true;
            }
        }
        else if (this$currentRegionsNames.equals(other$currentRegionsNames)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof RegionsChangedEvent;
    }
    
    @Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        final Object $uuid = this.getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        final Object $previousRegions = this.getPreviousRegions();
        result = result * 59 + (($previousRegions == null) ? 43 : $previousRegions.hashCode());
        final Object $currentRegions = this.getCurrentRegions();
        result = result * 59 + (($currentRegions == null) ? 43 : $currentRegions.hashCode());
        final Object $previousRegionsNames = this.getPreviousRegionsNames();
        result = result * 59 + (($previousRegionsNames == null) ? 43 : $previousRegionsNames.hashCode());
        final Object $currentRegionsNames = this.getCurrentRegionsNames();
        result = result * 59 + (($currentRegionsNames == null) ? 43 : $currentRegionsNames.hashCode());
        return result;
    }
    
    @Generated
    public UUID getUuid() {
        return this.uuid;
    }
    
    @Generated
    public Set<ProtectedRegion> getPreviousRegions() {
        return this.previousRegions;
    }
    
    @Generated
    public Set<ProtectedRegion> getCurrentRegions() {
        return this.currentRegions;
    }
    
    @Generated
    public Set<String> getPreviousRegionsNames() {
        return this.previousRegionsNames;
    }
    
    @Generated
    public Set<String> getCurrentRegionsNames() {
        return this.currentRegionsNames;
    }
    
    @Generated
    public String toString() {
        return "RegionsChangedEvent(cancelled=" + this.isCancelled() + ", uuid=" + String.valueOf((Object)this.getUuid()) + ", previousRegions=" + String.valueOf((Object)this.getPreviousRegions()) + ", currentRegions=" + String.valueOf((Object)this.getCurrentRegions()) + ", previousRegionsNames=" + String.valueOf((Object)this.getPreviousRegionsNames()) + ", currentRegionsNames=" + String.valueOf((Object)this.getCurrentRegionsNames());
    }
    
    static {
        handlers = new HandlerList();
    }
}
