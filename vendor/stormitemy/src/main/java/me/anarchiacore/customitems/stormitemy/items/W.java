/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.io.File
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package me.anarchiacore.customitems.stormitemy.items;

import java.io.File;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class W {
    private final Plugin B;
    private ConfigurationSection A;
    private final String C = "template";

    public void A() {
        try {
            File file = new File(this.B.getDataFolder(), "items/template.yml");
            if (file.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file);
                ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("template");
                if (configurationSection == null) {
                    configurationSection = yamlConfiguration;
                }
                this.A = configurationSection;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public W(Plugin plugin) {
        this.B = plugin;
    }
}

