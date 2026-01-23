package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import org.intellij.lang.annotations.Pattern;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.minecraft.MinecraftMapping;

public interface NameableReflectiveHandle extends NamedReflectiveHandle
{
    NameableReflectiveHandle map(final MinecraftMapping p0, @Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String p1);
    
    NameableReflectiveHandle named(@Pattern("\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*") final String... p0);
}
