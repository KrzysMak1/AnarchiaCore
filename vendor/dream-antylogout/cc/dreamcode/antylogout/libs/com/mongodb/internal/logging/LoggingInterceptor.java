package cc.dreamcode.antylogout.libs.com.mongodb.internal.logging;

import cc.dreamcode.antylogout.libs.com.mongodb.annotations.ThreadSafe;

@FunctionalInterface
@ThreadSafe
public interface LoggingInterceptor
{
    void intercept(final LogMessage p0);
}
