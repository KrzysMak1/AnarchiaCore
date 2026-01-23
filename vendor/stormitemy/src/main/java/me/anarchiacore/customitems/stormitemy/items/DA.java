/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Class
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
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Random
 *  java.util.UUID
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.EvokerFangs
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Villager
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
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

public class DA
implements Listener {
    private final Main D;
    private final ConfigurationSection B;
    private final Map<UUID, Long> E = new HashMap();
    private final Map<UUID, Long> A = new HashMap();
    private final Map<UUID, BukkitRunnable> G = new HashMap();
    private boolean F = false;
    private boolean C = false;

    public DA(Main main) {
        YamlConfiguration yamlConfiguration;
        File file;
        this.D = main;
        File file2 = new File(main.getDataFolder(), "items");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (!(file = new File(file2, "rozdzkailuzjonisty.yml")).exists()) {
            try {
                file.createNewFile();
                yamlConfiguration = new YamlConfiguration();
                String string = "=== Konfiguracja przedmiotu 'R\u00f3\u017cd\u017cka iluzjonisty' ===\nOpis: Plik konfiguracyjny okre\u015bla w\u0142a\u015bciwo\u015bci przedmiotu eventowego 'R\u00f3\u017cd\u017cka iluzjonisty'.\nDost\u0119pne zmienne (placeholdery):\n  {TIME}         - pozosta\u0142y czas cooldownu (w sekundach)\n  {PLAYER}      - nazwa gracza, kt\u00f3ry u\u017cy\u0142 przedmiotu\n";
                yamlConfiguration.options().header(string);
                yamlConfiguration.options().copyHeader(true);
                yamlConfiguration.set("rozdzka_iluzjonisty.material", (Object)"GOLDEN_HOE");
                yamlConfiguration.set("rozdzka_iluzjonisty.name", (Object)"&5&lR\u00f3\u017cd\u017cka iluzjonisty");
                yamlConfiguration.set("rozdzka_iluzjonisty.lore", (Object)Arrays.asList((Object[])new String[]{"&7", "&8 \u00bb &7Jest to przedmiot zdobyty podczas", "&8 \u00bb &fwydarzenia cyrkowego 2025&7!", "&7", "&8 \u00bb &7LPM: przywo\u0142uje &dSzcz\u0119ki Evokera", "&8 \u00bb &7atakuj\u0105ce przeciwnik\u00f3w na wprost.", "&7", "&8 \u00bb &7PPM: &dznikasz na &e4s &7i tworzysz", "&8 \u00bb &dprawdziwego bota &7kt\u00f3ry biega przed siebie!", "&7"}));
                yamlConfiguration.set("rozdzka_iluzjonisty.customModelData", (Object)1);
                yamlConfiguration.set("rozdzka_iluzjonisty.leftClickCooldown", (Object)60);
                yamlConfiguration.set("rozdzka_iluzjonisty.rightClickCooldown", (Object)120);
                yamlConfiguration.set("rozdzka_iluzjonisty.enchantments", (Object)Arrays.asList((Object[])new Object[0]));
                yamlConfiguration.set("rozdzka_iluzjonisty.flags", (Object)Arrays.asList((Object[])new String[]{"HIDE_ATTRIBUTES", "HIDE_ENCHANTS", "HIDE_UNBREAKABLE", "HIDE_POTION_EFFECTS", "HIDE_DESTROYS", "HIDE_PLACED_ON"}));
                yamlConfiguration.set("rozdzka_iluzjonisty.unbreakable", (Object)true);
                yamlConfiguration.set("rozdzka_iluzjonisty.evokerFangs.waves", (Object)5);
                yamlConfiguration.set("rozdzka_iluzjonisty.evokerFangs.fangsPerWave", (Object)3);
                yamlConfiguration.set("rozdzka_iluzjonisty.evokerFangs.distanceBetweenWaves", (Object)2.0);
                yamlConfiguration.set("rozdzka_iluzjonisty.evokerFangs.damage", (Object)8.0);
                yamlConfiguration.set("rozdzka_iluzjonisty.evokerFangs.delayBetweenWaves", (Object)5);
                yamlConfiguration.set("rozdzka_iluzjonisty.invisibility.duration", (Object)4);
                yamlConfiguration.set("rozdzka_iluzjonisty.clone.runDuration", (Object)4);
                yamlConfiguration.set("rozdzka_iluzjonisty.clone.speed", (Object)0.3);
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.evokerFangs.title", (Object)"&5&lR\u00d3\u017bD\u017bKA ILUZJONISTY");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.evokerFangs.subtitle", (Object)"&7Przywo\u0142a\u0142e\u015b &dSzcz\u0119ki Evokera&7!");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.evokerFangs.chatMessage", (Object)"");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.invisibility.title", (Object)"&5&lR\u00d3\u017bD\u017bKA ILUZJONISTY");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.invisibility.subtitle", (Object)"&7Sta\u0142e\u015b si\u0119 &dniewidzialny&7!");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.invisibility.chatMessage", (Object)"");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.cooldown.leftClick", (Object)"");
                yamlConfiguration.set("rozdzka_iluzjonisty.messages.cooldown.rightClick", (Object)"");
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.lpm.enabled", (Object)true);
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.lpm.sound", (Object)"ENTITY_ILLUSIONER_CAST_SPELL");
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.lpm.volume", (Object)1.0);
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.lpm.pitch", (Object)1.0);
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.ppm.enabled", (Object)true);
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.ppm.sound", (Object)"ENTITY_ILLUSIONER_PREPARE_BLINDNESS");
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.ppm.volume", (Object)1.0);
                yamlConfiguration.set("rozdzka_iluzjonisty.sounds.ppm.pitch", (Object)1.2);
                yamlConfiguration.save(file);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
        this.B = yamlConfiguration.getConfigurationSection("rozdzka_iluzjonisty");
        this.C = Bukkit.getPluginManager().getPlugin("Citizens") != null;
        this.F = false;
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
        String string2 = this.B.getString("material", "GOLDEN_HOE");
        try {
            material = Material.valueOf((String)string2);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            material = Material.GOLDEN_HOE;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&5&lR\u00f3\u017cd\u017cka iluzjonisty"));
        itemMeta.setDisplayName(string3);
        if (this.B.contains("customModelData")) {
            itemMeta.setCustomModelData(Integer.valueOf((int)this.B.getInt("customModelData")));
        }
        if (this.B.contains("lore")) {
            list = this.B.getStringList("lore");
            Object object = new ArrayList();
            stringArray2 = list.iterator();
            while (stringArray2.hasNext()) {
                string = (String)stringArray2.next();
                object.add((Object)me.anarchiacore.customitems.stormitemy.utils.color.A.C(string));
            }
            itemMeta.setLore((List)object);
        }
        if (this.B.contains("flags")) {
            list = this.B.getStringList("flags");
            for (String[] stringArray2 : list) {
                try {
                    string = ItemFlag.valueOf((String)stringArray2);
                    itemMeta.addItemFlags(new ItemFlag[]{string});
                }
                catch (IllegalArgumentException illegalArgumentException) {}
            }
        }
        if (this.B.getBoolean("unbreakable", false)) {
            itemMeta.setUnbreakable(true);
        }
        if (this.B.contains("enchantments")) {
            for (Object object : this.B.getStringList("enchantments")) {
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
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }
        String string2 = itemStack.getItemMeta().getDisplayName();
        if (!string2.equals((Object)(string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&5&lR\u00f3\u017cd\u017cka iluzjonisty"))))) {
            return;
        }
        if (this.D.isItemDisabledByKey("rozdzkailuzjonisty")) {
            return;
        }
        if (this.B.getBoolean("regions.disabledInSpawn", true)) {
            try {
                if (this.D.isPlayerInBlockedRegion(player)) {
                    me.anarchiacore.customitems.stormitemy.utils.language.A.A(player);
                    return;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (playerInteractEvent.getAction().toString().contains((CharSequence)"LEFT_CLICK")) {
            playerInteractEvent.setCancelled(true);
            this.B(player, itemStack);
        } else if (playerInteractEvent.getAction().toString().contains((CharSequence)"RIGHT_CLICK")) {
            playerInteractEvent.setCancelled(true);
            this.A(player, itemStack);
        }
    }

    private void B(Player player, ItemStack itemStack) {
        Object object;
        long l2;
        UUID uUID = player.getUniqueId();
        long l3 = System.currentTimeMillis();
        int n2 = this.B.getInt("leftClickCooldown", 60);
        if (this.E.containsKey((Object)uUID) && (l2 = (Long)this.E.get((Object)uUID) + (long)n2 * 1000L - l3) > 0L) {
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.cooldown.leftClick", "").replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)((int)(l2 / 1000L)))));
            if (!string.isEmpty()) {
                player.sendMessage(string);
            }
            return;
        }
        this.E.put((Object)uUID, (Object)l3);
        try {
            object = this.D;
            if (((Main)((Object)object)).getActionbarManager() != null && ((Main)((Object)object)).getActionbarManager().isEnabled()) {
                ((Main)((Object)object)).getActionbarManager().registerCooldown(player, "rozdzka_iluzjonisty_lpm", n2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        object = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.evokerFangs.title", ""));
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.evokerFangs.subtitle", ""));
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.evokerFangs.chatMessage", ""));
        if (!object.isEmpty() || !string.isEmpty()) {
            player.sendTitle((String)object, string, 10, 70, 20);
        }
        if (!string2.isEmpty()) {
            player.sendMessage(string2);
        }
        this.C(player);
    }

    private void A(Player player, ItemStack itemStack) {
        Object object;
        long l2;
        UUID uUID = player.getUniqueId();
        long l3 = System.currentTimeMillis();
        int n2 = this.B.getInt("rightClickCooldown", 120);
        if (this.A.containsKey((Object)uUID) && (l2 = (Long)this.A.get((Object)uUID) + (long)n2 * 1000L - l3) > 0L) {
            String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.cooldown.rightClick", "").replace((CharSequence)"{TIME}", (CharSequence)String.valueOf((int)((int)(l2 / 1000L)))));
            if (!string.isEmpty()) {
                player.sendMessage(string);
            }
            return;
        }
        this.A.put((Object)uUID, (Object)l3);
        try {
            object = this.D;
            if (((Main)((Object)object)).getActionbarManager() != null && ((Main)((Object)object)).getActionbarManager().isEnabled()) {
                ((Main)((Object)object)).getActionbarManager().registerCooldown(player, "rozdzka_iluzjonisty_ppm", n2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        object = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.invisibility.title", ""));
        String string = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.invisibility.subtitle", ""));
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("messages.invisibility.chatMessage", ""));
        if (!object.isEmpty() || !string.isEmpty()) {
            player.sendTitle((String)object, string, 10, 70, 20);
        }
        if (!string2.isEmpty()) {
            player.sendMessage(string2);
        }
        this.A(player);
    }

    private void C(final Player player) {
        final int n2 = this.B.getInt("evokerFangs.waves", 5);
        final int n3 = this.B.getInt("evokerFangs.fangsPerWave", 3);
        final double d2 = this.B.getDouble("evokerFangs.distanceBetweenWaves", 2.0);
        final double d3 = this.B.getDouble("evokerFangs.damage", 8.0);
        int n4 = this.B.getInt("evokerFangs.delayBetweenWaves", 5);
        final Vector vector = player.getLocation().getDirection().normalize();
        final Location location = player.getLocation().add(vector.clone().multiply(1.5));
        new BukkitRunnable(this){
            int currentWave = 0;
            final /* synthetic */ DA this$0;
            {
                this.this$0 = dA;
            }

            public void run() {
                if (this.currentWave >= n2) {
                    this.cancel();
                    return;
                }
                Location location3 = location.clone().add(vector.clone().multiply((double)this.currentWave * d2));
                for (int i2 = 0; i2 < n3; ++i2) {
                    Vector vector2;
                    Location location2 = location3.clone();
                    if (n3 > 1) {
                        vector2 = vector.clone().crossProduct(new Vector(0, 1, 0)).normalize();
                        double d22 = ((double)i2 - (double)(n3 - 1) / 2.0) * 1.5;
                        location2.add(vector2.multiply(d22));
                    }
                    location2.getWorld().spawnParticle(Particle.FLAME, location2.add(0.0, 0.5, 0.0), 10, 0.3, 0.3, 0.3, 0.1);
                    vector2 = (EvokerFangs)location2.getWorld().spawnEntity(location2, EntityType.EVOKER_FANGS);
                    vector2.setOwner((LivingEntity)player);
                    for (Entity entity : location2.getWorld().getNearbyEntities(location2, 1.5, 1.5, 1.5)) {
                        if (!(entity instanceof LivingEntity) || entity == player || entity instanceof ArmorStand || entity instanceof Villager || entity.hasMetadata("NPC")) continue;
                        LivingEntity livingEntity = (LivingEntity)entity;
                        boolean bl = true;
                        if (this.this$0.B.getBoolean("regions.evokerFangsNoDamageInSpawn", true)) {
                            try {
                                Player player2;
                                if (livingEntity instanceof Player && this.this$0.D.isPlayerInBlockedRegion(player2 = (Player)livingEntity)) {
                                    bl = false;
                                }
                            }
                            catch (Exception exception) {
                                // empty catch block
                            }
                        }
                        if (!bl) continue;
                        livingEntity.damage(d3, (Entity)player);
                    }
                }
                ++this.currentWave;
            }
        }.runTaskTimer((Plugin)this.D, 0L, (long)n4);
    }

    private void A(final Player player) {
        final UUID uUID = player.getUniqueId();
        int n2 = this.B.getInt("invisibility.duration", 4);
        int n3 = this.B.getInt("clone.runDuration", 4);
        double d2 = this.B.getDouble("clone.speed", 0.3);
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            if (player2.equals((Object)player)) continue;
            player2.hidePlayer((Plugin)this.D, player);
        }
        if (this.C) {
            this.B(player, n3, d2);
        } else {
            player.sendMessage(String.valueOf((Object)ChatColor.RED) + "Brak pluginu Citizens! R\u00f3\u017cd\u017cka Iluzjonisty wymaga Citizens do dzia\u0142ania.");
        }
        Object object = new BukkitRunnable(this){
            final /* synthetic */ DA this$0;
            {
                this.this$0 = dA;
            }

            public void run() {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.equals((Object)player)) continue;
                    player2.showPlayer((Plugin)this.this$0.D, player);
                }
                this.this$0.G.remove((Object)uUID);
            }
        };
        object.runTaskLater((Plugin)this.D, (long)n2 * 20L);
        this.G.put((Object)uUID, object);
    }

    private void A(final Player player, final int n2, double d2) {
        try {
            String string;
            Object object;
            Plugin plugin = Bukkit.getPluginManager().getPlugin("FancyNpcs");
            if (plugin == null) {
                throw new Exception("FancyNpcs plugin nie jest za\u0142adowany");
            }
            Object object2 = null;
            try {
                object2 = plugin.getClass().getMethod("getNpcManager", new Class[0]).invoke((Object)plugin, new Object[0]);
            }
            catch (Exception exception) {
                try {
                    object2 = plugin.getClass().getMethod("getManager", new Class[0]).invoke((Object)plugin, new Object[0]);
                }
                catch (Exception exception2) {
                    try {
                        object2 = plugin.getClass().getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    }
                    catch (Exception exception3) {
                        try {
                            object = plugin.getClass().getDeclaredField("npcManager");
                            object.setAccessible(true);
                            object2 = object.get((Object)plugin);
                        }
                        catch (Exception exception4) {
                            throw new Exception("Nie mo\u017cna pobra\u0107 NpcManager - \u017cadna metoda nie zadzia\u0142a\u0142a");
                        }
                    }
                }
            }
            if (object2 == null) {
                throw new Exception("NpcManager jest null");
            }
            Location location = player.getLocation().clone().add(1.0, 0.0, 0.0);
            String string2 = string = player.getDisplayName();
            object = object2.getClass().getMethod("createNPC", new Class[]{String.class, Location.class}).invoke(object2, new Object[]{string2, location});
            if (object == null) {
                throw new Exception("createNPC zwr\u00f3ci\u0142 null");
            }
            try {
                try {
                    object.getClass().getMethod("setHeadName", new Class[]{String.class}).invoke(object, new Object[]{player.getName()});
                }
                catch (Exception exception) {
                    try {
                        object.getClass().getMethod("setName", new Class[]{String.class}).invoke(object, new Object[]{player.getName()});
                    }
                    catch (Exception exception5) {
                        try {
                            object.getClass().getMethod("setPlayerName", new Class[]{String.class}).invoke(object, new Object[]{player.getName()});
                        }
                        catch (Exception exception6) {
                            this.D.getLogger().warning("Nie mo\u017cna ustawi\u0107 skina gracza - wszystkie metody nie zadzia\u0142a\u0142y");
                        }
                    }
                }
            }
            catch (Exception exception) {
                this.D.getLogger().warning("Nie mo\u017cna ustawi\u0107 skina gracza: " + exception.getMessage());
            }
            final Object object3 = object2;
            final Object object4 = object;
            BukkitRunnable bukkitRunnable = new BukkitRunnable(this){
                long startTime = System.currentTimeMillis();
                long endTime = this.startTime + (long)n2 * 1000L;
                final /* synthetic */ DA this$0;
                {
                    this.this$0 = dA;
                }

                public void run() {
                    long l2 = System.currentTimeMillis();
                    if (l2 >= this.endTime) {
                        this.cancel();
                        try {
                            object3.getClass().getMethod("deleteNPC", new Class[]{Object.class}).invoke(object3, new Object[]{object4});
                        }
                        catch (Exception exception) {
                            this.this$0.D.getLogger().warning("B\u0142\u0105d przy usuwaniu FancyNPC klona: " + exception.getMessage());
                        }
                        return;
                    }
                    try {
                        Location location = (Location)object4.getClass().getMethod("getLocation", new Class[0]).invoke(object4, new Object[0]);
                        Location location2 = player.getLocation();
                        float f2 = location2.getYaw();
                        float f3 = f2 + 90.0f + (new Random().nextFloat() * 180.0f - 90.0f);
                        double d2 = Math.toRadians((double)f3);
                        double d3 = -Math.sin((double)d2);
                        double d4 = Math.cos((double)d2);
                        Location location3 = location.clone().add(d3 * 5.0, 0.0, d4 * 5.0);
                        object4.getClass().getMethod("setLocation", new Class[]{Location.class}).invoke(object4, new Object[]{location3});
                    }
                    catch (Exception exception) {
                        this.this$0.D.getLogger().warning("B\u0142\u0105d przy poruszaniu FancyNPC klona: " + exception.getMessage());
                    }
                }
            };
            bukkitRunnable.runTaskTimer((Plugin)this.D, 0L, 20L);
        }
        catch (Exception exception) {
            this.D.getLogger().warning("B\u0142\u0105d przy tworzeniu FancyNPC klona: " + exception.getMessage());
        }
    }

    private void B(final Player player, final int n2, final double d2) {
        try {
            Object object;
            Object object2;
            Object object3;
            String string;
            Class clazz = Class.forName((String)"net.citizensnpcs.api.CitizensAPI");
            Object object4 = clazz.getMethod("getNPCRegistry", new Class[0]).invoke(null, new Object[0]);
            Location location = player.getLocation().clone().add(1.0, 0.0, 0.0);
            String string2 = string = player.getDisplayName();
            Object object5 = null;
            try {
                object5 = object4.getClass().getMethod("createNPC", new Class[]{Class.forName((String)"org.bukkit.entity.EntityType"), String.class}).invoke(object4, new Object[]{EntityType.PLAYER, string2});
            }
            catch (Exception exception) {
                try {
                    object5 = object4.getClass().getMethod("createNPC", new Class[]{Class.forName((String)"net.citizensnpcs.api.npc.NPCType"), String.class}).invoke(object4, new Object[]{Class.forName((String)"net.citizensnpcs.api.npc.NPCType").getField("PLAYER").get(null), string2});
                }
                catch (Exception exception2) {
                    throw new Exception("Nie mo\u017cna stworzy\u0107 NPC - \u017cadna metoda nie zadzia\u0142a\u0142a: " + exception.getMessage() + " | " + exception2.getMessage());
                }
            }
            if (object5 == null) {
                throw new Exception("createNPC zwr\u00f3ci\u0142 null");
            }
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.D, () -> {
                try {
                    Class clazz = Class.forName((String)"net.citizensnpcs.util.SkinFetcher");
                    clazz.getMethod("fetchSkin", new Class[]{String.class}).invoke(null, new Object[]{player.getName()});
                }
                catch (Exception exception) {
                    // empty catch block
                }
            });
            try {
                try {
                    object3 = Class.forName((String)"net.citizensnpcs.trait.SkinTrait");
                    object2 = object5.getClass().getMethod("getOrAddTrait", new Class[]{Class.class}).invoke(object5, new Object[]{object3});
                    object2.getClass().getMethod("setSkinName", new Class[]{String.class}).invoke(object2, new Object[]{player.getName()});
                }
                catch (Exception exception) {
                    try {
                        object2 = Class.forName((String)"net.citizensnpcs.api.util.Skin");
                        object = object2.getMethod("get", new Class[]{String.class}).invoke(null, new Object[]{player.getName()});
                        Object object6 = object5.getClass().getMethod("data", new Class[0]).invoke(object5, new Object[0]);
                        object6.getClass().getMethod("set", new Class[]{String.class, Object.class}).invoke(object6, new Object[]{"player-skin", object});
                    }
                    catch (Exception exception3) {
                        try {
                            object = Class.forName((String)"net.citizensnpcs.trait.SkinTrait");
                            Object object7 = object5.getClass().getMethod("getOrAddTrait", new Class[]{Class.class}).invoke(object5, new Object[]{object});
                            object7.getClass().getMethod("setName", new Class[]{String.class}).invoke(object7, new Object[]{player.getName()});
                        }
                        catch (Exception exception4) {
                            this.D.getLogger().warning("Nie mo\u017cna ustawi\u0107 skina gracza - wszystkie metody nie zadzia\u0142a\u0142y");
                        }
                    }
                }
            }
            catch (Exception exception) {
                this.D.getLogger().warning("Nie mo\u017cna ustawi\u0107 skina gracza: " + exception.getMessage());
            }
            try {
                object3 = Class.forName((String)"net.citizensnpcs.trait.Gravity");
                object2 = object5.getClass().getMethod("getOrAddTrait", new Class[]{Class.class}).invoke(object5, new Object[]{object3});
                try {
                    object2.getClass().getMethod("setHasGravity", new Class[]{Boolean.TYPE}).invoke(object2, new Object[]{true});
                }
                catch (Exception exception) {
                    try {
                        object2.getClass().getMethod("gravitate", new Class[]{Boolean.TYPE}).invoke(object2, new Object[]{true});
                    }
                    catch (Exception exception5) {}
                }
            }
            catch (Exception exception) {
                this.D.getLogger().warning("Nie mo\u017cna doda\u0107 grawitacji do NPC: " + exception.getMessage());
            }
            object5.getClass().getMethod("spawn", new Class[]{Location.class}).invoke(object5, new Object[]{location});
            object3 = object5;
            object2 = object4;
            object = new BukkitRunnable(this){
                long startTime = System.currentTimeMillis();
                long endTime = this.startTime + (long)n2 * 1000L;
                Random random = new Random();
                final /* synthetic */ DA this$0;
                {
                    this.this$0 = dA;
                }

                public void run() {
                    long l2 = System.currentTimeMillis();
                    if (l2 >= this.endTime) {
                        this.cancel();
                        try {
                            try {
                                object3.getClass().getMethod("destroy", new Class[0]).invoke(object3, new Object[0]);
                            }
                            catch (Exception exception) {
                                try {
                                    object3.getClass().getMethod("despawn", new Class[0]).invoke(object3, new Object[0]);
                                }
                                catch (Exception exception2) {
                                    object2.getClass().getMethod("deregister", new Class[]{object3.getClass()}).invoke(object2, new Object[]{object3});
                                }
                            }
                        }
                        catch (Exception exception) {
                            this.this$0.D.getLogger().warning("B\u0142\u0105d przy usuwaniu Citizens klona: " + exception.getMessage());
                        }
                        return;
                    }
                    try {
                        Object object = object3.getClass().getMethod("getEntity", new Class[0]).invoke(object3, new Object[0]);
                        Location location = (Location)object.getClass().getMethod("getLocation", new Class[0]).invoke(object, new Object[0]);
                        Location location2 = player.getLocation();
                        float f2 = location2.getYaw();
                        float f3 = f2 + 90.0f + (this.random.nextFloat() * 180.0f - 90.0f);
                        double d22 = Math.toRadians((double)f3);
                        double d3 = -Math.sin((double)d22);
                        double d4 = Math.cos((double)d22);
                        Location location3 = location.clone().add(d3 * d2, 0.0, d4 * d2);
                        object.getClass().getMethod("teleport", new Class[]{Location.class}).invoke(object, new Object[]{location3});
                    }
                    catch (Exception exception) {
                        this.this$0.D.getLogger().warning("B\u0142\u0105d przy poruszaniu Citizens klona: " + exception.getMessage());
                    }
                }
            };
            object.runTaskTimer((Plugin)this.D, 0L, 2L);
        }
        catch (Exception exception) {
            this.D.getLogger().warning("B\u0142\u0105d przy tworzeniu Citizens klona: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public boolean isItemValid(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return false;
        }
        String string = itemStack.getItemMeta().getDisplayName();
        String string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(this.B.getString("name", "&5&lR\u00f3\u017cd\u017cka iluzjonisty"));
        return string.equals((Object)string2);
    }

    private void A(Player player, String string) {
        ConfigurationSection configurationSection = this.B.getConfigurationSection("sounds." + string);
        if (configurationSection == null || !configurationSection.getBoolean("enabled", true)) {
            return;
        }
        String string2 = configurationSection.getString("sound", "ENTITY_ILLUSIONER_CAST_SPELL");
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

