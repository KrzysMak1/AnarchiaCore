package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.inventory;

import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

class NewInventoryView extends BukkitInventoryView
{
    private final InventoryView view;
    
    public NewInventoryView(final Object view) {
        this.view = (InventoryView)view;
    }
    
    @Override
    public Inventory getTopInventory() {
        return this.view.getTopInventory();
    }
    
    @Override
    public Inventory getBottomInventory() {
        return this.view.getBottomInventory();
    }
    
    @Override
    public HumanEntity getPlayer() {
        return this.view.getPlayer();
    }
    
    @Override
    public InventoryType getType() {
        return this.view.getType();
    }
    
    @Override
    public void setItem(final int slot, final ItemStack item) {
        this.view.setItem(slot, item);
    }
    
    @Override
    public ItemStack getItem(final int slot) {
        return this.view.getItem(slot);
    }
    
    @Override
    public void setCursor(final ItemStack item) {
        this.view.setCursor(item);
    }
    
    @Override
    public ItemStack getCursor() {
        return this.view.getCursor();
    }
    
    @Override
    public int convertSlot(final int slot) {
        return this.view.convertSlot(slot);
    }
    
    @Override
    public void close() {
        this.view.close();
    }
    
    @Override
    public int countSlots() {
        return this.view.countSlots();
    }
    
    @Override
    public String getTitle() {
        return this.view.getTitle();
    }
    
    public InventoryView object() {
        return this.view;
    }
}
