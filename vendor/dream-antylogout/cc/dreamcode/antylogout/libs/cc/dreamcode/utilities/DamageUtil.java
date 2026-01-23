package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities;

import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.entity.Monster;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class DamageUtil
{
    public static Entity getAttacker(final EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        if (!(damager instanceof Projectile)) {
            return damager;
        }
        final Projectile projectile = (Projectile)damager;
        final ProjectileSource source = projectile.getShooter();
        if (!(source instanceof LivingEntity)) {
            return null;
        }
        return (Entity)source;
    }
    
    public static boolean isMonster(final Entity entity) {
        return entity instanceof Monster;
    }
    
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof Player;
    }
    
    @Generated
    private DamageUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
