/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.io.IOException
 *  java.lang.Boolean
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.Runtime
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.lang.reflect.Method
 *  java.util.Collection
 *  java.util.UUID
 *  java.util.function.BiConsumer
 *  java.util.function.Consumer
 *  java.util.function.Supplier
 *  java.util.logging.Level
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.bstats.bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.Plugin;
import me.anarchiacore.customitems.stormitemy.bstats.MetricsBase;
import me.anarchiacore.customitems.stormitemy.bstats.charts.CustomChart;
import me.anarchiacore.customitems.stormitemy.bstats.json.JsonObjectBuilder;

public class Metrics {
    private final Plugin A;
    private final MetricsBase B;

    public Metrics(Plugin javaPlugin, int n2) {
        this.A = javaPlugin;
        File file = new File(javaPlugin.getDataFolder().getParentFile(), "bStats");
        File file2 = new File(file, "config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
        if (!yamlConfiguration.isSet("serverUuid")) {
            yamlConfiguration.addDefault("enabled", (Object)true);
            yamlConfiguration.addDefault("serverUuid", (Object)UUID.randomUUID().toString());
            yamlConfiguration.addDefault("logFailedRequests", (Object)false);
            yamlConfiguration.addDefault("logSentData", (Object)false);
            yamlConfiguration.addDefault("logResponseStatusText", (Object)false);
            yamlConfiguration.options().header("bStats (https://bStats.org) collects some basic information for plugin authors, like how\nmany people use their plugin and their total player count. It's recommended to keep bStats\nenabled, but if you're not comfortable with this, you can turn this setting off. There is no\nperformance penalty associated with having metrics enabled, and data sent to bStats is fully\nanonymous.").copyDefaults(true);
            try {
                yamlConfiguration.save(file2);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        boolean bl = yamlConfiguration.getBoolean("enabled", true);
        String string2 = yamlConfiguration.getString("serverUuid");
        boolean bl2 = yamlConfiguration.getBoolean("logFailedRequests", false);
        boolean bl3 = yamlConfiguration.getBoolean("logSentData", false);
        boolean bl4 = yamlConfiguration.getBoolean("logResponseStatusText", false);
        this.B = new MetricsBase("bukkit", string2, n2, bl, (Consumer<JsonObjectBuilder>)((Consumer)this::B), (Consumer<JsonObjectBuilder>)((Consumer)this::A), (Consumer<Runnable>)((Consumer)runnable -> Bukkit.getScheduler().runTask((Plugin)javaPlugin, runnable)), (Supplier<Boolean>)((Supplier)() -> ((Plugin)javaPlugin).isEnabled()), (BiConsumer<String, Throwable>)((BiConsumer)(string, throwable) -> this.A.getLogger().log(Level.WARNING, string, throwable)), (Consumer<String>)((Consumer)string -> this.A.getLogger().log(Level.INFO, string)), bl2, bl3, bl4);
    }

    public void shutdown() {
        this.B.shutdown();
    }

    public void addCustomChart(CustomChart customChart) {
        this.B.addCustomChart(customChart);
    }

    private void B(JsonObjectBuilder jsonObjectBuilder) {
        jsonObjectBuilder.appendField("playerAmount", this.A());
        jsonObjectBuilder.appendField("onlineMode", Bukkit.getOnlineMode() ? 1 : 0);
        jsonObjectBuilder.appendField("bukkitVersion", Bukkit.getVersion());
        jsonObjectBuilder.appendField("bukkitName", Bukkit.getName());
        jsonObjectBuilder.appendField("javaVersion", System.getProperty((String)"java.version"));
        jsonObjectBuilder.appendField("osName", System.getProperty((String)"os.name"));
        jsonObjectBuilder.appendField("osArch", System.getProperty((String)"os.arch"));
        jsonObjectBuilder.appendField("osVersion", System.getProperty((String)"os.version"));
        jsonObjectBuilder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void A(JsonObjectBuilder jsonObjectBuilder) {
        jsonObjectBuilder.appendField("pluginVersion", this.A.getDescription().getVersion());
    }

    private int A() {
        try {
            Method method = Class.forName((String)"org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
            return method.getReturnType().equals(Collection.class) ? ((Collection)method.invoke((Object)Bukkit.getServer(), new Object[0])).size() : ((Player[])method.invoke((Object)Bukkit.getServer(), new Object[0])).length;
        }
        catch (Exception exception) {
            return Bukkit.getOnlinePlayers().size();
        }
    }
}

