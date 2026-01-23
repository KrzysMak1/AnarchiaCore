package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface ToStringFunction<T>
{
    String applyAsString(final T value);
}
