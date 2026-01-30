/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Float
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.reflect.Field
 *  java.util.ArrayList
 *  java.util.List
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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

public class K
implements Listener {
    private final Plugin C;
    private ConfigurationSection A;
    private boolean B;

    public K(Plugin javaPlugin) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.C = javaPlugin;
        this.B = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        javaPlugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        File file2 = new File(javaPlugin.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "trojzabposejdona.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'Tr\u00f3jz\u0105b Posejdona' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'Tr\u00f3jz\u0105b Posejdona'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}    - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}  - nazwa gracza\n  {DAMAGE}  - warto\u015b\u0107 zadanych obra\u017ce\u0144\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("trojzab_posejdona.material", (Object)"BOW");
                yamlConfiguration.set("trojzab_posejdona.name", (Object)"&3&lTr\u00f3jz\u0105b posejdona");
                yamlConfiguration.set("trojzab_posejdona.lore", (Object)List.of((Object[])new String[]{"&7", "&8 \u00bb &7Jest to przedmiot zdobyty podczas", "&8 \u00bb &feventu wakacyjnego 2025&7!", "&7", "&8 \u00bb &7Po rzuceniu tr\u00f3jz\u0119bem w miejsce", "&8 \u00bb &7uderzenia &buderza piorun&7, kt\u00f3ry", "&8 \u00bb &7zadaje obra\u017cenia i odpycha", "&8 \u00bb &7pobliskich przeciwnik\u00f3w!", "&7", "&8 \u00bb &7Klikaj\u0105c &fSHIFT &7mozesz si\u0119", "&8 \u00bb &7wystrzeli\u0107 w dowolnym miejscu!", "&7"}));
                yamlConfiguration.set("trojzab_posejdona.customModelData", (Object)6000);
                yamlConfiguration.set("trojzab_posejdona.cooldown", (Object)30);
                yamlConfiguration.set("trojzab_posejdona.enchantments", (Object)List.of((Object)"ARROW_INFINITE:1", (Object)"DURABILITY:3"));
                yamlConfiguration.set("trojzab_posejdona.flags", (Object)List.of((Object)"HIDE_ENCHANTS", (Object)"HIDE_UNBREAKABLE", (Object)"HIDE_ATTRIBUTES"));
                yamlConfiguration.set("trojzab_posejdona.unbreakable", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.lightning.enabled", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.aoe.radius", (Object)8);
                yamlConfiguration.set("trojzab_posejdona.aoe.damage", (Object)15.0);
                yamlConfiguration.set("trojzab_posejdona.knockback.strength", (Object)2.5);
                yamlConfiguration.set("trojzab_posejdona.riptide.enabled", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.riptide.strength", (Object)3.0);
                yamlConfiguration.set("trojzab_posejdona.riptide.upwardBoost", (Object)1.5);
                yamlConfiguration.set("trojzab_posejdona.messages.shoot.title", (Object)"&3&lTR\u00d3JZ\u0104B POSEJDONA");
                yamlConfiguration.set("trojzab_posejdona.messages.shoot.subtitle", (Object)"&7Wystrzelono &btr\u00f3jz\u0105b &7z moc\u0105 posejdona!");
                yamlConfiguration.set("trojzab_posejdona.messages.shoot.chatMessage", (Object)"");
                yamlConfiguration.set("trojzab_posejdona.messages.hit.title", (Object)"&3&lTR\u00d3JZ\u0104B POSEJDONA");
                yamlConfiguration.set("trojzab_posejdona.messages.hit.subtitle", (Object)"&7Tr\u00f3jz\u0105b uderzy\u0142 z &fmoc\u0105 pioruna&7!");
                yamlConfiguration.set("trojzab_posejdona.messages.hit.chatMessage", (Object)"");
                yamlConfiguration.set("trojzab_posejdona.messages.riptide.title", (Object)"&3&lTR\u00d3JZ\u0104B POSEJDONA");
                yamlConfiguration.set("trojzab_posejdona.messages.riptide.subtitle", (Object)"&7Wystrzelono si\u0119 w &fpowietrze&7!");
                yamlConfiguration.set("trojzab_posejdona.messages.riptide.chatMessage", (Object)"");
                yamlConfiguration.set("trojzab_posejdona.messages.cooldown", (Object)"");
                yamlConfiguration.set("trojzab_posejdona.sounds.shoot.enabled", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.sounds.shoot.sound", (Object)"ITEM_TRIDENT_THROW");
                yamlConfiguration.set("trojzab_posejdona.sounds.shoot.volume", (Object)1.0);
                yamlConfiguration.set("trojzab_posejdona.sounds.shoot.pitch", (Object)1.0);
                yamlConfiguration.set("trojzab_posejdona.sounds.hit.enabled", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.sounds.hit.sound", (Object)"ENTITY_LIGHTNING_BOLT_THUNDER");
                yamlConfiguration.set("trojzab_posejdona.sounds.hit.volume", (Object)1.0);
                yamlConfiguration.set("trojzab_posejdona.sounds.hit.pitch", (Object)1.2);
                yamlConfiguration.set("trojzab_posejdona.sounds.riptide.enabled", (Object)true);
                yamlConfiguration.set("trojzab_posejdona.sounds.riptide.sound", (Object)"ITEM_TRIDENT_RIPTIDE_3");
                yamlConfiguration.set("trojzab_posejdona.sounds.riptide.volume", (Object)1.0);
                yamlConfiguration.set("trojzab_posejdona.sounds.riptide.pitch", (Object)1.0);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.A = yamlConfiguration.getConfigurationSection("trojzab_posejdona");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (playerInteractEvent.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != Material.BOW) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&3&lTr\u00f3jz\u0105b posejdona"));
        if (!itemMeta.getDisplayName().equals((Object)string)) {
            return;
        }
        boolean bl = false;
        for (ItemStack itemStack2 : player.getInventory().getContents()) {
            if (itemStack2 == null || itemStack2.getType() != Material.ARROW) continue;
            bl = true;
            break;
        }
        if (!bl) {
            ItemStack itemStack2;
            int n2 = -1;
            ItemStack itemStack3 = null;
            int n3 = player.getInventory().firstEmpty();
            if (n3 != -1) {
                n2 = n3;
                itemStack3 = null;
            } else {
                n2 = 35;
                itemStack3 = player.getInventory().getItem(35);
            }
            itemStack2 = new ItemStack(Material.ARROW, 1);
            player.getInventory().setItem(n2, itemStack2);
            ItemStack itemStack4 = null;
            if (itemStack3 != null) {
                itemStack4 = itemStack3.clone();
            }
            player.setMetadata("trojzab_temporary_arrow_slot", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)n2));
            player.setMetadata("trojzab_temporary_arrow_original", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)itemStack4));
            final int n4 = n2;
            final Player player2 = player;
            new BukkitRunnable(this){
                final /* synthetic */ k this$0;
                {
                    this.this$0 = k2;
                }

                public void run() {
                    if (player2.hasMetadata("trojzab_temporary_arrow_slot")) {
                        ItemStack itemStack = null;
                        if (player2.hasMetadata("trojzab_temporary_arrow_original")) {
                            itemStack = (ItemStack)((MetadataValue)player2.getMetadata("trojzab_temporary_arrow_original").get(0)).value();
                        }
                        if (player2.hasMetadata("trojzab_shot_executed")) {
                            ItemStack itemStack2 = player2.getInventory().getItem(n4);
                            player2.getInventory().setItem(n4, itemStack);
                            if (itemStack != null) {
                                // empty if block
                            }
                            player2.removeMetadata("trojzab_temporary_arrow_slot", (Plugin)this.this$0.C);
                            player2.removeMetadata("trojzab_temporary_arrow_original", (Plugin)this.this$0.C);
                            player2.removeMetadata("trojzab_shot_executed", (Plugin)this.this$0.C);
                        } else {
                            new BukkitRunnable(){

                                public void run() {
                                    if (player2.hasMetadata("trojzab_temporary_arrow_slot")) {
                                        ItemStack itemStack = null;
                                        if (player2.hasMetadata("trojzab_temporary_arrow_original")) {
                                            itemStack = (ItemStack)((MetadataValue)player2.getMetadata("trojzab_temporary_arrow_original").get(0)).value();
                                        }
                                        ItemStack itemStack2 = player2.getInventory().getItem(n4);
                                        player2.getInventory().setItem(n4, itemStack);
                                        if (itemStack != null) {
                                            // empty if block
                                        }
                                        player2.removeMetadata("trojzab_temporary_arrow_slot", (Plugin)this$0.C);
                                        player2.removeMetadata("trojzab_temporary_arrow_original", (Plugin)this$0.C);
                                        player2.removeMetadata("trojzab_shot_executed", (Plugin)this$0.C);
                                    }
                                }
                            }.runTaskLater((Plugin)this.this$0.C, 40L);
                        }
                    }
                }
            }.runTaskLater((Plugin)this.C, 5L);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent entityShootBowEvent) {
        Object object;
        String string;
        if (!(entityShootBowEvent.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)entityShootBowEvent.getEntity();
        ItemStack itemStack = entityShootBowEvent.getBow();
        if (itemStack == null) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasDisplayName()) {
            return;
        }
        final String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&3&lTr\u00f3jz\u0105b posejdona"));
        if (!itemMeta.getDisplayName().equals((Object)string2)) {
            return;
        }
        boolean bl = player.isSneaking();
        String string3 = bl ? "trojzab_posejdona_riptide_cooldown" : "trojzab_posejdona_arrow_cooldown";
        String string4 = string = bl ? "trojzabposejdona_riptide" : "trojzabposejdona";
        if (player.hasMetadata(string3)) {
            long l2 = ((MetadataValue)player.getMetadata(string3).get(0)).asLong();
            if (System.currentTimeMillis() < l2) {
                long l3 = (l2 - System.currentTimeMillis()) / 1000L;
                String string5 = this.A.getString("messages.cooldown", "");
                if (!string5.isEmpty()) {
                    String string6 = bl ? "Riptide" : "Strza\u0142a";
                    string5 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5.replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((long)l3)));
                    string5 = string5.replace((CharSequence)"Tr\u00f3jz\u0105b Posejdona", (CharSequence)("Tr\u00f3jz\u0105b Posejdona (" + string6 + ")"));
                    player.sendMessage(string5);
                }
                entityShootBowEvent.setCancelled(true);
                return;
            }
            player.removeMetadata(string3, (Plugin)this.C);
        }
        if (player.isSneaking()) {
            String string7;
            entityShootBowEvent.setCancelled(true);
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
            final ItemStack itemStack2 = new ItemStack(Material.TRIDENT);
            ItemMeta itemMeta2 = itemStack2.getItemMeta();
            float f2 = entityShootBowEvent.getForce();
            final int n2 = f2 >= 0.9f ? 2 : (f2 >= 0.5f ? 1 : 1);
            Enchantment enchantment = null;
            try {
                enchantment = Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"riptide"));
            }
            catch (Exception exception) {
                try {
                    enchantment = Enchantment.getByName((String)"RIPTIDE");
                }
                catch (Exception exception2) {
                    try {
                        enchantment = Enchantment.RIPTIDE;
                    }
                    catch (Exception exception3) {
                        // empty catch block
                    }
                }
            }
            if (enchantment != null) {
                itemMeta2.addEnchant(enchantment, n2, true);
            }
            if ((string7 = itemStack.getItemMeta().getDisplayName()) != null && !string7.isEmpty()) {
                itemMeta2.setDisplayName(string7);
            }
            if (itemStack.getItemMeta().hasLore()) {
                itemMeta2.setLore(itemStack.getItemMeta().getLore());
            }
            if (itemStack.getItemMeta().hasCustomModelData()) {
                itemMeta2.setCustomModelData(Integer.valueOf((int)itemStack.getItemMeta().getCustomModelData()));
            }
            itemStack2.setItemMeta(itemMeta2);
            final ItemStack itemStack3 = player.getInventory().getItemInMainHand();
            player.getInventory().setItemInMainHand(itemStack2);
            int n3 = this.A.getInt("cooldown", 30);
            long l4 = System.currentTimeMillis() + (long)n3 * 1000L;
            player.setMetadata("trojzab_posejdona_riptide_cooldown", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)l4));
            try {
                Main main;
                if (this.C instanceof Main && (main = (Main)this.C).getActionbarManager() != null && main.getActionbarManager().isEnabled()) {
                    main.getActionbarManager().registerCooldown(player, "trojzabposejdona_riptide", n3);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            new BukkitRunnable(this){
                final /* synthetic */ k this$0;
                {
                    this.this$0 = k2;
                }

                public void run() {
                    if (player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
                        try {
                            Vector vector = player.getLocation().getDirection().multiply((double)n2 * 2.0);
                            vector.setY(Math.max((double)(vector.getY() + 0.5), (double)0.5));
                            player.setVelocity(vector);
                            try {
                                player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ITEM_TRIDENT_RIPTIDE_1"), 1.0f, 1.0f);
                                player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ITEM_TRIDENT_RIPTIDE_2"), 1.0f, 1.2f);
                            }
                            catch (Exception exception) {
                                try {
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ENTITY_PLAYER_SPLASH"), 1.0f, 1.0f);
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ENTITY_PLAYER_SWIM"), 1.0f, 1.2f);
                                }
                                catch (Exception exception2) {
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"BLOCK_WATER_AMBIENT"), 1.0f, 1.2f);
                                }
                            }
                            Location location = player.getLocation();
                            try {
                                player.getWorld().spawnParticle(Particle.valueOf((String)"WATER_SPLASH"), location.add(0.0, 1.0, 0.0), 20, 0.5, 0.5, 0.5, 0.1);
                                player.getWorld().spawnParticle(Particle.valueOf((String)"WATER_BUBBLE"), location, 15, 0.3, 0.3, 0.3, 0.05);
                            }
                            catch (Exception exception) {
                                try {
                                    player.getWorld().spawnParticle(Particle.valueOf((String)"SPLASH"), location, 20, 0.5, 0.5, 0.5, 0.1);
                                }
                                catch (Exception exception3) {
                                    try {
                                        player.getWorld().spawnParticle(Particle.valueOf((String)"CLOUD"), location, 15, 0.5, 0.5, 0.5, 0.05);
                                    }
                                    catch (Exception exception4) {
                                        // empty catch block
                                    }
                                }
                            }
                            try {
                                Class clazz = Class.forName((String)"org.bukkit.event.player.PlayerRiptideEvent");
                                Object object = clazz.getConstructor(new Class[]{Player.class, ItemStack.class}).newInstance(new Object[]{player, itemStack2});
                                this.this$0.C.getServer().getPluginManager().callEvent((Event)object);
                            }
                            catch (Exception exception) {}
                        }
                        catch (Exception exception) {
                            Vector vector = player.getLocation().getDirection().multiply((double)n2 * 2.0);
                            vector.setY(Math.max((double)(vector.getY() + 0.5), (double)0.5));
                            player.setVelocity(vector);
                            try {
                                player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ITEM_TRIDENT_RIPTIDE_1"), 1.0f, 1.0f);
                            }
                            catch (Exception exception5) {
                                try {
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"ENTITY_PLAYER_SPLASH"), 1.0f, 1.2f);
                                }
                                catch (Exception exception6) {
                                    player.getWorld().playSound(player.getLocation(), Sound.valueOf((String)"BLOCK_WATER_AMBIENT"), 1.0f, 1.2f);
                                }
                            }
                        }
                    }
                    new BukkitRunnable(){

                        public void run() {
                            player.getInventory().setItemInMainHand(itemStack3);
                            String string = this$0.A.getString("messages.riptide.title", "");
                            String string2 = this$0.A.getString("messages.riptide.subtitle", "");
                            String string3 = this$0.A.getString("messages.riptide.chatMessage", "");
                            if (!string.isEmpty() || !string2.isEmpty()) {
                                player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2), 10, 70, 20);
                            }
                            if (!string3.isEmpty()) {
                                player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3));
                            }
                        }
                    }.runTaskLater((Plugin)this.this$0.C, 3L);
                }
            }.runTaskLater((Plugin)this.C, 1L);
            return;
        }
        if (player.hasMetadata("trojzab_temporary_arrow_slot")) {
            // empty if block
        }
        int n4 = this.A.getInt("cooldown", 30);
        long l5 = System.currentTimeMillis() + (long)n4 * 1000L;
        player.setMetadata("trojzab_posejdona_arrow_cooldown", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)l5));
        try {
            if (this.C instanceof Main && ((Main)((Object)(object = (Main)this.C))).getActionbarManager() != null && ((Main)((Object)object)).getActionbarManager().isEnabled()) {
                ((Main)((Object)object)).getActionbarManager().registerCooldown(player, "trojzabposejdona", n4);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        try {
            object = (Projectile)entityShootBowEvent.getProjectile();
            if (object != null) {
                object.setMetadata("trojzabPosejdona", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
                object.setMetadata("trojzabShooter", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)player.getUniqueId().toString()));
                float f3 = entityShootBowEvent.getForce();
                object.setMetadata("trojzabForce", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)Float.valueOf((float)f3)));
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (player.hasMetadata("trojzab_temporary_arrow_slot")) {
            player.setMetadata("trojzab_shot_executed", (MetadataValue)new FixedMetadataValue((Plugin)this.C, (Object)true));
        }
        object = this.A.getString("messages.shoot.title", "");
        String string8 = this.A.getString("messages.shoot.subtitle", "");
        String string9 = this.A.getString("messages.shoot.chatMessage", "");
        if (!object.isEmpty() || !string8.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object), me.anarchiacore.customitems.stormitemy.utils.color.A.C(string8), 10, 70, 20);
        }
        if (!string9.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string9));
        }
        new BukkitRunnable(this){
            final /* synthetic */ k this$0;
            {
                this.this$0 = k2;
            }

            public void run() {
                for (int i2 = 0; i2 < player.getInventory().getSize(); ++i2) {
                    ItemStack itemStack = player.getInventory().getItem(i2);
                    if (itemStack == null || itemStack.getType() != Material.BOW || itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals((Object)string2)) continue;
                    player.getInventory().setItem(i2, null);
                }
            }
        }.runTaskLater((Plugin)this.C, 1L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        Object object;
        Object object22;
        if (!(projectileHitEvent.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow)projectileHitEvent.getEntity();
        if (!arrow.hasMetadata("trojzabPosejdona")) {
            return;
        }
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }
        if (this.C instanceof Main && ((Main)this.C).isItemDisabledByKey("trojzabposejdona")) {
            return;
        }
        Player player = (Player)arrow.getShooter();
        Location location = arrow.getLocation();
        boolean bl = false;
        boolean bl2 = false;
        try {
            if (this.C instanceof Main) {
                Main main = (Main)this.C;
                if (main.isPlayerInBlockedRegion(player)) {
                    bl = true;
                }
                if (main.isTargetBlockInProtectedRegion(location)) {
                    bl2 = true;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (bl) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
            return;
        }
        if (bl2) {
            me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
            return;
        }
        if (this.A.getBoolean("lightning.enabled", true)) {
            location.getWorld().strikeLightningEffect(location);
            try {
                location.getWorld().playSound(location, Sound.valueOf((String)"ENTITY_LIGHTNING_BOLT_THUNDER"), 1.0f, 1.0f);
            }
            catch (Exception exception) {
                try {
                    location.getWorld().playSound(location, Sound.valueOf((String)"ENTITY_LIGHTNING_BOLT_IMPACT"), 1.0f, 1.0f);
                }
                catch (Exception exception2) {
                    location.getWorld().playSound(location, Sound.valueOf((String)"ENTITY_GENERIC_EXPLODE"), 1.0f, 1.0f);
                }
            }
            try {
                location.getWorld().playSound(location, Sound.valueOf((String)"ITEM_TRIDENT_THUNDER"), 1.0f, 1.2f);
            }
            catch (Exception exception) {
                try {
                    location.getWorld().playSound(location, Sound.valueOf((String)"ITEM_TRIDENT_HIT"), 1.0f, 1.2f);
                }
                catch (Exception exception3) {
                    location.getWorld().playSound(location, Sound.valueOf((String)"ENTITY_PLAYER_SPLASH"), 1.0f, 1.2f);
                }
            }
            try {
                location.getWorld().spawnParticle(Particle.valueOf((String)"EXPLOSION_LARGE"), location, 2, 0.5, 0.5, 0.5);
            }
            catch (Exception exception) {
                try {
                    location.getWorld().spawnParticle(Particle.valueOf((String)"EXPLOSION_NORMAL"), location, 5, 0.5, 0.5, 0.5);
                }
                catch (Exception exception4) {
                    location.getWorld().spawnParticle(Particle.valueOf((String)"CLOUD"), location, 10, 0.5, 0.5, 0.5);
                }
            }
            try {
                location.getWorld().spawnParticle(Particle.valueOf((String)"ELECTRIC_SPARK"), location, 30, 1.0, 1.0, 1.0, 0.1);
            }
            catch (Exception exception) {
                try {
                    location.getWorld().spawnParticle(Particle.valueOf((String)"CRIT"), location, 20, 1.0, 1.0, 1.0, 0.1);
                }
                catch (Exception exception5) {
                    location.getWorld().spawnParticle(Particle.valueOf((String)"SPELL"), location, 15, 1.0, 1.0, 1.0, 0.1);
                }
            }
        }
        double d2 = this.A.getDouble("aoe.radius", 8.0);
        double d3 = this.A.getDouble("aoe.damage", 10.0);
        double d4 = this.A.getDouble("knockback.strength", 2.5);
        for (Object object22 : location.getWorld().getNearbyEntities(location, d2, d2, d2)) {
            Main main;
            if (!(object22 instanceof LivingEntity) || object22.equals((Object)player)) continue;
            object = (LivingEntity)object22;
            double d5 = object22.getLocation().distance(location);
            boolean bl3 = false;
            if (object22 instanceof Player) {
                try {
                    if (this.C instanceof Main && (main = (Main)this.C).isPlayerInBlockedRegion((Player)object22)) {
                        bl3 = true;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (bl3) continue;
            object.damage(d3);
            object22.getWorld().strikeLightningEffect(object22.getLocation());
            main = object22.getLocation().toVector().subtract(location.toVector());
            if (main.lengthSquared() > 1.0E-4) {
                main = main.normalize().multiply(d4);
                main.setY(Math.max((double)main.getY(), (double)0.5));
                if (Double.isFinite((double)main.getX()) && Double.isFinite((double)main.getY()) && Double.isFinite((double)main.getZ())) {
                    object22.setVelocity((Vector)main);
                }
            }
            try {
                object22.getWorld().spawnParticle(Particle.valueOf((String)"WATER_SPLASH"), object22.getLocation().add(0.0, 1.0, 0.0), 20, 0.5, 0.5, 0.5, 0.1);
            }
            catch (Exception exception) {
                try {
                    object22.getWorld().spawnParticle(Particle.valueOf((String)"SPLASH"), object22.getLocation().add(0.0, 1.0, 0.0), 20, 0.5, 0.5, 0.5, 0.1);
                }
                catch (Exception exception6) {
                    object22.getWorld().spawnParticle(Particle.valueOf((String)"CLOUD"), object22.getLocation().add(0.0, 1.0, 0.0), 15, 0.5, 0.5, 0.5, 0.1);
                }
            }
            try {
                object22.getWorld().spawnParticle(Particle.valueOf((String)"NAUTILUS"), object22.getLocation().add(0.0, 1.0, 0.0), 10, 0.3, 0.3, 0.3, 0.05);
            }
            catch (Exception exception) {
                try {
                    object22.getWorld().spawnParticle(Particle.valueOf((String)"WATER_BUBBLE"), object22.getLocation().add(0.0, 1.0, 0.0), 10, 0.3, 0.3, 0.3, 0.05);
                }
                catch (Exception exception7) {
                    object22.getWorld().spawnParticle(Particle.valueOf((String)"SPELL"), object22.getLocation().add(0.0, 1.0, 0.0), 8, 0.3, 0.3, 0.3, 0.05);
                }
            }
        }
        Object object3 = this.A.getString("messages.hit.title", "");
        object22 = this.A.getString("messages.hit.subtitle", "");
        object = this.A.getString("messages.hit.chatMessage", "");
        if (!object3.isEmpty() || !object22.isEmpty()) {
            player.sendTitle(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object3), me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object22), 10, 70, 20);
        }
        if (!object.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C((String)object));
        }
    }

    public ItemStack getItem() {
        Material material;
        try {
            material = Material.valueOf((String)this.A.getString("material", "BOW"));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.BOW;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.A.getString("name", "&3&lTr\u00f3jz\u0105b posejdona"));
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
                Enchantment enchantment = null;
                try {
                    enchantment = Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)string2.toLowerCase()));
                }
                catch (Exception exception) {
                    try {
                        enchantment = Enchantment.getByName((String)string2);
                    }
                    catch (Exception exception2) {
                        // empty catch block
                    }
                }
                if (enchantment == null) continue;
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

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent) {
        Player player = playerDropItemEvent.getPlayer();
        ItemStack itemStack = playerDropItemEvent.getItemDrop().getItemStack();
        if (player.hasMetadata("trojzab_temporary_arrow_slot") && itemStack.getType() == Material.ARROW) {
            playerDropItemEvent.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        if (player.hasMetadata("trojzab_temporary_arrow_slot")) {
            int n2 = ((MetadataValue)player.getMetadata("trojzab_temporary_arrow_slot").get(0)).asInt();
            ItemStack itemStack = player.getInventory().getItem(n2);
            if (itemStack != null && itemStack.getType() == Material.ARROW) {
                if (inventoryClickEvent.getRawSlot() == n2 || inventoryClickEvent.getSlot() == n2) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
                ItemStack itemStack2 = inventoryClickEvent.getCursor();
                if (itemStack2 != null && itemStack2.getType() == Material.ARROW) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
                if (inventoryClickEvent.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || inventoryClickEvent.getAction() == InventoryAction.COLLECT_TO_CURSOR || inventoryClickEvent.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }
            }
        }
    }

    public void reload() {
        try {
            YamlConfiguration yamlConfiguration;
            ConfigurationSection configurationSection;
            File file = new File(this.C.getDataFolder(), "items/trojzabposejdona.yml");
            if (file.exists() && (configurationSection = (yamlConfiguration = YamlConfiguration.loadConfiguration((File)file)).getConfigurationSection("trojzab_posejdona")) != null) {
                Field field = this.getClass().getDeclaredField("config");
                field.setAccessible(true);
                field.set((Object)this, (Object)configurationSection);
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
        String string2 = configurationSection.getString("sound", "ITEM_TRIDENT_THROW");
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

