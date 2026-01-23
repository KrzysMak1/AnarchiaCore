package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.function;

import lombok.NonNull;

@FunctionalInterface
public interface TripleConsumer<A, B, C>
{
    void accept(final A first, final B second, final C third);
    
    default TripleConsumer<A, B, C> then(@NonNull final TripleConsumer<? super A, ? super B, ? super C> after) {
        if (after == null) {
            throw new NullPointerException("after is marked non-null but is null");
        }
        return (a, b, c) -> {
            this.accept(a, b, c);
            after.accept(a, b, c);
        };
    }
}
