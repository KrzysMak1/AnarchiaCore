package cc.dreamcode.antylogout.libs.cc.dreamcode.menu;

import java.util.function.Consumer;
import lombok.NonNull;

public interface DreamMenuProvider<D, T, P>
{
    D createDefault(@NonNull final String title, final int rows);
    
    D createDefault(@NonNull final String title, final int rows, @NonNull final Consumer<D> consumer);
    
    D createDefault(@NonNull final T type, @NonNull final String title);
    
    D createDefault(@NonNull final T type, @NonNull final String title, @NonNull final Consumer<D> consumer);
    
    P createPaginated(@NonNull final D d);
    
    P createPaginated(@NonNull final D d, @NonNull final Consumer<D> consumer);
}
