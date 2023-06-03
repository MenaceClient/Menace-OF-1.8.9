package dev.menace.module.modules.movement.speed.ncp;

import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;

public class NCPWTFSpeed extends SpeedBase {

    @Override
    public void onUpdate() {

        if (!MovementUtils.shouldMove()) {
            return;
        }

        if (mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 2f;
            mc.thePlayer.jump();
        } else {
            mc.timer.timerSpeed = 0.98f;
            mc.thePlayer.motionY = -0.427;
        }

        super.onUpdate();
    }
}
