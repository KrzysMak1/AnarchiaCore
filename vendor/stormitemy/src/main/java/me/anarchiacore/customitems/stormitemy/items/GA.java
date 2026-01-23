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
 *  java.util.Random
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class GA
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public GA(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "piekielnatarcza.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Piekielna tarcza' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Piekielna tarcza'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}    - pozosta\u0142y czas cooldownu (w sekundach)\n  {ATTACKER} - nazwa gracza, kt\u00f3ry zaatakowa\u0142\n  {TARGET}  - nazwa gracza posiadaj\u0105cego tarcz\u0119\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("piekielnatarcza.material", (Object)"SHIELD");
                yamlConfiguration.set("piekielnatarcza.name", (Object)"&6Piekielna tarcza");
                yamlConfiguration.set("piekielnatarcza.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Posiada &f25% szansy &7na odbicie ", (Object)"&8 \u00bb &cataku &7wroga!", (Object)"&r"));
                yamlConfiguration.set("piekielnatarcza.customModelData", (Object)0);
                yamlConfiguration.set("piekielnatarcza.cooldown", (Object)5);
                yamlConfiguration.set("piekielnatarcza.reflectChance", (Object)25);
                yamlConfiguration.set("piekielnatarcza.enchantments", (Object)List.of());
                yamlConfiguration.set("piekielnatarcza.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("piekielnatarcza.unbreakable", (Object)false);
                yamlConfiguration.set("piekielnatarcza.messages.defenderTitle", (Object)"&6PIEKIELNA TARCZA");
                yamlConfiguration.set("piekielnatarcza.messages.defenderSubtitle", (Object)"&7Odbi\u0142e\u015b atak od &f{ATTACKER}&7!");
                yamlConfiguration.set("piekielnatarcza.messages.defender", (Object)"");
                yamlConfiguration.set("piekielnatarcza.messages.attackerTitle", (Object)"&6PIEKIELNA TARCZA");
                yamlConfiguration.set("piekielnatarcza.messages.attackerSubtitle", (Object)"&7Tw\u00f3j atak zosta\u0142 odbity przez &f{TARGET}&7!");
                yamlConfiguration.set("piekielnatarcza.messages.attacker", (Object)"");
                yamlConfiguration.set("piekielnatarcza.messages.cooldown", (Object)"");
                yamlConfiguration.set("piekielnatarcza.sounds.enabled", (Object)false);
                yamlConfiguration.set("piekielnatarcza.sounds.activation.sound", (Object)"");
                yamlConfiguration.set("piekielnatarcza.sounds.activation.volume", (Object)1.0);
                yamlConfiguration.set("piekielnatarcza.sounds.activation.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("piekielnatarcza");
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
            this.B.getLogger().warning("B\u0142\u0105d podczas odtwarzania d\u017awi\u0119ku dla PiekielnaTarcza: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        ItemMeta itemMeta;
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("piekielnatarcza")) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInOffHand();
        boolean bl = true;
        if (itemStack == null || itemStack.getType() != Material.SHIELD) {
            itemStack = player.getInventory().getItemInMainHand();
            bl = false;
            if (itemStack == null || itemStack.getType() != Material.SHIELD) {
                return;
            }
        }
        if ((itemMeta = itemStack.getItemMeta()) == null || !itemMeta.hasDisplayName()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6Piekielna tarcza"));
        if (!itemMeta.getDisplayName().equals((Object)string)) {
            return;
        }
        if (player.getCooldown(Material.SHIELD) > 0) {
            String string2 = this.A.getString("messages.cooldown", "");
            if (!string2.isEmpty()) {
                string2 = string2.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)(player.getCooldown(Material.SHIELD) / 20)));
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player2 = (Player)entityDamageByEntityEvent.getDamager();
        boolean bl2 = false;
        boolean bl3 = false;
        try {
            if (this.B instanceof Main) {
                Main main = (Main)this.B;
                if (main.isPlayerInBlockedRegion(player)) {
                    bl2 = true;
                }
                if (main.isPlayerInBlockedRegion(player2)) {
                    bl3 = true;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl2 || bl3) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
            return;
        }
        int n2 = this.A.getInt("reflectChance", 25);
        Random random = new Random();
        if (random.nextInt(100) < n2) {
            String string3;
            entityDamageByEntityEvent.setCancelled(true);
            this.A(player);
            double d2 = entityDamageByEntityEvent.getDamage();
            player2.damage(d2, (Entity)player);
            String string4 = this.A.getString("messages.defender", "");
            if (!string4.isEmpty()) {
                string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2));
                player.sendMessage(string4);
            }
            if (!(string3 = this.A.getString("messages.attacker", "")).isEmpty()) {
                string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2));
                player2.sendMessage(string3);
            }
            String string5 = this.A.getString("messages.defenderTitle", "");
            String string6 = this.A.getString("messages.defenderSubtitle", "");
            if (!string5.isEmpty() || !string6.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2)), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2)), 10, 70, 20);
            }
            String string7 = this.A.getString("messages.attackerTitle", "");
            String string8 = this.A.getString("messages.attackerSubtitle", "");
            if (!string7.isEmpty() || !string8.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string7).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2)), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string8).replace((CharSequence)"{ATTACKER}", (CharSequence)player2.getName()).replace((CharSequence)"{TARGET}", (CharSequence)player.getName()).replace((CharSequence)"{CHANCE}", (CharSequence)String.valueOf((int)n2)), 10, 70, 20);
            }
            player.setCooldown(Material.SHIELD, this.A.getInt("cooldown", 5) * 20);
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "SHIELD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.SHIELD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&6Piekielna tarcza"));
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

