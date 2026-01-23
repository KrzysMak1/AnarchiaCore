package cc.dreamcode.antylogout.libs.cc.dreamcode.command;

import lombok.NonNull;

public interface CommandExtension
{
    void register(@NonNull final CommandProvider commandProvider);
}
