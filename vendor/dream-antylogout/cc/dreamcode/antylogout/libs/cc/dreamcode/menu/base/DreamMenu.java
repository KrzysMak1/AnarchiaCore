package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.base;

import java.util.function.Consumer;
import java.util.List;
import lombok.NonNull;

public interface DreamMenu<R, T, I, E, H, P>
{
    int getPage();
    
    R addItem(@NonNull final I i);
    
    R addItem(@NonNull final I i, @NonNull final List<Integer> applySlots);
    
    R addItem(@NonNull final I i, @NonNull final Consumer<E> event);
    
    R addItem(@NonNull final I i, @NonNull final List<Integer> applySlots, @NonNull final Consumer<E> event);
    
    R setItem(final int slot, @NonNull final I i);
    
    R setItem(final int slot, @NonNull final I i, @NonNull final Consumer<E> event);
    
    R setItem(final int x, final int z, @NonNull final I i);
    
    R setItem(final int x, final int z, @NonNull final I i, @NonNull final Consumer<E> event);
    
    R setItem(final int[] slots, @NonNull final I i);
    
    R setItem(final int[] slots, @NonNull final I i, @NonNull final Consumer<E> event);
    
    R fillInventoryWith(@NonNull final I i);
    
    R fillInventoryWith(@NonNull final I i, @NonNull final Consumer<E> event);
    
    H getHolder();
    
    R open(@NonNull final P p);
    
    T toPaginated();
    
    R dispose();
}
