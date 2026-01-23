package cc.dreamcode.antylogout.libs.cc.dreamcode.command.handler;

import java.util.List;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.DreamSender;

public interface InvalidSenderHandler
{
    void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final List<DreamSender.Type> requireType);
}
