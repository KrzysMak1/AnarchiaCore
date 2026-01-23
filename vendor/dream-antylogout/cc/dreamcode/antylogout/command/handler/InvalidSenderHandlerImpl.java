package cc.dreamcode.antylogout.command.handler;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.bukkit.BukkitSender;
import java.util.List;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.antylogout.config.MessageConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.handler.InvalidSenderHandler;

public class InvalidSenderHandlerImpl implements InvalidSenderHandler
{
    private final MessageConfig messageConfig;
    
    @Override
    public void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final List<DreamSender.Type> requireType) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (requireType == null) {
            throw new NullPointerException("requireType is marked non-null but is null");
        }
        final BukkitSender bukkitSender = (BukkitSender)dreamSender;
        if (requireType.contains((Object)DreamSender.Type.CONSOLE)) {
            this.messageConfig.notConsole.send(bukkitSender.getHandler());
            return;
        }
        if (requireType.contains((Object)DreamSender.Type.CLIENT)) {
            this.messageConfig.notPlayer.send(bukkitSender.getHandler());
        }
    }
    
    @Inject
    @Generated
    public InvalidSenderHandlerImpl(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }
}
