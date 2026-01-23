package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.com.mongodb.annotations.NotThreadSafe;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import java.util.Objects;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class ClientSessionOptions
{
    private final Boolean causallyConsistent;
    private final Boolean snapshot;
    private final TransactionOptions defaultTransactionOptions;
    
    @Nullable
    public Boolean isCausallyConsistent() {
        return this.causallyConsistent;
    }
    
    @Nullable
    public Boolean isSnapshot() {
        return this.snapshot;
    }
    
    public TransactionOptions getDefaultTransactionOptions() {
        return this.defaultTransactionOptions;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClientSessionOptions that = (ClientSessionOptions)o;
        return Objects.equals((Object)this.causallyConsistent, (Object)that.causallyConsistent) && Objects.equals((Object)this.snapshot, (Object)that.snapshot) && Objects.equals((Object)this.defaultTransactionOptions, (Object)that.defaultTransactionOptions);
    }
    
    @Override
    public int hashCode() {
        int result = (this.causallyConsistent != null) ? this.causallyConsistent.hashCode() : 0;
        result = 31 * result + ((this.snapshot != null) ? this.snapshot.hashCode() : 0);
        result = 31 * result + ((this.defaultTransactionOptions != null) ? this.defaultTransactionOptions.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ClientSessionOptions{causallyConsistent=" + (Object)this.causallyConsistent + "snapshot=" + (Object)this.snapshot + ", defaultTransactionOptions=" + (Object)this.defaultTransactionOptions + '}';
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static Builder builder(final ClientSessionOptions options) {
        Assertions.notNull("options", options);
        final Builder builder = new Builder();
        builder.causallyConsistent = options.isCausallyConsistent();
        builder.snapshot = options.isSnapshot();
        builder.defaultTransactionOptions = options.getDefaultTransactionOptions();
        return builder;
    }
    
    private ClientSessionOptions(final Builder builder) {
        if (builder.causallyConsistent != null && builder.causallyConsistent && builder.snapshot != null && builder.snapshot) {
            throw new IllegalArgumentException("A session can not be both a snapshot and causally consistent");
        }
        this.causallyConsistent = ((builder.causallyConsistent != null || builder.snapshot == null) ? builder.causallyConsistent : (!builder.snapshot));
        this.snapshot = builder.snapshot;
        this.defaultTransactionOptions = builder.defaultTransactionOptions;
    }
    
    @NotThreadSafe
    public static final class Builder
    {
        private Boolean causallyConsistent;
        private Boolean snapshot;
        private TransactionOptions defaultTransactionOptions;
        
        public Builder causallyConsistent(final boolean causallyConsistent) {
            this.causallyConsistent = causallyConsistent;
            return this;
        }
        
        public Builder snapshot(final boolean snapshot) {
            this.snapshot = snapshot;
            return this;
        }
        
        public Builder defaultTransactionOptions(final TransactionOptions defaultTransactionOptions) {
            this.defaultTransactionOptions = Assertions.notNull("defaultTransactionOptions", defaultTransactionOptions);
            return this;
        }
        
        public ClientSessionOptions build() {
            return new ClientSessionOptions(this, null);
        }
        
        private Builder() {
            this.defaultTransactionOptions = TransactionOptions.builder().build();
        }
    }
}
