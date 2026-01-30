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
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class N
implements Listener {
    private final Main B;
    private final ConfigurationSection A;

    public N(Main main) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = main;
        File file2 = new File(main.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "boskitopor.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Boski top\u00f3r' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Boski top\u00f3r'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("boskitopor.material", (Object)"IRON_AXE");
                yamlConfiguration.set("boskitopor.name", (Object)"&b&lBoski top\u00f3r");
                yamlConfiguration.set("boskitopor.lore", (Object)Arrays.asList((Object[])new String[]{"&r", "&8 \u00bb &7Jest to przedmiot z:", "&8 \u00bb &fspecjalnego wydarzenia (2025)", "&r", "&8 \u00bb &7Po klikni\u0119ciu prawym przyciskiem", "&8 \u00bb &7aktywujesz &epot\u0119\u017cn\u0105 fale uderzeniow\u0105", "&8 \u00bb &7kt\u00f3ra zapewnia Tobie &cnie\u015bmiertelno\u015b\u0107&7!", "&r"}));
                yamlConfiguration.set("boskitopor.customModelData", (Object)3462232);
                yamlConfiguration.set("boskitopor.cooldown", (Object)60);
                yamlConfiguration.set("boskitopor.enchantments", (Object)Arrays.asList((Object[])new String[]{"DURABILITY:3", "EFFICIENCY:2"}));
                yamlConfiguration.set("boskitopor.flags", (Object)Arrays.asList((Object[])new String[]{"HIDE_ATTRIBUTES", "HIDE_ENCHANTS", "HIDE_UNBREAKABLE", "HIDE_POTION_EFFECTS", "HIDE_DESTROYS", "HIDE_PLACED_ON"}));
                yamlConfiguration.set("boskitopor.messages.consumer.title", (Object)"&b&lBOSKI TOP\u00d3R");
                yamlConfiguration.set("boskitopor.messages.consumer.subtitle", (Object)"&7Aktywowa\u0142e\u015b przedmiot &fboski top\u00f3r&7!");
                yamlConfiguration.set("boskitopor.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("boskitopor.messages.target.title", (Object)"&b&lBOSKI TOP\u00d3R");
                yamlConfiguration.set("boskitopor.messages.target.subtitle", (Object)"&7Gracz &f{PLAYER} &7aktywowa\u0142 &fboski top\u00f3r&7!");
                yamlConfiguration.set("boskitopor.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("boskitopor.messages.cooldown", (Object)"");
                yamlConfiguration.set("boskitopor.invulnerability.duration", (Object)3);
                yamlConfiguration.set("boskitopor.particleEffects.explosion.type", (Object)"EXPLOSION_LARGE");
                yamlConfiguration.set("boskitopor.particleEffects.explosion.count", (Object)10);
                yamlConfiguration.set("boskitopor.particleEffects.flame.type", (Object)"FLAME");
                yamlConfiguration.set("boskitopor.particleEffects.flame.count", (Object)20);
                yamlConfiguration.set("boskitopor.particleEffects.flame.offset.x", (Object)1);
                yamlConfiguration.set("boskitopor.particleEffects.flame.offset.y", (Object)2);
                yamlConfiguration.set("boskitopor.particleEffects.flame.offset.z", (Object)1);
                yamlConfiguration.set("boskitopor.particleEffects.circle.type", (Object)"CRIT_MAGIC");
                yamlConfiguration.set("boskitopor.particleEffects.circle.delay", (Object)5);
                yamlConfiguration.set("boskitopor.particleEffects.circle.duration", (Object)3);
                yamlConfiguration.set("boskitopor.particleEffects.circle.radius", (Object)1.5);
                yamlConfiguration.set("boskitopor.particleEffects.circle.angleStep", (Object)20);
                yamlConfiguration.set("boskitopor.pushEffect.radius", (Object)5.0);
                yamlConfiguration.set("boskitopor.pushEffect.verticalBoost", (Object)0.2);
                yamlConfiguration.set("boskitopor.pushEffect.minForce", (Object)1.0);
                yamlConfiguration.set("boskitopor.pushEffect.maxForce", (Object)3.0);
                yamlConfiguration.set("boskitopor.sound.type", (Object)"ENTITY_ENDER_DRAGON_AMBIENT");
                yamlConfiguration.set("boskitopor.sound.volume", (Object)1.0);
                yamlConfiguration.set("boskitopor.sound.pitch", (Object)0.5);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("boskitopor");
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
    }

    public ItemStack getItem() {
        return this.getItemStack();
    }

    public ItemStack getItemStack() {
        String string;
        String[] stringArray2;
        List list;
        Material material;
        String string2 = this.A.getString("material", "IRON_AXE");
        try {
            material = Material.valueOf((String)string2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.IRON_AXE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lBoski top\u00f3r"));
        itemMeta.setDisplayName(string3);
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("lore")) {
            list = this.A.getStringList("lore");
            Object object = new ArrayList();
            stringArray2 = list.iterator();
            while (stringArray2.hasNext()) {
                string = (String)stringArray2.next();
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            }
            itemMeta.setLore((List)object);
        }
        if (this.A.contains("flags")) {
            list = this.A.getStringList("flags");
            for (String[] stringArray2 : list) {
                try {
                    string = ItemFlag.valueOf((String)stringArray2);
                    itemMeta.addItemFlags(new ItemFlag[]{string});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        if (this.A.contains("enchantments")) {
            for (Object object : this.A.getStringList("enchantments")) {
                Enchantment enchantment;
                if (object == null || (stringArray2 = object.split(":")).length < 2) continue;
                string = stringArray2[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray2[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                if ((enchantment = Enchantment.getByName((String)string)) == null) continue;
                itemMeta.addEnchant(enchantment, n2, true);
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        String string;
        if (playerInteractEvent.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!playerInteractEvent.getAction().toString().contains((CharSequence)"RIGHT_CLICK")) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }
        String string2 = itemStack.getItemMeta().getDisplayName();
        if (!string2.equals((Object)(string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b&lBoski top\u00f3r"))))) {
            return;
        }
        if (this.B instanceof Main && this.B.isItemDisabledByKey("boskitopor")) {
            return;
        }
        try {
            if (this.B.isPlayerInBlockedRegion(player)) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                return;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.A(player, itemStack);
    }

    private void A(final Player player, ItemStack itemStack) {
        ConfigurationSection configurationSection;
        ConfigurationSection configurationSection2;
        ConfigurationSection configurationSection3;
        ConfigurationSection configurationSection4;
        double d2;
        Object object;
        String string;
        int n2 = this.A.getInt("cooldown", 10);
        if (player.hasCooldown(itemStack.getType())) {
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("messages.cooldown", "").replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)n2)));
            if (!string2.isEmpty()) {
                player.sendMessage(string2);
            }
            return;
        }
        me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A((Plugin)this.B, player, itemStack, n2, "boskitopor");
        ConfigurationSection configurationSection5 = this.A.getConfigurationSection("particleEffects.explosion");
        if (configurationSection5 != null) {
            string = configurationSection5.getString("type", "EXPLOSION");
            try {
                object = Particle.valueOf((String)string);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                object = Particle.EXPLOSION_LARGE;
            }
            int n3 = configurationSection5.getInt("count", 10);
            player.getWorld().spawnParticle(object, player.getLocation(), n3);
        }
        if ((string = this.A.getConfigurationSection("particleEffects.flame")) != null) {
            Particle particle;
            object = string.getString("type", "FLAME");
            try {
                particle = Particle.valueOf((String)object);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                particle = Particle.FLAME;
            }
            int n4 = string.getInt("count", 20);
            double d3 = string.getConfigurationSection("offset").getDouble("x", 0.0);
            d2 = string.getConfigurationSection("offset").getDouble("y", 0.0);
            double d4 = string.getConfigurationSection("offset").getDouble("z", 0.0);
            player.getWorld().spawnParticle(particle, player.getLocation(), n4, d3, d2, d4, 0.0);
        }
        if ((object = this.A.getConfigurationSection("particleEffects.circle")) != null) {
            Particle particle;
            String string3 = object.getString("type", "CRIT_MAGIC");
            try {
                particle = Particle.valueOf((String)string3);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                particle = Particle.CRIT_MAGIC;
            }
            final Particle particle2 = particle;
            final int n5 = object.getInt("angleStep", 20);
            d2 = object.getDouble("radius", 1.5);
            final int n6 = object.getInt("delay", 5);
            int n7 = object.getInt("duration", 3);
            final int n8 = n7 * 20;
            new BukkitRunnable(this){
                int ticks = 0;
                final /* synthetic */ n this$0;
                {
                    this.this$0 = n2;
                }

                public void run() {
                    if (this.ticks >= n8) {
                        this.cancel();
                        return;
                    }
                    Location location = player.getLocation();
                    for (double d22 = 0.0; d22 < 360.0; d22 += (double)n5) {
                        double d3 = Math.toRadians((double)d22);
                        double d4 = Math.cos((double)d3) * d2;
                        double d5 = Math.sin((double)d3) * d2;
                        Location location2 = location.clone().add(d4, 0.5, d5);
                        player.getWorld().spawnParticle(particle2, location2, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                    this.ticks += n6;
                }
            }.runTaskTimer((Plugin)this.B, 0L, (long)n6);
        }
        if ((configurationSection4 = this.A.getConfigurationSection("pushEffect")) != null) {
            double d5;
            double d6;
            double d7 = configurationSection4.getDouble("radius", 5.0);
            if (configurationSection4.contains("minForce") && configurationSection4.contains("maxForce")) {
                d6 = configurationSection4.getDouble("minForce");
                double d8 = configurationSection4.getDouble("maxForce");
                d5 = d6 + (d8 - d6) * Math.random();
            } else {
                d5 = configurationSection4.getDouble("multiplier", 2.0);
            }
            d6 = configurationSection4.getDouble("verticalBoost", 0.2);
            for (Entity entity : player.getNearbyEntities(d7, d7, d7)) {
                Player player2;
                if (!(entity instanceof Player) || entity == player || this.B.isPlayerInBlockedRegion(player2 = (Player)entity)) continue;
                Vector vector = player2.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                vector.setY(d6);
                player2.setVelocity(vector.multiply(d5));
                ConfigurationSection configurationSection6 = this.A.getConfigurationSection("messages.target");
                if (configurationSection6 == null) continue;
                String string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection6.getString("title", "").replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()));
                String string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection6.getString("subtitle", "").replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()));
                player2.sendTitle(string4, string5, 10, 70, 15);
            }
        }
        if ((configurationSection3 = this.A.getConfigurationSection("invulnerability")) != null) {
            int n9 = configurationSection3.getInt("duration", 3);
            player.setInvulnerable(true);
            new BukkitRunnable(this){
                final /* synthetic */ n this$0;
                {
                    this.this$0 = n2;
                }

                public void run() {
                    player.setInvulnerable(false);
                }
            }.runTaskLater((Plugin)this.B, (long)(n9 * 20));
        }
        if ((configurationSection2 = this.A.getConfigurationSection("sound")) != null) {
            Sound sound;
            String string6 = configurationSection2.getString("type", "ENTITY_ENDER_DRAGON_AMBIENT");
            try {
                sound = Sound.valueOf((String)string6);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                sound = Sound.ENTITY_ENDER_DRAGON_AMBIENT;
            }
            float f2 = (float)configurationSection2.getDouble("volume", 1.0);
            float f3 = (float)configurationSection2.getDouble("pitch", 0.5);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        if ((configurationSection = this.A.getConfigurationSection("messages.consumer")) != null) {
            String string7 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("title", ""));
            String string8 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(configurationSection.getString("subtitle", ""));
            player.sendTitle(string7, string8, 10, 70, 15);
        }
    }
}

