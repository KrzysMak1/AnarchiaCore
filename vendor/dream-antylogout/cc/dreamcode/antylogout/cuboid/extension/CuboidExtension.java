package cc.dreamcode.antylogout.cuboid.extension;

import cc.dreamcode.antylogout.listener.region.CuboidEnteredListener;
import lombok.NonNull;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.antylogout.libs.cc.dreamcode.platform.component.ComponentExtension;

public class CuboidExtension implements ComponentExtension
{
    @Override
    public void register(@NonNull final ComponentService componentService) {
        if (componentService == null) {
            throw new NullPointerException("componentService is marked non-null but is null");
        }
        componentService.registerComponent(CuboidCache.class);
        componentService.registerComponent(CuboidEventController.class);
        componentService.registerComponent(CuboidEnteredListener.class);
    }
}
