/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;
import me.anarchiacore.customitems.stormitemy.books.D;
import me.anarchiacore.customitems.stormitemy.books.enchantments.K;

public class J
extends D
implements Listener {
    public J(Main main) {
        super(main, "Unik I", "unik", (List<String>)Arrays.asList((Object[])new String[]{"helmet", "chestplate", "leggings", "boots"}), 0.05, 30, (List<String>)Arrays.asList((Object[])new String[]{"&7Unik I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &azbroj\u0119&7!", "&7Masz szans\u0119 na unikni\u0119cie ataku.", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        return new ArrayList();
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"enabled", (Object)true);
        hashMap.put((Object)"sound", (Object)"entity.enderman.teleport");
        hashMap.put((Object)"volume", (Object)0.7);
        hashMap.put((Object)"pitch", (Object)1.2);
        return hashMap;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)11);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (!fileConfiguration.isSet("messages.title") || fileConfiguration.getString("messages.title", "").contains((CharSequence)"TRUCIZNY")) {
            fileConfiguration.set("messages.attacker.title", (Object)"&bKSI\u0118GA UNIKU");
            fileConfiguration.set("messages.attacker.subtitle", (Object)"&7Gracz &f{TARGET} &7unikn\u0105\u0142 &7Twojego ataku!");
            fileConfiguration.set("messages.attacker.chat", (Object)"");
            fileConfiguration.set("messages.target.title", (Object)"&bKSI\u0118GA UNIKU");
            fileConfiguration.set("messages.target.subtitle", (Object)"&7Uda\u0142o Ci si\u0119 unikn\u0105\u0107 &7ataku gracza &f{PLAYER}&7!");
            fileConfiguration.set("messages.target.chat", (Object)"");
            try {
                this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!fileConfiguration.isSet("dodge.chance")) {
            fileConfiguration.set("dodge.chance", (Object)15);
            try {
                this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return fileConfiguration;
    }

    @Override
    public void applyEffectToItem(ItemStack itemStack) {
        super.applyEffectToItem(itemStack);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerDamaged(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getEntity();
        Entity entity = entityDamageByEntityEvent.getDamager();
        ItemStack itemStack = player.getInventory().getLeggings();
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return;
        }
        NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
            return;
        }
        UUID uUID = player.getUniqueId();
        A a2 = this.plugin.getEnchantedBooksManager();
        if (a2.B(uUID, this.getIdentifier())) {
            return;
        }
        double d2 = this.getConfigChance();
        double d3 = Math.random() * 100.0;
        if (d3 <= d2) {
            entityDamageByEntityEvent.setCancelled(true);
            a2.A(uUID, this.getIdentifier(), this.getCooldownSeconds());
            this.I(player);
            this.J(player);
        }
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private void I(Player player) {
        ConfigurationSection configurationSection = this.getSoundConfig();
        if (configurationSection != null && configurationSection.getBoolean("enabled", true)) {
            String string = configurationSection.getString("sound", "entity.enderman.teleport");
            float f2 = (float)configurationSection.getDouble("volume", 0.7);
            float f3 = (float)configurationSection.getDouble("pitch", 1.2);
            try {
                Sound sound = Sound.valueOf((String)string.toUpperCase().replace((CharSequence)".", (CharSequence)"_"));
                player.playSound(player.getLocation(), sound, f2, f3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                player.playSound(player.getLocation(), string, f2, f3);
            }
        }
    }

    private void J(Player player) {
        String string = this.config.getString("messages.title", "&b&lUNIK");
        String string2 = this.config.getString("messages.subtitle", "&7Uda\u0142o Ci si\u0119 &funikn\u0105\u0107 &7uderzenia!");
        String string3 = this.config.getString("messages.chat", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 60, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }

    @Override
    public boolean isIncompatibleWith(D d2) {
        return d2 instanceof K;
    }
}

