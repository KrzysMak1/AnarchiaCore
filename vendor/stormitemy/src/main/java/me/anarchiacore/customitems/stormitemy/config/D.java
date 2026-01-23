/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.lang.StringBuilder
 *  java.nio.file.CopyOption
 *  java.nio.file.DirectoryStream
 *  java.nio.file.Files
 *  java.nio.file.LinkOption
 *  java.nio.file.Path
 *  java.nio.file.attribute.FileAttribute
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 *  java.util.Map$Entry
 *  java.util.regex.Matcher
 *  java.util.regex.Pattern
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.messages.A;

public class D {
    private Plugin E;
    private String D;
    private FileConfiguration H;
    private static final Pattern C = Pattern.compile((String)"&#([A-Fa-f0-9]{6})");
    private static final Pattern B = Pattern.compile((String)"%([a-zA-Z0-9_]+)%");
    private List<String> G;
    private static volatile D A;
    private static final Object F;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public D(Plugin plugin) {
        this.E = plugin;
        Object object = F;
        synchronized (object) {
            A = this;
        }
        this.G = new ArrayList();
        this.D();
        this.C();
    }

    public static D E() {
        if (A == null) {
            throw new IllegalStateException("LanguageManager nie zosta\u0142 zainicjalizowany!");
        }
        return A;
    }

    private void D() {
        this.G.clear();
        this.G.add((Object)"POL");
        this.G.add((Object)"ENG");
        Path path = this.E.getDataFolder().toPath().resolve("lang");
        if (!Files.exists((Path)path, (LinkOption[])new LinkOption[0])) {
            try {
                Files.createDirectories((Path)path, (FileAttribute[])new FileAttribute[0]);
            }
            catch (IOException iOException) {
                this.E.getLogger().warning("Nie mo\u017cna utworzy\u0107 katalogu lang: " + iOException.getMessage());
            }
            return;
        }
        try (DirectoryStream directoryStream = Files.newDirectoryStream((Path)path, (String)"*.yml");){
            for (Path path2 : directoryStream) {
                String string = path2.getFileName().toString();
                String string2 = string.replace((CharSequence)".yml", (CharSequence)"");
                if (this.G.contains((Object)string2)) continue;
                this.G.add((Object)string2);
            }
        }
        catch (IOException iOException) {
            this.E.getLogger().warning("B\u0142\u0105d przy \u0142adowaniu j\u0119zyk\u00f3w: " + iOException.getMessage());
        }
    }

    public void C() {
        File file;
        this.D();
        this.D = this.E.getConfig().getString("settings.language", "POL");
        if (!this.D(this.D)) {
            this.D = "POL";
        }
        if (!(file = new File(this.E.getDataFolder(), "lang")).exists()) {
            file.mkdirs();
        }
        this.E("POL.yml");
        this.E("ENG.yml");
        File file2 = new File(String.valueOf((Object)this.E.getDataFolder()) + "/lang", this.D + ".yml");
        if (!file2.exists()) {
            // empty if block
        }
        this.H = YamlConfiguration.loadConfiguration((File)file2);
        String string = this.H.getString("menu.main-title");
        if (string == null) {
            // empty if block
        }
    }

    private void E(String string) {
        File file = new File(String.valueOf((Object)this.E.getDataFolder()) + "/lang", string);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                InputStream inputStream = this.E.getResource("lang/" + string);
                if (inputStream != null) {
                    Files.copy((InputStream)inputStream, (Path)file.toPath(), (CopyOption[])new CopyOption[0]);
                    inputStream.close();
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    private boolean D(String string) {
        return this.G.contains((Object)string);
    }

    public String F(String string) {
        String string2 = this.H.getString(string);
        if (string2 == null) {
            return "\u00a7cMissing language key: " + string;
        }
        return me.anarchiacore.customitems.stormitemy.config.D.C(string2);
    }

    public String A(String string, Map<String, String> map) {
        String string2 = this.F(string);
        for (Map.Entry entry : map.entrySet()) {
            string2 = string2.replace((CharSequence)("%" + (String)entry.getKey() + "%"), (CharSequence)entry.getValue());
        }
        return string2;
    }

    public String B(String string, String string2, String string3) {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)string2, (Object)string3);
        return this.A(string, (Map<String, String>)hashMap);
    }

    public static String C(String string) {
        if (string == null) {
            return "";
        }
        Matcher matcher = C.matcher((CharSequence)string);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String string2 = matcher.group(1);
            StringBuilder stringBuilder = new StringBuilder("&x");
            for (char c2 : string2.toCharArray()) {
                stringBuilder.append("&").append(c2);
            }
            matcher.appendReplacement(stringBuffer, stringBuilder.toString());
        }
        matcher.appendTail(stringBuffer);
        return ChatColor.translateAlternateColorCodes((char)'&', (String)stringBuffer.toString());
    }

    public String B() {
        return this.D;
    }

    public List<String> A() {
        return new ArrayList(this.G);
    }

    public boolean B(String string) {
        if (!this.D(string)) {
            return false;
        }
        this.E.getConfig().set("settings.language", (Object)string);
        try {
            this.E.saveConfig();
        }
        catch (Exception exception) {
            return false;
        }
        this.D = string;
        this.C();
        return true;
    }

    public A A(String string) {
        if (this.H.isConfigurationSection(string)) {
            ConfigurationSection configurationSection = this.H.getConfigurationSection(string);
            return new A(configurationSection);
        }
        String string2 = this.H.getString(string);
        if (string2 == null) {
            return new A("\u00a7cMissing language key: " + string);
        }
        return new A(me.anarchiacore.customitems.stormitemy.config.D.C(string2));
    }

    public A C(String string, Map<String, String> map) {
        A a2 = this.A(string);
        A._A _A2 = new A._A().A(a2.I()).B(a2.A()).C(a2.B());
        if (a2.D()) {
            _A2.D(this.B(a2.F(), map));
        }
        if (a2.H()) {
            _A2.A(this.B(a2.E(), map));
        }
        if (a2.K()) {
            _A2.B(this.B(a2.G(), map));
        }
        if (a2.J()) {
            _A2.C(this.B(a2.C(), map));
        }
        return _A2.A();
    }

    public A A(String string, String string2, String string3) {
        HashMap hashMap = new HashMap();
        hashMap.put((Object)string2, (Object)string3);
        return this.C(string, (Map<String, String>)hashMap);
    }

    private String B(String string, Map<String, String> map) {
        if (string == null || map == null) {
            return string;
        }
        String string2 = string;
        for (Map.Entry entry : map.entrySet()) {
            string2 = string2.replace((CharSequence)("%" + (String)entry.getKey() + "%"), (CharSequence)entry.getValue()).replace((CharSequence)("{" + (String)entry.getKey() + "}"), (CharSequence)entry.getValue());
        }
        return string2;
    }

    static {
        F = new Object();
    }
}

