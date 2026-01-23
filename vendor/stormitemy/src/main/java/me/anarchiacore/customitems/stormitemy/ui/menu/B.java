/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 */
package me.anarchiacore.customitems.stormitemy.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.anarchiacore.customitems.stormitemy.Main;

public class B
implements Listener {
    private final Main A;

    public B(Main main) {
        this.A = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string = inventoryClickEvent.getView().getTitle();
        if (string.startsWith("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u028f ")) {
            if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
                return;
            }
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            int n2 = inventoryClickEvent.getRawSlot();
            if (n2 >= 45 && n2 <= 53) {
                inventoryClickEvent.setCancelled(true);
                if (n2 == 49) {
                    this.A.getPanelCommand().openPanelGUI(player);
                } else if ((n2 == 48 || n2 == 50) && inventoryClickEvent.getCurrentItem() != null) {
                    this.A.getMenuManager().A(player, n2);
                }
            }
        }
    }
}

