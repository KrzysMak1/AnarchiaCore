package cc.dreamcode.antylogout.libs.cc.dreamcode.command.handler;

import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.DreamSender;

public interface InvalidInputHandler
{
    void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final Class<?> requiringClass, @NonNull final String input);
}
