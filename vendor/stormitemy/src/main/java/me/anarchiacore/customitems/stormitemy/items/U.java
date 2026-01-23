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
 *  org.bukkit.potion.PotionEffect
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
import org.bukkit.potion.PotionEffect;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class u
implements Listener {
    private final Plugin B;
    private File C;
    private YamlConfiguration A;

    public u(Plugin plugin) {
        this.B = plugin;
        File file = new File(plugin.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.C = new File(file, "cieplemleko.yml");
        if (!this.C.exists()) {
            try {
                this.C.createNewFile();
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Ciep\u0142e mleko' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Ciep\u0142e mleko'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("cieplemleko.material", (Object)"BOWL");
                yamlConfiguration.set("cieplemleko.name", (Object)"&9&lCiep\u0142e mleko");
                yamlConfiguration.set("cieplemleko.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu \u015bwi\u0105tecznego (2024)", (Object)"&r", (Object)"&8 \u00bb &7Po wypiciu usuwa wszystkie", (Object)"&8 \u00bb &cnegatywne efekty&7! R\u00f3wnie\u017c posiada", (Object)"&8 \u00bb &7mechanike stakowania!", (Object)"&r"));
                yamlConfiguration.set("cieplemleko.customModelData", (Object)43634712);
                yamlConfiguration.set("cieplemleko.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("cieplemleko.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("cieplemleko.unbreakable", (Object)false);
                yamlConfiguration.set("cieplemleko.cooldown", (Object)40);
                yamlConfiguration.set("cieplemleko.negativeEffects", (Object)List.of((Object[])new String[]{"BLINDNESS", "NAUSEA", "POISON", "SLOWNESS", "MINING_FATIGUE", "WEAKNESS", "WITHER", "HUNGER", "BAD_OMEN", "UNLUCK", "DARKNESS", "LEVITATION"}));
                yamlConfiguration.set("cieplemleko.messages.consumer.title", (Object)"&9&lCIEP\u0141E MLEKO");
                yamlConfiguration.set("cieplemleko.messages.consumer.subtitle", (Object)"&7Twoje negatywne efekty &fznikn\u0119\u0142y&7!");
                yamlConfiguration.set("cieplemleko.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("cieplemleko.messages.cooldown", (Object)"");
                yamlConfiguration.set("cieplemleko.sounds.enabled", (Object)false);
                yamlConfiguration.set("cieplemleko.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("cieplemleko.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("cieplemleko.sounds.activation.pitch", (Object)1.0);
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
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("cieplemleko")) {
            return;
        }
        ConfigurationSection configurationSection = this.A.getConfigurationSection("cieplemleko");
        if (configurationSection == null) {
            return;
        }
        Material material = itemStack.getType();
        int n2 = configurationSection.getInt("cooldown", 30) * 20;
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
        this.A(player);
        this.A(player, configurationSection);
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setCooldown(material, n2);
        this.B(player);
    }

    private void A(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("cieplemleko");
        if (configurationSection == null) {
            return;
        }
        List list = configurationSection.getStringList("negativeEffects");
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (!list.contains((Object)potionEffect.getType().getName().toUpperCase())) continue;
            player.removePotionEffect(potionEffect.getType());
        }
    }

    private void A(Player player, ConfigurationSection configurationSection) {
        try {
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
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla CiepleMleko: " + exception.getMessage());
        }
    }

    private void B(Player player) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("cieplemleko.messages.consumer");
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
        if (itemStack.getType() != Material.BOWL) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return false;
        }
        String string = ChatColor.stripColor((String)itemMeta.getDisplayName());
        ConfigurationSection configurationSection = this.A.getConfigurationSection("cieplemleko");
        if (configurationSection == null) {
            return false;
        }
        String string2 = ChatColor.stripColor((String)me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("name", "&9&lCiep\u0142e mleko")));
        return string.equals((Object)string2);
    }

    public ItemStack getItem() {
        Material material;
        ConfigurationSection configurationSection = this.A.getConfigurationSection("cieplemleko");
        if (configurationSection == null) {
            return new ItemStack(Material.BOWL);
        }
        try {
            material = Material.valueOf((String)configurationSection.getString("material", "BOWL"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BOWL;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return itemStack;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("name", "&9&lCiep\u0142e mleko"));
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

