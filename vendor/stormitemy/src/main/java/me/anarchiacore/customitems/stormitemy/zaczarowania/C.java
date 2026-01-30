/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.concurrent.ThreadLocalRandom
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.zaczarowania;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;
import me.anarchiacore.customitems.stormitemy.zaczarowania.D;

public class C {
    private final Main C;
    private D E;
    private final Map<String, Integer> B = new HashMap();
    private volatile long A = 0L;
    private static final long D = 300000L;

    public C(Main main) {
        this.C = main;
        this.B();
    }

    public void A() {
        this.B();
        this.C.getLogger().info("[ZaczarowaniaManager] Cache enchantment\u00f3w prze\u0142adowany");
    }

    public void A(D d2) {
        this.E = d2;
    }

    public D C() {
        return this.E;
    }

    public ItemStack E() {
        Material material;
        String string3 = this.C.getConfig().getString("zaczarowania.item.material", "BLUE_DYE");
        try {
            material = Material.valueOf((String)string3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            this.C.getLogger().warning("Nieznany materia\u0142 dla zaczarowania: " + string3);
            material = Material.BLUE_DYE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            this.C.getLogger().warning("Nie mo\u017cna pobra\u0107 ItemMeta dla materia\u0142u: " + String.valueOf((Object)material));
            return itemStack;
        }
        String string4 = this.C.getConfig().getString("zaczarowania.item.name", "&3&lZaczarowanie przedmiotu");
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4));
        List<String> lore = this.C.getConfig().getStringList("zaczarowania.item.lore");
        List<String> coloredLore = new ArrayList();
        for (String line : lore) {
            coloredLore.add(me.anarchiacore.customitems.stormitemy.utils.color.A.C(line));
        }
        itemMeta.setLore(coloredLore);
        List<String> flags = this.C.getConfig().getStringList("zaczarowania.item.flags");
        for (String flagName : flags) {
            try {
                ItemFlag flag = ItemFlag.valueOf(flagName);
                itemMeta.addItemFlags(flag);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                this.C.getLogger().warning("Nieznana flaga przedmiotu: " + flagName);
            }
        }
        List<String> enchantmentEntries = this.C.getConfig().getStringList("zaczarowania.item.enchantments");
        for (String enchantmentEntry : enchantmentEntries) {
            String[] stringArray = enchantmentEntry.split(":");
            if (stringArray.length != 2) continue;
            try {
                Enchantment enchantment = Enchantment.getByName((String)stringArray[0]);
                int n2 = Integer.parseInt((String)stringArray[1]);
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, n2, true);
                    continue;
                }
                this.C.getLogger().warning("Nieznane zaczarowanie: " + stringArray[0]);
            }
            catch (NumberFormatException numberFormatException) {
                this.C.getLogger().warning("B\u0142\u0105d parsowania poziomu zaczarowania: " + stringArray[1]);
            }
        }
        int n3 = this.C.getConfig().getInt("zaczarowania.item.customModelData", 1);
        itemMeta.setCustomModelData(Integer.valueOf((int)n3));
        boolean bl = this.C.getConfig().getBoolean("zaczarowania.item.unbreakable", false);
        itemMeta.setUnbreakable(bl);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean A(ItemStack itemStack) {
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        String string = this.C.getConfig().getString("zaczarowania.item.material", "BLUE_DYE");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BLUE_DYE;
        }
        String string2 = this.C.getConfig().getString("zaczarowania.item.name", "&3&lZaczarowanie przedmiotu");
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2);
        String string4 = ChatColor.stripColor((String)string3);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string5 = ChatColor.stripColor((String)itemMeta.getDisplayName());
        return itemStack.getType() == material && (string5.equalsIgnoreCase(string4) || string5.equalsIgnoreCase("Zaczarowanie przedmiotu"));
    }

    public ItemStack A(ItemStack itemStack, int n2) {
        if (itemStack == null || !this.B(itemStack)) {
            return itemStack;
        }
        ItemStack itemStack2 = itemStack.clone();
        ItemMeta itemMeta = itemStack2.getItemMeta();
        ArrayList arrayList = itemMeta.hasLore() ? new ArrayList((Collection)itemMeta.getLore()) : new ArrayList();
        arrayList.removeIf(string -> this.C((String)string));
        int n3 = this.C.getConfig().getInt("zaczarowania.effects.negative_threshold", 0);
        int n4 = this.C.getConfig().getInt("zaczarowania.effects.high_threshold", 40);
        String string2 = this.C.getConfig().getString("zaczarowania.effects.format_negative", "&cDodatkowe obra\u017cenia {percent}%");
        String string3 = this.C.getConfig().getString("zaczarowania.effects.format_normal", "&fDodatkowe obra\u017cenia {percent}%");
        String string4 = this.C.getConfig().getString("zaczarowania.effects.format_high", "&6Dodatkowe obra\u017cenia {percent}%");
        String string5 = n2 < n3 ? me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2))) : (n2 >= n4 ? me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2))) : me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3.replace((CharSequence)"{percent}", (CharSequence)String.valueOf((int)n2))));
        arrayList.add((Object)string5);
        itemMeta.setLore((List)arrayList);
        itemStack2.setItemMeta(itemMeta);
        return itemStack2;
    }

    public int D() {
        boolean bl = this.C.getConfig().getBoolean("zaczarowania.effects.probability.enabled", false);
        if (!bl) {
            int n2 = this.C.getConfig().getInt("zaczarowania.effects.min_bonus", -20);
            int n3 = this.C.getConfig().getInt("zaczarowania.effects.max_bonus", 50);
            if (n3 <= n2) {
                n3 = n2 + 1;
            }
            return ThreadLocalRandom.current().nextInt(n2, n3 + 1);
        }
        ConfigurationSection configurationSection = this.C.getConfig().getConfigurationSection("zaczarowania.effects.probability.ranges");
        if (configurationSection == null) {
            this.C.getLogger().warning("Brak konfiguracji zakres\u00f3w prawdopodobie\u0144stwa! U\u017cywam domy\u015blnego losowania.");
            int n4 = this.C.getConfig().getInt("zaczarowania.effects.min_bonus", -20);
            int n5 = this.C.getConfig().getInt("zaczarowania.effects.max_bonus", 50);
            if (n5 <= n4) {
                n5 = n4 + 1;
            }
            return ThreadLocalRandom.current().nextInt(n4, n5 + 1);
        }
        List<_A> ranges = new ArrayList();
        int n6 = 0;
        boolean bl2 = this.C.getLuckyEventManager() != null && this.C.getLuckyEventManager().D();
        int n7 = this.C.getConfig().getInt("zaczarowania.effects.high_threshold", 40);
        for (String string : configurationSection.getKeys(false)) {
            try {
                int n8 = configurationSection.getInt(string, 0);
                if (n8 <= 0) continue;
                String[] range = this.A(string);
                if (range == null) {
                    this.C.getLogger().warning("Nieprawid\u0142owy format zakresu: " + string);
                    continue;
                }
                int n9 = Integer.parseInt(range[0]);
                int n10 = Integer.parseInt(range[1]);
                if (n10 < n9) {
                    this.C.getLogger().warning("Nieprawid\u0142owy zakres (max < min): " + string);
                    continue;
                }
                if (bl2) {
                    if (n10 < 0) continue;
                    if (n10 >= n7) {
                        n8 *= 5;
                    } else if (n9 >= 0) {
                        n8 *= 3;
                    }
                }
                ranges.add(new _A(n9, n10, n8));
                n6 += n8;
            }
            catch (NumberFormatException numberFormatException) {
                this.C.getLogger().warning("B\u0142\u0105d parsowania zakresu: " + string + " - " + numberFormatException.getMessage());
            }
        }
        if (ranges.isEmpty() || n6 == 0) {
            this.C.getLogger().warning("Brak prawid\u0142owych zakres\u00f3w! U\u017cywam domy\u015blnego losowania.");
            return ThreadLocalRandom.current().nextInt(-20, 51);
        }
        int n11 = ThreadLocalRandom.current().nextInt(n6);
        int n12 = 0;
        for (_A range : ranges) {
            if (n11 >= (n12 += range.C)) continue;
            return ThreadLocalRandom.current().nextInt(range.B, range.A + 1);
        }
        _A range = ranges.get(ranges.size() - 1);
        return ThreadLocalRandom.current().nextInt(range.B, range.A + 1);
    }

    private String[] A(String string) {
        if ((string = string.trim()).startsWith("-")) {
            int n2 = string.indexOf("-", 1);
            if (n2 == -1) {
                return null;
            }
            String string2 = string.substring(0, n2);
            String string3 = string.substring(n2 + 1);
            if (string3.startsWith("-")) {
                return new String[]{string2, string3};
            }
            return new String[]{string2, string3};
        }
        String[] stringArray = string.split("-");
        if (stringArray.length != 2) {
            return null;
        }
        return stringArray;
    }

    public boolean B(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        return itemStack.getType() == Material.WOODEN_SWORD || itemStack.getType() == Material.STONE_SWORD || itemStack.getType() == Material.IRON_SWORD || itemStack.getType() == Material.GOLDEN_SWORD || itemStack.getType() == Material.DIAMOND_SWORD || itemStack.getType() == Material.NETHERITE_SWORD;
    }

    public int C(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasLore()) {
            return 0;
        }
        List list = itemMeta.getLore();
        if (list == null) {
            return 0;
        }
        for (String string : list) {
            if (!this.C(string)) continue;
            try {
                String string2 = ChatColor.stripColor((String)string);
                return Integer.parseInt((String)string2.replaceAll("[^-?0-9]+", ""));
            }
            catch (NumberFormatException numberFormatException) {
                return 0;
            }
        }
        return 0;
    }

    private boolean C(String string) {
        String[] stringArray;
        if (string == null) {
            return false;
        }
        String string2 = ChatColor.stripColor((String)string);
        String string3 = this.C.getConfig().getString("zaczarowania.effects.format_negative", "&cDodatkowe obra\u017cenia {percent}%");
        String string4 = this.C.getConfig().getString("zaczarowania.effects.format_normal", "&fDodatkowe obra\u017cenia {percent}%");
        String string5 = this.C.getConfig().getString("zaczarowania.effects.format_high", "&6Dodatkowe obra\u017cenia {percent}%");
        for (String string6 : stringArray = new String[]{string3, string4, string5}) {
            String string7 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6));
            String string8 = string7.replace((CharSequence)"{percent}", (CharSequence)"-?\\d+");
            try {
                if (!string2.matches(".*" + string8 + ".*")) continue;
                return true;
            }
            catch (Exception exception) {
                String string9 = string7.replace((CharSequence)"{percent}", (CharSequence)"").trim();
                if (string9.isEmpty() || !string2.contains((CharSequence)string9)) continue;
                return true;
            }
        }
        return string2.contains((CharSequence)"Dodatkowe obra\u017cenia") || string2.contains((CharSequence)"\u1d05\u1d0f\u1d05\u1d00\u1d1b\u1d0b\u1d0f\u1d21\u1d07 \u1d0f\u0299\u0280\u1d00\u1d22\u0307\u1d07\u0274\u026a\u1d00") || string2.matches(".*[+-]?\\d+%.*");
    }

    private void B() {
        this.B.clear();
        List<String> entries = this.C.getConfig().getStringList("zaczarowania.item.enchantments");
        for (String string : entries) {
            String[] stringArray = string.split(":");
            if (stringArray.length != 2) continue;
            try {
                Enchantment enchantment = Enchantment.getByName((String)stringArray[0]);
                int n2 = Integer.parseInt((String)stringArray[1]);
                if (enchantment == null) continue;
                this.B.put(enchantment.getKey().getKey(), n2);
            }
            catch (NumberFormatException numberFormatException) {
                this.C.getLogger().warning("B\u0142\u0105d parsowania enchantmentu: " + string);
            }
        }
        this.A = System.currentTimeMillis();
    }

    public void A(ItemMeta itemMeta) {
        if (itemMeta == null) {
            return;
        }
        long l2 = System.currentTimeMillis();
        if (l2 - this.A > 300000L) {
            this.B();
        }
        for (Map.Entry entry : this.B.entrySet()) {
            Enchantment enchantment = Enchantment.getByName((String)((String)entry.getKey()));
            if (enchantment == null) continue;
            itemMeta.addEnchant(enchantment, ((Integer)entry.getValue()).intValue(), true);
        }
    }

    private static class _A {
        int B;
        int A;
        int C;

        _A(int n2, int n3, int n4) {
            this.B = n2;
            this.A = n3;
            this.C = n4;
        }
    }
}
