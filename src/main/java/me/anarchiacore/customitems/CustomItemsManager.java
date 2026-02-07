package me.anarchiacore.customitems;

import me.anarchiacore.config.ConfigManager;
import me.anarchiacore.config.MessageService;
import me.anarchiacore.util.ItemUtil;
import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private final NamespacedKey hydroCageProjectileKey;
    private final WorldGuardIntegration worldGuardIntegration;
    private final SchematicService schematicService;
    private final Map<String, Set<String>> cachedNoPlaceRegions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> cachedNoDestroyRegions = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private boolean actionbarEnabled;
    private String actionbarSeparator;
    private String actionbarTimeFormat;
    private int actionbarMaxLength;
    private final Map<String, String> actionbarColors = new HashMap<>();
    private final Map<String, String> actionbarDisplayNames = new HashMap<>();
    private final Map<java.util.UUID, Map<String, Long>> actionbarCooldowns = new ConcurrentHashMap<>();

    public CustomItemsManager(Plugin plugin, ConfigManager configManager, MessageService messageService) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.messageService = messageService;
        this.itemKey = new NamespacedKey(plugin, "custom_item");
        this.bombardaProjectileKey = new NamespacedKey(plugin, "bombarda_maxima_projectile");
        this.hydroCageProjectileKey = new NamespacedKey(plugin, "hydro_cage_projectile");
        this.worldGuardIntegration = new WorldGuardIntegration(plugin);
        this.schematicService = new SchematicService(plugin);
        loadActionbarConfig();
        startEventItemEffectTask();
        startCooldownActionbarTask();
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
        loadActionbarConfig();
        actionbarCooldowns.clear();
    }

    private void startEventItemEffectTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            CustomItemsConfig config = configManager.getCustomItemsConfig();
            if (config == null) {
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                applyEffectsForItem(player, player.getInventory().getItemInMainHand(), EquipmentSlot.HAND);
                applyEffectsForItem(player, player.getInventory().getItemInOffHand(), EquipmentSlot.OFF_HAND);
                applyEffectsForItem(player, player.getInventory().getHelmet(), EquipmentSlot.HEAD);
            }
        }, 0L, 20L);
    }

    private void startCooldownActionbarTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!actionbarEnabled) {
                return;
            }
            long now = System.currentTimeMillis();
            for (Player player : Bukkit.getOnlinePlayers()) {
                Map<String, Long> cooldowns = actionbarCooldowns.get(player.getUniqueId());
                if (cooldowns == null || cooldowns.isEmpty()) {
                    continue;
                }
                String actionbar = buildCooldownActionbar(cooldowns, now);
                if (actionbar == null || actionbar.isBlank()) {
                    continue;
                }
                player.sendActionBar(MiniMessageUtil.parseComponent(actionbar));
            }
        }, 0L, 20L);
    }

    private void applyEffectsForItem(Player player, ItemStack item, EquipmentSlot slot) {
        String itemId = getCustomItemId(item);
        if (itemId == null) {
            return;
        }
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(itemId);
        if (eventItem == null || eventItem.effects() == null || eventItem.effects().isEmpty()) {
            return;
        }
        if (!shouldApplyEffectsForSlot(item, slot)) {
            return;
        }
        for (CustomItemsConfig.EffectDefinition effect : eventItem.effects()) {
            if (effect == null || effect.type() == null || effect.type().isBlank()) {
                continue;
            }
            PotionEffectType type = PotionEffectType.getByName(effect.type().toUpperCase(Locale.ROOT));
            if (type == null) {
                continue;
            }
            player.addPotionEffect(new PotionEffect(type, effect.duration(), effect.amplifier(), effect.ambient(), effect.particles()), true);
        }
    }

    private boolean shouldApplyEffectsForSlot(ItemStack item, EquipmentSlot slot) {
        if (item == null) {
            return false;
        }
        Material material = item.getType();
        if (isHelmetMaterial(material)) {
            return slot == EquipmentSlot.HEAD;
        }
        return slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND;
    }

    private boolean isHelmetMaterial(Material material) {
        if (material == null) {
            return false;
        }
        return switch (material) {
            case LEATHER_HELMET,
                 CHAINMAIL_HELMET,
                 IRON_HELMET,
                 GOLDEN_HELMET,
                 DIAMOND_HELMET,
                 NETHERITE_HELMET,
                 TURTLE_HELMET -> true;
            default -> false;
        };
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
            case "smoczymiecz" -> {
                handleSmoczyMiecz(event, item, eventItem);
            }
            case "turbotrap" -> {
                handleTurboTrap(event, item, eventItem);
            }
            case "turbodomek" -> {
                handleTurboDomek(event, item, eventItem);
            }
            case "dynamit" -> {
                handleDynamit(event, item, eventItem);
            }
            case "wyrzutniahydroklatki" -> {
                handleHydroCage(event, item, eventItem);
            }
            case "boskitopor" -> {
                handleBoskiTopor(event, item, eventItem);
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
            trackCooldown(player, eventItem, cooldownSeconds);
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
            trackCooldown(player, eventItem, cooldownSeconds);
        }
        consumeItemFromHand(player, event.getHand());
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("turboTrap", new FixedMetadataValue(plugin, true));
        event.setCancelled(true);
    }

    private void handleTurboDomek(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
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
        egg.setMetadata("turboDomek", new FixedMetadataValue(plugin, true));
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
            trackCooldown(player, eventItem, cooldownSeconds);
        }
        spawnDynamite(player, eventItem);
        sendConsumerMessage(player, eventItem);
        consumeItemFromHand(player, event.getHand());
        event.setCancelled(true);
    }

    private void handleSmoczyMiecz(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "&cSmoczy miecz jest na cooldownie! {TIME}s pozostało.");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
            trackCooldown(player, eventItem, cooldownSeconds);
        }
        EnderPearl pearl = player.launchProjectile(EnderPearl.class);
        pearl.setVelocity(player.getLocation().getDirection().multiply(1.5));
        sendConsumerMessage(player, eventItem);
        event.setCancelled(true);
    }

    private void handleHydroCage(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        HydroCageConfig config = loadHydroCageConfig();
        if (config == null) {
            return;
        }
        if (!config.allowedInWorld(player.getWorld())) {
            if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER) {
                sendHydroMessage(player, config.cannotUseNetherMessage);
            } else if (player.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
                sendHydroMessage(player, config.cannotUseEndMessage);
            }
            event.setCancelled(true);
            return;
        }
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
            trackCooldown(player, eventItem, cooldownSeconds);
        }
        Projectile projectile = player.launchProjectile(org.bukkit.entity.Snowball.class);
        projectile.getPersistentDataContainer().set(hydroCageProjectileKey, PersistentDataType.STRING, eventItem.id());
        projectile.setVelocity(player.getLocation().getDirection().multiply(config.projectileSpeed));
        projectile.setGravity(config.projectileGravity);
        playHydroSound(player.getLocation(), config.shootSound);
        sendHydroTitle(player, config.shootTitle, config.shootSubtitle);
        consumeItemFromHand(player, event.getHand());
        event.setCancelled(true);
    }

    private void handleHydroCageProjectileHit(Projectile projectile, ProjectileHitEvent event, String eventId) {
        if (!(projectile.getShooter() instanceof Player player)) {
            projectile.remove();
            return;
        }
        HydroCageConfig config = loadHydroCageConfig();
        if (config == null) {
            projectile.remove();
            return;
        }
        Location location = event.getHitBlock() != null
            ? event.getHitBlock().getLocation().add(0.5, 0.5, 0.5)
            : projectile.getLocation();
        List<ChangedBlock> changedBlocks = createHydroCage(location, config);
        if (!changedBlocks.isEmpty()) {
            playHydroSound(location, config.cageCreateSound);
            sendHydroTitle(player, config.cageCreatedTitle, config.cageCreatedSubtitle);
            scheduleHydroCageRemoval(changedBlocks, config);
        }
        projectile.remove();
    }

    private void handleBoskiTopor(PlayerInteractEvent event, ItemStack item, CustomItemsConfig.EventItemDefinition eventItem) {
        Player player = event.getPlayer();
        BoskiToporConfig config = loadBoskiToporConfig();
        if (config == null) {
            return;
        }
        int remaining = player.getCooldown(item.getType());
        if (remaining > 0) {
            sendCooldownMessage(player, eventItem, remaining, "");
            event.setCancelled(true);
            return;
        }
        int cooldownSeconds = Math.max(0, eventItem.cooldown());
        if (cooldownSeconds > 0) {
            player.setCooldown(item.getType(), cooldownSeconds * 20);
            trackCooldown(player, eventItem, cooldownSeconds);
        }
        boolean wasInvulnerable = player.isInvulnerable();
        player.setInvulnerable(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvulnerable(wasInvulnerable);
            }
        }.runTaskLater(plugin, Math.max(1, config.invulnerabilitySeconds) * 20L);
        playBoskiToporSound(player.getLocation(), config.sound);
        spawnBoskiToporParticles(player.getLocation(), config);
        applyBoskiToporPush(player, config);
        sendConsumerMessage(player, eventItem);
        sendBoskiToporTargetMessage(player, config);
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
        if (projectile instanceof Egg && projectile.hasMetadata("turboDomek")) {
            handleTurboDomekProjectileHit(projectile, event);
            return;
        }
        String hydroValue = projectile.getPersistentDataContainer().get(hydroCageProjectileKey, PersistentDataType.STRING);
        if (hydroValue != null) {
            handleHydroCageProjectileHit(projectile, event, hydroValue);
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

    @EventHandler(ignoreCancelled = true)
    public void onHydroCageBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata("hydro_cage_block")) {
            return;
        }
        HydroCageConfig config = loadHydroCageConfig();
        if (config == null || config.allowBreaking) {
            return;
        }
        event.setCancelled(true);
        sendHydroMessage(event.getPlayer(), config.cannotBreakMessage);
    }

    @EventHandler(ignoreCancelled = true)
    public void onHydroCagePlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata("hydro_cage_block")) {
            return;
        }
        HydroCageConfig config = loadHydroCageConfig();
        if (config == null || config.allowBlockPlacing) {
            return;
        }
        event.setCancelled(true);
        sendHydroMessage(event.getPlayer(), config.cannotPlaceMessage);
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

    private void handleTurboDomekProjectileHit(Projectile projectile, ProjectileHitEvent event) {
        if (!(projectile.getShooter() instanceof Player player)) {
            return;
        }
        CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition("turbodomek");
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
        if (!placeTurboDomek(player, location, eventItem)) {
            return;
        }
        sendConsumerMessage(player, eventItem);
        player.playSound(player.getLocation(), Sound.BLOCK_WOOD_PLACE, 1.0f, 0.5f);
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

    private boolean placeTurboDomek(Player player, Location location, CustomItemsConfig.EventItemDefinition eventItem) {
        String schematic = "configs/customitems/turbodomek.schem";
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
        int seconds = Math.max(1, remainingTicks / 20);
        if (actionbarEnabled && (eventItem.messages().cooldown() == null || eventItem.messages().cooldown().isBlank())) {
            sendActionbarCooldown(player, eventItem, seconds);
            return;
        }
        String message = eventItem.messages().cooldown();
        if (message == null || message.isBlank()) {
            message = fallback;
        }
        if (message == null || message.isBlank()) {
            return;
        }
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
            || "smoczymiecz".equals(itemId)
            || "turbotrap".equals(itemId)
            || "turbodomek".equals(itemId)
            || "dynamit".equals(itemId)
            || "wyrzutniahydroklatki".equals(itemId)
            || "boskitopor".equals(itemId);
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
        String displayName = normalizeLegacyText(MiniMessageUtil.serializeLegacy(meta.displayName()));
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
            String expected = normalizeLegacyText(MiniMessageUtil.parseLegacy(eventItem.displayName()));
            if (!expected.isBlank() && expected.equals(displayName)) {
                return eventItem.id();
            }
        }
        return null;
    }

    private String normalizeLegacyText(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value;
        while (normalized.startsWith("§r") || normalized.startsWith("§R")) {
            normalized = normalized.substring(2);
        }
        return normalized;
    }

    private String normalizeItemId(String id) {
        if (id == null) {
            return "";
        }
        return id.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    private void loadActionbarConfig() {
        actionbarEnabled = false;
        actionbarSeparator = "&8|";
        actionbarTimeFormat = "&8({color}{time}s&8)";
        actionbarMaxLength = 0;
        actionbarColors.clear();
        actionbarDisplayNames.clear();
        File stormConfig = new File(plugin.getDataFolder(), "configs/STORMITEMY/config.yml");
        if (!stormConfig.exists()) {
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(stormConfig);
        actionbarEnabled = yaml.getBoolean("actionbar.enabled", false);
        actionbarSeparator = yaml.getString("actionbar.separator", "&8|");
        actionbarTimeFormat = yaml.getString("actionbar.time_format", "&8({color}{time}s&8)");
        actionbarMaxLength = Math.max(0, yaml.getInt("actionbar.max_length", 0));
        var colorsSection = yaml.getConfigurationSection("actionbar.colors");
        if (colorsSection != null) {
            for (String key : colorsSection.getKeys(false)) {
                String value = colorsSection.getString(key, "");
                if (key != null && value != null && !value.isBlank()) {
                    actionbarColors.put(key.toLowerCase(Locale.ROOT), value);
                }
            }
        }
        var namesSection = yaml.getConfigurationSection("actionbar.display_names");
        if (namesSection != null) {
            for (String key : namesSection.getKeys(false)) {
                String value = namesSection.getString(key, "");
                if (key != null && value != null && !value.isBlank()) {
                    actionbarDisplayNames.put(key.toLowerCase(Locale.ROOT), value);
                }
            }
        }
    }

    private void sendActionbarCooldown(Player player, CustomItemsConfig.EventItemDefinition eventItem, int seconds) {
        if (!actionbarEnabled || player == null || eventItem == null) {
            return;
        }
        String itemId = normalizeItemId(eventItem.id());
        String color = actionbarColors.getOrDefault(itemId, "&f");
        String name = actionbarDisplayNames.get(itemId);
        if (name == null || name.isBlank()) {
            name = eventItem.displayName() != null && !eventItem.displayName().isBlank()
                ? eventItem.displayName()
                : itemId;
        }
        String timePart = actionbarTimeFormat
            .replace("{time}", String.valueOf(seconds))
            .replace("{color}", color);
        String raw = color + name + " " + timePart;
        if (actionbarMaxLength > 0 && raw.length() > actionbarMaxLength) {
            raw = raw.substring(0, actionbarMaxLength);
        }
        player.sendActionBar(MiniMessageUtil.parseComponent(raw));
    }

    private void trackCooldown(Player player, CustomItemsConfig.EventItemDefinition eventItem, int cooldownSeconds) {
        if (!actionbarEnabled || player == null || eventItem == null || cooldownSeconds <= 0) {
            return;
        }
        long expiresAt = System.currentTimeMillis() + (cooldownSeconds * 1000L);
        actionbarCooldowns
            .computeIfAbsent(player.getUniqueId(), key -> new ConcurrentHashMap<>())
            .put(normalizeItemId(eventItem.id()), expiresAt);
    }

    private String buildCooldownActionbar(Map<String, Long> cooldowns, long now) {
        if (cooldowns == null || cooldowns.isEmpty()) {
            return "";
        }
        java.util.List<String> parts = new java.util.ArrayList<>();
        cooldowns.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue() <= now);
        for (Map.Entry<String, Long> entry : cooldowns.entrySet()) {
            long remainingMs = entry.getValue() - now;
            if (remainingMs <= 0) {
                continue;
            }
            int seconds = Math.max(1, (int) Math.ceil(remainingMs / 1000.0));
            String itemId = entry.getKey();
            CustomItemsConfig.EventItemDefinition eventItem = configManager.getCustomItemsConfig().getEventItemDefinition(itemId);
            String name = actionbarDisplayNames.get(itemId);
            if (name == null || name.isBlank()) {
                name = eventItem != null && eventItem.displayName() != null && !eventItem.displayName().isBlank()
                    ? eventItem.displayName()
                    : itemId;
            }
            String color = actionbarColors.getOrDefault(itemId, "&f");
            String timePart = actionbarTimeFormat
                .replace("{time}", String.valueOf(seconds))
                .replace("{color}", color);
            String part = color + name + " " + timePart;
            parts.add(part);
        }
        if (parts.isEmpty()) {
            return "";
        }
        String raw = String.join(actionbarSeparator, parts);
        if (actionbarMaxLength > 0 && raw.length() > actionbarMaxLength) {
            raw = raw.substring(0, actionbarMaxLength);
        }
        return raw;
    }

    private void applyBoskiToporPush(Player player, BoskiToporConfig config) {
        double radius = Math.max(0.0, config.pushRadius);
        for (Player target : player.getWorld().getPlayers()) {
            if (target.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            if (target.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }
            Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector());
            if (direction.lengthSquared() == 0) {
                continue;
            }
            direction.normalize();
            double force = config.pushMinForce + (random.nextDouble() * Math.max(0.0, config.pushMaxForce - config.pushMinForce));
            Vector velocity = direction.multiply(force).setY(config.pushVerticalBoost);
            target.setVelocity(velocity);
        }
    }

    private void spawnBoskiToporParticles(Location location, BoskiToporConfig config) {
        if (location.getWorld() == null) {
            return;
        }
        if (config.explosionParticle != null && config.explosionCount > 0) {
            location.getWorld().spawnParticle(config.explosionParticle, location, config.explosionCount, 0.0, 0.0, 0.0, 0.0);
        }
        if (config.flameParticle != null && config.flameCount > 0) {
            location.getWorld().spawnParticle(config.flameParticle, location, config.flameCount,
                config.flameOffsetX, config.flameOffsetY, config.flameOffsetZ, 0.0);
        }
        if (config.circleParticle != null && config.circleDurationSeconds > 0) {
            new BukkitRunnable() {
                int elapsed = 0;
                @Override
                public void run() {
                    if (elapsed >= config.circleDurationSeconds * 20) {
                        cancel();
                        return;
                    }
                    double radius = config.circleRadius;
                    for (int angle = 0; angle < 360; angle += config.circleAngleStep) {
                        double radians = Math.toRadians(angle);
                        double x = Math.cos(radians) * radius;
                        double z = Math.sin(radians) * radius;
                        Location particleLocation = location.clone().add(x, 0.1, z);
                        location.getWorld().spawnParticle(config.circleParticle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0);
                    }
                    elapsed += config.circleDelayTicks;
                }
            }.runTaskTimer(plugin, 0L, Math.max(1, config.circleDelayTicks));
        }
    }

    private void playBoskiToporSound(Location location, SoundConfig sound) {
        if (location.getWorld() == null || sound == null || sound.sound == null) {
            return;
        }
        location.getWorld().playSound(location, sound.sound, sound.volume, sound.pitch);
    }

    private void sendBoskiToporTargetMessage(Player player, BoskiToporConfig config) {
        if ((config.targetTitle == null || config.targetTitle.isBlank())
            && (config.targetSubtitle == null || config.targetSubtitle.isBlank())
            && (config.targetChatMessage == null || config.targetChatMessage.isBlank())) {
            return;
        }
        Map<String, String> placeholders = Map.of("PLAYER", player.getName());
        double radius = Math.max(0.0, config.pushRadius);
        for (Player target : player.getWorld().getPlayers()) {
            if (target.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }
            if (target.getLocation().distanceSquared(player.getLocation()) > radius * radius) {
                continue;
            }
            if ((config.targetTitle != null && !config.targetTitle.isBlank())
                || (config.targetSubtitle != null && !config.targetSubtitle.isBlank())) {
                Title title = Title.title(
                    MiniMessageUtil.parseComponent(defaultString(config.targetTitle), placeholders),
                    MiniMessageUtil.parseComponent(defaultString(config.targetSubtitle), placeholders)
                );
                target.showTitle(title);
            }
            if (config.targetChatMessage != null && !config.targetChatMessage.isBlank()) {
                target.sendMessage(MiniMessageUtil.parseComponent(config.targetChatMessage, placeholders));
            }
        }
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }

    private HydroCageConfig loadHydroCageConfig() {
        org.bukkit.configuration.ConfigurationSection section = loadEventItemSection("wyrzutniahydroklatki");
        if (section == null) {
            return null;
        }
        org.bukkit.configuration.ConfigurationSection cageSection = section.getConfigurationSection("cage");
        int radius = cageSection != null ? cageSection.getInt("radius", 8) : 8;
        int duration = cageSection != null ? cageSection.getInt("duration", 15) : 15;
        int thickness = cageSection != null ? cageSection.getInt("wall_thickness", 1) : 1;
        org.bukkit.configuration.ConfigurationSection mapping = cageSection != null ? cageSection.getConfigurationSection("block_mappings") : null;
        Material wallMaterial = parseMaterial(mapping != null ? mapping.getString("external_wall") : null, Material.BLUE_GLAZED_TERRACOTTA);
        Material defaultMaterial = parseMaterial(mapping != null ? mapping.getString("default") : null, Material.LIGHT_BLUE_TERRACOTTA);
        Material fillMaterial = defaultMaterial;
        org.bukkit.configuration.ConfigurationSection projectileSection = section.getConfigurationSection("projectile");
        double projectileSpeed = projectileSection != null ? projectileSection.getDouble("speed", 1.5) : 1.5;
        boolean gravity = projectileSection == null || projectileSection.getBoolean("gravity", true);
        org.bukkit.configuration.ConfigurationSection messages = section.getConfigurationSection("messages");
        String shootTitle = messages != null ? messages.getString("shoot.title") : "";
        String shootSubtitle = messages != null ? messages.getString("shoot.subtitle") : "";
        String cageCreatedTitle = messages != null ? messages.getString("cage_created.title") : "";
        String cageCreatedSubtitle = messages != null ? messages.getString("cage_created.subtitle") : "";
        String cageExpiredTitle = messages != null ? messages.getString("cage_expired.title") : "";
        String cageExpiredSubtitle = messages != null ? messages.getString("cage_expired.subtitle") : "";
        String cannotBreak = messages != null ? messages.getString("cannot_break_cage.chatMessage") : "";
        String cannotPlace = messages != null ? messages.getString("cannot_place_block.subtitle") : "";
        String cannotUseNether = messages != null ? messages.getString("cannot_use_nether") : "";
        String cannotUseEnd = messages != null ? messages.getString("cannot_use_end") : "";
        org.bukkit.configuration.ConfigurationSection dimensions = section.getConfigurationSection("dimensions");
        boolean allowNether = dimensions == null || dimensions.getBoolean("allow_nether", true);
        boolean allowEnd = dimensions == null || dimensions.getBoolean("allow_end", true);
        boolean allowBlockPlacing = cageSection != null && cageSection.getBoolean("allow_block_placing", false);
        boolean allowBreaking = false;
        org.bukkit.configuration.ConfigurationSection sounds = section.getConfigurationSection("sounds");
        SoundConfig shootSound = readSoundConfig(sounds != null ? sounds.getConfigurationSection("shoot") : null, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
        SoundConfig cageCreateSound = readSoundConfig(sounds != null ? sounds.getConfigurationSection("cage_create") : null, Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);
        SoundConfig cageExpireSound = readSoundConfig(sounds != null ? sounds.getConfigurationSection("cage_expire") : null, Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0f, 1.0f);
        return new HydroCageConfig(radius, duration, thickness, wallMaterial, defaultMaterial, fillMaterial,
            projectileSpeed, gravity, shootTitle, shootSubtitle, cageCreatedTitle, cageCreatedSubtitle, cageExpiredTitle,
            cageExpiredSubtitle, cannotBreak, cannotPlace,
            cannotUseNether, cannotUseEnd, allowNether, allowEnd, allowBlockPlacing, allowBreaking,
            shootSound, cageCreateSound, cageExpireSound);
    }

    private BoskiToporConfig loadBoskiToporConfig() {
        org.bukkit.configuration.ConfigurationSection section = loadEventItemSection("boskitopor");
        if (section == null) {
            return null;
        }
        int invulnerability = section.getInt("invulnerability.duration", 3);
        org.bukkit.configuration.ConfigurationSection particleSection = section.getConfigurationSection("particleEffects");
        Particle explosionParticle = parseParticle(particleSection != null ? particleSection.getString("explosion.type") : null);
        int explosionCount = particleSection != null ? particleSection.getInt("explosion.count", 0) : 0;
        Particle flameParticle = parseParticle(particleSection != null ? particleSection.getString("flame.type") : null);
        int flameCount = particleSection != null ? particleSection.getInt("flame.count", 0) : 0;
        double flameOffsetX = particleSection != null ? particleSection.getDouble("flame.offset.x", 0.0) : 0.0;
        double flameOffsetY = particleSection != null ? particleSection.getDouble("flame.offset.y", 0.0) : 0.0;
        double flameOffsetZ = particleSection != null ? particleSection.getDouble("flame.offset.z", 0.0) : 0.0;
        Particle circleParticle = parseParticle(particleSection != null ? particleSection.getString("circle.type") : null);
        int circleDelayTicks = particleSection != null ? particleSection.getInt("circle.delay", 5) : 5;
        int circleDuration = particleSection != null ? particleSection.getInt("circle.duration", 3) : 3;
        double circleRadius = particleSection != null ? particleSection.getDouble("circle.radius", 1.5) : 1.5;
        int circleAngleStep = particleSection != null ? particleSection.getInt("circle.angleStep", 20) : 20;
        org.bukkit.configuration.ConfigurationSection pushSection = section.getConfigurationSection("pushEffect");
        double pushRadius = pushSection != null ? pushSection.getDouble("radius", 5.0) : 5.0;
        double verticalBoost = pushSection != null ? pushSection.getDouble("verticalBoost", 0.2) : 0.2;
        double minForce = pushSection != null ? pushSection.getDouble("minForce", 1.0) : 1.0;
        double maxForce = pushSection != null ? pushSection.getDouble("maxForce", 3.0) : 3.0;
        SoundConfig sound = readSoundConfig(section.getConfigurationSection("sound"), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1.0f, 0.5f);
        org.bukkit.configuration.ConfigurationSection messages = section.getConfigurationSection("messages");
        String targetTitle = messages != null ? messages.getString("target.title") : "";
        String targetSubtitle = messages != null ? messages.getString("target.subtitle") : "";
        String targetChat = messages != null ? messages.getString("target.chatMessage") : "";
        return new BoskiToporConfig(invulnerability, explosionParticle, explosionCount, flameParticle, flameCount,
            flameOffsetX, flameOffsetY, flameOffsetZ, circleParticle, circleDelayTicks, circleDuration,
            circleRadius, circleAngleStep, pushRadius, verticalBoost, minForce, maxForce, sound,
            targetTitle, targetSubtitle, targetChat);
    }

    private List<ChangedBlock> createHydroCage(Location center, HydroCageConfig config) {
        if (center.getWorld() == null) {
            return List.of();
        }
        int radius = Math.max(1, config.radius);
        int thickness = Math.max(1, config.wallThickness);
        int innerRadius = Math.max(0, radius - thickness);
        int outerRadiusSquared = radius * radius;
        int innerRadiusSquared = innerRadius * innerRadius;
        List<ChangedBlock> changedBlocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int distanceSquared = x * x + y * y + z * z;
                    if (distanceSquared > outerRadiusSquared || distanceSquared < innerRadiusSquared) {
                        continue;
                    }
                    Material targetMaterial = config.wallMaterial;
                    Location target = center.clone().add(x, y, z);
                    Block block = target.getBlock();
                    if (!isReplaceable(block.getType())) {
                        continue;
                    }
                    BlockData previous = block.getBlockData();
                    block.setType(targetMaterial, false);
                    block.setMetadata("hydro_cage_block", new FixedMetadataValue(plugin, true));
                    changedBlocks.add(new ChangedBlock(block, previous));
                }
            }
        }
        return changedBlocks;
    }

    private void scheduleHydroCageRemoval(List<ChangedBlock> blocks, HydroCageConfig config) {
        if (blocks.isEmpty()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ChangedBlock changed : blocks) {
                    Block block = changed.block;
                    block.setBlockData(changed.previousData, false);
                    block.removeMetadata("hydro_cage_block", plugin);
                }
                Location location = blocks.get(0).block.getLocation();
                playHydroSound(location, config.cageExpireSound);
            }
        }.runTaskLater(plugin, Math.max(1, config.durationSeconds) * 20L);
    }

    private boolean isReplaceable(Material material) {
        return material == Material.AIR
            || material == Material.CAVE_AIR
            || material == Material.VOID_AIR
            || material == Material.WATER;
    }

    private void sendHydroMessage(Player player, String raw) {
        if (player == null || raw == null || raw.isBlank()) {
            return;
        }
        player.sendMessage(MiniMessageUtil.parseComponent(raw));
    }

    private void sendHydroTitle(Player player, String titleRaw, String subtitleRaw) {
        if (player == null) {
            return;
        }
        if ((titleRaw == null || titleRaw.isBlank()) && (subtitleRaw == null || subtitleRaw.isBlank())) {
            return;
        }
        Title title = Title.title(
            MiniMessageUtil.parseComponent(defaultString(titleRaw)),
            MiniMessageUtil.parseComponent(defaultString(subtitleRaw))
        );
        player.showTitle(title);
    }

    private void playHydroSound(Location location, SoundConfig sound) {
        if (location.getWorld() == null || sound == null || sound.sound == null) {
            return;
        }
        location.getWorld().playSound(location, sound.sound, sound.volume, sound.pitch);
    }

    private org.bukkit.configuration.ConfigurationSection loadEventItemSection(String id) {
        String normalized = normalizeItemId(id);
        File file = new File(plugin.getDataFolder(), "configs/customitems/" + normalized + ".yml");
        if (!file.exists()) {
            return null;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        if (yaml.getKeys(false).isEmpty()) {
            return null;
        }
        String root = yaml.getKeys(false).iterator().next();
        return yaml.getConfigurationSection(root);
    }

    private Material parseMaterial(String raw, Material fallback) {
        if (raw == null || raw.isBlank()) {
            return fallback;
        }
        Material material = Material.matchMaterial(raw);
        return material != null ? material : fallback;
    }

    private Particle parseParticle(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Particle.valueOf(raw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private SoundConfig readSoundConfig(org.bukkit.configuration.ConfigurationSection section, Sound fallback, float fallbackVolume, float fallbackPitch) {
        if (section == null) {
            return new SoundConfig(fallback, fallbackVolume, fallbackPitch);
        }
        boolean enabled = section.getBoolean("enabled", true);
        if (!enabled) {
            return null;
        }
        Sound sound = fallback;
        String raw = section.getString("sound");
        if (raw == null || raw.isBlank()) {
            raw = section.getString("type");
        }
        if (raw != null && !raw.isBlank()) {
            try {
                sound = Sound.valueOf(raw.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                sound = fallback;
            }
        }
        float volume = (float) section.getDouble("volume", fallbackVolume);
        float pitch = (float) section.getDouble("pitch", fallbackPitch);
        return new SoundConfig(sound, volume, pitch);
    }

    private record ChangedBlock(Block block, BlockData previousData) {
    }

    private record SoundConfig(Sound sound, float volume, float pitch) {
    }

    private record HydroCageConfig(
        int radius,
        int durationSeconds,
        int wallThickness,
        Material wallMaterial,
        Material defaultMaterial,
        Material fillMaterial,
        double projectileSpeed,
        boolean projectileGravity,
        String shootTitle,
        String shootSubtitle,
        String cageCreatedTitle,
        String cageCreatedSubtitle,
        String cageExpiredTitle,
        String cageExpiredSubtitle,
        String cannotBreakMessage,
        String cannotPlaceMessage,
        String cannotUseNetherMessage,
        String cannotUseEndMessage,
        boolean allowNether,
        boolean allowEnd,
        boolean allowBlockPlacing,
        boolean allowBreaking,
        SoundConfig shootSound,
        SoundConfig cageCreateSound,
        SoundConfig cageExpireSound
    ) {
        boolean allowedInWorld(org.bukkit.World world) {
            if (world == null) {
                return false;
            }
            if (world.getEnvironment() == org.bukkit.World.Environment.NETHER) {
                return allowNether;
            }
            if (world.getEnvironment() == org.bukkit.World.Environment.THE_END) {
                return allowEnd;
            }
            return true;
        }
    }

    private record BoskiToporConfig(
        int invulnerabilitySeconds,
        Particle explosionParticle,
        int explosionCount,
        Particle flameParticle,
        int flameCount,
        double flameOffsetX,
        double flameOffsetY,
        double flameOffsetZ,
        Particle circleParticle,
        int circleDelayTicks,
        int circleDurationSeconds,
        double circleRadius,
        int circleAngleStep,
        double pushRadius,
        double pushVerticalBoost,
        double pushMinForce,
        double pushMaxForce,
        SoundConfig sound,
        String targetTitle,
        String targetSubtitle,
        String targetChatMessage
    ) {
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
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
        event.setCancelled(true);
    }
}
