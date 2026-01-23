package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.lang.reflect.AccessibleObject;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.ApiStatus;
import java.util.EnumSet;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XAccessFlag;
import java.util.Set;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;

public abstract class MemberHandle implements ReflectiveHandle<MethodHandle>
{
    protected Set<XAccessFlag> accessFlags;
    protected final ClassHandle clazz;
    
    protected MemberHandle(final ClassHandle clazz) {
        this.accessFlags = (Set<XAccessFlag>)EnumSet.noneOf((Class)XAccessFlag.class);
        this.clazz = clazz;
    }
    
    @ApiStatus.Internal
    public Set<XAccessFlag> getAccessFlags() {
        return this.accessFlags;
    }
    
    public ClassHandle getClassHandle() {
        return this.clazz;
    }
    
    public MemberHandle makeAccessible() {
        this.accessFlags.add((Object)XAccessFlag.PRIVATE);
        return this;
    }
    
    public abstract MemberHandle signature(@Language("Java") final String p0);
    
    @Override
    public abstract MethodHandle reflect() throws ReflectiveOperationException;
    
    public abstract <T extends AccessibleObject & Member> T reflectJvm() throws ReflectiveOperationException;
    
    protected <T extends AccessibleObject & Member> T handleAccessible(final T accessibleObject) throws ReflectiveOperationException {
        if (this.accessFlags.contains((Object)XAccessFlag.PRIVATE) || Modifier.isPrivate(accessibleObject.getDeclaringClass().getModifiers())) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }
    
    @Override
    public abstract MemberHandle copy();
}
