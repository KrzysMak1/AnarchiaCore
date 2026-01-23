/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.ui.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class C {
    private final Main C;
    private final E A;
    private static final int B = 28;
    private static final String D = "\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21";

    public C(Main main, E e2) {
        this.C = main;
        this.A = e2;
    }

    public void A(Player player) {
        this.A(player, 0);
    }

    public void A(Player player, int n2) {
        ItemStack itemStack;
        String string;
        Map.Entry entry;
        Map<String, ItemStack> map = this.A.B();
        int n3 = map.size();
        int n4 = Math.max((int)1, (int)((int)Math.ceil((double)((double)n3 / 28.0))));
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= n4) {
            n2 = n4 - 1;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21 (" + (n2 + 1) + "/" + n4 + ")");
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string2);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        ArrayList arrayList = new ArrayList((Collection)map.entrySet());
        int n5 = n2 * 28;
        for (int i2 = 0; i2 < 28 && n5 + i2 < arrayList.size(); ++i2) {
            entry = (Map.Entry)arrayList.get(n5 + i2);
            string = (String)entry.getKey();
            itemStack = (ItemStack)entry.getValue();
            if (itemStack == null || i2 >= nArray.length) continue;
            ItemStack itemStack2 = itemStack.clone();
            ItemMeta itemMeta = itemStack2.getItemMeta();
            if (itemMeta != null) {
                ArrayList arrayList2 = itemMeta.hasLore() ? new ArrayList((Collection)itemMeta.getLore()) : new ArrayList();
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7ID: &f" + string));
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#00B5FF&n\u029f\u1d07\u1d21\u028f\u1d0d&7, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107 \u1d05\u1d0f \u1d07\u01eb!"));
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#2DFF00&n\u1d18\u0280\u1d00\u1d21\u028f\u1d0d&7, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
                arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107!"));
                itemMeta.setLore((List)arrayList2);
                itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DYE});
                itemStack2.setItemMeta(itemMeta);
            }
            inventory.setItem(nArray[i2], itemStack2);
        }
        if (n4 > 1) {
            if (n2 > 0) {
                ItemStack itemStack3 = this.A(Material.LIGHT_BLUE_DYE, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1A99FFPoprzednia strona"));
                inventory.setItem(48, itemStack3);
            }
            if (n2 < n4 - 1) {
                ItemStack itemStack4 = this.A(Material.LIME_DYE, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ANast\u0119pna strona"));
                inventory.setItem(50, itemStack4);
            }
        }
        ItemStack itemStack5 = this.A(Material.BARRIER, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        inventory.setItem(49, itemStack5);
        entry = new ItemStack(Material.AMETHYST_SHARD);
        string = entry.getItemMeta();
        string.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFDodaj nowy przedmiot"));
        itemStack = new ArrayList();
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kliknij aby &fdoda\u0107"));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7nowy &fw\u0142asny przedmiot&7!"));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!"));
        string.setLore((List)itemStack);
        string.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        entry.setItemMeta((ItemMeta)string);
        inventory.setItem(52, (ItemStack)entry);
        player.openInventory(inventory);
    }

    private ItemStack A(Material material, String string) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(string);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String B() {
        return D;
    }

    public E A() {
        return this.A;
    }
}

