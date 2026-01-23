package cc.dreamcode.antylogout.barrier;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import java.util.HashMap;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Material;
import java.util.HashSet;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.PostConstruct;
import com.comphenix.protocol.events.PacketListener;
import cc.dreamcode.antylogout.libs.cc.dreamcode.utilities.LocationUtil;
import org.bukkit.entity.Player;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import cc.dreamcode.antylogout.AntyLogoutPlugin;
import org.bukkit.Location;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import cc.dreamcode.antylogout.hook.worldguard.extension.RegionService;
import cc.dreamcode.antylogout.config.PluginConfig;
import com.comphenix.protocol.ProtocolManager;

public class BarrierService
{
    private final ProtocolManager protocolManager;
    private final PluginConfig pluginConfig;
    private final RegionService regionService;
    private final Map<UUID, Set<Location>> activeWalls;
    
    @PostConstruct
    public void registerListener() {
        this.protocolManager.addPacketListener((PacketListener)new PacketAdapter(AntyLogoutPlugin.getInstance(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.BLOCK_DIG }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                final BlockPosition pos = (BlockPosition)event.getPacket().getBlockPositionModifier().read(0);
                final Set<Location> locations = (Set<Location>)BarrierService.this.activeWalls.get((Object)player.getUniqueId());
                if (locations == null) {
                    return;
                }
                if (locations.stream().anyMatch(loc -> LocationUtil.equals(loc, pos.toLocation(event.getPlayer().getWorld())))) {
                    BarrierService.this.sendFakeBlock(player, pos.toLocation(player.getWorld()), BarrierService.this.pluginConfig.barrier.wallMaterial);
                }
            }
        });
    }
    
    public void resetPlayer(final Player player) {
        if (!this.activeWalls.containsKey((Object)player.getUniqueId())) {
            return;
        }
        ((Set)this.activeWalls.get((Object)player.getUniqueId())).forEach(prevLoc -> this.resetBlock(player, prevLoc));
        this.activeWalls.remove((Object)player.getUniqueId());
    }
    
    public void updatePlayer(final Player player) {
        final Set<Location> borderBlocks = this.findNearbyBarrierLocations(player.getLocation());
        if (this.activeWalls.containsKey((Object)player.getUniqueId())) {
            ((Set)this.activeWalls.get((Object)player.getUniqueId())).forEach(loc -> this.resetBlock(player, loc));
            this.activeWalls.remove((Object)player.getUniqueId());
        }
        final Set<Location> newLocSet = (Set<Location>)new HashSet();
        borderBlocks.forEach(newLoc -> {
            if (!newLoc.getBlock().getType().isSolid()) {
                this.sendFakeBlock(player, newLoc, this.pluginConfig.barrier.wallMaterial);
                newLocSet.add((Object)newLoc);
            }
        });
        this.activeWalls.put((Object)player.getUniqueId(), (Object)newLocSet);
    }
    
    public Set<Location> findNearbyBarrierLocations(final Location loc) {
        final Set<Location> barrierLocation = (Set<Location>)new HashSet();
        final int radius = 5;
        final double radiusSquared = radius * radius;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                if (x * x + z * z <= radiusSquared) {
                    for (int y = -3; y <= 2; ++y) {
                        final Location checkLoc = loc.clone().add((double)x, (double)y, (double)z);
                        if (this.isBorderLocation(checkLoc)) {
                            barrierLocation.add((Object)checkLoc);
                        }
                    }
                }
            }
        }
        return barrierLocation;
    }
    
    private boolean isBorderLocation(final Location loc) {
        final int[][] directions = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 } };
        if (!this.regionService.isDisabledRegion(loc)) {
            return false;
        }
        for (final int[] dir : directions) {
            final Location adjacent = loc.clone().add((double)dir[0], (double)dir[1], (double)dir[2]);
            if (!this.regionService.isDisabledRegion(adjacent)) {
                return true;
            }
        }
        return false;
    }
    
    private void sendFakeBlock(final Player player, final Location loc, final Material material) {
        final PacketContainer packet = this.protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        packet.getBlockPositionModifier().write(0, (Object)new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.getBlockData().write(0, (Object)WrappedBlockData.createData(material));
        this.protocolManager.sendServerPacket(player, packet);
    }
    
    private void resetBlock(final Player player, final Location loc) {
        final PacketContainer packet = this.protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        packet.getBlockPositionModifier().write(0, (Object)new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.getBlockData().write(0, (Object)WrappedBlockData.createData(loc.getBlock().getType()));
        this.protocolManager.sendServerPacket(player, packet);
    }
    
    @Inject
    @Generated
    public BarrierService(final PluginConfig pluginConfig, final RegionService regionService) {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.activeWalls = (Map<UUID, Set<Location>>)new HashMap();
        this.pluginConfig = pluginConfig;
        this.regionService = regionService;
    }
}
