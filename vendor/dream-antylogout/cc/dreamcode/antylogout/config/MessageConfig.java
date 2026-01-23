package cc.dreamcode.antylogout.config;

import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Comment;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.CustomKey;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Header;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.annotation.Headers;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.OkaeriConfig;

@Configuration(child = "message.yml")
@Headers({ @Header({ "## Dream-AntyLogout (Message-Config) ##" }), @Header({ "Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)" }) })
public class MessageConfig extends OkaeriConfig
{
    @CustomKey("command-usage")
    public BukkitNotice usage;
    @CustomKey("command-usage-help")
    public BukkitNotice usagePath;
    @CustomKey("command-usage-not-found")
    public BukkitNotice usageNotFound;
    @CustomKey("command-path-not-found")
    public BukkitNotice pathNotFound;
    @CustomKey("command-no-permission")
    public BukkitNotice noPermission;
    @CustomKey("command-not-player")
    public BukkitNotice notPlayer;
    @CustomKey("command-not-console")
    public BukkitNotice notConsole;
    @CustomKey("command-invalid-format")
    public BukkitNotice invalidFormat;
    @CustomKey("player-not-found")
    public BukkitNotice playerNotFound;
    @CustomKey("world-not-found")
    public BukkitNotice worldNotFound;
    @CustomKey("config-reloaded")
    public BukkitNotice reloaded;
    @CustomKey("config-reload-error")
    public BukkitNotice reloadError;
    @CustomKey("error")
    public BukkitNotice error;
    @CustomKey("no-protection")
    public BukkitNotice noProtection;
    @CustomKey("pvp-is-off")
    public BukkitNotice pvpDisabled;
    @CustomKey("player-has-protection")
    public BukkitNotice playerHasProtection;
    @CustomKey("you-have-protection")
    public BukkitNotice cantHurtAnybody;
    @CustomKey("cant-hurt-on-creative")
    public BukkitNotice cantHurtOnCreative;
    @CustomKey("cant-use-commands")
    public BukkitNotice cantUseCommands;
    @CustomKey("protection-ended")
    public BukkitNotice protectionEnded;
    @CustomKey("combat-ended")
    public BukkitNotice combatEnded;
    @CustomKey("combat-start")
    public BukkitNotice combatStart;
    @CustomKey("combat-logout")
    public BukkitNotice combatLogout;
    @CustomKey("teleport-cancelled")
    public BukkitNotice cantTeleportThere;
    @CustomKey("interaction-blocked")
    public BukkitNotice cantInteract;
    @CustomKey("health-left")
    public BukkitNotice healthLeft;
    @CustomKey("protection-added")
    public BukkitNotice protectionAdded;
    @CustomKey("cant-fly-in-combat")
    public BukkitNotice cantFlyInCombat;
    public String enabled;
    public String disabled;
    @Comment({ "{status} zostanie podmienione przez &cwylaczone lub &awlaczone" })
    @CustomKey("pvp-status-changed")
    public BukkitNotice pvpStatusChanged;
    @Comment({ "{status} zostanie podmienione przez &cwylaczone lub &awlaczone" })
    @CustomKey("autorespawn-status-changed")
    public BukkitNotice autorespawnStatusChanged;
    
    public MessageConfig() {
        this.usage = BukkitNotice.chat("&7Przyklady uzycia komendy: &c{label}");
        this.usagePath = BukkitNotice.chat("&f{usage} &8- &7{description}");
        this.usageNotFound = BukkitNotice.chat("&cNie znaleziono pasujacych do kryteriow komendy.");
        this.pathNotFound = BukkitNotice.chat("&cTa komenda jest pusta lub nie posiadasz dostepu do niej.");
        this.noPermission = BukkitNotice.chat("&cNie posiadasz uprawnien.");
        this.notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
        this.notConsole = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu konsoli.");
        this.invalidFormat = BukkitNotice.chat("&cPodano nieprawidlowy format argumentu komendy. ({input})");
        this.playerNotFound = BukkitNotice.chat("&cPodanego gracza nie znaleziono.");
        this.worldNotFound = BukkitNotice.chat("&cPodanego swiata nie znaleziono.");
        this.reloaded = BukkitNotice.chat("&aPrzeladowano! &7({time})");
        this.reloadError = BukkitNotice.chat("&cZnaleziono problem w konfiguracji: &6{error}");
        this.error = BukkitNotice.chat("&cWystapil nieznany b\u0142\u0105d. Spr\u00f3buj ponownie p\u00f3\u017aniej lub relognij");
        this.noProtection = BukkitNotice.chat("&cNie masz aktywnej ochrony");
        this.pvpDisabled = BukkitNotice.chat("&cPvp jest w tej chwili wy\u0142\u0105czone");
        this.playerHasProtection = BukkitNotice.chat("&cGracz posiada aktywna ochrone. Nie mozesz go zranic");
        this.cantHurtAnybody = BukkitNotice.chat("&cMasz aktywna ochrone. Nie mozesz nikogo ranic");
        this.cantHurtOnCreative = BukkitNotice.chat("&cNie mozesz nikogo ranic bedac na creative");
        this.cantUseCommands = BukkitNotice.chat("&cNie mozesz uzywac komend bedac podczas walki");
        this.protectionEnded = BukkitNotice.chat("&cTwoja ochrona sie skonczyla, uwazaj na siebie!");
        this.combatEnded = BukkitNotice.chat("&aKoniec walki! Mo\u017cesz si\u0119 bezpiecznie wylogowa\u0107");
        this.combatStart = BukkitNotice.chat("&cJestes teraz podczas walki z {opponent}, nie wylogowuj si\u0119 z serwera albo zginiesz!");
        this.combatLogout = BukkitNotice.chat("&cGracz {nick} wylogowa\u0142 si\u0119 podczas walki");
        this.cantTeleportThere = BukkitNotice.chat("&cNie mozesz przeniesc sie w to miejsce bedac podczas walki");
        this.cantInteract = BukkitNotice.chat("&cNie mozesz tego uzywac podczas walki");
        this.healthLeft = BukkitNotice.chat("&7Trafiony! Pozostalo mu &c{hp} HP");
        this.protectionAdded = BukkitNotice.chat("&aJestes bezpieczny z powodu ochrony ;)");
        this.cantFlyInCombat = BukkitNotice.chat("&cNie mozesz latac podczas walki");
        this.enabled = "&awlaczono";
        this.disabled = "&cwylaczono";
        this.pvpStatusChanged = BukkitNotice.chat("{status} &ePVP &7na serwerze");
        this.autorespawnStatusChanged = BukkitNotice.chat("{status} &eAutoRespawn &7na serwerze");
    }
}
