package cc.dreamcode.antylogout.barrier.extension;

import lombok.Generated;
import cc.dreamcode.antylogout.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.antylogout.barrier.scheduler.WallBarrierController;
import cc.dreamcode.antylogout.barrier.BarrierService;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentExtension;

public class BarrierExtension implements ComponentExtension
{
    @Override
    public void register(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        componentService.registerComponent(BarrierService.class);
        componentService.registerComponent(WallBarrierController.class);
    }
    
    @Inject
    @Generated
    public BarrierExtension() {
    }
}
