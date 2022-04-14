package dev.menace.module.modules.movement.speeds.vanilla;

import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;

public class OnGround extends SpeedBase {
	
	@Override
	public void onMove(EventMove event) {
		if (PlayerUtils.isMoving()) {
			PlayerUtils.strafe(0.7f);
		}
	}

}
