package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.NameableReflectiveHandle;
import java.util.Collection;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.PackageHandle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.ReflectiveNamespace;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.DynamicClassHandle;

public class MinecraftClassHandle extends DynamicClassHandle
{
    public MinecraftClassHandle(final ReflectiveNamespace namespace) {
        super(namespace);
    }
    
    public MinecraftClassHandle inPackage(final MinecraftPackage minecraftPackage) {
        super.inPackage(minecraftPackage);
        return this;
    }
    
    public MinecraftClassHandle inPackage(final MinecraftPackage minecraftPackage, @Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String packageName) {
        super.inPackage(minecraftPackage, packageName);
        return this;
    }
    
    @Override
    public MinecraftClassHandle inner(@Language(value = "Java", suffix = "{}") final String declaration) {
        return this.inner(this.namespace.ofMinecraft(declaration));
    }
    
    @Override
    public MinecraftClassHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... clazzNames) {
        super.named(clazzNames);
        return this;
    }
    
    @Override
    public MinecraftClassHandle map(final MinecraftMapping mapping, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String className) {
        this.classNames.add((Object)className);
        return this;
    }
    
    @Override
    public MinecraftClassHandle copy() {
        final MinecraftClassHandle handle = new MinecraftClassHandle(this.namespace);
        handle.array = this.array;
        handle.parent = this.parent;
        handle.packageName = this.packageName;
        handle.classNames.addAll((Collection)this.classNames);
        return handle;
    }
}
