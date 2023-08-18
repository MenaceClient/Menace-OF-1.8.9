package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventRender3D;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class ESPModule extends BaseModule {
    public ESPModule() {
        super("ESP", "Allows you to see players through walls", Category.RENDER, 0);
    }

    @EventTarget
    public void on3D(EventRender3D event) {
        mc.theWorld.playerEntities.forEach(p -> {
            if (p != mc.thePlayer) {
                RenderUtils.drawEntityBox(p, Color.red, true);
            }
        });
    }

}
