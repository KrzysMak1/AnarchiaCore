package me.anarchiacore.texturepack;

import me.anarchiacore.util.MiniMessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TexturePackManager implements Listener {
    private final JavaPlugin plugin;
    private boolean enabled;
    private String stormItemyConfigPath;
    private String packUrl;
    private boolean required;
    private String kickMessage;
    private long promptDelayTicks;

    public TexturePackManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        enabled = plugin.getConfig().getBoolean("texturepack.enabled", true);
        stormItemyConfigPath = plugin.getConfig().getString("texturepack.stormItemyConfigPath", "configs/STORMITEMY/config.yml");
        packUrl = null;
        required = false;
        kickMessage = "Musisz zaakceptować texturepack aby grać na tym serwerze!";
        promptDelayTicks = 60L;
        if (!enabled) {
            return;
        }
        if (stormItemyConfigPath == null || stormItemyConfigPath.isBlank()) {
            return;
        }
        File stormConfig = new File(plugin.getDataFolder(), stormItemyConfigPath);
        if (!stormConfig.exists()) {
            plugin.getLogger().warning("Nie znaleziono konfiguracji texturepacka: " + stormConfig.getAbsolutePath());
            return;
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(stormConfig);
        enabled = yaml.getBoolean("texturepack.enabled", enabled);
        packUrl = yaml.getString("texturepack.url", packUrl);
        required = yaml.getBoolean("texturepack.required", required);
        kickMessage = yaml.getString("texturepack.kick_message", kickMessage);
        promptDelayTicks = Math.max(0L, yaml.getLong("texturepack.prompt_delay", promptDelayTicks));
        if (packUrl != null && packUrl.isBlank()) {
            packUrl = null;
        }
        if (packUrl == null) {
            plugin.getLogger().warning("Brak ustawionego URL texturepacka w " + stormItemyConfigPath);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!enabled || packUrl == null) {
            return;
        }
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(plugin, () -> sendPack(player), promptDelayTicks);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPackStatus(PlayerResourcePackStatusEvent event) {
        if (!enabled || !required) {
            return;
        }
        PlayerResourcePackStatusEvent.Status status = event.getStatus();
        if (status == PlayerResourcePackStatusEvent.Status.DECLINED
            || status == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            kickPlayer(event.getPlayer());
        }
    }

    private void sendPack(Player player) {
        if (!enabled || packUrl == null) {
            return;
        }
        if (player == null || !player.isOnline()) {
            return;
        }
        try {
            player.setResourcePack(packUrl);
        } catch (Exception ex) {
            plugin.getLogger().warning("Nie można wysłać texturepacka do " + player.getName() + ": " + ex.getMessage());
        }
    }

    private void kickPlayer(Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        String message = kickMessage;
        if (message == null || message.isBlank()) {
            message = "Musisz zaakceptować texturepack aby grać na tym serwerze!";
        }
        String normalized = message.replace("{player}", player.getName());
        player.kick(MiniMessageUtil.parseComponent(normalized));
    }

    public String getPackUrl() {
        return packUrl;
    }

    public boolean isRequired() {
        return required;
    }

    public long getPromptDelayTicks() {
        return promptDelayTicks;
    }

    public String getStormItemyConfigPath() {
        return stormItemyConfigPath;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
