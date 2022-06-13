package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.module.modules.movement.flight.FlightBase;

public class MotionFly extends FlightBase {

	@Override
	public void onUpdate() {
		MC.thePlayer.motionY = 0;
		
		if (MC.gameSettings.keyBindSneak.isKeyDown()) {
			MC.thePlayer.motionY = -0.1;
		} else if (MC.gameSettings.keyBindJump.isKeyDown()) {
			MC.thePlayer.motionY = 0.1;
		}
	}
	
}
