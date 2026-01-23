package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.holder;

import lombok.NonNull;

public interface DreamMenuHolder<P>
{
    void open(@NonNull final P customer);
    
    void dispose();
}
