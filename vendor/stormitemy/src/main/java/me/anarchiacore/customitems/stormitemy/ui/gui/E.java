/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.LinkedHashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.anarchiacore.customitems.stormitemy.ui.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class E {
    private final Main C;
    private final File D;
    private FileConfiguration A;
    private final Map<String, ItemStack> B = new LinkedHashMap();

    public E(Main main) {
        this.C = main;
        this.D = new File(main.getDataFolder(), "custom_items.yml");
        this.D();
    }

    public void D() {
        this.B.clear();
        if (!this.D.exists()) {
            try {
                this.D.getParentFile().mkdirs();
                this.D.createNewFile();
                this.A = YamlConfiguration.loadConfiguration((File)this.D);
                this.A.options().header("W\u0142asne przedmioty stworzone przez kreator przedmiot\u00f3w STORMITEMY");
                this.A.save(this.D);
            }
            catch (IOException iOException) {
                this.C.getLogger().warning("[CustomItems] Nie uda\u0142o si\u0119 stworzy\u0107 pliku custom_items.yml: " + iOException.getMessage());
            }
        } else {
            this.A = YamlConfiguration.loadConfiguration((File)this.D);
        }
        if (this.A == null) {
            this.A = new YamlConfiguration();
        }
        for (String string : this.A.getKeys(false)) {
            ItemStack itemStack;
            ConfigurationSection configurationSection = this.A.getConfigurationSection(string);
            if (configurationSection == null || (itemStack = this.A(string, configurationSection)) == null) continue;
            this.B.put((Object)string, (Object)itemStack);
        }
        this.C.getLogger().info("[CustomItems] Za\u0142adowano " + this.B.size() + " w\u0142asnych przedmiot\u00f3w.");
    }

    private ItemStack A(String string, ConfigurationSection configurationSection) {
        try {
            int n2;
            String string22;
            ItemStack itemStack;
            ItemMeta itemMeta;
            String string3 = configurationSection.getString("material", "STONE");
            Material material = Material.matchMaterial((String)string3);
            if (material == null) {
                material = Material.STONE;
            }
            if ((itemMeta = (itemStack = new ItemStack(material)).getItemMeta()) == null) {
                return itemStack;
            }
            String string4 = configurationSection.getString("name", "&f" + string);
            itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4));
            List list = configurationSection.getStringList("lore");
            if (!list.isEmpty()) {
                ArrayList arrayList = new ArrayList();
                for (String string22 : list) {
                    arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string22));
                }
                itemMeta.setLore((List)arrayList);
            }
            if ((n2 = configurationSection.getInt("customModelData", 0)) > 0) {
                itemMeta.setCustomModelData(Integer.valueOf((int)n2));
            }
            boolean bl = configurationSection.getBoolean("unbreakable", false);
            itemMeta.setUnbreakable(bl);
            string22 = configurationSection.getStringList("flags");
            Iterator iterator = string22.iterator();
            while (iterator.hasNext()) {
                String string5 = (String)iterator.next();
                try {
                    ItemFlag itemFlag = ItemFlag.valueOf((String)string5.toUpperCase());
                    itemMeta.addItemFlags(new ItemFlag[]{itemFlag});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
            itemStack.setItemMeta(itemMeta);
            iterator = configurationSection.getMapList("enchantments");
            for (ItemFlag itemFlag : iterator) {
                for (Map.Entry entry : itemFlag.entrySet()) {
                    String string6 = entry.getKey().toString();
                    int n3 = Integer.parseInt((String)entry.getValue().toString());
                    Enchantment enchantment = Enchantment.getByName((String)string6.toUpperCase());
                    if (enchantment == null) continue;
                    itemStack.addUnsafeEnchantment(enchantment, n3);
                }
            }
            return itemStack;
        }
        catch (Exception exception) {
            this.C.getLogger().warning("[CustomItems] B\u0142\u0105d przy \u0142adowaniu przedmiotu " + string + ": " + exception.getMessage());
            return null;
        }
    }

    public boolean L(String string) {
        if (this.A.contains(string)) {
            return false;
        }
        this.A.set(string + ".material", (Object)"STONE");
        this.A.set(string + ".name", (Object)("&6Przedmiot (" + string + ")"));
        ArrayList arrayList = new ArrayList();
        arrayList.add((Object)"&7");
        arrayList.add((Object)"&8 \u00bb &7Jest to domy\u015blny lore twojego");
        arrayList.add((Object)"&8 \u00bb &7przedmiotu, zmie\u0144 go w &fkreatorze&7!");
        arrayList.add((Object)"&7");
        this.A.set(string + ".lore", (Object)arrayList);
        this.A.set(string + ".customModelData", (Object)0);
        this.A.set(string + ".unbreakable", (Object)false);
        this.A.set(string + ".flags", (Object)new ArrayList());
        this.A.set(string + ".enchantments", (Object)new ArrayList());
        this.A.set(string + ".cooldown", (Object)0);
        this.A.set(string + ".cooldownColor", (Object)"&#FFFFFF");
        this.A.set(string + ".chance", (Object)100);
        this.A.set(string + ".disabled", (Object)false);
        this.A.set(string + ".messages.attacker.title", (Object)"");
        this.A.set(string + ".messages.attacker.subtitle", (Object)"");
        this.A.set(string + ".messages.attacker.chat", (Object)"");
        this.A.set(string + ".messages.victim.title", (Object)"");
        this.A.set(string + ".messages.victim.subtitle", (Object)"");
        this.A.set(string + ".messages.victim.chat", (Object)"");
        this.A.set(string + ".activationType", (Object)"ATTACK");
        this.A();
        this.D();
        return true;
    }

    public boolean K(String string) {
        if (!this.A.contains(string)) {
            return false;
        }
        this.A.set(string, null);
        this.A();
        this.B.remove((Object)string);
        return true;
    }

    public void A(String string, String string2) {
        this.A.set(string + ".name", (Object)string2);
        this.A();
        this.H(string);
    }

    public void A(String string, Material material) {
        this.A.set(string + ".material", (Object)material.name());
        this.A();
        this.H(string);
    }

    public void C(String string, List<String> list) {
        this.A.set(string + ".lore", list);
        this.A();
        this.H(string);
    }

    public void C(String string, int n2) {
        this.A.set(string + ".customModelData", (Object)n2);
        this.A();
        this.H(string);
    }

    public void A(String string, boolean bl) {
        this.A.set(string + ".unbreakable", (Object)bl);
        this.A();
        this.H(string);
    }

    public void A(String string, Set<ItemFlag> set) {
        ArrayList arrayList = new ArrayList();
        for (ItemFlag itemFlag : set) {
            arrayList.add((Object)itemFlag.name());
        }
        this.A.set(string + ".flags", (Object)arrayList);
        this.A();
        this.H(string);
    }

    public void C(String string, Map<Enchantment, Integer> map) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : map.entrySet()) {
            HashMap hashMap = new HashMap();
            hashMap.put((Object)((Enchantment)entry.getKey()).getName(), (Object)((Integer)entry.getValue()));
            arrayList.add((Object)hashMap);
        }
        this.A.set(string + ".enchantments", (Object)arrayList);
        this.A();
        this.H(string);
    }

    public void B(String string, int n2) {
        this.A.set(string + ".cooldown", (Object)n2);
        this.A();
        this.H(string);
    }

    public void D(String string, String string2) {
        this.A.set(string + ".cooldownColor", (Object)string2);
        this.A();
        this.H(string);
    }

    public void B(String string, boolean bl) {
        this.A.set(string + ".disabled", (Object)bl);
        this.A();
        this.H(string);
    }

    public int J(String string) {
        return this.A.getInt(string + ".cooldown", 0);
    }

    public String D(String string) {
        return this.A.getString(string + ".cooldownColor", "&#FFFFFF");
    }

    public boolean B(String string) {
        return this.A.getBoolean(string + ".disabled", false);
    }

    public Material A(String string) {
        String string2 = this.A.getString(string + ".material", "STONE");
        Material material = Material.matchMaterial((String)string2);
        return material != null ? material : Material.STONE;
    }

    public int C(String string) {
        return this.A.getInt(string + ".chance", 100);
    }

    public void A(String string, int n2) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 100) {
            n2 = 100;
        }
        this.A.set(string + ".chance", (Object)n2);
        this.A();
        this.H(string);
    }

    public String C(String string, String string2) {
        return this.A.getString(string + ".messages.attacker." + string2, "");
    }

    public String E(String string, String string2) {
        return this.A.getString(string + ".messages.victim." + string2, "");
    }

    public void B(String string, String string2, String string3) {
        this.A.set(string + ".messages.attacker." + string2, (Object)string3);
        this.A();
    }

    public void A(String string, String string2, String string3) {
        this.A.set(string + ".messages.victim." + string2, (Object)string3);
        this.A();
    }

    public String E(String string) {
        return this.A.getString(string + ".activationType", "ATTACK");
    }

    public void B(String string, String string2) {
        this.A.set(string + ".activationType", (Object)string2);
        this.A();
    }

    private void H(String string) {
        ItemStack itemStack;
        ConfigurationSection configurationSection = this.A.getConfigurationSection(string);
        if (configurationSection != null && (itemStack = this.A(string, configurationSection)) != null) {
            this.B.put((Object)string, (Object)itemStack);
        }
    }

    private void A() {
        try {
            this.A.save(this.D);
        }
        catch (IOException iOException) {
            this.C.getLogger().warning("[CustomItems] Nie uda\u0142o si\u0119 zapisa\u0107 pliku custom_items.yml: " + iOException.getMessage());
        }
    }

    public Map<String, ItemStack> B() {
        return new LinkedHashMap(this.B);
    }

    public ItemStack G(String string) {
        return (ItemStack)this.B.get((Object)string);
    }

    public boolean I(String string) {
        return this.A.contains(string);
    }

    public int E() {
        return this.B.size();
    }

    public void C() {
        this.D();
    }

    public List<Map<String, Object>> M(String string) {
        List list = this.A.getList(string + ".abilities");
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (Object object : list) {
            if (!(object instanceof Map)) continue;
            arrayList.add((Object)((Map)object));
        }
        return arrayList;
    }

    public List<Map<String, Object>> F(String string) {
        List list = this.A.getList(string + ".visualEffects");
        if (list == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (Object object : list) {
            if (!(object instanceof Map)) continue;
            arrayList.add((Object)((Map)object));
        }
        return arrayList;
    }

    public void B(String string, List<Map<String, Object>> list) {
        this.A.set(string + ".abilities", list);
        this.A();
        this.H(string);
    }

    public void A(String string, List<Map<String, Object>> list) {
        this.A.set(string + ".visualEffects", list);
        this.A();
        this.H(string);
    }

    public boolean B(String string, Map<String, Object> map) {
        List<Map<String, Object>> list = this.M(string);
        if (list.size() >= 14) {
            return false;
        }
        list.add(map);
        this.B(string, list);
        return true;
    }

    public boolean E(String string, int n2) {
        List<Map<String, Object>> list = this.M(string);
        if (n2 < 0 || n2 >= list.size()) {
            return false;
        }
        list.remove(n2);
        this.B(string, list);
        return true;
    }

    public boolean A(String string, Map<String, Object> map) {
        List<Map<String, Object>> list = this.F(string);
        if (list.size() >= 14) {
            return false;
        }
        list.add(map);
        this.A(string, list);
        return true;
    }

    public boolean D(String string, int n2) {
        List<Map<String, Object>> list = this.F(string);
        if (n2 < 0 || n2 >= list.size()) {
            return false;
        }
        list.remove(n2);
        this.A(string, list);
        return true;
    }

    public void B(String string, int n2, String string2, Object object) {
        List<Map<String, Object>> list = this.M(string);
        if (n2 >= 0 && n2 < list.size()) {
            ((Map)list.get(n2)).put((Object)string2, object);
            this.B(string, list);
        }
    }

    public void A(String string, int n2, String string2, Object object) {
        List<Map<String, Object>> list = this.F(string);
        if (n2 >= 0 && n2 < list.size()) {
            ((Map)list.get(n2)).put((Object)string2, object);
            this.A(string, list);
        }
    }
}

