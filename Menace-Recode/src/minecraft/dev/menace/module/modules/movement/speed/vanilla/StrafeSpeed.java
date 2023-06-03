package dev.menace.module.modules.movement.speed.vanilla;

import dev.menace.event.events.EventPreMotion;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class StrafeSpeed extends SpeedBase {

    @Override
    public void onPreMotion(EventPreMotion event) {
        if (!MovementUtils.shouldMove()) {
            return;
        }

        mc.gameSettings.keyBindJump.pressed = false;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
        }

        MovementUtils.strafe();

        super.onPreMotion(event);
    }
}
