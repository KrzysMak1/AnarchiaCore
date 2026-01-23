/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.BufferedReader
 *  java.io.InputStreamReader
 *  java.io.Reader
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.net.HttpURLConnection
 *  java.net.URL
 *  java.security.SecureRandom
 *  java.security.cert.X509Certificate
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.HttpsURLConnection
 *  javax.net.ssl.SSLContext
 *  javax.net.ssl.SSLSocketFactory
 *  javax.net.ssl.TrustManager
 *  javax.net.ssl.X509TrustManager
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.premium;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.Main;

public class A {
    private final Main D;
    private boolean C;
    private String E;
    private boolean A;
    private static final String B = "https://stormcode.tech/api/premium/validate";

    public A(Main main) {
        this.D = main;
        this.A = false;
        this.C();
        this.I();
    }

    private void C() {
        try {
            TrustManager[] trustManagerArray = new TrustManager[]{new X509TrustManager(){

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
                }

                public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
                }
            }};
            SSLContext sSLContext = SSLContext.getInstance((String)"SSL");
            sSLContext.init(null, trustManagerArray, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory((SSLSocketFactory)sSLContext.getSocketFactory());
            HostnameVerifier hostnameVerifier = (string, sSLSession) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier((HostnameVerifier)hostnameVerifier);
        }
        catch (Exception exception) {
            this.D.getLogger().warning("Nie uda\u0142o si\u0119 skonfigurowa\u0107 SSL: " + exception.getMessage());
        }
    }

    public void I() {
        FileConfiguration fileConfiguration = this.D.getConfig();
        this.C = fileConfiguration.getBoolean("premium.enabled", false);
        this.E = fileConfiguration.getString("premium.code", "");
        if (this.C && !this.E.isEmpty()) {
            this.B();
        } else {
            this.D();
        }
    }

    private void B() {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.D, () -> {
            try {
                String string = "https://stormcode.tech/api/premium/validate?code=" + this.E;
                URL uRL = new URL(string);
                HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                int n2 = httpURLConnection.getResponseCode();
                if (n2 == 200) {
                    String string2;
                    BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((string2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(string2);
                    }
                    bufferedReader.close();
                    String string3 = stringBuilder.toString();
                    if (string3.contains((CharSequence)"\"valid\":true") || string3.contains((CharSequence)"\"valid\": true")) {
                        this.A = true;
                        this.E();
                        Bukkit.getScheduler().runTask((Plugin)this.D, () -> {
                            if (this.D.getInitializer() != null && !this.D.getInitializer().B()) {
                                this.D.getInitializer().M();
                            }
                        });
                    } else {
                        this.A = false;
                        String string4 = this.A(string3, "error");
                        this.C(string4);
                    }
                } else {
                    this.A = false;
                    this.C("B\u0142\u0105d po\u0142\u0105czenia z serwerem walidacji (HTTP " + n2 + ")");
                }
                httpURLConnection.disconnect();
            }
            catch (Exception exception) {
                this.A = false;
                this.C("B\u0142\u0105d walidacji: " + exception.getMessage());
            }
        });
    }

    private void E() {
        Bukkit.getScheduler().runTask((Plugin)this.D, () -> {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a78\u00a7m                                                            \u00a7r");
            Bukkit.getConsoleSender().sendMessage("\u00a76\u00a7l  \u2605 \u00a7e\u00a7lSTORMITEMY PREMIUM \u00a76\u00a7l\u2605");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a7a  \u2713 \u00a7fWersja premium aktywna!");
            Bukkit.getConsoleSender().sendMessage("\u00a7a  \u2713 \u00a7fKod: \u00a77" + this.A(this.E));
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a77  Dzi\u0119kujemy za wsparcie! \u00a7c\u2764");
            Bukkit.getConsoleSender().sendMessage("\u00a78\u00a7m                                                            \u00a7r");
            Bukkit.getConsoleSender().sendMessage("");
        });
    }

    private void C(String string) {
        Bukkit.getScheduler().runTask((Plugin)this.D, () -> {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a78\u00a7m                                                            \u00a7r");
            Bukkit.getConsoleSender().sendMessage("\u00a7c\u00a7l  \u2717 \u00a7e\u00a7lSTORMITEMY PREMIUM \u00a7c\u00a7l\u2717");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a7c  \u2717 \u00a7fNieprawid\u0142owy kod premium!");
            if (string != null && !string.isEmpty()) {
                Bukkit.getConsoleSender().sendMessage("\u00a7c  \u2717 \u00a77Pow\u00f3d: \u00a7f" + string);
            }
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("\u00a77  Uzyskaj kod na: \u00a7bhttps://stormcode.tech/panel");
            Bukkit.getConsoleSender().sendMessage("\u00a77  Sekcja: \u00a7eStrefa Premium");
            Bukkit.getConsoleSender().sendMessage("\u00a78\u00a7m                                                            \u00a7r");
            Bukkit.getConsoleSender().sendMessage("");
        });
    }

    private void D() {
    }

    private String A(String string) {
        if (string == null || string.length() < 10) {
            return "****";
        }
        return string.substring(0, 6) + "****" + string.substring(string.length() - 4);
    }

    private String A(String string, String string2) {
        try {
            int n2;
            String string3 = "\"" + string2 + "\":";
            int n3 = string.indexOf(string3);
            if (n3 == -1) {
                string3 = "\"" + string2 + "\": ";
                n3 = string.indexOf(string3);
            }
            if (n3 == -1) {
                return null;
            }
            n3 += string3.length();
            while (n3 < string.length() && string.charAt(n3) == ' ') {
                ++n3;
            }
            if (string.charAt(n3) == '\"') {
                int n4;
                if ((n4 = string.indexOf(34, ++n3)) == -1) {
                    return null;
                }
                return string.substring(n3, n4);
            }
            for (n2 = n3; n2 < string.length() && string.charAt(n2) != ',' && string.charAt(n2) != '}'; ++n2) {
            }
            return string.substring(n3, n2).trim();
        }
        catch (Exception exception) {
            return null;
        }
    }

    public boolean G() {
        return this.C && this.A;
    }

    public void A() {
        this.I();
    }
}

