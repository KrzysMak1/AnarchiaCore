package me.anarchiacore.customhits;

import me.anarchiacore.util.MiniMessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StormItemyCommandRouter implements CommandExecutor, TabCompleter {
    private static final double MIN_MULTIPLIER = 0.1;
    private static final double MAX_MULTIPLIER = 10.0;

    private final JavaPlugin plugin;
    private final CustomHitManager customHitManager;
    private final CommandExecutor delegateExecutor;
    private final TabCompleter delegateTabCompleter;

    public StormItemyCommandRouter(
        JavaPlugin plugin,
        CustomHitManager customHitManager,
        CommandExecutor delegateExecutor,
        TabCompleter delegateTabCompleter
    ) {
        this.plugin = plugin;
        this.customHitManager = customHitManager;
        this.delegateExecutor = delegateExecutor;
        this.delegateTabCompleter = delegateTabCompleter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("customhit")) {
            return handleCustomHitCommand(sender, args);
        }
        if (delegateExecutor != null) {
            return delegateExecutor.onCommand(sender, command, label, args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("customhit");
            if (delegateTabCompleter != null) {
                List<String> delegate = delegateTabCompleter.onTabComplete(sender, command, alias, args);
                if (delegate != null) {
                    suggestions.addAll(delegate);
                }
            }
            return suggestions;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("customhit")) {
            return List.of("set", "info");
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("customhit")) {
            return Collections.emptyList();
        }
        if (delegateTabCompleter != null) {
            List<String> delegate = delegateTabCompleter.onTabComplete(sender, command, alias, args);
            return delegate != null ? delegate : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public boolean handleCustomHitCommand(CommandSender sender, String[] args) {
        LangContext lang = loadLangContext();
        if (!sender.hasPermission("stormitemy.admin")) {
            sendMessage(sender, lang, "messages.noPermission", "&cBrak uprawnień.", null);
            return true;
        }
        if (args.length == 1) {
            sendCustomHitHelp(sender, lang);
            return true;
        }
        if (args[1].equalsIgnoreCase("info")) {
            double multiplier = resolveMultiplier(lang);
            sendMessage(sender, lang, "messages.customhit_info",
                "&7Aktualny mnożnik damage: &f{MULTIPLIER}",
                Map.of("MULTIPLIER", formatMultiplier(multiplier)));
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sendMessage(sender, lang, "messages.customhit_usage_set",
                    "&7Użycie: &f/stormitemy customhit set <mnożnik>", null);
                return true;
            }
            Double parsed = parseMultiplier(args[2]);
            if (parsed == null) {
                sendMessage(sender, lang, "messages.customhit_invalid_number",
                    "&cPodana wartość nie jest prawidłową liczbą!", null);
                return true;
            }
            if (parsed < MIN_MULTIPLIER || parsed > MAX_MULTIPLIER) {
                sendMessage(sender, lang, "messages.customhit_invalid_range",
                    "&cMnożnik musi być w zakresie od 0.1 do 10.0!", null);
                return true;
            }
            if (!saveMultiplier(lang, parsed)) {
                sendMessage(sender, lang, "messages.customhit_save_error",
                    "&cWystąpił błąd podczas zapisu konfiguracji StormItemy.", null);
                return true;
            }
            if (customHitManager != null) {
                customHitManager.reload();
            }
            sendMessage(sender, lang, "messages.customhit_set_success",
                "&aMnożnik damage ustawiony na: &f{MULTIPLIER}",
                Map.of("MULTIPLIER", formatMultiplier(parsed)));
            return true;
        }
        sendCustomHitHelp(sender, lang);
        return true;
    }

    private void sendCustomHitHelp(CommandSender sender, LangContext lang) {
        sendMessage(sender, lang, "messages.customhit_help_header",
            "&7Komendy Custom Hit:", null);
        sendMessage(sender, lang, "messages.customhit_help_set",
            "&f/stormitemy customhit set <mnożnik> &8- &7Ustaw mnożnik damage (0.1-10.0)", null);
        sendMessage(sender, lang, "messages.customhit_help_info",
            "&f/stormitemy customhit info &8- &7Wyświetl aktualny mnożnik", null);
    }

    private Double parseMultiplier(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private boolean saveMultiplier(LangContext lang, double value) {
        if (lang.stormConfig == null || lang.stormConfigFile == null) {
            return false;
        }
        lang.stormConfig.set("customhit.damage_multiplier", value);
        try {
            lang.stormConfig.save(lang.stormConfigFile);
            return true;
        } catch (IOException ex) {
            plugin.getLogger().warning("Failed to save StormItemy config: " + ex.getMessage());
            return false;
        }
    }

    private double resolveMultiplier(LangContext lang) {
        if (lang.stormConfig != null) {
            double fallback = customHitManager != null ? customHitManager.getMultiplier() : 1.0;
            return lang.stormConfig.getDouble("customhit.damage_multiplier", fallback);
        }
        return customHitManager != null ? customHitManager.getMultiplier() : 1.0;
    }

    private String formatMultiplier(double value) {
        if (Math.floor(value) == value) {
            return String.format(Locale.ROOT, "%.0f", value);
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private void sendMessage(CommandSender sender, LangContext lang, String path, String fallback, Map<String, String> placeholders) {
        String raw = fallback;
        if (lang.langConfig != null) {
            raw = lang.langConfig.getString(path, fallback);
        }
        Component component = MiniMessageUtil.parseComponent(raw, placeholders);
        if (sender instanceof Player player) {
            player.sendMessage(component);
        } else {
            sender.sendMessage(component);
        }
    }

    private LangContext loadLangContext() {
        File stormConfigFile = getStormItemyConfigFile();
        if (stormConfigFile == null || !stormConfigFile.exists()) {
            return new LangContext(null, null, stormConfigFile);
        }
        YamlConfiguration stormConfig = YamlConfiguration.loadConfiguration(stormConfigFile);
        String language = stormConfig.getString("language", "POL");
        File langFile = new File(stormConfigFile.getParentFile(), "lang/" + language + ".yml");
        YamlConfiguration langConfig = null;
        if (langFile.exists()) {
            langConfig = YamlConfiguration.loadConfiguration(langFile);
        }
        return new LangContext(stormConfig, langConfig, stormConfigFile);
    }

    private File getStormItemyConfigFile() {
        String path = plugin.getConfig().getString("customHits.stormItemyConfigPath",
            "configs/STORMITEMY/config.yml");
        if (path == null || path.isBlank()) {
            return null;
        }
        return new File(plugin.getDataFolder(), path);
    }

    private record LangContext(YamlConfiguration stormConfig, YamlConfiguration langConfig, File stormConfigFile) {
    }
}
