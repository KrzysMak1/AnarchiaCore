package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.EnumMap;
import org.bukkit.Registry;
import java.util.Map;
import java.util.Collections;
import java.util.EnumSet;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.Color;
import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.bukkit.potion.PotionType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import java.util.Set;
import org.bukkit.potion.PotionEffectType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XBase;

public enum XPotion implements XBase<XPotion, PotionEffectType>
{
    ABSORPTION(new String[] { "ABSORB" }), 
    BAD_OMEN(new String[] { "OMEN_BAD", "PILLAGER" }), 
    BLINDNESS(new String[] { "BLIND" }), 
    CONDUIT_POWER(new String[] { "CONDUIT", "POWER_CONDUIT" }), 
    DARKNESS(new String[0]), 
    DOLPHINS_GRACE(new String[] { "DOLPHIN", "GRACE" }), 
    FIRE_RESISTANCE(new String[] { "FIRE_RESIST", "RESIST_FIRE", "FIRE_RESISTANCE" }), 
    GLOWING(new String[] { "GLOW", "SHINE", "SHINY" }), 
    HASTE(new String[] { "FAST_DIGGING", "SUPER_PICK", "DIGFAST", "DIG_SPEED", "QUICK_MINE", "SHARP" }), 
    HEALTH_BOOST(new String[] { "BOOST_HEALTH", "BOOST", "HP" }), 
    HERO_OF_THE_VILLAGE(new String[] { "HERO", "VILLAGE_HERO" }), 
    HUNGER(new String[] { "STARVE", "HUNGRY" }), 
    INFESTED(new String[0]), 
    INSTANT_DAMAGE(new String[] { "INJURE", "DAMAGE", "HARMING", "INFLICT", "HARM" }), 
    INSTANT_HEALTH(new String[] { "HEALTH", "INSTA_HEAL", "INSTANT_HEAL", "INSTA_HEALTH", "HEAL", "HEALING" }), 
    INVISIBILITY(new String[] { "INVISIBLE", "VANISH", "INVIS", "DISAPPEAR", "HIDE" }), 
    JUMP_BOOST(new String[] { "LEAP", "LEAPING", "JUMP" }), 
    LEVITATION(new String[] { "LEVITATE" }), 
    LUCK(new String[] { "LUCKY" }), 
    MINING_FATIGUE(new String[] { "SLOW_DIGGING", "FATIGUE", "DULL", "DIGGING", "SLOW_DIG", "DIG_SLOW" }), 
    NAUSEA(new String[] { "CONFUSION", "SICKNESS", "SICK" }), 
    NIGHT_VISION(new String[] { "VISION", "VISION_NIGHT" }), 
    OOZING(new String[0]), 
    POISON(new String[] { "VENOM" }), 
    RAID_OMEN(new String[0]), 
    REGENERATION(new String[] { "REGEN" }), 
    RESISTANCE(new String[] { "DAMAGE_RESISTANCE", "ARMOR", "DMG_RESIST", "DMG_RESISTANCE" }), 
    SATURATION(new String[] { "FOOD" }), 
    SLOWNESS(new String[] { "SLOW", "SLUGGISH" }), 
    SLOW_FALLING(new String[] { "SLOW_FALL", "FALL_SLOW" }), 
    SPEED(new String[] { "SPRINT", "RUNFAST", "SWIFT", "SWIFTNESS", "FAST" }), 
    STRENGTH(new String[] { "INCREASE_DAMAGE", "BULL", "STRONG", "ATTACK" }), 
    TRIAL_OMEN(new String[0]), 
    UNLUCK(new String[] { "UNLUCKY" }), 
    WATER_BREATHING(new String[] { "WATER_BREATH", "UNDERWATER_BREATHING", "UNDERWATER_BREATH", "AIR" }), 
    WEAKNESS(new String[] { "WEAK" }), 
    WEAVING(new String[0]), 
    WIND_CHARGED(new String[0]), 
    WITHER(new String[] { "DECAY" });
    
    public static final XPotion[] VALUES;
    @Deprecated
    public static final Set<XPotion> DEBUFFS;
    private static final XPotion[] POTIONEFFECTTYPE_MAPPING;
    public static final XRegistry<XPotion, PotionEffectType> REGISTRY;
    private final PotionEffectType potionEffectType;
    private final PotionType potionType;
    
    private XPotion(final String[] aliases) {
        PotionEffectType tempType = PotionEffectType.getByName(this.name());
        for (final String legacy : aliases) {
            if (tempType == null) {
                tempType = PotionEffectType.getByName(legacy);
            }
        }
        if (this.name().equals((Object)"TURTLE_MASTER")) {
            tempType = findSlowness();
        }
        this.potionEffectType = tempType;
        this.potionType = PotionType.getByEffect(this.potionEffectType);
        Data.REGISTRY.stdEnum(this, aliases, this.potionEffectType);
        if (this.potionType != null) {
            final String basePotionType = this.potionType.name();
            final String strongPotionType = "STRONG_" + basePotionType;
            final String longPotionType = "LONG_" + basePotionType;
            Data.POTION_TYPE_MAPPING.put((Object)this.potionType, (Object)this);
            try {
                Data.POTION_TYPE_MAPPING.put((Object)PotionType.valueOf(strongPotionType), (Object)this);
                Data.REGISTRY.registerName(strongPotionType, this);
            }
            catch (final IllegalArgumentException ex) {}
            try {
                Data.POTION_TYPE_MAPPING.put((Object)PotionType.valueOf(longPotionType), (Object)this);
                Data.REGISTRY.registerName(longPotionType, this);
            }
            catch (final IllegalArgumentException ex2) {}
        }
    }
    
    private static PotionEffectType findSlowness() {
        return (PotionEffectType)Stream.of((Object[])new String[] { "SLOWNESS", "SLOW", "SLUGGISH" }).map(PotionEffectType::getByName).filter(Objects::nonNull).findFirst().orElseThrow(() -> new IllegalStateException("Cannot find slowness potion type"));
    }
    
    @Deprecated
    @NotNull
    public static Optional<XPotion> matchXPotion(@NotNull final String potion) {
        return of(potion);
    }
    
    @NotNull
    public static XPotion of(@NotNull final PotionType potion) {
        return (XPotion)Data.POTION_TYPE_MAPPING.get((Object)potion);
    }
    
    public static Optional<XPotion> of(@NotNull final String potion) {
        if (potion == null || potion.isEmpty()) {
            throw new IllegalArgumentException("Cannot match XPotion of a null or empty potion effect type");
        }
        final PotionEffectType idType = fromId(potion);
        if (idType == null) {
            return XPotion.REGISTRY.getByName(potion);
        }
        final Optional<XPotion> type = XPotion.REGISTRY.getByName(idType.getName());
        if (!type.isPresent()) {
            throw new UnsupportedOperationException("Unsupported potion effect type ID: " + (Object)idType);
        }
        return type;
    }
    
    @Deprecated
    public static XPotion matchXPotion(@NotNull final PotionType type) {
        return of(type);
    }
    
    @Deprecated
    @NotNull
    public static XPotion matchXPotion(@NotNull final PotionEffectType type) {
        return of(type);
    }
    
    @NotNull
    public static XPotion of(@NotNull final PotionEffectType type) {
        Objects.requireNonNull((Object)type, "Cannot match XPotion of a null potion effect type");
        return XPotion.REGISTRY.getByBukkitForm(type);
    }
    
    @Nullable
    private static PotionEffectType fromId(@NotNull final String type) {
        try {
            final int id = Integer.parseInt(type);
            return PotionEffectType.getById(id);
        }
        catch (final NumberFormatException ex) {
            return null;
        }
    }
    
    private static List<String> split(@NotNull final String str, final char separatorChar) {
        final List<String> list = (List<String>)new ArrayList(5);
        boolean match = false;
        boolean lastMatch = false;
        final int len = str.length();
        int start = 0;
        for (int i = 0; i < len; ++i) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add((Object)str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = i + 1;
            }
            else {
                lastMatch = false;
                match = true;
            }
        }
        if (match || lastMatch) {
            list.add((Object)str.substring(start, len));
        }
        return list;
    }
    
    @Nullable
    public static Effect parseEffect(@Nullable final String potion) {
        if (Strings.isNullOrEmpty(potion) || potion.equalsIgnoreCase("none")) {
            return null;
        }
        List<String> split = split(potion.replace((CharSequence)" ", (CharSequence)""), ',');
        if (split.isEmpty()) {
            split = split(potion, ' ');
        }
        double chance = 100.0;
        int chanceIndex = 0;
        if (split.size() > 2) {
            chanceIndex = ((String)split.get(2)).indexOf(37);
            if (chanceIndex != -1) {
                try {
                    chance = Double.parseDouble(((String)split.get(2)).substring(chanceIndex + 1));
                }
                catch (final NumberFormatException ex) {}
            }
        }
        final Optional<XPotion> typeOpt = of((String)split.get(0));
        if (!typeOpt.isPresent()) {
            return null;
        }
        final PotionEffectType type = ((XPotion)typeOpt.get()).potionEffectType;
        if (type == null) {
            return null;
        }
        int duration = 2400;
        int amplifier = 0;
        if (split.size() > 1) {
            duration = toInt((String)split.get(1), 1) * 20;
            if (split.size() > 2) {
                amplifier = toInt((chanceIndex <= 0) ? ((String)split.get(2)) : ((String)split.get(2)).substring(0, chanceIndex), 1) - 1;
            }
        }
        return new Effect(new PotionEffect(type, duration, amplifier), chance);
    }
    
    private static int toInt(final String str, final int defaultValue) {
        try {
            return Integer.parseInt(str);
        }
        catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }
    
    public static void addEffects(@NotNull final LivingEntity entity, @Nullable final List<String> effects) {
        Objects.requireNonNull((Object)entity, "Cannot add potion effects to null entity");
        for (final Effect effect : parseEffects(effects)) {
            effect.apply(entity);
        }
    }
    
    public static List<Effect> parseEffects(@Nullable final List<String> effectsString) {
        if (effectsString == null || effectsString.isEmpty()) {
            return (List<Effect>)new ArrayList();
        }
        final List<Effect> effects = (List<Effect>)new ArrayList(effectsString.size());
        for (final String effectStr : effectsString) {
            final Effect effect = parseEffect(effectStr);
            if (effect != null) {
                effects.add((Object)effect);
            }
        }
        return effects;
    }
    
    @NotNull
    public static ThrownPotion throwPotion(@NotNull final LivingEntity entity, @Nullable final Color color, @Nullable final PotionEffect... effects) {
        Objects.requireNonNull((Object)entity, "Cannot throw potion from null entity");
        final ItemStack potion = (Material.getMaterial("SPLASH_POTION") == null) ? new ItemStack(Material.POTION, 1, (short)16398) : new ItemStack(Material.SPLASH_POTION);
        final PotionMeta meta = (PotionMeta)potion.getItemMeta();
        meta.setColor(color);
        if (effects != null) {
            for (final PotionEffect effect : effects) {
                meta.addCustomEffect(effect, true);
            }
        }
        potion.setItemMeta((ItemMeta)meta);
        final ThrownPotion thrownPotion = (ThrownPotion)entity.launchProjectile((Class)ThrownPotion.class);
        thrownPotion.setItem(potion);
        return thrownPotion;
    }
    
    @NotNull
    public static ItemStack buildItemWithEffects(@NotNull final Material type, @Nullable final Color color, @Nullable final PotionEffect... effects) {
        Objects.requireNonNull((Object)type, "Cannot build an effected item with null type");
        if (!canHaveEffects(type)) {
            throw new IllegalArgumentException("Cannot build item with " + type.name() + " potion type");
        }
        final ItemStack item = new ItemStack(type);
        final PotionMeta meta = (PotionMeta)item.getItemMeta();
        meta.setColor(color);
        meta.setDisplayName((type == Material.POTION) ? "Potion" : ((type == Material.SPLASH_POTION) ? "Splash Potion" : ((type == Material.TIPPED_ARROW) ? "Tipped Arrow" : "Lingering Potion")));
        if (effects != null) {
            for (final PotionEffect effect : effects) {
                meta.addCustomEffect(effect, true);
            }
        }
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
    
    public static boolean canHaveEffects(@Nullable final Material material) {
        return material != null && (material.name().endsWith("POTION") || material.name().startsWith("TIPPED_ARROW"));
    }
    
    @Nullable
    public PotionEffectType getPotionEffectType() {
        return this.potionEffectType;
    }
    
    public String[] getNames() {
        return new String[] { this.name() };
    }
    
    @Nullable
    public PotionEffectType get() {
        return this.potionEffectType;
    }
    
    @Nullable
    public PotionType getPotionType() {
        return this.potionType;
    }
    
    @Nullable
    public PotionEffect buildPotionEffect(final int duration, final int amplifier) {
        return (this.potionEffectType == null) ? null : new PotionEffect(this.potionEffectType, duration, amplifier - 1);
    }
    
    public String toString() {
        return this.friendlyName();
    }
    
    static {
        VALUES = values();
        DEBUFFS = Collections.unmodifiableSet((Set)EnumSet.of((Enum)XPotion.BAD_OMEN, (Enum[])new XPotion[] { XPotion.BLINDNESS, XPotion.NAUSEA, XPotion.INSTANT_DAMAGE, XPotion.HUNGER, XPotion.LEVITATION, XPotion.POISON, XPotion.SLOWNESS, XPotion.MINING_FATIGUE, XPotion.UNLUCK, XPotion.WEAKNESS, XPotion.WITHER }));
        POTIONEFFECTTYPE_MAPPING = new XPotion[XPotion.VALUES.length + 1];
        for (final XPotion pot : XPotion.VALUES) {
            if (pot.potionEffectType != null) {
                XPotion.POTIONEFFECTTYPE_MAPPING[pot.potionEffectType.getId()] = pot;
            }
        }
        (REGISTRY = Data.REGISTRY).discardMetadata();
    }
    
    private static final class Data
    {
        private static final Map<PotionType, XPotion> POTION_TYPE_MAPPING;
        private static final XRegistry<XPotion, PotionEffectType> REGISTRY;
        
        static {
            POTION_TYPE_MAPPING = (Map)new EnumMap((Class)PotionType.class);
            REGISTRY = new XRegistry<XPotion, PotionEffectType>(PotionEffectType.class, XPotion.class, (Supplier<Object>)(() -> Registry.EFFECT), null, (java.util.function.Function<Integer, XPotion[]>)(x$0 -> new XPotion[x$0]));
        }
    }
    
    public static class Effect
    {
        private PotionEffect effect;
        private double chance;
        
        public Effect(final PotionEffect effect, final double chance) {
            this.effect = effect;
            this.chance = chance;
        }
        
        public XPotion getXPotion() {
            return XPotion.of(this.effect.getType());
        }
        
        public double getChance() {
            return this.chance;
        }
        
        public boolean hasChance() {
            return this.chance >= 100.0 || ThreadLocalRandom.current().nextDouble(0.0, 100.0) <= this.chance;
        }
        
        public void setChance(final double chance) {
            this.chance = chance;
        }
        
        public void apply(final LivingEntity entity) {
            if (this.hasChance()) {
                entity.addPotionEffect(this.effect);
            }
        }
        
        public PotionEffect getEffect() {
            return this.effect;
        }
        
        public void setEffect(final PotionEffect effect) {
            this.effect = effect;
        }
    }
}
