package cc.dreamcode.antylogout.config.subconfig;

import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.antylogout.notice.bossbar.BossBarData;
import cc.dreamcode.antylogout.notice.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import java.util.List;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import java.time.Duration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class AntylogoutConfig extends OkaeriConfig
{
    @Comment({ "czas trwania walki" })
    @CustomKey("duration")
    public Duration duration;
    @Comment({ "czy antylogout ma dzialac od mobow?" })
    @CustomKey("monster-antylogout")
    public boolean mobAntylogout;
    @Comment({ "antylogout po otrzymaniu obrazen z per\u0142y" })
    @CustomKey("ender-pearl-antylogout")
    public boolean enderPearlAntylogout;
    @Comment({ "Czy plugin ma blokowac elytre jesli gracz jest podczas walki" })
    @CustomKey("block-gliding")
    public boolean blockElytra;
    @Comment({ "Czy void ma nadawac antylogout" })
    public boolean voidAntylogout;
    @Comments({ @Comment, @Comment({ "Regiony, na ktorych antylogout bedzie wylaczony (regiony z worldguard wpisuj nastepujaco: wg:<nazwa_swiata>:<nazwa_regionu>" }), @Comment({ "Wlasne regiony wpisuj rg:<nazwa_swiata>:minX:minY:minZ:maxX:maxY:maxZ" }) })
    @CustomKey("disabled-regions")
    public List<String> disabledRegions;
    @Comments({ @Comment, @Comment({ "z jakimi blokami zablokowac interakcje" }) })
    @CustomKey("blocked-blocks")
    public List<Material> blockedBlocks;
    @Comments({ @Comment, @Comment({ "z jakimi bytami zablokowac interakcje" }), @Comment({ "blocked-entity" }) })
    public List<EntityType> blockedEntity;
    @Comments({ @Comment, @Comment({ "Czy komendy maja byc zablokowane w trakcie walki" }) })
    @CustomKey("disable-commands")
    public boolean disableCommands;
    @Comment({ "Komendy dozwolone podczas antylogoutu" })
    @CustomKey("allowed-commands")
    public List<String> allowedCommands;
    @Comments({ @Comment, @Comment({ "gdzie pokazywac ma sie odliczanie (dostepne opcje: BOSSBAR, ACTIONBAR)" }) })
    @CustomKey("display")
    public Display display;
    @Comment({ "jesli powyzsza wartosc ustawiona jest na false, uzywane bedzie tylko pole 'title'" })
    @CustomKey("notice")
    public BossBarData notice;
    @Comment
    @CustomKey("send-end")
    public boolean sendEndNotice;
    @CustomKey("end-notice")
    public BossBarData endNotice;
    
    public AntylogoutConfig() {
        this.duration = Duration.ofSeconds(30L);
        this.mobAntylogout = true;
        this.enderPearlAntylogout = true;
        this.blockElytra = true;
        this.voidAntylogout = true;
        this.disabledRegions = ListBuilder.of("wg:world:spawn", "wg:world:lobby", "rg:world:0:0:0:50:100:50");
        this.blockedBlocks = ListBuilder.of(Material.CRAFTING_TABLE, Material.ENDER_CHEST);
        this.blockedEntity = ListBuilder.of(EntityType.BOAT, EntityType.MINECART);
        this.disableCommands = true;
        this.allowedCommands = ListBuilder.of("msg", "r");
        this.display = Display.BOSSBAR;
        this.notice = new BossBarData("&cJestes podczas walki! Pozostaly czas: &4{time}", BarColor.RED, BarStyle.SOLID);
        this.sendEndNotice = true;
        this.endNotice = new BossBarData("&aWalka zakonczona! Mozesz sie bezpiecznie wylogowac!", BarColor.GREEN, BarStyle.SOLID);
    }
}
