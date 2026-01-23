package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.art;

import org.bukkit.Art;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.AbstractReferencedClass;

public abstract class BukkitArt extends AbstractReferencedClass<Art>
{
    public abstract int getBlockWidth();
    
    public abstract int getBlockHeight();
    
    public abstract String getKey();
    
    public abstract int getId();
}
