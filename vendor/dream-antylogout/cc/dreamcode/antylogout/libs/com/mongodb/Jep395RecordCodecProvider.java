package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.record.RecordCodecProvider;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;

public class Jep395RecordCodecProvider implements CodecProvider
{
    @Nullable
    private static final CodecProvider RECORD_CODEC_PROVIDER;
    
    @Nullable
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        return this.get(clazz, (List<Type>)Collections.emptyList(), registry);
    }
    
    @Nullable
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final List<Type> typeArguments, final CodecRegistry registry) {
        return (Jep395RecordCodecProvider.RECORD_CODEC_PROVIDER != null) ? Jep395RecordCodecProvider.RECORD_CODEC_PROVIDER.get(clazz, typeArguments, registry) : null;
    }
    
    public boolean hasRecordSupport() {
        return Jep395RecordCodecProvider.RECORD_CODEC_PROVIDER != null;
    }
    
    static {
        CodecProvider possibleCodecProvider;
        try {
            Class.forName("java.lang.Record");
            Class.forName("cc.dreamcode.antylogout.libs.org.bson.codecs.record.RecordCodecProvider");
            possibleCodecProvider = new RecordCodecProvider();
        }
        catch (final ClassNotFoundException | UnsupportedClassVersionError e) {
            possibleCodecProvider = null;
        }
        RECORD_CODEC_PROVIDER = possibleCodecProvider;
    }
}
