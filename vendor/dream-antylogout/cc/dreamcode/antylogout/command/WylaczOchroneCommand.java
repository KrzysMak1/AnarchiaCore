package cc.dreamcode.antylogout.command;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.antylogout.profile.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import cc.dreamcode.antylogout.config.MessageConfig;
import cc.dreamcode.antylogout.profile.ProfileService;
import cc.dreamcode.antylogout.profile.ProfileCache;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Command;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandBase;

@Command(name = "wylaczochrone", aliases = { "ochrona" })
public class WylaczOchroneCommand implements CommandBase
{
    private final ProfileCache profileCache;
    private final ProfileService profileService;
    private final MessageConfig messageConfig;
    
    @Permission("dream.antylogout.command.protection-off")
    @Executor
    void disableProtection(final Player player) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        if (profile == null) {
            this.messageConfig.error.send((CommandSender)player);
            return;
        }
        if (!profile.hasProtection()) {
            this.messageConfig.noProtection.send((CommandSender)player);
            return;
        }
        this.profileService.removeProtection(profile, true);
    }
    
    @Permission("dream.antylogout.command.protection-off")
    @Executor(path = { "wylacz" })
    void disableProtection2(final Player player) {
        final Profile profile = this.profileCache.get((HumanEntity)player);
        if (profile == null) {
            this.messageConfig.error.send((CommandSender)player);
            return;
        }
        if (!profile.hasProtection()) {
            this.messageConfig.noProtection.send((CommandSender)player);
            return;
        }
        this.profileService.removeProtection(profile, true);
    }
    
    @Inject
    @Generated
    public WylaczOchroneCommand(final ProfileCache profileCache, final ProfileService profileService, final MessageConfig messageConfig) {
        this.profileCache = profileCache;
        this.profileService = profileService;
        this.messageConfig = messageConfig;
    }
}
