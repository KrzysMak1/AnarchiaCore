package cc.dreamcode.antylogout.api;

import cc.dreamcode.antylogout.profile.Profile;
import java.time.Duration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.antylogout.profile.ProfileCache;
import cc.dreamcode.antylogout.profile.ProfileService;

public class AntylogoutAPI
{
    private final ProfileService profileService;
    private final ProfileCache profileCache;
    private static AntylogoutAPI instance;
    
    private static AntylogoutAPI getInstance() {
        if (AntylogoutAPI.instance == null) {
            throw new IllegalStateException("API not initialized");
        }
        return AntylogoutAPI.instance;
    }
    
    private AntylogoutAPI(final DreamBukkitPlatform platform) {
        this.profileService = (ProfileService)platform.getInject(ProfileService.class).get();
        this.profileCache = (ProfileCache)platform.getInject(ProfileCache.class).get();
    }
    
    public static void init(final DreamBukkitPlatform platform) {
        AntylogoutAPI.instance = new AntylogoutAPI(platform);
    }
    
    public boolean hasAntyLogout(final Player player) {
        return getInstance().profileCache.get((HumanEntity)player).isInCombat();
    }
    
    public boolean hasProtection(final Player player) {
        return getInstance().profileCache.get((HumanEntity)player).hasProtection();
    }
    
    public void setAntyLogout(final Player player, final Duration duration) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        this.profileService.setAntylogout(profile, duration);
    }
    
    public void removeAntyLogout(final Player player, final boolean sendBossbar) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        getInstance().profileService.removeAntylogout(profile, sendBossbar);
    }
    
    public void setProtection(final Player player, final Profile.ProtectionType type, final Duration duration) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        getInstance().profileService.setProtection(profile, duration, type);
    }
    
    public void removeProtection(final Player player, final boolean sendBossbar) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        getInstance().profileService.removeProtection(profile, sendBossbar);
    }
}
