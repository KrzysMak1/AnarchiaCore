package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import java.util.Collection;
import java.util.Arrays;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;
import java.util.HashSet;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public abstract class NamedMemberHandle extends MemberHandle implements NameableReflectiveHandle
{
    protected final Set<String> names;
    
    @NotNull
    @Override
    public Set<String> getPossibleNames() {
        return this.names;
    }
    
    protected NamedMemberHandle(final ClassHandle clazz) {
        super(clazz);
        this.names = (Set<String>)new HashSet(5);
    }
    
    @Override
    public NamedMemberHandle map(final MinecraftMapping mapping, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String name) {
        this.names.add((Object)name);
        return this;
    }
    
    @Override
    public NamedMemberHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... names) {
        this.names.addAll((Collection)Arrays.asList((Object[])names));
        return this;
    }
    
    @Override
    public abstract NamedMemberHandle copy();
}
