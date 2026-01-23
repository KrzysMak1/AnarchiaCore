package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

@FunctionalInterface
public interface ToBooleanFunction<T>
{
    Boolean applyAsBoolean(final T value);
}
