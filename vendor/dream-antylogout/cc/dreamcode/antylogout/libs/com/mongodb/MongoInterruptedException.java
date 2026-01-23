package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;

public class MongoInterruptedException extends MongoException
{
    private static final long serialVersionUID = -4110417867718417860L;
    
    public MongoInterruptedException(@Nullable final String message, @Nullable final Exception e) {
        super(message, (Throwable)e);
    }
}
