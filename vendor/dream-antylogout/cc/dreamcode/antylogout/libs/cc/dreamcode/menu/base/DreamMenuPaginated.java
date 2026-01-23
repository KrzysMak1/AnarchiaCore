package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.base;

import java.util.function.Consumer;
import lombok.NonNull;
import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface DreamMenuPaginated<R, M, I, E, H>
{
    M getMenuPlatform();
    
    List<Integer> getStorageItemSlots();
    
    Optional<M> getMenuByPage(final int page);
    
    Map<Integer, M> getMenuPages();
    
    int getSize();
    
    List<H> getViewers();
    
    int getPlayerPage(@NonNull final H h);
    
    R setNextPageSlot(final int slot, @NonNull final Consumer<H> lastPageReach);
    
    R setNextPageSlot(final int slot, @NonNull final I i, @NonNull final Consumer<H> lastPageReach);
    
    R setNextPageSlot(final int x, final int z, @NonNull final Consumer<H> lastPageReach);
    
    R setNextPageSlot(final int x, final int z, @NonNull final I i, @NonNull final Consumer<H> lastPageReach);
    
    R setPreviousPageSlot(final int slot, @NonNull final Consumer<H> firstPageReach);
    
    R setPreviousPageSlot(final int slot, @NonNull final I i, @NonNull final Consumer<H> firstPageReach);
    
    R setPreviousPageSlot(final int x, final int z, @NonNull final Consumer<H> firstPageReach);
    
    R setPreviousPageSlot(final int x, final int z, @NonNull final I i, @NonNull final Consumer<H> firstPageReach);
    
    R addStorageItem(@NonNull final M m, final int page, @NonNull final I i, final Consumer<E> event);
    
    R addStorageItem(@NonNull final I i, final Consumer<E> event);
    
    R addStorageItem(@NonNull final I i);
    
    R addStorageItems(@NonNull final List<I> list, final Consumer<E> event);
    
    R addStorageItems(@NonNull final List<I> list);
    
    R open(final int page, @NonNull final H h);
    
    R openPage(@NonNull final H h);
    
    R openFirstPage(@NonNull final H h);
    
    R openLastPage(@NonNull final H h);
    
    R dispose();
}
