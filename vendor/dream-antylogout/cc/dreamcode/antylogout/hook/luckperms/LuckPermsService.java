package cc.dreamcode.antylogout.hook.luckperms;

import lombok.Generated;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.SuffixNode;
import lombok.NonNull;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.Bukkit;
import net.luckperms.api.LuckPerms;

public class LuckPermsService
{
    private final LuckPerms luckPerms;
    
    public LuckPermsService() {
        final RegisteredServiceProvider<LuckPerms> provider = (RegisteredServiceProvider<LuckPerms>)Bukkit.getServicesManager().getRegistration((Class)LuckPerms.class);
        this.luckPerms = (LuckPerms)provider.getProvider();
    }
    
    public User getUser(final Player player) {
        return this.luckPerms.getPlayerAdapter((Class)Player.class).getUser((Object)player);
    }
    
    public void saveUser(final User user) {
        this.luckPerms.getUserManager().saveUser(user);
    }
    
    public SuffixNode getSuffix(@NonNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        final User user = this.getUser(player);
        return (SuffixNode)user.getCachedData().getMetaData().querySuffix().node();
    }
    
    public boolean hasSuffix(@NonNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        return this.getSuffix(player) != null;
    }
    
    public void removeSuffix(@NonNull final Player player) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (!this.hasSuffix(player)) {
            return;
        }
        final User user = this.getUser(player);
        user.data().remove((Node)this.getSuffix(player));
        this.saveUser(user);
    }
    
    public void setSuffix(@NonNull final Player player, final String suffix) {
        if (player == null) {
            throw new NullPointerException("player is marked non-null but is null");
        }
        if (this.hasSuffix(player)) {
            this.removeSuffix(player);
        }
        if (suffix.isEmpty()) {
            return;
        }
        final User user = this.getUser(player);
        final SuffixNode suffixNode = (SuffixNode)SuffixNode.builder(suffix, 99).build();
        user.data().add((Node)suffixNode);
        this.saveUser(user);
    }
    
    @Generated
    public LuckPerms getLuckPerms() {
        return this.luckPerms;
    }
}
