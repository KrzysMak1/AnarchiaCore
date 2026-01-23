/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.zaczarowania;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.zaczarowania.C;

public class A
implements Listener {
    private final Main B;
    private final C A;

    public A(Main main, C c2) {
        this.B = main;
        this.A = c2;
        main.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        int n2;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (this.A.B(itemStack) && (n2 = this.A.C(itemStack)) != 0) {
            double d2 = entityDamageByEntityEvent.getDamage();
            double d3 = 1.0 + (double)n2 / 100.0;
            double d4 = d2 * d3;
            entityDamageByEntityEvent.setDamage(d4);
        }
    }
}

