package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.lock;

import java.util.UUID;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class MojangRequestQueue
{
    public static final KeyedLockMap<String> USERNAME_REQUESTS;
    public static final KeyedLockMap<UUID> UUID_REQUESTS;
    
    static {
        USERNAME_REQUESTS = new KeyedLockMap<String>();
        UUID_REQUESTS = new KeyedLockMap<UUID>();
    }
}
