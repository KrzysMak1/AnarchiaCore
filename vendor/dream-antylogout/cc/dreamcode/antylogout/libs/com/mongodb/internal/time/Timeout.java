package cc.dreamcode.antylogout.libs.com.mongodb.internal.time;

import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class Timeout
{
    private static final Timeout INFINITE;
    private static final Timeout IMMEDIATE;
    private final long durationNanos;
    @Nullable
    private final TimePoint start;
    
    private Timeout(final long durationNanos, @Nullable final TimePoint start) {
        this.durationNanos = durationNanos;
        this.start = start;
    }
    
    public static Timeout started(final long duration, final TimeUnit unit, final TimePoint at) {
        return started(unit.toNanos(duration), Assertions.assertNotNull(at));
    }
    
    public static Timeout started(final long durationNanos, final TimePoint at) {
        if (durationNanos < 0L || durationNanos == Long.MAX_VALUE) {
            return infinite();
        }
        if (durationNanos == 0L) {
            return immediate();
        }
        return new Timeout(durationNanos, Assertions.assertNotNull(at));
    }
    
    public static Timeout startNow(final long duration, final TimeUnit unit) {
        return started(duration, unit, TimePoint.now());
    }
    
    public static Timeout startNow(final long durationNanos) {
        return started(durationNanos, TimePoint.now());
    }
    
    public static Timeout infinite() {
        return Timeout.INFINITE;
    }
    
    public static Timeout immediate() {
        return Timeout.IMMEDIATE;
    }
    
    long remainingNanos(final TimePoint now) {
        return Math.max(0L, this.durationNanos - now.durationSince(Assertions.assertNotNull(this.start)).toNanos());
    }
    
    public long remaining(final TimeUnit unit) {
        Assertions.assertFalse(this.isInfinite());
        return this.isImmediate() ? 0L : convertRoundUp(this.remainingNanos(TimePoint.now()), unit);
    }
    
    public long remainingOrInfinite(final TimeUnit unit) {
        return this.isInfinite() ? -1L : this.remaining(unit);
    }
    
    public boolean expired() {
        return expired(this.remainingOrInfinite(TimeUnit.NANOSECONDS));
    }
    
    public static boolean expired(final long remaining) {
        return remaining == 0L;
    }
    
    public boolean isInfinite() {
        return this.equals(Timeout.INFINITE);
    }
    
    public boolean isImmediate() {
        return this.equals(Timeout.IMMEDIATE);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Timeout other = (Timeout)o;
        return this.durationNanos == other.durationNanos && Objects.equals((Object)this.start, (Object)other.start());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.durationNanos, this.start });
    }
    
    @Override
    public String toString() {
        return "Timeout{durationNanos=" + this.durationNanos + ", start=" + (Object)this.start + '}';
    }
    
    public String toUserString() {
        if (this.isInfinite()) {
            return "infinite";
        }
        if (this.isImmediate()) {
            return "0 ms (immediate)";
        }
        return convertRoundUp(this.durationNanos, TimeUnit.MILLISECONDS) + " ms";
    }
    
    long durationNanos() {
        return this.durationNanos;
    }
    
    @Nullable
    TimePoint start() {
        return this.start;
    }
    
    static long convertRoundUp(final long nonNegativeNanos, final TimeUnit unit) {
        Assertions.assertTrue(nonNegativeNanos >= 0L);
        if (unit == TimeUnit.NANOSECONDS) {
            return nonNegativeNanos;
        }
        final long trimmed = unit.convert(nonNegativeNanos, TimeUnit.NANOSECONDS);
        return (TimeUnit.NANOSECONDS.convert(trimmed, unit) < nonNegativeNanos) ? (trimmed + 1L) : trimmed;
    }
    
    static {
        INFINITE = new Timeout(-1L, null);
        IMMEDIATE = new Timeout(0L, null);
    }
}
