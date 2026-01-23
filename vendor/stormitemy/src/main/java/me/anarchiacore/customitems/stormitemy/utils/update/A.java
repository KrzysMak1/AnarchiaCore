/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.BufferedReader
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.net.URL
 *  java.net.URLConnection
 *  java.util.function.Consumer
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.utils.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;

public class A {
    private final Plugin C;
    private final int E;
    private static final long B = 3600000L;
    private static String D = null;
    private static long A = 0L;

    public A(Plugin javaPlugin, int n2) {
        this.C = javaPlugin;
        this.E = n2;
    }

    public void A(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.C, () -> {
            if (D != null && System.currentTimeMillis() - A < 3600000L) {
                consumer.accept((Object)D);
                return;
            }
            try {
                URL uRL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.E);
                URLConnection uRLConnection = uRL.openConnection();
                uRLConnection.setConnectTimeout(5000);
                uRLConnection.setReadTimeout(5000);
                uRLConnection.addRequestProperty("User-Agent", "StormItemy-UpdateChecker");
                try (BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader(uRLConnection.getInputStream()));){
                    String string = bufferedReader.readLine();
                    if (string != null && !string.trim().isEmpty()) {
                        D = string.trim();
                        A = System.currentTimeMillis();
                        consumer.accept((Object)D);
                    } else {
                        consumer.accept(null);
                    }
                }
            }
            catch (Exception exception) {
                this.C.getLogger().warning("Nie uda\u0142o si\u0119 sprawdzi\u0107 aktualizacji: " + exception.getMessage());
                consumer.accept(null);
            }
        });
    }

    private static int A(String string, String string2) {
        if (string == null || string2 == null) {
            return 0;
        }
        string = string.replaceAll("[Tt]$", "").trim();
        string2 = string2.replaceAll("[Tt]$", "").trim();
        String[] stringArray = string.split("\\.");
        String[] stringArray2 = string2.split("\\.");
        int n2 = Math.max((int)stringArray.length, (int)stringArray2.length);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = 0;
            int n4 = 0;
            if (i2 < stringArray.length) {
                try {
                    n3 = Integer.parseInt((String)stringArray[i2]);
                }
                catch (NumberFormatException numberFormatException) {
                    n3 = 0;
                }
            }
            if (i2 < stringArray2.length) {
                try {
                    n4 = Integer.parseInt((String)stringArray2[i2]);
                }
                catch (NumberFormatException numberFormatException) {
                    n4 = 0;
                }
            }
            if (n3 < n4) {
                return -1;
            }
            if (n3 <= n4) continue;
            return 1;
        }
        return 0;
    }

    public void A(String string, Consumer<Boolean> consumer) {
        this.A((Consumer<String>)((Consumer)string2 -> {
            if (string2 == null) {
                consumer.accept((Object)false);
                return;
            }
            boolean bl = me.anarchiacore.customitems.stormitemy.utils.update.A.A(string2, string) > 0;
            consumer.accept((Object)bl);
        }));
    }
}

