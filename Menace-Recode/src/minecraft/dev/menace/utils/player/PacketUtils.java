package dev.menace.utils.player;

import dev.menace.event.events.EventSendPacket;
import dev.menace.utils.misc.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class PacketUtils {

	private static final Minecraft MC = Minecraft.getMinecraft();

	public static void sendPacket(Packet<?> packetIn) {
		if (MC.getNetHandler() == null || MC.getNetHandler().getNetworkManager() == null) {
			return;
		}
		MC.getNetHandler().getNetworkManager().sendPacket(packetIn);
	}

	public static void sendPacketNoEvent(Packet<?> packetIn) {
		if (MC.getNetHandler() == null || MC.getNetHandler().getNetworkManager() == null) {
			return;
		}

		//Update PacketBalance even if we dont send an event
		if (packetIn instanceof C03PacketPlayer) {
			PacketBalanceUtils.instance.handleNoEvent();
		}

		MC.getNetHandler().getNetworkManager().sendPacketNoEvent(packetIn);
	}

	public static void sendPacketNoEventDelayed(Packet<?> packetIn, long delay) {
		if (MC.getNetHandler() == null || MC.getNetHandler().getNetworkManager() == null) {
			return;
		}

		//Update PacketBalance even if we dont send an event
		if (packetIn instanceof C03PacketPlayer) {
			PacketBalanceUtils.instance.handleNoEvent();
		}

		MC.getNetHandler().getNetworkManager().sendPacketNoEventDelayed(packetIn, delay);
	}
	
	public static void addToSendQueue(Packet<?> packetIn) {
		MC.thePlayer.sendQueue.addToSendQueue(packetIn);
	}
}
