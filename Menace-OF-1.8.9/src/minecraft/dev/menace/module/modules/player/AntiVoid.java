package dev.menace.module.modules.player;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.utils.entity.self.PlayerUtils;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AntiVoid extends Module {

	private boolean waitingForDamage;
	private boolean sent;
	
	public AntiVoid() {
		super("AntiVoid", 0, Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		waitingForDamage = false;
		sent = false;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (waitingForDamage) {
			MC.thePlayer.motionY = 0;
			if (!sent) {
				MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.25, MC.thePlayer.posZ, false));
				MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
				MC.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, true));
				sent = true;
			}
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (PlayerUtils.isInVoid() && !waitingForDamage && event.getPacket() instanceof C03PacketPlayer && !(MC.thePlayer.fallDistance >= 3) && MC.thePlayer.fallDistance < 20) {
			//ChatUtils.message("in void");
			((C03PacketPlayer) event.getPacket()).setOnGround(true);
			waitingForDamage = true;
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S12PacketEntityVelocity && waitingForDamage) {
			
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			packet.motionY = 5;
			waitingForDamage = false;
			sent = false;
		}
	}

}
