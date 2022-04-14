package dev.menace.module.modules.misc.disablers.verus;

import java.util.LinkedList;

import dev.menace.event.events.EventReceivePacket;
import dev.menace.event.events.EventSendPacket;
import dev.menace.module.modules.misc.disablers.DisablerBase;
import dev.menace.utils.misc.ChatUtils;
import dev.menace.utils.misc.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class VerusNew extends DisablerBase {

	private LinkedList<Packet<INetHandlerPlayServer>> packetBuffer = new LinkedList<Packet<INetHandlerPlayServer>>();
	private MSTimer fakeLagDelay = new MSTimer();
	private MSTimer fakeLagDuration = new MSTimer();
	private boolean verus2Stat = false;

	@Override
	public void onEnable() {
		packetBuffer.clear();
		fakeLagDelay.reset();
		fakeLagDuration.reset();
		verus2Stat = false;
	}
	
	@Override
	public void onDisable() {
		while (!packetBuffer.isEmpty()) {
			MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
		}
	}

	@Override
	public void onUpdate() {
		if(fakeLagDelay.hasTimePassed(490L)) {
			fakeLagDelay.reset();
			//ChatUtils.message("Packet Removed");
			if (!packetBuffer.isEmpty()) {
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
			}
		}
	}

	@Override
	public void onSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C0FPacketConfirmTransaction) {
			C0FPacketConfirmTransaction packet = (C0FPacketConfirmTransaction) event.getPacket();
			packetBuffer.add(packet);
			event.setCancelled(true);
			//ChatUtils.message("Packet: " + String.valueOf(packetBuffer.size()));
			if(packetBuffer.size() > 300) {
				if(!verus2Stat) {
					verus2Stat = true;
					ChatUtils.message("Verus disabled!");
				}
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(packetBuffer.poll());
			}
		} else if(event.getPacket() instanceof C03PacketPlayer) {
			C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
			if(MC.thePlayer.ticksExisted % 40 == 0) {

				packet.setY(packet.getPositionY() - 11.4514);
				packet.setOnGround(false);
			}
		}

		if (MC.thePlayer != null && MC.thePlayer.ticksExisted <= 7) {
			fakeLagDelay.reset();
			packetBuffer.clear();
		}
	}

	@Override
	public void onReceivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook && verus2Stat) {
			S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
			double x = packet.getX() - MC.thePlayer.posX;
			double y = packet.getY() - MC.thePlayer.posY;
			double z = packet.getZ() - MC.thePlayer.posZ;
			double diff = Math.sqrt(x * x + y * y + z * z);
			if (diff <= 8) {
				event.setCancelled(true);
				MC.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), true));
			}
		}
	}
	
	 boolean isInventory(short action) {
	        return action > 0 && action < 100;
	    }
}
