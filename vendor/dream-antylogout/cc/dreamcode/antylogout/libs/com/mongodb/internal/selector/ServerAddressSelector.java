package cc.dreamcode.antylogout.libs.com.mongodb.internal.selector;

import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.ClusterDescriptionHelper;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.selector.ServerSelector;

public class ServerAddressSelector implements ServerSelector
{
    private final ServerAddress serverAddress;
    
    public ServerAddressSelector(final ServerAddress serverAddress) {
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
    }
    
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        final ServerDescription serverDescription = ClusterDescriptionHelper.getByServerAddress(clusterDescription, this.serverAddress);
        if (serverDescription != null) {
            return (List<ServerDescription>)Collections.singletonList((Object)serverDescription);
        }
        return (List<ServerDescription>)Collections.emptyList();
    }
    
    @Override
    public String toString() {
        return "ServerAddressSelector{serverAddress=" + (Object)this.serverAddress + '}';
    }
}
