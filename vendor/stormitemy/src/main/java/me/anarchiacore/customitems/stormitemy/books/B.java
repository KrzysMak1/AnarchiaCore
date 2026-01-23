/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;

public class B
implements Listener {
    private final Main B;
    private final A A;

    public B(Main main, A a2) {
        this.B = main;
        this.A = a2;
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string = inventoryClickEvent.getView().getTitle();
        if (string.equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 &8\u1d0b\ua731\u026a\u1d07\u0262\u026a"))) {
            Player player = (Player)inventoryClickEvent.getWhoClicked();
            int n2 = inventoryClickEvent.getRawSlot();
            inventoryClickEvent.setCancelled(true);
            if (n2 == 40) {
                this.B.getPanelCommand().openPanelGUI(player);
                return;
            }
            int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
            boolean bl = false;
            for (int n3 : nArray) {
                if (n2 != n3) continue;
                bl = true;
                break;
            }
            if (bl && inventoryClickEvent.getCurrentItem() != null && this.A.C(inventoryClickEvent.getCurrentItem())) {
                ItemStack itemStack = inventoryClickEvent.getCurrentItem().clone();
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(new ItemStack[]{itemStack});
                } else {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cTw\u00f3j ekwipunek jest pe\u0142ny!"));
                }
            }
        }
    }
}

