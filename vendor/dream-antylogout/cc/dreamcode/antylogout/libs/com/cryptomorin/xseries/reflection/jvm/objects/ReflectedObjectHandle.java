package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

public final class ReflectedObjectHandle implements ReflectiveHandle<ReflectedObject>
{
    private final ReflectiveOperation jvmGetter;
    
    public ReflectedObjectHandle(final ReflectiveOperation jvmGetter) {
        this.jvmGetter = jvmGetter;
    }
    
    @Override
    public ReflectiveHandle<ReflectedObject> copy() {
        return new ReflectedObjectHandle(this.jvmGetter);
    }
    
    @NotNull
    @Override
    public ReflectedObject reflect() throws ReflectiveOperationException {
        return this.jvmGetter.get();
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return this;
    }
    
    @FunctionalInterface
    public interface ReflectiveOperation
    {
        ReflectedObject get() throws ReflectiveOperationException;
    }
}
