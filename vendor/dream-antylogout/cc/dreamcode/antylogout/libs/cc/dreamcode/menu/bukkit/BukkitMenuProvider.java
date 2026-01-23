package cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit;

import java.util.function.Consumer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit.listener.BukkitMenuListener;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit.base.BukkitMenuPaginated;
import org.bukkit.event.inventory.InventoryType;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.antylogout.libs.cc.dreamcode.menu.DreamMenuProvider;

public final class BukkitMenuProvider implements DreamMenuProvider<BukkitMenu, InventoryType, BukkitMenuPaginated>
{
    private static BukkitMenuProvider INSTANCE;
    
    public BukkitMenuProvider(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        BukkitMenuProvider.INSTANCE = this;
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents((Listener)new BukkitMenuListener(), plugin);
    }
    
    public static BukkitMenuProvider create(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return new BukkitMenuProvider(plugin);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final String title, final int rows) {
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        return new BukkitMenu(title, rows, 0);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final String title, final int rows, @NonNull final Consumer<BukkitMenu> consumer) {
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = new BukkitMenu(title, rows, 0);
        consumer.accept((Object)bukkitMenu);
        return bukkitMenu;
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final InventoryType type, @NonNull final String title) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        return new BukkitMenu(type, title, 0);
    }
    
    @Override
    public BukkitMenu createDefault(@NonNull final InventoryType type, @NonNull final String title, @NonNull final Consumer<BukkitMenu> consumer) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (title == null) {
            throw new NullPointerException("title is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        final BukkitMenu bukkitMenu = new BukkitMenu(type, title, 0);
        consumer.accept((Object)bukkitMenu);
        return bukkitMenu;
    }
    
    @Override
    public BukkitMenuPaginated createPaginated(@NonNull final BukkitMenu bukkitMenu) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        return new BukkitMenuPaginated(bukkitMenu);
    }
    
    @Override
    public BukkitMenuPaginated createPaginated(@NonNull final BukkitMenu bukkitMenu, @NonNull final Consumer<BukkitMenu> consumer) {
        if (bukkitMenu == null) {
            throw new NullPointerException("bukkitMenu is marked non-null but is null");
        }
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        consumer.accept((Object)bukkitMenu);
        return new BukkitMenuPaginated(bukkitMenu);
    }
    
    public static BukkitMenuProvider getInstance() {
        if (BukkitMenuProvider.INSTANCE == null) {
            throw new RuntimeException("BukkitMenuProvider not found, make sure it is registered");
        }
        return BukkitMenuProvider.INSTANCE;
    }
}
