package me.anarchiacore.combatlog;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CombatLogConfig {
    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    private boolean debug;
    private Duration combatDuration;
    private boolean monsterAntylogout;
    private boolean enderPearlAntylogout;
    private boolean blockGliding;
    private boolean voidAntylogout;
    private List<String> disabledRegions;
    private Set<Material> blockedBlocks;
    private Set<EntityType> blockedEntities;
    private boolean disableCommands;
    private List<String> allowedCommands;
    private DisplayType combatDisplay;
    private Notice combatNotice;
    private boolean combatSendEnd;
    private Notice combatEndNotice;

    private Duration protectionDuration;
    private boolean protectionDamageOnProtection;
    private String protectionSuffix;
    private DisplayType protectionDisplay;
    private Notice protectionNotice;
    private Duration protectionDeathDuration;
    private Notice protectionDeathNotice;
    private boolean protectionSendEnd;
    private Notice protectionEndNotice;

    private BarrierType barrierType;
    private Material barrierWallMaterial;
    private double barrierKnockbackStrength;
    private double barrierKnockbackY;

    private CreativeDamageMode creativeDamageMode;
    private boolean pvpDisabled;
    private boolean autorespawn;

    public CombatLogConfig(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
        this.debug = config.getBoolean("debug", false);
        this.combatDuration = parseDuration(config.getString("antylogout.duration", "30s"), Duration.ofSeconds(30));
        this.monsterAntylogout = config.getBoolean("antylogout.monster-antylogout", true);
        this.enderPearlAntylogout = config.getBoolean("antylogout.ender-pearl-antylogout", true);
        this.blockGliding = config.getBoolean("antylogout.block-gliding", true);
        this.voidAntylogout = config.getBoolean("antylogout.voidAntylogout", true);
        this.disabledRegions = new ArrayList<>(config.getStringList("antylogout.disabled-regions"));
        this.blockedBlocks = parseMaterials(config.getStringList("antylogout.blocked-blocks"));
        this.blockedEntities = parseEntities(config.getStringList("antylogout.blockedEntity"));
        this.disableCommands = config.getBoolean("antylogout.disable-commands", true);
        this.allowedCommands = new ArrayList<>(config.getStringList("antylogout.allowed-commands"));
        this.combatDisplay = parseDisplay(config.getString("antylogout.display", "BOSSBAR"));
        this.combatNotice = parseNotice("antylogout.notice", "&cJestes podczas walki! Pozostaly czas: &4{time}", "RED", "SOLID");
        this.combatSendEnd = config.getBoolean("antylogout.send-end", true);
        this.combatEndNotice = parseNotice("antylogout.end-notice", "&aWalka zakonczona! Mozesz sie bezpiecznie wylogowac!", "GREEN", "SOLID");

        this.protectionDuration = parseDuration(config.getString("protection.duration", "0s"), Duration.ZERO);
        this.protectionDamageOnProtection = config.getBoolean("protection.damage-on-protection", true);
        this.protectionSuffix = config.getString("protection.suffix", "");
        this.protectionDisplay = parseDisplay(config.getString("protection.display", "BOSSBAR"));
        this.protectionNotice = parseNotice("protection.notice", "&aAktywna ochrona: &2{time}", "GREEN", "SOLID");
        this.protectionDeathDuration = parseDuration(config.getString("protection.death-duration", "0s"), Duration.ZERO);
        this.protectionDeathNotice = parseNotice("protection.death-notice", "&eAktywna ochrona po smierci: &6({time})", "YELLOW", "SOLID");
        this.protectionSendEnd = config.getBoolean("protection.send-end", true);
        this.protectionEndNotice = parseNotice("protection.end-notice", "&cOchrona sie skonczyla. Uwazaj na siebie!", "RED", "SOLID");

        this.barrierType = parseBarrierType(config.getString("barrier.barrier-type", "KNOCKBACK"));
        this.barrierWallMaterial = parseMaterial(config.getString("barrier.wall-block", "RED_STAINED_GLASS"), Material.RED_STAINED_GLASS);
        this.barrierKnockbackStrength = config.getDouble("barrier.knockback-strength", 1.2);
        this.barrierKnockbackY = config.getDouble("barrier.knockback-y", 0.2);

        this.creativeDamageMode = parseCreativeDamageMode(config.getString("misc.cancel-creative-damage", "NONE"));
        this.pvpDisabled = config.getBoolean("misc.pvpDisabled", false);
        this.autorespawn = config.getBoolean("misc.autorespawn", true);
    }

    public void save() throws IOException {
        if (config != null) {
            config.save(file);
        }
    }

    public YamlConfiguration getRawConfig() {
        return config;
    }

    public boolean isDebug() {
        return debug;
    }

    public Duration getCombatDuration() {
        return combatDuration;
    }

    public boolean isMonsterAntylogout() {
        return monsterAntylogout;
    }

    public boolean isEnderPearlAntylogout() {
        return enderPearlAntylogout;
    }

    public boolean isBlockGliding() {
        return blockGliding;
    }

    public boolean isVoidAntylogout() {
        return voidAntylogout;
    }

    public List<String> getDisabledRegions() {
        return disabledRegions;
    }

    public Set<Material> getBlockedBlocks() {
        return blockedBlocks;
    }

    public Set<EntityType> getBlockedEntities() {
        return blockedEntities;
    }

    public boolean isDisableCommands() {
        return disableCommands;
    }

    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    public DisplayType getCombatDisplay() {
        return combatDisplay;
    }

    public Notice getCombatNotice() {
        return combatNotice;
    }

    public boolean isCombatSendEnd() {
        return combatSendEnd;
    }

    public Notice getCombatEndNotice() {
        return combatEndNotice;
    }

    public Duration getProtectionDuration() {
        return protectionDuration;
    }

    public boolean isProtectionDamageOnProtection() {
        return protectionDamageOnProtection;
    }

    public String getProtectionSuffix() {
        return protectionSuffix;
    }

    public DisplayType getProtectionDisplay() {
        return protectionDisplay;
    }

    public Notice getProtectionNotice() {
        return protectionNotice;
    }

    public Duration getProtectionDeathDuration() {
        return protectionDeathDuration;
    }

    public Notice getProtectionDeathNotice() {
        return protectionDeathNotice;
    }

    public boolean isProtectionSendEnd() {
        return protectionSendEnd;
    }

    public Notice getProtectionEndNotice() {
        return protectionEndNotice;
    }

    public BarrierType getBarrierType() {
        return barrierType;
    }

    public Material getBarrierWallMaterial() {
        return barrierWallMaterial;
    }

    public double getBarrierKnockbackStrength() {
        return barrierKnockbackStrength;
    }

    public double getBarrierKnockbackY() {
        return barrierKnockbackY;
    }

    public CreativeDamageMode getCreativeDamageMode() {
        return creativeDamageMode;
    }

    public boolean isPvpDisabled() {
        return pvpDisabled;
    }

    public void setPvpDisabled(boolean pvpDisabled) {
        this.pvpDisabled = pvpDisabled;
        if (config != null) {
            config.set("misc.pvpDisabled", pvpDisabled);
        }
    }

    public boolean isAutorespawn() {
        return autorespawn;
    }

    public void setAutorespawn(boolean autorespawn) {
        this.autorespawn = autorespawn;
        if (config != null) {
            config.set("misc.autorespawn", autorespawn);
        }
    }

    private Notice parseNotice(String path, String fallbackTitle, String fallbackColor, String fallbackStyle) {
        String title = config.getString(path + ".title", fallbackTitle);
        BossBar.Color color = parseBossBarColor(config.getString(path + ".color", fallbackColor));
        BossBar.Overlay overlay = parseBossBarOverlay(config.getString(path + ".style", fallbackStyle));
        return new Notice(title, color, overlay);
    }

    private DisplayType parseDisplay(String value) {
        if (value == null) {
            return DisplayType.BOSSBAR;
        }
        return switch (value.trim().toUpperCase(Locale.ROOT)) {
            case "ACTIONBAR" -> DisplayType.ACTIONBAR;
            default -> DisplayType.BOSSBAR;
        };
    }

    private BossBar.Color parseBossBarColor(String value) {
        if (value == null) {
            return BossBar.Color.RED;
        }
        try {
            return BossBar.Color.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return BossBar.Color.RED;
        }
    }

    private BossBar.Overlay parseBossBarOverlay(String value) {
        if (value == null) {
            return BossBar.Overlay.PROGRESS;
        }
        try {
            return BossBar.Overlay.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return BossBar.Overlay.PROGRESS;
        }
    }

    private BarrierType parseBarrierType(String value) {
        if (value == null) {
            return BarrierType.KNOCKBACK;
        }
        try {
            return BarrierType.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return BarrierType.KNOCKBACK;
        }
    }

    private CreativeDamageMode parseCreativeDamageMode(String value) {
        if (value == null) {
            return CreativeDamageMode.NONE;
        }
        try {
            return CreativeDamageMode.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return CreativeDamageMode.NONE;
        }
    }

    private Set<Material> parseMaterials(List<String> values) {
        Set<Material> materials = new HashSet<>();
        if (values == null) {
            return materials;
        }
        for (String value : values) {
            if (value == null) {
                continue;
            }
            Material material = parseMaterial(value, null);
            if (material != null) {
                materials.add(material);
            }
        }
        return materials;
    }

    private Material parseMaterial(String value, Material fallback) {
        if (value == null) {
            return fallback;
        }
        Material material = Material.matchMaterial(value.trim().toUpperCase(Locale.ROOT));
        return material != null ? material : fallback;
    }

    private Set<EntityType> parseEntities(List<String> values) {
        Set<EntityType> entities = new HashSet<>();
        if (values == null) {
            return entities;
        }
        for (String value : values) {
            if (value == null) {
                continue;
            }
            try {
                entities.add(EntityType.valueOf(value.trim().toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException ex) {
                if (debug) {
                    plugin.getLogger().warning("Nieznany byt w blockedEntity: " + value);
                }
            }
        }
        return entities;
    }

    public static Duration parseDuration(String value, Duration fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String trimmed = value.trim().toLowerCase(Locale.ROOT);
        long multiplier = 1;
        if (trimmed.endsWith("ms")) {
            multiplier = 1;
            trimmed = trimmed.substring(0, trimmed.length() - 2);
        } else if (trimmed.endsWith("s")) {
            multiplier = 1000;
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        } else if (trimmed.endsWith("m")) {
            multiplier = 60_000;
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        } else if (trimmed.endsWith("h")) {
            multiplier = 3_600_000;
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        try {
            long amount = Long.parseLong(trimmed.trim());
            return Duration.ofMillis(amount * multiplier);
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    public record Notice(String title, BossBar.Color color, BossBar.Overlay overlay) {
    }

    public enum DisplayType {
        BOSSBAR,
        ACTIONBAR
    }

    public enum BarrierType {
        KNOCKBACK,
        BLOCK,
        WALL,
        IGNORE
    }

    public enum CreativeDamageMode {
        NONE,
        PLAYER,
        MONSTER,
        BOTH
    }
}
