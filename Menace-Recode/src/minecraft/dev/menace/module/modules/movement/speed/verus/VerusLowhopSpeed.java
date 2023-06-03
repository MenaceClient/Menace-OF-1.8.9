package dev.menace.module.modules.movement.speed.verus;

import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class VerusLowhopSpeed extends SpeedBase {

    @Override
    public void onMove(EventMove event) {
        if (!MovementUtils.shouldMove()) return;

        mc.gameSettings.keyBindJump.pressed = false;
        if (mc.thePlayer.onGround) {
            mc.thePlayer.jump();
            mc.thePlayer.motionY = 0.0;
            MovementUtils.strafe(0.61F);
            event.setY(0.41999998688698);
        }
        MovementUtils.strafe();

        super.onMove(event);
    }
}
