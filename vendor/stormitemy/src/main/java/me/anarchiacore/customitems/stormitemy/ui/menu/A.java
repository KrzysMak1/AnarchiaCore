/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.ui.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;

public class A {
    private static final String D = "c3Rvcm1jb2Rl";
    private Main C;
    private Map<String, Integer> A = new HashMap();
    private static final int B = 28;

    public A(Main main) {
        this.C = main;
    }

    public void A(Player player) {
        if (!this.C.areItemsInitialized()) {
            player.sendMessage("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77Przedmioty s\u0105 jeszcze \u0142adowane. Spr\u00f3buj ponownie za chwil\u0119...");
            return;
        }
        List<ItemStack> list = this.C.getAllItems();
        int n2 = (int)Math.ceil((double)((double)list.size() / 28.0));
        if (n2 < 1) {
            n2 = 1;
        }
        this.A.put((Object)player.getName(), (Object)0);
        this.A(player, 0, n2, list);
    }

    private void A(Player player, int n2, int n3, List<ItemStack> list) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        }
        this.A.put((Object)player.getName(), (Object)n2);
        String string = "\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u028f (" + (n2 + 1) + "/" + n3 + ")";
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int n4 = n2 * 28;
        for (int i2 = 0; i2 < 28 && n4 + i2 < list.size(); ++i2) {
            ItemStack itemStack = (ItemStack)list.get(n4 + i2);
            if (itemStack == null || i2 >= nArray.length) continue;
            inventory.setItem(nArray[i2], itemStack.clone());
        }
        if (n3 > 1) {
            if (n2 > 0) {
                ItemStack itemStack = this.A(Material.LIGHT_BLUE_DYE, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1A99FFPoprzednia strona"));
                inventory.setItem(48, itemStack);
            }
            if (n2 < n3 - 1) {
                ItemStack itemStack = this.A(Material.LIME_DYE, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ANast\u0119pna strona"));
                itemStack.setDurability((short)10);
                inventory.setItem(50, itemStack);
            }
        }
        ItemStack itemStack = this.A(Material.BARRIER, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        inventory.setItem(49, itemStack);
        player.openInventory(inventory);
    }

    private ItemStack A(Material material, String string) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(string);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean A(Player player, int n2) {
        Integer n3 = (Integer)this.A.get((Object)player.getName());
        if (n3 == null) {
            n3 = 0;
        }
        if (!this.C.areItemsInitialized()) {
            player.sendMessage("\u00a78[\u00a7x\u00a7B\u00a73\u00a70\u00a70\u00a7F\u00a7F\ud83e\ude93\u00a78] \u00a77Przedmioty s\u0105 jeszcze \u0142adowane...");
            player.closeInventory();
            return true;
        }
        List<ItemStack> list = this.C.getAllItems();
        int n4 = (int)Math.ceil((double)((double)list.size() / 28.0));
        if (n4 < 1) {
            n4 = 1;
        }
        if (n2 == 48 && n3 > 0) {
            this.A(player, n3 - 1, n4, list);
            return true;
        }
        if (n2 == 50 && n3 < n4 - 1) {
            this.A(player, n3 + 1, n4, list);
            return true;
        }
        if (n2 == 49) {
            player.closeInventory();
            return true;
        }
        return false;
    }
}

