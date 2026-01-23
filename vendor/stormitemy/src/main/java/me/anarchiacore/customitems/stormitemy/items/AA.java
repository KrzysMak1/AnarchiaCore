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
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
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
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class AA
implements Listener {
    private final Plugin B;
    private File C;
    private YamlConfiguration A;

    public AA(Plugin plugin) {
        this.B = plugin;
        File file = new File(plugin.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.C = new File(file, "krewwampira.yml");
        if (!this.C.exists()) {
            try {
                this.C.createNewFile();
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Krew wampira' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Krew wampira'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("krewwampira.material", (Object)"BEETROOT_SOUP");
                yamlConfiguration.set("krewwampira.name", (Object)"&c&lKrew wampira");
                yamlConfiguration.set("krewwampira.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu halloween (2023)", (Object)"&r", (Object)"&8 \u00bb &7Po klikni\u0119ciu uleczy Ci\u0119", (Object)"&8 \u00bb &7do &cpe\u0142nego HP&7!", (Object)"&r"));
                yamlConfiguration.set("krewwampira.customModelData", (Object)1);
                yamlConfiguration.set("krewwampira.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("krewwampira.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("krewwampira.unbreakable", (Object)false);
                yamlConfiguration.set("krewwampira.cooldown", (Object)80);
                yamlConfiguration.set("krewwampira.messages.consumer.title", (Object)"&c&lKREW WAMPIRA");
                yamlConfiguration.set("krewwampira.messages.consumer.subtitle", (Object)"&7Zosta\u0142e\u015b &fuleczony&7!");
                yamlConfiguration.set("krewwampira.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("krewwampira.messages.cooldown", (Object)"");
                yamlConfiguration.set("krewwampira.sounds.enabled", (Object)false);
                yamlConfiguration.set("krewwampira.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("krewwampira.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("krewwampira.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(this.C);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        this.A = YamlConfiguration.loadConfiguration((File)this.C);
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !this.A(itemStack)) {
            return;
        }
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("krewwampira")) {
            return;
        }
        ConfigurationSection configurationSection = this.A.getConfigurationSection("krewwampira");
        if (configurationSection == null) {
            return;
        }
        Material material = itemStack.getType();
        int n2 = configurationSection.getInt("cooldown", 60) * 20;
        if (player.hasCooldown(material)) {
            long l2 = player.getCooldown(material) / 20;
            String string = configurationSection.getString("messages.cooldown", "");
            if (!string.isEmpty()) {
                string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                player.sendMessage(string);
            }
            playerInteractEvent.setCancelled(true);
            return;
        }
        player.setHealth(player.getMaxHealth());
        this.A(player);
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setCooldown(material, n2);
        this.B(player);
    }

    private void A(Player player) {
        try {
            ConfigurationSection configurationSection = this.A.getConfigurationSection("krewwampira");
            if (configurationSection == null) {
                return;
            }
            boolean bl = configurationSection.getBoolean("sounds.enabled", false);
            if (!bl) {
                return;
            }
            String string = configurationSection.getString("sounds.activation.sound", "");
            if (string == null || string.trim().isEmpty()) {
                return;
            }
            Sound sound = Sound.valueOf((String)string);
            float f2 = (float)configurationSection.getDouble("sounds.activation.volume", 1.0);
            float f3 = (float)configurationSection.getDouble("sounds.activation.pitch", 1.0);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (Exception exception) {
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla KrewWampira: " + exception.getMessage());
        }
    }

    private void B(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("krewwampira.messages.consumer");
        if (configurationSection == null) {
            return;
        }
        String string = configurationSection.getString("title", "");
        String string2 = configurationSection.getString("subtitle", "");
        String string3 = configurationSection.getString("chatMessage", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    private boolean A(ItemStack itemStack) {
        if (itemStack.getType() != Material.BEETROOT_SOUP) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return false;
        }
        String string = ChatColor.stripColor((String)itemMeta.getDisplayName());
        ConfigurationSection configurationSection = this.A.getConfigurationSection("krewwampira");
        if (configurationSection == null) {
            return false;
        }
        String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("name", "&c&lKrew wampira")));
        return string.equals((Object)string2);
    }

    public ItemStack getItem() {
        Material material;
        ConfigurationSection configurationSection = this.A.getConfigurationSection("krewwampira");
        if (configurationSection == null) {
            this.B.getLogger().warning("Brak sekcji 'krewwampira' w pliku krewwampira.yml!");
            return new ItemStack(Material.BEETROOT_SOUP);
        }
        try {
            material = Material.valueOf((String)configurationSection.getString("material", "BEETROOT_SOUP"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BEETROOT_SOUP;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("name", "&c&lKrew wampira"));
        itemMeta.setDisplayName(string);
        if (configurationSection.contains("lore")) {
            List list = configurationSection.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        if (configurationSection.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)configurationSection.getInt("customModelData")));
        }
        if (configurationSection.contains("enchantments")) {
            for (Object object : configurationSection.getStringList("enchantments")) {
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
        if (configurationSection.contains("flags")) {
            for (Object object : configurationSection.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(configurationSection.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

