/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.commands;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class D
implements CommandExecutor {
    private final Main B;
    private final me.anarchiacore.customitems.stormitemy.books.A A;

    public D(Main main, me.anarchiacore.customitems.stormitemy.books.A a2) {
        this.B = main;
        this.A = a2;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] stringArray) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getConfigManager().B("onlyPlayers")));
            return true;
        }
        Player player = (Player)commandSender;
        if (!player.hasPermission("stormitemy.books")) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getConfigManager().B("no-permission")));
            return true;
        }
        this.openBooksMenu(player);
        return true;
    }

    public void openBooksMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 &8\u1d0b\ua731\u026a\u1d07\u0262\u026a"));
        List<ItemStack> list = this.A.E();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i2 = 0; i2 < list.size() && i2 < nArray.length; ++i2) {
            inventory.setItem(nArray[i2], (ItemStack)list.get(i2));
        }
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(40, itemStack);
        player.openInventory(inventory);
    }
}

