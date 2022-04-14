package dev.menace.module.modules.movement.flys.vanilla;

import dev.menace.module.modules.movement.flys.FlightBase;

public class Jetpack extends FlightBase {

	@Override
	public void onUpdate() {
		if (MC.gameSettings.keyBindJump.isKeyDown()) {
			MC.thePlayer.motionY = 1;
		}
	}
	
}
