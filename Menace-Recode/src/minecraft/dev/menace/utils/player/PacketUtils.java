package dev.menace.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class PacketUtils {

	private static final Minecraft MC = Minecraft.getMinecraft();

	public static void sendPacket(Packet packetIn) {
		MC.getNetHandler().getNetworkManager().sendPacket(packetIn);
	}

	public static void sendPacketNoEvent(Packet packetIn) {
		MC.getNetHandler().getNetworkManager().sendPacketNoEvent(packetIn);
	}

	public static void sendPacketNoEventDelayed(Packet packetIn, long delay) {
		MC.getNetHandler().getNetworkManager().sendPacketNoEventDelayed(packetIn, delay);
	}
	
	public static void addToSendQueue(Packet packetIn) {
		MC.thePlayer.sendQueue.addToSendQueue(packetIn);
	}
}
