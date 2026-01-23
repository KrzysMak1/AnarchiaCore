package cc.dreamcode.antylogout.libs.com.mongodb.internal.event;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandFailedEvent;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandSucceededEvent;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandStartedEvent;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.diagnostics.logging.Logger;
import cc.dreamcode.antylogout.libs.com.mongodb.event.CommandListener;

final class CommandListenerMulticaster implements CommandListener
{
    private static final Logger LOGGER;
    private final List<CommandListener> commandListeners;
    
    CommandListenerMulticaster(final List<CommandListener> commandListeners) {
        Assertions.isTrue("All CommandListener instances are non-null", !commandListeners.contains((Object)null));
        this.commandListeners = (List<CommandListener>)new ArrayList((Collection)commandListeners);
    }
    
    @Override
    public void commandStarted(final CommandStartedEvent event) {
        for (final CommandListener cur : this.commandListeners) {
            try {
                cur.commandStarted(event);
            }
            catch (final Exception e) {
                if (!CommandListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                CommandListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising command started event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void commandSucceeded(final CommandSucceededEvent event) {
        for (final CommandListener cur : this.commandListeners) {
            try {
                cur.commandSucceeded(event);
            }
            catch (final Exception e) {
                if (!CommandListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                CommandListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising command succeeded event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    @Override
    public void commandFailed(final CommandFailedEvent event) {
        for (final CommandListener cur : this.commandListeners) {
            try {
                cur.commandFailed(event);
            }
            catch (final Exception e) {
                if (!CommandListenerMulticaster.LOGGER.isWarnEnabled()) {
                    continue;
                }
                CommandListenerMulticaster.LOGGER.warn(String.format("Exception thrown raising command failed event to listener %s", new Object[] { cur }), (Throwable)e);
            }
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("protocol.event");
    }
}
