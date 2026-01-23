package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import com.mojang.authlib.properties.PropertyMap;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;

public interface ProfileTransformer
{
    @NotNull
    GameProfile transform(@NotNull final Profileable p0, @NotNull final GameProfile p1);
    
    @ApiStatus.Internal
    boolean canBeCached();
    
    @NotNull
    default ProfileTransformer stackable() {
        return RemoveMetadata.INSTANCE;
    }
    
    @NotNull
    default ProfileTransformer nonStackable() {
        return MakeNotStackable.INSTANCE;
    }
    
    @NotNull
    default ProfileTransformer removeMetadata() {
        return RemoveMetadata.INSTANCE;
    }
    
    @NotNull
    default ProfileTransformer includeOriginalValue() {
        return IncludeOriginalValue.INSTANCE;
    }
    
    public static final class IncludeOriginalValue implements ProfileTransformer
    {
        private static final IncludeOriginalValue INSTANCE;
        public static final String PROPERTY_NAME = "OriginalValue";
        
        @Nullable
        public static String getOriginalValue(@NotNull final GameProfile profile) {
            final PropertyMap props = profile.getProperties();
            final Collection<Property> prop = (Collection<Property>)props.get((Object)"OriginalValue");
            if (prop.isEmpty()) {
                return null;
            }
            final Property first = (Property)Iterables.getFirst((Iterable)prop, (Object)null);
            return PlayerProfiles.getPropertyValue(first);
        }
        
        @Override
        public GameProfile transform(final Profileable profileable, final GameProfile profile) {
            final String originalValue = profileable.getProfileValue();
            profile.getProperties().put((Object)"OriginalValue", (Object)new Property("OriginalValue", originalValue));
            return profile;
        }
        
        @Override
        public boolean canBeCached() {
            return true;
        }
        
        static {
            INSTANCE = new IncludeOriginalValue();
        }
    }
    
    public static final class MakeNotStackable implements ProfileTransformer
    {
        private static final MakeNotStackable INSTANCE;
        private static final String PROPERTY_NAME = "XSeriesSeed";
        private static final AtomicLong NEXT_ID;
        
        @Override
        public GameProfile transform(final Profileable profileable, final GameProfile profile) {
            final String value = System.currentTimeMillis() + "-" + MakeNotStackable.NEXT_ID.getAndIncrement();
            profile.getProperties().put((Object)"XSeriesSeed", (Object)new Property("XSeriesSeed", value));
            return profile;
        }
        
        @Override
        public boolean canBeCached() {
            return false;
        }
        
        static {
            INSTANCE = new MakeNotStackable();
            NEXT_ID = new AtomicLong();
        }
    }
    
    public static final class RemoveMetadata implements ProfileTransformer
    {
        private static final RemoveMetadata INSTANCE;
        
        @Override
        public GameProfile transform(final Profileable profileable, final GameProfile profile) {
            PlayerProfiles.removeTimestamp(profile);
            final Map<String, Collection<Property>> props = (Map<String, Collection<Property>>)profile.getProperties().asMap();
            props.remove((Object)"XSeries");
            props.remove((Object)"OriginalValue");
            return profile;
        }
        
        @Override
        public boolean canBeCached() {
            return true;
        }
        
        static {
            INSTANCE = new RemoveMetadata();
        }
    }
}
