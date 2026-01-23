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
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.anarchiacore.customitems.stormitemy.Main;

public class B
implements Listener {
    private final Main A;

    public B(Main main) {
        this.A = main;
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        double d2 = this.A.getConfig().getDouble("customhit.damage_multiplier", 1.0);
        if (d2 == 1.0) {
            return;
        }
        double d3 = entityDamageByEntityEvent.getDamage();
        double d4 = d3 * d2;
        entityDamageByEntityEvent.setDamage(d4);
    }
}

