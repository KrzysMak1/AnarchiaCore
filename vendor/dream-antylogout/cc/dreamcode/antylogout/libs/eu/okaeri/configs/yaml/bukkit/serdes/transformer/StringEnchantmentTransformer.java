package cc.dreamcode.antylogout.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer;

import java.util.HashMap;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.exception.OkaeriException;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Locale;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.schema.GenericsPair;
import java.util.Map;
import org.bukkit.enchantments.Enchantment;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringEnchantmentTransformer extends BidirectionalTransformer<String, Enchantment>
{
    private static Map<String, Enchantment> byName;
    
    @Override
    public GenericsPair<String, Enchantment> getPair() {
        return this.genericsPair(String.class, Enchantment.class);
    }
    
    @Override
    public Enchantment leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        Enchantment enchantment = Enchantment.getByName(data);
        if (enchantment == null) {
            if (StringEnchantmentTransformer.byName.isEmpty()) {
                for (final Enchantment e : Enchantment.values()) {
                    StringEnchantmentTransformer.byName.put((Object)e.getName().toUpperCase(Locale.ROOT), (Object)e);
                }
            }
            enchantment = (Enchantment)StringEnchantmentTransformer.byName.get((Object)data.toUpperCase(Locale.ROOT));
        }
        if (enchantment == null) {
            final String available = (String)Arrays.stream((Object[])Enchantment.values()).map(Enchantment::getName).collect(Collectors.joining((CharSequence)", "));
            throw new OkaeriException("Unknown enchantment: " + data + " (Available: " + available);
        }
        return enchantment;
    }
    
    @Override
    public String rightToLeft(@NonNull final Enchantment data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.getName();
    }
    
    static {
        StringEnchantmentTransformer.byName = (Map<String, Enchantment>)new HashMap();
    }
}
