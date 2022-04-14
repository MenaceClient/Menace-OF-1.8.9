package dev.menace.module.modules.movement.speeds.verus;

import org.lwjgl.input.Keyboard;

import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;

public class Verus extends SpeedBase {

	@Override
	public void onDisable() {
		MC.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(MC.gameSettings.keyBindJump.getKeyCode());
	}
	
	@Override
	public void onMove(EventMove event) {
		if (PlayerUtils.isMoving()) {
			MC.gameSettings.keyBindJump.pressed = true;
			if (MC.thePlayer.onGround) {
				MC.thePlayer.jump();
				PlayerUtils.strafe(0.69F);
			}
			PlayerUtils.strafe();
		} else {
			MC.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(MC.gameSettings.keyBindJump.getKeyCode());
		}
	}
	
}
