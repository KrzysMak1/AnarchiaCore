package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.com.mongodb.internal.async.AsyncBatchCursor;

public interface MapReduceAsyncBatchCursor<T> extends AsyncBatchCursor<T>
{
    MapReduceStatistics getStatistics();
}
