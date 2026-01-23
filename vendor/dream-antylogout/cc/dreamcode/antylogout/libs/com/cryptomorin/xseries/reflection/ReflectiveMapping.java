package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.NamedReflectiveHandle;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface ReflectiveMapping
{
    boolean shouldBeChecked();
    
    String category();
    
    String name();
    
    String process(final NamedReflectiveHandle p0, final String p1);
}
