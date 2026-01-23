package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.aggregate;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import java.util.Iterator;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.concurrent.Callable;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

public class AggregateReflectiveHandle<T, H extends ReflectiveHandle<T>> implements ReflectiveHandle<T>
{
    private final List<Callable<H>> handles;
    private Consumer<H> handleModifier;
    
    @ApiStatus.Internal
    public AggregateReflectiveHandle(final Collection<Callable<H>> handles) {
        (this.handles = (List<Callable<H>>)new ArrayList(handles.size())).addAll((Collection)handles);
    }
    
    public AggregateReflectiveHandle<T, H> or(@NotNull final H handle) {
        return this.or((Callable<H>)(() -> handle));
    }
    
    public AggregateReflectiveHandle<T, H> or(@NotNull final Callable<H> handle) {
        this.handles.add((Object)handle);
        return this;
    }
    
    public AggregateReflectiveHandle<T, H> modify(@Nullable final Consumer<H> handleModifier) {
        this.handleModifier = handleModifier;
        return this;
    }
    
    public H getHandle() {
        ClassNotFoundException errors = null;
        for (final Callable<H> handle : this.handles) {
            try {
                final H handled = (H)handle.call();
                if (this.handleModifier != null) {
                    this.handleModifier.accept((Object)handled);
                }
                if (!handled.exists()) {
                    handled.reflect();
                }
                return handled;
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
    
    @Override
    public AggregateReflectiveHandle<T, H> copy() {
        final AggregateReflectiveHandle<T, H> handle = new AggregateReflectiveHandle<T, H>((Collection<Callable<H>>)new ArrayList((Collection)this.handles));
        handle.handleModifier = this.handleModifier;
        return handle;
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return this.getHandle().jvm();
    }
    
    @Override
    public T reflect() throws ReflectiveOperationException {
        ClassNotFoundException errors = null;
        for (final Callable<H> handle : this.handles) {
            H handled;
            try {
                handled = (H)handle.call();
                if (this.handleModifier != null) {
                    this.handleModifier.accept((Object)handled);
                }
            }
            catch (final Throwable ex) {
                if (errors == null) {
                    errors = new ClassNotFoundException("None of the aggregate handles were successful");
                }
                errors.addSuppressed(ex);
                continue;
            }
            try {
                return handled.reflect();
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
        throw XReflection.relativizeSuppressedExceptions(errors);
    }
}
