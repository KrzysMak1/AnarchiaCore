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

public class H
extends D
implements Listener {
    public H(Main main) {
        super(main, "Obra\u017cenia krytyczne I", "obrazenia_krytyczne", (List<String>)Arrays.asList((Object[])new String[]{"sword"}), 0.1, 20, (List<String>)Arrays.asList((Object[])new String[]{"&7Obra\u017cenia krytyczne I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &amiecz&7!", "&7Zwi\u0119ksza krytyczne obra\u017cenia.", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        return null;
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"enabled", (Object)true);
        hashMap.put((Object)"sound", (Object)"entity.player.attack.crit");
        hashMap.put((Object)"volume", (Object)1.0);
        hashMap.put((Object)"pitch", (Object)0.8);
        return hashMap;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)12);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (fileConfiguration.getString("messages.attacker.title", "").contains((CharSequence)"TRUCIZNY")) {
            fileConfiguration.set("messages.attacker.title", (Object)"&c&lZAKL\u0118CIE KRYTYCZNYCH OBRA\u017bE\u0143");
            fileConfiguration.set("messages.attacker.subtitle", (Object)"&7Zada\u0142e\u015b &fkrytyczne &7obra\u017cenia!");
            fileConfiguration.set("messages.attacker.chat", (Object)"");
            fileConfiguration.set("messages.target.title", (Object)"&c&lZAKL\u0118CIE KRYTYCZNYCH OBRA\u017bE\u0143");
            fileConfiguration.set("messages.target.subtitle", (Object)"&7Otrzyma\u0142e\u015b &fkrytyczne &7obra\u017cenia!");
            fileConfiguration.set("messages.target.chat", (Object)"");
            try {
                this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (!fileConfiguration.isSet("critical_hit")) {
            fileConfiguration.set("critical_hit.chance", (Object)15);
            fileConfiguration.set("critical_hit.damage_multiplier", (Object)1.5);
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

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerAttack(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        Entity entity = entityDamageByEntityEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
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
            double d4 = this.config.getDouble("critical.damage_multiplier", 2.0);
            double d5 = entityDamageByEntityEvent.getDamage();
            double d6 = d5 * d4;
            entityDamageByEntityEvent.setDamage(d6);
            a2.A(uUID, this.getIdentifier(), this.getCooldownSeconds());
            this.G(player);
            this.A(player, d5, d6);
        }
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private void G(Player player) {
        ConfigurationSection configurationSection = this.getSoundConfig();
        if (configurationSection != null && configurationSection.getBoolean("enabled", true)) {
            String string = configurationSection.getString("sound", "entity.player.attack.crit");
            float f2 = (float)configurationSection.getDouble("volume", 1.0);
            float f3 = (float)configurationSection.getDouble("pitch", 0.8);
            try {
                Sound sound = Sound.valueOf((String)string.toUpperCase().replace((CharSequence)".", (CharSequence)"_"));
                player.playSound(player.getLocation(), sound, f2, f3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                player.playSound(player.getLocation(), string, f2, f3);
            }
        }
    }

    private void A(Player player, double d2, double d3) {
        String string = this.config.getString("messages.title", "&cZAKL\u0118CIE KRYTYCZNE");
        String string2 = this.config.getString("messages.subtitle", "&7Zada\u0142e\u015b &cKRYTYCZNE &7obra\u017cenia!");
        String string3 = this.config.getString("messages.chat", "");
        string2 = string2.replace((CharSequence)"{DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d3})).replace((CharSequence)"{ORIGINAL_DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2})).replace((CharSequence)"{MULTIPLIER}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{this.config.getDouble("critical.damage_multiplier", 2.0)}));
        string3 = string3.replace((CharSequence)"{DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d3})).replace((CharSequence)"{ORIGINAL_DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2})).replace((CharSequence)"{MULTIPLIER}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{this.config.getDouble("critical.damage_multiplier", 2.0)}));
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 60, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
    }
}

