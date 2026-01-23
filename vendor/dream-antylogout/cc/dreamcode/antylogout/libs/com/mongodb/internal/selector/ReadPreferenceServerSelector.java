package cc.dreamcode.antylogout.libs.com.mongodb.internal.selector;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterConnectionMode;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.ReadPreference;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;

public class ReadPreferenceServerSelector implements ServerSelector
{
    private final ReadPreference readPreference;
    
    public ReadPreferenceServerSelector(final ReadPreference readPreference) {
        this.readPreference = Assertions.notNull("readPreference", readPreference);
    }
    
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        if (clusterDescription.getConnectionMode() == ClusterConnectionMode.SINGLE) {
            return ClusterDescriptionHelper.getAny(clusterDescription);
        }
        return this.readPreference.choose(clusterDescription);
    }
    
    @Override
    public String toString() {
        return "ReadPreferenceServerSelector{readPreference=" + (Object)this.readPreference + '}';
    }
}
