/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.BufferedReader
 *  java.io.ByteArrayOutputStream
 *  java.io.DataOutputStream
 *  java.io.IOException
 *  java.io.InputStreamReader
 *  java.io.OutputStream
 *  java.io.Reader
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.net.URL
 *  java.nio.charset.StandardCharsets
 *  java.util.HashSet
 *  java.util.Objects
 *  java.util.Set
 *  java.util.concurrent.ScheduledExecutorService
 *  java.util.concurrent.ScheduledThreadPoolExecutor
 *  java.util.concurrent.TimeUnit
 *  java.util.function.BiConsumer
 *  java.util.function.Consumer
 *  java.util.function.Supplier
 *  java.util.zip.GZIPOutputStream
 *  javax.net.ssl.HttpsURLConnection
 */
package me.anarchiacore.customitems.stormitemy.bstats;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import me.anarchiacore.customitems.stormitemy.bstats.charts.CustomChart;
import me.anarchiacore.customitems.stormitemy.bstats.json.JsonObjectBuilder;

public class MetricsBase {
    public static final String METRICS_VERSION = "3.0.2";
    private static final String P = "https://bStats.org/api/v2/data/%s";
    private final ScheduledExecutorService H;
    private final String D;
    private final String B;
    private final int O;
    private final Consumer<JsonObjectBuilder> A;
    private final Consumer<JsonObjectBuilder> G;
    private final Consumer<Runnable> J;
    private final Supplier<Boolean> M;
    private final BiConsumer<String, Throwable> N;
    private final Consumer<String> L;
    private final boolean I;
    private final boolean E;
    private final boolean F;
    private final Set<CustomChart> C = new HashSet();
    private final boolean K;

    public MetricsBase(String string, String string2, int n2, boolean bl, Consumer<JsonObjectBuilder> consumer, Consumer<JsonObjectBuilder> consumer2, Consumer<Runnable> consumer3, Supplier<Boolean> supplier, BiConsumer<String, Throwable> biConsumer, Consumer<String> consumer4, boolean bl2, boolean bl3, boolean bl4) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, runnable -> new Thread(runnable, "bStats-Metrics"));
        scheduledThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.H = scheduledThreadPoolExecutor;
        this.D = string;
        this.B = string2;
        this.O = n2;
        this.K = bl;
        this.A = consumer;
        this.G = consumer2;
        this.J = consumer3;
        this.M = supplier;
        this.N = biConsumer;
        this.L = consumer4;
        this.I = bl2;
        this.E = bl3;
        this.F = bl4;
        this.D();
        if (bl) {
            this.C();
        }
    }

    public void addCustomChart(CustomChart customChart) {
        this.C.add((Object)customChart);
    }

    public void shutdown() {
        this.H.shutdown();
    }

    private void C() {
        Runnable runnable = () -> {
            if (!this.K || !((Boolean)this.M.get()).booleanValue()) {
                this.H.shutdown();
                return;
            }
            if (this.J != null) {
                this.J.accept(this::A);
            } else {
                this.A();
            }
        };
        long l2 = (long)(60000.0 * (3.0 + Math.random() * 3.0));
        long l3 = (long)(60000.0 * (Math.random() * 30.0));
        this.H.schedule(runnable, l2, TimeUnit.MILLISECONDS);
        this.H.scheduleAtFixedRate(runnable, l2 + l3, 1800000L, TimeUnit.MILLISECONDS);
    }

    private void A() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        this.A.accept((Object)jsonObjectBuilder);
        JsonObjectBuilder jsonObjectBuilder2 = new JsonObjectBuilder();
        this.G.accept((Object)jsonObjectBuilder2);
        JsonObjectBuilder._A[] _AArray = (JsonObjectBuilder._A[])this.C.stream().map(customChart -> customChart.getRequestJsonObject(this.N, this.I)).filter(Objects::nonNull).toArray(JsonObjectBuilder._A[]::new);
        jsonObjectBuilder2.appendField("id", this.O);
        jsonObjectBuilder2.appendField("customCharts", _AArray);
        jsonObjectBuilder.appendField("service", jsonObjectBuilder2.build());
        jsonObjectBuilder.appendField("serverUUID", this.B);
        jsonObjectBuilder.appendField("metricsVersion", METRICS_VERSION);
        JsonObjectBuilder._A _A2 = jsonObjectBuilder.build();
        this.H.execute(() -> {
            block2: {
                try {
                    this.B(_A2);
                }
                catch (Exception exception) {
                    if (!this.I) break block2;
                    this.N.accept((Object)"Could not submit bStats metrics data", (Object)exception);
                }
            }
        });
    }

    private void B(JsonObjectBuilder._A _A2) throws Exception {
        if (this.E) {
            this.L.accept((Object)("Sent bStats metrics data: " + _A2.toString()));
        }
        String string = String.format((String)P, (Object[])new Object[]{this.D});
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(string).openConnection();
        byte[] byArray = MetricsBase.A(_A2.toString());
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.addRequestProperty("Accept", "application/json");
        httpsURLConnection.addRequestProperty("Connection", "close");
        httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
        httpsURLConnection.addRequestProperty("Content-Length", String.valueOf((int)byArray.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("User-Agent", "Metrics-Service/1");
        httpsURLConnection.setDoOutput(true);
        try (DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());){
            dataOutputStream.write(byArray);
        }
        dataOutputStream = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader((Reader)new InputStreamReader(httpsURLConnection.getInputStream()));){
            String string2;
            while ((string2 = bufferedReader.readLine()) != null) {
                dataOutputStream.append(string2);
            }
        }
        if (this.F) {
            this.L.accept((Object)("Sent data to bStats and received response: " + dataOutputStream));
        }
    }

    private void D() {
        if (System.getProperty((String)"bstats.relocatecheck") == null || !System.getProperty((String)"bstats.relocatecheck").equals((Object)"false")) {
            String string = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115});
            String string2 = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
            if (MetricsBase.class.getPackage().getName().startsWith(string) || MetricsBase.class.getPackage().getName().startsWith(string2)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }

    private static byte[] A(String string) throws IOException {
        if (string == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gZIPOutputStream = new GZIPOutputStream((OutputStream)byteArrayOutputStream);){
            gZIPOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
        }
        return byteArrayOutputStream.toByteArray();
    }
}

