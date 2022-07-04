package net.minecraft.network.play.client;

import java.io.IOException;

import dev.menace.utils.misc.ChatUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C69PacketMenaceOutbound implements Packet<INetHandlerPlayServer> {

	public C69PacketMenaceOutbound() {
		
	}
	
	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
	}

	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
	}

	@Override
	public void processPacket(INetHandlerPlayServer handler) {
		ChatUtils.message("IN");
	}

}
