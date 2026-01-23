/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.BufferedReader
 *  java.io.ByteArrayOutputStream
 *  java.io.File
 *  java.io.FileReader
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.io.Reader
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Iterable
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.nio.file.CopyOption
 *  java.nio.file.Files
 *  java.nio.file.OpenOption
 *  java.nio.file.Path
 *  java.nio.file.StandardCopyOption
 *  java.sql.Connection
 *  java.sql.DriverManager
 *  java.sql.PreparedStatement
 *  java.util.ArrayList
 *  java.util.Base64
 *  java.util.HashMap
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Map
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Executors
 *  java.util.concurrent.Future
 *  java.util.concurrent.TimeUnit
 *  java.util.concurrent.TimeoutException
 *  java.util.regex.Pattern
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.util.io.BukkitObjectOutputStream
 */
package me.anarchiacore.customitems.stormitemy.config;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class B {
    private final Plugin C;
    private static final int A = 50;
    private static final int B = 10000;
    private static final Pattern D = Pattern.compile((String)".*&\\d+.*\\*\\d+.*");

    public B(Plugin plugin) {
        this.C = plugin;
    }

    public int A(File file, File file2) {
        if (!file.exists()) {
            this.C.getLogger().warning("Plik " + file.getName() + " nie istnieje!");
            return 0;
        }
        this.C.getLogger().info("Rozpoczynam odzyskiwanie danych z uszkodzonego pliku: " + file.getName());
        int n2 = 0;
        try {
            ItemStack itemStack;
            String string;
            String string2 = "jdbc:sqlite:" + file2.getAbsolutePath();
            Connection connection = DriverManager.getConnection((String)string2);
            String string3 = "INSERT OR REPLACE INTO pouches (pouch_id, slot, item_data) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(string3);
            List list = Files.readAllLines((Path)file.toPath());
            String string4 = null;
            String string5 = null;
            HashMap hashMap = new HashMap();
            ArrayList arrayList = new ArrayList();
            boolean bl = false;
            for (int i2 = 0; i2 < list.size(); ++i2) {
                String string6;
                Object object;
                string = (String)list.get(i2);
                String string7 = string.trim();
                if (string7.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}:")) {
                    string4 = string7.replace((CharSequence)":", (CharSequence)"");
                    this.C.getLogger().info("Znaleziono sakiewk\u0119: " + string4);
                    continue;
                }
                if (string7.matches("'?\\d+'?:") && string4 != null) {
                    if (string5 != null && !hashMap.isEmpty()) {
                        if (!arrayList.isEmpty()) {
                            hashMap.put((Object)"lore-list", (Object)String.join((CharSequence)"|||", (Iterable)arrayList));
                        }
                        if ((object = this.A((Map<String, String>)hashMap)) != null) {
                            try {
                                string6 = this.A((ItemStack)object);
                                preparedStatement.setString(1, string4);
                                preparedStatement.setInt(2, Integer.parseInt((String)string5));
                                preparedStatement.setString(3, string6);
                                preparedStatement.executeUpdate();
                                ++n2;
                                this.C.getLogger().info("Odzyskano przedmiot: " + String.valueOf((Object)object.getType()) + " (slot " + string5 + ")");
                            }
                            catch (Exception exception) {
                                this.C.getLogger().warning("Nie mo\u017cna zapisa\u0107 odzyskanego przedmiotu: " + exception.getMessage());
                            }
                        }
                    }
                    string5 = string7.replace((CharSequence)":", (CharSequence)"").replace((CharSequence)"'", (CharSequence)"");
                    hashMap.clear();
                    arrayList.clear();
                    bl = false;
                    continue;
                }
                if (bl && string7.startsWith("- ")) {
                    object = string7.substring(2).trim();
                    if (object.startsWith("'") && object.endsWith("'")) {
                        object = object.substring(1, object.length() - 1);
                    }
                    if (object.startsWith("\"") && object.endsWith("\"")) {
                        object = object.substring(1, object.length() - 1);
                    }
                    if (object.startsWith("{") && object.contains((CharSequence)"\"text\"") && (string6 = this.D((String)object)) != null && !string6.equals(object)) {
                        object = string6;
                        this.C.getLogger().info("  \u2192 Sparsowano lore: " + (String)object);
                    }
                    if (object.isEmpty()) continue;
                    arrayList.add(object);
                    continue;
                }
                if (string5 == null || !string7.contains((CharSequence)":") || ((ItemStack)(object = string7.split(":", 2))).length != 2) continue;
                string6 = object[0].trim().replace((CharSequence)"'", (CharSequence)"");
                String string8 = object[1].trim();
                if (string6.startsWith("*") || string6.startsWith("&") || string8.startsWith("*") || string8.startsWith("&")) {
                    this.C.getLogger().warning("Pomijam YAML anchor/alias: " + string6 + ": " + string8);
                    continue;
                }
                if (string6.equals((Object)"lore")) {
                    bl = true;
                    if (string8.matches("\\d+") || string8.isEmpty()) continue;
                    hashMap.put((Object)string6, (Object)string8);
                    continue;
                }
                bl = false;
                if (string8.startsWith("'") && string8.endsWith("'")) {
                    string8 = string8.substring(1, string8.length() - 1);
                }
                if (string8.startsWith("\"") && string8.endsWith("\"")) {
                    string8 = string8.substring(1, string8.length() - 1);
                }
                if (string8.startsWith("*") || string8.startsWith("&")) {
                    this.C.getLogger().warning("Pomijam warto\u015b\u0107 YAML anchor: " + string6 + " = " + string8);
                    continue;
                }
                hashMap.put((Object)string6, (Object)string8);
            }
            if (string5 != null && !hashMap.isEmpty() && (itemStack = this.A((Map<String, String>)hashMap)) != null) {
                try {
                    string = this.A(itemStack);
                    preparedStatement.setString(1, string4);
                    preparedStatement.setInt(2, Integer.parseInt(string5));
                    preparedStatement.setString(3, string);
                    preparedStatement.executeUpdate();
                    ++n2;
                    this.C.getLogger().info("Odzyskano przedmiot: " + String.valueOf((Object)itemStack.getType()) + " (slot " + string5 + ")");
                }
                catch (Exception exception) {
                    this.C.getLogger().warning("Nie mo\u017cna zapisa\u0107 odzyskanego przedmiotu: " + exception.getMessage());
                }
            }
            preparedStatement.close();
            connection.close();
            this.C.getLogger().info("Odzyskiwanie zako\u0144czone! Odzyskano " + n2 + " przedmiot\u00f3w.");
        }
        catch (Exception exception) {
            this.C.getLogger().severe("B\u0142\u0105d podczas odzyskiwania danych: " + exception.getMessage());
            exception.printStackTrace();
        }
        return n2;
    }

    public YamlConfiguration F(File file) {
        this.C.getLogger().info("[SAFE YAML] Rozpoczynam bezpieczne \u0142adowanie: " + file.getName());
        try {
            long l2 = file.length();
            if (l2 > 0x3200000L) {
                this.C.getLogger().warning("[SAFE YAML] Plik jest zbyt du\u017cy (" + l2 + " bajt\u00f3w), u\u017cywam odzyskiwania");
                return null;
            }
            if (this.E(file)) {
                this.C.getLogger().warning("[SAFE YAML] Wykryto cykliczne referencje, pomijam standardowe \u0142adowanie");
                return this.H(file);
            }
            if (this.B(file)) {
                this.C.getLogger().warning("[SAFE YAML] Wykryto problematyczne wzorce, u\u017cywam naprawy");
                return this.H(file);
            }
            YamlConfiguration yamlConfiguration = this.G(file);
            if (yamlConfiguration == null || yamlConfiguration.getKeys(false).isEmpty()) {
                this.C.getLogger().warning("[SAFE YAML] Standardowe \u0142adowanie nie powiod\u0142o si\u0119, u\u017cywam odzyskiwania");
                return this.H(file);
            }
            this.C.getLogger().info("[SAFE YAML] \u2713 Pomy\u015blnie za\u0142adowano YAML");
            return yamlConfiguration;
        }
        catch (Exception exception) {
            this.C.getLogger().warning("[SAFE YAML] B\u0142\u0105d podczas \u0142adowania: " + exception.getMessage());
            return this.H(file);
        }
    }

    private YamlConfiguration G(File file) {
        return this.C(file);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private YamlConfiguration C(File file) {
        ExecutorService executorService = null;
        try {
            YamlConfiguration yamlConfiguration;
            executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(() -> {
                try {
                    return YamlConfiguration.loadConfiguration((File)file);
                }
                catch (Exception exception) {
                    this.C.getLogger().warning("[SAFE YAML] B\u0142\u0105d w w\u0105tku \u0142adowania: " + exception.getMessage());
                    return null;
                }
            });
            YamlConfiguration yamlConfiguration2 = yamlConfiguration = (YamlConfiguration)future.get(10L, TimeUnit.SECONDS);
            return yamlConfiguration2;
        }
        catch (TimeoutException timeoutException) {
            this.C.getLogger().warning("[SAFE YAML] Timeout podczas \u0142adowania YAML - prawdopodobnie cykliczne referencje");
            YamlConfiguration yamlConfiguration = null;
            return yamlConfiguration;
        }
        catch (Exception exception) {
            this.C.getLogger().warning("[SAFE YAML] B\u0142\u0105d podczas \u0142adowania z timeoutem: " + exception.getMessage());
            YamlConfiguration yamlConfiguration = null;
            return yamlConfiguration;
        }
        finally {
            if (executorService != null) {
                try {
                    executorService.shutdown();
                    if (!executorService.awaitTermination(1L, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                }
                catch (InterruptedException interruptedException) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean B(File file) {
        try (BufferedReader bufferedReader = new BufferedReader((Reader)new FileReader(file));){
            int n2;
            String string;
            int n3 = 0;
            HashSet hashSet = new HashSet();
            while ((string = bufferedReader.readLine()) != null && n3 < 10000) {
                char c2;
                ++n3;
                if (string.length() > 10000) {
                    this.C.getLogger().warning("[SAFE YAML] Znaleziono zbyt d\u0142ug\u0105 lini\u0119: " + n3);
                    n2 = 1;
                    return n2 != 0;
                }
                n2 = 0;
                Object object = string.toCharArray();
                boolean bl = ((char[])object).length;
                for (boolean bl2 = false; bl2 < bl && (c2 = object[bl2]) == ' '; ++n2, bl2 += 1) {
                }
                if (n2 > 200) {
                    this.C.getLogger().warning("[SAFE YAML] Nadmierna g\u0142\u0119boko\u015b\u0107 wci\u0119\u0107 na linii: " + n3);
                    boolean bl3 = true;
                    return bl3;
                }
                if (!string.contains((CharSequence)"&") || (object = (Object)this.F(string)) == null) continue;
                if (hashSet.contains(object)) {
                    this.C.getLogger().warning("[SAFE YAML] Duplikat anchora: " + (String)object);
                    bl = true;
                    return bl;
                }
                hashSet.add(object);
            }
            n2 = 0;
            return n2 != 0;
        }
        catch (IOException iOException) {
            this.C.getLogger().warning("[SAFE YAML] B\u0142\u0105d podczas sprawdzania wzorc\u00f3w: " + iOException.getMessage());
            return true;
        }
    }

    private YamlConfiguration H(File file) {
        try {
            String string2;
            this.C.getLogger().info("[YAML REPAIR] Rozpoczynam napraw\u0119 pliku: " + file.getName());
            File file2 = this.D(file);
            this.C.getLogger().info("[YAML REPAIR] Utworzono kopi\u0119 zapasow\u0105: " + file2.getName());
            List list = Files.readAllLines((Path)file.toPath());
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            int n2 = 0;
            for (String string2 : list) {
                String string3;
                if (++n2 > 5000) {
                    this.C.getLogger().warning("[YAML REPAIR] Przekroczono limit przetwarzanych linii, przerywam");
                    break;
                }
                if (string2.length() > 10000) {
                    this.C.getLogger().warning("[YAML REPAIR] Pomini\u0119to zbyt d\u0142ug\u0105 lini\u0119 (" + string2.length() + " znak\u00f3w)");
                    continue;
                }
                if (D.matcher((CharSequence)string2).matches()) {
                    this.C.getLogger().warning("[YAML REPAIR] Wykryto cykliczn\u0105 referencj\u0119, pomijam: " + string2.substring(0, Math.min((int)50, (int)string2.length())));
                    continue;
                }
                if (string2.contains((CharSequence)"&") && string2.contains((CharSequence)"*") && (string3 = this.F(string2)) != null) {
                    if (hashSet.contains((Object)string3)) {
                        this.C.getLogger().warning("[YAML REPAIR] Wykryto duplikat anchora: " + string3 + ", pomijam");
                        continue;
                    }
                    hashSet.add((Object)string3);
                }
                string3 = string2.replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]", "");
                arrayList.add((Object)string3);
            }
            Iterator iterator = new File(file.getParent(), "temp_repaired_" + System.currentTimeMillis() + "_" + file.getName());
            Files.write((Path)iterator.toPath(), (Iterable)arrayList, (OpenOption[])new OpenOption[0]);
            string2 = this.C((File)iterator);
            iterator.delete();
            if (string2 != null) {
                this.C.getLogger().info("[YAML REPAIR] \u2713 Pomy\u015blnie naprawiono i za\u0142adowano YAML");
                return string2;
            }
            this.C.getLogger().warning("[YAML REPAIR] Nie uda\u0142o si\u0119 za\u0142adowa\u0107 naprawionego pliku");
            return new YamlConfiguration();
        }
        catch (Exception exception) {
            this.C.getLogger().severe("[YAML REPAIR] B\u0142\u0105d podczas naprawy: " + exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    private boolean E(File file) {
        try {
            List list = Files.readAllLines((Path)file.toPath());
            HashSet hashSet = new HashSet();
            HashSet hashSet2 = new HashSet();
            int n2 = 0;
            for (String string : list) {
                String string2;
                if (++n2 > 2500) {
                    this.C.getLogger().warning("[CYCLIC CHECK] Przekroczono limit sprawdzanych linii");
                    return true;
                }
                if (string.length() > 10000) continue;
                if (string.contains((CharSequence)"&") && (string2 = this.F(string)) != null) {
                    if (hashSet.contains((Object)string2)) {
                        this.C.getLogger().warning("[CYCLIC CHECK] Wykryto duplikat anchora: " + string2);
                        return true;
                    }
                    hashSet.add((Object)string2);
                }
                if (!string.contains((CharSequence)"*")) continue;
                int n3 = string.indexOf("*") + 1;
                int n4 = string.indexOf(" ", n3);
                if (n4 == -1) {
                    n4 = string.length();
                }
                if (n3 >= string.length()) continue;
                String string3 = string.substring(n3, Math.min((int)n4, (int)string.length())).trim();
                hashSet2.add((Object)string3);
            }
            for (String string : hashSet2) {
                if (hashSet.contains((Object)string)) continue;
                this.C.getLogger().warning("[CYCLIC CHECK] Referencja bez anchora: " + string);
                return true;
            }
            return false;
        }
        catch (Exception exception) {
            this.C.getLogger().warning("[CYCLIC CHECK] B\u0142\u0105d podczas sprawdzania: " + exception.getMessage());
            return true;
        }
    }

    private String F(String string) {
        try {
            if (string.contains((CharSequence)"&")) {
                int n2 = string.indexOf("&") + 1;
                int n3 = string.indexOf(" ", n2);
                if (n3 == -1) {
                    n3 = string.length();
                }
                if (n2 < string.length()) {
                    return string.substring(n2, Math.min((int)n3, (int)string.length())).trim();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    private File D(File file) throws IOException {
        String string = String.valueOf((long)System.currentTimeMillis());
        File file2 = new File(file.getParent(), "safe_backup_" + string + "_" + file.getName());
        Files.copy((Path)file.toPath(), (Path)file2.toPath(), (CopyOption[])new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        return file2;
    }

    private ItemStack A(Map<String, String> map) {
        try {
            Object object;
            String string = (String)map.get((Object)"type");
            if (string == null) {
                return null;
            }
            Material material = Material.getMaterial((String)string.toUpperCase());
            if (material == null) {
                this.C.getLogger().warning("Nieznany materia\u0142: " + string);
                return null;
            }
            int n2 = 1;
            if (map.containsKey((Object)"amount")) {
                try {
                    n2 = Integer.parseInt((String)((String)map.get((Object)"amount")));
                }
                catch (NumberFormatException numberFormatException) {
                    n2 = 1;
                }
            }
            ItemStack itemStack = new ItemStack(material, n2);
            if ((map.containsKey((Object)"meta-type") || map.containsKey((Object)"display-name") || map.containsKey((Object)"lore") || map.containsKey((Object)"custom-model-data") || map.containsKey((Object)"enchants") || map.containsKey((Object)"ItemFlags") || map.containsKey((Object)"Unbreakable")) && (object = itemStack.getItemMeta()) != null) {
                Object object2;
                String string2;
                if (map.containsKey((Object)"display-name")) {
                    string2 = (String)map.get((Object)"display-name");
                    if (string2.startsWith("{") && string2.contains((CharSequence)"\"text\"")) {
                        try {
                            object2 = this.D(string2);
                            object.setDisplayName(object2);
                            this.C.getLogger().info("  \u2192 Odzyskano nazw\u0119: " + object2);
                        }
                        catch (Exception exception) {
                            object.setDisplayName(string2);
                        }
                    } else {
                        object.setDisplayName(string2);
                    }
                }
                if (map.containsKey((Object)"lore-list")) {
                    string2 = (String)map.get((Object)"lore-list");
                    object2 = string2.split("\\|\\|\\|");
                    ArrayList arrayList = new ArrayList();
                    for (List<String> list : object2) {
                        if (list.isEmpty()) continue;
                        arrayList.add(list);
                    }
                    if (!arrayList.isEmpty()) {
                        object.setLore((List)arrayList);
                        this.C.getLogger().info("  \u2192 Odzyskano lore (" + arrayList.size() + " linii)");
                    }
                } else if (map.containsKey((Object)"lore") && !(object2 = this.C(string2 = (String)map.get((Object)"lore"))).isEmpty()) {
                    object.setLore(object2);
                }
                if (map.containsKey((Object)"custom-model-data")) {
                    try {
                        int n3 = Integer.parseInt((String)((String)map.get((Object)"custom-model-data")));
                        object.setCustomModelData(Integer.valueOf((int)n3));
                    }
                    catch (NumberFormatException numberFormatException) {
                        this.C.getLogger().warning("Nieprawid\u0142owy custom-model-data: " + (String)map.get((Object)"custom-model-data"));
                    }
                }
                if (map.containsKey((Object)"Unbreakable")) {
                    boolean bl = Boolean.parseBoolean((String)((String)map.get((Object)"Unbreakable")));
                    object.setUnbreakable(bl);
                }
                if (map.containsKey((Object)"ItemFlags")) {
                    String string3 = (String)map.get((Object)"ItemFlags");
                    object2 = this.G(string3);
                    for (ItemFlag itemFlag : object2) {
                        object.addItemFlags(new ItemFlag[]{itemFlag});
                    }
                }
                itemStack.setItemMeta(object);
            }
            if (map.containsKey((Object)"enchants")) {
                object = (String)map.get((Object)"enchants");
                Map<Enchantment, Integer> map2 = this.E((String)object);
                for (ArrayList arrayList : map2.entrySet()) {
                    itemStack.addUnsafeEnchantment((Enchantment)arrayList.getKey(), ((Integer)arrayList.getValue()).intValue());
                }
            }
            this.C.getLogger().info("Odzyskano: " + String.valueOf((Object)material) + " x" + n2 + (String)(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() ? " (" + itemStack.getItemMeta().getDisplayName() + ")" : ""));
            return itemStack;
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d podczas odzyskiwania przedmiotu: " + exception.getMessage());
            return null;
        }
    }

    private String D(String string) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (string.contains((CharSequence)"\"extra\"")) {
                int n2 = string.indexOf("\"extra\":[") + 9;
                int n3 = string.lastIndexOf("]");
                if (n2 > 9 && n3 > n2) {
                    int n4;
                    int n5;
                    String string2 = string.substring(n2, n3);
                    int n6 = 0;
                    while (n6 < string2.length() && (n5 = string2.indexOf("\"text\":\"", n6)) != -1 && (n4 = string2.indexOf("\"", n5 += 8)) > n5) {
                        String string3 = string2.substring(n5, n4);
                        String string4 = "";
                        int n7 = string2.indexOf("}", n4);
                        if (n7 > n4) {
                            String string5 = string2.substring(n5 - 8, n7);
                            if (string5.contains((CharSequence)"\"color\":\"")) {
                                int n8 = string5.indexOf("\"color\":\"") + 9;
                                int n9 = string5.indexOf("\"", n8);
                                if (n8 > 9 && n9 > n8) {
                                    String string6 = string5.substring(n8, n9);
                                    string4 = string4 + this.B(string6);
                                }
                            }
                            if (string5.contains((CharSequence)"\"bold\":true")) {
                                string4 = string4 + "\u00a7l";
                            }
                            if (string5.contains((CharSequence)"\"italic\":true")) {
                                string4 = string4 + "\u00a7o";
                            }
                            if (string5.contains((CharSequence)"\"underlined\":true")) {
                                string4 = string4 + "\u00a7n";
                            }
                            if (string5.contains((CharSequence)"\"strikethrough\":true")) {
                                string4 = string4 + "\u00a7m";
                            }
                            if (string5.contains((CharSequence)"\"obfuscated\":true")) {
                                string4 = string4 + "\u00a7k";
                            }
                        }
                        stringBuilder.append(string4).append(string3);
                        n6 = n4;
                    }
                    if (stringBuilder.length() > 0) {
                        return stringBuilder.toString();
                    }
                }
            } else if (string.contains((CharSequence)"\"text\":\"")) {
                int n10 = string.indexOf("\"text\":\"") + 8;
                int n11 = string.indexOf("\"", n10);
                if (n10 > 8 && n11 > n10) {
                    return string.substring(n10, n11);
                }
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("B\u0142\u0105d parsowania JSON: " + exception.getMessage());
        }
        return string;
    }

    private String A(String string) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            if (string.contains((CharSequence)"\"extra\"")) {
                int n2 = string.indexOf("\"extra\":[") + 9;
                int n3 = string.lastIndexOf("]");
                if (n2 > 9 && n3 > n2) {
                    String[] stringArray;
                    String string2 = string.substring(n2, n3);
                    for (String string3 : stringArray = string2.split("\\{")) {
                        if (!string3.contains((CharSequence)"\"text\":\"")) continue;
                        int n4 = string3.indexOf("\"text\":\"") + 8;
                        int n5 = string3.indexOf("\"", n4);
                        if (n4 <= 8 || n5 <= n4) continue;
                        String string4 = string3.substring(n4, n5);
                        String string5 = "";
                        if (string3.contains((CharSequence)"\"color\":\"")) {
                            int n6 = string3.indexOf("\"color\":\"") + 9;
                            int n7 = string3.indexOf("\"", n6);
                            if (n6 > 9 && n7 > n6) {
                                String string6 = string3.substring(n6, n7);
                                string5 = string5 + this.B(string6);
                            }
                        }
                        if (string3.contains((CharSequence)"\"bold\":true")) {
                            string5 = string5 + "\u00a7l";
                        }
                        if (string3.contains((CharSequence)"\"italic\":true")) {
                            string5 = string5 + "\u00a7o";
                        }
                        if (string3.contains((CharSequence)"\"underlined\":true")) {
                            string5 = string5 + "\u00a7n";
                        }
                        if (string3.contains((CharSequence)"\"strikethrough\":true")) {
                            string5 = string5 + "\u00a7m";
                        }
                        if (string3.contains((CharSequence)"\"obfuscated\":true")) {
                            string5 = string5 + "\u00a7k";
                        }
                        stringBuilder.append(string5).append(string4);
                    }
                }
                if (stringBuilder.length() > 0) {
                    return stringBuilder.toString();
                }
            } else if (string.contains((CharSequence)"\"text\":\"")) {
                int n8 = string.indexOf("\"text\":\"") + 8;
                int n9 = string.indexOf("\"", n8);
                if (n8 > 8 && n9 > n8) {
                    return string.substring(n8, n9);
                }
            }
        }
        catch (Exception exception) {
            this.C.getLogger().warning("Nie mo\u017cna sparsowa\u0107 JSON display-name: " + exception.getMessage());
        }
        return string;
    }

    /*
     * Exception decompiling
     */
    private String B(String var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * ob.f0$e
         *     at ob.f0.e(SourceFile:35)
         *     at ob.f0.a(SourceFile:1)
         *     at ob.f0$d.a(SourceFile:68)
         *     at qb.n.i(SourceFile:13)
         *     at qb.e.i(SourceFile:9)
         *     at qb.l.i(SourceFile:14)
         *     at qb.n.i(SourceFile:3)
         *     at ob.f0.g(SourceFile:649)
         *     at ob.f0.d(SourceFile:37)
         *     at ib.f.d(SourceFile:235)
         *     at ib.f.e(SourceFile:7)
         *     at ib.f.c(SourceFile:95)
         *     at rc.f.n(SourceFile:11)
         *     at pc.i.m(SourceFile:5)
         *     at pc.d.K(SourceFile:92)
         *     at pc.d.g0(SourceFile:1)
         *     at fb.b.d(SourceFile:191)
         *     at fb.b.c(SourceFile:145)
         *     at fb.a.a(SourceFile:108)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.decompileWithCFR(SourceFile:76)
         *     at com.thesourceofcode.jadec.decompilers.JavaExtractionWorker.doWork(SourceFile:110)
         *     at com.thesourceofcode.jadec.decompilers.BaseDecompiler.withAttempt(SourceFile:3)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.d(SourceFile:53)
         *     at com.thesourceofcode.jadec.workers.DecompilerWorker.b(SourceFile:1)
         *     at e7.a.run(SourceFile:1)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1154)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:652)
         *     at java.lang.Thread.run(Thread.java:1563)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private List<String> C(String string) {
        ArrayList arrayList = new ArrayList();
        if (string.matches("\\d+")) {
            this.C.getLogger().warning("Lore jest hash code, nie mo\u017cna odzyska\u0107 pe\u0142nej zawarto\u015bci");
            return arrayList;
        }
        if (string.startsWith("[") && string.endsWith("]")) {
            String[] stringArray;
            String string2 = string.substring(1, string.length() - 1);
            for (String string3 : stringArray = string2.split(",")) {
                arrayList.add((Object)string3.trim());
            }
        }
        return arrayList;
    }

    private Map<Enchantment, Integer> E(String string) {
        HashMap hashMap = new HashMap();
        if (string.matches("\\d+")) {
            this.C.getLogger().warning("Enchantmenty s\u0105 hash code, nie mo\u017cna odzyska\u0107");
            return hashMap;
        }
        if (string.startsWith("{") && string.endsWith("}")) {
            String[] stringArray;
            String string2 = string.substring(1, string.length() - 1);
            for (String string3 : stringArray = string2.split(",")) {
                String[] stringArray2 = string3.split(":");
                if (stringArray2.length != 2) continue;
                String string4 = stringArray2[0].trim().toUpperCase();
                try {
                    int n2 = Integer.parseInt((String)stringArray2[1].trim());
                    Enchantment enchantment = Enchantment.getByName((String)string4);
                    if (enchantment == null) continue;
                    hashMap.put((Object)enchantment, (Object)n2);
                }
                catch (NumberFormatException numberFormatException) {
                    this.C.getLogger().warning("Nieprawid\u0142owy poziom enchantu: " + stringArray2[1]);
                }
            }
        }
        return hashMap;
    }

    private List<ItemFlag> G(String string) {
        ArrayList arrayList = new ArrayList();
        if (string.matches("\\d+")) {
            this.C.getLogger().warning("ItemFlags s\u0105 hash code, nie mo\u017cna odzyska\u0107");
            return arrayList;
        }
        if (string.startsWith("[") && string.endsWith("]")) {
            String[] stringArray;
            String string2 = string.substring(1, string.length() - 1);
            for (String string3 : stringArray = string2.split(",")) {
                try {
                    ItemFlag itemFlag = ItemFlag.valueOf((String)string3.trim().toUpperCase());
                    arrayList.add((Object)itemFlag);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    this.C.getLogger().warning("Nieznana flaga: " + string3);
                }
            }
        }
        return arrayList;
    }

    private String A(ItemStack itemStack) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream)byteArrayOutputStream);
            bukkitObjectOutputStream.writeObject((Object)itemStack);
            bukkitObjectOutputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        }
        catch (IOException iOException) {
            throw new IOException("Nie mo\u017cna zserializowa\u0107 ItemStack: " + iOException.getMessage(), (Throwable)iOException);
        }
    }
}

