package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.MongoInternalException;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.com.mongodb.ExplainVerbosity;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class ExplainHelper
{
    static BsonDocument asExplainCommand(final BsonDocument command, @Nullable final ExplainVerbosity explainVerbosity) {
        final BsonDocument explainCommand = new BsonDocument("explain", command);
        if (explainVerbosity != null) {
            explainCommand.append("verbosity", getVerbosityAsString(explainVerbosity));
        }
        return explainCommand;
    }
    
    private static BsonString getVerbosityAsString(final ExplainVerbosity explainVerbosity) {
        switch (explainVerbosity) {
            case QUERY_PLANNER: {
                return new BsonString("queryPlanner");
            }
            case EXECUTION_STATS: {
                return new BsonString("executionStats");
            }
            case ALL_PLANS_EXECUTIONS: {
                return new BsonString("allPlansExecution");
            }
            default: {
                throw new MongoInternalException(String.format("Unsupported explain verbosity %s", new Object[] { explainVerbosity }));
            }
        }
    }
    
    private ExplainHelper() {
    }
}
