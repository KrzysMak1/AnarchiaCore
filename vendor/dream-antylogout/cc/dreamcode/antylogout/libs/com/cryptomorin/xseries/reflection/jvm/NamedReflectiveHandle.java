package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm;

import org.jetbrains.annotations.NotNull;
import java.util.Set;

public interface NamedReflectiveHandle
{
    @NotNull
    Set<String> getPossibleNames();
}
