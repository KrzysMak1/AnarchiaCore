package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.com.mongodb.assertions.Assertions;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;

public abstract class CreateIndexCommitQuorum
{
    public static final CreateIndexCommitQuorum MAJORITY;
    public static final CreateIndexCommitQuorum VOTING_MEMBERS;
    
    public static CreateIndexCommitQuorum create(final String mode) {
        return new CreateIndexCommitQuorumWithMode(mode);
    }
    
    public static CreateIndexCommitQuorum create(final int w) {
        return new CreateIndexCommitQuorumWithW(w);
    }
    
    public abstract BsonValue toBsonValue();
    
    private CreateIndexCommitQuorum() {
    }
    
    static {
        MAJORITY = new CreateIndexCommitQuorumWithMode("majority");
        VOTING_MEMBERS = new CreateIndexCommitQuorumWithMode("votingMembers");
    }
    
    private static final class CreateIndexCommitQuorumWithMode extends CreateIndexCommitQuorum
    {
        private final String mode;
        
        private CreateIndexCommitQuorumWithMode(final String mode) {
            super(null);
            Assertions.notNull("mode", mode);
            this.mode = mode;
        }
        
        public String getMode() {
            return this.mode;
        }
        
        @Override
        public BsonValue toBsonValue() {
            return new BsonString(this.mode);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CreateIndexCommitQuorumWithMode that = (CreateIndexCommitQuorumWithMode)o;
            return this.mode.equals((Object)that.mode);
        }
        
        @Override
        public int hashCode() {
            return this.mode.hashCode();
        }
        
        @Override
        public String toString() {
            return "CreateIndexCommitQuorum{mode=" + this.mode + '}';
        }
    }
    
    private static final class CreateIndexCommitQuorumWithW extends CreateIndexCommitQuorum
    {
        private final int w;
        
        private CreateIndexCommitQuorumWithW(final int w) {
            super(null);
            if (w < 0) {
                throw new IllegalArgumentException("w cannot be less than zero");
            }
            this.w = w;
        }
        
        public int getW() {
            return this.w;
        }
        
        @Override
        public BsonValue toBsonValue() {
            return new BsonInt32(this.w);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CreateIndexCommitQuorumWithW that = (CreateIndexCommitQuorumWithW)o;
            return this.w == that.w;
        }
        
        @Override
        public int hashCode() {
            return this.w;
        }
        
        @Override
        public String toString() {
            return "CreateIndexCommitQuorum{w=" + this.w + '}';
        }
    }
}
