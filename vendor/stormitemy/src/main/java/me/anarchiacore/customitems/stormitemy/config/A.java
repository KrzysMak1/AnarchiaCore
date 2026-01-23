/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Exception
 *  java.lang.NoSuchMethodError
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.logging.Logger
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.config;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class A {
    private final Plugin B;
    private final Logger A;

    public A(Plugin javaPlugin) {
        this.B = javaPlugin;
        this.A = javaPlugin.getLogger();
    }

    public boolean G() {
        try {
            File file = new File(this.B.getDataFolder(), "config.yml");
            if (!file.exists()) {
                this.B.saveDefaultConfig();
                return true;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            InputStream inputStream = this.B.getResource("config.yml");
            if (inputStream == null) {
                this.A.warning("Nie mo\u017cna znale\u017a\u0107 domy\u015blnego pliku config.yml");
                return false;
            }
            YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(inputStream));
            boolean bl = false;
            for (String string : yamlConfiguration2.getKeys(false)) {
                if (yamlConfiguration.contains(string)) continue;
                yamlConfiguration.set(string, yamlConfiguration2.get(string));
                bl = true;
            }
            if (bl) {
                yamlConfiguration.save(file);
                this.A.info("Konfiguracja zosta\u0142a zaktualizowana");
            }
            return true;
        }
        catch (Exception exception) {
            this.A.severe("B\u0142\u0105d podczas aktualizacji konfiguracji: " + exception.getMessage());
            exception.printStackTrace();
            return false;
        }
    }

    public boolean A(File file) {
        try {
            InputStream inputStream;
            if (!file.exists()) {
                return false;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            File file2 = new File(this.B.getDataFolder(), "config.yml");
            if (!file2.exists()) {
                this.B.saveDefaultConfig();
            }
            if ((inputStream = this.B.getResource("config.yml")) == null) {
                return false;
            }
            YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(inputStream));
            YamlConfiguration yamlConfiguration3 = new YamlConfiguration();
            try {
                yamlConfiguration3.options().parseComments(true);
            }
            catch (NoSuchMethodError noSuchMethodError) {
                // empty catch block
            }
            for (String string : yamlConfiguration2.getKeys(true)) {
                if (yamlConfiguration2.isConfigurationSection(string)) continue;
                yamlConfiguration3.set(string, yamlConfiguration2.get(string));
            }
            int n2 = 0;
            for (String string : yamlConfiguration.getKeys(true)) {
                if (yamlConfiguration.isConfigurationSection(string) || !yamlConfiguration2.contains(string)) continue;
                yamlConfiguration3.set(string, yamlConfiguration.get(string));
                ++n2;
            }
            yamlConfiguration3.save(file2);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public boolean A(String string, ConfigurationSection configurationSection) {
        try {
            File file = new File(this.B.getDataFolder(), "items");
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(file, string.toLowerCase() + ".yml");
            YamlConfiguration yamlConfiguration = null;
            if (file2.exists()) {
                yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
            }
            YamlConfiguration yamlConfiguration2 = new YamlConfiguration();
            try {
                yamlConfiguration2.options().parseComments(true);
            }
            catch (NoSuchMethodError noSuchMethodError) {
                // empty catch block
            }
            String string2 = configurationSection.getName();
            if (string2 == null || string2.isEmpty()) {
                string2 = configurationSection.getRoot() != null && !configurationSection.getRoot().getKeys(false).isEmpty() ? (String)configurationSection.getRoot().getKeys(false).iterator().next() : string.toLowerCase();
            }
            ConfigurationSection configurationSection2 = this.A(yamlConfiguration != null ? yamlConfiguration.getConfigurationSection(string2) : null, configurationSection);
            for (String string3 : configurationSection2.getKeys(true)) {
                if (configurationSection2.isConfigurationSection(string3)) continue;
                yamlConfiguration2.set(string2 + "." + string3, configurationSection2.get(string3));
            }
            yamlConfiguration2.save(file2);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private ConfigurationSection A(ConfigurationSection configurationSection, ConfigurationSection configurationSection2) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        for (String string : configurationSection2.getKeys(true)) {
            if (configurationSection2.isConfigurationSection(string)) continue;
            yamlConfiguration.set(string, configurationSection2.get(string));
        }
        if (configurationSection != null) {
            for (String string : configurationSection.getKeys(true)) {
                if (configurationSection.isConfigurationSection(string)) continue;
                yamlConfiguration.set(string, configurationSection.get(string));
            }
        }
        return yamlConfiguration;
    }

    public void A() {
        try {
            this.F();
            this.D();
            this.H();
            this.I();
            Map<String, ConfigurationSection> map = this.B();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private Map<String, ConfigurationSection> B() {
        HashMap hashMap = new HashMap();
        try {
            String[] stringArray;
            for (String string : stringArray = new String[]{"smoczymiecz", "bombardamaxima", "boskitopor", "sniezka", "splesnialakanapka", "turbotrap", "turbodomek", "wedkasurferka", "wedkanielota", "zaczarowanie", "lopatagrincha", "piernik", "cieplemleko", "rozakupidyna", "parawan", "lizak", "rozga", "kupaanarchi", "koronaanarchi", "totemuaskawienia", "siekieragrincha", "lukkupidyna", "marchewkowymiecz", "kosa", "excalibur", "krewwampira", "marchewkowakusza", "lewejajko", "zajeczymiecz", "zatrutyolowek", "sakiewkadropu", "wampirzejablko", "arcusmagnus", "piekielnatarcza", "kostkarubika", "zlamaneserce", "dynamit", "rozgotowanakukurydza", "zmutowanycreeper", "wzmocnionaeltra", "anarchicznymiecz", "anarchicznykilof", "antycobweb", "anarchicznyhelm", "anarchicznaklata", "anarchicznespodnie", "anarchicznebuty", "anarchicznyhelm2", "anarchicznaklata2", "anarchicznespodnie2", "anarchicznebuty2", "balonikzhelem"}) {
                String string2 = "items/" + string + ".yml";
                InputStream inputStream = this.B.getResource(string2);
                if (inputStream == null) continue;
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(inputStream));
                ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection(string);
                if (configurationSection != null) {
                    hashMap.put((Object)string, (Object)configurationSection);
                    continue;
                }
                hashMap.put((Object)string, (Object)yamlConfiguration);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return hashMap;
    }

    public void F() {
        try {
            File file;
            File file2 = new File(this.B.getDataFolder(), "items");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!(file = new File(file2, "balonikzhelem.yml")).exists()) {
                return;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            if (!yamlConfiguration.contains("balonikzhelem")) {
                return;
            }
            boolean bl = false;
            if (!yamlConfiguration.contains("balonikzhelem.balloon.blocksBeforePop")) {
                yamlConfiguration.set("balonikzhelem.balloon.blocksBeforePop", (Object)30);
                bl = true;
            }
            if (!yamlConfiguration.contains("balonikzhelem.balloon.speedBlocksPerSecond")) {
                yamlConfiguration.set("balonikzhelem.balloon.speedBlocksPerSecond", (Object)3);
                bl = true;
            }
            if (!yamlConfiguration.contains("balonikzhelem.balloon.updateTickRate")) {
                yamlConfiguration.set("balonikzhelem.balloon.updateTickRate", (Object)1);
                bl = true;
            }
            if (!yamlConfiguration.contains("balonikzhelem.customModelData")) {
                yamlConfiguration.set("balonikzhelem.customModelData", (Object)14001);
                bl = true;
            }
            if (!yamlConfiguration.contains("balonikzhelem.active_balloon_value")) {
                yamlConfiguration.set("balonikzhelem.active_balloon_value", (Object)"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTFiZTQ0ZTg0ZjAxMmY0M2ZhODExNzI3ZDJkNzQ2YTEwYjc1ZGQ5MjQzNzZkZDgwZmJjYjE3NzY4M2QzNTNjZSJ9fX0=");
                bl = true;
            }
            if (bl) {
                yamlConfiguration.save(file);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void D() {
        try {
            File file;
            File file2 = new File(this.B.getDataFolder(), "items");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!(file = new File(file2, "blokwidmo.yml")).exists()) {
                return;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("blokwidmo");
            if (configurationSection == null) {
                configurationSection = yamlConfiguration.createSection("blokwidmo");
            }
            if (!configurationSection.contains("messages.exit_area.title")) {
                configurationSection.set("messages.exit_area.title", (Object)"&c&lBLOK WIDMO");
            }
            if (!configurationSection.contains("messages.exit_area.subtitle")) {
                configurationSection.set("messages.exit_area.subtitle", (Object)"&7Wyszed\u0142e\u015b poza zasi\u0119g &fbloku widmo&7!");
            }
            if (!configurationSection.contains("messages.exit_area.chatMessage")) {
                configurationSection.set("messages.exit_area.chatMessage", (Object)"");
            }
            if (!configurationSection.contains("messages.enter_area.title")) {
                configurationSection.set("messages.enter_area.title", (Object)"&c&lBLOK WIDMO");
            }
            if (!configurationSection.contains("messages.enter_area.subtitle")) {
                configurationSection.set("messages.enter_area.subtitle", (Object)"&7Wr\u00f3ci\u0142e\u015b w zasi\u0119g &fbloku widmo&7!");
            }
            if (!configurationSection.contains("messages.enter_area.chatMessage")) {
                configurationSection.set("messages.enter_area.chatMessage", (Object)"");
            }
            if (!configurationSection.contains("customModelData")) {
                configurationSection.set("customModelData", (Object)0);
            }
            yamlConfiguration.save(file);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void H() {
        try {
            this.A("POL.yml", this.E());
            this.A("ENG.yml", this.C());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void A(String string, Map<String, String> map) {
        try {
            File file;
            File file2 = new File(this.B.getDataFolder(), "lang");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!(file = new File(file2, string)).exists()) {
                this.B.saveResource("lang/" + string, false);
                return;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            boolean bl = false;
            for (Map.Entry entry : map.entrySet()) {
                String string2 = "messages." + (String)entry.getKey();
                if (yamlConfiguration.contains(string2)) continue;
                yamlConfiguration.set(string2, entry.getValue());
                bl = true;
            }
            if (bl) {
                yamlConfiguration.save(file);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private Map<String, String> E() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"customhit_usage_set", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7U\u017cycie: &f/stormitemy &x&D&0&6&0&F&Fcustomhit&f set <&x&D&0&6&0&F&Fmno\u017cnik&f>");
        hashMap.put((Object)"customhit_invalid_range", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cMno\u017cnik musi by\u0107 w zakresie od &x&D&0&6&0&F&F0.1&c do &x&D&0&6&0&F&F10.0&c!");
        hashMap.put((Object)"customhit_set_success", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &aMno\u017cnik &x&D&0&6&0&F&Fdamage&a ustawiony na: &f{MULTIPLIER}");
        hashMap.put((Object)"customhit_invalid_number", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cPodana warto\u015b\u0107 nie jest prawid\u0142ow\u0105 &x&D&0&6&0&F&Fliczb\u0105&c!");
        hashMap.put((Object)"customhit_info", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Aktualny mno\u017cnik &x&D&0&6&0&F&Fdamage&7: &f{MULTIPLIER}");
        hashMap.put((Object)"customhit_help_header", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Komendy &x&D&0&6&0&F&FCustom Hit&7:");
        hashMap.put((Object)"customhit_help_set", (Object)"&f/stormitemy &x&D&0&6&0&F&Fcustomhit&f set <&x&D&0&6&0&F&Fmno\u017cnik&f> &8- &7Ustaw mno\u017cnik &x&D&0&6&0&F&Fdamage&7 (0.1-10.0)");
        hashMap.put((Object)"customhit_help_info", (Object)"&f/stormitemy &x&D&0&6&0&F&Fcustomhit&f info &8- &7Wy\u015bwietl aktualny &x&D&0&6&0&F&Fmno\u017cnik&7");
        return hashMap;
    }

    private Map<String, String> C() {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)"customhit_usage_set", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Usage: &f/stormitemy &x&D&0&6&0&F&Fcustomhit&f set <&x&D&0&6&0&F&Fmultiplier&f>");
        hashMap.put((Object)"customhit_invalid_range", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cMultiplier must be between &x&D&0&6&0&F&F0.1&c and &x&D&0&6&0&F&F10.0&c!");
        hashMap.put((Object)"customhit_set_success", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &aDamage &x&D&0&6&0&F&Fmultiplier&a set to: &f{MULTIPLIER}");
        hashMap.put((Object)"customhit_invalid_number", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &cProvided value is not a valid &x&D&0&6&0&F&Fnumber&c!");
        hashMap.put((Object)"customhit_info", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7Current damage &x&D&0&6&0&F&Fmultiplier&7: &f{MULTIPLIER}");
        hashMap.put((Object)"customhit_help_header", (Object)"&8[&x&B&3&0&0&F&F\ud83e\ude93&8] &7&x&D&0&6&0&F&FCustom Hit&7 Commands:");
        hashMap.put((Object)"customhit_help_set", (Object)"&f/stormitemy &x&D&0&6&0&F&Fcustomhit&f set <&x&D&0&6&0&F&Fmultiplier&f> &8- &7Set damage &x&D&0&6&0&F&Fmultiplier&7 (0.1-10.0)");
        hashMap.put((Object)"customhit_help_info", (Object)"&f/stormitemy &x&D&0&6&0&F&Fcustomhit&f info &8- &7Display current &x&D&0&6&0&F&Fmultiplier&7");
        return hashMap;
    }

    public void I() {
        try {
            File file = new File(this.B.getDataFolder(), "config.yml");
            if (!file.exists()) {
                return;
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            boolean bl = false;
            if (!yamlConfiguration.contains("texturepack")) {
                yamlConfiguration.set("texturepack.enabled", (Object)true);
                yamlConfiguration.set("texturepack.url", (Object)"https://example.com/texturepack.zip");
                yamlConfiguration.set("texturepack.required", (Object)false);
                yamlConfiguration.set("texturepack.kick_message", (Object)"&#FF0000Musisz zaakceptowa\u0107 texturepack aby gra\u0107 na tym serwerze!");
                yamlConfiguration.set("texturepack.prompt_delay", (Object)60);
                yamlConfiguration.set("texturepack.permission", (Object)"stormitemy.texturepack");
                yamlConfiguration.set("texturepack.gui.title", (Object)"&#DD00FF\u1d1b\u1d07x\u1d1b\u1d1c\u0280\u1d07\u1d18\u1d00\u1d04\u1d0b");
                yamlConfiguration.set("texturepack.gui.accept_button.material", (Object)"LIME_DYE");
                yamlConfiguration.set("texturepack.gui.accept_button.name", (Object)"&#1DFF1ATak, zaakceptuj");
                yamlConfiguration.set("texturepack.gui.accept_button.lore", (Object)Arrays.asList((Object[])new String[]{"&7", "&8 \u00bb &7Kliknij, aby &fzaakceptowa\u0107", "&8 \u00bb &ftexturepack &7serwera.", "&7", "&8 \u00bb &7Texturepack b\u0119dzie &fautomatycznie", "&8 \u00bb &f\u0142adowany &7przy ka\u017cdym wej\u015bciu.", "&7", "&#1DFF1A\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d18\u0280\u1d22\u028f\u1d0a\u0105\u0107!"}));
                yamlConfiguration.set("texturepack.gui.decline_button.material", (Object)"RED_DYE");
                yamlConfiguration.set("texturepack.gui.decline_button.name", (Object)"&#FF0000Nie, odrzu\u0107");
                yamlConfiguration.set("texturepack.gui.decline_button.lore", (Object)Arrays.asList((Object[])new String[]{"&7", "&8 \u00bb &7Kliknij, aby &codrzuci\u0107", "&8 \u00bb &ctexturepack &7serwera.", "&7", "&8 \u00bb &7Texturepack &cnie b\u0119dzie", "&8 \u00bb &c\u0142adowany &7automatycznie.", "&7", "&#FF0000\u1d0b\u029f\u026a\u1d0b\u0274\u026a\u1d0a, \u1d00\u0299\u028f \u1d0f\u1d05\u0280\u1d22\u1d1c\u1d04\u026a\u0107!"}));
                yamlConfiguration.set("texturepack.gui.info_item.material", (Object)"BOOK");
                yamlConfiguration.set("texturepack.gui.info_item.name", (Object)"&#DD00FFInformacje");
                yamlConfiguration.set("texturepack.gui.info_item.lore", (Object)Arrays.asList((Object[])new String[]{"&7", "&8 \u00bb &7Czy chcesz &fautomatycznie", "&8 \u00bb &f\u0142adowa\u0107 &7texturepack serwera?", "&7", "&8 \u00bb &7Texturepack zawiera &fniestandardowe", "&8 \u00bb &ftekstury &7przedmiot\u00f3w pluginu.", "&7", "&8 \u00bb &7Wybierz opcj\u0119 &fponi\u017cej&7:", "&7"}));
                bl = true;
                this.A.info("Dodano konfiguracj\u0119 texturepacka do config.yml");
            }
            if (bl) {
                yamlConfiguration.save(file);
            }
        }
        catch (Exception exception) {
            this.A.warning("B\u0142\u0105d podczas aktualizacji konfiguracji texturepacka: " + exception.getMessage());
        }
    }
}

