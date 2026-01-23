package cc.dreamcode.antylogout.libs.com.mongodb.selector;

import cc.dreamcode.antylogout.libs.com.mongodb.connection.ServerDescription;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterDescription;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public interface ServerSelector
{
    List<ServerDescription> select(final ClusterDescription p0);
}
