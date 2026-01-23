package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit.holder;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.function.Consumer;
import java.util.Map;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.HumanEntity;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.holder.DreamMenuHolder;

public interface BukkitMenuHolder extends DreamMenuHolder<HumanEntity>, InventoryHolder
{
    Map<Integer, Consumer<InventoryClickEvent>> getSlotActions();
    
    void setActionOnSlot(final int slot, @NonNull final Consumer<InventoryClickEvent> consumer);
    
    void removeActionOnSlot(final int slot);
    
    void handleClick(@NonNull final InventoryInteractEvent event);
    
    void handleClose(@NonNull final InventoryCloseEvent event);
    
    void handleDrag(@NonNull final InventoryDragEvent event);
}
