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
import org.bukkit.potion.PotionEffectType;
import cc.dreamcode.antylogout.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringPotionEffectTypeTransformer extends BidirectionalTransformer<String, PotionEffectType>
{
    private static Map<String, PotionEffectType> byName;
    
    @Override
    public GenericsPair<String, PotionEffectType> getPair() {
        return this.genericsPair(String.class, PotionEffectType.class);
    }
    
    @Override
    public PotionEffectType leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        PotionEffectType potionEffectType = PotionEffectType.getByName(data);
        if (potionEffectType == null) {
            if (StringPotionEffectTypeTransformer.byName.isEmpty()) {
                for (final PotionEffectType p : PotionEffectType.values()) {
                    StringPotionEffectTypeTransformer.byName.put((Object)p.getName().toUpperCase(Locale.ROOT), (Object)p);
                }
            }
            potionEffectType = (PotionEffectType)StringPotionEffectTypeTransformer.byName.get((Object)data.toUpperCase(Locale.ROOT));
        }
        if (potionEffectType == null) {
            final String available = (String)Arrays.stream((Object[])PotionEffectType.values()).map(PotionEffectType::getName).collect(Collectors.joining((CharSequence)", "));
            throw new OkaeriException("Unknown potion effect type: " + data + " (Available: " + available + ")");
        }
        return potionEffectType;
    }
    
    @Override
    public String rightToLeft(@NonNull final PotionEffectType data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.getName();
    }
    
    static {
        StringPotionEffectTypeTransformer.byName = (Map<String, PotionEffectType>)new HashMap();
    }
}
