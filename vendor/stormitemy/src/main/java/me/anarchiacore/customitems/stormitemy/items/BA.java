/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Set
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.BlockFace
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.Vector
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class BA
implements Listener {
    private final Plugin B;
    private final ConfigurationSection A;

    public BA(Plugin plugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.B = plugin;
        File file2 = new File(plugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "rozgotowanakukurydza.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Rozgotowana kukurydza' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci eventowego przedmiotu 'Rozgotowana kukurydza'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME} - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER} - nazwa gracza, kt\u00f3ry u\u017cy\u0142 przedmiotu\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("rozgotowanakukurydza.material", (Object)"BLAZE_ROD");
                yamlConfiguration.set("rozgotowanakukurydza.name", (Object)"&a&lRozgotowana kukurydza");
                yamlConfiguration.set("rozgotowanakukurydza.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty z", (Object)"&8 \u00bb &fwydarzenia wakacyjnego 2024&7!", (Object)"&r", (Object)"&8 \u00bb &7Wystrzeliwuje magiczny pocisk,", (Object)"&8 \u00bb &7kt\u00f3ry niszczy zbroj\u0119 oraz zadaje", (Object)"&8 \u00bb &7obra\u017cenia w miejscu wybuchu.", (Object)"&r"));
                yamlConfiguration.set("rozgotowanakukurydza.customModelData", (Object)1);
                yamlConfiguration.set("rozgotowanakukurydza.cooldown", (Object)60);
                yamlConfiguration.set("rozgotowanakukurydza.enchantments", (Object)List.of());
                yamlConfiguration.set("rozgotowanakukurydza.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("rozgotowanakukurydza.unbreakable", (Object)true);
                yamlConfiguration.set("rozgotowanakukurydza.explosionPower", (Object)4);
                yamlConfiguration.set("rozgotowanakukurydza.createFire", (Object)false);
                yamlConfiguration.set("rozgotowanakukurydza.noDestroyRegions", (Object)List.of((Object)"pvp"));
                yamlConfiguration.set("rozgotowanakukurydza.preventRegionDestruction", (Object)true);
                yamlConfiguration.set("rozgotowanakukurydza.protectedBlocks", (Object)List.of((Object)"BEDROCK"));
                yamlConfiguration.set("rozgotowanakukurydza.pvpWorlds", (Object)List.of((Object)"mapapvp"));
                yamlConfiguration.set("rozgotowanakukurydza.messages.shooter.title", (Object)"&a&lROZGOTOWANA KUKURYDZA");
                yamlConfiguration.set("rozgotowanakukurydza.messages.shooter.subtitle", (Object)"&7Wystrzeli\u0142e\u015b &fpocisk&7!");
                yamlConfiguration.set("rozgotowanakukurydza.messages.cooldown", (Object)"");
                yamlConfiguration.set("rozgotowanakukurydza.sounds.shoot.enabled", (Object)true);
                yamlConfiguration.set("rozgotowanakukurydza.sounds.shoot.sound", (Object)"ENTITY_BLAZE_SHOOT");
                yamlConfiguration.set("rozgotowanakukurydza.sounds.shoot.volume", (Object)1.0);
                yamlConfiguration.set("rozgotowanakukurydza.sounds.shoot.pitch", (Object)1.0);
                yamlConfiguration.set("rozgotowanakukurydza.sounds.explode.enabled", (Object)true);
                yamlConfiguration.set("rozgotowanakukurydza.sounds.explode.sound", (Object)"ENTITY_GENERIC_EXPLODE");
                yamlConfiguration.set("rozgotowanakukurydza.sounds.explode.volume", (Object)1.0);
                yamlConfiguration.set("rozgotowanakukurydza.sounds.explode.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                plugin.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku rozgotowanakukurydza.yml: " + iOException.getMessage());
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("rozgotowanakukurydza");
        plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
    }

    public ItemStack getItem() {
        List list;
        Material material;
        String string = this.A.getString("material", "BLAZE_ROD");
        try {
            material = Material.valueOf((String)string);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BLAZE_ROD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lRozgotowana kukurydza"));
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
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lRozgotowana kukurydza"));
        try {
            material = Material.valueOf((String)this.A.getString("material", "BLAZE_ROD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BLAZE_ROD;
        }
        int n2 = this.A.getInt("customModelData", 0);
        boolean bl2 = bl = itemStack.getType() == material && itemStack.getItemMeta().getDisplayName().equals((Object)string);
        if (itemStack.getItemMeta().hasCustomModelData()) {
            bl = bl && itemStack.getItemMeta().getCustomModelData() == n2;
        }
        return bl;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Action action = playerInteractEvent.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || !this.A(itemStack)) {
            return;
        }
        if (this.B instanceof Main && ((Main)this.B).isItemDisabledByKey("rozgotowanakukurydza")) {
            return;
        }
        try {
            Main main;
            if (this.B instanceof Main && (main = (Main)this.B).isPlayerInBlockedRegion(player)) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                playerInteractEvent.setCancelled(true);
                return;
            }
        }
        catch (Exception exception) {
            this.B.getLogger().warning("B\u0142\u0105d sprawdzania regionu dla RozgotowanaKukurydza: " + exception.getMessage());
        }
        int n2 = this.A.getInt("cooldown", 15);
        int n3 = n2 * 20;
        if (player.hasCooldown(itemStack.getType())) {
            long l2 = player.getCooldown(itemStack.getType()) / 20;
            String string = this.A.getString("messages.cooldown", "");
            if (!string.isEmpty()) {
                string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l2)));
                player.sendMessage(string);
            }
            return;
        }
        player.setCooldown(itemStack.getType(), n3);
        Fireball fireball = (Fireball)player.launchProjectile(Fireball.class);
        fireball.setYield(0.0f);
        fireball.setIsIncendiary(false);
        fireball.setMetadata("rozgotowana", (MetadataValue)new FixedMetadataValue(this.B, (Object)true));
        this.A(player, "shoot");
        String string = this.A.getString("messages.shooter.title", "");
        String string2 = this.A.getString("messages.shooter.subtitle", "");
        if (!string.isEmpty() || !string2.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 70, 15);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        if (!(projectileHitEvent.getEntity() instanceof Fireball) || !projectileHitEvent.getEntity().hasMetadata("rozgotowana")) {
            return;
        }
        Fireball fireball = (Fireball)projectileHitEvent.getEntity();
        Location location = fireball.getLocation();
        int n2 = this.A.getInt("explosionPower", 4);
        boolean bl = this.A.getBoolean("createFire", false);
        List list = this.A.getStringList("protectedBlocks");
        HashSet hashSet = new HashSet();
        for (String string : list) {
            try {
                Material material = Material.valueOf((String)string);
                hashSet.add((Object)material);
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
        this.A(location, n2, bl, (Set<Material>)hashSet);
        this.A(location, 5.0, 1.5);
        fireball.remove();
    }

    private void A(Location location, double d2, double d3) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, d2, d2, d2)) {
            Player player;
            if (entity instanceof Player) {
                Vector vector;
                player = (Player)entity;
                try {
                    vector = player.getLocation().toVector().subtract(location.toVector());
                    if (vector.lengthSquared() > 1.0E-4 && Double.isFinite((double)(vector = vector.normalize().multiply(d3)).getX()) && Double.isFinite((double)vector.getY()) && Double.isFinite((double)vector.getZ())) {
                        player.setVelocity(vector);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                vector = player.getInventory().getArmorContents();
                for (int i2 = 0; i2 < ((Vector)vector).length; ++i2) {
                    Vector vector2 = vector[i2];
                    if (vector2 == null || vector2.getType() == Material.AIR || !(vector2.getItemMeta() instanceof Damageable)) continue;
                    Damageable damageable = (Damageable)vector2.getItemMeta();
                    damageable.setDamage(damageable.getDamage() + 30);
                    vector2.setItemMeta((ItemMeta)damageable);
                }
                double d4 = player.getLocation().distance(location);
                if (!(d4 <= d2)) continue;
                double d5 = 14.0 * (1.0 - d4 / d2);
                player.damage(d5);
                continue;
            }
            try {
                player = entity.getLocation().toVector().subtract(location.toVector());
                if (!(player.lengthSquared() > 1.0E-4) || !Double.isFinite((double)(player = player.normalize().multiply(d3)).getX()) || !Double.isFinite((double)player.getY()) || !Double.isFinite((double)player.getZ())) continue;
                entity.setVelocity((Vector)player);
            }
            catch (Exception exception) {
                this.B.getLogger().warning("B\u0142\u0105d przy knockback entity: " + exception.getMessage());
            }
        }
    }

    private void A(Location location, int n2, boolean bl, Set<Material> set) {
        File file;
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (this.B instanceof Main) {
            Main main = (Main)this.B;
            arrayList = main.getConfig().getStringList("disabled-regions");
            arrayList2 = this.A.getStringList("noDestroyRegions");
            List<String> list = main.getRegionManager().A();
            for (String string : list) {
                file = new File(main.getDataFolder(), "items");
                try {
                    YamlConfiguration yamlConfiguration;
                    List list2;
                    File file2 = new File(file, "rozgotowanakukurydza.yml");
                    if (!file2.exists() || !(list2 = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2)).getStringList("rozgotowanakukurydza.noDestroyRegions")).contains((Object)string.toLowerCase()) || arrayList2.contains((Object)string.toLowerCase())) continue;
                    arrayList2.add((Object)string.toLowerCase());
                }
                catch (Exception exception) {
                    this.B.getLogger().warning("B\u0142\u0105d \u0142adowania konfiguracji noDestroyRegions: " + exception.getMessage());
                }
            }
        }
        world.spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        for (int i2 = -n2; i2 <= n2; ++i2) {
            for (int i3 = -n2; i3 <= n2; ++i3) {
                for (int i4 = -n2; i4 <= n2; ++i4) {
                    Block blockAbove;
                    boolean bl2;
                    String string;
                    if (!(Math.sqrt((double)(i2 * i2 + i3 * i3 + i4 * i4)) <= (double)n2)) continue;
                    Iterator iterator = location.clone().add((double)i2, (double)i3, (double)i4);
                    string = iterator.getBlock();
                    file = string.getType();
                    boolean bl3 = bl2 = file == Material.BLUE_GLAZED_TERRACOTTA || file == Material.BLUE_TERRACOTTA || file == Material.BUBBLE_CORAL_BLOCK || file == Material.LIGHT_BLUE_TERRACOTTA;
                    if (file == Material.AIR || set.contains((Object)file) || bl2) continue;
                    boolean bl4 = false;
                    boolean bl5 = false;
                    boolean bl6 = this.A.getBoolean("preventRegionDestruction", true);
                    if (bl6 && this.B instanceof Main) {
                        Main main = (Main)this.B;
                        String string3 = main.getRegionChecker().C((Location)iterator);
                        if (string3 != null && arrayList2.stream().anyMatch(string2 -> string2.equalsIgnoreCase(string3))) {
                            bl5 = true;
                        }
                        if (!bl5 && main.isTargetBlockInProtectedRegion((Location)iterator)) {
                            bl4 = true;
                        }
                    }
                    if (bl4) continue;
                    if (bl5) {
                        world.spawnParticle(Particle.EXPLOSION_NORMAL, (Location)iterator, 1, 0.0, 0.0, 0.0, 0.0);
                        continue;
                    }
                    if (string.hasMetadata("hydro_cage_block")) {
                        world.spawnParticle(Particle.WATER_SPLASH, (Location)iterator, 3, 0.2, 0.2, 0.2, 0.1);
                        continue;
                    }
                    string.setType(Material.AIR);
                    if (!bl || !(Math.random() < 0.3) || (blockAbove = string.getRelative(BlockFace.UP)).getType() != Material.AIR) continue;
                    blockAbove.setType(Material.FIRE);
                }
            }
        }
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_BLAZE_SHOOT");
        float f2 = (float)configurationSection.getDouble("volume", 1.0);
        float f3 = (float)configurationSection.getDouble("pitch", 1.0);
        try {
            Sound sound = Sound.valueOf((String)string2);
            player.getWorld().playSound(player.getLocation(), sound, f2, f3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            player.getWorld().playSound(player.getLocation(), string2, f2, f3);
        }
    }
}
