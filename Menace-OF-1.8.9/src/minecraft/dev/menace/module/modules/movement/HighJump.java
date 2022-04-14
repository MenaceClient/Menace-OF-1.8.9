package dev.menace.module.modules.movement;

import java.util.ArrayList;

import dev.menace.Menace;
import dev.menace.event.EventTarget;
import dev.menace.event.events.EventJump;
import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.event.events.EventUpdate;
import dev.menace.module.Category;
import dev.menace.module.Module;
import dev.menace.module.settings.DoubleSetting;
import dev.menace.module.settings.StringSetting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;

public class HighJump extends Module {

	//Settings
	StringSetting mode;
	DoubleSetting height;
	
	int packetcount = 0;
	boolean damage = false;
	
	public HighJump() {
		super("HighJump", 0, Category.MOVEMENT);
	}
	
	@Override
	public void setup() {
		ArrayList<String> options = new ArrayList<>();
		options.add("Vanilla");
		options.add("Verus");
		mode = new StringSetting("Mode", this, "Vanilla", options);
		height = new DoubleSetting("Jump Height", this, 5, 1, 20);
		this.rSetting(mode);
		this.rSetting(height);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		packetcount = 0;
		damage = false;
		
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if (packetcount < 3 && (event.getPacket() instanceof C03PacketPlayer ||
				event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition ||
				event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook ||
				event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
			event.setCancelled(true);
			packetcount++;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (packetcount == 3 && mode.getString().equalsIgnoreCase("Verus") && !damage) {
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(MC.thePlayer.posX, MC.thePlayer.posY - 1.5, MC.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(MC.theWorld, new BlockPos(-1, -1, -1))), 0f, 0.94f, 0f));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY + 3.05, MC.thePlayer.posZ, false));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY, MC.thePlayer.posZ, false));
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(MC.thePlayer.posX, MC.thePlayer.posY+0.41999998688697815, MC.thePlayer.posZ, true));
			damage = true;
		}
	}
	
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if (mode.getString().equalsIgnoreCase("Verus")
				&& event.getPacket() instanceof S12PacketEntityVelocity) {
			
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
			S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
			
			if (packet.motionY <= 0) return;
			
			packet.motionY *= height.getValue();
			this.toggle();
		
		}
	}
	
	
	@EventTarget
	public void onJump(EventJump event) {
		if (mode.getString().equalsIgnoreCase("Vanilla")) {
			event.setUpwardsMotion(height.getValue());
		}
	}

}
