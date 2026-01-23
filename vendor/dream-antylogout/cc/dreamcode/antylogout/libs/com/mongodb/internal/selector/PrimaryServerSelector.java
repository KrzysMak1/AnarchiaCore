package cc.dreamcode.antylogout.libs.com.mongodb.internal.selector;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;

public final class PrimaryServerSelector implements ServerSelector
{
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        return ClusterDescriptionHelper.getPrimaries(clusterDescription);
    }
    
    @Override
    public String toString() {
        return "PrimaryServerSelector";
    }
}
