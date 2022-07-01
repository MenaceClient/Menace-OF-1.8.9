package dev.menace.module.modules.player;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventSlowdown;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class NoSlowModule extends BaseModule {
    public NoSlowModule() {
        super("NoSlow", Category.PLAYER, 0);
    }



    @EventTarget
    public void onSlowdown(EventSlowdown event) {
        event.cancel();
    }
}
