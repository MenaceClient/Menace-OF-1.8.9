package dev.menace.module.modules.movement.speeds.vanilla;

import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;

public class GroundStrafeHop extends SpeedBase {

	@Override
	public void onMove(EventMove event) {
        if (PlayerUtils.isMoving()) {
            if (MC.thePlayer.onGround) {
                MC.thePlayer.jump();
                PlayerUtils.strafe();
            }
        } else {
            MC.thePlayer.motionX = 0.0;
            MC.thePlayer.motionZ = 0.0;
        }
    }
	
}
