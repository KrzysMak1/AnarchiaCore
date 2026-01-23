/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.ui.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class D {
    private final Main C;
    private final E A;
    private static final String B = "\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d0b\u0280\u1d07\u1d00\u1d1b\u1d0f\u0280 ";

    public D(Main main, E e2) {
        this.C = main;
        this.A = e2;
    }

    public void E(Player player, String string) {
        ArrayList arrayList;
        ItemMeta itemMeta;
        ItemStack itemStack;
        ItemFlag itemFlag2;
        Map map;
        ItemStack itemStack2 = this.A.G(string);
        if (itemStack2 == null) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie znaleziono przedmiotu o ID: " + string));
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(B + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string3);
        ItemStack itemStack3 = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta2 = itemStack3.getItemMeta();
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 nazw\u0119 przedmiotu"));
        String string4 = itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasDisplayName() ? itemStack2.getItemMeta().getDisplayName() : "&f" + string;
        itemMeta2.setLore(Arrays.asList((Object[])new String[]{me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fnazw\u0119 &7przedmiotu!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna nazwa przedmiotu:"), string4, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!")}));
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta2);
        inventory.setItem(11, itemStack3);
        ItemStack itemStack4 = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta itemMeta3 = itemStack4.getItemMeta();
        itemMeta3.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 lore przedmiotu"));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &flore &7przedmiotu!"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Aktualna lore:"));
        ArrayList arrayList3 = itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasLore() ? itemStack2.getItemMeta().getLore() : new ArrayList();
        int n2 = Math.min((int)9, (int)arrayList3.size());
        for (int i2 = 0; i2 < n2; ++i2) {
            arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + (String)arrayList3.get(i2)));
        }
        if (arrayList3.size() > 9) {
            arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (arrayList3.size() - 9) + " wi\u0119cej)"));
        }
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta3.setLore((List)arrayList2);
        itemMeta3.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack4.setItemMeta(itemMeta3);
        inventory.setItem(12, itemStack4);
        ItemStack itemStack5 = new ItemStack(Material.BIRCH_SIGN);
        ItemMeta itemMeta4 = itemStack5.getItemMeta();
        itemMeta4.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 Custom Model Data"));
        int n3 = itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasCustomModelData() ? itemStack2.getItemMeta().getCustomModelData() : 0;
        itemMeta4.setLore(Arrays.asList((Object[])new String[]{me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &ftexture &7przedmiotu!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualnie cmd: &f" + n3), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!")}));
        itemMeta4.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack5.setItemMeta(itemMeta4);
        inventory.setItem(13, itemStack5);
        ItemStack itemStack6 = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta itemMeta5 = itemStack6.getItemMeta();
        itemMeta5.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 enchantmenty przedmiotu"));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fenchantmenty &7przedmiotu!"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne enchantmenty:"));
        if (itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasEnchants()) {
            map = itemStack2.getItemMeta().getEnchants();
            int n4 = 0;
            for (Map.Entry entry : map.entrySet()) {
                if (n4 >= 9) {
                    arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (map.size() - 9) + " wi\u0119cej)"));
                    break;
                }
                arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + ((Enchantment)entry.getKey()).getName() + " " + String.valueOf((Object)entry.getValue())));
                ++n4;
            }
        } else {
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fBRAK ENCHANTMENT\u00d3W"));
        }
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta5.setLore((List)arrayList4);
        itemMeta5.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack6.setItemMeta(itemMeta5);
        inventory.setItem(14, itemStack6);
        map = new ItemStack(Material.MAGENTA_BANNER);
        ItemMeta itemMeta6 = map.getItemMeta();
        itemMeta6.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 flagi przedmiotu"));
        Iterator iterator = new ArrayList();
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fflagi &7przedmiotu!"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne flagi:"));
        if (itemStack2.hasItemMeta() && !itemStack2.getItemMeta().getItemFlags().isEmpty()) {
            int n5 = 0;
            for (ItemFlag itemFlag2 : itemStack2.getItemMeta().getItemFlags()) {
                if (n5 >= 9) {
                    iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (itemStack2.getItemMeta().getItemFlags().size() - 9) + " wi\u0119cej)"));
                    break;
                }
                iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + itemFlag2.name()));
                ++n5;
            }
        } else {
            iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fBRAK FLAG"));
        }
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta6.setLore((List)iterator);
        itemMeta6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        map.setItemMeta(itemMeta6);
        inventory.setItem(15, (ItemStack)map);
        Material material = this.A.A(string);
        Iterator iterator2 = new ItemStack(material);
        itemFlag2 = iterator2.getItemMeta();
        itemFlag2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 rodzaj przedmiotu"));
        itemFlag2.setLore(Arrays.asList((Object[])new String[]{me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &frodzaj &7przedmiotu (material)!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny material: &f" + material.name()), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!")}));
        itemFlag2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DYE});
        iterator2.setItemMeta((ItemMeta)itemFlag2);
        inventory.setItem(20, (ItemStack)iterator2);
        int n6 = this.A.J(string);
        ItemStack itemStack7 = new ItemStack(Material.CLOCK);
        ItemMeta itemMeta7 = itemStack7.getItemMeta();
        itemMeta7.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 cooldown przedmiotu"));
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fcooldown &7przedmiotu!"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny cooldown: &f" + n6 + "s"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta7.setLore((List)arrayList5);
        itemMeta7.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack7.setItemMeta(itemMeta7);
        inventory.setItem(21, itemStack7);
        boolean bl = itemStack2.hasItemMeta() && itemStack2.getItemMeta().isUnbreakable();
        ItemStack itemStack8 = new ItemStack(Material.BEDROCK);
        ItemMeta itemMeta8 = itemStack8.getItemMeta();
        itemMeta8.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFCzy ma posiada\u0107 Unbreakable"));
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fw\u0142\u0105czy\u0107 i wy\u0142\u0105czy\u0107"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &funbreakable &7na przedmiocie!"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Status: " + (bl ? "&fW\u0141\u0104CZONE" : "&fWY\u0141\u0104CZONE")));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\u029f\u1d00\u1d04\u1d22\u028f\u0107!"));
        itemMeta8.setLore((List)arrayList6);
        itemMeta8.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack8.setItemMeta(itemMeta8);
        inventory.setItem(22, itemStack8);
        ItemStack itemStack9 = new ItemStack(Material.LIGHT_BLUE_CANDLE);
        ItemMeta itemMeta9 = itemStack9.getItemMeta();
        itemMeta9.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 kolor cooldownu"));
        String string5 = this.A.D(string);
        String string6 = string5.replace((CharSequence)"&", (CharSequence)"");
        ArrayList arrayList7 = new ArrayList();
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fkolor cooldownu &7na actionbarze!"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny kolor: " + string5 + string6));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta9.setLore((List)arrayList7);
        itemMeta9.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack9.setItemMeta(itemMeta9);
        inventory.setItem(23, itemStack9);
        List<Map<String, Object>> list = this.A.M(string);
        List<Map<String, Object>> list2 = this.A.F(string);
        ItemStack itemStack10 = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta itemMeta10 = itemStack10.getItemMeta();
        itemMeta10.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFUmiej\u0119tno\u015bci przedmiotu"));
        ArrayList arrayList8 = new ArrayList();
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fdodawa\u0107"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fumiej\u0119tno\u015bci &7i &fefekty wizualne&7!"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Umiej\u0119tno\u015bci: &f" + list.size() + "/7"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Efekty wizualne: &f" + list2.size() + "/7"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList8.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!"));
        itemMeta10.setLore((List)arrayList8);
        itemMeta10.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack10.setItemMeta(itemMeta10);
        inventory.setItem(24, itemStack10);
        int n7 = this.A.C(string);
        ItemStack itemStack11 = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta itemMeta11 = itemStack11.getItemMeta();
        itemMeta11.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFSzansa na zadzia\u0142anie"));
        ArrayList arrayList9 = new ArrayList();
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fszans\u0119 &7na zadzia\u0142anie przedmiotu!"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna szansa: &f" + n7 + "%"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList9.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta11.setLore((List)arrayList9);
        itemMeta11.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack11.setItemMeta(itemMeta11);
        inventory.setItem(30, itemStack11);
        ItemStack itemStack12 = this.A.G(string);
        String string7 = this.A.E(string);
        if (itemStack12 != null && itemStack12.getType().isBlock()) {
            itemStack = new ItemStack(Material.REPEATER);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFTyp aktywacji"));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wybierz typ aktywacji:"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   " + (string7.equals((Object)"ATTACK") ? "&#1DFF1A" : "&f") + "\u2022 ATTACK &7- uderzenie"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   " + (string7.equals((Object)"BLOCK_PLACE") ? "&#1DFF1A" : "&f") + "\u2022 BLOCK_PLACE &7- postawienie"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny: &f" + string7));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d22\u1d0d\u026a\u1d07\u0274\u026a\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(31, itemStack);
        } else if (itemStack12 != null) {
            itemStack = new ItemStack(Material.TRIPWIRE_HOOK);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFSpos\u00f3b aktywacji"));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wybierz spos\u00f3b aktywacji:"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   " + (string7.equals((Object)"ATTACK") ? "&#1DFF1A" : "&f") + "\u2022 ATTACK &7- uderzenie kogo\u015b"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   " + (string7.equals((Object)"LEFT_CLICK") ? "&#1DFF1A" : "&f") + "\u2022 LEFT_CLICK &7- klikni\u0119cie lewym"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   " + (string7.equals((Object)"RIGHT_CLICK") ? "&#1DFF1A" : "&f") + "\u2022 RIGHT_CLICK &7- klikni\u0119cie prawym"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny: &f" + string7));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d22\u1d0d\u026a\u1d07\u0274\u026a\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(31, itemStack);
        }
        itemStack = new ItemStack(Material.PAPER);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFKonfiguracja wiadomo\u015bci"));
        arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fkonfigurowa\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fwiadomo\u015bci &7wy\u015bwietlane graczom!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015bci dla &fu\u017cywaj\u0105cego&7:"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   - &fTitle&7, &fSubtitle&7, &fChat"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015bci dla &fprzeciwnika&7:"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   - &fTitle&7, &fSubtitle&7, &fChat"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d1b\u1d21\u1d0f\u0280\u1d22\u028f\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(32, itemStack);
        ItemStack itemStack13 = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta12 = itemStack13.getItemMeta();
        itemMeta12.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do listy"));
        itemMeta12.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack13.setItemMeta(itemMeta12);
        inventory.setItem(48, itemStack13);
        boolean bl2 = !this.A.B(string);
        Material material2 = bl2 ? Material.LIME_DYE : Material.RED_DYE;
        ItemStack itemStack14 = new ItemStack(material2);
        ItemMeta itemMeta13 = itemStack14.getItemMeta();
        itemMeta13.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(bl2 ? "&#1DFF1AW\u0142\u0105czony" : "&#FF0000Wy\u0142\u0105czony"));
        ArrayList arrayList10 = new ArrayList();
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fw\u0142\u0105czy\u0107 &7i &fwy\u0142\u0105czy\u0107"));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fprzedmiot &7w grze!"));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Status: " + (bl2 ? "&fW\u0141\u0104CZONY" : "&fWY\u0141\u0104CZONY")));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList10.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\u029f\u1d00\u1d04\u1d22\u028f\u0107!"));
        itemMeta13.setLore((List)arrayList10);
        itemMeta13.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack14.setItemMeta(itemMeta13);
        inventory.setItem(50, itemStack14);
        player.openInventory(inventory);
    }

    public void C(Player player, String string) {
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d21\u026a\u1d00\u1d05\u1d0f\u1d0d\u1d0f\u015b\u1d04\u026a " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        String string4 = this.A.C(string, "title");
        ItemStack itemStack = new ItemStack(Material.OAK_SIGN);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Title (u\u017cywaj\u0105cy)"));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fTitle &7dla"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fu\u017cywaj\u0105cego &7przedmiot."));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string4.isEmpty() ? "&7(brak)" : string4)));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(10, itemStack);
        String string5 = this.A.C(string, "subtitle");
        ItemStack itemStack2 = new ItemStack(Material.BIRCH_SIGN);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Subtitle (u\u017cywaj\u0105cy)"));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fSubtitle &7dla"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fu\u017cywaj\u0105cego &7przedmiot."));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string5.isEmpty() ? "&7(brak)" : string5)));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta2.setLore((List)arrayList2);
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack2.setItemMeta(itemMeta2);
        inventory.setItem(11, itemStack2);
        String string6 = this.A.C(string, "chat");
        ItemStack itemStack3 = new ItemStack(Material.PAPER);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Chat (u\u017cywaj\u0105cy)"));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fChat &7dla"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fu\u017cywaj\u0105cego &7przedmiot."));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string6.isEmpty() ? "&7(brak)" : string6)));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta3.setLore((List)arrayList3);
        itemMeta3.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta3);
        inventory.setItem(12, itemStack3);
        String string7 = this.A.E(string, "title");
        ItemStack itemStack4 = new ItemStack(Material.OAK_SIGN);
        ItemMeta itemMeta4 = itemStack4.getItemMeta();
        itemMeta4.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#00D2FFTitle (przeciwnik)"));
        ArrayList arrayList4 = new ArrayList();
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fTitle &7dla"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fprzeciwnika&7."));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string7.isEmpty() ? "&7(brak)" : string7)));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta4.setLore((List)arrayList4);
        itemMeta4.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack4.setItemMeta(itemMeta4);
        inventory.setItem(14, itemStack4);
        String string8 = this.A.E(string, "subtitle");
        ItemStack itemStack5 = new ItemStack(Material.BIRCH_SIGN);
        ItemMeta itemMeta5 = itemStack5.getItemMeta();
        itemMeta5.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#00D2FFSubtitle (przeciwnik)"));
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fSubtitle &7dla"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fprzeciwnika&7."));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string8.isEmpty() ? "&7(brak)" : string8)));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta5.setLore((List)arrayList5);
        itemMeta5.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack5.setItemMeta(itemMeta5);
        inventory.setItem(15, itemStack5);
        String string9 = this.A.E(string, "chat");
        ItemStack itemStack6 = new ItemStack(Material.PAPER);
        ItemMeta itemMeta6 = itemStack6.getItemMeta();
        itemMeta6.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#00D2FFChat (przeciwnik)"));
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Wiadomo\u015b\u0107 &fChat &7dla"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7gracza &fprzeciwnika&7."));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna: &f" + (string9.isEmpty() ? "&7(brak)" : string9)));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000\ua731\u029c\u026a\ua730\u1d1b+\u1d18\u1d18\u1d0d \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u0105\u0107"));
        itemMeta6.setLore((List)arrayList6);
        itemMeta6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack6.setItemMeta(itemMeta6);
        inventory.setItem(16, itemStack6);
        ItemStack itemStack7 = new ItemStack(Material.BOOK);
        ItemMeta itemMeta7 = itemStack7.getItemMeta();
        itemMeta7.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFInformacje"));
        ArrayList arrayList7 = new ArrayList();
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Dost\u0119pne placeholdery:"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   &f{PLAYER} &7- nazwa u\u017cywaj\u0105cego"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   &f{TARGET} &7- nazwa przeciwnika"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Mo\u017cesz u\u017cywa\u0107 &fkolor\u00f3w&7:"));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8   &f&a&7, &f&#FF5555&7, itp."));
        arrayList7.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemMeta7.setLore((List)arrayList7);
        itemMeta7.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack7.setItemMeta(itemMeta7);
        inventory.setItem(22, itemStack7);
        ItemStack itemStack8 = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta8 = itemStack8.getItemMeta();
        itemMeta8.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        itemMeta8.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack8.setItemMeta(itemMeta8);
        inventory.setItem(39, itemStack8);
        player.openInventory(inventory);
    }

    public void F(Player player, String string) {
        ItemStack itemStack = this.A.G(string);
        if (itemStack == null) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u029f\u1d0f\u0280\u1d07 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        ArrayList arrayList = itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i2 = 0; i2 < arrayList.size() && i2 < nArray.length; ++i2) {
            inventory.setItem(nArray[i2], this.A((String)arrayList.get(i2), i2 + 1));
        }
        ItemStack itemStack2 = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack2.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack2.setItemMeta(itemMeta);
        inventory.setItem(39, itemStack2);
        boolean bl = arrayList.size() < 7;
        ItemStack itemStack3 = new ItemStack(bl ? Material.LIME_DYE : Material.LIGHT_GRAY_DYE);
        ItemMeta itemMeta2 = itemStack3.getItemMeta();
        if (bl) {
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj linijk\u0119 lore"));
        } else {
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Dodaj linijk\u0119 lore"));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cOsi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 linijek (7)"));
            itemMeta2.setLore((List)arrayList2);
        }
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta2);
        inventory.setItem(41, itemStack3);
        player.openInventory(inventory);
    }

    public void G(Player player, String string) {
        ArrayList arrayList;
        ItemMeta itemMeta;
        ItemStack itemStack;
        Map.Entry entry2;
        ItemStack itemStack2 = this.A.G(string);
        if (itemStack2 == null) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d07\u0274\u1d04\u029c\u1d00\u0274\u1d1b\u00f3\u1d21 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        HashMap hashMap = itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasEnchants() ? itemStack2.getItemMeta().getEnchants() : new HashMap();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = 0;
        for (Map.Entry entry2 : hashMap.entrySet()) {
            if (n2 >= nArray.length) break;
            itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FF" + ((Enchantment)entry2.getKey()).getName() + " " + String.valueOf((Object)entry2.getValue())));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(nArray[n2], itemStack);
            ++n2;
        }
        Iterator iterator = new ItemStack(Material.BARRIER);
        entry2 = iterator.getItemMeta();
        entry2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        entry2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        iterator.setItemMeta((ItemMeta)entry2);
        inventory.setItem(39, (ItemStack)iterator);
        itemStack = new ItemStack(Material.LIME_DYE);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj enchantment"));
        arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kliknij aby doda\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7nowy enchantment!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(41, itemStack);
        player.openInventory(inventory);
    }

    public void A(Player player, String string) {
        ArrayList arrayList;
        ItemMeta itemMeta;
        ItemStack itemStack;
        ItemFlag itemFlag2;
        ItemStack itemStack2 = this.A.G(string);
        if (itemStack2 == null) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \ua730\u029f\u1d00\u0262 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        HashSet hashSet = itemStack2.hasItemMeta() ? itemStack2.getItemMeta().getItemFlags() : new HashSet();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = 0;
        for (ItemFlag itemFlag2 : hashSet) {
            if (n2 >= nArray.length) break;
            itemStack = new ItemStack(Material.PAPER);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD700" + itemFlag2.name()));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Flaga: &f" + itemFlag2.name()));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(nArray[n2], itemStack);
            ++n2;
        }
        Iterator iterator = new ItemStack(Material.BARRIER);
        itemFlag2 = iterator.getItemMeta();
        itemFlag2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        itemFlag2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        iterator.setItemMeta((ItemMeta)itemFlag2);
        inventory.setItem(39, (ItemStack)iterator);
        itemStack = new ItemStack(Material.LIME_DYE);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj flag\u0119"));
        arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kliknij aby doda\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7now\u0105 flag\u0119!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(41, itemStack);
        player.openInventory(inventory);
    }

    private ItemStack A(String string, int n2) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD700Linia #" + n2));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7" + string));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#00D2FF&n\u029f\u1d07\u1d21\u028f\u1d0d&7, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\ua731\u1d1c\u0274\u1d00\u1d04 \u1d21 \u0262\u00f3\u0280\u1d07!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#03FF00&n\u1d18\u0280\u1d00\u1d21\u028f\u1d0d&7, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\ua731\u1d1c\u0274\u1d00\u1d04 \u1d21 \u1d05\u00f3\u029f!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String A(String string) {
        return string.replace((CharSequence)"\u0105", (CharSequence)"a").replace((CharSequence)"\u0107", (CharSequence)"c").replace((CharSequence)"\u0119", (CharSequence)"e").replace((CharSequence)"\u0142", (CharSequence)"l").replace((CharSequence)"\u0144", (CharSequence)"n").replace((CharSequence)"\u00f3", (CharSequence)"o").replace((CharSequence)"\u015b", (CharSequence)"s").replace((CharSequence)"\u017a", (CharSequence)"z").replace((CharSequence)"\u017c", (CharSequence)"z").replace((CharSequence)"\u0104", (CharSequence)"A").replace((CharSequence)"\u0106", (CharSequence)"C").replace((CharSequence)"\u0118", (CharSequence)"E").replace((CharSequence)"\u0141", (CharSequence)"L").replace((CharSequence)"\u0143", (CharSequence)"N").replace((CharSequence)"\u00d3", (CharSequence)"O").replace((CharSequence)"\u015a", (CharSequence)"S").replace((CharSequence)"\u0179", (CharSequence)"Z").replace((CharSequence)"\u017b", (CharSequence)"Z");
    }

    public static String B() {
        return B;
    }

    public E A() {
        return this.A;
    }

    public void D(Player player, String string) {
        ItemMeta itemMeta;
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(this.A(string));
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d1c\u1d0d\u026a\u1d07\u1d0a\u1d07\u1d1b\u0274\u1d0f\u015b\u1d04\u026a " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string3);
        List<Map<String, Object>> list = this.A.M(string);
        List<Map<String, Object>> list2 = this.A.F(string);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        for (int i2 = 0; i2 < list.size() && i2 < nArray.length; ++i2) {
            Map map = (Map)list.get(i2);
            inventory.setItem(nArray[i2], this.A((Map<String, Object>)map, i2, false));
        }
        int[] nArray2 = new int[]{28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        for (int i3 = 0; i3 < list2.size() && i3 < nArray2.length; ++i3) {
            itemMeta = (Map)list2.get(i3);
            inventory.setItem(nArray2[i3], this.A((Map<String, Object>)itemMeta, i3, true));
        }
        ItemStack itemStack = new ItemStack(Material.ENDER_EYE);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#00D2FFDodaj efekt wizualny"));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Dodaj &fefekty wizualne&7 jak:"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fpioruny&7, &fparticle&7, &fd\u017awi\u0119ki&7..."));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne: &f" + list2.size() + "/7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(47, itemStack);
        ItemStack itemStack2 = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack2.setItemMeta(itemMeta2);
        inventory.setItem(49, itemStack2);
        ItemStack itemStack3 = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Dodaj umiej\u0119tno\u015b\u0107"));
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Dodaj &fumiej\u0119tno\u015bci&7 jak:"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fzabranie HP&7, &fefekty&7, &fobra\u017cenia&7..."));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne: &f" + list.size() + "/7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!"));
        itemMeta3.setLore((List)arrayList2);
        itemMeta3.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta3);
        inventory.setItem(51, itemStack3);
        player.openInventory(inventory);
    }

    public void H(Player player, String string) {
        this.A(player, string, 0);
    }

    public void A(Player player, String string, int n2) {
        ItemMeta itemMeta;
        String[][] stringArray = D.D();
        int n3 = (int)Math.ceil((double)((double)stringArray.length / 28.0));
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d21\u028f\u0299\u026a\u1d07\u0280\u1d22 \u1d1c\u1d0d\u026a\u1d07\u1d0a\u1d07\u1d1b\u0274\u1d0f\u015b\u0107 (" + (n2 + 1) + "/" + n3 + ")");
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string2);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int n4 = n2 * 28;
        for (int i2 = 0; i2 < nArray.length && n4 + i2 < stringArray.length; ++i2) {
            itemMeta = stringArray[n4 + i2];
            Material material = Material.matchMaterial((String)itemMeta[3]);
            if (material == null) {
                material = Material.PAPER;
            }
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta2 = itemStack.getItemMeta();
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300" + (String)itemMeta[1]));
            ArrayList arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7" + (String)itemMeta[2]));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8ID: &7" + (String)itemMeta[0]));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!"));
            itemMeta2.setLore((List)arrayList);
            itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta2);
            inventory.setItem(nArray[i2], itemStack);
        }
        if (n2 > 0) {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300\u25c0 Poprzednia strona"));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(45, itemStack);
        }
        if (n2 < n3 - 1) {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Nast\u0119pna strona \u25b6"));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(53, itemStack);
        }
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(49, itemStack);
        player.openInventory(inventory);
    }

    public static String[][] D() {
        return new String[][]{{"DAMAGE_PERCENT", "Zabranie % obra\u017ce\u0144", "Zabiera procent HP celu", "IRON_SWORD"}, {"DAMAGE_FLAT", "Zabranie sta\u0142ych obra\u017ce\u0144", "Zabiera sta\u0142\u0105 ilo\u015b\u0107 HP", "DIAMOND_SWORD"}, {"TRUE_DAMAGE", "Prawdziwe obra\u017cenia", "Obra\u017cenia ignoruj\u0105ce zbroj\u0119", "NETHERITE_SWORD"}, {"ARMOR_PIERCE", "Przebicie zbroi", "Ignoruje % zbroi celu", "CHAINMAIL_CHESTPLATE"}, {"MAGIC_DAMAGE", "Obra\u017cenia magiczne", "Zadaje obra\u017cenia magiczne", "ENCHANTED_BOOK"}, {"FIRE_DAMAGE_TICK", "Obra\u017cenia od ognia", "Zadaje obra\u017cenia od ognia co tick", "BLAZE_POWDER"}, {"COLD_DAMAGE", "Obra\u017cenia od zimna", "Zadaje obra\u017cenia od zimna", "BLUE_ICE"}, {"LIGHTNING_DAMAGE", "Obra\u017cenia od pioruna", "Zadaje obra\u017cenia od pioruna", "LIGHTNING_ROD"}, {"POISON_DAMAGE", "Obra\u017cenia od trucizny", "Zadaje obra\u017cenia od trucizny", "POISONOUS_POTATO"}, {"WITHER_DAMAGE", "Obra\u017cenia od withera", "Zadaje obra\u017cenia od withera", "WITHER_ROSE"}, {"VOID_DAMAGE", "Obra\u017cenia od pustki", "Zadaje obra\u017cenia od pustki", "END_PORTAL_FRAME"}, {"PERCENT_CURRENT_HP", "% aktualnego HP", "Zadaje obra\u017cenia % aktualnego HP", "HEART_OF_THE_SEA"}, {"PERCENT_MAX_HP", "% maksymalnego HP", "Zadaje obra\u017cenia % maksymalnego HP", "NETHER_STAR"}, {"PERCENT_MISSING_HP", "% brakuj\u0105cego HP", "Zadaje obra\u017cenia % brakuj\u0105cego HP", "FERMENTED_SPIDER_EYE"}, {"BONUS_DAMAGE_PER_ARMOR", "Bonus za zbroj\u0119", "Dodatkowe obra\u017cenia za ka\u017cdy punkt zbroi", "DIAMOND_CHESTPLATE"}, {"HEAL_PERCENT", "Leczenie % HP", "Leczy procent HP u\u017cytkownika", "GOLDEN_APPLE"}, {"HEAL_FLAT", "Leczenie sta\u0142e HP", "Leczy sta\u0142\u0105 ilo\u015b\u0107 HP", "APPLE"}, {"FULL_HEAL", "Pe\u0142ne leczenie", "Leczy do pe\u0142nego HP", "BEETROOT_SOUP"}, {"STEAL_HP", "Kradzie\u017c HP", "Kradnie HP od celu", "REDSTONE"}, {"LIFESTEAL_PERCENT", "Wampiryzm %", "Leczy % zadanych obra\u017ce\u0144", "GHAST_TEAR"}, {"HEAL_ON_KILL", "Leczenie za zabicie", "Leczy po zabiciu celu", "BONE"}, {"HEAL_OVER_TIME", "Leczenie w czasie", "Leczy przez okre\u015blony czas", "GLISTERING_MELON_SLICE"}, {"HEAL_NEARBY_ALLIES", "Leczenie sojusznik\u00f3w", "Leczy pobliskich sojusznik\u00f3w", "GOLDEN_CARROT"}, {"HEAL_FROM_DAMAGE_TAKEN", "Leczenie z obra\u017ce\u0144", "Leczy % otrzymanych obra\u017ce\u0144", "ENCHANTED_GOLDEN_APPLE"}, {"OVERHEAL", "Nadleczenie", "Mo\u017ce leczy\u0107 ponad max HP (absorpcja)", "TOTEM_OF_UNDYING"}, {"HEAL_PER_HIT", "Leczenie za trafienie", "Leczy sta\u0142\u0105 ilo\u015b\u0107 za ka\u017cde trafienie", "SWEET_BERRIES"}, {"HEAL_MISSING_HP", "Leczenie brakuj\u0105cego HP", "Leczy % brakuj\u0105cego HP", "POTION"}, {"DRAIN_LIFE", "Drena\u017c \u017cycia", "Wysysa \u017cycie z celu", "WITHER_SKELETON_SKULL"}, {"REGENERATION_BURST", "Wybuch regeneracji", "Daje siln\u0105 regeneracj\u0119 na kr\u00f3tki czas", "GOLDEN_APPLE"}, {"VAMPIRIC_AURA", "Aura wampiryzmu", "Wampiryzm dla pobliskich sojusznik\u00f3w", "BAT_SPAWN_EGG"}, {"POISON_EFFECT", "Efekt zatrucia", "Nak\u0142ada efekt zatrucia", "SPIDER_EYE"}, {"SLOWNESS_EFFECT", "Efekt spowolnienia", "Nak\u0142ada efekt spowolnienia", "COBWEB"}, {"BLINDNESS_EFFECT", "Efekt \u015blepoty", "Nak\u0142ada efekt \u015blepoty", "INK_SAC"}, {"WITHER_EFFECT", "Efekt withera", "Nak\u0142ada efekt withera", "WITHER_SKELETON_SKULL"}, {"NAUSEA_EFFECT", "Efekt md\u0142o\u015bci", "Nak\u0142ada efekt md\u0142o\u015bci", "PUFFERFISH"}, {"HUNGER_EFFECT", "Efekt g\u0142odu", "Nak\u0142ada efekt g\u0142odu", "ROTTEN_FLESH"}, {"WEAKNESS_EFFECT", "Efekt s\u0142abo\u015bci", "Nak\u0142ada efekt s\u0142abo\u015bci", "FERMENTED_SPIDER_EYE"}, {"MINING_FATIGUE", "Efekt zm\u0119czenia", "Nak\u0142ada efekt zm\u0119czenia g\u00f3rniczego", "WOODEN_PICKAXE"}, {"LEVITATION_EFFECT", "Efekt lewitacji", "Nak\u0142ada efekt lewitacji", "SHULKER_SHELL"}, {"GLOWING_EFFECT", "Efekt \u015bwiecenia", "Nak\u0142ada efekt \u015bwiecenia", "GLOWSTONE_DUST"}, {"DARKNESS_EFFECT", "Efekt ciemno\u015bci", "Nak\u0142ada efekt ciemno\u015bci", "SCULK"}, {"BAD_OMEN", "Z\u0142a wr\u00f3\u017cba", "Nak\u0142ada efekt z\u0142ej wr\u00f3\u017cby", "OMINOUS_BOTTLE"}, {"UNLUCK_EFFECT", "Efekt pecha", "Nak\u0142ada efekt pecha", "RABBIT_FOOT"}, {"SLOW_FALLING_ENEMY", "Powolne spadanie wroga", "Nak\u0142ada powolne spadanie na wroga", "FEATHER"}, {"INFESTED", "Zara\u017cenie", "Nak\u0142ada efekt zara\u017cenia", "INFESTED_STONE"}, {"OOZING", "S\u0105czenie", "Nak\u0142ada efekt s\u0105czenia", "SLIME_BALL"}, {"WEAVING", "Tkanie", "Nak\u0142ada efekt tkania (paj\u0119czyny)", "STRING"}, {"WIND_CHARGED", "Na\u0142adowanie wiatrem", "Nak\u0142ada efekt na\u0142adowania wiatrem", "WIND_CHARGE"}, {"TRIAL_OMEN", "Omen pr\u00f3by", "Nak\u0142ada efekt omenu pr\u00f3by", "TRIAL_KEY"}, {"FREEZE_EFFECT", "Efekt zamro\u017cenia", "Nak\u0142ada efekt zamro\u017cenia (powder snow)", "POWDER_SNOW_BUCKET"}, {"CONFUSION", "Dezorientacja", "Odwraca sterowanie celu", "COMPASS"}, {"SILENCE_EFFECT", "Efekt uciszenia", "Blokuje u\u017cywanie przedmiot\u00f3w", "ECHO_SHARD"}, {"CURSE_OF_BINDING", "Kl\u0105twa wi\u0105zania", "Cel nie mo\u017ce zdj\u0105\u0107 zbroi", "CHAIN"}, {"CURSE_OF_VANISHING", "Kl\u0105twa znikania", "Przedmioty celu znikaj\u0105 po \u015bmierci", "PHANTOM_MEMBRANE"}, {"ANTI_HEAL", "Anty-leczenie", "Zmniejsza leczenie celu", "WITHER_ROSE"}, {"STRENGTH_EFFECT", "Efekt si\u0142y", "Daje efekt si\u0142y", "BLAZE_POWDER"}, {"SPEED_EFFECT", "Efekt szybko\u015bci", "Daje efekt szybko\u015bci", "SUGAR"}, {"RESISTANCE_EFFECT", "Efekt odporno\u015bci", "Daje efekt odporno\u015bci", "IRON_CHESTPLATE"}, {"REGENERATION_EFFECT", "Efekt regeneracji", "Daje efekt regeneracji", "GLISTERING_MELON_SLICE"}, {"FIRE_RESISTANCE", "Odporno\u015b\u0107 na ogie\u0144", "Daje odporno\u015b\u0107 na ogie\u0144", "MAGMA_CREAM"}, {"ABSORPTION_EFFECT", "Efekt absorpcji", "Daje efekt absorpcji", "GOLDEN_APPLE"}, {"HASTE_EFFECT", "Efekt po\u015bpiechu", "Daje efekt po\u015bpiechu", "GOLDEN_PICKAXE"}, {"JUMP_BOOST", "Efekt skoku", "Daje efekt skoku", "RABBIT_FOOT"}, {"INVISIBILITY_EFFECT", "Efekt niewidzialno\u015bci", "Daje efekt niewidzialno\u015bci", "GLASS"}, {"NIGHT_VISION", "Widzenie w ciemno\u015bci", "Daje widzenie w ciemno\u015bci", "GOLDEN_CARROT"}, {"WATER_BREATHING", "Oddychanie pod wod\u0105", "Daje oddychanie pod wod\u0105", "PUFFERFISH"}, {"SLOW_FALLING", "Powolne spadanie", "Daje powolne spadanie", "FEATHER"}, {"CONDUIT_POWER", "Moc konduitowa", "Daje moc konduitow\u0105", "CONDUIT"}, {"DOLPHINS_GRACE", "\u0141aska delfin\u00f3w", "Daje \u0142ask\u0119 delfin\u00f3w", "DOLPHIN_SPAWN_EGG"}, {"HERO_OF_VILLAGE", "Bohater wioski", "Daje status bohatera wioski", "EMERALD"}, {"LUCK_EFFECT", "Efekt szcz\u0119\u015bcia", "Daje efekt szcz\u0119\u015bcia", "FOUR_LEAF_CLOVER"}, {"SATURATION_EFFECT", "Efekt nasycenia", "Daje efekt nasycenia", "COOKED_BEEF"}, {"INSTANT_HEALTH", "Natychmiastowe leczenie", "Daje natychmiastowe leczenie", "SPLASH_POTION"}, {"HEALTH_BOOST", "Wzmocnienie zdrowia", "Daje dodatkowe serca", "RED_DYE"}, {"DAMAGE_RESISTANCE_TEMP", "Tymczasowa odporno\u015b\u0107", "Daje tymczasow\u0105 odporno\u015b\u0107 na obra\u017cenia", "NETHERITE_CHESTPLATE"}, {"FIRE_DAMAGE", "Podpalenie celu", "Podpala cel", "FIRE_CHARGE"}, {"KNOCKBACK", "Odrzucenie celu", "Odrzuca cel", "PISTON"}, {"PULL_TARGET", "Przyci\u0105gni\u0119cie celu", "Przyci\u0105ga cel", "FISHING_ROD"}, {"FREEZE", "Zamro\u017cenie celu", "Zamra\u017ca cel na miejscu", "PACKED_ICE"}, {"STUN", "Og\u0142uszenie celu", "Og\u0142usza cel", "DIAMOND_SHOVEL"}, {"ROOT", "Unieruchomienie", "Unieruchamia cel", "VINE"}, {"LAUNCH_UP", "Wyrzucenie w g\u00f3r\u0119", "Wyrzuca cel w g\u00f3r\u0119", "FIREWORK_ROCKET"}, {"SWAP_POSITIONS", "Zamiana pozycji", "Zamienia pozycje z celem", "ENDER_EYE"}, {"LAUNCH_FORWARD", "Wyrzucenie do przodu", "Wyrzuca cel do przodu", "STICKY_PISTON"}, {"LAUNCH_BACKWARD", "Wyrzucenie do ty\u0142u", "Wyrzuca cel do ty\u0142u", "PISTON"}, {"SPIN_TARGET", "Obr\u00f3t celu", "Obraca cel wok\u00f3\u0142 w\u0142asnej osi", "COMPASS"}, {"GROUND_POUND", "Uderzenie w ziemi\u0119", "Uderza w ziemi\u0119 i odpycha", "ANVIL"}, {"GRAPPLE", "Hak", "Przyci\u0105ga u\u017cytkownika do celu", "LEAD"}, {"DASH_THROUGH", "Przebicie", "Przebiega przez cel zadaj\u0105c obra\u017cenia", "SPECTRAL_ARROW"}, {"BLINK", "Mrugni\u0119cie", "Kr\u00f3tka teleportacja w kierunku patrzenia", "ENDER_PEARL"}, {"PHASE", "Faza", "Przechodzi przez bloki na kr\u00f3tki czas", "PHANTOM_MEMBRANE"}, {"GRAVITY_WELL", "Studnia grawitacji", "Przyci\u0105ga wszystkich w pobli\u017cu", "OBSIDIAN"}, {"ANTI_GRAVITY", "Anty-grawitacja", "Wyrzuca wszystkich w g\u00f3r\u0119", "END_ROD"}, {"SLOW_MOTION", "Spowolnienie czasu", "Spowalnia wszystkich w pobli\u017cu", "CLOCK"}, {"SPEED_BOOST_AREA", "Strefa szybko\u015bci", "Przyspiesza wszystkich sojusznik\u00f3w w pobli\u017cu", "SUGAR"}, {"TELEPORT_BEHIND", "Teleportacja za cel", "Teleportuje za cel", "ENDER_PEARL"}, {"TELEPORT_FORWARD", "Teleportacja do przodu", "Teleportuje do przodu", "CHORUS_FRUIT"}, {"TELEPORT_TO_TARGET", "Teleportacja do celu", "Teleportuje do celu", "ENDER_CHEST"}, {"TELEPORT_RANDOM", "Losowa teleportacja", "Teleportuje w losowe miejsce", "ENDER_EYE"}, {"TELEPORT_HOME", "Teleportacja do domu", "Teleportuje do \u0142\u00f3\u017cka", "RED_BED"}, {"TELEPORT_SWAP", "Zamiana teleportacyjna", "Zamienia miejsca z celem", "END_CRYSTAL"}, {"TELEPORT_UP", "Teleportacja w g\u00f3r\u0119", "Teleportuje w g\u00f3r\u0119", "ELYTRA"}, {"TELEPORT_DOWN", "Teleportacja w d\u00f3\u0142", "Teleportuje w d\u00f3\u0142", "ANVIL"}, {"SHADOW_STEP", "Krok cienia", "Teleportuje do cienia celu", "BLACK_WOOL"}, {"RECALL", "Przywo\u0142anie", "Wraca do poprzedniej pozycji", "RECOVERY_COMPASS"}, {"EXPLOSION", "Eksplozja", "Tworzy eksplozj\u0119", "TNT"}, {"AOE_DAMAGE", "Obra\u017cenia obszarowe", "Zadaje obra\u017cenia w obszarze", "TNT_MINECART"}, {"SHOCKWAVE", "Fala uderzeniowa", "Odpycha wszystkich w pobli\u017cu", "IRON_AXE"}, {"CHAIN_LIGHTNING", "\u0141a\u0144cuchowy piorun", "Piorun przeskakuje mi\u0119dzy celami", "TRIDENT"}, {"METEOR_STRIKE", "Uderzenie meteorytu", "Spada meteoryt z nieba", "FIRE_CHARGE"}, {"EARTHQUAKE", "Trz\u0119sienie ziemi", "Trz\u0119sienie ziemi w obszarze", "BEDROCK"}, {"TORNADO", "Tornado", "Tworzy tornado", "WIND_CHARGE"}, {"ICE_STORM", "Lodowa burza", "Tworzy lodow\u0105 burz\u0119", "BLUE_ICE"}, {"FIRE_STORM", "Ognista burza", "Tworzy ognist\u0105 burz\u0119", "BLAZE_POWDER"}, {"POISON_CLOUD", "Chmura trucizny", "Tworzy chmur\u0119 trucizny", "LINGERING_POTION"}, {"HEALING_AURA", "Aura leczenia", "Leczy wszystkich sojusznik\u00f3w w pobli\u017cu", "BEACON"}, {"DAMAGE_AURA", "Aura obra\u017ce\u0144", "Zadaje obra\u017cenia wszystkim wrogom w pobli\u017cu", "WITHER_SKELETON_SKULL"}, {"NOVA", "Nova", "Eksplozja energii we wszystkich kierunkach", "NETHER_STAR"}, {"BLACK_HOLE", "Czarna dziura", "Przyci\u0105ga i zadaje obra\u017cenia", "OBSIDIAN"}, {"RAIN_OF_ARROWS", "Deszcz strza\u0142", "Spada deszcz strza\u0142 z nieba", "ARROW"}, {"DISARM", "Rozbrojenie", "Wyrzuca przedmiot z r\u0119ki celu", "BARRIER"}, {"SILENCE", "Uciszenie", "Blokuje u\u017cywanie przedmiot\u00f3w", "ECHO_SHARD"}, {"RANDOM_ROTATION", "Losowa rotacja", "Losowo obraca cel", "COMPASS"}, {"INVULNERABILITY", "Nie\u015bmiertelno\u015b\u0107", "Daje chwilow\u0105 nie\u015bmiertelno\u015b\u0107", "TOTEM_OF_UNDYING"}, {"EXECUTE", "Egzekucja", "Dodatkowe obra\u017cenia gdy cel ma ma\u0142o HP", "NETHERITE_AXE"}, {"CRITICAL_BOOST", "Wzmocnienie krytyka", "Zwi\u0119ksza szans\u0119 na krytyka", "DIAMOND_SWORD"}, {"COMBO_DAMAGE", "Obra\u017cenia combo", "Zwi\u0119ksza obra\u017cenia przy kolejnych trafieniach", "GOLDEN_SWORD"}, {"REFLECT_DAMAGE", "Odbicie obra\u017ce\u0144", "Odbija cz\u0119\u015b\u0107 obra\u017ce\u0144", "SHIELD"}, {"THORNS", "Ciernie", "Zadaje obra\u017cenia atakuj\u0105cemu", "CACTUS"}, {"REDUCE_MAX_HP", "Redukcja max HP", "Zmniejsza max HP celu", "STRUCTURE_BLOCK"}, {"STEAL_EFFECTS", "Kradzie\u017c efekt\u00f3w", "Kradnie efekty od celu", "BREWING_STAND"}, {"CLEAR_EFFECTS", "Usuni\u0119cie efekt\u00f3w", "Usuwa efekty z celu", "MILK_BUCKET"}, {"BONUS_XP", "Bonus XP", "Daje dodatkowe XP", "EXPERIENCE_BOTTLE"}, {"DOUBLE_DROP", "Podw\u00f3jny drop", "Szansa na podw\u00f3jny drop", "CHEST"}, {"SOUL_HARVEST", "\u017bniwa dusz", "Zbiera dusze z zabitych", "SOUL_LANTERN"}, {"MARK_TARGET", "Oznaczenie celu", "Oznacza cel (\u015bwiecenie + bonus obra\u017ce\u0144)", "TARGET"}, {"EXECUTE_MARKED", "Egzekucja oznaczonego", "Dodatkowe obra\u017cenia na oznaczonych", "NETHERITE_SWORD"}, {"BLEED", "Krwawienie", "Cel krwawi przez czas", "REDSTONE"}, {"ARMOR_BREAK", "Z\u0142amanie zbroi", "Tymczasowo zmniejsza zbroj\u0119 celu", "CHAINMAIL_CHESTPLATE"}, {"SHIELD_BREAK", "Z\u0142amanie tarczy", "Wy\u0142\u0105cza tarcz\u0119 celu", "SHIELD"}, {"MANA_BURN", "Spalenie many", "Zadaje obra\u017cenia za ka\u017cdy efekt na celu", "LAPIS_LAZULI"}, {"PURGE", "Oczyszczenie", "Usuwa pozytywne efekty z celu", "WATER_BUCKET"}, {"DISPEL", "Rozproszenie", "Usuwa negatywne efekty z u\u017cytkownika", "MILK_BUCKET"}, {"CLONE", "Klon", "Tworzy klona u\u017cytkownika", "ARMOR_STAND"}, {"DECOY", "Wabik", "Tworzy wabika przyci\u0105gaj\u0105cego moby", "CARVED_PUMPKIN"}, {"SUMMON_WOLF", "Przywo\u0142anie wilka", "Przywo\u0142uje wilka do pomocy", "WOLF_SPAWN_EGG"}, {"SUMMON_IRON_GOLEM", "Przywo\u0142anie golema", "Przywo\u0142uje \u017celaznego golema", "IRON_BLOCK"}, {"SUMMON_LIGHTNING", "Przywo\u0142anie pioruna", "Przywo\u0142uje piorun na cel", "LIGHTNING_ROD"}, {"TRANSFORM_TARGET", "Transformacja celu", "Zamienia cel w zwierz\u0119", "PIG_SPAWN_EGG"}, {"BANISH", "Wygnanie", "Teleportuje cel daleko", "END_PORTAL_FRAME"}};
    }

    public void B(Player player, String string) {
        this.B(player, string, 0);
    }

    public void B(Player player, String string, int n2) {
        ItemMeta itemMeta;
        String[][] stringArray = D.C();
        int n3 = (int)Math.ceil((double)((double)stringArray.length / 28.0));
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d21\u028f\u0299\u026a\u1d07\u0280\u1d22 \u1d07\ua730\u1d07\u1d0b\u1d1b (" + (n2 + 1) + "/" + n3 + ")");
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string2);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        int n4 = n2 * 28;
        for (int i2 = 0; i2 < nArray.length && n4 + i2 < stringArray.length; ++i2) {
            itemMeta = stringArray[n4 + i2];
            Material material = Material.matchMaterial((String)itemMeta[3]);
            if (material == null) {
                material = Material.PAPER;
            }
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta2 = itemStack.getItemMeta();
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#00D2FF" + (String)itemMeta[1]));
            ArrayList arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7" + (String)itemMeta[2]));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8ID: &7" + (String)itemMeta[0]));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d05\u1d0f\u1d05\u1d00\u0107!"));
            itemMeta2.setLore((List)arrayList);
            itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta2);
            inventory.setItem(nArray[i2], itemStack);
        }
        if (n2 > 0) {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300\u25c0 Poprzednia strona"));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(45, itemStack);
        }
        if (n2 < n3 - 1) {
            ItemStack itemStack = new ItemStack(Material.ARROW);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300Nast\u0119pna strona \u25b6"));
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(53, itemStack);
        }
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(49, itemStack);
        player.openInventory(inventory);
    }

    public static String[][] C() {
        return new String[][]{{"LIGHTNING", "Strzelenie piorunem", "Uderza piorunem w cel", "LIGHTNING_ROD"}, {"LIGHTNING_SILENT", "Cichy piorun", "Piorun bez d\u017awi\u0119ku", "LIGHTNING_ROD"}, {"LIGHTNING_CHAIN", "\u0141a\u0144cuch piorun\u00f3w", "Seria piorun\u00f3w", "TRIDENT"}, {"EXPLOSION_VISUAL", "Wizualna eksplozja", "Eksplozja bez obra\u017ce\u0144", "TNT"}, {"EXPLOSION_LARGE", "Du\u017ca eksplozja", "Du\u017ca wizualna eksplozja", "TNT_MINECART"}, {"EXPLOSION_HUGE", "Ogromna eksplozja", "Ogromna wizualna eksplozja", "COMMAND_BLOCK"}, {"FIREBALL_VISUAL", "Wizualna kula ognia", "Kula ognia bez obra\u017ce\u0144", "FIRE_CHARGE"}, {"METEOR_VISUAL", "Wizualny meteoryt", "Spadaj\u0105cy meteoryt", "MAGMA_BLOCK"}, {"NUKE_VISUAL", "Wizualna bomba", "Efekt bomby atomowej", "BEACON"}, {"SHOCKWAVE_VISUAL", "Wizualna fala", "Fala uderzeniowa", "IRON_BLOCK"}, {"PARTICLE_FLAME", "Particle ognia", "Wy\u015bwietla particle ognia", "FIRE_CHARGE"}, {"PARTICLE_SMOKE", "Particle dymu", "Wy\u015bwietla particle dymu", "CAMPFIRE"}, {"PARTICLE_HEART", "Particle serduszek", "Wy\u015bwietla particle serduszek", "RED_DYE"}, {"PARTICLE_ENCHANT", "Particle enchant\u00f3w", "Wy\u015bwietla particle enchant\u00f3w", "ENCHANTING_TABLE"}, {"PARTICLE_PORTAL", "Particle portalu", "Wy\u015bwietla particle portalu", "OBSIDIAN"}, {"PARTICLE_EXPLOSION", "Particle eksplozji", "Wy\u015bwietla particle eksplozji", "TNT_MINECART"}, {"PARTICLE_CLOUD", "Particle chmury", "Wy\u015bwietla particle chmury", "WHITE_WOOL"}, {"PARTICLE_REDSTONE", "Particle redstone", "Wy\u015bwietla kolorowe particle", "REDSTONE"}, {"PARTICLE_TOTEM", "Particle totemu", "Wy\u015bwietla particle totemu", "TOTEM_OF_UNDYING"}, {"PARTICLE_DRAGON_BREATH", "Particle smoczego oddechu", "Wy\u015bwietla particle smoka", "DRAGON_BREATH"}, {"PARTICLE_LAVA", "Particle lawy", "Wy\u015bwietla particle lawy", "LAVA_BUCKET"}, {"PARTICLE_WATER_SPLASH", "Particle plusk wody", "Wy\u015bwietla plusk wody", "WATER_BUCKET"}, {"PARTICLE_WATER_WAKE", "Particle fali wody", "Wy\u015bwietla fal\u0119 wody", "PRISMARINE"}, {"PARTICLE_SUSPENDED", "Particle zawieszone", "Wy\u015bwietla zawieszone particle", "GLASS"}, {"PARTICLE_BARRIER", "Particle bariery", "Wy\u015bwietla particle bariery", "BARRIER"}, {"PARTICLE_ITEM_CRACK", "Particle p\u0119kaj\u0105cego itemu", "Wy\u015bwietla p\u0119kaj\u0105cy item", "CRACKED_STONE_BRICKS"}, {"PARTICLE_BLOCK_CRACK", "Particle p\u0119kaj\u0105cego bloku", "Wy\u015bwietla p\u0119kaj\u0105cy blok", "STONE"}, {"PARTICLE_BLOCK_DUST", "Particle py\u0142u bloku", "Wy\u015bwietla py\u0142 bloku", "DIRT"}, {"PARTICLE_WATER_DROP", "Particle kropli wody", "Wy\u015bwietla krople wody", "BLUE_STAINED_GLASS"}, {"PARTICLE_MOB_APPEARANCE", "Particle pojawienia moba", "Wy\u015bwietla pojawienie moba", "ZOMBIE_HEAD"}, {"PARTICLE_CRIT", "Particle krytyka", "Wy\u015bwietla particle krytyka", "DIAMOND_SWORD"}, {"PARTICLE_CRIT_MAGIC", "Particle magicznego krytyka", "Wy\u015bwietla magiczny krytyk", "ENCHANTED_BOOK"}, {"PARTICLE_DAMAGE_INDICATOR", "Particle obra\u017ce\u0144", "Wy\u015bwietla wska\u017anik obra\u017ce\u0144", "IRON_SWORD"}, {"PARTICLE_SWEEP_ATTACK", "Particle sweep attack", "Wy\u015bwietla sweep attack", "GOLDEN_SWORD"}, {"PARTICLE_ANGRY_VILLAGER", "Particle z\u0142o\u015bci", "Wy\u015bwietla particle z\u0142o\u015bci", "IRON_GOLEM_SPAWN_EGG"}, {"PARTICLE_HAPPY_VILLAGER", "Particle szcz\u0119\u015bcia", "Wy\u015bwietla particle szcz\u0119\u015bcia", "EMERALD"}, {"PARTICLE_ENCHANTED_HIT", "Particle magicznego trafienia", "Wy\u015bwietla magiczne trafienie", "ENCHANTED_BOOK"}, {"PARTICLE_DAMAGE_HEART", "Particle serca obra\u017ce\u0144", "Wy\u015bwietla serce obra\u017ce\u0144", "FERMENTED_SPIDER_EYE"}, {"PARTICLE_DEATH", "Particle \u015bmierci", "Wy\u015bwietla particle \u015bmierci", "BONE"}, {"PARTICLE_BLOOD", "Particle krwi", "Wy\u015bwietla particle krwi", "REDSTONE"}, {"PARTICLE_SPELL", "Particle zakl\u0119cia", "Wy\u015bwietla particle zakl\u0119cia", "POTION"}, {"PARTICLE_SPELL_INSTANT", "Particle instant zakl\u0119cia", "Wy\u015bwietla instant zakl\u0119cie", "SPLASH_POTION"}, {"PARTICLE_SPELL_MOB", "Particle zakl\u0119cia moba", "Wy\u015bwietla zakl\u0119cie moba", "SPIDER_EYE"}, {"PARTICLE_SPELL_MOB_AMBIENT", "Particle ambient zakl\u0119cia", "Wy\u015bwietla ambient zakl\u0119cie", "BEACON"}, {"PARTICLE_SPELL_WITCH", "Particle wied\u017amy", "Wy\u015bwietla particle wied\u017amy", "CAULDRON"}, {"PARTICLE_INSTANT_EFFECT", "Particle instant efektu", "Wy\u015bwietla instant efekt", "LINGERING_POTION"}, {"PARTICLE_ENTITY_EFFECT", "Particle efektu entity", "Wy\u015bwietla efekt entity", "BREWING_STAND"}, {"PARTICLE_AMBIENT_ENTITY_EFFECT", "Particle ambient efektu", "Wy\u015bwietla ambient efekt", "BEACON"}, {"PARTICLE_WITCH_MAGIC", "Particle magii wied\u017amy", "Wy\u015bwietla magi\u0119 wied\u017amy", "WITCH_SPAWN_EGG"}, {"PARTICLE_ENCHANT_GLYPHS", "Particle glif\u00f3w", "Wy\u015bwietla glify enchant\u00f3w", "ENCHANTING_TABLE"}, {"PARTICLE_MAGIC_CIRCLE", "Particle magicznego kr\u0119gu", "Wy\u015bwietla magiczny kr\u0105g", "AMETHYST_SHARD"}, {"PARTICLE_RUNE", "Particle runy", "Wy\u015bwietla run\u0119", "CHISELED_STONE_BRICKS"}, {"PARTICLE_ARCANE", "Particle arkany", "Wy\u015bwietla arkan\u0119", "END_CRYSTAL"}, {"PARTICLE_HOLY", "Particle \u015bwi\u0119to\u015bci", "Wy\u015bwietla \u015bwi\u0119to\u015b\u0107", "GOLDEN_APPLE"}, {"PARTICLE_DARK_MAGIC", "Particle ciemnej magii", "Wy\u015bwietla ciemn\u0105 magi\u0119", "WITHER_ROSE"}, {"PARTICLE_DRIP_WATER", "Particle kapi\u0105cej wody", "Wy\u015bwietla kapi\u0105c\u0105 wod\u0119", "WATER_BUCKET"}, {"PARTICLE_DRIP_LAVA", "Particle kapi\u0105cej lawy", "Wy\u015bwietla kapi\u0105c\u0105 law\u0119", "LAVA_BUCKET"}, {"PARTICLE_DRIPPING_HONEY", "Particle kapi\u0105cego miodu", "Wy\u015bwietla kapi\u0105cy mi\u00f3d", "HONEY_BOTTLE"}, {"PARTICLE_FALLING_HONEY", "Particle spadaj\u0105cego miodu", "Wy\u015bwietla spadaj\u0105cy mi\u00f3d", "HONEYCOMB"}, {"PARTICLE_LANDING_HONEY", "Particle l\u0105duj\u0105cego miodu", "Wy\u015bwietla l\u0105duj\u0105cy mi\u00f3d", "HONEY_BLOCK"}, {"PARTICLE_FALLING_NECTAR", "Particle spadaj\u0105cego nektaru", "Wy\u015bwietla spadaj\u0105cy nektar", "BEE_NEST"}, {"PARTICLE_DRIPPING_OBSIDIAN_TEAR", "Particle \u0142zy obsydianu", "Wy\u015bwietla \u0142z\u0119 obsydianu", "CRYING_OBSIDIAN"}, {"PARTICLE_FALLING_OBSIDIAN_TEAR", "Particle spadaj\u0105cej \u0142zy", "Wy\u015bwietla spadaj\u0105c\u0105 \u0142z\u0119", "CRYING_OBSIDIAN"}, {"PARTICLE_LANDING_OBSIDIAN_TEAR", "Particle l\u0105duj\u0105cej \u0142zy", "Wy\u015bwietla l\u0105duj\u0105c\u0105 \u0142z\u0119", "CRYING_OBSIDIAN"}, {"PARTICLE_DRIPPING_DRIPSTONE_WATER", "Particle wody z dripstone", "Wy\u015bwietla wod\u0119 z dripstone", "POINTED_DRIPSTONE"}, {"PARTICLE_DRIPPING_DRIPSTONE_LAVA", "Particle lawy z dripstone", "Wy\u015bwietla law\u0119 z dripstone", "POINTED_DRIPSTONE"}, {"PARTICLE_FALLING_DRIPSTONE_WATER", "Particle spadaj\u0105cej wody", "Wy\u015bwietla spadaj\u0105c\u0105 wod\u0119", "DRIPSTONE_BLOCK"}, {"PARTICLE_FALLING_DRIPSTONE_LAVA", "Particle spadaj\u0105cej lawy", "Wy\u015bwietla spadaj\u0105c\u0105 law\u0119", "DRIPSTONE_BLOCK"}, {"PARTICLE_SPLASH", "Particle plusk", "Wy\u015bwietla plusk", "WATER_BUCKET"}, {"PARTICLE_FISHING", "Particle w\u0119dkowania", "Wy\u015bwietla w\u0119dkowanie", "FISHING_ROD"}, {"PARTICLE_NOTE", "Particle nutki", "Wy\u015bwietla nutk\u0119", "NOTE_BLOCK"}, {"PARTICLE_END_ROD", "Particle end rod", "Wy\u015bwietla end rod", "END_ROD"}, {"PARTICLE_FALLING_DUST", "Particle spadaj\u0105cego py\u0142u", "Wy\u015bwietla spadaj\u0105cy py\u0142", "SAND"}, {"PARTICLE_SNEEZE", "Particle kichni\u0119cia", "Wy\u015bwietla kichni\u0119cie", "PANDA_SPAWN_EGG"}, {"PARTICLE_SQUID_INK", "Particle atramentu", "Wy\u015bwietla atrament", "INK_SAC"}, {"PARTICLE_CAMPFIRE_COSY", "Particle przytulnego ogniska", "Wy\u015bwietla przytulne ognisko", "CAMPFIRE"}, {"PARTICLE_CAMPFIRE_SIGNAL", "Particle sygna\u0142owego ogniska", "Wy\u015bwietla sygna\u0142owe ognisko", "SOUL_CAMPFIRE"}, {"PARTICLE_SOUL", "Particle duszy", "Wy\u015bwietla dusz\u0119", "SOUL_SAND"}, {"PARTICLE_SOUL_FIRE_FLAME", "Particle ognia dusz", "Wy\u015bwietla ogie\u0144 dusz", "SOUL_TORCH"}, {"PARTICLE_COMPOSTER", "Particle kompostownika", "Wy\u015bwietla kompostownik", "COMPOSTER"}, {"PARTICLE_FLASH", "Particle b\u0142ysku", "Wy\u015bwietla b\u0142ysk", "FIREWORK_ROCKET"}, {"PARTICLE_ELECTRIC_SPARK", "Particle iskry elektrycznej", "Wy\u015bwietla iskr\u0119 elektryczn\u0105", "LIGHTNING_ROD"}, {"PARTICLE_GLOW", "Particle blasku", "Wy\u015bwietla blask", "GLOW_INK_SAC"}, {"PARTICLE_WAX_ON", "Particle woskowania", "Wy\u015bwietla woskowanie", "HONEYCOMB"}, {"PARTICLE_WAX_OFF", "Particle usuwania wosku", "Wy\u015bwietla usuwanie wosku", "COPPER_BLOCK"}, {"PARTICLE_SCRAPE", "Particle skrobania", "Wy\u015bwietla skrobanie", "COPPER_INGOT"}, {"PARTICLE_SNOWFLAKE", "Particle p\u0142atka \u015bniegu", "Wy\u015bwietla p\u0142atek \u015bniegu", "SNOWBALL"}, {"PARTICLE_SMALL_FLAME", "Particle ma\u0142ego p\u0142omienia", "Wy\u015bwietla ma\u0142y p\u0142omie\u0144", "TORCH"}, {"PARTICLE_CHERRY_LEAVES", "Particle li\u015bci wi\u015bni", "Wy\u015bwietla li\u015bcie wi\u015bni", "CHERRY_LEAVES"}, {"PARTICLE_SCULK_SOUL", "Particle duszy sculk", "Wy\u015bwietla dusz\u0119 sculk", "SCULK"}, {"PARTICLE_CRIMSON_SPORE", "Particle crimson spore", "Wy\u015bwietla crimson spore", "CRIMSON_NYLIUM"}, {"PARTICLE_WARPED_SPORE", "Particle warped spore", "Wy\u015bwietla warped spore", "WARPED_NYLIUM"}, {"PARTICLE_ASH", "Particle popio\u0142u", "Wy\u015bwietla popi\u00f3\u0142", "BASALT"}, {"PARTICLE_WHITE_ASH", "Particle bia\u0142ego popio\u0142u", "Wy\u015bwietla bia\u0142y popi\u00f3\u0142", "BONE_MEAL"}, {"PARTICLE_SHRIEK", "Particle krzyku", "Wy\u015bwietla krzyk", "SCULK_SHRIEKER"}, {"PARTICLE_SCULK_CHARGE", "Particle \u0142adunku sculk", "Wy\u015bwietla \u0142adunek sculk", "SCULK_CATALYST"}, {"PARTICLE_SCULK_CHARGE_POP", "Particle pop sculk", "Wy\u015bwietla pop sculk", "SCULK_SENSOR"}, {"PARTICLE_VIBRATION", "Particle wibracji", "Wy\u015bwietla wibracj\u0119", "SCULK_SENSOR"}, {"PARTICLE_SONIC_BOOM", "Particle sonic boom", "Wy\u015bwietla sonic boom", "SCULK_SHRIEKER"}, {"PARTICLE_SPORE_BLOSSOM_AIR", "Particle spore blossom", "Wy\u015bwietla spore blossom", "SPORE_BLOSSOM"}, {"PARTICLE_BUBBLE", "Particle b\u0105belka", "Wy\u015bwietla b\u0105belek", "BUBBLE_CORAL"}, {"PARTICLE_BUBBLE_POP", "Particle p\u0119kaj\u0105cego b\u0105belka", "Wy\u015bwietla p\u0119kaj\u0105cy b\u0105belek", "BUBBLE_CORAL"}, {"PARTICLE_BUBBLE_COLUMN_UP", "Particle kolumny b\u0105belk\u00f3w", "Wy\u015bwietla kolumn\u0119 b\u0105belk\u00f3w", "MAGMA_BLOCK"}, {"PARTICLE_CURRENT_DOWN", "Particle pr\u0105du w d\u00f3\u0142", "Wy\u015bwietla pr\u0105d w d\u00f3\u0142", "SOUL_SAND"}, {"PARTICLE_NAUTILUS", "Particle nautilusa", "Wy\u015bwietla nautilusa", "NAUTILUS_SHELL"}, {"PARTICLE_DOLPHIN", "Particle delfina", "Wy\u015bwietla delfina", "DOLPHIN_SPAWN_EGG"}, {"PARTICLE_RAIN", "Particle deszczu", "Wy\u015bwietla deszcz", "BLUE_STAINED_GLASS"}, {"PARTICLE_UNDERWATER", "Particle podwodne", "Wy\u015bwietla podwodne particle", "PRISMARINE"}, {"PARTICLE_SPIT", "Particle plucia", "Wy\u015bwietla plucie", "LLAMA_SPAWN_EGG"}, {"PARTICLE_REVERSE_PORTAL", "Particle odwr\u00f3conego portalu", "Wy\u015bwietla odwr\u00f3cony portal", "RESPAWN_ANCHOR"}, {"SOUND_EXPLOSION", "D\u017awi\u0119k eksplozji", "Odtwarza d\u017awi\u0119k eksplozji", "TNT"}, {"SOUND_THUNDER", "D\u017awi\u0119k grzmotu", "Odtwarza d\u017awi\u0119k grzmotu", "BELL"}, {"SOUND_WITHER_SPAWN", "D\u017awi\u0119k spawnu withera", "Odtwarza spawn withera", "WITHER_SKELETON_SKULL"}, {"SOUND_WITHER_DEATH", "D\u017awi\u0119k \u015bmierci withera", "Odtwarza \u015bmier\u0107 withera", "NETHER_STAR"}, {"SOUND_ENDER_DRAGON_GROWL", "D\u017awi\u0119k ryku smoka", "Odtwarza ryk smoka", "DRAGON_HEAD"}, {"SOUND_ENDER_DRAGON_DEATH", "D\u017awi\u0119k \u015bmierci smoka", "Odtwarza \u015bmier\u0107 smoka", "DRAGON_EGG"}, {"SOUND_ANVIL_LAND", "D\u017awi\u0119k l\u0105dowania kowad\u0142a", "Odtwarza l\u0105dowanie kowad\u0142a", "ANVIL"}, {"SOUND_ANVIL_BREAK", "D\u017awi\u0119k z\u0142amania kowad\u0142a", "Odtwarza z\u0142amanie kowad\u0142a", "DAMAGED_ANVIL"}, {"SOUND_FIREWORK_BLAST", "D\u017awi\u0119k wybuchu fajerwerku", "Odtwarza wybuch fajerwerku", "FIREWORK_ROCKET"}, {"SOUND_FIREWORK_LARGE_BLAST", "D\u017awi\u0119k du\u017cego wybuchu", "Odtwarza du\u017cy wybuch", "FIREWORK_STAR"}, {"SOUND_BLAZE", "D\u017awi\u0119k blaze'a", "Odtwarza d\u017awi\u0119k blaze'a", "BLAZE_ROD"}, {"SOUND_GHAST", "D\u017awi\u0119k ghasta", "Odtwarza d\u017awi\u0119k ghasta", "GHAST_TEAR"}, {"SOUND_GHAST_SCREAM", "D\u017awi\u0119k krzyku ghasta", "Odtwarza krzyk ghasta", "GHAST_SPAWN_EGG"}, {"SOUND_ENDERMAN", "D\u017awi\u0119k endermana", "Odtwarza d\u017awi\u0119k endermana", "ENDER_PEARL"}, {"SOUND_ENDERMAN_SCREAM", "D\u017awi\u0119k krzyku endermana", "Odtwarza krzyk endermana", "ENDERMAN_SPAWN_EGG"}, {"SOUND_ZOMBIE", "D\u017awi\u0119k zombie", "Odtwarza d\u017awi\u0119k zombie", "ZOMBIE_HEAD"}, {"SOUND_SKELETON", "D\u017awi\u0119k szkieleta", "Odtwarza d\u017awi\u0119k szkieleta", "SKELETON_SKULL"}, {"SOUND_CREEPER_HISS", "D\u017awi\u0119k syczenia creepera", "Odtwarza syczenie creepera", "CREEPER_HEAD"}, {"SOUND_WOLF_HOWL", "D\u017awi\u0119k wycia wilka", "Odtwarza wycie wilka", "WOLF_SPAWN_EGG"}, {"SOUND_CAT_HISS", "D\u017awi\u0119k syczenia kota", "Odtwarza syczenie kota", "CAT_SPAWN_EGG"}, {"SOUND_IRON_GOLEM_DEATH", "D\u017awi\u0119k \u015bmierci golema", "Odtwarza \u015bmier\u0107 golema", "IRON_BLOCK"}, {"SOUND_RAVAGER_ROAR", "D\u017awi\u0119k ryku ravagera", "Odtwarza ryk ravagera", "RAVAGER_SPAWN_EGG"}, {"SOUND_WARDEN_ROAR", "D\u017awi\u0119k ryku wardena", "Odtwarza ryk wardena", "SCULK_SHRIEKER"}, {"SOUND_ELDER_GUARDIAN", "D\u017awi\u0119k elder guardiana", "Odtwarza elder guardiana", "PRISMARINE_SHARD"}, {"SOUND_PHANTOM_BITE", "D\u017awi\u0119k ugryzienia phantoma", "Odtwarza ugryzienie phantoma", "PHANTOM_MEMBRANE"}, {"SOUND_LEVEL_UP", "D\u017awi\u0119k level up", "Odtwarza d\u017awi\u0119k level up", "EXPERIENCE_BOTTLE"}, {"SOUND_ITEM_BREAK", "D\u017awi\u0119k z\u0142amania itemu", "Odtwarza z\u0142amanie itemu", "WOODEN_SWORD"}, {"SOUND_TOTEM_USE", "D\u017awi\u0119k u\u017cycia totemu", "Odtwarza u\u017cycie totemu", "TOTEM_OF_UNDYING"}, {"SOUND_SHIELD_BLOCK", "D\u017awi\u0119k bloku tarczy", "Odtwarza blok tarczy", "SHIELD"}, {"SOUND_ARMOR_EQUIP", "D\u017awi\u0119k zak\u0142adania zbroi", "Odtwarza zak\u0142adanie zbroi", "DIAMOND_CHESTPLATE"}, {"SOUND_ELYTRA_FLYING", "D\u017awi\u0119k latania elytr\u0105", "Odtwarza latanie elytr\u0105", "ELYTRA"}, {"SOUND_TRIDENT_RIPTIDE", "D\u017awi\u0119k riptide", "Odtwarza riptide", "TRIDENT"}, {"SOUND_TRIDENT_THUNDER", "D\u017awi\u0119k grzmotu tr\u00f3jz\u0119ba", "Odtwarza grzmot tr\u00f3jz\u0119ba", "TRIDENT"}, {"SOUND_BELL_RING", "D\u017awi\u0119k dzwonu", "Odtwarza dzwon", "BELL"}, {"SOUND_BEACON_ACTIVATE", "D\u017awi\u0119k aktywacji beacona", "Odtwarza aktywacj\u0119 beacona", "BEACON"}, {"SOUND_PORTAL", "D\u017awi\u0119k portalu", "Odtwarza d\u017awi\u0119k portalu", "OBSIDIAN"}, {"SOUND_CHEST_OPEN", "D\u017awi\u0119k otwierania skrzyni", "Odtwarza otwieranie skrzyni", "CHEST"}, {"SOUND_CHEST_CLOSE", "D\u017awi\u0119k zamykania skrzyni", "Odtwarza zamykanie skrzyni", "CHEST"}, {"SOUND_DOOR_OPEN", "D\u017awi\u0119k otwierania drzwi", "Odtwarza otwieranie drzwi", "OAK_DOOR"}, {"SOUND_DOOR_CLOSE", "D\u017awi\u0119k zamykania drzwi", "Odtwarza zamykanie drzwi", "IRON_DOOR"}, {"SOUND_LAVA_POP", "D\u017awi\u0119k bulgotania lawy", "Odtwarza bulgotanie lawy", "LAVA_BUCKET"}, {"SOUND_WATER_SPLASH", "D\u017awi\u0119k plusk wody", "Odtwarza plusk wody", "WATER_BUCKET"}, {"SOUND_FIRE_CRACKLE", "D\u017awi\u0119k trzaskania ognia", "Odtwarza trzaskanie ognia", "CAMPFIRE"}, {"SOUND_WIND", "D\u017awi\u0119k wiatru", "Odtwarza d\u017awi\u0119k wiatru", "WIND_CHARGE"}, {"SOUND_AMETHYST", "D\u017awi\u0119k ametystu", "Odtwarza d\u017awi\u0119k ametystu", "AMETHYST_SHARD"}, {"COMBO_FIRE_BURST", "Wybuch ognia", "Particle ognia + d\u017awi\u0119k", "BLAZE_POWDER"}, {"COMBO_ICE_BURST", "Wybuch lodu", "Particle lodu + d\u017awi\u0119k", "BLUE_ICE"}, {"COMBO_LIGHTNING_STRIKE", "Uderzenie pioruna", "Piorun + particle + d\u017awi\u0119k", "LIGHTNING_ROD"}, {"COMBO_EXPLOSION_FULL", "Pe\u0142na eksplozja", "Eksplozja + particle + d\u017awi\u0119k", "TNT"}, {"COMBO_MAGIC_BURST", "Wybuch magii", "Particle magii + d\u017awi\u0119k", "ENCHANTING_TABLE"}, {"COMBO_HEAL_EFFECT", "Efekt leczenia", "Particle serduszek + d\u017awi\u0119k", "GOLDEN_APPLE"}, {"COMBO_DEATH_EFFECT", "Efekt \u015bmierci", "Particle + d\u017awi\u0119k \u015bmierci", "WITHER_ROSE"}, {"COMBO_TELEPORT_EFFECT", "Efekt teleportacji", "Particle portalu + d\u017awi\u0119k", "ENDER_PEARL"}, {"COMBO_SUMMON_EFFECT", "Efekt przywo\u0142ania", "Particle + d\u017awi\u0119k przywo\u0142ania", "SPAWNER"}, {"COMBO_POWER_UP", "Efekt wzmocnienia", "Particle + d\u017awi\u0119k wzmocnienia", "NETHER_STAR"}, {"COMBO_SHIELD_EFFECT", "Efekt tarczy", "Particle tarczy + d\u017awi\u0119k", "SHIELD"}, {"COMBO_CURSE_EFFECT", "Efekt kl\u0105twy", "Particle kl\u0105twy + d\u017awi\u0119k", "WITHER_SKELETON_SKULL"}, {"COMBO_BLESSING_EFFECT", "Efekt b\u0142ogos\u0142awie\u0144stwa", "Particle b\u0142ogos\u0142awie\u0144stwa + d\u017awi\u0119k", "GOLDEN_APPLE"}, {"COMBO_RAGE_EFFECT", "Efekt sza\u0142u", "Particle sza\u0142u + d\u017awi\u0119k", "BLAZE_POWDER"}, {"COMBO_STEALTH_EFFECT", "Efekt skradania", "Particle skradania + d\u017awi\u0119k", "PHANTOM_MEMBRANE"}};
    }

    private ItemStack A(Map<String, Object> map, int n2, boolean bl) {
        String string = (String)map.getOrDefault((Object)"type", (Object)"UNKNOWN");
        String string2 = this.A(string, bl);
        Material material = bl ? Material.ENDER_EYE : Material.NETHER_STAR;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string3 = bl ? "&#00D2FF" : "&#FFD300";
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3 + string2));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Typ: &f" + string));
        for (Map.Entry entry : map.entrySet()) {
            if (((String)entry.getKey()).equals((Object)"type")) continue;
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7" + (String)entry.getKey() + ": &f" + String.valueOf((Object)entry.getValue())));
        }
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FFD300&n\u029f\u1d07\u1d21\u028f\u1d0d&7, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void A(Player player, String string, int n2, boolean bl) {
        ItemStack itemStack;
        Object object;
        String string2;
        Map.Entry entry2;
        List<Map<String, Object>> list;
        List<Map<String, Object>> list2 = list = bl ? this.A.F(string) : this.A.M(string);
        if (n2 < 0 || n2 >= list.size()) {
            return;
        }
        Map map = (Map)list.get(n2);
        String string3 = (String)map.getOrDefault((Object)"type", (Object)"UNKNOWN");
        String string4 = this.A(string3, bl);
        String string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 " + me.anarchiacore.customitems.stormitemy.utils.color.A.D(string4.substring(0, Math.min((int)10, (int)string4.length()))));
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string5);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n3 = 0;
        for (Map.Entry entry2 : map.entrySet()) {
            if (n3 >= nArray.length) break;
            string2 = (String)entry2.getKey();
            object = entry2.getValue();
            if (string2.equals((Object)"type")) continue;
            itemStack = this.A(string2, object);
            inventory.setItem(nArray[n3], itemStack);
            ++n3;
        }
        Iterator iterator = new ItemStack(Material.BARRIER);
        entry2 = iterator.getItemMeta();
        entry2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        entry2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        iterator.setItemMeta((ItemMeta)entry2);
        inventory.setItem(39, (ItemStack)iterator);
        string2 = new ItemStack(Material.BOOK);
        object = string2.getItemMeta();
        object.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FF" + string4));
        itemStack = new ArrayList();
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Typ: &f" + string3));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Index: &f" + n2));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kategoria: &f" + (bl ? "Efekt wizualny" : "Umiej\u0119tno\u015b\u0107")));
        itemStack.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        object.setLore((List)itemStack);
        object.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        string2.setItemMeta((ItemMeta)object);
        inventory.setItem(40, (ItemStack)string2);
        player.openInventory(inventory);
    }

    private ItemStack A(String string, Object object) {
        Material material = this.B(string);
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = this.C(string);
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD300" + string2));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna warto\u015b\u0107: &f" + String.valueOf((Object)object)));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8ID: &7" + string));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d22\u1d0d\u026a\u1d07\u0274\u026a\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*
     * Exception decompiling
     */
    private Material B(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private String C(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private String A(String var1_1, boolean var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }
}

