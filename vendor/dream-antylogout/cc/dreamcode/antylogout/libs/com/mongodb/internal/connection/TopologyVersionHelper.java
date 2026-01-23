package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoCommandException;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.TopologyVersion;

final class TopologyVersionHelper
{
    static boolean newer(@Nullable final TopologyVersion current, @Nullable final TopologyVersion candidate) {
        return compare(current, candidate) > 0;
    }
    
    static boolean newerOrEqual(@Nullable final TopologyVersion current, @Nullable final TopologyVersion candidate) {
        return compare(current, candidate) >= 0;
    }
    
    static Optional<TopologyVersion> topologyVersion(@Nullable final Throwable t) {
        TopologyVersion result = null;
        if (t instanceof MongoCommandException) {
            final BsonDocument rawTopologyVersion = ((MongoCommandException)t).getResponse().getDocument("topologyVersion", null);
            if (rawTopologyVersion != null) {
                result = new TopologyVersion(rawTopologyVersion);
            }
        }
        return (Optional<TopologyVersion>)Optional.ofNullable((Object)result);
    }
    
    private static int compare(@Nullable final TopologyVersion o1, @Nullable final TopologyVersion o2) {
        if (o1 == null || o2 == null) {
            return -1;
        }
        if (o1.getProcessId().equals(o2.getProcessId())) {
            return Long.compare(o1.getCounter(), o2.getCounter());
        }
        return -1;
    }
    
    private TopologyVersionHelper() {
        throw new AssertionError();
    }
}
