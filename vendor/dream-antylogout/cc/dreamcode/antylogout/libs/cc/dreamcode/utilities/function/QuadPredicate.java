package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface QuadPredicate<A, B, C, D>
{
    boolean test(final A first, final B second, final C third, final D fourth);
}
