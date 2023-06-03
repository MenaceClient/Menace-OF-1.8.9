package dev.menace.module.modules.movement.speed.other;

import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class ExperimentalSpeed extends SpeedBase {

    @Override
    public void onMove(EventMove event) {

        if (!MovementUtils.shouldMove()) return;
        mc.gameSettings.keyBindJump.pressed = false;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            mc.thePlayer.motionY = 0.0;
            event.setY(0.1);
            MovementUtils.strafe(0.5f);
        }
        MovementUtils.strafe();

        super.onMove(event);
    }
}
