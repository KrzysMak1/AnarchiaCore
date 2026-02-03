package me.anarchiacore.customitems.stormitemy.listeners;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.anarchiacore.customitems.stormitemy.Main;
import me.anarchiacore.customitems.stormitemy.messages.B;
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

public class F implements Listener {
    private final Main plugin;
    private final Map<UUID, Map<Material, BukkitTask>> cooldownTasks = new ConcurrentHashMap<>();
    private final Map<UUID, Map<Material, ItemStack>> lastKnownItems = new ConcurrentHashMap<>();
    private final Set<UUID> activePlayers = ConcurrentHashMap.newKeySet();
    private BukkitTask refreshTask;

    public F(Main main) {
        this.plugin = main;
        main.getServer().getPluginManager().registerEvents(this, main);
        scheduleRefreshTask();
    }

    private void scheduleRefreshTask() {
        if (this.refreshTask != null) {
            this.refreshTask.cancel();
        }
        this.refreshTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (activePlayers.isEmpty()) {
                    return;
                }
                Set<UUID> snapshot = new HashSet<>(activePlayers);
                for (UUID uuid : snapshot) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.isOnline()) {
                        checkNewPlayerCooldowns(player);
                        continue;
                    }
                    activePlayers.remove(uuid);
                    removePlayer(uuid);
                }
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            UUID uuid = player.getUniqueId();
            Map<Material, ItemStack> items = lastKnownItems.computeIfAbsent(uuid, key -> new ConcurrentHashMap<>());
            items.put(itemStack.getType(), itemStack.clone());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        checkNewPlayerCooldowns(event.getPlayer());
    }

    public void checkNewPlayerCooldowns(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        UUID uuid = player.getUniqueId();
        Map<Material, BukkitTask> taskMap = cooldownTasks.computeIfAbsent(uuid, key -> new ConcurrentHashMap<>());
        Map<Material, ItemStack> cachedItems = lastKnownItems.computeIfAbsent(uuid, key -> new ConcurrentHashMap<>());
        Set<Material> seen = new HashSet<>();
        registerCooldown(player, player.getInventory().getItemInMainHand(), seen);
        registerCooldown(player, player.getInventory().getItemInOffHand(), seen);
        for (int slot = 0; slot < 9; ++slot) {
            registerCooldown(player, player.getInventory().getItem(slot), seen);
        }
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }
            Material material = itemStack.getType();
            if (seen.contains(material)) {
                continue;
            }
            seen.add(material);
            int cooldown = player.getCooldown(material);
            if (cooldown < 20 || taskMap.containsKey(material)) {
                continue;
            }
            registerCooldown(player, material, itemStack);
        }
        for (Map.Entry<Material, ItemStack> entry : cachedItems.entrySet()) {
            Material material = entry.getKey();
            if (seen.contains(material)) {
                continue;
            }
            seen.add(material);
            int cooldown = player.getCooldown(material);
            if (cooldown < 20 || taskMap.containsKey(material)) {
                continue;
            }
            registerCooldown(player, material, entry.getValue());
        }
    }

    private void registerCooldown(final Player player, final Material material, ItemStack itemStack) {
        final UUID uuid = player.getUniqueId();
        final Map<Material, BukkitTask> taskMap = cooldownTasks.computeIfAbsent(uuid, key -> new ConcurrentHashMap<>());
        BukkitTask existing = taskMap.get(material);
        if (existing != null) {
            existing.cancel();
        }
        int cooldown = player.getCooldown(material);
        if (cooldown < 20) {
            return;
        }
        String identifier;
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            identifier = me.anarchiacore.customitems.stormitemy.utils.cooldown.A.A(itemStack);
        } else if (material == Material.CREEPER_SPAWN_EGG) {
            boolean mutated = false;
            for (ItemStack stack : player.getInventory().getContents()) {
                if (stack == null || stack.getType() != Material.CREEPER_SPAWN_EGG || !stack.hasItemMeta()
                    || !stack.getItemMeta().hasDisplayName()) {
                    continue;
                }
                String name = stack.getItemMeta().getDisplayName().toLowerCase();
                if (name.contains("zmutowany") && name.contains("creeper")) {
                    mutated = true;
                    break;
                }
            }
            identifier = mutated ? "zmutowanycreeper" : material.name().toLowerCase();
        } else {
            identifier = material.name().toLowerCase();
        }
        if (identifier == null || identifier.isEmpty()) {
            identifier = material.name().toLowerCase();
        }
        B actionbar = this.plugin.getActionbarManager();
        if (actionbar != null && actionbar.isEnabled()) {
            boolean shouldRegister = false;
            if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()
                && itemStack.getItemMeta().hasLore()) {
                shouldRegister = true;
            } else if (actionbar.isPluginItem(identifier)) {
                shouldRegister = true;
            }
            if (shouldRegister) {
                if ("creeper_spawn_egg".equals(identifier)) {
                    actionbar.registerCooldown(player, "zmutowanycreeper", cooldown / 20);
                } else {
                    actionbar.registerCooldown(player, identifier, cooldown / 20);
                }
            }
        }
        BukkitTask task = new BukkitRunnable() {
            private int lastCooldown = cooldown;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    taskMap.remove(material);
                    return;
                }
                int current = player.getCooldown(material);
                if (current == 0 || current > this.lastCooldown) {
                    this.cancel();
                    taskMap.remove(material);
                    if (taskMap.isEmpty()) {
                        activePlayers.remove(uuid);
                    }
                    return;
                }
                this.lastCooldown = current;
            }
        }.runTaskTimer(this.plugin, 0L, 1L);
        taskMap.put(material, task);
        activePlayers.add(uuid);
    }

    private void registerCooldown(Player player, ItemStack itemStack, Set<Material> seen) {
        if (itemStack == null) {
            return;
        }
        Material material = itemStack.getType();
        if (seen.contains(material)) {
            return;
        }
        seen.add(material);
        int cooldown = player.getCooldown(material);
        if (cooldown >= 20) {
            UUID uuid = player.getUniqueId();
            Map<Material, BukkitTask> taskMap = cooldownTasks.get(uuid);
            if (taskMap == null || !taskMap.containsKey(material)) {
                registerCooldown(player, material, itemStack);
            }
            Map<Material, ItemStack> items = lastKnownItems.computeIfAbsent(uuid, key -> new ConcurrentHashMap<>());
            items.put(material, itemStack.clone());
        }
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        checkNewPlayerCooldowns(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkNewPlayerCooldowns(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                checkNewPlayerCooldowns(player);
            }
        }.runTaskLater(this.plugin, 1L);
    }

    @EventHandler
    public void onPlayerSwapItems(PlayerSwapHandItemsEvent event) {
        checkNewPlayerCooldowns(event.getPlayer());
    }

    public void checkAllPlayerItems(Player player) {
        checkNewPlayerCooldowns(player);
    }

    public boolean hasActiveCooldowns(Player player) {
        return activePlayers.contains(player.getUniqueId());
    }

    private void removePlayer(UUID uuid) {
        Map<Material, BukkitTask> tasks = cooldownTasks.remove(uuid);
        if (tasks != null) {
            for (BukkitTask task : tasks.values()) {
                if (task == null) {
                    continue;
                }
                task.cancel();
            }
        }
        lastKnownItems.remove(uuid);
        activePlayers.remove(uuid);
    }

    public void disable() {
        if (this.refreshTask != null) {
            this.refreshTask.cancel();
            this.refreshTask = null;
        }
        for (Map<Material, BukkitTask> tasks : cooldownTasks.values()) {
            for (BukkitTask task : tasks.values()) {
                if (task == null) {
                    continue;
                }
                task.cancel();
            }
        }
        cooldownTasks.clear();
        lastKnownItems.clear();
        activePlayers.clear();
    }
}
