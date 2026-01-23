package cc.dreamcode.antylogout.libs.net.kyori.adventure.pointer;

import org.jetbrains.annotations.Contract;
import cc.dreamcode.antylogout.libs.net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface PointersSupplier<T>
{
    @NotNull
    default <T> Builder<T> builder() {
        return new PointersSupplierImpl.BuilderImpl<T>();
    }
    
    @NotNull
    Pointers view(@NotNull final T instance);
    
     <P> boolean supports(@NotNull final Pointer<P> pointer);
    
    @Nullable
     <P> Function<? super T, P> resolver(@NotNull final Pointer<P> pointer);
    
    public interface Builder<T> extends AbstractBuilder<PointersSupplier<T>>
    {
        @Contract("_ -> this")
        @NotNull
        Builder<T> parent(@Nullable final PointersSupplier<? super T> parent);
        
        @Contract("_, _ -> this")
        @NotNull
         <P> Builder<T> resolving(@NotNull final Pointer<P> pointer, @NotNull final Function<T, P> resolver);
    }
}
