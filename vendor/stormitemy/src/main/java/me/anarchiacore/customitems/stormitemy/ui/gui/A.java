/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Collections
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.UUID
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.regions.C;

public class A {
    private final Main A;
    private final Map<UUID, Set<String>> C = new HashMap();
    private final Map<String, String> B = new HashMap();

    public A(Main main, C c2) {
        this.A = main;
    }

    public void A(Player player) {
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 &8\u0280\u1d07\u0262\u026a\u1d0f\u0274\u028f"));
        Material material = this.A.isWorldGuardPresent() ? Material.LIME_DYE : Material.LIGHT_GRAY_DYE;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.A.isWorldGuardPresent()) {
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#2EFF12Dodaj region"));
            itemMeta.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz doda\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7region z pluginu &fworldguard&7."), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#75FF3F\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#3DDA00\u1d18\u0280\u1d00\u1d21\u028f\u1d0d&#75FF3F, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!")));
        } else {
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Dodaj region &8[WY\u0141\u0104CZONY]"));
            itemMeta.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7W tym miejscu mo\u017cesz doda\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7region z pluginu &fworldguard&7."), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aby doda\u0107 region nie instaluj\u0105c"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7tego pluginu &f/stormitemy region&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Nie posiadasz pluginu &#FF0000WorldGuard&7!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#E13636\u1d05\u1d0f\u1d05\u1d00\u1d0a &#CF0000\u1d21\u1d0f\u0280\u029f\u1d05\u0262\u1d1c\u1d00\u0280\u1d05&#E13636, \u1d00\u0299\u028f \ua731\u1d0b\u1d0f\u0280\u1d22\u028f\u1d1b\u1d00\u0107!")));
        }
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(41, itemStack);
        ItemStack itemStack2 = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
        itemStack2.setItemMeta(itemMeta2);
        inventory.setItem(39, itemStack2);
        this.A(inventory, player);
        player.openInventory(inventory);
    }

    private void A(Inventory inventory, Player player) {
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        Set<String> regions = new HashSet();
        List<String> stormItemyRegions = this.A.getRegionManager().A();
        for (String regionName : stormItemyRegions) {
            regions.add(regionName);
            this.B.put(regionName, "STORMITEMY");
        }
        if (this.A.isWorldGuardPresent()) {
            // empty if block
        }
        Set<String> customRegions = this.C.getOrDefault(player.getUniqueId(), new HashSet());
        regions.addAll(customRegions);
        List<String> allRegions = new ArrayList(regions);
        Collections.sort(allRegions);
        for (int i2 = 0; i2 < Math.min(allRegions.size(), nArray.length); ++i2) {
            String string3 = allRegions.get(i2);
            String string4 = this.B.getOrDefault(string3, "STORMITEMY");
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + string3 + " &8(" + string4 + ")"));
            itemMeta.setLore(Arrays.asList(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0106!")));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(nArray[i2], itemStack);
        }
    }

    public void A(Player player, String string) {
        Set<String> set = this.C.computeIfAbsent(player.getUniqueId(), uUID -> new HashSet());
        set.add(string);
        String string2 = this.A.isWorldGuardPresent() ? "WORLDGUARD" : "STORMITEMY";
        this.B.put(string, string2);
    }

    public void B(Player player, String string) {
        Set<String> set = this.C.get(player.getUniqueId());
        if (set != null) {
            set.remove(string);
            if (set.isEmpty()) {
                this.C.remove(player.getUniqueId());
            }
        }
        this.B.remove(string);
    }

    public void B(String string) {
        String string2 = this.B.getOrDefault(string, "STORMITEMY");
        String string3 = string2.equals((Object)"WORLDGUARD") ? "STORMITEMY" : (this.A.isWorldGuardPresent() ? "WORLDGUARD" : "STORMITEMY");
        this.B.put(string, string3);
    }

    public String A(String string) {
        return this.B.getOrDefault(string, "STORMITEMY");
    }
}
