package cc.dreamcode.antylogout.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer;

import org.bukkit.Registry;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsPair;
import org.bukkit.block.Biome;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringBiomeTransformer extends BidirectionalTransformer<String, Biome>
{
    @Override
    public GenericsPair<String, Biome> getPair() {
        return this.genericsPair(String.class, Biome.class);
    }
    
    @Override
    public Biome leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final NamespacedKey key = data.contains((CharSequence)":") ? NamespacedKey.fromString(data) : NamespacedKey.minecraft(data.toLowerCase(Locale.ROOT));
        if (key == null) {
            throw new IllegalArgumentException("Invalid biome key: " + data);
        }
        try {
            return (Biome)Registry.BIOME.getOrThrow(key);
        }
        catch (final NoSuchMethodError error) {
            return Biome.valueOf(data);
        }
    }
    
    @Override
    public String rightToLeft(@NonNull final Biome data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        NamespacedKey key;
        try {
            key = data.getKeyOrThrow();
        }
        catch (final NoSuchMethodError error) {
            return data.name();
        }
        if (key.getNamespace().equals((Object)"minecraft")) {
            return key.getKey().toUpperCase(Locale.ROOT);
        }
        return String.valueOf((Object)key);
    }
}
