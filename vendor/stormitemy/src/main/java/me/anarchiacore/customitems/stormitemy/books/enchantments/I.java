/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Number
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.Override
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.books.enchantments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;
import me.anarchiacore.customitems.stormitemy.books.D;

public class I
extends D
implements Listener {
    private final Random G = new Random();

    public I(Main main) {
        super(main, "Regeneracja I", "regeneracja", (List<String>)Arrays.asList((Object[])new String[]{"chestplate"}), 0.05, 60, (List<String>)Arrays.asList((Object[])new String[]{"&7Regeneracji I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &anapier\u015bnik&7!", "&7Na\u0142o\u017cony daje szans\u0119, \u017ce po", "&7otrzymanym uderzeniu otrzymasz efekt", "&7regeneracji II na kr\u00f3tki czas.", "&7"}));
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    @Override
    protected List<Map<String, Object>> getDefaultEffects() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"type", (Object)"REGENERATION");
        hashMap.put((Object)"duration", (Object)8);
        hashMap.put((Object)"amplifier", (Object)1);
        hashMap.put((Object)"particles", (Object)true);
        hashMap.put((Object)"ambient", (Object)false);
        hashMap.put((Object)"icon", (Object)true);
        arrayList.add((Object)hashMap);
        return arrayList;
    }

    @Override
    protected Map<String, Object> getDefaultSoundConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"enabled", (Object)true);
        hashMap.put((Object)"sound", (Object)"entity.player.levelup");
        hashMap.put((Object)"volume", (Object)0.8);
        hashMap.put((Object)"pitch", (Object)1.5);
        return hashMap;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDamage(EntityDamageEvent entityDamageEvent) {
        if (!(entityDamageEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageEvent.getEntity();
        ItemStack itemStack = player.getInventory().getChestplate();
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
            if (itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
                UUID uUID = player.getUniqueId();
                A a2 = this.plugin.getEnchantedBooksManager();
                if (a2.B(uUID, this.getIdentifier())) {
                    return;
                }
                double d2 = this.getConfigChance();
                if (this.G.nextDouble() * 100.0 <= d2) {
                    this.H(player);
                    a2.A(uUID, this.getIdentifier(), this.getCooldownSeconds());
                }
            }
        }
    }

    private void H(Player player) {
        Object object;
        String string;
        Object object22;
        List<Map<?, ?>> list = this.getEffects();
        for (Object object22 : list) {
            string = String.valueOf((Object)object22.get((Object)"type"));
            object = PotionEffectType.getByName((String)string);
            if (object == null) continue;
            int n2 = this.C((Map<?, ?>)object22, "duration", 8) * 20;
            int n3 = this.C((Map<?, ?>)object22, "amplifier", 1);
            boolean bl = this.C((Map<?, ?>)object22, "particles", true);
            boolean bl2 = this.C((Map<?, ?>)object22, "ambient", false);
            boolean bl3 = this.C((Map<?, ?>)object22, "icon", true);
            player.addPotionEffect(new PotionEffect(object, n2, n3, bl2, bl, bl3));
        }
        Iterator iterator = this.getSoundConfig();
        if (iterator != null && iterator.getBoolean("enabled", true)) {
            object22 = iterator.getString("sound", "entity.player.levelup");
            float f2 = (float)iterator.getDouble("volume", 0.8);
            float f3 = (float)iterator.getDouble("pitch", 1.5);
            try {
                Sound sound = Sound.valueOf((String)object22.toUpperCase().replace((CharSequence)".", (CharSequence)"_"));
                player.playSound(player.getLocation(), sound, f2, f3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                player.playSound(player.getLocation(), (String)object22, f2, f3);
            }
        }
        object22 = this.config.getString("messages.title", "&dZAKL\u0118CIE REGENERACJI");
        string = this.config.getString("messages.subtitle", "&7Twoja zbroja aktywowa\u0142a efekt &fregenercji&7!");
        if (!object22.isEmpty() || !string.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object22), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), 10, 60, 20);
        }
        if (!(object = this.config.getString("messages.chat", "")).isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
        }
    }

    @Override
    public void applyEffectToItem(ItemStack itemStack) {
        super.applyEffectToItem(itemStack);
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
    }

    private int C(Map<?, ?> map, String string, int n2) {
        Object object = map.get((Object)string);
        if (object == null) {
            return n2;
        }
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        if (object instanceof String) {
            try {
                return Integer.parseInt((String)((String)object));
            }
            catch (NumberFormatException numberFormatException) {
                return n2;
            }
        }
        return n2;
    }

    private boolean C(Map<?, ?> map, String string, boolean bl) {
        Object object = map.get((Object)string);
        if (object == null) {
            return bl;
        }
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        if (object instanceof String) {
            return Boolean.parseBoolean((String)((String)object));
        }
        return bl;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)4);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (fileConfiguration.isSet("messages.attacker") || fileConfiguration.isSet("messages.target")) {
            fileConfiguration.set("messages.attacker", null);
            fileConfiguration.set("messages.target", null);
            fileConfiguration.set("messages.title", (Object)"&c&lREGENERACJA");
            fileConfiguration.set("messages.subtitle", (Object)"&7Otrzyma\u0142e\u015b efekt regeneracji na &e{DURATION} &7sekund!");
            fileConfiguration.set("messages.chat", (Object)"");
            try {
                this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return fileConfiguration;
    }
}

