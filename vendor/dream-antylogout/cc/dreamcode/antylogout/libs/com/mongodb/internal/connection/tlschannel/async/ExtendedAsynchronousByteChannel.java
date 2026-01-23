package cc.dreamcode.antylogout.libs.com.mongodb.internal.connection.tlschannel.async;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;

public interface ExtendedAsynchronousByteChannel extends AsynchronousByteChannel
{
     <A> void read(final ByteBuffer p0, final long p1, final TimeUnit p2, final A p3, final CompletionHandler<Integer, ? super A> p4);
    
     <A> void read(final ByteBuffer[] p0, final int p1, final int p2, final long p3, final TimeUnit p4, final A p5, final CompletionHandler<Long, ? super A> p6);
    
     <A> void write(final ByteBuffer p0, final long p1, final TimeUnit p2, final A p3, final CompletionHandler<Integer, ? super A> p4);
    
     <A> void write(final ByteBuffer[] p0, final int p1, final int p2, final long p3, final TimeUnit p4, final A p5, final CompletionHandler<Long, ? super A> p6);
}
