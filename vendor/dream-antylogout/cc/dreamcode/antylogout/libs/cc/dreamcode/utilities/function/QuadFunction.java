package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface QuadFunction<R, A, B, C, D>
{
    R apply(final A first, final B second, final C third, final D fourth);
}
