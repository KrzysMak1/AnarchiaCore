package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonBoolean;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class ReadPreferenceHedgeOptions
{
    private final boolean enabled;
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public BsonDocument toBsonDocument() {
        return new BsonDocument("enabled", new BsonBoolean(this.enabled));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ReadPreferenceHedgeOptions that = (ReadPreferenceHedgeOptions)o;
        return this.enabled == that.enabled;
    }
    
    @Override
    public int hashCode() {
        return this.enabled ? 1 : 0;
    }
    
    @Override
    public String toString() {
        return "ReadPreferenceHedgeOptions{enabled=" + this.enabled + '}';
    }
    
    private ReadPreferenceHedgeOptions(final Builder builder) {
        this.enabled = builder.enabled;
    }
    
    public static final class Builder
    {
        private boolean enabled;
        
        public Builder enabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public ReadPreferenceHedgeOptions build() {
            return new ReadPreferenceHedgeOptions(this, null);
        }
        
        private Builder() {
        }
    }
}
