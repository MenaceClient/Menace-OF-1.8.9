package dev.menace.module.modules.movement.speeds.verus;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.module.modules.movement.Speed;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.client.Minecraft;

public class VerusYPort extends SpeedBase {

	Minecraft MC = Menace.instance.MC;

	public VerusYPort() {
	}

	@Override
	public void onMove(EventMove event) {
		if (PlayerUtils.isMoving()) {
			MC.gameSettings.keyBindJump.pressed = false;
			if (MC.thePlayer.onGround) {
				MC.thePlayer.jump();
				MC.thePlayer.motionY = 0.0;
				PlayerUtils.strafe(0.43F);
			}
			PlayerUtils.strafe();
		}
	}
}
