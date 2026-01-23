package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;

public class StaticReflectiveHandle<T> implements ReflectiveHandle<T>
{
    private final T reflected;
    private final ReflectiveHandle<ReflectedObject> jvm;
    
    public StaticReflectiveHandle(final T reflected, final ReflectedObject jvm) {
        this.reflected = reflected;
        this.jvm = new StaticReflectiveHandle<ReflectedObject>(jvm);
    }
    
    private StaticReflectiveHandle(final T reflected) {
        this.reflected = reflected;
        this.jvm = null;
    }
    
    @Override
    public ReflectiveHandle<T> copy() {
        return this;
    }
    
    @NotNull
    @Override
    public T reflect() throws ReflectiveOperationException {
        return this.reflected;
    }
    
    @NotNull
    @Override
    public ReflectiveHandle<ReflectedObject> jvm() {
        return (ReflectiveHandle<ReflectedObject>)((this.jvm == null) ? this : this.jvm);
    }
}
