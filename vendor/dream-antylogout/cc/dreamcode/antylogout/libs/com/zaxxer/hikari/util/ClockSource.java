package cc.dreamcode.antylogout.libs.com.zaxxer.hikari.util;

import java.util.concurrent.TimeUnit;

public interface ClockSource
{
    public static final ClockSource CLOCK = Factory.create();
    public static final TimeUnit[] TIMEUNITS_DESCENDING = { TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS, TimeUnit.MILLISECONDS, TimeUnit.MICROSECONDS, TimeUnit.NANOSECONDS };
    public static final String[] TIMEUNIT_DISPLAY_VALUES = { "ns", "µs", "ms", "s", "m", "h", "d" };
    
    default long currentTime() {
        return ClockSource.CLOCK.currentTime0();
    }
    
    long currentTime0();
    
    default long toMillis(final long time) {
        return ClockSource.CLOCK.toMillis0(time);
    }
    
    long toMillis0(final long p0);
    
    default long toNanos(final long time) {
        return ClockSource.CLOCK.toNanos0(time);
    }
    
    long toNanos0(final long p0);
    
    default long elapsedMillis(final long startTime) {
        return ClockSource.CLOCK.elapsedMillis0(startTime);
    }
    
    long elapsedMillis0(final long p0);
    
    default long elapsedMillis(final long startTime, final long endTime) {
        return ClockSource.CLOCK.elapsedMillis0(startTime, endTime);
    }
    
    long elapsedMillis0(final long p0, final long p1);
    
    default long elapsedNanos(final long startTime) {
        return ClockSource.CLOCK.elapsedNanos0(startTime);
    }
    
    long elapsedNanos0(final long p0);
    
    default long elapsedNanos(final long startTime, final long endTime) {
        return ClockSource.CLOCK.elapsedNanos0(startTime, endTime);
    }
    
    long elapsedNanos0(final long p0, final long p1);
    
    default long plusMillis(final long time, final long millis) {
        return ClockSource.CLOCK.plusMillis0(time, millis);
    }
    
    long plusMillis0(final long p0, final long p1);
    
    default TimeUnit getSourceTimeUnit() {
        return ClockSource.CLOCK.getSourceTimeUnit0();
    }
    
    TimeUnit getSourceTimeUnit0();
    
    default String elapsedDisplayString(final long startTime, final long endTime) {
        return ClockSource.CLOCK.elapsedDisplayString0(startTime, endTime);
    }
    
    default String elapsedDisplayString0(final long startTime, final long endTime) {
        long elapsedNanos = this.elapsedNanos0(startTime, endTime);
        final StringBuilder sb = new StringBuilder((elapsedNanos < 0L) ? "-" : "");
        elapsedNanos = Math.abs(elapsedNanos);
        for (final TimeUnit unit : ClockSource.TIMEUNITS_DESCENDING) {
            final long converted = unit.convert(elapsedNanos, TimeUnit.NANOSECONDS);
            if (converted > 0L) {
                sb.append(converted).append(ClockSource.TIMEUNIT_DISPLAY_VALUES[unit.ordinal()]);
                elapsedNanos -= TimeUnit.NANOSECONDS.convert(converted, unit);
            }
        }
        return sb.toString();
    }
    
    public static class Factory
    {
        private static ClockSource create() {
            final String os = System.getProperty("os.name");
            if ("Mac OS X".equals((Object)os)) {
                return new MillisecondClockSource();
            }
            return new NanosecondClockSource();
        }
    }
    
    public static final class MillisecondClockSource implements ClockSource
    {
        @Override
        public long currentTime0() {
            return System.currentTimeMillis();
        }
        
        @Override
        public long elapsedMillis0(final long startTime) {
            return System.currentTimeMillis() - startTime;
        }
        
        @Override
        public long elapsedMillis0(final long startTime, final long endTime) {
            return endTime - startTime;
        }
        
        @Override
        public long elapsedNanos0(final long startTime) {
            return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis() - startTime);
        }
        
        @Override
        public long elapsedNanos0(final long startTime, final long endTime) {
            return TimeUnit.MILLISECONDS.toNanos(endTime - startTime);
        }
        
        @Override
        public long toMillis0(final long time) {
            return time;
        }
        
        @Override
        public long toNanos0(final long time) {
            return TimeUnit.MILLISECONDS.toNanos(time);
        }
        
        @Override
        public long plusMillis0(final long time, final long millis) {
            return time + millis;
        }
        
        @Override
        public TimeUnit getSourceTimeUnit0() {
            return TimeUnit.MILLISECONDS;
        }
    }
    
    public static class NanosecondClockSource implements ClockSource
    {
        @Override
        public long currentTime0() {
            return System.nanoTime();
        }
        
        @Override
        public long toMillis0(final long time) {
            return TimeUnit.NANOSECONDS.toMillis(time);
        }
        
        @Override
        public long toNanos0(final long time) {
            return time;
        }
        
        @Override
        public long elapsedMillis0(final long startTime) {
            return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        }
        
        @Override
        public long elapsedMillis0(final long startTime, final long endTime) {
            return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        }
        
        @Override
        public long elapsedNanos0(final long startTime) {
            return System.nanoTime() - startTime;
        }
        
        @Override
        public long elapsedNanos0(final long startTime, final long endTime) {
            return endTime - startTime;
        }
        
        @Override
        public long plusMillis0(final long time, final long millis) {
            return time + TimeUnit.MILLISECONDS.toNanos(millis);
        }
        
        @Override
        public TimeUnit getSourceTimeUnit0() {
            return TimeUnit.NANOSECONDS;
        }
    }
}
