package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.particles;

import java.awt.image.ImageObserver;
import java.awt.Image;
import java.util.concurrent.CompletionStage;
import java.util.HashMap;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.image.RenderedImage;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import java.util.concurrent.Callable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.util.NumberConversions;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.function.BooleanSupplier;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import org.bukkit.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Optional;

public final class Particles
{
    public static final double PII = 6.283185307179586;
    public static final double R270;
    public static final double R90 = 1.5707963267948966;
    
    private Particles() {
    }
    
    public static Optional<XParticle> randomParticle(final String... particles) {
        final int rand = randInt(0, particles.length - 1);
        return XParticle.of(particles[rand]);
    }
    
    public static double random(final double min, final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public static int randInt(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public static Color randomColor() {
        final ThreadLocalRandom gen = ThreadLocalRandom.current();
        final int randR = gen.nextInt(0, 256);
        final int randG = gen.nextInt(0, 256);
        final int randB = gen.nextInt(0, 256);
        return Color.fromRGB(randR, randG, randB);
    }
    
    public static Particle.DustOptions randomDust() {
        final float size = randInt(5, 10) / 10.0f;
        return new Particle.DustOptions(randomColor(), size);
    }
    
    public static void blackSun(final double radius, final double radiusRate, final double rate, final double rateChange, final ParticleDisplay display) {
        double j = 0.0;
        for (double i = 10.0; i > 0.0; i -= radiusRate) {
            j += rateChange;
            circle(radius + i, rate - j, display);
        }
    }
    
    public static void circle(final double radius, final double rate, final ParticleDisplay display) {
        circle(radius, radius, 1.0, rate, 0.0, display);
    }
    
    public static void circle(final double radius, final double radius2, final double extension, final double rate, double limit, final ParticleDisplay display) {
        final double rateDiv = 3.141592653589793 / Math.abs(rate);
        if (limit == 0.0) {
            limit = 6.283185307179586;
        }
        else if (limit == -1.0) {
            limit = 6.283185307179586 / Math.abs(extension);
        }
        for (double theta = 0.0; theta <= limit; theta += rateDiv) {
            final double x = radius * Math.cos(extension * theta);
            final double z = radius2 * Math.sin(extension * theta);
            if (display.isDirectional()) {
                final double phi = Math.atan2(z, x);
                final double directionX = Math.cos(extension * phi);
                final double directionZ = Math.sin(extension * phi);
                display.particleDirection(directionX, display.getOffset().getY(), directionZ);
            }
            display.spawn(x, 0.0, z);
        }
    }
    
    public static void diamond(final double radiusRate, final double rate, final double height, final ParticleDisplay display) {
        double count = 0.0;
        for (double y = 0.0; y < height * 2.0; y += rate) {
            if (y < height) {
                count += radiusRate;
            }
            else {
                count -= radiusRate;
            }
            for (double x = -count; x < count; x += rate) {
                display.spawn(x, y, 0.0);
            }
        }
    }
    
    public static Runnable circularBeam(final double maxRadius, final double rate, final double radiusRate, final double extend, final ParticleDisplay display) {
        return (Runnable)new Runnable() {
            final double rateDiv = 3.141592653589793 / rate;
            final double radiusDiv = 3.141592653589793 / radiusRate;
            final Vector dir = display.getLocation().getDirection().normalize().multiply(extend);
            double dynamicRadius = 0.0;
            
            public void run() {
                final double radius = maxRadius * Math.sin(this.dynamicRadius);
                for (double theta = 0.0; theta < 6.283185307179586; theta += this.rateDiv) {
                    final double x = radius * Math.sin(theta);
                    final double z = radius * Math.cos(theta);
                    display.spawn(x, 0.0, z);
                }
                this.dynamicRadius += this.radiusDiv;
                if (this.dynamicRadius > 3.141592653589793) {
                    this.dynamicRadius = 0.0;
                }
                display.getLocation().add(this.dir);
            }
        };
    }
    
    public static BukkitTask circularBeam(final Plugin plugin, final double maxRadius, final double rate, final double radiusRate, final double extend, final ParticleDisplay display) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, circularBeam(maxRadius, rate, radiusRate, extend, display), 0L, 1L);
    }
    
    public static void flower(final int count, final double radius, final ParticleDisplay display, final Runnable runnable) {
        for (double theta = 0.0; theta < 6.283185307179586; theta += 6.283185307179586 / count) {
            final double x = radius * Math.cos(theta);
            final double z = radius * Math.sin(theta);
            display.getLocation().add(x, 0.0, z);
            runnable.run();
            display.getLocation().subtract(x, 0.0, z);
        }
    }
    
    public static void filledCircle(final double radius, final double rate, final double radiusRate, final ParticleDisplay display) {
        double dynamicRate = 0.0;
        for (double i = 0.1; i < radius; i += radiusRate) {
            if (i > radius) {
                i = radius;
            }
            dynamicRate += rate / (radius / radiusRate);
            circle(i, dynamicRate, display);
        }
    }
    
    public static Runnable chaoticDoublePendulum(final double radius, final double gravity, final double length, final double length2, final double mass1, final double mass2, final boolean dimension3, final int speed, final ParticleDisplay display) {
        return (Runnable)new Runnable() {
            double theta = 1.5707963267948966;
            double theta2 = 1.5707963267948966;
            double thetaPrime = 0.0;
            double thetaPrime2 = 0.0;
            
            public void run() {
                int repeat = speed;
                while (repeat-- != 0) {
                    if (dimension3) {
                        display.rotate(0.09519977738150888, 0.07139983303613166, 0.057119866428905326);
                    }
                    final double totalMass = mass1 + mass2;
                    final double totalMassDouble = 2.0 * totalMass;
                    final double deltaTheta = this.theta - this.theta2;
                    final double lenLunar = totalMassDouble - mass2 * Math.cos(2.0 * this.theta - 2.0 * this.theta2);
                    final double deltaCosTheta = Math.cos(deltaTheta);
                    final double deltaSinTheta = Math.sin(deltaTheta);
                    final double phi = this.thetaPrime * this.thetaPrime * length;
                    final double phi2 = this.thetaPrime2 * this.thetaPrime2 * length2;
                    double num1 = -gravity * totalMassDouble * Math.sin(this.theta);
                    double num2 = -mass2 * gravity * Math.sin(this.theta - 2.0 * this.theta2);
                    double num3 = -2.0 * deltaSinTheta * mass2;
                    double num4 = phi2 + phi * deltaCosTheta;
                    double len = length * lenLunar;
                    final double thetaDoublePrime = (num1 + num2 + num3 * num4) / len;
                    num1 = 2.0 * deltaSinTheta;
                    num2 = phi * totalMass;
                    num3 = gravity * totalMass * Math.cos(this.theta);
                    num4 = phi2 * mass2 * deltaCosTheta;
                    len = length2 * lenLunar;
                    final double thetaDoublePrime2 = num1 * (num2 + num3 + num4) / len;
                    this.thetaPrime += thetaDoublePrime;
                    this.thetaPrime2 += thetaDoublePrime2;
                    this.theta += this.thetaPrime;
                    this.theta2 += this.thetaPrime2;
                    final double x = radius * Math.sin(this.theta);
                    final double y = radius * Math.cos(this.theta);
                    final double x2 = x + radius * Math.sin(this.theta2);
                    final double y2 = y + radius * Math.cos(this.theta2);
                    display.spawn(x2, y2, 0.0);
                }
            }
        };
    }
    
    public static BukkitTask chaoticDoublePendulum(final Plugin plugin, final double radius, final double gravity, final double length, final double length2, final double mass1, final double mass2, final boolean dimension3, final int speed, final ParticleDisplay display) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, chaoticDoublePendulum(radius, gravity, length, length2, mass1, mass2, dimension3, speed, display), 0L, 1L);
    }
    
    public static Runnable magicCircles(final double radius, final double rate, final double radiusRate, final double distance, final ParticleDisplay display) {
        return (Runnable)new Runnable() {
            final double radiusDiv = 3.141592653589793 / radiusRate;
            final Vector dir = display.getLocation().getDirection().normalize().multiply(distance);
            double dynamicRadius = radius;
            
            public void run() {
                for (double rateDiv = 3.141592653589793 / (rate * this.dynamicRadius), theta = 0.0; theta < 6.283185307179586; theta += rateDiv) {
                    final double x = this.dynamicRadius * Math.sin(theta);
                    final double z = this.dynamicRadius * Math.cos(theta);
                    display.spawn(x, 0.0, z);
                }
                this.dynamicRadius += this.radiusDiv;
                display.getLocation().add(this.dir);
            }
        };
    }
    
    public static BukkitTask magicCircles(final Plugin plugin, final double radius, final double rate, final double radiusRate, final double distance, final ParticleDisplay display) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, magicCircles(radius, rate, radiusRate, distance, display), 0L, 1L);
    }
    
    public static void infinity(final double radius, final double rate, final ParticleDisplay display) {
        for (double rateDiv = 3.141592653589793 / rate, i = 0.0; i < 6.283185307179586; i += rateDiv) {
            final double x = Math.sin(i);
            final double smooth = Math.pow(x, 2.0) + 1.0;
            final double curve = radius * Math.cos(i);
            final double z = curve / smooth;
            final double y = curve * x / smooth;
            circle(1.0, rate, display.cloneWithLocation(x, y, z));
        }
    }
    
    public static void cone(final double height, double radius, final double rate, final double circleRate, final ParticleDisplay display) {
        final double radiusDiv = radius / (height / rate);
        for (double i = 0.0; i < height; i += rate) {
            radius -= radiusDiv;
            if (radius < 0.0) {
                radius = 0.0;
            }
            circle(radius, circleRate - i, display.cloneWithLocation(0.0, i, 0.0));
        }
    }
    
    public static void slash(final double size, final boolean useWideSide, final ParticleDisplay display) {
        final double start = useWideSide ? 1.5707963267948966 : 0.0;
        final double end = useWideSide ? Particles.R270 : 3.141592653589793;
        ellipse(start, end, 0.10471975511965977, size, size + 2.0, display);
    }
    
    public static void slash(final Plugin plugin, final double distance, final boolean useWideSide, final Supplier<Double> size, final Supplier<Double> speed, final ParticleDisplay display) {
        new BukkitRunnable() {
            double distanceTraveled = 0.0;
            
            public void run() {
                Particles.slash((double)size.get(), useWideSide, display);
                final double speedConst = (double)speed.get();
                this.distanceTraveled += speedConst;
                if (this.distanceTraveled >= distance) {
                    this.cancel();
                }
                else {
                    display.advanceInDirection(speedConst);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 1L, 1L);
    }
    
    public static void ellipse(final double start, final double end, final double rate, final double radius, final double otherRadius, final ParticleDisplay display) {
        for (double theta = start; theta <= end; theta += rate) {
            final double x = radius * Math.cos(theta);
            final double z = otherRadius * Math.sin(theta);
            display.spawn(x, 0.0, z);
        }
    }
    
    public static BooleanSupplier blackhole(final int points, final double radius, final double rate, final int mode, final int time, final ParticleDisplay display) {
        display.extra = 0.1;
        return (BooleanSupplier)new BooleanSupplier() {
            final double rateDiv = 3.141592653589793 / rate;
            int timer = time;
            double theta = 0.0;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                for (int i = 0; i < points; ++i) {
                    final double angle = 6.283185307179586 * (i / (double)points);
                    double x = radius * Math.cos(this.theta + angle);
                    double z = radius * Math.sin(this.theta + angle);
                    double phi = Math.atan2(z, x);
                    double xDirection = -Math.cos(phi);
                    double zDirection = -Math.sin(phi);
                    display.particleDirection(xDirection, 0.0, zDirection);
                    display.spawn(x, 0.0, z);
                    if (mode > 1) {
                        x = radius * Math.cos(-this.theta + angle);
                        z = radius * Math.sin(-this.theta + angle);
                        if (mode == 2) {
                            phi = Math.atan2(z, x);
                        }
                        else if (mode == 3) {
                            phi = Math.atan2(x, z);
                        }
                        else if (mode == 4) {
                            Math.atan2(Math.log(x), Math.log(z));
                        }
                        xDirection = -Math.cos(phi);
                        zDirection = -Math.sin(phi);
                        display.particleDirection(xDirection, 0.0, zDirection);
                        display.spawn(x, 0.0, z);
                    }
                }
                this.theta += this.rateDiv;
                if (--this.timer <= 0) {
                    this.done = true;
                    return false;
                }
                return true;
            }
        };
    }
    
    public static BukkitTask blackhole(final Plugin plugin, final int points, final double radius, final double rate, final int mode, final int time, final ParticleDisplay display) {
        final BooleanSupplier blackhole = blackhole(points, radius, rate, mode, time, display);
        return new BukkitRunnable() {
            public void run() {
                if (!blackhole.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    public static void rainbow(double radius, final double rate, final double curve, final double layers, final double compact, ParticleDisplay display) {
        final int[][] rainbow = { { 128, 0, 128 }, { 75, 0, 130 }, { 0, 0, 255 }, { 0, 255, 0 }, { 255, 255, 0 }, { 255, 140, 0 }, { 255, 0, 0 } };
        final double secondRadius = radius * curve;
        for (int i = 0; i < 7; ++i) {
            final int[] rgb = rainbow[i];
            display = ParticleDisplay.of(XParticle.DUST).withLocation(display.getLocation()).withColor(new java.awt.Color(rgb[0], rgb[1], rgb[2]), 1.0f);
            for (int layer = 0; layer < layers; ++layer) {
                for (double rateDiv = 3.141592653589793 / (rate * (i + 2)), theta = 0.0; theta <= 3.141592653589793; theta += rateDiv) {
                    final double x = radius * Math.cos(theta);
                    final double y = secondRadius * Math.sin(theta);
                    display.spawn(x, y, 0.0);
                }
                radius += compact;
            }
        }
    }
    
    public static void crescent(final double radius, final double rate, final ParticleDisplay display) {
        for (double rateDiv = 3.141592653589793 / rate, end = Math.toRadians(325.0), theta = Math.toRadians(45.0); theta <= end; theta += rateDiv) {
            final double x = Math.cos(theta);
            final double z = Math.sin(theta);
            display.spawn(radius * x, 0.0, radius * z);
            final double smallerRadius = radius / 1.3;
            display.spawn(smallerRadius * x + 0.8, 0.0, smallerRadius * z);
        }
    }
    
    public static void waveFunction(final double extend, final double heightRange, double size, final double rate, final ParticleDisplay display) {
        double height = heightRange / 2.0;
        boolean increase = true;
        double increaseRandomizer = random(heightRange / 2.0, heightRange);
        final double rateDiv = 3.141592653589793 / rate;
        size *= 6.283185307179586;
        for (double x = 0.0; x <= size; x += rateDiv) {
            final double xx = extend * x;
            final double y1 = Math.sin(x);
            if (y1 == 1.0) {
                increase = !increase;
                if (increase) {
                    increaseRandomizer = random(heightRange / 2.0, heightRange);
                }
                else {
                    increaseRandomizer = random(-heightRange, -heightRange / 2.0);
                }
            }
            height += increaseRandomizer;
            for (double z = 0.0; z <= size; z += rateDiv) {
                final double y2 = Math.cos(z);
                final double yy = height * y1 * y2;
                final double zz = extend * z;
                display.spawn(xx, yy, zz);
            }
        }
    }
    
    public static Runnable vortex(final int points, final double rate, final ParticleDisplay display) {
        final double rateDiv = 3.141592653589793 / rate;
        return (Runnable)new Runnable() {
            double theta = 0.0;
            
            public void run() {
                this.theta += rateDiv;
                for (int i = 0; i < points; ++i) {
                    final double multiplier = 6.283185307179586 * (i / (double)points);
                    final double x = Math.cos(this.theta + multiplier);
                    final double z = Math.sin(this.theta + multiplier);
                    final double angle = Math.atan2(z, x);
                    final double xDirection = Math.cos(angle);
                    final double zDirection = Math.sin(angle);
                    display.particleDirection(xDirection, 0.0, zDirection);
                    display.spawn(x, 0.0, z);
                }
            }
        };
    }
    
    public static BukkitTask vortex(final Plugin plugin, final int points, final double rate, final ParticleDisplay display) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, vortex(points, rate, display), 0L, 1L);
    }
    
    public static void cylinder(final double height, final double radius, final double rate, final ParticleDisplay display) {
        filledCircle(radius, rate, 3.0, display);
        filledCircle(radius, rate, 3.0, display.cloneWithLocation(0.0, height, 0.0));
        for (double y = 0.0; y < height; y += 0.1) {
            circle(radius, rate, display.cloneWithLocation(0.0, y, 0.0));
        }
    }
    
    public static Runnable moveRotatingAround(final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return (Runnable)new Runnable() {
            double rotation = 180.0;
            
            public void run() {
                this.rotation += rate;
                final double x = Math.toRadians(90.0 + this.rotation);
                final double y = Math.toRadians(60.0 + this.rotation);
                final double z = Math.toRadians(30.0 + this.rotation);
                final Vector vector = new Vector(offsetx * 3.141592653589793, offsety * 3.141592653589793, offsetz * 3.141592653589793);
                if (offsetx != 0.0) {
                    ParticleDisplay.rotateAround(vector, ParticleDisplay.Axis.X, x);
                }
                if (offsety != 0.0) {
                    ParticleDisplay.rotateAround(vector, ParticleDisplay.Axis.Y, y);
                }
                if (offsetz != 0.0) {
                    ParticleDisplay.rotateAround(vector, ParticleDisplay.Axis.Z, z);
                }
                for (final ParticleDisplay display : displays) {
                    display.getLocation().add(vector);
                }
                runnable.run();
                for (final ParticleDisplay display : displays) {
                    display.getLocation().subtract(vector);
                }
            }
        };
    }
    
    public static BukkitTask moveRotatingAround(final Plugin plugin, final long update, final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, moveRotatingAround(rate, offsetx, offsety, offsetz, runnable, displays), 0L, update);
    }
    
    public static Runnable moveAround(final double rate, final double endRate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return (Runnable)new Runnable() {
            double multiplier = 0.0;
            boolean opposite = false;
            
            public void run() {
                if (this.opposite) {
                    this.multiplier -= rate;
                }
                else {
                    this.multiplier += rate;
                }
                final double x = this.multiplier * offsetx;
                final double y = this.multiplier * offsety;
                final double z = this.multiplier * offsetz;
                for (final ParticleDisplay display : displays) {
                    display.getLocation().add(x, y, z);
                }
                runnable.run();
                for (final ParticleDisplay display : displays) {
                    display.getLocation().subtract(x, y, z);
                }
                if (this.opposite) {
                    if (this.multiplier <= 0.0) {
                        this.opposite = false;
                    }
                }
                else if (this.multiplier >= endRate) {
                    this.opposite = true;
                }
            }
        };
    }
    
    public static BukkitTask moveAround(final Plugin plugin, final long update, final double rate, final double endRate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, moveAround(rate, endRate, offsetx, offsety, offsetz, runnable, displays), 0L, update);
    }
    
    public static BukkitTask testDisplay(final Plugin plugin, final Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, 1L);
    }
    
    public static Runnable rotateAround(final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return (Runnable)new Runnable() {
            double rotation = 180.0;
            
            public void run() {
                this.rotation += rate;
                final double x = Math.toRadians((90.0 + this.rotation) * offsetx);
                final double y = Math.toRadians((60.0 + this.rotation) * offsety);
                final double z = Math.toRadians((30.0 + this.rotation) * offsetz);
                for (final ParticleDisplay display : displays) {
                    display.rotate(x, y, z);
                }
                runnable.run();
            }
        };
    }
    
    public static BukkitTask rotateAround(final Plugin plugin, final long update, final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, rotateAround(rate, offsetx, offsety, offsetz, runnable, displays), 0L, update);
    }
    
    public static Runnable guard(final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return (Runnable)new Runnable() {
            double rotation = 180.0;
            
            public void run() {
                this.rotation += rate;
                final double x = Math.toRadians((90.0 + this.rotation) * offsetx);
                final double y = Math.toRadians((60.0 + this.rotation) * offsety);
                final double z = Math.toRadians((30.0 + this.rotation) * offsetz);
                final Vector vector = new Vector(offsetx * 3.141592653589793, offsety * 3.141592653589793, offsetz * 3.141592653589793);
                ParticleDisplay.rotateAround(vector, x, y, z);
                for (final ParticleDisplay display : displays) {
                    display.rotate(x, y, z);
                    display.getLocation().add(vector);
                }
                runnable.run();
                for (final ParticleDisplay display : displays) {
                    display.getLocation().subtract(vector);
                }
            }
        };
    }
    
    public static BukkitTask guard(final Plugin plugin, final long update, final double rate, final double offsetx, final double offsety, final double offsetz, final Runnable runnable, final ParticleDisplay... displays) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, guard(rate, offsetx, offsety, offsetz, runnable, displays), 0L, update);
    }
    
    public static void sphere(final double radius, final double rate, final ParticleDisplay display) {
        for (double rateDiv = 3.141592653589793 / rate, phi = 0.0; phi <= 3.141592653589793; phi += rateDiv) {
            final double y1 = radius * Math.cos(phi);
            final double y2 = radius * Math.sin(phi);
            for (double theta = 0.0; theta <= 6.283185307179586; theta += rateDiv) {
                final double x = Math.cos(theta) * y2;
                final double z = Math.sin(theta) * y2;
                if (display.isDirectional()) {
                    final double omega = Math.atan2(z, x);
                    final double directionX = Math.cos(omega);
                    final double directionY = Math.sin(Math.atan2(y2, y1));
                    final double directionZ = Math.sin(omega);
                    display.particleDirection(directionX, directionY, directionZ);
                }
                display.spawn(x, y1, z);
            }
        }
    }
    
    public static void spikeSphere(final double radius, final double rate, final int chance, final double minRandomDistance, final double maxRandomDistance, final ParticleDisplay display) {
        for (double rateDiv = 3.141592653589793 / rate, phi = 0.0; phi <= 3.141592653589793; phi += rateDiv) {
            final double y = radius * Math.cos(phi);
            final double sinPhi = radius * Math.sin(phi);
            for (double theta = 0.0; theta <= 6.283185307179586; theta += rateDiv) {
                final double x = Math.cos(theta) * sinPhi;
                final double z = Math.sin(theta) * sinPhi;
                if (chance == 0 || randInt(0, chance) == 1) {
                    final Location start = display.cloneLocation(x, y, z);
                    final Vector endVect = start.clone().subtract(display.getLocation()).toVector().multiply(random(minRandomDistance, maxRandomDistance));
                    final Location end = start.clone().add(endVect);
                    line(start, end, 0.1, display);
                }
            }
        }
    }
    
    public static void ring(final double rate, final double radius, final double tubeRadius, final ParticleDisplay display) {
        final double rateDiv = 3.141592653589793 / rate;
        final double tubeDiv = 3.141592653589793 / tubeRadius;
        for (double theta = 0.0; theta <= 6.283185307179586; theta += rateDiv) {
            final double cos = Math.cos(theta);
            final double sin = Math.sin(theta);
            for (double phi = 0.0; phi <= 6.283185307179586; phi += tubeDiv) {
                final double finalRadius = radius + tubeRadius * Math.cos(phi);
                final double x = finalRadius * cos;
                final double y = finalRadius * sin;
                final double z = tubeRadius * Math.sin(phi);
                display.spawn(x, y, z);
            }
        }
    }
    
    public static BooleanSupplier spread(final int amount, final int rate, final Location start, final Location originEnd, final double offsetx, final double offsety, final double offsetz, final ParticleDisplay display) {
        return (BooleanSupplier)new BooleanSupplier() {
            int count = amount;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                int frame = rate;
                while (frame-- != 0) {
                    final double x = Particles.random(-offsetx, offsetx);
                    final double y = Particles.random(-offsety, offsety);
                    final double z = Particles.random(-offsetz, offsetz);
                    final Location end = originEnd.clone().add(x, y, z);
                    Particles.line(start, end, 0.1, display);
                }
                if (this.count-- <= 0) {
                    this.done = true;
                    return false;
                }
                return true;
            }
        };
    }
    
    public static BukkitTask spread(final Plugin plugin, final int amount, final int rate, final Location start, final Location originEnd, final double offsetx, final double offsety, final double offsetz, final ParticleDisplay display) {
        final BooleanSupplier spread = spread(amount, rate, start, originEnd, offsetx, offsety, offsetz, display);
        return new BukkitRunnable() {
            public void run() {
                if (!spread.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    public static void heart(final double cut, final double cutAngle, final double depth, final double compressHeight, final double rate, final ParticleDisplay display) {
        for (double theta = 0.0; theta <= 6.283185307179586; theta += 3.141592653589793 / rate) {
            final double phi = theta / cut;
            final double cos = Math.cos(phi);
            final double sin = Math.sin(phi);
            final double omega = Math.pow(Math.abs(Math.sin(2.0 * cutAngle * phi)) + depth * Math.abs(Math.sin(cutAngle * phi)), 1.0 / compressHeight);
            final double y = omega * (sin + cos);
            final double z = omega * (cos - sin);
            display.spawn(0.0, y, z);
        }
    }
    
    public static Runnable atomic(final int orbits, final double radius, final double rate, final ParticleDisplay orbit) {
        return (Runnable)new Runnable() {
            final double rateDiv = 3.141592653589793 / rate;
            final double dist = 3.141592653589793 / orbits;
            double theta = 0.0;
            
            public void run() {
                int orbital = orbits;
                this.theta += this.rateDiv;
                final double x = radius * Math.cos(this.theta);
                final double z = radius * Math.sin(this.theta);
                for (double angle = 0.0; orbital > 0; --orbital, angle += this.dist) {
                    orbit.rotate(ParticleDisplay.Rotation.of(angle, ParticleDisplay.Axis.Z));
                    orbit.spawn(x, 0.0, z);
                }
            }
        };
    }
    
    public static BukkitTask atomic(final Plugin plugin, final int orbits, final double radius, final double rate, final ParticleDisplay orbit) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, atomic(orbits, radius, rate, orbit), 0L, 1L);
    }
    
    public static BooleanSupplier helix(final int strings, final double radius, final double rate, final double extension, final double length, final double speed, final double rotationRate, final boolean fadeUp, final boolean fadeDown, final ParticleDisplay display) {
        return (BooleanSupplier)new BooleanSupplier() {
            final double distanceBetweenEachCirclePoints = 6.283185307179586 / strings;
            final double radiusDiv = radius / (length / rate);
            final double radiusDiv2 = (fadeUp && fadeDown) ? (this.radiusDiv * 2.0) : this.radiusDiv;
            double dynamicRadius = fadeDown ? 0.0 : radius;
            boolean center = !fadeDown;
            final double calculatedRotRate = this.distanceBetweenEachCirclePoints / rotationRate;
            double rotation = 0.0;
            double currentDistance = 0.0;
            
            public boolean getAsBoolean() {
                if (this.currentDistance >= length) {
                    return false;
                }
                if (!this.center) {
                    this.dynamicRadius += this.radiusDiv2;
                    if (this.dynamicRadius >= radius) {
                        this.center = true;
                    }
                }
                else if (fadeUp) {
                    this.dynamicRadius -= this.radiusDiv2;
                }
                for (double i = 0.0; i < strings; ++i) {
                    final double angle = i * this.distanceBetweenEachCirclePoints * extension + this.rotation;
                    final double x = this.dynamicRadius * Math.cos(angle);
                    final double z = this.dynamicRadius * Math.sin(angle);
                    display.spawn(x, 0.0, z);
                }
                this.currentDistance += speed;
                if (this.currentDistance < length) {
                    display.advanceInDirection(speed);
                }
                else {
                    display.advanceInDirection(speed - (this.currentDistance - length));
                }
                this.rotation += this.calculatedRotRate;
                return true;
            }
        };
    }
    
    public static BukkitTask helix(final Plugin plugin, final int strings, final double radius, final double rate, final double extension, final double height, final double speed, final double rotationRate, final boolean fadeUp, final boolean fadeDown, final ParticleDisplay display) {
        final BooleanSupplier helix = helix(strings, radius, rate, extension, height, speed, rotationRate, fadeUp, fadeDown, display);
        return new BukkitRunnable() {
            public void run() {
                if (!helix.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    public static void lightning(final Location start, final Vector direction, final int entries, final int branches, final double radius, final double offset, final double offsetRate, final double length, final double lengthRate, final double branch, final double branchRate, final ParticleDisplay display) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        if (entries <= 0) {
            return;
        }
        boolean inRange = true;
        while (random.nextDouble() < branch || inRange) {
            final Vector randomizer = new Vector(random.nextDouble(-radius, radius), random.nextDouble(-radius, radius), random.nextDouble(-radius, radius)).normalize().multiply(random.nextDouble(-radius, radius) * offset);
            final Vector endVector = start.clone().toVector().add(direction.clone().multiply(length)).add(randomizer);
            final Location end = endVector.toLocation(start.getWorld());
            if (end.distance(start) <= length) {
                inRange = true;
            }
            else {
                inRange = false;
                final int rate = (int)(start.distance(end) / 0.1);
                final Vector rateDir = endVector.clone().subtract(start.toVector()).normalize().multiply(0.1);
                for (int i = 0; i < rate; ++i) {
                    final Location loc = start.clone().add(rateDir.clone().multiply(i));
                    display.spawn(loc);
                }
                lightning(end.clone(), direction, entries - 1, branches - 1, radius, offset * offsetRate, offsetRate, length * lengthRate, lengthRate, branch * branchRate, branchRate, display);
                if (branches <= 0) {
                    break;
                }
                continue;
            }
        }
    }
    
    public static void dna(final double radius, final double rate, final double extension, final int height, final int hydrogenBondDist, final ParticleDisplay display, final ParticleDisplay hydrogenBondDisplay) {
        int nucleotideDist = 0;
        for (double y = 0.0; y <= height; y += rate) {
            ++nucleotideDist;
            final double x = radius * Math.cos(extension * y);
            final double z = radius * Math.sin(extension * y);
            final Location nucleotide1 = display.getLocation().clone().add(x, y, z);
            display.spawn(x, y, z);
            final Location nucleotide2 = display.getLocation().clone().subtract(x, -y, z);
            display.spawn(-x, y, -z);
            if (nucleotideDist >= hydrogenBondDist) {
                nucleotideDist = 0;
                line(nucleotide1, nucleotide2, rate * 2.0, hydrogenBondDisplay);
            }
        }
    }
    
    public static BooleanSupplier dnaReplication(final double radius, final double rate, final int speed, final double extension, final int height, final int hydrogenBondDist, final ParticleDisplay display) {
        final ParticleDisplay adenine = ParticleDisplay.of(XParticle.DUST).withColor(java.awt.Color.BLUE, 1.0f);
        final ParticleDisplay thymine = ParticleDisplay.of(XParticle.DUST).withColor(java.awt.Color.YELLOW, 1.0f);
        final ParticleDisplay guanine = ParticleDisplay.of(XParticle.DUST).withColor(java.awt.Color.GREEN, 1.0f);
        final ParticleDisplay cytosine = ParticleDisplay.of(XParticle.DUST).withColor(java.awt.Color.RED, 1.0f);
        return (BooleanSupplier)new BooleanSupplier() {
            double y = 0.0;
            int nucleotideDist = 0;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                int repeat = speed;
                while (repeat-- != 0) {
                    this.y += rate;
                    ++this.nucleotideDist;
                    final double x = radius * Math.cos(extension * this.y);
                    final double z = radius * Math.sin(extension * this.y);
                    final Location nucleotide1 = display.getLocation().clone().add(x, this.y, z);
                    Particles.circle(0.1, 10.0, display.cloneWithLocation(x, this.y, z));
                    final Location nucleotide2 = display.getLocation().clone().subtract(x, -this.y, z);
                    Particles.circle(0.1, 10.0, display.cloneWithLocation(-x, this.y, -z));
                    final Location midPointBond = nucleotide1.toVector().midpoint(nucleotide2.toVector()).toLocation(nucleotide1.getWorld());
                    if (this.nucleotideDist >= hydrogenBondDist) {
                        this.nucleotideDist = 0;
                        if (Particles.randInt(0, 1) == 1) {
                            Particles.line(nucleotide1, midPointBond, rate - 0.1, adenine);
                            Particles.line(nucleotide2, midPointBond, rate - 0.1, thymine);
                        }
                        else {
                            Particles.line(nucleotide1, midPointBond, rate - 0.1, cytosine);
                            Particles.line(nucleotide2, midPointBond, rate - 0.1, guanine);
                        }
                    }
                    if (this.y > height) {
                        this.done = true;
                        return false;
                    }
                }
                return true;
            }
        };
    }
    
    public static BukkitTask dnaReplication(final Plugin plugin, final double radius, final double rate, final int speed, final double extension, final int height, final int hydrogenBondDist, final ParticleDisplay display) {
        final BooleanSupplier dnaReplication = dnaReplication(radius, rate, speed, extension, height, hydrogenBondDist, display);
        return new BukkitRunnable() {
            public void run() {
                if (!dnaReplication.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    public static void drawLine(final Player player, final double length, final double rate, final ParticleDisplay display) {
        final Location eye = player.getEyeLocation();
        line(eye, eye.clone().add(eye.getDirection().multiply(length)), rate, display);
    }
    
    public static Runnable cloud(final ParticleDisplay cloud, final ParticleDisplay rain) {
        return () -> {
            cloud.spawn();
            rain.spawn();
        };
    }
    
    public static BukkitTask cloud(final Plugin plugin, final ParticleDisplay cloud, final ParticleDisplay rain) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, cloud(cloud, rain), 0L, 1L);
    }
    
    public static void line(final Location start, final Location end, double rate, final ParticleDisplay display) {
        rate = Math.abs(rate);
        double x = end.getX() - start.getX();
        double y = end.getY() - start.getY();
        double z = end.getZ() - start.getZ();
        final double length = Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
        x /= length;
        y /= length;
        z /= length;
        final ParticleDisplay clone = display.copy();
        clone.withLocation(start);
        for (double i = 0.0; i < length; i += rate) {
            if (i > length) {
                i = length;
            }
            clone.spawn(x * i, y * i, z * i);
        }
    }
    
    public static void rectangle(final Location start, final Location end, final double rate, final ParticleDisplay display) {
        display.withLocation(start);
        final double maxX = Math.max(start.getX(), end.getX());
        final double minX = Math.min(start.getX(), end.getX());
        final double maxY = Math.max(start.getY(), end.getY());
        final double minY = Math.min(start.getY(), end.getY());
        for (double x = minX; x <= maxX; x += rate) {
            for (double y = minY; y <= maxY; y += rate) {
                display.spawn(x - minX, y - minY, 0.0);
            }
        }
    }
    
    public static void cage(final Location start, final Location end, final double rate, final double barRate, final ParticleDisplay display) {
        final double maxX = Math.max(start.getX(), end.getX());
        final double minX = Math.min(start.getX(), end.getX());
        final double maxZ = Math.max(start.getZ(), end.getZ());
        final double minZ = Math.min(start.getZ(), end.getZ());
        double barChance = 0.0;
        for (double x = minX; x <= maxX; x += rate) {
            for (double z = minZ; z <= maxZ; z += rate) {
                final Location barStart = display.spawn(x - minX, 0.0, z - minZ);
                final Location barEnd = display.spawn(x - minX, 3.0, z - minZ);
                if (x == minX || x + rate > maxX || z == minZ || z + rate > maxZ) {
                    ++barChance;
                    if (barChance >= barRate) {
                        barChance = 0.0;
                        line(barStart, barEnd, rate, display);
                    }
                }
            }
        }
    }
    
    public static void filledCube(final Location start, final Location end, final double rate, final ParticleDisplay display) {
        display.withLocation(start);
        final double maxX = Math.max(start.getX(), end.getX());
        final double minX = Math.min(start.getX(), end.getX());
        final double maxY = Math.max(start.getY(), end.getY());
        final double minY = Math.min(start.getY(), end.getY());
        final double maxZ = Math.max(start.getZ(), end.getZ());
        final double minZ = Math.min(start.getZ(), end.getZ());
        for (double x = minX; x <= maxX; x += rate) {
            for (double y = minY; y <= maxY; y += rate) {
                for (double z = minZ; z <= maxZ; z += rate) {
                    display.spawn(x - minX, y - minY, z - minZ);
                }
            }
        }
    }
    
    public static void cube(final Location start, final Location end, final double rate, final ParticleDisplay display) {
        display.withLocation(start);
        final double maxX = Math.max(start.getX(), end.getX());
        final double minX = Math.min(start.getX(), end.getX());
        final double maxY = Math.max(start.getY(), end.getY());
        final double minY = Math.min(start.getY(), end.getY());
        final double maxZ = Math.max(start.getZ(), end.getZ());
        final double minZ = Math.min(start.getZ(), end.getZ());
        for (double x = minX; x <= maxX; x += rate) {
            for (double y = minY; y <= maxY; y += rate) {
                for (double z = minZ; z <= maxZ; z += rate) {
                    if (y == minY || y + rate > maxY || x == minX || x + rate > maxX || z == minZ || z + rate > maxZ) {
                        display.spawn(x - minX, y - minY, z - minZ);
                    }
                }
            }
        }
    }
    
    public static void structuredCube(final Location start, final Location end, final double rate, final ParticleDisplay display) {
        display.withLocation(start);
        final double maxX = Math.max(start.getX(), end.getX());
        final double minX = Math.min(start.getX(), end.getX());
        final double maxY = Math.max(start.getY(), end.getY());
        final double minY = Math.min(start.getY(), end.getY());
        final double maxZ = Math.max(start.getZ(), end.getZ());
        final double minZ = Math.min(start.getZ(), end.getZ());
        for (double x = minX; x <= maxX; x += rate) {
            for (double y = minY; y <= maxY; y += rate) {
                for (double z = minZ; z <= maxZ; z += rate) {
                    int components = 0;
                    if (x == minX || x + rate > maxX) {
                        ++components;
                    }
                    if (y == minY || y + rate > maxY) {
                        ++components;
                    }
                    if (z == minZ || z + rate > maxZ) {
                        ++components;
                    }
                    if (components >= 2) {
                        display.spawn(x - minX, y - minY, z - minZ);
                    }
                }
            }
        }
    }
    
    public static void hypercube(final Location startOrigin, final Location endOrigin, final double rate, final double sizeRate, final int cubes, final ParticleDisplay display) {
        List<Location> previousPoints = null;
        for (int i = 0; i < cubes + 1; ++i) {
            final List<Location> points = (List<Location>)new ArrayList(8);
            final Location start = startOrigin.clone().subtract(i * sizeRate, i * sizeRate, i * sizeRate);
            final Location end = endOrigin.clone().add(i * sizeRate, i * sizeRate, i * sizeRate);
            display.withLocation(start);
            final double maxX = Math.max(start.getX(), end.getX());
            final double minX = Math.min(start.getX(), end.getX());
            final double maxY = Math.max(start.getY(), end.getY());
            final double minY = Math.min(start.getY(), end.getY());
            final double maxZ = Math.max(start.getZ(), end.getZ());
            final double minZ = Math.min(start.getZ(), end.getZ());
            points.add((Object)new Location(start.getWorld(), maxX, maxY, maxZ));
            points.add((Object)new Location(start.getWorld(), minX, minY, minZ));
            points.add((Object)new Location(start.getWorld(), maxX, minY, maxZ));
            points.add((Object)new Location(start.getWorld(), minX, maxY, minZ));
            points.add((Object)new Location(start.getWorld(), minX, minY, maxZ));
            points.add((Object)new Location(start.getWorld(), maxX, minY, minZ));
            points.add((Object)new Location(start.getWorld(), maxX, maxY, minZ));
            points.add((Object)new Location(start.getWorld(), minX, maxY, maxZ));
            if (previousPoints != null) {
                for (int p = 0; p < 8; ++p) {
                    final Location current = (Location)points.get(p);
                    final Location previous = (Location)previousPoints.get(p);
                    line(previous, current, rate, display);
                }
            }
            previousPoints = points;
            for (double x = minX; x <= maxX; x += rate) {
                for (double y = minY; y <= maxY; y += rate) {
                    for (double z = minZ; z <= maxZ; z += rate) {
                        int components = 0;
                        if (x == minX || x + rate > maxX) {
                            ++components;
                        }
                        if (y == minY || y + rate > maxY) {
                            ++components;
                        }
                        if (z == minZ || z + rate > maxZ) {
                            ++components;
                        }
                        if (components >= 2) {
                            display.spawn(x - minX, y - minY, z - minZ);
                        }
                    }
                }
            }
        }
    }
    
    public static BooleanSupplier tesseract(final double size, final double rate, final double speed, final long ticks, final ParticleDisplay display) {
        final double[][] positions = { { -1.0, -1.0, -1.0, 1.0 }, { 1.0, -1.0, -1.0, 1.0 }, { 1.0, 1.0, -1.0, 1.0 }, { -1.0, 1.0, -1.0, 1.0 }, { -1.0, -1.0, 1.0, 1.0 }, { 1.0, -1.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0, 1.0 }, { -1.0, 1.0, 1.0, 1.0 }, { -1.0, -1.0, -1.0, -1.0 }, { 1.0, -1.0, -1.0, -1.0 }, { 1.0, 1.0, -1.0, -1.0 }, { -1.0, 1.0, -1.0, -1.0 }, { -1.0, -1.0, 1.0, -1.0 }, { 1.0, -1.0, 1.0, -1.0 }, { 1.0, 1.0, 1.0, -1.0 }, { -1.0, 1.0, 1.0, -1.0 } };
        final List<int[]> connections = (List<int[]>)new ArrayList();
        final int level = 1;
        for (int h = 0; h <= level; ++h) {
            int i;
            for (int start = i = 8 * h; i < start + 4; ++i) {
                connections.add((Object)new int[] { i, (i + 1) % 4 + start });
                connections.add((Object)new int[] { i + 4, (i + 1) % 4 + 4 + start });
                connections.add((Object)new int[] { i, i + 4 });
            }
        }
        for (int j = 0; j < (level + 1) * 4; ++j) {
            connections.add((Object)new int[] { j, j + 8 });
        }
        return (BooleanSupplier)new BooleanSupplier() {
            double angle = 0.0;
            long repeat = 0L;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                final double cos = Math.cos(this.angle);
                final double sin = Math.sin(this.angle);
                final double[][] rotationXY = { { cos, -sin, 0.0, 0.0 }, { sin, cos, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };
                final double[][] rotationZW = { { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, cos, -sin }, { 0.0, 0.0, sin, cos } };
                final double[][] projected3D = new double[positions.length][4];
                for (int i = 0; i < positions.length; ++i) {
                    final double[] point = positions[i];
                    double[] rotated = matrix(rotationXY, point);
                    rotated = matrix(rotationZW, rotated);
                    final int distance = 2;
                    final double w = 1.0 / (distance - rotated[3]);
                    final double[][] projection = { { w, 0.0, 0.0, 0.0 }, { 0.0, w, 0.0, 0.0 }, { 0.0, 0.0, w, 0.0 } };
                    final double[] projected = matrix(projection, rotated);
                    for (int proj = 0; proj < projected.length; ++proj) {
                        final double[] array = projected;
                        final int n = proj;
                        array[n] *= size;
                    }
                    projected3D[i] = projected;
                    display.spawn(projected[0], projected[1], projected[2]);
                }
                for (final int[] connection : connections) {
                    final double[] pointA = projected3D[connection[0]];
                    final double[] pointB = projected3D[connection[1]];
                    final Location start = display.cloneLocation(pointA[0], pointA[1], pointA[2]);
                    final Location end = display.cloneLocation(pointB[0], pointB[1], pointB[2]);
                    Particles.line(start, end, rate, display);
                }
                final long repeat = this.repeat + 1L;
                this.repeat = repeat;
                if (repeat > ticks) {
                    this.done = true;
                    return false;
                }
                this.angle += speed;
                return true;
            }
        };
    }
    
    public static BukkitTask tesseract(final Plugin plugin, final double size, final double rate, final double speed, final long ticks, final ParticleDisplay display) {
        final BooleanSupplier tesseract = tesseract(size, rate, speed, ticks, display);
        return new BukkitRunnable() {
            public void run() {
                if (!tesseract.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    private static double[] matrix(final double[][] a, final double[] m) {
        final double[][] b = new double[4][1];
        b[0][0] = m[0];
        b[1][0] = m[1];
        b[2][0] = m[2];
        b[3][0] = m[3];
        final int colsA = a[0].length;
        final int rowsA = a.length;
        final int colsB = b[0].length;
        final int rowsB = b.length;
        final double[][] result = new double[rowsA][rowsB];
        for (int i = 0; i < rowsA; ++i) {
            for (int j = 0; j < colsB; ++j) {
                float sum = 0.0f;
                for (int k = 0; k < colsA; ++k) {
                    sum += (float)(a[i][k] * b[k][j]);
                }
                result[i][j] = sum;
            }
        }
        final double[] v = { result[0][0], result[1][0], result[2][0], 0.0 };
        if (result.length > 3) {
            v[3] = result[3][0];
        }
        return v;
    }
    
    public static void mandelbrot(final double size, final double zoom, final double rate, final double x0, final double y0, final int color, final ParticleDisplay display) {
        for (double y = -size; y < size; y += rate) {
            for (double x = -size; x < size; x += rate) {
                double zy = 0.0;
                double zx = 0.0;
                final double cX = (x - x0) / zoom;
                double cY;
                int iteration;
                double xtemp;
                for (cY = (y - y0) / zoom, iteration = color; zx * zx + zy * zy <= 4.0 && iteration > 0; zy = 2.0 * zx * zy + cY, zx = xtemp, --iteration) {
                    xtemp = zx * zx - zy * zy + cX;
                }
                if (iteration == 0) {
                    display.spawn(x, y, 0.0);
                }
            }
        }
    }
    
    public static void julia(final double size, final double zoom, final int colorScheme, final double moveX, final double moveY, final ParticleDisplay display) {
        final double cx = -0.7;
        final double cy = 0.27015;
        for (double x = -size; x < size; x += 0.1) {
            for (double y = -size; y < size; y += 0.1) {
                double zx;
                double zy;
                int i;
                double xtemp;
                for (zx = 1.5 * (size - size / 2.0) / (0.5 * zoom * size) + moveX, zy = (y - size / 2.0) / (0.5 * zoom * size) + moveY, i = colorScheme; zx * zx + zy * zy < 4.0 && i > 0; zy = 2.0 * zx * zy + cy, zx = xtemp, --i) {
                    xtemp = zx * zx - zy * zy + cx;
                }
                final java.awt.Color color = new java.awt.Color((i << 21) + (i << 10) + i * 8);
                display.withColor(color, 0.8f).spawn(x, y, 0.0);
            }
        }
    }
    
    public static List<BooleanSupplier> star(final int points, final int spikes, final double rate, final double spikeLength, final double coreRadius, final double neuron, final boolean prototype, final int speed, final ParticleDisplay display) {
        final double pointsRate = 6.283185307179586 / points;
        final double rateDiv = 3.141592653589793 / rate;
        final ThreadLocalRandom random = prototype ? null : ThreadLocalRandom.current();
        final List<BooleanSupplier> tasks = (List<BooleanSupplier>)new ArrayList();
        for (int i = 0; i < spikes * 2; ++i) {
            final double spikeAngle = i * 3.141592653589793 / spikes;
            tasks.add((Object)new BooleanSupplier() {
                double vein = 0.0;
                double theta = 0.0;
                boolean done = false;
                
                public boolean getAsBoolean() {
                    if (this.done) {
                        return false;
                    }
                    int repeat = speed;
                    while (repeat-- != 0) {
                        this.theta += rateDiv;
                        final double height = (prototype ? this.vein : random.nextDouble(0.0, neuron)) * spikeLength;
                        if (prototype) {
                            this.vein += neuron;
                        }
                        final Vector vector = new Vector(Math.cos(this.theta), 0.0, Math.sin(this.theta));
                        vector.multiply((spikeLength - height) * coreRadius / spikeLength);
                        vector.setY(coreRadius + height);
                        ParticleDisplay.rotateAround(vector, ParticleDisplay.Axis.X, spikeAngle);
                        for (int j = 0; j < points; ++j) {
                            ParticleDisplay.rotateAround(vector, ParticleDisplay.Axis.Y, pointsRate);
                            display.spawn(vector);
                        }
                    }
                    if (this.theta >= 6.283185307179586) {
                        this.done = true;
                        return false;
                    }
                    return true;
                }
            });
        }
        return tasks;
    }
    
    public static List<BukkitTask> star(final Plugin plugin, final int points, final int spikes, final double rate, final double spikeLength, final double coreRadius, final double neuron, final boolean prototype, final int speed, final ParticleDisplay display) {
        final List<BukkitTask> tasks = (List<BukkitTask>)new ArrayList();
        for (final BooleanSupplier task : star(points, spikes, rate, spikeLength, coreRadius, neuron, prototype, speed, display)) {
            tasks.add((Object)new BukkitRunnable() {
                public void run() {
                    if (!task.getAsBoolean()) {
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(plugin, 0L, 1L));
        }
        return tasks;
    }
    
    public static void eye(final double radius, final double radius2, final double rate, final double extension, final ParticleDisplay display) {
        final double rateDiv = 3.141592653589793 / rate;
        final double limit = 3.141592653589793 / extension;
        double x = 0.0;
        for (double i = 0.0; i < limit; i += rateDiv) {
            final double y = radius * Math.sin(extension * i);
            final double y2 = radius2 * Math.sin(extension * -i);
            display.spawn(x, y, 0.0);
            display.spawn(x, y2, 0.0);
            x += 0.1;
        }
    }
    
    public static void illuminati(final double size, final double extension, final ParticleDisplay display) {
        polygon(3, 1, size, 1.0 / (size * 30.0), 0.0, display);
        eye(size / 4.0, size / 4.0, 30.0, extension, display.cloneWithLocation(0.3, 0.0, size / 1.8).rotate(1.5707963267948966, 1.5707963267948966, 0.0));
        circle(size / 5.0, size * 5.0, display.cloneWithLocation(0.3, 0.0, 0.0));
    }
    
    public static void polygon(final int points, final int connection, final double size, final double rate, final double extend, final ParticleDisplay display) {
        for (int point = 0; point < points; ++point) {
            final double angle = Math.toRadians(360.0 / points * point);
            final double nextAngle = Math.toRadians(360.0 / points * (point + connection));
            final double x = Math.cos(angle) * size;
            final double z = Math.sin(angle) * size;
            final double x2 = Math.cos(nextAngle) * size;
            final double z2 = Math.sin(nextAngle) * size;
            final double deltaX = x2 - x;
            final double deltaZ = z2 - z;
            for (double pos = 0.0; pos < 1.0 + extend; pos += rate) {
                final double x3 = x + deltaX * pos;
                final double z3 = z + deltaZ * pos;
                display.spawn(x3, 0.0, z3);
            }
        }
    }
    
    public static void neopaganPentagram(final double size, final double rate, final double extend, final ParticleDisplay star, final ParticleDisplay circle) {
        polygon(5, 2, size, rate, extend, star);
        circle(size + 0.5, rate * 1000.0, circle);
    }
    
    public static void atom(int orbits, final double radius, final double rate, final ParticleDisplay orbit, final ParticleDisplay nucleus) {
        for (double dist = 3.141592653589793 / orbits, angle = 0.0; orbits > 0; --orbits, angle += dist) {
            orbit.rotate(ParticleDisplay.Rotation.of(angle, ParticleDisplay.Axis.Z));
            circle(radius, rate, orbit);
        }
        sphere(radius / 3.0, rate / 2.0, nucleus);
    }
    
    public static BooleanSupplier meguminExplosion(final double size, final ParticleDisplay display) {
        final BooleanSupplier spread = spread(30, 2, display.getLocation(), display.getLocation().clone().add(0.0, 10.0, 0.0), 5.0, 5.0, 5.0, display);
        return (BooleanSupplier)new BooleanSupplier() {
            boolean first = true;
            
            public boolean getAsBoolean() {
                if (this.first) {
                    this.first = false;
                    Particles.polygon(10, 4, size, 0.02, 0.3, display);
                    Particles.polygon(10, 3, size / (size - 1.0), 0.5, 0.0, display);
                    Particles.circle(size, 40.0, display);
                }
                return spread.getAsBoolean();
            }
        };
    }
    
    public static BukkitTask meguminExplosion(final Plugin plugin, final double size, final ParticleDisplay display) {
        final BooleanSupplier explosion = meguminExplosion(size, display);
        return new BukkitRunnable() {
            public void run() {
                if (!explosion.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    public static BooleanSupplier explosionWave(final double rate, final ParticleDisplay display, final ParticleDisplay secDisplay) {
        return (BooleanSupplier)new BooleanSupplier() {
            static final double addition = 0.3141592653589793;
            final double rateDiv = 3.141592653589793 / rate;
            double times = 0.7853981633974483;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                this.times += 0.3141592653589793;
                for (double theta = 0.0; theta <= 6.283185307179586; theta += this.rateDiv) {
                    double x = this.times * Math.cos(theta);
                    final double y = 2.0 * Math.exp(-0.1 * this.times) * Math.sin(this.times) + 1.5;
                    double z = this.times * Math.sin(theta);
                    display.spawn(x, y, z);
                    theta += 0.04908738521234052;
                    x = this.times * Math.cos(theta);
                    z = this.times * Math.sin(theta);
                    secDisplay.spawn(x, y, z);
                }
                if (this.times > 20.0) {
                    this.done = true;
                    return false;
                }
                return true;
            }
        };
    }
    
    public static BukkitTask explosionWave(final Plugin plugin, final double rate, final ParticleDisplay display, final ParticleDisplay secDisplay) {
        final BooleanSupplier explosionWave = explosionWave(rate, display, secDisplay);
        return new BukkitRunnable() {
            public void run() {
                if (!explosionWave.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1L);
    }
    
    private static BufferedImage getImage(final Path path) {
        if (!Files.exists(path, new LinkOption[0])) {
            return null;
        }
        try {
            return ImageIO.read(Files.newInputStream(path, new OpenOption[] { (OpenOption)StandardOpenOption.READ }));
        }
        catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static CompletableFuture<BufferedImage> getScaledImage(final Path path, final int width, final int height) {
        return (CompletableFuture<BufferedImage>)CompletableFuture.supplyAsync(() -> {
            final BufferedImage image = getImage(path);
            if (image == null) {
                return null;
            }
            int finalHeight = height;
            int finalWidth = width;
            if (image.getWidth() > image.getHeight()) {
                finalHeight = width * image.getHeight() / image.getWidth();
            }
            else {
                finalWidth = height * image.getWidth() / image.getHeight();
            }
            final BufferedImage resizedImg = new BufferedImage(width, height, 2);
            final Graphics2D graphics = resizedImg.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage((Image)image, 0, 0, finalWidth, finalHeight, (ImageObserver)null);
            graphics.dispose();
            return resizedImg;
        });
    }
    
    public static CompletableFuture<Map<double[], Color>> renderImage(final Path path, final int resizedWidth, final int resizedHeight, final double compact) {
        return (CompletableFuture<Map<double[], Color>>)getScaledImage(path, resizedWidth, resizedHeight).thenCompose(image -> renderImage(image, resizedWidth, resizedHeight, compact));
    }
    
    public static CompletableFuture<Map<double[], Color>> renderImage(final BufferedImage image, final int resizedWidth, final int resizedHeight, final double compact) {
        return (CompletableFuture<Map<double[], Color>>)CompletableFuture.supplyAsync(() -> {
            if (image == null) {
                return null;
            }
            final int width = image.getWidth();
            final int height = image.getHeight();
            final double centerX = width / 2.0;
            final double centerY = height / 2.0;
            final Map<double[], Color> rendered = (Map<double[], Color>)new HashMap();
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    final int pixel = image.getRGB(x, y);
                    if (pixel >> 24 != 0) {
                        final java.awt.Color color = new java.awt.Color(pixel);
                        final int r = color.getRed();
                        final int g = color.getGreen();
                        final int b = color.getBlue();
                        final double[] coords = { (x - centerX) * compact, (y - centerY) * compact };
                        final Color bukkitColor = Color.fromRGB(r, g, b);
                        rendered.put((Object)coords, (Object)bukkitColor);
                    }
                }
            }
            return rendered;
        });
    }
    
    public static BooleanSupplier displayRenderedImage(final Map<double[], Color> render, final Callable<Location> location, final int repeat, final int quality, final int speed, final float size) {
        return (BooleanSupplier)new BooleanSupplier() {
            int times = repeat;
            boolean done = false;
            
            public boolean getAsBoolean() {
                if (this.done) {
                    return false;
                }
                try {
                    Particles.displayRenderedImage(render, (Location)location.call(), quality, speed, size);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
                if (this.times-- <= 0) {
                    this.done = true;
                    return false;
                }
                return true;
            }
        };
    }
    
    public static BukkitTask displayRenderedImage(final Plugin plugin, final Map<double[], Color> render, final Callable<Location> location, final int repeat, final long period, final int quality, final int speed, final float size) {
        final BooleanSupplier displayRenderedImage = displayRenderedImage(render, location, repeat, quality, speed, size);
        return new BukkitRunnable() {
            public void run() {
                if (!displayRenderedImage.getAsBoolean()) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, period);
    }
    
    public static void displayRenderedImage(final Map<double[], Color> render, final Location location, final int quality, final int speed, final float size) {
        final World world = location.getWorld();
        final double rotation = location.getYaw();
        BlockFace facing;
        if (rotation >= 135.0 || rotation < -135.0) {
            facing = BlockFace.NORTH;
        }
        else if (rotation >= -135.0 && rotation < -45.0) {
            facing = BlockFace.EAST;
        }
        else if (rotation >= -45.0 && rotation < 45.0) {
            facing = BlockFace.SOUTH;
        }
        else {
            if (rotation < 45.0 || rotation >= 135.0) {
                throw new IllegalArgumentException("Unknown rotation yaw: " + rotation);
            }
            facing = BlockFace.WEST;
        }
        for (final Map.Entry<double[], Color> pixel : render.entrySet()) {
            final Particle.DustOptions data = new Particle.DustOptions((Color)pixel.getValue(), size);
            final double[] pixelLoc = (double[])pixel.getKey();
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            switch (facing) {
                case NORTH: {
                    x = location.getX() - pixelLoc[0];
                    y = location.getY() - pixelLoc[1];
                    z = location.getZ();
                    break;
                }
                case EAST: {
                    x = location.getX();
                    y = location.getY() - pixelLoc[0];
                    z = location.getZ() - pixelLoc[1];
                    break;
                }
                case SOUTH: {
                    x = location.getX() - pixelLoc[1];
                    y = location.getY() - pixelLoc[0];
                    z = location.getZ();
                    break;
                }
                case WEST: {
                    x = location.getX();
                    y = location.getY() - pixelLoc[1];
                    z = location.getZ() - pixelLoc[0];
                    break;
                }
                default: {
                    throw new AssertionError((Object)("Invalid facing: " + (Object)facing));
                }
            }
            final Location loc = new Location(world, x, y, z);
            world.spawnParticle(XParticle.DUST.get(), loc, quality, 0.0, 0.0, 0.0, (double)speed, (Object)data);
        }
    }
    
    public static void saveImage(final BufferedImage image, final Path path) {
        try {
            ImageIO.write((RenderedImage)image, "png", Files.newOutputStream(path, new OpenOption[] { (OpenOption)StandardOpenOption.CREATE_NEW, (OpenOption)StandardOpenOption.WRITE }));
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    public static CompletableFuture<BufferedImage> stringToImage(final Font font, final java.awt.Color color, final String str) {
        return (CompletableFuture<BufferedImage>)CompletableFuture.supplyAsync(() -> {
            BufferedImage image = new BufferedImage(1, 1, 2);
            Graphics2D graphics = image.createGraphics();
            graphics.setFont(font);
            final FontRenderContext context = graphics.getFontMetrics().getFontRenderContext();
            final Rectangle2D frame = font.getStringBounds(str, context);
            graphics.dispose();
            image = new BufferedImage((int)Math.ceil(frame.getWidth()), (int)Math.ceil(frame.getHeight()), 2);
            graphics = image.createGraphics();
            graphics.setColor(color);
            graphics.setFont(font);
            graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            final FontMetrics metrics = graphics.getFontMetrics();
            graphics.drawString(str, 0, metrics.getAscent());
            graphics.dispose();
            return image;
        });
    }
    
    static {
        R270 = Math.toRadians(270.0);
    }
}
