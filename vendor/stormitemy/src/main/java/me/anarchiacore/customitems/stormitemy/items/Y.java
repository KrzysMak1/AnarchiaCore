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
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.weather.LightningStrikeEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class Y
implements Listener {
    private final Plugin C;
    private final ConfigurationSection A;
    private final Map<UUID, Long> B = new HashMap();

    public Y(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "siekieragrincha.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Siekiera grincha' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Siekiera grincha'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}      - nazwa gracza, kt\u00f3ry uderzy\u0142\n  {DAMAGE}      - obra\u017cenia zadane umiej\u0119tno\u015bci\u0105\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("siekieragrincha.material", (Object)"GOLDEN_AXE");
                yamlConfiguration.set("siekieragrincha.name", (Object)"&2Siekiera Grincha");
                yamlConfiguration.set("siekieragrincha.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot z:", (Object)"&8 \u00bb &feventu \u015bwi\u0105tecznego (2023)", (Object)"&r", (Object)"&8 \u00bb &7Po &auderzeniu &7strzela &fpiorun", (Object)"&8 \u00bb &7w przeciwnika zadaj\u0105c mu &c30%&7 jego", (Object)"&8 \u00bb &7aktualnego zdrowia jako obra\u017cenia,", (Object)"&8 \u00bb &7oraz wywo\u0142uj\u0105c &fefekt wizualny&7!", (Object)"&8 \u00bb &7Umiej\u0119tno\u015b\u0107 &7dzia\u0142a raz na 1 minut\u0119.", (Object)"&r"));
                yamlConfiguration.set("siekieragrincha.customModelData", (Object)1);
                yamlConfiguration.set("siekieragrincha.cooldown", (Object)60);
                yamlConfiguration.set("siekieragrincha.damage.enabled", (Object)true);
                yamlConfiguration.set("siekieragrincha.damage.percentage", (Object)30);
                yamlConfiguration.set("siekieragrincha.enchantments", (Object)List.of((Object)"DURABILITY:3", (Object)"EFFICIENCY:5"));
                yamlConfiguration.set("siekieragrincha.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("siekieragrincha.unbreakable", (Object)true);
                yamlConfiguration.set("siekieragrincha.messages.attacker.title", (Object)"&2SIEKIERA GRINCHA");
                yamlConfiguration.set("siekieragrincha.messages.attacker.subtitle", (Object)"&7Uderzy\u0142e\u015b &f{player} &7siekier\u0105 &agrincha&7! (&c-{damage} HP&7)");
                yamlConfiguration.set("siekieragrincha.messages.attacker.chatMessage", (Object)"");
                yamlConfiguration.set("siekieragrincha.messages.victim.title", (Object)"&2SIEKIERA GRINCHA");
                yamlConfiguration.set("siekieragrincha.messages.victim.subtitle", (Object)"&7Zosta\u0142e\u015b &fuderzony &7siekier\u0105 &agrincha&7! (&c-{damage} HP&7)");
                yamlConfiguration.set("siekieragrincha.messages.victim.chatMessage", (Object)"");
                yamlConfiguration.set("siekieragrincha.messages.cooldown", (Object)"");
                yamlConfiguration.set("siekieragrincha.sounds.hit.enabled", (Object)true);
                yamlConfiguration.set("siekieragrincha.sounds.hit.sound", (Object)"ENTITY_LIGHTNING_BOLT_THUNDER");
                yamlConfiguration.set("siekieragrincha.sounds.hit.volume", (Object)1.0);
                yamlConfiguration.set("siekieragrincha.sounds.hit.pitch", (Object)1.2);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("siekieragrincha");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public ItemStack getItem() {
        List list;
        Material material;
        String string = this.A.getString("material", "GOLDEN_AXE");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.GOLDEN_AXE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&2Siekiera Grincha"));
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

    private boolean A(ItemStack itemStack) {
        boolean bl;
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&2Siekiera Grincha"));
        try {
            material = Material.valueOf((String)this.A.getString("material", "GOLDEN_AXE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.GOLDEN_AXE;
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
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("siekieragrincha")) {
            return;
        }
        if (entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Player player = (Player)entityDamageByEntityEvent.getDamager();
            Player player2 = (Player)entityDamageByEntityEvent.getEntity();
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (!this.A(itemStack)) {
                return;
            }
            try {
                boolean bl = false;
                boolean bl2 = false;
                if (this.C instanceof Main) {
                    Main main = (Main)this.C;
                    if (main.isPlayerInBlockedRegion(player)) {
                        bl = true;
                    }
                    if (main.isPlayerInBlockedRegion(player2)) {
                        bl2 = true;
                    }
                    if (bl || bl2) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        return;
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
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
            this.B.put((Object)player2.getUniqueId(), (Object)(System.currentTimeMillis() + 1000L));
            player2.getWorld().strikeLightning(player2.getLocation());
            this.A(player, player2, "hit");
            double d2 = 0.0;
            boolean bl = this.A.getBoolean("damage.enabled", true);
            if (bl) {
                int n3 = this.A.getInt("damage.percentage", 30);
                double d3 = player2.getHealth();
                if (d3 - (d2 = d3 * (double)n3 / 100.0) <= 0.5) {
                    d2 = d3 - 0.5;
                }
                if (d2 > 0.0) {
                    double d4 = d3 - d2;
                    player2.setHealth(Math.max((double)0.5, (double)d4));
                }
            }
            entityDamageByEntityEvent.setCancelled(true);
            ConfigurationSection configurationSection = this.A.getConfigurationSection("messages.attacker");
            ConfigurationSection configurationSection2 = this.A.getConfigurationSection("messages.victim");
            String string = String.format((String)"%.1f", (Object[])new Object[]{d2});
            String string2 = configurationSection2.getString("title", "");
            String string3 = configurationSection2.getString("subtitle", "").replace((CharSequence)"{damage}", (CharSequence)string);
            if (!string2.isEmpty() || !string3.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 70, 15);
            }
            String string4 = configurationSection.getString("title", "");
            String string5 = configurationSection.getString("subtitle", "").replace((CharSequence)"{player}", (CharSequence)player2.getName()).replace((CharSequence)"{damage}", (CharSequence)string);
            if (!string4.isEmpty() || !string5.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5), 10, 70, 15);
            }
            String string6 = configurationSection.getString("chatMessage", "").replace((CharSequence)"{player}", (CharSequence)player2.getName()).replace((CharSequence)"{damage}", (CharSequence)string);
            String string7 = configurationSection2.getString("chatMessage", "").replace((CharSequence)"{player}", (CharSequence)player.getName()).replace((CharSequence)"{damage}", (CharSequence)string);
            if (!string6.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6));
            }
            if (!string7.isEmpty()) {
                player2.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string7));
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onLightningStrike(LightningStrikeEvent lightningStrikeEvent) {
        lightningStrikeEvent.getLightning().getNearbyEntities(5.0, 5.0, 5.0).forEach(entity -> {
            Player player;
            UUID uUID;
            Long l2;
            if (entity instanceof Player && (l2 = (Long)this.B.get((Object)(uUID = (player = (Player)entity).getUniqueId()))) != null) {
                if (System.currentTimeMillis() < l2) {
                    lightningStrikeEvent.getLightning().setMetadata("siekiera_grincha_no_damage", (MetadataValue)new FixedMetadataValue(this.C, (Object)uUID.toString()));
                } else {
                    this.B.remove((Object)uUID);
                }
            }
        });
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onLightningDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getDamager().getType() == EntityType.LIGHTNING && entityDamageByEntityEvent.getEntity() instanceof Player) {
            Object object;
            Player player = (Player)entityDamageByEntityEvent.getEntity();
            UUID uUID = player.getUniqueId();
            if (entityDamageByEntityEvent.getDamager().hasMetadata("siekiera_grincha_no_damage")) {
                object = ((MetadataValue)entityDamageByEntityEvent.getDamager().getMetadata("siekiera_grincha_no_damage").get(0)).asString();
                if (uUID.toString().equals(object)) {
                    entityDamageByEntityEvent.setCancelled(true);
                    return;
                }
            }
            if ((object = (Long)this.B.get((Object)uUID)) != null && System.currentTimeMillis() < object) {
                entityDamageByEntityEvent.setCancelled(true);
            }
        }
    }

    private void A(Player player, Player player2, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_LIGHTNING_BOLT_THUNDER");
        float f2 = (float)configurationSection.getDouble("volume", 1.0);
        float f3 = (float)configurationSection.getDouble("pitch", 1.0);
        try {
            Sound sound = Sound.valueOf((String)string2);
            player.playSound(player2.getLocation(), sound, f2, f3);
            player2.playSound(player2.getLocation(), sound, f2, f3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            player.playSound(player2.getLocation(), string2, f2, f3);
            player2.playSound(player2.getLocation(), string2, f2, f3);
        }
    }
}

