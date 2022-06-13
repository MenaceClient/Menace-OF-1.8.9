package dev.menace.module.modules.movement;

import dev.menace.event.EventTarget;
import dev.menace.event.events.EventMove;
import dev.menace.event.events.EventPreMotion;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.BaseModule;
import dev.menace.module.Category;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.player.MovementUtils;
import dev.menace.utils.player.PacketUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.BlockPos;

public class LongJumpModule extends BaseModule {

	int C03Count = 0;
	public boolean damage = false;
	private boolean C03Sent = false;
	
	public LongJumpModule() {
		super("LongJump", Category.MOVEMENT, 0);
	}
	
	@Override
	public void onEnable() {
		C03Count = 0;
		C03Sent = false;
		damage = false;
		super.onEnable();
	}
	
	@EventTarget
	public void onPre(EventPreMotion event) {
		if (C03Count >= 4 && !damage && !C03Sent) {
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.05, MC.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY+0.41999998688697815, MC.thePlayer.posZ, true));
			MC.thePlayer.motionX = 0.0;
			MC.thePlayer.motionY = 0.0;
			MC.thePlayer.motionZ = 0.0;
			C03Sent = true;
		}
		
		if (MC.thePlayer.hurtTime != 0) {
			damage = true;
		}
		
		if (!damage) {
			return;
		}
		
		if (MC.thePlayer.motionY > .5) {
            MC.thePlayer.motionY = -.4F;
            this.toggle();
            MC.timer.timerSpeed = 1F;
        }
        if (damage) {
            if (MC.thePlayer.onGround) {
                MC.thePlayer.jump();
            } else {
            	MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
            	MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0, 0.94f, 0));
                MC.thePlayer.motionY = .6F;
                MovementUtils.strafe(6);
            }
        }
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		if (!damage) {
			event.cancel();
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if ((event.getPacket() instanceof C03PacketPlayer ||
				event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition ||
				event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook ||
				event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)
				&& !damage && !C03Sent) {
			event.setCancelled(true);
			C03Count++;
		}
	}

}
