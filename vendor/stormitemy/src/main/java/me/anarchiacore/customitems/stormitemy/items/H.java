/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerItemConsumeEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class H
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public H(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "wampirzejablko.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Wampirze jab\u0142ko' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Wampirze jab\u0142ko'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("wampirzejablko.material", (Object)"ENCHANTED_GOLDEN_APPLE");
                yamlConfiguration.set("wampirzejablko.name", (Object)"&c&lWampirze jab\u0142ko");
                yamlConfiguration.set("wampirzejablko.lore", (Object)List.of((Object)"", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu halloween (2024)", (Object)"", (Object)"&8 \u00bb &7Po zjedzeniu otrzymujesz na", (Object)"&8 \u00bb &7kr\u00f3tki czas efekt &csi\u0142y II&7!", (Object)""));
                yamlConfiguration.set("wampirzejablko.customModelData", (Object)5324);
                yamlConfiguration.set("wampirzejablko.cooldown", (Object)15);
                yamlConfiguration.set("wampirzejablko.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("wampirzejablko.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("wampirzejablko.unbreakable", (Object)true);
                ArrayList arrayList = new ArrayList();
                arrayList.add((Object)Map.of((Object)"type", (Object)"REGENERATION", (Object)"duration", (Object)600, (Object)"amplifier", (Object)1));
                arrayList.add((Object)Map.of((Object)"type", (Object)"FIRE_RESISTANCE", (Object)"duration", (Object)6000, (Object)"amplifier", (Object)0));
                arrayList.add((Object)Map.of((Object)"type", (Object)"DAMAGE_RESISTANCE", (Object)"duration", (Object)6000, (Object)"amplifier", (Object)0));
                arrayList.add((Object)Map.of((Object)"type", (Object)"ABSORPTION", (Object)"duration", (Object)2400, (Object)"amplifier", (Object)3));
                arrayList.add((Object)Map.of((Object)"type", (Object)"INCREASE_DAMAGE", (Object)"duration", (Object)60, (Object)"amplifier", (Object)1));
                yamlConfiguration.set("wampirzejablko.effects", (Object)arrayList);
                yamlConfiguration.set("wampirzejablko.messages.consumer.title", (Object)"&c&lWAMPIRZE JAB\u0141KO");
                yamlConfiguration.set("wampirzejablko.messages.consumer.subtitle", (Object)"&7Dosta\u0142e\u015b efekt &fsi\u0142y 2&7!");
                yamlConfiguration.set("wampirzejablko.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("wampirzejablko.messages.cooldown", (Object)"");
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("wampirzejablko");
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onPlayerConsumeWampirzeJablko(PlayerItemConsumeEvent playerItemConsumeEvent) {
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("wampirzejablko")) {
            return;
        }
        Player player = playerItemConsumeEvent.getPlayer();
        ItemStack itemStack = playerItemConsumeEvent.getItem();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            String string = ChatColor.stripColor((String)itemStack.getItemMeta().getDisplayName());
            int n2 = itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : -1;
            int n3 = this.A.getInt("customModelData", -1);
            String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&c&lWampirze jab\u0142ko")));
            if (string.equals((Object)string2) && n2 == n3) {
                Object object;
                String string3;
                Object object22;
                Material material = itemStack.getType();
                if (player.hasCooldown(material)) {
                    long l2 = player.getCooldown(material) / 20;
                    String string4 = this.A.getString("messages.cooldown", "");
                    if (!string4.isEmpty()) {
                        string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                        player.sendMessage(string4);
                    }
                    playerItemConsumeEvent.setCancelled(true);
                    return;
                }
                int n4 = this.A.getInt("cooldown", 5) * 20;
                player.setCooldown(material, n4);
                List list = this.A.getMapList("effects");
                for (Object object22 : list) {
                    string3 = object22.get((Object)"type").toString();
                    object = PotionEffectType.getByName((String)string3);
                    int n5 = Integer.parseInt((String)object22.get((Object)"duration").toString());
                    int n6 = Integer.parseInt((String)object22.get((Object)"amplifier").toString());
                    if (object == null) continue;
                    player.addPotionEffect(new PotionEffect(object, n5, n6));
                }
                player.playSound(player.getLocation(), "minecraft:entity.wither.shoot", 1.0f, 1.0f);
                Iterator iterator = this.A.getConfigurationSection("messages.consumer");
                object22 = iterator.getString("title", "");
                string3 = iterator.getString("subtitle", "");
                if (!object22.isEmpty() || !string3.isEmpty()) {
                    player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object22), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 70, 20);
                }
                if (!(object = iterator.getString("chatMessage", "")).isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
                }
            }
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "ENCHANTED_GOLDEN_APPLE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.ENCHANTED_GOLDEN_APPLE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&c&lWampirze jab\u0142ko"));
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
}

