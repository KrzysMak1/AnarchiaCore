package cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.transformer;

import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.PlayerProfiles;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import org.jetbrains.annotations.ApiStatus;
import java.util.List;
import cc.dreamcode.antylogout.libs.com.cryptomorin.xseries.profiles.objects.Profileable;

public final class TransformableProfile implements Profileable
{
    private final Profileable profileable;
    private final TransformationSequence transformers;
    
    @ApiStatus.Internal
    public TransformableProfile(final Profileable profileable, final List<ProfileTransformer> transformers) {
        this.profileable = profileable;
        this.transformers = new TransformationSequence(profileable, (List)transformers);
    }
    
    @Override
    public boolean isReady() {
        return true;
    }
    
    @Override
    public Profileable transform(final ProfileTransformer... transformers) {
        final List<ProfileTransformer> transformersList = (List<ProfileTransformer>)new ArrayList(this.transformers.transformers.length + transformers.length);
        transformersList.addAll((Collection)Arrays.stream((Object[])this.transformers.transformers).map(x -> x.transformer).collect(Collectors.toList()));
        transformersList.addAll((Collection)Arrays.asList((Object[])transformers));
        return new TransformableProfile(this.profileable, transformersList);
    }
    
    @Override
    public GameProfile getProfile() {
        this.transformers.profile = this.profileable.getProfile();
        if (this.transformers.profile == null) {
            return null;
        }
        for (final TransformationSequence.TransformedProfileCache transformer : this.transformers.transformers) {
            transformer.transform();
        }
        return this.transformers.profile;
    }
    
    private static final class TransformationSequence
    {
        private final Profileable profileable;
        @Nullable
        private GameProfile profile;
        private boolean expired;
        private boolean markRestAsCopy;
        private final TransformedProfileCache[] transformers;
        
        private TransformationSequence(final Profileable profileable, final List<ProfileTransformer> transformers) {
            this.profileable = profileable;
            this.transformers = (TransformedProfileCache[])transformers.stream().map(x$0 -> new TransformedProfileCache(x$0)).toArray(TransformedProfileCache[]::new);
        }
        
        private final class TransformedProfileCache
        {
            @Nullable
            private final ProfileTransformer transformer;
            @Nullable
            private GameProfile cacheProfile;
            
            private TransformedProfileCache(final ProfileTransformer transformer) {
                this.transformer = transformer;
            }
            
            private void transform() {
                if (this.cacheProfile != null && this.transformer.canBeCached()) {
                    if (!TransformationSequence.this.expired) {
                        TransformationSequence.this.profile = this.cacheProfile;
                        return;
                    }
                }
                else {
                    TransformationSequence.this.expired = true;
                }
                TransformationSequence.this.profile = (this.cacheProfile = this.transformer.transform(TransformationSequence.this.profileable, TransformationSequence.this.markRestAsCopy ? TransformationSequence.this.profile : PlayerProfiles.clone(TransformationSequence.this.profile)));
                if (!this.transformer.canBeCached()) {
                    TransformationSequence.this.markRestAsCopy = true;
                }
            }
        }
    }
}
