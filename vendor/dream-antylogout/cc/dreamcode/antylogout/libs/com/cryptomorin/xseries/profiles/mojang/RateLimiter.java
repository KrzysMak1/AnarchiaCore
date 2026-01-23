package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.mojang;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class RateLimiter
{
    private final ConcurrentLinkedQueue<Long> requests;
    private final int maxRequests;
    private final long per;
    
    RateLimiter(final int maxRequests, final Duration per) {
        this.requests = (ConcurrentLinkedQueue<Long>)new ConcurrentLinkedQueue();
        this.maxRequests = maxRequests;
        this.per = per.toMillis();
    }
    
    @CanIgnoreReturnValue
    private ConcurrentLinkedQueue<Long> getRequests() {
        if (this.requests.isEmpty()) {
            return this.requests;
        }
        final long now = System.currentTimeMillis();
        final Iterator<Long> iter = (Iterator<Long>)this.requests.iterator();
        while (iter.hasNext()) {
            final long requestedAt = (long)iter.next();
            final long diff = now - requestedAt;
            if (diff <= this.per) {
                break;
            }
            iter.remove();
        }
        return this.requests;
    }
    
    public int getRemainingRequests() {
        return Math.max(0, this.maxRequests - this.getRequests().size());
    }
    
    public int getEffectiveRequestsCount() {
        return this.getRequests().size();
    }
    
    public void instantRateLimit() {
        final long now = System.currentTimeMillis();
        for (int i = 0; i < this.getRemainingRequests(); ++i) {
            this.requests.add((Object)now);
        }
    }
    
    public boolean acquire() {
        if (this.getRemainingRequests() <= 0) {
            return false;
        }
        this.requests.add((Object)System.currentTimeMillis());
        return true;
    }
    
    public Duration timeUntilNextFreeRequest() {
        if (this.getRemainingRequests() == 0) {
            final long now = System.currentTimeMillis();
            final long oldestRequestedAt = (long)this.requests.peek();
            final long diff = now - oldestRequestedAt;
            return Duration.ofMillis(this.per - diff);
        }
        return Duration.ZERO;
    }
    
    public synchronized void acquireOrWait() {
        final long sleepUntil = this.timeUntilNextFreeRequest().toMillis();
        if (sleepUntil == 0L) {
            return;
        }
        try {
            Thread.sleep(sleepUntil);
        }
        catch (final InterruptedException e) {
            throw new IllegalStateException("RateLimiter lock was interrupted unexpectedly", (Throwable)e);
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[total=" + this.getRequests().size() + ", remaining=" + this.getRemainingRequests() + ", maxRequests=" + this.maxRequests + ", per=" + this.per + ']';
    }
}
