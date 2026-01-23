/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class b
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;
    private final Map<UUID, Long> C = new HashMap();

    public b(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "antycobweb.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Anty Cobweb' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Anty Cobweb'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("antycobweb.material", (Object)"FIREWORK_STAR");
                yamlConfiguration.set("antycobweb.name", (Object)"&dAnty cobweb");
                yamlConfiguration.set("antycobweb.lore", (Object)List.of((Object)"&7", (Object)"&f Usuwa paj\u0119czyny &e3 kratki &fod postaci.", (Object)"&7"));
                yamlConfiguration.set("antycobweb.customModelData", (Object)0);
                yamlConfiguration.set("antycobweb.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("antycobweb.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("antycobweb.range", (Object)3);
                yamlConfiguration.set("antycobweb.cooldown", (Object)20);
                yamlConfiguration.set("antycobweb.messages.user.title", (Object)"&d&lANTY COBWEB");
                yamlConfiguration.set("antycobweb.messages.user.subtitle", (Object)"&7Paj\u0119czyny zosta\u0142y &fusuni\u0119te&7!");
                yamlConfiguration.set("antycobweb.messages.user.chatMessage", (Object)"");
                yamlConfiguration.set("antycobweb.messages.cooldown", (Object)"");
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("antycobweb");
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onPlayerUseAntyCobweb(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }
        String string = ChatColor.stripColor((String)itemStack.getItemMeta().getDisplayName());
        int n2 = itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : -1;
        int n3 = this.A.getInt("customModelData", -1);
        String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&dAnty cobweb")));
        if (string.equals((Object)string2) && (n2 == n3 || n3 == 0)) {
            ItemStack itemStack2;
            String string3;
            Object object;
            if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("antycobweb")) {
                return;
            }
            playerInteractEvent.setCancelled(true);
            if (this.B instanceof Main) {
                object = (Main)this.B;
                try {
                    if (((Main)((Object)object)).isPlayerInBlockedRegion(player)) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (player.hasCooldown((Material)(object = itemStack.getType()))) {
                long l2 = player.getCooldown((Material)object) / 20;
                String string4 = this.A.getString("messages.cooldown", "");
                if (!string4.isEmpty()) {
                    string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                    player.sendMessage(string4);
                }
                return;
            }
            int n4 = this.A.getInt("cooldown", 20);
            if (this.B instanceof Main) {
                me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(this.B, player, itemStack, n4, "antycobweb");
            } else {
                player.setCooldown((Material)object, n4 * 20);
            }
            int n5 = this.A.getInt("range", 3);
            int n6 = this.A(player, n5);
            player.playSound(player.getLocation(), "minecraft:block.wool.break", 1.0f, 1.0f);
            ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.user");
            String string5 = configurationSection.getString("title", "");
            String string6 = configurationSection.getString("subtitle", "");
            if (!string5.isEmpty() || !string6.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6), 10, 70, 20);
            }
            if ((string3 = configurationSection.getString("chatMessage", "")) != null && !string3.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
            if ((itemStack2 = player.getInventory().getItemInMainHand()).getAmount() > 1) {
                itemStack2.setAmount(itemStack2.getAmount() - 1);
                player.getInventory().setItemInMainHand(itemStack2);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }
    }

    private int A(Player player, int n2) {
        int n3 = 0;
        int n4 = player.getLocation().getBlockX();
        int n5 = player.getLocation().getBlockY();
        int n6 = player.getLocation().getBlockZ();
        int n7 = n2 / 2;
        for (int i2 = -n7; i2 <= n7; ++i2) {
            for (int i3 = -n7; i3 <= n7; ++i3) {
                for (int i4 = -n7; i4 <= n7; ++i4) {
                    Block block = player.getWorld().getBlockAt(n4 + i2, n5 + i3, n6 + i4);
                    if (block.getType() != Material.COBWEB) continue;
                    block.setType(Material.AIR);
                    ++n3;
                }
            }
        }
        return n3;
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "FIREWORK_STAR"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.FIREWORK_STAR;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&dAnty cobweb"));
        itemMeta.setDisplayName(string);
        if (this.A.contains("lore")) {
            List list = this.A.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        if (this.A.contains("customModelData") && this.A.getInt("customModelData") != 0) {
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
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

