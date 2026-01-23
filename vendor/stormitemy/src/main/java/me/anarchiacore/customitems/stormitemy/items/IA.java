/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Iterable
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Snowball
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class IA
implements Listener {
    private final Plugin C;
    private ConfigurationSection A;
    private boolean B;

    public IA(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = javaPlugin;
        this.B = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "sniezynka.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu '\u015anie\u017cynka' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego '\u015anie\u017cynka'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}    - pozosta\u0142y czas cooldownu (w sekundach)\n  {SHOOTER} - nazwa gracza, kt\u00f3ry rzuci\u0142 przedmiot\n  {TARGET}  - nazwa gracza, kt\u00f3ry zosta\u0142 trafiony\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("sniezynka.material", (Object)"SNOWBALL");
                yamlConfiguration.set("sniezynka.name", (Object)"&b\u015anie\u017cynka");
                yamlConfiguration.set("sniezynka.lore", (Object)List.of((Object)"&7", (Object)"&8 \u00bb &7Przedmiot z &fwydarzenia miko\u0142ajkowego 2025&7!", (Object)"&7", (Object)"&8 \u00bb &7Rzu\u0107 w gracza, aby go &bzamrozi\u0107 &7na kr\u00f3tk\u0105 chwil\u0119.", (Object)"&8 \u00bb &7Mo\u017cesz j\u0105 &fzdoby\u0107 &7poprzez &ezabijanie ba\u0142wank\u00f3w", (Object)"&8 \u00bb &7albo &ekopi\u0105c bloki&7.", (Object)"&7", (Object)"&8 \u00bb &4\u26a0 &cPo zako\u0144czeniu wydarzenia przedmiot", (Object)"&8 \u00bb &ctraci swoje specjalne mo\u017cliwo\u015bci!", (Object)"&7"));
                yamlConfiguration.set("sniezynka.customModelData", (Object)1);
                yamlConfiguration.set("sniezynka.cooldown", (Object)1);
                yamlConfiguration.set("sniezynka.freezeDuration", (Object)10);
                yamlConfiguration.set("sniezynka.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("sniezynka.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("sniezynka.unbreakable", (Object)true);
                yamlConfiguration.set("sniezynka.messages.shooter.title", (Object)"&b&l\u015aNIE\u017bYNKA");
                yamlConfiguration.set("sniezynka.messages.shooter.subtitle", (Object)"&7Zamrozi\u0142e\u015b gracza &f{TARGET}&7!");
                yamlConfiguration.set("sniezynka.messages.shooter.chatMessage", (Object)"");
                yamlConfiguration.set("sniezynka.messages.target.title", (Object)"&b&l\u015aNIE\u017bYNKA");
                yamlConfiguration.set("sniezynka.messages.target.subtitle", (Object)"&7Zosta\u0142e\u015b zamro\u017cony przez &f{SHOOTER}&7!");
                yamlConfiguration.set("sniezynka.messages.target.chatMessage", (Object)"");
                yamlConfiguration.set("sniezynka.messages.cooldown", (Object)"");
                yamlConfiguration.set("sniezynka.sounds.throw.enabled", (Object)true);
                yamlConfiguration.set("sniezynka.sounds.throw.sound", (Object)"ENTITY_SNOWBALL_THROW");
                yamlConfiguration.set("sniezynka.sounds.throw.volume", (Object)1.0);
                yamlConfiguration.set("sniezynka.sounds.throw.pitch", (Object)1.2);
                yamlConfiguration.set("sniezynka.sounds.freeze.enabled", (Object)true);
                yamlConfiguration.set("sniezynka.sounds.freeze.sound", (Object)"BLOCK_GLASS_BREAK");
                yamlConfiguration.set("sniezynka.sounds.freeze.volume", (Object)1.0);
                yamlConfiguration.set("sniezynka.sounds.freeze.pitch", (Object)1.5);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("sniezynka");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Action action = playerInteractEvent.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemMeta itemMeta;
            Player player = playerInteractEvent.getPlayer();
            ItemStack itemStack = playerInteractEvent.getItem();
            if (itemStack != null && itemStack.getType() == Material.SNOWBALL && (itemMeta = itemStack.getItemMeta()) != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b\u015anie\u017cynka")))) {
                Object object;
                Object object2;
                Main main;
                if (this.A.contains("customModelData") && (!itemMeta.hasCustomModelData() || itemMeta.getCustomModelData() != this.A.getInt("customModelData", 1))) {
                    return;
                }
                if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("sniezynka")) {
                    return;
                }
                boolean bl = false;
                try {
                    if (this.C instanceof Main && (main = (Main)this.C).isPlayerInBlockedRegion(player)) {
                        bl = true;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (!bl && this.B) {
                    try {
                        main = Class.forName((String)"com.sk89q.worldguard.WorldGuard");
                        Object object3 = main.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                        object2 = main.getMethod("getPlatform", new Class[0]).invoke(object3, new Object[0]);
                        object = object2.getClass().getMethod("getRegionContainer", new Class[0]).invoke(object2, new Object[0]);
                        Object object4 = object.getClass().getMethod("createQuery", new Class[0]).invoke(object, new Object[0]);
                        Class clazz = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
                        Object object5 = clazz.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{player.getLocation()});
                        Object object6 = object4.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object4, new Object[]{object5});
                        List list = this.C.getConfig().getStringList("disabled-regions");
                        Iterable iterable = (Iterable)object6;
                        for (Object object7 : iterable) {
                            String string = (String)object7.getClass().getMethod("getId", new Class[0]).invoke(object7, new Object[0]);
                            for (String string2 : list) {
                                if (!string.equalsIgnoreCase(string2)) continue;
                                bl = true;
                                break;
                            }
                            if (!bl) continue;
                            break;
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (bl) {
                    me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                    playerInteractEvent.setCancelled(true);
                    return;
                }
                int n2 = this.A.getInt("cooldown", 10);
                int n3 = n2 * 20;
                if (player.getCooldown(Material.SNOWBALL) > 0) {
                    int n4 = player.getCooldown(Material.SNOWBALL) / 20;
                    object = this.A.getString("messages.cooldown", "");
                    if (!object.isEmpty()) {
                        object = object.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)n4));
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
                    }
                    playerInteractEvent.setCancelled(true);
                    return;
                }
                player.setCooldown(Material.SNOWBALL, n3);
                playerInteractEvent.setCancelled(true);
                object2 = player.getInventory().getItemInMainHand();
                if (object2.getAmount() > 1) {
                    object2.setAmount(object2.getAmount() - 1);
                    player.getInventory().setItemInMainHand((ItemStack)object2);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
                object = (Snowball)player.launchProjectile(Snowball.class);
                object.setMetadata("sniezynka", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        Projectile projectile = projectileHitEvent.getEntity();
        if (!(projectile instanceof Snowball)) {
            return;
        }
        if (!projectile.hasMetadata("sniezynka")) {
            return;
        }
        if (!(projectile.getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectile.getShooter();
        if (projectileHitEvent.getHitEntity() instanceof Player) {
            Object object;
            Object object2;
            Object object3;
            Object object4;
            Object object5;
            Object object6;
            Main main;
            Player player2 = (Player)projectileHitEvent.getHitEntity();
            boolean bl = false;
            boolean bl2 = false;
            try {
                if (this.C instanceof Main) {
                    main = (Main)this.C;
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
            if (this.B && !bl && !bl2) {
                try {
                    main = Class.forName((String)"com.sk89q.worldguard.WorldGuard");
                    object6 = main.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    object5 = main.getMethod("getPlatform", new Class[0]).invoke(object6, new Object[0]);
                    object4 = object5.getClass().getMethod("getRegionContainer", new Class[0]).invoke(object5, new Object[0]);
                    object3 = object4.getClass().getMethod("createQuery", new Class[0]).invoke(object4, new Object[0]);
                    object2 = Class.forName((String)"com.sk89q.worldedit.bukkit.BukkitAdapter");
                    object = object2.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{player.getLocation()});
                    Object object7 = object3.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object3, new Object[]{object});
                    Object object8 = object2.getMethod("adapt", new Class[]{Location.class}).invoke(null, new Object[]{player2.getLocation()});
                    Object object9 = object3.getClass().getMethod("getApplicableRegions", new Class[]{Class.forName((String)"com.sk89q.worldedit.math.BlockVector3")}).invoke(object3, new Object[]{object8});
                    List list = this.C.getConfig().getStringList("disabled-regions");
                    Iterable iterable = (Iterable)object7;
                    for (Object object10 : iterable) {
                        Object object11 = (String)object10.getClass().getMethod("getId", new Class[0]).invoke(object10, new Object[0]);
                        for (String string : list) {
                            if (!object11.equalsIgnoreCase(string)) continue;
                            bl = true;
                            break;
                        }
                        if (!bl) continue;
                        break;
                    }
                    Iterator iterator = (Iterable)object9;
                    for (Object object11 : iterator) {
                        Object object12 = (String)object11.getClass().getMethod("getId", new Class[0]).invoke(object11, new Object[0]);
                        for (String string : list) {
                            if (!object12.equalsIgnoreCase(string)) continue;
                            bl2 = true;
                            break;
                        }
                        if (!bl2) continue;
                        break;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (bl || bl2) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                return;
            }
            int n2 = this.A.getInt("freezeDuration", 10);
            player2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, n2, 255, false, false, false));
            player2.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, n2, 128, false, false, false));
            this.A(player2, "freeze");
            this.A(player2, n2);
            object6 = this.A.getString("messages.shooter.title", "");
            object5 = this.A.getString("messages.shooter.subtitle", "");
            object4 = this.A.getString("messages.shooter.chatMessage", "");
            object6 = object6.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            object5 = object5.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            object4 = object4.replace((CharSequence)"{TARGET}", (CharSequence)player2.getName());
            if (!object6.isEmpty() || !object5.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object6), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object5), 10, 70, 20);
            }
            if (!object4.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object4));
            }
            object3 = this.A.getString("messages.target.title", "");
            object2 = this.A.getString("messages.target.subtitle", "");
            object = this.A.getString("messages.target.chatMessage", "");
            object3 = object3.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            object2 = object2.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            object = object.replace((CharSequence)"{SHOOTER}", (CharSequence)player.getName());
            if (!object3.isEmpty() || !object2.isEmpty()) {
                player2.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object3), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object2), 10, 70, 20);
            }
            if (!object.isEmpty()) {
                player2.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
            }
        }
    }

    private void A(final Player player, final int n2) {
        new BukkitRunnable(this){
            int ticks = 0;
            final /* synthetic */ IA this$0;
            {
                this.this$0 = iA;
            }

            public void run() {
                if (this.ticks >= n2 || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                if (this.ticks % 2 != 0) {
                    ++this.ticks;
                    return;
                }
                Location location = player.getLocation().add(0.0, 1.0, 0.0);
                player.getWorld().spawnParticle(Particle.SNOWFLAKE, location, 7, 0.4, 0.8, 0.4, 0.0);
                player.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 5, 0.3, 0.6, 0.3, 0.0, (Object)Material.PACKED_ICE.createBlockData());
                ++this.ticks;
            }
        }.runTaskTimer((Plugin)this.C, 0L, 1L);
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "SNOWBALL"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.SNOWBALL;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&b\u015anie\u017cynka"));
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
                int n2;
                String string2;
                String[] stringArray = object.split(":");
                if (stringArray.length < 2) continue;
                string2 = stringArray[0].trim().toUpperCase();
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    n2 = 1;
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

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.C.getDataFolder(), "items/sniezynka.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("sniezynka")) != null) {
                this.A = configurationSection;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_SNOWBALL_THROW");
        float f2 = (float)configurationSection.getDouble("volume", 1.0);
        float f3 = (float)configurationSection.getDouble("pitch", 1.0);
        try {
            Sound sound = Sound.valueOf((String)string2);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            player.playSound(player.getLocation(), string2, f2, f3);
        }
    }
}

