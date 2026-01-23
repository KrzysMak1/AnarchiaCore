package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import org.bukkit.boss.BarFlag;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.antylogout.AntyLogoutPlugin;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import java.util.Map;
import java.util.Collections;
import java.time.Duration;
import cc.dreamcode.antylogout.notice.bossbar.BossBarData;
import cc.dreamcode.antylogout.profile.Profile;

public final class BossBarUtil
{
    public static void sendTemporaryBossBar(final Profile profile, final BossBarData bossBarData, final Duration duration) {
        sendTemporaryBossBar(profile, bossBarData, duration, (Map<String, Object>)Collections.emptyMap());
    }
    
    public static void sendTemporaryBossBar(final Profile profile, final BossBarData bossBarData, final Duration duration, final Map<String, Object> replaceMap) {
        final NamespacedKey key = NamespacedKey.minecraft("dream-antylogout-temp-" + profile.getUniqueId().toString());
        processBossBar(profile, bossBarData, 0.0, replaceMap, key);
        Bukkit.getScheduler().runTaskLater((Plugin)AntyLogoutPlugin.getInstance(), () -> removeTempBossBar(profile), duration.toMillis() / 50L);
    }
    
    public static void processBossBar(final Profile profile, final BossBarData bossBarData, final double progress, final Map<String, Object> replaceMap) {
        final NamespacedKey key = NamespacedKey.minecraft("dream-antylogout-" + profile.getUniqueId().toString());
        processBossBar(profile, bossBarData, progress, replaceMap, key);
    }
    
    public static void processBossBar(final Profile profile, final BossBarData bossBarData, final double progress, final Map<String, Object> replaceMap, final NamespacedKey key) {
        final Player player = Bukkit.getPlayer(profile.getUniqueId());
        KeyedBossBar bossBar = Bukkit.getBossBar(key);
        removeTempBossBar(profile);
        if (player == null) {
            return;
        }
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(key, "", bossBarData.getBarColor(), bossBarData.getBarStyle(), new BarFlag[0]);
            bossBar.addPlayer(player);
        }
        bossBar.setProgress(progress);
        bossBar.setTitle(StringColorUtil.fixColor(bossBarData.getTitle(), replaceMap));
    }
    
    public static void removeTempBossBar(final Profile profile) {
        final NamespacedKey key = NamespacedKey.minecraft("dream-antylogout-temp-" + profile.getUniqueId().toString());
        final KeyedBossBar bossBar = Bukkit.getBossBar(key);
        if (bossBar != null) {
            bossBar.removeAll();
            Bukkit.removeBossBar(key);
        }
    }
    
    public static void removeBossBar(final Profile profile) {
        final NamespacedKey key = NamespacedKey.minecraft("dream-antylogout-" + profile.getUniqueId().toString());
        final KeyedBossBar bossBar = Bukkit.getBossBar(key);
        if (bossBar != null) {
            bossBar.removeAll();
            Bukkit.removeBossBar(key);
        }
    }
    
    @Generated
    private BossBarUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
