package cc.dreamcode.antylogout.libs.com.mongodb.internal.thread;

import java.net.SocketException;
import java.nio.channels.ClosedByInterruptException;
import java.net.SocketTimeoutException;
import java.io.InterruptedIOException;
import java.util.Optional;
import cc.dreamcode.antylogout.libs.com.mongodb.MongoInterruptedException;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;

public final class InterruptionUtil
{
    public static MongoInterruptedException interruptAndCreateMongoInterruptedException(@Nullable final String msg, @Nullable final InterruptedException cause) {
        Thread.currentThread().interrupt();
        return new MongoInterruptedException(msg, (Exception)cause);
    }
    
    public static Optional<MongoInterruptedException> translateInterruptedException(@Nullable final Throwable e, @Nullable final String message) {
        if (e instanceof InterruptedException) {
            return (Optional<MongoInterruptedException>)Optional.of((Object)interruptAndCreateMongoInterruptedException(message, (InterruptedException)e));
        }
        if ((e instanceof InterruptedIOException && !(e instanceof SocketTimeoutException)) || e instanceof ClosedByInterruptException || (e instanceof SocketException && Thread.currentThread().isInterrupted())) {
            return (Optional<MongoInterruptedException>)Optional.of((Object)new MongoInterruptedException(message, (Exception)e));
        }
        return (Optional<MongoInterruptedException>)Optional.empty();
    }
    
    private InterruptionUtil() {
    }
}
