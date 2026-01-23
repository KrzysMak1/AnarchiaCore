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
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.books.A;
import me.anarchiacore.customitems.stormitemy.books.D;

public class F
extends D
implements Listener {
    public F(Main main) {
        super(main, "Wampiryzm I", "wampiryzm", (List<String>)Arrays.asList((Object[])new String[]{"sword"}), 0.2, 30, (List<String>)Arrays.asList((Object[])new String[]{"&7Wampiryzm I", "&7", "&2&lZakl\u0119cie specjalne!", "&7Mo\u017ce zosta\u0107 na\u0142o\u017cony na &amiecz&7!", "&7Masz szans\u0119, \u017ce zadaj\u0105c obra\u017cenia przeciwnikowi", "&7wyleczy Ci\u0119 to o cz\u0119\u015b\u0107 zadanych obra\u017ce\u0144.", "&7"}));
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
        hashMap.put((Object)"sound", (Object)"entity.wither.hurt");
        hashMap.put((Object)"volume", (Object)0.5);
        hashMap.put((Object)"pitch", (Object)1.5);
        return hashMap;
    }

    @Override
    protected FileConfiguration loadConfiguration() {
        FileConfiguration fileConfiguration = super.loadConfiguration();
        fileConfiguration.set("customModelData", (Object)10);
        try {
            this.saveCustomConfig((YamlConfiguration)fileConfiguration, this.configFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (fileConfiguration.getString("messages.attacker.title", "").contains((CharSequence)"TRUCIZNY") || !fileConfiguration.isSet("vampirism.heal_percent") || !fileConfiguration.isSet("vampirism.min_heal_amount")) {
            fileConfiguration.set("messages.attacker.title", (Object)"&4ZAKL\u0118CIE WAMPIRYZMU");
            fileConfiguration.set("messages.attacker.subtitle", (Object)"&fWyleczy\u0142e\u015b &7swoje HP!");
            fileConfiguration.set("messages.attacker.chat", (Object)"");
            fileConfiguration.set("messages.target.title", (Object)"");
            fileConfiguration.set("messages.target.subtitle", (Object)"");
            fileConfiguration.set("messages.target.chat", (Object)"");
            fileConfiguration.set("vampirism.heal_percent", (Object)80);
            fileConfiguration.set("vampirism.min_heal_amount", (Object)1);
            fileConfiguration.set("vampirism.chance", (Object)20);
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

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerDamageEntity(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        final Player player = (Player)entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType().isAir()) {
            return;
        }
        if (!itemStack.hasItemMeta()) {
            return;
        }
        NamespacedKey namespacedKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + this.getIdentifier());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)) {
            return;
        }
        final UUID uUID = player.getUniqueId();
        final A a2 = this.plugin.getEnchantedBooksManager();
        if (a2.B(uUID, this.getIdentifier())) {
            return;
        }
        double d2 = this.getConfigChance();
        final double d3 = entityDamageByEntityEvent.getDamage();
        final double d4 = entityDamageByEntityEvent.getFinalDamage();
        double d5 = Math.random() * 100.0;
        if (d5 <= d2) {
            new BukkitRunnable(this){
                final /* synthetic */ F this$0;
                {
                    this.this$0 = f2;
                }

                public void run() {
                    double d2;
                    double d32;
                    double d42;
                    double d5;
                    int n2;
                    if (player.isDead() || player.getHealth() <= 0.0) {
                        return;
                    }
                    double d6 = Math.max((double)d3, (double)d4);
                    double d7 = d6 * (double)(n2 = this.this$0.config.getInt("vampirism.heal_percent", 80)) / 100.0;
                    if (d7 < (d5 = this.this$0.config.getDouble("vampirism.min_heal_amount", 1.0))) {
                        d7 = d5;
                    }
                    if ((d42 = Math.min((double)((d32 = player.getHealth()) + d7), (double)(d2 = player.getMaxHealth()))) > d32) {
                        player.setHealth(d42);
                        a2.A(uUID, this.this$0.getIdentifier(), this.this$0.getCooldownSeconds());
                        this.this$0.F(player);
                        this.this$0.A(player, entityDamageByEntityEvent.getEntity(), d7);
                    }
                }
            }.runTaskLater((Plugin)this.plugin, 1L);
        }
    }

    @Override
    public void handleEffect(ItemStack itemStack, Object ... objectArray) {
        if (objectArray.length < 3) {
            return;
        }
        if (!(objectArray[0] instanceof Player) || !(objectArray[1] instanceof Entity)) {
            return;
        }
        Player player = (Player)objectArray[0];
        Entity entity = (Entity)objectArray[1];
        EntityDamageByEntityEvent entityDamageByEntityEvent = null;
        if (objectArray.length < 4 || !(objectArray[3] instanceof EntityDamageByEntityEvent)) {
            return;
        }
        entityDamageByEntityEvent = (EntityDamageByEntityEvent)objectArray[3];
    }

    private void F(Player player) {
        ConfigurationSection configurationSection = this.getSoundConfig();
        if (configurationSection != null && configurationSection.getBoolean("enabled", true)) {
            String string = configurationSection.getString("sound", "entity.wither.hurt");
            float f2 = (float)configurationSection.getDouble("volume", 0.5);
            float f3 = (float)configurationSection.getDouble("pitch", 1.5);
            try {
                Sound sound = Sound.valueOf((String)string.toUpperCase().replace((CharSequence)".", (CharSequence)"_"));
                player.playSound(player.getLocation(), sound, f2, f3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                player.playSound(player.getLocation(), string, f2, f3);
            }
        }
    }

    private void A(Player player, Entity entity, double d2) {
        String string = this.getAttackerChat().replace((CharSequence)"{TARGET}", (CharSequence)(entity instanceof Player ? ((Player)entity).getName() : "entity"));
        if (!(string = string.replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2}))).isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        }
        String string2 = this.getAttackerTitle();
        String string3 = this.getAttackerSubtitle().replace((CharSequence)"{TARGET}", (CharSequence)(entity instanceof Player ? ((Player)entity).getName() : "entity")).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2}));
        if (!string2.isEmpty() || !string3.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 60, 20);
        }
        if (entity instanceof Player) {
            Player player2 = (Player)entity;
            String string4 = this.getTargetChat().replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2}));
            if (!string4.isEmpty()) {
                player2.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4));
            }
            String string5 = this.getTargetTitle();
            String string6 = this.getTargetSubtitle().replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d2}));
            if (!string5.isEmpty() || !string6.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6), 10, 60, 20);
            }
        }
    }
}

