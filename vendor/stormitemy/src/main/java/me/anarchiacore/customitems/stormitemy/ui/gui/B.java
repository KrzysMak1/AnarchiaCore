/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NoSuchFieldException
 *  java.lang.NoSuchMethodException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Field
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.ui.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.D;
import me.anarchiacore.customitems.stormitemy.core.ItemRegistry;
import me.anarchiacore.customitems.stormitemy.messages.A;
import me.anarchiacore.customitems.stormitemy.messages.C;

public class B {
    private final Main E;
    private me.anarchiacore.customitems.stormitemy.listeners.C B;
    private final Map<String, ItemStack> F = new LinkedHashMap();
    private final Map<String, String> A = new HashMap();
    private static final int C = 28;
    private volatile long G = 0L;
    private static final long D = 30000L;

    public B(Main main) {
        this.E = main;
        this.A();
    }

    public void A(me.anarchiacore.customitems.stormitemy.listeners.C c2) {
        this.B = c2;
    }

    private void A() {
        Object object;
        this.F.clear();
        this.A.clear();
        Map<String, Object> map = this.E.getItems();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Map.Entry entry3 = null;
        for (Map.Entry entry4 : map.entrySet()) {
            Object object2 = (String)entry4.getKey();
            object = entry4.getValue();
            if (object == null) continue;
            if (object2.toLowerCase().equals((Object)"zaczarowanie")) {
                entry3 = entry4;
                continue;
            }
            if (object2.toLowerCase().contains((CharSequence)"anarchicz")) {
                arrayList2.add((Object)entry4);
                continue;
            }
            arrayList.add((Object)entry4);
        }
        arrayList.sort((entry, entry2) -> ((String)entry.getKey()).compareToIgnoreCase((String)entry2.getKey()));
        arrayList2.sort((entry, entry2) -> ((String)entry.getKey()).compareToIgnoreCase((String)entry2.getKey()));
        this.A((List<Map.Entry<String, Object>>)arrayList);
        this.A((List<Map.Entry<String, Object>>)arrayList2);
        if (entry3 != null) {
            this.A((String)entry3.getKey(), entry3.getValue());
        }
        try {
            me.anarchiacore.customitems.stormitemy.books.A a2 = this.E.getEnchantedBooksManager();
            if (a2 != null) {
                for (Object object2 : a2.C()) {
                    object = ((D)object2).getIdentifier();
                    ItemStack itemStack = ItemRegistry.getItemStack((String)object);
                    if (itemStack == null) {
                        itemStack = ((D)object2).getBookItem();
                    }
                    if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) continue;
                    String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                    this.F.put(object, (Object)itemStack.clone());
                    this.A.put((Object)string, object);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void A(List<Map.Entry<String, Object>> list) {
        for (Map.Entry entry : list) {
            this.A((String)entry.getKey(), entry.getValue());
        }
    }

    private void A(String string, Object object) {
        try {
            Object object2;
            ItemStack itemStack = ItemRegistry.getItemStack(string);
            if (itemStack == null) {
                try {
                    object2 = object.getClass().getMethod("getItem", new Class[0]);
                    itemStack = (ItemStack)object2.invoke(object, new Object[0]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                object2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
                this.F.put((Object)string, (Object)itemStack.clone());
                this.A.put(object2, (Object)string);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void A(Player player) {
        int n2;
        long l2 = System.currentTimeMillis();
        if (l2 - this.G > 30000L || this.F.isEmpty()) {
            this.A();
            this.G = l2;
        }
        if ((n2 = (int)Math.ceil((double)((double)this.F.size() / 28.0))) < 1) {
            n2 = 1;
        }
        this.C(player, 0, n2);
    }

    public void B() {
        this.G = 0L;
        this.F.clear();
        this.A.clear();
    }

    private void C(Player player, int n2, int n3) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d18\u0280\u1d22\u1d07\u1d05\u1d0d\u026a\u1d0f\u1d1b\u00f3\u1d21 (" + (n2 + 1) + "/" + n3 + ")");
        Inventory inventory = Bukkit.createInventory(null, (int)54, (String)string);
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        ArrayList arrayList = new ArrayList(this.F.values());
        int n4 = n2 * 28;
        for (int i2 = 0; i2 < 28 && n4 + i2 < arrayList.size(); ++i2) {
            ItemStack itemStack = (ItemStack)arrayList.get(n4 + i2);
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
                inventory.setItem(50, itemStack);
            }
        }
        ItemStack itemStack = this.A(Material.BARRIER, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t"));
        inventory.setItem(49, itemStack);
        player.openInventory(inventory);
    }

    public void A(Player player, ItemStack itemStack) {
        ItemMeta itemMeta;
        String string;
        String string2;
        ItemStack itemStack2;
        ItemMeta itemMeta2;
        ItemFlag itemFlag2;
        Map map;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cB\u0142\u0105d: Nie mo\u017cna edytowa\u0107 tego przedmiotu!"));
            return;
        }
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string4 = this.F(string3);
        String string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.D(string4);
        String string6 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 " + string5);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string6);
        ItemStack itemStack3 = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta3 = itemStack3.getItemMeta();
        itemMeta3.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 nazw\u0119 przedmiotu"));
        String string7 = itemStack.getItemMeta().getDisplayName();
        itemMeta3.setLore(Arrays.asList((Object[])new String[]{me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fnazw\u0119 &7przedmiotu!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualna nazwa przedmiotu:"), string7, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!")}));
        itemMeta3.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta3);
        inventory.setItem(11, itemStack3);
        ItemStack itemStack4 = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta itemMeta4 = itemStack4.getItemMeta();
        itemMeta4.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 lore przedmiotu"));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &flore &7przedmiotu!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Aktualna lore peta:"));
        ArrayList arrayList2 = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList();
        int n2 = Math.min((int)9, (int)arrayList2.size());
        for (int i2 = 0; i2 < n2; ++i2) {
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + (String)arrayList2.get(i2)));
        }
        if (arrayList2.size() > 9) {
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (arrayList2.size() - 9) + " wi\u0119cej)"));
        }
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta4.setLore((List)arrayList);
        itemMeta4.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack4.setItemMeta(itemMeta4);
        inventory.setItem(12, itemStack4);
        ItemStack itemStack5 = new ItemStack(Material.BIRCH_SIGN);
        ItemMeta itemMeta5 = itemStack5.getItemMeta();
        itemMeta5.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 Custom Model Data"));
        int n3 = itemStack.hasItemMeta() && itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : 0;
        itemMeta5.setLore(Arrays.asList((Object[])new String[]{me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &ftexture &7przedmiotu!"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualnie cmd: &f" + n3), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"), me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!")}));
        itemMeta5.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack5.setItemMeta(itemMeta5);
        inventory.setItem(13, itemStack5);
        ItemStack itemStack6 = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta itemMeta6 = itemStack6.getItemMeta();
        itemMeta6.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 enchantmenty przedmiotu"));
        ArrayList arrayList3 = new ArrayList();
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fenchantmenty &7przedmiotu!"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne enchantmenty:"));
        if (itemStack.getItemMeta().hasEnchants()) {
            map = itemStack.getItemMeta().getEnchants();
            int n4 = 0;
            for (Map.Entry entry : map.entrySet()) {
                if (n4 >= 9) {
                    arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (map.size() - 9) + " wi\u0119cej)"));
                    break;
                }
                arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&f" + ((Enchantment)entry.getKey()).getName() + " " + String.valueOf((Object)entry.getValue())));
                ++n4;
            }
        } else {
            arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&fBRAK ENCHANTMENT\u00d3W"));
        }
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList3.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
        itemMeta6.setLore((List)arrayList3);
        itemMeta6.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack6.setItemMeta(itemMeta6);
        inventory.setItem(14, itemStack6);
        map = new ItemStack(Material.MAGENTA_BANNER);
        ItemMeta itemMeta7 = map.getItemMeta();
        itemMeta7.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 flagi przedmiotu"));
        Iterator iterator = new ArrayList();
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fflagi &7przedmiotu!"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualne flagi:"));
        if (itemStack.getItemMeta().getItemFlags().size() > 0) {
            int n5 = 0;
            for (ItemFlag itemFlag2 : itemStack.getItemMeta().getItemFlags()) {
                if (n5 >= 9) {
                    iterator.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7... (+" + (itemStack.getItemMeta().getItemFlags().size() - 9) + " wi\u0119cej)"));
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
        itemMeta7.setLore((List)iterator);
        itemMeta7.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        map.setItemMeta(itemMeta7);
        inventory.setItem(15, (ItemStack)map);
        String string8 = (String)this.A.get((Object)string3);
        int n6 = this.E(string8);
        if (n6 > 0) {
            itemFlag2 = new ItemStack(Material.CLOCK);
            itemMeta2 = itemFlag2.getItemMeta();
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 cooldown przedmiotu"));
            ArrayList arrayList4 = new ArrayList();
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fcooldown &7przedmiotu!"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny cooldown: &f" + n6 + "s"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList4.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
            itemMeta2.setLore((List)arrayList4);
            itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemFlag2.setItemMeta(itemMeta2);
            inventory.setItem(21, (ItemStack)itemFlag2);
        }
        itemFlag2 = new ItemStack(Material.BEDROCK);
        itemMeta2 = itemFlag2.getItemMeta();
        boolean bl = itemStack.getItemMeta().isUnbreakable();
        itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFCzy ma posiada\u0107 Unbreakable"));
        ArrayList arrayList5 = new ArrayList();
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fw\u0142\u0105czy\u0107 i wy\u0142\u0105czy\u0107"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &funbreakable &7na przedmiocie!"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Status: " + (bl ? "&fW\u0141\u0104CZONE" : "&fWY\u0141\u0104CZONE")));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList5.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\u029f\u1d00\u1d04\u1d22\u028f\u0107!"));
        itemMeta2.setLore((List)arrayList5);
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemFlag2.setItemMeta(itemMeta2);
        inventory.setItem(22, (ItemStack)itemFlag2);
        if (string8 != null && n6 > 0) {
            itemStack2 = new ItemStack(Material.LIGHT_BLUE_CANDLE);
            ItemMeta itemMeta8 = itemStack2.getItemMeta();
            itemMeta8.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FFZmie\u0144 kolor cooldownu"));
            string2 = this.D(string8);
            string = string2.replace((CharSequence)"&", (CharSequence)"");
            itemMeta = new ArrayList();
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fedytowa\u0107"));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fkolor cooldownu &7na actionbarze!"));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Aktualny kolor: " + string2 + string));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            itemMeta.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d07\u1d05\u028f\u1d1b\u1d0f\u1d21\u1d00\u0107!"));
            itemMeta8.setLore((List)itemMeta);
            itemMeta8.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack2.setItemMeta(itemMeta8);
            inventory.setItem(23, itemStack2);
        }
        itemStack2 = this.A(Material.BARRIER, me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edytora"));
        inventory.setItem(39, itemStack2);
        boolean bl2 = string8 != null ? !this.C(string8) : true;
        string2 = bl2 ? Material.LIME_DYE : Material.RED_DYE;
        string = new ItemStack((Material)string2);
        itemMeta = string.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(bl2 ? "&#1DFF1AW\u0142\u0105czony" : "&#FF0000Wy\u0142\u0105czony"));
        ArrayList arrayList6 = new ArrayList();
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Tutaj mo\u017cesz &fw\u0142\u0105czy\u0107 &7i &fwy\u0142\u0105czy\u0107"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &fprzedmiot &7w grze!"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Status: " + (bl2 ? "&fW\u0141\u0104CZONY" : "&fWY\u0141\u0104CZONY")));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList6.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u1d07\u029f\u1d00\u1d04\u1d22\u028f\u0107!"));
        itemMeta.setLore((List)arrayList6);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        string.setItemMeta(itemMeta);
        inventory.setItem(41, (ItemStack)string);
        player.openInventory(inventory);
    }

    public void B(Player player, int n2, int n3) {
        this.C(player, n2, n3);
    }

    public void A(Player player, int n2, int n3) {
        this.C(player, n2, n3);
    }

    private String F(String string) {
        return string.replace((CharSequence)"\u0105", (CharSequence)"a").replace((CharSequence)"\u0107", (CharSequence)"c").replace((CharSequence)"\u0119", (CharSequence)"e").replace((CharSequence)"\u0142", (CharSequence)"l").replace((CharSequence)"\u0144", (CharSequence)"n").replace((CharSequence)"\u00f3", (CharSequence)"o").replace((CharSequence)"\u015b", (CharSequence)"s").replace((CharSequence)"\u017a", (CharSequence)"z").replace((CharSequence)"\u017c", (CharSequence)"z").replace((CharSequence)"\u0104", (CharSequence)"A").replace((CharSequence)"\u0106", (CharSequence)"C").replace((CharSequence)"\u0118", (CharSequence)"E").replace((CharSequence)"\u0141", (CharSequence)"L").replace((CharSequence)"\u0143", (CharSequence)"N").replace((CharSequence)"\u00d3", (CharSequence)"O").replace((CharSequence)"\u015a", (CharSequence)"S").replace((CharSequence)"\u0179", (CharSequence)"Z").replace((CharSequence)"\u017b", (CharSequence)"Z");
    }

    private ItemStack A(Material material, String string) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(string);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean A(Player player, int n2, int n3, int n4) {
        if (n2 == 48 && n3 > 0) {
            this.C(player, n3 - 1, n4);
            return true;
        }
        if (n2 == 50 && n3 < n4 - 1) {
            this.C(player, n3 + 1, n4);
            return true;
        }
        if (n2 == 49) {
            player.closeInventory();
            return true;
        }
        return false;
    }

    public boolean A(Player player, int n2, ItemStack itemStack) {
        if (n2 == 11) {
            return true;
        }
        if (n2 == 12) {
            this.C(player, itemStack);
            return true;
        }
        if (n2 == 13) {
            return true;
        }
        if (n2 == 14) {
            this.B(player, itemStack);
            return true;
        }
        if (n2 == 15) {
            this.E(player, itemStack);
            return true;
        }
        if (n2 == 21) {
            int n3;
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
            String string2 = (String)this.A.get((Object)string);
            if (string2 != null && (n3 = this.E(string2)) > 0) {
                player.closeInventory();
                if (this.B != null) {
                    this.B.setWaitingForCooldown(player, true);
                }
                me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300nowy cooldown &7przedmiotu w &fsekundach &8(&7Wpisz &#FF0000/anuluj&7, aby anulowa\u0107&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Aktualnie: &f" + n3 + "s")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
                this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
            }
            return true;
        }
        if (n2 == 23) {
            int n4;
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
            String string3 = (String)this.A.get((Object)string);
            if (string3 != null && (n4 = this.E(string3)) > 0) {
                player.closeInventory();
                if (this.B != null) {
                    this.B.setWaitingForCooldownColor(player, true);
                }
                String string4 = this.D(string3);
                me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8\u00bb &7Wpisz &#FFD300nowy kolor &7cooldownu w &fformacie hex &8(&7np. &#FFD300#FF5555&7 lub &#FFD300&#FF5555&8)&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Aktualnie: " + string4)).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
                this.A(player, "ENTITY_EXPERIENCE_ORB_PICKUP");
            }
            return true;
        }
        if (n2 == 22) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            boolean bl = itemMeta.isUnbreakable();
            boolean bl2 = !bl;
            itemMeta.setUnbreakable(bl2);
            itemStack.setItemMeta(itemMeta);
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
            String string5 = (String)this.A.get((Object)string);
            if (string5 != null) {
                this.A(string5, bl2, player);
            }
            return true;
        }
        if (n2 == 39) {
            this.A(player);
            return true;
        }
        if (n2 == 41) {
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
            String string6 = (String)this.A.get((Object)string);
            if (string6 != null) {
                boolean bl = this.C(string6);
                this.B(string6, bl, player);
                me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C(bl ? "&8\u00bb &7Przedmiot zosta\u0142 &#1DFF1Aw\u0142\u0105czony&7!" : "&8\u00bb &7Przedmiot zosta\u0142 &#FF0000wy\u0142\u0105czony&7!")).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(bl ? "&fPrzedmiot &7zosta\u0142 &#1DFF1Aw\u0142\u0105czony" : "&fPrzedmiot &7zosta\u0142 &#FF0000wy\u0142\u0105czony")).A(10).B(60).C(15).A();
                me.anarchiacore.customitems.stormitemy.messages.C.A(player, a4);
                this.A(player, "ENTITY_PLAYER_LEVELUP");
                this.A(player, itemStack);
            }
            return true;
        }
        return false;
    }

    public void C(Player player, ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string2 = this.F(string);
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u029f\u1d0f\u0280\u1d07 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        ArrayList arrayList = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList();
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
        boolean bl = arrayList.size() < 14;
        ItemStack itemStack3 = new ItemStack(bl ? Material.LIME_DYE : Material.LIGHT_GRAY_DYE);
        ItemMeta itemMeta2 = itemStack3.getItemMeta();
        if (bl) {
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj linijk\u0119 lore"));
        } else {
            itemMeta2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7Dodaj linijk\u0119 lore"));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cOsi\u0105gni\u0119to maksymaln\u0105 liczb\u0119 linijek (14)"));
            itemMeta2.setLore((List)arrayList2);
        }
        itemMeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack3.setItemMeta(itemMeta2);
        inventory.setItem(41, itemStack3);
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
        boolean bl = arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void A(Player player, String string, ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            me.anarchiacore.customitems.stormitemy.messages.A a2 = new A._A().D("&cNie mo\u017cna zaktualizowa\u0107 przedmiotu!").B("&cB\u0142\u0105d systemu aktualizacji przedmiotu!").A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a2);
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string3 = (String)this.A.get((Object)string2);
        if (string3 == null) {
            me.anarchiacore.customitems.stormitemy.messages.A a3 = new A._A().D("&cNie znaleziono klucza przedmiotu!").B("&cNie znaleziono przedmiotu!").A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a3);
            this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            return;
        }
        if (!this.B(string3, string)) {
            me.anarchiacore.customitems.stormitemy.messages.A a4 = new A._A().D("&cNie uda\u0142o si\u0119 zaktualizowa\u0107 konfiguracji!").B("&cB\u0142\u0105d przy aktualizacji!").A(10).B(60).C(15).A();
            me.anarchiacore.customitems.stormitemy.messages.C.A(player, a4);
            this.A(player, "ENTITY_ENDERMAN_TELEPORT");
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD700" + string));
        itemStack.setItemMeta(itemMeta);
        this.F.put((Object)string3, (Object)itemStack);
        this.A.remove((Object)string2);
        this.A.put((Object)string, (Object)string3);
        String string4 = "&8\u00bb &7Zmieniono nazw\u0119 z &f" + string2 + " &7na &#27FF00" + string + "&7!";
        String string5 = "&fNowa &7nazwa zosta\u0142a &#27FF00ustawiona&7!";
        me.anarchiacore.customitems.stormitemy.messages.A a5 = new A._A().D(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4)).B(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5)).A(10).B(60).C(15).A();
        me.anarchiacore.customitems.stormitemy.messages.C.A(player, a5);
        this.A(player, "ENTITY_PLAYER_LEVELUP");
        this.A(player, itemStack);
    }

    public void A(Player player, ItemStack itemStack, List<String> list) {
        String string;
        File file;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string3 = (String)this.A.get((Object)string2);
        if (string3 == null) {
            return;
        }
        File file2 = new File(this.E.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, string3 + ".yml")).exists()) {
            string = string3.replace((CharSequence)"_", (CharSequence)"");
            file = new File(file2, string + ".yml");
        }
        if (!file.exists()) {
            return;
        }
        try {
            string = YamlConfiguration.loadConfiguration((File)file);
            Set set = string.getKeys(false);
            if (set.isEmpty()) {
                return;
            }
            String string4 = (String)set.iterator().next();
            string.set(string4 + ".lore", list);
            string.save(file);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
            this.F.put((Object)string3, (Object)itemStack.clone());
            this.A(string3, player);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void A(Player player, ItemStack itemStack, int n2) {
        String string;
        File file;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string3 = (String)this.A.get((Object)string2);
        if (string3 == null) {
            return;
        }
        File file2 = new File(this.E.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, string3 + ".yml")).exists()) {
            string = string3.replace((CharSequence)"_", (CharSequence)"");
            file = new File(file2, string + ".yml");
        }
        if (!file.exists()) {
            return;
        }
        try {
            string = YamlConfiguration.loadConfiguration((File)file);
            Set set = string.getKeys(false);
            if (set.isEmpty()) {
                return;
            }
            String string4 = (String)set.iterator().next();
            string.set(string4 + ".customModelData", (Object)n2);
            string.save(file);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setCustomModelData(Integer.valueOf((int)n2));
            itemStack.setItemMeta(itemMeta);
            this.F.put((Object)string3, (Object)itemStack.clone());
            this.A(string3, player);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private void A(Player player, String string) {
        try {
            player.playSound(player.getLocation(), string, 1.0f, 1.0f);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean B(String string, String string2) {
        YamlConfiguration yamlConfiguration;
        File file;
        File file2 = new File(this.E.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, string + ".yml")).exists()) {
            yamlConfiguration = string.replace((CharSequence)"_", (CharSequence)"");
            file = new File(file2, (String)yamlConfiguration + ".yml");
        }
        if (!file.exists() && (yamlConfiguration = file2.listFiles()) != null) {
            for (YamlConfiguration yamlConfiguration2 : yamlConfiguration) {
                if (!yamlConfiguration2.getName().endsWith(".yml")) continue;
                try {
                    YamlConfiguration yamlConfiguration3 = YamlConfiguration.loadConfiguration((File)yamlConfiguration2);
                    for (String string3 : yamlConfiguration3.getKeys(false)) {
                        if (!string3.equalsIgnoreCase(string)) continue;
                        file = yamlConfiguration2;
                        break;
                    }
                    if (!file.exists()) continue;
                    break;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (!file.exists()) {
            return false;
        }
        try {
            yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            YamlConfiguration yamlConfiguration4 = yamlConfiguration.getKeys(false);
            if (yamlConfiguration4.isEmpty()) {
                return false;
            }
            String string4 = (String)yamlConfiguration4.iterator().next();
            yamlConfiguration.set(string4 + ".name", (Object)string2);
            yamlConfiguration.save(file);
            this.G(string);
            return true;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return false;
        }
    }

    private void G(String string) {
        this.A(string, (Player)null);
    }

    private void A(String string, Player player) {
        String string2;
        Object object;
        File file;
        Object object2;
        block20: {
            object2 = this.E.getItem(string);
            if (object2 == null) {
                return;
            }
            try {
                Field field;
                file = new File(this.E.getDataFolder(), "items");
                object = new File(file, string + ".yml");
                if (!object.exists()) {
                    string2 = string.replace((CharSequence)"_", (CharSequence)"");
                    object = new File(file, string2 + ".yml");
                }
                if (!object.exists()) {
                    string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
                    field = file.listFiles();
                    if (field != null) {
                        for (Field field2 : field) {
                            String string3;
                            if (!field2.getName().endsWith(".yml") || !(string3 = field2.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase()).equals((Object)string2) && !string3.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) continue;
                            object = field2;
                            break;
                        }
                    }
                }
                if (!object.exists()) break block20;
                string2 = YamlConfiguration.loadConfiguration((File)object);
                try {
                    String string4;
                    ConfigurationSection configurationSection;
                    field = object2.getClass().getDeclaredField("config");
                    field.setAccessible(true);
                    Field field3 = string2.getKeys(false);
                    if (!field3.isEmpty() && (configurationSection = string2.getConfigurationSection(string4 = (String)field3.iterator().next())) != null) {
                        field.set(object2, (Object)configurationSection);
                    }
                }
                catch (NoSuchFieldException noSuchFieldException) {}
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        try {
            file = object2.getClass().getMethod("reload", new Class[0]);
            file.invoke(object2, new Object[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            file = ItemRegistry.getItemStack(string);
            if (file == null) {
                try {
                    object = object2.getClass().getMethod("getItem", new Class[0]);
                    file = (ItemStack)object.invoke(object2, new Object[0]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (file != null && file.hasItemMeta() && file.getItemMeta().hasDisplayName()) {
                object = me.anarchiacore.customitems.stormitemy.utils.color.A.B(file.getItemMeta().getDisplayName());
                this.F.put((Object)string, (Object)file.clone());
                this.A.put(object, (Object)string);
                string2 = file;
                if (player != null) {
                    this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> this.D(player, (ItemStack)string2));
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void B(Player player, ItemStack itemStack) {
        ArrayList arrayList;
        ItemMeta itemMeta;
        ItemStack itemStack2;
        Map.Entry entry2;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string2 = this.F(string);
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \u1d07\u0274\u1d04\u029c\u1d00\u0274\u1d1b\u00f3\u1d21 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        HashMap hashMap = itemStack.getItemMeta().hasEnchants() ? itemStack.getItemMeta().getEnchants() : new HashMap();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = 0;
        for (Map.Entry entry2 : hashMap.entrySet()) {
            if (n2 >= nArray.length) break;
            itemStack2 = new ItemStack(Material.ENCHANTED_BOOK);
            itemMeta = itemStack2.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#DD00FF" + ((Enchantment)entry2.getKey()).getName() + " " + String.valueOf((Object)entry2.getValue())));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack2.setItemMeta(itemMeta);
            inventory.setItem(nArray[n2], itemStack2);
            ++n2;
        }
        Iterator iterator = new ItemStack(Material.BARRIER);
        entry2 = iterator.getItemMeta();
        entry2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        entry2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        iterator.setItemMeta((ItemMeta)entry2);
        inventory.setItem(39, (ItemStack)iterator);
        itemStack2 = new ItemStack(Material.LIME_DYE);
        itemMeta = itemStack2.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj enchantment"));
        arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kliknij aby doda\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7nowy enchantment!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack2.setItemMeta(itemMeta);
        inventory.setItem(41, itemStack2);
        player.openInventory(inventory);
    }

    public void E(Player player, ItemStack itemStack) {
        ArrayList arrayList;
        ItemMeta itemMeta;
        ItemStack itemStack2;
        ItemFlag itemFlag2;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.B(itemStack.getItemMeta().getDisplayName());
        String string2 = this.F(string);
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C("\u00a7x\u00a7F\u00a7F\u00a7F\u00a79\u00a71\u00a7A\u26a1 \u00a78\u1d07\u1d05\u028f\u1d1b\u1d0f\u0280 \ua730\u029f\u1d00\u0262 " + string2);
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)string3);
        Set set = itemStack.getItemMeta().getItemFlags();
        int[] nArray = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int n2 = 0;
        for (ItemFlag itemFlag2 : set) {
            if (n2 >= nArray.length) break;
            itemStack2 = new ItemStack(Material.PAPER);
            itemMeta = itemStack2.getItemMeta();
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FFD700" + itemFlag2.name()));
            arrayList = new ArrayList();
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Flaga: &f" + itemFlag2.name()));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
            arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a &#FF0000&n\ua731\u029c\u026a\ua730\u1d1b + \u1d18\u0280\u1d00\u1d21\u028f&7, \u1d00\u0299\u028f \u1d1c\ua731\u1d1c\u0274\u1d00\u0107!"));
            itemMeta.setLore((List)arrayList);
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemStack2.setItemMeta(itemMeta);
            inventory.setItem(nArray[n2], itemStack2);
            ++n2;
        }
        Iterator iterator = new ItemStack(Material.BARRIER);
        itemFlag2 = iterator.getItemMeta();
        itemFlag2.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#FF0000Powr\u00f3t do edycji"));
        itemFlag2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        iterator.setItemMeta((ItemMeta)itemFlag2);
        inventory.setItem(39, (ItemStack)iterator);
        itemStack2 = new ItemStack(Material.LIME_DYE);
        itemMeta = itemStack2.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&#1DFF1ADodaj flag\u0119"));
        arrayList = new ArrayList();
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7Kliknij aby doda\u0107"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&8 \u00bb &7now\u0105 flag\u0119!"));
        arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C("&7"));
        itemMeta.setLore((List)arrayList);
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemStack2.setItemMeta(itemMeta);
        inventory.setItem(41, itemStack2);
        player.openInventory(inventory);
    }

    public void A(String string, boolean bl) {
        this.A(string, bl, null);
    }

    public void A(String string, boolean bl, Player player) {
        YamlConfiguration yamlConfiguration;
        File file;
        File file2 = new File(this.E.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, string + ".yml")).exists()) {
            yamlConfiguration = string.replace((CharSequence)"_", (CharSequence)"");
            file = new File(file2, (String)yamlConfiguration + ".yml");
        }
        if (!file.exists() && (yamlConfiguration = file2.listFiles()) != null) {
            for (YamlConfiguration yamlConfiguration2 : yamlConfiguration) {
                if (!yamlConfiguration2.getName().endsWith(".yml")) continue;
                try {
                    YamlConfiguration yamlConfiguration3 = YamlConfiguration.loadConfiguration((File)yamlConfiguration2);
                    for (String string2 : yamlConfiguration3.getKeys(false)) {
                        if (!string2.equalsIgnoreCase(string)) continue;
                        file = yamlConfiguration2;
                        break;
                    }
                    if (!file.exists()) continue;
                    break;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        if (!file.exists()) {
            return;
        }
        try {
            yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            YamlConfiguration yamlConfiguration4 = yamlConfiguration.getKeys(false);
            if (yamlConfiguration4.isEmpty()) {
                return;
            }
            String string3 = (String)yamlConfiguration4.iterator().next();
            yamlConfiguration.set(string3 + ".unbreakable", (Object)bl);
            yamlConfiguration.save(file);
            this.A(string, player);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private String A(String string) {
        Set set;
        String string2;
        File file = new File(this.E.getDataFolder(), "items");
        if (!file.exists()) {
            return null;
        }
        File file2 = new File(file, string + ".yml");
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"");
            file2 = new File(file, string2 + ".yml");
        }
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
            set = file.listFiles();
            if (set != null) {
                for (Set set2 : set) {
                    if (!set2.getName().endsWith(".yml")) continue;
                    String string3 = set2.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase();
                    if (string3.equals((Object)string2) || string3.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) {
                        file2 = set2;
                        break;
                    }
                    try {
                        String string4;
                        String string5;
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)set2);
                        Set set3 = yamlConfiguration.getKeys(false);
                        if (set3.isEmpty() || !(string5 = (string4 = (String)set3.iterator().next()).replace((CharSequence)"_", (CharSequence)"").toLowerCase()).equals((Object)string2)) continue;
                        file2 = set2;
                        break;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
        if (!file2.exists()) {
            return null;
        }
        try {
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            set = string2.getKeys(false);
            if (set.isEmpty()) {
                return null;
            }
            return (String)set.iterator().next();
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy pobieraniu klucza konfiguracji: " + exception.getMessage());
            return null;
        }
    }

    private boolean C(String string) {
        String string2;
        String string3 = this.A(string);
        if (string3 == null) {
            return false;
        }
        File file = new File(this.E.getDataFolder(), "items");
        File file2 = new File(file, string + ".yml");
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"");
            file2 = new File(file, string2 + ".yml");
        }
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
            File[] fileArray = file.listFiles();
            if (fileArray != null) {
                for (File file3 : fileArray) {
                    String string4;
                    if (!file3.getName().endsWith(".yml") || !(string4 = file3.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase()).equals((Object)string2) && !string4.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) continue;
                    file2 = file3;
                    break;
                }
            }
        }
        if (!file2.exists()) {
            return false;
        }
        try {
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            return string2.getBoolean(string3 + ".disabled", false);
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy sprawdzaniu stanu przedmiotu: " + exception.getMessage());
            return false;
        }
    }

    private void B(String string, boolean bl, Player player) {
        String string2;
        File file;
        String string3 = this.A(string);
        if (string3 == null) {
            this.E.getLogger().warning("[ItemEditor] Nie znaleziono klucza konfiguracji dla: " + string);
            return;
        }
        File file2 = new File(this.E.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, string + ".yml")).exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"");
            file = new File(file2, string2 + ".yml");
        }
        if (!file.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
            File[] fileArray = file2.listFiles();
            if (fileArray != null) {
                for (File file3 : fileArray) {
                    String string4;
                    if (!file3.getName().endsWith(".yml") || !(string4 = file3.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase()).equals((Object)string2) && !string4.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) continue;
                    file = file3;
                    break;
                }
            }
        }
        if (!file.exists()) {
            this.E.getLogger().warning("[ItemEditor] Nie znaleziono pliku konfiguracyjnego dla: " + string);
            return;
        }
        try {
            string2 = YamlConfiguration.loadConfiguration((File)file);
            boolean bl2 = !bl;
            string2.set(string3 + ".disabled", (Object)bl2);
            string2.save(file);
            this.E.getLogger().info("[ItemEditor] Przedmiot " + string + " zosta\u0142 " + (bl2 ? "wy\u0142\u0105czony" : "w\u0142\u0105czony"));
            this.A(string, player);
        }
        catch (IOException iOException) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy prze\u0142\u0105czaniu stanu przedmiotu: " + iOException.getMessage());
            iOException.printStackTrace();
        }
    }

    private int E(String string) {
        Set set;
        String string2;
        if (string == null) {
            return 0;
        }
        File file = new File(this.E.getDataFolder(), "items");
        if (!file.exists()) {
            return 0;
        }
        File file2 = new File(file, string + ".yml");
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"");
            file2 = new File(file, string2 + ".yml");
        }
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
            set = file.listFiles();
            if (set != null) {
                for (Set set2 : set) {
                    if (!set2.getName().endsWith(".yml")) continue;
                    String string3 = set2.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase();
                    if (string3.equals((Object)string2) || string3.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) {
                        file2 = set2;
                        break;
                    }
                    try {
                        String string4;
                        String string5;
                        YamlConfiguration exception = YamlConfiguration.loadConfiguration((File)set2);
                        Set set3 = exception.getKeys(false);
                        if (set3.isEmpty() || !(string5 = (string4 = (String)set3.iterator().next()).replace((CharSequence)"_", (CharSequence)"").toLowerCase()).equals((Object)string2)) continue;
                        file2 = set2;
                        break;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
        if (!file2.exists()) {
            return 0;
        }
        try {
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            set = string2.getKeys(false);
            if (set.isEmpty()) {
                return 0;
            }
            String string6 = (String)set.iterator().next();
            return string2.getInt(string6 + ".cooldown", 0);
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy pobieraniu cooldownu: " + exception.getMessage());
            return 0;
        }
    }

    public void B(String string, int n2) {
        this.A(string, n2, null);
    }

    public void A(String string, int n2, Player player) {
        Set set;
        String string2;
        File file = new File(this.E.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.E.getLogger().info("[ItemEditor] Szukam pliku dla klucza: " + string);
        File file2 = new File(file, string + ".yml");
        this.E.getLogger().info("[ItemEditor] Pr\u00f3buj\u0119: " + file2.getAbsolutePath() + " - istnieje: " + file2.exists());
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"");
            file2 = new File(file, string2 + ".yml");
            this.E.getLogger().info("[ItemEditor] Pr\u00f3buj\u0119 bez podkre\u015ble\u0144: " + file2.getAbsolutePath() + " - istnieje: " + file2.exists());
        }
        if (!file2.exists()) {
            string2 = string.replace((CharSequence)"_", (CharSequence)"").toLowerCase();
            set = file.listFiles();
            if (set != null) {
                for (Set set2 : set) {
                    if (!set2.getName().endsWith(".yml")) continue;
                    String string3 = set2.getName().replace((CharSequence)".yml", (CharSequence)"").toLowerCase();
                    if (string3.equals((Object)string2) || string3.replace((CharSequence)"_", (CharSequence)"").equals((Object)string2)) {
                        file2 = set2;
                        break;
                    }
                    try {
                        String string4;
                        String string5;
                        YamlConfiguration exception = YamlConfiguration.loadConfiguration((File)set2);
                        Set set3 = exception.getKeys(false);
                        if (set3.isEmpty() || !(string5 = (string4 = (String)set3.iterator().next()).replace((CharSequence)"_", (CharSequence)"").toLowerCase()).equals((Object)string2)) continue;
                        file2 = set2;
                        break;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
        if (!file2.exists()) {
            this.E.getLogger().warning("[ItemEditor] Nie znaleziono pliku konfiguracyjnego dla: " + string);
            return;
        }
        try {
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            set = string2.getKeys(false);
            if (set.isEmpty()) {
                return;
            }
            String string6 = (String)set.iterator().next();
            string2.set(string6 + ".cooldown", (Object)n2);
            string2.save(file2);
            this.E.getLogger().info("[ItemEditor] Pomy\u015blnie zaktualizowano cooldown dla: " + file2.getName());
            this.A(string, player);
        }
        catch (IOException iOException) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy zapisywaniu konfiguracji: " + iOException.getMessage());
            iOException.printStackTrace();
        }
    }

    private String D(String string) {
        if (string == null) {
            return "&#FFFFFF";
        }
        try {
            String string2;
            String string3;
            ConfigurationSection configurationSection;
            ConfigurationSection configurationSection2 = this.E.getConfig().getConfigurationSection("actionbar");
            if (configurationSection2 != null && (configurationSection = configurationSection2.getConfigurationSection("colors")) != null && configurationSection.contains(string3 = string.toLowerCase()) && (string2 = configurationSection.getString(string3)) != null && !string2.isEmpty()) {
                return string2;
            }
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy pobieraniu koloru cooldownu: " + exception.getMessage());
        }
        return "&#FFFFFF";
    }

    public void A(String string, String string2) {
        this.A(string, string2, null);
    }

    public void A(String string, String string2, Player player) {
        if (string == null) {
            return;
        }
        try {
            ConfigurationSection configurationSection;
            ConfigurationSection configurationSection2 = this.E.getConfig().getConfigurationSection("actionbar");
            if (configurationSection2 == null) {
                configurationSection2 = this.E.getConfig().createSection("actionbar");
            }
            if ((configurationSection = configurationSection2.getConfigurationSection("colors")) == null) {
                configurationSection = configurationSection2.createSection("colors");
            }
            configurationSection.set(string, (Object)string2);
            this.E.saveConfig();
            if (player != null) {
                this.E.getServer().getScheduler().runTask((Plugin)this.E, () -> {
                    ItemStack itemStack = (ItemStack)this.F.get((Object)string);
                    if (itemStack != null) {
                        this.A(player, itemStack.clone());
                    }
                });
            }
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy zapisywaniu koloru cooldownu: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void A(String string, ItemStack itemStack) {
        this.B(string, itemStack, null);
    }

    public void B(String string, ItemStack itemStack, Player player) {
        if (string == null || itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        try {
            String string2;
            File file = new File(this.E.getDataFolder(), "items");
            File file2 = new File(file, string + ".yml");
            if (!file2.exists()) {
                string2 = string.replace((CharSequence)"_", (CharSequence)"");
                file2 = new File(file, string2 + ".yml");
            }
            if (!file2.exists()) {
                return;
            }
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            Set set = string2.getKeys(false);
            if (set.isEmpty()) {
                return;
            }
            String string3 = (String)set.iterator().next();
            string2.set(string3 + ".enchantments", null);
            if (itemStack.getItemMeta().hasEnchants()) {
                Map map = itemStack.getItemMeta().getEnchants();
                ArrayList arrayList = new ArrayList();
                for (Map.Entry entry : map.entrySet()) {
                    String string4 = ((Enchantment)entry.getKey()).getName();
                    HashMap hashMap = new HashMap();
                    hashMap.put((Object)string4, (Object)((Integer)entry.getValue()));
                    arrayList.add((Object)hashMap);
                }
                string2.set(string3 + ".enchantments", (Object)arrayList);
            }
            string2.save(file2);
            this.E.getLogger().info("[ItemEditor] Pomy\u015blnie zaktualizowano enchantmenty dla: " + string);
            this.F.put((Object)string, (Object)itemStack.clone());
            this.A(string, player);
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy zapisywaniu enchantment\u00f3w: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public String B(String string) {
        return (String)this.A.get((Object)string);
    }

    public void B(String string, ItemStack itemStack) {
        this.A(string, itemStack, null);
    }

    public void A(String string, ItemStack itemStack, Player player) {
        if (string == null || itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        try {
            String string2;
            File file = new File(this.E.getDataFolder(), "items");
            File file2 = new File(file, string + ".yml");
            if (!file2.exists()) {
                string2 = string.replace((CharSequence)"_", (CharSequence)"");
                file2 = new File(file, string2 + ".yml");
            }
            if (!file2.exists()) {
                return;
            }
            string2 = YamlConfiguration.loadConfiguration((File)file2);
            Set set = string2.getKeys(false);
            if (set.isEmpty()) {
                return;
            }
            String string3 = (String)set.iterator().next();
            string2.set(string3 + ".flags", null);
            Set set2 = itemStack.getItemMeta().getItemFlags();
            if (set2 != null && !set2.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                for (ItemFlag itemFlag : set2) {
                    arrayList.add((Object)itemFlag.name());
                }
                string2.set(string3 + ".flags", (Object)arrayList);
            }
            string2.save(file2);
            this.E.getLogger().info("[ItemEditor] Pomy\u015blnie zaktualizowano flagi dla: " + string);
            this.F.put((Object)string, (Object)itemStack.clone());
            this.A(string, player);
        }
        catch (Exception exception) {
            this.E.getLogger().warning("[ItemEditor] B\u0142\u0105d przy zapisywaniu flag: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private /* synthetic */ void D(Player player, ItemStack itemStack) {
        this.A(player, itemStack.clone());
    }
}
