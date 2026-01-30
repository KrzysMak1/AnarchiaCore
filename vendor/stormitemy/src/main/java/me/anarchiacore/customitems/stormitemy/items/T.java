/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class T {
    private final Plugin B;
    private final ConfigurationSection A;

    public T(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "anarchicznaklata.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Anarchiczna klata' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Anarchiczna klata'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("anarchicznaklata.material", (Object)"NETHERITE_CHESTPLATE");
                yamlConfiguration.set("anarchicznaklata.name", (Object)"&4Anarchiczna klata");
                yamlConfiguration.set("anarchicznaklata.lore", (Object)new ArrayList());
                yamlConfiguration.set("anarchicznaklata.customModelData", (Object)0);
                yamlConfiguration.set("anarchicznaklata.enchantments", (Object)List.of((Object)"PROTECTION_ENVIRONMENTAL:5", (Object)"DURABILITY:5"));
                yamlConfiguration.set("anarchicznaklata.flags", (Object)List.of());
                yamlConfiguration.set("anarchicznaklata.unbreakable", (Object)false);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("anarchicznaklata");
    }

    public ItemStack A() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "NETHERITE_CHESTPLATE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.NETHERITE_CHESTPLATE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&4Anarchiczna klata")));
        if (this.A.contains("lore")) {
            List list = this.A.getStringList("lore");
            Object object = new ArrayList();
            for (String string : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            }
            itemMeta.setLore((List)object);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            for (Object object : this.A.getStringList("enchantments")) {
                Enchantment enchantment;
                String string;
                String[] stringArray = object.split(":");
                if (stringArray.length < 2) continue;
                string = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.A.contains("flags")) {
            for (Object object : this.A.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

