package cc.dreamcode.antylogout.command.handler;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import java.util.Map;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.bukkit.BukkitSender;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.antylogout.config.MessageConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.handler.InvalidPermissionHandler;

public class InvalidPermissionHandlerImpl implements InvalidPermissionHandler
{
    private final MessageConfig messageConfig;
    
    @Override
    public void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final String permission) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        final BukkitSender bukkitSender = (BukkitSender)dreamSender;
        this.messageConfig.noPermission.send(bukkitSender.getHandler(), (Map<String, Object>)new MapBuilder<String, String>().put("permission", permission).build());
    }
    
    @Inject
    @Generated
    public InvalidPermissionHandlerImpl(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }
}
