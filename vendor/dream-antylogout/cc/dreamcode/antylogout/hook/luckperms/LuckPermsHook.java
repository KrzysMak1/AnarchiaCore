package cc.dreamcode.antylogout.hook.luckperms;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.bukkit.hook.PluginHook;

@Hook(name = "LuckPerms")
public class LuckPermsHook implements PluginHook
{
    private final ComponentService componentService;
    
    @Override
    public void onInit() {
        this.componentService.registerComponent(LuckPermsService.class);
    }
    
    @Inject
    @Generated
    public LuckPermsHook(final ComponentService componentService) {
        this.componentService = componentService;
    }
}
