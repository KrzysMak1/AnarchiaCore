package cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component;

import lombok.NonNull;

public interface ComponentExtension
{
    void register(@NonNull final ComponentService componentService);
}
