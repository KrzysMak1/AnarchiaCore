package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import lombok.NonNull;
import java.time.Instant;

public final class DateUtil
{
    public static String format(@NonNull final Instant instant) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        return format(instant, ZoneId.of("Poland"));
    }
    
    public static String format(@NonNull final Instant instant, @NonNull final ZoneId zoneId) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        return format(instant, zoneId, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static String format(@NonNull final Instant instant, @NonNull final ZoneId zoneId, @NonNull final String pattern) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        if (pattern == null) {
            throw new NullPointerException("pattern is marked non-null but is null");
        }
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }
    
    public static String formatOnlyDate(@NonNull final Instant instant) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        return formatOnlyDate(instant, ZoneId.of("Poland"));
    }
    
    public static String formatOnlyDate(@NonNull final Instant instant, @NonNull final ZoneId zoneId) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        return formatOnlyDate(instant, zoneId, "yyyy-MM-dd");
    }
    
    public static String formatOnlyDate(@NonNull final Instant instant, @NonNull final ZoneId zoneId, @NonNull final String pattern) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        if (pattern == null) {
            throw new NullPointerException("pattern is marked non-null but is null");
        }
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }
    
    public static String formatOnlyTime(@NonNull final Instant instant) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        return formatOnlyTime(instant, ZoneId.of("Poland"));
    }
    
    public static String formatOnlyTime(@NonNull final Instant instant, @NonNull final ZoneId zoneId) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        return formatOnlyTime(instant, zoneId, "HH:mm:ss");
    }
    
    public static String formatOnlyTime(@NonNull final Instant instant, @NonNull final ZoneId zoneId, @NonNull final String pattern) {
        if (instant == null) {
            throw new NullPointerException("instant is marked non-null but is null");
        }
        if (zoneId == null) {
            throw new NullPointerException("zoneId is marked non-null but is null");
        }
        if (pattern == null) {
            throw new NullPointerException("pattern is marked non-null but is null");
        }
        final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }
    
    @Generated
    private DateUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
