package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.time.Duration;
import lombok.NonNull;
import java.util.concurrent.TimeUnit;

public final class TicksUtil
{
    public static final int SECOND = 20;
    public static final int MINUTE = 1200;
    public static final int HOUR = 72000;
    public static final int DAY = 1728000;
    public static final int WEEK = 12096000;
    
    public static int ticksOf(@NonNull final TimeUnit timeUnit, final int value) {
        if (timeUnit == null) {
            throw new NullPointerException("timeUnit is marked non-null but is null");
        }
        return ticksOf(timeUnit.toMillis((long)value));
    }
    
    public static int ticksOf(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return ticksOf(duration, false);
    }
    
    public static int ticksOf(@NonNull final Duration duration, final boolean failsafe) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return ticksOf(duration.toMillis(), failsafe);
    }
    
    public static int ticksOf(final long millis) {
        return ticksOf(millis, false);
    }
    
    public static int ticksOf(final long millis, final boolean failsafe) {
        if (millis == 0L) {
            return 0;
        }
        if (millis >= 50L) {
            return Math.toIntExact(millis / 50L);
        }
        if (failsafe) {
            return 1;
        }
        throw new IllegalArgumentException("Cannot transform " + millis + " ms to ticks, too low value");
    }
    
    @Generated
    private TicksUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
