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
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomItemsManager implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final MessageService messageService;
    private final NamespacedKey itemKey;
    private final NamespacedKey bombardaProjectileKey;
    private final WorldGuardIntegration worldGuardIntegration;
    private final SchematicService schematicService;
    private final Map<String, Set<String>> cachedNoPlaceRegions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> cachedNoDestroyRegions = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public CustomItemsManager(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
        this.itemKey = new NamespacedKey(plugin, "custom_item");
        this.bombardaProjectileKey = new NamespacedKey(plugin, "bombarda_maxima_projectile");
        this.worldGuardIntegration = new WorldGuardIntegration(plugin);
        this.schematicService = new SchematicService(plugin);
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
            ItemUtil.applyAttributeModifiers(item, def.attributes(), plugin, def.id());
            return item;
        }
        CustomItemsConfig.EventItemDefinition eventItem = config.getEventItemDefinition(id);
        if (eventItem == null) {
            return null;
        }
        ItemStack item = ItemUtil.createItem(eventItem.material());
        ItemUtil.applyMeta(item, eventItem.displayName(), eventItem.lore(), eventItem.enchantments(), eventItem.flags(), eventItem.customModelData(), plugin, itemKey, eventItem.id());
        ItemUtil.applyUnbreakable(item, eventItem.unbreakable());
        ItemUtil.applyAttributeModifiers(item, eventItem.attributes(), plugin, eventItem.id());
        return item;
    }

    public void reload() {
        cachedNoPlaceRegions.clear();
        cachedNoDestroyRegions.clear();
        schematicService.clear();
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
        if (event.getHand() == null) {
            return;
        }
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
            if (getCustomItemId(mainHand) != null) {
                return;
            }
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
        String normalized = normalizeItemId(customId);
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(customId);
        if (eventItem == null) {
            eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(normalized);
        }
        if (eventItem == null) {
            return;
        }
        boolean explosive = isExplosiveItem(normalized);
        if (!isHandledEventItem(normalized)) {
            return;
        }
        if (!eventItem.noPlaceRegions().isEmpty()
                && worldGuardIntegration.isInRegions(event.getPlayer().getLocation(), getCachedRegions(eventItem.id(), eventItem.noPlaceRegions(), cachedNoPlaceRegions))) {
            messageService.send(event.getPlayer(), plugin.getConfig().getString("messages.customItems.regionBlocked"));
            return;
        }
        if (!checkRegion(event.getPlayer(), explosive)) {
            return;
        }
        switch (normalized) {
            case "bombarda", "bombardamaxima" -> {
                handleBombarda(event, item, eventItem);
            }
            case "turbotrap" -> {
                handleTurboTrap(event, item, eventItem);
            }
            case "dynamit" -> {
                handleDynamit(event, item, eventItem);
            }
            default -> {
            }
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

    private void handleBombarda(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "&cBombarda maxima jest na cooldownie! {TIME}s pozostało.");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
        }
        if (eventItem.useProjectileMode()) {
            launchBombarda(player, eventItem);
        } else {
            Location location = event.getClickedBlock() != null
                    ? event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5)
                    : player.getLocation();
            if (location.getWorld() != null) {
                location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                int particles = Math.max(0, eventItem.explosionParticleCount());
                location.getWorld().spawnParticle(Particle.EXPLOSION, location, particles, 1.0, 1.0, 1.0);
            }
            explodeBombarda(location, eventItem);
            sendConsumerMessage(player, eventItem);
        }
        consumeItemFromHand(player, event.getHand());
        event.setCancelled(true);
    }

    private void handleTurboTrap(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
        }
        consumeItemFromHand(player, event.getHand());
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("turboTrap", new FixedMetadataValue(plugin, true));
        event.setCancelled(true);
    }

    private void handleDynamit(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
        }
        spawnDynamite(player, eventItem);
        sendConsumerMessage(player, eventItem);
        consumeItemFromHand(player, event.getHand());
        event.setCancelled(true);
    }

    private void launchBombarda(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        SmallFireball projectile = player.launchProjectile(SmallFireball.class);
        projectile.setMetadata("bombardaMaxima", new FixedMetadataValue(plugin, true));
        projectile.setMetadata("bombardaPlayer", new FixedMetadataValue(plugin, player.getUniqueId().toString()));
        projectile.setVelocity(player.getLocation().getDirection().multiply(eventItem.projectileSpeed()));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof SmallFireball && projectile.hasMetadata("bombardaMaxima")) {
            handleBombardaProjectileHit(projectile, event);
            return;
        }
        if (projectile instanceof Egg && projectile.hasMetadata("turboTrap")) {
            handleTurboTrapProjectileHit(projectile, event);
            return;
        }
        String value = projectile.getPersistentDataContainer().get(bombardaProjectileKey, PersistentDataType.STRING);
        if (value == null) {
            return;
        }
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(value);
        if (eventItem == null) {
            return;
        }
        Location location = event.getHitBlock() != null ? event.getHitBlock().getLocation().add(0.5, 0.5, 0.5) : projectile.getLocation();
        explodeBombarda(location, eventItem);
        projectile.remove();
    }

    private void handleBombardaProjectileHit(Projectile projectile, ProjectileHitEvent event) {
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition("bombarda");
        if (eventItem == null) {
            eventItem = configManager.getCustomItemsConfig().getEventItemDefinition("bombardamaxima");
        }
        if (eventItem == null) {
            return;
        }
        Location location = event.getHitBlock() != null ? event.getHitBlock().getLocation().add(0.5, 0.5, 0.5) : projectile.getLocation();
        if (location.getWorld() != null) {
            location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            int particles = Math.max(0, eventItem.explosionParticleCount());
            location.getWorld().spawnParticle(Particle.EXPLOSION, location, particles, 1.0, 1.0, 1.0);
        }
        explodeBombarda(location, eventItem);
        projectile.remove();
    }

    private void handleTurboTrapProjectileHit(Projectile projectile, ProjectileHitEvent event) {
        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition("turbotrap");
        if (eventItem == null) {
            return;
        }
        Location location = projectile.getLocation();
        if (eventItem.noPlaceRegions() != null && !eventItem.noPlaceRegions().isEmpty()) {
            Set<String> blocked = getCachedRegions(eventItem.id(), eventItem.noPlaceRegions(), cachedNoPlaceRegions);
            if (!blocked.isEmpty() && worldGuardIntegration.isInRegions(location, blocked)) {
                messageService.send(player, plugin.getConfig().getString("messages.customItems.regionBlocked"));
                return;
            }
        }
        if (!placeTurboTrap(player, location, eventItem)) {
            return;
        }
        sendConsumerMessage(player, eventItem);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 0.5f);
        event.setCancelled(true);
    }

    private void explodeBombarda(Location location, CustomItemsConfig.EventItemDefinition eventItem) {
        if (location.getWorld() == null) {
            return;
        }
        int radius = Math.max(0, eventItem.explosionRadius());
        Set<Material> protectedBlocks = parseProtectedBlocks(eventItem.protectedBlocks());
        Set<String> blockedRegions = eventItem.preventRegionDestruction()
            ? getCachedRegions(eventItem.id(), eventItem.noDestroyRegions(), cachedNoDestroyRegions)
            : Set.of();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location target = location.clone().add(x, y, z);
                    if (target.distance(location) > radius) {
                        continue;
                    }
                    Block block = target.getBlock();
                    if (protectedBlocks.contains(block.getType())) {
                        continue;
                    }
                    if (!blockedRegions.isEmpty() && worldGuardIntegration.isInRegions(target, blockedRegions)) {
                        location.getWorld().spawnParticle(Particle.EXPLOSION, target, 1, 0.0, 0.0, 0.0, 0.0);
                        continue;
                    }
                    if (block.hasMetadata("hydro_cage_block")) {
                        location.getWorld().spawnParticle(Particle.FALLING_WATER, target, 3, 0.2, 0.2, 0.2, 0.1);
                        continue;
                    }
                    block.setType(Material.AIR);
                    if (eventItem.spawnFire() && random.nextDouble() < eventItem.fireChance()) {
                        Block above = block.getRelative(BlockFace.UP);
                        if (above.getType() == Material.AIR) {
                            above.setType(Material.FIRE);
                        }
                    }
                }
            }
        }
    }

    private Set<Material> parseProtectedBlocks(java.util.List<String> protectedBlocks) {
        Set<Material> result = new HashSet<>();
        if (protectedBlocks == null) {
            return result;
        }
        for (String name : protectedBlocks) {
            if (name == null || name.isBlank()) {
                continue;
            }
            Material material = Material.matchMaterial(name);
            if (material != null) {
                result.add(material);
            }
        }
        return result;
    }

    private boolean placeTurboTrap(Player player, Location location, CustomItemsConfig.EventItemDefinition eventItem) {
        String schematic = "configs/customitems/trapanarchia.schem";
        if (schematicService.getSchematicData(schematic) == null) {
            messageService.send(player, plugin.getConfig().getString("messages.customItems.noSpace"));
            return false;
        }
        CustomItemsConfig.AnimationDefinition animation = eventItem.animation();
        boolean enabled = animation != null && animation.enabled();
        SchematicService.AnimationOptions options = new SchematicService.AnimationOptions(
            animation != null ? animation.minDelayMs() : 100,
            animation != null ? animation.maxDelayMs() : 250,
            animation != null && animation.particles(),
            animation != null && animation.sound() != null && animation.sound().enabled(),
            animation != null && animation.sound() != null ? animation.sound().sound() : "",
            animation != null && animation.sound() != null ? animation.sound().volume() : 1.0f,
            animation != null && animation.sound() != null ? animation.sound().pitch() : 0.8f
        );
        if (enabled) {
            boolean placed = schematicService.pasteAnimated(schematic, location, options, this::isTurboTrapBlocked);
            if (!placed) {
                messageService.send(player, plugin.getConfig().getString("messages.customItems.noSpace"));
            }
            return placed;
        }
        boolean placed = schematicService.pasteIgnoringAir(schematic, location, this::isTurboTrapBlocked);
        if (!placed) {
            messageService.send(player, plugin.getConfig().getString("messages.customItems.noSpace"));
        }
        return placed;
    }

    private boolean isTurboTrapBlocked(Block block) {
        if (block == null) {
            return true;
        }
        Material type = block.getType();
        if (type == Material.BLUE_GLAZED_TERRACOTTA
                || type == Material.BLUE_TERRACOTTA
                || type == Material.BUBBLE_CORAL_BLOCK
                || type == Material.LIGHT_BLUE_TERRACOTTA
                || type == Material.BEDROCK) {
            return true;
        }
        return block.hasMetadata("hydro_cage_block");
    }

    private void spawnDynamite(Player player, CustomItemsConfig.EventItemDefinition eventItem) {
        Location location = player.getLocation().add(player.getLocation().getDirection().normalize());
        TNTPrimed primed = player.getWorld().spawn(location, TNTPrimed.class);
        primed.setFuseTicks(Math.max(1, eventItem.explosionParticleCount()));
        primed.setYield(Math.max(0.0f, eventItem.explosionRadius()));
        primed.setIsIncendiary(eventItem.spawnFire());
    }

    private void consumeItemFromHand(Player player, EquipmentSlot hand) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (hand == EquipmentSlot.OFF_HAND) {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if (offHand != null && offHand.getType() != Material.AIR) {
                offHand.setAmount(offHand.getAmount() - 1);
                player.getInventory().setItemInOffHand(offHand.getAmount() > 0 ? offHand : null);
            }
            return;
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand != null && mainHand.getType() != Material.AIR) {
            mainHand.setAmount(mainHand.getAmount() - 1);
            player.getInventory().setItemInMainHand(mainHand.getAmount() > 0 ? mainHand : null);
        }
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
        String value = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return getEventItemIdByDisplayName(item, meta);
    }

    private void sendCooldownMessage(Player player, CustomItemsConfig.EventItemDefinition eventItem, int remainingTicks, String fallback) {
        String message = eventItem.messages().cooldown();
        if (message == null || message.isBlank()) {
            message = fallback;
        }
        if (message == null || message.isBlank()) {
            return;
        }
        int seconds = Math.max(1, remainingTicks / 20);
        message = message.replace("{TIME}", String.valueOf(seconds));
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
                    Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000))
            );
            player.showTitle(title);
        }
    }

    private boolean isExplosiveItem(String itemId) {
        return "bombarda".equals(itemId) || "bombardamaxima".equals(itemId) || "dynamit".equals(itemId);
    }

    private boolean isHandledEventItem(String itemId) {
        return "bombarda".equals(itemId)
            || "bombardamaxima".equals(itemId)
            || "turbotrap".equals(itemId)
            || "dynamit".equals(itemId);
    }

    private Set<String> getCachedRegions(String itemId, java.util.List<String> regions, Map<String, Set<String>> cache) {
        if (regions == null || regions.isEmpty()) {
            return Set.of();
        }
        return cache.computeIfAbsent(itemId, key -> regions.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(value -> value.toLowerCase(Locale.ROOT))
                .collect(java.util.stream.Collectors.toSet()));
    }

    private String getEventItemIdByDisplayName(ItemStack item, ItemMeta meta) {
        if (meta.displayName() == null) {
            return null;
        }
        for (CustomItemsConfig.EventItemDefinition eventItem : configManager.getCustomItemsConfig().getEventItems()) {
            if (eventItem == null || eventItem.displayName() == null || eventItem.displayName().isBlank()) {
                continue;
            }
            if (eventItem.material() != null && eventItem.material() != item.getType()) {
                continue;
            }
            if (eventItem.customModelData() > 0 && (!meta.hasCustomModelData() || meta.getCustomModelData() != eventItem.customModelData())) {
                continue;
            }
            if (meta.displayName().equals(MiniMessageUtil.parseComponent(eventItem.displayName()))) {
                return eventItem.id();
            }
        }
        return null;
    }

    private String normalizeItemId(String id) {
        if (id == null) {
            return "";
        }
        return id.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }
}
