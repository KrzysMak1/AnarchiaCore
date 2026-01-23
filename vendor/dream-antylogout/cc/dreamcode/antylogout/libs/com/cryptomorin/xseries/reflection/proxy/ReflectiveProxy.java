package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy;

import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.lang.invoke.MethodType;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.MappedType;
import java.util.Arrays;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.objects.ReflectedObject;
import java.util.IdentityHashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.ProxyMethodInfo;
import java.util.function.Function;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;
import java.lang.reflect.Method;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import java.lang.reflect.InvocationHandler;

@ApiStatus.Internal
public final class ReflectiveProxy<T extends ReflectiveProxyObject> implements InvocationHandler
{
    private static final Map<Class<?>, ReflectiveProxy<?>> PROXIFIED_CLASS_LOADER0;
    private static final ClassLoader CLASS_LOADER;
    private final Class<?> targetClass;
    private final Class<T> proxyClass;
    private T proxy;
    private final Object instance;
    private final Map<Method, ProxifiedObject> handles;
    private final ClassOverloadedMethods<ProxifiedObject> nameMapped;
    
    public static <T extends ReflectiveProxyObject> ReflectiveProxy<T> proxify(final Class<T> interfaceClass) {
        final ReflectiveProxy<?> proxified = (ReflectiveProxy<?>)ReflectiveProxy.PROXIFIED_CLASS_LOADER0.get((Object)interfaceClass);
        if (proxified != null) {
            return (ReflectiveProxy<T>)proxified;
        }
        final ReflectiveAnnotationProcessor processor = new ReflectiveAnnotationProcessor(interfaceClass);
        processor.process((Function<ProxyMethodInfo, String>)ReflectiveProxy::descriptorProcessor);
        final Set<Map.Entry<String, OverloadedMethod<ProxyMethodInfo>>> entries = (Set<Map.Entry<String, OverloadedMethod<ProxyMethodInfo>>>)processor.getMapped().mappings().entrySet();
        final Map<Method, ProxifiedObject> handles = (Map<Method, ProxifiedObject>)new IdentityHashMap(entries.size());
        final OverloadedMethod.Builder<ProxifiedObject> nameMapped = new OverloadedMethod.Builder<ProxifiedObject>((java.util.function.Function<ProxifiedObject, String>)ReflectiveProxy::descriptorProcessor);
        final ReflectiveProxy<T> proxy = new ReflectiveProxy<T>(processor.getTargetClass(), interfaceClass, null, handles, nameMapped.build());
        ReflectiveProxy.PROXIFIED_CLASS_LOADER0.put((Object)interfaceClass, (Object)proxy);
        for (final Map.Entry<String, OverloadedMethod<ProxyMethodInfo>> mapping : entries) {
            for (final ProxyMethodInfo overload : ((OverloadedMethod)mapping.getValue()).getOverloads()) {
                final ReflectedObject jvm = overload.handle.jvm().unreflect();
                MethodHandle methodHandle = (MethodHandle)overload.handle.unreflect();
                methodHandle = createDynamicProxy(null, methodHandle);
                final ProxifiedObject proxifiedObj = new ProxifiedObject(methodHandle, overload, jvm.accessFlags().contains((Object)XAccessFlag.STATIC), jvm.type() == ReflectedObject.Type.CONSTRUCTOR, overload.rType.isDifferent() ? proxify(overload.rType.synthetic) : null, (ReflectiveProxy<?>[])(Arrays.stream((Object[])overload.pTypes).anyMatch(MappedType::isDifferent) ? ((ReflectiveProxy[])Arrays.stream((Object[])overload.pTypes).map(x -> x.isDifferent() ? proxify(x.synthetic) : null).toArray(ReflectiveProxy[]::new)) : null));
                handles.put((Object)overload.interfaceMethod, (Object)proxifiedObj);
                nameMapped.add(proxifiedObj, (String)mapping.getKey());
            }
        }
        nameMapped.build(proxy.nameMapped.mappings());
        proxy.proxy = proxy.createProxy();
        for (final Method proxyMethod : proxy.proxy.getClass().getDeclaredMethods()) {
            final ProxifiedObject matched = proxy.nameMapped.get(proxyMethod.getName(), (Supplier<String>)(() -> descriptorProcessor(proxyMethod)), true);
            if (matched != null) {
                handles.put((Object)proxyMethod, (Object)matched);
            }
        }
        for (final Class<?> proxyInterfaces : proxy.proxy.getClass().getInterfaces()) {
            for (final Method proxyMethod2 : proxyInterfaces.getDeclaredMethods()) {
                final ProxifiedObject matched2 = proxy.nameMapped.get(proxyMethod2.getName(), (Supplier<String>)(() -> descriptorProcessor(proxyMethod2)), true);
                if (matched2 != null) {
                    handles.put((Object)proxyMethod2, (Object)matched2);
                }
            }
        }
        return proxy;
    }
    
    private static MethodHandle createDynamicProxy(@Nullable final Object bindInstance, MethodHandle methodHandle) {
        final int parameterCount = methodHandle.type().parameterCount();
        final int requireArgs = (bindInstance != null) ? 1 : 0;
        if (bindInstance != null) {
            methodHandle = methodHandle.bindTo(bindInstance);
        }
        if (parameterCount == requireArgs) {
            return methodHandle.asType(MethodType.methodType((Class)Object.class));
        }
        return methodHandle.asSpreader((Class)Object[].class, parameterCount - requireArgs).asType(MethodType.methodType((Class)Object.class, (Class)Object[].class));
    }
    
    private static String descriptorProcessor(final ProxifiedObject obj) {
        return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(obj.proxyMethodInfo.pTypes));
    }
    
    private static String descriptorProcessor(final ProxyMethodInfo obj) {
        return OverloadedMethod.getParameterDescriptor(MappedType.getRealTypes(obj.pTypes));
    }
    
    private static String descriptorProcessor(final Method method) {
        return OverloadedMethod.getParameterDescriptor(method.getParameterTypes());
    }
    
    private ReflectiveProxy(final Class<?> targetClass, final Class<T> proxyClass, final Object instance, final Map<Method, ProxifiedObject> handles, final ClassOverloadedMethods<ProxifiedObject> nameMapped) {
        this.targetClass = targetClass;
        this.proxyClass = proxyClass;
        this.instance = instance;
        this.handles = handles;
        this.nameMapped = nameMapped;
    }
    
    public static void checkInterfaceClass(final Class<?> interfaceClass) {
        Objects.requireNonNull((Object)interfaceClass, "Interface class is null");
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("Cannot proxify non-interface class: " + (Object)interfaceClass);
        }
        if (!ReflectiveProxyObject.class.isAssignableFrom(interfaceClass)) {
            throw new IllegalArgumentException("The provided interface class must extend ReflectiveProxyObject interface");
        }
    }
    
    @ApiStatus.Internal
    @NotNull
    public T createProxy() {
        return (T)Proxy.newProxyInstance(ReflectiveProxy.CLASS_LOADER, new Class[] { this.proxyClass }, (InvocationHandler)this);
    }
    
    @NotNull
    public T proxy() {
        return this.proxy;
    }
    
    @Nullable
    public Object instance() {
        return this.instance;
    }
    
    @NotNull
    public T bindTo(@NotNull final Object instance) {
        if (this.instance != null) {
            throw new IllegalStateException("This proxy object already has an instance bound to it: " + (Object)this);
        }
        Objects.requireNonNull(instance, "Instance cannot be null");
        if (!this.targetClass.isAssignableFrom(instance.getClass())) {
            throw new IllegalArgumentException("The given instance doesn't match the target class: " + instance + " -> " + (Object)this);
        }
        final Map<Method, ProxifiedObject> handles = (Map<Method, ProxifiedObject>)new IdentityHashMap(this.handles.size());
        final Map<ProxifiedObject, ProxifiedObject> boundStates = (Map<ProxifiedObject, ProxifiedObject>)new IdentityHashMap(this.nameMapped.mappings().size());
        final OverloadedMethod.Builder<ProxifiedObject> nameMapped = new OverloadedMethod.Builder<ProxifiedObject>((java.util.function.Function<ProxifiedObject, String>)ReflectiveProxy::descriptorProcessor);
        for (final Map.Entry<String, OverloadedMethod<ProxifiedObject>> entry : this.nameMapped.mappings().entrySet()) {
            for (final ProxifiedObject unbound : ((OverloadedMethod)entry.getValue()).getOverloads()) {
                final ProxifiedObject overload = unbound;
                ProxifiedObject bound = (ProxifiedObject)boundStates.get((Object)unbound);
                if (bound == null) {
                    if (unbound.isStatic || unbound.isConstructor) {
                        nameMapped.add(unbound, (String)entry.getKey());
                        continue;
                    }
                    MethodHandle insert;
                    try {
                        insert = (MethodHandle)unbound.proxyMethodInfo.handle.unreflect();
                        if (insert.type().parameterCount() == 0) {
                            throw new IllegalStateException("Non-static, non-constructor with 0 arguments found: " + (Object)insert);
                        }
                        insert = createDynamicProxy(instance, insert);
                    }
                    catch (final Exception e) {
                        throw new IllegalStateException("Failed to bind " + instance + " to " + (String)entry.getKey() + " -> " + (Object)unbound.handle + " (static=" + unbound.isStatic + ", constructor=" + unbound.isConstructor + ')', (Throwable)e);
                    }
                    bound = new ProxifiedObject(insert, unbound.proxyMethodInfo, unbound.isStatic, unbound.isConstructor, unbound.rType, unbound.pTypes);
                    boundStates.put((Object)unbound, (Object)bound);
                }
                nameMapped.add(bound, (String)entry.getKey());
            }
        }
        for (final Map.Entry<Method, ProxifiedObject> entry2 : this.handles.entrySet()) {
            final ProxifiedObject unbound2 = (ProxifiedObject)entry2.getValue();
            if (unbound2.isStatic || unbound2.isConstructor) {
                handles.put((Object)entry2.getKey(), (Object)unbound2);
            }
            else {
                final ProxifiedObject bound2 = (ProxifiedObject)boundStates.get((Object)unbound2);
                if (bound2 == null) {
                    throw new IllegalStateException("Cannot find bound method for " + entry2.getKey() + " (" + (Object)unbound2 + "::" + unbound2.hashCode() + ") in " + (Object)nameMapped.build() + " - " + boundStates.entrySet().stream().map(x -> x.getKey() + "::" + x.hashCode()).collect(Collectors.toList()));
                }
                handles.put((Object)entry2.getKey(), (Object)bound2);
            }
        }
        final ReflectiveProxy<T> bound3 = new ReflectiveProxy<T>(this.targetClass, this.proxyClass, instance, handles, nameMapped.build());
        return bound3.createProxy();
    }
    
    private static String getMethodList(final Class<?> clazz, final boolean declaredOnly) {
        return ((List)Arrays.stream((Object[])(declaredOnly ? clazz.getDeclaredMethods() : clazz.getMethods())).map(x -> x.getName() + "::" + System.identityHashCode((Object)x)).collect(Collectors.toList())).toString();
    }
    
    public Object invoke(final Object proxy, final Method method, @Nullable final Object[] args) throws Throwable {
        final int paramCount = method.getParameterCount();
        final String name = method.getName();
        if (paramCount == 0) {
            final String s = name;
            int n = -1;
            switch (s.hashCode()) {
                case 555127957: {
                    if (s.equals((Object)"instance")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case -1776922004: {
                    if (s.equals((Object)"toString")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case 147696667: {
                    if (s.equals((Object)"hashCode")) {
                        n = 2;
                        break;
                    }
                    break;
                }
                case -1039689911: {
                    if (s.equals((Object)"notify")) {
                        n = 3;
                        break;
                    }
                    break;
                }
                case 1902066072: {
                    if (s.equals((Object)"notifyAll")) {
                        n = 4;
                        break;
                    }
                    break;
                }
                case 3641717: {
                    if (s.equals((Object)"wait")) {
                        n = 5;
                        break;
                    }
                    break;
                }
                case 1544020273: {
                    if (s.equals((Object)"getTargetClass")) {
                        n = 6;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    return this.instance;
                }
                case 1: {
                    return (this.instance == null) ? this.proxyClass.toString() : this.instance.toString();
                }
                case 2: {
                    return (this.instance == null) ? this.proxyClass.hashCode() : this.instance.hashCode();
                }
                case 3: {
                    if (this.instance == null) {
                        this.proxyClass.notify();
                    }
                    else {
                        this.instance.notify();
                    }
                    return null;
                }
                case 4: {
                    if (this.instance == null) {
                        this.proxyClass.notifyAll();
                    }
                    else {
                        this.instance.notifyAll();
                    }
                    return null;
                }
                case 5: {
                    if (this.instance == null) {
                        this.proxyClass.wait();
                    }
                    else {
                        this.instance.wait();
                    }
                    return null;
                }
                case 6: {
                    return this.targetClass;
                }
            }
        }
        else if (paramCount == 1) {
            final String s2 = name;
            int n2 = -1;
            switch (s2.hashCode()) {
                case -1388964968: {
                    if (s2.equals((Object)"bindTo")) {
                        n2 = 0;
                        break;
                    }
                    break;
                }
                case -238142497: {
                    if (s2.equals((Object)"isInstance")) {
                        n2 = 1;
                        break;
                    }
                    break;
                }
                case -1295482945: {
                    if (s2.equals((Object)"equals")) {
                        n2 = 2;
                        break;
                    }
                    break;
                }
                case 3641717: {
                    if (s2.equals((Object)"wait")) {
                        n2 = 3;
                        break;
                    }
                    break;
                }
            }
            switch (n2) {
                case 0: {
                    return this.bindTo(args[0]);
                }
                case 1: {
                    return this.targetClass.isInstance(args[0]);
                }
                case 2: {
                    return (this.instance == null) ? (this.proxyClass == args[0]) : this.instance.equals(args[0]);
                }
                case 3: {
                    if (this.instance == null) {
                        this.proxyClass.wait((long)args[0]);
                    }
                    else {
                        this.instance.wait((long)args[0]);
                    }
                    return null;
                }
            }
        }
        else if (paramCount == 2 && name.equals((Object)"wait")) {
            if (this.instance == null) {
                this.proxyClass.wait((long)args[0], (int)args[1]);
            }
            else {
                this.instance.wait((long)args[0], (int)args[1]);
            }
            return null;
        }
        ProxifiedObject reflectedHandle = (ProxifiedObject)this.handles.get((Object)method);
        if (reflectedHandle == null) {
            reflectedHandle = this.nameMapped.get(method.getName(), (Supplier<String>)(() -> descriptorProcessor(method)));
            this.handles.put((Object)method, (Object)reflectedHandle);
        }
        if (!reflectedHandle.isStatic && !reflectedHandle.isConstructor && this.instance == null) {
            throw new IllegalStateException("Cannot invoke non-static non-constructor member handle with when no instance is set");
        }
        if (reflectedHandle.isConstructor && this.instance != null) {
            throw new IllegalStateException("Cannot invoke constructor twice");
        }
        if (reflectedHandle.pTypes != null && args != null) {
            for (int i = 0; i < args.length; ++i) {
                final Object arg = args[i];
                if (arg instanceof ReflectiveProxyObject) {
                    args[i] = ((ReflectiveProxyObject)arg).instance();
                }
            }
        }
        Object result;
        try {
            if (args == null) {
                result = reflectedHandle.handle.invokeExact();
            }
            else {
                result = reflectedHandle.handle.invoke(args);
            }
        }
        catch (final Throwable ex) {
            throw new IllegalStateException("Failed to execute " + (Object)method + " -> " + (Object)reflectedHandle.handle + " with args " + ((args == null) ? "null" : Arrays.stream(args).map(x -> (x == null) ? "null" : (x + " (" + x.getClass().getSimpleName() + ')'))), ex);
        }
        if (reflectedHandle.rType != null) {
            result = reflectedHandle.rType.bindTo(result);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "ReflectiveProxy(proxyClass=" + (Object)this.proxyClass + ", proxy=" + (Object)this.proxy + ", instance=" + this.instance + ", nameMapped=" + (Object)this.nameMapped + ')';
    }
    
    static {
        PROXIFIED_CLASS_LOADER0 = (Map)new IdentityHashMap();
        CLASS_LOADER = ReflectiveProxy.class.getClassLoader();
    }
    
    public static final class ProxifiedObject
    {
        private final MethodHandle handle;
        private final ProxyMethodInfo proxyMethodInfo;
        private final boolean isStatic;
        private final boolean isConstructor;
        private final ReflectiveProxy<?> rType;
        private final ReflectiveProxy<?>[] pTypes;
        
        public ProxifiedObject(final MethodHandle handle, final ProxyMethodInfo proxyMethodInfo, final boolean isStatic, final boolean isConstructor, final ReflectiveProxy<?> rType, final ReflectiveProxy<?>[] pTypes) {
            this.handle = handle;
            this.proxyMethodInfo = proxyMethodInfo;
            this.isStatic = isStatic;
            this.isConstructor = isConstructor;
            this.rType = rType;
            this.pTypes = pTypes;
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + '(' + (Object)this.proxyMethodInfo.interfaceMethod + ')';
        }
    }
}
