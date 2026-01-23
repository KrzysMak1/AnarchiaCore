package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.AbstractReferencedClass;

public abstract class BukkitInventoryView extends AbstractReferencedClass<InventoryView>
{
    public abstract Inventory getTopInventory();
    
    public abstract Inventory getBottomInventory();
    
    public abstract HumanEntity getPlayer();
    
    public abstract InventoryType getType();
    
    public abstract void setItem(final int p0, final ItemStack p1);
    
    public abstract ItemStack getItem(final int p0);
    
    public abstract void setCursor(final ItemStack p0);
    
    public abstract ItemStack getCursor();
    
    public abstract int convertSlot(final int p0);
    
    public abstract void close();
    
    public abstract int countSlots();
    
    public abstract String getTitle();
}
