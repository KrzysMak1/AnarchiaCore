/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.InputStream
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.StandardCharsets
 *  java.util.List
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.config;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class C {
    private static final String B = "c3Rvcm1jb2Rl";
    private final Plugin D;
    private FileConfiguration A;
    private FileConfiguration E;
    private String C;

    public C(Plugin plugin) {
        this.D = plugin;
        plugin.saveDefaultConfig();
        this.A();
    }

    public void A() {
        this.D.reloadConfig();
        this.A = this.D.getConfig();
        this.E();
    }

    public boolean A(File file) {
        if (!file.exists()) {
            return false;
        }
        try {
            int n2;
            Object object;
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
            int n3 = 0;
            if (yamlConfiguration.contains("disabled-regions")) {
                object = yamlConfiguration.getStringList("disabled-regions");
                this.A.set("disabled-regions", object);
                ++n3;
            }
            if (yamlConfiguration.contains("language")) {
                object = yamlConfiguration.getString("language");
                this.A.set("language", object);
                ++n3;
            }
            int n4 = 0;
            if (yamlConfiguration.contains("actionbar")) {
                if (yamlConfiguration.contains("actionbar.enabled")) {
                    n2 = yamlConfiguration.getBoolean("actionbar.enabled");
                    this.A.set("actionbar.enabled", (Object)(n2 != 0));
                    ++n4;
                }
                if (yamlConfiguration.contains("actionbar.separator")) {
                    String string = yamlConfiguration.getString("actionbar.separator");
                    this.A.set("actionbar.separator", (Object)string);
                    ++n4;
                }
                if (this.A.contains("actionbar.time_format") && yamlConfiguration.contains("actionbar.time_format")) {
                    String string = yamlConfiguration.getString("actionbar.time_format");
                    this.A.set("actionbar.time_format", (Object)string);
                    ++n4;
                }
                n2 = 0;
                if (yamlConfiguration.contains("actionbar.colors")) {
                    for (Object object2 : yamlConfiguration.getConfigurationSection("actionbar.colors").getKeys(false)) {
                        String string = yamlConfiguration.getString("actionbar.colors." + (String)object2);
                        this.A.set("actionbar.colors." + (String)object2, (Object)string);
                        ++n2;
                    }
                    if (n2 > 0) {
                        ++n4;
                    }
                }
                int n5 = 0;
                if (yamlConfiguration.contains("actionbar.display_names")) {
                    for (String string : yamlConfiguration.getConfigurationSection("actionbar.display_names").getKeys(false)) {
                        String string2 = yamlConfiguration.getString("actionbar.display_names." + string);
                        this.A.set("actionbar.display_names." + string, (Object)string2);
                        ++n5;
                    }
                    if (n5 > 0) {
                        ++n4;
                    }
                }
                if (n4 > 0) {
                    ++n3;
                }
            }
            if (yamlConfiguration.contains("bstats.enabled")) {
                n2 = yamlConfiguration.getBoolean("bstats.enabled");
                this.A.set("bstats.enabled", (Object)(n2 != 0));
                ++n3;
            }
            this.D.saveConfig();
            this.A();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private void E() {
        this.C = this.A.getString("language", "POL");
        File file = new File(this.D.getDataFolder(), "lang");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.A("POL.yml");
        this.A("ENG.yml");
        File file2 = new File(String.valueOf((Object)this.D.getDataFolder()) + "/lang", this.C + ".yml");
        if (!file2.exists()) {
            this.C = "POL";
            file2 = new File(String.valueOf((Object)this.D.getDataFolder()) + "/lang", "POL.yml");
        }
        this.E = YamlConfiguration.loadConfiguration((File)file2);
        InputStream inputStream = this.D.getResource("lang/" + this.C + ".yml");
        if (inputStream != null) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            this.E.setDefaults((Configuration)yamlConfiguration);
        }
    }

    private void A(String string) {
        File file = new File(String.valueOf((Object)this.D.getDataFolder()) + "/lang", string);
        if (!file.exists()) {
            this.D.saveResource("lang/" + string, false);
        }
    }

    public FileConfiguration C() {
        return this.A;
    }

    public FileConfiguration F() {
        return this.E;
    }

    public String D() {
        return this.C;
    }

    public List<String> B() {
        return this.A.getStringList("disabled-regions");
    }

    public String A(String string, String string2) {
        if (this.E == null) {
            return string2;
        }
        String string3 = "messages." + string;
        String string4 = this.E.getString(string3);
        if (string4 == null) {
            this.D.getLogger().warning("Missing message: " + string + " (full path: " + string3 + ")");
            return string2;
        }
        return string4;
    }

    public String B(String string) {
        return this.A(string, "Missing message: " + string);
    }

    public static class _A {
    }
}

