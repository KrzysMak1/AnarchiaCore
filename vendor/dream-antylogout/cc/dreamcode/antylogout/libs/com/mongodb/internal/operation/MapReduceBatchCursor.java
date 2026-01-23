package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

public interface MapReduceBatchCursor<T> extends BatchCursor<T>
{
    MapReduceStatistics getStatistics();
}
