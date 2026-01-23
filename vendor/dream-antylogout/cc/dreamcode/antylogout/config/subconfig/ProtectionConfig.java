package cc.dreamcode.antylogout.config.subconfig;

import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.antylogout.notice.bossbar.BossBarData;
import cc.dreamcode.antylogout.notice.Display;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import java.time.Duration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class ProtectionConfig extends OkaeriConfig
{
    @Comment({ "Ile ma trwac ochrona startowa gracza" })
    @CustomKey("duration")
    public Duration duration;
    @Comment({ "Czy mo\u017cna bi\u0107 graczy maj\u0105c ochrone (natychmiastowo zabiera ochrone)" })
    @CustomKey("damage-on-protection")
    public boolean damageOnProtection;
    @Comment({ "jak ma wygladac suffix jesli gracz ma ochrone" })
    @CustomKey("suffix")
    public String suffix;
    @Comment({ "gdzie pokazywac ma sie odliczanie (dostepne opcje: BOSSBAR, ACTIONBAR)" })
    @CustomKey("display")
    public Display display;
    @Comments({ @Comment({ "jesli powyzsza wartosc ustawiona jest na ACTIONBAR, uzywane bedzie tylko pole 'title'" }), @Comment({ "jak ma wygladac bossbar dla ochrony" }) })
    @CustomKey("notice")
    public BossBarData notice;
    @Comments({ @Comment, @Comment({ "Ile ma trwa\u0107 ochrona dla graczy po \u015bmierci (0s = wylaczona)" }) })
    @CustomKey("death-duration")
    public Duration deathDuration;
    @Comment({ "jak ma wygladac bossbar dla ochrony po smierci" })
    @CustomKey("death-notice")
    public BossBarData deathNotice;
    @Comment
    @CustomKey("send-end")
    public boolean sendEndNotice;
    @CustomKey("end-notice")
    public BossBarData endNotice;
    
    public ProtectionConfig() {
        this.duration = Duration.ofSeconds(60L);
        this.damageOnProtection = true;
        this.suffix = " &b[OCHRONA]";
        this.display = Display.BOSSBAR;
        this.notice = new BossBarData("&aAktywna ochrona: &2{time}", BarColor.GREEN, BarStyle.SOLID);
        this.deathDuration = Duration.ofSeconds(30L);
        this.deathNotice = new BossBarData("&eAktywna ochrona po smierci: &6({time})", BarColor.YELLOW, BarStyle.SOLID);
        this.sendEndNotice = true;
        this.endNotice = new BossBarData("&cOchrona sie skonczyla. Uwazaj na siebie!", BarColor.RED, BarStyle.SOLID);
    }
}
