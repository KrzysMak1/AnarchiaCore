package cc.dreamcode.antylogout.libs.com.google.gson.internal.bind;

import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonToken;
import cc.dreamcode.antylogout.libs.com.google.gson.JsonSyntaxException;
import cc.dreamcode.antylogout.libs.com.google.gson.stream.JsonReader;
import cc.dreamcode.antylogout.libs.com.google.gson.ToNumberPolicy;
import cc.dreamcode.antylogout.libs.com.google.gson.reflect.TypeToken;
import cc.dreamcode.antylogout.libs.com.google.gson.Gson;
import cc.dreamcode.antylogout.libs.com.google.gson.ToNumberStrategy;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapterFactory;
import cc.dreamcode.antylogout.libs.com.google.gson.TypeAdapter;

public final class NumberTypeAdapter extends TypeAdapter<Number>
{
    private static final TypeAdapterFactory LAZILY_PARSED_NUMBER_FACTORY;
    private final ToNumberStrategy toNumberStrategy;
    
    private NumberTypeAdapter(final ToNumberStrategy toNumberStrategy) {
        this.toNumberStrategy = toNumberStrategy;
    }
    
    private static TypeAdapterFactory newFactory(final ToNumberStrategy toNumberStrategy) {
        final NumberTypeAdapter adapter = new NumberTypeAdapter(toNumberStrategy);
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
                return (TypeAdapter<T>)((type.getRawType() == Number.class) ? adapter : null);
            }
        };
    }
    
    public static TypeAdapterFactory getFactory(final ToNumberStrategy toNumberStrategy) {
        if (toNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER) {
            return NumberTypeAdapter.LAZILY_PARSED_NUMBER_FACTORY;
        }
        return newFactory(toNumberStrategy);
    }
    
    @Override
    public Number read(final JsonReader in) throws IOException {
        final JsonToken jsonToken = in.peek();
        switch (jsonToken) {
            case NULL: {
                in.nextNull();
                return null;
            }
            case NUMBER:
            case STRING: {
                return this.toNumberStrategy.readNumber(in);
            }
            default: {
                throw new JsonSyntaxException("Expecting number, got: " + (Object)jsonToken + "; at path " + in.getPath());
            }
        }
    }
    
    @Override
    public void write(final JsonWriter out, final Number value) throws IOException {
        out.value(value);
    }
    
    static {
        LAZILY_PARSED_NUMBER_FACTORY = newFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    }
}
