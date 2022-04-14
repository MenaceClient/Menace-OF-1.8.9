package dev.menace.module.modules.movement.flys.other;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPostMotionUpdate;
import dev.menace.event.events.EventPreMotionUpdate;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Funcraft extends FlightBase {
	
	public double moveSpeed;
	
	@Override
	public void onEnable() {
	    if(MC.thePlayer.onGround)
	        MC.thePlayer.jump();
	    moveSpeed = 1.7;
	}
	
	@Override
	public void onDisable() {
		MC.timer.timerSpeed = 1f;
		PlayerUtils.stop();
	}
	
	@Override
	public void onPreMotion(EventPreMotionUpdate event) {
	    //MC.timer.timerSpeed = 1.75F; //(more faster but not smooth)
	    MC.thePlayer.jumpMovementFactor = 0;
	    if(!PlayerUtils.isMoving())
	        moveSpeed = 0.25;
	    if(moveSpeed > 0.25) {
	        moveSpeed -= moveSpeed / 159;
	    }
	    
	    if(PlayerUtils.isMoving())
	         PlayerUtils.strafe((float) moveSpeed);

	        MC.thePlayer.motionY = 0;
	        MC.thePlayer.setPosition(MC.thePlayer.posX, MC.thePlayer.posY - 8E-6, MC.thePlayer.posZ);
	}
	
	@Override
    public void onCollision(EventCollide event) {
        if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
        }
	}    
}
