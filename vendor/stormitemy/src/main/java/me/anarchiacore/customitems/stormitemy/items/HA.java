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
 *  java.lang.reflect.Field
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.HashSet
 *  java.util.List
 *  java.util.Set
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.SmallFireball
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.utils.color.A;

public class HA
implements Listener {
    private static final String A = "c3Rvcm1jb2Rl";
    private final Plugin G;
    private final ConfigurationSection B;
    private List<String> E = new ArrayList();
    private List<String> F = new ArrayList();
    private long D = 0L;
    private static final long C = 60000L;

    public HA(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.G = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "bombardamaxima.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Bombarda maxima' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Bombarda maxima'.\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("bombarda.material", (Object)"FIRE_CHARGE");
                yamlConfiguration.set("bombarda.name", (Object)"&5&lBombarda maxima");
                yamlConfiguration.set("bombarda.lore", (Object)Arrays.asList((Object[])new String[]{"&r", "&8 \u00bb &7Jest to przedmiot zdobyty z:", "&8 \u00bb &fkarnetu wakacyjnego 2024&7!", "&r", "&8 \u00bb &7Po wystrzeleniu wybucha w okolicy", "&8 \u00bb &7i niszczy ka\u017cdy mo\u017cliwy blok opr\u00f3cz", "&8 \u00bb &fska\u0142y macierzystej&7!", "&r"}));
                yamlConfiguration.set("bombarda.customModelData", (Object)1);
                yamlConfiguration.set("bombarda.enchantments", (Object)List.of((Object)"DURABILITY:1"));
                yamlConfiguration.set("bombarda.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("bombarda.unbreakable", (Object)true);
                yamlConfiguration.set("bombarda.cooldown", (Object)60);
                yamlConfiguration.set("bombarda.explosionRadius", (Object)5);
                yamlConfiguration.set("bombarda.explosionParticleCount", (Object)10);
                yamlConfiguration.set("bombarda.useProjectileMode", (Object)false);
                yamlConfiguration.set("bombarda.projectileSpeed", (Object)1.5);
                ArrayList arrayList = new ArrayList();
                arrayList.add((Object)"BEDROCK");
                yamlConfiguration.set("bombarda.protectedBlocks", (Object)arrayList);
                yamlConfiguration.set("bombarda.spawnFire", (Object)true);
                yamlConfiguration.set("bombarda.fireChance", (Object)0.3);
                yamlConfiguration.set("bombarda.messages.cooldown", (Object)"");
                yamlConfiguration.set("bombarda.messages.consumer.title", (Object)"&5&LBOMBARDA MAXIMA");
                yamlConfiguration.set("bombarda.messages.consumer.subtitle", (Object)"&7Teren wok\u00f3\u0142 ciebie &fwybuch\u0142&7!");
                yamlConfiguration.set("bombarda.messages.consumer.chatMessage", (Object)"");
                yamlConfiguration.set("bombarda.noDestroyRegions", (Object)new ArrayList());
                yamlConfiguration.set("bombarda.preventRegionDestruction", (Object)true);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                IOException iOException2 = iOException;
                iOException2.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.B = yamlConfiguration.getConfigurationSection("bombarda");
        this.B();
    }

    private void B() {
        if (this.G instanceof Main && this.B != null) {
            Main main = (Main)this.G;
            this.E = new ArrayList((Collection)main.getConfig().getStringList("disabled-regions"));
            this.F = new ArrayList((Collection)this.B.getStringList("noDestroyRegions"));
            this.D = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = playerInteractEvent.getPlayer();
            ItemStack itemStack = playerInteractEvent.getItem();
            if (itemStack != null && this.A(itemStack)) {
                if (this.G instanceof Main && ((Main)this.G).isItemDisabledByKey("bombardamaxima")) {
                    return;
                }
                try {
                    Main main;
                    if (this.G instanceof Main && (main = (Main)this.G).isPlayerInBlockedRegion(player)) {
                        me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                        playerInteractEvent.setCancelled(true);
                        return;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                int n2 = this.B.getInt("cooldown", 10);
                if (player.getCooldown(itemStack.getType()) > 0) {
                    int n3 = player.getCooldown(itemStack.getType()) / 20;
                    String string = this.B.getString("messages.cooldown", "&cBombarda maxima jest na cooldownie! {TIME}s pozosta\u0142o.");
                    if (!string.isEmpty()) {
                        string = string.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)n3));
                        player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
                    }
                    return;
                }
                me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A((Plugin)this.G, player, itemStack, n2, "bombardamaxima");
                boolean bl = this.B.getBoolean("useProjectileMode", false);
                if (bl) {
                    SmallFireball smallFireball = (SmallFireball)player.launchProjectile(SmallFireball.class);
                    smallFireball.setMetadata("bombardaMaxima", (MetadataValue)new FixedMetadataValue((Plugin)this.G, (Object)true));
                    smallFireball.setMetadata("bombardaPlayer", (MetadataValue)new FixedMetadataValue((Plugin)this.G, (Object)player.getUniqueId().toString()));
                    double d2 = this.B.getDouble("projectileSpeed", 1.5);
                    smallFireball.setVelocity(player.getLocation().getDirection().multiply(d2));
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
                } else {
                    Location location = playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && playerInteractEvent.getClickedBlock() != null ? playerInteractEvent.getClickedBlock().getLocation().add(0.5, 0.5, 0.5) : player.getLocation();
                    World world = location.getWorld();
                    if (world != null) {
                        world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                        int n4 = this.B.getInt("explosionParticleCount", 10);
                        world.spawnParticle(Particle.EXPLOSION_LARGE, location, n4, 1.0, 1.0, 1.0);
                    }
                    this.A(location);
                    String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.consumer.title", "&5&LBOMBARDA MAXIMA"));
                    String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.consumer.subtitle", "&7Teren wok\u00f3\u0142 ciebie &fwybuch\u0142&7!"));
                    player.sendTitle(string, string2, 10, 70, 20);
                }
                if (playerInteractEvent.getHand() != null) {
                    if (playerInteractEvent.getHand() == EquipmentSlot.HAND) {
                        this.A(player, true);
                    } else {
                        this.A(player, false);
                    }
                }
                playerInteractEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        if (projectileHitEvent.getEntity() instanceof SmallFireball && projectileHitEvent.getEntity().hasMetadata("bombardaMaxima")) {
            SmallFireball smallFireball = (SmallFireball)projectileHitEvent.getEntity();
            Location location = smallFireball.getLocation();
            World world = location.getWorld();
            if (world != null) {
                world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                int n2 = this.B.getInt("explosionParticleCount", 10);
                world.spawnParticle(Particle.EXPLOSION_LARGE, location, n2, 1.0, 1.0, 1.0);
            }
            this.A(location);
            if (smallFireball.hasMetadata("bombardaPlayer")) {
                String string = ((MetadataValue)smallFireball.getMetadata("bombardaPlayer").get(0)).asString();
                for (Player player : this.G.getServer().getOnlinePlayers()) {
                    if (!player.getUniqueId().toString().equals((Object)string)) continue;
                    String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.consumer.title", "&5&LBOMBARDA MAXIMA"));
                    String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.consumer.subtitle", "&7Teren wok\u00f3\u0142 ciebie &fwybuch\u0142&7!"));
                    player.sendTitle(string2, string3, 10, 70, 20);
                    break;
                }
            }
            smallFireball.remove();
        }
    }

    private boolean A(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            String string = itemStack.getItemMeta().getDisplayName();
            String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&5&lBombarda maxima"));
            return string.equals((Object)string2);
        }
        return false;
    }

    private void A(Player player, boolean bl) {
        ItemStack itemStack;
        ItemStack itemStack2 = itemStack = bl ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else if (bl) {
            player.getInventory().setItemInMainHand((ItemStack)null);
        } else {
            player.getInventory().setItemInOffHand((ItemStack)null);
        }
    }

    private void A(Location location) {
        World world = location.getWorld();
        int n2 = this.B.getInt("explosionRadius", 5);
        Set<Material> set = this.A();
        int n3 = n2;
        List<String> list = this.E;
        List<String> list2 = this.F;
        boolean bl = this.B.getBoolean("preventRegionDestruction", true);
        if (System.currentTimeMillis() - this.D > 60000L) {
            this.B();
            list = this.E;
            list2 = this.F;
        }
        for (int i2 = -n3; i2 <= n3; ++i2) {
            for (int i3 = -n3; i3 <= n3; ++i3) {
                for (int i4 = -n3; i4 <= n3; ++i4) {
                    Main main;
                    Block block;
                    Material material;
                    Location location2 = location.clone().add((double)i2, (double)i3, (double)i4);
                    if (!(location2.distance(location) <= (double)n2) || set.contains((Object)(material = (block = location2.getBlock()).getType()))) continue;
                    boolean bl2 = false;
                    boolean bl3 = false;
                    if (this.G instanceof Main) {
                        main = (Main)this.G;
                        if (bl) {
                            if (main.isTargetBlockInProtectedRegion(location2)) {
                                String string = main.getRegionChecker().A(location2);
                                if (string != null) {
                                    if (list2.contains((Object)string.toLowerCase())) {
                                        bl3 = true;
                                    } else if (list.contains((Object)string.toLowerCase())) {
                                        bl2 = true;
                                    } else if (main.getRegionManager().A(location2)) {
                                        bl2 = true;
                                    }
                                }
                            } else if (main.getRegionManager().A(location2)) {
                                bl2 = true;
                            }
                        }
                    }
                    if (bl2) continue;
                    if (bl3) {
                        if (world == null) continue;
                        world.spawnParticle(Particle.EXPLOSION_LARGE, location2, 1, 0.0, 0.0, 0.0, 0.0);
                        continue;
                    }
                    if (block.hasMetadata("hydro_cage_block")) {
                        if (world == null) continue;
                        world.spawnParticle(Particle.FALLING_WATER, location2, 3, 0.2, 0.2, 0.2, 0.1);
                        continue;
                    }
                    block.setType(Material.AIR);
                    if (!this.B.getBoolean("spawnFire", true) || !(Math.random() < this.B.getDouble("fireChance", 0.3)) || (main = block.getRelative(BlockFace.UP)).getType() != Material.AIR) continue;
                    main.setType(Material.FIRE);
                }
            }
        }
    }

    private Set<Material> A() {
        HashSet hashSet = new HashSet();
        List list = this.B.getStringList("protectedBlocks");
        for (String string : list) {
            try {
                Material material = Material.valueOf((String)string.toUpperCase());
                hashSet.add((Object)material);
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
        return hashSet;
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.B.getString("material", "FIRE_CHARGE"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.FIRE_CHARGE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&5&lBombarda maxima"));
        itemMeta.setDisplayName(string);
        if (this.B.contains("lore")) {
            List list = this.B.getStringList("lore");
            Object object = new ArrayList();
            for (String string2 : list) {
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2));
            }
            itemMeta.setLore((List)object);
        }
        if (this.B.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.B.getInt("customModelData")));
        }
        if (this.B.contains("enchantments")) {
            for (Object object : this.B.getStringList("enchantments")) {
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
        if (this.B.contains("flags")) {
            for (Object object : this.B.getStringList("flags")) {
                try {
                    itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf((String)object)});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        itemMeta.setUnbreakable(this.B.getBoolean("unbreakable", true));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.G.getDataFolder(), "items/bombardamaxima.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("bombarda")) != null) {
                Field field = this.getClass().getDeclaredField("config");
                field.setAccessible(true);
                field.set((Object)this, (Object)configurationSection);
                this.B();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

