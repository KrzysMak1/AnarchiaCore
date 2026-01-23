package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.art;

import org.bukkit.Art;

public class XArt
{
    private static final boolean USE_INTERFACE;
    
    private XArt() {
    }
    
    public static BukkitArt of(final Art art) {
        if (XArt.USE_INTERFACE) {
            return new NewArt(art);
        }
        return new OldArt(art);
    }
    
    static {
        USE_INTERFACE = Art.class.isInterface();
    }
}
