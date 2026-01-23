package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface TriplePredicate<A, B, C>
{
    boolean test(final A first, final B second, final C third);
}
