package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.setup;

import lombok.NonNull;

public interface MenuPlayerSetup<M, H>
{
    M build(@NonNull final H h);
}
