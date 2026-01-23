package cc.dreamcode.antylogout.libs.com.mongodb.internal.operation;

import cc.dreamcode.antylogout.libs.org.bson.BsonValue;
import cc.dreamcode.antylogout.libs.org.bson.BsonString;
import cc.dreamcode.antylogout.libs.org.bson.BsonNumber;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.SearchIndexModel;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.org.bson.BsonDocument;
import java.util.ArrayList;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.client.model.IndexModel;
import java.util.List;

public final class IndexHelper
{
    public static List<String> getIndexNames(final List<IndexModel> indexes, final CodecRegistry codecRegistry) {
        final List<String> indexNames = (List<String>)new ArrayList(indexes.size());
        for (final IndexModel index : indexes) {
            final String name = index.getOptions().getName();
            if (name != null) {
                indexNames.add((Object)name);
            }
            else {
                indexNames.add((Object)generateIndexName(index.getKeys().toBsonDocument(BsonDocument.class, codecRegistry)));
            }
        }
        return indexNames;
    }
    
    public static List<String> getSearchIndexNames(final List<SearchIndexModel> indexes) {
        return (List<String>)indexes.stream().map(IndexHelper::getSearchIndexName).collect(Collectors.toList());
    }
    
    private static String getSearchIndexName(final SearchIndexModel model) {
        final String name = model.getName();
        return (name != null) ? name : "default";
    }
    
    public static String generateIndexName(final BsonDocument index) {
        final StringBuilder indexName = new StringBuilder();
        for (final String keyNames : index.keySet()) {
            if (indexName.length() != 0) {
                indexName.append('_');
            }
            indexName.append(keyNames).append('_');
            final BsonValue ascOrDescValue = index.get(keyNames);
            if (ascOrDescValue instanceof BsonNumber) {
                indexName.append(((BsonNumber)ascOrDescValue).intValue());
            }
            else {
                if (!(ascOrDescValue instanceof BsonString)) {
                    continue;
                }
                indexName.append(((BsonString)ascOrDescValue).getValue().replace(' ', '_'));
            }
        }
        return indexName.toString();
    }
    
    private IndexHelper() {
    }
}
