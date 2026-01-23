package cc.dreamcode.antylogout.libs.com.mongodb.internal.selector;

import java.util.ArrayList;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterConnectionMode;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import java.util.concurrent.TimeUnit;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;

public class LatencyMinimizingServerSelector implements ServerSelector
{
    private final long acceptableLatencyDifferenceNanos;
    
    public LatencyMinimizingServerSelector(final long acceptableLatencyDifference, final TimeUnit timeUnit) {
        this.acceptableLatencyDifferenceNanos = TimeUnit.NANOSECONDS.convert(acceptableLatencyDifference, timeUnit);
    }
    
    public long getAcceptableLatencyDifference(final TimeUnit timeUnit) {
        return timeUnit.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS);
    }
    
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        if (clusterDescription.getConnectionMode() != ClusterConnectionMode.MULTIPLE) {
            return ClusterDescriptionHelper.getAny(clusterDescription);
        }
        return this.getServersWithAcceptableLatencyDifference(ClusterDescriptionHelper.getAny(clusterDescription), this.getFastestRoundTripTimeNanos(clusterDescription.getServerDescriptions()));
    }
    
    @Override
    public String toString() {
        return "LatencyMinimizingServerSelector{acceptableLatencyDifference=" + TimeUnit.MILLISECONDS.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS) + " ms" + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LatencyMinimizingServerSelector that = (LatencyMinimizingServerSelector)o;
        return this.acceptableLatencyDifferenceNanos == that.acceptableLatencyDifferenceNanos;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.acceptableLatencyDifferenceNanos ^ this.acceptableLatencyDifferenceNanos >>> 32);
    }
    
    private long getFastestRoundTripTimeNanos(final List<ServerDescription> members) {
        long fastestRoundTripTime = Long.MAX_VALUE;
        for (final ServerDescription cur : members) {
            if (!cur.isOk()) {
                continue;
            }
            if (cur.getRoundTripTimeNanos() >= fastestRoundTripTime) {
                continue;
            }
            fastestRoundTripTime = cur.getRoundTripTimeNanos();
        }
        return fastestRoundTripTime;
    }
    
    private List<ServerDescription> getServersWithAcceptableLatencyDifference(final List<ServerDescription> servers, final long bestPingTime) {
        final List<ServerDescription> goodSecondaries = (List<ServerDescription>)new ArrayList(servers.size());
        for (final ServerDescription cur : servers) {
            if (!cur.isOk()) {
                continue;
            }
            if (cur.getRoundTripTimeNanos() - this.acceptableLatencyDifferenceNanos > bestPingTime) {
                continue;
            }
            goodSecondaries.add((Object)cur);
        }
        return goodSecondaries;
    }
}
