package cc.dreamcode.antylogout.hook.worldguard.extension.event;

import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class RegionEnteredEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancelled;
    private final UUID uuid;
    private final ProtectedRegion region;
    private final String regionName;
    
    public RegionEnteredEvent(final UUID playerUUID, final ProtectedRegion region) {
        this.cancelled = false;
        this.uuid = playerUUID;
        this.region = region;
        this.regionName = region.getId();
    }
    
    public static HandlerList getHandlerList() {
        return RegionEnteredEvent.handlers;
    }
    
    @NotNull
    public HandlerList getHandlers() {
        return RegionEnteredEvent.handlers;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
    
    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegionEnteredEvent)) {
            return false;
        }
        final RegionEnteredEvent other = (RegionEnteredEvent)o;
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
        final Object this$region = this.getRegion();
        final Object other$region = other.getRegion();
        Label_0115: {
            if (this$region == null) {
                if (other$region == null) {
                    break Label_0115;
                }
            }
            else if (this$region.equals(other$region)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$regionName = this.getRegionName();
        final Object other$regionName = other.getRegionName();
        if (this$regionName == null) {
            if (other$regionName == null) {
                return true;
            }
        }
        else if (this$regionName.equals(other$regionName)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof RegionEnteredEvent;
    }
    
    @Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        final Object $uuid = this.getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        final Object $region = this.getRegion();
        result = result * 59 + (($region == null) ? 43 : $region.hashCode());
        final Object $regionName = this.getRegionName();
        result = result * 59 + (($regionName == null) ? 43 : $regionName.hashCode());
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
    public ProtectedRegion getRegion() {
        return this.region;
    }
    
    @Generated
    public String getRegionName() {
        return this.regionName;
    }
    
    @Generated
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Generated
    public String toString() {
        return "RegionEnteredEvent(cancelled=" + this.isCancelled() + ", uuid=" + String.valueOf((Object)this.getUuid()) + ", region=" + String.valueOf((Object)this.getRegion()) + ", regionName=" + this.getRegionName();
    }
    
    static {
        handlers = new HandlerList();
    }
}
