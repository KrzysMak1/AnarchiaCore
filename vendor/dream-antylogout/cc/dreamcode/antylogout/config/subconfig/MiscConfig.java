package cc.dreamcode.antylogout.config.subconfig;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import cc.dreamcode.antylogout.enums.TargetType;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

public class MiscConfig extends OkaeriConfig
{
    @Comment({ "Blokada bicia na creative (dostepne opcje NONE, PLAYER, MONSTER, BOTH" })
    @CustomKey("cancel-creative-damage")
    public TargetType blockDamageOnCreative;
    @Comments({ @Comment({ "Status pvp na serwerze (mozliwe do zmiany komenda /antylogout pvp on/off" }), @Comment({ "pvp-disabled" }) })
    public boolean pvpDisabled;
    @Comment({ "Automatyczne odradzanie po smierci (mozliwe do zmiany komenda /antylogout autorespawn on/off" })
    @CustomKey("autorespawn")
    public boolean autorespawn;
    
    public MiscConfig() {
        this.blockDamageOnCreative = TargetType.PLAYER;
        this.pvpDisabled = false;
        this.autorespawn = true;
    }
}
