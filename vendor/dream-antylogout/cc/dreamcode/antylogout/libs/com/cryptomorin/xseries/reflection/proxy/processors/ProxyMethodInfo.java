package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors;

import java.lang.reflect.Method;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ProxyMethodInfo
{
    public final ReflectiveHandle<?> handle;
    public final Method interfaceMethod;
    public final MappedType rType;
    public final MappedType[] pTypes;
    
    public ProxyMethodInfo(final ReflectiveHandle<?> handle, final Method interfaceMethod, final MappedType rType, final MappedType[] pTypes) {
        this.handle = handle;
        this.interfaceMethod = interfaceMethod;
        this.rType = rType;
        this.pTypes = pTypes;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + (Object)this.interfaceMethod + ')';
    }
}
