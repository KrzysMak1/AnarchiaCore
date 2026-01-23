package cc.dreamcode.antylogout.libs.com.mongodb;

import cc.dreamcode.antylogout.libs.org.bson.codecs.kotlin.DataClassCodecProvider;
import cc.dreamcode.antylogout.libs.org.bson.codecs.kotlinx.KotlinSerializerCodecProvider;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.org.bson.codecs.Codec;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecRegistry;
import cc.dreamcode.antylogout.libs.com.mongodb.lang.Nullable;
import cc.dreamcode.antylogout.libs.org.bson.codecs.configuration.CodecProvider;

public class KotlinCodecProvider implements CodecProvider
{
    @Nullable
    private static final CodecProvider KOTLIN_SERIALIZABLE_CODEC_PROVIDER;
    @Nullable
    private static final CodecProvider DATA_CLASS_CODEC_PROVIDER;
    
    @Nullable
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        return this.get(clazz, (List<Type>)Collections.emptyList(), registry);
    }
    
    @Nullable
    @Override
    public <T> Codec<T> get(final Class<T> clazz, final List<Type> typeArguments, final CodecRegistry registry) {
        Codec<T> codec = null;
        if (KotlinCodecProvider.KOTLIN_SERIALIZABLE_CODEC_PROVIDER != null) {
            codec = KotlinCodecProvider.KOTLIN_SERIALIZABLE_CODEC_PROVIDER.get(clazz, typeArguments, registry);
        }
        if (codec == null && KotlinCodecProvider.DATA_CLASS_CODEC_PROVIDER != null) {
            codec = KotlinCodecProvider.DATA_CLASS_CODEC_PROVIDER.get(clazz, typeArguments, registry);
        }
        return codec;
    }
    
    static {
        CodecProvider possibleCodecProvider = null;
        try {
            Class.forName("cc.dreamcode.antylogout.libs.org.bson.codecs.kotlinx.KotlinSerializerCodecProvider");
            possibleCodecProvider = (CodecProvider)new KotlinSerializerCodecProvider();
        }
        catch (final ClassNotFoundException ex) {}
        KOTLIN_SERIALIZABLE_CODEC_PROVIDER = possibleCodecProvider;
        possibleCodecProvider = null;
        try {
            Class.forName("cc.dreamcode.antylogout.libs.org.bson.codecs.kotlin.DataClassCodecProvider");
            possibleCodecProvider = (CodecProvider)new DataClassCodecProvider();
        }
        catch (final ClassNotFoundException ex2) {}
        DATA_CLASS_CODEC_PROVIDER = possibleCodecProvider;
    }
}
