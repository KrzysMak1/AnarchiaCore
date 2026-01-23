package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate;

import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

@ApiStatus.Experimental
public class AggregateReflectiveSupplier<H extends ReflectiveHandle<?>, O>
{
    private final List<ReflectivePair> handles;
    private Consumer<H> handleModifier;
    
    @ApiStatus.Internal
    public AggregateReflectiveSupplier() {
        this.handles = (List<ReflectivePair>)new ArrayList();
    }
    
    public AggregateReflectiveSupplier<H, O> or(@NotNull final H handle, final O object) {
        return this.or((Callable<H>)(() -> handle), object);
    }
    
    public AggregateReflectiveSupplier<H, O> or(@NotNull final Callable<H> handle, final O object) {
        return this.or(handle, (Supplier<O>)(() -> object));
    }
    
    public AggregateReflectiveSupplier<H, O> or(@NotNull final H handle, final Supplier<O> object) {
        return this.or((Callable<H>)(() -> handle), object);
    }
    
    public AggregateReflectiveSupplier<H, O> or(@NotNull final Callable<H> handle, final Supplier<O> object) {
        this.handles.add((Object)new ReflectivePair((Callable)handle, (Supplier)object));
        return this;
    }
    
    public AggregateReflectiveSupplier<H, O> modify(@Nullable final Consumer<H> handleModifier) {
        this.handleModifier = handleModifier;
        return this;
    }
    
    public O get() {
        ClassNotFoundException errors = null;
        for (final ReflectivePair pair : this.handles) {
            try {
                final H handled = (H)pair.handle.call();
                if (this.handleModifier != null) {
                    this.handleModifier.accept((Object)handled);
                }
                if (!handled.exists()) {
                    handled.reflect();
                }
                return (O)pair.object.get();
            }
            catch (final Throwable ex) {
                if (errors == null) {
                    errors = new ClassNotFoundException("None of the aggregate handles were successful");
                }
                errors.addSuppressed(ex);
                continue;
            }
            break;
        }
        throw XReflection.throwCheckedException(XReflection.relativizeSuppressedExceptions((Throwable)errors));
    }
    
    private final class ReflectivePair
    {
        private final Callable<H> handle;
        private final Supplier<O> object;
        
        private ReflectivePair(final Callable<H> handle, final Supplier<O> object) {
            this.handle = handle;
            this.object = object;
        }
    }
}
