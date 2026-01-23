package cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.countdown;

import lombok.Generated;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.List;
import java.time.Instant;
import java.time.Duration;
import java.util.Optional;
import lombok.NonNull;
import java.util.UUID;
import java.util.Map;

public class CountdownCache
{
    private final Map<UUID, Countdown> map;
    
    public boolean isComplete(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        return (boolean)this.map.entrySet().stream().filter(entry -> ((UUID)entry.getKey()).equals((Object)uuid)).map(entry -> ((Countdown)entry.getValue()).isComplete()).findAny().orElse((Object)false);
    }
    
    public Optional<Countdown> getIfPresent(@NonNull final UUID uuid) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        return (Optional<Countdown>)Optional.ofNullable((Object)this.map.get((Object)uuid));
    }
    
    public Countdown put(@NonNull final UUID uuid, @NonNull final Duration duration) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        final Countdown countdown = new Countdown(uuid, Instant.now(), duration);
        this.map.put((Object)uuid, (Object)countdown);
        return countdown;
    }
    
    public Countdown put(@NonNull final UUID uuid, @NonNull final Duration duration, @NonNull final Runnable runnable) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        final Countdown countdown = new Countdown(uuid, Instant.now(), duration, runnable);
        this.map.put((Object)uuid, (Object)countdown);
        return countdown;
    }
    
    public void remove(@NonNull final UUID uuid, final boolean runnableRun) {
        if (uuid == null) {
            throw new NullPointerException("uuid is marked non-null but is null");
        }
        this.getIfPresent(uuid).ifPresent(countdown -> {
            if (runnableRun && countdown.getRunnable() != null) {
                countdown.getRunnable().run();
            }
        });
        this.map.remove((Object)uuid);
    }
    
    public List<UUID> uuidToRemove() {
        return (List<UUID>)this.map.keySet().stream().filter(this::isComplete).collect(Collectors.toList());
    }
    
    @Generated
    public CountdownCache() {
        this.map = (Map<UUID, Countdown>)new WeakHashMap();
    }
}
