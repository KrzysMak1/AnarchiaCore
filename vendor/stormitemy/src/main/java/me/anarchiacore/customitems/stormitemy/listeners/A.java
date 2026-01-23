/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Number
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Random
 *  java.util.UUID
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Particle
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.potion.PotionEffectType
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.ui.gui.E;

public class A
implements Listener {
    private final Main D;
    private final E A;
    private final Random C = new Random();
    private final Map<Integer, String> E = new HashMap();
    private final Map<UUID, Map<String, Long>> B = new HashMap();

    public A(Main main, E e2) {
        this.D = main;
        this.A = e2;
    }

    private boolean C(Player player, String string) {
        UUID uUID = player.getUniqueId();
        if (!this.B.containsKey((Object)uUID)) {
            return false;
        }
        Map map = (Map)this.B.get((Object)uUID);
        if (!map.containsKey((Object)string)) {
            return false;
        }
        long l2 = (Long)map.get((Object)string);
        if (System.currentTimeMillis() >= l2) {
            map.remove((Object)string);
            return false;
        }
        return true;
    }

    private void A(Player player, String string) {
        int n2 = this.A.J(string);
        if (n2 <= 0) {
            return;
        }
        UUID uUID2 = player.getUniqueId();
        ((Map)this.B.computeIfAbsent((Object)uUID2, uUID -> new HashMap())).put((Object)string, (Object)(System.currentTimeMillis() + (long)n2 * 1000L));
        if (this.D.getActionbarManager() != null) {
            String string2 = this.A.D(string);
            this.D.getActionbarManager().registerCooldown(player, "custom_" + string, n2);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onProjectileLaunch(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!(projectileLaunchEvent.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectileLaunchEvent.getEntity().getShooter();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (!this.A(itemStack.getType())) {
            return;
        }
        String string = this.A(itemStack);
        if (string == null) {
            return;
        }
        this.E.put((Object)projectileLaunchEvent.getEntity().getEntityId(), (Object)string);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        String string;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getEntity();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        String string2 = this.A(itemStack);
        if (string2 == null) {
            return;
        }
        if (this.A(itemStack.getType())) {
            return;
        }
        if (itemStack.getType().isBlock() && (string = this.A.E(string2)).equals((Object)"BLOCK_PLACE")) {
            return;
        }
        if (this.A.B(string2)) {
            return;
        }
        if (this.C(player, string2)) {
            return;
        }
        int n2 = this.A.C(string2);
        if (this.C.nextInt(100) >= n2) {
            return;
        }
        this.A(player, string2);
        List<Map<String, Object>> list = this.A.M(string2);
        for (Map map : list) {
            this.A(player, livingEntity, (Map<String, Object>)map, entityDamageByEntityEvent);
        }
        List<Map<String, Object>> list2 = this.A.F(string2);
        for (Map map : list2) {
            this.A(player, livingEntity, (Map<String, Object>)map);
        }
        this.A(player, livingEntity, string2);
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        String string = this.A(itemStack);
        if (string == null) {
            return;
        }
        if (this.A.B(string)) {
            return;
        }
        String string2 = this.A.E(string);
        Action action = playerInteractEvent.getAction();
        boolean bl = false;
        if (string2.equals((Object)"LEFT_CLICK")) {
            bl = action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
        } else if (string2.equals((Object)"RIGHT_CLICK")) {
            boolean bl2 = bl = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
        }
        if (!bl) {
            return;
        }
        if (this.C(player, string)) {
            return;
        }
        int n2 = this.A.C(string);
        if (this.C.nextInt(100) >= n2) {
            return;
        }
        this.A(player, string);
        List<Map<String, Object>> list = this.A.M(string);
        for (Map map : list) {
            this.B(player, (Map<String, Object>)map);
        }
        List<Map<String, Object>> list2 = this.A.F(string);
        for (Map map : list2) {
            this.A(player, (Map<String, Object>)map);
        }
        this.B(player, string);
    }

    private boolean A(Material material) {
        return material == Material.BOW || material == Material.CROSSBOW || material == Material.SNOWBALL || material == Material.EGG || material == Material.ENDER_PEARL || material == Material.TRIDENT || material == Material.SPLASH_POTION || material == Material.LINGERING_POTION;
    }

    private void A(Player player, LivingEntity livingEntity, String string) {
        String string2;
        String string3;
        String string4 = player.getName();
        String string5 = livingEntity instanceof Player ? ((Player)livingEntity).getName() : livingEntity.getName();
        String string6 = this.A.C(string, "title");
        String string7 = this.A.C(string, "subtitle");
        String string8 = this.A.C(string, "chat");
        if (!string6.isEmpty() || !string7.isEmpty()) {
            string3 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string6.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5));
            string2 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string7.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5));
            player.sendTitle(string3, string2, 10, 40, 10);
        }
        if (!string8.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string8.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5)));
        }
        if (livingEntity instanceof Player) {
            string3 = (Player)livingEntity;
            string2 = this.A.E(string, "title");
            String string9 = this.A.E(string, "subtitle");
            String string10 = this.A.E(string, "chat");
            if (!string2.isEmpty() || !string9.isEmpty()) {
                String string11 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string2.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5));
                String string12 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string9.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5));
                string3.sendTitle(string11, string12, 10, 40, 10);
            }
            if (!string10.isEmpty()) {
                string3.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string10.replace((CharSequence)"{PLAYER}", (CharSequence)string4).replace((CharSequence)"{TARGET}", (CharSequence)string5)));
            }
        }
    }

    private String A(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        Map<String, ItemStack> map = this.A.B();
        for (Map.Entry entry : map.entrySet()) {
            ItemStack itemStack2 = (ItemStack)entry.getValue();
            if (itemStack2 == null || itemStack.getType() != itemStack2.getType() || !itemMeta.hasDisplayName() || !itemStack2.hasItemMeta() || !itemStack2.getItemMeta().hasDisplayName() || !itemMeta.getDisplayName().equals((Object)itemStack2.getItemMeta().getDisplayName()) || !(itemMeta.hasCustomModelData() && itemStack2.getItemMeta().hasCustomModelData() ? itemMeta.getCustomModelData() == itemStack2.getItemMeta().getCustomModelData() : !itemMeta.hasCustomModelData() && !itemStack2.getItemMeta().hasCustomModelData())) continue;
            return (String)entry.getKey();
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    private void A(Player var1_1, LivingEntity var2_2, Map<String, Object> var3_3, EntityDamageByEntityEvent var4_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private boolean A(PotionEffectType potionEffectType) {
        return potionEffectType == PotionEffectType.SPEED || potionEffectType == PotionEffectType.INCREASE_DAMAGE || potionEffectType == PotionEffectType.HEAL || potionEffectType == PotionEffectType.JUMP || potionEffectType == PotionEffectType.REGENERATION || potionEffectType == PotionEffectType.DAMAGE_RESISTANCE || potionEffectType == PotionEffectType.FIRE_RESISTANCE || potionEffectType == PotionEffectType.WATER_BREATHING || potionEffectType == PotionEffectType.INVISIBILITY || potionEffectType == PotionEffectType.NIGHT_VISION || potionEffectType == PotionEffectType.HEALTH_BOOST || potionEffectType == PotionEffectType.ABSORPTION || potionEffectType == PotionEffectType.SATURATION || potionEffectType == PotionEffectType.LUCK || potionEffectType == PotionEffectType.SLOW_FALLING || potionEffectType == PotionEffectType.CONDUIT_POWER || potionEffectType == PotionEffectType.DOLPHINS_GRACE || potionEffectType == PotionEffectType.HERO_OF_THE_VILLAGE;
    }

    /*
     * Exception decompiling
     */
    private void A(Player var1_1, LivingEntity var2_2, Map<String, Object> var3_3) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void A(World world, Location location, Particle particle, Map<String, Object> map) {
        int n2 = this.A(map, "count", 20);
        double d2 = this.A(map, "radius", 1.0);
        for (int i2 = 0; i2 < n2; ++i2) {
            double d3 = (this.C.nextDouble() - 0.5) * d2 * 2.0;
            double d4 = this.C.nextDouble() * d2;
            double d5 = (this.C.nextDouble() - 0.5) * d2 * 2.0;
            world.spawnParticle(particle, location.clone().add(d3, d4 + 1.0, d5), 1);
        }
    }

    private int A(Map<String, Object> map, String string, int n2) {
        Object object = map.get((Object)string);
        if (object instanceof Number) {
            return ((Number)object).intValue();
        }
        return n2;
    }

    private double A(Map<String, Object> map, String string, double d2) {
        Object object = map.get((Object)string);
        if (object instanceof Number) {
            return ((Number)object).doubleValue();
        }
        return d2;
    }

    private boolean A(Map<String, Object> map, String string, boolean bl) {
        Object object = map.get((Object)string);
        if (object instanceof Boolean) {
            return (Boolean)object;
        }
        return bl;
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent projectileHitEvent) {
        if (!(projectileHitEvent.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectileHitEvent.getEntity().getShooter();
        String string = (String)this.E.remove((Object)projectileHitEvent.getEntity().getEntityId());
        if (string == null) {
            return;
        }
        if (this.A.B(string)) {
            return;
        }
        if (this.C(player, string)) {
            return;
        }
        int n2 = this.A.C(string);
        if (this.C.nextInt(100) >= n2) {
            return;
        }
        if (projectileHitEvent.getHitEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)projectileHitEvent.getHitEntity();
            this.A(player, string);
            List<Map<String, Object>> list = this.A.M(string);
            for (Map map : list) {
                EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent((Entity)player, (Entity)livingEntity, EntityDamageEvent.DamageCause.PROJECTILE, 0.0);
                this.A(player, livingEntity, (Map<String, Object>)map, entityDamageByEntityEvent);
            }
            List<Map<String, Object>> list2 = this.A.F(string);
            for (EntityDamageByEntityEvent entityDamageByEntityEvent : list2) {
                this.A(player, livingEntity, (Map<String, Object>)entityDamageByEntityEvent);
            }
            this.A(player, livingEntity, string);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        ItemStack itemStack = blockPlaceEvent.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        String string = this.A(itemStack);
        if (string == null) {
            return;
        }
        String string2 = this.A.E(string);
        if (!string2.equals((Object)"BLOCK_PLACE")) {
            return;
        }
        if (this.A.B(string)) {
            return;
        }
        if (this.C(player, string)) {
            return;
        }
        int n2 = this.A.C(string);
        if (this.C.nextInt(100) >= n2) {
            return;
        }
        this.A(player, string);
        Location location = blockPlaceEvent.getBlockPlaced().getLocation();
        for (Entity entity : location.getWorld().getNearbyEntities(location, 10.0, 10.0, 10.0)) {
            EntityDamageByEntityEvent entityDamageByEntityEvent;
            Map map2;
            if (!(entity instanceof LivingEntity) || entity == player) continue;
            LivingEntity livingEntity = (LivingEntity)entity;
            List<Map<String, Object>> list = this.A.M(string);
            for (Map map2 : list) {
                entityDamageByEntityEvent = new EntityDamageByEntityEvent((Entity)player, (Entity)livingEntity, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, 0.0);
                this.A(player, livingEntity, (Map<String, Object>)map2, entityDamageByEntityEvent);
            }
            List<Map<String, Object>> list2 = this.A.F(string);
            map2 = list2.iterator();
            while (map2.hasNext()) {
                entityDamageByEntityEvent = (Map)map2.next();
                this.A(player, livingEntity, (Map<String, Object>)entityDamageByEntityEvent);
            }
            this.A(player, livingEntity, string);
        }
    }

    /*
     * Exception decompiling
     */
    private void B(Player var1_1, Map<String, Object> var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    private void A(Player var1_1, Map<String, Object> var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void B(Player player, String string) {
        String string2 = player.getName();
        String string3 = this.A.C(string, "title");
        String string4 = this.A.C(string, "subtitle");
        String string5 = this.A.C(string, "chat");
        if (!string3.isEmpty() || !string4.isEmpty()) {
            String string6 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string3.replace((CharSequence)"{PLAYER}", (CharSequence)string2).replace((CharSequence)"{TARGET}", (CharSequence)""));
            String string7 = me.anarchiacore.customitems.stormitemy.utils.color.A.C(string4.replace((CharSequence)"{PLAYER}", (CharSequence)string2).replace((CharSequence)"{TARGET}", (CharSequence)""));
            player.sendTitle(string6, string7, 10, 40, 10);
        }
        if (!string5.isEmpty()) {
            player.sendMessage(me.anarchiacore.customitems.stormitemy.utils.color.A.C(string5.replace((CharSequence)"{PLAYER}", (CharSequence)string2).replace((CharSequence)"{TARGET}", (CharSequence)"")));
        }
    }

    private static /* synthetic */ void A(LivingEntity livingEntity, double d2) {
        if (livingEntity.isValid() && !livingEntity.isDead()) {
            livingEntity.damage(d2);
            livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0.0, 1.0, 0.0), 10, (Object)Material.REDSTONE_BLOCK.createBlockData());
        }
    }
}

