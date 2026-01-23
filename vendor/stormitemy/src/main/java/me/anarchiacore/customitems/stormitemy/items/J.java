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
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Container
 *  org.bukkit.block.data.BlockData
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EnderPearl
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockFadeEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class j
implements Listener {
    private final Plugin C;
    private ConfigurationSection A;
    private final Map<String, _A> D = new ConcurrentHashMap();
    private final Map<String, Map<Location, _B>> B = new ConcurrentHashMap();

    public j(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "wyrzutniahydroklatki.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Wyrzutnia Hydro Klatki' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Wyrzutnia Hydro Klatki'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}    - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}  - nazwa gracza\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.material", (Object)"BLAZE_ROD");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.name", (Object)"&3&lWyrzutnia Hydro Klatki");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.lore", (Object)List.of((Object)"&7", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty podczas", (Object)"&8 \u00bb &fwydarzenia wakacyjnego 2025&7!", (Object)"&7", (Object)"&8 \u00bb &7Po u\u017cyciu wystrzeliwuje pocisk, kt\u00f3ry", (Object)"&8 \u00bb &7tworzy &3wodn\u0105 klatk\u0119&7 uwi\u0119ziaj\u0105c\u0105", (Object)"&8 \u00bb &7przeciwnik\u00f3w w miejscu!", (Object)"&7"));
                yamlConfiguration.set("wyrzutnia_hydro_klatki.customModelData", (Object)2);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cooldown", (Object)60);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.enchantments", (Object)List.of((Object)"DURABILITY:3"));
                yamlConfiguration.set("wyrzutnia_hydro_klatki.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("wyrzutnia_hydro_klatki.unbreakable", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.radius", (Object)8);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.duration", (Object)15);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.wall_thickness", (Object)1);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.block_mappings.external_wall", (Object)"BLUE_GLAZED_TERRACOTTA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.block_mappings.leaves", (Object)"BLUE_TERRACOTTA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.block_mappings.wood", (Object)"BUBBLE_CORAL_BLOCK");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.block_mappings.default", (Object)"LIGHT_BLUE_TERRACOTTA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.projectile.speed", (Object)1.5);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.projectile.gravity", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.shoot.title", (Object)"&3&lWYRZUTNIA HYDRO KLATKI");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.shoot.subtitle", (Object)"&7Wystrzeliwujesz &bwodny pocisk&7!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.shoot.chatMessage", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_created.title", (Object)"&3&lHYDRO KLATKA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_created.subtitle", (Object)"&7Utworzono &bwodn\u0105 klatk\u0119&7!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_created.chatMessage", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_expired.title", (Object)"&3&lHYDRO KLATKA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_expired.subtitle", (Object)"&7Twoja klatka &bwygas\u0142a&7!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cage_expired.chatMessage", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cooldown", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage.title", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage.subtitle", (Object)"&cNie mo\u017cesz zniszczy\u0107 granicy &4podwodnej klatki&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage.chatMessage", (Object)"&cNie mo\u017cesz zniszczy\u0107 blok\u00f3w &4Hydro Klatki&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage_outside.title", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage_outside.subtitle", (Object)"&cNie mo\u017cesz niszczy\u0107 &4Hydro Klatki&c z zewn\u0105trz!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_break_cage_outside.chatMessage", (Object)"&cNie mo\u017cesz niszczy\u0107 hydroklatki z zewn\u0105trz!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.allow_elytra", (Object)false);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.elytra_blocked.title", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.elytra_blocked.subtitle", (Object)"&cNie mo\u017cesz lata\u0107 &4elytr\u0105&c w hydroklatce!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.elytra_blocked.chatMessage", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_use_nether", (Object)"&cNie mo\u017cesz u\u017cywa\u0107 Wyrzutni Hydro Klatki w &4Netherze&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_use_end", (Object)"&cNie mo\u017cesz u\u017cywa\u0107 Wyrzutni Hydro Klatki w &4Endzie&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.teleport_blocked", (Object)"&cNie mo\u017cna si\u0119 teleportowa\u0107 z &4Hydro Klatki&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.teleport_into_cage_blocked", (Object)"&cNie mo\u017cna si\u0119 teleportowa\u0107 do &4Hydro Klatki&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.chorus_fruit_blocked", (Object)"&cNie mo\u017cna u\u017cywa\u0107 &4Chorus Fruit&c w Hydro Klatce!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.dragon_sword_blocked", (Object)"&cNie mo\u017cna u\u017cywa\u0107 &4Smoczego Miecza&c w Hydro Klatce!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.no_teleport_subtitle", (Object)"&cNie mo\u017cna si\u0119 teleportowa\u0107!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.bossbar.title", (Object)"&bHydro Klatka");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.bossbar.color", (Object)"AQUA");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.bossbar.style", (Object)"SOLID");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.dimensions.allow_nether", (Object)false);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.dimensions.allow_end", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.allow_block_placing", (Object)false);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_place_block.title", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_place_block.subtitle", (Object)"&cNie mo\u017cesz stawia\u0107 blok\u00f3w w &4Hydro Klatce&c!");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.messages.cannot_place_block.chatMessage", (Object)"");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.cage.block_teleport", (Object)false);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.noDestroyRegions", (Object)List.of());
                yamlConfiguration.set("wyrzutnia_hydro_klatki.preventRegionDestruction", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.shoot.enabled", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.shoot.sound", (Object)"ENTITY_BLAZE_SHOOT");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.shoot.volume", (Object)1.0);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.shoot.pitch", (Object)0.8);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_create.enabled", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_create.sound", (Object)"BLOCK_CONDUIT_ACTIVATE");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_create.volume", (Object)1.0);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_create.pitch", (Object)1.0);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_expire.enabled", (Object)true);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_expire.sound", (Object)"BLOCK_CONDUIT_DEACTIVATE");
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_expire.volume", (Object)1.0);
                yamlConfiguration.set("wyrzutnia_hydro_klatki.sounds.cage_expire.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("wyrzutnia_hydro_klatki");
        this.A(file, yamlConfiguration);
    }

    private void A(File file, YamlConfiguration yamlConfiguration) {
        boolean bl = false;
        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("wyrzutnia_hydro_klatki");
        if (configurationSection != null) {
            if (!configurationSection.contains("cage.allow_elytra")) {
                configurationSection.set("cage.allow_elytra", (Object)false);
                bl = true;
            }
            if (!configurationSection.contains("messages.elytra_blocked.title")) {
                configurationSection.set("messages.elytra_blocked.title", (Object)"");
                bl = true;
            }
            if (!configurationSection.contains("messages.elytra_blocked.subtitle")) {
                configurationSection.set("messages.elytra_blocked.subtitle", (Object)"&cNie mo\u017cesz lata\u0107 &4elytr\u0105&c w hydroklatce!");
                bl = true;
            }
            if (!configurationSection.contains("messages.elytra_blocked.chatMessage")) {
                configurationSection.set("messages.elytra_blocked.chatMessage", (Object)"");
                bl = true;
            }
        }
        if (bl) {
            try {
                yamlConfiguration.save(file);
                YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration((File)file);
                this.A = yamlConfiguration2.getConfigurationSection("wyrzutnia_hydro_klatki");
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Object object;
        List list;
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("wyrzutniahydroklatki")) {
            return;
        }
        if (!playerInteractEvent.getAction().toString().contains((CharSequence)"RIGHT_CLICK")) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || itemStack.getType() != Material.BLAZE_ROD) {
            return;
        }
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&3&lWyrzutnia Hydro Klatki"));
        if (!itemStack.getItemMeta().getDisplayName().equals((Object)string)) {
            return;
        }
        playerInteractEvent.setCancelled(true);
        World.Environment environment = player.getWorld().getEnvironment();
        if (environment == World.Environment.NETHER && !this.A.getBoolean("dimensions.allow_nether", false)) {
            String string2 = this.A.getString("messages.cannot_use_nether", "&cNie mo\u017cesz u\u017cywa\u0107 Wyrzutni Hydro Klatki w Netherze!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            return;
        }
        if (environment == World.Environment.THE_END && !this.A.getBoolean("dimensions.allow_end", false)) {
            String string3 = this.A.getString("messages.cannot_use_end", "&cNie mo\u017cesz u\u017cywa\u0107 Wyrzutni Hydro Klatki w Endzie!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            return;
        }
        try {
            Main main;
            if (this.C instanceof Main && (main = (Main)this.C).isPlayerInBlockedRegion(player)) {
                me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                return;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        int n2 = this.A.getInt("cooldown", 45);
        if (player.hasMetadata("wyrzutnia_hydro_klatki_cooldown") && !(list = player.getMetadata("wyrzutnia_hydro_klatki_cooldown")).isEmpty()) {
            long l2 = ((MetadataValue)list.get(0)).asLong();
            if (System.currentTimeMillis() < l2) {
                long l3 = (l2 - System.currentTimeMillis()) / 1000L;
                String string4 = this.A.getString("messages.cooldown", "");
                if (!string4.isEmpty()) {
                    string4 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l3)));
                    player.sendMessage(string4);
                }
                return;
            }
            player.removeMetadata("wyrzutnia_hydro_klatki_cooldown", (Plugin)this.C);
        }
        long l4 = System.currentTimeMillis() + (long)n2 * 1000L;
        player.setMetadata("wyrzutnia_hydro_klatki_cooldown", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)l4));
        try {
            if (this.C instanceof Main && ((Main)((Object)(object = (Main)this.C))).getActionbarManager() != null && ((Main)((Object)object)).getActionbarManager().isEnabled()) {
                ((Main)((Object)object)).getActionbarManager().registerCooldown(player, "wyrzutniahydroklatki", n2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.C(player);
        object = this.A.getString("messages.shoot.title", "");
        String string5 = this.A.getString("messages.shoot.subtitle", "");
        String string6 = this.A.getString("messages.shoot.chatMessage", "");
        if (!object.isEmpty() || !string5.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5), 10, 70, 20);
        }
        if (!string6.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6));
        }
    }

    private void C(Player player) {
        Fireball fireball = (Fireball)player.launchProjectile(Fireball.class);
        double d2 = this.A.getDouble("projectile.speed", 1.5);
        Vector vector = player.getLocation().getDirection().multiply(d2);
        fireball.setVelocity(vector);
        fireball.setYield(0.0f);
        fireball.setIsIncendiary(false);
        fireball.setMetadata("hydro_cage_projectile", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
        fireball.setMetadata("hydro_cage_shooter", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)player.getUniqueId().toString()));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        try {
            player.getWorld().spawnParticle(Particle.valueOf((String)"WATER_SPLASH"), player.getLocation().add(0.0, 1.0, 0.0), 10, 0.3, 0.3, 0.3, 0.1);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            player.getWorld().spawnParticle(Particle.valueOf((String)"SPLASH"), player.getLocation().add(0.0, 1.0, 0.0), 10, 0.3, 0.3, 0.3, 0.1);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        if (!(projectileHitEvent.getEntity() instanceof Fireball)) {
            return;
        }
        Fireball fireball = (Fireball)projectileHitEvent.getEntity();
        if (!fireball.hasMetadata("hydro_cage_projectile")) {
            return;
        }
        if (!fireball.hasMetadata("hydro_cage_shooter")) {
            return;
        }
        String string = ((MetadataValue)fireball.getMetadata("hydro_cage_shooter").get(0)).asString();
        Player player = Bukkit.getPlayer((UUID)UUID.fromString((String)string));
        if (player == null) {
            return;
        }
        Location location = fireball.getLocation();
        int n2 = this.A.getInt("cage.radius", 12);
        for (_A _A2 : this.D.values()) {
            double d2;
            double d3 = _A2.B.distance(location);
            if (!(d3 < (d2 = (double)(_A2.A + n2)))) continue;
            String string3 = this.A.getString("messages.cage_collision", "&cNie mo\u017cna utworzy\u0107 hydroklatki - koliduje z istniej\u0105c\u0105 klatk\u0105!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            return;
        }
        try {
            if (this.C instanceof Main) {
                String string4;
                Main main = (Main)this.C;
                boolean bl = this.A.getBoolean("preventRegionDestruction", true);
                List list = this.A.getStringList("noDestroyRegions");
                if (bl && (string4 = main.getRegionChecker().C(location)) != null && list.stream().anyMatch(string2 -> string2.equalsIgnoreCase(string4))) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie mo\u017cesz u\u017cy\u0107 tego przedmiotu w regionie &f" + string4 + "&c!"));
                    return;
                }
                if (main.isTargetBlockInProtectedRegion(location)) {
                    string4 = this.A.getString("messages.region_blocked", "");
                    if (string4.isEmpty()) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.B(player);
                    } else {
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4));
                    }
                    return;
                }
                for (int i2 = -n2; i2 <= n2; i2 += n2 / 2) {
                    for (int i3 = -n2; i3 <= n2; i3 += n2 / 2) {
                        for (int i4 = -n2; i4 <= n2; i4 += n2 / 2) {
                            String string5;
                            double d4 = Math.sqrt((double)(i2 * i2 + i3 * i3 + i4 * i4));
                            if (!(d4 <= (double)n2)) continue;
                            Location location2 = location.clone().add((double)i2, (double)i3, (double)i4);
                            if (bl && (string5 = main.getRegionChecker().C(location2)) != null && list.stream().anyMatch(string2 -> string2.equalsIgnoreCase(string5))) {
                                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cKlatka nachodzi\u0142aby na chroniony region &f" + string5 + "&c!"));
                                return;
                            }
                            if (!main.isTargetBlockInProtectedRegion(location2)) continue;
                            string5 = this.A.getString("messages.region_blocked", "");
                            if (string5.isEmpty()) {
                                me.anarchiacore.customitems.stormitemy.utils.language.A.B(player);
                            } else {
                                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5));
                            }
                            String string6 = this.A.getString("messages.cage_overlaps_region", "&7(Klatka nachodzi\u0142aby na chroniony region)");
                            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6));
                            return;
                        }
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        projectileHitEvent.setCancelled(true);
        fireball.remove();
        this.A(player, location);
    }

    private void A(Player player, Location location) {
        Object object4;
        double d2;
        Object object22;
        BarStyle barStyle;
        BarColor barColor;
        int n2 = this.A.getInt("cage.radius", 6);
        int n3 = this.A.getInt("cage.duration", 15);
        String string = UUID.randomUUID().toString();
        HashMap hashMap = new HashMap();
        this.A(location, n2, (Map<Location, _B>)hashMap, string);
        this.B.put((Object)string, (Object)hashMap);
        long l2 = hashMap.values().stream().filter(_B2 -> _B2.A() == Material.BLUE_GLAZED_TERRACOTTA).count();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("bossbar.title", "&bHydro Klatka"));
        try {
            barColor = BarColor.valueOf((String)this.A.getString("bossbar.color", "AQUA"));
        }
        catch (Exception exception) {
            barColor = BarColor.BLUE;
        }
        try {
            barStyle = BarStyle.valueOf((String)this.A.getString("bossbar.style", "SOLID"));
        }
        catch (Exception exception) {
            barStyle = BarStyle.SOLID;
        }
        BossBar bossBar = Bukkit.createBossBar((String)string2, (BarColor)barColor, (BarStyle)barStyle, (BarFlag[])new BarFlag[0]);
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
        int n4 = 0;
        for (Object object22 : location.getWorld().getNearbyEntities(location, (double)n2, (double)n2, (double)n2)) {
            if (!(object22 instanceof Player) || !((d2 = (object4 = (Player)object22).getLocation().distance(location)) <= (double)n2)) continue;
            this.A((Player)object4);
            bossBar.addPlayer(object4);
            ++n4;
        }
        Object object3 = new _A(string, location, n2, n3, bossBar, player);
        this.D.put((Object)string, object3);
        for (Object object4 : location.getWorld().getNearbyEntities(location, (double)n2, (double)n2, (double)n2)) {
            if (!(object4 instanceof Item) || !((d2 = object4.getLocation().distance(location)) <= (double)n2)) continue;
            object4.remove();
        }
        location.getWorld().playSound(location, Sound.BLOCK_WATER_AMBIENT, 2.0f, 1.0f);
        location.getWorld().playSound(location, Sound.ENTITY_PLAYER_SPLASH, 2.0f, 0.8f);
        try {
            location.getWorld().spawnParticle(Particle.valueOf((String)"WATER_SPLASH"), location, 100, (double)n2, (double)n2 / 2.0, (double)n2, 0.1);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            location.getWorld().spawnParticle(Particle.valueOf((String)"SPLASH"), location, 100, (double)n2, (double)n2 / 2.0, (double)n2, 0.1);
        }
        location.getWorld().spawnParticle(Particle.BUBBLE_POP, location, 50, (double)n2, (double)n2 / 2.0, (double)n2, 0.05);
        object22 = this.A.getString("messages.cage_created.title", "");
        object4 = this.A.getString("messages.cage_created.subtitle", "");
        String string3 = this.A.getString("messages.cage_created.chatMessage", "");
        if (!object22.isEmpty() || !object4.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object22), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object4), 10, 70, 20);
        }
        if (!string3.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
        }
        this.A((_A)object3);
    }

    private void A(Location location, int n3, Map<Location, _B> map, String string) {
        int n4;
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        int n5 = this.A.getInt("cage.wall_thickness", 1);
        HashMap hashMap = new HashMap();
        for (n4 = -n3; n4 <= n3; ++n4) {
            for (int i2 = -n3; i2 <= n3; ++i2) {
                for (int i3 = -n3; i3 <= n3; ++i3) {
                    boolean bl;
                    String string2;
                    double d2 = Math.sqrt((double)(n4 * n4 + i2 * i2 + i3 * i3));
                    if (!(d2 <= (double)n3)) continue;
                    Location location2 = location.clone().add((double)n4, (double)i2, (double)i3);
                    Material material = location2.getBlock().getType();
                    if (location2.getBlock().hasMetadata("hydro_cage_block") && !(string2 = ((MetadataValue)location2.getBlock().getMetadata("hydro_cage_block").get(0)).asString()).equals((Object)string)) continue;
                    boolean bl2 = false;
                    if (d2 > (double)(n3 - n5)) {
                        if (this.A(material)) continue;
                        string2 = Material.valueOf((String)this.A.getString("cage.block_mappings.external_wall", "BLUE_GLAZED_TERRACOTTA"));
                        bl2 = true;
                    } else {
                        if (material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR || (string2 = this.B(material)) == material) continue;
                        bl2 = true;
                    }
                    boolean bl3 = bl = d2 > (double)(n3 - n5);
                    if (!bl2 && !bl) continue;
                    BlockData blockData = location2.getBlock().getBlockData();
                    BlockData blockData2 = blockData != null ? blockData.clone() : null;
                    BlockState blockState = location2.getBlock().getState();
                    _B _B2 = new _B(material, blockData2, blockState);
                    map.put((Object)location2.clone(), (Object)_B2);
                    int n6 = i2 + n3;
                    ((List)hashMap.computeIfAbsent((Object)n6, n2 -> new ArrayList())).add((Object)new _C(location2.clone(), material, (Material)string2, string));
                }
            }
        }
        n4 = n3 * 2;
        new BukkitRunnable(this, (Map)hashMap, world, location){
            int currentLayer;
            final /* synthetic */ Map val$layerBlocks;
            final /* synthetic */ World val$world;
            final /* synthetic */ Location val$center;
            final /* synthetic */ j this$0;
            {
                this.val$layerBlocks = map;
                this.val$world = world;
                this.val$center = location;
                this.this$0 = j2;
                this.currentLayer = n4;
            }

            public void run() {
                if (this.currentLayer < 0) {
                    this.cancel();
                    return;
                }
                List list = (List)this.val$layerBlocks.get((Object)this.currentLayer);
                if (list != null) {
                    for (_C _C2 : list) {
                        if (_C2.D != _C2.C) {
                            _C2.A.getBlock().setType(_C2.C, false);
                        }
                        _C2.A.getBlock().setMetadata("hydro_cage_block", (MetadataValue)new FixedMetadataValue((Plugin)this.this$0.C, (Object)_C2.B));
                    }
                    if (this.currentLayer % 3 == 0) {
                        this.val$world.playSound(this.val$center, Sound.BLOCK_WATER_AMBIENT, 0.3f, 1.2f + (float)(n4 - this.currentLayer) * 0.02f);
                    }
                }
                --this.currentLayer;
            }
        }.runTaskTimer((Plugin)this.C, 0L, 1L);
    }

    private boolean A(Material material) {
        return material == Material.BEDROCK || material == Material.BARRIER || material == Material.COMMAND_BLOCK || material == Material.CHAIN_COMMAND_BLOCK || material == Material.REPEATING_COMMAND_BLOCK || material == Material.SPAWNER || material == Material.END_PORTAL || material == Material.END_PORTAL_FRAME || material == Material.NETHER_PORTAL || material == Material.END_GATEWAY || material == Material.STRUCTURE_VOID || material == Material.STRUCTURE_BLOCK || material == Material.JIGSAW || material == Material.LIGHT || material.name().contains((CharSequence)"PORTAL") || material.name().contains((CharSequence)"COMMAND_BLOCK") || material == Material.MOVING_PISTON || material.name().equals((Object)"BEDROCK") || material.name().contains((CharSequence)"SHULKER_BOX") || material.getHardness() < 0.0f;
    }

    private Material B(Material material) {
        String string = material.name();
        if (this.A(material)) {
            return material;
        }
        if (string.equals((Object)"GRASS") || string.equals((Object)"TALL_GRASS") || string.equals((Object)"FERN") || string.equals((Object)"LARGE_FERN") || string.equals((Object)"DEAD_BUSH") || string.equals((Object)"VINE") || string.contains((CharSequence)"FLOWER") || string.contains((CharSequence)"ROSE") || string.contains((CharSequence)"TULIP") || string.contains((CharSequence)"ORCHID") || string.contains((CharSequence)"ALLIUM") || string.contains((CharSequence)"BLUET") || string.contains((CharSequence)"OXEYE") || string.contains((CharSequence)"CORNFLOWER") || string.contains((CharSequence)"LILY") || string.contains((CharSequence)"WITHER_ROSE") || string.contains((CharSequence)"SUNFLOWER") || string.contains((CharSequence)"LILAC") || string.contains((CharSequence)"PEONY") || string.contains((CharSequence)"ROSE_BUSH") || string.contains((CharSequence)"MUSHROOM") && !string.contains((CharSequence)"BLOCK") || string.contains((CharSequence)"SAPLING") || string.contains((CharSequence)"KELP") || string.contains((CharSequence)"SEAGRASS") || string.contains((CharSequence)"SEA_PICKLE") || material == Material.DANDELION || material == Material.POPPY || material == Material.BLUE_ORCHID || material == Material.ALLIUM || material == Material.AZURE_BLUET || material == Material.RED_TULIP || material == Material.ORANGE_TULIP || material == Material.WHITE_TULIP || material == Material.PINK_TULIP || material == Material.OXEYE_DAISY || material == Material.CORNFLOWER || material == Material.LILY_OF_THE_VALLEY || material == Material.WITHER_ROSE) {
            return Material.AIR;
        }
        String string2 = this.A.getString("cage.block_mappings." + string, null);
        if (string2 != null) {
            try {
                return Material.valueOf((String)string2);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        if (string.contains((CharSequence)"LEAVES")) {
            return Material.valueOf((String)this.A.getString("cage.block_mappings.leaves", "BLUE_TERRACOTTA"));
        }
        if (string.contains((CharSequence)"LOG") || string.contains((CharSequence)"WOOD") || string.contains((CharSequence)"PLANKS")) {
            return Material.valueOf((String)this.A.getString("cage.block_mappings.wood", "BUBBLE_CORAL_BLOCK"));
        }
        return Material.valueOf((String)this.A.getString("cage.block_mappings.default", "LIGHT_BLUE_TERRACOTTA"));
    }

    private void A(final _A _A2) {
        BukkitRunnable bukkitRunnable;
        _A2.F = bukkitRunnable = new BukkitRunnable(this){
            int timeLeft;
            final /* synthetic */ j this$0;
            {
                this.this$0 = j2;
                this.timeLeft = _A2.E;
            }

            public void run() {
                if (this.timeLeft <= 0) {
                    this.cancel();
                    Bukkit.getScheduler().runTask((Plugin)this.this$0.C, () -> this.this$0.C(_A2));
                    return;
                }
                double d2 = (double)this.timeLeft / (double)_A2.E;
                _A2.G.setProgress(Math.max((double)0.0, (double)d2));
                if (this.timeLeft <= 3) {
                    _A2.B.getWorld().playSound(_A2.B, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f + (float)(3 - this.timeLeft) * 0.2f);
                }
                --this.timeLeft;
            }
        };
        bukkitRunnable.runTaskTimer((Plugin)this.C, 0L, 20L);
    }

    private void C(_A _A2) {
        if (_A2.F != null && !_A2.F.isCancelled()) {
            _A2.F.cancel();
        }
        _A2.G.removeAll();
        Map map = (Map)this.B.get((Object)_A2.D);
        if (map != null) {
            long l2 = map.values().stream().filter(_B2 -> _B2.A() == Material.BLUE_GLAZED_TERRACOTTA).count();
        }
        if (map != null) {
            int n2;
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            for (Map.Entry entry : map.entrySet()) {
                String string;
                Location location = (Location)entry.getKey();
                _B _B3 = (_B)entry.getValue();
                if (location.getBlock().hasMetadata("destroyed_in_cage")) {
                    location.getBlock().removeMetadata("destroyed_in_cage", (Plugin)this.C);
                    location.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
                    continue;
                }
                if (location.getBlock().getType() == Material.BLUE_GLAZED_TERRACOTTA) {
                    // empty if block
                }
                n2 = 0;
                if (location.getBlock().hasMetadata("hydro_cage_block")) {
                    string = ((MetadataValue)location.getBlock().getMetadata("hydro_cage_block").get(0)).asString();
                    if (string.equals((Object)_A2.D)) {
                        n2 = 1;
                    } else {
                        ++n5;
                        boolean bl = this.D.containsKey((Object)string);
                        if (bl) continue;
                        n2 = 0;
                    }
                } else {
                    ++n4;
                }
                try {
                    location.getBlock().setType(_B3.A(), false);
                    if (_B3.B() != null) {
                        location.getBlock().setBlockData(_B3.B(), false);
                    }
                    if (_B3.C() != null && (string = location.getBlock().getState()) instanceof Container) {
                        Container container = (Container)string;
                        container.getSnapshotInventory().setContents(_B3.C());
                        string.update(true, false);
                    }
                }
                catch (Exception exception) {
                    try {
                        location.getBlock().setType(_B3.A(), false);
                        if (_B3.B() != null) {
                            location.getBlock().setBlockData(_B3.B(), false);
                        }
                    }
                    catch (Exception exception2) {
                        // empty catch block
                    }
                }
                if (n2 != 0) {
                    location.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
                }
                try {
                    location.getBlock().getState().update(true, false);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                string = location.getWorld().getPlayers().iterator();
                while (string.hasNext()) {
                    Player player = (Player)string.next();
                    if (!(player.getLocation().distance(location) <= 100.0)) continue;
                    player.sendBlockChange(location, location.getBlock().getBlockData());
                    if (_B3.A() != Material.BLUE_GLAZED_TERRACOTTA && location.getBlock().getType() != Material.BLUE_GLAZED_TERRACOTTA) continue;
                    this.C.getServer().getScheduler().runTaskLater((Plugin)this.C, () -> player.sendBlockChange(location, location.getBlock().getBlockData()), 1L);
                }
                ++n3;
            }
            Iterator iterator = _A2.B.getWorld();
            int n6 = _A2.B.getChunk().getX();
            int n7 = _A2.B.getChunk().getZ();
            int n8 = _A2.A / 16 + 2;
            for (n2 = n6 - n8; n2 <= n6 + n8; ++n2) {
                for (int i2 = n7 - n8; i2 <= n7 + n8; ++i2) {
                    if (!iterator.isChunkLoaded(n2, i2)) continue;
                    iterator.refreshChunk(n2, i2);
                }
            }
            this.B(_A2);
        }
        this.D.remove((Object)_A2.D);
        this.B.remove((Object)_A2.D);
        _A2.B.getWorld().playSound(_A2.B, Sound.BLOCK_WATER_AMBIENT, 1.0f, 0.6f);
        try {
            _A2.B.getWorld().spawnParticle(Particle.valueOf((String)"WATER_SPLASH"), _A2.B, 50, (double)_A2.A, (double)_A2.A / 2.0, (double)_A2.A, 0.1);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            _A2.B.getWorld().spawnParticle(Particle.valueOf((String)"SPLASH"), _A2.B, 50, (double)_A2.A, (double)_A2.A / 2.0, (double)_A2.A, 0.1);
        }
        if (_A2.C != null && _A2.C.isOnline()) {
            String string = this.A.getString("messages.cage_expired.title", "");
            String string2 = this.A.getString("messages.cage_expired.subtitle", "");
            String string3 = this.A.getString("messages.cage_expired.chatMessage", "");
            if (!string.isEmpty() || !string2.isEmpty()) {
                _A2.C.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 50, 20);
            }
            if (!string3.isEmpty()) {
                _A2.C.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Object object;
        Object object2;
        Object object32;
        boolean bl;
        boolean bl2;
        Player player = blockBreakEvent.getPlayer();
        Location location = blockBreakEvent.getBlock().getLocation();
        Material material = blockBreakEvent.getBlock().getType();
        boolean bl3 = this.B(player);
        boolean bl4 = bl2 = material == Material.BLUE_GLAZED_TERRACOTTA || material == Material.BLUE_TERRACOTTA || material == Material.BUBBLE_CORAL_BLOCK || material == Material.LIGHT_BLUE_TERRACOTTA;
        if (bl3 && bl2) {
            if (material != Material.BLUE_GLAZED_TERRACOTTA) {
                blockBreakEvent.setDropItems(false);
                blockBreakEvent.getBlock().setMetadata("destroyed_in_cage", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
                return;
            }
            blockBreakEvent.setCancelled(true);
            String string = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.title", "");
            String string2 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.subtitle", "");
            String string3 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.chatMessage", "");
            if (!string.isEmpty() || !string2.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 50, 20);
            }
            if (!string3.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
            return;
        }
        if (!bl3 && (bl2 || blockBreakEvent.getBlock().hasMetadata("hydro_cage_block"))) {
            bl = false;
            for (Object object32 : this.D.values()) {
                object2 = (Map)this.B.get((Object)((_A)object32).D);
                if (object2 == null || !object2.containsKey((Object)location)) continue;
                bl = true;
                break;
            }
            if (bl || blockBreakEvent.getBlock().hasMetadata("hydro_cage_block")) {
                blockBreakEvent.setCancelled(true);
                object = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.title", "");
                object32 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.subtitle", "");
                object2 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.chatMessage", "");
                if (!object.isEmpty() || !object32.isEmpty()) {
                    player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object32), 10, 50, 20);
                }
                if (!object2.isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object2));
                }
                return;
            }
        }
        if (blockBreakEvent.getBlock().hasMetadata("hydro_cage_block")) {
            bl = false;
            for (Object object32 : this.D.values()) {
                object2 = (Map)this.B.get((Object)((_A)object32).D);
                if (object2 == null || !object2.containsKey((Object)location)) continue;
                bl = true;
                break;
            }
            if (bl) {
                blockBreakEvent.setCancelled(true);
                object = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.title", "");
                object32 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.subtitle", "");
                object2 = this.A.getString("wyrzutnia_hydro_klatki.messages.cannot_break_cage.chatMessage", "");
                if (!object.isEmpty() || !object32.isEmpty()) {
                    player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object32), 10, 50, 20);
                }
                if (!object2.isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object2));
                }
            } else {
                blockBreakEvent.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
            }
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent blockFadeEvent) {
        Material material;
        if (blockFadeEvent.getBlock().hasMetadata("hydro_cage_block") && ((material = blockFadeEvent.getBlock().getType()) == Material.BUBBLE_CORAL_BLOCK || material.name().contains((CharSequence)"CORAL"))) {
            blockFadeEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        Location location = blockPlaceEvent.getBlock().getLocation();
        boolean bl = this.B(player);
        if (bl) {
            boolean bl2 = this.A.getBoolean("cage.allow_block_placing", false);
            if (!bl2) {
                blockPlaceEvent.setCancelled(true);
                String string = this.A.getString("messages.cannot_place_block.title", "");
                String string2 = this.A.getString("messages.cannot_place_block.subtitle", "&cNie mo\u017cesz stawia\u0107 blok\u00f3w w &4Hydro Klatce&c!");
                String string3 = this.A.getString("messages.cannot_place_block.chatMessage", "");
                if (!string.isEmpty() || !string2.isEmpty()) {
                    player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 30, 10);
                }
                if (!string3.isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
                }
                return;
            }
            blockPlaceEvent.getBlock().setMetadata("player_placed_in_cage", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)player.getUniqueId().toString()));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        boolean bl;
        Player player = playerMoveEvent.getPlayer();
        Location location = playerMoveEvent.getFrom();
        Location location2 = playerMoveEvent.getTo();
        if (location2 == null || location.getWorld() != location2.getWorld()) {
            return;
        }
        boolean bl2 = this.B(player);
        if (bl2 && player.isGliding() && !(bl = this.A.getBoolean("cage.allow_elytra", false))) {
            player.setGliding(false);
            Object object = this.A.getString("messages.elytra_blocked.title", "");
            String string = this.A.getString("messages.elytra_blocked.subtitle", "&cNie mo\u017cesz lata\u0107 elytr\u0105 w hydroklatce!");
            String string2 = this.A.getString("messages.elytra_blocked.chatMessage", "");
            if (!object.isEmpty() || !string.isEmpty()) {
                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), 10, 30, 10);
            }
            if (!string2.isEmpty()) {
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
        }
        for (Object object : this.D.values()) {
            if (((_A)object).B.getWorld() != location2.getWorld()) continue;
            double d2 = location.distance(((_A)object).B);
            double d3 = location2.distance(((_A)object).B);
            if (d2 > (double)((_A)object).A && d3 <= (double)((_A)object).A) {
                if (((_A)object).G == null || ((_A)object).G.getPlayers().contains((Object)player)) continue;
                ((_A)object).G.addPlayer(player);
                if (!this.A.getBoolean("cage.block_teleport", false)) continue;
                String string = this.A.getString("messages.no_teleport_subtitle", "&cNie mo\u017cna si\u0119 teleportowa\u0107!");
                player.sendTitle("", me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), 10, 40, 10);
                continue;
            }
            if (!(d2 <= (double)((_A)object).A) || !(d3 > (double)((_A)object).A) || ((_A)object).G == null || !((_A)object).G.getPlayers().contains((Object)player)) continue;
            ((_A)object).G.removePlayer(player);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        List list = entityExplodeEvent.blockList();
        list.removeIf(block -> {
            Location location = block.getLocation();
            for (_A _A2 : this.D.values()) {
                double d2;
                if (_A2.B.getWorld() != location.getWorld() || !((d2 = location.distance(_A2.B)) <= (double)_A2.A)) continue;
                return true;
            }
            return false;
        });
    }

    private boolean B(Player player) {
        Location location = player.getLocation();
        int n2 = this.A.getInt("cage.wall_thickness", 1);
        for (_A _A2 : this.D.values()) {
            double d2;
            if (_A2.B.getWorld() != location.getWorld() || !((d2 = location.distance(_A2.B)) < (double)(_A2.A - n2))) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onEnderPearlTeleport(PlayerTeleportEvent playerTeleportEvent) {
        if (!this.A.getBoolean("cage.block_teleport", false)) {
            return;
        }
        if (playerTeleportEvent.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }
        Player player = playerTeleportEvent.getPlayer();
        if (this.B(player)) {
            playerTeleportEvent.setCancelled(true);
            String string = this.A.getString("messages.teleport_blocked", "&cNie mo\u017cesz si\u0119 teleportowa\u0107 b\u0119d\u0105c w hydroklatce!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            return;
        }
        Location location = playerTeleportEvent.getTo();
        if (location != null) {
            for (_A _A2 : this.D.values()) {
                double d2;
                if (_A2.B.getWorld() != location.getWorld() || !((d2 = location.distance(_A2.B)) <= (double)_A2.A)) continue;
                playerTeleportEvent.setCancelled(true);
                String string = this.A.getString("messages.teleport_into_cage_blocked", "&cNie mo\u017cesz si\u0119 teleportowa\u0107 do hydroklatki!");
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                return;
            }
        }
    }

    @EventHandler
    public void onChorusFruitTeleport(PlayerTeleportEvent playerTeleportEvent) {
        if (!this.A.getBoolean("cage.block_teleport", false)) {
            return;
        }
        if (playerTeleportEvent.getCause() != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
            return;
        }
        Player player = playerTeleportEvent.getPlayer();
        if (this.B(player)) {
            playerTeleportEvent.setCancelled(true);
            String string = this.A.getString("messages.chorus_fruit_blocked", "&cNie mo\u017cesz u\u017cywa\u0107 chorus fruit b\u0119d\u0105c w hydroklatce!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            return;
        }
        Location location = playerTeleportEvent.getTo();
        if (location != null) {
            for (_A _A2 : this.D.values()) {
                double d2;
                if (_A2.B.getWorld() != location.getWorld() || !((d2 = location.distance(_A2.B)) <= (double)_A2.A)) continue;
                playerTeleportEvent.setCancelled(true);
                String string = this.A.getString("messages.teleport_into_cage_blocked", "&cNie mo\u017cesz si\u0119 teleportowa\u0107 do hydroklatki!");
                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                return;
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!this.A.getBoolean("cage.block_teleport", false)) {
            return;
        }
        if (!(projectileLaunchEvent.getEntity() instanceof EnderPearl)) {
            return;
        }
        if (!(projectileLaunchEvent.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectileLaunchEvent.getEntity().getShooter();
        EnderPearl enderPearl = (EnderPearl)projectileLaunchEvent.getEntity();
        if (!enderPearl.hasMetadata("SmoczaMieczPearl")) {
            return;
        }
        if (this.B(player)) {
            projectileLaunchEvent.setCancelled(true);
            String string = this.A.getString("messages.dragon_sword_blocked", "&cNie mo\u017cesz u\u017cywa\u0107 smoczego miecza b\u0119d\u0105c w hydroklatce!");
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
        }
    }

    private void A(Player player) {
        for (_A _A2 : this.D.values()) {
            if (_A2.G == null) continue;
            _A2.G.removePlayer(player);
        }
    }

    public void cleanupAllCages() {
        for (_A _A2 : this.D.values()) {
            Map map;
            if (_A2.F != null && !_A2.F.isCancelled()) {
                _A2.F.cancel();
            }
            if (_A2.G != null) {
                _A2.G.removeAll();
            }
            if ((map = (Map)this.B.get((Object)_A2.D)) == null) continue;
            for (Map.Entry entry : map.entrySet()) {
                Location location;
                block8: {
                    location = (Location)entry.getKey();
                    _B _B2 = (_B)entry.getValue();
                    if (!location.getBlock().hasMetadata("hydro_cage_block")) continue;
                    try {
                        BlockState blockState;
                        location.getBlock().setType(_B2.A(), false);
                        if (_B2.B() != null) {
                            location.getBlock().setBlockData(_B2.B(), false);
                        }
                        if (_B2.C() != null && (blockState = location.getBlock().getState()) instanceof Container) {
                            Container container = (Container)blockState;
                            container.getSnapshotInventory().setContents(_B2.C());
                            blockState.update(true, false);
                        }
                    }
                    catch (Exception exception) {
                        location.getBlock().setType(_B2.A(), false);
                        if (_B2.B() == null) break block8;
                        location.getBlock().setBlockData(_B2.B(), false);
                    }
                }
                location.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
            }
        }
        this.D.clear();
        this.B.clear();
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "BLAZE_ROD"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BLAZE_ROD;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&3&lWyrzutnia Hydro Klatki"));
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

    private void B(_A _A2) {
        int n2 = 0;
        Location location = _A2.B;
        int n3 = _A2.A;
        Map map = (Map)this.B.get((Object)_A2.D);
        for (int i2 = -n3; i2 <= n3; ++i2) {
            for (int i3 = -n3; i3 <= n3; ++i3) {
                for (int i4 = -n3; i4 <= n3; ++i4) {
                    String string;
                    Location location2 = location.clone().add((double)i2, (double)i3, (double)i4);
                    double d2 = location.distance(location2);
                    if (!(d2 <= (double)n3) || location2.getBlock().getType() != Material.BLUE_GLAZED_TERRACOTTA || !location2.getBlock().hasMetadata("hydro_cage_block") || !(string = ((MetadataValue)location2.getBlock().getMetadata("hydro_cage_block").get(0)).asString()).equals((Object)_A2.D)) continue;
                    try {
                        Container container;
                        BlockState blockState;
                        _B _B2 = this.A(location2, (Map<Location, _B>)map);
                        if (_B2 != null) {
                            location2.getBlock().setType(_B2.A(), false);
                            if (_B2.B() != null) {
                                location2.getBlock().setBlockData(_B2.B(), false);
                            }
                            if (_B2.C() != null && (blockState = location2.getBlock().getState()) instanceof Container) {
                                container = (Container)blockState;
                                container.getSnapshotInventory().setContents(_B2.C());
                                blockState.update(true, false);
                            }
                        } else {
                            location2.getBlock().setType(Material.AIR, false);
                        }
                        location2.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
                        blockState = location2.getWorld().getPlayers().iterator();
                        while (blockState.hasNext()) {
                            container = (Player)blockState.next();
                            if (!(container.getLocation().distance(location2) <= 100.0)) continue;
                            container.sendBlockChange(location2, location2.getBlock().getBlockData());
                        }
                        ++n2;
                        continue;
                    }
                    catch (Exception exception) {
                        location2.getBlock().setType(Material.AIR, false);
                        location2.getBlock().removeMetadata("hydro_cage_block", (Plugin)this.C);
                    }
                }
            }
        }
    }

    private _B A(Location location, Map<Location, _B> map) {
        if (map == null) {
            return null;
        }
        for (Map.Entry entry : map.entrySet()) {
            Location location2 = (Location)entry.getKey();
            if (location2.getWorld() == null || location.getWorld() == null || !location2.getWorld().equals((Object)location.getWorld()) || location2.getBlockX() != location.getBlockX() || location2.getBlockY() != location.getBlockY() || location2.getBlockZ() != location.getBlockZ()) continue;
            return (_B)entry.getValue();
        }
        return null;
    }

    public void reload() {
        try {
            this.cleanupAllCages();
            File file = new File(this.C.getDataFolder(), "items");
            File file2 = new File(file, "wyrzutniahydroklatki.yml");
            if (file2.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
                this.A(file2, yamlConfiguration);
                ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("wyrzutnia_hydro_klatki");
                if (configurationSection != null) {
                    this.A = configurationSection;
                }
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
        String string2 = configurationSection.getString("sound", "ENTITY_BLAZE_SHOOT");
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

    private static class _A {
        public final String D;
        public final Location B;
        public final int A;
        public final int E;
        public final BossBar G;
        public final Player C;
        public BukkitRunnable F;

        public _A(String string, Location location, int n2, int n3, BossBar bossBar, Player player) {
            this.D = string;
            this.B = location;
            this.A = n2;
            this.E = n3;
            this.G = bossBar;
            this.C = player;
            this.F = null;
        }
    }

    private static class _B {
        public final Material D;
        public final BlockData B;
        public final BlockState C;
        public final ItemStack[] A;

        public _B(Material material, BlockData blockData, BlockState blockState) {
            this.D = material;
            this.B = blockData;
            this.C = blockState;
            ItemStack[] itemStackArray = null;
            if (blockState instanceof Container) {
                try {
                    Container container = (Container)blockState;
                    ItemStack[] itemStackArray2 = container.getSnapshotInventory().getContents();
                    itemStackArray = new ItemStack[itemStackArray2.length];
                    for (int i2 = 0; i2 < itemStackArray2.length; ++i2) {
                        if (itemStackArray2[i2] == null) continue;
                        itemStackArray[i2] = itemStackArray2[i2].clone();
                    }
                }
                catch (Exception exception) {
                    itemStackArray = null;
                }
            }
            this.A = itemStackArray;
        }

        public Material A() {
            return this.D;
        }

        public BlockData B() {
            return this.B;
        }

        public BlockState D() {
            return this.C;
        }

        public ItemStack[] C() {
            return this.A;
        }
    }

    private static class _C {
        public final Location A;
        public final Material D;
        public final Material C;
        public final String B;

        public _C(Location location, Material material, Material material2, String string) {
            this.A = location;
            this.D = material;
            this.C = material2;
            this.B = string;
        }
    }
}

