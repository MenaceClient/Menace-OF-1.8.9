package dev.menace.module.modules.movement.speed.ncp;

import dev.menace.module.modules.movement.speed.SpeedBase;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.world.TimerHandler;

public class NCPWTFSpeed extends SpeedBase {

    @Override
    public void onUpdate() {

        if (!MovementUtils.shouldMove()) {
            return;
        }

        if (mc.thePlayer.onGround) {
            TimerHandler.setTimer(2f, 10);
            mc.thePlayer.jump();
        } else {
            mc.timer.timerSpeed = 0.98f;
            mc.thePlayer.motionY = -0.427;
        }

        super.onUpdate();
    }
}
