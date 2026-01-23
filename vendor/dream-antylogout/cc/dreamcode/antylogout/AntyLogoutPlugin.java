package cc.dreamcode.antylogout;

import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.component.DocumentRepositoryResolver;
import cc.dreamcode.antylogout.libs.eu.okaeri.persistence.document.DocumentPersistence;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.component.DocumentPersistenceResolver;
import cc.dreamcode.antylogout.hook.luckperms.LuckPermsHook;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.PluginHook;
import cc.dreamcode.antylogout.hook.worldguard.WorldGuardHook;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.Generated;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import cc.dreamcode.antylogout.notice.bossbar.BossBarDataSerializer;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.serializer.MenuBuilderSerializer;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.antylogout.api.AntylogoutAPI;
import cc.dreamcode.antylogout.scheduler.AntylogoutScheduler;
import cc.dreamcode.antylogout.listener.ProjectileListener;
import cc.dreamcode.antylogout.listener.InteractionListener;
import cc.dreamcode.antylogout.listener.TeleportListener;
import cc.dreamcode.antylogout.listener.LogoutListener;
import cc.dreamcode.antylogout.listener.CommandListener;
import cc.dreamcode.antylogout.listener.ProtectionDamageListener;
import cc.dreamcode.antylogout.listener.DeathListener;
import cc.dreamcode.antylogout.listener.CreativeDamageListener;
import cc.dreamcode.antylogout.listener.GlideListener;
import cc.dreamcode.antylogout.listener.DamageListener;
import cc.dreamcode.antylogout.command.WylaczOchroneCommand;
import cc.dreamcode.antylogout.command.AntylogoutCommand;
import cc.dreamcode.antylogout.barrier.extension.BarrierExtension;
import cc.dreamcode.antylogout.cuboid.extension.CuboidExtension;
import cc.dreamcode.antylogout.profile.ProfileFactory;
import cc.dreamcode.antylogout.profile.ProfileService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.PluginHookManager;
import cc.dreamcode.antylogout.profile.ProfileCache;
import cc.dreamcode.antylogout.profile.ProfileRepository;
import cc.dreamcode.antylogout.notice.NoticeService;
import java.util.function.Consumer;
import cc.dreamcode.antylogout.config.PluginConfig;
import cc.dreamcode.antylogout.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.antylogout.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.antylogout.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.antylogout.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.antylogout.command.result.BukkitNoticeResolver;
import cc.dreamcode.antylogout.config.MessageConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.antylogout.libs.eu.okaeri.tasker.bukkit.BukkitTasker;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.color.ColorProcessor;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.bukkit.StringColorUtil;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.adventure.AdventureProcessor;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.adventure.AdventureUtil;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.DreamBukkitPlatform;

public final class AntyLogoutPlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence
{
    private static AntyLogoutPlugin instance;
    
    @Override
    public void load(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        AntyLogoutPlugin.instance = this;
        AdventureUtil.setRgbSupport(true);
        StringColorUtil.setColorProcessor(new AdventureProcessor());
    }
    
    public void enable(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        componentService.setDebug(false);
        this.registerInjectable(BukkitTasker.newPool((Plugin)this));
        this.registerInjectable(BukkitMenuProvider.create((Plugin)this));
        this.registerInjectable(BukkitCommandProvider.create((Plugin)this));
        componentService.registerExtension(DreamCommandExtension.class);
        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(MessageConfig.class);
        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);
        componentService.registerComponent(PluginConfig.class, (java.util.function.Consumer<PluginConfig>)(pluginConfig -> {
            this.registerInjectable(pluginConfig.storageConfig);
            componentService.registerResolver(DocumentPersistenceResolver.class);
            componentService.registerComponent(DocumentPersistence.class);
            componentService.registerResolver(DocumentRepositoryResolver.class);
            componentService.setDebug(pluginConfig.debug);
        }));
        componentService.registerComponent(NoticeService.class);
        componentService.registerComponent(ProfileRepository.class);
        componentService.registerComponent(ProfileCache.class);
        componentService.registerComponent(PluginHookManager.class, (java.util.function.Consumer<PluginHookManager>)(hookManager -> {
            hookManager.registerHook(WorldGuardHook.class);
            hookManager.registerHook(LuckPermsHook.class);
        }));
        componentService.registerComponent(ProfileService.class);
        componentService.registerComponent(ProfileFactory.class, (java.util.function.Consumer<ProfileFactory>)ProfileFactory::loadOnline);
        componentService.registerExtension(CuboidExtension.class);
        if (this.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            componentService.registerExtension(BarrierExtension.class);
        }
        else {
            this.getDreamLogger().warning("Nie wykryto pluginu ProtocolLib, niekt\u00f3re funkcje mog\u0105 nie dzia\u0142a\u0107");
        }
        componentService.registerComponent(AntylogoutCommand.class);
        componentService.registerComponent(WylaczOchroneCommand.class);
        componentService.registerComponent(DamageListener.class);
        componentService.registerComponent(GlideListener.class);
        componentService.registerComponent(CreativeDamageListener.class);
        componentService.registerComponent(DeathListener.class);
        componentService.registerComponent(ProtectionDamageListener.class);
        componentService.registerComponent(CommandListener.class);
        componentService.registerComponent(LogoutListener.class);
        componentService.registerComponent(TeleportListener.class);
        componentService.registerComponent(InteractionListener.class);
        componentService.registerComponent(ProjectileListener.class);
        componentService.registerComponent(AntylogoutScheduler.class);
        AntylogoutAPI.init(this);
    }
    
    public void disable() {
        this.getInject(ProfileService.class).ifPresent(ProfileService::safeDisable);
        this.getInject(ProfileFactory.class).ifPresent(ProfileFactory::saveAll);
    }
    
    @NonNull
    public DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-AntyLogout", "1.0-beta.3", "ForeX03");
    }
    
    @NonNull
    @Override
    public OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerializer());
            registry.register(new MenuBuilderSerializer());
            registry.register(new BossBarDataSerializer());
        };
    }
    
    @NonNull
    @Override
    public OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());
            registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
        };
    }
    
    @Generated
    public static AntyLogoutPlugin getInstance() {
        return AntyLogoutPlugin.instance;
    }
}
