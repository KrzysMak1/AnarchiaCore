package cc.dreamcode.antylogout.libs.com.mongodb.internal.time;

import java.time.Duration;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class TimePoint implements Comparable<TimePoint>
{
    private final long nanos;
    
    private TimePoint(final long nanos) {
        this.nanos = nanos;
    }
    
    public static TimePoint now() {
        return at(System.nanoTime());
    }
    
    static TimePoint at(final long nanos) {
        return new TimePoint(nanos);
    }
    
    public Duration durationSince(final TimePoint t) {
        return Duration.ofNanos(this.nanos - t.nanos);
    }
    
    public Duration elapsed() {
        return Duration.ofNanos(System.nanoTime() - this.nanos);
    }
    
    public TimePoint add(final Duration duration) {
        final long durationNanos = duration.toNanos();
        return at(this.nanos + durationNanos);
    }
    
    public int compareTo(final TimePoint t) {
        return Long.signum(this.nanos - t.nanos);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final TimePoint timePoint = (TimePoint)o;
        return this.nanos == timePoint.nanos;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(this.nanos);
    }
    
    @Override
    public String toString() {
        return "TimePoint{nanos=" + this.nanos + '}';
    }
}
