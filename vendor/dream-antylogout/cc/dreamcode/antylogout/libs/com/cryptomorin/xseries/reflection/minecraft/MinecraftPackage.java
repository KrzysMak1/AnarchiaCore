package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.PackageHandle;

public enum MinecraftPackage implements PackageHandle
{
    NMS(XReflection.NMS_PACKAGE), 
    CB(XReflection.CRAFTBUKKIT_PACKAGE), 
    BUKKIT("org.bukkit"), 
    SPIGOT("org.spigotmc");
    
    private final String packageId;
    
    private MinecraftPackage(final String packageName) {
        this.packageId = packageName;
    }
    
    public String packageId() {
        return this.name();
    }
    
    public String getBasePackageName() {
        return this.packageId;
    }
    
    public String getPackage(@Pattern("(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String packageName) {
        if (packageName.startsWith(".") || packageName.endsWith(".")) {
            throw new IllegalArgumentException("Package name must not start or end with a dot: " + packageName + " (" + (Object)this + ')');
        }
        if (!packageName.isEmpty() && (this != MinecraftPackage.NMS || XReflection.supports(17))) {
            return this.packageId + '.' + packageName;
        }
        return this.packageId;
    }
}
