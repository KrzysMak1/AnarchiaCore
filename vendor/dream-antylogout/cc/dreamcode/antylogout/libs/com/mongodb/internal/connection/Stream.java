package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection;

import cc.dreamcode.antylogout.libs.com.mongodb.ServerAddress;
import cc.dreamcode.antylogout.libs.org.bson.ByteBuf;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.AsyncCompletionHandler;
import java.io.IOException;

public interface Stream extends BufferProvider
{
    void open() throws IOException;
    
    void openAsync(final AsyncCompletionHandler<Void> p0);
    
    void write(final List<ByteBuf> p0) throws IOException;
    
    ByteBuf read(final int p0) throws IOException;
    
    ByteBuf read(final int p0, final int p1) throws IOException;
    
    void writeAsync(final List<ByteBuf> p0, final AsyncCompletionHandler<Void> p1);
    
    void readAsync(final int p0, final AsyncCompletionHandler<ByteBuf> p1);
    
    ServerAddress getAddress();
    
    void close();
    
    boolean isClosed();
}
