package cc.dreamcode.antylogout.libs.cc.dreamcode.command.bukkit;

import java.util.List;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandInput;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.command.CommandEntry;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.Command;

public class BukkitCommandWrapper extends Command implements PluginIdentifiableCommand
{
    private final Plugin plugin;
    private final CommandEntry commandEntry;
    private final BukkitCommandProvider bukkitCommandProvider;
    
    public BukkitCommandWrapper(@NonNull final Plugin plugin, @NonNull final CommandEntry commandEntry, @NonNull final BukkitCommandProvider bukkitCommandProvider) {
        super(commandEntry.getName());
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (commandEntry == null) {
            throw new NullPointerException("commandEntry is marked non-null but is null");
        }
        if (bukkitCommandProvider == null) {
            throw new NullPointerException("bukkitCommandProvider is marked non-null but is null");
        }
        this.plugin = plugin;
        this.commandEntry = commandEntry;
        this.bukkitCommandProvider = bukkitCommandProvider;
        this.setDescription(commandEntry.getDescription());
        this.setAliases(Arrays.asList((Object[])commandEntry.getAliases()));
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, false);
        this.bukkitCommandProvider.call(bukkitSender, commandInput);
        return true;
    }
    
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandEntry.getName(), args, false);
        return this.bukkitCommandProvider.getSuggestion(bukkitSender, commandInput);
    }
}
