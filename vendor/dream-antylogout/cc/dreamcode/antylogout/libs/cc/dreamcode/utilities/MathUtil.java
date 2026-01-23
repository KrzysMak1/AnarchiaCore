package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import lombok.NonNull;
import java.time.Instant;
import java.time.Duration;

public final class MathUtil
{
    public static double round(final double number, final int places) {
        final double factor = Math.pow(10.0, (double)places);
        return Math.round(number * factor) / factor;
    }
    
    public static Duration difference(final long startEpochMilli, final long timeInMills) {
        return Duration.ofMillis(startEpochMilli + timeInMills - Instant.now().toEpochMilli());
    }
    
    public static Duration difference(final long startEpochMilli, @NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return difference(startEpochMilli, duration.toMillis());
    }
    
    public static Duration difference(@NonNull final Instant instant, final long timeInMills) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        return Duration.ofMillis(instant.toEpochMilli() + timeInMills - Instant.now().toEpochMilli());
    }
    
    public static Duration difference(@NonNull final Instant instant, @NonNull final Duration duration) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return difference(instant, duration.toMillis());
    }
    
    public static boolean isNegative(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return duration.isZero() || duration.isNegative();
    }
    
    @Generated
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
