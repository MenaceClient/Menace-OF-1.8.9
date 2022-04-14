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

public class VerusLowHop extends SpeedBase {

	Minecraft MC = Menace.instance.MC;

	public VerusLowHop() {
	}

	@Override
	public void onMove(EventMove event) {
		if (PlayerUtils.isMoving()) {
			MC.gameSettings.keyBindJump.pressed = false;
			if (MC.thePlayer.onGround) {
				MC.thePlayer.jump();
				MC.thePlayer.motionY = 0.0;
				PlayerUtils.strafe(0.61F);
				event.y = 0.41999998688698;
			}
			PlayerUtils.strafe();
		}
	}
}
