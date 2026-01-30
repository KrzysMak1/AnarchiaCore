/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Field
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class P
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public P(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "rozakupidyna.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'R\u00f3\u017ca kupidyna' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'R\u00f3\u017ca kupidyna'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("rozaKupidyna.material", (Object)"POPPY");
                yamlConfiguration.set("rozaKupidyna.name", (Object)"&c&lR\u00f3\u017ca kupidyna");
                ArrayList arrayList = new ArrayList();
                arrayList.add((Object)"&8 \u00bb &7Przedmiot zdobyty w &fwalentynki w 2024 roku&7!");
                arrayList.add((Object)"&8 \u00bb &7Trzymaj go w r\u0119ce aby otrzyma\u0107:");
                arrayList.add((Object)"&8  - &fEfekt odporno\u015bci 1");
                arrayList.add((Object)"&8  - &fEfekt regeneracji 1");
                yamlConfiguration.set("rozaKupidyna.lore", (Object)arrayList);
                yamlConfiguration.set("rozaKupidyna.customModelData", (Object)1);
                yamlConfiguration.set("rozaKupidyna.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("rozaKupidyna.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("rozaKupidyna.unbreakable", (Object)true);
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add((Object)Map.of((Object)"type", (Object)"DAMAGE_RESISTANCE", (Object)"duration", (Object)40, (Object)"amplifier", (Object)0, (Object)"ambient", (Object)false, (Object)"particles", (Object)false));
                arrayList2.add((Object)Map.of((Object)"type", (Object)"REGENERATION", (Object)"duration", (Object)40, (Object)"amplifier", (Object)0, (Object)"ambient", (Object)false, (Object)"particles", (Object)false));
                yamlConfiguration.set("rozaKupidyna.effects", (Object)arrayList2);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("rozaKupidyna");
        Bukkit.getScheduler().runTaskTimer((Plugin)javaPlugin, () -> {
            if (javaPlugin instanceof Main && ((Main)javaPlugin).isItemDisabledByKey("rozakupidyna")) {
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!this.A(player)) continue;
                this.B(player);
            }
        }, 0L, 20L);
    }

    private boolean A(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack itemStack2 = player.getInventory().getItemInOffHand();
        return this.A(itemStack) || this.A(itemStack2);
    }

    private boolean A(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        String string = itemStack.getItemMeta().getDisplayName();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&c&lR\u00f3\u017ca kupidyna"));
        return string.equals((Object)string2);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        if (this.A(blockPlaceEvent.getItemInHand())) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    private void B(Player player) {
        if (this.A.contains("effects")) {
            List list = this.A.getMapList("effects");
            for (Map map : list) {
                String string;
                PotionEffectType potionEffectType;
                if (!map.containsKey((Object)"type") || (potionEffectType = PotionEffectType.getByName((String)(string = map.get((Object)"type").toString()))) == null) continue;
                int n2 = 40;
                int n3 = 0;
                boolean bl = false;
                boolean bl2 = false;
                try {
                    if (map.containsKey((Object)"duration")) {
                        n2 = Integer.parseInt((String)map.get((Object)"duration").toString());
                    }
                    if (map.containsKey((Object)"amplifier")) {
                        n3 = Integer.parseInt((String)map.get((Object)"amplifier").toString());
                    }
                    if (map.containsKey((Object)"ambient")) {
                        bl = Boolean.parseBoolean((String)map.get((Object)"ambient").toString());
                    }
                    if (map.containsKey((Object)"particles")) {
                        bl2 = Boolean.parseBoolean((String)map.get((Object)"particles").toString());
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                player.addPotionEffect(new PotionEffect(potionEffectType, n2, n3, bl, bl2), true);
            }
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "POPPY"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.POPPY;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&c&lR\u00f3\u017ca kupidyna"));
        itemMeta.setDisplayName(string);
        if (this.A.contains("lore")) {
            List list = this.A.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            for (Object object : this.A.getStringList("enchantments")) {
                Enchantment enchantment;
                String string2;
                String[] stringArray = object.split(":");
                if (stringArray.length < 2) continue;
                string2 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string2)) == null) continue;
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

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.B.getDataFolder(), "items/rozakupidyna.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("rozaKupidyna")) != null) {
                Field field = this.getClass().getDeclaredField("config");
                field.setAccessible(true);
                field.set((Object)this, (Object)configurationSection);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

