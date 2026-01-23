/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.CharSequence
 *  java.lang.Double
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  java.util.WeakHashMap
 *  org.bukkit.NamespacedKey
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.enchantments.B;
import me.anarchiacore.customitems.stormitemy.books.enchantments.C;
import me.anarchiacore.customitems.stormitemy.books.enchantments.D;
import me.anarchiacore.customitems.stormitemy.books.enchantments.E;
import me.anarchiacore.customitems.stormitemy.books.enchantments.F;
import me.anarchiacore.customitems.stormitemy.books.enchantments.G;
import me.anarchiacore.customitems.stormitemy.books.enchantments.H;
import me.anarchiacore.customitems.stormitemy.books.enchantments.I;
import me.anarchiacore.customitems.stormitemy.books.enchantments.J;
import me.anarchiacore.customitems.stormitemy.books.enchantments.K;
import me.anarchiacore.customitems.stormitemy.books.enchantments.L;

public class A {
    private final Main B;
    private final Map<String, me.anarchiacore.customitems.stormitemy.books.D> A;
    private final Map<UUID, Map<String, Long>> C;

    public A(Main main) {
        this.B = main;
        this.A = new HashMap();
        this.C = new WeakHashMap();
        this.B();
    }

    private void B() {
        this.A(new me.anarchiacore.customitems.stormitemy.books.enchantments.A(this.B));
        this.A(new L(this.B));
        this.A(new I(this.B));
        this.A(new K(this.B));
        this.A(new E(this.B));
        this.A(new C(this.B));
        this.A(new F(this.B));
        this.A(new J(this.B));
        this.A(new H(this.B));
        this.A(new D(this.B));
        this.A(new B(this.B));
        this.A(new G(this.B));
    }

    public void A() {
        this.A.clear();
        File file = new File(this.B.getDataFolder(), "books");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.B();
    }

    public void A(me.anarchiacore.customitems.stormitemy.books.D d2) {
        this.A.put((Object)d2.getIdentifier(), (Object)d2);
    }

    public me.anarchiacore.customitems.stormitemy.books.D A(String string) {
        return (me.anarchiacore.customitems.stormitemy.books.D)this.A.get((Object)string);
    }

    public List<me.anarchiacore.customitems.stormitemy.books.D> C() {
        return new ArrayList(this.A.values());
    }

    public List<ItemStack> E() {
        ArrayList arrayList = new ArrayList();
        for (me.anarchiacore.customitems.stormitemy.books.D d2 : this.A.values()) {
            arrayList.add((Object)d2.getBookItem());
        }
        return arrayList;
    }

    public me.anarchiacore.customitems.stormitemy.books.D A(ItemStack itemStack) {
        NamespacedKey namespacedKey;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(namespacedKey = new NamespacedKey((Plugin)this.B, "book_enchant"), PersistentDataType.STRING)) {
            return null;
        }
        String string = (String)persistentDataContainer.get(namespacedKey, PersistentDataType.STRING);
        return this.A(string);
    }

    public boolean C(ItemStack itemStack) {
        return this.A(itemStack) != null;
    }

    public int B(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        int n2 = 0;
        for (me.anarchiacore.customitems.stormitemy.books.D d2 : this.A.values()) {
            String string = d2.getIdentifier();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.B, "enchant_" + string);
            if (!persistentDataContainer.has(namespacedKey, PersistentDataType.DOUBLE)) continue;
            ++n2;
        }
        return n2;
    }

    public boolean B(ItemStack itemStack, String string) {
        int n2;
        NamespacedKey namespacedKey;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(namespacedKey = new NamespacedKey((Plugin)this.B, "enchant_" + string), PersistentDataType.DOUBLE)) {
            return false;
        }
        persistentDataContainer.remove(namespacedKey);
        if (itemMeta.hasLore()) {
            List list = itemMeta.getLore();
            ArrayList arrayList = new ArrayList();
            for (String string2 : list) {
                if (string2.toLowerCase().contains((CharSequence)string.toLowerCase())) continue;
                arrayList.add((Object)string2);
            }
            itemMeta.setLore((List)arrayList);
        }
        if ((n2 = this.B(itemStack)) == 0) {
            boolean bl = false;
            for (String string2 : itemMeta.getEnchants().keySet()) {
                if (string2 == Enchantment.DURABILITY && itemMeta.getEnchantLevel((Enchantment)string2) <= 1) continue;
                bl = true;
                break;
            }
            if (!bl) {
                itemMeta.removeEnchant(Enchantment.DURABILITY);
                if (itemMeta.getItemFlags().size() == 1 && itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    itemMeta.removeItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
            }
        }
        itemStack.setItemMeta(itemMeta);
        return true;
    }

    public boolean A(ItemStack itemStack, String string) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.B, "enchant_" + string);
        return persistentDataContainer.has(namespacedKey, PersistentDataType.DOUBLE);
    }

    public double C(ItemStack itemStack, String string) {
        NamespacedKey namespacedKey;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return 0.0;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (!persistentDataContainer.has(namespacedKey = new NamespacedKey((Plugin)this.B, "enchant_" + string), PersistentDataType.DOUBLE)) {
            return 0.0;
        }
        return (Double)persistentDataContainer.get(namespacedKey, PersistentDataType.DOUBLE);
    }

    public void D() {
        for (me.anarchiacore.customitems.stormitemy.books.D d2 : this.A.values()) {
            d2.reload();
        }
    }

    public boolean D(ItemStack itemStack) {
        return this.B(itemStack) > 0;
    }

    public boolean B(UUID uUID, String string) {
        if (!this.C.containsKey((Object)uUID)) {
            return false;
        }
        Map map = (Map)this.C.get((Object)uUID);
        if (!map.containsKey((Object)string)) {
            return false;
        }
        long l2 = (Long)map.get((Object)string);
        return System.currentTimeMillis() < l2;
    }

    public void A(UUID uUID, String string, int n2) {
        if (n2 <= 0) {
            return;
        }
        this.C.putIfAbsent((Object)uUID, (Object)new HashMap());
        ((Map)this.C.get((Object)uUID)).put((Object)string, (Object)(System.currentTimeMillis() + (long)n2 * 1000L));
    }

    public int A(UUID uUID, String string) {
        if (!this.C.containsKey((Object)uUID)) {
            return 0;
        }
        Map map = (Map)this.C.get((Object)uUID);
        if (!map.containsKey((Object)string)) {
            return 0;
        }
        long l2 = (Long)map.get((Object)string);
        long l3 = System.currentTimeMillis();
        if (l3 >= l2) {
            return 0;
        }
        return (int)((l2 - l3) / 1000L);
    }

    public void A(UUID uUID) {
        this.C.remove((Object)uUID);
    }
}

