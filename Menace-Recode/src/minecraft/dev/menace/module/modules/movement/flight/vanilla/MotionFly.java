package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.Menace;
import dev.menace.module.modules.movement.flight.FlightBase;
import dev.menace.utils.player.MovementUtils;

public class MotionFly extends FlightBase {

	@Override
	public void onUpdate() {
		mc.thePlayer.motionY = 0;
		
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.thePlayer.motionY = -0.3;
		} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer.motionY = 0.3;
		}

		if(MovementUtils.shouldMove()) {
			MovementUtils.strafe(Menace.instance.moduleManager.flightModule.speed.getValueF() / 5);
		}
	}
	
}
