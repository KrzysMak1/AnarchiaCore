package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface TripleFunction<R, A, B, C>
{
    R apply(final A first, final B second, final C third);
}
