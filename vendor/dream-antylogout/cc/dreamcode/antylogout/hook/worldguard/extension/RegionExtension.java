package cc.dreamcode.antylogout.hook.worldguard.extension;

import cc.dreamcode.antylogout.listener.region.WorldGuardRegionChangeListener;
import com.sk89q.worldguard.session.handler.Handler;
import cc.dreamcode.antylogout.hook.worldguard.WGHandler;
import com.sk89q.worldguard.WorldGuard;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentExtension;

public class RegionExtension implements ComponentExtension
{
    @Override
    public void register(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        WorldGuard.getInstance().getPlatform().getSessionManager().registerHandler((Handler.Factory)WGHandler.factory, (Handler.Factory)null);
        componentService.registerComponent(RegionService.class);
        componentService.registerComponent(WorldGuardRegionChangeListener.class);
    }
}
