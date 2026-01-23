package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import org.intellij.lang.annotations.Language;

public interface PackageHandle
{
    @Language("RegExp")
    @ApiStatus.Internal
    public static final String JAVA_PACKAGE_PATTERN = "(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    @Language("RegExp")
    @ApiStatus.Internal
    public static final String JAVA_IDENTIFIER_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
    
    @NotNull
    String packageId();
    
    @NotNull
    String getBasePackageName();
    
    @NotNull
    String getPackage(@NotNull final String p0);
}
