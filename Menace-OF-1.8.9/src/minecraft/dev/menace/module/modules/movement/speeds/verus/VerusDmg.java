package dev.menace.module.modules.movement.speeds.verus;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.module.modules.movement.speeds.SpeedBase;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;

public class VerusDmg extends SpeedBase {

	private MSTimer timer = new MSTimer();
	private boolean safe;
	private boolean expectDamage;
	
	@Override
	public void onEnable() {
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.35, MC.thePlayer.posZ, false));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
		MC.thePlayer.motionX = 0.0;
		MC.thePlayer.motionY = 0.0;
		MC.thePlayer.motionZ = 0.0;
		MC.timer.timerSpeed = 0.5f;
		expectDamage = true;
		timer.reset();
	}
	
	@Override
	public void onUpdate() {
		
		if (timer.hasTimePassed(300)) {
            MC.timer.timerSpeed = 1f;
        }
		
        if (timer.hasTimePassed(1500)) {
        	safe = false;
        }
        
        if (timer.hasTimePassed(3000)) {
        	MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.35, MC.thePlayer.posZ, false));
    		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
    		MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
    		MC.thePlayer.motionX = 0.0;
    		MC.thePlayer.motionY = 0.0;
    		MC.thePlayer.motionZ = 0.0;
    		MC.timer.timerSpeed = 0.5f;
    		expectDamage = true;
    		timer.reset();
        }
        
        if (!PlayerUtils.isMoving()) {
        	safe = false;
        }

        if (timer.hasTimePassed(100) && safe) {
            PlayerUtils.strafe(2f);
            if (MC.thePlayer.isCollidedHorizontally) safe = false;
        }
	}
	
	@Override
	public void onJump(EventJump event) {
		event.setCancelled(true);
	}
	
	@Override
	public void onRecievePacket(EventReceivePacket event) {
		if (expectDamage && event.getPacket() instanceof S12PacketEntityVelocity) {
			safe = true;
		}
	}
	
}
