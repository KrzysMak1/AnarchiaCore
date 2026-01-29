package me.anarchiacore.combatlog;

import me.anarchiacore.customitems.CustomItemsManager;
import me.anarchiacore.storage.DataStore;
import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleGlideEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CombatLogManager implements Listener {
    private static final String BYPASS_PERMISSION = "dream.antylogout.bypass";

    private final JavaPlugin plugin;
    private final CombatLogStorage storage;
    private final CombatLogConfig config;
    private final CombatLogMessageConfig messageConfig;
    private final CombatLogMessageService messageService;
    private CombatLogRegionService regionService;
    private final CustomItemsManager customItemsManager;
    private final DataStore dataStore;
    private final Map<UUID, CombatState> combatStates = new HashMap<>();
    private final Map<UUID, ProtectionState> protectionStates = new HashMap<>();
    private final Map<UUID, Component> originalDisplayNames = new HashMap<>();
    private final Map<UUID, Component> originalPlayerListNames = new HashMap<>();
    private BukkitTask task;

    public CombatLogManager(JavaPlugin plugin, String prefix, CustomItemsManager customItemsManager, DataStore dataStore) {
        this.plugin = plugin;
        this.customItemsManager = customItemsManager;
        this.dataStore = dataStore;
        this.storage = new CombatLogStorage(plugin);
        File configFile = resolveConfigFile("config.yml");
        File messageFile = resolveConfigFile("message.yml");
        this.config = new CombatLogConfig(plugin, configFile);
        this.messageConfig = new CombatLogMessageConfig(messageFile);
        this.messageService = new CombatLogMessageService(messageConfig);
        this.messageService.setPrefix(prefix);
        reload();
    }

    public void start() {
        stop();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        clearBossBars();
        restoreDisplayNames();
        combatStates.clear();
        protectionStates.clear();
        originalDisplayNames.clear();
        originalPlayerListNames.clear();
    }

    public void reload() {
        config.load();
        messageConfig.load();
        storage.reload();
        regionService = new CombatLogRegionService(config, plugin);
        regionService.load();
    }

    private File resolveConfigFile(String fileName) {
        File directory = new File(plugin.getDataFolder(), "combatlog");
        if (!directory.exists() && !directory.mkdirs()) {
            plugin.getLogger().warning("Nie można utworzyć katalogu combatlog.");
        }
        File file = new File(directory, fileName);
        if (!file.exists()) {
            exportDefault(fileName, file);
        }
        return file;
    }

    private void exportDefault(String fileName, File target) {
        String resourcePath = "me/anarchiacore/combatlog/" + fileName;
        try (var input = plugin.getResource(resourcePath)) {
            if (input == null) {
                plugin.getLogger().warning("Nie znaleziono zasobu combatlog: " + resourcePath);
                return;
            }
            if (target.getParentFile() != null) {
                target.getParentFile().mkdirs();
            }
            java.nio.file.Files.copy(input, target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            plugin.getLogger().warning("Nie można zapisać domyślnego pliku combatlog " + fileName + ": " + ex.getMessage());
        }
    }

    public void updatePrefix(String prefix) {
        messageService.setPrefix(prefix);
    }

    public boolean handleCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            messageService.send(sender, "command-usage", Map.of("label", "/anarchiacore combatlog <reload|pvp|autorespawn|protection-off>"));
            return true;
        }
        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "reload" -> {
                if (!sender.hasPermission("dream.antylogout.command.reload")) {
                    messageService.send(sender, "command-no-permission");
                    return true;
                }
                long start = System.currentTimeMillis();
                try {
                    reload();
                    clearBossBars();
                    messageService.send(sender, "config-reloaded", Map.of("time", formatDuration(Duration.ofMillis(System.currentTimeMillis() - start))));
                } catch (Exception ex) {
                    messageService.send(sender, "config-reload-error", Map.of("error", ex.getMessage()));
                }
                return true;
            }
            case "pvp" -> {
                if (!sender.hasPermission("dream.antylogout.command.pvp")) {
                    messageService.send(sender, "command-no-permission");
                    return true;
                }
                if (args.length < 2) {
                    messageService.send(sender, "command-invalid-format", Map.of("input", ""));
                    return true;
                }
                String status = args[1].toLowerCase(Locale.ROOT);
                if (!status.equals("on") && !status.equals("off")) {
                    messageService.send(sender, "command-invalid-format", Map.of("input", status));
                    return true;
                }
                config.setPvpDisabled(status.equals("off"));
                saveConfig();
                String statusText = config.isPvpDisabled() ? messageService.getText("disabled") : messageService.getText("enabled");
                messageService.send(sender, "pvp-status-changed", Map.of("status", statusText));
                return true;
            }
            case "autorespawn" -> {
                if (!sender.hasPermission("dream.antylogout.command.autorespawn")) {
                    messageService.send(sender, "command-no-permission");
                    return true;
                }
                if (args.length < 2) {
                    messageService.send(sender, "command-invalid-format", Map.of("input", ""));
                    return true;
                }
                String status = args[1].toLowerCase(Locale.ROOT);
                if (!status.equals("on") && !status.equals("off")) {
                    messageService.send(sender, "command-invalid-format", Map.of("input", status));
                    return true;
                }
                config.setAutorespawn(status.equals("on"));
                saveConfig();
                String statusText = config.isAutorespawn() ? messageService.getText("enabled") : messageService.getText("disabled");
                messageService.send(sender, "autorespawn-status-changed", Map.of("status", statusText));
                return true;
            }
            case "protection-off", "wylacz", "ochrona" -> {
                if (!sender.hasPermission("dream.antylogout.command.protection-off")) {
                    messageService.send(sender, "command-no-permission");
                    return true;
                }
                if (!(sender instanceof Player player)) {
                    messageService.send(sender, "command-not-player");
                    return true;
                }
                ProtectionState state = protectionStates.get(player.getUniqueId());
                if (state == null || state.isExpired()) {
                    messageService.send(sender, "no-protection");
                    return true;
                }
                removeProtection(player, true);
                return true;
            }
            default -> {
                messageService.send(sender, "command-path-not-found");
                return true;
            }
        }
    }

    public List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return List.of("reload", "pvp", "autorespawn", "protection-off");
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("pvp") || args[0].equalsIgnoreCase("autorespawn"))) {
            return List.of("on", "off");
        }
        return List.of();
    }

    public int getActiveTaskCount() {
        return task != null && !task.isCancelled() ? 1 : 0;
    }

    public int getListenerCount() {
        return 1;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }
        Player attacker = extractPlayer(event.getDamager());
        boolean attackerBypass = attacker != null && attacker.hasPermission(BYPASS_PERMISSION);
        if (attacker != null && !attackerBypass) {
            if (config.isPvpDisabled()) {
                event.setCancelled(true);
                messageService.send(attacker, "pvp-is-off");
                return;
            }
            if (shouldCancelCreativeDamage(attacker, victim)) {
                event.setCancelled(true);
                messageService.send(attacker, "cant-hurt-on-creative");
                return;
            }
            if (hasProtection(victim)) {
                event.setCancelled(true);
                messageService.send(attacker, "player-has-protection");
                messageService.send(victim, "you-have-protection");
                return;
            }
            if (hasProtection(attacker)) {
                if (config.isProtectionDamageOnProtection()) {
                    removeProtection(attacker, false);
                } else {
                    event.setCancelled(true);
                    messageService.send(attacker, "you-have-protection");
                    return;
                }
            }
        }

        if (event.isCancelled()) {
            return;
        }
        if (attacker != null) {
            if (!attackerBypass) {
                tagCombat(attacker, victim.getName());
            }
            tagCombat(victim, attacker.getName());
        } else if (config.isMonsterAntylogout() && event.getDamager() instanceof Monster) {
            tagCombat(victim, event.getDamager().getType().name().toLowerCase(Locale.ROOT));
        }

        if (attacker != null && !attackerBypass) {
            double remaining = Math.max(0, victim.getHealth() - event.getFinalDamage());
            messageService.send(attacker, "health-left", Map.of("hp", String.format(Locale.ROOT, "%.1f", remaining)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGenericDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (config.isVoidAntylogout() && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            tagCombat(player, "void");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && config.isEnderPearlAntylogout()) {
            tagCombat(event.getPlayer(), "ender-pearl");
        }
        if (isInCombat(event.getPlayer()) && event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND) {
            event.setCancelled(true);
            messageService.send(event.getPlayer(), "teleport-cancelled");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != null && event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Player player = event.getPlayer();
        if (!isInCombat(player) || player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        Material type = event.getClickedBlock() != null ? event.getClickedBlock().getType() : null;
        if (type != null && config.getBlockedBlocks().contains(type)) {
            event.setCancelled(true);
            messageService.send(player, "interaction-blocked");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!isInCombat(player) || player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        EntityType type = event.getRightClicked().getType();
        if (config.getBlockedEntities().contains(type)) {
            event.setCancelled(true);
            messageService.send(player, "interaction-blocked");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!isInCombat(player) || player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        if (!config.isDisableCommands()) {
            return;
        }
        String message = event.getMessage();
        if (message == null || message.isBlank()) {
            return;
        }
        String command = message.split(" ")[0].replaceFirst("/", "").toLowerCase(Locale.ROOT);
        for (String allowed : config.getAllowedCommands()) {
            if (command.equalsIgnoreCase(allowed) || command.startsWith(allowed.toLowerCase(Locale.ROOT))) {
                return;
            }
        }
        event.setCancelled(true);
        messageService.send(player, "cant-use-commands");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGlide(PlayerToggleGlideEvent event) {
        Player player = event.getPlayer();
        if (!config.isBlockGliding()) {
            return;
        }
        if (!isInCombat(player)) {
            return;
        }
        event.setCancelled(true);
        messageService.send(player, "cant-fly-in-combat");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (config.getBarrierType() == CombatLogConfig.BarrierType.IGNORE) {
            return;
        }
        Player player = event.getPlayer();
        if (!isInCombat(player) || player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to == null || from == null || to.getWorld() == null) {
            return;
        }
        if (!regionService.isDisabled(to)) {
            return;
        }
        if (config.getBarrierType() == CombatLogConfig.BarrierType.BLOCK || config.getBarrierType() == CombatLogConfig.BarrierType.WALL) {
            event.setCancelled(true);
            return;
        }
        if (config.getBarrierType() == CombatLogConfig.BarrierType.KNOCKBACK) {
            Vector direction = from.toVector().subtract(to.toVector()).normalize();
            Vector velocity = direction.multiply(config.getBarrierKnockbackStrength());
            velocity.setY(config.getBarrierKnockbackY());
            player.setVelocity(velocity);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!isInCombat(player)) {
            return;
        }
        if (player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        if (customItemsManager != null && customItemsManager.consumeTotemForCombatLogout(player)) {
            if (dataStore != null) {
                dataStore.setSpawnOnJoin(player.getUniqueId(), true);
                dataStore.save();
            }
            clearCombat(player, false);
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            var maxHealth = player.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealth != null) {
                player.damage(maxHealth.getValue());
            }
        }
        messageService.broadcast("combat-logout", Map.of("nick", player.getName()));
        combatStates.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!storage.hasJoined(player.getUniqueId())) {
            storage.setJoined(player.getUniqueId());
            storage.save();
            applyProtection(player, config.getProtectionDuration(), false);
        }
        if (dataStore != null && dataStore.getSpawnOnJoin(player.getUniqueId())) {
            Location spawn = player.getServer().getWorlds().isEmpty()
                ? player.getLocation()
                : player.getServer().getWorlds().get(0).getSpawnLocation();
            Bukkit.getScheduler().runTask(plugin, () -> player.teleport(spawn));
            dataStore.clearSpawnOnJoin(player.getUniqueId());
            dataStore.save();
            clearCombat(player, false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (config.getProtectionDeathDuration().isZero()) {
            return;
        }
        Bukkit.getScheduler().runTask(plugin, () -> applyProtection(player, config.getProtectionDeathDuration(), true));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        combatStates.remove(event.getEntity().getUniqueId());
        if (config.isAutorespawn()) {
            Player player = event.getEntity();
            Bukkit.getScheduler().runTask(plugin, player::respawn);
        }
    }

    private void tick() {
        long now = System.currentTimeMillis();
        Set<UUID> combatExpired = new HashSet<>();
        for (Map.Entry<UUID, CombatState> entry : combatStates.entrySet()) {
            CombatState state = entry.getValue();
            if (state.expiresAt() <= now) {
                combatExpired.add(entry.getKey());
                continue;
            }
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) {
                continue;
            }
            updateCombatDisplay(player, state);
        }
        for (UUID uuid : combatExpired) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                clearCombat(player, true);
            } else {
                combatStates.remove(uuid);
            }
        }

        Set<UUID> protectionExpired = new HashSet<>();
        for (Map.Entry<UUID, ProtectionState> entry : protectionStates.entrySet()) {
            ProtectionState state = entry.getValue();
            if (state.expiresAt() <= now) {
                protectionExpired.add(entry.getKey());
                continue;
            }
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) {
                continue;
            }
            updateProtectionDisplay(player, state);
        }
        for (UUID uuid : protectionExpired) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                removeProtection(player, true);
            } else {
                protectionStates.remove(uuid);
            }
        }
    }

    private void updateCombatDisplay(Player player, CombatState state) {
        Duration remaining = Duration.ofMillis(state.expiresAt() - System.currentTimeMillis());
        String formatted = formatDuration(remaining);
        Map<String, String> placeholders = Map.of("time", formatted);
        if (config.getCombatDisplay() == CombatLogConfig.DisplayType.ACTIONBAR) {
            Component component = MiniMessageUtil.parseComponent(config.getCombatNotice().title(), placeholders);
            player.sendActionBar(component);
        } else {
            BossBar bossBar = state.bossBar();
            if (bossBar == null) {
                bossBar = BossBar.bossBar(Component.empty(), 1.0f, config.getCombatNotice().color(), config.getCombatNotice().overlay());
                player.showBossBar(bossBar);
                state.setBossBar(bossBar);
            }
            bossBar.name(MiniMessageUtil.parseComponent(config.getCombatNotice().title(), placeholders));
            bossBar.progress(clampProgress(remaining, state.duration()));
        }
    }

    private void updateProtectionDisplay(Player player, ProtectionState state) {
        Duration remaining = Duration.ofMillis(state.expiresAt() - System.currentTimeMillis());
        String formatted = formatDuration(remaining);
        CombatLogConfig.Notice notice = state.deathProtection() ? config.getProtectionDeathNotice() : config.getProtectionNotice();
        Map<String, String> placeholders = Map.of("time", formatted);
        if (config.getProtectionDisplay() == CombatLogConfig.DisplayType.ACTIONBAR) {
            Component component = MiniMessageUtil.parseComponent(notice.title(), placeholders);
            player.sendActionBar(component);
        } else {
            BossBar bossBar = state.bossBar();
            if (bossBar == null) {
                bossBar = BossBar.bossBar(Component.empty(), 1.0f, notice.color(), notice.overlay());
                player.showBossBar(bossBar);
                state.setBossBar(bossBar);
            }
            bossBar.name(MiniMessageUtil.parseComponent(notice.title(), placeholders));
            bossBar.progress(clampProgress(remaining, state.duration()));
        }
    }

    private void tagCombat(Player player, String opponent) {
        if (player == null) {
            return;
        }
        if (player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }
        if (regionService.isDisabled(player.getLocation())) {
            return;
        }
        long expiresAt = System.currentTimeMillis() + config.getCombatDuration().toMillis();
        CombatState state = combatStates.get(player.getUniqueId());
        boolean newlyTagged = state == null || state.expiresAt() <= System.currentTimeMillis();
        if (state == null) {
            state = new CombatState(expiresAt, config.getCombatDuration(), null);
            combatStates.put(player.getUniqueId(), state);
        }
        state.setExpiresAt(expiresAt);
        if (newlyTagged) {
            messageService.send(player, "combat-start", Map.of("opponent", opponent));
        }
    }

    private void clearCombat(Player player, boolean sendEnd) {
        CombatState state = combatStates.remove(player.getUniqueId());
        if (state != null && state.bossBar() != null) {
            player.hideBossBar(state.bossBar());
        }
        if (sendEnd && config.isCombatSendEnd()) {
            messageService.send(player, "combat-ended");
        }
    }

    private boolean isInCombat(Player player) {
        CombatState state = combatStates.get(player.getUniqueId());
        return state != null && !state.isExpired();
    }

    public boolean isCombatTagged(UUID uuid) {
        CombatState state = combatStates.get(uuid);
        return state != null && !state.isExpired();
    }

    public int getCombatTimeLeftSeconds(UUID uuid) {
        CombatState state = combatStates.get(uuid);
        if (state == null) {
            return 0;
        }
        long remainingMillis = state.expiresAt() - System.currentTimeMillis();
        if (remainingMillis <= 0) {
            return 0;
        }
        return (int) Math.ceil(remainingMillis / 1000.0);
    }

    private void applyProtection(Player player, Duration duration, boolean deathProtection) {
        if (duration.isZero() || duration.isNegative()) {
            return;
        }
        long expiresAt = System.currentTimeMillis() + duration.toMillis();
        ProtectionState state = new ProtectionState(expiresAt, duration, null, deathProtection);
        protectionStates.put(player.getUniqueId(), state);
        updateProtectionSuffix(player, true);
        messageService.send(player, "protection-added");
    }

    private void removeProtection(Player player, boolean sendEnd) {
        ProtectionState state = protectionStates.remove(player.getUniqueId());
        if (state != null && state.bossBar() != null) {
            player.hideBossBar(state.bossBar());
        }
        updateProtectionSuffix(player, false);
        if (sendEnd && config.isProtectionSendEnd()) {
            messageService.send(player, "protection-ended");
        }
    }

    private void updateProtectionSuffix(Player player, boolean active) {
        if (config.getProtectionSuffix() == null || config.getProtectionSuffix().isBlank()) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (active) {
            originalDisplayNames.putIfAbsent(uuid, player.displayName());
            originalPlayerListNames.putIfAbsent(uuid, player.playerListName());
            Component suffix = MiniMessageUtil.parseComponent(config.getProtectionSuffix());
            player.displayName(originalDisplayNames.get(uuid).append(suffix));
            player.playerListName(originalPlayerListNames.get(uuid).append(suffix));
        } else {
            Component display = originalDisplayNames.remove(uuid);
            Component listName = originalPlayerListNames.remove(uuid);
            if (display != null) {
                player.displayName(display);
            }
            if (listName != null) {
                player.playerListName(listName);
            }
        }
    }

    private boolean hasProtection(Player player) {
        ProtectionState state = protectionStates.get(player.getUniqueId());
        return state != null && !state.isExpired();
    }

    private void clearBossBars() {
        for (Map.Entry<UUID, CombatState> entry : combatStates.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && entry.getValue().bossBar() != null) {
                player.hideBossBar(entry.getValue().bossBar());
            }
        }
        for (Map.Entry<UUID, ProtectionState> entry : protectionStates.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && entry.getValue().bossBar() != null) {
                player.hideBossBar(entry.getValue().bossBar());
            }
        }
    }

    private void restoreDisplayNames() {
        for (Map.Entry<UUID, Component> entry : originalDisplayNames.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                player.displayName(entry.getValue());
            }
        }
        for (Map.Entry<UUID, Component> entry : originalPlayerListNames.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null) {
                player.playerListName(entry.getValue());
            }
        }
    }

    private Player extractPlayer(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            return shooter;
        }
        return null;
    }

    private boolean shouldCancelCreativeDamage(Player attacker, Player victim) {
        if (attacker.getGameMode() != GameMode.CREATIVE) {
            return false;
        }
        CombatLogConfig.CreativeDamageMode mode = config.getCreativeDamageMode();
        return switch (mode) {
            case NONE -> false;
            case PLAYER -> true;
            case MONSTER -> false;
            case BOTH -> true;
        };
    }

    private void saveConfig() {
        try {
            config.save();
        } catch (IOException ex) {
            plugin.getLogger().warning("Nie można zapisać config.yml combatlog: " + ex.getMessage());
        }
    }

    private float clampProgress(Duration remaining, Duration total) {
        if (total.isZero() || total.isNegative()) {
            return 0f;
        }
        double progress = (double) remaining.toMillis() / (double) total.toMillis();
        return (float) Math.max(0.0, Math.min(1.0, progress));
    }

    private String formatDuration(Duration duration) {
        long seconds = Math.max(0, duration.toSeconds());
        long minutes = seconds / 60;
        long secs = seconds % 60;
        if (minutes > 0) {
            return String.format(Locale.ROOT, "%dm %ds", minutes, secs);
        }
        return String.format(Locale.ROOT, "%ds", secs);
    }

    private static class CombatState {
        private long expiresAt;
        private final Duration duration;
        private BossBar bossBar;

        private CombatState(long expiresAt, Duration duration, BossBar bossBar) {
            this.expiresAt = expiresAt;
            this.duration = duration;
            this.bossBar = bossBar;
        }

        private long expiresAt() {
            return expiresAt;
        }

        private void setExpiresAt(long expiresAt) {
            this.expiresAt = expiresAt;
        }

        private Duration duration() {
            return duration;
        }

        private BossBar bossBar() {
            return bossBar;
        }

        private void setBossBar(BossBar bossBar) {
            this.bossBar = bossBar;
        }

        private boolean isExpired() {
            return expiresAt <= System.currentTimeMillis();
        }
    }

    private static class ProtectionState {
        private long expiresAt;
        private final Duration duration;
        private BossBar bossBar;
        private final boolean deathProtection;

        private ProtectionState(long expiresAt, Duration duration, BossBar bossBar, boolean deathProtection) {
            this.expiresAt = expiresAt;
            this.duration = duration;
            this.bossBar = bossBar;
            this.deathProtection = deathProtection;
        }

        private long expiresAt() {
            return expiresAt;
        }

        private Duration duration() {
            return duration;
        }

        private BossBar bossBar() {
            return bossBar;
        }

        private boolean deathProtection() {
            return deathProtection;
        }

        private void setBossBar(BossBar bossBar) {
            this.bossBar = bossBar;
        }

        private boolean isExpired() {
            return expiresAt <= System.currentTimeMillis();
        }
    }
}
