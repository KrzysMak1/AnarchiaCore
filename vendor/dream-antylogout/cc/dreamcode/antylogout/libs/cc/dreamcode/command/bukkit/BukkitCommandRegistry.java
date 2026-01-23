package cc.dreamcode.antylogout.libs.cc.dreamcode.command.bukkit;

import org.bukkit.command.CommandMap;
import org.bukkit.command.Command;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandMeta;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandEntry;
import lombok.NonNull;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandRegistry;

public class BukkitCommandRegistry implements CommandRegistry
{
    private final Plugin plugin;
    private final BukkitCommandProvider bukkitCommandProvider;
    private final SimpleCommandMap bukkitCommandMap;
    
    public BukkitCommandRegistry(@NonNull final Plugin plugin, @NonNull final BukkitCommandProvider bukkitCommandProvider) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (bukkitCommandProvider == null) {
            throw new NullPointerException("bukkitCommandProvider is marked non-null but is null");
        }
        this.plugin = plugin;
        this.bukkitCommandProvider = bukkitCommandProvider;
        final SimpleCommandMap simpleCommandMap = BukkitCommandReflection.getSimpleCommandMap(plugin.getServer());
        if (simpleCommandMap == null) {
            throw new RuntimeException("SimpleCommandMap not found");
        }
        this.bukkitCommandMap = simpleCommandMap;
    }
    
    @Override
    public void register(@NonNull final CommandEntry commandEntry, @NonNull final CommandMeta commandMeta) {
        if (commandEntry == null) {
            throw new NullPointerException("commandEntry is marked non-null but is null");
        }
        if (commandMeta == null) {
            throw new NullPointerException("commandMeta is marked non-null but is null");
        }
        final BukkitCommandWrapper bukkitCommandWrapper = new BukkitCommandWrapper(this.plugin, commandEntry, this.bukkitCommandProvider);
        this.bukkitCommandMap.register(commandEntry.getName(), this.plugin.getName(), (Command)bukkitCommandWrapper);
    }
    
    @Override
    public void unregister(@NonNull final CommandEntry commandEntry) {
        if (commandEntry == null) {
            throw new NullPointerException("commandEntry is marked non-null but is null");
        }
        this.bukkitCommandMap.getCommand(commandEntry.getName()).unregister((CommandMap)this.bukkitCommandMap);
    }
}
