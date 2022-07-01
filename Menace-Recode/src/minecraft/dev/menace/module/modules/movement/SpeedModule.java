package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.player.MovementUtils;

public class SpeedModule extends BaseModule {
    public SpeedModule() {
        super("Speed", Category.MOVEMENT, 0);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!MovementUtils.shouldMove()) return;
        if (MC.thePlayer.onGround) {
            MC.thePlayer.jump();
            MovementUtils.strafe(0.7f);
        }
        MovementUtils.strafe();
    }

}
