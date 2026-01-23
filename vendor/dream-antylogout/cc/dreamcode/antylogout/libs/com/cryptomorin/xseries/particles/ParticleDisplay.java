package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.particles;

import java.util.StringJoiner;
import org.bukkit.World;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.bukkit.util.NumberConversions;
import java.util.concurrent.Callable;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;
import org.bukkit.block.data.BlockData;
import org.bukkit.Note;
import java.util.Arrays;
import java.util.Iterator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.Locale;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Objects;
import org.bukkit.Particle;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import java.awt.Color;

public class ParticleDisplay
{
    private static final boolean ISFLAT;
    private static final boolean SUPPORTS_ALPHA_COLORS;
    public static final Color[] NOTE_COLORS;
    @NotNull
    private static final XParticle DEFAULT_PARTICLE;
    public int count;
    public double extra;
    public boolean force;
    @NotNull
    private XParticle particle;
    @Nullable
    private Location location;
    @Nullable
    private Location lastLocation;
    @NotNull
    private Vector offset;
    @Nullable
    private Vector particleDirection;
    @NotNull
    private Vector direction;
    @NotNull
    public List<List<Rotation>> rotations;
    @Nullable
    private List<Quaternion> cachedFinalRotationQuaternions;
    @Nullable
    private ParticleData data;
    @Nullable
    private Consumer<CalculationContext> preCalculation;
    @Nullable
    private Consumer<CalculationContext> postCalculation;
    @Nullable
    private Function<Double, Double> onAdvance;
    @Nullable
    private Set<Player> players;
    
    public ParticleDisplay() {
        this.count = 1;
        this.particle = ParticleDisplay.DEFAULT_PARTICLE;
        this.offset = new Vector();
        this.direction = new Vector(0, 1, 0);
        this.rotations = (List<List<Rotation>>)new ArrayList();
    }
    
    @Deprecated
    @NotNull
    public static ParticleDisplay colored(@Nullable final Location location, final int r, final int g, final int b, final float size) {
        return of(XParticle.DUST).withLocation(location).withColor((float)r, (float)g, (float)b, size);
    }
    
    @Nullable
    public Set<Player> getPlayers() {
        return this.players;
    }
    
    public ParticleDisplay onlyVisibleTo(final Collection<Player> players) {
        if (players == null || players.isEmpty()) {
            this.players = null;
            return this;
        }
        if (this.players == null) {
            this.players = (Set<Player>)Collections.newSetFromMap((Map)new WeakHashMap());
        }
        this.players.addAll((Collection)players);
        return this;
    }
    
    public ParticleDisplay onlyVisibleTo(final Player... players) {
        if (players == null || players.length == 0) {
            this.players = null;
            return this;
        }
        if (this.players == null) {
            this.players = (Set<Player>)Collections.newSetFromMap((Map)new WeakHashMap());
        }
        Collections.addAll((Collection)this.players, (Object[])players);
        return this;
    }
    
    @Deprecated
    @NotNull
    public static ParticleDisplay colored(final Location location, @NotNull final Color color, final float size) {
        return of(XParticle.DUST).withLocation(location).withColor(color, size);
    }
    
    @Deprecated
    @NotNull
    public static ParticleDisplay simple(@Nullable final Location location, @NotNull final Particle particle) {
        Objects.requireNonNull((Object)particle, "Cannot build ParticleDisplay with null particle");
        final ParticleDisplay display = new ParticleDisplay();
        display.particle = XParticle.of(particle);
        display.location = location;
        return display;
    }
    
    @Deprecated
    @NotNull
    public static ParticleDisplay of(@NotNull final Particle particle) {
        return of(XParticle.of(particle));
    }
    
    @NotNull
    public static ParticleDisplay of(@NotNull final XParticle particle) {
        final ParticleDisplay display = new ParticleDisplay();
        display.particle = particle;
        return display;
    }
    
    @Deprecated
    @NotNull
    public static ParticleDisplay display(@NotNull final Location location, @NotNull final Particle particle) {
        Objects.requireNonNull((Object)location, "Cannot display particle in null location");
        final ParticleDisplay display = simple(location, particle);
        display.spawn();
        return display;
    }
    
    public static ParticleDisplay fromConfig(@NotNull final ConfigurationSection config) {
        return edit(new ParticleDisplay(), config);
    }
    
    private static int toInt(final String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (final NumberFormatException nfe) {
            return 0;
        }
    }
    
    private static double toDouble(final String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (final NumberFormatException nfe) {
            return 0.0;
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
    
    @NotNull
    public static ParticleDisplay edit(@NotNull final ParticleDisplay display, @NotNull final ConfigurationSection config) {
        Objects.requireNonNull((Object)display, "Cannot edit a null particle display");
        Objects.requireNonNull((Object)config, "Cannot parse ParticleDisplay from a null config section");
        final String particleName = config.getString("particle");
        final Optional<XParticle> particle = (Optional<XParticle>)((particleName == null) ? Optional.empty() : XParticle.of(particleName));
        particle.ifPresent(xParticle -> display.particle = xParticle);
        if (config.isSet("count")) {
            display.withCount(config.getInt("count"));
        }
        if (config.isSet("extra")) {
            display.withExtra(config.getDouble("extra"));
        }
        if (config.isSet("force")) {
            display.forceSpawn(config.getBoolean("force"));
        }
        final String offset = config.getString("offset");
        if (offset != null) {
            final List<String> offsets = split(offset.replace((CharSequence)" ", (CharSequence)""), ',');
            if (offsets.size() >= 3) {
                final double offsetx = toDouble((String)offsets.get(0));
                final double offsety = toDouble((String)offsets.get(1));
                final double offsetz = toDouble((String)offsets.get(2));
                display.offset(offsetx, offsety, offsetz);
            }
            else {
                final double masterOffset = toDouble((String)offsets.get(0));
                display.offset(masterOffset);
            }
        }
        final String particleDirection = config.getString("direction");
        if (particleDirection != null) {
            final List<String> directions = split(particleDirection.replace((CharSequence)" ", (CharSequence)""), ',');
            if (directions.size() >= 3) {
                final double directionx = toDouble((String)directions.get(0));
                final double directiony = toDouble((String)directions.get(1));
                final double directionz = toDouble((String)directions.get(2));
                display.particleDirection(directionx, directiony, directionz);
            }
        }
        final ConfigurationSection rotations = config.getConfigurationSection("rotations");
        if (rotations != null) {
            for (final String rotationGroupName : rotations.getKeys(false)) {
                final ConfigurationSection rotationGroup = rotations.getConfigurationSection(rotationGroupName);
                final List<Rotation> grouped = (List<Rotation>)new ArrayList();
                for (final String rotationName : rotationGroup.getKeys(false)) {
                    final ConfigurationSection rotation = rotationGroup.getConfigurationSection(rotationName);
                    final double angle = rotation.getDouble("angle");
                    final String axisStr = rotation.getString("vector").toUpperCase(Locale.ENGLISH).replace((CharSequence)" ", (CharSequence)"");
                    Vector axis;
                    if (axisStr.length() == 1) {
                        axis = Axis.valueOf(axisStr).vector;
                    }
                    else {
                        final String[] split = axisStr.split(",");
                        axis = new Vector(Math.toRadians(Double.parseDouble(split[0])), Math.toRadians(Double.parseDouble(split[1])), Math.toRadians(Double.parseDouble(split[2])));
                    }
                    grouped.add((Object)Rotation.of(angle, axis));
                }
                display.rotations.add((Object)grouped);
            }
        }
        double size;
        if (config.isSet("size")) {
            size = config.getDouble("size");
            display.extra = size;
        }
        else {
            size = 1.0;
        }
        final String color = config.getString("color");
        final String blockdata = config.getString("blockdata");
        final String item = config.getString("itemstack");
        final String materialdata = config.getString("materialdata");
        if (color != null) {
            final List<String> colors = split(color.replace((CharSequence)" ", (CharSequence)""), ',');
            if (colors.size() <= 3 || colors.size() == 6) {
                Color parsedColor1 = Color.white;
                Color parsedColor2 = null;
                if (colors.size() <= 2) {
                    try {
                        parsedColor1 = Color.decode((String)colors.get(0));
                        if (colors.size() == 2) {
                            parsedColor2 = Color.decode((String)colors.get(1));
                        }
                    }
                    catch (final NumberFormatException ex) {}
                }
                else {
                    parsedColor1 = new Color(toInt((String)colors.get(0)), toInt((String)colors.get(1)), toInt((String)colors.get(2)));
                    if (colors.size() == 6) {
                        parsedColor2 = new Color(toInt((String)colors.get(3)), toInt((String)colors.get(4)), toInt((String)colors.get(5)));
                    }
                }
                if (parsedColor2 != null) {
                    display.data = new DustTransitionParticleColor(parsedColor1, parsedColor2, size);
                }
                else {
                    display.data = new RGBParticleColor(parsedColor1);
                }
            }
        }
        else if (blockdata != null) {
            final Material material = Material.getMaterial(blockdata);
            if (material != null && material.isBlock()) {
                display.data = new ParticleBlockData(material.createBlockData());
            }
        }
        else if (item != null) {
            final Material material = Material.getMaterial(item);
            if (material != null && material.isItem()) {
                display.data = new ParticleItemData(new ItemStack(material, 1));
            }
        }
        else if (materialdata != null) {
            final Material material = Material.getMaterial(materialdata);
            if (material != null && material.isBlock()) {
                display.data = new ParticleMaterialData(material.getNewData((byte)0));
            }
        }
        return display;
    }
    
    public static void serialize(final ParticleDisplay display, final ConfigurationSection section) {
        section.set("particle", (Object)display.particle.name());
        if (display.count != 1) {
            section.set("count", (Object)display.count);
        }
        if (display.extra != 0.0) {
            section.set("extra", (Object)display.extra);
        }
        if (display.force) {
            section.set("force", (Object)true);
        }
        if (!isZero(display.offset)) {
            final Vector offset = display.offset;
            section.set("offset", (Object)(offset.getX() + ", " + offset.getY() + ", " + offset.getZ()));
        }
        if (display.particleDirection != null) {
            final Vector direction = display.particleDirection;
            section.set("direction", (Object)(direction.getX() + ", " + direction.getY() + ", " + direction.getZ()));
        }
        if (!display.rotations.isEmpty()) {
            final ConfigurationSection rotations = section.createSection("rotations");
            int index = 1;
            for (final List<Rotation> rotationGroup : display.rotations) {
                final ConfigurationSection rotationGroupSection = rotations.createSection("group-" + index++);
                int groupIndex = 1;
                for (final Rotation rotation : rotationGroup) {
                    final ConfigurationSection rotationSection = rotationGroupSection.createSection(String.valueOf(groupIndex++));
                    rotationSection.set("angle", (Object)rotation.angle);
                    final Vector axis = rotation.axis;
                    final Optional<Axis> mainAxis = (Optional<Axis>)Arrays.stream((Object[])Axis.values()).filter(x -> x.vector.equals((Object)axis)).findFirst();
                    if (mainAxis.isPresent()) {
                        rotationSection.set("axis", (Object)((Axis)mainAxis.get()).name());
                    }
                    else {
                        rotationSection.set("axis", (Object)(axis.getX() + ", " + axis.getY() + ", " + axis.getZ()));
                    }
                }
            }
        }
        if (display.data != null) {
            display.data.serialize(section);
        }
    }
    
    public static Vector rotateAround(@NotNull final Vector location, @NotNull final Axis axis, @NotNull final Vector rotation) {
        Objects.requireNonNull((Object)axis, "Cannot rotate around null axis");
        Objects.requireNonNull((Object)rotation, "Rotation vector cannot be null");
        switch (axis.ordinal()) {
            case 0: {
                return rotateAround(location, axis, rotation.getX());
            }
            case 1: {
                return rotateAround(location, axis, rotation.getY());
            }
            case 2: {
                return rotateAround(location, axis, rotation.getZ());
            }
            default: {
                throw new AssertionError((Object)("Unknown rotation axis: " + (Object)axis));
            }
        }
    }
    
    public static Vector rotateAround(@NotNull final Vector location, final double x, final double y, final double z) {
        rotateAround(location, Axis.X, x);
        rotateAround(location, Axis.Y, y);
        rotateAround(location, Axis.Z, z);
        return location;
    }
    
    public static Vector rotateAround(@NotNull final Vector location, @NotNull final Axis axis, final double angle) {
        Objects.requireNonNull((Object)location, "Cannot rotate a null location");
        Objects.requireNonNull((Object)axis, "Cannot rotate around null axis");
        if (angle == 0.0) {
            return location;
        }
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        switch (axis.ordinal()) {
            case 0: {
                final double y = location.getY() * cos - location.getZ() * sin;
                final double z = location.getY() * sin + location.getZ() * cos;
                return location.setY(y).setZ(z);
            }
            case 1: {
                final double x = location.getX() * cos + location.getZ() * sin;
                final double z = location.getX() * -sin + location.getZ() * cos;
                return location.setX(x).setZ(z);
            }
            case 2: {
                final double x = location.getX() * cos - location.getY() * sin;
                final double y2 = location.getX() * sin + location.getY() * cos;
                return location.setX(x).setY(y2);
            }
            default: {
                throw new AssertionError((Object)("Unknown rotation axis: " + (Object)axis));
            }
        }
    }
    
    public ParticleDisplay preCalculation(@Nullable final Consumer<CalculationContext> preCalculation) {
        this.preCalculation = preCalculation;
        return this;
    }
    
    public ParticleDisplay postCalculation(@Nullable final Consumer<CalculationContext> postCalculation) {
        this.postCalculation = postCalculation;
        return this;
    }
    
    public ParticleDisplay onAdvance(@Nullable final Function<Double, Double> onAdvance) {
        this.onAdvance = onAdvance;
        return this;
    }
    
    public ParticleDisplay withParticle(@NotNull final Particle particle) {
        return this.withParticle(XParticle.of((Particle)Objects.requireNonNull((Object)particle, "Particle cannot be null")));
    }
    
    public ParticleDisplay withParticle(@NotNull final XParticle particle) {
        this.particle = (XParticle)Objects.requireNonNull((Object)particle, "Particle cannot be null");
        return this;
    }
    
    @NotNull
    public Vector getDirection() {
        return this.direction;
    }
    
    public void advanceInDirection(double distance) {
        Objects.requireNonNull((Object)this.direction, "Cannot advance with null direction");
        if (distance == 0.0) {
            return;
        }
        if (this.onAdvance != null) {
            distance = (double)this.onAdvance.apply((Object)distance);
        }
        this.location.add(this.direction.clone().multiply(distance));
    }
    
    public ParticleDisplay withDirection(@Nullable final Vector direction) {
        this.direction = direction.clone().normalize();
        return this;
    }
    
    @NotNull
    public XParticle getParticle() {
        return this.particle;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public double getExtra() {
        return this.extra;
    }
    
    @Nullable
    public ParticleData getData() {
        return this.data;
    }
    
    public ParticleDisplay withData(final ParticleData data) {
        this.data = data;
        return this;
    }
    
    @Override
    public String toString() {
        return "ParticleDisplay:[Particle=" + (Object)this.particle + ", Count=" + this.count + ", Offset:{" + this.offset.getX() + ", " + this.offset.getY() + ", " + this.offset.getZ() + "}, " + ((this.location != null) ? ("Location:{" + this.location.getWorld().getName() + this.location.getX() + ", " + this.location.getY() + ", " + this.location.getZ() + "}, ") : "") + "Rotation:" + (Object)this.rotations + ", Extra=" + this.extra + ", Force=" + this.force + ", Data=" + (Object)this.data;
    }
    
    @NotNull
    public ParticleDisplay withCount(final int count) {
        this.count = count;
        return this;
    }
    
    @NotNull
    public ParticleDisplay withExtra(final double extra) {
        this.extra = extra;
        return this;
    }
    
    @NotNull
    public ParticleDisplay forceSpawn(final boolean force) {
        this.force = force;
        return this;
    }
    
    @NotNull
    public ParticleDisplay withColor(@NotNull final Color color, final float size) {
        return this.withColor((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), size);
    }
    
    @NotNull
    public ParticleDisplay withColor(@NotNull final Color color) {
        return this.withColor(color, 1.0f);
    }
    
    @NotNull
    public ParticleDisplay withNoteColor(final int color) {
        this.data = new NoteParticleColor(color);
        return this;
    }
    
    @NotNull
    public ParticleDisplay withNoteColor(final Note note) {
        return this.withNoteColor(note.getId());
    }
    
    @Deprecated
    @NotNull
    public ParticleDisplay withColor(final float red, final float green, final float blue, final float size) {
        this.data = new RGBParticleColor((int)red, (int)green, (int)blue);
        this.extra = size;
        return this;
    }
    
    @NotNull
    public ParticleDisplay withTransitionColor(@NotNull final Color fromColor, final float size, @NotNull final Color toColor) {
        this.data = new DustTransitionParticleColor(fromColor, toColor, size);
        this.extra = size;
        return this;
    }
    
    @Deprecated
    @NotNull
    public ParticleDisplay withTransitionColor(final float red1, final float green1, final float blue1, final float size, final float red2, final float green2, final float blue2) {
        return this.withTransitionColor(new Color((int)red1, (int)green1, (int)blue1), size, new Color((int)red2, (int)green2, (int)blue2));
    }
    
    @NotNull
    public ParticleDisplay withBlock(@NotNull final BlockData blockData) {
        this.data = new ParticleBlockData(blockData);
        return this;
    }
    
    @NotNull
    public ParticleDisplay withBlock(@NotNull final MaterialData materialData) {
        this.data = new ParticleMaterialData(materialData);
        return this;
    }
    
    @NotNull
    public ParticleDisplay withItem(@NotNull final ItemStack item) {
        this.data = new ParticleItemData(item);
        return this;
    }
    
    @NotNull
    public Vector getOffset() {
        return this.offset;
    }
    
    @NotNull
    public Vector getParticleDirection() {
        return this.direction;
    }
    
    @NotNull
    public ParticleDisplay withEntity(@NotNull final Entity entity) {
        Objects.requireNonNull((Object)entity);
        return this.withLocationCaller((Callable<Location>)entity::getLocation);
    }
    
    @NotNull
    public ParticleDisplay withLocationCaller(@Nullable final Callable<Location> locationCaller) {
        this.preCalculation = (Consumer<CalculationContext>)(context -> {
            try {
                context.location = (Location)locationCaller.call();
            }
            catch (final Exception e) {
                throw new IllegalStateException("Failed to calculate location of particle: " + (Object)this, (Throwable)e);
            }
        });
        return this;
    }
    
    @Nullable
    public Location getLocation() {
        return this.location;
    }
    
    public ParticleDisplay withLocation(@Nullable final Location location) {
        this.location = location;
        return this;
    }
    
    @NotNull
    public ParticleDisplay face(@NotNull final Entity entity) {
        return this.face(((Entity)Objects.requireNonNull((Object)entity, "Cannot face null entity")).getLocation());
    }
    
    @NotNull
    public ParticleDisplay face(@NotNull final Location location) {
        Objects.requireNonNull((Object)location, "Cannot face null location");
        this.rotate(Rotation.of(Math.toRadians((double)location.getYaw()), Axis.Y), Rotation.of(Math.toRadians((double)(-location.getPitch())), Axis.X));
        this.direction = location.getDirection().clone().normalize();
        return this;
    }
    
    @Nullable
    public Location cloneLocation(final double x, final double y, final double z) {
        return (this.location == null) ? null : cloneLocation(this.location).add(x, y, z);
    }
    
    @NotNull
    private static Location cloneLocation(@NotNull final Location location) {
        return new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
    
    private static boolean isZero(@NotNull final Vector vector) {
        return vector.getX() == 0.0 && vector.getY() == 0.0 && vector.getZ() == 0.0;
    }
    
    @NotNull
    public ParticleDisplay cloneWithLocation(final double x, final double y, final double z) {
        final ParticleDisplay display = this.copy();
        if (this.location == null) {
            return display;
        }
        display.location.add(x, y, z);
        return display;
    }
    
    @NotNull
    public ParticleDisplay copy() {
        final ParticleDisplay display = of(this.particle).withDirection(this.direction).withCount(this.count).offset(this.offset.clone()).forceSpawn(this.force).onlyVisibleTo((Collection<Player>)this.players).preCalculation(this.preCalculation).postCalculation(this.postCalculation);
        if (this.location != null) {
            display.location = cloneLocation(this.location);
        }
        if (!this.rotations.isEmpty()) {
            display.rotations = (List<List<Rotation>>)new ArrayList((Collection)this.rotations);
        }
        display.data = this.data;
        return display;
    }
    
    public static Vector getPrincipalAxesRotation(final Location location) {
        return getPrincipalAxesRotation(location.getPitch(), location.getYaw(), 0.0f);
    }
    
    public static Vector getPrincipalAxesRotation(final float pitch, final float yaw, final float roll) {
        return new Vector(Math.toRadians((double)(pitch + 90.0f)), Math.toRadians((double)(-yaw)), (double)roll);
    }
    
    public static float[] getYawPitch(final Vector vector) {
        final double _2PI = 6.283185307179586;
        final double x = vector.getX();
        final double z = vector.getZ();
        float yaw;
        float pitch;
        if (x == 0.0 && z == 0.0) {
            yaw = 0.0f;
            pitch = ((vector.getY() > 0.0) ? -90.0f : 90.0f);
        }
        else {
            final double theta = Math.atan2(-x, z);
            yaw = (float)Math.toDegrees((theta + 6.283185307179586) % 6.283185307179586);
            final double x2 = NumberConversions.square(x);
            final double z2 = NumberConversions.square(z);
            final double xz = Math.sqrt(x2 + z2);
            pitch = (float)Math.toDegrees(Math.atan(-vector.getY() / xz));
        }
        return new float[] { yaw, pitch };
    }
    
    @NotNull
    public List<Quaternion> getRotation(final boolean forceUpdate) {
        if (this.rotations.isEmpty()) {
            return (List<Quaternion>)new ArrayList();
        }
        if (forceUpdate) {
            this.cachedFinalRotationQuaternions = null;
        }
        if (this.cachedFinalRotationQuaternions == null) {
            this.cachedFinalRotationQuaternions = (List<Quaternion>)new ArrayList();
            for (final List<Rotation> rotationGroup : this.rotations) {
                Quaternion groupedQuat = null;
                for (final Rotation rotation : rotationGroup) {
                    final Quaternion q = Quaternion.rotation(rotation.angle, rotation.axis);
                    if (groupedQuat == null) {
                        groupedQuat = q;
                    }
                    else {
                        groupedQuat = groupedQuat.mul(q);
                    }
                }
                this.cachedFinalRotationQuaternions.add((Object)groupedQuat);
            }
        }
        return this.cachedFinalRotationQuaternions;
    }
    
    @NotNull
    public ParticleDisplay rotate(final double x, final double y, final double z) {
        return this.rotate(Rotation.of(x, Axis.X), Rotation.of(y, Axis.Y), Rotation.of(z, Axis.Z));
    }
    
    public ParticleDisplay rotate(final Rotation... rotations) {
        Objects.requireNonNull((Object)rotations, "Null rotations");
        if (rotations.length != 0) {
            final List<Rotation> finalRots = (List<Rotation>)Arrays.stream((Object[])rotations).filter(x -> x.angle != 0.0).collect(Collectors.toList());
            if (!finalRots.isEmpty()) {
                this.rotations.add((Object)finalRots);
                if (this.cachedFinalRotationQuaternions != null) {
                    this.cachedFinalRotationQuaternions.clear();
                }
            }
        }
        return this;
    }
    
    public ParticleDisplay rotate(final Rotation rotation) {
        Objects.requireNonNull((Object)rotation, "Null rotation");
        if (rotation.angle != 0.0) {
            this.rotations.add((Object)Collections.singletonList((Object)rotation));
            if (this.cachedFinalRotationQuaternions != null) {
                this.cachedFinalRotationQuaternions.clear();
            }
        }
        return this;
    }
    
    @Nullable
    public Location getLastLocation() {
        return (this.lastLocation == null) ? this.getLocation() : this.lastLocation;
    }
    
    @Nullable
    public Location finalizeLocation(@Nullable Vector local) {
        final CalculationContext preContext = new CalculationContext(this.location, local);
        if (this.preCalculation != null) {
            this.preCalculation.accept((Object)preContext);
        }
        if (!preContext.shouldSpawn) {
            return null;
        }
        Location location = preContext.location;
        if (location == null) {
            throw new IllegalStateException("Attempting to spawn particle when no location is set");
        }
        local = preContext.local;
        if (local != null && !this.rotations.isEmpty()) {
            final List<Quaternion> rotations = this.getRotation(false);
            for (final Quaternion grouped : rotations) {
                local = Quaternion.rotate(local, grouped);
            }
        }
        location = cloneLocation(location);
        if (local != null) {
            location.add(local);
        }
        final CalculationContext postContext = new CalculationContext(location, local);
        if (this.postCalculation != null) {
            this.postCalculation.accept((Object)postContext);
        }
        if (!postContext.shouldSpawn) {
            return null;
        }
        return location;
    }
    
    @NotNull
    public ParticleDisplay offset(final double x, final double y, final double z) {
        return this.offset(new Vector(x, y, z));
    }
    
    @NotNull
    public ParticleDisplay offset(@NotNull final Vector offset) {
        this.offset = (Vector)Objects.requireNonNull((Object)offset, "Particle offset cannot be null");
        return this;
    }
    
    @NotNull
    public ParticleDisplay offset(final double offset) {
        return this.offset(offset, offset, offset);
    }
    
    @NotNull
    public ParticleDisplay particleDirection(final double x, final double y, final double z) {
        return this.particleDirection(new Vector(x, y, z));
    }
    
    @NotNull
    public ParticleDisplay particleDirection(@Nullable final Vector particleDirection) {
        this.particleDirection = particleDirection;
        if (particleDirection != null && this.extra == 0.0) {
            this.extra = 1.0;
        }
        return this;
    }
    
    @NotNull
    public ParticleDisplay directional() {
        this.particleDirection = new Vector();
        return this;
    }
    
    public boolean isDirectional() {
        return this.particleDirection != null;
    }
    
    @Nullable
    public Location spawn() {
        return this.spawn(this.finalizeLocation(null));
    }
    
    @Nullable
    public Location spawn(@Nullable final Vector local) {
        return this.spawn(this.finalizeLocation(local));
    }
    
    @Nullable
    public Location spawn(final double x, final double y, final double z) {
        return this.spawn(this.finalizeLocation(new Vector(x, y, z)));
    }
    
    @Nullable
    public Location spawn(final Location loc) {
        if (loc == null) {
            return null;
        }
        this.lastLocation = loc;
        final Particle particle = this.particle.get();
        Objects.requireNonNull((Object)particle, () -> "Cannot spawn unsupported particle: " + (Object)particle);
        if (this.count == 0) {
            this.count = 1;
        }
        Object data = null;
        if (this.data != null) {
            this.data = this.data.transform(this);
            final Vector offsetData = this.data.offsetValues(this);
            if (offsetData != null) {
                this.spawnWithDataInOffset(particle, loc, offsetData, null);
                return loc;
            }
            data = this.data.data(this);
            if (!particle.getDataType().isInstance(data)) {
                data = null;
            }
        }
        if (this.particleDirection != null) {
            this.spawnWithDataInOffset(particle, loc, this.particleDirection, data);
            return loc;
        }
        this.spawnRaw(particle, loc, this.count, this.offset, data);
        return loc;
    }
    
    private void spawnWithDataInOffset(final Particle particle, final Location loc, final Vector offsetData, final Object data) {
        if (isZero(this.offset) && this.count < 2) {
            this.spawnRaw(particle, loc, 0, offsetData, data);
            return;
        }
        final double offsetx = this.offset.getX();
        final double offsety = this.offset.getY();
        final double offsetz = this.offset.getZ();
        final ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < this.count; ++i) {
            final double dx = (offsetx == 0.0) ? 0.0 : (r.nextGaussian() * 4.0 * offsetx);
            final double dy = (offsety == 0.0) ? 0.0 : (r.nextGaussian() * 4.0 * offsety);
            final double dz = (offsetz == 0.0) ? 0.0 : (r.nextGaussian() * 4.0 * offsetz);
            final Location offsetLoc = cloneLocation(loc).add(dx, dy, dz);
            this.spawnRaw(particle, offsetLoc, 0, offsetData, data);
        }
    }
    
    private void spawnRaw(final Particle particle, final Location loc, final int count, final Vector offset, final Object data) {
        final double dx = offset.getX();
        final double dy = offset.getY();
        final double dz = offset.getZ();
        double extra = this.extra;
        if (this.particle == XParticle.DUST || this.particle == XParticle.NOTE) {
            extra = 1.0;
        }
        if (this.players == null) {
            if (ParticleDisplay.ISFLAT) {
                loc.getWorld().spawnParticle(particle, loc, count, dx, dy, dz, extra, data, this.force);
            }
            else {
                loc.getWorld().spawnParticle(particle, loc, count, dx, dy, dz, extra, data);
            }
        }
        else {
            for (final Player player : this.players) {
                player.spawnParticle(particle, loc, count, dx, dy, dz, extra, data);
            }
        }
    }
    
    public static int findNearestNoteColor(final Color color) {
        double best = colorDistanceSquared(color, ParticleDisplay.NOTE_COLORS[0]);
        int bestIndex = 0;
        for (int i = 1; i < ParticleDisplay.NOTE_COLORS.length; ++i) {
            final double distance = colorDistanceSquared(color, ParticleDisplay.NOTE_COLORS[i]);
            if (distance < best) {
                best = distance;
                bestIndex = i;
            }
        }
        return bestIndex;
    }
    
    public static double colorDistanceSquared(final Color c1, final Color c2) {
        final int red1 = c1.getRed();
        final int red2 = c2.getRed();
        final int rmean = red1 + red2 >> 1;
        final int r = red1 - red2;
        final int g = c1.getGreen() - c2.getGreen();
        final int b = c1.getBlue() - c2.getBlue();
        return ((512 + rmean) * r * r >> 8) + 4 * g * g + ((767 - rmean) * b * b >> 8);
    }
    
    static {
        boolean isFlat;
        try {
            World.class.getDeclaredMethod("spawnParticle", Particle.class, Location.class, Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Object.class, Boolean.TYPE);
            isFlat = true;
        }
        catch (final NoSuchMethodException e) {
            isFlat = false;
        }
        ISFLAT = isFlat;
        boolean supportsAlphaColors;
        try {
            org.bukkit.Color.fromARGB(0);
            supportsAlphaColors = true;
        }
        catch (final NoSuchMethodError e2) {
            supportsAlphaColors = false;
        }
        SUPPORTS_ALPHA_COLORS = supportsAlphaColors;
        NOTE_COLORS = new Color[] { new Color(7853824), new Color(9814016), new Color(11707648), new Color(13403648), new Color(14836992), new Color(15941888), new Color(16522752), new Color(16646159), new Color(16187443), new Color(15204442), new Color(13566083), new Color(11403433), new Color(8782028), new Color(5964007), new Color(2949369), new Color(133886), new Color(14326), new Color(26848), new Color(39612), new Color(50829), new Color(59736), new Color(64545), new Color(2096128), new Color(5892096), new Color(9748736) };
        DEFAULT_PARTICLE = XParticle.FLAME;
    }
    
    public final class CalculationContext
    {
        private Location location;
        private Vector local;
        private boolean shouldSpawn;
        
        public CalculationContext(final Location location, final Vector local) {
            this.shouldSpawn = true;
            this.location = location;
            this.local = local;
        }
        
        @Nullable
        public Location getLocation() {
            return this.location;
        }
        
        @Nullable
        public Vector getLocal() {
            return this.local;
        }
        
        public void setLocal(final Vector local) {
            this.local = local;
        }
        
        public void setLocation(final Location location) {
            this.location = location;
        }
        
        public void dontSpawn() {
            this.shouldSpawn = false;
        }
        
        public ParticleDisplay getDisplay() {
            return ParticleDisplay.this;
        }
    }
    
    public enum Axis
    {
        X(new Vector(1, 0, 0)), 
        Y(new Vector(0, 1, 0)), 
        Z(new Vector(0, 0, 1));
        
        private final Vector vector;
        
        private Axis(final Vector vector) {
            this.vector = vector;
        }
        
        public Vector getVector() {
            return this.vector;
        }
    }
    
    public static class Rotation
    {
        public double angle;
        public Vector axis;
        
        public Rotation(final double angle, final Vector axis) {
            this.angle = angle;
            this.axis = axis;
        }
        
        public Rotation copy() {
            return new Rotation(this.angle, this.axis.clone());
        }
        
        public static Rotation of(final double angle, final Vector axis) {
            return new Rotation(angle, axis);
        }
        
        public static Rotation of(final double angle, final Axis axis) {
            return new Rotation(angle, axis.vector);
        }
    }
    
    public static class Quaternion
    {
        public final double w;
        public final double x;
        public final double y;
        public final double z;
        
        public Quaternion(final double w, final double x, final double y, final double z) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public Quaternion copy() {
            return new Quaternion(this.w, this.x, this.y, this.z);
        }
        
        public static Vector rotate(final Vector vector, final Quaternion rotation) {
            return rotation.mul(from(vector)).mul(rotation.inverse()).toVector();
        }
        
        public static Vector rotate(final Vector vector, final Vector axis, final double deg) {
            return rotate(vector, rotation(deg, axis));
        }
        
        public static Quaternion from(final Vector vector) {
            return new Quaternion(0.0, vector.getX(), vector.getY(), vector.getZ());
        }
        
        public static Quaternion rotation(double degrees, Vector vector) {
            vector = vector.normalize();
            degrees /= 2.0;
            final double sin = Math.sin(degrees);
            return new Quaternion(Math.cos(degrees), vector.getX() * sin, vector.getY() * sin, vector.getZ() * sin);
        }
        
        public String getInverseString() {
            final double rads = Math.acos(this.w);
            final double deg = Math.toDegrees(rads) * 2.0;
            final double sin = Math.sin(rads);
            final Vector axis = new Vector(this.x / sin, this.y / sin, this.z / sin);
            return deg + ", " + axis.getX() + ", " + axis.getY() + ", " + axis.getZ();
        }
        
        public Vector toVector() {
            return new Vector(this.x, this.y, this.z);
        }
        
        public Quaternion inverse() {
            final double l = this.w * this.w + this.x * this.x + this.y * this.y + this.z * this.z;
            return new Quaternion(this.w / l, -this.x / l, -this.y / l, -this.z / l);
        }
        
        public Quaternion conjugate() {
            return new Quaternion(this.w, -this.x, -this.y, -this.z);
        }
        
        public Quaternion mul(final Quaternion r) {
            final double n0 = r.w * this.w - r.x * this.x - r.y * this.y - r.z * this.z;
            final double n2 = r.w * this.x + r.x * this.w + r.y * this.z - r.z * this.y;
            final double n3 = r.w * this.y - r.x * this.z + r.y * this.w + r.z * this.x;
            final double n4 = r.w * this.z + r.x * this.y - r.y * this.x + r.z * this.w;
            return new Quaternion(n0, n2, n3, n4);
        }
        
        public Vector mul(final Vector point) {
            final double x = this.x * 2.0;
            final double y = this.y * 2.0;
            final double z = this.z * 2.0;
            final double xx = this.x * x;
            final double yy = this.y * y;
            final double zz = this.z * z;
            final double xy = this.x * y;
            final double xz = this.x * z;
            final double yz = this.y * z;
            final double wx = this.w * x;
            final double wy = this.w * y;
            final double wz = this.w * z;
            final double vx = (1.0 - (yy + zz)) * point.getX() + (xy - wz) * point.getY() + (xz + wy) * point.getZ();
            final double vy = (xy + wz) * point.getX() + (1.0 - (xx + zz)) * point.getY() + (yz - wx) * point.getZ();
            final double vz = (xz - wy) * point.getX() + (yz + wx) * point.getY() + (1.0 - (xx + yy)) * point.getZ();
            return new Vector(vx, vy, vz);
        }
    }
    
    public interface ParticleData
    {
        default Vector offsetValues(final ParticleDisplay display) {
            return null;
        }
        
        Object data(final ParticleDisplay p0);
        
        void serialize(final ConfigurationSection p0);
        
        default ParticleData transform(final ParticleDisplay display) {
            return this;
        }
    }
    
    public static class RGBParticleColor implements ParticleData
    {
        private final Color color;
        
        public RGBParticleColor(final Color color) {
            this.color = color;
        }
        
        public RGBParticleColor(final int r, final int g, final int b) {
            this(new Color(r, g, b));
        }
        
        @Override
        public Vector offsetValues(final ParticleDisplay display) {
            if (!ParticleDisplay.ISFLAT || (display.particle == XParticle.ENTITY_EFFECT && display.particle.isSupported() && display.particle.get().getDataType() == Void.class)) {
                final double red = (this.color.getRed() == 0) ? 1.401298464324817E-45 : (this.color.getRed() / 255.0);
                return new Vector(red, this.color.getGreen() / 255.0, this.color.getBlue() / 255.0);
            }
            return null;
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            final float particleSize = (display.extra == 0.0) ? 1.0f : ((float)display.extra);
            if (display.particle == XParticle.DUST) {
                return new Particle.DustOptions(org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue()), particleSize);
            }
            if (display.particle == XParticle.DUST_COLOR_TRANSITION) {
                final org.bukkit.Color color = org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
                return new Particle.DustTransition(color, color, particleSize);
            }
            if (ParticleDisplay.SUPPORTS_ALPHA_COLORS) {
                return org.bukkit.Color.fromARGB(this.color.getAlpha(), this.color.getRed(), this.color.getGreen(), this.color.getBlue());
            }
            return org.bukkit.Color.fromRGB(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            final StringJoiner colorJoiner = new StringJoiner((CharSequence)", ");
            colorJoiner.add((CharSequence)Integer.toString(this.color.getRed()));
            colorJoiner.add((CharSequence)Integer.toString(this.color.getGreen()));
            colorJoiner.add((CharSequence)Integer.toString(this.color.getBlue()));
            section.set("color", (Object)colorJoiner.toString());
        }
        
        @Override
        public ParticleData transform(final ParticleDisplay display) {
            if (display.particle == XParticle.NOTE) {
                return new NoteParticleColor(ParticleDisplay.findNearestNoteColor(this.color));
            }
            return this;
        }
    }
    
    public static class DustTransitionParticleColor implements ParticleData
    {
        private final Particle.DustTransition dustTransition;
        
        public DustTransitionParticleColor(final Color fromColor, final Color toColor, final double size) {
            this.dustTransition = new Particle.DustTransition(org.bukkit.Color.fromRGB(fromColor.getRed(), fromColor.getGreen(), fromColor.getBlue()), org.bukkit.Color.fromRGB(toColor.getRed(), toColor.getGreen(), toColor.getBlue()), (float)size);
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            return this.dustTransition;
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            final StringJoiner colorJoiner = new StringJoiner((CharSequence)", ");
            final org.bukkit.Color fromColor = this.dustTransition.getColor();
            final org.bukkit.Color toColor = this.dustTransition.getToColor();
            colorJoiner.add((CharSequence)Integer.toString(fromColor.getRed()));
            colorJoiner.add((CharSequence)Integer.toString(fromColor.getGreen()));
            colorJoiner.add((CharSequence)Integer.toString(fromColor.getBlue()));
            colorJoiner.add((CharSequence)Integer.toString(toColor.getRed()));
            colorJoiner.add((CharSequence)Integer.toString(toColor.getGreen()));
            colorJoiner.add((CharSequence)Integer.toString(toColor.getBlue()));
            section.set("color", (Object)colorJoiner.toString());
        }
    }
    
    public static class NoteParticleColor implements ParticleData
    {
        private final int note;
        
        public NoteParticleColor(final int note) {
            this.note = note;
        }
        
        public NoteParticleColor(final Note note) {
            this(note.getId());
        }
        
        @Override
        public Vector offsetValues(final ParticleDisplay display) {
            return new Vector(this.note / 24.0, 0.0, 0.0);
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            return null;
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            section.set("color", (Object)this.note);
        }
        
        @Override
        public ParticleData transform(final ParticleDisplay display) {
            if (display.particle == XParticle.NOTE) {
                return this;
            }
            return new RGBParticleColor(ParticleDisplay.NOTE_COLORS[this.note]);
        }
    }
    
    public static class ParticleBlockData implements ParticleData
    {
        private final BlockData blockData;
        
        public ParticleBlockData(final BlockData blockData) {
            this.blockData = blockData;
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            return this.blockData;
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            section.set("blockdata", (Object)this.blockData.getMaterial().name());
        }
    }
    
    public static class ParticleMaterialData implements ParticleData
    {
        private final MaterialData materialData;
        
        public ParticleMaterialData(final MaterialData materialData) {
            this.materialData = materialData;
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            return this.materialData;
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            section.set("materialdata", (Object)this.materialData.getItemType().name());
        }
    }
    
    public static class ParticleItemData implements ParticleData
    {
        private final ItemStack item;
        
        public ParticleItemData(final ItemStack item) {
            this.item = item;
        }
        
        @Override
        public Object data(final ParticleDisplay display) {
            return this.item;
        }
        
        @Override
        public void serialize(final ConfigurationSection section) {
            section.set("itemstack", (Object)this.item.getType());
        }
    }
}
