package me.anarchiacore.customitems;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.util.ItemUtil;
import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.title.Title;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CustomItemsManager implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;
    private final NamespacedKey itemKey;
    private final NamespacedKey bombardaProjectileKey;
    private final WorldGuardIntegration worldGuardIntegration;
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public CustomItemsManager(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
        this.itemKey = new NamespacedKey(plugin, "custom_item");
        this.bombardaProjectileKey = new NamespacedKey(plugin, "bombarda_maxima_projectile");
        this.worldGuardIntegration = new WorldGuardIntegration(plugin);
    }

    public ItemStack createItem(String id) {
        if (id == null) {
            return null;
        }
        CustomItemsConfig config = configManager.getCustomItemsConfig();
        CustomItemDefinition def = config.getItemDefinition(id);
        if (def != null) {
            ItemStack item = ItemUtil.createItem(def.material());
            ItemUtil.applyMeta(item, def.displayName(), def.lore(), def.enchantments(), def.flags(), def.customModelData(), plugin, itemKey, def.id());
            ItemUtil.applyUnbreakable(item, def.unbreakable());
            return item;
        }
        CustomItemsConfig.EventItemDefinition eventItem = config.getEventItemDefinition(id);
        if (eventItem == null) {
            return null;
        }
        ItemStack item = ItemUtil.createItem(eventItem.material());
        ItemUtil.applyMeta(item, eventItem.displayName(), eventItem.lore(), eventItem.enchantments(), eventItem.flags(), eventItem.customModelData(), plugin, itemKey, eventItem.id());
        ItemUtil.applyUnbreakable(item, eventItem.unbreakable());
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
        String customId = getCustomItemId(item);
        if (customId == null) {
            return;
        }
        String normalized = customId.toLowerCase(Locale.ROOT);
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(normalized);
        if (eventItem == null) {
            return;
        }
        event.setCancelled(true);
        boolean explosive = normalized.equals("bombardamaxima") || normalized.equals("dynamit");
        if (!eventItem.noPlaceRegions().isEmpty()
                && worldGuardIntegration.isInRegions(event.getPlayer().getLocation(), new HashSet<>(eventItem.noPlaceRegions()))) {
            messageService.send(event.getPlayer(), plugin.getConfig().getString("messages.customItems.regionBlocked"));
            return;
        }
        if (!checkRegion(event.getPlayer(), explosive)) {
            return;
        }
        if (isOnCooldown(event.getPlayer(), normalized, eventItem.cooldown())) {
            sendCooldownMessage(event.getPlayer(), eventItem);
            return;
        }
        if (normalized.equals("bombardamaxima")) {
            launchBombarda(event.getPlayer(), eventItem);
            sendConsumerMessage(event.getPlayer(), eventItem);
            consumeItem(event.getPlayer(), item);
            return;
        }
        if (normalized.equals("turbotrap")) {
            if (placeTurboTrap(event.getPlayer(), eventItem)) {
                sendConsumerMessage(event.getPlayer(), eventItem);
                consumeItem(event.getPlayer(), item);
            }
            return;
        }
        if (normalized.equals("dynamit")) {
            spawnDynamite(event.getPlayer(), eventItem);
            sendConsumerMessage(event.getPlayer(), eventItem);
            consumeItem(event.getPlayer(), item);
            return;
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

    private void launchBombarda(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setVelocity(player.getLocation().getDirection().normalize().multiply(eventItem.projectileSpeed()));
        projectile.getPersistentDataContainer().set(bombardaProjectileKey, PersistentDataType.STRING, eventItem.id());
        int lifeTimeTicks = Math.max(1, eventItem.projectileLifeTimeTicks());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isDead() || !projectile.isValid()) {
                    return;
                }
                explode(projectile.getLocation(), eventItem);
                projectile.remove();
            }
        }.runTaskLater(plugin, lifeTimeTicks);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        String value = projectile.getPersistentDataContainer().get(bombardaProjectileKey, PersistentDataType.STRING);
        if (value == null) {
            return;
        }
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(value);
        if (eventItem == null) {
            return;
        }
        Location location = event.getHitBlock() != null ? event.getHitBlock().getLocation().add(0.5, 0.5, 0.5) : projectile.getLocation();
        explode(location, eventItem);
        projectile.remove();
    }

    private void explode(Location location, CustomItemsConfig.EventItemDefinition eventItem) {
        if (location.getWorld() == null) {
            return;
        }
        if (eventItem.preventRegionDestruction()) {
            Set<String> blocked = new HashSet<>(eventItem.noDestroyRegions());
            if (!blocked.isEmpty() && worldGuardIntegration.isInRegions(location, blocked)) {
                return;
            }
        }
        float power = Math.max(0.0f, eventItem.explosionRadius());
        location.getWorld().createExplosion(location, power, eventItem.spawnFire(), true);
    }

    private boolean placeTurboTrap(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        Location base = player.getLocation().getBlock().getLocation();
        String schematic = "configs/STORMITEMY/trapanarchia.schem";
        if (!placeStructureFromSchematic(schematic, base)) {
            messageService.send(player, plugin.getConfig().getString("messages.customItems.noSpace"));
            return false;
        }
        return true;
    }

    private void spawnDynamite(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        Location location = player.getLocation().add(player.getLocation().getDirection().normalize());
        TNTPrimed primed = player.getWorld().spawn(location, TNTPrimed.class);
        primed.setFuseTicks(Math.max(1, eventItem.explosionParticleCount()));
        primed.setYield(Math.max(0.0f, eventItem.explosionRadius()));
        primed.setIsIncendiary(eventItem.spawnFire());
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
        if (item != null && isCustomItem(item, "totemulaskawienia")) {
            item.setAmount(item.getAmount() - 1);
            return true;
        }
        }
        return false;
    }

    private String getCustomItemId(ItemStack item) {
        if (item == null) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        return meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
    }

    private boolean isOnCooldown(Player player, String itemId, int cooldownSeconds) {
        if (cooldownSeconds <= 0) {
            return false;
        }
        Map<String, Long> playerCooldowns = cooldowns.computeIfAbsent(player.getUniqueId(), key -> new HashMap<>());
        long now = System.currentTimeMillis();
        Long next = playerCooldowns.get(itemId);
        if (next != null && next > now) {
            return true;
        }
        playerCooldowns.put(itemId, now + cooldownSeconds * 1000L);
        return false;
    }

    private void sendCooldownMessage(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        String message = eventItem.messages().cooldown();
        if (message == null || message.isBlank()) {
            return;
        }
        messageService.send(player, message);
    }

    private void sendConsumerMessage(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        CustomItemsConfig.MessageDefinition message = eventItem.messages();
        if (message == null) {
            return;
        }
        if (message.chatMessage() != null && !message.chatMessage().isBlank()) {
            messageService.send(player, message.chatMessage());
        }
        CustomItemsConfig.TitleMessage titleMessage = message.consumer();
        if (titleMessage != null && (!titleMessage.title().isBlank() || !titleMessage.subtitle().isBlank())) {
            Title title = Title.title(
                    MiniMessageUtil.parseComponent(titleMessage.title()),
                    MiniMessageUtil.parseComponent(titleMessage.subtitle()),
                    Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofMillis(500))
            );
            player.showTitle(title);
        }
    }

    private boolean placeStructureFromSchematic(String path, Location origin) {
        File schematicFile = new File(plugin.getDataFolder(), path);
        if (!schematicFile.exists()) {
            return false;
        }
        try {
            if (!canPasteSchematic(schematicFile, origin)) {
                return false;
            }
            Object schematic = null;
            try {
                Class<?> clipboardFormats = Class.forName("com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats");
                Object format = clipboardFormats.getMethod("findByFile", File.class).invoke(null, schematicFile);
                if (format == null) {
                    return false;
                }
                try (InputStream inputStream = new FileInputStream(schematicFile)) {
                    Object reader = format.getClass().getMethod("getReader", InputStream.class).invoke(format, inputStream);
                    schematic = reader.getClass().getMethod("read").invoke(reader);
                }
            } catch (ReflectiveOperationException ex) {
                plugin.getLogger().warning("Nie można wczytać schematu: " + ex.getMessage());
                return false;
            }
            if (schematic == null) {
                return false;
            }
            Class<?> worldEdit = Class.forName("com.sk89q.worldedit.WorldEdit");
            Object instance = worldEdit.getMethod("getInstance").invoke(null);
            Object editSessionFactory = instance.getClass().getMethod("getEditSessionFactory").invoke(instance);
            Object adaptedWorld = Class.forName("com.sk89q.worldedit.bukkit.BukkitAdapter").getMethod("adapt", org.bukkit.World.class).invoke(null, origin.getWorld());
            Object editSession = editSessionFactory.getClass().getMethod("getEditSession", Class.forName("com.sk89q.worldedit.world.World"), int.class).invoke(editSessionFactory, adaptedWorld, -1);
            Object clipboard = schematic;
            Object originVector = Class.forName("com.sk89q.worldedit.math.BlockVector3").getMethod("at", int.class, int.class, int.class)
                    .invoke(null, origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
            Object pasteBuilder = clipboard.getClass().getMethod("createPaste", Class.forName("com.sk89q.worldedit.EditSession"))
                    .invoke(clipboard, editSession);
            pasteBuilder.getClass().getMethod("to", Class.forName("com.sk89q.worldedit.math.BlockVector3"))
                    .invoke(pasteBuilder, originVector);
            pasteBuilder.getClass().getMethod("ignoreAirBlocks", boolean.class).invoke(pasteBuilder, false);
            Object operation = pasteBuilder.getClass().getMethod("build").invoke(pasteBuilder);
            Class.forName("com.sk89q.worldedit.function.operation.Operations").getMethod("complete", Class.forName("com.sk89q.worldedit.function.operation.Operation"))
                    .invoke(null, operation);
            editSession.getClass().getMethod("close").invoke(editSession);
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Nie można postawić struktury: " + e.getMessage());
            return false;
        }
    }

    private boolean canPasteSchematic(File schematicFile, Location origin) {
        try {
            Class<?> clipboardFormats = Class.forName("com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats");
            Object format = clipboardFormats.getMethod("findByFile", File.class).invoke(null, schematicFile);
            if (format == null) {
                return false;
            }
            Object clipboard;
            try (InputStream inputStream = new FileInputStream(schematicFile)) {
                Object reader = format.getClass().getMethod("getReader", InputStream.class).invoke(format, inputStream);
                clipboard = reader.getClass().getMethod("read").invoke(reader);
            }
            if (clipboard == null) {
                return false;
            }
            Object region = clipboard.getClass().getMethod("getRegion").invoke(clipboard);
            Object minimum = region.getClass().getMethod("getMinimumPoint").invoke(region);
            Object maximum = region.getClass().getMethod("getMaximumPoint").invoke(region);
            int minX = (int) minimum.getClass().getMethod("getBlockX").invoke(minimum);
            int minY = (int) minimum.getClass().getMethod("getBlockY").invoke(minimum);
            int minZ = (int) minimum.getClass().getMethod("getBlockZ").invoke(minimum);
            int maxX = (int) maximum.getClass().getMethod("getBlockX").invoke(maximum);
            int maxY = (int) maximum.getClass().getMethod("getBlockY").invoke(maximum);
            int maxZ = (int) maximum.getClass().getMethod("getBlockZ").invoke(maximum);
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        if (!origin.getWorld().getBlockAt(origin.getBlockX() + x, origin.getBlockY() + y, origin.getBlockZ() + z).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            plugin.getLogger().warning("Nie można sprawdzić miejsca schematu: " + e.getMessage());
            return false;
        }
    }
}
