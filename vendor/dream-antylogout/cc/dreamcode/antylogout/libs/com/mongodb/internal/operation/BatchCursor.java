package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.com.mongodb.ServerCursor;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import java.io.Closeable;
import java.util.List;
import java.util.Iterator;

@NotThreadSafe
public interface BatchCursor<T> extends Iterator<List<T>>, Closeable
{
    void close();
    
    boolean hasNext();
    
    List<T> next();
    
    int available();
    
    void setBatchSize(final int p0);
    
    int getBatchSize();
    
    @Nullable
    List<T> tryNext();
    
    @Nullable
    ServerCursor getServerCursor();
    
    ServerAddress getServerAddress();
}
