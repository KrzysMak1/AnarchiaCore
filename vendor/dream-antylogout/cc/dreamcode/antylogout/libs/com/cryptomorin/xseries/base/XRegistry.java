package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.util.stream.Collectors;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XInfo;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XMerge;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.annotations.XChange;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import java.util.Optional;
import java.util.Collections;
import java.util.Collection;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import java.util.Locale;
import java.util.Iterator;
import org.bukkit.Keyed;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.IdentityHashMap;
import java.util.HashMap;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.particles.XParticle;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XEnchantment;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XEntityType;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XPotion;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XItemFlag;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XBiome;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XSound;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.XAttribute;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.lang.reflect.Field;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class XRegistry<XForm extends XBase<XForm, BukkitForm>, BukkitForm> implements Iterable<XForm>
{
    @ApiStatus.Internal
    private static boolean PERFORM_AUTO_ADD;
    @ApiStatus.Internal
    private static boolean DISCARD_METADATA;
    private static final boolean KEYED_EXISTS;
    private static final Map<Class<? extends XBase<?, ?>>, XRegistry<?, ?>> REGISTRIES;
    private static boolean ensureLoaded;
    private final Map<String, XForm> nameMappings;
    private final Map<BukkitForm, XForm> bukkitToX;
    private Map<XForm, XModuleMetadata> metadata;
    private Map<XForm, Field> backingFields;
    private final Class<BukkitForm> bukkitFormClass;
    private final Class<XForm> xFormClass;
    private final Supplier<Object> registrySupplier;
    private final BiFunction<BukkitForm, String[], XForm> creator;
    private final Function<Integer, XForm[]> createArray;
    private final String registryName;
    private final boolean supportsRegistry;
    private final ClassType bukkitClassType;
    private boolean pulled;
    private boolean alreadyDiscardedMetadata;
    
    private static void ensureLoadedRegistries() {
        if (XRegistry.ensureLoaded) {
            return;
        }
        XAttribute.REGISTRY.getClass();
        XSound.REGISTRY.getClass();
        XBiome.REGISTRY.getClass();
        XItemFlag.REGISTRY.getClass();
        XPotion.REGISTRY.getClass();
        XEntityType.REGISTRY.getClass();
        XEnchantment.REGISTRY.getClass();
        XParticle.REGISTRY.getClass();
        XRegistry.ensureLoaded = true;
    }
    
    @Nullable
    @ApiStatus.Experimental
    public static XRegistry<?, ?> rawRegistryOf(final Class<?> clazz) {
        ensureLoadedRegistries();
        return (XRegistry)XRegistry.REGISTRIES.get((Object)clazz);
    }
    
    @Nullable
    @ApiStatus.Experimental
    public static <XForm extends XBase<XForm, BukkitForm>, BukkitForm> XRegistry<XForm, BukkitForm> registryOf(final Class<? extends XForm> clazz) {
        ensureLoadedRegistries();
        return (XRegistry)XRegistry.REGISTRIES.get((Object)clazz);
    }
    
    protected static <XForm extends XBase<XForm, BukkitForm>, BukkitForm> void registerModule(final XRegistry<XForm, BukkitForm> registry, final Class<? extends XForm> clazz) {
        XRegistry.REGISTRIES.put((Object)clazz, (Object)registry);
    }
    
    @ApiStatus.Internal
    public XRegistry(final Class<BukkitForm> bukkitFormClass, final Class<XForm> xFormClass, final Supplier<Object> registrySupplier, final BiFunction<BukkitForm, String[], XForm> creator, final Function<Integer, XForm[]> createArray) {
        this.nameMappings = (Map<String, XForm>)new HashMap(20);
        this.bukkitToX = (Map<BukkitForm, XForm>)new IdentityHashMap(20);
        this.pulled = false;
        this.alreadyDiscardedMetadata = false;
        boolean supported;
        try {
            registrySupplier.get();
            supported = true;
        }
        catch (final Throwable ex) {
            supported = false;
        }
        this.bukkitFormClass = (Class)Objects.requireNonNull((Object)bukkitFormClass);
        this.xFormClass = (Class)Objects.requireNonNull((Object)xFormClass);
        this.registryName = this.bukkitFormClass.getSimpleName();
        this.registrySupplier = registrySupplier;
        this.createArray = (Function<Integer, XForm[]>)Objects.requireNonNull((Object)createArray);
        this.creator = creator;
        this.supportsRegistry = supported;
        if (bukkitFormClass.isEnum()) {
            this.bukkitClassType = ClassType.ENUM;
        }
        else if (Modifier.isAbstract(bukkitFormClass.getModifiers())) {
            this.bukkitClassType = ClassType.ABSTRACTION;
        }
        else {
            this.bukkitClassType = null;
        }
        if (!this.supportsRegistry && this.bukkitClassType == null) {
            throw new IllegalStateException("Bukkit form is not an enum, abstraction or a registry " + (Object)bukkitFormClass);
        }
        registerModule((XRegistry<XBase, Object>)this, xFormClass);
    }
    
    @ApiStatus.Internal
    public XRegistry(final Class<BukkitForm> bukkitFormClass, final Class<XForm> xFormClass, final Function<Integer, XForm[]> createArray) {
        this(bukkitFormClass, xFormClass, null, null, createArray);
    }
    
    @ApiStatus.Internal
    @NotNull
    public Map<String, XForm> nameMapping() {
        return this.nameMappings;
    }
    
    @ApiStatus.Internal
    @NotNull
    public Map<BukkitForm, XForm> bukkitMapping() {
        return this.bukkitToX;
    }
    
    public Class<BukkitForm> getBukkitFormClass() {
        return this.bukkitFormClass;
    }
    
    public Class<XForm> getXFormClass() {
        return this.xFormClass;
    }
    
    public String getName() {
        return this.registryName;
    }
    
    private void pullValues() {
        if (!this.pulled) {
            this.pulled = true;
            if (this.creator == null) {
                return;
            }
            this.pullFieldNames();
            if (XRegistry.PERFORM_AUTO_ADD) {
                this.pullSystemValues();
            }
        }
    }
    
    private static <T> void processEnumLikeFields(final Class<T> clazz, final BiConsumer<Field, T> consumer) {
        for (final Field field : clazz.getDeclaredFields()) {
            final int modifiers = field.getModifiers();
            if (field.getType() == clazz && Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                try {
                    consumer.accept((Object)field, field.get((Object)null));
                }
                catch (final IllegalAccessException e) {
                    throw new IllegalStateException("Cannot process enum-like fields of: " + (Object)clazz, (Throwable)e);
                }
            }
        }
    }
    
    @ApiStatus.Internal
    public void registerName(final String name, final XForm xForm) {
        this.nameMappings.put((Object)normalizeName(name), (Object)xForm);
    }
    
    private void pullFieldNames() {
        processEnumLikeFields(this.xFormClass, (java.util.function.BiConsumer<Field, XForm>)((field, x) -> this.registerMerged((XForm)x, field)));
    }
    
    private void pullSystemValues() {
        if (this.bukkitClassType == ClassType.ENUM) {
            for (final BukkitForm bukkitForm : this.bukkitFormClass.getEnumConstants()) {
                this.std(((Enum)bukkitForm).name(), bukkitForm);
            }
        }
        else {
            processEnumLikeFields(this.bukkitFormClass, (java.util.function.BiConsumer<Field, BukkitForm>)((field, bukkit) -> {
                if (bukkit == null) {
                    return;
                }
                this.std(field.getName(), bukkit);
            }));
        }
        if (this.supportsRegistry) {
            for (final Keyed bukkitForm2 : this.bukkitRegistry()) {
                this.std(bukkitForm2);
            }
        }
    }
    
    private BukkitForm valueOf(String name) {
        name = name.toUpperCase(Locale.ENGLISH).replace('.', '_');
        final Class<Enum> clazz = (Class<Enum>)this.bukkitFormClass;
        try {
            return (BukkitForm)Enum.valueOf((Class)clazz, name);
        }
        catch (final IllegalArgumentException ignored) {
            return null;
        }
    }
    
    private BukkitForm fieldOf(final String name) {
        try {
            return (BukkitForm)this.bukkitFormClass.getDeclaredField(name).get((Object)null);
        }
        catch (final IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }
    
    @NotNull
    private Registry<?> bukkitRegistry() {
        return (Registry<?>)this.registrySupplier.get();
    }
    
    @Nullable
    protected BukkitForm getBukkit(final String[] names) {
        for (String name : names) {
            BukkitForm bukkitForm;
            if (this.supportsRegistry) {
                name = name.toLowerCase(Locale.ENGLISH);
                NamespacedKey key;
                if (name.contains((CharSequence)":")) {
                    key = XNamespacedKey.fromString(name);
                }
                else {
                    key = NamespacedKey.minecraft(name);
                }
                final Keyed bukkit = this.bukkitRegistry().get(key);
                if (bukkit != null) {
                    bukkitForm = (BukkitForm)bukkit;
                }
                else {
                    bukkitForm = null;
                }
            }
            else if (this.bukkitClassType == ClassType.ENUM) {
                bukkitForm = this.valueOf(name);
            }
            else {
                if (this.bukkitClassType != ClassType.ABSTRACTION) {
                    throw new AssertionError((Object)("None of the class strategies worked for " + (Object)this));
                }
                bukkitForm = this.fieldOf(name);
            }
            if (bukkitForm != null) {
                return bukkitForm;
            }
        }
        return null;
    }
    
    @ApiStatus.Internal
    public void discardMetadata() {
        if (!XRegistry.DISCARD_METADATA) {
            return;
        }
        this.backingFields = null;
        this.metadata = null;
    }
    
    @NotNull
    public Collection<XForm> getValues() {
        this.pullValues();
        return (Collection<XForm>)Collections.unmodifiableCollection(this.bukkitToX.values());
    }
    
    @Deprecated
    public XForm[] values() {
        this.pullValues();
        final Collection<XForm> values = (Collection<XForm>)this.bukkitToX.values();
        return (XForm[])values.toArray((Object[])this.createArray.apply((Object)values.size()));
    }
    
    @NotNull
    public Iterator<XForm> iterator() {
        return (Iterator<XForm>)this.getValues().iterator();
    }
    
    @NotNull
    public XForm getByBukkitForm(final BukkitForm bukkit) {
        Objects.requireNonNull((Object)bukkit, () -> "Cannot match null " + this.registryName);
        final XForm mapping = (XForm)this.bukkitToX.get((Object)bukkit);
        if (mapping == null) {
            if (!XRegistry.PERFORM_AUTO_ADD) {
                throw new UnsupportedOperationException("Unknown standard bukkit form (no auto-add) for " + this.registryName + ": " + (Object)bukkit);
            }
            if (this.creator == null) {
                throw new UnsupportedOperationException("Unsupported value for " + this.registryName + ": " + (Object)bukkit);
            }
            final XForm xForm = this.std(bukkit);
            if (xForm == null) {
                throw new IllegalStateException("Unknown " + this.registryName + ": " + (Object)bukkit);
            }
        }
        return mapping;
    }
    
    public Optional<XForm> getByName(@NotNull final String name) {
        Objects.requireNonNull((Object)name, () -> "Cannot match null " + this.registryName);
        if (name.isEmpty()) {
            return (Optional<XForm>)Optional.empty();
        }
        this.pullValues();
        return (Optional<XForm>)Optional.ofNullable((Object)this.nameMappings.get((Object)normalizeName(name)));
    }
    
    @ApiStatus.Internal
    @NotNull
    public static String getBukkitName(@NotNull final Object bukkitForm) {
        Objects.requireNonNull(bukkitForm, "Cannot get name of a null bukkit form");
        if (bukkitForm instanceof Enum) {
            return ((Enum)bukkitForm).name();
        }
        if (XRegistry.KEYED_EXISTS && bukkitForm instanceof Keyed) {
            return ((Keyed)bukkitForm).getKey().toString();
        }
        if (bukkitForm instanceof PotionEffectType) {
            return ((PotionEffectType)bukkitForm).getName();
        }
        if (bukkitForm instanceof Enchantment) {
            return ((Enchantment)bukkitForm).getName();
        }
        throw new AssertionError((Object)("Unknown xform type: " + bukkitForm + " (" + (Object)bukkitForm.getClass() + ')'));
    }
    
    @NotNull
    private static String format(@NotNull final String name) {
        final int len = name.length();
        final char[] chs = new char[len];
        int count = 0;
        boolean appendUnderline = false;
        for (int i = 0; i < len; ++i) {
            final char ch = name.charAt(i);
            if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_') {
                appendUnderline = true;
            }
            else {
                boolean number = false;
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (number = (ch >= '0' && ch <= '9'))) {
                    if (appendUnderline) {
                        chs[count++] = '_';
                        appendUnderline = false;
                    }
                    if (number) {
                        chs[count++] = ch;
                    }
                    else {
                        chs[count++] = (char)(ch & '_');
                    }
                }
            }
        }
        return new String(chs, 0, count);
    }
    
    private static String normalizeName(String name) {
        name = name.toLowerCase(Locale.ENGLISH);
        if (name.startsWith("minecraft:")) {
            name = name.substring("minecraft:".length());
        }
        name = name.replace('.', '_');
        return name;
    }
    
    private XForm std(final BukkitForm bukkit) {
        return this.std(null, bukkit);
    }
    
    private XForm std(@Nullable final String extraFieldName, final BukkitForm bukkit) {
        XForm xForm = (XForm)this.bukkitToX.get((Object)bukkit);
        if (xForm != null) {
            return xForm;
        }
        final String name = getBukkitName(bukkit);
        if (this.getBukkit(new String[] { name }) == null && extraFieldName == null) {
            throw new IllegalArgumentException("Unknown standard bukkit form for " + this.registryName + ": " + (Object)bukkit + (bukkit.toString().equals((Object)name) ? "" : (" (" + name + ')')));
        }
        xForm = (XForm)this.creator.apply((Object)bukkit, (Object)((extraFieldName == null) ? new String[] { name } : new String[] { extraFieldName, name }));
        if (!XRegistry.PERFORM_AUTO_ADD) {
            return xForm;
        }
        this.registerName(name, xForm);
        if (extraFieldName != null) {
            this.registerName(extraFieldName, xForm);
        }
        this.bukkitToX.put((Object)bukkit, (Object)xForm);
        return xForm;
    }
    
    @ApiStatus.Internal
    public XForm std(final String[] names) {
        final BukkitForm bukkit = this.getBukkit(names);
        final XForm xForm = (XForm)this.creator.apply((Object)bukkit, (Object)names);
        return this.std(xForm);
    }
    
    @ApiStatus.Internal
    public BukkitForm stdEnum(final XForm xForm, final String[] names) {
        final String enumName = xForm.name();
        boolean merged = false;
        BukkitForm bukkit = this.getBukkit(new String[] { enumName });
        if (bukkit == null) {
            bukkit = this.getBukkit(names);
        }
        if (bukkit == null) {
            bukkit = this.registerMerged(xForm);
            merged = true;
        }
        return this.stdEnum0(xForm, names, bukkit, merged);
    }
    
    public BukkitForm stdEnum(final XForm xForm, final String[] names, final BukkitForm bukkit) {
        return this.stdEnum0(xForm, names, bukkit, false);
    }
    
    @ApiStatus.Internal
    private BukkitForm stdEnum0(final XForm xForm, final String[] names, final BukkitForm bukkit, final boolean merged) {
        final String enumName = xForm.name();
        if (!merged) {
            this.registerMerged(xForm);
        }
        this.registerName(enumName, xForm);
        for (final String name : names) {
            this.registerName(name, xForm);
        }
        if (bukkit != null) {
            this.bukkitToX.put((Object)bukkit, (Object)xForm);
        }
        return bukkit;
    }
    
    private BukkitForm registerMerged(final XForm xForm) {
        return this.registerMerged(xForm, this.getBackingField(xForm));
    }
    
    @NotNull
    @ApiStatus.Internal
    public Field getBackingField(final XForm xForm) {
        try {
            return xForm.getClass().getDeclaredField(xForm.name());
        }
        catch (final NoSuchFieldException e) {
            try {
                if (this.backingFields == null) {
                    this.cacheBackingFields();
                }
                final Field field = (Field)this.backingFields.get((Object)xForm);
                if (field != null) {
                    return field;
                }
            }
            catch (final Throwable ex) {
                final IllegalStateException newEx = new IllegalStateException("Cannot find field for XForm: " + (Object)xForm + " - " + (Object)xForm.getClass(), ex);
                newEx.addSuppressed((Throwable)e);
                throw newEx;
            }
            throw new IllegalStateException("Cannot find field for XForm: " + (Object)xForm + " - " + (Object)xForm.getClass(), (Throwable)e);
        }
    }
    
    private void cacheBackingFields() {
        if (this.backingFields != null) {
            throw new IllegalStateException("Backing fields are already cached");
        }
        if (this.alreadyDiscardedMetadata) {
            throw new IllegalStateException("Metadata have already been used and discarded");
        }
        this.backingFields = (Map<XForm, Field>)new IdentityHashMap();
        this.alreadyDiscardedMetadata = true;
        for (final Field field : this.xFormClass.getDeclaredFields()) {
            final int mods = field.getModifiers();
            if (Modifier.isPublic(mods)) {
                if (Modifier.isStatic(mods)) {
                    if (Modifier.isFinal(mods)) {
                        if (field.getType() == this.xFormClass) {
                            if (!field.isAnnotationPresent((Class)Ignore.class)) {
                                try {
                                    final Object xform = Objects.requireNonNull(field.get((Object)null), () -> "XForm backing field returned null: " + (Object)field + " for registry of " + (Object)this.xFormClass);
                                    final XForm castForm = (XForm)xform;
                                    this.backingFields.put((Object)castForm, (Object)field);
                                }
                                catch (final IllegalAccessException e) {
                                    throw new RuntimeException((Throwable)e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @ApiStatus.Internal
    public XModuleMetadata getOrRegisterMetadata(final XForm form, final Field formField, final boolean peekOnly) {
        XModuleMetadata meta = (this.metadata == null) ? null : ((XModuleMetadata)this.metadata.get((Object)form));
        if (meta != null) {
            return meta;
        }
        meta = new XModuleMetadata(formField.isAnnotationPresent((Class)Deprecated.class), (XChange[])formField.getAnnotationsByType((Class)XChange.class), (XMerge[])formField.getAnnotationsByType((Class)XMerge.class), (XInfo)formField.getAnnotation((Class)XInfo.class));
        if (!peekOnly) {
            if (this.metadata == null) {
                this.metadata = (Map<XForm, XModuleMetadata>)new IdentityHashMap(10);
            }
            this.metadata.put((Object)form, (Object)meta);
        }
        return meta;
    }
    
    private BukkitForm registerMerged(final XForm xForm, final Field formField) {
        final XMerge[] merges = this.getOrRegisterMetadata(xForm, formField, true).getMerges();
        BukkitForm mergedBukkit = null;
        for (final XMerge merge : merges) {
            mergedBukkit = this.getBukkit(new String[] { merge.name() });
            this.registerName(merge.name(), xForm);
            if (mergedBukkit != null) {
                this.bukkitToX.put((Object)mergedBukkit, (Object)xForm);
            }
        }
        return mergedBukkit;
    }
    
    @ApiStatus.Internal
    public XForm std(final Function<BukkitForm, XForm> xForm, final String[] names) {
        final BukkitForm bukkit = this.getBukkit(names);
        return this.std((XForm)xForm.apply((Object)bukkit));
    }
    
    @ApiStatus.Internal
    public XForm std(final Function<BukkitForm, XForm> xForm, final XForm tryOther, final String[] names) {
        BukkitForm bukkit = this.getBukkit(names);
        if (bukkit == null) {
            bukkit = ((XBase<XForm, BukkitForm>)tryOther).get();
        }
        return this.std((XForm)xForm.apply((Object)bukkit));
    }
    
    @ApiStatus.Internal
    public XForm std(final XForm xForm) {
        for (final String name : xForm.getNames()) {
            this.registerName(name, xForm);
        }
        if (xForm.isSupported()) {
            this.bukkitToX.put(((XBase<XForm, Object>)xForm).get(), (Object)xForm);
        }
        return xForm;
    }
    
    @Override
    public String toString() {
        return "XRegistry<" + this.registryName + ">(nameMappings=" + this.nameMappings.size() + ", bukkitToX=" + this.bukkitToX.size() + ", bukkitFormClass=" + this.bukkitFormClass.getName() + ", xFormClass=" + this.xFormClass.getName() + ", supportsRegistry=" + this.supportsRegistry + ", bukkitFormClassType=" + (Object)this.bukkitClassType + ", pulled=" + this.pulled + ", values=[" + (String)this.bukkitToX.values().stream().limit(10L).map(XBase::name).collect(Collectors.joining((CharSequence)", ")) + ']' + ')';
    }
    
    static {
        XRegistry.PERFORM_AUTO_ADD = true;
        XRegistry.DISCARD_METADATA = true;
        boolean keyedExists = false;
        try {
            Class.forName("org.bukkit.Keyed");
            keyedExists = true;
        }
        catch (final ClassNotFoundException ex) {}
        KEYED_EXISTS = keyedExists;
        REGISTRIES = (Map)new IdentityHashMap();
        XRegistry.ensureLoaded = false;
    }
    
    private enum ClassType
    {
        ENUM, 
        ABSTRACTION;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    @Documented
    @ApiStatus.Internal
    public @interface Ignore {
    }
}
