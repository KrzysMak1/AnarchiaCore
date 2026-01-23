package cc.dreamcode.antylogout.cuboid.extension.event;

import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class CuboidEnteredEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private final Player player;
    private boolean cancelled;
    private final Location minimumLocation;
    private final Location maximumLocation;
    
    @NotNull
    public HandlerList getHandlers() {
        return CuboidEnteredEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
    
    public static HandlerList getHandlerList() {
        return CuboidEnteredEvent.handlers;
    }
    
    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CuboidEnteredEvent)) {
            return false;
        }
        final CuboidEnteredEvent other = (CuboidEnteredEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isCancelled() != other.isCancelled()) {
            return false;
        }
        final Object this$player = this.getPlayer();
        final Object other$player = other.getPlayer();
        Label_0078: {
            if (this$player == null) {
                if (other$player == null) {
                    break Label_0078;
                }
            }
            else if (this$player.equals(other$player)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$minimumLocation = this.getMinimumLocation();
        final Object other$minimumLocation = other.getMinimumLocation();
        Label_0115: {
            if (this$minimumLocation == null) {
                if (other$minimumLocation == null) {
                    break Label_0115;
                }
            }
            else if (this$minimumLocation.equals(other$minimumLocation)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$maximumLocation = this.getMaximumLocation();
        final Object other$maximumLocation = other.getMaximumLocation();
        if (this$maximumLocation == null) {
            if (other$maximumLocation == null) {
                return true;
            }
        }
        else if (this$maximumLocation.equals(other$maximumLocation)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CuboidEnteredEvent;
    }
    
    @Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        final Object $player = this.getPlayer();
        result = result * 59 + (($player == null) ? 43 : $player.hashCode());
        final Object $minimumLocation = this.getMinimumLocation();
        result = result * 59 + (($minimumLocation == null) ? 43 : $minimumLocation.hashCode());
        final Object $maximumLocation = this.getMaximumLocation();
        result = result * 59 + (($maximumLocation == null) ? 43 : $maximumLocation.hashCode());
        return result;
    }
    
    @Generated
    public CuboidEnteredEvent(final Player player, final Location minimumLocation, final Location maximumLocation) {
        this.cancelled = false;
        this.player = player;
        this.minimumLocation = minimumLocation;
        this.maximumLocation = maximumLocation;
    }
    
    @Generated
    public Player getPlayer() {
        return this.player;
    }
    
    @Generated
    public Location getMinimumLocation() {
        return this.minimumLocation;
    }
    
    @Generated
    public Location getMaximumLocation() {
        return this.maximumLocation;
    }
    
    @Generated
    public String toString() {
        return "CuboidEnteredEvent(player=" + String.valueOf((Object)this.getPlayer()) + ", cancelled=" + this.isCancelled() + ", minimumLocation=" + String.valueOf((Object)this.getMinimumLocation()) + ", maximumLocation=" + String.valueOf((Object)this.getMaximumLocation());
    }
    
    static {
        handlers = new HandlerList();
    }
}
