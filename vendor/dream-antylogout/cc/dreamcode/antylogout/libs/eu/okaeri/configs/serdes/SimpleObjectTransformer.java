package cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsPair;
import lombok.NonNull;

public abstract class SimpleObjectTransformer
{
    private SimpleObjectTransformer() {
    }
    
    public static <S, D> ObjectTransformer<S, D> of(@NonNull final Class<S> from, @NonNull final Class<D> to, @NonNull final SimpleObjectTransformerExecutor<S, D> transformer) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        if (transformer == null) {
            throw new NullPointerException("transformer is marked non-null but is null");
        }
        return new ObjectTransformer<S, D>() {
            @Override
            public GenericsPair<S, D> getPair() {
                return this.genericsPair(from, to);
            }
            
            @Override
            public D transform(@NonNull final S data, @NonNull final SerdesContext serdesContext) {
                if (data == null) {
                    throw new NullPointerException("data is marked non-null but is null");
                }
                if (serdesContext == null) {
                    throw new NullPointerException("serdesContext is marked non-null but is null");
                }
                return transformer.transform(data);
            }
        };
    }
}
