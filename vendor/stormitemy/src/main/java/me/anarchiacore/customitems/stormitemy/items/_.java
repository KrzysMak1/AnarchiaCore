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
 *  java.util.List
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
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
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class _
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public _(Plugin plugin) {
        me.anarchiacore.customitems.stormitemy.config.A a2;
        this.B = plugin;
        File file = new File(plugin.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "kosa.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        String string = "=== Konfiguracja przedmiotu 'Kosa' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Kosa'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n  {TARGET}       - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
        yamlConfiguration.options().header(string);
        yamlConfiguration.options().copyHeader(true);
        yamlConfiguration.set("kosa.material", (Object)"NETHERITE_HOE");
        yamlConfiguration.set("kosa.name", (Object)"&8&lKosa");
        yamlConfiguration.set("kosa.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu halloween (2023)", (Object)"&r", (Object)"&8 \u00bb &7Twoje uderzenie mo\u017ce&f co minut\u0119", (Object)"&8 \u00bb &7przestraszy\u0107 przeciwnika nadaj\u0105c", (Object)"&8 \u00bb &7mu efekty utrudniaj\u0105ce widoczno\u015b\u0107.", (Object)"&r"));
        yamlConfiguration.set("kosa.customModelData", (Object)1);
        yamlConfiguration.set("kosa.cooldown", (Object)40);
        yamlConfiguration.set("kosa.enchantments", (Object)List.of((Object)"MENDING:1", (Object)"DURABILITY:3"));
        yamlConfiguration.set("kosa.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
        yamlConfiguration.set("kosa.unbreakable", (Object)true);
        yamlConfiguration.set("kosa.messages.attacker.title", (Object)"&8&LKOSA");
        yamlConfiguration.set("kosa.messages.attacker.subtitle", (Object)"&7O\u015blepi\u0142e\u015b gracza &f{TARGET} &7!");
        yamlConfiguration.set("kosa.messages.attacker.chatMessage", (Object)"");
        yamlConfiguration.set("kosa.messages.victim.title", (Object)"&8&LKOSA");
        yamlConfiguration.set("kosa.messages.victim.subtitle", (Object)"&7Zosta\u0142e\u015b o\u015blepiony przez &f{PLAYER}&7!");
        yamlConfiguration.set("kosa.messages.victim.chatMessage", (Object)"");
        yamlConfiguration.set("kosa.messages.cooldown", (Object)"");
        yamlConfiguration.set("kosa.sounds.enabled", (Object)false);
        yamlConfiguration.set("kosa.sounds.activation.sound", (Object)"");
        yamlConfiguration.set("kosa.sounds.activation.volume", (Object)1.0);
        yamlConfiguration.set("kosa.sounds.activation.pitch", (Object)1.0);
        if (!file2.exists()) {
            try {
                yamlConfiguration.save(file2);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        } else {
            try {
                a2 = new me.anarchiacore.customitems.stormitemy.config.A((Plugin)plugin);
                a2.A("kosa", yamlConfiguration.getConfigurationSection("kosa"));
            }
            catch (Exception exception) {
                plugin.getLogger().warning("Failed to update kosa.yml: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
        a2 = YamlConfiguration.loadConfiguration((File)file2);
        this.A = a2.getConfigurationSection("kosa");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public ItemStack getItem() {
        List list;
        Material material;
        String string = this.A.getString("material", "NETHERITE_HOE");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.DIAMOND_SWORD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&8&lKosa"));
        itemMeta.setDisplayName(string2);
        if (this.A.contains("lore")) {
            list = this.A.getStringList("lore");
            for (int i2 = 0; i2 < list.size(); ++i2) {
                list.set(i2, (Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)list.get(i2)));
            }
            itemMeta.setLore(list);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            list = this.A.getStringList("enchantments");
            for (String string3 : list) {
                Enchantment enchantment;
                String[] stringArray = string3.split(":");
                if (stringArray.length < 2) continue;
                String string4 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string4)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        if (this.A.contains("flags")) {
            list = this.A.getStringList("flags");
            for (String string3 : list) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)string3)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.A.getBoolean("unbreakable", false));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void A(Player player) {
        try {
            boolean bl = this.A.getBoolean("sounds.enabled", false);
            if (!bl) {
                return;
            }
            String string = this.A.getString("sounds.activation.sound", "");
            if (string == null || string.trim().isEmpty()) {
                return;
            }
            Sound sound = Sound.valueOf((String)string);
            float f2 = (float)this.A.getDouble("sounds.activation.volume", 1.0);
            float f3 = (float)this.A.getDouble("sounds.activation.pitch", 1.0);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (Exception exception) {
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla Kosa: " + exception.getMessage());
        }
    }

    private boolean A(ItemStack itemStack) {
        boolean bl;
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&8&lKosa"));
        try {
            material = Material.valueOf((String)this.A.getString("material", "NETHERITE_HOE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.DIAMOND_SWORD;
        }
        int n2 = this.A.getInt("customModelData", 0);
        boolean bl2 = bl = itemStack.getType() == material && itemStack.getItemMeta().getDisplayName().equals((Object)string);
        if (itemStack.getItemMeta().hasCustomModelData()) {
            bl = bl && itemStack.getItemMeta().getCustomModelData() == n2;
        }
        return bl;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Player player = (Player)entityDamageByEntityEvent.getDamager();
            Player player2 = (Player)entityDamageByEntityEvent.getEntity();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (!this.A(itemStack)) {
                return;
            }
            if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("kosa")) {
                return;
            }
            boolean bl = false;
            boolean bl2 = false;
            try {
                if (this.B instanceof Main) {
                    Main main = (Main)this.B;
                    if (main.isPlayerInBlockedRegion(player)) {
                        bl = true;
                    }
                    if (main.isPlayerInBlockedRegion(player2)) {
                        bl2 = true;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (bl || bl2) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                return;
            }
            int n2 = this.A.getInt("cooldown", 60) * 20;
            Material material = itemStack.getType();
            if (player.hasCooldown(material)) {
                long l2 = player.getCooldown(material) / 20;
                String string = this.A.getString("messages.cooldown", "");
                if (!string.isEmpty()) {
                    string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                    player.sendMessage(string);
                }
                return;
            }
            player.setCooldown(material, n2);
            this.A(player);
            player2.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
            ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.attacker");
            ConfigurationSection configurationSection2 = this.A.getConfigurationSection("messages.victim");
            String string = configurationSection.getString("title", "");
            string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string);
            String string2 = configurationSection.getString("subtitle", "");
            string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()));
            String string3 = configurationSection.getString("chatMessage", "");
            string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName()));
            String string4 = configurationSection2.getString("title", "");
            string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4);
            String string5 = configurationSection2.getString("subtitle", "");
            string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5.replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()));
            String string6 = configurationSection2.getString("chatMessage", "");
            string6 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6.replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()));
            if (!string.isEmpty() || !string2.isEmpty()) {
                player.sendTitle(string, string2, 10, 70, 20);
            }
            if (!string4.isEmpty() || !string5.isEmpty()) {
                player2.sendTitle(string4, string5, 10, 70, 20);
            }
            if (!string3.isEmpty()) {
                player.sendMessage(string3);
            }
            if (!string6.isEmpty()) {
                player2.sendMessage(string6);
            }
        }
    }
}

