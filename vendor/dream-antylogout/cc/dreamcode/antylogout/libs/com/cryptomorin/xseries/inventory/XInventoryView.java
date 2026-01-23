package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.inventory;

import org.bukkit.inventory.InventoryView;

public final class XInventoryView
{
    private static final boolean USE_INTERFACE;
    
    private XInventoryView() {
    }
    
    public static BukkitInventoryView of(final InventoryView inventoryView) {
        if (XInventoryView.USE_INTERFACE) {
            return new NewInventoryView(inventoryView);
        }
        return new OldInventoryView(inventoryView);
    }
    
    static {
        USE_INTERFACE = InventoryView.class.isInterface();
    }
}
