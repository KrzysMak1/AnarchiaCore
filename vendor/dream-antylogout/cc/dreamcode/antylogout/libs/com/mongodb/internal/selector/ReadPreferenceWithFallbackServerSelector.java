package cc.dreamcode.antylogout.libs.com.mongodb.internal.selector;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerConnectionState;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;

public class ReadPreferenceWithFallbackServerSelector implements ServerSelector
{
    private final ReadPreference preferredReadPreference;
    private final int minWireVersion;
    private final ReadPreference fallbackReadPreference;
    private ReadPreference appliedReadPreference;
    
    public ReadPreferenceWithFallbackServerSelector(final ReadPreference preferredReadPreference, final int minWireVersion, final ReadPreference fallbackReadPreference) {
        this.preferredReadPreference = preferredReadPreference;
        this.minWireVersion = minWireVersion;
        this.fallbackReadPreference = fallbackReadPreference;
    }
    
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        if (this.clusterContainsOlderServers(clusterDescription)) {
            this.appliedReadPreference = this.fallbackReadPreference;
            return new ReadPreferenceServerSelector(this.fallbackReadPreference).select(clusterDescription);
        }
        this.appliedReadPreference = this.preferredReadPreference;
        return new ReadPreferenceServerSelector(this.preferredReadPreference).select(clusterDescription);
    }
    
    public ReadPreference getAppliedReadPreference() {
        return this.appliedReadPreference;
    }
    
    private boolean clusterContainsOlderServers(final ClusterDescription clusterDescription) {
        return clusterDescription.getServerDescriptions().stream().filter(serverDescription -> serverDescription.getState() == ServerConnectionState.CONNECTED).anyMatch(serverDescription -> serverDescription.getMaxWireVersion() < this.minWireVersion);
    }
    
    @Override
    public String toString() {
        return "ReadPreferenceWithFallbackServerSelector{preferredReadPreference=" + (Object)this.preferredReadPreference + ", fallbackReadPreference=" + (Object)this.fallbackReadPreference + ", minWireVersionForPreferred=" + this.minWireVersion + '}';
    }
}
