package cc.dreamcode.antylogout.command;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Completion;
import java.util.Map;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Async;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.exception.OkaeriException;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.TimeUtil;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.BossBarUtil;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.antylogout.profile.ProfileCache;
import cc.dreamcode.antylogout.cuboid.extension.CuboidCache;
import cc.dreamcode.antylogout.hook.worldguard.extension.RegionService;
import cc.dreamcode.antylogout.config.MessageConfig;
import cc.dreamcode.antylogout.config.PluginConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.annotation.Command;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandBase;

@Command(name = "antylogout")
public class AntylogoutCommand implements CommandBase
{
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;
    private final RegionService regionService;
    private final CuboidCache cuboidCache;
    private final ProfileCache profileCache;
    
    @Async
    @Permission("dream.antylogout.command.reload")
    @Executor(path = { "reload" }, description = "Przeladowuje konfiguracje.")
    BukkitNotice reload() {
        final long time = System.currentTimeMillis();
        try {
            this.messageConfig.load();
            this.pluginConfig.load();
            this.regionService.loadRegions();
            this.cuboidCache.loadCuboids();
            this.profileCache.values().forEach(BossBarUtil::removeBossBar);
            return this.messageConfig.reloaded.with("time", TimeUtil.format(System.currentTimeMillis() - time));
        }
        catch (final NullPointerException | OkaeriException e) {
            return this.messageConfig.reloadError.with("error", e.getMessage());
        }
    }
    
    @Async
    @Permission("dream.antylogout.command.pvp")
    @Executor(path = { "pvp" }, description = "Wlacza/Wylacza pvp na serwerzer")
    @Completion(arg = "status", value = { "on", "off" })
    BukkitNotice togglePvp(@Arg final String status) {
        if (!status.equalsIgnoreCase("on") && !status.equalsIgnoreCase("off")) {
            return this.messageConfig.invalidFormat;
        }
        if (status.equalsIgnoreCase("on")) {
            this.pluginConfig.misc.pvpDisabled = false;
        }
        else if (status.equalsIgnoreCase("off")) {
            this.pluginConfig.misc.pvpDisabled = true;
        }
        this.pluginConfig.save();
        return this.messageConfig.pvpStatusChanged.with((Map<String, Object>)MapBuilder.of("status", this.pluginConfig.misc.pvpDisabled ? this.messageConfig.disabled : this.messageConfig.enabled));
    }
    
    @Async
    @Permission("dream.antylogout.command.autorespawn")
    @Executor(path = { "autorespawn" }, description = "Wlacza/Wylacza autorespawn na serwerzer")
    @Completion(arg = "status", value = { "on", "off" })
    BukkitNotice toggleAutoRespawn(@Arg final String status) {
        if (!status.equalsIgnoreCase("on") && !status.equalsIgnoreCase("off")) {
            return this.messageConfig.invalidFormat;
        }
        if (status.equalsIgnoreCase("on")) {
            this.pluginConfig.misc.autorespawn = true;
        }
        else if (status.equalsIgnoreCase("off")) {
            this.pluginConfig.misc.autorespawn = false;
        }
        this.pluginConfig.save();
        return this.messageConfig.autorespawnStatusChanged.with((Map<String, Object>)MapBuilder.of("status", this.pluginConfig.misc.autorespawn ? this.messageConfig.enabled : this.messageConfig.disabled));
    }
    
    @Inject
    @Generated
    public AntylogoutCommand(final PluginConfig pluginConfig, final MessageConfig messageConfig, final RegionService regionService, final CuboidCache cuboidCache, final ProfileCache profileCache) {
        this.pluginConfig = pluginConfig;
        this.messageConfig = messageConfig;
        this.regionService = regionService;
        this.cuboidCache = cuboidCache;
        this.profileCache = profileCache;
    }
}
