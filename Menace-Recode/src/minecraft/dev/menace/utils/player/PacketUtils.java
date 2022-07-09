package dev.menace.utils.player;

import dev.menace.event.events.EventSendPacket;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class PacketUtils {

	private static Minecraft MC = Minecraft.getMinecraft();
	private static NetworkManager netManager = MC.getNetHandler().getNetworkManager();

	public static void sendPacket(Packet packetIn) {
		netManager.sendPacket(packetIn);
	}

	public static void sendPacketNoEvent(Packet packetIn) {
		netManager.sendPacketNoEvent(packetIn);
	}

	public static void sendPacketNoEventDelayed(Packet packetIn, long delay) {
		netManager.sendPacketNoEventDelayed(packetIn, delay);
	}
	
	public static void addToSendQueue(Packet packetIn) {
		MC.thePlayer.sendQueue.addToSendQueue(packetIn);
	}
}
