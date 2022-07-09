package dev.menace.module.modules.movement.flight.vanilla;

import dev.menace.module.modules.movement.flight.FlightBase;

public class MotionFly extends FlightBase {

	@Override
	public void onUpdate() {
		mc.thePlayer.motionY = 0;
		
		if (mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.thePlayer.motionY = -0.1;
		} else if (mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer.motionY = 0.1;
		}
	}
	
}
