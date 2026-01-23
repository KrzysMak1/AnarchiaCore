/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerResourcePackStatusEvent
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.texturepack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.texturepack.B;

public class A
implements Listener {
    private final Plugin B;
    private final B A;

    public A(Plugin javaPlugin, B b2) {
        this.B = javaPlugin;
        this.A = b2;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        if (!this.A.A()) {
            return;
        }
        Player player = playerJoinEvent.getPlayer();
        this.A.B(player);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        String string;
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        if (inventoryClickEvent.getView().getTitle() == null) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        String string2 = inventoryClickEvent.getView().getTitle();
        if (!string2.equals((Object)(string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.I())))) {
            return;
        }
        inventoryClickEvent.setCancelled(true);
        int n2 = inventoryClickEvent.getRawSlot();
        this.A.A(player, n2, string2);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onResourcePackStatus(PlayerResourcePackStatusEvent playerResourcePackStatusEvent) {
        if (!this.A.A()) {
            return;
        }
        Player player = playerResourcePackStatusEvent.getPlayer();
        this.A.A(player, playerResourcePackStatusEvent.getStatus());
    }
}

