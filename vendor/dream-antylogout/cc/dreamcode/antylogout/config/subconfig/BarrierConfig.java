package cc.dreamcode.antylogout.config.subconfig;

import org.bukkit.Material;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.antylogout.barrier.BarrierType;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class BarrierConfig extends OkaeriConfig
{
    @Comments({ @Comment({ "mozliwosci blokady wejscia gracza na zablokowany teren (z listy ponizej)" }), @Comment({ "dostepne opcje: KNOCKBACK, BLOCK, WALL (wkrotce), IGNORE" }) })
    @CustomKey("barrier-type")
    public BarrierType type;
    @Comment({ "(wkrotce)" })
    @CustomKey("wall-block")
    public Material wallMaterial;
    @Comment({ "si\u0142a odrzutu gracza (jesli powyzsza opcja to PUSH)" })
    @CustomKey("knockback-strength")
    public double knockbackStrength;
    @Comment({ "jak bardzo podrzuca\u0107 ma gracza do g\u00f3ry (jesli barrierType: PUSH" })
    @CustomKey("knockback-y")
    public double knockbackY;
    
    public BarrierConfig() {
        this.type = BarrierType.KNOCKBACK;
        this.wallMaterial = Material.RED_STAINED_GLASS;
        this.knockbackStrength = 1.2;
        this.knockbackY = 0.2;
    }
}
