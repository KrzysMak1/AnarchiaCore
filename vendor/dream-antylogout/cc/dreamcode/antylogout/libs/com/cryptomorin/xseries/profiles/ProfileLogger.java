package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ProfileLogger
{
    public static final Logger LOGGER;
    
    public static void debug(final String mainMessage, final Object... variables) {
        ProfileLogger.LOGGER.debug(mainMessage, variables);
    }
    
    static {
        LOGGER = LogManager.getLogger("XSkull");
    }
}
