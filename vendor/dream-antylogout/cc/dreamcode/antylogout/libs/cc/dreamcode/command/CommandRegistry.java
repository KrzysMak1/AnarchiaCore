package cc.dreamcode.antylogout.libs.cc.dreamcode.command;

import lombok.NonNull;

public interface CommandRegistry
{
    void register(@NonNull final CommandEntry commandEntry, @NonNull final CommandMeta commandMeta);
    
    void unregister(@NonNull final CommandEntry commandEntry);
}
