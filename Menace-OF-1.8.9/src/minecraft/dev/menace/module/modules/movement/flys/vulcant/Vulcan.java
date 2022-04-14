package dev.menace.module.modules.movement.flys.vulcant;

import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Vulcan extends FlightBase {

	private int ticks, hypixelTicks;
	private boolean hypixelStart;
	
	@Override
	public void onEnable() {
		hypixelStart = false;
		hypixelTicks = 0;
		ticks = 0;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
        MC.thePlayer.motionY = -0.09800000190735147;
        PlayerUtils.stop();
        PlayerUtils.strafe(0.1F);
	}
	
	@Override
	public void onPreMotion(EventPreMotionUpdate event) {
		
		ticks++;
        hypixelTicks++;
        MC.thePlayer.posY = this.launchY;
        if (ticks == 1) {
            event.y = event.y - 0.1;
        }

        if (hypixelTicks == -9) {
            MC.thePlayer.motionY = 2.9 / 3f;
            PlayerUtils.strafe(3.4F);
        }
        
        if (PlayerUtils.getSpeed() > 1 && hypixelTicks > -6) PlayerUtils.strafe(0.34F);

        if (hypixelTicks == -6) {
        	PlayerUtils.strafe(0.32F);
            MC.thePlayer.motionY = 0.24813599859094576 - 0.313605186719;
        }

        if (hypixelTicks > -6 && MC.thePlayer.ticksExisted % 2 == 0) {
            MC.thePlayer.motionY = -0.09800000190735147;
        }

        if (hypixelStart && hypixelTicks % 35 == 0) MC.thePlayer.motionY = 2.9;

        if (!PlayerUtils.isMoving()) PlayerUtils.stop();
	}
	
	@Override
	public void onRecievePacket(EventReceivePacket event) {
		if (!(event.getPacket() instanceof S08PacketPlayerPosLook)) return;
		 hypixelTicks = -10;
         hypixelStart = true;
	}
	
	@Override
	public void onJump(EventJump event) {
		event.setCancelled(true);
	}
	
}
