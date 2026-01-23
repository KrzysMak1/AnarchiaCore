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
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.Set
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockDamageEvent
 *  org.bukkit.event.block.BlockExplodeEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.ItemSpawnEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class m
implements Listener {
    private final Main F;
    private ConfigurationSection C;
    private final Map<Location, _A> E = new ConcurrentHashMap();
    private final Map<UUID, Location> D = new ConcurrentHashMap();
    private final Map<UUID, BossBar> B = new ConcurrentHashMap();
    private final Map<UUID, Map<PotionEffectType, PotionEffect>> A = new ConcurrentHashMap();
    private final Map<UUID, Map<PotionEffectType, Long>> G = new ConcurrentHashMap();

    public m(Main main) {
        File file;
        this.F = main;
        main.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)main);
        File file2 = new File(main.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "cudownalatarnia.yml")).exists()) {
            this.A(file);
        }
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.C = yamlConfiguration.getConfigurationSection("cudownalatarnia");
        this.A((FileConfiguration)yamlConfiguration, file);
    }

    private void A(File file) {
        try {
            file.createNewFile();
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            String string = "=== Konfiguracja przedmiotu 'Cudowna Latarnia' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Cudowna Latarnia'.\nDost\u0119pne zmienne (placeholdery):\n  {PLAYER}  - nazwa gracza u\u017cywaj\u0105cego przedmiotu\n  {TIME}    - pozosta\u0142y czas dzia\u0142ania (w sekundach)\n";
            yamlConfiguration.options().header(string);
            yamlConfiguration.options().copyHeader(true);
            yamlConfiguration.set("cudownalatarnia.material", (Object)"BEACON");
            yamlConfiguration.set("cudownalatarnia.name", (Object)"&d&lCudowna Latarnia");
            yamlConfiguration.set("cudownalatarnia.cooldown", (Object)120);
            yamlConfiguration.set("cudownalatarnia.lore", (Object)List.of((Object[])new String[]{"&7", "&8 \u00bb &7Przedmiot z &f\u015bwi\u0105tecznego wydarzenia 2025&7!", "&7", "&8 \u00bb &7Po postawieniu otrzymujesz:", "&8 \u00bb &dRegeneracje V &8(20s)", "&8 \u00bb &eAbsorbcje VI &8(10s)", "&8 \u00bb & &8(10s)", "&7", "&8 \u00bb &7Fontanna pozostanie przez &f30 sekund&7.", "&8 \u00bb &7Efekty dzia\u0142aj\u0105 w zasi\u0119gu &f30 kratek&7.", "&8 \u00bb &7Zniszczenie fontanny usuwa efekty.", "&7"}));
            yamlConfiguration.set("cudownalatarnia.customModelData", (Object)0);
            yamlConfiguration.set("cudownalatarnia.enchantments", (Object)List.of());
            yamlConfiguration.set("cudownalatarnia.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_ATTRIBUTES"));
            yamlConfiguration.set("cudownalatarnia.effect.duration", (Object)30);
            yamlConfiguration.set("cudownalatarnia.effect.radius", (Object)30);
            ArrayList arrayList = new ArrayList();
            arrayList.add((Object)Map.of((Object)"type", (Object)"REGENERATION", (Object)"duration", (Object)400, (Object)"amplifier", (Object)4));
            arrayList.add((Object)Map.of((Object)"type", (Object)"ABSORPTION", (Object)"duration", (Object)200, (Object)"amplifier", (Object)5));
            arrayList.add((Object)Map.of((Object)"type", (Object)"INCREASE_DAMAGE", (Object)"duration", (Object)200, (Object)"amplifier", (Object)1));
            yamlConfiguration.set("cudownalatarnia.effects", (Object)arrayList);
            yamlConfiguration.set("cudownalatarnia.messages.placed.title", (Object)"&d&lCUDOWNA LATARNIA");
            yamlConfiguration.set("cudownalatarnia.messages.placed.subtitle", (Object)"&7Efekty dzia\u0142aj\u0105 w zasi\u0119gu &f30 kratek&7!");
            yamlConfiguration.set("cudownalatarnia.messages.enter.title", (Object)"&d&lCUDOWNA LATARNIA");
            yamlConfiguration.set("cudownalatarnia.messages.enter.subtitle", (Object)"&7Wchodzisz w zasi\u0119g &flatarni&7!");
            yamlConfiguration.set("cudownalatarnia.messages.exit.title", (Object)"&d&lCUDOWNA LATARNIA");
            yamlConfiguration.set("cudownalatarnia.messages.exit.subtitle", (Object)"&7Wychodzisz z zasi\u0119gu &flatarni&7!");
            yamlConfiguration.set("cudownalatarnia.messages.destroyed.title", (Object)"&d&lCUDOWNA LATARNIA");
            yamlConfiguration.set("cudownalatarnia.messages.destroyed.subtitle", (Object)"&fLatarnia &7zosta\u0142a zniszczona!");
            yamlConfiguration.set("cudownalatarnia.messages.ended.title", (Object)"&d&lLATARNIA WYGAS\u0141A");
            yamlConfiguration.set("cudownalatarnia.messages.ended.subtitle", (Object)"&7Czas dzia\u0142ania &flatarni &7dobieg\u0142 ko\u0144ca!");
            yamlConfiguration.set("cudownalatarnia.bossbar.enabled", (Object)true);
            yamlConfiguration.set("cudownalatarnia.bossbar.title", (Object)"&5Cudowna Latarnia aktywna! &8({TIME}s&8)");
            yamlConfiguration.set("cudownalatarnia.bossbar.color", (Object)"PINK");
            yamlConfiguration.set("cudownalatarnia.bossbar.style", (Object)"SEGMENTED_10");
            yamlConfiguration.set("cudownalatarnia.sounds.enabled", (Object)true);
            yamlConfiguration.set("cudownalatarnia.sounds.activation.sound", (Object)"BLOCK_BEACON_ACTIVATE");
            yamlConfiguration.set("cudownalatarnia.sounds.activation.volume", (Object)1.0);
            yamlConfiguration.set("cudownalatarnia.sounds.activation.pitch", (Object)1.2);
            yamlConfiguration.set("cudownalatarnia.sounds.enter.sound", (Object)"BLOCK_BEACON_AMBIENT");
            yamlConfiguration.set("cudownalatarnia.sounds.enter.volume", (Object)0.8);
            yamlConfiguration.set("cudownalatarnia.sounds.enter.pitch", (Object)1.5);
            yamlConfiguration.set("cudownalatarnia.sounds.exit.sound", (Object)"BLOCK_BEACON_DEACTIVATE");
            yamlConfiguration.set("cudownalatarnia.sounds.exit.volume", (Object)0.8);
            yamlConfiguration.set("cudownalatarnia.sounds.exit.pitch", (Object)0.8);
            yamlConfiguration.set("cudownalatarnia.sounds.destroyed.sound", (Object)"ENTITY_GENERIC_EXPLODE");
            yamlConfiguration.set("cudownalatarnia.sounds.destroyed.volume", (Object)1.0);
            yamlConfiguration.set("cudownalatarnia.sounds.destroyed.pitch", (Object)1.5);
            yamlConfiguration.set("cudownalatarnia.sounds.ended.sound", (Object)"BLOCK_BEACON_DEACTIVATE");
            yamlConfiguration.set("cudownalatarnia.sounds.ended.volume", (Object)1.0);
            yamlConfiguration.set("cudownalatarnia.sounds.ended.pitch", (Object)1.0);
            yamlConfiguration.set("cudownalatarnia.particles.enabled", (Object)true);
            yamlConfiguration.set("cudownalatarnia.particles.dust_color_r", (Object)255);
            yamlConfiguration.set("cudownalatarnia.particles.dust_color_g", (Object)150);
            yamlConfiguration.set("cudownalatarnia.particles.dust_color_b", (Object)200);
            yamlConfiguration.set("cudownalatarnia.particles.dust_size", (Object)1.5);
            yamlConfiguration.set("cudownalatarnia.particles.ambient_dust_size", (Object)0.8);
            yamlConfiguration.save(file);
        }
        catch (IOException iOException) {
            this.F.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku cudownalatarnia.yml: " + iOException.getMessage());
        }
    }

    private void A(FileConfiguration fileConfiguration, File file) {
        boolean bl = false;
        if (!fileConfiguration.contains("cudownalatarnia.sounds.enabled")) {
            fileConfiguration.set("cudownalatarnia.sounds.enabled", (Object)true);
            fileConfiguration.set("cudownalatarnia.sounds.activation.sound", (Object)"BLOCK_BEACON_ACTIVATE");
            fileConfiguration.set("cudownalatarnia.sounds.activation.volume", (Object)1.0);
            fileConfiguration.set("cudownalatarnia.sounds.activation.pitch", (Object)1.2);
            bl = true;
        }
        if (bl) {
            try {
                fileConfiguration.save(file);
                this.C = fileConfiguration.getConfigurationSection("cudownalatarnia");
            }
            catch (IOException iOException) {
                this.F.getLogger().warning("Nie mo\u017cna zapisa\u0107 aktualizacji konfiguracji cudownalatarnia.yml: " + iOException.getMessage());
            }
        }
    }

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.F.getDataFolder(), "items/cudownalatarnia.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("cudownalatarnia")) != null) {
                this.C = configurationSection;
                this.F.getLogger().info("Prze\u0142adowano konfiguracj\u0119 CudownaLatarnia");
            }
        }
        catch (Exception exception) {
            this.F.getLogger().warning("B\u0142\u0105d prze\u0142adowywania CudownaLatarnia: " + exception.getMessage());
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!this.A(itemStack)) {
            return;
        }
        if (this.F.isItemDisabledByKey("cudownalatarnia")) {
            return;
        }
        playerInteractEvent.setCancelled(true);
        int n2 = this.C.getInt("cooldown", 120);
        if (player.getCooldown(Material.BEACON) > 0) {
            return;
        }
        Block block = playerInteractEvent.getClickedBlock();
        if (block == null) {
            return;
        }
        Location location = block.getLocation().add(0.0, 1.0, 0.0);
        try {
            if (this.F.isTargetBlockInProtectedRegion(location)) {
                return;
            }
        }
        catch (Exception exception) {
            this.F.getLogger().warning("B\u0142\u0105d sprawdzania chronionego regionu: " + exception.getMessage());
        }
        if (location.getBlock().getType() != Material.AIR) {
            return;
        }
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        int n3 = n2 * 20;
        player.setCooldown(Material.BEACON, n3);
        me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A((Plugin)this.F, player, itemStack, n2, "cudownalatarnia");
        location.getBlock().setType(Material.BEACON);
        this.A(player, location);
    }

    private void A(Player player, final Location location) {
        int n2 = this.C.getInt("effect.duration", 30);
        final int n3 = this.C.getInt("effect.radius", 30);
        _A _A2 = new _A(player.getUniqueId(), location, n2);
        this.E.put((Object)location, (Object)_A2);
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        this.A(location);
        this.A(player, "activation");
        String string = this.C.getString("messages.placed.title", "&5Cudowna Latarnia aktywna!");
        String string2 = this.C.getString("messages.placed.subtitle", "&7Efekty dzia\u0142aj\u0105 w zasi\u0119gu &f30 kratek&7!");
        player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 70, 20);
        this.B(player, _A2);
        if (player.getLocation().distance(location) <= (double)n3) {
            this.D(player, _A2);
            this.D.put((Object)player.getUniqueId(), (Object)location);
            _A2.B.add((Object)player.getUniqueId());
        }
        _A2.G = new BukkitRunnable(this){
            final /* synthetic */ m this$0;
            {
                this.this$0 = m2;
            }

            public void run() {
                if (!this.this$0.E.containsKey((Object)location)) {
                    this.cancel();
                    return;
                }
                this.this$0.A(location, n3);
            }
        }.runTaskTimer((Plugin)this.F, 0L, 5L);
        _A2.E = new BukkitRunnable(this){
            final /* synthetic */ m this$0;
            {
                this.this$0 = m2;
            }

            public void run() {
                if (!this.this$0.E.containsKey((Object)location)) {
                    this.cancel();
                    return;
                }
                _A _A2 = (_A)this.this$0.E.get((Object)location);
                if (_A2 == null) {
                    this.cancel();
                    return;
                }
                if (_A2.A() <= 0) {
                    this.this$0.A(location, false);
                    this.cancel();
                    return;
                }
                this.this$0.A(_A2);
                this.this$0.A(_A2, n3);
            }
        }.runTaskTimer((Plugin)this.F, 20L, 20L);
        Bukkit.getScheduler().runTaskLater((Plugin)this.F, () -> {
            if (this.E.containsKey((Object)location)) {
                this.A(location, false);
            }
        }, (long)n2 * 20L);
    }

    private void A(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        Location location2 = location.clone().add(0.5, 0.5, 0.5);
        int n2 = this.C.getInt("particles.dust_color_r", 255);
        int n3 = this.C.getInt("particles.dust_color_g", 150);
        int n4 = this.C.getInt("particles.dust_color_b", 200);
        float f2 = (float)this.C.getDouble("particles.dust_size", 1.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB((int)n2, (int)n3, (int)n4), f2);
        world.spawnParticle(Particle.REDSTONE, location2, 100, 1.5, 1.5, 1.5, 0.0, (Object)dustOptions);
        world.spawnParticle(Particle.TOTEM, location2, 50, 1.0, 1.0, 1.0, 0.3);
    }

    private void A(Location location, int n2) {
        World world = location.getWorld();
        if (world == null) {
            return;
        }
        if (!this.C.getBoolean("particles.enabled", true)) {
            return;
        }
        Location location2 = location.clone().add(0.5, 0.5, 0.5);
        int n3 = this.C.getInt("particles.dust_color_r", 255);
        int n4 = this.C.getInt("particles.dust_color_g", 150);
        int n5 = this.C.getInt("particles.dust_color_b", 200);
        float f2 = (float)this.C.getDouble("particles.ambient_dust_size", 0.8);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB((int)n3, (int)n4, (int)n5), f2);
        Random random = new Random();
        for (int i2 = 0; i2 < 15; ++i2) {
            double d2 = random.nextDouble() * 2.0 * Math.PI;
            double d3 = random.nextDouble() * (double)n2;
            double d4 = location2.getX() + Math.cos((double)d2) * d3;
            double d5 = location2.getZ() + Math.sin((double)d2) * d3;
            Location location3 = new Location(world, d4, location2.getY(), d5);
            world.spawnParticle(Particle.REDSTONE, location3, 1, 0.0, 0.0, 0.0, 0.0, (Object)dustOptions);
            if (!(random.nextDouble() < 0.3)) continue;
            world.spawnParticle(Particle.VILLAGER_HAPPY, location3, 1, 0.2, 0.2, 0.2, 0.0);
        }
        world.spawnParticle(Particle.REDSTONE, location2, 5, 0.3, 0.3, 0.3, 0.0, (Object)dustOptions);
        world.spawnParticle(Particle.VILLAGER_HAPPY, location2, 2, 0.5, 0.5, 0.5, 0.0);
    }

    private void A(_A _A2, int n2) {
        Player player = Bukkit.getPlayer((UUID)_A2.D);
        if (player == null || !player.isOnline()) {
            return;
        }
        World world = _A2.A.getWorld();
        if (world == null) {
            return;
        }
        double d2 = player.getLocation().distance(_A2.A);
        boolean bl = this.D.containsKey((Object)_A2.D) && ((Location)this.D.get((Object)_A2.D)).equals((Object)_A2.A);
        boolean bl2 = d2 <= (double)n2;
        boolean bl3 = false;
        try {
            if (this.F.isPlayerInBlockedRegion(player)) {
                bl3 = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl3) {
            if (bl) {
                this.C(player, _A2);
            }
            return;
        }
        if (bl2 && !bl) {
            this.A(player, _A2);
        } else if (!bl2 && bl) {
            this.C(player, _A2);
        }
    }

    private void A(Player player, _A _A2) {
        UUID uUID = player.getUniqueId();
        this.D.put((Object)uUID, (Object)_A2.A);
        _A2.B.add((Object)uUID);
        String string = this.C.getString("messages.enter.title", "&d&lCUDOWNA LATARNIA");
        String string2 = this.C.getString("messages.enter.subtitle", "&7Wchodzisz w zasi\u0119g &flatarni&7!");
        player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        this.A(player, "enter");
        this.B(player, _A2);
        this.D(player, _A2);
    }

    private void C(Player player, _A _A2) {
        UUID uUID = player.getUniqueId();
        this.D.remove((Object)uUID);
        String string = this.C.getString("messages.exit.title", "&d&lCUDOWNA LATARNIA");
        String string2 = this.C.getString("messages.exit.subtitle", "&7Wychodzisz z zasi\u0119gu &dlatarni&7!");
        player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 40, 10);
        this.A(player, "exit");
        this.A(uUID);
        this.A(player);
    }

    private void D(Player player, _A _A2) {
        HashMap hashMap;
        UUID uUID2 = player.getUniqueId();
        if (!this.A.containsKey((Object)uUID2)) {
            hashMap = new HashMap();
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                hashMap.put((Object)potionEffect.getType(), (Object)potionEffect);
            }
            this.A.put((Object)uUID2, (Object)hashMap);
        }
        hashMap = (Map)this.G.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
        long l2 = System.currentTimeMillis();
        List list = this.C.getMapList("effects");
        for (Map map : list) {
            long l3;
            int n2;
            int n3;
            PotionEffectType potionEffectType;
            block7: {
                String string;
                if (!map.containsKey((Object)"type") || (potionEffectType = PotionEffectType.getByName((String)(string = map.get((Object)"type").toString()))) == null) continue;
                n3 = 200;
                n2 = 0;
                try {
                    if (map.containsKey((Object)"duration")) {
                        n3 = Integer.parseInt((String)map.get((Object)"duration").toString());
                    }
                    if (!map.containsKey((Object)"amplifier")) break block7;
                    n2 = Integer.parseInt((String)map.get((Object)"amplifier").toString());
                }
                catch (NumberFormatException numberFormatException) {
                    continue;
                }
            }
            int n4 = n3 + 40;
            long l4 = (long)n4 * 50L;
            Long l5 = (Long)hashMap.get((Object)potionEffectType);
            if (l5 == null) {
                l5 = l2 + l4;
                hashMap.put((Object)potionEffectType, (Object)l5);
            }
            if ((l3 = l5 - l2) <= 0L) continue;
            int n5 = (int)(l3 / 50L);
            player.addPotionEffect(new PotionEffect(potionEffectType, n5, n2, false, true), true);
        }
    }

    private void A(Player player) {
        UUID uUID = player.getUniqueId();
        List list = this.C.getMapList("effects");
        for (Map map : list) {
            String string;
            PotionEffectType potionEffectType;
            if (!map.containsKey((Object)"type") || (potionEffectType = PotionEffectType.getByName((String)(string = map.get((Object)"type").toString()))) == null) continue;
            player.removePotionEffect(potionEffectType);
        }
        Iterator iterator = (Map)this.A.remove((Object)uUID);
        if (iterator != null) {
            for (String string : iterator.values()) {
                player.addPotionEffect((PotionEffect)string, true);
            }
        }
    }

    private void B(Player player) {
        UUID uUID = player.getUniqueId();
        List list = this.C.getMapList("effects");
        for (Map map : list) {
            String string;
            PotionEffectType potionEffectType;
            if (!map.containsKey((Object)"type") || (potionEffectType = PotionEffectType.getByName((String)(string = map.get((Object)"type").toString()))) == null) continue;
            player.removePotionEffect(potionEffectType);
        }
        Iterator iterator = (Map)this.A.remove((Object)uUID);
        if (iterator != null) {
            for (String string : iterator.values()) {
                player.addPotionEffect((PotionEffect)string, true);
            }
        }
        this.D.remove((Object)uUID);
        this.G.remove((Object)uUID);
        this.A(uUID);
    }

    private void A(Location location, boolean bl) {
        UUID uUID2;
        _A _A2 = (_A)this.E.remove((Object)location);
        if (_A2 == null) {
            return;
        }
        if (_A2.G != null) {
            _A2.G.cancel();
        }
        if (_A2.E != null) {
            _A2.E.cancel();
        }
        if (!bl && location.getBlock().getType() == Material.BEACON) {
            location.getBlock().setType(Material.AIR);
        }
        String string = bl ? "destroyed" : "ended";
        String string2 = this.C.getString("messages." + string + ".title", bl ? "&c&lLATARNIA ZNISZCZONA" : "&d&lLATARNIA WYGAS\u0141A");
        String string3 = this.C.getString("messages." + string + ".subtitle", bl ? "&7Efekty zosta\u0142y usuni\u0119te!" : "&7Czas dzia\u0142ania latarni dobieg\u0142 ko\u0144ca!");
        for (UUID uUID2 : _A2.B) {
            Player player = Bukkit.getPlayer((UUID)uUID2);
            if (player == null || !player.isOnline()) continue;
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), 10, 70, 20);
            this.A(player, string);
            this.B(player);
        }
        Iterator iterator = Bukkit.getPlayer((UUID)_A2.D);
        if (iterator != null) {
            this.A(_A2.D);
        }
        if ((uUID2 = location.getWorld()) != null && bl) {
            uUID2.spawnParticle(Particle.EXPLOSION_LARGE, location.clone().add(0.5, 0.5, 0.5), 3, 0.5, 0.5, 0.5, 0.0);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Block block = blockBreakEvent.getBlock();
        Location location = block.getLocation();
        if (this.E.containsKey((Object)location)) {
            blockBreakEvent.setDropItems(false);
            this.A(location, true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBeaconInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = playerInteractEvent.getClickedBlock();
        if (block == null) {
            return;
        }
        Location location = block.getLocation();
        if (this.E.containsKey((Object)location)) {
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockDamage(BlockDamageEvent blockDamageEvent) {
        Block block = blockDamageEvent.getBlock();
        Location location = block.getLocation();
        if (this.E.containsKey((Object)location) && blockDamageEvent.getInstaBreak()) {
            blockDamageEvent.setInstaBreak(false);
        }
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onBlockExplode(BlockExplodeEvent blockExplodeEvent) {
        ArrayList arrayList = new ArrayList();
        for (Block block : blockExplodeEvent.blockList()) {
            Location location = block.getLocation();
            if (!this.E.containsKey((Object)location)) continue;
            arrayList.add((Object)block);
        }
        blockExplodeEvent.blockList().removeAll((Collection)arrayList);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        ArrayList arrayList = new ArrayList();
        for (Block block : entityExplodeEvent.blockList()) {
            Location location = block.getLocation();
            if (!this.E.containsKey((Object)location)) continue;
            arrayList.add((Object)block);
        }
        entityExplodeEvent.blockList().removeAll((Collection)arrayList);
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onItemSpawn(ItemSpawnEvent itemSpawnEvent) {
        Location location = itemSpawnEvent.getEntity().getLocation();
        for (Location location2 : this.E.keySet()) {
            if (location.getBlockX() != location2.getBlockX() || location.getBlockY() != location2.getBlockY() || location.getBlockZ() != location2.getBlockZ() || !location.getWorld().equals((Object)location2.getWorld())) continue;
            itemSpawnEvent.setCancelled(true);
            return;
        }
    }

    private void B(Player player, _A _A2) {
        BarStyle barStyle;
        BarColor barColor;
        if (!this.C.getBoolean("bossbar.enabled", true)) {
            return;
        }
        UUID uUID = player.getUniqueId();
        this.A(uUID);
        String string = this.C.getString("bossbar.title", "&5Cudowna Latarnia aktywna! &8({TIME}s&8)");
        string = string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)_A2.A()));
        string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string);
        try {
            barColor = BarColor.valueOf((String)this.C.getString("bossbar.color", "PINK").toUpperCase());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            barColor = BarColor.PINK;
        }
        try {
            barStyle = BarStyle.valueOf((String)this.C.getString("bossbar.style", "SEGMENTED_10").toUpperCase());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            barStyle = BarStyle.SEGMENTED_10;
        }
        BossBar bossBar = Bukkit.createBossBar((String)string, (BarColor)barColor, (BarStyle)barStyle, (BarFlag[])new BarFlag[0]);
        bossBar.setProgress(1.0);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        this.B.put((Object)uUID, (Object)bossBar);
    }

    private void A(_A _A2) {
        int n2 = _A2.A();
        double d2 = (double)n2 / (double)_A2.F;
        String string = this.C.getString("bossbar.title", "&5Cudowna Latarnia aktywna! &8({TIME}s&8)");
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)n2)));
        BossBar bossBar = (BossBar)this.B.get((Object)_A2.D);
        if (bossBar != null) {
            bossBar.setTitle(string2);
            bossBar.setProgress(Math.max((double)0.0, (double)Math.min((double)1.0, (double)d2)));
        }
        for (UUID uUID : _A2.B) {
            BossBar bossBar2;
            if (uUID.equals((Object)_A2.D) || (bossBar2 = (BossBar)this.B.get((Object)uUID)) == null) continue;
            bossBar2.setTitle(string2);
            bossBar2.setProgress(Math.max((double)0.0, (double)Math.min((double)1.0, (double)d2)));
        }
    }

    private void A(UUID uUID) {
        BossBar bossBar = (BossBar)this.B.remove((Object)uUID);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    private void A(Player player, String string) {
        try {
            boolean bl = this.C.getBoolean("sounds.enabled", true);
            if (!bl) {
                return;
            }
            String string2 = this.C.getString("sounds." + string + ".sound", "");
            if (string2 == null || string2.trim().isEmpty()) {
                return;
            }
            Sound sound = Sound.valueOf((String)string2);
            float f2 = (float)this.C.getDouble("sounds." + string + ".volume", 1.0);
            float f3 = (float)this.C.getDouble("sounds." + string + ".pitch", 1.0);
            player.playSound(player.getLocation(), sound, f2, f3);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean A(ItemStack itemStack) {
        Material material;
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        try {
            material = Material.valueOf((String)this.C.getString("material", "BEACON"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BEACON;
        }
        if (itemStack.getType() != material) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.C.getString("name", "&d&lCudowna Latarnia"));
        return itemMeta.getDisplayName().equals((Object)string);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        UUID uUID = player.getUniqueId();
        if (this.D.containsKey((Object)uUID)) {
            this.B(player);
        }
        this.A.remove((Object)uUID);
        this.G.remove((Object)uUID);
        this.A(uUID);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        UUID uUID = playerJoinEvent.getPlayer().getUniqueId();
        this.A.remove((Object)uUID);
        this.D.remove((Object)uUID);
        this.G.remove((Object)uUID);
    }

    public void cleanup() {
        for (Location location : new ArrayList((Collection)this.E.keySet())) {
            this.A(location, false);
        }
        for (Location location : this.B.values()) {
            if (location == null) continue;
            location.removeAll();
        }
        this.E.clear();
        this.D.clear();
        this.B.clear();
        this.A.clear();
        this.G.clear();
    }

    public ItemStack getItem() {
        List list;
        Material material;
        try {
            material = Material.valueOf((String)this.C.getString("material", "BEACON"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BEACON;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.C.getString("name", "&d&lCudowna Latarnia"));
        itemMeta.setDisplayName(string);
        if (this.C.contains("lore")) {
            list = this.C.getStringList("lore");
            Object object = new ArrayList();
            for (String[] stringArray : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)stringArray));
            }
            itemMeta.setLore((List)object);
        }
        if (this.C.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.C.getInt("customModelData")));
        }
        if (this.C.contains("enchantments")) {
            list = this.C.getStringList("enchantments");
            for (Object object : list) {
                String[] stringArray;
                if (object == null || object.trim().isEmpty() || (stringArray = object.split(":")).length < 2) continue;
                String string2 = stringArray[0].trim().toUpperCase();
                int n2 = 1;
                try {
                    n2 = Integer.parseInt((String)stringArray[1].trim());
                }
                catch (NumberFormatException numberFormatException) {
                    this.F.getLogger().warning("B\u0142\u0105d parsowania poziomu enchantu: " + (String)object);
                }
                Enchantment enchantment = Enchantment.getByName((String)string2);
                if (enchantment != null) {
                    itemMeta.addEnchant(enchantment, n2, true);
                    continue;
                }
                this.F.getLogger().warning("Nieznany enchant: " + string2);
            }
        }
        if (this.C.contains("flags")) {
            for (Object object : this.C.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static class _A {
        final UUID D;
        final Location A;
        final long C;
        final int F;
        BukkitTask G;
        BukkitTask E;
        final Set<UUID> B = ConcurrentHashMap.newKeySet();

        _A(UUID uUID, Location location, int n2) {
            this.D = uUID;
            this.A = location;
            this.C = System.currentTimeMillis();
            this.F = n2;
        }

        int A() {
            long l2 = (System.currentTimeMillis() - this.C) / 1000L;
            return Math.max((int)0, (int)(this.F - (int)l2));
        }
    }
}

