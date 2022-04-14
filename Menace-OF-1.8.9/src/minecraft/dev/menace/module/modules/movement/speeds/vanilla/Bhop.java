package dev.menace.module.modules.movement.speeds.vanilla;

import org.lwjgl.input.Keyboard;

import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;

public class Bhop extends SpeedBase {

	@Override
	public void onMove(EventMove event) {
		if (!MC.thePlayer.onGround || !PlayerUtils.isMoving()) return;
		MC.gameSettings.keyBindJump.pressed = true;
		//PlayerUtils.strafe(1f);
		MC.thePlayer.motionX *= 1.5;
		MC.thePlayer.motionY *= 1.2;
		MC.thePlayer.motionZ *= 1.5;
	}
	
	@Override
	public void onDisable() {
		MC.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(MC.gameSettings.keyBindJump.getKeyCode());
	}
	
}
