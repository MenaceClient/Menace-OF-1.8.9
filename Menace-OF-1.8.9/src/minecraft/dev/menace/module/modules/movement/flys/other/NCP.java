package dev.menace.module.modules.movement.flys.other;

import dev.menace.event.events.EventCollide;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

public class NCP extends FlightBase {

	boolean clipped;

	@Override
	public void onEnable() {
		MC.timer.timerSpeed = 0.4f;
		clipped = false;
	}

	@Override
	public void onDisable() {
		MC.timer.timerSpeed = 1f;
	}

	@Override
	public void onUpdate() {
		MC.thePlayer.onGround = true;

		if(!clipped) {
			if(MC.thePlayer.isCollided) {
				MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY - 1, MC.thePlayer.posZ);
				clipped = true;
				MC.timer.timerSpeed = 1f;
			} else {
				MC.thePlayer.motionZ = 0;
				MC.thePlayer.motionX = 0;
				MC.thePlayer.motionY = 0;
			}
		}

		if(clipped) {

			MC.thePlayer.motionY = 0;
			
			if (PlayerUtils.isMoving()) {
				PlayerUtils.strafe();
			} else {
				MC.thePlayer.motionX = 0;
				MC.thePlayer.motionZ = 0;
			}

		}
	}

}
