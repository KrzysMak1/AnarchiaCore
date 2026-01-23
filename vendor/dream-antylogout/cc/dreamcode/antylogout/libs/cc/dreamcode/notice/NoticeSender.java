package cc.dreamcode.antylogout.libs.cc.dreamcode.notice;

import java.util.Collection;
import java.util.Map;
import lombok.NonNull;

public interface NoticeSender<T>
{
    void send(@NonNull final T target);
    
    void send(@NonNull final T target, @NonNull final Map<String, Object> mapReplacer);
    
    void send(@NonNull final T target, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders);
    
    void send(@NonNull final Collection<T> targets);
    
    void send(@NonNull final Collection<T> targets, @NonNull final Map<String, Object> mapReplacer);
    
    void send(@NonNull final Collection<T> targets, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders);
}
