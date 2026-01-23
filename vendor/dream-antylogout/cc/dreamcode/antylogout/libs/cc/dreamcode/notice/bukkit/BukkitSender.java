package cc.dreamcode.antylogout.libs.cc.dreamcode.notice.bukkit;

import lombok.NonNull;
import java.util.Map;
import org.bukkit.command.CommandSender;
import cc.dreamcode.antylogout.libs.cc.dreamcode.notice.NoticeSender;

public interface BukkitSender extends NoticeSender<CommandSender>
{
    void sendAll();
    
    void sendAll(@NonNull final Map<String, Object> mapReplacer);
    
    void sendAll(@NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders);
    
    void sendBroadcast();
    
    void sendBroadcast(@NonNull final Map<String, Object> mapReplacer);
    
    void sendBroadcast(@NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders);
    
    void sendPermitted(@NonNull final String permission);
    
    void sendPermitted(@NonNull final String permission, @NonNull final Map<String, Object> mapReplacer);
    
    void sendPermitted(@NonNull final String permission, @NonNull final Map<String, Object> mapReplacer, final boolean colorizePlaceholders);
}
