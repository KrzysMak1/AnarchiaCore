package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.teleport;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.MathUtil;
import java.util.function.Consumer;
import org.bukkit.entity.HumanEntity;
import java.util.function.BiConsumer;
import java.time.Duration;
import java.time.Instant;

public class QueuedTeleport
{
    private final Instant instant;
    private final Duration duration;
    private final boolean cancelOnMove;
    private final BiConsumer<HumanEntity, Duration> countdownNotice;
    private final Consumer<HumanEntity> movedNotice;
    private final Consumer<HumanEntity> alreadyInAction;
    private final Consumer<HumanEntity> taskAfter;
    
    public Duration getCountdown() {
        return MathUtil.difference(this.instant, this.duration);
    }
    
    @Generated
    public Instant getInstant() {
        return this.instant;
    }
    
    @Generated
    public Duration getDuration() {
        return this.duration;
    }
    
    @Generated
    public boolean isCancelOnMove() {
        return this.cancelOnMove;
    }
    
    @Generated
    public BiConsumer<HumanEntity, Duration> getCountdownNotice() {
        return this.countdownNotice;
    }
    
    @Generated
    public Consumer<HumanEntity> getMovedNotice() {
        return this.movedNotice;
    }
    
    @Generated
    public Consumer<HumanEntity> getAlreadyInAction() {
        return this.alreadyInAction;
    }
    
    @Generated
    public Consumer<HumanEntity> getTaskAfter() {
        return this.taskAfter;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof QueuedTeleport)) {
            return false;
        }
        final QueuedTeleport other = (QueuedTeleport)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isCancelOnMove() != other.isCancelOnMove()) {
            return false;
        }
        final Object this$instant = this.getInstant();
        final Object other$instant = other.getInstant();
        Label_0078: {
            if (this$instant == null) {
                if (other$instant == null) {
                    break Label_0078;
                }
            }
            else if (this$instant.equals(other$instant)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$duration = this.getDuration();
        final Object other$duration = other.getDuration();
        Label_0115: {
            if (this$duration == null) {
                if (other$duration == null) {
                    break Label_0115;
                }
            }
            else if (this$duration.equals(other$duration)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$countdownNotice = this.getCountdownNotice();
        final Object other$countdownNotice = other.getCountdownNotice();
        Label_0152: {
            if (this$countdownNotice == null) {
                if (other$countdownNotice == null) {
                    break Label_0152;
                }
            }
            else if (this$countdownNotice.equals(other$countdownNotice)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$movedNotice = this.getMovedNotice();
        final Object other$movedNotice = other.getMovedNotice();
        Label_0189: {
            if (this$movedNotice == null) {
                if (other$movedNotice == null) {
                    break Label_0189;
                }
            }
            else if (this$movedNotice.equals(other$movedNotice)) {
                break Label_0189;
            }
            return false;
        }
        final Object this$alreadyInAction = this.getAlreadyInAction();
        final Object other$alreadyInAction = other.getAlreadyInAction();
        Label_0226: {
            if (this$alreadyInAction == null) {
                if (other$alreadyInAction == null) {
                    break Label_0226;
                }
            }
            else if (this$alreadyInAction.equals(other$alreadyInAction)) {
                break Label_0226;
            }
            return false;
        }
        final Object this$taskAfter = this.getTaskAfter();
        final Object other$taskAfter = other.getTaskAfter();
        if (this$taskAfter == null) {
            if (other$taskAfter == null) {
                return true;
            }
        }
        else if (this$taskAfter.equals(other$taskAfter)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof QueuedTeleport;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelOnMove() ? 79 : 97);
        final Object $instant = this.getInstant();
        result = result * 59 + (($instant == null) ? 43 : $instant.hashCode());
        final Object $duration = this.getDuration();
        result = result * 59 + (($duration == null) ? 43 : $duration.hashCode());
        final Object $countdownNotice = this.getCountdownNotice();
        result = result * 59 + (($countdownNotice == null) ? 43 : $countdownNotice.hashCode());
        final Object $movedNotice = this.getMovedNotice();
        result = result * 59 + (($movedNotice == null) ? 43 : $movedNotice.hashCode());
        final Object $alreadyInAction = this.getAlreadyInAction();
        result = result * 59 + (($alreadyInAction == null) ? 43 : $alreadyInAction.hashCode());
        final Object $taskAfter = this.getTaskAfter();
        result = result * 59 + (($taskAfter == null) ? 43 : $taskAfter.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "QueuedTeleport(instant=" + (Object)this.getInstant() + ", duration=" + (Object)this.getDuration() + ", cancelOnMove=" + this.isCancelOnMove() + ", countdownNotice=" + (Object)this.getCountdownNotice() + ", movedNotice=" + (Object)this.getMovedNotice() + ", alreadyInAction=" + (Object)this.getAlreadyInAction() + ", taskAfter=" + (Object)this.getTaskAfter() + ")";
    }
    
    @Generated
    public QueuedTeleport(final Instant instant, final Duration duration, final boolean cancelOnMove, final BiConsumer<HumanEntity, Duration> countdownNotice, final Consumer<HumanEntity> movedNotice, final Consumer<HumanEntity> alreadyInAction, final Consumer<HumanEntity> taskAfter) {
        this.instant = instant;
        this.duration = duration;
        this.cancelOnMove = cancelOnMove;
        this.countdownNotice = countdownNotice;
        this.movedNotice = movedNotice;
        this.alreadyInAction = alreadyInAction;
        this.taskAfter = taskAfter;
    }
}
