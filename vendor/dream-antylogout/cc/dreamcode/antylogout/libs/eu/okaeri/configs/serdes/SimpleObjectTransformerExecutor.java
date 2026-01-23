package cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes;

public interface SimpleObjectTransformerExecutor<S, D>
{
    D transform(final S data);
}
