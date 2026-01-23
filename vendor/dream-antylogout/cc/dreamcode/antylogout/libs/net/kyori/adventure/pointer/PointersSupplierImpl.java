package cc.dreamcode.antylogout.libs.net.kyori.adventure.pointer;

import cc.dreamcode.antylogout.libs.net.kyori.adventure.util.Buildable;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.Optional;
import java.util.Objects;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

final class PointersSupplierImpl<T> implements PointersSupplier<T>
{
    @Nullable
    private final PointersSupplier<? super T> parent;
    private final Map<Pointer<?>, Function<T, ?>> resolvers;
    
    PointersSupplierImpl(@NotNull final BuilderImpl<T> builder) {
        this.parent = ((BuilderImpl<Object>)builder).parent;
        this.resolvers = (Map<Pointer<?>, Function<T, ?>>)new HashMap(((BuilderImpl<Object>)builder).resolvers);
    }
    
    @NotNull
    @Override
    public Pointers view(@NotNull final T instance) {
        return new ForwardingPointers<Object>(instance, this);
    }
    
    @Override
    public <P> boolean supports(@NotNull final Pointer<P> pointer) {
        return this.resolvers.containsKey(Objects.requireNonNull((Object)pointer, "pointer")) || (this.parent != null && this.parent.supports(pointer));
    }
    
    @Nullable
    @Override
    public <P> Function<? super T, P> resolver(@NotNull final Pointer<P> pointer) {
        final Function<? super T, ?> resolver = (Function<? super T, ?>)this.resolvers.get(Objects.requireNonNull((Object)pointer, "pointer"));
        if (resolver != null) {
            return (Function<? super T, P>)resolver;
        }
        if (this.parent == null) {
            return null;
        }
        return this.parent.resolver(pointer);
    }
    
    static final class ForwardingPointers<U> implements Pointers
    {
        private final U instance;
        private final PointersSupplierImpl<U> supplier;
        
        ForwardingPointers(@NotNull final U instance, @NotNull final PointersSupplierImpl<U> supplier) {
            this.instance = instance;
            this.supplier = supplier;
        }
        
        @NotNull
        @Override
        public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
            Function<? super U, ?> resolver = (Function<? super U, ?>)((PointersSupplierImpl<Object>)this.supplier).resolvers.get(Objects.requireNonNull((Object)pointer, "pointer"));
            if (resolver == null) {
                final PointersSupplier<? super U> parent = ((PointersSupplierImpl<Object>)this.supplier).parent;
                if (parent != null) {
                    resolver = parent.resolver((Pointer<?>)pointer);
                }
            }
            if (resolver == null) {
                return (Optional<T>)Optional.empty();
            }
            return (Optional<T>)Optional.ofNullable(resolver.apply((Object)this.instance));
        }
        
        @Override
        public <T> boolean supports(@NotNull final Pointer<T> pointer) {
            return this.supplier.supports(pointer);
        }
        
        @Override
        public Pointers.Builder toBuilder() {
            final Pointers.Builder builder = (((PointersSupplierImpl<Object>)this.supplier).parent == null) ? Pointers.builder() : ((Buildable<R, Pointers.Builder>)((PointersSupplierImpl<Object>)this.supplier).parent.view(this.instance)).toBuilder();
            for (final Map.Entry<Pointer<?>, Function<U, ?>> entry : ((PointersSupplierImpl<Object>)this.supplier).resolvers.entrySet()) {
                builder.withDynamic((Pointer<Object>)entry.getKey(), (java.util.function.Supplier<Object>)(() -> ((Function)entry.getValue()).apply((Object)this.instance)));
            }
            return builder;
        }
    }
    
    static final class BuilderImpl<T> implements Builder<T>
    {
        @Nullable
        private PointersSupplier<? super T> parent;
        private final Map<Pointer<?>, Function<T, ?>> resolvers;
        
        BuilderImpl() {
            this.parent = null;
            this.resolvers = (Map<Pointer<?>, Function<T, ?>>)new HashMap();
        }
        
        @NotNull
        @Override
        public Builder<T> parent(@Nullable final PointersSupplier<? super T> parent) {
            this.parent = parent;
            return this;
        }
        
        @NotNull
        @Override
        public <P> Builder<T> resolving(@NotNull final Pointer<P> pointer, @NotNull final Function<T, P> resolver) {
            this.resolvers.put((Object)pointer, (Object)resolver);
            return this;
        }
        
        @NotNull
        @Override
        public PointersSupplier<T> build() {
            return new PointersSupplierImpl<T>(this);
        }
    }
}
