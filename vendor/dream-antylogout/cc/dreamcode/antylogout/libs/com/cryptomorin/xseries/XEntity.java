package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XModule;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.base.XBase;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.StaticClassHandle;
import java.util.Collections;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.jvm.classes.ClassHandle;
import java.util.HashMap;
import java.util.function.Consumer;
import org.bukkit.World;
import java.util.EnumSet;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Strider;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Husk;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Raider;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Creeper;
import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.Boss;
import org.bukkit.ChatColor;
import java.util.Optional;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.bukkit.NamespacedKey;
import com.google.common.base.Enums;
import java.util.Locale;
import org.bukkit.Registry;
import com.google.common.base.Strings;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.reflection.XReflection;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import java.util.function.BiConsumer;
import java.util.Map;
import org.bukkit.entity.EntityType;
import java.util.Set;

public final class XEntity
{
    public static final Set<EntityType> UNDEAD;
    private static final boolean SUPPORTS_DELAYED_SPAWN;
    private static final Object REGISTRY_CAT_VARIANT;
    private static Object REGISTRY_DEFAULT_CAT_VARIANT;
    private static final Map<Class<?>, BiConsumer<Entity, ConfigurationSection>> MAPPING;
    private static final boolean SUPPORTS_Villager_setVillagerLevel;
    private static final boolean SUPPORTS_Villager_setVillagerExperience;
    private static final boolean SUPPORTS_Villager_setVillagerType;
    
    private static <T extends Entity> void register(final Class<T> entityType, final BiConsumer<T, ConfigurationSection> handler) {
        XEntity.MAPPING.put((Object)entityType, (Object)cast(handler));
    }
    
    private static void mapObjectToConfig(final Class<? extends Entity> entityClass) {
        final List<MappedConfigObject> mappedConfigObjects = (List<MappedConfigObject>)new ArrayList();
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        for (final Method method : entityClass.getDeclaredMethods()) {
            final String name = method.getName();
            if (name.startsWith("set")) {
                String configEntry = name.substring(3).replaceAll("[A-Z]", "-");
                if (configEntry.startsWith("-")) {
                    configEntry = name.charAt(3) + configEntry.substring(1);
                }
                MethodHandle setter;
                try {
                    setter = lookup.unreflect(method);
                }
                catch (final IllegalAccessException e) {
                    throw new IllegalStateException((Throwable)e);
                }
                mappedConfigObjects.add((Object)new MappedConfigObject(configEntry, setter, (Function)null));
            }
        }
    }
    
    private XEntity() {
    }
    
    private static Object supportsRegistry(final String name) {
        try {
            final Class<?> registryClass = XReflection.ofMinecraft().inPackage("org.bukkit").named("Registry").reflect();
            return XReflection.of(registryClass).field().asStatic().getter().named(name).returns(registryClass).reflect().invoke();
        }
        catch (final Throwable ex) {
            return null;
        }
    }
    
    private static <T> T getRegistryOrEnum(final Class<T> typeClass, final Object registry, final String name, final Object defaultValue) {
        if (Strings.isNullOrEmpty(name)) {
            return (T)defaultValue;
        }
        T type;
        if (registry != null) {
            type = cast(((Registry)registry).get(fromConfig(name)));
        }
        else {
            type = cast(Enums.getIfPresent((Class)cast(typeClass), name.toUpperCase(Locale.ENGLISH)).orNull());
        }
        if (type == null) {
            return (T)defaultValue;
        }
        return type;
    }
    
    private static <T> T cast(final Object something) {
        return (T)something;
    }
    
    private static NamespacedKey fromConfig(final String name) {
        NamespacedKey namespacedKey;
        if (!name.contains((CharSequence)":")) {
            namespacedKey = NamespacedKey.minecraft(name.toLowerCase(Locale.ENGLISH));
        }
        else {
            namespacedKey = NamespacedKey.fromString(name.toLowerCase(Locale.ENGLISH));
        }
        return (NamespacedKey)Objects.requireNonNull((Object)namespacedKey, () -> "Invalid namespace key: " + name);
    }
    
    @NotNull
    private static Cat.Type getCatVariant(@Nullable final String name) {
        if (XEntity.REGISTRY_DEFAULT_CAT_VARIANT == null) {
            XEntity.REGISTRY_DEFAULT_CAT_VARIANT = getRegistryOrEnum(Cat.Type.class, XEntity.REGISTRY_CAT_VARIANT, "TABBY", null);
        }
        return getRegistryOrEnum(Cat.Type.class, XEntity.REGISTRY_CAT_VARIANT, name, XEntity.REGISTRY_DEFAULT_CAT_VARIANT);
    }
    
    public static boolean isUndead(@Nullable final EntityType type) {
        return type != null && XEntity.UNDEAD.contains((Object)type);
    }
    
    @Nullable
    public static Entity spawn(@NotNull final Location location, @NotNull final ConfigurationSection config) {
        Objects.requireNonNull((Object)location, "Cannot spawn entity at a null location.");
        Objects.requireNonNull((Object)config, "Cannot spawn entity from a null configuration section");
        final String typeStr = config.getString("type");
        if (typeStr == null) {
            return null;
        }
        final Optional<XEntityType> type = XEntityType.of(typeStr);
        if (!type.isPresent()) {
            return null;
        }
        final XEntityType finalType = ((XBase<XEntityType, BukkitForm>)type.get()).or(XEntityType.ZOMBIE);
        if (!finalType.isSupported()) {
            return null;
        }
        if (XEntity.SUPPORTS_DELAYED_SPAWN) {
            return location.getWorld().spawn(location, finalType.get().getEntityClass(), false, entity -> edit(entity, config));
        }
        return edit(location.getWorld().spawnEntity(location, finalType.get()), config);
    }
    
    private static void map(final Class<?> target, final Entity entity, final ConfigurationSection config) {
        if (target == Entity.class) {
            return;
        }
        final BiConsumer<Entity, ConfigurationSection> mapping = (BiConsumer<Entity, ConfigurationSection>)XEntity.MAPPING.get((Object)target);
        if (mapping != null) {
            mapping.accept((Object)entity, (Object)config);
        }
        final Class<?> superclass = target.getSuperclass();
        if (superclass != null) {
            map(superclass, entity, config);
        }
        for (final Class<?> interf : target.getInterfaces()) {
            map(interf, entity, config);
        }
    }
    
    @NotNull
    public static Entity edit(@NotNull final Entity entity, @NotNull final ConfigurationSection config) {
        Objects.requireNonNull((Object)entity, "Cannot edit properties of a null entity");
        Objects.requireNonNull((Object)config, "Cannot edit an entity from a null configuration section");
        final String name = config.getString("name");
        if (name != null) {
            entity.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
            entity.setCustomNameVisible(true);
        }
        if (config.isSet("glowing")) {
            entity.setGlowing(config.getBoolean("glowing"));
        }
        if (config.isSet("gravity")) {
            entity.setGravity(config.getBoolean("gravity"));
        }
        if (config.isSet("silent")) {
            entity.setSilent(config.getBoolean("silent"));
        }
        entity.setFireTicks(config.getInt("fire-ticks"));
        entity.setFallDistance((float)config.getInt("fall-distance"));
        if (config.isSet("invulnerable")) {
            entity.setInvulnerable(config.getBoolean("invulnerable"));
        }
        final int live = config.getInt("ticks-lived");
        if (live > 0) {
            entity.setTicksLived(live);
        }
        if (config.isSet("portal-cooldown")) {
            entity.setPortalCooldown(config.getInt("portal-cooldown", -1));
        }
        if (XReflection.supports(13) && entity instanceof Boss) {
            final Boss boss = (Boss)entity;
            final ConfigurationSection bossBarSection = config.getConfigurationSection("bossbar");
            if (bossBarSection != null) {
                final BossBar bossBar = boss.getBossBar();
                editBossBar(bossBar, bossBarSection);
            }
        }
        if (entity instanceof Vehicle && entity instanceof Boat) {
            final Boat boat = (Boat)entity;
            final String speciesName = config.getString("tree-species");
            if (speciesName != null) {
                final com.google.common.base.Optional<TreeSpecies> species = (com.google.common.base.Optional<TreeSpecies>)Enums.getIfPresent((Class)TreeSpecies.class, speciesName);
                if (species.isPresent()) {
                    boat.setWoodType((TreeSpecies)species.get());
                }
            }
        }
        if (entity instanceof LivingEntity) {
            final LivingEntity living = (LivingEntity)entity;
            if (config.isSet("health")) {
                final double hp = config.getDouble("health");
                living.getAttribute((Attribute)((XModule<XForm, Attribute>)XAttribute.MAX_HEALTH).get()).setBaseValue(hp);
                living.setHealth(hp);
            }
            if (XReflection.supports(14)) {
                living.setAbsorptionAmount((double)config.getInt("absorption"));
            }
            if (config.isSet("AI")) {
                living.setAI(config.getBoolean("AI"));
            }
            if (config.isSet("can-pickup-items")) {
                living.setCanPickupItems(config.getBoolean("can-pickup-items"));
            }
            if (config.isSet("collidable")) {
                living.setCollidable(config.getBoolean("collidable"));
            }
            if (config.isSet("gliding")) {
                living.setGliding(config.getBoolean("gliding"));
            }
            if (config.isSet("remove-when-far-away")) {
                living.setRemoveWhenFarAway(config.getBoolean("remove-when-far-away"));
            }
            if (XReflection.supports(13) && config.isSet("swimming")) {
                living.setSwimming(config.getBoolean("swimming"));
            }
            if (config.isSet("max-air")) {
                living.setMaximumAir(config.getInt("max-air"));
            }
            if (config.isSet("no-damage-ticks")) {
                living.setNoDamageTicks(config.getInt("no-damage-ticks"));
            }
            if (config.isSet("remaining-air")) {
                living.setRemainingAir(config.getInt("remaining-air"));
            }
            XPotion.addEffects(living, (List<String>)config.getStringList("effects"));
            final ConfigurationSection equip = config.getConfigurationSection("equipment");
            if (equip != null) {
                final EntityEquipment equipment = living.getEquipment();
                final boolean isMob = entity instanceof Mob;
                final ConfigurationSection helmet = equip.getConfigurationSection("helmet");
                if (helmet != null) {
                    equipment.setHelmet(XItemStack.deserialize(helmet.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setHelmetDropChance((float)helmet.getInt("drop-chance"));
                    }
                }
                final ConfigurationSection chestplate = equip.getConfigurationSection("chestplate");
                if (chestplate != null) {
                    equipment.setChestplate(XItemStack.deserialize(chestplate.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setChestplateDropChance((float)chestplate.getInt("drop-chance"));
                    }
                }
                final ConfigurationSection leggings = equip.getConfigurationSection("leggings");
                if (leggings != null) {
                    equipment.setLeggings(XItemStack.deserialize(leggings.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setLeggingsDropChance((float)leggings.getInt("drop-chance"));
                    }
                }
                final ConfigurationSection boots = equip.getConfigurationSection("boots");
                if (boots != null) {
                    equipment.setBoots(XItemStack.deserialize(boots.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setBootsDropChance((float)boots.getInt("drop-chance"));
                    }
                }
                final ConfigurationSection mainHand = equip.getConfigurationSection("main-hand");
                if (mainHand != null) {
                    equipment.setItemInMainHand(XItemStack.deserialize(mainHand.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setItemInMainHandDropChance((float)mainHand.getInt("drop-chance"));
                    }
                }
                final ConfigurationSection offHand = equip.getConfigurationSection("off-hand");
                if (offHand != null) {
                    equipment.setItemInOffHand(XItemStack.deserialize(offHand.getConfigurationSection("item")));
                    if (isMob) {
                        equipment.setItemInOffHandDropChance((float)offHand.getInt("drop-chance"));
                    }
                }
            }
            if (living instanceof Ageable) {
                final Ageable ageable = (Ageable)living;
                if (config.isSet("breed")) {
                    ageable.setBreed(config.getBoolean("breed"));
                }
                if (config.isSet("baby")) {
                    if (config.getBoolean("baby")) {
                        ageable.setBaby();
                    }
                    else {
                        ageable.setAdult();
                    }
                }
                final int age = config.getInt("age", 0);
                if (age > 0) {
                    ageable.setAge(age);
                }
                if (config.isSet("age-lock")) {
                    ageable.setAgeLock(config.getBoolean("age-lock"));
                }
                if (living instanceof Animals) {
                    final Animals animals = (Animals)living;
                    final int loveModeTicks = config.getInt("love-mode");
                    if (loveModeTicks != 0) {
                        animals.setLoveModeTicks(loveModeTicks);
                    }
                    if (living instanceof Tameable) {
                        final Tameable tam = (Tameable)living;
                        tam.setTamed(config.getBoolean("tamed"));
                    }
                }
            }
            if (living instanceof Sittable) {
                final Sittable sit = (Sittable)living;
                sit.setSitting(config.getBoolean("sitting"));
            }
            if (living instanceof Spellcaster) {
                final Spellcaster caster = (Spellcaster)living;
                final String spell = config.getString("spell");
                if (spell != null) {
                    caster.setSpell((Spellcaster.Spell)Enums.getIfPresent((Class)Spellcaster.Spell.class, spell).or((Object)Spellcaster.Spell.NONE));
                }
            }
            if (living instanceof AbstractHorse) {
                final AbstractHorse horse = (AbstractHorse)living;
                if (config.isSet("domestication")) {
                    horse.setDomestication(config.getInt("domestication"));
                }
                if (config.isSet("jump-strength")) {
                    horse.setJumpStrength(config.getDouble("jump-strength"));
                }
                if (config.isSet("max-domestication")) {
                    horse.setMaxDomestication(config.getInt("max-domestication"));
                }
                final ConfigurationSection items = config.getConfigurationSection("items");
                if (items != null) {
                    final Inventory inventory = (Inventory)horse.getInventory();
                    for (final String key : items.getKeys(false)) {
                        final ConfigurationSection itemSec = items.getConfigurationSection(key);
                        final int slot = itemSec.getInt("slot", -1);
                        if (slot != -1) {
                            final ItemStack item = XItemStack.deserialize(itemSec);
                            if (item == null) {
                                continue;
                            }
                            inventory.setItem(slot, item);
                        }
                    }
                }
                if (living instanceof ChestedHorse) {
                    final ChestedHorse chested = (ChestedHorse)living;
                    final boolean hasChest = config.getBoolean("has-chest");
                    if (hasChest) {
                        chested.setCarryingChest(true);
                    }
                }
            }
            map(entity.getClass(), entity, config);
            if (living instanceof Villager) {
                final Villager villager = (Villager)living;
                if (XEntity.SUPPORTS_Villager_setVillagerLevel) {
                    villager.setVillagerLevel(config.getInt("level"));
                }
                if (XEntity.SUPPORTS_Villager_setVillagerExperience) {
                    villager.setVillagerExperience(config.getInt("xp"));
                }
            }
            else if (living instanceof Enderman) {
                final Enderman enderman = (Enderman)living;
                final String block = config.getString("carrying");
                if (block != null) {
                    final Optional<XMaterial> mat = XMaterial.matchXMaterial(block);
                    if (mat.isPresent()) {
                        final ItemStack item2 = ((XMaterial)mat.get()).parseItem();
                        if (item2 != null) {
                            enderman.setCarriedMaterial(item2.getData());
                        }
                    }
                }
            }
            else if (living instanceof Sheep) {
                final Sheep sheep = (Sheep)living;
                final boolean sheared = config.getBoolean("sheared");
                if (sheared) {
                    sheep.setSheared(true);
                }
            }
            else if (living instanceof Rabbit) {
                final Rabbit rabbit = (Rabbit)living;
                rabbit.setRabbitType((Rabbit.Type)Enums.getIfPresent((Class)Rabbit.Type.class, config.getString("color")).or((Object)Rabbit.Type.WHITE));
            }
            else if (living instanceof Bat) {
                final Bat bat = (Bat)living;
                if (!config.getBoolean("awake")) {
                    bat.setAwake(false);
                }
            }
            else if (living instanceof Wolf) {
                final Wolf wolf = (Wolf)living;
                wolf.setAngry(config.getBoolean("angry"));
                wolf.setCollarColor((DyeColor)Enums.getIfPresent((Class)DyeColor.class, config.getString("color")).or((Object)DyeColor.GREEN));
            }
            else if (living instanceof Creeper) {
                final Creeper creeper = (Creeper)living;
                creeper.setExplosionRadius(config.getInt("explosion-radius"));
                creeper.setMaxFuseTicks(config.getInt("max-fuse-ticks"));
                creeper.setPowered(config.getBoolean("powered"));
            }
            else if (XReflection.supports(10) && XReflection.supports(11)) {
                if (living instanceof Llama) {
                    final Llama llama = (Llama)living;
                    if (config.isSet("strength")) {
                        llama.setStrength(config.getInt("strength"));
                    }
                    final com.google.common.base.Optional<Llama.Color> color = (com.google.common.base.Optional<Llama.Color>)Enums.getIfPresent((Class)Llama.Color.class, config.getString("color"));
                    if (color.isPresent()) {
                        llama.setColor((Llama.Color)color.get());
                    }
                }
                else if (XReflection.supports(12)) {
                    if (living instanceof Parrot) {
                        final Parrot parrot = (Parrot)living;
                        parrot.setVariant((Parrot.Variant)Enums.getIfPresent((Class)Parrot.Variant.class, config.getString("color")).or((Object)Parrot.Variant.RED));
                    }
                    if (XReflection.supports(13)) {
                        thirteen(entity, config);
                    }
                    if (XReflection.supports(14)) {
                        fourteen(entity, config);
                    }
                    if (XReflection.supports(15)) {
                        fifteen(entity, config);
                    }
                    if (XReflection.supports(16)) {
                        sixteen(entity, config);
                    }
                    if (XReflection.supports(17)) {
                        seventeen(entity, config);
                    }
                }
            }
        }
        else if (entity instanceof EnderSignal) {
            final EnderSignal signal = (EnderSignal)entity;
            signal.setDespawnTimer(config.getInt("despawn-timer"));
            signal.setDropItem(config.getBoolean("drop-item"));
        }
        else if (entity instanceof ExperienceOrb) {
            final ExperienceOrb orb = (ExperienceOrb)entity;
            orb.setExperience(config.getInt("exp"));
        }
        else if (entity instanceof Explosive) {
            final Explosive explosive = (Explosive)entity;
            explosive.setYield((float)config.getDouble("yield"));
            explosive.setIsIncendiary(config.getBoolean("incendiary"));
        }
        else if (entity instanceof EnderCrystal) {
            final EnderCrystal crystal = (EnderCrystal)entity;
            crystal.setShowingBottom(config.getBoolean("show-bottom"));
        }
        return entity;
    }
    
    private static void fourteen(final Entity entity, final ConfigurationSection config) {
        if (entity instanceof Raider) {
            final Raider raider = (Raider)entity;
            if (config.isSet("can-join-raid")) {
                raider.setCanJoinRaid(config.getBoolean("can-join-raid"));
            }
            if (config.isSet("is-patrol-leader")) {
                raider.setCanJoinRaid(config.getBoolean("is-patrol-leader"));
            }
        }
        else if (entity instanceof Cat) {
            final Cat cat = (Cat)entity;
            cat.setCatType(getCatVariant(config.getString("variant")));
            cat.setCollarColor((DyeColor)Enums.getIfPresent((Class)DyeColor.class, config.getString("color")).or((Object)DyeColor.GREEN));
        }
        else if (entity instanceof Fox) {
            final Fox fox = (Fox)entity;
            fox.setCrouching(config.getBoolean("crouching"));
            fox.setSleeping(config.getBoolean("sleeping"));
            fox.setFoxType((Fox.Type)Enums.getIfPresent((Class)Fox.Type.class, config.getString("color")).or((Object)Fox.Type.RED));
        }
        else if (entity instanceof Panda) {
            final Panda panda = (Panda)entity;
            panda.setHiddenGene((Panda.Gene)Enums.getIfPresent((Class)Panda.Gene.class, config.getString("hidden-gene")).or((Object)Panda.Gene.PLAYFUL));
            panda.setMainGene((Panda.Gene)Enums.getIfPresent((Class)Panda.Gene.class, config.getString("main-gene")).or((Object)Panda.Gene.NORMAL));
        }
        else if (entity instanceof MushroomCow) {
            final MushroomCow mooshroom = (MushroomCow)entity;
            mooshroom.setVariant((MushroomCow.Variant)Enums.getIfPresent((Class)MushroomCow.Variant.class, config.getString("color")).or((Object)MushroomCow.Variant.RED));
        }
    }
    
    private static void thirteen(final Entity entity, final ConfigurationSection config) {
        if (entity instanceof Husk) {
            final Husk husk = (Husk)entity;
            husk.setConversionTime(config.getInt("conversion-time"));
        }
        else if (entity instanceof Vex) {
            final Vex vex = (Vex)entity;
            vex.setCharging(config.getBoolean("charging"));
        }
        else if (entity instanceof PufferFish) {
            final PufferFish pufferFish = (PufferFish)entity;
            pufferFish.setPuffState(config.getInt("puff-state"));
        }
        else if (entity instanceof TropicalFish) {
            final TropicalFish tropicalFish = (TropicalFish)entity;
            tropicalFish.setBodyColor((DyeColor)Enums.getIfPresent((Class)DyeColor.class, config.getString("color")).or((Object)DyeColor.WHITE));
            tropicalFish.setPattern((TropicalFish.Pattern)Enums.getIfPresent((Class)TropicalFish.Pattern.class, config.getString("pattern")).or((Object)TropicalFish.Pattern.BETTY));
            tropicalFish.setPatternColor((DyeColor)Enums.getIfPresent((Class)DyeColor.class, config.getString("pattern-color")).or((Object)DyeColor.WHITE));
        }
        else if (entity instanceof EnderDragon) {
            final EnderDragon dragon = (EnderDragon)entity;
            dragon.setPhase((EnderDragon.Phase)Enums.getIfPresent((Class)EnderDragon.Phase.class, config.getString("phase")).or((Object)EnderDragon.Phase.ROAR_BEFORE_ATTACK));
        }
        else if (entity instanceof Phantom) {
            final Phantom phantom = (Phantom)entity;
            phantom.setSize(config.getInt("size"));
        }
    }
    
    private static void fifteen(final Entity entity, final ConfigurationSection config) {
        if (entity instanceof Bee) {
            final Bee bee = (Bee)entity;
            bee.setAnger(config.getInt("anger") * 20);
            bee.setHasNectar(config.getBoolean("nectar"));
            bee.setHasStung(config.getBoolean("stung"));
            bee.setCannotEnterHiveTicks(config.getInt("disallow-hive") * 20);
        }
    }
    
    private static void sixteen(final Entity entity, final ConfigurationSection config) {
        if (entity instanceof Hoglin) {
            final Hoglin hoglin = (Hoglin)entity;
            hoglin.setConversionTime(config.getInt("conversation") * 20);
            hoglin.setImmuneToZombification(config.getBoolean("zombification-immunity"));
            hoglin.setIsAbleToBeHunted(config.getBoolean("can-be-hunted"));
        }
        else if (entity instanceof Piglin) {
            final Piglin piglin = (Piglin)entity;
            piglin.setConversionTime(config.getInt("conversation") * 20);
            piglin.setImmuneToZombification(config.getBoolean("zombification-immunity"));
        }
        else if (entity instanceof Strider) {
            final Strider strider = (Strider)entity;
            strider.setShivering(config.getBoolean("shivering"));
        }
    }
    
    private static void frog(final Entity entity, final ConfigurationSection config) {
        final Frog frog = (Frog)entity;
        frog.setVariant((Frog.Variant)Registry.FROG_VARIANT.get(fromConfig(config.getString("variant"))));
    }
    
    private static boolean seventeen(final Entity entity, final ConfigurationSection config) {
        if (entity instanceof Axolotl) {
            final Axolotl axolotl = (Axolotl)entity;
            final String variantStr = config.getString("variant");
            if (Strings.isNullOrEmpty(variantStr)) {
                final com.google.common.base.Optional<Axolotl.Variant> variant = (com.google.common.base.Optional<Axolotl.Variant>)Enums.getIfPresent((Class)Axolotl.Variant.class, variantStr);
                if (variant.isPresent()) {
                    axolotl.setVariant((Axolotl.Variant)variant.get());
                }
            }
            if (config.isSet("playing-dead")) {
                axolotl.setPlayingDead(config.getBoolean("playing-dead"));
            }
            return true;
        }
        if (entity instanceof Goat) {
            final Goat goat = (Goat)entity;
            if (config.isSet("screaming")) {
                goat.setScreaming(config.getBoolean("screaming"));
            }
            return true;
        }
        if (entity instanceof GlowSquid) {
            final GlowSquid glowSquid = (GlowSquid)entity;
            if (config.isSet("dark-ticks-remaining")) {
                glowSquid.setDarkTicksRemaining(config.getInt("dark-ticks-remaining"));
            }
            return true;
        }
        return false;
    }
    
    public static void editBossBar(final BossBar bossBar, final ConfigurationSection section) {
        final String title = section.getString("title");
        if (title != null) {
            bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', title));
        }
        final String colorStr = section.getString("color");
        if (colorStr != null) {
            final com.google.common.base.Optional<BarColor> color = (com.google.common.base.Optional<BarColor>)Enums.getIfPresent((Class)BarColor.class, colorStr.toUpperCase(Locale.ENGLISH));
            if (color.isPresent()) {
                bossBar.setColor((BarColor)color.get());
            }
        }
        final String styleStr = section.getString("style");
        if (styleStr != null) {
            final com.google.common.base.Optional<BarStyle> style = (com.google.common.base.Optional<BarStyle>)Enums.getIfPresent((Class)BarStyle.class, styleStr.toUpperCase(Locale.ENGLISH));
            if (style.isPresent()) {
                bossBar.setStyle((BarStyle)style.get());
            }
        }
        final List<String> flagList = (List<String>)section.getStringList("flags");
        if (!flagList.isEmpty()) {
            final Set<BarFlag> flags = (Set<BarFlag>)EnumSet.noneOf((Class)BarFlag.class);
            for (final String flagName : flagList) {
                final BarFlag flag = (BarFlag)Enums.getIfPresent((Class)BarFlag.class, flagName.toUpperCase(Locale.ENGLISH)).orNull();
                if (flag != null) {
                    flags.add((Object)flag);
                }
            }
            for (final BarFlag flag2 : BarFlag.values()) {
                if (flags.contains((Object)flag2)) {
                    bossBar.addFlag(flag2);
                }
                else {
                    bossBar.removeFlag(flag2);
                }
            }
        }
    }
    
    static {
        REGISTRY_CAT_VARIANT = supportsRegistry("CAT_VARIANT");
        boolean delayed;
        try {
            World.class.getMethod("spawn", Location.class, Class.class, Boolean.TYPE, Consumer.class);
            delayed = true;
        }
        catch (final NoSuchMethodException ex) {
            delayed = false;
        }
        SUPPORTS_DELAYED_SPAWN = delayed;
        MAPPING = (Map)new HashMap(20);
        if (XReflection.supports(19)) {
            register(Frog.class, (java.util.function.BiConsumer<Frog, ConfigurationSection>)XEntity::frog);
        }
        final StaticClassHandle villager = XReflection.of(Villager.class);
        SUPPORTS_Villager_setVillagerLevel = villager.method("void setVillagerLevel(int var1);").exists();
        SUPPORTS_Villager_setVillagerExperience = villager.method("void setVillagerExperience(int xp);").exists();
        SUPPORTS_Villager_setVillagerType = villager.method().named("setVillagerType").returns(Void.TYPE).parameters(villager.inner(XReflection.ofMinecraft().named("Type"))).exists();
        final Set<EntityType> undead = (Set<EntityType>)EnumSet.of((Enum)EntityType.SKELETON, (Enum[])new EntityType[] { EntityType.ZOMBIE, EntityType.GIANT, EntityType.ZOMBIE_VILLAGER, EntityType.WITHER, EntityType.WITHER_SKELETON, EntityType.ZOMBIE_HORSE });
        if (XReflection.supports(10)) {
            undead.add((Object)EntityType.HUSK);
            undead.add((Object)EntityType.STRAY);
            if (XReflection.supports(11)) {
                undead.add((Object)EntityType.SKELETON_HORSE);
                if (XReflection.supports(13)) {
                    undead.add((Object)EntityType.DROWNED);
                    undead.add((Object)EntityType.PHANTOM);
                    if (XReflection.supports(16)) {
                        undead.add((Object)EntityType.ZOGLIN);
                        undead.add((Object)EntityType.PIGLIN);
                        undead.add((Object)EntityType.ZOMBIFIED_PIGLIN);
                    }
                }
            }
        }
        if (!XReflection.supports(16)) {
            undead.add((Object)EntityType.valueOf("PIG_ZOMBIE"));
        }
        UNDEAD = Collections.unmodifiableSet((Set)undead);
    }
    
    private static final class MappedConfigObject
    {
        private final String configEntry;
        private final MethodHandle setter;
        private final Function<ConfigurationSection, Object> configurationValue;
        
        private MappedConfigObject(final String configEntry, final MethodHandle setter, final Function<ConfigurationSection, Object> configurationValue) {
            this.configEntry = configEntry;
            this.setter = setter;
            this.configurationValue = configurationValue;
        }
        
        private void handle(final Entity entity, final ConfigurationSection config) {
            if (config.isSet(this.configEntry)) {
                try {
                    this.setter.invoke(this.setter, this.configurationValue.apply((Object)config));
                }
                catch (final Throwable e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
