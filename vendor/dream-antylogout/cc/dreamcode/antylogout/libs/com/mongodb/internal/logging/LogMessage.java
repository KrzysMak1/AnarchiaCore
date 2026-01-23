package cc.dreamcode.antylogout.libs.com.mongodb.internal.logging;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Map;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import java.util.List;
import java.util.Collection;
import cc.dreamcode.antylogout.libs.com.mongodb.connection.ClusterId;

public final class LogMessage
{
    private final Component component;
    private final Level level;
    private final String messageId;
    private final ClusterId clusterId;
    private final Throwable exception;
    private final Collection<Entry> entries;
    private final String format;
    
    public LogMessage(final Component component, final Level level, final String messageId, final ClusterId clusterId, final List<Entry> entries, final String format) {
        this(component, level, messageId, clusterId, null, (Collection<Entry>)entries, format);
    }
    
    public LogMessage(final Component component, final Level level, final String messageId, final ClusterId clusterId, @Nullable final Throwable exception, final Collection<Entry> entries, final String format) {
        this.component = component;
        this.level = level;
        this.messageId = messageId;
        this.clusterId = clusterId;
        this.exception = exception;
        this.entries = entries;
        this.format = format;
    }
    
    public ClusterId getClusterId() {
        return this.clusterId;
    }
    
    public Component getComponent() {
        return this.component;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public String getMessageId() {
        return this.messageId;
    }
    
    @Nullable
    public Throwable getException() {
        return this.exception;
    }
    
    public Collection<Entry> getEntries() {
        return this.entries;
    }
    
    public StructuredLogMessage toStructuredLogMessage() {
        final List<Entry> nullableEntries = (List<Entry>)this.entries.stream().filter(entry -> entry.getValue() != null).collect(Collectors.toList());
        return new StructuredLogMessage((Collection)nullableEntries);
    }
    
    public UnstructuredLogMessage toUnstructuredLogMessage() {
        return new UnstructuredLogMessage();
    }
    
    public enum Component
    {
        COMMAND("command"), 
        CONNECTION("connection"), 
        SERVER_SELECTION("serverSelection");
        
        private static final Map<String, Component> INDEX;
        private final String value;
        
        private Component(final String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public static Component of(final String value) {
            final Component result = (Component)Component.INDEX.get((Object)value);
            return Assertions.assertNotNull(result);
        }
        
        static {
            INDEX = (Map)Stream.of((Object[])values()).collect(Collectors.toMap(Component::getValue, Function.identity()));
        }
    }
    
    public enum Level
    {
        INFO, 
        DEBUG;
    }
    
    public static final class Entry
    {
        private final Name name;
        private final Object value;
        
        public Entry(final Name name, @Nullable final Object value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return this.name.getValue();
        }
        
        @Nullable
        public Object getValue() {
            return this.value;
        }
        
        public enum Name
        {
            SERVER_HOST("serverHost"), 
            SERVER_PORT("serverPort"), 
            COMMAND_NAME("commandName"), 
            REQUEST_ID("requestId"), 
            OPERATION_ID("operationId"), 
            OPERATION("operation"), 
            SERVICE_ID("serviceId"), 
            SERVER_CONNECTION_ID("serverConnectionId"), 
            DRIVER_CONNECTION_ID("driverConnectionId"), 
            DURATION_MS("durationMS"), 
            DATABASE_NAME("databaseName"), 
            REPLY("reply"), 
            COMMAND_CONTENT("command"), 
            REASON_DESCRIPTION("reason"), 
            ERROR_DESCRIPTION("error"), 
            FAILURE("failure"), 
            MAX_IDLE_TIME_MS("maxIdleTimeMS"), 
            MIN_POOL_SIZE("minPoolSize"), 
            MAX_POOL_SIZE("maxPoolSize"), 
            MAX_CONNECTING("maxConnecting"), 
            WAIT_QUEUE_TIMEOUT_MS("waitQueueTimeoutMS"), 
            SELECTOR("selector"), 
            TOPOLOGY_DESCRIPTION("topologyDescription"), 
            REMAINING_TIME_MS("remainingTimeMS");
            
            private final String value;
            
            public String getValue() {
                return this.value;
            }
            
            private Name(final String value) {
                this.value = value;
            }
        }
    }
    
    public static final class StructuredLogMessage
    {
        private final Collection<Entry> entries;
        
        private StructuredLogMessage(final Collection<Entry> entries) {
            entries.forEach(entry -> Assertions.assertNotNull(entry.getValue()));
            this.entries = entries;
        }
        
        public Collection<Entry> getEntries() {
            return this.entries;
        }
    }
    
    public final class UnstructuredLogMessage
    {
        public String interpolate() {
            final Iterator<Entry> iterator = (Iterator<Entry>)LogMessage.this.entries.iterator();
            final StringBuilder builder = new StringBuilder();
            int s = 0;
            for (int i = 0; i < LogMessage.this.format.length(); ++i) {
                final char curr = LogMessage.this.format.charAt(i);
                if (curr == '[' || curr == '{') {
                    final Object value = ((Entry)iterator.next()).getValue();
                    builder.append((CharSequence)LogMessage.this.format, s, i);
                    if (curr == '{') {
                        builder.append(value);
                    }
                    else if (value == null) {
                        i = LogMessage.this.format.indexOf(93, i);
                    }
                    else {
                        final int openBrace = LogMessage.this.format.indexOf(123, i);
                        builder.append((CharSequence)LogMessage.this.format, i + 1, openBrace);
                        builder.append(value);
                        i = openBrace + 1;
                    }
                    s = i + 1;
                }
                else if (curr == ']' || curr == '}') {
                    if (curr == ']') {
                        builder.append((CharSequence)LogMessage.this.format, s, i);
                    }
                    s = i + 1;
                }
            }
            builder.append((CharSequence)LogMessage.this.format, s, LogMessage.this.format.length());
            return builder.toString();
        }
    }
}
