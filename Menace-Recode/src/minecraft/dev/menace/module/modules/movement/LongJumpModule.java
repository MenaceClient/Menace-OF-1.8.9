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

	public boolean damage = false;
	boolean sent = false;
	int count = 0;
	boolean disable;
	
	public LongJumpModule() {
		super("LongJump", Category.MOVEMENT, 0);
	}

	@Override
	public void onEnable() {
		damage = false;
		sent = false;
		count = 0;
		disable = false;
		super.onEnable();
	}

	@EventTarget
	public void onMove(EventMove event) {
		if (!sent) {
			event.cancel();
		}
	}

	@EventTarget
	public void onPre(EventPreMotion event) {

		if (count >= 5 && !damage && !sent) {
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0, 0.5f, 0));
			PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ), 1, new net.minecraft.item.ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.05, mc.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.41999998688697815, mc.thePlayer.posZ, true));
			sent = true;
		}

		if (mc.thePlayer.hurtTime != 0) {
			damage = true;
		}

		if (!sent) {
			return;
		}
		if (!damage) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.jump();
				MovementUtils.strafe(1.5F);
			}
		} else {
			if (!disable) {
				mc.thePlayer.motionY = 0.42;
				disable = true;
			}
			MovementUtils.strafe(3.7F);
		}

		if (mc.thePlayer.onGround && disable) {
			this.toggle();
		}
	}

	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof C03PacketPlayer && !damage && !sent) {
			event.cancel();
			count++;
		}
	}

}
