package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.processors;

import java.lang.reflect.Parameter;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.ConstructorMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.MethodMemberHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxy;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.FieldMemberHandle;
import java.lang.annotation.Annotation;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.annotations.Ignore;
import java.util.Collection;
import java.util.Arrays;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.proxy.ReflectiveProxyObject;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ReflectiveHandleProxyProcessor
{
    public static <T extends ReflectiveProxyObject> T proxify(@NotNull final Class<T> interfaceClass, @NotNull final ClassHandle targetClass, @NotNull final ReflectiveHandle<?>... handles) {
        final Set<ReflectiveHandle<?>> remainingHandles = (Set<ReflectiveHandle<?>>)Collections.newSetFromMap((Map)new IdentityHashMap());
        remainingHandles.addAll((Collection)Arrays.asList((Object[])handles));
        final Method[] interfaceMethods = interfaceClass.getMethods();
        final Map<Method, ReflectiveProxy.ProxifiedObject> mappedHandles = (Map<Method, ReflectiveProxy.ProxifiedObject>)new IdentityHashMap(interfaceMethods.length);
        for (final Method method : interfaceMethods) {
            if (!ReflectiveAnnotationProcessor.isAnnotationInherited(interfaceClass, method, (Class<? extends Annotation>)Ignore.class)) {
                final String name = method.getName();
                final Iterator<ReflectiveHandle<?>> iter = (Iterator<ReflectiveHandle<?>>)remainingHandles.iterator();
                while (iter.hasNext()) {
                    final ReflectiveHandle<?> next = (ReflectiveHandle<?>)iter.next();
                    if (next instanceof FieldMemberHandle) {
                        final FieldMemberHandle field = (FieldMemberHandle)next;
                        if (field.getPossibleNames().stream().anyMatch(x -> x.equals((Object)name))) {
                            iter.remove();
                            mappedHandles.put((Object)method, (Object)new ReflectiveProxy.ProxifiedObject(field.unreflect(), null, field.getAccessFlags().contains((Object)XAccessFlag.STATIC), false, null, null));
                            break;
                        }
                        continue;
                    }
                    else if (next instanceof MethodMemberHandle) {
                        final MethodMemberHandle methodHandle = (MethodMemberHandle)next;
                        if (methodHandle.getPossibleNames().stream().anyMatch(x -> x.equals((Object)name))) {
                            iter.remove();
                            mappedHandles.put((Object)method, (Object)new ReflectiveProxy.ProxifiedObject(methodHandle.unreflect(), null, methodHandle.getAccessFlags().contains((Object)XAccessFlag.STATIC), false, null, null));
                            break;
                        }
                        continue;
                    }
                    else {
                        if (!(next instanceof ConstructorMemberHandle) || method.getReturnType() != interfaceClass || !name.equals((Object)interfaceClass.getName())) {
                            continue;
                        }
                        final ConstructorMemberHandle ctor = (ConstructorMemberHandle)next;
                        if (ctor.getParameterTypes().length != method.getParameterCount()) {
                            continue;
                        }
                        Constructor<?> constructor;
                        try {
                            constructor = ctor.reflectJvm();
                        }
                        catch (final ReflectiveOperationException e) {
                            throw new IllegalStateException("Failed to map " + (Object)method, (Throwable)e);
                        }
                        int index = 0;
                        for (final Parameter parameter : method.getParameters()) {
                            if (constructor.getParameters()[index].getType() != parameter.getType()) {
                                index = -1;
                                break;
                            }
                            ++index;
                        }
                        if (index == -1) {
                            continue;
                        }
                        iter.remove();
                        mappedHandles.put((Object)method, (Object)new ReflectiveProxy.ProxifiedObject(ctor.unreflect(), null, false, true, null, null));
                        break;
                    }
                }
            }
        }
        return null;
    }
}
