package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class LoggerSettings
{
    private final int maxDocumentLength;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(final LoggerSettings loggerSettings) {
        return builder().applySettings(loggerSettings);
    }
    
    public int getMaxDocumentLength() {
        return this.maxDocumentLength;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final LoggerSettings that = (LoggerSettings)o;
        return this.maxDocumentLength == that.maxDocumentLength;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.maxDocumentLength });
    }
    
    @Override
    public String toString() {
        return "LoggerSettings{maxDocumentLength=" + this.maxDocumentLength + '}';
    }
    
    private LoggerSettings(final Builder builder) {
        this.maxDocumentLength = builder.maxDocumentLength;
    }
    
    public static final class Builder
    {
        private int maxDocumentLength;
        
        private Builder() {
            this.maxDocumentLength = 1000;
        }
        
        public Builder applySettings(final LoggerSettings loggerSettings) {
            Assertions.notNull("loggerSettings", loggerSettings);
            this.maxDocumentLength = loggerSettings.maxDocumentLength;
            return this;
        }
        
        public Builder maxDocumentLength(final int maxDocumentLength) {
            this.maxDocumentLength = maxDocumentLength;
            return this;
        }
        
        public LoggerSettings build() {
            return new LoggerSettings(this, null);
        }
    }
}
