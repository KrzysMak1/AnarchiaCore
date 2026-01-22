package me.anarchiacore.customitems;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.util.ItemUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomItemsManager implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;
    private final NamespacedKey itemKey;
    private final NamespacedKey bombardaProjectileKey;
    private final WorldGuardIntegration worldGuardIntegration;

    public CustomItemsManager(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
        this.itemKey = new NamespacedKey(plugin, "custom_item");
        this.bombardaProjectileKey = new NamespacedKey(plugin, "bombarda_maxima_projectile");
        this.worldGuardIntegration = new WorldGuardIntegration(plugin);
    }

    public ItemStack createItem(String id) {
        CustomItemDefinition def = configManager.getCustomItemsConfig().getItemDefinition(id);
        if (def == null) {
            return null;
        }
        ItemStack item = ItemUtil.createItem(def.material());
        ItemUtil.applyMeta(item, def.displayName(), def.lore(), def.enchantments(), def.flags(), def.customModelData(), plugin, itemKey, id);
        return item;
    }

    public boolean isCustomItem(ItemStack item, String id) {
        if (item == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        String value = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        return id.equalsIgnoreCase(value);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        if (isCustomItem(item, "bombardaMaxima")) {
            event.setCancelled(true);
            if (!checkRegion(event.getPlayer(), true)) {
                return;
            }
            launchBombarda(event.getPlayer());
            consumeItem(event.getPlayer(), item);
            return;
        }
        if (isCustomItem(item, "turboTrap")) {
            event.setCancelled(true);
            if (!checkRegion(event.getPlayer(), false)) {
                return;
            }
            if (placeTurboTrap(event.getPlayer())) {
                consumeItem(event.getPlayer(), item);
            }
            return;
        }
        if (isCustomItem(item, "dynamit")) {
            event.setCancelled(true);
            if (!checkRegion(event.getPlayer(), true)) {
                return;
            }
            spawnDynamite(event.getPlayer());
            consumeItem(event.getPlayer(), item);
        }
    }

    private boolean checkRegion(Player player, boolean explosive) {
        CustomItemsConfig.WorldGuardRules rules = configManager.getCustomItemsConfig().getWorldGuardRules();
        boolean hasRegions = rules != null && (!rules.allowAllRegions().isEmpty() || !rules.allowExceptExplosivesRegions().isEmpty());
        WorldGuardIntegration.RegionResult result = worldGuardIntegration.checkRegion(player.getLocation(), rules);
        if (result == WorldGuardIntegration.RegionResult.ALLOW_ALL) {
            return true;
        }
        if (result == WorldGuardIntegration.RegionResult.ALLOW_NON_EXPLOSIVES && explosive) {
            messageService.send(player, plugin.getConfig().getString("messages.customItems.explosivesBlocked"));
            return false;
        }
        if (result == WorldGuardIntegration.RegionResult.ALLOW_NON_EXPLOSIVES) {
            return true;
        }
        if (rules != null && rules.enabled() && hasRegions) {
            messageService.send(player, plugin.getConfig().getString("messages.customItems.regionBlocked"));
            return false;
        }
        return true;
    }

    private void launchBombarda(Player player) {
        CustomItemsConfig config = configManager.getCustomItemsConfig();
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setVelocity(player.getLocation().getDirection().normalize().multiply(config.getBombardaSpeed()));
        projectile.getPersistentDataContainer().set(bombardaProjectileKey, PersistentDataType.STRING, "1");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isDead() || !projectile.isValid()) {
                    return;
                }
                explode(projectile.getLocation());
                projectile.remove();
            }
        }.runTaskLater(plugin, config.getBombardaLifeTimeTicks());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        String value = projectile.getPersistentDataContainer().get(bombardaProjectileKey, PersistentDataType.STRING);
        if (!"1".equals(value)) {
            return;
        }
        Location location = event.getHitBlock() != null ? event.getHitBlock().getLocation().add(0.5, 0.5, 0.5) : projectile.getLocation();
        explode(location);
        projectile.remove();
    }

    private void explode(Location location) {
        CustomItemsConfig config = configManager.getCustomItemsConfig();
        location.getWorld().createExplosion(location, config.getBombardaPower(), config.isBombardaSetFire(), config.isBombardaBreakBlocks());
    }

    private boolean placeTurboTrap(Player player) {
        CustomItemsConfig config = configManager.getCustomItemsConfig();
        if (config.getTurboTrapBlocks().isEmpty()) {
            return false;
        }
        Location base = player.getLocation().getBlock().getLocation();
        for (String entry : config.getTurboTrapBlocks()) {
            String[] parts = entry.split(":", 2);
            if (parts.length != 2) {
                continue;
            }
            String[] coords = parts[0].split(",");
            if (coords.length != 3) {
                continue;
            }
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);
            Location target = base.clone().add(x, y, z);
            if (!target.getBlock().isEmpty()) {
                messageService.send(player, plugin.getConfig().getString("messages.customItems.noSpace"));
                return false;
            }
        }
        for (String entry : config.getTurboTrapBlocks()) {
            String[] parts = entry.split(":", 2);
            if (parts.length != 2) {
                continue;
            }
            String[] coords = parts[0].split(",");
            if (coords.length != 3) {
                continue;
            }
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            int z = Integer.parseInt(coords[2]);
            Material material = Material.matchMaterial(parts[1]);
            if (material == null) {
                continue;
            }
            Location target = base.clone().add(x, y, z);
            target.getBlock().setType(material, false);
        }
        return true;
    }

    private void spawnDynamite(Player player) {
        CustomItemsConfig config = configManager.getCustomItemsConfig();
        Location location = player.getLocation().add(player.getLocation().getDirection().normalize());
        TNTPrimed primed = player.getWorld().spawn(location, TNTPrimed.class);
        primed.setFuseTicks(config.getDynamitFuseTicks());
        primed.setYield(config.getDynamitPower());
        primed.setIsIncendiary(false);
    }

    private void consumeItem(Player player, ItemStack item) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        item.setAmount(item.getAmount() - 1);
    }

    public boolean consumeTotemForCombatLogout(Player player) {
        if (player == null) {
            return false;
        }
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isCustomItem(item, "totemUaskawienia")) {
                item.setAmount(item.getAmount() - 1);
                return true;
            }
        }
        return false;
    }
}
