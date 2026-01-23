/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class E
implements CommandExecutor {
    private final Main A;

    public E(Main main) {
        this.A = main;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (!commandSender.isOp() && !commandSender.hasPermission("stormitemy.admin")) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie masz uprawnie\u0144 do u\u017cycia tej komendy!"));
            return true;
        }
        if (stringArray.length == 0) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cPoprawne u\u017cycie: /menuprzedmioty open <gracz>"));
            return true;
        }
        if (!stringArray[0].equalsIgnoreCase("open")) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cPoprawne u\u017cycie: /menuprzedmioty open <gracz>"));
            return true;
        }
        if (stringArray.length != 2) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cPoprawne u\u017cycie: /menuprzedmioty open <gracz>"));
            return true;
        }
        Player player = Bukkit.getPlayer((String)stringArray[1]);
        if (player == null) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie znaleziono gracza!"));
            return true;
        }
        this.A(player);
        commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&aOtworzono menu podgl\u0105du przedmiot\u00f3w dla gracza &f" + player.getName()));
        return true;
    }

    private void A(Player player) {
        if (!this.A.areItemsInitialized()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cPrzedmioty s\u0105 jeszcze \u0142adowane. Spr\u00f3buj ponownie za chwil\u0119..."));
            return;
        }
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u028f \u1d07\u1d20\u1d07\u0274\u1d1b\u1d0f\u1d21\u1d07"));
        for (ItemStack itemStack : this.A.getAllItems()) {
            if (itemStack == null) continue;
            inventory.addItem(new ItemStack[]{itemStack});
        }
        this.A.getServer().getPluginManager().registerEvents(new Listener(){

            @EventHandler
            public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
                if (inventoryClickEvent.getView().getTitle().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u028f \u1d07\u1d20\u1d07\u0274\u1d1b\u1d0f\u1d21\u1d07"))) {
                    inventoryClickEvent.setCancelled(true);
                }
            }
        }, (Plugin)this.A);
        player.openInventory(inventory);
    }
}

