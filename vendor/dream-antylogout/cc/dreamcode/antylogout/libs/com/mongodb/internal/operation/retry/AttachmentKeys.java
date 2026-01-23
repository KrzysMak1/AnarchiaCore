package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.retry;

import java.util.HashSet;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.mongodb.annotations.Immutable;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.com.mongodb.bulk.BulkWriteResult;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.operation.MixedBulkWriteOperation;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.function.LoopState;

public final class AttachmentKeys
{
    private static final LoopState.AttachmentKey<Integer> MAX_WIRE_VERSION;
    private static final LoopState.AttachmentKey<BsonDocument> COMMAND;
    private static final LoopState.AttachmentKey<Boolean> RETRYABLE_COMMAND_FLAG;
    private static final LoopState.AttachmentKey<Supplier<String>> COMMAND_DESCRIPTION_SUPPLIER;
    private static final LoopState.AttachmentKey<MixedBulkWriteOperation.BulkWriteTracker> BULK_WRITE_TRACKER;
    private static final LoopState.AttachmentKey<BulkWriteResult> BULK_WRITE_RESULT;
    
    public static LoopState.AttachmentKey<Integer> maxWireVersion() {
        return AttachmentKeys.MAX_WIRE_VERSION;
    }
    
    public static LoopState.AttachmentKey<BsonDocument> command() {
        return AttachmentKeys.COMMAND;
    }
    
    public static LoopState.AttachmentKey<Boolean> retryableCommandFlag() {
        return AttachmentKeys.RETRYABLE_COMMAND_FLAG;
    }
    
    public static LoopState.AttachmentKey<Supplier<String>> commandDescriptionSupplier() {
        return AttachmentKeys.COMMAND_DESCRIPTION_SUPPLIER;
    }
    
    public static LoopState.AttachmentKey<MixedBulkWriteOperation.BulkWriteTracker> bulkWriteTracker() {
        return AttachmentKeys.BULK_WRITE_TRACKER;
    }
    
    public static LoopState.AttachmentKey<BulkWriteResult> bulkWriteResult() {
        return AttachmentKeys.BULK_WRITE_RESULT;
    }
    
    private AttachmentKeys() {
        Assertions.fail();
    }
    
    static {
        MAX_WIRE_VERSION = new DefaultAttachmentKey<Integer>("maxWireVersion");
        COMMAND = new DefaultAttachmentKey<BsonDocument>("command");
        RETRYABLE_COMMAND_FLAG = new DefaultAttachmentKey<Boolean>("retryableCommandFlag");
        COMMAND_DESCRIPTION_SUPPLIER = new DefaultAttachmentKey<Supplier<String>>("commandDescriptionSupplier");
        BULK_WRITE_TRACKER = new DefaultAttachmentKey<MixedBulkWriteOperation.BulkWriteTracker>("bulkWriteTracker");
        BULK_WRITE_RESULT = new DefaultAttachmentKey<BulkWriteResult>("bulkWriteResult");
    }
    
    @Immutable
    private static final class DefaultAttachmentKey<V> implements LoopState.AttachmentKey<V>
    {
        private static final Set<String> AVOID_KEY_DUPLICATION;
        private final String key;
        
        private DefaultAttachmentKey(final String key) {
            Assertions.assertTrue(DefaultAttachmentKey.AVOID_KEY_DUPLICATION.add((Object)key));
            this.key = key;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final DefaultAttachmentKey<?> that = (DefaultAttachmentKey<?>)o;
            return this.key.equals((Object)that.key);
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode();
        }
        
        @Override
        public String toString() {
            return this.key;
        }
        
        static {
            AVOID_KEY_DUPLICATION = (Set)new HashSet();
        }
    }
}
