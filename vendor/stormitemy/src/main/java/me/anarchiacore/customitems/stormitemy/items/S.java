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
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Creeper
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class S
implements Listener {
    private final Main C;
    private final ConfigurationSection A;
    private final Map<UUID, UUID> B = new ConcurrentHashMap();

    public S(Main main) {
        me.anarchiacore.customitems.stormitemy.config.A a2;
        this.C = main;
        main.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)main);
        File file = new File(main.getDataFolder(), "items");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, "zmutowanycreeper.yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        String string = "=== Konfiguracja przedmiotu 'Jajko zmutowanego creepera' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu 'Jajko zmutowanego creepera'.\nDost\u0119pne zmienne (placeholdery):\n  {OWNER}        - nazwa gracza, kt\u00f3ry u\u017cy\u0142 przedmiotu\n  {PLAYER}       - nazwa gracza, kt\u00f3ry zosta\u0142 zaatakowany\n  {HEALTH}       - aktualne zdrowie gracza po ataku\n  {DAMAGE}       - ilo\u015b\u0107 obra\u017ce\u0144 zadanych graczowi\n";
        yamlConfiguration.options().header(string);
        yamlConfiguration.options().copyHeader(true);
        yamlConfiguration.set("zmutowanycreeper.material", (Object)"CREEPER_SPAWN_EGG");
        yamlConfiguration.set("zmutowanycreeper.name", (Object)"&a&lJajko zmutowanego creepera");
        yamlConfiguration.set("zmutowanycreeper.lore", (Object)List.of((Object)"&r", (Object)"&8 \u00bb &7Jest to przedmiot zdobyty podczas", (Object)"&8 \u00bb &fspecjalnego wydarzenia marcowego (2025)", (Object)"&r", (Object)"&8 \u00bb &7Prawym klikni\u0119ciem w jajko przywo\u0142uje ono", (Object)"&8 \u00bb &fpot\u0119\u017cnego&7 creepera, kt\u00f3ry po &cwybuchu", (Object)"&8 \u00bb &7zadaje &eogromne &7obra\u017cenia.", (Object)"&r"));
        yamlConfiguration.set("zmutowanycreeper.customModelData", (Object)12345);
        yamlConfiguration.set("zmutowanycreeper.cooldown", (Object)60);
        yamlConfiguration.set("zmutowanycreeper.unbreakable", (Object)true);
        yamlConfiguration.set("zmutowanycreeper.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_ATTRIBUTES", (Object)"HIDE_UNBREAKABLE"));
        yamlConfiguration.set("zmutowanycreeper.creeper.name", (Object)"&2Zainfekowany Creeper");
        yamlConfiguration.set("zmutowanycreeper.creeper.poweredChance", (Object)100);
        yamlConfiguration.set("zmutowanycreeper.creeper.aggressiveToOwner", (Object)false);
        yamlConfiguration.set("zmutowanycreeper.creeper.explosionRadius", (Object)5.0);
        yamlConfiguration.set("zmutowanycreeper.messages.spawn", (Object)"");
        yamlConfiguration.set("zmutowanycreeper.messages.despawn", (Object)"&8\u00bb &7Tw\u00f3j &2Zmutowany Creeper &7znikn\u0105\u0142 po wej\u015bciu do regionu bezpiecznego!");
        yamlConfiguration.set("zmutowanycreeper.messages.target.title", (Object)"&a&lZMUTOWANY CREEPER");
        yamlConfiguration.set("zmutowanycreeper.messages.target.subtitle", (Object)"&7Straci\u0142e\u015b &f50% &7swojego zdrowia!");
        yamlConfiguration.set("zmutowanycreeper.messages.target.chat", (Object)"");
        yamlConfiguration.set("zmutowanycreeper.noDestroyRegions", (Object)List.of());
        yamlConfiguration.set("zmutowanycreeper.preventRegionDestruction", (Object)true);
        yamlConfiguration.set("zmutowanycreeper.sounds.spawn.enabled", (Object)true);
        yamlConfiguration.set("zmutowanycreeper.sounds.spawn.sound", (Object)"ENTITY_CREEPER_PRIMED");
        yamlConfiguration.set("zmutowanycreeper.sounds.spawn.volume", (Object)1.0);
        yamlConfiguration.set("zmutowanycreeper.sounds.spawn.pitch", (Object)0.8);
        yamlConfiguration.set("zmutowanycreeper.sounds.explode.enabled", (Object)true);
        yamlConfiguration.set("zmutowanycreeper.sounds.explode.sound", (Object)"ENTITY_GENERIC_EXPLODE");
        yamlConfiguration.set("zmutowanycreeper.sounds.explode.volume", (Object)1.5);
        yamlConfiguration.set("zmutowanycreeper.sounds.explode.pitch", (Object)0.8);
        if (!file2.exists()) {
            try {
                yamlConfiguration.save(file2);
            }
            catch (IOException iOException) {
                main.getLogger().warning("Nie mo\u017cna utworzy\u0107 pliku zmutowanycreeper.yml: " + iOException.getMessage());
            }
        } else {
            try {
                a2 = new me.anarchiacore.customitems.stormitemy.config.A(main);
                a2.A("zmutowanycreeper", yamlConfiguration.getConfigurationSection("zmutowanycreeper"));
            }
            catch (Exception exception) {
                main.getLogger().warning("B\u0142\u0105d aktualizacji konfiguracji zmutowanycreeper.yml: " + exception.getMessage());
            }
        }
        a2 = YamlConfiguration.loadConfiguration((File)file2);
        this.A = a2.getConfigurationSection("zmutowanycreeper");
        new BukkitRunnable(){

            public void run() {
                s.this.A();
            }
        }.runTaskTimer((Plugin)main, 5L, 5L);
    }

    private void A() {
        try {
            for (UUID uUID : new ArrayList((Collection)this.B.keySet())) {
                Object object2;
                World world2;
                Creeper creeper = null;
                for (World world2 : this.C.getServer().getWorlds()) {
                    for (Object object2 : world2.getEntitiesByClass(Creeper.class)) {
                        if (!object2.getUniqueId().equals((Object)uUID)) continue;
                        creeper = (Creeper)object2;
                        break;
                    }
                    if (creeper == null) continue;
                    break;
                }
                if (creeper == null || !creeper.isValid()) {
                    this.B.remove((Object)uUID);
                    continue;
                }
                boolean bl = this.C.isTargetBlockInProtectedRegion(creeper.getLocation());
                if (!bl) continue;
                world2 = (UUID)this.B.get((Object)uUID);
                Iterator iterator = this.C.getServer().getPlayer((UUID)world2);
                if (iterator != null && iterator.isOnline() && (object2 = this.A.getString("messages.despawn", "&8\u00bb &7Tw\u00f3j &2Zainfekowany Creeper &7znikn\u0105\u0142 po wej\u015bciu do regionu bezpiecznego!")) != null && !object2.isEmpty()) {
                    iterator.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object2));
                }
                creeper.remove();
                this.B.remove((Object)uUID);
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d sprawdzania creeper\u00f3w w safe regions: " + exception.getMessage());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK || playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = playerInteractEvent.getPlayer();
            ItemStack itemStack = playerInteractEvent.getItem();
            if (itemStack != null && this.A(itemStack)) {
                String string;
                Location location;
                List list;
                int n2;
                if (this.C instanceof Main && this.C.isItemDisabledByKey("zmutowanycreeper")) {
                    return;
                }
                if (player.getCooldown(itemStack.getType()) > 0) {
                    return;
                }
                try {
                    if (this.C.isPlayerInBlockedRegion(player)) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                    n2 = this.A.getBoolean("preventRegionDestruction", true);
                    list = this.A.getStringList("noDestroyRegions");
                    Location location2 = location = playerInteractEvent.getClickedBlock() != null ? playerInteractEvent.getClickedBlock().getLocation() : player.getLocation();
                    if (n2 != 0 && (string = this.C.getRegionChecker().C(location)) != null && list.stream().anyMatch(string2 -> string2.equalsIgnoreCase(string))) {
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C("&cNie mo\u017cesz u\u017cy\u0107 tego przedmiotu w regionie &f" + string + "&c!"));
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                    if (this.C.isTargetBlockInProtectedRegion(location)) {
                        string = me.anarchiacore.customitems.stormitemy.utils.language.A.A(this.C, "region_target");
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                }
                catch (Exception exception) {
                    this.C.getLogger().warning("B\u0142\u0105d sprawdzania regionu dla ZmutowanyCreeper: " + exception.getMessage());
                }
                playerInteractEvent.setCancelled(true);
                n2 = itemStack.getAmount();
                if (n2 > 1) {
                    itemStack.setAmount(n2 - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                list = playerInteractEvent.getClickedBlock() != null ? playerInteractEvent.getClickedBlock().getLocation().add(0.0, 1.0, 0.0) : player.getLocation();
                location = (Creeper)player.getWorld().spawnEntity((Location)list, EntityType.CREEPER);
                string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("creeper.name", "&2Zainfekowany Creeper"));
                location.setCustomName(string);
                location.setCustomNameVisible(true);
                int n3 = this.A.getInt("creeper.poweredChance", 100);
                if (Math.random() * 100.0 < (double)n3) {
                    location.setPowered(true);
                }
                location.setMetadata("owner", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)player.getUniqueId().toString()));
                this.B.put((Object)location.getUniqueId(), (Object)player.getUniqueId());
                String string3 = this.A.getString("messages.spawn", "&8\u00bb &7Przywo\u0142a\u0142e\u015b &2Zainfekowanego Creepera&7!");
                if (string3 != null && !string3.isEmpty()) {
                    player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
                }
                int n4 = this.A.getInt("cooldown", 60) * 20;
                player.setCooldown(itemStack.getType(), n4);
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent entityTargetEvent) {
        boolean bl;
        if (!(entityTargetEvent.getEntity() instanceof Creeper)) {
            return;
        }
        if (!(entityTargetEvent.getTarget() instanceof Player)) {
            return;
        }
        Creeper creeper = (Creeper)entityTargetEvent.getEntity();
        Player player = (Player)entityTargetEvent.getTarget();
        if (!creeper.hasMetadata("owner")) {
            return;
        }
        String string = ((MetadataValue)creeper.getMetadata("owner").get(0)).asString();
        if (player.getUniqueId().toString().equals((Object)string) && !(bl = this.A.getBoolean("creeper.aggressiveToOwner", false))) {
            entityTargetEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        if (!(entityExplodeEvent.getEntity() instanceof Creeper)) {
            return;
        }
        Creeper creeper = (Creeper)entityExplodeEvent.getEntity();
        if (!this.B.containsKey((Object)creeper.getUniqueId())) {
            return;
        }
        entityExplodeEvent.blockList().clear();
        try {
            if (this.C.isTargetBlockInProtectedRegion(creeper.getLocation())) {
                this.B.remove((Object)creeper.getUniqueId());
                return;
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d sprawdzania chronionego regionu dla eksplozji: " + exception.getMessage());
        }
        creeper.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, creeper.getLocation(), 8, 0.5, 0.5, 0.5, 0.1);
        creeper.getWorld().playSound(creeper.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
        UUID uUID = (UUID)this.B.get((Object)creeper.getUniqueId());
        boolean bl = this.A.getBoolean("creeper.aggressiveToOwner", false);
        double d2 = 5.0;
        Player player = this.C.getServer().getPlayer(uUID);
        String string = player != null ? player.getName() : "";
        creeper.getWorld().getNearbyEntities(creeper.getLocation(), d2, d2, d2).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                if (!bl && player.getUniqueId().equals((Object)uUID)) {
                    return;
                }
                if (!player.isDead() && player.getHealth() > 0.0) {
                    String string2;
                    double d2 = player.getHealth();
                    double d3 = Math.max((double)1.0, (double)(d2 * 0.5));
                    double d4 = d2 - d3;
                    player.damage(0.1);
                    player.setNoDamageTicks(0);
                    player.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation().add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1);
                    player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0.0, 1.0, 0.0), 30, 0.5, 0.5, 0.5, (Object)new Particle.DustOptions(Color.RED, 1.0f));
                    player.setHealth(d3);
                    player.setVelocity(player.getLocation().toVector().subtract(creeper.getLocation().toVector()).normalize().multiply(0.5).setY(0.2));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 0.5f, 1.5f);
                    if (Math.random() < 0.3) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false, true, true));
                    }
                    if ((string2 = this.A.getString("messages.target.chat", "")) != null && !string2.isEmpty()) {
                        string2 = string2.replace((CharSequence)"{OWNER}", (CharSequence)string).replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d3})).replace((CharSequence)"{DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d4}));
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
                    }
                    String string3 = this.A.getString("messages.target.title", "&a&lZMUTOWANY CREEPER");
                    String string4 = this.A.getString("messages.target.subtitle", "&7Straci\u0142e\u015b &f50% &7swojego zdrowia!");
                    if (string3 != null && !string3.isEmpty() || string4 != null && !string4.isEmpty()) {
                        string3 = string3.replace((CharSequence)"{OWNER}", (CharSequence)string).replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d3})).replace((CharSequence)"{DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d4}));
                        string4 = string4.replace((CharSequence)"{OWNER}", (CharSequence)string).replace((CharSequence)"{PLAYER}", (CharSequence)player.getName()).replace((CharSequence)"{HEALTH}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d3})).replace((CharSequence)"{DAMAGE}", (CharSequence)String.format((String)"%.1f", (Object[])new Object[]{d4}));
                        player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4), 10, 70, 20);
                    }
                }
            }
        });
        this.B.remove((Object)creeper.getUniqueId());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        boolean bl;
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Creeper)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getEntity();
        Creeper creeper = (Creeper)entityDamageByEntityEvent.getDamager();
        if (!this.B.containsKey((Object)creeper.getUniqueId())) {
            return;
        }
        UUID uUID = (UUID)this.B.get((Object)creeper.getUniqueId());
        if (player.getUniqueId().equals((Object)uUID) && !(bl = this.A.getBoolean("creeper.aggressiveToOwner", false))) {
            entityDamageByEntityEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent entityDamageEvent) {
        if (!(entityDamageEvent.getEntity() instanceof Player)) {
            return;
        }
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }
        Player player = (Player)entityDamageEvent.getEntity();
        double d2 = this.A.getDouble("creeper.explosionRadius", 5.0);
        for (Entity entity : player.getNearbyEntities(d2, d2, d2)) {
            Creeper creeper;
            UUID uUID;
            if (!(entity instanceof Creeper) || !this.B.containsKey((Object)(uUID = (creeper = (Creeper)entity).getUniqueId()))) continue;
            UUID uUID2 = (UUID)this.B.get((Object)uUID);
            if (player.getUniqueId().equals((Object)uUID2)) {
                boolean bl = this.A.getBoolean("creeper.aggressiveToOwner", false);
                if (!bl) {
                    entityDamageEvent.setCancelled(true);
                } else {
                    entityDamageEvent.setCancelled(true);
                }
            } else {
                entityDamageEvent.setCancelled(true);
            }
            return;
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent entityChangeBlockEvent) {
        if (!(entityChangeBlockEvent.getEntity() instanceof Creeper)) {
            return;
        }
        Creeper creeper = (Creeper)entityChangeBlockEvent.getEntity();
        if (this.B.containsKey((Object)creeper.getUniqueId())) {
            entityChangeBlockEvent.setCancelled(true);
        }
    }

    public ItemStack getItem() {
        String string3;
        List list;
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "CREEPER_SPAWN_EGG"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.CREEPER_SPAWN_EGG;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lJajko zmutowanego creepera"));
        itemMeta.setDisplayName(string2);
        if (this.A.contains("lore")) {
            list = this.A.getStringList("lore");
            ArrayList arrayList = new ArrayList();
            for (String string3 : list) {
                arrayList.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
            }
            itemMeta.setLore((List)arrayList);
        }
        if (this.A.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.A.getInt("customModelData")));
        }
        if (this.A.contains("enchantments")) {
            list = this.A.getList("enchantments");
            for (Object object : list) {
                if (!(object instanceof Map)) continue;
                string3 = (Map)object;
                for (Object object2 : string3.keySet()) {
                    String string4 = object2.toString();
                    int n2 = Integer.parseInt((String)string3.get(object2).toString());
                    Enchantment enchantment = Enchantment.getByName((String)string4.toUpperCase());
                    if (enchantment == null) continue;
                    itemMeta.addEnchant(enchantment, n2, true);
                }
            }
        }
        if (this.A.contains("flags")) {
            list = this.A.getStringList("flags");
            for (Object object : list) {
                try {
                    string3 = ItemFlag.valueOf((String)object);
                    itemMeta.addItemFlags(new ItemFlag[]{string3});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        } else {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE});
        }
        boolean bl = this.A.getBoolean("unbreakable", true);
        itemMeta.setUnbreakable(bl);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private boolean A(ItemStack itemStack) {
        Material material;
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        try {
            material = Material.valueOf((String)this.A.getString("material", "CREEPER_SPAWN_EGG"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.CREEPER_SPAWN_EGG;
        }
        if (itemStack.getType() != material) {
            return false;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&a&lJajko zmutowanego creepera"));
        if (!itemStack.getItemMeta().getDisplayName().equals((Object)string)) {
            return false;
        }
        if (this.A.contains("customModelData") && itemStack.getItemMeta().hasCustomModelData()) {
            int n2 = this.A.getInt("customModelData");
            if (itemStack.getItemMeta().getCustomModelData() != n2) {
                return false;
            }
        }
        return true;
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.A.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_CREEPER_PRIMED");
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

