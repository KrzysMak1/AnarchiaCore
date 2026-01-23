package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.countdown;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.MathUtil;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Countdown
{
    private final UUID uuid;
    private final Instant start;
    private final Duration duration;
    private final Runnable runnable;
    
    public Countdown(final UUID uuid, final Instant start, final Duration duration) {
        this.uuid = uuid;
        this.start = start;
        this.duration = duration;
        this.runnable = (() -> {});
    }
    
    public boolean isComplete() {
        return MathUtil.isNegative(MathUtil.difference(this.start, this.duration));
    }
    
    @Generated
    public UUID getUuid() {
        return this.uuid;
    }
    
    @Generated
    public Instant getStart() {
        return this.start;
    }
    
    @Generated
    public Duration getDuration() {
        return this.duration;
    }
    
    @Generated
    public Runnable getRunnable() {
        return this.runnable;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Countdown)) {
            return false;
        }
        final Countdown other = (Countdown)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        Label_0065: {
            if (this$uuid == null) {
                if (other$uuid == null) {
                    break Label_0065;
                }
            }
            else if (this$uuid.equals(other$uuid)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$start = this.getStart();
        final Object other$start = other.getStart();
        Label_0102: {
            if (this$start == null) {
                if (other$start == null) {
                    break Label_0102;
                }
            }
            else if (this$start.equals(other$start)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$duration = this.getDuration();
        final Object other$duration = other.getDuration();
        Label_0139: {
            if (this$duration == null) {
                if (other$duration == null) {
                    break Label_0139;
                }
            }
            else if (this$duration.equals(other$duration)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$runnable = this.getRunnable();
        final Object other$runnable = other.getRunnable();
        if (this$runnable == null) {
            if (other$runnable == null) {
                return true;
            }
        }
        else if (this$runnable.equals(other$runnable)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Countdown;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        final Object $start = this.getStart();
        result = result * 59 + (($start == null) ? 43 : $start.hashCode());
        final Object $duration = this.getDuration();
        result = result * 59 + (($duration == null) ? 43 : $duration.hashCode());
        final Object $runnable = this.getRunnable();
        result = result * 59 + (($runnable == null) ? 43 : $runnable.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Countdown(uuid=" + (Object)this.getUuid() + ", start=" + (Object)this.getStart() + ", duration=" + (Object)this.getDuration() + ", runnable=" + (Object)this.getRunnable() + ")";
    }
    
    @Generated
    public Countdown(final UUID uuid, final Instant start, final Duration duration, final Runnable runnable) {
        this.uuid = uuid;
        this.start = start;
        this.duration = duration;
        this.runnable = runnable;
    }
}
