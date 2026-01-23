package cc.dreamcode.antylogout.config;

import cc.dreamcode.antylogout.config.subconfig.MiscConfig;
import cc.dreamcode.antylogout.config.subconfig.BarrierConfig;
import cc.dreamcode.antylogout.config.subconfig.ProtectionConfig;
import cc.dreamcode.antylogout.config.subconfig.AntylogoutConfig;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comments;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Header;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Headers;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

@Configuration(child = "config.yml")
@Headers({ @Header({ "## Dream-AntyLogout (Main-Config) ##" }), @Header({ "# dream.antylogout.bypass - uprawnienie do bypassu antylogoutu" }), @Header({ "# dream.antylogout.command.pvp - uprawnienie do przelaczania pvp" }), @Header({ "# dream.antylogout.command.autorespawn - uprawnienie do przelaczania autorespawnu" }), @Header({ "# dream.antylogout.command.reload - uprawnienie do przeladowania konfiguracji" }), @Header({ "# dream.antylogout.command.protection-off - uprawnienie do wylaczania ochrony" }) })
public class PluginConfig extends OkaeriConfig
{
    @Comments({ @Comment, @Comment({ "Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P" }) })
    @CustomKey("debug")
    public boolean debug;
    @Comments({ @Comment, @Comment({ "Ponizej znajduja sie dane do logowania bazy danych:" }) })
    @CustomKey("storage-config")
    public StorageConfig storageConfig;
    @CustomKey("antylogout")
    public AntylogoutConfig antylogout;
    @CustomKey("protection")
    public ProtectionConfig protection;
    @CustomKey("barrier")
    public BarrierConfig barrier;
    @CustomKey("misc")
    public MiscConfig misc;
    
    public PluginConfig() {
        this.debug = true;
        this.storageConfig = new StorageConfig("dreamantylogout");
        this.antylogout = new AntylogoutConfig();
        this.protection = new ProtectionConfig();
        this.barrier = new BarrierConfig();
        this.misc = new MiscConfig();
    }
}
