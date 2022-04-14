package dev.menace.packets.client;

import dev.menace.packets.MenacePacket;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketListener;

public class S69MenaceInbound extends MenacePacket {
	
	@Override
	public void handle(PacketListener listener) {
		//null
	}
	
	@Override
	public void readPacketData(PacketDataSerializer data) {
		//null
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) {
		// not needed yet
	}
	
}
