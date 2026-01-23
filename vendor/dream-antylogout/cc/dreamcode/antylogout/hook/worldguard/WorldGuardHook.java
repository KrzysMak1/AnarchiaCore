package cc.dreamcode.antylogout.hook.worldguard;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.antylogout.hook.worldguard.extension.RegionExtension;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.PluginHook;

@Hook(name = "WorldGuard")
public class WorldGuardHook implements PluginHook
{
    private final ComponentService componentService;
    
    @Override
    public void onInit() {
        this.componentService.registerExtension(RegionExtension.class);
    }
    
    @Inject
    @Generated
    public WorldGuardHook(final ComponentService componentService) {
        this.componentService = componentService;
    }
}
