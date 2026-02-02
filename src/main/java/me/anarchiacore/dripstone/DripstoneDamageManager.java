package me.anarchiacore.dripstone;

import me.anarchiacore.config.MessageService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.Locale;
import java.util.Map;

public class DripstoneDamageManager implements Listener {
    private static final String PERMISSION = "anarchiacore.admin.dripstone";

    private final JavaPlugin plugin;
    private final MessageService messageService;
    private boolean enabled;
    private double multiplier;

    public DripstoneDamageManager(JavaPlugin plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
        reload();
    }

    public void reload() {
        enabled = plugin.getConfig().getBoolean("dripstone-damage.enabled", true);
        String raw = plugin.getConfig().getString("dripstone-damage.multiplier", "1.0");
        Double parsed = parseMultiplier(raw);
        if (parsed == null) {
            multiplier = 1.0;
            plugin.getLogger().warning("Nieprawidłowy dripstone-damage.multiplier w config.yml. Ustawiono 1.0");
        } else {
            multiplier = parsed;
        }
    }

    public boolean handleCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            messageService.send(sender, plugin.getConfig().getString("messages.noPermission"));
            return true;
        }
        if (args.length == 0) {
            messageService.send(sender, plugin.getConfig().getString("messages.dripstone.usage"));
            return true;
        }
        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "get" -> {
                messageService.send(sender, plugin.getConfig().getString("messages.dripstone.get"),
                    Map.of("value", formatMultiplier(multiplier)));
                return true;
            }
            case "multiplier", "set" -> {
                if (args.length < 2) {
                    messageService.send(sender, plugin.getConfig().getString("messages.dripstone.usage"));
                    return true;
                }
                Double parsed = parseMultiplier(args[1]);
                if (parsed == null) {
                    messageService.send(sender, plugin.getConfig().getString("messages.dripstone.invalid"));
                    return true;
                }
                multiplier = parsed;
                plugin.getConfig().set("dripstone-damage.multiplier", multiplier);
                plugin.saveConfig();
                messageService.send(sender, plugin.getConfig().getString("messages.dripstone.set"),
                    Map.of("value", formatMultiplier(multiplier)));
                return true;
            }
            case "reload" -> {
                reload();
                messageService.send(sender, plugin.getConfig().getString("messages.dripstone.reload"));
                return true;
            }
            default -> {
                messageService.send(sender, plugin.getConfig().getString("messages.dripstone.usage"));
                return true;
            }
        }
    }

    public java.util.List<String> tabComplete(String[] args) {
        if (args.length == 1) {
            return java.util.List.of("get", "multiplier", "reload");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("multiplier")) {
            return java.util.List.of("0.0", "0.5", "1.0", "1.5", "2.0");
        }
        return java.util.List.of();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!enabled) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!isDripstoneDamage(event)) {
            return;
        }
        if (multiplier == 1.0) {
            return;
        }
        double baseDamage = event.getDamage();
        event.setDamage(baseDamage * multiplier);
    }

    private boolean isDripstoneDamage(EntityDamageEvent event) {
        DamageType damageType = event.getDamageSource().getDamageType();
        if (damageType == DamageType.STALAGMITE || damageType == DamageType.FALLING_STALACTITE) {
            return true;
        }
        if (event instanceof EntityDamageByEntityEvent byEntity) {
            if (isFallingDripstone(byEntity.getDamager())) {
                return true;
            }
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK
            && event instanceof EntityDamageByEntityEvent byEntity) {
            return isFallingDripstone(byEntity.getDamager());
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            return hasPointedDripstoneNearby(event.getEntity());
        }
        return false;
    }

    private boolean isFallingDripstone(Entity damager) {
        if (damager instanceof FallingBlock fallingBlock) {
            return fallingBlock.getBlockData().getMaterial() == Material.POINTED_DRIPSTONE;
        }
        return false;
    }

    private boolean hasPointedDripstoneNearby(Entity entity) {
        BoundingBox box = entity.getBoundingBox().expand(0.1, 0.1, 0.1);
        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.floor(box.getMaxX());
        int minY = (int) Math.floor(box.getMinY());
        int maxY = (int) Math.floor(box.getMaxY());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = entity.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.POINTED_DRIPSTONE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Double parseMultiplier(String raw) {
        if (raw == null) {
            return null;
        }
        String normalized = raw.trim().replace(',', '.');
        if (normalized.isEmpty()) {
            return null;
        }
        try {
            double value = Double.parseDouble(normalized);
            if (!Double.isFinite(value) || value < 0.0) {
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String formatMultiplier(double value) {
        return String.format(Locale.ROOT, "%.2f", value).replaceAll("\\.?0+$", "");
    }
}
