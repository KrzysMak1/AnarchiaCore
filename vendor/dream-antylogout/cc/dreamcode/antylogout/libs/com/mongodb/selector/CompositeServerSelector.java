package cc.dreamcode.antylogout.libs.com.mongodb.selector;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;

public final class CompositeServerSelector implements ServerSelector
{
    private final List<ServerSelector> serverSelectors;
    
    public CompositeServerSelector(final List<? extends ServerSelector> serverSelectors) {
        Assertions.notNull("serverSelectors", serverSelectors);
        if (serverSelectors.isEmpty()) {
            throw new IllegalArgumentException("Server selectors can not be an empty list");
        }
        final ArrayList<ServerSelector> mergedServerSelectors = (ArrayList<ServerSelector>)new ArrayList();
        for (final ServerSelector cur : serverSelectors) {
            if (cur == null) {
                throw new IllegalArgumentException("Can not have a null server selector in the list of composed selectors");
            }
            if (cur instanceof CompositeServerSelector) {
                mergedServerSelectors.addAll((Collection)((CompositeServerSelector)cur).serverSelectors);
            }
            else {
                mergedServerSelectors.add((Object)cur);
            }
        }
        this.serverSelectors = (List<ServerSelector>)Collections.unmodifiableList((List)mergedServerSelectors);
    }
    
    public List<ServerSelector> getServerSelectors() {
        return this.serverSelectors;
    }
    
    @Override
    public List<ServerDescription> select(final ClusterDescription clusterDescription) {
        ClusterDescription curClusterDescription = clusterDescription;
        List<ServerDescription> choices = null;
        for (final ServerSelector cur : this.serverSelectors) {
            choices = cur.select(curClusterDescription);
            curClusterDescription = new ClusterDescription(clusterDescription.getConnectionMode(), clusterDescription.getType(), choices, clusterDescription.getClusterSettings(), clusterDescription.getServerSettings());
        }
        return Assertions.assertNotNull(choices);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CompositeServerSelector that = (CompositeServerSelector)o;
        return this.serverSelectors.size() == that.serverSelectors.size() && this.serverSelectors.equals((Object)that.serverSelectors);
    }
    
    @Override
    public int hashCode() {
        return (this.serverSelectors != null) ? this.serverSelectors.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "CompositeServerSelector{serverSelectors=" + (Object)this.serverSelectors + '}';
    }
}
