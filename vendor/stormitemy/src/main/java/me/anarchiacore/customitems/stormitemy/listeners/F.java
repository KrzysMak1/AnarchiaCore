/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashSet
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.Set
 *  java.util.UUID
 *  java.util.concurrent.ConcurrentHashMap
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemHeldEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerSwapHandItemsEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.anarchiacore.customitems.stormitemy.listeners;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.messages.B;
import me.anarchiacore.customitems.stormitemy.utils.cooldown.A;

public class F
implements Listener {
    private final Main D;
    private final Map<UUID, Map<Material, BukkitTask>> C = new ConcurrentHashMap();
    private final Map<UUID, Map<Material, ItemStack>> A = new ConcurrentHashMap();
    private final Set<UUID> E = ConcurrentHashMap.newKeySet();
    private BukkitTask B;

    public F(Main main) {
        this.D = main;
        main.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)main);
        this.A();
    }

    private void A() {
        if (this.B != null) {
            this.B.cancel();
        }
        this.B = new BukkitRunnable(){

            public void run() {
                if (F.this.E.isEmpty()) {
                    return;
                }
                HashSet hashSet = new HashSet(F.this.E);
                for (UUID uUID : hashSet) {
                    Player player = Bukkit.getPlayer((UUID)uUID);
                    if (player != null && player.isOnline()) {
                        F.this.checkNewPlayerCooldowns(player);
                        continue;
                    }
                    F.this.E.remove((Object)uUID);
                    F.this.D(uUID);
                }
            }
        }.runTaskTimer((Plugin)this.D, 0L, 20L);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerPreInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        ItemStack itemStack = playerInteractEvent.getItem();
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            UUID uUID2 = player.getUniqueId();
            Map map = (Map)this.A.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
            map.put((Object)itemStack.getType(), (Object)itemStack.clone());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        this.checkNewPlayerCooldowns(player);
    }

    public void checkNewPlayerCooldowns(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        UUID uUID2 = player.getUniqueId();
        Map map = (Map)this.C.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
        Map map2 = (Map)this.A.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
        HashSet hashSet = new HashSet();
        this.A(player, player.getInventory().getItemInMainHand(), (Set<Material>)hashSet);
        this.A(player, player.getInventory().getItemInOffHand(), (Set<Material>)hashSet);
        for (int i2 = 0; i2 < 9; ++i2) {
            this.A(player, player.getInventory().getItem(i2), (Set<Material>)hashSet);
        }
        for (ItemStack itemStack : player.getInventory().getContents()) {
            Material material;
            if (itemStack == null || hashSet.contains((Object)(material = itemStack.getType()))) continue;
            hashSet.add((Object)material);
            int n2 = player.getCooldown(material);
            if (n2 < 20 || map.containsKey((Object)material)) continue;
            this.A(player, material, itemStack);
        }
        for (Map.Entry entry : map2.entrySet()) {
            Material material = (Material)entry.getKey();
            if (hashSet.contains((Object)material)) continue;
            hashSet.add((Object)material);
            int n3 = player.getCooldown(material);
            if (n3 < 20 || map.containsKey((Object)material)) continue;
            this.A(player, material, (ItemStack)entry.getValue());
        }
    }

    private void A(final Player player, final Material material, ItemStack itemStack) {
        String string;
        int n2;
        BukkitTask bukkitTask;
        final UUID uUID2 = player.getUniqueId();
        final Map map = (Map)this.C.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
        if (map.containsKey((Object)material) && (bukkitTask = (BukkitTask)map.get((Object)material)) != null) {
            bukkitTask.cancel();
        }
        if ((n2 = player.getCooldown(material)) < 20) {
            return;
        }
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            string = me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(itemStack);
        } else if (material == Material.CREEPER_SPAWN_EGG) {
            boolean bl = false;
            for (ItemStack itemStack2 : player.getInventory().getContents()) {
                String string2;
                if (itemStack2 == null || itemStack2.getType() != Material.CREEPER_SPAWN_EGG || !itemStack2.hasItemMeta() || !itemStack2.getItemMeta().hasDisplayName() || !(string2 = itemStack2.getItemMeta().getDisplayName().toLowerCase()).contains((CharSequence)"zmutowany") || !string2.contains((CharSequence)"creeper")) continue;
                bl = true;
                break;
            }
            string = bl ? "zmutowanycreeper" : material.name().toLowerCase();
        } else {
            string = material.name().toLowerCase();
        }
        if (string == null || string.isEmpty()) {
            string = material.name().toLowerCase();
        }
        String string3 = string;
        B b2 = this.D.getActionbarManager();
        if (b2 != null && b2.isEnabled()) {
            int n3 = 0;
            if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasLore()) {
                n3 = 1;
            } else if (b2.isPluginItem(string3)) {
                n3 = 1;
            }
            if (n3 != 0) {
                if (string3.equals((Object)"creeper_spawn_egg")) {
                    b2.registerCooldown(player, "zmutowanycreeper", n2 / 20);
                } else {
                    b2.registerCooldown(player, string3, n2 / 20);
                }
            }
        }
        BukkitTask bukkitTask2 = new BukkitRunnable(this){
            private int A;
            final /* synthetic */ F this$0;
            {
                this.this$0 = f2;
                this.A = n2;
            }

            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    map.remove((Object)material);
                    return;
                }
                int n22 = player.getCooldown(material);
                if (n22 == 0 || n22 > this.A) {
                    this.cancel();
                    map.remove((Object)material);
                    if (map.isEmpty()) {
                        this.this$0.E.remove((Object)uUID2);
                    }
                    return;
                }
                this.A = n22;
            }
        }.runTaskTimer((Plugin)this.D, 0L, 1L);
        map.put((Object)material, (Object)bukkitTask2);
        this.E.add((Object)uUID2);
    }

    private void A(Player player, ItemStack itemStack, Set<Material> set) {
        if (itemStack == null) {
            return;
        }
        Material material = itemStack.getType();
        if (set.contains((Object)material)) {
            return;
        }
        set.add((Object)material);
        int n2 = player.getCooldown(material);
        if (n2 >= 20) {
            UUID uUID2 = player.getUniqueId();
            Map map = (Map)this.C.get((Object)uUID2);
            if (map == null || !map.containsKey((Object)material)) {
                this.A(player, material, itemStack);
            }
            Map map2 = (Map)this.A.computeIfAbsent((Object)uUID2, uUID -> new ConcurrentHashMap());
            map2.put((Object)material, (Object)itemStack.clone());
        }
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent playerItemHeldEvent) {
        this.checkNewPlayerCooldowns(playerItemHeldEvent.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        this.checkNewPlayerCooldowns(playerJoinEvent.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        this.D(playerQuitEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent) {
        final Player player = playerDropItemEvent.getPlayer();
        new BukkitRunnable(this){
            final /* synthetic */ F this$0;
            {
                this.this$0 = f2;
            }

            public void run() {
                this.this$0.checkNewPlayerCooldowns(player);
            }
        }.runTaskLater((Plugin)this.D, 1L);
    }

    @EventHandler
    public void onPlayerSwapItems(PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        this.checkNewPlayerCooldowns(playerSwapHandItemsEvent.getPlayer());
    }

    public void checkAllPlayerItems(Player player) {
        this.checkNewPlayerCooldowns(player);
    }

    public boolean hasActiveCooldowns(Player player) {
        return this.E.contains((Object)player.getUniqueId());
    }

    private void D(UUID uUID) {
        Map map = (Map)this.C.remove((Object)uUID);
        if (map != null) {
            for (BukkitTask bukkitTask : map.values()) {
                if (bukkitTask == null) continue;
                bukkitTask.cancel();
            }
        }
        this.A.remove((Object)uUID);
        this.E.remove((Object)uUID);
    }

    public void disable() {
        if (this.B != null) {
            this.B.cancel();
            this.B = null;
        }
        for (Map map : this.C.values()) {
            for (BukkitTask bukkitTask : map.values()) {
                if (bukkitTask == null) continue;
                bukkitTask.cancel();
            }
        }
        this.C.clear();
        this.A.clear();
        this.E.clear();
    }
}

