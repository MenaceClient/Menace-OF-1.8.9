package dev.menace.module.modules.render;

import dev.menace.event.EventTarget;
import dev.menace.event.events.Event3D;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.render.RenderUtils;

import java.awt.*;

public class ESPModule extends BaseModule {
    public ESPModule() {
        super("ESP", Category.RENDER, 0);
    }

    @EventTarget
    public void on3D(Event3D event) {
        MC.theWorld.playerEntities.forEach(p -> {
            if (p != MC.thePlayer) {
                RenderUtils.drawEntityBox(p, Color.red, true);
            }
        });
    }

}
