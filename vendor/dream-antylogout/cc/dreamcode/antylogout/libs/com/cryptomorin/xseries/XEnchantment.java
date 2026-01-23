package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import java.util.Collections;
import java.util.EnumSet;
import com.google.common.base.Enums;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.bukkit.NamespacedKey;
import java.util.Locale;
import org.bukkit.Registry;
import org.bukkit.enchantments.EnchantmentWrapper;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.EntityType;
import java.util.Set;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XRegistry;
import org.bukkit.enchantments.Enchantment;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;

public final class XEnchantment extends XModule<XEnchantment, Enchantment>
{
    private static final boolean ISFLAT;
    private static final boolean IS_SUPER_FLAT;
    private static final boolean USES_WRAPPER;
    public static final XRegistry<XEnchantment, Enchantment> REGISTRY;
    public static final XEnchantment AQUA_AFFINITY;
    public static final XEnchantment BANE_OF_ARTHROPODS;
    public static final XEnchantment BINDING_CURSE;
    public static final XEnchantment BLAST_PROTECTION;
    public static final XEnchantment BREACH;
    public static final XEnchantment CHANNELING;
    public static final XEnchantment DENSITY;
    public static final XEnchantment DEPTH_STRIDER;
    public static final XEnchantment EFFICIENCY;
    public static final XEnchantment FEATHER_FALLING;
    public static final XEnchantment FIRE_ASPECT;
    public static final XEnchantment FIRE_PROTECTION;
    public static final XEnchantment FLAME;
    public static final XEnchantment FORTUNE;
    public static final XEnchantment FROST_WALKER;
    public static final XEnchantment IMPALING;
    public static final XEnchantment INFINITY;
    public static final XEnchantment KNOCKBACK;
    public static final XEnchantment LOOTING;
    public static final XEnchantment LOYALTY;
    public static final XEnchantment LUCK_OF_THE_SEA;
    public static final XEnchantment LURE;
    public static final XEnchantment MENDING;
    public static final XEnchantment MULTISHOT;
    public static final XEnchantment PIERCING;
    public static final XEnchantment POWER;
    public static final XEnchantment PROJECTILE_PROTECTION;
    public static final XEnchantment PROTECTION;
    public static final XEnchantment PUNCH;
    public static final XEnchantment QUICK_CHARGE;
    public static final XEnchantment RESPIRATION;
    public static final XEnchantment RIPTIDE;
    public static final XEnchantment SHARPNESS;
    public static final XEnchantment SILK_TOUCH;
    public static final XEnchantment SMITE;
    public static final XEnchantment SOUL_SPEED;
    public static final XEnchantment SWIFT_SNEAK;
    public static final XEnchantment THORNS;
    public static final XEnchantment UNBREAKING;
    public static final XEnchantment VANISHING_CURSE;
    public static final XEnchantment WIND_BURST;
    public static final XEnchantment SWEEPING_EDGE;
    @Deprecated
    public static final XEnchantment[] VALUES;
    @Deprecated
    public static final Set<EntityType> EFFECTIVE_SMITE_ENTITIES;
    @Deprecated
    public static final Set<EntityType> EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES;
    
    private XEnchantment(final Enchantment enchantment, final String[] names) {
        super(enchantment, names);
    }
    
    @NotNull
    public static XEnchantment of(@NotNull final Enchantment enchantment) {
        return XEnchantment.REGISTRY.getByBukkitForm(enchantment);
    }
    
    public static Optional<XEnchantment> of(@NotNull final String enchantment) {
        return XEnchantment.REGISTRY.getByName(enchantment);
    }
    
    @Deprecated
    @NotNull
    public static XEnchantment[] values() {
        return XEnchantment.REGISTRY.values();
    }
    
    @NotNull
    private static XEnchantment std(@NotNull final String... names) {
        final XEnchantment std = XEnchantment.REGISTRY.std(names);
        if (XEnchantment.USES_WRAPPER && std.isSupported()) {
            final Enchantment enchantment = ((XModule<XForm, Enchantment>)std).get();
            if (enchantment instanceof EnchantmentWrapper) {
                final Enchantment wrapped = ((EnchantmentWrapper)enchantment).getEnchantment();
                XEnchantment.REGISTRY.bukkitMapping().put((Object)wrapped, (Object)std);
            }
        }
        return std;
    }
    
    @Deprecated
    private static Enchantment getBukkitEnchant(final String name) {
        if (XEnchantment.IS_SUPER_FLAT) {
            return (Enchantment)Registry.ENCHANTMENT.get(NamespacedKey.minecraft(name.toLowerCase(Locale.ENGLISH)));
        }
        if (XEnchantment.ISFLAT) {
            return Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase(Locale.ENGLISH)));
        }
        return Enchantment.getByName(name);
    }
    
    @Deprecated
    public static boolean isSmiteEffectiveAgainst(@Nullable final EntityType type) {
        return type != null && XEnchantment.EFFECTIVE_SMITE_ENTITIES.contains((Object)type);
    }
    
    @Deprecated
    public static boolean isArthropodsEffectiveAgainst(@Nullable final EntityType type) {
        return type != null && XEnchantment.EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES.contains((Object)type);
    }
    
    @Deprecated
    @NotNull
    public static Optional<XEnchantment> matchXEnchantment(@NotNull final String enchantment) {
        if (enchantment == null || enchantment.isEmpty()) {
            throw new IllegalArgumentException("Enchantment name cannot be null or empty");
        }
        return of(enchantment);
    }
    
    @Deprecated
    @NotNull
    public static XEnchantment matchXEnchantment(@NotNull final Enchantment enchantment) {
        Objects.requireNonNull((Object)enchantment, "Cannot parse XEnchantment of a null enchantment");
        return of(enchantment);
    }
    
    @NotNull
    public ItemStack getBook(final int level) {
        final ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        meta.addStoredEnchant((Enchantment)((XModule<XForm, Enchantment>)this).get(), level, true);
        book.setItemMeta((ItemMeta)meta);
        return book;
    }
    
    @Deprecated
    @Nullable
    public Enchantment getEnchant() {
        return ((XModule<XForm, Enchantment>)this).get();
    }
    
    static {
        boolean usesWrapper = false;
        boolean flat;
        try {
            final Class<?> namespacedKeyClass = Class.forName("org.bukkit.NamespacedKey");
            final Class<?> enchantmentClass = Class.forName("org.bukkit.enchantments.Enchantment");
            enchantmentClass.getDeclaredMethod("getByKey", namespacedKeyClass);
            flat = true;
        }
        catch (final ClassNotFoundException | NoSuchMethodException ex) {
            flat = false;
        }
        boolean superFlat;
        try {
            Class.forName("org.bukkit.Registry");
            superFlat = true;
        }
        catch (final ClassNotFoundException ex2) {
            superFlat = false;
        }
        for (final Field field : Enchantment.class.getDeclaredFields()) {
            final int mods = field.getModifiers();
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods) && field.getType() == Enchantment.class) {
                try {
                    final Object enchant = field.get((Object)null);
                    if (enchant instanceof EnchantmentWrapper) {
                        usesWrapper = true;
                    }
                }
                catch (final IllegalAccessException e) {
                    throw new IllegalStateException("Cannot get enchantment field for " + (Object)field, (Throwable)e);
                }
            }
        }
        ISFLAT = flat;
        IS_SUPER_FLAT = superFlat;
        USES_WRAPPER = usesWrapper;
        REGISTRY = new XRegistry<XEnchantment, Enchantment>(Enchantment.class, XEnchantment.class, (Supplier<Object>)(() -> Registry.ENCHANTMENT), (java.util.function.BiFunction<Enchantment, String[], XEnchantment>)XEnchantment::new, (java.util.function.Function<Integer, XEnchantment[]>)(x$0 -> new XEnchantment[x$0]));
        AQUA_AFFINITY = std("WATER_WORKER", "WATER_WORKER", "AQUA_AFFINITY", "WATER_MINE");
        BANE_OF_ARTHROPODS = std("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS", "BANE_OF_ARTHROPOD", "ARTHROPOD");
        BINDING_CURSE = std("BINDING_CURSE", "BIND_CURSE", "BINDING", "BIND");
        BLAST_PROTECTION = std("PROTECTION_EXPLOSIONS", "BLAST_PROTECT", "EXPLOSIONS_PROTECTION", "EXPLOSION_PROTECTION", "BLAST_PROTECTION");
        BREACH = std("BREACH");
        CHANNELING = std("CHANNELING", "CHANNELLING", "CHANELLING", "CHANELING", "CHANNEL");
        DENSITY = std("DENSITY");
        DEPTH_STRIDER = std("DEPTH_STRIDER", "DEPTH", "STRIDER");
        EFFICIENCY = std("EFFICIENCY", "DIG_SPEED", "MINE_SPEED", "CUT_SPEED");
        FEATHER_FALLING = std("PROTECTION_FALL", "FEATHER_FALL", "FALL_PROTECTION", "FEATHER_FALLING");
        FIRE_ASPECT = std("FIRE_ASPECT", "FIRE", "MELEE_FIRE", "MELEE_FLAME");
        FIRE_PROTECTION = std("PROTECTION_FIRE", "FIRE_PROT", "FIRE_PROTECT", "FIRE_PROTECTION", "FLAME_PROTECTION", "FLAME_PROTECT");
        FLAME = std("FLAME", "ARROW_FIRE", "FLAME_ARROW", "FIRE_ARROW");
        FORTUNE = std("FORTUNE", "LOOT_BONUS_BLOCKS", "BLOCKS_LOOT_BONUS");
        FROST_WALKER = std("FROST_WALKER", "FROST", "WALKER");
        IMPALING = std("IMPALING", "IMPALE", "OCEAN_DAMAGE");
        INFINITY = std("INFINITY", "ARROW_INFINITE", "INFINITE_ARROWS", "INFINITE", "UNLIMITED_ARROWS");
        KNOCKBACK = std("KNOCKBACK");
        LOOTING = std("LOOTING", "LOOT_BONUS_MOBS", "MOB_LOOT", "MOBS_LOOT_BONUS");
        LOYALTY = std("LOYALTY", "LOYAL", "RETURN");
        LUCK_OF_THE_SEA = std("LUCK_OF_THE_SEA", "LUCK", "LUCK_OF_SEA", "LUCK_OF_SEAS", "ROD_LUCK");
        LURE = std("LURE", "ROD_LURE");
        MENDING = std("MENDING");
        MULTISHOT = std("MULTISHOT", "TRIPLE_SHOT");
        PIERCING = std("PIERCING");
        POWER = std("POWER", "ARROW_DAMAGE", "ARROW_POWER");
        PROJECTILE_PROTECTION = std("PROTECTION_PROJECTILE", "PROJECTILE_PROTECTION");
        PROTECTION = std("PROTECTION", "PROTECTION_ENVIRONMENTAL", "PROTECT");
        PUNCH = std("PUNCH", "ARROW_KNOCKBACK", "ARROW_PUNCH");
        QUICK_CHARGE = std("QUICK_CHARGE", "QUICKCHARGE", "QUICK_DRAW", "FAST_CHARGE", "FAST_DRAW");
        RESPIRATION = std("RESPIRATION", "OXYGEN", "BREATH", "BREATHING");
        RIPTIDE = std("RIPTIDE", "RIP", "TIDE", "LAUNCH");
        SHARPNESS = std("SHARPNESS", "DAMAGE_ALL", "ALL_DAMAGE", "ALL_DMG", "SHARP");
        SILK_TOUCH = std("SILK_TOUCH", "SOFT_TOUCH");
        SMITE = std("SMITE", "DAMAGE_UNDEAD", "UNDEAD_DAMAGE");
        SOUL_SPEED = std("SOUL_SPEED", "SPEED_SOUL", "SOUL_RUNNER");
        SWIFT_SNEAK = std("SWIFT_SNEAK", "SNEAK_SWIFT");
        THORNS = std("THORNS", "HIGHCRIT", "THORN", "HIGHERCRIT");
        UNBREAKING = std("UNBREAKING", "DURABILITY", "DURA");
        VANISHING_CURSE = std("VANISHING_CURSE", "VANISH_CURSE", "VANISHING", "VANISH");
        WIND_BURST = std("WIND_BURST");
        SWEEPING_EDGE = std("SWEEPING", "SWEEPING_EDGE", "SWEEP_EDGE");
        VALUES = values();
        final EntityType bee = (EntityType)Enums.getIfPresent((Class)EntityType.class, "BEE").orNull();
        final EntityType phantom = (EntityType)Enums.getIfPresent((Class)EntityType.class, "PHANTOM").orNull();
        final EntityType drowned = (EntityType)Enums.getIfPresent((Class)EntityType.class, "DROWNED").orNull();
        final EntityType witherSkeleton = (EntityType)Enums.getIfPresent((Class)EntityType.class, "WITHER_SKELETON").orNull();
        final EntityType skeletonHorse = (EntityType)Enums.getIfPresent((Class)EntityType.class, "SKELETON_HORSE").orNull();
        final EntityType stray = (EntityType)Enums.getIfPresent((Class)EntityType.class, "STRAY").orNull();
        final EntityType husk = (EntityType)Enums.getIfPresent((Class)EntityType.class, "HUSK").orNull();
        final Set<EntityType> arthorposEffective = (Set<EntityType>)EnumSet.of((Enum)EntityType.SPIDER, (Enum)EntityType.CAVE_SPIDER, (Enum)EntityType.SILVERFISH, (Enum)EntityType.ENDERMITE);
        if (bee != null) {
            arthorposEffective.add((Object)bee);
        }
        EFFECTIVE_BANE_OF_ARTHROPODS_ENTITIES = Collections.unmodifiableSet((Set)arthorposEffective);
        final Set<EntityType> smiteEffective = (Set<EntityType>)EnumSet.of((Enum)EntityType.ZOMBIE, (Enum)EntityType.SKELETON, (Enum)EntityType.WITHER);
        if (phantom != null) {
            smiteEffective.add((Object)phantom);
        }
        if (drowned != null) {
            smiteEffective.add((Object)drowned);
        }
        if (witherSkeleton != null) {
            smiteEffective.add((Object)witherSkeleton);
        }
        if (skeletonHorse != null) {
            smiteEffective.add((Object)skeletonHorse);
        }
        if (stray != null) {
            smiteEffective.add((Object)stray);
        }
        if (husk != null) {
            smiteEffective.add((Object)husk);
        }
        EFFECTIVE_SMITE_ENTITIES = Collections.unmodifiableSet((Set)smiteEffective);
        if (XEnchantment.USES_WRAPPER) {
            for (final Field field2 : Enchantment.class.getDeclaredFields()) {
                final int mods2 = field2.getModifiers();
                if (Modifier.isPublic(mods2) && Modifier.isStatic(mods2) && Modifier.isFinal(mods2) && field2.getType() == Enchantment.class) {
                    try {
                        final Object enchant2 = field2.get((Object)null);
                        if (enchant2 instanceof EnchantmentWrapper) {
                            final EnchantmentWrapper wrapper = (EnchantmentWrapper)enchant2;
                            final XEnchantment mainMapping = (XEnchantment)XEnchantment.REGISTRY.bukkitMapping().get((Object)wrapper.getEnchantment());
                            Objects.requireNonNull((Object)mainMapping, () -> "No main mapping found for Enchantment." + field2.getName() + " (" + (Object)wrapper + ')');
                            XEnchantment.REGISTRY.bukkitMapping().put((Object)wrapper, (Object)mainMapping);
                        }
                    }
                    catch (final IllegalAccessException e2) {
                        throw new IllegalStateException("Cannot get direct enchantment field for " + (Object)field2, (Throwable)e2);
                    }
                }
            }
        }
        XEnchantment.REGISTRY.discardMetadata();
    }
}
