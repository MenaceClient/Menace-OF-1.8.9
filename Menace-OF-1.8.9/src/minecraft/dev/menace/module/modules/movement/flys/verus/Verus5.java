package dev.menace.module.modules.movement.flys.verus;

import dev.menace.event.events.EventCollide;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventMove;
import dev.menace.module.modules.movement.flys.FlightBase;
import dev.menace.utils.entity.self.PlayerUtils;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Verus5 extends FlightBase {

	private int waitTicks;

	@Override
	public void onEnable() {
		waitTicks = 0;
	}

	@Override
	public void onMove(EventMove event) {
		MC.thePlayer.setSprinting(true);
		if (MC.thePlayer.onGround) {
			PlayerUtils.strafe(0.42f);
			waitTicks++;
			if (waitTicks >= 3) {
				waitTicks = 0;
				MC.thePlayer.triggerAchievement(StatList.jumpStat);
				MC.thePlayer.motionY = 0.0;
				event.y = 0.41999998688698;
	            if (MC.gameSettings.keyBindJump.isKeyDown()) {
	    			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
	    			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
	            	this.launchY += 0.41999998688698;
	            }
			}
		} else {
			PlayerUtils.strafe(0.5f);
		}

	}

	@Override
	public void onCollision(EventCollide event) {	
		if (event.getBlock() instanceof BlockAir && event.getPosY() <= this.launchY) {
			event.setBoundingBox(AxisAlignedBB.fromBounds(event.getPosX(), event.getPosY(), event.getPosZ(), event.getPosX() + 1.0, this.launchY, event.getPosZ() + 1.0));
		}
	}

	@Override
	public void onJump(EventJump event) {
		event.setCancelled(true);
	}

}
