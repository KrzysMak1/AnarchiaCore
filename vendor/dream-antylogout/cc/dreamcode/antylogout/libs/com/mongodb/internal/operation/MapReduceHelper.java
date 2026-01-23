package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonNumber;
import cc.dreamcode.antylogout.libs.org.bson.BsonInt32;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;

final class MapReduceHelper
{
    static MapReduceStatistics createStatistics(final BsonDocument result) {
        return new MapReduceStatistics(getInputCount(result), getOutputCount(result), getEmitCount(result), getDuration(result));
    }
    
    private static int getInputCount(final BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("input", new BsonInt32(0)).intValue();
    }
    
    private static int getOutputCount(final BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("output", new BsonInt32(0)).intValue();
    }
    
    private static int getEmitCount(final BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("emit", new BsonInt32(0)).intValue();
    }
    
    private static int getDuration(final BsonDocument result) {
        return result.getNumber("timeMillis", new BsonInt32(0)).intValue();
    }
    
    private MapReduceHelper() {
    }
}
