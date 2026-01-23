package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

import lombok.NonNull;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D>
{
    void accept(final A first, final B second, final C third, final D fourth);
    
    default QuadConsumer<A, B, C, D> then(@NonNull final QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
        if (after == null) {
            throw new NullPointerException("after is marked non-null but is null");
        }
        return (a, b, c, d) -> {
            this.accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}
